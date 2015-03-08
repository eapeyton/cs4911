module.exports = GameLoop;
var
  cp = require('child_process');
  Promise = require('bluebird');
  models = require('../../models');

function GameLoop(socket, msg) {
  this.socket = socket;
  this.msg = msg;
}

GameLoop.prototype.getGame = function() {
  var self = this;
  return new Promise(function(resolve, reject) {
    models.Game.find({
      where: {
        roomId: self.socket.roomId,
        finishTime: null
      } 
    }).then(function(game) {
      resolve(game);
    }); 
  });
}

GameLoop.prototype.getCurrentRound = function(game) {
  var self = this;
  return new Promise(function(resolve, reject) {
    models.Round.find({
      where: ['"gameId" = ? AND "state" != ?', game.id, "over"]
    })
    .then(function(round) {
      self.round = round;
      resolve({
        round: round,
        game: game
      });
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
     cardId: id of card played
  }
 */
GameLoop.prototype.handlePlay = function() {
  var self = this;

  var socket = self.socket;
  var userId = socket.userId;
  var roomId = socket.roomId;

  var msg = self.msg;
  var cardId = msg.cardId;

  self.getGame()
  .then(self.getCurrentRound)
  .then(createPlayedCard)
  .then(updateHandToPlayed)
  .then(updatePlayerStateToWaiting)
  .then(updateGameState)
  .then(broadcastResponse)
  .catch(function(errors) {
    socket.emit('error', errors);
    reject(errors)
  });

  
  function createPlayedCard(response) {
    return new Promise(function(resolve, reject) {
      models.PlayedCard.create({
        userId: userId,
        cardId: cardId,
        roundId: response.round.id
      }).then(function(playedCard) {
        response.playedCard = playedCard;
        resolve(response);
      })
    });
  }

  function updateHandToPlayed(response) {
    return new Promise(function(resolve, reject) {
      var values = {
        played: true 
      };

      models.Hand.find({
        where: {
          cardId: cardId,
          gameId: response.game.id,
          userId: userId,
          played: false
        } 
      }).then(function(hand) {
        if(hand === null){
          reject("card is not in hand or already played")
        }else{
          hand.updateAttributes(values)
          .then(function(){
            resolve(response)
          })
        }
      });
    });
  }

  function updatePlayerStateToWaiting(response) {
    return new Promise(function(resolve, reject) {
      var values = {
        state: "waiting for players"
      };

      models.PlayerState.update(values, {
        where: {
          gameId: response.game.id,
          userId: userId
        } 
      }).then(function(count, obj) {
        resolve(response); 
      });
    });
  }

  function updateGameState(response) {
    return new Promise(function(resolve, reject) {
      getPlayerStates()
      .then(checkAllPlayersForPlay)
      .then(adjustGameState)
      .finally(function(){
        resolve(response);
      });

      function getPlayerStates(){
        return new Promise(function(resolve, reject) {
          models.PlayerState.findAll({
            where: {
              gameId: response.game.id
            }
          }).then(function(playerStates){
            resolve(playerStates);
          });
        });
      }
      
      function checkAllPlayersForPlay(playerStates) {
        return new Promise(function(resolve, reject) {
          for (var i = 0; i < playerStates.length; i++) {
            if (playerStates[i].state !== "waiting for players") {
              resolve(false);
            }
          }
          resolve(true);
        });
      }

      function adjustGameState(allPlayed) {
        return new Promise(function(resolve, reject) {
          if (allPlayed) {
            updateRoundStateToJudge()      
            .then(updatePlayersPlayerStates)
            .then(updateJudgesPlayerState)
            .then(getPlayedCardsForRound)
            .then(resolve);
          } else {
            response.key = "user has played";
            response.userId = userId;
            resolve();
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
              id: response.round.id
            } 
          }).then(function(count, obj) {
            resolve();
          });
        });
      }

      function updatePlayersPlayerStates() {
        return new Promise(function(resolve, reject) {
          var values = {
            state: "waiting for judge" 
          }; 

          models.PlayerState.update(values, {
            where: ['"gameId" = ? AND "userId" != ?', response.game.id, response.round.judge]
          }).then(function(count, obj) {
            resolve();
          });
        }); 
      }

      function updateJudgesPlayerState() {
        return new Promise(function(resolve, reject) {
          var values = {
            state: "judging" 
          }; 

          models.PlayerState.update(values, {
            where: {
              userId: response.round.judge,
              gameId: response.game.id
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
              roundId: response.round.id
            },
            include:[{
              model: models.Card
            }]
          }).then(function(playedCards) {
            response.key = 'waiting for judge';
            response.playedCards = playedCards;
            resolve();
          });
        });
      }
    });
  }

  function broadcastResponse(response) {
    socket.broadcast.to(socket.roomId).emit(response.key, response);
    socket.emit(response.key, response);
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
  .then(self.getCurrentRound)
  .then(updateRoundWithJudgement)
  .then(getWinningCard)
  .then(getWinner)
  .then(getRoundsByWinner)
  .then(broadcastResponse)
  .catch(function(errors) {
    socket.emit('error', errors);
    reject(errors)
  });

  function updateRoundWithJudgement(response) {
    return new Promise(function(resolve,reject) {
      var values = {
        winner: winner,
        winningCard: winningCard,
        state: "over"
      }

      models.Round.update({
        where: {
          id: response.round.id
        }
      }).then(function(count, obj) {
        resolve(response);
      });
    });
  }
  
  function getWinningCard(response) {
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

  function getRoundsByWinner(response){
    return new Promise(function(resolve, reject) {
      models.Round.findAll({
        where: {
          gameId: response.game.id
        } 
      }).then(function(rounds) {
        var leader = {userId: null, points: -1};
        var roundsByWinner = {};
        for(var i=0; i<rounds.length; i++){
          if(rounds[i].winner in roundsByWinner){
            roundsByWinner[rounds[i].winner].push(rounds[i]); 
          }else{
            roundsByWinner[rounds[i].winner] = [rounds[i]]; 
          }
          if(leader.points < roundsByWinner[rounds[i].winner].length){
            leader.userId = rounds[i].winner;
            leader.points = roundsByWinner[rounds[i].winner].length;
          }
        }

        response.roundsByWinner = roundsByWinner;
        response.leader = leader;
        resolve(response);
      });
    }); 
  }

  function broadcastResponse(response) {
    return new Promise(function(resolve, reject) {
      if(response.leader.points < 7){
        response.sentTime = new Date();
        socket.broadcast.to(socket.roomId).emit("round review", response);
        socket.emit("round review", response);

        nextRoundService = new nextRoundService();
        nextRoundService.setupNextRound(socket, response)
        .then(resolve);
      }else{
        socket.broadcast.to(socket.roomId).emit("game review", response);
        socket.emit("game review", response);

        //endGameService = new endGameService();
        //endGameService.endGame(socket, response)
        //.then(resolve);
      }
    });
  }
}
