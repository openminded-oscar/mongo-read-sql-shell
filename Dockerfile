FROM openjdk:8
ADD target/mongo-read-sql-shell.jar mongo-read-sql-shell.jar
ENTRYPOINT ["java", "-jar", "mongo-read-sql-shell.jar", "--spring.profiles.active=docker"]