package co.oleh.mongoreadsqlshell;

import co.oleh.mongoreadsqlshell.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommonMongoRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public void read(){
        Query query = new Query();

        List<User> users = mongoTemplate.find(query, User.class);
        System.out.println(users);
    }
}
