package com.kentchiu.jpa;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.kentchiu.jpa.domain.Column;
import com.kentchiu.jpa.domain.Table;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


public class EntityGenerator extends AbstractGenerator {


    private Logger logger = LoggerFactory.getLogger(EntityGenerator.class);
    private List<String> ignoreColumns;

    public EntityGenerator(Config config) {
        super(config);
        ignoreColumns = new ArrayList<>();
    }

    @Override
    public Optional<Path> export(Table table) {
        String templateName = config.getType().getTemplateName();
        return exportToFile(table, applyTemplate(templateName + ".mustache", getContext(table)));
    }

    private Map<String, Object> getContext(Table table) {
        String packageName = transformer.getPackage(table.getName(), config.getType());
        Map<String, Object> context = Maps.newHashMap();
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
        return context;
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
        return applyTemplate("property_" + config.getType().getTemplateName() + ".mustache", context);
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


    public List<String> exportTable(Table table) {
        String templateName = config.getType().getTemplateName();
        return applyTemplate(templateName + ".mustache", getContext(table));
    }
}

