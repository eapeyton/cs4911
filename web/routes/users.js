var models  = require('../models');
var fb = require('fb');
var express = require('express');
var router = express.Router();


//create a user
router.post('/', function(req, res) {
  models.User.create(req.body.user)
  .then(function(user) {
    res.json({ success: true, user: user });
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });;
});

//get all users
router.get('/', function(req, res, next) {
  models.User.all({attributes: ['id', 'rid', 'username']})
  .then(function(users) {
    res.json({ users: users });
  });
});

//get a user
router.get('/:id', function(req, res) {
  models.User.find({
    attributes: ['id', 'rid', 'username'],
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
    .catch(function(errors){
      res.json({success: false, errors: errors});
    });;
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });;
});

module.exports = router;
