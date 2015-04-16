var models  = require('../models');
var express = require('express');
var router  = express.Router();
var Promise = require("bluebird");
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
      order: '"createdAt" DESC'
    })
    .then(function(handEntries){
      var cards = [];
      Promise.each(handEntries, addCard)
      .finally(function(){
        res.json({
          success: true,
          cards: cards
        });
      })
      function addCard(handEntry){
        return new Promise(function(resolve, reject){
          models.Card.find(handEntry.cardId)
          .then(function(card){
            cards.push(card);
            resolve();
          })
        });
      }


    });
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });
});

module.exports = router;
