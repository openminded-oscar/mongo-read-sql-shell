package co.oleh.mongoreadsqlshell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class CommonRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public void read(){
        Query query = new Query();

        mongoTemplate.find(query, null);
    }
}
