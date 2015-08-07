package com.kentchiu.jpa.generator;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.kentchiu.jpa.domain.Column;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Table model -> Java Model
 */
public class Transformer {

    private Logger logger = LoggerFactory.getLogger(Transformer.class);
    private Map<String, String> tableNameMapper = new HashMap<>();
    private Map<String, String> columnMapper = new HashMap<>();
    private Map<String, DetailConfig> masterDetailMapper = new HashMap<>();

    public Map<String, DetailConfig> getMasterDetailMapper() {
        return masterDetailMapper;
    }

    /**
     * master - detail mapper. Mapping by DetailConfig.
     * <p/>
     * 如果 table有 master - detail 的關係，可透過 `MasterDetailsMapper` 做 master - detail 的mapping，這樣 code gen出來的 detail controller
     * 就會有 master - detail關聯的 controller
     * ex:
     * TBL_ORDER - TBL_ITEM 這兩個 object 是 master - detail的關係
     * 如果沒有做 mapping， TBL_ITEM 產生出來的 controller 的 api是像這樣 `/items`
     * 如果有做 mapping， TBL_ITEM 產生出來的 controller 的 api是像這樣 `orders/{orderUuid}/items`
     */
    public void setMasterDetailMapper(Map<String, DetailConfig> masterDetailMapper) {
        this.masterDetailMapper = masterDetailMapper;
    }

    public Map<String, String> getColumnMapper() {
        return columnMapper;
    }

    /**
     * abbreviate name -> full name, and foreign key name -> property name.
     * <p/>
     * database column name 轉成換 domain object property時，有時需要做名稱的映射(mapping)
     * ex: 在db裡，可能商問數量是 `ITEM_QTY`, 但我們希望在程式裡不要使用縮寫字，那就可以把原來的`QTY` mapping成 `QUANTITY`
     * 如果沒有做mapping，`ITEM_QTY` code gen的結果是，`itemQty`
     * 如果`QTY` mapping成 `QUANTITY`，code gen的結果是 `itemQuantity`
     */
    public void setColumnMapper(Map<String, String> columnMapper) {
        this.columnMapper = columnMapper;
    }


    public Map<String, String> getTableNameMapper() {
        return tableNameMapper;
    }

    /**
     * table name -> java fully qualified name.
     * <p/>
     * 將table name mapping成 java class name.
     * schema中的table name，根據 schema name rule的規範，會像這樣`FOO_BAR_MY_TABLE`，
     * 如果這樣的 table name，直接轉成 java class name，會變這樣 `fooBarMyTable`，
     * 這樣的名稱可讀性不高，而且也會被 code gen到根目錄
     * 所以，透過 `tableNameMapper` 可以把 table name mapping 至適合的package及適合的 class name
     * ex: 原來的 ``FOO_BAR_MY_TABLE` mapping 成 `foo.bar.domain.MyObject`這樣，code gen時，就會在
     * `foo.bar.domain`下產生名為 `MyObject` 的class
     * <p/>
     * NOTE: mapping的package name，最後一定要是以 `.domain` 結尾，因為 code gen會自動去算出模組名稱，然後找到適當的package 放其他  code 出來的code
     */
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
//            String[] searchList = mapper.keySet().toArray(new String[mapper.size()]);
//            String[] replacementList = mapper.values().toArray(new String[mapper.size()]);
//            String name = StringUtils.replaceEach(column.getName(), searchList, replacementList);
            Set<String> keys = mapper.keySet();

            String name = column.getName();

            for (Map.Entry<String, String> entry : mapper.entrySet()) {
                name = name.replaceAll("_" + entry.getKey() + "_", "_" + entry.getValue() + "_");
                System.out.println(name);
            }

            if (StringUtils.isBlank(column.getReferenceTable())) {
                processSimpleColumn(type, name);
            } else {
                processReference(type);
            }
            logger.debug("property:{}, method:{}, type:{}", propertyName, methodName, typeName);
        }

        private void processSimpleColumn(Type type, String name) {
            if (isDateType()) {
                if (type == Type.QUERY || type == Type.JPA) {
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