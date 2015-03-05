module.exports = GameLoop;
var
  Promise = require('bluebird');
  models = require('../../models');

function GameLoop(socket, msg) {
  this.socket = socket;
  this.msg = msg;
  this.round = null;
  this.judge = null;
  this.game = null;
}

GameLoop.prototype.getGameId = function() {
  var self = this;
  return new Promise(function(resolve, reject) {
    models.Game.find({
      where: {
        roomId: room,
        finishTime: null
      } 
    }).then(function(game) {
      self.game = game.id; 
      resolve();
    }); 
  });
}

GameLoop.prototype.getCurrentRoundAndJudge = function() {
  var self = this;
  return new Promise(function(resolve, reject) {
    models.Round.find({
      where: {
        gameId: game,
        state: {
          $not: "over" 
        }
      }
    }).then(function(round) {
      self.round = round.id; 
      self.judge = round.judge;
    }); 
  });
}

/*
  -Create PlayedCard
  -update Hand entry to played=true
  -update PlayerState to "waiting for players"
  -check if all players are waiting
    
  If all players are waiting
  -move to play time up
  else
  -just broadcast that this player played a card

  msg is of the form:
  {
     card: id of card played
  }
 */
GameLoop.prototype.handlePlay = function() {
  var self = this;

  var socket = self.socket;
  var user = socket.userId;
  var room = socket.roomId;

  var msg = self.msg;
  var card = msg.card;

  self.getGameId()
  .then(self.getCurrentRoundAndJudge)
  .then(createPlayedCard)
  .then(updateHandToPlayed)
  .then(updatePlayerStateToWaiting)
  .then(updateGameState)
  .then(broadcastResponse)
  .catch(function(errors) {
    socket.emit('error', errors);
    reject(errors)
  });

  
  function createPlayedCard() {
    return new Promise(function(resolve, reject) {
      models.PlayedCard.create({
        userId: user,
        cardId: card,
        roundId: round
      }).then(function(playedCard) {
        resolve();
      })
    });
  }

  function updateHandToPlayed() {
    return new Promise(function(resolve, reject) {
      var values = {
        played: true 
      };

      models.Hand.find({
        where: {
          cardId: card,
          gameId: game,
          userId: user,
          played: false
        } 
      }).then(function(hand) {
        models.Hand.update(values, {
          where: {id: hand}
        }).then(function(count, obj) {
          resolve();
        });
      });
    });
  }

  function updatePlayerStateToReady() {
    return new Promise(function(resolve, reject) {
      var values = {
        state: "waiting for players"
      };

      models.PlayerState.update(values, {
        where: {
          gameId: game,
          userId: user
        } 
      }).then(function(count, obj) {
        resolve(); 
      });
    });
  }

  function updateGameState() {
    var response = {};
    return new Promise(function(resolve, reject) {
      models.PlayerState.findAll({
        where: {gameId: socket.roomId}
      })
      .then(checkAllPlayersForPlay)
      .then(adjustGameState)
      .then(function(responseKey, response) {
        resolve(responseKey, response);
      });
      
      function checkAllPlayersForPlay(playerStates) {
        var allPlayed = true;
        return new Promise(function(resolve, reject) {
          for (var i = 0; i < playerStates.length; i++) {
            if (playerStates[i].state !== "waiting for players") {
              resolve(false);
            }
          }  
        });
      }

      function adjustGameState(allPlayed) {
        return new Promise(function(resolve, reject) {
          if (allPlayed) {
            updateRoundStateToJudge()      
            .then(updatePlayersPlayerStatesToJudge)
            .then(updateJudgesPlayerStatetoJudge)
            .then(getPlayedCardsForRound)
            .then(function(responseKey, response) {
              resolve(responseKey, response);
            });
          } else {
            var responseKey = 'user has played';
            var response = {user: user};
            resolve(responseKey, response);
          }
        });
      }

      function updateRoundStateToJudge() {
        return new Promise(function(resolve, reject) {
          var values = {
            state: "waiting for judge" 
          };

          models.Round.update(values, {
            where: {
              id: round
            } 
          }).then(function(count, obj) {
            resolve();
          });
        });
      }

      function updatePlayersPlayerStatesToJudge() {
        return new Promise(function(resolve, reject) {
          var values = {
            state: "waiting for judge" 
          }; 

          models.PlayerState.update(values, {
            where: {
              id: {
                $not: judge              
              },
              gameId: game
            }
          }).then(function(count, obj) {
            resolve();
          });
        }); 
      }

      function updateJudgesPlayerStateToJudge() {
        return new Promise(function(resolve, reject) {
          var values = {
            state: "judging" 
          }; 

          models.PlayerState.update(values, {
            where: {
              id: judge
            } 
          }).then(function(count, obj) {
            resolve();
          });
        });
      }

      function getPlayedCardsForRound() {
        return new Promise(function(resolve, reject) {
          models.PlayedCard.findAll({
            where: {
              roundId: round 
            } 
          }).then(function(playedCards) {
            var responseKey = 'waiting for judge';
            var response = {playedCards: playedCards};
            resolve(responseKey, response);
          });
        });
      }
    });
  }

  function broadcastResponse(responseKey, response) {
    socket.broadcast.to(socket.roomId).emit(responseKey, response);
  }
}


/*
if game not over
  - update last Round with winner info and state ="over"
  - update all PlayerStates so state="round review"
  broadcast "round review" state with winner

else there is a game winner

  - update last Round with winner info and state ="over"
  - update users' PlayerStates so state="game review"
  - update Game finnishTime
  broadcast "game review" state with winner and stats

  msg is of the form:
  {
     winner: id of winning user
     winningCard: id of winning card
  }
*/
GameLoop.prototype.handleJudgement = function() {
  var self = this;

  var socket = self.socket;
  var user = socket.userId;
  var room = socket.roomId;

  var msg = self.msg;
  var winner = msg.winner;
  var winningCard = msg.winningCard;

  self.getGameId()
  .then(self.getCurrentRoundAndJudge)
  .then(updateRoundWithJudgement)
  .then(updatePlayerStatesWithJudgement)
  .then(getWinningCard)
  .then(getWinner)
  .then(broadcastResponse)
  .catch(function(errors) {
    socket.emit('error', errors);
    reject(errors)
  });

  function updateRoundWithJudgement() {
    return new Promise(function(resolve,reject) {
      var values = {
        winner: winner,
        winningCard: winningCard,
        state: "over"
      }

      models.Round.update({
        where: {
          id: self.round 
        }
      }).then(function(count, obj) {
        resolve();
      });
    });
  }

  function updatePlayerStatesWithJudgement() {
    return new Promise(function(resolve, reject) {
      var values = {
        state: "round review" 
      } 

      models.PlayerState.update(values, {
        where: {
          gameId: self.game
        } 
      }).then(function(count, obj) {
        resolve();
      });
    }); 
  }
  
  function getWinningCard() {
    var response = {};
    return new Promise(function(resolve, reject) {
      models.Card.find({
        where: {
          id: winningCard 
        } 
      }).then(function(card) {
        response.winningCard = card; 
        resolve(response);
      });
    });
  }

  function getWinner(response) {
    return new Promise(function(resolve, reject) {
      models.User.find({
        where: {
          id: winner 
        } 
      }).then(function(user) {
        response.winner = user; 
        resolve(response);
      }); 
    }); 
  }

  function broadcastResponse(response) {
    socket.broadcast.to(socket.roomId).emit('round review', response);
  }
}
