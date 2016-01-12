package com.kentchiu.jpa.generator;

import com.kentchiu.jpa.domain.Table;

import java.util.List;
import java.util.Map;

public abstract class AbstractDetailGenerator extends AbstractControllerGenerator {
    public AbstractDetailGenerator(Transformer transformer) {
        super(transformer);
    }

    protected List<String> applyTemplate(Table table) {
        Map<String, Object> context = getBaseContext(table);
        return applyTemplate(getTemplate(), context);
    }


}
