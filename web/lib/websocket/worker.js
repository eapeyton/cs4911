CircularJSON = require('circular-json')

process.on('message', function(m) {
  console.log("CHILD DOING STUFF");
  var delay = m.delay;
  console.log('delay = ', delay);

  setTimeout(function(){
    console.log('child timeout done');
    process.send({ status: 'delay over' });
  }, delay);
});
