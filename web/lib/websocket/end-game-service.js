module.exports = EndGameService;
var
  Promise = require('bluebird'),
  models = require('../../models'),
  ChildProcessService = require('./child-process-service');

function EndGameService(socket, roundOverResponse){
  this.socket = socket;
  this.roundOverResponse = roundOverResponse;
}

EndGameService.prototype.endGame = function(){
  var socket = this.socket;
  var roundOverResponse = this.roundOverResponse;
  return new Promise(function(resolve, reject){
    setGameFinishTime()
    .then(updatePlayerStates)
    .then(setupChildProcessBroadcast)
    .then(resolve)
    .catch(function(errors){
      socket.emit('error', errors);
      reject(errors);
    });
  });

  function setGameFinishTime(){
    var response = {}
    return new Promise(function(resolve, reject){
      var values = {
        finishTime: new Date()
      }; 
      roundOverResponse.game.updateAttributes(values)
      .then(function(game){
        response.game = game;
        resolve(response);
      })
    });
  }

  function updatePlayerStates(response){
    return new Promise(function(resolve, reject){
      var values = {
        state: "done" 
      }; 

      models.PlayerState.update(values, {
        where: {
          gameId: roundOverResponse.game.id
        }
      }).then(function(count, obj) {
        response.playerStates = count;
        resolve(response);
      });
    });
  }

  function setupChildProcessBroadcast(response){
    return new Promise(function(resolve, reject) {
      var delay = (new Date()) - roundOverResponse.sendTime;
      var GAME_REVIEW_TIME = 2000;
      var childProcessService = new ChildProcessService();
      childProcessService.sendMsgWithDelay("pre-game", response, socket, GAME_REVIEW_TIME-delay);
      resolve();
    });
  }
}
