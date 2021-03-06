package com.kentchiu.jpa.generator;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.CaseFormat;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractGenerator {

    protected Transformer transformer;
    protected Path projectHome;
    private Logger logger = LoggerFactory.getLogger(AbstractGenerator.class);
    private Map<String, Object> extraParams = new HashMap<>();

    public AbstractGenerator(Transformer transformer) {
        this.transformer = transformer;
        try {
            this.projectHome = Files.createTempDirectory("com.kentchiu.jpa.codegen");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void dump(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            String lineNo = StringUtils.leftPad(Integer.toString(i), 3);
            System.out.println(lineNo + "| " + lines.get(i));
        }
    }

    public Map<String, Object> getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(Map<String, Object> extraParams) {
        this.extraParams = extraParams;
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
        return createFile(file, lines);
    }


    protected Optional<Path> createFile(Path file, List<String> content) {
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
            Files.write(file, content);
            return Optional.of(file);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    protected String getDomain(Table table) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, transformer.getDomainName(table.getName()));
    }

    protected boolean isTest() {
        return false;
    }

    protected Map<String, Object> getBaseContext(Table table) {
        Map<String, Object> context = Maps.newHashMap();
        context.put("table", table);
        context.put("Domain", transformer.getDomainName(table.getName()));
        context.put("DomainPlural", English.plural(transformer.getDomainName(table.getName())));
        context.put("domain", getDomain(table));
        context.put("domainPlural", English.plural(getDomain(table)));

        context.put("topPackage", transformer.getTopPackage(table.getName()));
        context.put("moduleName", transformer.getModuleName(table.getName()));
        context.put("extraParams", extraParams);

        DetailConfig config = transformer.getMasterDetailMapper().get(table.getName());
        if (config != null) {
            String masterName = config.getMasterName();
            String detailName = config.getDetailName();
            String masterDomain = transformer.getDomainName(config.getMasterTable());
            String detailDomain = transformer.getDomainName(config.getDetailTable());

            context.put("masterName", masterName);
            context.put("detailName", detailName);
            context.put("masterDomain", lowerCamel(masterDomain));
            context.put("detailDomain", lowerCamel(detailDomain));

            context.put("MasterName", upperCamel(masterName));
            context.put("DetailName", upperCamel(detailName));
            context.put("MasterDomain", masterDomain);
            context.put("DetailDomain", detailDomain);

            Preconditions.checkNotNull(context.get("masterName"), "masterName is mandatory");
            Preconditions.checkNotNull(context.get("detailName"), "detailName is mandatory");
            Preconditions.checkNotNull(context.get("masterDomain"), "masterDomain is mandatory");
            Preconditions.checkNotNull(context.get("detailDomain"), "detailDomain is mandatory");

            context.put("masterNamePlural", English.plural(masterName));
            context.put("detailNamePlural", English.plural(detailName));
            context.put("masterDomainPlural", English.plural(config.getMasterDomain(transformer)));
            context.put("detailDomainPlural", English.plural(config.getDetailDomain(transformer)));
            context.put("MasterNamePlural", English.plural(upperCamel(masterName)));
            context.put("DetailNamePlural", English.plural(upperCamel(detailName)));
            context.put("MasterDomainPlural", English.plural(upperCamel(masterDomain)));
            context.put("DetailDomainPlural", English.plural(upperCamel(detailDomain)));

        }
        logger.debug("base context: {}", context);
        return context;
    }

    private String upperCamel(String masterName) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, masterName);
    }

    private String lowerCamel(String masterName) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, masterName);
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
