package co.oleh.mongoreadsqlshell;

import co.oleh.mongoreadsqlshell.entities.User;
import co.oleh.mongoreadsqlshell.repositories.GenericDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class Main implements CommandLineRunner {
    @Autowired
    private SqlToMongoQueryTransformer sqlToMongo;

    @Autowired
    private GenericDocumentRepository repository;

    @Override
    public void run(String... args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String sqlQuery = "";
        System.out.println("Welcome to MongoDB alternative (SQL-based) client. Type desired SELECT queries or \"exit\"");

        while (true) {
            System.out.print("> ");
            String input = br.readLine().trim();
            if (isStopPhrase(input)) {
                break;
            } else {
                sqlQuery += " " + input;
                if (input.trim().charAt(input.length() - 1) == ';') {
                    sqlQuery = (sqlQuery.substring(0, sqlQuery.length() - 1) + " ;").trim();
                    System.out.println(sqlQuery);
                    Query query = sqlToMongo.transform(sqlQuery);
                    List<Object> users = repository.read(query, User.class);
                    System.out.println(users);
                    sqlQuery = "";
                }
            }
        }
    }

    private boolean isStopPhrase(String sqlQuery) {
        return sqlQuery.equalsIgnoreCase("exit");
    }
}
