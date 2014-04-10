package io.core9.plugin.nashorn.fongo;

import static org.junit.Assert.*;


import org.junit.Test;

import com.github.fakemongo.Fongo;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class JsonQuery {

	@Test
	public void test() {
		Fongo fongo = new Fongo("mongo server 1");
		DB db = fongo.getDB("mydb");
		DBCollection collection = db.getCollection("friends");
		collection.insert(new BasicDBObject("name", "jon"));

	
		
		String query = " 	{ 											" +
					   " 		\"$query\": {							" + 
					   " 			\"publisher\": \"Stephane\"			" +
			  		   " 		\"},									" +
			  		   " 		\"$orderby\": {							" +
			  		   "			\"creationDate\": 1				 	" +
			  		   " 		} 										" +
					   "	} 											";
		
		
		
		System.out.println(query);
		
	
	}
}
