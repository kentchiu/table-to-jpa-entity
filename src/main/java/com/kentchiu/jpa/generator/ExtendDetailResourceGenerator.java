package com.kentchiu.jpa.generator;

import com.kentchiu.jpa.domain.Table;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class ExtendDetailResourceGenerator extends AbstractExtendDetailGenerator {

    public ExtendDetailResourceGenerator(Transformer transformer) {
        super(transformer);
    }

    @Override
    protected String getTemplate() {
        return "detail_resource.mustache";
    }

    @Override
    protected boolean isTest() {
        return true;
    }


    protected String getClassName(Table table) {
        return transformer.getDomainName(table.getName());
    }


    protected Optional<Path> exportToFile(Table table, List<String> lines) {
        return exportApiDocument(table, lines);
    }


}
