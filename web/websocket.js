var WebSocketHandler = function(io) {
  io.on('connection', function(socket) {
    socket.emit('message', 'hello world');

    socket.on('ping', function() {
      socket.emit('pong');
    });
  });
}

module.exports = WebSocketHandler; 
