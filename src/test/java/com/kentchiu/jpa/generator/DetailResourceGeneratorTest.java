package com.kentchiu.jpa.generator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kentchiu.jpa.domain.Table;
import com.kentchiu.jpa.domain.Tables;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class DetailResourceGeneratorTest {

    private DetailResourceGenerator generator;
    private Table table;

    @Before
    public void setUp() throws Exception {
        table = Tables.detail();
        Transformer transformer = new Transformer();
        Map<String, String> mapper = new HashMap<>();
        mapper.put("TBL_MASTER", "com.kentchiu.module.domain.Master");
        mapper.put("TBL_DETAIL", "com.kentchiu.module.domain.Detail");
        transformer.setTableNameMapper(mapper);
        DetailConfig config = new DetailConfig("master", "detail", "TBL_MASTER", "TBL_DETAIL");
        transformer.setMasterDetailMapper(ImmutableMap.of(table.getName(), config));


        generator = new DetailResourceGenerator(transformer);
        generator.getExtraParams().put("title", "Detail");
    }

    @Test
    public void testExport() throws Exception {
        Optional<Path> export = generator.exportToFile(table, ImmutableList.of());
        assertThat(export.isPresent(), Is.is(true));
        assertThat(export.get().toString(), containsString("/api/module/detail.md"));
    }


    @Test
    public void testApplyTemplate() throws Exception {
        List<String> list = generator.applyTemplate(table);

        int i = 0;

        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("# Group Detail管理"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("Detail屬性:"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{DetailControllerTest/Detail.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("## Detail清單 [/masters/{masterUuid}/details]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("+ Parameters"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    + masterUuid: `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` (required, UUID) - The master UUID"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("### 取得Detail清單 [GET]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("Arguments:"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{DetailControllerTest/DetailQuery.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("``` bash"));
        assertThat(list.get(i++), is("{{DetailControllerTest/testListDetails-curl.md}}"));
        assertThat(list.get(i++), is("```"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{DetailControllerTest/testListDetails.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("### 新增Detail [POST]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("Arguments:"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{DetailControllerTest/DetailInput.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("``` bash"));
        assertThat(list.get(i++), is("{{DetailControllerTest/testAddDetail-curl.md}}"));
        assertThat(list.get(i++), is("```"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{DetailControllerTest/testAddDetail.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("## Detail [/masters/{masterUuid}/details/{uuid}]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("+ Parameters"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    + masterUuid: `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` (required, UUID) - The master UUID"));
        assertThat(list.get(i++), is("    + uuid: `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` (required, UUID) - The detail UUID"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("### 取得Detail [GET]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("``` bash"));
        assertThat(list.get(i++), is("{{DetailControllerTest/testGetDetail-curl.md}}"));
        assertThat(list.get(i++), is("```"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{DetailControllerTest/testGetDetail.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("### 更新Detail [PATCH]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("Arguments:"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{DetailControllerTest/DetailUpdateInput.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("``` bash"));
        assertThat(list.get(i++), is("{{DetailControllerTest/testUpdateDetail-curl.md}}"));
        assertThat(list.get(i++), is("```"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{DetailControllerTest/testUpdateDetail.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("### 刪除Detail [DELETE]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("``` bash"));
        assertThat(list.get(i++), is("{{DetailControllerTest/testDeleteDetail-curl.md}}"));
        assertThat(list.get(i++), is("```"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{DetailControllerTest/testDeleteDetail.md}}"));

    }


}