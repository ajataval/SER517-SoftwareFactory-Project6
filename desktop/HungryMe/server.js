/**
 * http://usejsdoc.org/
 */
var port = 8080;
var express = require('express');
var bodyParser = require('body-parser');
var app = express();
//connect to MongoDB and setup the user collection
var MongoClient = require('mongodb').MongoClient;
var assert = require('assert');
var ObjectId = require('mongodb').ObjectID;
var url = 'mongodb://localhost:27017/HungryMe';

app.use(bodyParser.json()); 

var insertAppUser = function(db, user, callback) {
	   db.collection('User').insertOne( user , function(err, result) {
	    assert.equal(err, null);
	    console.log("Inserted a document into the User collection.");
	    callback();
	  });
	};
	
app.get('/', function(req, res) {
    res.send('HungryMe Home');
});


app.post('/app/user',function(req, res){
	console.log("SignUp App User here");
	var app_user = req.body;
	MongoClient.connect(url, function(err, db) {
		  assert.equal(null, err);
		  insertAppUser(db,app_user, function() {
		      db.close();
		  });
		});
	
	res.status(201);
	res.send();
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

app.post('/hotel/user',function(req, res){
	console.log("SignUp Hotel User here");
	var hotel_user = req.body;
	MongoClient.connect(url, function(err, db) {
		  assert.equal(null, err);
		  insertAppUser(db,hotel_user, function() {
		      db.close();
		  });
	});
	res.status(201);
	res.send();
});

app.listen(port);

console.log('Server running at http://localhost:' + port);