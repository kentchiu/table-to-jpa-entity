package com.kentchiu.jpa.generator;

import com.google.common.collect.ImmutableMap;
import com.kentchiu.jpa.domain.Tables;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PartialGeneratorTest {

    PartialGenerator generator;

    @Before
    public void setUp() throws Exception {
        generator = new PartialGenerator(new Transformer());
        generator.setProjectHome(Files.createTempDirectory("java"));
        generator.transformer.setTableNameMapper(ImmutableMap.of("MY_TABLE_1", "com.foobar.module.domain.FooBar"));
    }


    @Test
    public void testGenerate() throws Exception {
        generator.export(Tables.table1());
        assertThat(Files.exists(generator.getJavaSourceHome().resolve("com/foobar/module/FooBarPartial.md")), is(true));
    }


    @Test
    public void testExportTable() throws Exception {
        List<String> lines = generator.exportTable(Tables.table1());
        lines.stream().forEach(System.out::println);
        //AbstractGenerator.dump(lines));

        int i = 0;
        assertThat(lines.get(i++), is("## FooBar.java"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("```java"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    private String myColumn11;"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    @Column(name = \"MY_COLUMN_11\")"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"my column 1-1 comment\")"));
        assertThat(lines.get(i++), is("    public String getMyColumn11() {"));
        assertThat(lines.get(i++), is("        return myColumn11;"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    public void setMyColumn11(String myColumn11) {"));
        assertThat(lines.get(i++), is("        this.myColumn11 = myColumn11;"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    private String myColumn12;"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    @NotBlank"));
        assertThat(lines.get(i++), is("    @Column(name = \"MY_COLUMN_12\")"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"my column 1-2 comment\")"));
        assertThat(lines.get(i++), is("    public String getMyColumn12() {"));
        assertThat(lines.get(i++), is("        return myColumn12;"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    public void setMyColumn12(String myColumn12) {"));
        assertThat(lines.get(i++), is("        this.myColumn12 = myColumn12;"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    private Date myColumn13;"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    @NotNull"));
        assertThat(lines.get(i++), is("    @Column(name = \"MY_COLUMN_13\")"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"my column 1-3 comment\")"));
        assertThat(lines.get(i++), is("    public Date getMyColumn13() {"));
        assertThat(lines.get(i++), is("        return myColumn13;"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    public void setMyColumn13(Date myColumn13) {"));
        assertThat(lines.get(i++), is("        this.myColumn13 = myColumn13;"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("```"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("##  FooBarInput.java"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("```java"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    private String myColumn11;"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"my column 1-1 comment\")"));
        assertThat(lines.get(i++), is("    public String getMyColumn11() {"));
        assertThat(lines.get(i++), is("        return myColumn11;"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    public void setMyColumn11(String myColumn11) {"));
        assertThat(lines.get(i++), is("        this.myColumn11 = myColumn11;"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    private String myColumn12;"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    @NotBlank"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"my column 1-2 comment\")"));
        assertThat(lines.get(i++), is("    public String getMyColumn12() {"));
        assertThat(lines.get(i++), is("        return myColumn12;"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    public void setMyColumn12(String myColumn12) {"));
        assertThat(lines.get(i++), is("        this.myColumn12 = myColumn12;"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    private Date myColumn13;"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    @NotNull"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"my column 1-3 comment\")"));
        assertThat(lines.get(i++), is("    public Date getMyColumn13() {"));
        assertThat(lines.get(i++), is("        return myColumn13;"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    public void setMyColumn13(Date myColumn13) {"));
        assertThat(lines.get(i++), is("        this.myColumn13 = myColumn13;"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("```"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("##  FooBarUpdateInput.java"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("```java"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//    private String myColumn11;"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//    @AttributeInfo(description = \"my column 1-1 comment\")"));
        assertThat(lines.get(i++), is("//    public String getMyColumn11() {"));
        assertThat(lines.get(i++), is("//        return myColumn11;"));
        assertThat(lines.get(i++), is("//    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//    public void setMyColumn11(String myColumn11) {"));
        assertThat(lines.get(i++), is("//        this.myColumn11 = myColumn11;"));
        assertThat(lines.get(i++), is("//    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//    private String myColumn12;"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//    @AttributeInfo(description = \"my column 1-2 comment\")"));
        assertThat(lines.get(i++), is("//    public String getMyColumn12() {"));
        assertThat(lines.get(i++), is("//        return myColumn12;"));
        assertThat(lines.get(i++), is("//    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//    public void setMyColumn12(String myColumn12) {"));
        assertThat(lines.get(i++), is("//        this.myColumn12 = myColumn12;"));
        assertThat(lines.get(i++), is("//    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//    private Date myColumn13;"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//    @AttributeInfo(description = \"my column 1-3 comment\")"));
        assertThat(lines.get(i++), is("//    public Date getMyColumn13() {"));
        assertThat(lines.get(i++), is("//        return myColumn13;"));
        assertThat(lines.get(i++), is("//    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//    public void setMyColumn13(Date myColumn13) {"));
        assertThat(lines.get(i++), is("//        this.myColumn13 = myColumn13;"));
        assertThat(lines.get(i++), is("//    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("```"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("##  FooBarQuery.java"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("```java"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    private String myColumn11;"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"my column 1-1 comment\")"));
        assertThat(lines.get(i++), is("    public String getMyColumn11() {"));
        assertThat(lines.get(i++), is("        return myColumn11;"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    public void setMyColumn11(String myColumn11) {"));
        assertThat(lines.get(i++), is("        this.myColumn11 = myColumn11;"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    private String myColumn12;"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    @NotBlank"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"my column 1-2 comment\")"));
        assertThat(lines.get(i++), is("    public String getMyColumn12() {"));
        assertThat(lines.get(i++), is("        return myColumn12;"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    public void setMyColumn12(String myColumn12) {"));
        assertThat(lines.get(i++), is("        this.myColumn12 = myColumn12;"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    private Date myColumn13;"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    @NotNull"));
        assertThat(lines.get(i++), is("    @AttributeInfo(description = \"my column 1-3 comment\")"));
        assertThat(lines.get(i++), is("    public Date getMyColumn13() {"));
        assertThat(lines.get(i++), is("        return myColumn13;"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    public void setMyColumn13(Date myColumn13) {"));
        assertThat(lines.get(i++), is("        this.myColumn13 = myColumn13;"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("```"));


    }
}