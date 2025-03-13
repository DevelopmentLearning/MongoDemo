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

* create a new user

```js
db.createUser({
    user: 'test3',
    pwd: 'test3',
    roles: [{role: 'readWrite', db: 'test_db'}],
});
```

