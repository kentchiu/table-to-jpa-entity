package com.kentchiu.jpa.generator;

import com.google.common.base.Preconditions;
import com.kentchiu.jpa.domain.Table;
import org.atteo.evo.inflector.English;

import java.util.List;
import java.util.Map;

public abstract class AbstractDetailGenerator extends AbstractControllerGenerator {
    public AbstractDetailGenerator(Transformer transformer) {
        super(transformer);
    }

    protected List<String> applyTemplate(Table table) {
        Map<String, Object> context = getBaseContext(table);

        DetailConfig config = transformer.getMasterDetailMapper().get(table.getName());
        context.put("masterName", config.getMasterName());
        context.put("detailName", config.getDetailName());
        context.put("masterDomain", transformer.getDomainName(config.getMasterTable()));
        context.put("detailDomain", transformer.getDomainName(config.getDetailTable()));

        Preconditions.checkNotNull(context.get("masterName"), "masterName is mandatory");
        Preconditions.checkNotNull(context.get("detailName"), "detailName is mandatory");
        Preconditions.checkNotNull(context.get("masterDomain"), "masterDomain is mandatory");
        Preconditions.checkNotNull(context.get("detailDomain"), "detailDomain is mandatory");

        context.put("masterNamePlural", English.plural(config.getMasterName()));
        context.put("detailNamePlural", English.plural(config.getDetailName()));
        context.put("masterDomainPlural", English.plural(config.getMasterDomain(transformer)));
        context.put("detailDomainPlural", English.plural(config.getDetailDomain(transformer)));

        return applyTemplate(getTemplate(), context);
    }

}
