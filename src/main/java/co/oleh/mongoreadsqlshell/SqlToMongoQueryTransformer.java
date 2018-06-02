package co.oleh.mongoreadsqlshell;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SqlToMongoQueryTransformer {
    private static final String SELECT = "SELECT";
    private static final String FROM = "FROM";
    private static final String SKIP = "SKIP";
    private static final String LIMIT = "LIMIT";

    private static final Pattern NUMBER_INSIDE_STRING_PATTERN = Pattern.compile(".*\\s+([0-9]+)\\s+.*");

    public Query transform(String sqlQuery) {
        Query mongoQuery = new Query();
        mongoQuery = setFields(sqlQuery, mongoQuery);
        mongoQuery = setSkipIfPassed(sqlQuery, mongoQuery);
        mongoQuery = setLimitIfPassed(sqlQuery, mongoQuery);
        mongoQuery = setCondition(sqlQuery, mongoQuery);
        mongoQuery = setOrdering(sqlQuery, mongoQuery);

        return mongoQuery;
    }

    private Query setLimitIfPassed(String sqlQuery, Query mongoQuery) {
        int limitIndex = sqlQuery.lastIndexOf(LIMIT);
        if (limitIndex >= 0) {
            // checking whether it's the keyword - not part of the field
            if (!(Character.isLetterOrDigit(sqlQuery.charAt(limitIndex - 1)) ||
                    Character.isLetterOrDigit(sqlQuery.charAt(limitIndex + LIMIT.length())))) {
                String afterLimit = sqlQuery.substring(limitIndex + LIMIT.length());
                Matcher m = NUMBER_INSIDE_STRING_PATTERN.matcher(afterLimit);

                if (m.matches()) {
                    int limitAmount = Integer.valueOf(m.group(1));
                    mongoQuery.limit(limitAmount);
                }
            }
        }

        return mongoQuery;
    }

    private Query setSkipIfPassed(String sqlQuery, Query mongoQuery) {
        int skipIndex = sqlQuery.lastIndexOf(SKIP);
        if (skipIndex >= 0) {
            // checking whether it's the keyword - not part of the field
            if (!(Character.isLetterOrDigit(sqlQuery.charAt(skipIndex - 1)) ||
                    Character.isLetterOrDigit(sqlQuery.charAt(skipIndex + SKIP.length())))) {
                String afterSkip = sqlQuery.substring(skipIndex + SKIP.length());
                Matcher m = NUMBER_INSIDE_STRING_PATTERN.matcher(afterSkip);

                if (m.matches()) {
                    int skipAmount = Integer.valueOf(m.group(1));
                    mongoQuery.skip(skipAmount);
                }
            }
        }

        return mongoQuery;
    }

    private Query setFields(String sqlQuery, Query mongoQuery) {
        String fieldsPart = sqlQuery.substring(sqlQuery.indexOf(SELECT) + SELECT.length(), sqlQuery.indexOf(FROM)).trim();
        String[] fields = fieldsPart.split(",");

        if (!((fields.length == 1) && (fields[0].equals("*")))) {
            // disable getting id by default
            mongoQuery.fields().exclude("id");
            for (String field : fields) {
                mongoQuery.fields().include(field.trim());
            }
        }

        return mongoQuery;
    }
}
