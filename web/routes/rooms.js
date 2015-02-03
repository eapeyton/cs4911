var models  = require('../models');
var express = require('express');
var router  = express.Router();

//create a room
router.post('/', function(req, res) {
  models.Room.create(req.body.room).success(function(room) {
    res.json({ success: true, room: room });
  }).error(function(errors){
    res.json({success: false, errors: errors});
  });;
});

//list all rooms
router.get('/', function(req, res) {
  models.Room.all().then(function(rooms) {
    res.json({ rooms: rooms });
  });
});

//get a room
router.get('/:id', function(req, res) {
  models.Room.find({
    where: {id: req.params.id}
  })
  .then(function(room) {
    res.json({success: true, room: room});
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });
});

//update a room
router.put('/:id', function(req, res) {
  models.Room.find({
    where: {id: req.params.id}
  })
  .then(function(room) {
    room.updateAttributes(req.body.room)
    .then(function() {
      res.json({success: true, room: room});
    })
    .catch(function(errors){
      res.json({success: false, errors: errors});
    });;
  })
  .catch(function(errors){
    res.json({success: false, errors: errors});
  });
});

//delete a room
router.delete('/:id', function(req, res) {
  models.Room.find({
    where: {id: req.params.id}
  })
  .then(function(room) {
    room.destroy()
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
