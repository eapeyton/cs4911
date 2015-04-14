var 
  Promise = require("bluebird"),
  assert = require('assert'),
  should = require('should'),
  request = require('supertest'),
  io = require('socket.io-client'),
  websocketHelper = require('./websocket-helper');

describe("'start game'",function(){

  it('broadcast "host started" with black card and who the judge is', function(done){
    var startGameEvents = [
      {
        sender: 0,
        sendKey: 'start game',
        resKey: 'host started game'
      }
    ];


    websocketHelper.createClients()
    .then(websocketHelper.createCards)
    .then(websocketHelper.connectClients)
    .then(websocketHelper.waitForEvents.bind({events: startGameEvents}))
    .then(function(clients){
      var lastResponse = clients[0].lastResponse;

      lastResponse.should.have.property('judge');
      lastResponse.should.have.property('blackCard').with.property('type', 'black');
      lastResponse.should.have.property('playerStates');

      // one of the players should be waiting, the others playing
      lastResponse.playerStates.should.containDeep([{state: 'waiting for players'},{state: 'playing'},{state: 'playing'}]);
      done();
    });
  });

  it('if non-host tries to start game, send error ', function(done){
    var errorEvent = {
      sender: 1,
      sendKey: 'start game',
      resKey: 'user is not host'
    };

    websocketHelper.createClients()
    .then(websocketHelper.createCards)
    .then(websocketHelper.connectClients)
    .then(websocketHelper.waitForError.bind({errorEvent: errorEvent}))
    .then(function(clients){
      done();
    });
  });

  it('if host tries to start game while game in progress, send error ', function(done){
    var startGameEvents = [
      {
        sender: 0,
        sendKey: 'start game',
        resKey: 'host started game'
      }
    ];
    var errorEvent = {
      sender: 0,
      sendKey: 'start game',
      resKey: 'game is already being played'
    };

    websocketHelper.createClients()
    .then(websocketHelper.createCards)
    .then(websocketHelper.connectClients)
    .then(websocketHelper.waitForEvents.bind({events: startGameEvents}))
    .then(websocketHelper.waitForError.bind({errorEvent: errorEvent}))
    .then(function(clients){
      done();
    });
  });
});
