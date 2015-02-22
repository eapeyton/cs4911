var 
  assert = require('assert'),
  should = require('should'),
  request = require('supertest'),
  fs = require('fs'),
  apiHelper = require('./api-helper'),
  models = require('../../../models'),
  app = require('../../../app');

describe('Creating a vote', function() {
  before(function(done) {
    apiHelper.resetDb(done);
  });

  it('should create a vote', function(done) {
    apiHelper.getTestUserAndRun(function(testUser){
      apiHelper.createSecondUserAndRun(function(testUser2){
        var cardId;
        var cardData = {
          text: "test card", 
          type: "white",
          userId: testUser.id
        };

        request(app).post('/cards')
          .set("Content-Type", "application/json")
          .set("Authorization", "Token "+testUser.fbToken)
          .send({"card": cardData})
          .expect(function(res){
            cardId = res.body.card.id;
          })
          .end(function(){
            var voteData = {
              upvoted: true,
              userId: testUser2.id,
              cardId: cardId
            }
            request(app).post('/votes')
              .set("Content-Type", "application/json")
              .set("Authorization", "Token "+testUser2.fbToken)
              .send({"vote": voteData})
              .expect(function(res){
                res.body.success.should.equal(true);
                res.body.vote.upvoted.should.equal(voteData.upvoted);
                res.body.vote.cardId.should.equal(voteData.cardId);
                res.body.vote.userId.should.equal(voteData.userId);
              })
              .end(done);
          });
      });
    });
  });
});

describe('Getting vote details', function(){
  before(function(done) {
    apiHelper.resetDb(done);
  });

  it('should get a vote', function(done){
    apiHelper.getTestUserAndRun(function(testUser){
      apiHelper.createSecondUserAndRun(function(testUser2){
        var 
          cardId,
          voteId,
          cardData = {
            text: "test card", 
            type: "white",
            userId: testUser.id
          };

        request(app).post('/cards')
          .set("Content-Type", "application/json")
          .set("Authorization", "Token "+testUser.fbToken)
          .send({"card": cardData})
          .expect(function(res){
            cardId = res.body.card.id;
          })
          .end(function(){
            var voteData = {
              upvoted: true,
              userId: testUser2.id,
              cardId: cardId
            }
            request(app).post('/votes')
              .set("Content-Type", "application/json")
              .set("Authorization", "Token "+testUser2.fbToken)
              .send({"vote": voteData})
              .expect(function(res){
                voteId = res.body.vote.id;
              })
              .end(function(){
                request(app).get('/votes/'+voteId)
                  .send({"vote": {upvoted: false}})
                  .expect(function(res){
                    res.body.vote.upvoted.should.equal(voteData.upvoted);
                    res.body.vote.cardId.should.equal(voteData.cardId);
                    res.body.vote.userId.should.equal(voteData.userId);
                  })
                  .end(done);
              });
          });
        });
      });
    });

    it('should list all votes', function(done) {
      request(app).get('/votes')
        .expect('Content-Type', /json/)
        .expect(200)
        .expect(function(res) {
          if(!('votes' in res.body)) return "votes array not present";
        })
        .end(done);
    });
});

describe('Updating a vote', function(){
  before(function(done) {
    apiHelper.resetDb(done);
  });

  it('should update a vote', function(done) {
    apiHelper.getTestUserAndRun(function(testUser){
      apiHelper.createSecondUserAndRun(function(testUser2){
        var 
          cardId,
          voteId,
          cardData = {
            text: "test card", 
            type: "white",
            userId: testUser.id
          };

        request(app).post('/cards')
          .set("Content-Type", "application/json")
          .set("Authorization", "Token "+testUser.fbToken)
          .send({"card": cardData})
          .expect(function(res){
            cardId = res.body.card.id;
          })
          .end(function(){
            var voteData = {
              upvoted: true,
              userId: testUser2.id,
              cardId: cardId
            }
            request(app).post('/votes')
              .set("Content-Type", "application/json")
              .set("Authorization", "Token "+testUser2.fbToken)
              .send({"vote": voteData})
              .expect(function(res){
                voteId = res.body.vote.id;
              })
              .end(function(){
                request(app).put('/votes/'+voteId)
                  .set("Content-Type", "application/json")
                  .set("Authorization", "Token "+testUser2.fbToken)
                  .send({"vote": {upvoted: false}})
                  .expect(function(res){
                    res.body.vote.upvoted.should.equal(false);
                  })
                  .end(done);
              });
          });
      });
    });
  });
});

describe('Deleting a vote', function(){
  before(function(done) {
    apiHelper.resetDb(done);
  });
  
  it('should delete the vote', function(done) {
    apiHelper.getTestUserAndRun(function(testUser){
      apiHelper.createSecondUserAndRun(function(testUser2){
        var 
          cardId,
          voteId,
          cardData = {
            text: "test card", 
            type: "white",
            userId: testUser.id
          };

        request(app).post('/cards')
          .set("Content-Type", "application/json")
          .set("Authorization", "Token "+testUser.fbToken)
          .send({"card": cardData})
          .expect(function(res){
            cardId = res.body.card.id;
          })
          .end(function(){
            var voteData = {
              upvoted: true,
              userId: testUser2.id,
              cardId: cardId
            }
            request(app).post('/votes')
              .set("Content-Type", "application/json")
              .set("Authorization", "Token "+testUser2.fbToken)
              .send({"vote": voteData})
              .expect(function(res){
                voteId = res.body.vote.id;
              })
              .end(function(){
                request(app).delete('/votes/'+voteId)
                  .set("Content-Type", "application/json")
                  .set("Authorization", "Token "+testUser2.fbToken)
                  .send()
                  .expect(function(res){
                    res.body.success.should.equal(true);
                  })
                  .end(done);
              });
          });
      });
    });
  });
});