var 
  Promise = require("bluebird"),
  assert = require('assert'),
  should = require('should'),
  request = require('supertest'),
  io = require('socket.io-client'),
  websocketHelper = require('./websocket-helper');

describe("Judge send 'choose winning card'",function(){
  before(function(done) {
    websocketHelper.createClients()
    .then(websocketHelper.createCards)
    .finally(done);
  });

  it('broadcast "round review"', function(done){
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
        }
      ];
      var runPlayCardEvents = websocketHelper.waitForEvents.bind({events: playCardEvents});

      runPlayCardEvents(clients)
      .then(function(clients){
        var lastResponse = clients[0].lastResponse;

        // ensure the round is judged and over
        lastResponse.should.have.property('round').with.property('state', 'over');

        // ensure the proper winner across multiple models
        lastResponse.should.have.property('round').with.property('winner');
        lastResponse.should.have.property('winner').with.property('id', lastResponse.round.winner);

        // ensure the proper winning card acress multiple models
        lastResponse.should.have.property('round').with.property('winningCard');
        lastResponse.should.have.property('winningCard').with.property('id', lastResponse.round.winningCard);
        done();
      })
    });
  });
});
