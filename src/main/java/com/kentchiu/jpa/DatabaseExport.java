package com.kentchiu.jpa;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kentchiu.jpa.domain.Column;
import com.kentchiu.jpa.domain.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class DatabaseExport {

    protected Connection connection;
    Logger logger = LoggerFactory.getLogger(DatabaseExport.class);
    private Map<String, String> foreignKeyMap = Maps.newHashMap();
    private HashMultimap<String, String> uniqueKeyMap = HashMultimap.create();

    public HashMultimap<String, String> getUniqueKeyMap() {
        return uniqueKeyMap;
    }

    public void setUniqueKeyMap(HashMultimap<String, String> uniqueKeyMap) {
        this.uniqueKeyMap = uniqueKeyMap;
    }

    public Map<String, String> getForeignKeyMap() {
        return foreignKeyMap;
    }

    public void setForeignKeyMap(Map<String, String> foreignKeyMap) {
        this.foreignKeyMap = foreignKeyMap;
    }

    public Table createTable(String tableName) throws SQLException {
        logger.info("create table: {}", tableName);
        Table table = new Table();
        table.setName(tableName);
        table.setPrimaryKey(getPrimaryKey(tableName));
        List<Column> columns = buildColumns(tableName);
        table.setColumns(columns);
        table.setComment(getTableComment(tableName));
        return table;
    }


    String getPrimaryKey(String tableName) throws SQLException {
        DatabaseMetaData databaseMetaData = this.connection.getMetaData();
        String pkName = "";
        ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(null, null, tableName);
        while (primaryKeys.next()) {
            pkName = primaryKeys.getString("COLUMN_NAME");
        }
        return pkName;
    }

    protected List<Column> buildColumns(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from " + tableName);
        ResultSetMetaData metaData = rs.getMetaData();
        Map<String, String> commentMap = buildColumnCommentMap(tableName);

        Set<String> uniqueKeys = uniqueKeyMap.get(tableName);

        int columnCount = metaData.getColumnCount();
        List<Column> columns = new ArrayList<>();
        for (int i = 1; i < columnCount + 1; i++) {
            String name = metaData.getColumnName(i);
            boolean isNullable = metaData.isNullable(i) == 1;
            String javaType = metaData.getColumnClassName(i);
            String referenceTable = foreignKeyMap.getOrDefault(name, "");
            String comment = commentMap.getOrDefault(name, "");
            logger.trace("------ {}.{} ----------", tableName, name);
            logger.trace("getColumnClassName: {}", javaType);
            logger.trace("getColumnLabel: {}", metaData.getColumnLabel(i));
            logger.trace("getColumnType: {}", metaData.getColumnType(i));
            logger.trace("getColumnName: {}", name);
            logger.trace("getColumnTypeName: {}", metaData.getColumnTypeName(i));
            logger.trace("getPrecision: {}", metaData.getPrecision(i));
            logger.trace("getScale: {}", metaData.getScale(i));
            logger.trace("isAutoIncrement: {}", metaData.isAutoIncrement(i));
            logger.trace("isCaseSensitive: {}", metaData.isCaseSensitive(i));
            logger.trace("isCurrency: {}", metaData.isCurrency(i));
            logger.trace("isNullable: {}", isNullable);
            logger.trace("reference table: {}", referenceTable);
            logger.trace("comment: {}", comment);
            Column column = new Column();
            column.setName(name);
            column.setJavaType(javaType);
            column.setNullable(isNullable);
            column.setReferenceTable(referenceTable);
            column.setComment(comment);
            column.setUnique(uniqueKeys.contains(name));
            columns.add(column);
        }
        return columns;
    }


    List<Table> buildTables(String tableNamePattern) throws SQLException {
        DatabaseMetaData databaseMetaData = this.connection.getMetaData();

        List<Table> tables = Lists.newArrayList();
        ResultSet tableResultSet = databaseMetaData.getTables(null, null, tableNamePattern, new String[]{"TABLE"});
        while (tableResultSet.next()) {
            // dumping debug
            ResultSetMetaData tableMetaData = tableResultSet.getMetaData();
            for (int i = 1; i <= tableMetaData.getColumnCount(); i++) {
                String name = tableMetaData.getColumnName(i);
                logger.trace("{} => {}", name, tableResultSet.getString(name));
            }
            Table table = new Table();
            table.setName(tableResultSet.getString("TABLE_NAME"));
            tables.add(table);
        }
        return tables;
    }
//    no idea how to distinguish pk, index, unique, composite key unique
//    public ArrayList<String> getUniqueColumns(String tableName) throws SQLException {
//        DatabaseMetaData databaseMetaData = this.connection.getMetaData();
//        boolean listUniqueIndex = true;
//        ResultSet rs = databaseMetaData.getIndexInfo(null, null, tableName, listUniqueIndex, true);
//        ArrayList<String> results = Lists.newArrayList();
//        while (rs.next()) {
//            String indexName = rs.getString("INDEX_NAME");
//            String table = rs.getString("TABLE_NAME");
//            String schema = rs.getString("TABLE_SCHEM");
//            String columnName = rs.getString("COLUMN_NAME");
//            if (indexName == null) {
//                continue;
//            }
//            System.out.println("****************************************");
//            System.out.println("Table: " + schema + "." + table);
//            System.out.println("Index Name: " + indexName);
//            System.out.println("Column Name: " + columnName);
//            results.add(columnName);
//        }
//        return results;
//    }

    protected abstract Map<String, String> buildColumnCommentMap(String tableName);

    protected abstract String getTableComment(String tableName) throws SQLException;

}
