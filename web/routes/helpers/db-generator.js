var randomWords = require('random-words');

module.exports = {
  getTestCards: function(){
    var cards = [];
    for(var i=0; i<50; i++){
      cards.push({
        text: (randomWords({ min: 1, max: 3 })+" _ "+randomWords({ min: 1, max: 3 })).split(",").join(" "),
        type: "black"
      });
    }

    for(var i=0; i<100; i++){
      cards.push({
        text: randomWords(),
        type: "white"
      });
    }

    return cards;
  }
}