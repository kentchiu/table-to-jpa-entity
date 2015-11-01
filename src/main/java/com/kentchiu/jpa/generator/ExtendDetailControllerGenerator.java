package com.kentchiu.jpa.generator;

public class ExtendDetailControllerGenerator extends AbstractExtendDetailGenerator {

    public ExtendDetailControllerGenerator(Transformer transformer) {
        super(transformer);
    }

    protected String getTemplate() {
        return "extend_detail_controller.mustache";
    }

}



