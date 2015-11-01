package com.kentchiu.jpa.generator;

import com.kentchiu.jpa.domain.Table;

import java.nio.file.Path;
import java.util.Optional;

public class ControllerTestGenerator extends AbstractMasterGenerator {

    public ControllerTestGenerator(Transformer transformer) {
        super(transformer);
    }

    @Override
    protected boolean isTest() {
        return true;
    }

    public Optional<Path> export(Table table) {
        return exportToFile(table, applyTemplate(table));
    }

    @Override
    protected String getTemplate() {
        return "controller_test.mustache";
    }


    protected String getClassName(Table table) {
        return transformer.getDomainName(table.getName()) + "ControllerTest";
    }

}
