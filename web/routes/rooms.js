var models  = require('../models');
var express = require('express');
var bodyParser = require('body-parser')
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
router.get('/:room_id', function(req, res) {
  models.Room.find({
    where: {id: req.param('room_id')}
  }).success(function(room) {
    res.json({success: true, room: room});
  }).error(function(errors){
    res.json({success: false, errors: errors});
  });
});

//update a room
router.put('/:room_id', function(req, res) {
  models.Room.find({
    where: {id: req.param('room_id')}
  }).success(function(room) {
    room.updateAttributes(req.body.room).success(function() {
      res.json({success: true, room: room});
    }).error(function(errors){
      res.json({success: false, errors: errors});
    });;
  }).error(function(errors){
    res.json({success: false, errors: errors});
  });
});

//delete a room
router.delete('/:room_id', function(req, res) {
  models.Room.find({
    where: {id: req.param('room_id')}
  }).success(function(room) {
    room.destroy().success(function() {
      res.json({success: true});
    }).error(function(errors){
      res.json({success: false, errors: errors});
    });;
  }).error(function(errors){
    res.json({success: false, errors: errors});
  });;
});


module.exports = router;
