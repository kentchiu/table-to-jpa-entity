package com.kentchiu.jpa;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PostgresExportTest {
    private OracleExport export;

    @Before
    public void setUp() throws Exception {
        String url = "jdbc:postgresql://localhost:5432/iotview";
        String username = "postgres";
        String password = "postgres";
        export = new OracleExport(url, username, password);
    }


    @Test
    public void testGetPrimaryKey() throws Exception {
        String pk = export.getPrimaryKey("evt_event");
        assertThat(pk, is("uuid"));
    }

    @Test
    public void testGetTableComment() throws Exception {
        assertThat(export.getTableComment("evt_event"), is("事件"));
    }


    @Test
    public void testBuildColumnCommentMap() throws Exception {
        Map<String, String> comments = export.buildColumnCommentMap("evt_event");
        assertThat(comments.isEmpty(), is(false));
        assertThat(comments.get("time"), is("時間(yyyy-MM-dd HH:mm:ss)"));
    }
}