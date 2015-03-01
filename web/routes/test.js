var models  = require('../models');
var express = require('express');
var router  = express.Router();

//reset database
router.post('/reset', function(req, res) {
  models.sequelize.sync({force:true}).complete(function(err) {
    console.log("\n\n\nDATABASE RESET\n\n\n");
    res.json({error: err});
  });
});

module.exports = router;
