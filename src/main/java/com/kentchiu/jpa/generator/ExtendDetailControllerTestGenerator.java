package com.kentchiu.jpa.generator;

import com.google.common.base.Preconditions;
import com.kentchiu.jpa.domain.Table;
import org.atteo.evo.inflector.English;

import java.util.List;
import java.util.Map;

public class ExtendDetailControllerTestGenerator extends AbstractControllerGenerator {

    public ExtendDetailControllerTestGenerator(Transformer transformer) {
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

        DetailConfig detailConfig = transformer.getMasterDetailMapper().get(table.getName());
        if (!(detailConfig instanceof ExtendDetailConfig)) {
            throw new IllegalStateException("must be instance of ExtendDetailConfig but was " + detailConfig.getClass());
        }
        ExtendDetailConfig config = (ExtendDetailConfig) detailConfig;

        Preconditions.checkNotNull(config, "master detail should be config and put in MasterDetailMapper by key : [" + table.getName() + "]");

        context.put("masterName", config.getMasterName());
        context.put("detailName", config.getDetailName());
        context.put("extendDetailName", config.getDetailName());
        context.put("masterDomain", transformer.getDomainName(config.getMasterTable()));
        context.put("detailDomain", transformer.getDomainName(config.getDetailTable()));
        context.put("extendDetailDomain", transformer.getDomainName(config.getExtendDetailTable()));

        Preconditions.checkNotNull(context.get("masterName"), "masterName is mandatory");
        Preconditions.checkNotNull(context.get("detailName"), "detailName is mandatory");
        Preconditions.checkNotNull(context.get("extendDetailName"), "extendName is mandatory");
        Preconditions.checkNotNull(context.get("masterDomain"), "masterDomain is mandatory");
        Preconditions.checkNotNull(context.get("detailDomain"), "detailDomain is mandatory");
        Preconditions.checkNotNull(context.get("extendDetailDomain"), "extendDetailDomain is mandatory");

        context.put("masterNamePlural", English.plural(config.getMasterName()));
        context.put("detailNamePlural", English.plural(config.getDetailName()));
        context.put("extendDetailNamePlural", English.plural(config.getExtendDetailName()));
        context.put("masterDomainPlural", English.plural(config.getMasterDomain(transformer)));
        context.put("detailDomainPlural", English.plural(config.getDetailDomain(transformer)));
        context.put("extendDetailDomainPlural", English.plural(config.getExtendDetailDomain(transformer)));

        String domain = getDomain(table);
        context.put("domain", domain);
        return applyTemplate("extend_detail_controller_test.mustache", context);
    }


}
