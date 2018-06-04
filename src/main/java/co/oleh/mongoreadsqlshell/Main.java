package co.oleh.mongoreadsqlshell;

import co.oleh.mongoreadsqlshell.components.MongoReader;
import co.oleh.mongoreadsqlshell.components.ObjectToJsonStringProjector;
import co.oleh.mongoreadsqlshell.components.SelectQuery;
import co.oleh.mongoreadsqlshell.components.SelectQueryParser;
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
    private SelectQueryParser parser;

    @Autowired
    private ObjectToJsonStringProjector projector;

    @Autowired
    private MongoReader mongoReader;

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
                    try {
                        sqlQuery = (sqlQuery.substring(0, sqlQuery.length() - 1) + " ;").trim();
                        SelectQuery parsedQuery = parser.parse(sqlQuery);
                        List<Object> objects = mongoReader.readBySelectQuery(parsedQuery);
                        System.out.println(projector.projectList(objects, parsedQuery.getFrom(), parsedQuery.getProjection()));
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    sqlQuery = "";
                }
            }
        }
    }

    private boolean isStopPhrase(String sqlQuery) {
        return sqlQuery.equalsIgnoreCase("exit");
    }
}
