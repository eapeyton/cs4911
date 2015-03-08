setTimeout(function() {
  console.log('finished doing some long task');
  process.send({ status: 'done' });
}, 3000); // set this to something larger than 5000ms to see the timeout