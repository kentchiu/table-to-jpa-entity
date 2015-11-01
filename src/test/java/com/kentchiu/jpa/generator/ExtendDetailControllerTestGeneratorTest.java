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

public class ExtendDetailControllerTestGeneratorTest {

    private ExtendDetailControllerTestGenerator generator;

    @Before
    public void setUp() throws Exception {
        Table table = Tables.extendDetail();
        Transformer transformer = new Transformer();

        Map<String, String> mapper = new HashMap<>();
        mapper.put("TBL_MASTER", "com.kentchiu.module.domain.Master");
        mapper.put("TBL_DETAIL", "com.kentchiu.module.domain.Detail");
        mapper.put("TBL_EXTEND_DETAIL", "com.kentchiu.module.domain.ExtendDetail");
        transformer.setTableNameMapper(mapper);
        DetailConfig detailConfig = new DetailConfig("master", "detail", "TBL_MASTER", "TBL_DETAIL");
        ExtendDetailConfig config = new ExtendDetailConfig(detailConfig, "extendDetail", "TBL_EXTEND_DETAIL");
        transformer.setMasterDetailMapper(ImmutableMap.of(table.getName(), config));
        generator = new ExtendDetailControllerTestGenerator(transformer);
    }

    @Test
    public void testExport() throws Exception {
        Table table = Tables.extendDetail();
        Optional<Path> export = generator.exportToFile(table, ImmutableList.of());
        assertThat(export.isPresent(), Is.is(true));
        assertThat(export.get().toString(), containsString("/src/test/java/com/kentchiu/module/web/ExtendDetailControllerTest.java"));
    }


    @Test
    public void testApplyTemplate() throws Exception {
        Table table = Tables.extendDetail();
        List<String> list = generator.applyTemplate(table);

        list.stream().forEach(System.out::println);
        int i = 0;

        assertThat(list.get(i++), is("package com.kentchiu.module.web;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import com.kentchiu.base.web.AbstractControllerTest;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.DeviceKind;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.FooBar;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.DeviceKinds;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.FooBars;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.DeviceKindService;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.FooBarService;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.query.FooBarQuery;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.web.dto.FooBarInput;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.web.dto.FooBarUpdateInput;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import com.google.common.collect.Maps;"));
        assertThat(list.get(i++), is("import com.kentchiu.spring.base.domain.DomainUtil;"));
        assertThat(list.get(i++), is("import org.hamcrest.Matchers;"));
        assertThat(list.get(i++), is("import org.junit.Before;"));
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
        assertThat(list.get(i++), is("@ContextConfiguration(classes = {TestConfig.class})"));
        assertThat(list.get(i++), is("public class FooBarControllerTest extends AbstractControllerTest {"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Autowired"));
        assertThat(list.get(i++), is("    private FooBarService mockService;"));
        assertThat(list.get(i++), is("    @Autowired"));
        assertThat(list.get(i++), is("    private DeviceKindService mockDeviceKindService;"));
        assertThat(list.get(i++), is("    private String deviceKindUuid;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Override"));
        assertThat(list.get(i++), is("    @Before"));
        assertThat(list.get(i++), is("    public void setUp() throws Exception {"));
        assertThat(list.get(i++), is("        super.setUp();"));
        assertThat(list.get(i++), is("        reset(mockService);"));
        assertThat(list.get(i++), is("        reset(mockDeviceKindService);"));
        assertThat(list.get(i++), is("        DeviceKind deviceKind = DeviceKinds.all().get(0);"));
        assertThat(list.get(i++), is("        deviceKindUuid = deviceKind.getUuid();"));
        assertThat(list.get(i++), is("        when(mockDeviceKindService.findOne(deviceKindUuid)).thenReturn(Optional.of(deviceKind));"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Test"));
        assertThat(list.get(i++), is("    public void testListFooBars() throws Exception {"));
        assertThat(list.get(i++), is("        when(mockService.findAll(any(FooBarQuery.class))).thenReturn(FooBars.page(3));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        MockHttpServletRequestBuilder requestBuilder = get(\"/deviceKinds/\" + deviceKindUuid + \"/detections\")"));
        assertThat(list.get(i++), is("                .contentType(MediaType.APPLICATION_JSON);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        mockMvc.perform(requestBuilder)"));
        assertThat(list.get(i++), is("                .andExpect(status().is(HttpStatus.OK.value()))"));
        assertThat(list.get(i++), is("                .andExpect(jsonPath(\"$.totalElements\").value(Matchers.greaterThan(1)));"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Test"));
        assertThat(list.get(i++), is("    public void testAddFooBar() throws Exception {"));
        assertThat(list.get(i++), is("        Map<String, String> input = Maps.newLinkedHashMap();"));
        assertThat(list.get(i++), is("        input.put(\"name\", \"name_001\");"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        String json = DomainUtil.toJson(input);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        MockHttpServletRequestBuilder requestBuilder = post(\"/deviceKinds/\" + deviceKindUuid + \"/detections\")"));
        assertThat(list.get(i++), is("                .contentType(MediaType.APPLICATION_JSON)"));
        assertThat(list.get(i++), is("                .content(json);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        mockMvc.perform(requestBuilder)"));
        assertThat(list.get(i++), is("                .andExpect(status().is(HttpStatus.CREATED.value()))"));
        assertThat(list.get(i++), is("                .andExpect(jsonPath(\"$.name\").value(\"name_001\"));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        verify(mockService).add(Mockito.any(FooBar.class));"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Test"));
        assertThat(list.get(i++), is("    public void testGetFooBar() throws Exception {"));
        assertThat(list.get(i++), is("        FooBar detection = FooBars.all().get(0);"));
        assertThat(list.get(i++), is("        String uuid = detection.getUuid();"));
        assertThat(list.get(i++), is("        when(mockService.findOne(uuid)).thenReturn(Optional.of(detection));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        MockHttpServletRequestBuilder requestBuilder = get(\"/deviceKinds/\" + deviceKindUuid + \"/detections/\" + uuid)"));
        assertThat(list.get(i++), is("                .contentType(MediaType.APPLICATION_JSON);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        mockMvc.perform(requestBuilder)"));
        assertThat(list.get(i++), is("                .andExpect(status().is(HttpStatus.OK.value()))"));
        assertThat(list.get(i++), is("                .andExpect(jsonPath(\"$.uuid\").exists());"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Test"));
        assertThat(list.get(i++), is("    public void testUpdateFooBar() throws Exception {"));
        assertThat(list.get(i++), is("        FooBar detection = FooBars.all().get(0);"));
        assertThat(list.get(i++), is("        String uuid = detection.getUuid();"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        Map<String, String> input = Maps.newLinkedHashMap();"));
        assertThat(list.get(i++), is("        input.put(\"name\", \"name_new\");"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        String json = DomainUtil.toJson(input);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        when(mockService.findOne(uuid)).thenReturn(Optional.of(detection));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        MockHttpServletRequestBuilder requestBuilder = patch(\"/deviceKinds/\" + deviceKindUuid + \"/detections/\" + uuid)"));
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
        assertThat(list.get(i++), is("    public void testDeleteFooBar() throws Exception {"));
        assertThat(list.get(i++), is("        FooBar detection = FooBars.all().get(0);"));
        assertThat(list.get(i++), is("        String uuid = detection.getUuid();"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        MockHttpServletRequestBuilder requestBuilder = delete(\"/deviceKinds/\" + deviceKindUuid + \"/detections/\" + uuid)"));
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