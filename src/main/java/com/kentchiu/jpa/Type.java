package com.kentchiu.jpa;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

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

    public String getJavaFileName(String className) {
        Preconditions.checkState(StringUtils.isNoneBlank(className));
        switch (this) {
            case JPA:
                return className + ".java";
            case INPUT:
                return className + "Input.java";
            case UPDATE:
                return className + "UpdateInput.java";
            case QUERY:
                return className + "Query.java";
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
