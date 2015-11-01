package com.kentchiu.jpa.generator;

import com.kentchiu.jpa.domain.Table;

public class ExtendDetailControllerTestGenerator extends AbstractExtendDetailGenerator {

    public ExtendDetailControllerTestGenerator(Transformer transformer) {
        super(transformer);
    }

    @Override
    protected boolean isTest() {
        return true;
    }


    protected String getClassName(Table table) {
        return transformer.getDomainName(table.getName()) + "ControllerTest";
    }


    protected String getTemplate() {
        return "extend_detail_controller_test.mustache";
    }


}
