var 
  assert = require('assert'),
  request = require('supertest'),
  models = require('../../../models'),
  app = require('../../../app'),
  fs = require('fs'),
  testUserId;

module.exports = {
  resetDb : function(done){
    models.sequelize.sync({force:true, logging:false}).complete(function(err) {
      if(err) done(err);
      models.User.create({
        fbToken: "testFbToken", 
        fbId: "testFbId",
        pic: "testPic",
        name: "testName"
      })
      .then(function(user) {
        testUserId = user.id;
        done();
      });
    });
  },

  getTestUserAndRun: function(callback){
    var testUser;
    models.User.find({
      where: {id: testUserId},
      logging: false
    })
    .then(function(user) {
      user = user.dataValues;
      user.updatedAt = user.updatedAt.toJSON();
      user.createdAt = user.createdAt.toJSON();
      callback(user);
    });
  },
  createSecondUserAndRun: function(callback){
    models.User.create({
      fbToken: "testFbToken2", 
      fbId: "testFbId2",
      pic: "testPic2",
      name: "testName2"
    })
    .then(function(user) {
      testUserId = user.id;
      callback(user);
    });
  }
}