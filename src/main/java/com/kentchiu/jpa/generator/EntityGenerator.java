package com.kentchiu.jpa.generator;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.kentchiu.jpa.domain.Column;
import com.kentchiu.jpa.domain.Table;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


public class EntityGenerator extends AbstractGenerator {

    protected Config config;
    private Logger logger = LoggerFactory.getLogger(EntityGenerator.class);

    public EntityGenerator(Transformer transformer, Config config) {
        super(transformer);
        this.config = config;
    }

    @Override
    public Optional<Path> export(Table table) {
        String templateName = config.getType().getTemplateName();
        return exportToFile(table, applyTemplate(templateName + ".mustache", getContext(table)));
    }

    @Override
    protected boolean isTest() {
        return Type.QUERY_TEST == config.getType();
    }

    protected String buildProperties(Table table) {
        List<String> ignoreColumns = getIgnoreColumns(table);
        List<String> results = new ArrayList<>();
        String masterColumn = getMasterColumn(table).orElse("");
        table.getColumns().stream().filter(column -> !ignoreColumns.contains(column.getName())).forEach(column -> {
            if (Type.QUERY == config.getType() && StringUtils.equals(column.getName(), masterColumn)) {
                List<String> lines = buildProperty(column);

                lines.forEach(line -> {
                    results.add(line.replaceFirst("//", "  "));
                });
            } else {
                results.addAll(buildProperty(column));
            }
        });

        return Joiner.on('\n').join(results);
    }

    List<String> buildImports() {
        List<String> results = new ArrayList<>();
        Class<?> baseClass = config.getBaseClass();
        if (!Objects.equals(Object.class, baseClass)) {
            results.add(baseClass.getName());
        }

        if (Boolean.TRUE.equals(getExtraParams().getOrDefault("enableFilter", Boolean.FALSE))) {
            results.add("import com.bq.i1.base.service.query.FilterQuery");
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


        if (property.isDateType()) {
            if (Type.QUERY == config.getType() || Type.JPA == config.getType()) {
                if (StringUtils.contains(column.getComment(), "(format")) {
                    String value = "@DateTimeFormat(pattern = \"" + StringUtils.substringBetween(column.getComment(), "=", ")") + "\")";
                    context.put("dateTimeFormat", value);
                } else {
                    logger.warn("date type should has format attribute : {}", column);
                }
            } else if (Type.INPUT == config.getType() || Type.UPDATE == config.getType()) {
                if (StringUtils.contains(column.getComment(), "(format")) {
                    String value = "@JsonFormat(pattern = \"" + StringUtils.substringBetween(column.getComment(), "=", ")") + "\")";
                    context.put("dateTimeFormat", value);
                } else {
                    logger.warn("date type should has format attribute : {}", column);
                }
            } else {
                throw new IllegalStateException("Unknown type : " + config.getType());
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
            if (property.getTypeName().equals("String")) {
                context.put("defaultValue", " = \"" + column.getDefaultValue() + "\"");
            } else {
                context.put("defaultValue", " = " + column.getDefaultValue());
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
            String format = StringUtils.substringBetween(column.getComment(), "=", ")");
            sb.append(format);
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
        Map<String, Object> context = getContext(table);
        logger.debug("content: {}", context);
        return applyTemplate(templateName + ".mustache", context);
    }

    private Map<String, Object> getContext(Table table) {
        Map<String, Object> baseContext = getBaseContext(table);

        // package
        String packageName = getPackageName(table);
        if (!StringUtils.isBlank(packageName)) {
            baseContext.put("packageName", packageName);
        }

        // import
        List<Map<String, String>> imports = buildImports().stream().map(i -> ImmutableMap.of("import", i)).collect(Collectors.toList());
        baseContext.put("imports", imports);
        Boolean enableFilter = (Boolean) getExtraParams().getOrDefault("enableFilter", false);
        if (Type.QUERY == config.getType()) {
            if (enableFilter) {
                baseContext.put("extend", "extends FilterQuery<" + transformer.getDomainName(table.getName()) + "> ");
            } else {
                baseContext.put("extend", "extends PageableQuery<" + transformer.getDomainName(table.getName()) + "> ");
            }
            List<Transformer.Property> properties = table.getColumns().stream().map(c -> transformer.getProperty(c, config.getType())).collect(Collectors.toList());
            baseContext.put("properties2", properties);
        } else {
            if (!Objects.equals(Object.class, config.getBaseClass())) {
                baseContext.put("extend", "extends " + config.getBaseClass().getSimpleName());
            } else {
                baseContext.put("extend", "");
            }
        }

        if (Type.QUERY_TEST != config.getType()) {
            // fieldEnums
            baseContext.put("fieldEnums", buildFieldEnums(table));

            // properties
            baseContext.put("properties", buildProperties(table));
        }
        return baseContext;
    }

    private Optional<String> getMasterColumn(Table table) {
        DetailConfig config = transformer.getMasterDetailMapper().get(table.getName());
        if (config == null) {
            return Optional.empty();
        }
        String masterTable = config.getMasterTable();
        String masterFkName;
        if (CharUtils.isAsciiAlphaUpper(masterTable.charAt(0))) {
            masterFkName = masterTable + "_UUID";
        } else {
            masterFkName = masterTable + "_uuid";
        }
        return Optional.ofNullable(masterFkName);
    }

    protected String buildFieldEnums(Table table) {
        List<String> ignoreColumns = getIgnoreColumns(table);
        List<FieldEnum> results = new ArrayList();
        table.getColumns().stream().filter(column -> !ignoreColumns.contains(column.getName())).forEach(c -> {
            List<FieldEnum> fieldEnums = buildEnum(c);
            results.addAll(fieldEnums);
        });
        List<String> lines = applyTemplate("enum.mustache", ImmutableMap.of("fieldEnums", results));
        return Joiner.on('\n').join(lines);
    }

    private List<String> getIgnoreColumns(Table table) {
        List<String> ignoreColumns = new ArrayList<>();
        if (Type.INPUT == config.getType() || Type.UPDATE == config.getType()) {
            if (transformer.getMasterDetailMapper().containsKey(table.getName())) {
                getMasterColumn(table).ifPresent(masterColumn -> {
                    logger.debug("ignore master foreign key : {}", masterColumn);
                    ignoreColumns.add(masterColumn);
                });
            }
        }
        return ignoreColumns;
    }

    protected String getClassName(Table table) {
        String javaFileName = config.getType().getClassPostFix();
        return transformer.getDomainName(table.getName()) + javaFileName;
    }

    protected String getPackageName(Table table) {
        return transformer.getTopPackage(table.getName()) + "." + transformer.getModuleName(table.getName()) + "." + config.getType().getPackage();
    }

    public List<FieldEnum> buildEnum(Column column) {
        ArrayList<FieldEnum> results = new ArrayList<>();
        Map<String, String> options = column.getOptions();
        for (Map.Entry<String, String> entry : options.entrySet()) {
            FieldEnum fe = new FieldEnum();
            String property = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, transformer.getProperty(column, Type.JPA).getPropertyName());
            String name = property + "_" + entry.getKey();
            fe.setName(name);
            fe.setValue("\"" + entry.getKey() + "\"");
            fe.setDescription(column.getDescription() + " : " + entry.getValue());
            results.add(fe);
        }
        return results;
    }


    class FieldEnum {
        private String value;
        private String name;
        private String description;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}

