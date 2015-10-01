package com.kentchiu.jpa.generator;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.kentchiu.jpa.domain.Column;
import com.kentchiu.jpa.domain.Columns;
import com.kentchiu.jpa.domain.Table;
import com.kentchiu.jpa.domain.Tables;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EntityGeneratorTest extends DomainObjectGeneratorTest {


    @Before
    public void setUp() throws Exception {
        Transformer transformer = new Transformer();
        transformer.setTableNameMapper(ImmutableMap.of(Tables.table1().getName(), "com.kentchiu.jpa.domain.FooBar"));
        generator = new EntityGenerator(transformer, new Config(Type.JPA));
        generator.setProjectHome(Files.createTempDirectory("java"));
    }

    @Test
    public void testGenerate() throws Exception {
        generator.export(Tables.table1());
        assertThat(Files.exists(generator.getJavaSourceHome().resolve("com/kentchiu/jpa/domain/FooBar.java")), is(true));
    }


    @Test
    public void testTableMapping() throws Exception {
        //generator.setTableNameMapper(ImmutableMap.of("MY_TABLE_1", "com.kentchiu.jpa.domain.FooBar"));
        List<String> lines = generator.exportTable(Tables.table1());
        AbstractGenerator.dump(lines);
        assertThat(lines, hasItem("package com.kentchiu.jpa.domain;"));
        assertThat(lines, hasItem("/*"));
        assertThat(lines, hasItem(" * a table comment"));
        assertThat(lines, hasItem(" */"));
        assertThat(lines, hasItem("public class FooBar {"));
    }

    @Test
    public void testDomainClass() throws Exception {
        Table table = Tables.table1();
        List<String> strings = generator.exportTable(table);
        assertThat(strings, hasItem("@Entity"));
        assertThat(strings, hasItem("@Table(name = \"MY_TABLE_1\")"));
        assertThat(strings, hasItem("public class FooBar {"));
    }


    @Test
    public void testProperty_BigDecimal() throws Exception {
        List<String> lines = generator.buildProperty(Columns.bigDecimalColumn());
        AbstractGenerator.dump(lines);

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
    public void testProperty_options() throws Exception {
        Column column = Columns.stringColumn();
        column.getOptions().put("Y", "foo");
        column.getOptions().put("N", "bar");

        List<String> lines = generator.buildProperty(column);
        AbstractGenerator.dump(lines);

        int i = 0;
        // field
        assertThat(lines.get(i++), is("    private String column1;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("    @Column(name = \"column1\")"));
        assertThat(lines.get(i++), is("    @Option(value = {\"Y\", \"N\"})"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"column comment\", format = \"Y=foo/N=bar\")"));
        assertThat(lines.get(i++), is("    public String getColumn1() {"));
        assertThat(lines.get(i++), is("        return column1;"));
        assertThat(lines.get(i++), is("    }"));

        i = 9;
        // setter
        assertThat(lines.get(i++), is("    public void setColumn1(String column1) {"));
        assertThat(lines.get(i++), is("        this.column1 = column1;"));
        assertThat(lines.get(i++), is("    }"));
    }


    @Test
    public void testProperty_string() throws Exception {
        List<String> lines = generator.buildProperty(Columns.stringColumn());
        AbstractGenerator.dump(lines);

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
        List<String> lines = generator.buildProperty(Columns.booleanColumn());
        AbstractGenerator.dump(lines);

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
    public void testProperty_boolean_with_default_value() throws Exception {
        Column column = Columns.booleanColumn();
        column.setDefaultValue("true");
        List<String> lines = generator.buildProperty(column);
        AbstractGenerator.dump(lines);

        int i = 0;
        // field
        assertThat(lines.get(i++), is("    private Boolean boolProperty = true;"));
    }

    @Test
    public void testProperty_Date() throws Exception {
        List<String> lines = generator.buildProperty(Columns.dateColumn());
        AbstractGenerator.dump(lines);

        int i = 0;
        // field
        assertThat(lines.get(i++), is("    private Date dateProperty;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("    @Column(name = \"DATE_PROPERTY\")"));
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
    public void testProperty_string_not_null() throws Exception {
        List<String> lines = generator.buildProperty(Columns.createStringColumn("FOO_BAR", "The foo bar comment", false));
        AbstractGenerator.dump(lines);

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
        List<String> lines = generator.buildProperty(column);
        AbstractGenerator.dump(lines);

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
        List<String> lines = generator.buildProperty(column);
        AbstractGenerator.dump(lines);

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
        Column column = Columns.stringColumn();
        column.setNullable(true);
        column.setReferenceTable("OTHER_TABLE");
        generator.getTransformer().setColumnMapper(ImmutableMap.of("OTHER_TABLE", "xxx"));
        List<String> lines = generator.buildProperty(column);

        AbstractGenerator.dump(lines);
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
    public void testProperty_ManyToOne_Not_Null() throws Exception {
        Column column = Columns.stringColumn();
        column.setNullable(false);
        column.setReferenceTable("OTHER_TABLE");
        generator.transformer.setColumnMapper(ImmutableMap.of("OTHER_TABLE", "xxx"));
        List<String> lines = generator.buildProperty(column);

        AbstractGenerator.dump(lines);
        int i = 0;
        // field
        assertThat(lines, not(hasItem("    @NotNull")));
        assertThat(lines, not(hasItem("    @NotBlank")));
    }


    @Test
    public void testColumnMapping() throws Exception {
        Map<String, String> mapper = new HashMap<>();
        mapper.put("MY_TABLE", "com.kentchiu.jpa.FooBar");
        generator.transformer.setTableNameMapper(mapper);

        Column column = Columns.createStringColumn("prop", "comment", true);
        column.setReferenceTable("MY_TABLE");
        List<String> lines = generator.buildProperty(column);

        AbstractGenerator.dump(lines);

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


    @Test
    public void testExportTable() throws Exception {
        Table table = Tables.table1();
        Column column = new Column();
        column.setName("type");
        column.setDescription("sexual");
        Map<String, String> options = column.getOptions();
        options.put("M", "MALE");
        options.put("F", "FEMALE");

        table.getColumns().add(column);
        List<String> lines = generator.exportTable(table);

        AbstractGenerator.dump(lines);

        assertThat(lines, hasItem("import com.kentchiu.spring.attribute.AttributeInfo;"));
        assertThat(lines, hasItem("import com.kentchiu.spring.base.domain.Option;"));
        assertThat(lines, hasItem("import org.hibernate.validator.constraints.*;"));
        assertThat(lines, hasItem("import org.hibernate.annotations.GenericGenerator;"));
        assertThat(lines, hasItem("import org.hibernate.annotations.NotFound;"));
        assertThat(lines, hasItem("import org.hibernate.annotations.NotFoundAction;"));
        assertThat(lines, hasItem("import javax.persistence.*;"));
        assertThat(lines, hasItem("import javax.validation.constraints.*;"));
        assertThat(lines, hasItem("import java.util.Date;"));
        assertThat(lines, hasItem("import java.math.BigDecimal;"));

        int i = 15;
        assertThat(lines.get(i++), is("/*"));
        assertThat(lines.get(i++), is(" * a table comment"));
        assertThat(lines.get(i++), is(" */"));
        assertThat(lines.get(i++), is("@Entity"));
        assertThat(lines.get(i++), is("@Table(name = \"MY_TABLE_1\")"));
        assertThat(lines.get(i++), is("public class FooBar {"));

        String content = Joiner.on('\n').join(lines);


        assertThat(content, containsString("    /**"));
        assertThat(content, containsString("     * sexual : MALE"));
        assertThat(content, containsString("     */"));
        assertThat(content, containsString("    public static final String TYPE_M = \"M\";"));

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

    @Test
    public void testEnum_form_options() throws Exception {
        Column column = new Column();
        column.setName("ITEM_TYPE");
        column.setDescription("产品类型");
        Map<String, String> options = column.getOptions();
        options.put("1", "套件");
        options.put("2", "包件");
        options.put("3", "补件");
        options.put("4", "其他");

        List<EntityGenerator.FieldEnum> enums = generator.buildEnum(column);
        assertThat(enums, hasSize(4));
        assertThat(enums.get(0).getName(), is("ITEM_TYPE_1"));
        assertThat(enums.get(0).getValue(), is("\"1\""));
        assertThat(enums.get(0).getDescription(), is("产品类型 : 套件"));

        assertThat(enums.get(1).getName(), is("ITEM_TYPE_2"));
        assertThat(enums.get(1).getValue(), is("\"2\""));
        assertThat(enums.get(1).getDescription(), is("产品类型 : 包件"));

        assertThat(enums.get(2).getName(), is("ITEM_TYPE_3"));
        assertThat(enums.get(2).getValue(), is("\"3\""));
        assertThat(enums.get(2).getDescription(), is("产品类型 : 补件"));

        assertThat(enums.get(3).getName(), is("ITEM_TYPE_4"));
        assertThat(enums.get(3).getValue(), is("\"4\""));
        assertThat(enums.get(3).getDescription(), is("产品类型 : 其他"));
    }
}
