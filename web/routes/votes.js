var models  = require('../models');
var express = require('express');
var router  = express.Router();

//create a vote
//todo check body.uid and token match
router.post('/', function(req, res) {
  models.Vote.find({
    where: {
      uid: req.body.vote.uid,
      cid: req.body.vote.cid
    }
  })
  .then(function(oldVote){
    if (oldVote !== null) {
      throw "Vote already exists for this card and user";
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
router.put('/:id', function(req, res) {
  models.Vote.find({
    where: {id: req.params.id}
  })
  .then(function(vote) {
    // todo check vote.uuid and token match
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
//todo check token and vote.uid match
router.delete('/:id', function(req, res) {
  models.Vote.find({
    where: {id: req.params.id}
  })
  .then(function(vote) {
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
