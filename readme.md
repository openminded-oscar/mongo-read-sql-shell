<h3>Description</h3>
This program is another version of Mongo shell.

Supported query structure:
SELECT `Projections` FROM `Target`
[WHERE `Condition`]
[ORDER BY `Field [ASC|DESC]`]
[SKIP `SkipRecords`]
[LIMIT `MaxRecords`]

<h3>Features:</h3>
- support of projections, * and field.* - for embedded fields;
- multiple ordering;
- complex condition separated by AND|OR;
- support of LIMIT and SKIP.

<h3>Limitations:</h3>
- complex clauses can be constructed using `AND`and `OR` - without braces;
- complex clauses are executing from-left-to-right.

<h3>Dependencies:</h3>
* spring-boot-starter-data-mongodb;
* jackson-databind for constructing JSON-results from Java objects;
* com.monitorjbl.json-view for making projections of resulting JSONs according to user query;
* spring-boot-starter-test[test] - for unit-tests construction;
* json-path[test] to check JSONs constructing.
To access to MongoDb from Java we will use MongoTemplate to have flexibility to construct queries dynamically 
[https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongo-template].
Using Spring Data Mongo repositories we would have to declare methods for all possible queries, order, limits, etc.


<h3>Usage instruction:</h3>
- to start program run `ShellApplication` class from IDE (or `mvn spring-boot:run` from terminal);
- to launch tests run `mvn test` from terminal;
- db is connecting to mongo on `default port (27017)` and `realty` database - as set in `application.properties` file. Use next properties to override default configuration: `spring.data.mongodb.host, spring.data.mongodb.port, spring.data.mongodb.password, spring.data.mongodb.username, spring.data.mongodb.uri`.
- to test application you can use test database dump, provided in realty-db-to-test project folder - using `mongorestore --db realty <path-to-dump>` command;
- you have to use upper case for keywords and same case for fields as application will output at beginning (typically camelcase);
- use `;` to end query;
- <h5>To run app in Docker run `DockerRunApp` script, to install and run app in Docker - `DockerInstallAndRunApp`</h5>.

<h3>DockerInstallAndRunApp script explanation:</h3>
0) `mvn clean package` - to build jar file that will be built as image in 2);
1) `docker run --name mongo-image -d mongo:3.6.5` - starts mongo in separate container (you have to pull mongo image before);
2) [optional - populate db from dump folder] 
2.1) `docker cp test-db-dump mongo-image:/home` from project root directory;
2.2) `docker exec -it mongo-image  mongorestore -v --db realty /home/test-db-dump`;
2.3) `docker exec -it mongo-image rm -rf /home/test-db-dump`;
3) `docker build . -t mongo-read-sql-shell` - builds application image and activates docker profile;
4) `docker run -i -t --link mongo-image:27017 mongo-read-sql-shell` - launches application container in interactive mode (stdout and stdin enabled).