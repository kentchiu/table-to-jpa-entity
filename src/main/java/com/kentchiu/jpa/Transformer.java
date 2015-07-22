package com.kentchiu.jpa;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.kentchiu.jpa.domain.Column;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Table model -> Java Model
 */
public class Transformer {

    private Logger logger = LoggerFactory.getLogger(Transformer.class);
    /**
     * table name -> java fully qualified name
     */
    private Map<String, String> tableNameMapper = new HashMap<>();
    /**
     * abbreviate name -> full name, and foreign key name -> property name
     */
    private Map<String, String> columnMapper = new HashMap<>();


    public Map<String, String> getColumnMapper() {
        return columnMapper;
    }

    public void setColumnMapper(Map<String, String> columnMapper) {
        this.columnMapper = columnMapper;
    }

    public Map<String, String> getTableNameMapper() {
        return tableNameMapper;
    }

    public void setTableNameMapper(Map<String, String> tableNameMapper) {
        this.tableNameMapper = tableNameMapper;
    }

//    String getPackage(String tableName, Type type) {
//        if (tableNameMapper.containsKey(tableName)) {
//            return getTopPackage(tableName) + "." + type.getPackage();
//        } else {
//            return "";
//        }
//    }

    public String getTopPackage(String tableName) {
        if (tableNameMapper.containsKey(tableName)) {
            String qualifier = tableNameMapper.getOrDefault(tableName, "");
            String pkgName = StringUtils.substringBeforeLast(qualifier, ".");
            logger.debug("{}  -> {}", tableName, pkgName);
            return StringUtils.substringBeforeLast(pkgName, ".");
        } else {
            return "";
        }
    }

//    public String getClassName(String tableName) {
//        return getDomainName(tableName);
//    }

    public Property getProperty(Column column, Type type) {
        Property p = new Property(column);
        p.invoke(type, getColumnMapper());
        return p;
    }


    String getDomainName(String tableName) {
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

    public class Property {

        private Column column;
        private String propertyName;
        private String methodName;
        private String typeName;

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

        private void invoke(Type type, Map<String, String> mapper) {
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
                if (type != Type.JPA) {
                    typeName = column.getSimpleJavaType();
                    String mapName = columnMapper.getOrDefault(column.getName(), column.getReferenceTable());
                    if (columnMapper.containsKey(column.getName())) {
                        logger.info("map {} -> {}", column.getReferenceTable(), mapName);
                    }

                    propertyName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, getDomainName(mapName)) + "Uuid";
                    methodName = getDomainName(mapName) + "Uuid";
                } else {
                    String referenceTable = column.getReferenceTable();
                    typeName = getDomainName(referenceTable);
                    String mapName = columnMapper.getOrDefault(column.getName(), referenceTable);
                    if (columnMapper.containsKey(column.getName())) {
                        logger.info("map {} -> {}", referenceTable, mapName);
                    }
                    if (columnMapper.containsKey(column.getName())) {
                        propertyName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, mapName);
                        methodName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, mapName);
                    } else {
                        propertyName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, typeName);
                        methodName = getDomainName(mapName);
                    }
                }
            }
            logger.debug("property:{}, method:{}, type:{}", propertyName, methodName, typeName);
        }

        public boolean isDateType() {
            return ArrayUtils.contains(new String[]{"java.util.Date", "java.sql.Date", "java.sql.Time", "java.sql.Timestamp"}, column.getJavaType());
        }
    }
}