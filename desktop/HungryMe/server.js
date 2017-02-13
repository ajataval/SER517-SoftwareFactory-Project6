/**
 * http://usejsdoc.org/
 */
var port = 8080;
var express = require('express');
var app = express();
//connect to MongoDB and setup the user collection

app.get('/', function(req, res) {
    res.send('HungryMe Home');
});

app.post('/hotel',function(req, res){
	console.log("Create a new hotel here");
	res.status(201);
	res.send();
});

app.get('/hotel',function(req, res){
	console.log("Get Hotel From MongoDB and return the JSON object using Restaurant Model");
	res.status(200);
	res.send();
});

app.post('/user/app',function(req, res){
	console.log("SignUp App User here");
	res.status(201);
	res.send();
});

app.post('/user/hotel',function(req, res){
	console.log("SignUp Hotel User here");
	res.status(201);
	res.send();
});

app.listen(port);

console.log('Server running at http://localhost:' + port);