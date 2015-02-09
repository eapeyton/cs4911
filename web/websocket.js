var WebSocketHandler = function(io) {
  io.on('connection', function(socket) {
    socket.emit('message', 'hello world');
    
    socket.on('join room', function(msg) {
      // attach socket to room id
    });
    socket.on('start game', function(msg) {
      // deal hands
      // draw blank
    });
    socket.on('play',function(msg) {
      // update state to judgement if applicable
    });
    socket.on('timeout', function(msg) {
      // update state to judgement if applicable
    });
    socket.on('judgement', function(msg) {
      // move to end game if applicable
      // else update the hand count and draw new cards
      // draw blank
    });

  });
}

module.exports = WebSocketHandler; 
