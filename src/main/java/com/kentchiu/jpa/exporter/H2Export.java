package com.bellwin.ipac;

import com.google.common.collect.Maps;
import com.kentchiu.jpa.exporter.DatabaseExport;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

// TODO: MOVE TO CODE GEN PROJECT
public class H2Export extends DatabaseExport {

    public H2Export(String url, String username, String password) {
        try {
            Class.forName("org.h2.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected Map<String, String> buildColumnCommentMap(String tableName) {
        HashMap<String, String> results = Maps.newHashMap();
        try {
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + tableName + "'";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                results.put(rs.getString("COLUMN_NAME"), rs.getString("REMARKS"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected String getTableComment(String tableName) throws SQLException {
        try {
            Statement stmt = connection.createStatement();
            String sql = "SELECT REMARKS FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '" + tableName + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                return rs.getString("REMARKS");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }


}
