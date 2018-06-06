package co.oleh.mongoreadsqlshell.components;

import co.oleh.mongoreadsqlshell.repositories.GenericDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongoReader {
    @Autowired
    private GenericDocumentRepository repository;

    public List<Object> readBySelectQuery(SelectQuery selectQuery) {
        Query mongoQuery = new Query();

        List<String> fields = selectQuery.getProjection();
        List<Sort> sorts = selectQuery.getOrderBy();
        Criteria criteria = selectQuery.getConditions();
        Integer limit = selectQuery.getLimit();
        Integer skip = selectQuery.getSkip();

        if (!(fields.size() == 1 && fields.get(0).equals("*"))) {
            mongoQuery.fields().exclude("id");
            for (String field : fields) {
                if (field.endsWith(".*")) {
                    field = field.substring(0, field.length() - 2);
                }
                mongoQuery.fields().include(field);
            }
        }
        if (criteria != null) {
            mongoQuery.addCriteria(criteria);
        }
        if (sorts != null) {
            for (Sort sort : sorts) {
                mongoQuery.with(sort);
            }
        }
        if (limit != null) {
            mongoQuery.limit(limit);
        }
        if (skip != null) {
            mongoQuery.limit(skip);
        }

        List<Object> objects = repository.read(mongoQuery, selectQuery.getFrom());
        return objects;
    }
}
