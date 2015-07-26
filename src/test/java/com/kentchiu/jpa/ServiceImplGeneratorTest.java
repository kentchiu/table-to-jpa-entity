package com.kentchiu.jpa;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kentchiu.jpa.domain.Table;
import com.kentchiu.jpa.domain.Tables;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class ServiceImplGeneratorTest {

    private ServiceImplGenerator generator;

    @Before
    public void setUp() throws Exception {
        generator = new ServiceImplGenerator(new Transformer());
    }


    @Test
    public void testExport() throws Exception {
        Table table = Tables.table1();
        generator.transformer.setTableNameMapper(ImmutableMap.of(table.getName(), "com.kentchiu.module.domain.FooBar"));
        Optional<Path> export = generator.exportToFile(table, ImmutableList.of());
        assertThat(export.isPresent(), Is.is(true));
        assertThat(export.get().toString(), containsString("/src/main/java/com/kentchiu/module/service/FooBarServiceImpl.java"));
    }


    @Test
    public void testApplyTemplate() throws Exception {
        Table table = Tables.table1();
        generator.transformer.setTableNameMapper(ImmutableMap.of(table.getName(), "com.kentchiu.module.domain.FooBar"));
        List<String> list = generator.applyTemplate(table);
        int i = 0;
        assertThat(list.get(i++), is("package com.kentchiu.module.service;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.FooBar;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.dao.FooBarRepository;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.FooBarService;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import org.springframework.beans.factory.annotation.Autowired;"));
        assertThat(list.get(i++), is("import org.springframework.data.jpa.repository.JpaRepository;"));
        assertThat(list.get(i++), is("import org.springframework.stereotype.Service;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import javax.persistence.EntityManager;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("@Service"));
        assertThat(list.get(i++), is("public class FooBarServiceImpl implements FooBarService {"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    private FooBarRepository repository;"));
        assertThat(list.get(i++), is("    private EntityManager entityManager;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Override"));
        assertThat(list.get(i++), is("    public JpaRepository<FooBar, String> getRepository() {"));
        assertThat(list.get(i++), is("        return repository;"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Autowired"));
        assertThat(list.get(i++), is("    public void setRepository(FooBarRepository repository) {"));
        assertThat(list.get(i++), is("        this.repository = repository;"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Override"));
        assertThat(list.get(i++), is("    public EntityManager getEntityManager() {"));
        assertThat(list.get(i++), is("        return entityManager;"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Autowired"));
        assertThat(list.get(i++), is("    public void setEntityManager(EntityManager entityManager) {"));
        assertThat(list.get(i++), is("        this.entityManager = entityManager;"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is("}"));

    }


}