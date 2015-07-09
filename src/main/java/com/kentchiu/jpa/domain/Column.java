package com.kentchiu.jpa.domain;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Column {

    private String name;
    private String comment;
    private String javaType;
    private boolean nullable = true;
    private String referenceTable;
    private String description;
    private Map<String, String> options = Maps.newLinkedHashMap();
    private String defaultValue = "";
    private boolean unique = false;

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    void parser(String comment) {

        Pattern p = Pattern.compile("([^(]*)(\\((.*/.*?)\\))?(\\((.*)\\))?");
        Matcher m = p.matcher(comment);
        if (m.matches()) {
            MatchResult r = m.toMatchResult();
            setDescription(r.group(1));
            for (int i = 2; i < r.groupCount() + 1; i++) {
                String token = r.group(i);
                if (StringUtils.contains(token, "default")) {
                    String defaultValue = StringUtils.substringAfter(token, "=").trim();
                    if (defaultValue.startsWith("'")) {
                        setDefaultValue(StringUtils.substringBetween(defaultValue, "'", "'"));
                    } else {
                        setDefaultValue(defaultValue);
                    }
                }
                if (StringUtils.contains(token, "/") && !StringUtils.startsWith(token, "(")) {
                    List<String> ops = Splitter.on("/").splitToList(token);
                    ops.forEach(op -> {
                        String key = StringUtils.substringBefore(op, "=").trim();
                        String value = StringUtils.substringAfter(op, "=").trim();
                        getOptions().put(key, value);
                    });
                }
            }
        }
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }


    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        if (StringUtils.equals("java.math.BigDecimal", javaType)) {
            defaultValue = "BigDecimal.ZERO";
        }
        this.javaType = javaType;
    }

    public String getReferenceTable() {
        return referenceTable;
    }

    public void setReferenceTable(String referenceTable) {
        this.referenceTable = referenceTable;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        parser(comment);
        this.comment = comment;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(name).append(" ").append(comment);
        return sb.toString();
    }

    public String getSimpleJavaType() {
        if (StringUtils.endsWithIgnoreCase(javaType, "TIMESTAMP")) {
            return "Date";
        }
        return StringUtils.substringAfterLast(javaType, ".");
    }
}

