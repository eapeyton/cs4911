module.exports = StartGameService;
var
  Promise = require('bluebird');
  models = require('../../models');

function StartGameService(socket){
  this.socket = socket;
}
/*
  -get first Judge
  -get a Black Card
  -create Game
  -create Hand for each user
  -create Round, state "waiting for players"
  -create players' PlayerState with state "playing"
  -create judge's PlayerState with state "waiting for players"
  -broadcast  "host started game" with black card, the judge, player states, and the round
*/
StartGameService.prototype.startGame = function(){
  var socket = this.socket;

  validate()
  .then(getFirstJudge)
  .then(getBlackCard)
  .then(createGame)
  .then(createRound)
  .then(createHands)
  .then(createPlayerStates)
  .then(broadcastResponse)

  function validate(){
    return new Promise(function(resolve, reject){
      validateHost()
      .then(validateGameNotStarted)
      .then(resolve)
    });

    function validateHost(){
      return new Promise(function(resolve, reject){
        models.Host.find({
          where: {roomId: socket.roomId}
        })
        .then(function(host){
          if(host.userId !== socket.userId){
            socket.emit("user is not host", {});
            reject("user is not host");
          }else{
            resolve();
          }
        });
      });
    }

    function validateGameNotStarted(){
      return new Promise(function(resolve, reject){
        models.Game.findAll({
          where: {roomId: socket.roomId},
          order: '"finishTime" DESC'
        })
        .then(function(games){
          game = games[0]
          if(game !== undefined && game.finishTime === null){
            socket.emit('game is already being played', {});
            reject('game is already being played');
          }else{
            resolve();
          }
        });
      });
    }
  }

  function getFirstJudge(){
    var response = {};
    return new Promise(function(resolve, reject){
      models.Judge.findAll({
        where: {roomId: socket.roomId},
        order: 'place ASC',
        include:[{
          model: models.User
        }]
      })
      .then(function(judges){
        response.judge = judges[0];
        resolve(response);
      })
    });
  }

  function getBlackCard(response){
    return new Promise(function(resolve, reject){
      models.Card.findAll({
        where: {type: 'black'},
        limit: 1,
        random: true
      })
      .then(function(blackCard){
        response.blackCard = blackCard[0];
        resolve(response);
      })
    });
  }

  function createGame(response){
    return new Promise(function(resolve, reject){
      models.Game.create({
        roomId: socket.roomId,
        finishTime: null
      })
      .then(function(game){
        response.game = game;
        resolve(response);
      })
    });
  }

  function createRound(response){
    return new Promise(function(resolve, reject){
      models.Round.create({
        gameId: response.game.dataValues.id,
        judge: response.judge.dataValues.userId,
        blackCard: response.blackCard.dataValues.id,
        state: "waiting for players"
      })
      .then(function(round){
        response.round = round;
        resolve(response);
      })
    });
  }

  function createHands(response){
    return new Promise(function(resolve, reject){
      getPlayers()
      .then(getWhiteCards)
      .then(dealCards)
      .then(resolve)

      function getWhiteCards(players){
        return new Promise(function(resolve, reject){
          models.Card.findAll({
            where: {type: 'white'},
            order: 'RANDOM()',
            limit: players.length*7
          })
          .then(function(cards){
            var playersWithCards = [];
            for(var i=0; i<players.length; i++){
              playersWithCards.push({
                cards: cards.slice(i*7, i*7+7),
                player: players[i]
              });
            }
            resolve(playersWithCards);
          })
        });
      }

      function dealCards(playersWithCards){
        return new Promise(function(resolve, reject){
          var handEntries = [];
          for(var p=0; p<playersWithCards.length; p++){
            for(var c=0; c<playersWithCards[p].cards.length; c++){
              handEntries.push({
                userId: playersWithCards[p].player.id,
                cardId: playersWithCards[p].cards[c].id,
                gameId: response.game.dataValues.id,
                played: false
              });
            }
          }

          models.Hand.bulkCreate(handEntries)
          .then(function(hands){
            resolve(response);
          })
        });
      }
    });
  }

  function createPlayerStates(response){
    return new Promise(function(resolve, reject){
      getPlayers()
      .then(createPlayerStates)
      .then(addStatesToResponse)
      .then(resolve)

      function createPlayerStates(players){
        return new Promise(function(resolve, reject){
          var playerStateEntries = [];
          for(var i=0; i<players.length; i++){
            var state = "playing"
            if(response.judge.dataValues.userId === players[i].id){
              state = "waiting for players"
            }
            playerStateEntries.push({
              userId: players[i].id,
              gameId: response.game.dataValues.id,
              state: state
            });
          }
          models.PlayerState.bulkCreate(playerStateEntries)
          .then(function(playerState){
            resolve(playerState);
          });
        });
      }

      function addStatesToResponse(playerStates){
        response.playerStates = playerStates;
        return response;
      }
    });
  }

  function getPlayers(){
    return new Promise(function(resolve, reject){
      models.User.findAll({
        where: {roomId: socket.roomId}
      })
      .then(function(players){
        resolve(players);
      })
    });
  }

  function broadcastResponse(response){
    socket.broadcast.to(socket.roomId).emit('host started game', response);
    socket.emit('host started game', response);
  }
}
