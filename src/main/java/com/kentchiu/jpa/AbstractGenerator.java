package com.kentchiu.jpa;

import java.nio.file.Path;
import java.util.Map;

public class AbstractGenerator {

    protected Config config;
    protected Transformer transformer;
    protected Path projectHome;

    public AbstractGenerator(Config config) {
        this.config = config;
        transformer = new Transformer();
    }

    public Path getProjectHome() {
        return projectHome;
    }

    public void setProjectHome(Path projectHome) {
        this.projectHome = projectHome;
    }

    public Path getJavaSourceHome() {
        return projectHome.resolve("main/java");
    }

    public void setTableNameMapper(Map<String, String> tableNameMapper) {
        transformer.setTableNameMapper(tableNameMapper);
    }

    public void setColumnMapper(Map<String, String> columnMapper) {
        transformer.setColumnMapper(columnMapper);
    }

}
