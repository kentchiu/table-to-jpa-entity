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
     * table name -> java fully qualified name.
     */
    private Map<String, String> tableNameMapper = new HashMap<>();
    /**
     * abbreviate name -> full name, and foreign key name -> property name.
     */
    private Map<String, String> columnMapper = new HashMap<>();
    /**
     * master - detail mapper. Mapping by DetailConfig.
     */
    private Map<String, DetailConfig> masterDetailMapper = new HashMap<>();

    public Map<String, DetailConfig> getMasterDetailMapper() {
        return masterDetailMapper;
    }

    public void setMasterDetailMapper(Map<String, DetailConfig> masterDetailMapper) {
        this.masterDetailMapper = masterDetailMapper;
    }

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

    public String getTopPackage(String tableName) {
        if (tableNameMapper.containsKey(tableName)) {
            String qualifier = tableNameMapper.getOrDefault(tableName, "");
            Preconditions.checkState(StringUtils.contains(qualifier, "domain"), "table mapper value should contain domain package");
            String modulePackage = StringUtils.substringBefore(qualifier, ".domain");
            return StringUtils.substringBeforeLast(modulePackage, ".");
        } else {
            return "";
        }
    }


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

    public String getModuleName(String tableName) {
        if (tableNameMapper.containsKey(tableName)) {
            String qualifier = tableNameMapper.getOrDefault(tableName, "");
            Preconditions.checkState(StringUtils.contains(qualifier, "domain"), "table mapper value should contain domain package but was " + qualifier);
            String modulePackage = StringUtils.substringBefore(qualifier, ".domain");
            return StringUtils.substringAfterLast(modulePackage, ".");
        } else {
            return "";
        }
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
                processSimpleColumn(type, name);
            } else {
                processReference(type);
            }
            logger.debug("property:{}, method:{}, type:{}", propertyName, methodName, typeName);
        }

        private void processSimpleColumn(Type type, String name) {
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
        }

        private void processReference(Type type) {
            if (type != Type.JPA) {
                typeName = "String";
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

        public boolean isDateType() {
            return ArrayUtils.contains(new String[]{"java.util.Date", "java.sql.Date", "java.sql.Time", "java.sql.Timestamp"}, column.getJavaType());
        }
    }
}