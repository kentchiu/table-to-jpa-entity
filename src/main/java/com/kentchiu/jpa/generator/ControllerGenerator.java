package com.kentchiu.jpa.generator;

public class ControllerGenerator extends AbstractMasterGenerator {


    public ControllerGenerator(Transformer transformer) {
        super(transformer);
    }


    protected String getTemplate() {
        return "controller.mustache";
    }


}
