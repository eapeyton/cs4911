var 
  Promise = require("bluebird"),
  assert = require('assert'),
  should = require('should'),
  request = require('supertest'),
  io = require('socket.io-client'),
  websocketHelper = require('./websocket-helper');

describe("Clients recieve 'new round'",function(){

  it('server should send "new round" to clients after "round review"', function(done){
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
        var lastResponse = clients[0].lastResponse;

        // check for judge
        lastResponse.should.have.property('round').with.property('judge');

        // check for black card
        lastResponse.should.have.property('blackCard');
        
        // check for correct round state
        lastResponse.should.have.property('round').with.property('state');
        lastResponse.round.state.should.be.exactly('waiting for players');

        websocketHelper.getClientsCards(2)
        .then(function(cards){
          console.log(clients[1].cards);
          console.log(cards);
          var a =[];
          var b =[];
          for(var i=0; i<cards.length; i++){
            a.push(clients[1].cards[i].text);
            b.push(cards[i].text);
          }
          console.log(a);
          console.log(b);
          clients[1].cards.length.should.equal(cards.length);
          clients[1].cards[0].text.should.not.equal(cards[0].text);
          clients[1].cards[1].text.should.equal(cards[1].text);
          done();
        });
      })
    });
  });
});

