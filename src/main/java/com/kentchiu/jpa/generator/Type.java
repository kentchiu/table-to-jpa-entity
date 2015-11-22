package com.kentchiu.jpa.generator;

public enum Type {
    JPA, INPUT, UPDATE, QUERY, QUERY_TEST;

    public String getTemplateName() {
        switch (this) {
            case JPA:
                return "entity";
            case INPUT:
                return "input";
            case UPDATE:
                return "updateInput";
            case QUERY:
                return "query";
            case QUERY_TEST:
                return "query_test";
            default:
                throw new IllegalStateException("Unknown type :" + this);
        }
    }

    public String getClassPostFix() {
        switch (this) {
            case JPA:
                return "";
            case INPUT:
                return "Input";
            case UPDATE:
                return "UpdateInput";
            case QUERY:
                return "Query";
            case QUERY_TEST:
                return "QueryTest";
            default:
                throw new IllegalStateException("Unknown type :" + this);
        }
    }

    public String getPackage() {
        switch (this) {
            case JPA:
                return "domain";
            case INPUT:
                return "web.dto";
            case UPDATE:
                return "web.dto";
            case QUERY:
            case QUERY_TEST:
                return "service.query";
            default:
                throw new IllegalStateException("Unknown type :" + this);
        }
    }
}
