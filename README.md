1.	To view the mongoDB commands in command prompt:
	help

2. Object ID: _id
	----/---/--/--- : 12-byte Hex string
	date(in seconds)/mac address(machine on which mongoDB is running)/process id/counter(to make the id unique).
	_id can be a document in itself. db.collection_name.insert({_id: {name: "value", class:"classname"}, hometown: "value"});

3. To retrieve data from DB:
	db.collection_name.find() OR db.collection_name.find({}) 
	// find method returns a cursor that iterate by itself for 20 times. 
	// Batch size is always less than BSON size. Batch size returned will 101 documents or documents equal to 1MB, subsequent batches will be 4 MB.

4. To prettify the data from DB:
	db.collection_name.find().pretty() OR db.collection_name.find({}).pretty()

5. To specify a particular criteria to fetch a particular record:
	db.collection_name.find({"key": "value"}).pretty() // If the value is number, then remove the double quotes.
	db.collection_name.find({"key1": "value"}, {"key2": "value").pretty() // This will be an AND condition.
	db.collection_name.find({"tomato.meter": 100}).pretty(); // This can be done.
	db.collection_name.find({rating: "PG-13"}).pretty(); // This can be done.
	db.collection_name.find({tomato.meter: 100}).count(); // This cannot be done.
	db.collection_name.find({"writers": ["name1", "name2"]}).count(); // Matches absolutely with only these 2 values and the same order.
	db.collection_name.find({"writers": "name1"}).count(); // Matches if the value is in any array(size).
	db.collection_name.find({"writers.0": "name1"}).pretty(); // Matches if the value is in any array(size) at the first position.

6. To save the data returned by find():
	var variable_name = db.collection_name.find(); // The dataype of the variable_name is cursor.

7. To check if the variable_name has data:
	variable_name.hasNext(); // This returns true or false.

8. To iterate over the values in variable_name:
	variable_name.next(); // Type 'it' to iterate over the cursor.
	
9. To create a function
	var c = db.collection_name.find();
	c.objsLeftInBatch(); // To get the count of records in Batch
	var doc = function(){return c.hasNext() ? c.next() : null;} // doc can be called now to iterate instead of 'it'.
	db.collection_name.find({rated: "PG"}, {title: 1}).pretty(); // Only _id and title will be returned.
	db.collection_name.find({rated: "PG"}, {title: 1, _id: 0}).pretty(); // Only title will be returned.
	// projection reduces size of the data returned. It reduces network overhead and processing requirements.

10. To insert one record in a DB: 
	db.collection_name.insertOne({
		"key1": "value",
		"key2": "value"
	});
	>>db.moviesScratch.insertOne({ "title": "Rocky", "year": "1976", "imdb": "tt0075148"});
	>>db.moviesScratch.insertOne({ "_id": "tt0075148", "title": "Rocky", "year": "1976" });

11. To insert many records in a DB:
	11.1. For unordered data
			db.collection_name.insertMany([
				{
				"_id" : "tt0084726",
				"title" : "Star Trek II: The Wrath of Khan",
				"year" : 1982,
				"type" : "movie"
				},
				{
				"_id" : "tt0796366",
				"title" : "Star Trek",
				"year" : 2009,
				"type" : "movie"
				}
			],
			{
				"ordered": false 
			});
	11.2. For ordered data
			db.collection_name.insertMany([
				{
				"_id" : "tt0084726",
				"title" : "Star Trek II: The Wrath of Khan",
				"year" : 1982,
				"type" : "movie"
				},
				{
				"_id" : "tt0796366",
				"title" : "Star Trek",
				"year" : 2009,
				"type" : "movie"
				}
			]);

12. Comparison operators:
	db.collection_name.find({runtime: {$gt: 90, $lt: 120}}).pretty();// greater than and less than
	db.collection_name.find({runtime: {$gte: 90, $lte: 120}}).pretty(); // greater than or equal to and less than or equal to
	db.collection_name.find({rated: {$ne: "UNRATED"}}).count(); // not equal to - returns empty or null value also - in mongoDB, the field only will not be stored
	db.collection_name.find({runtime: {$in: ["G", "PG"]}}).pretty(); // in a given array
	db.collection_name.find({runtime: {$nin: ["G", "PG"]}}).pretty(); // not in a given array
	
13. Element operators:
	db.collection_name.find({"tomato.meter": {$exists: true}}).pretty(); // if the tomato.meter exists
	db.collection_name.find({"_id": {$type: "string"}}).pretty(); // where _id is a string value
	
14. Logical operators:
	db.collection_name.find({$or: [{"tomato.meter": {$gt: 90}}, {"metacritic": {$gt: 85}}]}).pretty();
	db.collection_name.find({$and: [{"metacritic": {$ne: null}}, {"metacritic": {$exists: true}}]}).pretty(); // Genreally used for same attribute
	
15. Regex operators:
	db.collection_name.find({"awards.text": {$regex: /^Won\s.*/}}).pretty();
	
16. Array operators:
	db.collection_name.find({"genres": {$all: ["Comedy", "Crime", "Drama"]}}).pretty(); // All values should be present
	db.collection_name.find({countries: {$size: 1}}).pretty(); // Only 1 country value present
	Example for $elemMatch - 
	boxOffice: [
				{"country": "USA", "revenue": 41.3},
				{"country": "UK", "revenue": 41.3},
				{"country": "Australia", "revenue": 41.3}
				]
	db.collection_name.find({boxOffice: {$elemMatch: {"country": "UK", "revenue": {$gt: 20}}}}).pretty();

17. db.collection_name.updateOne({"title": "The Martian"}, {$set: {poster: "URL"}});
	db.collection_name.updateMany({rated: null}, {$unset: {rated: ""}}); // Removes the field
	db.collection_name.updateOne({"title": "The Martian"}, {$push: {reviews: {...}}}); // Pushes this value to the existing values of reviews - for one value
	db.collection_name.updateOne({"title": "The Martian"}, {$push: {reviews: {$each: [{...}]}}}); // Pushes an array of values to the existing values of reviews
	db.collection_name.updateOne({"title": "The Martian"}, {$push: {reviews: {$each: [{...}], $poistion: 0, $slice: 5}}}); // Slice means to keep first 5, if positive value, else last five for negative value. Position specifies the location where to add the reviews.
	db.collection_name.updateOne({"title": "The Martian"}, {$inc: {field: value}}); // Increments the value of the field by the value specified
	
18. When using update, if the record to be updated is not found, then that data is inserted - upsert.
	db.collection_name.updateOne({"imdb.id": detail.imdb.id}, {$set: detail}, {$upsert: true});
	db.collection_name.replaceOne({"imdb": detail.imdb.id}, detail);
	db.collection_name.remove({thing: 'apple'}); // Removes all occurrences
	db.collection_name.remove({thing: 'apple'}, {justOne: true})
	
19. Multikeys:
	students collection = {"_id": 0, "name": "Andrew", "teachers": [0, 1, 2]};
	teachers collects = {"_id": 0, "name": "Mark"};	
	To create an index: db.collection_name.ensureIndex({'teachers': 1}); // Creating a multikey index in students collection for teachers
	db.students.find({'teachers': {$all: [0, 1]}); // Returns all students that has teachers 0 and 1. Can be more than these values
	db.students.find({'teachers': {$all: [0, 1]}).explain(); // To understand about the indexes and other details
	
20. MMAP and Wired Tiger:
	MMAP uses mmap() system call for memory management. MongoDB puts data inside Files(Disk). MMAP maps 100GB of file into a 100GB of virtual memory space(physical memory). These are all page sized 4k or 8k. Operating system hits a page to see if the data is present in the virtual memory space, else it will go to the disk to fetch the data.
	MMAP provides collection level locking/concurrency. Only one write can happen to a collection at a time.
	This provides In-place update. Try to update in the same page. If the data surpasses the page size, then a new page is found within the virtual memory space in powers of 2.
	Wired Tiger is not turned on by default. It offers document level concurrency. No lock in document level. Assumes 2 writes to the same document doesn't happen at the same time. If 2 writes occur then one of those writes is unwound and has to try again. This mechanism is hidden to the application.
	It offers compression - documents and indexes. Here, it will manage the memory that access the 100GB data in disk. It manages which data to be kept in physical memory and which to be in disk. No in-place updates. They always mark the current document is no longr used and allocate a new space. Hence, no document level lock. No power-of-two document padding.
	Command to turn on wired tiger: 
	killall mongod
	mkdir WT
	mongod -dbpath WT -storageEngine wiredTiger
	To check which storage engine is used:
	db.collection_name.stats();
	
21. Indexing slows down the writes. Reads will be much faster. Index on (a, b, c) - full indexing on a, ab and abc, but not on c or cb and partial indexing on ac.

22. To use explain:
	db.collection_name.explain().find({key: value});
	Look for "winningPlan". If it says "stage": "COLLSCAN", then that means it scanned the whole collection.
	
23. db.collection_name.createIndex({student_id: 1}); // This means that the index is on student_id in ascending order.
	When we run the explain command, the "winningPlan" will say "stage": "FETCH" and "inputStage" will have "indexName": "student_id".
	To see how many documents it actually looked:
	db.collection_name.explain(true).find({key: value});
	This will show "executionStats" which will have "executionStages" which will have "docsExamined": 10.
	
24. To add a compound index:
	db.collection_name.createIndex({student_id: 1, class_id: -1}) // This means class_id is descending.
	
25. To see what indexes exist in the collection:
	db.collection_name.getIndexes(); // This will give an array of documents. The number of documents is equal to the number of indexes. This is a preferred way to check the indexes from MongoDB 3.0 and this can be used in both MMAP and Wired Tiger.
	An index always exist for _id and this cannot be deleted.
	
26. To drop and index:
	db.collection_name.dropIndex({student_id: 1}); // Same signature while creating the index.
	
27. Multikey indexes: Assume a collection:
	{tags: ['photography', 'hiking', 'golf'], color: 'red'}; // Here multikey index can be on the (tags) array. Or, both (tags, color) which will then have indexes such as ('photography', 'red'), ('hiking', 'red') and ('golf', 'red').
	Restriction on multikey is for a compound index is that both the values in the compound index cannot be an array. This is because there will be indexes for cartesian product of the arrays.
	
28. db.collection_name.createdIndex({a: 1, b: 1});
	Assume, a and b are single numbers. Now, when explain command is used, then inside "queryPlanner", inside the "winningPlan", inside "inputStage", there will be "indexName": "a_1_b_1". Then here, there will be "isMultiKey": false. Here, there will also be "indexBounds": {"a": ["[1.0, 1.0]"], "b": ["[1.0, 1.0]"]}. The values 1.0 here are the values queried using find().
	If a or b is an array, then "isMultiKey": true. If after this compound index is created and we try to insert both a and b as arrays, then we will fet an error saying cannot index parallel arrays. We can have either a or b as array and can inserted interchangeably.
	
29. To create an index in an inner property:
	db.collection_name.createIndex({'scores.score': 1}); //	Here, the index "key" will be "scores.score": 1 and "name": "scores.score_1"
	
30. students collection: find the scores for a particular type
	{"_id": ObjectId("a number"), "sturent_id": 123, "scores": [{"type": "exam", "score": 30.76}, {"type": "homework", "score": 40}]};
	db.students.explain().find({'scores': {$elemMatch: {type: 'exam', score: {'$gt': 99.8}}}});
	db.students.explain().find({'$and': [{'scores.type': 'exam'}, {'scores.score': {'$gt': 99.8}}]}); // score may not be of exam
	
31. Create unique index: db.collection_name.createIndex({key: 1}, {unique: true});
	db.collection_name.getIndexes(); // This will show "unique": true for this index. _id is also unique, but this won't be shown here.
	db.collection_name.createIndex({key1: 1, key2: 1}, {unique: true}); // Here there can only one record which doesn't have key1. There cannot be two records with no key1. Same applies for key2 as well. As it means 2 or more records with key1 or key2 with value as null.
	
32. In the above case, to create index: db.collection_name.createIndex({key1: 1, key2: 2}, {unique: true, sparse: true});
	Using sparse index for sorting will result in full collection scan since the database knows that there are chances of records with null indexes. Sparse index uses very less space.
	
33. Types of index creation:
	Foreground: It is fast. It blocks writes and reads in the database during index creation. This is in both MMAP and Wired Tiger. This is the default option.
	Background: It is slow. It doesn't block any writes and reads in the database during index creation. Prior MongoDB 2.4, index creation queues up on a per-database level. With MongoDB 2.4 and later, we can create multiple background indexes in parallel even on the same database.
	To create background index: db.collection_name.createIndex({key: 1}, {background: true});
	
34. db.collection_name.explain(); // this returns an explainable object. 
	On this we can run .find(), .aggregate(), .count(), .group(), .remove(), .update(), .getCollection(), .getVerbosity and .setVerbosity(verbosity). .insert() cannot be run on this. Earlier versions had - db.collection_name.find({key: value}).explain(); // This needs a cursor to be returned for explain() to work. hence, this will not work: db.collection_name.find({key: value}).count().explain(); // Hence, in MongoDB 3.0, this was changed to db.collection_name.explain().find({key: value})
	
35. There are 3 modes to run the explain mode. 
	Query Planner mode is the default which has been used till now in the session. Tells what DB would use in terms of indexes, but, doesn't tell what the results of using those indexes are.
	Execution Stats mode includes Query Planner mode also. The above results is told by this mode. This will show execution stats for the winning plan.
	var exp = db.collection_name.explain("executionStats"); // There will be "executionStats" in the result after using exp in find(). This will have "nReturned" which is the number of documents returned to the client by the query. Then there is "executionTimeMillis" which is the execution time of the query in milliseconds. Then there is "totalKeysExamined" and "nReturned"; if these both are same then the query it really good because that means all the keys referred were for the required documents. Then there is "executionStages" that will have the results of each stage of execution. Inside this there is "inputStage". This "nReturned" and "docsExamined". If "docsExamined" is much greater than "inputStage", then the query or index has to be modified.
	All Plans Execution mode includes Execution Stats and Query Planner modes. This will show the execution stats of all the plans.
	var exp = db.collection_name.explain("allPlansExecution");
	
36. Covered queries: A query that is satisfied completely by the index. Fastest query. "totalDocsExamined" should be 0. For a collection with index {i: 1, j: 1, k: 1},
	db.collection_name.find({i: 45, j: 23}, {_id: 0, i: 1, j: 1, k: 1}); is a covered query.
	db.collection_name.find({i: 45, j: 23}, {_id: 0}); and 
	db.collection_name.find({i: 45, j: 23}, {i: 1, j: 1, k: 1}); are not covered queries. The order of the index also matters in the query execution. 
	Eg: Indexes are: { name: 1, dob: 1 }, { _id: 1 } and { hair: 1, name: 1 }
	db.example.find({name: {$in: ["Bart", "Homer"]}}, {_id: 0, dob: 1, name: 1}); // This is covered query.
	db.example.find({name: {$in: ["Bart", "Homer"]}}, {_id: 0, hair: 1, name: 1}); // Here name will checked and then hair will also be checked.
	
37. How to choose an index. The winning plan is stored in cache and is used for queries which have similar shape in future. 
	To find the winning query plan, there is assumed to have a limit. This limit could be either All Results or a Threshold with a caveat that it is in Sorted order. We can evict the query plan from the cache because the collection changes, index changes and so winning plan may not be the same always. In order to evict, one way is if there is a threshold number of writes which is 1000 writes. Second is if there is a rebuild of indexes (either an index is added or removed). Or finally, if the mongod process is restarted. We no longer evict plans from the cache after a threshold number of writes. Instead, we evict when the "works" of the first portion of the query exceed the number of "works" used to decide on the winning plan by a factor of 10x. This can be seen in the "works" for a particular plan using .explain("executionStats").

38. To find index size: 
	db.collection_name.stats(); // This will have "totalIndexSize". Wired Tiger has index prefix compression. This feature can be turned on. This will compress the index size. This compression comes at the cost of CPU and whether or not you can take advantage of prefix compression in your data structure. The index size should fit in your memory, even compared to data. Else, the query will have to go to the disk always to fetch the data.
	
39. Index cardinality: How many index points are there for each different types of indexes that MongoDB supports.
	In regular index, for every single key that we put in the index, there is going to be an index point. In addition to that if there is no key, there is going to be an index under the null entry. Here we have 1:1, relative to the number of documents in the collection index cardinality. This has index size which is proportional to the collection size in terms of its end pointers to the documents.
	In sparse index, when a document is missing the key being indexed, it is not in the index. It is null and we don't keep null values in the sparse index. This will have less than or equal to the document size.
	In multikey index, there can be multiple index points for each document since this is on an array. This can be greater than the document size.
	In terms of considering the cost of moving documents, we will be updating index entries. That cost only exists in the MMAPv1 storage engine. In the WiredTiger storage engine, index entries don't contain pointers to actual disk locations. Instead, in WiredTiger, the index points to an internal document identifier(the RecordId) that is immutable. Therefore, when a document is updated, its index does not need to be updated at all.
	
40. Geospatial indexes: Locate a point in 2D space.
	db.collection_name.createIndex({location: '2d'}, tyepe: 1); // where "location" = [x,y]
	db.collection_name.find({location: {$near: [x,y]}}); // This is often used with a limit: db.collection_name.find({location: {$near: [x,y]}}).limit(20);
	This returns data in an increasing order.

41. Geospatial spherical: Locate in 2D sphere. geojson.org website mentioned.
	db.collection_name.createIndex({'location': '2dsphere'});
	db.collection_name.find({location: {$near: {$geometry: {type: "Point", coordinates: [value1, value2], $maxDistance: 2000}}});
	
42. After putting a queries in .js file, they can be run in command prompt using 'mongo < filename.js'. The content of the .js file can be viewed using 'more filename.js'.

43. Full text search index: $regex can be used for pattern search. To create a text serach index:
	db.collection_name.createIndex({'key': 'text});
	db.collection_name.find({$text: {$search: 'value'}}); // The value can be a substring of the original value.
	db.collection_name.find({$text: {$search: 'complete value'}}, {score: {$meta: 'textScore'}}).sort({score: {$meta: 'textScore'}}); // Sorts by the textScore. The highest value will have the the complete value.

44. The goal of designing or using indexes are: efficient read and write, selectivity - minimum records scanned and other ops such as how sorts are handled.

45. If we see "stage": "SORT" in the executionStats, then this mean MongoDB did an in-memory sort.

46. db.collection_name.find({student_id: {$gt: 5000}, class_id: 54}).sort({student_id: 1}).hint({class_id: 1}).explain("executionStats");
	Using hints we can override the index to be used. Quality queries should come before range queries.
	db.collection_name.find({student_id: {$gt: 5000}, class_id: 54}).sort({final_grade: -1}).explain("executionStats"); 
	Here, the index should be: db.collection_name.createIndex({'class_id': 1, 'final_grade': 1, 'student_id': 1}); // This is a better index because class_id filters a major part of the data(very selective), then the final_grade walks the index keys in order to sort the data and the student_id can filter the data on this.
	
47. MongoDB automatically logs slow queries that have execution time of more than 100ms. Profiler writes entries into system.profile. 
	3 levels. 0 is default level which means off. 1 means log slow queries. 2 means log all queries - not for performance debugging, but, to see all DB traffic.
	Default way: mongod -dbpath /usr/local/var/mongodb
	1 level: mongod -dbpath /usr/local/var/mongodb --profile 1 --slowms 2 // To check: db.system.profile.find(); // This is a cat collection which means it recycles the space once the full space is used.
	To find inside system.profiler:
	db.system.profile.find({ns:/dbname.collectionname/}).sort({ts: 1}); // ts is timestamp
	To get the profile level: db.getProfilingLevel();
	To get the profile status: db.getProfilingStatus();
	To set the profile level: db.setProfilingLevel(1, 4); // query more than 4ms.
	To off: db.setProfilingLevel(0);
	
48. Mongotop - tool to find where MongoDB spends more time. The command is: mongotop 3 // run mogotop every 3 seconds.

49. Mongostat: sample DB in 1 second increments and gives bunch of info happening in that 1 sec: number of inserts, queries, updates and deletes.
	It will tell you if Wired Tiger or MMAP is being used.
	
50. Sharding: Splitting a large collection among multiple servers. 
	Application talks to mongos router, mongos talks to talks to mongod servers (or replica set - which is if a server goes down, we will not lose data - keeps the data in sync across instances). We choose a shard key. student_key or a compund key can be a shard key. An insert should include a shard key(an entire shard key). Update or remove or find will broadcast across all the shards if shard key is not given. Update will do multi-update if shard key is not present so that it knows it has to broadcast. Mongos is co-located with the application. There can be multiple mongos.
	
51. Aggregate functions: 
	select manufacturer, count(*) from products group by manufacturer
	db.products.aggregate([{$group: {_id: "$manufacturer", num_products: {$sum: 1}}}]);
	
52. Aggregation pipeline:
	collection -> $project -> $match -> $group -> $sort -> result // These can occur multiple times.
	$project: reshape the document. Select certain fields and also select from deep in the hierarchy and bring to the top. One document that into projection stage, another document leaves the projection stage. Hence, this is a 1:1.
	$match: filtering step. This is n:1.
	$group: allow to aggregate. sum and count that groups the document. This is n:1.
	$sort: sorts the document. This is 1:1.
	$skip: skip forward a number of documents. This is n:1.
	$limit: limits the number of documents. Works well with $sort. First sort and then limit. This is n:1.
	$unwind: normalize of flatten the data. If tags: ["red, "blue", "green"]. After unwind, this becomes multiple documents such as tags: red, tags: blue and tags: green. One document will become three with these three data and other data remains same.
	$out: take the output to another collection. This is 1:1.
	$readact: security related feature.
	$geonear: location based filtering.
	$sum: count or if we add value of the key, it can sum up the keys.
	$avg: average the values of the keys across the document.
	$min: minimum value of the key in the documents.
	$max: maximum value of the key in the documents.
	$push and $addToSet: build arrays. To push a value to an array in the result. addtoset will add only unique values.
	$first and $last: first sort the document. first value of the key that it sees in the data that is being processed during the group. last will be the last value. If not sorted, first and last will return arbitrary values.
	
53. Aggregation function:
	Creating a new collection of results. Group phase doesn't run the documents from one end to another. It actually processes each one, grouping it and then creating a new set of aggregated documents. $group is upserting the values.
	
54. Compound grouping:
	db.collection_name.aggregate([{$group: {_id: {"manufacturer": "$manufacturer", "category": "$category"}, num_products: {$sum: 1}}}]);
	
55. To sum the values of a particular key(similar to column values):
	db.collection_name.aggregate([{$group: {_id: {"maker": "$manufacturer"}, sum_price: {$sum: "$price"}}}]);
	
56. To find the average:
	db.collection_name.aggregate([{$group: {_id: {"category": "$category"}, avg_price: {$avg: "$price"}}}]);
	
57. Add to set: This creates an array.
	db.collection_name.aggregate([{$group: {_id: {"maker": "$manufacturer"}, categories: {$addToSet: "$category"}}}]);
	
58. Push:
	db.collection_name.aggregate([{$group: {_id: {"maker": "$manufacturer"}, categories: {$push: "$category"}}}]);
	
59. Max and Min:
	db.collection_name.aggregate([{$group: {_id: {"maker": "$manufacturer"}, maxprice: {$max: "$price"}}}]);
	db.collection_name.aggregate([{$group: {_id: {"maker": "$manufacturer"}, minprice: {$min: "$price"}}}]);
	
60. Double grouping:
	db.collection_name.aggregate([{'$group': {_id: {class_id: "$class_id", student_id: "$student_id"}, 'average': {"$avg": "$score"}}},
	{'$group': {_id: "$_id.class_id", 'average': {"$avg": "$average"}}}]);
	
61. Project:
	In $project, we can add keys, remove keys, reshape keys - take a key and put into a sub-document with another key, some functions can be used on keys - $toUpper, $toLower, $add and $multiply.
	db.collection_name.aggregate([{$project: {_id: 0, 'maker': {$toLower: "$manufacturer"}, 'details': {'category': "$category", 'price': {"$multiply": ["$price", 10]}},
	'item': '$name'}}]);

62. Match:
	db.collection_name.aggregate([{$match: {state: "CA"}}]); // Filter for CA state.
	db.collection_name.aggregate([{$match: {state: "CA"}}, {$group: {_id: "$city", population: {$sum: "$pop"}, zip_codes: {$addToSet: "$_id"}}}]);
	db.collection_name.aggregate([{$match: {state: "CA"}}, {$group: {_id: "$city", population: {$sum: "$pop"}, zip_codes: {$addToSet: "$_id"}}},
	{$project: {_id: 0, city: "$_id", population: 1, zip_codes: 1}}]); // $project does not retain the order specified in the query.
	db.collection_name.aggregate([{$match: {pop: {$gt: 1000000}}}]);

63. Sort: 
	Both disk and memory based sorting are supported by aggregation framework. This does in-memory sorting by default for a size of 100MB size. Before or after the grouping.
	db.collection_name.aggregate([{$match: {state: "CA"}}, {$group: {_id: "$city", population: {$sum: "$pop"}, zip_codes: {$addToSet: "$_id"}}},
	{$project: {_id: 0, city: "$_id", population: 1, zip_codes: 1}}, {$sort: {population: -1}}]);

64. Skip and Limit:
	Usually forst skips and then limits.
	db.collection_name.aggregate([{$match: {state: "CA"}}, {$group: {_id: "$city", population: {$sum: "$pop"}, zip_codes: {$addToSet: "$_id"}}},
	{$project: {_id: 0, city: "$_id", population: 1, zip_codes: 1}}, {$sort: {population: -1}}, {$skip: 10}, {$limit: 5}]);

65. First and Last:
	db.collection_name.aggregate([{$group: {_id: {state: "$state", city: "$city"}, population: {$sum: "$pop"}}, {$sort: {"_id.state": 1, "population": -1}}, 
	{$group: {_id: "$_id.state", city: {$first: "$_id.city"}, population: {$first: "$population"}}}}]);
	db.collection_name.aggregate([{$group: {_id: {state: "$state", city: "$city"}, population: {$sum: "$pop"}}, {$sort: {"_id.state": 1, "population": -1}}, 
	{$group: {_id: "$_id.state", city: {$last: "$_id.city"}, population: {$last: "$population"}}}, {$sort: {"_id": 1}}}]);
	
66. Unwind:
	db.collection_name.aggregate([{"$unwind: "$tags"}, {"$group": {"_id": "$tags", "count": {$sum: 1}}, {"$sort": {"count": -1}, {"$limit": 10}, 
	{"$project": {_id: 0, 'tag': '$_id', 'count': 1}}]);
	Doube=le unwind:
	db.collection_name.aggregate([{"$unwind": "$sizes"}, {"$unwind": "$colors"}, {"$group": {'_id': {'size': '$sizes', 'color': '$colors'}, 'count': {'$sum': 1}}}]);
	db.collection_name.aggregate([{"$unwind": "$sizes"}, {"$unwind": "$colors"}, {"$group: {"_id": {"name": "$_id.name", "size": "$sizes"}, {"colors": {"$push": "$colors"}}},
	{"$group": {"_id": {"name": "$_id.name", "colors": "$colors"}, {"sizes": {"$push": "$_id.size"}}, 
	{"$project": {_id: 0, "name": "$_id.name", "sizes": 1, "colors": "$_id.colors"}}]); // Reversing the unwind operation
	If the array doesn't have duplicate data, then:
	db.collection_name.aggregate([{"$unwind": "$sizes"}, {"$unwind": "$colors"}, {"$group": {"_id": "name", "sizes": {"$addToSet": "$sizes"}, 
	"colors": {"$addToSet": "$colors"}}}]);
	
67. Limitatios:
	100MB size limit for pipeline stages. To get around allowDiskUse has to be enabled.
	If return the results in one document, there is a limit of 16MB. To get around, we use a cursor = empty document {}.
	In a sharded system as soon as group by or sort, or anything that requires looking at all results is used, then results will have to be returned in the first shard. When an aggregation query is fired by the application to the mongos server, the mongos server fires that query to all the sharded servers. So for queries that has to look into the entire results, mongoDB will return the result to the primary shard. Primary shard is the place where an unsharded collection would live. Hence, for a large data set, the performance for aggregation will not be as expected. Hence, we can use Hadoop(data has to be taken from mongoDB to hadoop), which has map/reduce that provides parallelism. Hadoop connector is used to connect with Hadoop. MongoDB also has map/reduce which is old and has limitations. Hence, not recommended except for simple steps, but, aggregation is better choice than map/reduce.
	
68. Durability - Write concern:
	When application issues a write operation, it is written in the memory(in pages) inside the server. This is not written to disk at the same time. There is another part called journal inside the server. Journal has details about the update - when was the data written in the memory. Journal is not written into disk frequently. Now the wait concern depends or w which is should we wait for the write to be acknowledged by the server and j which is the journal. Server has CPU, Pages, Journal and Disk.
	By default, w = 1 and j = false. This is fast, but, there is a small window of vulnerability. Next is w = 1 and j = true. This is a bit slow but the vulnerability is removed. There is a historical option that is not recommended where w = 0 - unacknowledged write.
	
69. Replica set - Fault tolerance:
	When Primary node goes down, the other secondary nodes will do election to find the primary node. When primary node comes back, it acts as secondary. The minimum original number of nodes to elect when the primary node goes down is 3. Replication between nodes is asynchronous. During failover and when a primary is elected, writes won't happen successfully.
	
70. Regular type of nodes: It has a data and become primary. Normal type of node. This is the default. It can be primary and secondary.
	Arbiter type of nodes: It is for voting purposes. If we have even number of replica set, then when the primary node goes down, there is a major vote to elect primary node. It has no data in it.
	Delayed type of nodes: Disaster recovery nodes. It can participate in voting. It cannot be primary. Its priority is 0 - any node that is 0 cannot be primary.
	Hidden type of nodes: Used for analytics. It cannot be primary. Its priority is set to 0.
	
71. To create a replica set: 
	mongod --replSet rs1 --logpath "1.log" --dbpath /data/rs1 --port 27017 --fork
	mongod --replSet rs1 --logpath "2.log" --dbpath /data/rs2 --port 27018 --fork
	mongod --replSet rs1 --logpath "3.log" --dbpath /data/rs3 --port 27019 --fork
	create_replica_set.sh created the replica set, but, it is not initialized. init_replica.js has the configuration and it does the initialization. To run this .sh file: bash < create_replica_set.sh. To read the file: more 1.log.
	To open a port: mongo --port 27018;
	To read from a secondary node: rs.slaveOk();
	To check the status: rs.status();
	The secondaries are constantly reading the oplog of the primary. It's true that the oplog entries originally come from the primary, but secondaries can sync from another secondary, as long as at least there is a chain of oplog syncs that lead back to the primary.
	
72. oplog is a capped collection which means it is going to be rolled off eventually after a certain amount of time. 
	The secondary replicate set will have the logged time details which will help to understand till what time the data has been copied from primary to secondary. Primary and secondary can be different memory management systems. Primary can be MMAP and secondary can be Wired Tiger and vice versa.
	
73. Failover and Rollback:
	While it is true that a replica set will never rollback a write if it was performed with w=majority and that write successfully replicated to a majority of nodes, it is possible that a write performed with w=majority gets rolled back. Here is the scenario: you do write with w=majority and a failover over occurs after the write has committed to the primary but before replication completes. You will likely see an exception at the client. An election occurs and a new primary is elected. When the original primary comes back up, it will rollback the committed write. However, from your application's standpoint, that write never completed, so that's ok.
	
74. Write concern (w) value can be set at client, database or collection level within PyMongo. 
	When you call MongoClient, you get a connection to the driver, but behind the scenes, PyMongo connects to multiple nodes of the replica set. The w value can be set at the client level. Andrew says that the w concern can be set at the connection level; he really means client level. It's also important to note that wtimeout is the amount of time that the database will wait for replication before returning an error on the driver, but that even if the database returns an error due to wtimeout, the write will not be unwound at the primary and may complete at the secondaries. Hence, writes that return errors to the client due to wtimeout may in fact succeed, but writes that return success, do in fact succeed. Finally, the video shows the use of an insert command in PyMongo. That call is deprecated and it should have been insert_one.
	
75. Read Preference:
	Primary(always from primary), Primary preferred(prefer primary but can use secondary if primary is not available), Secondary(always from secondary), Secondary preferred(prefer secondary but can use primary if secondary is not available) and Nearest - where you can set the data center location.