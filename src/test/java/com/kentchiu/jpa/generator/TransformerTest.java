package com.kentchiu.jpa.generator;

import com.google.common.collect.ImmutableMap;
import com.kentchiu.jpa.domain.Column;
import com.kentchiu.jpa.domain.Columns;
import com.kentchiu.jpa.domain.Tables;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TransformerTest {

    private Transformer transformer = new Transformer();

    @Test
    public void testTopPackage() throws Exception {
        transformer.setTableNameMapper(ImmutableMap.of(Tables.table1().getName(), "com.kentchiu.jpa.module.domain.MyTable1"));
        assertThat(transformer.getTopPackage(Tables.table1().getName()), is("com.kentchiu.jpa"));
    }

    @Test
    public void testGetModuleName() throws Exception {
        transformer.setTableNameMapper(ImmutableMap.of(Tables.table1().getName(), "com.kentchiu.jpa.module.domain.MyTable1"));
        assertThat(transformer.getModuleName(Tables.table1().getName()), is("module"));
    }


    @Test
    public void testProperty_substitute() throws Exception {
        Column column = Columns.createStringColumn("FOO_QTY_AND_AMT_PROP", "column comment", true);
        transformer.setColumnMapper(ImmutableMap.of("QTY", "QUALITY", "AMT", "AMOUNT"));
        Transformer.Property property = transformer.getProperty(column, Type.JPA);
        assertThat(property.getPropertyName(), is("fooQualityAndAmountProp"));
    }

    @Test
    public void testProperty_substitute_2() throws Exception {
        Column column = Columns.createStringColumn("QTY_AND_AMT", "column comment", true);
        transformer.setColumnMapper(ImmutableMap.of("QTY", "QUALITY", "AMT", "AMOUNT"));
        Transformer.Property property = transformer.getProperty(column, Type.JPA);
        assertThat(property.getPropertyName(), is("qualityAndAmount"));
    }

    @Test
    public void testProperty_substitute_not_an_abbreviate() throws Exception {
        Column column = Columns.createStringColumn("FOO_QUALITY_AND_AMTT_PROP", "column comment", true);
        transformer.setColumnMapper(ImmutableMap.of("QTY", "QUALITY", "AMT", "AMOUNT"));
        Transformer.Property property = transformer.getProperty(column, Type.JPA);
        assertThat(property.getPropertyName(), is("fooQualityAndAmttProp"));
    }


    @Test
    public void testProperty_ManyToOne_name_conflict() throws Exception {
        Column column = Columns.stringColumn();
        column.setNullable(true);
        column.setReferenceTable("OTHER_TABLE");
        transformer.setColumnMapper(ImmutableMap.of("column1", "FOO_BAR"));
        Transformer.Property property = transformer.getProperty(column, Type.JPA);
        assertThat(property.getPropertyName(), is("fooBar"));
    }


}