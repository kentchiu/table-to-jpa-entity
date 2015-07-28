package com.kentchiu.jpa.generator;

public class ControllerGeneratorFactory {

    private ControllerGeneratorFactory() {
    }

    public static AbstractControllerGenerator makeController(Transformer transformer, String tableName) {
        boolean isDetail = transformer.getMasterDetailMapper().containsKey(tableName);
        if (isDetail) {
            return new DetailControllerGenerator(transformer);
        } else {
            return new ControllerGenerator(transformer);
        }
    }


    public static AbstractControllerGenerator makeControllerTest(Transformer transformer, String tableName) {
        boolean isDetail = transformer.getMasterDetailMapper().containsKey(tableName);
        if (isDetail) {
            return new DetailControllerTestGenerator(transformer);
        } else {
            return new ControllerTestGenerator(transformer);
        }
    }

}
