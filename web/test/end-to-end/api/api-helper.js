var 
  assert = require('assert'),
  request = require('supertest'),
  models = require('../../../models'),
  app = require('../../../app'),
  fs = require('fs'),
  testUserId;

module.exports = {
  resetDb : function(done){
    if(fs.existsSync('db.test.sqlite')){
      fs.unlinkSync('db.test.sqlite');
    }
    models.sequelize.sync({logging: false}).complete(function(err) {
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
  }
}