package com.kentchiu.jpa.domain;

import oracle.sql.TIMESTAMP;

import java.util.ArrayList;
import java.util.List;

public class Tables {

    public static List<Table> all() {
        List<Table> results = new ArrayList<>();
        results.add(table1());
        results.add(table2());
        return results;
    }

    public static Table table1() {
        Table table = createTable("MY_TABLE_1", "my table 1 comment");
        table.setComment("a table comment");
        table.getColumns().add(createColumn("MY_COLUMN_11", "my column 1-1 comment", String.class.getName()));
        table.getColumns().add(createColumn("MY_COLUMN_12", "my column 1-2 comment", String.class.getName(), false));
        table.getColumns().add(createColumn("MY_COLUMN_13", "my column 1-3 comment", TIMESTAMP.class.getName(), false));
        table.setPrimaryKey("MY_COLUMN_11");
        return table;
    }

    public static Table table2() {
        Table table = createTable("MY_TABLE_2", "my table 2 comment");
        table.getColumns().add(createColumn("MY_COLUMN_21", "my column 2-1 comment", String.class.getName()));
        table.getColumns().add(createColumn("MY_COLUMN_22", "my column 2-2 comment", String.class.getName()));
        table.getColumns().add(createColumn("MY_COLUMN_23", "my column 2-3 comment", String.class.getName()));
        return table;
    }

    public static Table master() {
        Table table = createTable("TBL_MASTER", "master table");
        table.getColumns().add(createColumn("MY_COLUMN_1", "my column 1 comment", String.class.getName()));
        table.getColumns().add(createColumn("MY_COLUMN_1", "my column 2 comment", String.class.getName(), false));
        table.getColumns().add(createColumn("MY_COLUMN_1", "my column 3 comment", TIMESTAMP.class.getName(), false));
        table.setPrimaryKey("MY_COLUMN_1");
        return table;
    }


    public static Table detail() {
        Table table = createTable("TBL_DETAIL", "detail table");
        table.getColumns().add(createColumn("MY_COLUMN_1_1", "my column 1-1 comment", String.class.getName()));
        table.getColumns().add(createColumn("MY_COLUMN_1_2", "my column 1-2 comment", String.class.getName(), false));
        table.getColumns().add(createColumn("MY_COLUMN_1_2", "my column 1-3 comment", TIMESTAMP.class.getName(), false));
        table.setPrimaryKey("MY_COLUMN_1_1");
        return table;
    }

    public static Table extendDetail() {
        Table table = createTable("TBL_EXTEND_DETAIL", "extend_detail table");
        table.getColumns().add(createColumn("MY_COLUMN_1_1_1", "my column 1_1-1 comment", String.class.getName()));
        table.getColumns().add(createColumn("MY_COLUMN_1_1_2", "my column 1_1-2 comment", String.class.getName(), false));
        table.getColumns().add(createColumn("MY_COLUMN_1_1_2", "my column 1_1-3 comment", TIMESTAMP.class.getName(), false));
        table.setPrimaryKey("MY_COLUMN_1_1_1");
        return table;
    }


    private static Table createTable(String name, String comment) {
        Table table = new Table();
        table.setName(name);
        table.setComment(comment);
        return table;
    }

    private static Column createColumn(String name, String comment, String type) {
        return createColumn(name, comment, type, true);
    }

    private static Column createColumn(String name, String comment, String type, boolean nullable) {
        Column c = new Column();
        c.setName(name);
        c.setJavaType(type);
        c.setComment(comment);
        c.setNullable(nullable);
        return c;
    }
}
