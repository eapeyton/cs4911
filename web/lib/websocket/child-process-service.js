module.exports = ChildProcessService;

var
  Promise = require('bluebird'),
  models = require('../../models'),
  cp = require('child_process'),
  CircularJSON = require('circular-json');

function ChildProcessService(){

}


ChildProcessService.prototype.sendMsgWithDelay = function(msgKey, msgBody, socket, delay){
  var child = cp.fork(__dirname + '/worker');
  child.send({
    delay: delay
  });

  child.on('message', function(m) {
    socket.broadcast.to(socket.roomId).emit(msgKey, msgBody);
    socket.emit('new round', msgBody);
  });
}