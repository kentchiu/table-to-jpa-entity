package com.kentchiu.jpa;

import com.kentchiu.jpa.domain.Table;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public abstract class AbstractControllerGenerator extends AbstractGenerator {


    public AbstractControllerGenerator(Transformer transformer) {
        super(transformer);
    }

    public Optional<Path> export(Table table) {
        return exportToFile(table, applyTemplate(table));
    }

    protected abstract List<String> applyTemplate(Table table);


    protected String getClassName(Table table) {
        return transformer.getDomainName(table.getName()) + "Controller";
    }

    protected String getPackageName(Table table) {
        return transformer.getTopPackage(table.getName()) + "." + transformer.getModuleName(table.getName()) + ".web";
    }
}
