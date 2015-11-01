package com.kentchiu.jpa.generator;

import com.kentchiu.jpa.domain.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class ResourceDetailGenerator extends AbstractDetailGenerator {

    private Logger logger = LoggerFactory.getLogger(ResourceDetailGenerator.class);

    public ResourceDetailGenerator(Transformer transformer) {
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
        return "resource_detail.mustache";
    }

    protected Optional<Path> exportToFile(Table table, List<String> lines) {
        return exportApiDocument(table, lines);
    }


}
