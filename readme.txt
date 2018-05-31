To access to MongoDb from Java we will use MongoTemplate to have flexibility to construct queries dynamically 
[https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongo-template].
Using Spring Data Mongo repositories we would have to declare methods for all possible queries, order, limits, etc.

You can read here about constructing queries for MongoDb: http://www.baeldung.com/queries-in-spring-data-mongodb.