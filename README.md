# MongoDemo

1. Run 

```docker compose up```

2. Connect to mongo db with Mongo Compass with this url:

```mongodb://test:test@localhost:27017/test_db```

3. In container terminal run :
  
```mongosh```

   or

   ```mongosh -u "test" -p "test" --authenticationDatabase "admin"```

![img.png](img.png)

## Commands for mongo

* create or change db

```
use mydb
```

* create collection

```js
db.createCollection("products");
````

* show all databases

```
show dbs; 
```

* drop database

```js
db.dropDatabase();
```

* db stats

```js
db.stats(); // Database statistics
```

* create a new user

```js
db.createUser({
    user: 'test3',
    pwd: 'test3',
    roles: [{role: 'readWrite', db: 'test_db'}],
});
```

* grant roles to user

```js
db.grantRolesToUser("test3", ["readWriteAnyDatabase"]);
```

* helper methods

```js
db.help(); // List database methods

db.collection.help(); // List collection methods
```

* create a new collection

```js
db.createCollection("users");
```

* Insert Documents

```js
db.users.insertOne({ name: "Alice", age: 30 }); // Insert one document

db.users.insertMany([{ name: "Bob" }, { name: "Charlie" }]); // Insert multiple
```

* Update documents

```js
db.users.updateOne({name: "Alice"}, {$set: {age: 31}}); // Update one

db.users.updateMany({}, {$set: {status: "active"}});// Update all

db.users.updateMany({status: "active"}, {$inc: {count: 1}}); // Increment

db.users.replaceOne({name: "Bob"}, {name: "Robert"}); // Replace entire doc
```

* Delete documents

```js
db.users.deleteOne({ name: "Alice" }); // Delete one

db.users.deleteMany({ status: "active" }); // Delete multiple
```

* find documents

```js
db.users.find({ age: { $gt: 25 } }); // Find users older than 25

db.users.findOne({ name: "Alice" }); // Return the first match
```

* query operators

Comparison

* $eq: Values are equal
* $ne: Values are not equal
* $gt: Value is greater than another value
* $gte: Value is greater than or equal to another value
* $lt: Value is less than another value
* $lte: Value is less than or equal to another value
* $in: Value is matched within an array

Logical


* $and: Returns documents where both queries match
* $or: Returns documents where either query matches
* $nor: Returns documents where both queries fail to match
* $not: Returns documents where the query does not match


Evaluation

* $regex: Allows the use of regular expressions when evaluating field values
* $text: Performs a text search
* $where: Uses a JavaScript expression to match documents

```js
db.users.find({
  age: { $gte: 18, $lte: 65 },
  $or: [{ role: "admin" }, { status: "active" }]
});
```

* indexes

```js
db.users.createIndex({ age: 1 }); // 1 for ascending, -1 for descending

db.collection.dropIndex("age_1"); // Drop by index name
```

```js
db.users.getIndexes();
```

```js
// Create a unique index on the "name" field
db.users.createIndex({ name: 1 }, { unique: true });
```

```js
// Create a compound unique index on "name" and "age"
db.users.createIndex({ name: 1, age: 1 }, { unique: true });
```

```js
// Check for duplicates in "name"
db.users.aggregate([
  { $group: { _id: "$name", count: { $sum: 1 } } },
  { $match: { count: { $gt: 1 } } }
]);
```

Use sparse: true to skip indexing documents where the field is missing/null

```js
db.users.createIndex({ email: 1 }, { unique: true, sparse: true });
```

Enforce uniqueness conditionally (e.g., only for active users)

```js
db.users.createIndex(
  { name: 1 },
  { unique: true, partialFilterExpression: { status: "active" } }
);
```