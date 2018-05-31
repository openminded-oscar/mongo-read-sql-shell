package co.oleh.mongoreadsqlshell.repositories;

import co.oleh.mongoreadsqlshell.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<?> read(Query query){
        return mongoTemplate.find(query, User.class);
    }
}
