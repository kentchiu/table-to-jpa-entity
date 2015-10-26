package com.kentchiu.jpa.generator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kentchiu.jpa.domain.Table;
import com.kentchiu.jpa.domain.Tables;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class ResourceTestGeneratorTest {

    private ResourceGenerator generator;

    @Before
    public void setUp() throws Exception {
        Table table = Tables.table1();
        Transformer transformer = new Transformer();
        transformer.setTableNameMapper(ImmutableMap.of(table.getName(), "com.kentchiu.module.domain.FooBar"));
        generator = new ResourceGenerator(transformer);
    }

    @Test
    public void testExport() throws Exception {
        Table table = Tables.table1();
        Optional<Path> export = generator.exportToFile(table, ImmutableList.of());
        assertThat(export.isPresent(), Is.is(true));
        assertThat(export.get().toString(), containsString("/api/module/fooBar.md"));
    }


    @Test
    public void testApplyTemplate() throws Exception {
        Table table = Tables.table1();
        generator.getExtraParams().put("title", "FooBar");
        List<String> list = generator.applyTemplate(table);
        int i = 0;
        list.stream().forEach(System.out::println);
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("# Group FooBar管理"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("FooBar屬性:"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{FooBarControllerTest/FooBar.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("## FooBar清單 [/fooBars]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("### 取得FooBar清單 [GET]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("Arguments:"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{FooBarControllerTest/FooBarQuery.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("``` bash"));
        assertThat(list.get(i++), is("{{FooBarControllerTest/testListFooBars-curl.md}}"));
        assertThat(list.get(i++), is("```"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{FooBarControllerTest/testListFooBars.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("### 新增FooBar [POST]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("Arguments:"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{FooBarControllerTest/FooBarInput.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("``` bash"));
        assertThat(list.get(i++), is("{{FooBarControllerTest/testAddFooBar-curl.md}}"));
        assertThat(list.get(i++), is("```"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{FooBarControllerTest/testAddFooBar.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("## FooBar [/fooBars/{uuid}]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("+ Parameters"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    + uuid: `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` (required, UUID) - The FooBar UUID"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("### 取得FooBar [GET]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("``` bash"));
        assertThat(list.get(i++), is("{{FooBarControllerTest/testGetFooBar-curl.md}}"));
        assertThat(list.get(i++), is("```"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{FooBarControllerTest/testGetFooBar.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("### 更新FooBar [PATCH]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("Arguments:"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{FooBarControllerTest/FooBarUpdateInput.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("``` bash"));
        assertThat(list.get(i++), is("{{FooBarControllerTest/testUpdateFooBar-curl.md}}"));
        assertThat(list.get(i++), is("```"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{FooBarControllerTest/testUpdateFooBar.md}}"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("### 刪除FooBar [DELETE]"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("``` bash"));
        assertThat(list.get(i++), is("{{FooBarControllerTest/testDeleteFooBar-curl.md}}"));
        assertThat(list.get(i++), is("```"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("{{FooBarControllerTest/testDeleteFooBar.md}}"));
    }


}