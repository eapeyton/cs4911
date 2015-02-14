var models  = require('../../models');
var fb = require('fb');

module.exports = {
  updateToken: function(user, newToken, res){
    user.updateAttributes({fbToken: newToken, roomId: null})
    .then(function() {
      res.json({success: true, user: user});
    })
    .catch(function(errors){
      res.json({success: false, errors: errors});
    });
  },

  createNewUser: function(req, res){
    models.User.create(req.body.user)
    .then(function(user) {
      res.json({ success: true, user: user });
    })
    .catch(function(errors){
      res.json({success: false, errors: errors});
    });
  }
}