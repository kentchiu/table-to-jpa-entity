package com.kentchiu.jpa;


import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

//@Ignore
public class OracleExportTest {

    private OracleExport export;

    @Before
    public void setUp() throws Exception {
        String url = "jdbc:oracle:thin:@//121.40.148.3:1521";
        String username = "i1";
        String password = "i1";
        export = new OracleExport(url, username, password);
    }


    @Test
    public void testGetPrimaryKey() throws Exception {
        String pk = export.getPrimaryKey("PLM_BASE_ITEM_FILE");
        assertThat(pk, is("UUID"));
    }

    @Test
    public void testGetTableComment() throws Exception {
        assertThat(export.getTableComment("PLM_BASE_ITEM_FILE"), is("Product Lifecycle Management_base_item_file\n商品管理模块_基本_商品_资料"));
    }


    @Test
    public void testBuildColumnCommentMap() throws Exception {
        Map<String, String> comments = export.buildColumnCommentMap("PLM_BASE_ITEM_FILE");
        assertThat(comments.isEmpty(), is(false));
        assertThat(comments.get("UUID"), is("uuid"));
        assertThat(comments.get("BARCODE"), is("条形码"));
    }

}