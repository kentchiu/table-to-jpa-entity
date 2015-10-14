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

public class ResourceDetailGenerator extends AbstractControllerGenerator {

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


    protected List<String> applyTemplate(Table table) {
        Map<String, Object> context = getBaseContext(table);

        DetailConfig config = transformer.getMasterDetailMapper().get(table.getName());
        context.put("masterName", config.getMasterName());
        context.put("detailName", config.getDetailName());
        context.put("masterDomain", transformer.getDomainName(config.getMasterTable()));
        context.put("detailDomain", transformer.getDomainName(config.getMasterTable()));

        Preconditions.checkNotNull(context.get("masterName"), "masterName is mandatory");
        Preconditions.checkNotNull(context.get("detailName"), "detailName is mandatory");
        Preconditions.checkNotNull(context.get("masterDomain"), "masterDomain is mandatory");
        Preconditions.checkNotNull(context.get("detailDomain"), "detailDomain is mandatory");

        context.put("masterNamePlural", English.plural(config.getMasterName()));
        context.put("detailNamePlural", English.plural(config.getDetailName()));
        context.put("masterDomainPlural", English.plural(config.getMasterDomain(transformer)));
        context.put("detailDomainPlural", English.plural(config.getDetailDomain(transformer)));

        String domain = getDomain(table);
        context.put("domain", domain);
        return applyTemplate("resource_detail.mustache", context);
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


}
