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

//    @Override
//    protected Map<String, String> buildTableCommentMap(String tableNamePattern) throws SQLException {
//        Statement stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
//        ResultSet rs = stmt.executeQuery("SELECT t.TABLE_NAME TABLE_NAME, tc.COMMENTS TABLE_COMMENT FROM USER_TABLES t LEFT JOIN  USER_TAB_COMMENTS tc on t.TABLE_NAME = tc.TABLE_NAME WHERE t.TABLE_NAME like '" + tableNamePattern + "'");
//        Map<String, String> results = Maps.newHashMap();
//        while (rs.next()) {
//            String name = rs.getString("TABLE_NAME");
//            String comment = rs.getString("TABLE_COMMENT");
//            results.put(name, comment);
//        }
//        return results;
//    }

    @Override
    protected String getTableComment(String tableName) throws SQLException {
        Statement stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery("SELECT t.TABLE_NAME TABLE_NAME, tc.COMMENTS TABLE_COMMENT FROM USER_TABLES t LEFT JOIN  USER_TAB_COMMENTS tc on t.TABLE_NAME = tc.TABLE_NAME WHERE t.TABLE_NAME = '" + tableName + "'");
        while (rs.next()) {
            return rs.getString("TABLE_COMMENT");
        }
        return "";
    }

//    public List<Map<String, Object>> exportData(String sql) throws SQLException {
//        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
//        ResultSet rs = stmt.executeQuery(sql);
//        List<Map<String, Object>> results = Lists.newArrayList();
//        ResultSetMetaData meta = rs.getMetaData();
//        while (rs.next()) {
//            Map<String, Object> record = Maps.newHashMap();
//            int columnCount = meta.getColumnCount();
//            for (int i = 1; i < columnCount + 1; i++) {
//                record.put(meta.getColumnName(i), rs.getObject(i));
//                logger.trace("record :{}", record);
//            }
//            results.add(record);
//        }
//        return results;
//    }
}
