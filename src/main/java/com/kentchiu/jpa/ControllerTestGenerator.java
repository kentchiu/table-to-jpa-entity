package com.kentchiu.jpa;

import com.google.common.base.CaseFormat;
import com.kentchiu.jpa.domain.Table;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ControllerTestGenerator extends AbstractGenerator {

    @Override
    protected boolean isTest() {
        return true;
    }

    public Optional<Path> export(Table table) {
        return exportToFile(table, applyTemplate(table));
    }

    List<String> applyTemplate(Table table) {
        Map<String, Object> context = getBaseContext(table);
        String domain = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, transformer.getDomainName(table.getName()));
        context.put("domain", domain);
        return applyTemplate("controller_test.mustache", context);
    }

    protected String getClassName(Table table) {
        return transformer.getDomainName(table.getName()) + "Controller";
    }

    protected String getPackageName(Table table) {
        return transformer.getTopPackage(table.getName()) + "/web";
    }
}
