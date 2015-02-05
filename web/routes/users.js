var models  = require('../models');
var fb = require('fb');
var express = require('express');
var router = express.Router();
var userHelper = require('./helpers/user-helper');


//login
router.post('/login', function(req, res) {
  var token = req.body.user.token

  models.User.find({
    where: {fbid: req.body.user.fbid}
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
  models.User.all({attributes: ['id', 'rid', 'name', 'pic', 'fbid']})
  .then(function(users) {
    res.json({ users: users });
  });
});

//get a user
router.get('/:id', function(req, res) {
  models.User.find({
    attributes: ['id', 'rid', 'name', 'pic', 'fbid'],
    where: {id: req.params.id}
  })
  .then(function(user) {
    res.json({success: true, user: user});
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });
});

//delete a user
router.delete('/:id', function(req, res) {
  models.User.find({
    where: {id: req.params.id}
  })
  .then(function(user) {
    user.destroy()
    .then(function() {
      res.json({success: true});
    })
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });;
});

module.exports = router;
