package com.kentchiu.jpa;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

public enum Type {
    JPA, INPUT, UPDATE;

    public String getTemplateName() {
        switch (this) {
            case JPA:
                return "entity";
            case INPUT:
                return "input";
            case UPDATE:
                return "updateInput";
            default:
                throw new IllegalStateException("Unknown type :" + this);
        }
    }

    public String getJavaFileName(String className) {
        Preconditions.checkState(StringUtils.isNoneBlank(className));
        switch (this) {
            case JPA:
                return className;
            case INPUT:
                return className + "Input";
            case UPDATE:
                return className + "UpdateInput";
            default:
                throw new IllegalStateException("Unknown type :" + this);
        }
    }
}
