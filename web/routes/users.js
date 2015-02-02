var fb = require('fb');
var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});

router.post('/auth', function(req, res, next) {
  fb.setAccessToken(req.body.token);
});

module.exports = router;
