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
    public void testPackage() throws Exception {
        assertThat(generator.toPackageName(Tables.table1().getName()), is(""));
    }

    @Test
    public void testImports() throws Exception {
        assertThat(generator.buildImports(), hasItem("import com.kentchiu.spring.attribute.AttributeInfo;"));
        assertThat(generator.buildImports(), hasItem("import com.kentchiu.spring.base.domain.Option;"));
        assertThat(generator.buildImports(), hasItem("import org.hibernate.validator.constraints.*;"));
        assertThat(generator.buildImports(), hasItem("import org.hibernate.annotations.GenericGenerator;"));
        assertThat(generator.buildImports(), hasItem("import org.hibernate.annotations.NotFound;"));
        assertThat(generator.buildImports(), hasItem("import org.hibernate.annotations.NotFoundAction;"));
        assertThat(generator.buildImports(), hasItem("import javax.persistence.*;"));
        assertThat(generator.buildImports(), hasItem("import javax.validation.constraints.*;"));
        assertThat(generator.buildImports(), hasItem("import java.util.Date;"));
        assertThat(generator.buildImports(), hasItem("import java.math.BigDecimal;"));
    }

    @Test
    public void testImports_reference_tables() throws Exception {
        generator.setTableNameMapper(ImmutableMap.of("MY_TABLE", "com.foo.bar.Foo", "MY_TABLE_2", "com.foo.bar.Boo"));
        assertThat(generator.buildImports(), hasItem("import com.foo.bar.Foo;"));
        assertThat(generator.buildImports(), hasItem("import com.foo.bar.Boo;"));
    }

    @Test
    public void testAttributeInfo() throws Exception {
        Column comment = new Column();
        comment.setDescription("column comment");
        List<String> lines = generator.attributeInfo(comment);
        assertThat(lines.size(), is(1));
        assertThat(lines.get(0), is("    @AttributeInfo(description = \"column comment\")"));
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

        List<String> lines = generator.attributeInfo(comment);
        assertThat(lines.size(), is(1));
        assertThat(lines.get(0), is("    @AttributeInfo(description = \"产品类型\", format = \"1=套件/2=包件/3=补件/4=其他\")"));
    }

    @Test
    public void testOptions() throws Exception {
        Column column = new Column();
        column.getOptions().put("Y", "foo");
        column.getOptions().put("N", "bar");
        List<String> lines = generator.options(column);
        assertThat(lines.size(), is(1));
        assertThat(lines.get(0), is("    @Option(value = {\"Y\", \"N\"})"));
    }

}
