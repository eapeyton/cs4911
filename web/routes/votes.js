var models  = require('../models');
var authorize = require('../lib/authorization-middleware')().authorize;
var express = require('express');
var router  = express.Router();

//create a vote
router.post('/', authorize, function(req, res) {
  models.Vote.find({
    where: {
      userId: req.body.vote.userId,
      cardId: req.body.vote.cardId
    }
  })
  .then(function(oldVote){
    if (oldVote !== null) {
      throw "Vote already exists for this card and user";
    }else if(req.authorizedUser.id !== req.body.vote.userId){
      throw "Token and vote's userId don't match";
    }else{
      models.Vote.create(req.body.vote)
      .then(function(vote) {
        res.json({ success: true, vote: vote });
      })
      .catch(function(error){
        res.json({success: false, error: error});
      });
    }
  })
  .catch(function(error){
    res.json({success: false, error: error});
  });
});

//list all votes
router.get('/', function(req, res) {
  models.Vote.all()
  .then(function(votes) {
    res.json({ votes: votes });
  });
});

//get a vote
router.get('/:id', function(req, res) {
  models.Vote.find({
    where: {id: req.params.id}
  })
  .then(function(vote) {
    res.json({vote: vote});
  });
});

//update a vote
router.put('/:id', authorize, function(req, res) {
  models.Vote.find({
    where: {id: req.params.id}
  })
  .then(function(vote) {
    if(req.authorizedUser.id !== vote.userId){
      throw "Token and vote's userId don't match";
    }
    vote.updateAttributes({upvoted: req.body.vote.upvoted})
    .then(function() {
      res.json({success: true, vote: vote});
    })
    .catch(function(error){
      res.json({success: false, error: error});
    });
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });
});

//delete a vote
router.delete('/:id', authorize, function(req, res) {
  models.Vote.find({
    where: {id: req.params.id}
  })
  .then(function(vote) {
    if(req.authorizedUser.id !== vote.userId){
      throw "Token and vote's userId don't match";
    }
    vote.destroy()
    .then(function() {
      res.json({success: true});
    })
    .catch(function(error){
      res.json({success: false, error: error});
    });
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });
});

module.exports = router;
