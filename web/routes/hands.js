var models  = require('../models');
var express = require('express');
var router  = express.Router();
var authorize = require('../lib/authorization-middleware')().authorize;


//get cards in users hand
router.get('/', authorize, function(req, res) {
  models.Game.find({where:
    {
      roomId: req.authorizedUser.roomId,
      finishTime: null
    }
  })
  .then(function(game){
    if(game === null){
      throw "user not in game"
    }
    models.Hand.findAll({
      where: {
        userId: req.authorizedUser.id,
        gameId: game.id,
        played: false
      },
      order: [['"createdAt"', 'DESC']],
      include:[{
        model: models.Card
      }]
    })
    .then(function(hand){
      var cards = [];
      for(var i=0; i<hand.length; i++){
        cards.push(hand[i].Card);
      }
      res.json({
        success: true,
        cards: cards
      });
    });
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });
});

module.exports = router;
