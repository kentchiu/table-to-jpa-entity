package com.kentchiu.jpa;


import com.kentchiu.jpa.domain.Table;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Ignore
public class MySqlExportTest {

    private MySqlExport export;

    @Before
    public void setUp() throws Exception {
        String url = "jdbc:mysql://127.0.0.1:3306/i1";
        String username = "root";
        String password = "password";
        export = new MySqlExport(url, username, password);
    }

    @Test
    public void testGetTables() throws Exception {
        Table table = export.createTable("PLM_BASE_ITEM_FILE");
        assertThat(table.getName(), not(Matchers.isEmptyOrNullString()));
        assertThat(table.getColumns().size(), greaterThan(0));
        assertThat(table.getColumns().get(0).getName(), not(Matchers.isEmptyOrNullString()));
    }


    @Test
    public void testBuildTableCommentMap() throws Exception {
        Map<String, String> comments = export.buildTableCommentMap("PLM%");
        assertThat(comments.isEmpty(), is(false));
        assertThat(comments.get("PLM_BASE_ITEM_FILE"), containsString("商品管理模块"));
    }

    @Test
    public void testGetTableComment() throws Exception {
        assertThat(export.getTableComment("PLM_BASE_ITEM_FILE"), containsString("商品管理模块"));
    }


    @Test
    public void testBuildColumnCommentMap() throws Exception {
        Map<String, String> comments = export.buildColumnCommentMap("PLM_BASE_ITEM_FILE");
        assertThat(comments.isEmpty(), is(false));
        assertThat(comments.get("UUID"), is("uuid"));
        assertThat(comments.get("BARCODE"), is("条形码"));
    }


}