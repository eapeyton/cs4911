var models  = require('../models');
var express = require('express');
var router  = express.Router();

//create a black card
router.post('/', function(req, res) {
  models.BlackCard.create(req.body.blackCard).success(function(blackCard) {
    res.json({ success: true, blackCard: blackCard });
  }).error(function(errors){
    res.json({success: false, errors: errors});
  });;
});

//list all black cards
router.get('/', function(req, res) {
  models.BlackCard.all().then(function(blackCards) {
    res.json({ blackCards: blackCards });
  });
});

//get a black card
router.get('/:id', function(req, res) {
  models.BlackCard.find({
    where: {id: req.params.id}
  })
  .then(function(blackCard) {
    res.json({success: true, blackCard: blackCard});
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });
});

//update a black card
router.put('/:id', function(req, res) {
  models.BlackCard.find({
    where: {id: req.params.id}
  })
  .then(function(blackCard) {
    blackCard.updateAttributes(req.body.blackCard)
    .then(function() {
      res.json({success: true, blackCard: blackCard});
    })
    .catch(function(errors){
      res.json({success: false, errors: errors});
    });;
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });
});

//delete a black card
router.delete('/:id', function(req, res) {
  models.BlackCard.find({
    where: {id: req.params.id}
  })
  .then(function(blackCard) {
    blackCard.destroy()
    .then(function() {
      res.json({success: true});
    })
    .catch(function(errors){
      res.json({success: false, errors: errors});
    });;
  }).catch(function(errors){
    res.json({success: false, errors: errors});
  });;
});


module.exports = router;
