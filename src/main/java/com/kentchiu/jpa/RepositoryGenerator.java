package com.kentchiu.jpa;

import com.google.common.collect.Maps;
import com.kentchiu.jpa.domain.Table;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepositoryGenerator extends AbstractGenerator {

    public RepositoryGenerator(Config config) {
        super(config);
    }


    public Optional<Path> export(Table table) {
        return exportToFile(table, applyTemplate(table));
    }

    List<String> applyTemplate(Table table) {
        Map<String, Object> context = Maps.newHashMap();
        context.put("class", transformer.getCalssName(table.getName()));
        context.put("topPackage", transformer.getTopPackage(table.getName()));
        List<String> lines = applyTemplate("repository.mustache", context);
        lines.forEach(System.out::println);
        return lines;
    }
}
