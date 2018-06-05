package co.oleh.mongoreadsqlshell;

public class SelectStringBuilder {
    private String buffer = "";

    public SelectStringBuilder withProjection(String projection, String from) {
        buffer += "SELECT " + projection + " FROM " + from;
        return this;
    }

    public SelectStringBuilder orderBy(String orderByString) {
        buffer += " ORDER BY " + orderByString;
        return this;
    }

    public SelectStringBuilder limit(Integer limit) {
        buffer += " LIMIT " + limit;
        return this;
    }

    public SelectStringBuilder skip(Integer offset) {
        buffer += " SKIP " + offset;
        return this;
    }

    public SelectStringBuilder withConditions(String conditionString) {
        buffer += " WHERE " + conditionString;
        return this;
    }

    public String build() {
        String value = buffer;
        value += " ;";
        reset();
        return value;
    }

    public void reset() {
        buffer = "";
    }
}
