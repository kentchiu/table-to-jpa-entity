package com.kentchiu.jpa;

import com.google.common.collect.Maps;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class HsqldbExport extends DatabaseExport {

    public HsqldbExport(String url, String username, String password) {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected Map<String, String> buildColumnCommentMap(String tableName) {
        return Maps.newHashMap();
    }

    @Override
    protected String getTableComment(String tableName) throws SQLException {
        return "";
    }


}
