var models  = require('../models');
var express = require('express');
var bodyParser = require('body-parser')
var router  = express.Router();

//create a room
router.post('/', function(req, res) {
  models.Room.create(req.body.room).then(function() {
    res.json({ success: true, room: req.body.room });
  });
});

//list all rooms
router.get('/', function(req, res) {
  models.Room.all().then(function(rooms) {
    res.json({ rooms: rooms });
  });
});

//update a room
router.put('/:room_id', function(req, res) {
  models.Room.find({
    where: {id: req.param('room_id')}
  }).then(function(room) {
    room.updateAttributes(req.body.room).then(function() {
      res.json({success: true, room: room});
    });
  });
});

//delete a room
router.delete('/:room_id', function(req, res) {
  models.Room.find({
    where: {id: req.param('room_id')}
  }).then(function(room) {
    room.destroy().then(function() {
      res.json({success: true});
    });
  });
});


module.exports = router;
