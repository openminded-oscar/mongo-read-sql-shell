package co.oleh.mongoreadsqlshell;

import co.oleh.mongoreadsqlshell.components.SelectQuery;
import co.oleh.mongoreadsqlshell.components.SelectQueryParser;
import co.oleh.mongoreadsqlshell.models.entities.User;
import org.junit.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SqlParsingTest {
    private SelectQueryParser selectQueryParser = new SelectQueryParser();
    private SelectStringBuilder sb = new SelectStringBuilder();

    @Test
    public void testParsingSimpleSelect() {
        SelectQuery selectQuery =
                selectQueryParser.parse(sb
                        .withProjection("fname", "user")
                        .build());

        assertEquals(Arrays.asList("fname"), selectQuery.getProjection());
        assertEquals(User.class, selectQuery.getFrom());
    }

    @Test
    public void testParsingCriteria() {
        SelectQuery selectQuery =
                selectQueryParser.parse(sb
                        .withProjection("fname", "user")
                        .withConditions("fname=\"peter\"")
                        .build());

        assertEquals(Criteria.where("fname").is("peter"), selectQuery.getConditions());
    }

    @Test
    public void testParsingComplexCriteria() {
        SelectQuery selectQuery =
                selectQueryParser.parse(sb
                        .withProjection("fname", "user")
                        .withConditions("fname=\"peter\" OR fname=\"thomas\"")
                        .build());
        assertEquals(Criteria.where("fname").is("thomas").orOperator(Criteria.where("fname").is("peter")), selectQuery.getConditions());

        selectQuery =
                selectQueryParser.parse(sb
                        .withProjection("fname", "user")
                        .withConditions("fname=\"peter\" AND lname=\"soyer\"")
                        .build());
        assertEquals(Criteria.where("lname").is("soyer").andOperator(Criteria.where("fname").is("peter")), selectQuery.getConditions());
    }

    @Test
    public void testParsingLimit() {
        SelectQuery selectQuery =
                selectQueryParser.parse(sb.withProjection("fname", "user")
                        .withConditions("fname=\"peter\" OR fname=\"thomas\"")
                        .limit(1)
                        .build());

        assertEquals((Integer) 1, selectQuery.getLimit());
    }

    @Test
    public void testParsingSkip() {
        SelectQuery selectQuery =
                selectQueryParser.parse(sb
                        .withProjection("fname", "user")
                        .withConditions("fname=\"peter\" OR fname=\"thomas\"")
                        .limit(1)
                        .skip(2)
                        .build());

        assertEquals((Integer) 2, selectQuery.getSkip());
    }

    @Test
    public void testParsingOrderBy() {
        SelectQuery selectQuery =
                selectQueryParser.parse(sb
                        .withProjection("fname, lname", "user")
                        .withConditions("fname=\"peter\"")
                        .orderBy("lname, fname DESC")
                        .build());

        List<Sort> expectedOrdering = Arrays.asList(new Sort(Sort.Direction.ASC, "lname"), new Sort(Sort.Direction.DESC, "fname"));
        assertEquals(expectedOrdering, selectQuery.getOrderBy());
    }

    @Test
    public void testParsingAltogether() {
        SelectQuery selectQueryActual =
                selectQueryParser.parse(sb
                        .withProjection("fname, lname", "user")
                        .withConditions("fname=\"peter\" AND lname=\"soyer\"")
                        .limit(1)
                        .skip(2)
                        .orderBy("lname")
                        .build());

        SelectQuery expectedQuery = new SelectQuery();
        expectedQuery.setProjection(Arrays.asList("fname", "lname"));
        expectedQuery.setFrom(User.class);
        expectedQuery.setLimit(1);
        expectedQuery.setSkip(2);
        expectedQuery.setConditions(Criteria.where("lname").is("soyer").andOperator(Criteria.where("fname").is("peter")));
        expectedQuery.setOrderBy(Arrays.asList(new Sort(Sort.Direction.ASC, "lname")));

        assertEquals(expectedQuery, selectQueryActual);
    }
}
