package com.kentchiu.jpa.generator;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.kentchiu.jpa.domain.Table;
import org.apache.commons.lang3.StringUtils;
import org.atteo.evo.inflector.English;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ResourceGenerator extends AbstractControllerGenerator {

    private Logger logger = LoggerFactory.getLogger(ResourceGenerator.class);

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

    protected List<String> applyTemplate(Table table) {
        Map<String, Object> context = getBaseContext(table);
        String domain = getDomain(table);
        context.put("domain", domain);
        context.put("domainPlural", English.plural(domain));
        return applyTemplate("resource.mustache", context);
    }

    protected Optional<Path> exportToFile(Table table, List<String> lines) {
        Path baseFolder = projectHome.resolve("api");

        String pkgName = getPackageName(table);
        String folder = StringUtils.replace(pkgName, ".", "/");
        Path path = Paths.get(folder).getParent().getFileName();

        String className = getClassName(table);
        Path file = baseFolder.resolve(path).resolve(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, className) + ".md");
        try {
            if (!Files.exists(file)) {
                if (Files.exists(file.getParent())) {
                    Preconditions.checkState(Files.isDirectory(file.getParent()), file.getParent() + " is not a directory");
                } else {
                    Files.createDirectories(file.getParent());
                }
                Files.createFile(file);
            }
            logger.info("create entity file : {}", file.toAbsolutePath().toString());
            Files.write(file, lines);
            return Optional.of(file);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    protected String getClassName(Table table) {
        return transformer.getDomainName(table.getName());
    }

}
