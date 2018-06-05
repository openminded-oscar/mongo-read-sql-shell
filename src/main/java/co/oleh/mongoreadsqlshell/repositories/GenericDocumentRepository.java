package co.oleh.mongoreadsqlshell.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GenericDocumentRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Object> read(Query query, Class clazz){
        List<Object> objects = new ArrayList<>();
        mongoTemplate.find(query, clazz)
                .stream()
                .forEach(object -> objects.add(object));

        return objects;
    }
}
