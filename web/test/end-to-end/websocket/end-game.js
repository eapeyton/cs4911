var 
  Promise = require("bluebird"),
  assert = require('assert'),
  should = require('should'),
  request = require('supertest'),
  io = require('socket.io-client'),
  websocketHelper = require('./websocket-helper');

describe("Clients recieve 'game review' then 'pre-game'",function(){
  before(function(done) {
    websocketHelper.createClients()
    .then(websocketHelper.createCards)
    .finally(done);
  });

  it('server should send "game review" and delayed "pre-game" to clients after entire game has played', function(done){
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
        //round 1, judge client[0]
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
        },
        //round 2, judge client[1]
        {
          sender:0,
          sendKey: 'play card',
          sendMsg: {cardId: clients[0].cards[1].id},
          resKey: 'user has played'
        },
        {
          sender:2,
          sendKey: 'play card',
          sendMsg: {cardId: clients[2].cards[1].id},
          resKey: 'waiting for judge'
        },
        {
          sender:1,
          sendKey: 'choose winning card',
          sendMsg: {
            winner: clients[2].id,
            winningCard: clients[2].cards[1].id
          },
          resKey: 'round review'
        },
        {
          sender: 0,
          sendKey: 'blank',
          sendMsg: {},
          resKey: 'new round'
        },
        //round 3, judge client[2]
        {
          sender:0,
          sendKey: 'play card',
          sendMsg: {cardId: clients[0].cards[2].id},
          resKey: 'user has played'
        },
        {
          sender:1,
          sendKey: 'play card',
          sendMsg: {cardId: clients[1].cards[2].id},
          resKey: 'waiting for judge'
        },
        {
          sender:2,
          sendKey: 'choose winning card',
          sendMsg: {
            winner: clients[0].id,
            winningCard: clients[0].cards[2].id
          },
          resKey: 'round review'
        },
        {
          sender: 0,
          sendKey: 'blank',
          sendMsg: {},
          resKey: 'new round'
        },
        //round 4, judge client[0]
        {
          sender:1,
          sendKey: 'play card',
          sendMsg: {cardId: clients[1].cards[3].id},
          resKey: 'user has played'
        },
        {
          sender:2,
          sendKey: 'play card',
          sendMsg: {cardId: clients[2].cards[3].id},
          resKey: 'waiting for judge'
        },
        {
          sender:0,
          sendKey: 'choose winning card',
          sendMsg: {
            winner: clients[2].id,
            winningCard: clients[2].cards[3].id
          },
          resKey: 'game review'
        },
        {
          sender: 0,
          sendKey: 'blank',
          sendMsg: {},
          resKey: 'pre-game'
        }
      ];
      var runPlayCardEvents = websocketHelper.waitForEvents.bind({events: playCardEvents});

      runPlayCardEvents(clients)
      .then(function(clients){
        var lastResponse = clients[0].lastResponse;

        // ending the game sends the clients back to the pregame state (3)
        lastResponse.should.have.property('playerStates').with.lengthOf(1);
        lastResponse.playerStates[0].should.be.exactly(3);

        done();
      })
    });
  });
});
