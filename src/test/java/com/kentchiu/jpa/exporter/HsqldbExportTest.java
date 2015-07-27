package com.kentchiu.jpa.exporter;

import com.google.common.collect.ImmutableMap;
import com.kentchiu.jpa.domain.Column;
import com.kentchiu.jpa.domain.Table;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class HsqldbExportTest {

    private static HsqldbExport export;

    @BeforeClass
    public static void setUp() throws Exception {
        String url = "jdbc:hsqldb:mem:mymemdb";
        String username = "SA";
        String password = "";
        export = new HsqldbExport(url, username, password);
        Connection c = export.connection;

        Statement stmt = c.createStatement();
        String sql = "CREATE TABLE PUBLIC.TEST_TABLE\n" +
                "(COL1 INTEGER NOT NULL,\n" +
                "COL2 CHAR(25),\n" +
                "COL3 VARCHAR(25),\n" +
                "COL4 DECIMAL(10,2) NOT NULL,\n" +
                "COL5 DATE,\n" +
                "PRIMARY KEY (COL1))";
        stmt.execute(sql);
        stmt.execute("ALTER TABLE PUBLIC.TEST_TABLE ALTER COLUMN COL4 SET DEFAULT 99;");
    }

    @Test
    public void testGetTable() throws Exception {
        Table table = export.createTable("TEST_TABLE");
        assertThat(table.getName(), not(Matchers.isEmptyOrNullString()));
        assertThat(table.getColumns().size(), greaterThan(0));
        assertThat(table.getColumns().get(0).getName(), not(Matchers.isEmptyOrNullString()));
    }

    @Test
    public void testPrimaryKey() throws Exception {
        String pk = export.getPrimaryKey("TEST_TABLE");
        assertThat(pk, is("COL1"));
    }


    @Test
    public void testUniqueKey() throws Exception {
        export.getUniqueKeyMap().put("TEST_TABLE", "COL2");
        export.getUniqueKeyMap().put("TEST_TABLE", "COL3");

        Table table = export.createTable("TEST_TABLE");

        Column column = table.getColumns().get(0);
        assertThat(column.getName(), is("COL1"));
        assertThat(column.isUnique(), is(false));

        Column column1 = table.getColumns().get(1);
        assertThat(column1.getName(), is("COL2"));
        assertThat(column1.isUnique(), is(true));

        Column column2 = table.getColumns().get(2);
        assertThat(column2.getName(), is("COL3"));
        assertThat(column2.isUnique(), is(true));

        Column column3 = table.getColumns().get(3);
        assertThat(column3.getName(), is("COL4"));
        assertThat(column3.isUnique(), is(false));
    }

    @Test
    public void testGetReferenceTable() throws Exception {
        export.setForeignKeyMap(ImmutableMap.of("COL3", "Foo"));
        Table table = export.createTable("TEST_TABLE");
        Column column = table.getColumns().stream().filter(c -> StringUtils.equals("COL3", c.getName())).findFirst().get();
        assertThat(column.getReferenceTable(), is("Foo"));
    }

}
