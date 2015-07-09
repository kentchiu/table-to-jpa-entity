package com.kentchiu.jpa;

import com.google.common.collect.Maps;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class MySqlExport extends DatabaseExport {

    public MySqlExport(String url, String username, String password) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    protected Map<String, String> buildColumnCommentMap(String tableName) {
        Map<String, String> results = Maps.newTreeMap();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT a.COLUMN_NAME , a.COLUMN_COMMENT COMMENTS FROM  information_schema.COLUMNS a WHERE a.TABLE_NAME = '" + tableName + "'");
            while (rs.next()) {
                results.put(rs.getString("COLUMN_NAME"), rs.getString("COMMENTS"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    protected Map<String, String> buildTableCommentMap(String tableNamePattern) throws SQLException {
        Statement stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery("select TABLE_NAME,TABLE_COMMENT from information_schema.TABLES where TABLE_NAME like '" + tableNamePattern + "'");
        Map<String, String> results = Maps.newHashMap();
        while (rs.next()) {
            String name = rs.getString("TABLE_NAME");
            String comment = rs.getString("TABLE_COMMENT");
            results.put(name, comment);
        }
        return results;
    }

    @Override
    protected String getTableComment(String tableName) throws SQLException {
        Statement stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery("select TABLE_NAME,TABLE_COMMENT from information_schema.TABLES where TABLE_NAME = '" + tableName + "'");
        while (rs.next()) {
            return rs.getString("TABLE_COMMENT");
        }
        return "";
    }
}
