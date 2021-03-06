var models  = require('../models');
var authorize = require('../lib/authorization-middleware')().authorize;
var express = require('express');
var router  = express.Router();

//create a room
router.post('/', authorize, function(req, res) {
  if(req.authorizedUser.roomId !== null){
    res.json({success: false, errors: "User already in a room"});
  }else{
    models.Room.create(req.body.room)
    .then(function(room) {
      req.authorizedUser.updateAttributes({roomId: room.id})
      .then(function() {
        models.Host.create({userId: req.authorizedUser.id, roomId: room.id})
        .then(function(host){
          models.Judge.create({userId: req.authorizedUser.id, roomId: room.id, place: 1})
          .then(function(judge){
            models.User.findAll({ 
              where: {roomId: room.id},
              attributes: ['id', 'roomId', 'name', 'pic', 'fbId']
            })
            .then(function(users){
              res.json({
                success: true, 
                room: room,
                host: host,
                judges: [judge],
                Users: users
              });
            }).catch(function(errors){
              res.json({success: false, errors: errors});
            });
          })
          .catch(function(errors){
            res.json({success: false, errors: errors});
          });
        })
        .catch(function(errors){
          res.json({success: false, errors: errors});
        });
      })
      .catch(function(errors){
        res.json({success: false, errors: errors});
      });
    })
    .catch(function(errors){
      res.json({success: false, errors: errors});
    });
  }
});

//list all rooms
router.get('/', function(req, res) {
  models.Room.all({
    include:[{
      model: models.User,
      attributes: ['id', 'fbId', 'name', 'pic']
    }]
  })
  .then(function(rooms) {
    res.json({ rooms: rooms });
  });
});

//get a room
router.get('/:id', function(req, res) {
  models.Room.find({
    where: {id: req.params.id}
  })
  .then(function(room) {
    room.getUsers({
      attributes: ['id', 'fbId', 'name', 'pic']
    })
    .then(function(users){

      room.dataValues.Users = users;
      res.json({
        room: room
      });
    });
  });
});

//join room
//TODO - add game info if game in progress
router.post('/join/:id', authorize, function(req, res) {
  models.Room.find({
    where: {id: req.params.id}
  })
  .then(function(room){
    if(room === null){
      throw 'room does not exist';
    } else if(room.id === req.authorizedUser.roomId){
      throw 'user already in room';
    }
    room.getUsers()
    .then(function(users){
      if(users.length >= room.maxPlayers){
        throw 'room is full';
      }
      req.authorizedUser.updateAttributes({roomId: req.params.id})
      .then(function() {
        room.getHost()
        .then(function(host){
          room.getJudges(
            { order: 'place ASC' })
          .then(function(judges){
            models.Judge.create({
              userId: req.authorizedUser.id, 
              roomId: room.id, 
              place: judges[judges.length-1].place + 1
            })
            .then(function(judge){
              models.User.findAll({ 
                where: {roomId: room.id},
                attributes: ['id', 'roomId', 'name', 'pic', 'fbId']
              })
              .then(function(users){
                judges.push(judge);
                res.json({
                  success: true, 
                  room: room,
                  host: host,
                  judges: judges,
                  Users: users
                });
              }).catch(function(errors){
                res.json({success: false, errors: errors});
              });
            })
            .catch(function(errors){
              res.json({success: false, errors: errors});
            });
          })
          .catch(function(errors){
            res.json({success: false, errors: errors});
          });
        })
        .catch(function(errors){
          res.json({success: false, errors: errors});
        });
      })
      .catch(function(errors){
        res.json({success: false, errors: errors});
      });
    })
    .catch(function(errors){
      res.json({success: false, errors: errors});
    });
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });
});

//update a room
/*
router.put('/:id', authorize, function(req, res) {
  models.Room.find({
    where: {id: req.params.id}
  })
  .then(function(room) {
    room.updateAttributes(req.body.room)
    .then(function() {
      res.json({success: true, room: room});
    })
    .catch(function(error){
      res.json({success: false, error: error});
    });
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });
});

//delete a room
router.delete('/:id', authorize, function(req, res) {
  models.Room.find({
    where: {id: req.params.id}
  })
  .then(function(room) {
    room.destroy()
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
*/

module.exports = router;
