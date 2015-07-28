package com.kentchiu.jpa.generator;

import com.google.common.base.Preconditions;
import com.kentchiu.jpa.domain.Table;

import java.util.List;
import java.util.Map;

public class DetailControllerTestGenerator extends AbstractControllerGenerator {

    public DetailControllerTestGenerator(Transformer transformer) {
        super(transformer);
    }

    @Override
    protected boolean isTest() {
        return true;
    }


    protected String getClassName(Table table) {
        return transformer.getDomainName(table.getName()) + "ControllerTest";
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

        String domain = getDomain(table);
        context.put("domain", domain);
        return applyTemplate("detail_controller_test.mustache", context);
    }


}
