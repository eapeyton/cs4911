var 
  Promise = require("bluebird"),
  assert = require('assert'),
  should = require('should'),
  request = require('supertest'),
  io = require('socket.io-client'),
  websocketHelper = require('./websocket-helper');

describe("Clients recieve 'new round'",function(){
  before(function(done) {
    websocketHelper.createClients()
    .then(websocketHelper.createCards)
    .finally(done);
  });

  it('server should send "new round" to clients after "round review"', function(done){
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
        },
        {
          sender:0,
          sendKey: 'choose winning card',
          sendMsg: {
            winner: clients[2].id,
            winningCard: clients[2].cards[0].id
          },
          resKey: 'round review'
        },
        {
          sender: 0,
          sendKey: 'blank',
          sendMsg: {},
          resKey: 'new round'
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