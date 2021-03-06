package com.kentchiu.jpa.generator;

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

public class ControllerTestGeneratorTest {

    private ControllerTestGenerator generator;
    private Table table;

    @Before
    public void setUp() throws Exception {
        table = Tables.table1();
        Transformer transformer = new Transformer();
        transformer.setTableNameMapper(ImmutableMap.of(table.getName(), "com.kentchiu.module.domain.FooBar"));
        generator = new ControllerTestGenerator(transformer);
        generator.getExtraParams().put("resourceName", "資源");
    }

    @Test
    public void testExport() throws Exception {
        Optional<Path> export = generator.exportToFile(table, ImmutableList.of());
        assertThat(export.isPresent(), Is.is(true));
        assertThat(export.get().toString(), containsString("/src/test/java/com/kentchiu/module/web/FooBarControllerTest.java"));
    }


    @Test
    public void testApplyTemplate() throws Exception {
        List<String> list = generator.applyTemplate(table);
        int i = 0;

        assertThat(list.get(i++), is("package com.kentchiu.module.web;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import com.kentchiu.base.web.AbstractControllerTest;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.web.TestConfig;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.FooBar;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.FooBars;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.FooBarService;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.query.FooBarQuery;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import com.google.common.collect.Maps;"));
        assertThat(list.get(i++), is("import com.kentchiu.spring.base.domain.DomainUtil;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import org.hamcrest.Matchers;"));
        assertThat(list.get(i++), is("import org.junit.Before;"));
        assertThat(list.get(i++), is("import org.junit.Ignore;"));
        assertThat(list.get(i++), is("import org.junit.Test;"));
        assertThat(list.get(i++), is("import org.mockito.Mockito;"));
        assertThat(list.get(i++), is("import org.springframework.beans.factory.annotation.Autowired;"));
        assertThat(list.get(i++), is("import org.springframework.http.HttpStatus;"));
        assertThat(list.get(i++), is("import org.springframework.http.MediaType;"));
        assertThat(list.get(i++), is("import org.springframework.test.context.ContextConfiguration;"));
        assertThat(list.get(i++), is("import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import java.util.Map;"));
        assertThat(list.get(i++), is("import java.util.Optional;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import static org.mockito.Matchers.any;"));
        assertThat(list.get(i++), is("import static org.mockito.Mockito.reset;"));
        assertThat(list.get(i++), is("import static org.mockito.Mockito.verify;"));
        assertThat(list.get(i++), is("import static org.mockito.Mockito.when;"));
        assertThat(list.get(i++), is("import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;"));
        assertThat(list.get(i++), is("import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;"));
        assertThat(list.get(i++), is("import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("@ResourceDoc(title = \"資源\", type = FooBar.class)"));
        assertThat(list.get(i++), is("@ContextConfiguration(classes = {TestConfig.class})"));
        assertThat(list.get(i++), is("public class FooBarControllerTest extends AbstractControllerTest {"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Autowired"));
        assertThat(list.get(i++), is("    private FooBarService mockService;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Override"));
        assertThat(list.get(i++), is("    @Before"));
        assertThat(list.get(i++), is("    public void setUp() throws Exception {"));
        assertThat(list.get(i++), is("        super.setUp();"));
        assertThat(list.get(i++), is("        reset(mockService);"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Test"));
        assertThat(list.get(i++), is("    @ApiDoc(title = \"資源清单\")"));
        assertThat(list.get(i++), is("    public void testListFooBars() throws Exception {"));
        assertThat(list.get(i++), is("        when(mockService.findAll(any(FooBarQuery.class))).thenReturn(FooBars.page(3));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        MockHttpServletRequestBuilder requestBuilder = get(\"/fooBars\")"));
        assertThat(list.get(i++), is("                .contentType(MediaType.APPLICATION_JSON);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        mockMvc.perform(requestBuilder)"));
        assertThat(list.get(i++), is("                .andExpect(status().is(HttpStatus.OK.value()))"));
        assertThat(list.get(i++), is("                .andExpect(jsonPath(\"$.totalElements\").value(Matchers.greaterThan(1)));"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Ignore(\"Don't ignore this, make it GREEN BAR instead\")"));
        assertThat(list.get(i++), is("    @Test"));
        assertThat(list.get(i++), is("    @ApiDoc(title = \"新增資源\")"));
        assertThat(list.get(i++), is("    public void testAddFooBar() throws Exception {"));
        assertThat(list.get(i++), is("        Map<String, String> input = Maps.newLinkedHashMap();"));
        assertThat(list.get(i++), is("        input.put(\"status\", \"2\");"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        String json = DomainUtil.toJson(input);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        MockHttpServletRequestBuilder requestBuilder = post(\"/fooBars\")"));
        assertThat(list.get(i++), is("                .contentType(MediaType.APPLICATION_JSON)"));
        assertThat(list.get(i++), is("                .content(json);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        mockMvc.perform(requestBuilder)"));
        assertThat(list.get(i++), is("                .andExpect(status().is(HttpStatus.CREATED.value()))"));
        assertThat(list.get(i++), is("                .andExpect(jsonPath(\"$.status\").value(\"2\"));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        verify(mockService).add(Mockito.any(FooBar.class));"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Test"));
        assertThat(list.get(i++), is("    @ApiDoc(title = \"取得資源\")"));
        assertThat(list.get(i++), is("    public void testGetFooBar() throws Exception {"));
        assertThat(list.get(i++), is("        FooBar fooBar = FooBars.all().get(0);"));
        assertThat(list.get(i++), is("        String uuid = fooBar.getUuid();"));
        assertThat(list.get(i++), is("        when(mockService.findOne(uuid)).thenReturn(Optional.of(fooBar));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        MockHttpServletRequestBuilder requestBuilder = get(\"/fooBars/\" + uuid)"));
        assertThat(list.get(i++), is("                .contentType(MediaType.APPLICATION_JSON);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        mockMvc.perform(requestBuilder)"));
        assertThat(list.get(i++), is("                .andExpect(status().is(HttpStatus.OK.value()))"));
        assertThat(list.get(i++), is("                .andExpect(jsonPath(\"$.uuid\").exists());"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Ignore(\"Don't ignore this, make it GREEN BAR instead\")"));
        assertThat(list.get(i++), is("    @Test"));
        assertThat(list.get(i++), is("    @ApiDoc(title = \"更新資源\")"));
        assertThat(list.get(i++), is("    public void testUpdateFooBar() throws Exception {"));
        assertThat(list.get(i++), is("        FooBar fooBar = FooBars.all().get(0);"));
        assertThat(list.get(i++), is("        String uuid = fooBar.getUuid();"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        Map<String, String> input = Maps.newLinkedHashMap();"));
        assertThat(list.get(i++), is("        input.put(\"name\", \"name_new\");"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        String json = DomainUtil.toJson(input);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        when(mockService.findOne(uuid)).thenReturn(Optional.of(fooBar));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        MockHttpServletRequestBuilder requestBuilder = patch(\"/fooBars/\" + uuid)"));
        assertThat(list.get(i++), is("                .contentType(MediaType.APPLICATION_JSON)"));
        assertThat(list.get(i++), is("                .content(json);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        mockMvc.perform(requestBuilder)"));
        assertThat(list.get(i++), is("                .andExpect(status().is(HttpStatus.OK.value()))"));
        assertThat(list.get(i++), is("                .andExpect(jsonPath(\"$.name\").value(\"name_new\"));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        verify(mockService).update(any(FooBar.class));"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Test"));
        assertThat(list.get(i++), is("    @ApiDoc(title = \"删除資源\")"));
        assertThat(list.get(i++), is("    public void testDeleteFooBar() throws Exception {"));
        assertThat(list.get(i++), is("        FooBar fooBar = FooBars.all().get(0);"));
        assertThat(list.get(i++), is("        String uuid = fooBar.getUuid();"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        MockHttpServletRequestBuilder requestBuilder = delete(\"/fooBars/\" + uuid)"));
        assertThat(list.get(i++), is("                .contentType(MediaType.APPLICATION_JSON);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        mockMvc.perform(requestBuilder)"));
        assertThat(list.get(i++), is("                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        verify(mockService).delete(uuid);"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
    }


}