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

public class DomainFactoryGeneratorTest {

    private DomainFactoryGenerator generator;

    @Before
    public void setUp() throws Exception {
        Transformer transformer = new Transformer();
        Table table = Tables.table1();
        transformer.setTableNameMapper(ImmutableMap.of(table.getName(), "com.kentchiu.module.domain.FooBar", "CITY", "com.kentchiu.module.domain.City"));
        generator = new DomainFactoryGenerator(transformer);
    }

    @Test
    public void testExport() throws Exception {
        Table table = Tables.table1();
        Optional<Path> export = generator.exportToFile(table, ImmutableList.of());
        assertThat(export.isPresent(), Is.is(true));
        assertThat(export.get().toString(), containsString("/src/test/java/com/kentchiu/module/domain/FooBars.java"));
    }


    @Test
    public void testExport_plural() throws Exception {
        Table table = new Table();
        table.setName("CITY");
        Optional<Path> export = generator.exportToFile(table, ImmutableList.of());
        assertThat(export.isPresent(), Is.is(true));
        assertThat(export.get().toString(), containsString("/src/test/java/com/kentchiu/module/domain/Cities.java"));
    }

    @Test
    public void testApplyTemplate() throws Exception {
        Table table = Tables.table1();
        List<String> list = generator.applyTemplate(table);
        int i = 0;
        assertThat(list.get(i++), is("package com.kentchiu.module.domain;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import com.kentchiu.base.domain.BaseDomains;"));
        assertThat(list.get(i++), is("import com.google.common.collect.Lists;"));
        assertThat(list.get(i++), is("import com.kentchiu.spring.base.domain.CsvExporter;"));
        assertThat(list.get(i++), is("import org.springframework.data.domain.Page;"));
        assertThat(list.get(i++), is("import org.springframework.data.domain.PageImpl;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import java.io.IOException;"));
        assertThat(list.get(i++), is("import java.util.List;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("public class FooBars {"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    private FooBars() {"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    public static List<FooBar> all() {"));
        assertThat(list.get(i++), is("        return BaseDomains.export(FooBar.class, \"MY_TABLE_1.csv\");"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    public static Page<FooBar> page(int size) {"));
        assertThat(list.get(i++), is("        List<FooBar> all = all();"));
        assertThat(list.get(i++), is("        if (all.size() > size) {"));
        assertThat(list.get(i++), is("            return new PageImpl<>(all.subList(0, size));"));
        assertThat(list.get(i++), is("        } else {"));
        assertThat(list.get(i++), is("            return  new PageImpl<>(all);"));
        assertThat(list.get(i++), is("        }"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("}"));
        assertThat(list.get(i++), is(""));
    }


}