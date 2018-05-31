package co.oleh.mongoreadsqlshell;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class SqlToMongoQueryTransformer {
    public Query transform(String sqlQuery){
        return new Query();
    }
}
