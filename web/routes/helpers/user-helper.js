var models  = require('../../models');
var fb = require('fb');

module.exports = {
  updateToken: function(user, newToken, res){
    console.log("user=", user)
    user.updateAttributes({fbToken: newToken})
    .then(function() {
      res.json({success: true, user: user});
    })
  },

  createNewUser: function(req, res){
    models.User.create(req.body.user)
    .then(function(user) {
      res.json({ success: true, user: user });
    })
  }
}