var models  = require('../models');
var express = require('express');
var router  = express.Router();
var dbGenerator = require('./helpers/db-generator');

//reset database
router.post('/reset', function(req, res) {
  models.sequelize.sync({force:true}).complete(function(err) {
    console.log("\n\n\nDATABASE RESET\n\n\n");
    res.json({error: err});
  });
});


//create cards
router.post('/cards', function(req, res) {
  models.Card.bulkCreate(dbGenerator.getTestCards())
  .then(function(cards){
    res.json({cards: cards});
  });
});

module.exports = router;
