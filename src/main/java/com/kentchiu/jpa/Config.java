package com.kentchiu.jpa;

public class Config {

    private Class<?> baseClass;
    private Type type = Type.JPA;

    public Config(Type type, Class<?> baseClass) {
        this.baseClass = baseClass;
        this.type = type;
    }

    public Config(Type type) {
        this.type = type;
    }

    public Config() {
    }


    public Class<?> getBaseClass() {
        return baseClass;
    }

    public void setBaseClass(Class<?> baseClass) {
        this.baseClass = baseClass;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}