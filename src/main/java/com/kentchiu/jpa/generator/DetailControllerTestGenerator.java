package com.kentchiu.jpa.generator;

import com.kentchiu.jpa.domain.Table;

public class DetailControllerTestGenerator extends AbstractDetailGenerator {

    public DetailControllerTestGenerator(Transformer transformer) {
        super(transformer);
    }

    @Override
    protected boolean isTest() {
        return true;
    }


    protected String getClassName(Table table) {
        return transformer.getDomainName(table.getName()) + "ControllerTest";
    }


    @Override
    protected String getTemplate() {
        return "detail_controller_test.mustache";
    }


}
