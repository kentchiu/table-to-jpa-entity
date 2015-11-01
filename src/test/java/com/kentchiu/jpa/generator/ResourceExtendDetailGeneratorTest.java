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

public class ResourceExtendDetailGeneratorTest {

    private ResourceExtendDetailGenerator generator;
    private Table table;

    @Before
    public void setUp() throws Exception {
        Table table = Tables.extendDetail();
        Transformer transformer = new Transformer();

        Map<String, String> mapper = new HashMap<>();
        mapper.put("TBL_MASTER", "com.kentchiu.module.domain.Master");
        mapper.put("TBL_DETAIL", "com.kentchiu.module.domain.Detail");
        mapper.put("TBL_EXTEND_DETAIL", "com.kentchiu.module.domain.ExtendDetail");
        transformer.setTableNameMapper(mapper);
        DetailConfig detailConfig = new DetailConfig("master", "detail", "TBL_MASTER", "TBL_DETAIL");
        ExtendDetailConfig config = new ExtendDetailConfig(detailConfig, "extendDetail", "TBL_EXTEND_DETAIL");
        transformer.setMasterDetailMapper(ImmutableMap.of(table.getName(), config));


        generator = new ResourceExtendDetailGenerator(transformer);
        generator.getExtraParams().put("title", "FooBar");
    }

    @Test
    public void testExport() throws Exception {
        Table table = Tables.extendDetail();
        Optional<Path> export = generator.exportToFile(table, ImmutableList.of());
        assertThat(export.isPresent(), Is.is(true));
        assertThat(export.get().toString(), containsString("/api/module/extendDetail.md"));
    }


    @Test
    public void testApplyTemplate() throws Exception {
        Table table = Tables.extendDetail();
        List<String> list = generator.applyTemplate(table);

        list.stream().forEach(System.out::println);
        int i = 0;

        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("# Group FooBar管理"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("FooBar屬性:"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{ExtendDetailControllerTest/ExtendDetail.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("## FooBar清單 [/masters/{masterUuid}/details]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("+ Parameters"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    + masterUuid: `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` (required, UUID) - The master UUID"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("### 取得FooBar清單 [GET]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("Arguments:"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{ExtendDetailControllerTest/ExtendDetailQuery.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("``` bash"));
        assertThat(list.get(i++), is("{{ExtendDetailControllerTest/testListExtendDetails-curl.md}}"));
        assertThat(list.get(i++), is("```"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{ExtendDetailControllerTest/testListExtendDetails.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("### 新增FooBar [POST]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("Arguments:"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{ExtendDetailControllerTest/ExtendDetailInput.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("``` bash"));
        assertThat(list.get(i++), is("{{ExtendDetailControllerTest/testAddExtendDetail-curl.md}}"));
        assertThat(list.get(i++), is("```"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{ExtendDetailControllerTest/testAddExtendDetail.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("## FooBar [/masters/{masterUuid}/details/{uuid}]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("+ Parameters"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    + masterUuid: `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` (required, UUID) - The master UUID"));
        assertThat(list.get(i++), is("    + uuid: `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` (required, UUID) - The detail UUID"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("### 取得FooBar [GET]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("``` bash"));
        assertThat(list.get(i++), is("{{ExtendDetailControllerTest/testGetExtendDetail-curl.md}}"));
        assertThat(list.get(i++), is("```"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{ExtendDetailControllerTest/testGetExtendDetail.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("### 更新FooBar [PATCH]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("Arguments:"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{ExtendDetailControllerTest/ExtendDetailUpdateInput.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("``` bash"));
        assertThat(list.get(i++), is("{{ExtendDetailControllerTest/testUpdateExtendDetail-curl.md}}"));
        assertThat(list.get(i++), is("```"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{ExtendDetailControllerTest/testUpdateExtendDetail.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("### 刪除FooBar [DELETE]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("``` bash"));
        assertThat(list.get(i++), is("{{ExtendDetailControllerTest/testDeleteExtendDetail-curl.md}}"));
        assertThat(list.get(i++), is("```"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{ExtendDetailControllerTest/testDeleteExtendDetail.md}}"));


    }


}