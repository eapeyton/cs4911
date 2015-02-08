var models = require('../models');
module.exports = AuthorizationMiddleware;

function AuthorizationMiddleware(){
  var self;
  self = {
    authorize: authorize
  };
  return self;

  function authorize(req, res, next){
    if(req.headers.authorization===null || req.headers.authorization.split(' ')[1]===undefined){
      send401(next);
    }else{
      var token = req.headers.authorization.split(' ')[1];
      models.User.find({
        where: {fbToken: token}
      })
      .then(function(user){
        if(user){
          req.authorizedUser = user;
          next();
        }else{
          send401(next);
        }
      });
    }
  }

  function send401(next){
    var err = new Error();
    err.status = 401;
    next(err);
  }
}