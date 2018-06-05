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

    @Test
    public void testParsingSimpleSelect(){
        SelectQuery selectQuery =
                selectQueryParser.parse("SELECT fname FROM user ;");

        assertEquals(Arrays.asList("fname"), selectQuery.getProjection());
        assertEquals(User.class, selectQuery.getFrom());
    }

    @Test
    public void testParsingCriteria(){
        SelectQuery selectQuery =
                selectQueryParser.parse("SELECT fname FROM user WHERE fname=\"peter\" ;");

        assertEquals(Criteria.where("fname").is("peter"), selectQuery.getConditions());
    }

    @Test
    public void testParsingComplexCriteria(){
        SelectQuery selectQuery =
                selectQueryParser.parse("SELECT fname FROM user WHERE fname=\"peter\" OR fname=\"thomas\" ;");
        assertEquals(Criteria.where("fname").is("thomas").orOperator(Criteria.where("fname").is("peter")), selectQuery.getConditions());

        selectQuery =
                selectQueryParser.parse("SELECT fname FROM user WHERE fname=\"peter\" AND lname=\"soyer\" ;");
        assertEquals(Criteria.where("lname").is("soyer").andOperator(Criteria.where("fname").is("peter")), selectQuery.getConditions());
    }

    @Test
    public void testParsingLimit(){
        SelectQuery selectQuery =
                selectQueryParser.parse("SELECT fname FROM user WHERE fname=\"peter\" OR fname=\"thomas\" LIMIT 1 ;");
        assertEquals((Integer) 1, selectQuery.getLimit());
    }

    @Test
    public void testParsingSkip(){
        SelectQuery selectQuery =
                selectQueryParser.parse("SELECT fname FROM user WHERE fname=\"peter\" OR fname=\"thomas\" LIMIT 1 SKIP 2 ;");
        assertEquals((Integer) 2, selectQuery.getSkip());
    }

    @Test
    public void testParsingOrderBy(){
        SelectQuery selectQuery =
                selectQueryParser.parse("SELECT fname, lname FROM user WHERE fname=\"peter\" ORDER BY lname, fname DESC ;");
        List<Sort> expectedOrdering =  Arrays.asList(new Sort(Sort.Direction.ASC, "lname"), new Sort(Sort.Direction.DESC, "fname"));
        assertEquals(expectedOrdering, selectQuery.getOrderBy());
    }

    @Test
    public void testParsingAltogether(){
        SelectQuery selectQueryActual =
                selectQueryParser.parse("SELECT fname, lname FROM user WHERE fname=\"peter\" AND lname=\"soyer\" LIMIT 1 SKIP 2 ORDER BY lname ;");

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
