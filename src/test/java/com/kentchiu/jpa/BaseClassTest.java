package com.kentchiu.jpa;

import com.kentchiu.jpa.domain.Table;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

public class BaseClassTest extends AbstractGeneratorTest {

    @Before
    public void setUp() throws Exception {
        generator = new EntityGenerator(new Config(Type.JPA, Object.class));
    }


    @Test
    public void testDomainClass_base_class() throws Exception {
        Table table = Tables.table1();
        List<String> clazz = generator.buildClass(table);
        assertThat(clazz, hasItem("@Entity"));
        assertThat(clazz, hasItem("@Table(name = \"MY_TABLE_1\")"));
        assertThat(clazz, hasItem("public class MyTable1 extends Object {"));
    }

    @Test
    public void testImports_base_class() throws Exception {
        assertThat(generator.buildImports(), hasItem("import java.lang.Object;"));
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
}
