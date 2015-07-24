package com.kentchiu.jpa;

import com.google.common.collect.ImmutableMap;
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

}