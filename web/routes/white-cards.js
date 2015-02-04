var models  = require('../models');
var express = require('express');
var router  = express.Router();

//create a white card
router.post('/', function(req, res) {
  models.WhiteCard.create(req.body.whiteCard).success(function(whiteCard) {
    res.json({ success: true, whiteCard: whiteCard });
  }).error(function(errors){
    res.json({success: false, errors: errors});
  });;
});

//list all white cards
router.get('/', function(req, res) {
  models.WhiteCard.all().then(function(whiteCards) {
    res.json({ whiteCards: whiteCards });
  });
});

//get a white card
router.get('/:id', function(req, res) {
  models.WhiteCard.find({
    where: {id: req.params.id}
  })
  .then(function(whiteCard) {
    res.json({success: true, whiteCard: whiteCard});
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });
});

//update a white card
router.put('/:id', function(req, res) {
  models.WhiteCard.find({
    where: {id: req.params.id}
  })
  .then(function(whiteCard) {
    whiteCard.updateAttributes(req.body.whiteCard)
    .then(function() {
      res.json({success: true, whiteCard: whiteCard});
    })
    .catch(function(errors){
      res.json({success: false, errors: errors});
    });;
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });
});

//delete a white card
router.delete('/:id', function(req, res) {
  models.WhiteCard.find({
    where: {id: req.params.id}
  })
  .then(function(whiteCard) {
    whiteCard.destroy()
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
