# MongoDemo

* Mongo compass:

https://www.mongodb.com/products/tools/compass

* Mongo official documentation:

https://www.mongodb.com/docs/

* Mongo tutorial:

https://www.w3schools.com/mongodb/index.php

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
db.grantRolesToUser("test3", ["readWrite"]);

db.grantRolesToUser("test3", ["dbAdmin"]);
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

----------------------

---------------------



# Mongo Demo Part 2

* drop collection

```js
db.users.drop();
```

* insert test data

````js
// Insert sample users
db.users.insertMany([
  {
    name: "Alice",
    age: 28,
    email: "alice@example.com",
    address: { city: "New York", country: "USA" },
    created_at: new Date("2023-01-15"),
    tags: ["admin", "tech"],
    balance: 1500
  },
  {
    name: "Bob",
    age: 35,
    email: "bob@example.com",
    address: { city: "London", country: "UK" },
    created_at: new Date("2023-02-20"),
    tags: ["user", "sales"],
    balance: 800
  },
  {
    name: "Charlie",
    age: 42,
    email: "charlie@example.com",
    address: { city: "Paris", country: "France" },
    created_at: new Date("2023-03-10"),
    tags: ["manager", "hr"],
    balance: 2000
  },
  {
    name: "Diana",
    age: 28,
    email: "diana@example.com",
    address: { city: "Berlin", country: "Germany" },
    created_at: new Date("2023-04-05"),
    tags: ["user", "support"],
    balance: 1200
  }
]);
````

## Aggregations

* $match (Filter documents)

```js
// Find users aged 28
db.users.aggregate([
  { $match: { age: 28 } }
]);
```

* $group (Group documents)

```js
// Group by country and calculate average balance
db.users.aggregate([
  { $group: { 
    _id: "$address.country", 
    avgBalance: { $avg: "$balance" },
    count: { $sum: 1 }
  }}
]);
```

* $project (Reshape documents)

```js
// Include only name and email with uppercase name
db.users.aggregate([
  { $project: { 
    _id: 0,
    fullName: { $toUpper: "$name" }, 
    email: 1 
  }}
]);
```

* $sort (Sort documents)

```js
// Sort by age descending
db.users.aggregate([
  { $sort: { age: -1 } }
]);
```

* $limit (Limit results)

```js
// Get first 2 documents
db.users.aggregate([
  { $limit: 2 }
]);
```

* $skip (Skip documents)

```js
// Skip first document
db.users.aggregate([
  { $skip: 1 }
]);
```

* $unwind (Deconstruct array)

```js
// Unwind tags array
db.users.aggregate([
  { $unwind: "$tags" }
]);
```

```js
db.users.aggregate([
 { $project: { 
    _id: 0,
    fullName: { $toUpper: "$name" }, 
    email: 1 ,
    tags: 1
  }},
  { $sort: { age: -1 } },
  { $unwind: "$tags" }
]);
```

* $lookup (Join collections)


```js
// First, create an orders collection
db.orders.insertMany([
  { user_email: "alice@example.com", amount: 100 },
  { user_email: "bob@example.com", amount: 50 }
]);

// Join users with their orders
db.users.aggregate([
  {
    $lookup: {
      from: "orders",
      localField: "email",
      foreignField: "user_email",
      as: "orders"
    }
  }
]);
```

* $addFields (Add new fields)

```js
// Add balance status field
db.users.aggregate([
  { $addFields: { 
    balanceStatus: { 
      $cond: { if: { $gte: ["$balance", 1000] }, then: "High", else: "Low" } 
    } 
  }}
]);
```

* $count (Count documents)

```js
// Count users from USA
db.users.aggregate([
  { $match: { "address.country": "USA" } },
  { $count: "totalUsersInUSA" }
]);
```

* $facet (Multiple pipelines)

```js
// Categorize users by age and active status
db.users.aggregate([
  { $facet: {
    "ageGroups": [
      { $bucket: { groupBy: "$age", boundaries: [20, 30, 40, 50], default: "Other" } }
    ],
    "activeUsers": [
      { $match: { tags: "admin" } },
      { $count: "activeAdmins" }
    ]
  }}
]);
```

* $bucket (Group into ranges)

```js
// Group users by age ranges
db.users.aggregate([
  { $bucket: {
    groupBy: "$age",
    boundaries: [25, 30, 35, 40, 45],
    default: "Other",
    output: { count: { $sum: 1 } }
  }}
]);
```


* $sortByCount (Sort groups by count)

```js
// Count tags and sort
db.users.aggregate([
  { $unwind: "$tags" },
  { $sortByCount: "$tags" }
]);
```

https://www.mongodb.com/docs/manual/reference/operator/aggregation/graphLookup/

* $graphLookup (Hierarchical data)

```js
// Add manager references
db.users.updateMany({}, [{ $set: { managerId: null } }]); // Add managerId field

// Find reporting hierarchy
db.users.aggregate([
  { $graphLookup: {
    from: "users",
    startWith: "$managerId",
    connectFromField: "managerId",
    connectToField: "_id",
    as: "reportingHierarchy"
  }}
]);
```

https://www.mongodb.com/docs/manual/reference/operator/aggregation/replaceRoot/

* $replaceRoot (Promote nested document)

```js
// Promote address to root
db.users.aggregate([
  { $replaceRoot: { newRoot: "$address" } }
]);
```

https://www.mongodb.com/docs/manual/reference/operator/aggregation/merge/

* $merge (Write to collection)

```js
// Create summary collection
db.users.aggregate([
  { $group: { _id: "$address.country", total: { $sum: 1 } } },
  { $merge: { into: "country_summary" } }
]);
```

https://www.mongodb.com/docs/manual/reference/operator/aggregation/out/

* $out (Replace collection)

```js
// Create new collection with users over 30
db.users.aggregate([
  { $match: { age: { $gt: 30 } } },
  { $out: "senior_users" }
]);
```

![img_1.png](img_1.png)

*  $sample (Random selection)

```js
// Get 2 random users
db.users.aggregate([
  { $sample: { size: 2 } }
]);
```