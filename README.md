



[![Build Status](https://travis-ci.org/jessec/plugin-nashorn.png?branch=master)](https://travis-ci.org/jessec/plugin-nashorn)


Boilerplate for your js datahandler : 


var preDatabaseFilter = function(server) {
	return server;
};

var databaseQueries = function(preDatabaseData) {
	return {
				"Paul" : "db.friends.find( { 'name' : 'Paul'}, { _id: 0 }  ).sort( { name: 1 } ).limit( 2 )",
				"John" : "db.friends.find( { 'name' : 'John'}, { _id: 0 } ).sort( { name: 1 } ).limit( 2 )",
				"articles" : "db.core.Articles.find( { 'title' : 'test'} )"
			};
};

var postDatabaseFilter = function(databaseResults) {
	return databaseResults;
};