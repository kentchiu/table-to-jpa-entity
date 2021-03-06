package com.kentchiu.jpa.generator;

public class Config {

    private Class<?> baseClass = Object.class;
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