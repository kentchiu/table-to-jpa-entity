package com.kentchiu.jpa.generator;

import com.kentchiu.jpa.domain.Table;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class ServiceImplGenerator extends AbstractGenerator {


    public ServiceImplGenerator(Transformer transformer) {
        super(transformer);
    }

    public Optional<Path> export(Table table) {
        return exportToFile(table, applyTemplate(table));
    }

    List<String> applyTemplate(Table table) {
        return applyTemplate("service_impl.mustache", getBaseContext(table));
    }

    protected String getClassName(Table table) {
        return transformer.getDomainName(table.getName()) + "ServiceImpl";
    }

    protected String getPackageName(Table table) {
        return transformer.getTopPackage(table.getName()) + "." + transformer.getModuleName(table.getName()) + ".service";
    }
}
