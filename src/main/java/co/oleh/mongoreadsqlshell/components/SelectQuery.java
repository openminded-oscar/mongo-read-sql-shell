package co.oleh.mongoreadsqlshell.components;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

@Getter
@Setter
public class SelectQuery {
    private List<String> projection;
    private Class from;
    private Criteria conditions;
    private Integer limit;
    private Integer skip;
    private List<Sort> orderBy;
}
