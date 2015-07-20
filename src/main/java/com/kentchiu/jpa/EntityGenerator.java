package com.kentchiu.jpa;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kentchiu.jpa.domain.Column;
import com.kentchiu.jpa.domain.Table;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class EntityGenerator {

    private Config config;

    private Logger logger = LoggerFactory.getLogger(EntityGenerator.class);
    /**
     * table name -> java fully qualified name
     */
    private Map<String, String> tableNameMapper = new HashMap<>();
    /**
     * abbreviate name -> full name, and foreign key name -> property name
     */
    private Map<String, String> columnMapper = new HashMap<>();


    public EntityGenerator(Config config) {
        this.config = config;
    }

    public Map<String, String> getColumnMapper() {
        return columnMapper;
    }

    public void setColumnMapper(Map<String, String> columnMapper) {
        this.columnMapper = columnMapper;
    }


    public void export(Path javaSourceHome, List<Table> tables, List<String> ignoreColumns) {
        for (Table table : tables) {
            export(javaSourceHome, table, ignoreColumns);
        }
    }

    public void export(Path javaSourceHome, Table table, List<String> ignoreColumns) {
        List<String> lines = exportTable(table, ignoreColumns);
        String pkgName = buildPackageName(table.getName());
        if (config.getType() != Type.JPA) {
            pkgName = pkgName.replaceAll("domain", "web.dto");
        }
        String folder = StringUtils.replace(pkgName, ".", "/");
        String className = buildClassName(table.getName());
        String name;
        if (config.getType() == Type.INPUT) {
            name = className + "Input.java";
        } else if (config.getType() == Type.UPDATE) {
            name = className + "UpdateInput.java";
        } else {
            name = className + ".java";
        }
        Path file = javaSourceHome.resolve(folder).resolve(name);
        try {
            if (!Files.exists(file)) {
                if (Files.exists(file.getParent())) {
                    Preconditions.checkState(Files.isDirectory(file.getParent()), file.getParent() + " is not a directory");
                } else {
                    Files.createDirectories(file.getParent());
                }
                Files.createFile(file);
            }
            logger.info("create entity file : {}", file.toAbsolutePath().toString());
            Files.write(file, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected List<String> exportTable(Table table) {
        return exportTable(table, ImmutableList.of());
    }

    protected List<String> exportTable(Table table, List<String> ignoreColumns) {

        String packageName = buildPackageName(table.getName());


        HashMap<Object, Object> context = Maps.newHashMap();
        if (!StringUtils.isBlank(packageName)) {
            context.put("packageName", packageName);
        }

        context.put("imports", buildImports());
        context.put("table", table);
        context.put("class", buildClassName(table.getName()));
        if (!Objects.equals(Object.class, config.getBaseClass())) {
            context.put("extend", "extends " + config.getBaseClass().getSimpleName());
        } else {
            context.put("extend", "");
        }
        context.put("properties", buildProperties(table, ignoreColumns));

        MustacheFactory mf = new DefaultMustacheFactory();
        String templateName = config.getType().getTemplateName();
        Mustache mustache = mf.compile(templateName + ".mustache");
        try {
            StringWriter stringWriter = new StringWriter();
            mustache.execute(stringWriter, context).flush();
            String content = stringWriter.toString();
            Iterable<String> split = Splitter.on('\n').split(content);
            return Lists.newArrayList(split);
        } catch (IOException e) {
            e.printStackTrace();
            return Lists.newArrayList();
        }

    }

    private String buildProperties(Table table, List<String> ignoreColumns) {
        List<String> lines = new ArrayList<>();
        table.getColumns().stream().filter(column -> !ignoreColumns.contains(column.getName())).forEach(column -> {
            lines.addAll(buildProperty(column));
        });

        return Joiner.on('\n').join(lines);
    }

    List<String> buildImports() {
        List<String> results = new ArrayList<>();
        if (config.getBaseClass() != null) {
            results.add(config.getBaseClass().getName());
        }
        tableNameMapper.values().forEach(p -> results.add(p));
        return results;
    }

    private List<String> convert(String template, Map<String, Object> context) {
        try {
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile(template);
            StringWriter stringWriter = new StringWriter();
            mustache.execute(stringWriter, context).flush();
            String content = stringWriter.toString();
            Iterable<String> split = Splitter.on('\n').split(content);
            return Lists.newArrayList(split);
        } catch (IOException e) {
            e.printStackTrace();
            return Lists.newArrayList();
        }
    }

    String buildPackageName(String tableName) {
        if (tableNameMapper.containsKey(tableName)) {
            String qualifier = tableNameMapper.getOrDefault(tableName, "");
            String pkgName = StringUtils.substringBeforeLast(qualifier, ".");
            logger.debug("{}  -> {}", tableName, pkgName);
            return pkgName;
        } else {
            return "";
        }
    }


    List<String> buildProperty(Column column) {
        Property p = new Property(column);
        p.setMapper(columnMapper);
        Property property = p.invoke(config.getType());

        Map<String, Object> context = new HashMap<>();

        if (StringUtils.isNotBlank(buildAttributeInfo(column))) {
            context.put("options", buildOptions(column));
        }

        if (Type.INPUT == config.getType() || Type.UPDATE == config.getType()) {
            if (property.isDateType() && StringUtils.contains(column.getComment(), "(format")) {
                String value = "@DateTimeFormat(pattern = \"" + StringUtils.substringBetween(column.getComment(), "=", ")") + "\")";
                context.put("dateTimeFormat", value);
            }

        }

        if (StringUtils.isNotBlank(buildAttributeInfo(column))) {
            context.put("attributeInfo", buildAttributeInfo(column));
        }

        if (!column.isNullable()) {
            if (StringUtils.equals("String", p.getTypeName())) {
                context.put("nullability", "@NotBlank");
            } else {
                context.put("nullability", "@NotNull");
            }
        }

        if (StringUtils.isNotBlank(column.getReferenceTable())) {
            context.put("manyToOne", column.getReferenceTable());
        }

        if (column.isUnique()) {
            context.put("columnAnnotation", (String.format("@Column(name = \"%s\", unique = true)", column.getName())));
        } else {
            context.put("columnAnnotation", (String.format("@Column(name = \"%s\")", column.getName())));
        }


        context.put("property", property);
        context.put("column", column);


        if (StringUtils.isNotBlank(column.getDefaultValue())) {
            if (StringUtils.equals("BigDecimal.ZERO", column.getDefaultValue())) {
                context.put("defaultValue", (" = BigDecimal.ZERO"));
            } else {
                context.put("defaultValue", " = \"" + column.getDefaultValue() + "\"");
            }
        }


        System.out.println("context : " + context);
        return convert("property_" + config.getType().getTemplateName() + ".mustache", context);
    }


    String buildAttributeInfo(Column column) {
        StringBuffer sb = new StringBuffer();
        sb.append("@AttributeInfo(");
        if (StringUtils.isNotBlank(column.getDescription())) {
            sb.append("description = \"").append(column.getDescription()).append("\"");
        }
        if (!column.getOptions().isEmpty()) {
            sb.append(", ").append("format = \"");
            List<String> pairs = new ArrayList<>();
            column.getOptions().forEach((k, v) -> pairs.add(k + "=" + v));
            sb.append(Joiner.on("/").join(pairs));
            sb.append("\"");
        }

        if (StringUtils.isNotBlank(column.getComment()) && column.getComment().contains("(format")) {
            sb.append(", ").append("format = \"");
            sb.append("yyyy-MM-dd");
            sb.append("\"");
        }

        if (StringUtils.isNotBlank(column.getDefaultValue()) && config.getType() != Type.UPDATE) {
            if (StringUtils.equals("BigDecimal.ZERO", column.getDefaultValue())) {
                sb.append(", ").append("defaultValue = \"0\"");
            } else {
                sb.append(", ").append("defaultValue = \"").append(column.getDefaultValue()).append("\"");
            }
        }
        sb.append(")");

        return sb.toString();
    }

    String buildOptions(Column c) {
        if (!c.getOptions().isEmpty()) {
            StringBuilder s = new StringBuilder();
            s.append("@Option(value = {");
            List<String> keys = c.getOptions().keySet().stream().map(k -> "\"" + k + "\"").collect(Collectors.toList());
            s.append(Joiner.on(", ").join(keys));
            s.append("})").toString();
            return s.toString();
        }
        return "";
    }


    private String buildClassName(String tableName) {
        String className;
        if (tableNameMapper.containsKey(tableName)) {
            String qualifier = tableNameMapper.getOrDefault(tableName, "");
            className = StringUtils.substringAfterLast(qualifier, ".");
            Preconditions.checkState(StringUtils.isNotBlank(className), "class name should not empty");
        } else {
            className = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
        }
        logger.debug("{}  -> {}", tableName, className);
        return className;
    }

    public void setTableNameMapper(Map<String, String> tableNameMapper) {
        this.tableNameMapper = tableNameMapper;
    }


    class Property {
        private Column column;
        private String propertyName;
        private String methodName;
        private String typeName;
        private Map<String, String> mapper = Maps.newHashMap();

        public Property(Column column) {
            this.column = column;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public String getMethodName() {
            return methodName;
        }

        public String getTypeName() {
            return typeName;
        }

        public Property invoke(Type type) {

            String[] searchList = mapper.keySet().toArray(new String[mapper.size()]);
            String[] replacementList = mapper.values().toArray(new String[mapper.size()]);
            String name = StringUtils.replaceEach(column.getName(), searchList, replacementList);


            if (StringUtils.isBlank(column.getReferenceTable())) {
                if (type != Type.JPA && isDateType()) {
                    if (isDateType()) {
                        typeName = "Date";
                    } else {
                        typeName = "String";
                    }
                    propertyName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
                    methodName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
                } else {
                    typeName = column.getSimpleJavaType();
                    propertyName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
                    methodName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
                }
            } else {
                if (config.getType() != Type.JPA) {
                    typeName = column.getSimpleJavaType();
                    String mapName = columnMapper.getOrDefault(column.getName(), column.getReferenceTable());
                    if (columnMapper.containsKey(column.getName())) {
                        logger.info("map {} -> {}", column.getReferenceTable(), mapName);
                    }

                    propertyName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, buildClassName(mapName)) + "Uuid";
                    methodName = buildClassName(mapName) + "Uuid";
                } else {
                    String referenceTable = column.getReferenceTable();
                    typeName = buildClassName(referenceTable);
                    String mapName = columnMapper.getOrDefault(column.getName(), referenceTable);
                    if (columnMapper.containsKey(column.getName())) {
                        logger.info("map {} -> {}", referenceTable, mapName);
                    }
                    if (columnMapper.containsKey(column.getName())) {
                        propertyName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, mapName);
                        methodName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, mapName);
                    } else {
                        propertyName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, typeName);
                        methodName = buildClassName(mapName);
                    }
                }
            }
            logger.debug("property:{}, method:{}, type:{}", propertyName, methodName, typeName);
            return this;
        }

        private boolean isDateType() {
            return ArrayUtils.contains(new String[]{"java.util.Date", "java.sql.Date", "java.sql.Time", "java.sql.Timestamp"}, column.getJavaType());
        }

        public void setMapper(Map<String, String> mapper) {
            this.mapper = mapper;
        }
    }
}

