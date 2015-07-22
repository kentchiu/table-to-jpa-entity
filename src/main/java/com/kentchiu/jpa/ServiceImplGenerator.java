package com.kentchiu.jpa;

import com.kentchiu.jpa.domain.Table;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class ServiceImplGenerator extends AbstractGenerator {

    public ServiceImplGenerator(Config config) {
        super(config);
    }


    public Optional<Path> export(Table table) {
        return exportToFile(table, applyTemplate(table));
    }

    List<String> applyTemplate(Table table) {
        List<String> lines = applyTemplate("service_impl.mustache", getBaseContext(table));
        lines.forEach(System.out::println);
        return lines;
    }

    protected String getClassName(Table table) {
        return transformer.getDomainName(table.getName()) + "ServiceImpl";
    }

    protected String getPackageName(Table table) {
        return transformer.getTopPackage(table.getName()) + "/service";
    }
}
