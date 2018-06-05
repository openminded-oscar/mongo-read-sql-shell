package co.oleh.mongoreadsqlshell.components;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
public class SelectQuery {
    private List<String> projection;
    private Class from;
    private Criteria conditions;
    private Integer limit;
    private Integer skip;
    private List<Sort> orderBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectQuery that = (SelectQuery) o;
        return Objects.equals(projection, that.projection) &&
                Objects.equals(from, that.from) &&
                Objects.equals(conditions, that.conditions) &&
                Objects.equals(limit, that.limit) &&
                Objects.equals(skip, that.skip) &&
                Objects.equals(orderBy, that.orderBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projection, from, conditions, limit, skip, orderBy);
    }
}
