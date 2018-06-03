package co.oleh.mongoreadsqlshell;

import co.oleh.mongoreadsqlshell.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.JsonViewModule;
import org.junit.Test;

import static com.monitorjbl.json.Match.match;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OutputTest {
    private ObjectMapper objectMapper = new ObjectMapper();
    private Configuration config = Configuration.defaultConfiguration()
            .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
    {
        objectMapper.registerModule(new JsonViewModule());
    }

    @Test
    public void testProjection() throws JsonProcessingException {
        String fnameToSet = "Peter";
        String lnameToSet = "Peterson";
        String idToSet = "1";

        User user = new User();
        user.setFname(fnameToSet);
        user.setLname(lnameToSet);
        user.setId(idToSet);

        String json = objectMapper.writeValueAsString(JsonView.with(user).onClass(User.class, match().exclude("*").include("fname")));
        assertEquals(fnameToSet, JsonPath.using(config).parse(json).read("$['fname']", String.class));
        assertNull(JsonPath.using(config).parse(json).read("$['lname']", String.class));
        assertNull(JsonPath.using(config).parse(json).read("$['id']", String.class));
    }
}
