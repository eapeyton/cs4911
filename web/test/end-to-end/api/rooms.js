var 
  assert = require('assert'),
  should = require('should'),
  request = require('supertest'),
  fs = require('fs'),
  apiHelper = require('./api-helper'),
  models = require('../../../models'),
  app = require('../../../app');

describe('Creating a room', function() {
  before(function(done) {
    apiHelper.resetDb(done);
  });
  it('should create a new room and put user in room as host', function(done) {
    apiHelper.getTestUserAndRun(function(testUser){
      var roomData = {
        name: "test room",
        maxPlayers: 4
      };
      request(app).post('/rooms')
        .set("Content-Type", "application/json")
        .set("Authorization", "Token "+testUser.fbToken)
        .send({"room": roomData})
        .expect('Content-Type', /json/)
        .expect(200)
        .expect(function(res){
          //TODO: break this up
          res.body.success.should.equal(true);
          res.body.room.name.should.equal(roomData.name);
          res.body.room.maxPlayers.should.equal(roomData.maxPlayers);
          res.body.host.userId.should.equal(testUser.id);
          res.body.host.roomId.should.equal(res.body.room.id);
          res.body.judges[0].userId.should.equal(testUser.id);
          res.body.judges[0].roomId.should.equal(res.body.room.id);
          res.body.judges[0].place.should.equal(1);
        })
        .end(done);
    });
  });
});

describe('Joining a room', function(){
  before(function(done) {
    apiHelper.resetDb(done);
  });

  it('should allow a user to join a new room', function(done) {
    apiHelper.getTestUserAndRun(function(testUser){
      apiHelper.createSecondUserAndRun(function(testUser2){
      var roomId;
      var roomData = {
        name: "test room",
        maxPlayers: 4
      };
      request(app).post('/rooms')
        .set("Content-Type", "application/json")
        .set("Authorization", "Token "+testUser.fbToken)
        .send({"room": roomData})
        .expect(function(res){
          roomId = res.body.room.id;
        })
        .end(function(){
          request(app).post('/rooms/join/'+roomId)
            .set("Content-Type", "application/json")
            .set("Authorization", "Token "+testUser2.fbToken)
            .send()
            .expect('Content-Type', /json/)
            .expect(200)
            .expect(function(res){
              //TODO: break this up
              res.body.success.should.equal(true);
              res.body.room.name.should.equal(roomData.name);
              res.body.room.maxPlayers.should.equal(roomData.maxPlayers);
              res.body.host.userId.should.equal(testUser.id);
              res.body.host.roomId.should.equal(res.body.room.id);
              res.body.judges[0].userId.should.equal(testUser.id);
              res.body.judges[0].roomId.should.equal(res.body.room.id);
              res.body.judges[0].place.should.equal(1);
              res.body.judges[1].userId.should.equal(testUser2.id);
              res.body.judges[1].roomId.should.equal(res.body.room.id);
              res.body.judges[1].place.should.equal(2);
            })
            .end(done);
        });
      });
    });
  });
});

describe('Getting room details', function(){
  before(function(done) {
    apiHelper.resetDb(done);
  });

  it('should get a room', function(done){
    apiHelper.getTestUserAndRun(function(testUser){
      var roomData = {
        name: "test room",
        maxPlayers: 4
      };
      var roomId;
      request(app).post('/rooms')
        .set("Content-Type", "application/json")
        .set("Authorization", "Token "+testUser.fbToken)
        .send({"room": roomData})
        .expect('Content-Type', /json/)
        .expect(200)
        .expect(function(res){
          roomId = res.body.room.id;
        })
        .end(function(room) {
          request(app).get('/rooms/'+roomId)
            .expect('Content-Type', /json/)
            .expect(200)
            .expect(function(res){
              res.body.room.name.should.equal(roomData.name);
              res.body.room.maxPlayers.should.equal(roomData.maxPlayers);
              res.body.Users[0].fbId.should.equal('testFbId');
              res.body.Users[0].name.should.equal('testName');
              res.body.Users[0].pic.should.equal('testPic');
            })
            .end(done);
        });
    });
  });

  it('should list all rooms', function(done) {
    request(app).get('/rooms')
      .expect('Content-Type', /json/)
      .expect(200)
      .expect(function(res) {
        if(!('rooms' in res.body)) return "room array not present";
      })
      .end(done);
  });
});