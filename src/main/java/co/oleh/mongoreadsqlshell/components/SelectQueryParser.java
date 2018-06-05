package co.oleh.mongoreadsqlshell.components;

import co.oleh.mongoreadsqlshell.models.entities.Car;
import co.oleh.mongoreadsqlshell.models.entities.RealtyObject;
import co.oleh.mongoreadsqlshell.models.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SelectQueryParser {
    private static final Pattern PROJECTION_PATTERN = Pattern.compile("^SELECT\\s+(.+)\\s+FROM\\s+.*");

    private static final Pattern LIMIT_PATTERN = Pattern.compile("\\s+LIMIT\\s+(.+)\\s+");
    private static final Pattern SKIP_PATTERN = Pattern.compile("\\s+SKIP\\s+(.+)\\s+");

    private static final Pattern ORDER_BY_PATTERN = Pattern.compile("\\s+ORDER\\s+BY\\s+(.+)\\s+(LIMIT|SKIP|;)");

    private static final Pattern FROM_PATTERN = Pattern.compile("\\s+FROM\\s+(.+)\\s+((ORDER(\\s+)BY)|WHERE|LIMIT|SKIP|;)");
    private static final Pattern WHERE_PATTERN = Pattern.compile("\\s+WHERE\\s+(.+)\\s+((ORDER(\\s+)BY)|LIMIT|SKIP|;)");
    private static final Pattern CONDITIONS_SPLIT_PATTERN = Pattern.compile("(.+)\\s+(AND|OR)\\s+(.+)");
    private static final Pattern CONDITION_PATTERN = Pattern.compile("\\s*(.+)(=|<>|>|>=|<|<=)(.+)\\s*");


    public SelectQuery parse(String sqlString) {
        SelectQuery selectQuery = new SelectQuery();

        selectQuery.setProjection(parseFields(sqlString));
        selectQuery.setFrom(parseFrom(sqlString));
        selectQuery.setConditions(parseWhere(sqlString));
        selectQuery.setSkip(parseSkip(sqlString));
        selectQuery.setLimit(parseLimit(sqlString));
        selectQuery.setOrderBy(parseOrderBy(sqlString));

        return selectQuery;
    }

    private Class parseFrom(String sqlString) {
        Matcher matcher = WHERE_PATTERN.matcher(sqlString);

        if (matcher.matches()) {
            String type = matcher.group(1).trim();
            switch (type) {
                case "user":
                case "USER":
                    return User.class;
                case "realty":
                case "REALTY":
                    return RealtyObject.class;
                case "car":
                case "CAR":
                    return Car.class;
                default:
                    throw new RuntimeException("Type not supported");
            }
        }

        return null;
    }

    private Criteria parseWhere(String sqlQuery) {
        Matcher matcher = WHERE_PATTERN.matcher(sqlQuery);
        if (matcher.matches()) {
            return parseCriteria(matcher.group(1));
        }

        return null;
    }

    private Criteria parseCriteria(String sqlQuery) {
        Matcher conditionsCombinationMatcher = CONDITIONS_SPLIT_PATTERN.matcher(sqlQuery);
        Criteria criteria;
        // if complex condition then recursion
        if (conditionsCombinationMatcher.matches()) {
            String lastCondition = conditionsCombinationMatcher.group(3).trim();
            String criteriaSeparator = conditionsCombinationMatcher.group(2).trim();
            String allConditionsBefore = conditionsCombinationMatcher.group(1).trim();

            criteria = parseSingleCondition(lastCondition);

            if (criteriaSeparator.equals("AND")) {
                criteria.andOperator(parseCriteria(allConditionsBefore));
            } else if (criteriaSeparator.equals("OR")) {
                criteria.orOperator(parseCriteria(allConditionsBefore));
            }
        } else {
            criteria = parseSingleCondition(sqlQuery);
        }

        return criteria;
    }

    private Criteria parseSingleCondition(String condition) {
        Matcher conditionMatcher = CONDITION_PATTERN.matcher(condition);

        if (conditionMatcher.matches()) {
            String operand1 = conditionMatcher.group(1).trim();
            String operator = conditionMatcher.group(2).trim();
            String operand2 = conditionMatcher.group(3).trim();
            switch (operator) {
                case "=":
                    return Criteria.where(operand1).is(operand2);
                case "<>":
                    return Criteria.where(operand1).ne(operand2);
                case ">":
                    return Criteria.where(operand1).gt(operand2);
                case ">=":
                    return Criteria.where(operand1).gte(operand2);
                case "<":
                    return Criteria.where(operand1).lt(operand2);
                case "<=":
                    return Criteria.where(operand1).lte(operand2);
            }
        } else {
            throw new RuntimeException(condition + " does not match to the condition pattern");
        }

        return null;
    }

    private List<Sort> parseOrderBy(String sqlQuery) {
        Matcher matcher = ORDER_BY_PATTERN.matcher(sqlQuery);

        if (matcher.matches()) {
            String orderBy = matcher.group(1);
            String[] orderings = orderBy.split(",");
            List<Sort> orderingObjects = new ArrayList<>();

            for (String ordering : orderings) {
                String[] orderingComponents = ordering.trim().split("\\s+");
                String property = orderingComponents[0].trim();
                Sort.Direction direction = (orderingComponents.length > 1 && orderingComponents[1].trim().startsWith("DESC"))
                        ? Sort.Direction.DESC : Sort.Direction.ASC;
                orderingObjects.add(Sort.by(new Sort.Order(direction, property)));
            }

            return orderingObjects;
        }

        return null;
    }

    private List<String> parseFields(String sqlQuery) {
        Matcher matcher = PROJECTION_PATTERN.matcher(sqlQuery);

        if (matcher.matches()) {
            List<String> fieldsList = new ArrayList<>();
            String[] fields = matcher.group(1).trim().split(",");

            for (String field : fields) {
                fieldsList.add(field.trim());
            }
            return fieldsList;
        } else {
            throw new RuntimeException("Query parsing exception. Check the projection part");
        }
    }

    private Integer parseLimit(String sqlQuery) {
        Matcher matcher = LIMIT_PATTERN.matcher(sqlQuery);

        if (matcher.matches()) {
            return Integer.valueOf(matcher.group(1).trim());
        }

        return null;
    }

    private Integer parseSkip(String sqlQuery) {
        Matcher matcher = SKIP_PATTERN.matcher(sqlQuery);

        if (matcher.matches()) {
            return Integer.valueOf(matcher.group(1).trim());
        }

        return null;
    }
}
