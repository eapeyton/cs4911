var 
  Promise = require("bluebird"),
  assert = require('assert'),
  should = require('should'),
  request = require('supertest'),
  io = require('socket.io-client'),
  websocketHelper = require('./websocket-helper');

describe("'setup socket for user'",function(){
  before(function(done) {
    websocketHelper.createClients(done);
  });

  it('should setup socket and notify others in the room', function(done){
    websocketHelper.connectClients()
    .spread(function(client1, client2, client3){
      for(var i=0; i<3; i++){
        client1.players[i].name.should.equal('testName'+(i+1));
        client1.players[i].pic.should.equal('testPic'+(i+1));
      }
      done();
    });
  });
});