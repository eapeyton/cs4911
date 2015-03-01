var 
  Promise = require("bluebird"),
  assert = require('assert'),
  should = require('should'),
  request = require('supertest'),
  io = require('socket.io-client'),
  websocketHelper = require('./websocket-helper');

describe("'setup socket for user'",function(){
  before(function(done) {
    websocketHelper.createClients()
    .finally(done);
  });

  it('should setup socket and notify others in the room', function(done){
    websocketHelper.connectClients()
    .spread(function(client1, client2, client3){
      for(var i=0; i<3; i++){
        client1.players.some(function(element){
          return element.name === 'testName'+ (i+1);
        }).should.equal(true);
      }
      done();
    });
  });
});

describe("'start game'",function(){
  before(function(done) {
    websocketHelper.createClients()
    .then(websocketHelper.createCards)
    .finally(done);
  });

  it('broadcast "host started" with hand ids, black card, and who the judge is', function(done){
    websocketHelper.connectClients()
    .spread(function(client1, client2, client3){
      Promise.join(
        websocketHelper.clientWaitFor(client1, 'host started game'),
        websocketHelper.clientWaitFor(client2, 'host started game'),
        websocketHelper.clientWaitFor(client3, 'host started game'),
        function(client1Res, client2Res, client3Res){
          console.log(client1Res);
          console.log(client2Res);
          console.log(client3Res);
          done();
        }
      );
      client1.emit("start game");
    });
  });
});