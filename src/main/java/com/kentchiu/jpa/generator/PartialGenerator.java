package com.kentchiu.jpa.generator;

import com.google.common.base.Joiner;
import com.kentchiu.jpa.domain.Table;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.*;


public class PartialGenerator extends AbstractGenerator {

    public PartialGenerator(Transformer transformer) {
        super(transformer);
    }

    @Override
    public Optional<Path> export(Table table) {
        return exportToFile(table, exportTable(table));
    }


    protected Optional<Path> exportToFile(Table table, List<String> lines) {
        Path baseFolder;
        baseFolder = getJavaSourceHome();
        String pkgName = getPackageName(table);
        String folder = StringUtils.replace(pkgName, ".", "/");
        Path file = baseFolder.resolve(folder).resolve(getClassName(table) + ".md");
        return createFile(file, lines);
    }

    private Map<String, Object> getContent(Table table) {
        Map<String, Object> map = new HashMap<>();
        map.putAll(getBaseContext(table));
        map.put("entity", section(table, Type.JPA));
        map.put("input", section(table, Type.INPUT));
        map.put("update", section(table, Type.UPDATE));
        map.put("query", section(table, Type.QUERY));
        return map;
    }

    protected String getClassName(Table table) {
        return transformer.getDomainName(table.getName()) + "Partial";
    }

    protected String getPackageName(Table table) {
        return transformer.getTopPackage(table.getName()) + "." + transformer.getModuleName(table.getName());
    }

    private String section(Table table, Type type) {
        EntityGenerator generator = new EntityGenerator(transformer, new Config(type));

        List<String> lines = new ArrayList<>();
        if (Type.JPA == type) {
            lines.add(generator.buildFieldEnums(table));
        }
        lines.add(generator.buildProperties(table));
        return Joiner.on('\n').join(lines);
    }

    public List<String> exportTable(Table table) {
        return applyTemplate("domain_partial.mustache", getContent(table));
    }
}

