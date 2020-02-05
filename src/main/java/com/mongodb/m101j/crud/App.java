/*
 * Copyright 2015 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mongodb.m101j.crud;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class App {

	public static void main(String[] args) {
		
		// MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(10).build();
		MongoClientOptions options = MongoClientOptions.builder().build();
		
		MongoClient client = new MongoClient();
		
		/*
		 * MongoClient client = new MongoClient("localhost", 27017); 
		 * MongoClient client = new MongoClient(new ServerAddress("localhost", 27017));
		 * // If connecting to mongo clusters
		 * MongoClient client = new MongoClient(asList(new  ServerAddress("localhost", 27017)));
		 * MongoClient client = new MongoClient(new MongoClientURL("mongodb://localhost:27017)); 
		 * MongoClient client = new MongoClient(new ServerAddress(), options);
		 */
		MongoDatabase db = client.getDatabase("test");
		
		/*
		 * To change the configuration properties in the database.
		 * client.getDatabase("test").withCodecRegistry(arg0)
		 * client.getDatabase("test").withReadConcern(arg0);
		 * client.getDatabase("test").withReadPreference(arg0);
		 * client.getDatabase("test").withWriteConcern(arg0);
		 */
		// This also has methods to change the configurations
		MongoCollection<Document> collection = db.getCollection("test"); 
		
		System.out.println(options + " " + client + " " + collection);
		
		client.close();
	}

}
