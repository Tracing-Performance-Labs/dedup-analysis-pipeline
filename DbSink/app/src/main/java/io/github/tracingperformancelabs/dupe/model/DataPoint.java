package io.github.tracingperformancelabs.dupe.model;

public class DataPoint<ID> {
    private ID id;
    private String content;
    private String source;

    public DataPoint(ID id, String content, String source) {
        this.id = id;
        this.content = content;
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public String getContent() {
        return content;
    }

    public ID getId() {
        return id;
    }
}
