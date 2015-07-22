package com.kentchiu.jpa;

import org.junit.Before;
import org.junit.Test;

public class RepositoryGeneratorTest {

    private RepositoryGenerator generator;


    @Before
    public void setUp() throws Exception {
        generator = new RepositoryGenerator(new Config());
    }

    @Test
    public void testExport() throws Exception {
//        Table table = Tables.table1();
//        generator.setTableNameMapper(ImmutableMap.of(table.getName(), "com.kentchiu.module.FooBar"));
//        Optional<Path> export = generator.export(table, ImmutableList.of());
//        assertThat(export.isPresent(), Is.is(true));
//        assertThat(export.get().toString(), containsString("/src/main/java/com/kentchiu/domain/FooBar.java"));
    }

}