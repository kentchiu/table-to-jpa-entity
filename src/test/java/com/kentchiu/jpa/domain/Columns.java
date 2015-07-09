package com.kentchiu.jpa.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Columns {
    private Columns() {
    }


    public static Column bigDecimalColumn() {
        Column result = new Column();
        result.setNullable(true);
        result.setName("BIG_DECIMAL_PROPERTY");
        result.setComment("this is a big decimal property");
        result.setDefaultValue("BigDecimal.ZERO");
        result.setJavaType(BigDecimal.class.getName());
        return result;
    }

    public static Column stringColumn() {
        String column1 = "column1";
        String comment = "column comment";
        boolean nullable = true;
        return createStringColumn(column1, comment, nullable);
    }

    public static Column createStringColumn(String name, String comment, boolean nullable) {
        Column result = new Column();
        result.setNullable(nullable);
        result.setName(name);
        result.setComment(comment);
        result.setJavaType(String.class.getName());
        return result;
    }

    public static Column booleanColumn() {
        Column result = new Column();
        result.setNullable(true);
        result.setName("BOOL_PROPERTY");
        result.setComment("this is a boolean property");
        result.setJavaType(Boolean.class.getName());
        return result;
    }

    public static Column dateColumn() {
        Column result = new Column();
        result.setNullable(true);
        result.setName("DATE_PROPERTY");
        result.setComment("this is a date property(format=yyyy-MM-dd)");
        result.setJavaType(Date.class.getName());
        return result;

    }
}
