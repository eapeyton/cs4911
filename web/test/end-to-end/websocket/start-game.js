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

  it('broadcast "host started" with hand ids, black card, and who the judge is', function(done){
    websocketHelper.connectClientsAndWaitForEvents([
      {
        sender: 0,
        sendKey: 'start game',
        resKey: 'host started game'
      }
    ])
    .then(function(clients){
      console.log(clients[0].lastResponse);
      console.log(clients[1].lastResponse);
      console.log(clients[2].lastResponse);
      done();
    });
  });
});