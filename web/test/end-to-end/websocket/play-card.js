var 
  Promise = require("bluebird"),
  assert = require('assert'),
  should = require('should'),
  request = require('supertest'),
  io = require('socket.io-client'),
  websocketHelper = require('./websocket-helper');

describe("A single client sends 'play card'",function(){
  before(function(done) {
    websocketHelper.createClients()
    .then(websocketHelper.createCards)
    .finally(done);
  });

  it('broadcast "user has played"', function(done){
    var startGameEvents = [
      {
        sender: 0,
        sendKey: 'start game',
        resKey: 'host started game'
      }
    ];

    websocketHelper.connectClients()
    .then(websocketHelper.waitForEvents.bind({events: startGameEvents}))
    .then(websocketHelper.updateClientsCards)
    .then(function(clients){
      var playCardEvents = [
        {
          sender:0,
          sendKey: 'play card',
          sendMsg: {cardId: clients[0].cards[0].id},
          resKey: 'user has played'
        }
      ];
      var runPlayCardEvents = websocketHelper.waitForEvents.bind({events: playCardEvents});

      runPlayCardEvents(clients)
      .then(function(clients){
        console.log(clients[0].lastResponse);
        done();
      })
    });
  });
});

describe("All clients send 'play card'",function(){
  before(function(done) {
    websocketHelper.createClients()
    .then(websocketHelper.createCards)
    .finally(done);
  });

  it('broadcast "waiting for judge"', function(done){
    var startGameEvents = [
      {
        sender: 0,
        sendKey: 'start game',
        resKey: 'host started game'
      }
    ];

    websocketHelper.connectClients()
    .then(websocketHelper.waitForEvents.bind({events: startGameEvents}))
    .then(websocketHelper.updateClientsCards)
    .then(function(clients){
      var playCardEvents = [
        {
          sender:0,
          sendKey: 'play card',
          sendMsg: {cardId: clients[0].cards[0].id},
          resKey: 'user has played'
        },
        {
          sender:1,
          sendKey: 'play card',
          sendMsg: {cardId: clients[1].cards[0].id},
          resKey: 'user has played'
        },
        {
          sender:2,
          sendKey: 'play card',
          sendMsg: {cardId: clients[2].cards[0].id},
          resKey: 'waiting for judge'
        }
      ];
      var runPlayCardEvents = websocketHelper.waitForEvents.bind({events: playCardEvents});

      runPlayCardEvents(clients)
      .then(function(clients){
        console.log(clients[0].lastResponse);
        done();
      })
    });
  });
});