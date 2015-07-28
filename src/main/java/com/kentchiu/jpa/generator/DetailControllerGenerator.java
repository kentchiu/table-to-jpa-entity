package com.kentchiu.jpa.generator;

import com.google.common.base.Preconditions;
import com.kentchiu.jpa.domain.Table;

import java.util.List;
import java.util.Map;

public class DetailControllerGenerator extends AbstractControllerGenerator {

    public DetailControllerGenerator(Transformer transformer) {
        super(transformer);
    }

    protected List<String> applyTemplate(Table table) {
        Map<String, Object> context = getBaseContext(table);

        DetailConfig config = transformer.getMasterDetailMapper().get(table.getName());

        Preconditions.checkNotNull(config, "master detail should be config and put in MasterDetailMapper by key : [" + table.getName() + "]");

        context.put("masterName", config.getMasterName());
        context.put("detailName", config.getDetailName());
        context.put("masterDomain", config.getMasterDomain(transformer));
        context.put("detailDomain", config.getDetailDomain(transformer));

        Preconditions.checkNotNull(context.get("masterName"), "masterName is mandatory");
        Preconditions.checkNotNull(context.get("detailName"), "detailName is mandatory");
        Preconditions.checkNotNull(context.get("masterDomain"), "masterDomain is mandatory");
        Preconditions.checkNotNull(context.get("detailDomain"), "detailDomain is mandatory");

        String domain = getDomain(table);
        context.put("domain", domain);
        return applyTemplate("detail_controller.mustache", context);
    }

}



