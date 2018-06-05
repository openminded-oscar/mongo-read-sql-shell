To access to MongoDb from Java we will use MongoTemplate to have flexibility to construct queries dynamically 
[https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongo-template].
Using Spring Data Mongo repositories we would have to declare methods for all possible queries, order, limits, etc.

Usage instruction:
- db is connecting to mongo on default port (27017) and "realty" database - as set in application.properties. Use next properties to override default configuration spring.data.mongodb.host, spring.data.mongodb.port, spring.data.mongodb.password, spring.data.mongodb.username, spring.data.mongodb.uri.
- to test application you can use test database dump, provided in realty-db-to-test project folder - using "mongorestore --db realty <path>" command;
- you have to use upper case for keywords and same case for fields as application will output at beginning (typically camelcase);