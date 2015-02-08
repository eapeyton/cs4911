var models  = require('../models');
var authorize = require('../lib/authorization-middleware')().authorize;
var express = require('express');
var router  = express.Router();

//create a card
router.post('/', authorize, function(req, res) {
  if(req.authorizedUser.id !== req.body.card.uid){
    res.json({success: false, errors: "Token and card's uid don't match"});
  }else{
    models.Card.create(req.body.card)
    .then(function(card) {
      res.json({ success: true, card: card });
    })
    .catch(function(errors){
      res.json({success: false, errors: errors});
    });
  }
});

//list all cards
router.get('/', function(req, res) {
  models.Card.all().then(function(cards) {
    res.json({ cards: cards });
  });
});

//get a card
router.get('/:id', function(req, res) {
  models.Card.find({
    where: {id: req.params.id}
  })
  .then(function(card) {
    res.json({card: card});
  });
});

//update a card
/*
router.put('/:id', authorize, function(req, res) {
  models.Card.find({
    where: {id: req.params.id}
  })
  .then(function(card) {
    if(req.authorizedUser.id !== card.uid){
      throw "Token and card's uid don't match";
    }
    card.updateAttributes(req.body.card)
    .then(function() {
      res.json({success: true, card: card});
    })
    .catch(function(error){
      res.json({success: false, error: error});
    });
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });
});
*/
//delete a card
router.delete('/:id', authorize, function(req, res) {
  models.Card.find({
    where: {id: req.params.id}
  })
  .then(function(card) {
    if(req.authorizedUser.id !== card.uid){
      throw "Token and card's uid don't match";
    }
    card.destroy()
    .then(function() {
      res.json({success: true});
    })
    .catch(function(error){
      res.json({success: false, error: error});
    });
  }).catch(function(errors){
    res.json({success: false, errors: errors});
  });
});


module.exports = router;
