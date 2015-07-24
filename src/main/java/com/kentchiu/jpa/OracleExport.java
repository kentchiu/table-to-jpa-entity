package com.kentchiu.jpa;

import com.google.common.collect.Maps;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class OracleExport extends DatabaseExport {

    public OracleExport(String url, String username, String password) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected Map<String, String> buildColumnCommentMap(String tableName) {
        Map<String, String> results = Maps.newTreeMap();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from USER_COL_COMMENTS where TABLE_NAME = '" + tableName + "'");
            while (rs.next()) {
                results.put(rs.getString("COLUMN_NAME"), rs.getString("COMMENTS"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected String getTableComment(String tableName) throws SQLException {
        Statement stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery("SELECT t.TABLE_NAME TABLE_NAME, tc.COMMENTS TABLE_COMMENT FROM USER_TABLES t LEFT JOIN  USER_TAB_COMMENTS tc on t.TABLE_NAME = tc.TABLE_NAME WHERE t.TABLE_NAME = '" + tableName + "'");
        while (rs.next()) {
            return rs.getString("TABLE_COMMENT");
        }
        return "";
    }
}
