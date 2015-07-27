package com.kentchiu.jpa.generator;

import com.google.common.base.CaseFormat;
import com.kentchiu.jpa.domain.Table;

import java.util.List;
import java.util.Map;

public class ControllerGenerator extends AbstractControllerGenerator {


    public ControllerGenerator(Transformer transformer) {
        super(transformer);
    }


    protected List<String> applyTemplate(Table table) {
        Map<String, Object> context = getBaseContext(table);
        String domain = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, transformer.getDomainName(table.getName()));
        context.put("domain", domain);
        return applyTemplate("controller.mustache", context);
    }

    protected String getClassName(Table table) {
        return transformer.getDomainName(table.getName()) + "Controller";
    }

}
