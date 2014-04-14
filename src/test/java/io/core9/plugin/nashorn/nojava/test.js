/**
 * 
 */

var preDatabaseFilter = function(server) {
	return "greetings from javascript to " + server.request.path;
};

var databaseQueries = function(preDatabaseData) {
	return [
			{
				"Paul" : "db.friends.find( { 'name' : 'Paul'} ).sort( { name: 1 } ).limit( 2 )"
			},
			{
				"John" : "db.friends.find( { 'name' : 'John'} ).sort( { name: 1 } ).limit( 2 )"
			} ];
};

var postDatabaseFilter = function(databaseResults) {

	var arrayLength = databaseResults.length;
	for (var i = 0; i < arrayLength; i++) {
		if (i == 0) {
			var res = databaseResults[i];
			return res.Paul.name;
		}
	}
	return databaseResults;
};