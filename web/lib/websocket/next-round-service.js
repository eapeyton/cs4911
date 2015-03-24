module.exports = NextRoundService;
var
  Promise = require('bluebird'),
  models = require('../../models'),
  ChildProcessService = require('./child-process-service');

function NextRoundService(socket, roundOverResponse){
  this.socket = socket;
  this.roundOverResponse = roundOverResponse;
}

NextRoundService.prototype.setupNextRound = function(){
  var socket = this.socket;
  var roundOverResponse = this.roundOverResponse;
  return new Promise(function(resolve, reject){
    getNextJudge()
    .then(getBlackCard)
    .then(createRound)
    .then(dealReplacementCards)
    .then(updatePlayerStates)
    .then(setupChildProcessBroadcast)
    .then(resolve)
    .catch(function(errors){
      socket.emit('error', errors);
      reject(errors);
    });
  });

  function getNextJudge(){
    var response = {};
    return new Promise(function(resolve, reject){
      models.Judge.findAll({
        where: {roomId: socket.roomId},
        order: 'place ASC'
      })
      .then(function(judges){
        var lastJudgeIndex = -1;
        for(var i=0; i<judges.length; i++){
          if(judges[i].userId === roundOverResponse.round.judge){
            lastJudgeIndex = i;
          }
        }
        var nextJudgeIndex = lastJudgeIndex+1;
        if(nextJudgeIndex >= judges.length){
          nextJudgeIndex = 0;
        }
        response.judge = judges[nextJudgeIndex];
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

  function createRound(response){
    return new Promise(function(resolve, reject){
      models.Round.create({
        gameId: roundOverResponse.game.id,
        judge: response.judge.userId,
        blackCard: response.blackCard.id,
        state: "waiting for players"
      })
      .then(function(round){
        response.round = round;
        resolve(response);
      })
    });
  }

  function dealReplacementCards(response){
    return new Promise(function(resolve, reject){
      getPlayers()
      .then(getWhiteCards)
      .then(dealCards)
      .then(resolve)

      //TODO: MAKE CARDS NOT DUPLICATE AND RANDOM
      function getWhiteCards(players){
        return new Promise(function(resolve, reject){
          models.Card.findAll({
            where: {type: 'white'},
            limit: players.length
          })
          .then(function(cards){
            var handEntries = [];
            for(var i=0; i<players.length; i++){
              handEntries.push({
                userId: players[i].id,
                cardId: cards[i].id,
                gameId: roundOverResponse.game.id,
                played: false
              });
            }
            resolve(handEntries);
          })
        });
      }

      function dealCards(handEntries){
        return new Promise(function(resolve, reject){
          models.Hand.bulkCreate(handEntries)
          .then(function(hands){
            resolve(response);
          })
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
    });
  }

  function updatePlayerStates(response){
    return new Promise(function(resolve, reject){
      updatePlayersPlayerStates()
      .then(updateJudgesPlayerState)
      .then(resolve);

      function updatePlayersPlayerStates() {
        return new Promise(function(resolve, reject) {
          var values = {
            state: "playing" 
          }; 

          models.PlayerState.update(values, {
            where: ['"gameId" = ? AND "userId" != ?', 
            roundOverResponse.game.id, 
            response.round.judge]
          }).then(function(count, obj) {
            resolve();
          });
        }); 
      }

      function updateJudgesPlayerState() {
        return new Promise(function(resolve, reject) {
          var values = {
            state: "waiting for players" 
          }; 

          models.PlayerState.update(values, {
            where: {
              userId: response.round.judge,
              gameId: roundOverResponse.game.id
            } 
          }).then(function(count, obj) {
            resolve(response);
          });
        });
      }
    });
  }

  function setupChildProcessBroadcast(response){
    return new Promise(function(resolve, reject) {
      var delay = (new Date()) - roundOverResponse.sendTime;
      var ROUND_REVIEW_TIME = 2000;
      var childProcessService = new ChildProcessService();
      childProcessService.sendMsgWithDelay("new round", response, socket, ROUND_REVIEW_TIME-delay);
      resolve();
    });
  }
}
