package com.kentchiu.jpa.generator;

import com.google.common.base.CaseFormat;
import com.kentchiu.jpa.domain.Table;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public abstract class AbstractControllerGenerator extends AbstractGenerator {

    public AbstractControllerGenerator(Transformer transformer) {
        super(transformer);
    }

    public Optional<Path> export(Table table) {
        return exportToFile(table, applyTemplate(table));
    }

    protected abstract List<String> applyTemplate(Table table);


    protected String getClassName(Table table) {
        return transformer.getDomainName(table.getName()) + "Controller";
    }

    protected String getPackageName(Table table) {
        return transformer.getTopPackage(table.getName()) + "." + transformer.getModuleName(table.getName()) + ".web";
    }


    protected String getDomain(Table table) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, transformer.getDomainName(table.getName()));
    }

    protected Optional<Path> exportApiDocument(Table table, List<String> lines) {
        Path baseFolder = projectHome.resolve("api");

        String pkgName = getPackageName(table);
        String folder = StringUtils.replace(pkgName, ".", "/");
        Path path = Paths.get(folder).getParent().getFileName();

        String className = getClassName(table);
        Path file = baseFolder.resolve(path).resolve(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, className) + ".md");
        return createFile(file, lines);
    }

    protected abstract String getTemplate();

}
