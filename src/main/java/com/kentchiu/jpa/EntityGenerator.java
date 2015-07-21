package com.kentchiu.jpa;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kentchiu.jpa.domain.Column;
import com.kentchiu.jpa.domain.Table;
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
    private Transformer transformer;
    private Logger logger = LoggerFactory.getLogger(EntityGenerator.class);


    public EntityGenerator(Config config) {
        this.config = config;
        transformer = new Transformer();
    }


    public void export(Path javaSourceHome, List<Table> tables, List<String> ignoreColumns) {
        for (Table table : tables) {
            export(javaSourceHome, table, ignoreColumns);
        }
    }

    public void export(Path javaSourceHome, Table table, List<String> ignoreColumns) {
        List<String> lines = exportTable(table, ignoreColumns);
        String pkgName = transformer.buildPackageName(table.getName(), config.getType());
        String className = transformer.buildClassName(table.getName());
        String folder = StringUtils.replace(pkgName, ".", "/");
        String name = config.getType().getJavaFileName(className);
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
        String packageName = transformer.buildPackageName(table.getName(), config.getType());
        HashMap<Object, Object> context = Maps.newHashMap();
        if (!StringUtils.isBlank(packageName)) {
            context.put("packageName", packageName);
        }

        List<Map<String, String>> imports = buildImports().stream().map(i -> ImmutableMap.of("import", i)).collect(Collectors.toList());

        String className = transformer.buildClassName(table.getName());
        context.put("imports", imports);
        context.put("table", table);
        context.put("class", className);
        if (Type.QUERY == config.getType()) {
            context.put("extend", "extends PageableQuery<" + className + "> ");
        } else {
            if (!Objects.equals(Object.class, config.getBaseClass())) {
                context.put("extend", "extends " + config.getBaseClass().getSimpleName());
            } else {
                context.put("extend", "");
            }
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
        Class<?> baseClass = config.getBaseClass();
        if (!Objects.equals(Object.class, baseClass)) {
            results.add(baseClass.getName());
        }
        transformer.getTableNameMapper().values().forEach(p -> results.add(p));
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


    List<String> buildProperty(Column column) {

        Transformer.Property property = transformer.getProperty(column, config.getType());

        Map<String, Object> context = new HashMap<>();

        if (StringUtils.isNotBlank(buildOptions(column))) {
            context.put("options", buildOptions(column));
        }


        if (Type.INPUT == config.getType() || Type.UPDATE == config.getType() || Type.QUERY == config.getType()) {
            if (property.isDateType() && StringUtils.contains(column.getComment(), "(format")) {
                String value = "@DateTimeFormat(pattern = \"" + StringUtils.substringBetween(column.getComment(), "=", ")") + "\")";
                context.put("dateTimeFormat", value);
            }

        }

        if (StringUtils.isNotBlank(buildAttributeInfo(column))) {
            context.put("attributeInfo", buildAttributeInfo(column));
        }

        if (!column.isNullable()) {
            if (StringUtils.equals("String", property.getTypeName())) {
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

        if (StringUtils.isNotBlank(column.getDefaultValue())) {
            if (config.getType() != Type.UPDATE && config.getType() != Type.QUERY) {
                if (StringUtils.equals("BigDecimal.ZERO", column.getDefaultValue())) {
                    sb.append(", ").append("defaultValue = \"0\"");
                } else {
                    sb.append(", ").append("defaultValue = \"").append(column.getDefaultValue()).append("\"");
                }
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


    public void setTableNameMapper(Map<String, String> tableNameMapper) {
        transformer.setTableNameMapper(tableNameMapper);
    }

    public void setColumnMapper(Map<String, String> columnMapper) {
        transformer.setColumnMapper(columnMapper);
    }
}

