process.env.NODE_ENV = 'test';
var 
  assert = require('assert'),
  request = require('supertest'),
  fs = require('fs'),
  apiHelper = require('./api-helper'),
  app = require('../../../app');

before(function(done) {
  apiHelper.resetDb(done);
});

describe('User', function() {
  it('should create a new user', function(done) {
    var userData = {
      "fbToken": "createTestToken", 
      "fbId": "createTestFbId",
      "pic": "www.sicpic.com",
      "name": "createTest"
    };

    request(app).post('/users/login')
      .send({"user": userData})
      .expect('Content-Type', /json/)
      .expect(200)
      .expect(function(res) {
        if(!(res.body.success)) return "success was false";
        for (var key in userData) {
          if (userData.hasOwnProperty(key)) {
            if(!res.body.user.hasOwnProperty(key) || res.body.user[key] !== userData[key]){
              return "user's " + key + " data not saved properly";
            }
          }
        }
      })
      .end(done);
  });

  it('should login test user', function(done) {
    apiHelper.getTestUserAndRun(function(testUser){
      var loginData = {
        "fbToken": testUser.fbToken, 
        "fbId": testUser.fbId
      };

      request(app).post('/users/login')
        .send({"user": loginData})
        .expect('Content-Type', /json/)
        .expect(200)
        .expect(function(res) {
          if(!(res.body.success)) return "success was false";
          for (var key in res.body.user) {
            if (res.body.user.hasOwnProperty(key) && key !== "updatedAt") {
              if(key === "updatedAt" && testUser[key] === res.body.user[key]){
                return "user did not update on login"
              }else if(!testUser.hasOwnProperty(key) || testUser[key] !== res.body.user[key]){
                return "incorrect user data for "+key;
              }
            }
          }
        })
        .end(done);
    });
  });

  it('should get a user', function(done){
    apiHelper.getTestUserAndRun(function(testUser){
      request(app).get('/users/'+testUser.id)
        .expect('Content-Type', /json/)
        .expect(200)
        .expect(function(res) {
          if(res.body.user.id !== testUser.id) return "got the wrong user";
        })
        .end(done);
    });
  });

  it('should list users', function(done) {
    request(app).get('/users')
      .expect('Content-Type', /json/)
      .expect(200)
      .expect(function(res) {
        if(!('users' in res.body)) return "user array not present";
      })
      .end(done);
  });
});