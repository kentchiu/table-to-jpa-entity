package com.kentchiu.jpa.generator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kentchiu.jpa.domain.Table;
import com.kentchiu.jpa.domain.Tables;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class DetailControllerTestGeneratorTest {

    private DetailControllerTestGenerator generator;
    private Table table;

    @Before
    public void setUp() throws Exception {
        table = Tables.detail();
        Transformer transformer = new Transformer();
        Map<String, String> mapper = new HashMap<>();
        mapper.put("TBL_MASTER", "com.kentchiu.module.domain.Master");
        mapper.put("TBL_DETAIL", "com.kentchiu.module.domain.Detail");
        transformer.setTableNameMapper(mapper);
        DetailConfig config = new DetailConfig("master", "detail", "TBL_MASTER", "TBL_DETAIL");
        transformer.setMasterDetailMapper(ImmutableMap.of(table.getName(), config));
        generator = new DetailControllerTestGenerator(transformer);
        generator.getExtraParams().put("resourceName", "資源");
    }

    @Test
    public void testExport() throws Exception {
        Optional<Path> export = generator.exportToFile(table, ImmutableList.of());
        assertThat(export.isPresent(), Is.is(true));
        assertThat(export.get().toString(), containsString("/src/test/java/com/kentchiu/module/web/DetailControllerTest.java"));
    }


    @Test
    public void testApplyTemplate() throws Exception {
        List<String> list = generator.applyTemplate(table);

        int i = 0;

        assertThat(list.get(i++), is("package com.kentchiu.module.web;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import com.kentchiu.base.web.AbstractControllerTest;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.Master;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.Detail;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.Masters;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.Details;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.MasterService;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.DetailService;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.query.DetailQuery;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.web.dto.DetailInput;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.web.dto.DetailUpdateInput;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import com.google.common.collect.Maps;"));
        assertThat(list.get(i++), is("import com.kentchiu.spring.base.domain.DomainUtil;"));
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
        assertThat(list.get(i++), is("@ResourceDoc(title = \"資源\", type = Detail.class)"));
        assertThat(list.get(i++), is("@ContextConfiguration(classes = {TestConfig.class})"));
        assertThat(list.get(i++), is("public class DetailControllerTest extends AbstractControllerTest {"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Autowired"));
        assertThat(list.get(i++), is("    private DetailService mockService;"));
        assertThat(list.get(i++), is("    @Autowired"));
        assertThat(list.get(i++), is("    private MasterService mockMasterService;"));
        assertThat(list.get(i++), is("    private Master master;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Override"));
        assertThat(list.get(i++), is("    @Before"));
        assertThat(list.get(i++), is("    public void setUp() throws Exception {"));
        assertThat(list.get(i++), is("        super.setUp();"));
        assertThat(list.get(i++), is("        reset(mockService);"));
        assertThat(list.get(i++), is("        reset(mockMasterService);"));
        assertThat(list.get(i++), is("        Master master = Masters.all().get(0);"));
        assertThat(list.get(i++), is("        when(mockMasterService.exits(master.getUuid())).thenReturn(true);"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Test"));
        assertThat(list.get(i++), is("    @ApiDoc(title = \"資源清单\")"));
        assertThat(list.get(i++), is("    public void testListDetails() throws Exception {"));
        assertThat(list.get(i++), is("        when(mockService.findAll(any(DetailQuery.class))).thenReturn(Details.page(3));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        MockHttpServletRequestBuilder requestBuilder = get(\"/masters/\" + masterUuid + \"/details\")"));
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
        assertThat(list.get(i++), is("    public void testAddDetail() throws Exception {"));
        assertThat(list.get(i++), is("        Map<String, String> input = Maps.newLinkedHashMap();"));
        assertThat(list.get(i++), is("        input.put(\"status\", \"2\");"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        String json = DomainUtil.toJson(input);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        MockHttpServletRequestBuilder requestBuilder = post(\"/masters/\" + masterUuid + \"/details\")"));
        assertThat(list.get(i++), is("                .contentType(MediaType.APPLICATION_JSON)"));
        assertThat(list.get(i++), is("                .content(json);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        mockMvc.perform(requestBuilder)"));
        assertThat(list.get(i++), is("                .andExpect(status().is(HttpStatus.CREATED.value()))"));
        assertThat(list.get(i++), is("                .andExpect(jsonPath(\"$.status\").value(\"2\"));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        verify(mockService).add(Mockito.any(Detail.class));"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Test"));
        assertThat(list.get(i++), is("    @ApiDoc(title = \"取得資源\")"));
        assertThat(list.get(i++), is("    public void testGetDetail() throws Exception {"));
        assertThat(list.get(i++), is("        Detail detail = Details.all().get(0);"));
        assertThat(list.get(i++), is("        String uuid = detail.getUuid();"));
        assertThat(list.get(i++), is("        when(mockService.findOne(uuid)).thenReturn(Optional.of(detail));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        MockHttpServletRequestBuilder requestBuilder = get(\"/masters/\" + masterUuid + \"/details/\" + uuid)"));
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
        assertThat(list.get(i++), is("    public void testUpdateDetail() throws Exception {"));
        assertThat(list.get(i++), is("        Detail detail = Details.all().get(0);"));
        assertThat(list.get(i++), is("        String uuid = detail.getUuid();"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        Map<String, String> input = Maps.newLinkedHashMap();"));
        assertThat(list.get(i++), is("        input.put(\"name\", \"name_new\");"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        String json = DomainUtil.toJson(input);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        when(mockService.findOne(uuid)).thenReturn(Optional.of(detail));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        MockHttpServletRequestBuilder requestBuilder = patch(\"/masters/\" + masterUuid + \"/details/\" + uuid)"));
        assertThat(list.get(i++), is("                .contentType(MediaType.APPLICATION_JSON)"));
        assertThat(list.get(i++), is("                .content(json);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        mockMvc.perform(requestBuilder)"));
        assertThat(list.get(i++), is("                .andExpect(status().is(HttpStatus.OK.value()))"));
        assertThat(list.get(i++), is("                .andExpect(jsonPath(\"$.name\").value(\"name_new\"));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        verify(mockService).update(any(Detail.class));"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Test"));
        assertThat(list.get(i++), is("    @ApiDoc(title = \"删除資源\")"));
        assertThat(list.get(i++), is("    public void testDeleteDetail() throws Exception {"));
        assertThat(list.get(i++), is("        Detail detail = Details.all().get(0);"));
        assertThat(list.get(i++), is("        String uuid = detail.getUuid();"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        MockHttpServletRequestBuilder requestBuilder = delete(\"/masters/\" + masterUuid + \"/details/\" + uuid)"));
        assertThat(list.get(i++), is("                .contentType(MediaType.APPLICATION_JSON);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        mockMvc.perform(requestBuilder)"));
        assertThat(list.get(i++), is("                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        verify(mockService).delete(uuid);"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("}"));
    }


}