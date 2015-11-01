package com.kentchiu.jpa.generator;

import com.kentchiu.jpa.domain.Table;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class DetailResourceGenerator extends AbstractDetailGenerator {

    public DetailResourceGenerator(Transformer transformer) {
        super(transformer);
    }

    @Override
    protected boolean isTest() {
        return true;
    }


    protected String getClassName(Table table) {
        return transformer.getDomainName(table.getName());
    }


    protected String getTemplate() {
        return "detail_resource.mustache";
    }

    protected Optional<Path> exportToFile(Table table, List<String> lines) {
        return exportApiDocument(table, lines);
    }


}
