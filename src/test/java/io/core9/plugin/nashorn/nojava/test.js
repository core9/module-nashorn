/**
 * 
 */
var databaseResults = [];
var server = {};

var preDatabase = function(name) {
    return "greetings from javascript to " + name + " : " + server.path;
};

var databaseQueries = [
		{
			"Paul" : "db.friends.find( { 'name' : 'Paul'} ).sort( { name: 1 } ).limit( 2 )"
		},
		{
			"John" : "db.friends.find( { 'name' : 'John'} ).sort( { name: 1 } ).limit( 2 )"
		} ];


var postDatabase = function() {
};