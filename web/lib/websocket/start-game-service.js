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
  -broadcast  "new game" state with hand ids, black card, and who the judge is. (front end will get players cards through hand ids with token)
*/
StartGameService.prototype.startGame = function(){
  var socket = this.socket;

  getFirstJudge()
  .then(getBlackCard)
  .then(createGame)
  .then(createRound)
  .then(createHands)
  .then(createPlayerStates)
  .then(broadcastResponse);

  function getFirstJudge(){
    return new Promise(function(resolve, reject){
      resolve();
    });
  }

  function getBlackCard(response){
    return new Promise(function(resolve, reject){
      resolve();
    });
  }

  function createGame(response){
    return new Promise(function(resolve, reject){
      resolve();
    });
  }

  function createRound(response){
    return new Promise(function(resolve, reject){
      resolve();
    });
  }

  function createHands(response){
    return new Promise(function(resolve, reject){
      resolve();
    });
  }

  function createPlayerStates(response){
    return new Promise(function(resolve, reject){
      resolve({
        roomId: socket.roomId,
        userId: socket.userId
      });
    });
  }

  function broadcastResponse(response){
    socket.broadcast.to(socket.roomId).emit('host started game', response);
    socket.emit('host started game', response);
  }
}
