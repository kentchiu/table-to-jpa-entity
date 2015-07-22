package com.kentchiu.jpa;

import com.google.common.collect.ImmutableMap;
import com.kentchiu.jpa.domain.Tables;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TransformerTest {

    private Transformer transformer = new Transformer();

    @Test
    public void testPackage() throws Exception {
        transformer.setTableNameMapper(ImmutableMap.of(Tables.table1().getName(), "com.kentchiu.jpa.domain.MyTable1"));
        assertThat(transformer.getTopPackage(Tables.table1().getName()), is("com.kentchiu.jpa"));
//        assertThat(transformer.getPackage(Tables.table1().getName(), Type.INPUT), is("com.kentchiu.jpa.web.dto"));
//        assertThat(transformer.getPackage(Tables.table1().getName(), Type.UPDATE), is("com.kentchiu.jpa.web.dto"));
//        assertThat(transformer.getPackage(Tables.table1().getName(), Type.QUERY), is("com.kentchiu.jpa.service.query"));

    }


}