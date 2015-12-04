package com.kentchiu.jpa.generator;

import com.google.common.collect.ImmutableMap;
import com.kentchiu.jpa.domain.Tables;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class QueryTestGeneratorTest extends DomainObjectGeneratorTest {


    @Before
    public void setUp() throws Exception {
        generator = new EntityGenerator(new Transformer(), new Config(Type.QUERY_TEST));
        generator.setProjectHome(Files.createTempDirectory("java"));
        generator.transformer.setTableNameMapper(ImmutableMap.of("MY_TABLE_1", "com.foobar.module.domain.FooBar"));
    }


    @Test
    public void testGenerate() throws Exception {
        generator.export(Tables.table1());
        assertThat(Files.exists(generator.getJavaTestSourceHome().resolve("com/foobar/module/service/query/FooBarQueryTest.java")), is(true));
    }


    @Test
    public void testExportTable() throws Exception {
        List<String> lines = generator.exportTable(Tables.table1());
        AbstractGenerator.dump(lines);

        int i = 0;
        assertThat(lines.get(i++), is("package com.foobar.module.service.query;"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("import org.junit.Test;"));
        assertThat(lines.get(i++), is("import org.torpedoquery.jpa.Query;"));
        assertThat(lines.get(i++), is("import java.util.UUID;"));
        assertThat(lines.get(i++), is("import static org.hamcrest.Matchers.is;"));
        assertThat(lines.get(i++), is("import static org.junit.Assert.assertThat;"));
        assertThat(lines.get(i++), is("import com.foobar.module.domain.FooBar;"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("public class FooBarQueryTest {"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    @Test"));
        assertThat(lines.get(i++), is("    public void testBuildQuery() throws Exception {"));
        assertThat(lines.get(i++), is("        FooBarQuery query = new FooBarQuery();"));
        assertThat(lines.get(i++), is("        Query<FooBar> q = query.buildQuery();"));
        assertThat(lines.get(i++), is("        assertThat(q.getQuery(), is(\"select fooBar_0 from FooBar fooBar_0\"));"));
        assertThat(lines.get(i++), is("        assertThat(q.getParameters().size(), is(0));"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("}"));


    }

}
