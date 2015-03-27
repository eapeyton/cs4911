var 
  Promise = require("bluebird"),
  assert = require('assert'),
  should = require('should'),
  request = require('supertest'),
  io = require('socket.io-client'),
  websocketHelper = require('./websocket-helper');

describe("'start game'",function(){
  before(function(done) {
    websocketHelper.createClients()
    .then(websocketHelper.createCards)
    .finally(done);
  });

  it('broadcast "host started" with black card and who the judge is', function(done){
    var startGameEvents = [
      {
        sender: 0,
        sendKey: 'start game',
        resKey: 'host started game'
      }
    ];

    websocketHelper.connectClients()
    .then(websocketHelper.waitForEvents.bind({events: startGameEvents}))
    .then(function(clients){
      var lastResponse = clients[0].lastResponse;

      lastResponse.should.have.property('judge');
      lastResponse.should.have.property('blackCard').with.property('type', 'black');
      lastResponse.should.have.property('round').with.property('winner', null);
      lastResponse.should.have.property('round').with.property('winningCard', null);
      lastResponse.should.have.property('playerStates');

      // one of the players should be waiting, the others playing
      lastResponse.playerStates.should.containDeep([{state: 'waiting for players'},{state: 'playing'},{state: 'playing'}]);
      done();
    });
  });
});
