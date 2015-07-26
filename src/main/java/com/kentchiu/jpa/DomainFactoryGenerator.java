package com.kentchiu.jpa;

import com.kentchiu.jpa.domain.Table;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class DomainFactoryGenerator extends AbstractGenerator {

    public DomainFactoryGenerator(Transformer transformer) {
        super(transformer);
    }

    @Override
    protected boolean isTest() {
        return true;
    }

    public Optional<Path> export(Table table) {
        return exportToFile(table, applyTemplate(table));
    }

    List<String> applyTemplate(Table table) {
        return applyTemplate("domain_factory.mustache", getBaseContext(table));
    }

    protected String getClassName(Table table) {
        return transformer.getDomainName(table.getName()) + "s";
    }

    protected String getPackageName(Table table) {
        return transformer.getTopPackage(table.getName()) + "." + transformer.getModuleName(table.getName()) + ".domain";
    }
}
