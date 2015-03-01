var WebSocketHandler = function(io) {
  io.on('connection', function(socket) {
    socket.emit('message', 'hello world');

    socket.on('setup socket for user', function(user){
      socket.roomId = user.roomId;
      socket.join(user.roomId);
      var newUser = {
        id: user.id,
        name: user.name,
        pic: user.pic
      }
      socket.broadcast.to(socket.roomId).emit('user joined', newUser);
      socket.emit('user joined', newUser);
    });
    
    socket.on('start game', function(msg) {
      socket.broadcast.to(socket.roomId).emit('host started game', "started in room-"+socket.roomId);
      socket.emit('host started game', "started in room-"+socket.roomId);
      /*
        -get first Judge
        -get a Black Card
        -create Game
        -create Hand for each user
        -create Round, state "waiting for players"
        -create players' PlayerState with state "playing"
        -create judge's PlayerState with state "waiting for players"
        -broadcast  "new game" state with hand ids, black card, and who the judge is. (front end will get players cards through hand ids with token)
      */
    });
    socket.on('play card', function(msg) {
      /*
        -Create PlayedCard
        -update Hand entry to played=true
        -update PlayerState to "waiting for players"
        -check if all players are waiting
         
        If all players are waiting
        -move to play time up
        else
        -just broadcast that this player played a card
      */
    });

    socket.on('time up for card playing',function(msg) {
      // check play time up
      /*
        -update Round State to "waiting for judge"
        -update players' PlayerState to "waiting for judge"
        -update judge's PlayerState to "judging"
        -broadcast "waiting for judge" state with the played cards for the round
      */
    });

    socket.on('choose winning card', function(msg) {
      /*
      //check if judge
      if game not over
        - update last Round with winner info and state ="over"
        - update all PlayerStates so state="round review"
        broadcast "round review" state with winner

      else ther is a game winner

        - update last Round with winner info and state ="over"
        - update users' PlayerStates so state="game review"
        - update Game finnishTime
        broadcast "game review" state with winner and stats
      */
    });

    socket.on('time up for judge', function(msg) {
      // check if judge time up
      // do judge chose with random
    });

    socket.on('time up for round review', function(msg) {
      /*
        //check if round review time up
        -add card to needed Hands (if a user in the game has less than 7 cards, add a card)
        -get next Judge
        -get a Black Card
        -create Round, state "waiting for players"
        -update players' PlayerState to "playing"
        -update judge's PlayerState to "waiting for players"
        -broadcast "new round" state with black card and who the judge is (front end will recall Hand to get updated cards)
      */
    });

    socket.on('time up for game review', function(msg) {
      // check if game review time up
      /*
        -broadcast "pre-game" state to users
      */
    });

  });
}

module.exports = WebSocketHandler; 
