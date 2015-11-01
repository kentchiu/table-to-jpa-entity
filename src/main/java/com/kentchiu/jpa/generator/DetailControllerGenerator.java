package com.kentchiu.jpa.generator;

public class DetailControllerGenerator extends AbstractDetailGenerator {

    public DetailControllerGenerator(Transformer transformer) {
        super(transformer);
    }

    @Override
    protected String getTemplate() {
        return "detail_controller.mustache";
    }

}



