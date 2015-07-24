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

public class RepositoryGeneratorTest {

    private RepositoryGenerator generator;

    @Before
    public void setUp() throws Exception {
        generator = new RepositoryGenerator();
    }

    @Test
    public void testExport() throws Exception {
        Table table = Tables.table1();
        generator.setTableNameMapper(ImmutableMap.of(table.getName(), "com.kentchiu.module.domain.FooBar"));
        Optional<Path> export = generator.exportToFile(table, ImmutableList.of());
        assertThat(export.isPresent(), Is.is(true));
        assertThat(export.get().toString(), containsString("/src/main/java/com/kentchiu/module/dao/FooBarRepository.java"));
    }

    @Test
    public void testApplyTemplate() throws Exception {
        Table table = Tables.table1();
        generator.setTableNameMapper(ImmutableMap.of(table.getName(), "com.kentchiu.module.domain.FooBar"));
        List<String> list = generator.applyTemplate(table);
        int i = 0;
        assertThat(list.get(i++), is("package com.kentchiu.module.dao;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.FooBar;"));
        assertThat(list.get(i++), is("import org.springframework.data.jpa.repository.JpaRepository;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("public interface FooBarRepository extends JpaRepository<FooBar, String> {"));
        assertThat(list.get(i++), is("}"));
    }

}