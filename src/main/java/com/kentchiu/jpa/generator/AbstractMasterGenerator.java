package com.kentchiu.jpa.generator;

import com.kentchiu.jpa.domain.Table;
import org.atteo.evo.inflector.English;

import java.util.List;
import java.util.Map;

public abstract class AbstractMasterGenerator extends AbstractControllerGenerator {
    public AbstractMasterGenerator(Transformer transformer) {
        super(transformer);
    }

    protected List<String> applyTemplate(Table table) {
        Map<String, Object> context = getBaseContext(table);
        String domain = getDomain(table);
        context.put("domain", domain);
        context.put("domainPlural", English.plural(domain));
        return applyTemplate(getTemplate(), context);
    }

}
