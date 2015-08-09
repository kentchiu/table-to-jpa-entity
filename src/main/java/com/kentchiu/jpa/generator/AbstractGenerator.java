package com.kentchiu.jpa.generator;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kentchiu.jpa.domain.Table;
import org.apache.commons.lang3.StringUtils;
import org.atteo.evo.inflector.English;
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

    protected Transformer transformer;
    protected Path projectHome;
    private Logger logger = LoggerFactory.getLogger(AbstractGenerator.class);

    public AbstractGenerator(Transformer transformer) {
        this.transformer = transformer;
        try {
            this.projectHome = Files.createTempDirectory("com.kentchiu.jpa.codegen");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Transformer getTransformer() {
        return transformer;
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
    public Path getJavaTestSourceHome() {
        return projectHome.resolve("src/test/java");
    }

    public abstract Optional<Path> export(Table table);

    protected Optional<Path> exportToFile(Table table, List<String> lines) {
        Path baseFolder;
        if (isTest()) {
            baseFolder = getJavaTestSourceHome();
        } else {
            baseFolder = getJavaSourceHome();
        }
        String pkgName = getPackageName(table);
        String folder = StringUtils.replace(pkgName, ".", "/");
        Path file = baseFolder.resolve(folder).resolve(getClassName(table) + ".java");
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



    protected boolean isTest() {
        return false;
    }

    protected Map<String, Object> getBaseContext(Table table) {
        Map<String, Object> context = Maps.newHashMap();
        context.put("table", table);
        String domainName = transformer.getDomainName(table.getName());
        context.put("Domain", domainName);
        context.put("DomainPlural", English.plural(domainName));
        context.put("topPackage", transformer.getTopPackage(table.getName()));
        context.put("moduleName", transformer.getModuleName(table.getName()));
        return context;
    }

    protected abstract String getClassName(Table table);

    protected abstract String getPackageName(Table table);

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
