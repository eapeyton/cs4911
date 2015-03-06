var 
  Promise = require("bluebird"),
  assert = require('assert'),
  request = require('request'),
  models = require('../../../models'),
  app = require('../../../app'),
  testRoomId;

var should = require('should');
var io = require('socket.io-client');

var socketURL = 'https://ah-jeez.herokuapp.com';

var options ={
  transports: ['websocket'],
  'force new connection': true
};

module.exports = {
  createClients: function(){
    return new Promise(function(resolve, reject){
      dbReset()
      .then(createClientAndCreateRoom)
      .then(createNextClientAndJoinRoom)
      .then(createNextClientAndJoinRoom)
      .then(resolve)
    })
  },

  connectClients: function(){
    return new Promise(function(resolve, reject){
      Promise.join(
        connectClient(1),
        connectClient(2),
        connectClient(3),
        function(client1, client2, client3){
          console.log("\n\n\nclients connected");
          client2.players = client1.players;
          client3.players = client1.players;
          resolve([client1, client2, client3]);
        }
      );
    });
  },

  waitForEvents: function(clients){
    var events = this.events;
    return new Promise(function(resolve, reject){
      Promise.each(events, waitForEvent)
      .finally(function(){
        resolve(clients);
      });

      function waitForEvent(event){
        return new Promise(function(resolve, reject){
          console.log("waiting for...", event);
          Promise.join(
            clientWaitFor(clients[0], event.resKey),
            clientWaitFor(clients[1], event.resKey),
            clientWaitFor(clients[2], event.resKey),
            function(client1Res, client2Res, client3Res){
              console.log("completed", event);
              clients[0].lastResponse = client1Res;
              clients[1].lastResponse = client2Res;
              clients[2].lastResponse = client3Res;
              resolve([clients[0], clients[1], clients[2]]);
            }
          );
          clients[event.sender].emit(event.sendKey, event.sendMsg);
        });
      }
    });
  },

  createCards: function(){
    return new Promise(function(resolve, reject){
      var options = {
        url: socketURL+'/test/cards'
      };
      var callback = function(error, response){
        resolve();
      }

      request.post(options, callback);  
    });
  },

  updateClientsCards: function(clients){
    return new Promise(function(resolve, reject){
      Promise.join(
        getClientsCards(1),
        getClientsCards(2),
        getClientsCards(3),
        function(client1Res, client2Res, client3Res){
          clients[0].cards = client1Res;
          clients[1].cards = client2Res;
          clients[2].cards = client3Res;
          resolve([clients[0], clients[1], clients[2]]);
        }
      );
    });

    function getClientsCards(num){
      return new Promise(function(resolve, reject){
        var options = {
          url: socketURL+'/hands',
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Token testFbToken" + num
          }
        };
        var callback = function(error, response){
          resolve(JSON.parse(response.body).cards);
        }

        request.get(options, callback);  
      });
    }
  }
}

function clientWaitFor(client, msg){
  return new Promise(function(resolve, reject){
    client.on(msg, function(data){
      resolve(data)
    });
  });
}

function dbReset(){
  return new Promise(function(resolve, reject){
    var options = {
      url: socketURL+'/test/reset'
    };
    var callback = function(error, response){
      resolve();
    }

    request.post(options, callback);  
  });
}

function createClientAndCreateRoom(){
  return new Promise(function(resolve, reject){
    createClient(1)
    .then(createRoom)
    .then(resolve)
  });

  function createRoom(user){
    return new Promise(function(resolve, reject){
      var options = {
        url: socketURL+'/rooms',
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Token "+user.fbToken
        },
        body: JSON.stringify({
          room: {
            name: "test room",
            maxPlayers: 4
          }
        })
      };
      var callback = function(error, response){
        var roomResponse = JSON.parse(response.body);
        testRoomId = roomResponse.room.id;
        resolve(roomResponse);
      }

      request.post(options, callback);  
    });
  }
}

function createNextClientAndJoinRoom(roomResponse){
  var nextClientNum = roomResponse.judges.length+1;
  return new Promise(function(resolve, reject){
    createClient(nextClientNum)
    .then(joinRoom)
    .then(resolve)
  });
  function joinRoom(user){
    return new Promise(function(resolve, reject){
      var options = {
        url: socketURL+'/rooms/join/'+roomResponse.room.id,
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Token "+user.fbToken
        }
      };
      var callback = function(error, response){
        resolve(JSON.parse(response.body));
      };

      request.post(options, callback);  
    });
  }
}

function getClient(num){
  return new Promise(function(resolve, reject){
    var options = {
      url: socketURL+'/users/login',
      headers: {
        'content-type': 'application/json'
      },
      body: JSON.stringify({
        user: {
          fbToken: "testFbToken"+num, 
          fbId: "testFbId"+num
        }
      })
    };
    var callback = function(error, response){
      resolve(JSON.parse(response.body).user);
    }

    request.post(options, callback);  
  });
}

function createClient(num){
  return new Promise(function(resolve, reject){
    var options = {
      url: socketURL+'/users/login',
      headers: {
        'content-type': 'application/json'
      },
      body: JSON.stringify({
        "user": {
          fbToken: "testFbToken"+num, 
          fbId: "testFbId"+num,
          pic: "testPic"+num,
          name: "testName"+num
        }
      })
    };
    var callback = function(error, response){
      resolve(JSON.parse(response.body).user);
    }

    request.post(options, callback);  
  });
}

function connectClient(num){
  return new Promise(function(resolve, reject){
    var client = io.connect(socketURL, options);
    client.on('connect', function(data){
      getClient(num)
      .then(setupWebsocket)
      .then(resolve);

      function setupWebsocket(user){
        return new Promise(function(){
          client.fbToken = user.fbToken;
          client.players = [];
          client.on('user joined', function(joinedUser){
            if(user.id === joinedUser.id){
              resolve(client);
            }
            client.players.push(joinedUser);
          });
          client.emit("setup socket for user", user);
        });
      }
    });
  });
}