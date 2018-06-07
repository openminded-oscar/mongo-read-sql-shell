call mvn clean package 
call docker run --name mongo-image -d mongo:3.6.5
call docker cp test-db-dump mongo-image:/home
call docker exec -it mongo-image  mongorestore -v --db realty /home/test-db-dump
call docker exec -it mongo-image rm -rf /home/test-db-dump
call docker build . -t mongo-read-sql-shell
call docker run -i -t --link mongo-image:27017 mongo-read-sql-shell