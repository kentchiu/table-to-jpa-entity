package com.kentchiu.jpa.generator;

public class ControllerGeneratorFactory {

    private ControllerGeneratorFactory() {
    }

    public static AbstractControllerGenerator makeController(Transformer transformer, String tableName) {
        boolean isDetail = transformer.getMasterDetailMapper().containsKey(tableName);
        if (isDetail) {
            DetailConfig detailConfig = transformer.getMasterDetailMapper().get(tableName);
            if (detailConfig instanceof ExtendDetailConfig) {
                return new ExtendDetailControllerGenerator(transformer);
            } else {
                return new DetailControllerGenerator(transformer);
            }
        } else {
            return new ControllerGenerator(transformer);
        }
    }


    public static AbstractControllerGenerator makeControllerTest(Transformer transformer, String tableName) {
        boolean isDetail = transformer.getMasterDetailMapper().containsKey(tableName);
        if (isDetail) {
            DetailConfig detailConfig = transformer.getMasterDetailMapper().get(tableName);
            if (detailConfig instanceof ExtendDetailConfig) {
                return new ExtendDetailControllerTestGenerator(transformer);
            } else {
                return new DetailControllerTestGenerator(transformer);
            }
        } else {
            return new ControllerTestGenerator(transformer);
        }
    }


    public static AbstractControllerGenerator makeResource(Transformer transformer, String tableName) {
        boolean isDetail = transformer.getMasterDetailMapper().containsKey(tableName);
        if (isDetail) {

            DetailConfig detailConfig = transformer.getMasterDetailMapper().get(tableName);
            if (detailConfig instanceof ExtendDetailConfig) {
                return new ExtendDetailResourceGenerator(transformer);
            } else {
                return new DetailResourceGenerator(transformer);
            }

        } else {
            return new ResourceGenerator(transformer);
        }
    }

}
