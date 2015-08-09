package com.kentchiu.jpa.generator;

import com.kentchiu.jpa.domain.Table;
import org.atteo.evo.inflector.English;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ControllerTestGenerator extends AbstractControllerGenerator {

    public ControllerTestGenerator(Transformer transformer) {
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
        return applyTemplate("controller_test.mustache", context);
    }


    protected String getClassName(Table table) {
        return transformer.getDomainName(table.getName()) + "ControllerTest";
    }

}
