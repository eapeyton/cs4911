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
        res.json({success: true, room: room, user: req.authorizedUser});
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
  models.Room.all()
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
    res.json({room: room});
  });
});

//join room
router.put('/join/:id', authorize, function(req, res) {
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
        res.json({success: true, user: req.authorizedUser});
      })
      .catch(function(error){
        res.json({success: false, error: error});
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
