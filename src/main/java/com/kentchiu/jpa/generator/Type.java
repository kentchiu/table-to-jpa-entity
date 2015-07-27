package com.kentchiu.jpa.generator;

public enum Type {
    JPA, INPUT, UPDATE, QUERY;

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
                return "service.query";
            default:
                throw new IllegalStateException("Unknown type :" + this);
        }
    }
}
