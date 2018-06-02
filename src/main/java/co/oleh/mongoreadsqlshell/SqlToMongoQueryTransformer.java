package co.oleh.mongoreadsqlshell;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SqlToMongoQueryTransformer {
    private static final Pattern NUMBER_INSIDE_STRING_PATTERN = Pattern.compile(".*\\s+([0-9]+)\\s+.*");
    private static final Pattern PROJECTION_PATTERN = Pattern.compile("^SELECT\\s+(.+)\\s+FROM\\s+.*");
    private static final Pattern WHERE_PATTERN = Pattern.compile("\\s+WHERE\\s+(.+)\\s+");
    private static final Pattern LIMIT_PATTERN = Pattern.compile("\\s+LIMIT\\s+(.+)\\s+");
    private static final Pattern SKIP_PATTERN = Pattern.compile("\\s+SKIP\\s+(.+)\\s+");

    public Query transform(String sqlQuery) {
        Query mongoQuery = new Query();
        mongoQuery = parseAndSetFields(sqlQuery, mongoQuery);
        mongoQuery = parseAndSetWhere(sqlQuery, mongoQuery);
        mongoQuery = parseAndSetSkipIfPassed(sqlQuery, mongoQuery);
        mongoQuery = parseAndSetLimitIfPassed(sqlQuery, mongoQuery);
//        mongoQuery = setOrdering(sqlQuery, mongoQuery);

        return mongoQuery;
    }

    private Query parseAndSetWhere(String sqlQuery, Query mongoQuery) {
        Matcher matcher = WHERE_PATTERN.matcher(sqlQuery);

        if (matcher.matches()) {
            String condition = matcher.group(1);
            // =, <>, >, >=, <, <= needs to be supported
        }

        return mongoQuery;
    }

    private Query parseAndSetFields(String sqlQuery, Query mongoQuery) {
        Matcher matcher = PROJECTION_PATTERN.matcher(sqlQuery);

        if (matcher.matches()) {
            String[] fields = matcher.group(1).split(",");

            if (!((fields.length == 1) && (fields[0].equals("*")))) {
                mongoQuery.fields().exclude("id");
                for (String field : fields) {
                    mongoQuery.fields().include(field.trim());
                }
            }
        } else {
            throw new RuntimeException("Query parsing exception. Check the projection part");
        }

        return mongoQuery;
    }

    private Query parseAndSetLimitIfPassed(String sqlQuery, Query mongoQuery) {
        Matcher matcher = LIMIT_PATTERN.matcher(sqlQuery);
        if (matcher.matches()) {
            int limitAmount = Integer.valueOf(matcher.group(1));
            mongoQuery.limit(limitAmount);
        }

        return mongoQuery;
    }

    private Query parseAndSetSkipIfPassed(String sqlQuery, Query mongoQuery) {
        Matcher matcher = SKIP_PATTERN.matcher(sqlQuery);
        if (matcher.matches()) {
            int skipAmount = Integer.valueOf(matcher.group(1));
            mongoQuery.skip(skipAmount);
        }

        return mongoQuery;
    }
}
