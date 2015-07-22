package com.kentchiu.jpa;

import com.kentchiu.jpa.domain.Table;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class RepositoryGenerator extends AbstractGenerator {

    public RepositoryGenerator(Config config) {
        super(config);
    }


    public Optional<Path> export(Table table) {
        //return exportXXX(table, applyTemplate(table));
        return null;
    }

    List<String> applyTemplate(Table table) {
//        Map<String, Object> context = Maps.newHashMap();
//        context.put("class", transformer.getCalssName(table.getName()));
//        List<String> lines = applyTemplate("repository.mustache", context);
//        lines.forEach(System.out::println);
//        return lines;
        return null;
    }
}
