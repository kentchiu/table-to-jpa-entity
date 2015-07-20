package com.kentchiu.jpa;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kentchiu.jpa.domain.Column;
import com.kentchiu.jpa.domain.Columns;
import com.kentchiu.jpa.domain.Table;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

public class EntityGeneratorTest extends AbstractGeneratorTest {


    @Before
    public void setUp() throws Exception {
        generator = new EntityGenerator(new Config(Type.JPA));
    }

    @Test
    public void testGenerate() throws Exception {
        generator.setTableNameMapper(ImmutableMap.of("MY_TABLE_1", "com.foobar.MyTest"));
        Path javaSourceHome = Files.createTempDirectory("java");
        generator.export(javaSourceHome, Tables.all(), ImmutableList.of());
        assertThat(Files.exists(javaSourceHome.resolve("com/foobar/MyTest.java")), is(true));
    }


    @Test
    public void testTableMapping() throws Exception {
        generator.setTableNameMapper(ImmutableMap.of("MY_TABLE_1", "com.kentchiu.jpa.FooBar"));
        List<String> lines = generator.exportTable(Tables.table1());
        dump(lines);
        assertThat(lines, hasItem("package com.kentchiu.jpa;"));
        assertThat(lines, hasItem("/*"));
        assertThat(lines, hasItem(" * a table comment"));
        assertThat(lines, hasItem(" */"));
        assertThat(lines, hasItem("public class FooBar extends Object {"));
    }

    @Test
    public void testDomainClass() throws Exception {
        Table table = Tables.table1();
        assertThat(generator.exportTable(table), hasItem("@Entity"));
        assertThat(generator.exportTable(table), hasItem("@Table(name = \"MY_TABLE_1\")"));
        assertThat(generator.exportTable(table), hasItem("public class MyTable1 extends Object {"));
    }


    @Test
    public void testProperty_BigDecimal() throws Exception {
        List<String> lines = generator.buildProperty(Tables.table1(), Columns.bigDecimalColumn());
        dump(lines);

        int i = 0;
        // field
        assertThat(lines.get(i++), is("    private BigDecimal bigDecimalProperty = BigDecimal.ZERO;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("    @Column(name = \"BIG_DECIMAL_PROPERTY\")"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"this is a big decimal property\", defaultValue = \"0\")"));
        assertThat(lines.get(i++), is("    public BigDecimal getBigDecimalProperty() {"));
        assertThat(lines.get(i++), is("        return bigDecimalProperty;"));
        assertThat(lines.get(i++), is("    }"));

        i = 8;
        // setter
        assertThat(lines.get(i++), is("    public void setBigDecimalProperty(BigDecimal bigDecimalProperty) {"));
        assertThat(lines.get(i++), is("        this.bigDecimalProperty = bigDecimalProperty;"));
        assertThat(lines.get(i++), is("    }"));
    }



    @Test
    public void testProperty_string() throws Exception {
        List<String> lines = generator.buildProperty(Tables.table1(), Columns.stringColumn());
        dump(lines);

        int i = 0;
        // field
        assertThat(lines.get(i++), is("    private String column1;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("    @Column(name = \"column1\")"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"column comment\")"));
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
    public void testProperty_boolean() throws Exception {
        List<String> lines = generator.buildProperty(Tables.table1(), Columns.booleanColumn());
        dump(lines);

        int i = 0;
        // field
        assertThat(lines.get(i++), is("    private Boolean boolProperty;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("    @Column(name = \"BOOL_PROPERTY\")"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"this is a boolean property\")"));
        assertThat(lines.get(i++), is("    public Boolean getBoolProperty() {"));
        assertThat(lines.get(i++), is("        return boolProperty;"));
        assertThat(lines.get(i++), is("    }"));

        i = 8;
        // setter
        assertThat(lines.get(i++), is("    public void setBoolProperty(Boolean boolProperty) {"));
        assertThat(lines.get(i++), is("        this.boolProperty = boolProperty;"));
        assertThat(lines.get(i++), is("    }"));
    }


    @Test
    public void testProperty_string_not_null() throws Exception {
        List<String> lines = generator.buildProperty(Tables.table1(), Columns.createStringColumn("FOO_BAR", "The foo bar comment", false));
        dump(lines);

        // field
        assertThat(lines.get(0), is("    private String fooBar;"));

        // getter
        int i = 2;
        assertThat(lines.get(i++), is("    @NotBlank"));
        assertThat(lines.get(i++), is("    @Column(name = \"FOO_BAR\")"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"The foo bar comment\")"));
        assertThat(lines.get(i++), is("    public String getFooBar() {"));
        assertThat(lines.get(i++), is("        return fooBar;"));
        assertThat(lines.get(i++), is("    }"));

        // setter
        i = 9;
        assertThat(lines.get(i++), is("    public void setFooBar(String fooBar) {"));
        assertThat(lines.get(i++), is("        this.fooBar = fooBar;"));
        assertThat(lines.get(i++), is("    }"));
    }

    @Test
    public void testProperty_with_default_value() throws Exception {
        Column column = Columns.stringColumn();
        column.setDefaultValue("foo");
        List<String> lines = generator.buildProperty(Tables.table1(), column);
        dump(lines);

        int i = 0;
        // field
        assertThat(lines.get(i++), is("    private String column1 = \"foo\";"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("    @Column(name = \"column1\")"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"column comment\", defaultValue = \"foo\")"));
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
    public void testProperty_unique() throws Exception {

        Column column = Columns.stringColumn();
        column.setDefaultValue("foo");
        column.setUnique(true);
        List<String> lines = generator.buildProperty(Tables.table1(), column);
        dump(lines);

        int i = 0;
        // field
        assertThat(lines.get(i++), is("    private String column1 = \"foo\";"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("    @Column(name = \"column1\", unique = true)"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"column comment\", defaultValue = \"foo\")"));
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
    public void testProperty_ManyToOne() throws Exception {
        Table table = Tables.table1();

        Column column = Columns.stringColumn();
        column.setNullable(true);
        column.setReferenceTable("OTHER_TABLE");
        generator.getColumnMapper().put("OTHER_TABLE", "xxx");
        List<String> lines = generator.buildProperty(table, column);

        dump(lines);
        int i = 0;
        // field
        assertThat(lines.get(i++), is("    private OtherTable otherTable;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("    @ManyToOne"));
        assertThat(lines.get(i++), is("    @NotFound(action = NotFoundAction.IGNORE)"));
        assertThat(lines.get(i++), is("    @JoinColumn(name = \"column1\")"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"column comment\")"));
        assertThat(lines.get(i++), is("    public OtherTable getOtherTable() {"));
        assertThat(lines.get(i++), is("        return otherTable;"));
        assertThat(lines.get(i++), is("    }"));

        i = 10;
        // setter
        assertThat(lines.get(i++), is("    public void setOtherTable(OtherTable otherTable) {"));
        assertThat(lines.get(i++), is("        this.otherTable = otherTable;"));
        assertThat(lines.get(i++), is("    }"));
    }


    @Test
    public void testColumnMapping() throws Exception {
        Map<String, String> mapper = new HashMap<>();
        mapper.put("MY_TABLE", "com.kentchiu.jpa.FooBar");
        generator.setTableNameMapper(mapper);

        Column column = Columns.createStringColumn("prop", "comment", true);
        column.setReferenceTable("MY_TABLE");
        List<String> lines = generator.buildProperty(Tables.table1(), column);

        dump(lines);

        int i = 0;
        assertThat(lines.get(i++), is("    private FooBar fooBar;"));

        i = 2;
        assertThat(lines.get(i++), is("    @ManyToOne"));
        assertThat(lines.get(i++), is("    @NotFound(action = NotFoundAction.IGNORE)"));
        assertThat(lines.get(i++), is("    @JoinColumn(name = \"prop\")"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"comment\")"));
        assertThat(lines.get(i++), is("    public FooBar getFooBar() {"));
        assertThat(lines.get(i++), is("        return fooBar;"));
        assertThat(lines.get(i++), is("    }"));

        i = 10;
        assertThat(lines.get(i++), is("    public void setFooBar(FooBar fooBar) {"));
        assertThat(lines.get(i++), is("        this.fooBar = fooBar;"));
        assertThat(lines.get(i++), is("    }"));
    }


//    @Test
//    public void testExportTable() throws Exception {
//        List<String> lines = generator.exportTable(Tables.table1());
//        int i = 0;
//
//        dump(lines);
//
//        assertThat(lines,hasItem("import com.kentchiu.spring.attribute.AttributeInfo;"));
//        assertThat(lines,hasItem("import com.kentchiu.spring.base.domain.Option;"));
//        assertThat(lines,hasItem("import org.hibernate.validator.constraints.*;"));
//        assertThat(lines,hasItem("import org.hibernate.annotations.GenericGenerator;"));
//        assertThat(lines,hasItem("import org.hibernate.annotations.NotFound;"));
//        assertThat(lines,hasItem("import org.hibernate.annotations.NotFoundAction;"));
//        assertThat(lines,hasItem("import javax.persistence.*;"));
//        assertThat(lines,hasItem("import javax.validation.constraints.*;"));
//        assertThat(lines,hasItem("import java.util.Date;"));
//        assertThat(lines,hasItem("import java.math.BigDecimal;"));
//
//        i = 15;
//        assertThat(lines.get(i++), is("/*"));
//        assertThat(lines.get(i++), is(" * a table comment"));
//        assertThat(lines.get(i++), is(" */"));
//        assertThat(lines.get(i++), is("@Entity"));
//        assertThat(lines.get(i++), is("@Table(name = \"MY_TABLE_1\")"));
//        assertThat(lines.get(i++), is("public class MyTable1 {"));
//
//        String content = Joiner.on('\n').join(lines);
//
//        assertThat(content, containsString("    private String myColumn11;"));
//        assertThat(content, containsString("    private String myColumn12;"));
//        assertThat(content, containsString("    private Date myColumn13;"));
//        assertThat(content, containsString("    @NotNull"));
//        // PK
//        assertThat(content, containsString("    @Id"));
//        assertThat(content, containsString("@AttributeInfo(description = \"my column 1-3 comment\")"));
//    }
//


    @Test
    public void testProperty_substitute() throws Exception {
        Column column = Columns.createStringColumn("FOO_QTY_AND_AMT_PROP", "column comment", true);
        generator.setColumnMapper(ImmutableMap.of("QTY", "QUALITY", "AMT", "AMOUNT"));
        List<String> lines = generator.buildProperty(Tables.table1(), column);
        dump(lines);

        int i = 0;
        // field
        assertThat(lines.get(i++), is("    private String fooQualityAndAmountProp;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("    @Column(name = \"FOO_QTY_AND_AMT_PROP\")"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"column comment\")"));
        assertThat(lines.get(i++), is("    public String getFooQualityAndAmountProp() {"));
        assertThat(lines.get(i++), is("        return fooQualityAndAmountProp;"));
        assertThat(lines.get(i++), is("    }"));

        i = 8;
        // setter
        assertThat(lines.get(i++), is("    public void setFooQualityAndAmountProp(String fooQualityAndAmountProp) {"));
        assertThat(lines.get(i++), is("        this.fooQualityAndAmountProp = fooQualityAndAmountProp;"));
        assertThat(lines.get(i++), is("    }"));
    }


    @Test
    public void testProperty_ManyToOne_name_conflict() throws Exception {
        Table table = Tables.table1();

        Column column = Columns.stringColumn();
        column.setNullable(true);
        column.setReferenceTable("OTHER_TABLE");
        generator.getColumnMapper().put("column1", "FOO_BAR");
        List<String> lines = generator.buildProperty(table, column);

        dump(lines);
        int i = 0;
        // field
        assertThat(lines.get(i++), is("    private OtherTable fooBar;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("    @ManyToOne"));
        assertThat(lines.get(i++), is("    @NotFound(action = NotFoundAction.IGNORE)"));
        assertThat(lines.get(i++), is("    @JoinColumn(name = \"column1\")"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"column comment\")"));
        assertThat(lines.get(i++), is("    public OtherTable getFooBar() {"));
        assertThat(lines.get(i++), is("        return fooBar;"));
        assertThat(lines.get(i++), is("    }"));

        i = 10;
        // setter
        assertThat(lines.get(i++), is("    public void setFooBar(OtherTable fooBar) {"));
        assertThat(lines.get(i++), is("        this.fooBar = fooBar;"));
        assertThat(lines.get(i++), is("    }"));
    }


    @Test
    public void testAttributeInfo_with_options_and_default_value() throws Exception {
        Column column = new Column();
        column.setDescription("是否允许定制颜色");
        column.setDefaultValue("'Y'");
        Map<String, String> options = column.getOptions();
        options.put("Y", "允许");
        options.put("N", "不允许");

        assertThat(generator.attributeInfo(column), is("@AttributeInfo(description = \"是否允许定制颜色\", format = \"Y=允许/N=不允许\", defaultValue = \"'Y'\")"));
    }
}
