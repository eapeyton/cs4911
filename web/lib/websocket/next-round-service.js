module.exports = NextRoundService;
var
  Promise = require('bluebird'),
  models = require('../../models'),
  ChildProcessService = require('./child-process-service');

function NextRoundService(socket, roundOverResponse){
  this.socket = socket;
  this.roundOverResponse = roundOverResponse;
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
StartGameService.prototype.setupNextRound = function(){
  var socket = this.socket;
  var roundOverResponse = this.roundOverResponse;

  getNextJudge()
  .then(getBlackCard)
  .then(createRound)
  .then(dealReplacementCards)
  .then(updatePlayerStates)
  .then(setupChildProcessBroadcast)

  .catch(function(errors){
    socket.emit('error', errors);
    reject(errors);
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
        if(lastJudgeIndex = judges.length){
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
      updatePlayersPlayerStates
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
            resolve();
          });
        });
      }
    });
  }

  function setupChildProcessBroadcast(response){
    return new Promise(function(resolve, reject) {
      var delay = (new Date()) - roundOverResponse.sendTime;
      var ROUND_REVIEW_TIME = 10000;
      var childProcessService = new ChildProcessService();
      ChildProcessService.sendMsgWithDelay("new round", response, socket, ROUND_REVIEW_TIME-delay);
      resolve();
    });
  }
}
