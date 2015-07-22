package com.kentchiu.jpa;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.kentchiu.jpa.domain.Table;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractGenerator {

    protected Config config;
    protected Transformer transformer;
    protected Path projectHome;
    private Logger logger = LoggerFactory.getLogger(AbstractGenerator.class);

    public AbstractGenerator(Config config) {
        this.config = config;
        this.transformer = new Transformer();
        try {
            this.projectHome = Files.createTempDirectory("com.kentchiu.jpa.codegen");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Path getProjectHome() {
        return projectHome;
    }

    public void setProjectHome(Path projectHome) {
        this.projectHome = projectHome;
    }

    public Path getJavaSourceHome() {
        return projectHome.resolve("src/main/java");
    }

    public void setTableNameMapper(Map<String, String> tableNameMapper) {
        transformer.setTableNameMapper(tableNameMapper);
    }

    public void setColumnMapper(Map<String, String> columnMapper) {
        transformer.setColumnMapper(columnMapper);
    }

    public abstract Optional<Path> export(Table table);

    protected Optional<Path> exportToFile(Table table, List<String> lines) {
        String pkgName = transformer.getPackage(table.getName(), config.getType());
        String className = transformer.buildClassName(table.getName());
        String name = config.getType().getJavaFileName(className);
        String folder = StringUtils.replace(pkgName, ".", "/");
        Path file = getJavaSourceHome().resolve(folder).resolve(name);
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

    protected List<String> applyTemplate(String template, Map<String, Object> context) {
        try {
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile(template);
            StringWriter stringWriter = new StringWriter();
            mustache.execute(stringWriter, context).flush();
            String content = stringWriter.toString();
            Iterable<String> split = Splitter.on('\n').split(content);
            return Lists.newArrayList(split);
        } catch (IOException e) {
            e.printStackTrace();
            return Lists.newArrayList();
        }
    }
}
