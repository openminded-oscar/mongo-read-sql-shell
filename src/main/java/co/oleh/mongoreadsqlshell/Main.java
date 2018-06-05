package co.oleh.mongoreadsqlshell;

import co.oleh.mongoreadsqlshell.components.MongoReader;
import co.oleh.mongoreadsqlshell.components.ObjectToJsonStringProjector;
import co.oleh.mongoreadsqlshell.components.SelectQueryParser;
import co.oleh.mongoreadsqlshell.components.SelectQuery;
import co.oleh.mongoreadsqlshell.models.entities.Car;
import co.oleh.mongoreadsqlshell.models.entities.RealtyObject;
import co.oleh.mongoreadsqlshell.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Main implements CommandLineRunner {
    @Autowired
    private SelectQueryParser parser;

    @Autowired
    private ObjectToJsonStringProjector projector;

    @Autowired
    private MongoReader mongoReader;

    private final List<Class> supportedClasses = Arrays.asList(User.class, Car.class, RealtyObject.class);

    @Override
    public void run(String... args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        printMenuAndSupportedClasses();
        processQueriesOneByOne(br);
    }

    private void processQueriesOneByOne(BufferedReader br) throws IOException {
        String sqlQuery = "";
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

    private void printMenuAndSupportedClasses() {
        System.out.println("Welcome to MongoDB alternative (SQL-based) client. Type desired SELECT queries or \"exit\"");

        System.out.println("Supported entities list:");
        for (Class clazz : supportedClasses) {
            System.out.print("Class " + clazz.getSimpleName().toLowerCase() + " - ");
            System.out.println(Arrays.asList(clazz.getDeclaredFields())
                    .stream()
                    .map(Field::getName)
                    .collect(Collectors.joining(",")));
        }
    }

    private boolean isStopPhrase(String sqlQuery) {
        return sqlQuery.equalsIgnoreCase("exit");
    }
}
