var 
  assert = require('assert'),
  should = require('should'),
  request = require('supertest'),
  fs = require('fs'),
  apiHelper = require('./api-helper'),
  models = require('../../../models'),
  app = require('../../../app');

describe('Creating a card', function() {
  before(function(done) {
    apiHelper.resetDb(done);
  });

  it('should create a card', function(done) {
    apiHelper.getTestUserAndRun(function(testUser){
      var cardData = {
        text: "test card", 
        type: "white",
        userId: testUser.id
      };

      request(app).post('/cards')
        .set("Content-Type", "application/json")
        .set("Authorization", "Token "+testUser.fbToken)
        .send({"card": cardData})
        .expect('Content-Type', /json/)
        .expect(200)
        .expect(function(res){
          res.body.card.text.should.equal(cardData.text);
          res.body.card.type.should.equal(cardData.type);
          res.body.card.userId.should.equal(cardData.userId);
        })
        .end(done);
    });
  });
});

describe('Getting card details', function(){
    before(function(done) {
      apiHelper.resetDb(done);
    });

    it('should get a card', function(done){
      apiHelper.getTestUserAndRun(function(testUser){
        var cardData = {
          text: "test card", 
          type: "white",
          userId: testUser.id
        };

        models.Card.create(cardData)
        .then(function(card) {
          request(app).get('/cards/'+card.id)
            .expect('Content-Type', /json/)
            .expect(200)
            .expect(function(res){
              res.body.card.text.should.equal(cardData.text);
              res.body.card.type.should.equal(cardData.type);
              res.body.card.userId.should.equal(cardData.userId);
            })
            .end(done);
        });
      });
    });

    it('should list all cards', function(done) {
      request(app).get('/cards')
        .expect('Content-Type', /json/)
        .expect(200)
        .expect(function(res) {
          if(!('cards' in res.body)) return "card array not present";
        })
        .end(done);
    });
});

describe('Deleting a card', function(){
  before(function(done) {
    apiHelper.resetDb(done);
  });
  
  it('should delete the card', function(done) {
    apiHelper.getTestUserAndRun(function(testUser){
      var cardData = {
        text: "test card", 
        type: "white",
        userId: testUser.id
      };

      models.Card.create(cardData)
      .then(function(card) {
        request(app).delete('/cards/'+card.id)
          .set("Content-Type", "application/json")
          .set("Authorization", "Token "+testUser.fbToken)
          .expect('Content-Type', /json/)
          .expect(200)
          .expect(function(res){
            res.body.success.should.equal(true);
          })
          .end(done);
      });
    });
  });
});