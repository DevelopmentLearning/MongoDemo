// mongo-init/init-mongo.js

db.createUser({
    user: 'test',
    pwd: 'test',
    roles: [{ role: 'readWrite', db: 'test_db' }],
});

db.createCollection('test_collection'); // Create a dummy collection to ensure the database exists


