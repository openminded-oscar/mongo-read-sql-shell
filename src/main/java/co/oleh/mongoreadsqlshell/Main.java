package co.oleh.mongoreadsqlshell;

import co.oleh.mongoreadsqlshell.entities.User;
import co.oleh.mongoreadsqlshell.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
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
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String sqlQuery = "";

        System.out.println("Welcome to MongoDB alternative (SQL-based) client. Type desired SELECT queries or \"exit\"");
        while (true) {
            System.out.print("...");
            sqlQuery = br.readLine();

            if (!isStopPhrase(sqlQuery)) {
                Query query = sqlToMongo.transform(sqlQuery);
                List<User> users = (List<User>) userRepository.read(query);
                System.out.println(users);
            } else {
                break;
            }
        }
    }

    private boolean isStopPhrase(String sqlQuery){
        return sqlQuery.equalsIgnoreCase("exit");
    }
}
