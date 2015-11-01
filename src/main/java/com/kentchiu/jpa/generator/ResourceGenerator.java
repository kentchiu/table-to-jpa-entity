package com.kentchiu.jpa.generator;

import com.kentchiu.jpa.domain.Table;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class ResourceGenerator extends AbstractMasterGenerator {


    public ResourceGenerator(Transformer transformer) {
        super(transformer);
    }

    @Override
    protected boolean isTest() {
        return true;
    }

    public Optional<Path> export(Table table) {
        return exportToFile(table, applyTemplate(table));
    }


    protected String getTemplate() {
        return "resource.mustache";
    }

    protected Optional<Path> exportToFile(Table table, List<String> lines) {
        return exportApiDocument(table, lines);
    }


    protected String getClassName(Table table) {
        return transformer.getDomainName(table.getName());
    }

}
