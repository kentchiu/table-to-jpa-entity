package com.kentchiu.jpa.domain;


import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

public class ColumnCommentTest {


    @Test
    public void testEmpty() throws Exception {
        Column c = new Column();
        c.parser("");
        assertThat(c.getDescription(), is(""));
        assertThat(c.getDefaultValue(), is(""));
        assertThat(c.getOptions().isEmpty(), is(true));
    }


    @Test
    public void testNull() throws Exception {
        Column c = new Column();
        c.parser(null);
        assertThat(c.getDescription(), is(""));
        assertThat(c.getDefaultValue(), is(""));
        assertThat(c.getOptions().isEmpty(), is(true));
    }

    @Test
    public void testSimpleComment() throws Exception {
        Column c = new Column();
        c.parser("this is a comment");
        assertThat(c.getDescription(), is("this is a comment"));
        assertThat(c.getDefaultValue(), is(""));
        assertThat(c.getOptions().isEmpty(), is(true));
    }

    @Test
    public void testDefaultValue() throws Exception {
        Column c = new Column();
        c.parser("this is a comment(default='op2')");
        assertThat(c.getDescription(), is("this is a comment"));
        assertThat(c.getDefaultValue(), is("op2"));
        assertThat(c.getOptions().size(), is(0));
    }

    @Test
    public void testDefaultValue2() throws Exception {
        Column c = new Column();
        c.parser("this is a comment(default=op2)");
        assertThat(c.getDescription(), is("this is a comment"));
        assertThat(c.getDefaultValue(), is("op2"));
        assertThat(c.getOptions().size(), is(0));
    }

    @Test
    public void testOptions() throws Exception {
        Column c = new Column();
        c.parser("this is a comment(op1=foo/op2=bar/op3=foobar/op4=zzz)");
        assertThat(c.getDescription(), is("this is a comment"));
        assertThat(c.getDefaultValue(), is(""));
        assertThat(c.getOptions().size(), is(4));
        assertThat(c.getOptions(), hasEntry("op1", "foo"));
        assertThat(c.getOptions(), hasEntry("op2", "bar"));
        assertThat(c.getOptions(), hasEntry("op3", "foobar"));
        assertThat(c.getOptions(), hasEntry("op4", "zzz"));
    }

    @Test
    public void testOptionsAndDefaultValue() throws Exception {
        Column c = new Column();
        c.parser("this is a comment(op1=foo/op2=bar/op3=foobar)(default='op2')");
        assertThat(c.getDescription(), is("this is a comment"));
        assertThat(c.getDefaultValue(), is("op2"));
        assertThat(c.getOptions().size(), is(3));
        assertThat(c.getOptions(), hasEntry("op1", "foo"));
        assertThat(c.getOptions(), hasEntry("op2", "bar"));
        assertThat(c.getOptions(), hasEntry("op3", "foobar"));
    }

    @Test
    public void testOptionsAndDefaultValue_colon() throws Exception {
        Column c = new Column();
        c.parser("this is a comment(op1:foo/op2:bar/op3:foobar)(default='op2')");
        assertThat(c.getDescription(), is("this is a comment"));
        assertThat(c.getDefaultValue(), is("op2"));
        assertThat(c.getOptions().size(), is(3));
        assertThat(c.getOptions(), hasEntry("op1", "foo"));
        assertThat(c.getOptions(), hasEntry("op2", "bar"));
        assertThat(c.getOptions(), hasEntry("op3", "foobar"));
    }

    @Test
    public void testOptionsAndDefaultValue_with_space() throws Exception {
        Column c = new Column();
        c.parser("this is a comment( op1 = foo / op2 = bar / op3 = foobar )( default  =  'op2' )");
        assertThat(c.getDescription(), is("this is a comment"));
        assertThat(c.getDefaultValue(), is("op2"));
        assertThat(c.getOptions().size(), is(3));
        assertThat(c.getOptions(), hasEntry("op1", "foo"));
        assertThat(c.getOptions(), hasEntry("op2", "bar"));
        assertThat(c.getOptions(), hasEntry("op3", "foobar"));
    }
}