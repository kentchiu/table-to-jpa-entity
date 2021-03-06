package com.kentchiu.jpa.generator;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.kentchiu.jpa.domain.Column;
import com.kentchiu.jpa.domain.Columns;
import com.kentchiu.jpa.domain.Tables;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class InputGeneratorTest extends DomainObjectGeneratorTest {

    @Before
    public void setUp() throws Exception {
        generator = new EntityGenerator(new Transformer(), new Config(Type.INPUT));
        generator.setProjectHome(Files.createTempDirectory("java"));
    }

    @Test
    public void testGenerate() throws Exception {
        generator.getTransformer().setTableNameMapper(ImmutableMap.of("MY_TABLE_1", "com.foobar.module.domain.MyTest"));
        generator.export(Tables.table1());
        assertThat(Files.exists(generator.getJavaSourceHome().resolve("com/foobar/module/web/dto/MyTestInput.java")), is(true));
    }

    @Test
    public void testTableMapping() throws Exception {
        generator.getTransformer().setTableNameMapper(ImmutableMap.of("MY_TABLE_1", "com.kentchiu.jpa.domain.FooBar"));
        List<String> lines = generator.exportTable(Tables.table1());
        assertThat(lines, hasItem("package com.kentchiu.jpa.web.dto;"));
        assertThat(lines, hasItem("/**"));
        assertThat(lines, hasItem(" * a table comment"));
        assertThat(lines, hasItem(" */"));
        assertThat(lines, hasItem("public class FooBarInput {"));
    }

    @Test
    public void testColumnMapping() throws Exception {
        Map<String, String> mapper = new HashMap<>();
        mapper.put("MY_TABLE", "com.kentchiu.jpa.FooBar");
        generator.getTransformer().setTableNameMapper(mapper);

        Column column = Columns.createStringColumn("prop", "comment", true);
        column.setReferenceTable("MY_TABLE");
        List<String> lines = generator.buildProperty(column);

        int i = 0;
        assertThat(lines.get(i++), is("    private String fooBarUuid;"));

        i = 2;
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"comment\")"));
        assertThat(lines.get(i++), is("    public String getFooBarUuid() {"));
        assertThat(lines.get(i++), is("        return fooBarUuid;"));
        assertThat(lines.get(i++), is("    }"));

        i = 7;
        assertThat(lines.get(i++), is("    public void setFooBarUuid(String fooBarUuid) {"));
        assertThat(lines.get(i++), is("        this.fooBarUuid = fooBarUuid;"));
        assertThat(lines.get(i++), is("    }"));
    }


    @Test
    public void testProperty_BigDecimal() throws Exception {
        List<String> lines = generator.buildProperty(Columns.bigDecimalColumn());

        int i = 0;
        // field
        assertThat(lines.get(i++), is("    private BigDecimal bigDecimalProperty = BigDecimal.ZERO;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"this is a big decimal property\", defaultValue = \"0\")"));
        assertThat(lines.get(i++), is("    public BigDecimal getBigDecimalProperty() {"));
        assertThat(lines.get(i++), is("        return bigDecimalProperty;"));
        assertThat(lines.get(i++), is("    }"));

        i = 7;
        // setter
        assertThat(lines.get(i++), is("    public void setBigDecimalProperty(BigDecimal bigDecimalProperty) {"));
        assertThat(lines.get(i++), is("        this.bigDecimalProperty = bigDecimalProperty;"));
        assertThat(lines.get(i++), is("    }"));
    }


    @Test
    public void testProperty_date() throws Exception {
        List<String> lines = generator.buildProperty(Columns.dateColumn());

        int i = 0;
        // field
        assertThat(lines.get(i++), is("    private Date dateProperty;"));


        i = 2;
        // getter
        assertThat(lines.get(i++), is("    @JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"this is a date property\", format = \"yyyy-MM-dd HH:mm:ss\")"));
        assertThat(lines.get(i++), is("    public Date getDateProperty() {"));
        assertThat(lines.get(i++), is("        return dateProperty;"));
        assertThat(lines.get(i++), is("    }"));

        i = 8;
        // setter
        assertThat(lines.get(i++), is("    public void setDateProperty(Date dateProperty) {"));
        assertThat(lines.get(i++), is("        this.dateProperty = dateProperty;"));
        assertThat(lines.get(i++), is("    }"));
    }


    @Test
    public void testProperty_string() throws Exception {
        List<String> lines = generator.buildProperty(Columns.stringColumn());

        int i = 0;
        // field
        assertThat(lines.get(i++), is("    private String column1;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"column comment\")"));
        assertThat(lines.get(i++), is("    public String getColumn1() {"));
        assertThat(lines.get(i++), is("        return column1;"));
        assertThat(lines.get(i++), is("    }"));

        i = 7;
        // setter
        assertThat(lines.get(i++), is("    public void setColumn1(String column1) {"));
        assertThat(lines.get(i++), is("        this.column1 = column1;"));
        assertThat(lines.get(i++), is("    }"));
    }

    @Test
    public void testProperty_options() throws Exception {
        Column column = Columns.stringColumn();
        column.getOptions().put("Y", "foo");
        column.getOptions().put("N", "bar");

        List<String> lines = generator.buildProperty(column);

        int i = 0;
        // field
        assertThat(lines.get(i++), is("    private String column1;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("    @Option(value = {\"Y\", \"N\"})"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"column comment\", format = \"Y=foo/N=bar\")"));
        assertThat(lines.get(i++), is("    public String getColumn1() {"));
        assertThat(lines.get(i++), is("        return column1;"));
        assertThat(lines.get(i++), is("    }"));

        i = 8;
        // setter
        assertThat(lines.get(i++), is("    public void setColumn1(String column1) {"));
        assertThat(lines.get(i++), is("        this.column1 = column1;"));
        assertThat(lines.get(i++), is("    }"));
    }


    @Test
    public void testProperty_with_default_value() throws Exception {
        Column column = Columns.stringColumn();
        column.setComment("column comment(default=foo)");
        List<String> lines = generator.buildProperty(column);

        int i = 0;
        // field
        assertThat(lines.get(i++), is("    private String column1 = \"foo\";"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"column comment\", defaultValue = \"foo\")"));
        assertThat(lines.get(i++), is("    public String getColumn1() {"));
        assertThat(lines.get(i++), is("        return column1;"));
        assertThat(lines.get(i++), is("    }"));

        i = 7;
        // setter
        assertThat(lines.get(i++), is("    public void setColumn1(String column1) {"));
        assertThat(lines.get(i++), is("        this.column1 = column1;"));
        assertThat(lines.get(i++), is("    }"));
    }

    @Test
    public void testProperty_ManyToOne() throws Exception {
        Column column = Columns.stringColumn();
        column.setNullable(true);
        column.setReferenceTable("OTHER_TABLE");
        List<String> lines = generator.buildProperty(column);

        int i = 0;
        // field
        assertThat(lines.get(i++), is("    private String otherTableUuid;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"column comment\")"));
        assertThat(lines.get(i++), is("    public String getOtherTableUuid() {"));
        assertThat(lines.get(i++), is("        return otherTableUuid;"));
        assertThat(lines.get(i++), is("    }"));

        i = 7;
        // setter
        assertThat(lines.get(i++), is("    public void setOtherTableUuid(String otherTableUuid) {"));
        assertThat(lines.get(i++), is("        this.otherTableUuid = otherTableUuid;"));
        assertThat(lines.get(i++), is("    }"));
    }


    @Test
    public void testProperty_boolean() throws Exception {
        List<String> lines = generator.buildProperty(Columns.booleanColumn());

        int i = 0;
        // field
        assertThat(lines.get(i++), is("    private Boolean boolProperty;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"this is a boolean property\")"));
        assertThat(lines.get(i++), is("    public Boolean getBoolProperty() {"));
        assertThat(lines.get(i++), is("        return boolProperty;"));
        assertThat(lines.get(i++), is("    }"));

        i = 7;
        // setter
        assertThat(lines.get(i++), is("    public void setBoolProperty(Boolean boolProperty) {"));
        assertThat(lines.get(i++), is("        this.boolProperty = boolProperty;"));
        assertThat(lines.get(i++), is("    }"));
    }


    @Test
    public void testExportTable() throws Exception {
        List<String> lines = generator.exportTable(Tables.table1());

        assertThat(lines, hasItem("import com.kentchiu.spring.attribute.AttributeInfo;"));
        assertThat(lines, hasItem("import com.kentchiu.spring.base.domain.Option;"));
        assertThat(lines, hasItem("import org.hibernate.validator.constraints.*;"));
        assertThat(lines, hasItem("import javax.validation.constraints.*;"));
        assertThat(lines, hasItem("import java.util.Date;"));
        assertThat(lines, hasItem("import java.math.BigDecimal;"));
        assertThat(lines, hasItem("import com.fasterxml.jackson.annotation.JsonFormat;"));

        int i = 11;
        assertThat(lines.get(i++), is("/**"));
        assertThat(lines.get(i++), is(" * a table comment"));
        assertThat(lines.get(i++), is(" */"));
        assertThat(lines.get(i++), is("public class MyTable1Input {"));

        String content = Joiner.on('\n').join(lines);

        assertThat(content, containsString("    private String myColumn11;"));
        assertThat(content, containsString("    private String myColumn12;"));
        assertThat(content, containsString("    private Date myColumn13;"));
        assertThat(content, containsString("    @NotNull"));
    }


    @Test
    public void testAttributeInfo_with_options_and_default_value() throws Exception {
        Column column = new Column();
        column.setDescription("是否允许定制颜色");
        column.setDefaultValue("'Y'");
        Map<String, String> options = column.getOptions();
        options.put("Y", "允许");
        options.put("N", "不允许");

        assertThat(generator.buildAttributeInfo(column), is("@AttributeInfo(description = \"是否允许定制颜色\", format = \"Y=允许/N=不允许\", defaultValue = \"'Y'\")"));
    }
}
