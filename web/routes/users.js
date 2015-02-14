var models  = require('../models');
var authorize = require('../lib/authorization-middleware')().authorize;
var fb = require('fb');
var express = require('express');
var router = express.Router();
var userHelper = require('./helpers/user-helper');


//login
router.post('/login', function(req, res) {
  var token = req.body.user.fbToken

  models.User.find({
    where: {fbId: req.body.user.fbId}
  })
  .then(function(user){
    if(user!==null){
      userHelper.updateToken(user, token, res);
    }else{
      userHelper.createNewUser(req, res);
    }
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });
});

//get all users
router.get('/', function(req, res, next) {
  models.User.all({attributes: ['id', 'roomId', 'name', 'pic', 'fbId']})
  .then(function(users) {
    res.json({ users: users });
  });
});

//get a user
router.get('/:id', function(req, res) {
  models.User.find({
    attributes: ['id', 'roomId', 'name', 'pic', 'fbId'],
    where: {id: req.params.id}
  })
  .then(function(user) {
    res.json({user: user});
  });
});

//delete a user
router.delete('/:id', authorize, function(req, res) {
  models.User.find({
    where: {id: req.params.id}
  })
  .then(function(user) {
    if(req.authorizedUser.id !== user.id){
      throw "Token and user's id don't match";
    }
    user.destroy()
    .then(function() {
      res.json({success: true});
    })
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });
});

module.exports = router;
