package com.kentchiu.jpa;

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

public class ServiceGeneratorTest {

    private ServiceGenerator generator;

    @Before
    public void setUp() throws Exception {
        generator = new ServiceGenerator(new Config());
    }

    @Test
    public void testExport() throws Exception {
        Table table = Tables.table1();
        generator.setTableNameMapper(ImmutableMap.of(table.getName(), "com.kentchiu.module.FooBar"));
        Optional<Path> export = generator.exportToFile(table, ImmutableList.of());
        assertThat(export.isPresent(), Is.is(true));
        assertThat(export.get().toString(), containsString("/src/main/java/com/kentchiu/service/FooBarService.java"));
    }


    @Test
    public void testApplyTemplate() throws Exception {
        Table table = Tables.table1();
        generator.setTableNameMapper(ImmutableMap.of(table.getName(), "com.kentchiu.module.FooBar"));
        List<String> list = generator.applyTemplate(table);
        int i = 0;
        assertThat(list.get(i++), is("package com.kentchiu.service;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import com.bq.i1.base.serivce.CrudService;"));
        assertThat(list.get(i++), is("import com.kentchiu.domain.FooBar;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("public interface FooBarService extends CrudService<FooBar> {"));
        assertThat(list.get(i++), is("}"));
    }


}