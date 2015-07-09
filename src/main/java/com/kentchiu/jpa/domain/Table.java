package com.kentchiu.jpa.domain;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private String name;
    private String primaryKey;
    private String comment;
    private List<Column> columns = new ArrayList<Column>();

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("");
        sb.append(name).append("\t").append(comment).append("\n");
        for (Column column : columns) {
            sb.append("\t").append(column).append("\n");
        }
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table table = (Table) o;

        return name.equals(table.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
