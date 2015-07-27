package com.kentchiu.jpa.exporter;

import com.google.common.collect.Maps;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class PostgresExport extends DatabaseExport {

    public PostgresExport(String url, String username, String password) {
        try {
            Class.forName("org.postgresql.Driver");
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
            String sql = "SELECT\n" +
                    "  c.table_name,\n" +
                    "  c.column_name,\n" +
                    "  pgd.description\n" +
                    "FROM pg_catalog.pg_statio_all_tables AS st\n" +
                    "  INNER JOIN pg_catalog.pg_description pgd ON (pgd.objoid = st.relid)\n" +
                    "  INNER JOIN information_schema.columns c ON (pgd.objsubid = c.ordinal_position\n" +
                    "                                              AND c.table_schema = st.schemaname AND c.table_name = st.relname)\n" +
                    "where c.table_name = '" + tableName + "'";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                results.put(rs.getString("column_name"), rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }


    @Override
    protected String getTableComment(String tableName) throws SQLException {
        Statement stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery("SELECT pg_catalog.obj_description('" + tableName + "'::regclass, 'pg_class');");
        while (rs.next()) {
            return rs.getString("obj_description");
        }
        return "";
    }

}
