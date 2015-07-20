package com.kentchiu.jpa;

import com.google.common.collect.ImmutableMap;
import com.kentchiu.jpa.domain.Column;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

public abstract class AbstractGeneratorTest {

    EntityGenerator generator;


    void dump(List<String> lines) {
        lines.stream().forEach(System.out::println);
    }


    @Test
    public void testImports_reference_tables() throws Exception {
        generator.setTableNameMapper(ImmutableMap.of("MY_TABLE", "com.foo.bar.Foo", "MY_TABLE_2", "com.foo.bar.Boo"));
        List<String> imports = generator.buildImports();
        assertThat(imports, hasItem("com.foo.bar.Foo"));
        assertThat(imports, hasItem("com.foo.bar.Boo"));
    }

    @Test
    public void testAttributeInfo() throws Exception {
        Column comment = new Column();
        comment.setDescription("column comment");
        assertThat(generator.buildAttributeInfo(comment), is("@AttributeInfo(description = \"column comment\")"));
    }

    @Test
    public void testAttributeInfo_with_options() throws Exception {
        Column comment = new Column();
        comment.setDescription("产品类型");
        Map<String, String> options = comment.getOptions();
        options.put("1", "套件");
        options.put("2", "包件");
        options.put("3", "补件");
        options.put("4", "其他");

        assertThat(generator.buildAttributeInfo(comment), is("@AttributeInfo(description = \"产品类型\", format = \"1=套件/2=包件/3=补件/4=其他\")"));
    }

    @Test
    public void testOptions() throws Exception {
        Column column = new Column();
        column.getOptions().put("Y", "foo");
        column.getOptions().put("N", "bar");
        assertThat(generator.buildOptions(column), is("@Option(value = {\"Y\", \"N\"})"));
    }
}
