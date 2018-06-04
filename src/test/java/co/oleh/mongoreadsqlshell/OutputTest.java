package co.oleh.mongoreadsqlshell;

import co.oleh.mongoreadsqlshell.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OutputTest {
    private ObjectToJsonStringProjector projector = new ObjectToJsonStringProjector();

    private Configuration jsonPathConfig = Configuration.defaultConfiguration()
            .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

    @Test
    public void testProjection() throws JsonProcessingException {
        String fnameToSet = "Peter";
        String lnameToSet = "Peterson";
        String idToSet = "1";

        User user = new User();
        user.setFname(fnameToSet);
        user.setLname(lnameToSet);
        user.setId(idToSet);

        String json = projector.project(user, User.class, "fname");
        assertEquals(fnameToSet, JsonPath.using(jsonPathConfig).parse(json).read("$['fname']", String.class));
        assertNull(JsonPath.using(jsonPathConfig).parse(json).read("$['lname']", String.class));
        assertNull(JsonPath.using(jsonPathConfig).parse(json).read("$['id']", String.class));
    }
}
