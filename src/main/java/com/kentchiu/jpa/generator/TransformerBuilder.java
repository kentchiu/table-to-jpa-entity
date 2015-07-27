package com.kentchiu.jpa.generator;

import java.util.Map;

public class TransformerBuilder {


    private Transformer transformer;

    public static TransformerBuilder transformer() {
        TransformerBuilder transformerBuilder = new TransformerBuilder();
        transformerBuilder.transformer = new Transformer();
        return transformerBuilder;
    }

    public Transformer build() {
        return transformer;
    }

    public TransformerBuilder mapTable(Map<String, String> tableNameMapper) {
        transformer.setTableNameMapper(tableNameMapper);
        return this;
    }

    public TransformerBuilder mapColumn(Map<String, String> columnMapper) {
        transformer.setColumnMapper(columnMapper);
        return this;
    }

    public TransformerBuilder mapMasterDetail(Map<String, DetailConfig> masterDetailMapper) {
        transformer.setMasterDetailMapper(masterDetailMapper);
        return this;
    }
}

