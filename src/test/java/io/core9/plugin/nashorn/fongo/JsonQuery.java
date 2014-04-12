package io.core9.plugin.nashorn.fongo;



import java.net.UnknownHostException;
import java.util.HashMap;

import org.junit.Test;

import com.ee.dynamicmongoquery.MongoQuery;
import com.ee.dynamicmongoquery.MongoQueryParser;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class JsonQuery {

	@Test
	public void test() throws UnknownHostException {



		String query = "db.friends.find( { 'name' : 'John'} ).sort( { name: 1 } ).limit( 2 )";

		//FIXME needs real database fongo doesn't work!!
		MongoClient mongoClient = new MongoClient();
		DB db = mongoClient.getDB( "friends" );
		DBCollection collection = db.getCollection("friends");
		collection.insert(new BasicDBObject("name", "John"));
		collection.insert(new BasicDBObject("name", "Paul"));
		
		MongoQueryParser parser = new MongoQueryParser();
		MongoQuery mongoQuery = parser.parse(query, new HashMap<String, String>());
		BasicDBList results = mongoQuery.execute(db);
		
		String res = results.toString();
		System.out.println(res);
	}
}
