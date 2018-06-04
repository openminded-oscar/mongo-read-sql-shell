package co.oleh.mongoreadsqlshell;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.JsonViewModule;
import com.monitorjbl.json.Match;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.monitorjbl.json.Match.match;

@Component
public class ObjectToJsonStringProjector {
    private ObjectMapper objectMapper;

    public ObjectToJsonStringProjector() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JsonViewModule());
    }


    public String project(Object object, Class clazz, List<String> fields) throws JsonProcessingException {
        Match match = match().exclude("*").include(fields.toArray(new String[fields.size()]));
        JsonView jsonView = JsonView.with(object).onClass(clazz, match);

        return objectMapper.writeValueAsString(jsonView);
    }

    public String projectList(List<? extends Object> objects, Class clazz, List<String> fields) throws JsonProcessingException {
        String collectionProjection = "";

        for (Object o : objects) {
            collectionProjection += project(o, clazz, fields);
        }

        return collectionProjection;
    }
}
