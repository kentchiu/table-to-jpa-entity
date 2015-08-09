package com.kentchiu.jpa.exporter;


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
        //String url = "jdbc:mysql://121.40.148.3:3306/dm";
        String url = "jdbc:mysql://127.0.0.1:3306/dm";
        String username = "root";
        String password = "password";
        export = new MySqlExport(url, username, password);
    }

    @Test
    public void testGetTables() throws Exception {
        Table table = export.createTable("dm_detail_cash_mst");
        assertThat(table.getName(), not(Matchers.isEmptyOrNullString()));
        assertThat(table.getColumns().size(), greaterThan(0));
        assertThat(table.getColumns().get(0).getName(), not(Matchers.isEmptyOrNullString()));
    }


    @Test
    public void testGetTableComment() throws Exception {
        assertThat(export.getTableComment("dm_detail_cash_mst"), containsString("资金单单头"));
    }


    @Test
    public void testBuildColumnCommentMap() throws Exception {
        Map<String, String> comments = export.buildColumnCommentMap("dm_detail_cash_mst");
        assertThat(comments.isEmpty(), is(false));
        assertThat(comments.get("no"), is("资金单单号"));
    }


}