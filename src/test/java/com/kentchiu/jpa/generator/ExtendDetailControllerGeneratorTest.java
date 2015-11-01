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

public class ExtendDetailControllerGeneratorTest {

    private ExtendDetailControllerGenerator generator;

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
        generator = new ExtendDetailControllerGenerator(transformer);
    }

    @Test
    public void testExport() throws Exception {
        Table table = Tables.extendDetail();
        Optional<Path> export = generator.exportToFile(table, ImmutableList.of());
        assertThat(export.isPresent(), Is.is(true));
        assertThat(export.get().toString(), containsString("/src/main/java/com/kentchiu/module/web/ExtendDetailController.java"));
    }


    @Test
    public void testApplyTemplate() throws Exception {
        Table table = Tables.extendDetail();
        List<String> list = generator.applyTemplate(table);

        list.stream().forEach(System.out::println);
        int i = 0;

        assertThat(list.get(i++), is("package com.kentchiu.module.web;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import com.kentchiu.base.web.AbstractController;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.Master;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.Detail;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.ExtendDetail;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.MasterService;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.DetailService;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.ExtendDetailService;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.query.ExtendDetailQuery;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.web.dto.ExtendDetailInput;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.web.dto.ExtendDetailUpdateInput;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.web.AbstractController;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import com.kentchiu.spring.base.domain.DomainUtil;"));
        assertThat(list.get(i++), is("import com.kentchiu.spring.base.domain.ResourceNotFoundException;"));
        assertThat(list.get(i++), is("import com.kentchiu.spring.base.domain.Validators;"));
        assertThat(list.get(i++), is("import org.apache.commons.lang3.StringUtils;"));
        assertThat(list.get(i++), is("import org.springframework.beans.factory.annotation.Autowired;"));
        assertThat(list.get(i++), is("import org.springframework.data.domain.Page;"));
        assertThat(list.get(i++), is("import org.springframework.http.HttpStatus;"));
        assertThat(list.get(i++), is("import org.springframework.validation.BindException;"));
        assertThat(list.get(i++), is("import org.springframework.web.bind.annotation.*;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import javax.validation.Valid;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("@RestController"));
        assertThat(list.get(i++), is("@RequestMapping(\"/masters/{masterUuid}/details/{detailUuid}/extendDetails\")"));
        assertThat(list.get(i++), is("public class ExtendDetailController extends AbstractController {"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    private MasterService masterService;"));
        assertThat(list.get(i++), is("    private DetailService detailService;"));
        assertThat(list.get(i++), is("    private ExtendDetailService service;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Autowired"));
        assertThat(list.get(i++), is("    public void setMasterService(MasterService masterService) {"));
        assertThat(list.get(i++), is("        this.masterService = masterService;"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Autowired"));
        assertThat(list.get(i++), is("    public void setDetailService(DetailService detailService) {"));
        assertThat(list.get(i++), is("        this.detailService = detailService;"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Autowired"));
        assertThat(list.get(i++), is("    public void setService(ExtendDetailService service) {"));
        assertThat(list.get(i++), is("        this.service = service;"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @RequestMapping(method = RequestMethod.GET)"));
        assertThat(list.get(i++), is("    public Page<ExtendDetail> listExtendDetails(@PathVariable String masterUuid, @PathVariable String detailUuid, @Valid ExtendDetailQuery query) {"));
        assertThat(list.get(i++), is("        checkParentExist(masterUuid, detailUuid);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        query.setDetailUuid(detailUuid);"));
        assertThat(list.get(i++), is("        return service.findAll(query);"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @ResponseStatus(HttpStatus.CREATED)"));
        assertThat(list.get(i++), is("    @RequestMapping(method = RequestMethod.POST)"));
        assertThat(list.get(i++), is("    public ExtendDetail addExtendDetail(@PathVariable String masterUuid, @PathVariable String detailUuid, @Valid @RequestBody ExtendDetailInput input) throws BindException {"));
        assertThat(list.get(i++), is("        checkParentExist(masterUuid, detailUuid);"));
        assertThat(list.get(i++), is("        ExtendDetail detail = new ExtendDetail();"));
        assertThat(list.get(i++), is("        DomainUtil.copyNotNullProperties(input, detail);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        // detail"));
        assertThat(list.get(i++), is("        Detail detail = detailService.findOne(detailUuid).orElseThrow(() -> new ResourceNotFoundException(Detail.class, detailUuid));"));
        assertThat(list.get(i++), is("        detail.setDetail(detail);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        // FIXME  add references"));
        assertThat(list.get(i++), is("        // if (StringUtils.isNotBlank(input.getXxxUuid())) {"));
        assertThat(list.get(i++), is("        //    Xxx xxx = xxxService.findOne(input.getXxxUuid()).orElseThrow(() -> new ResourceNotFoundException(Xxx.class, input.getXxxUuid()));"));
        assertThat(list.get(i++), is("        //    ExtendDetail.setXxx(xxx);"));
        assertThat(list.get(i++), is("        // }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        BindException exception = new BindException(detail, detail.getClass().getSimpleName());"));
        assertThat(list.get(i++), is("        Validators.validateBean(exception, detail);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        if (exception.hasErrors()) {"));
        assertThat(list.get(i++), is("            throw exception;"));
        assertThat(list.get(i++), is("        }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        service.add(detail);"));
        assertThat(list.get(i++), is("        return detail;"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @RequestMapping(value = \"/{uuid}\", method = RequestMethod.GET)"));
        assertThat(list.get(i++), is("    public ExtendDetail getExtendDetail(@PathVariable String masterUuid, @PathVariable String detailUuid, @PathVariable String uuid) {"));
        assertThat(list.get(i++), is("        checkParentExist(masterUuid, detailUuid);"));
        assertThat(list.get(i++), is("        return service.findOne(uuid).orElseThrow(() -> new ResourceNotFoundException(ExtendDetail.class, uuid));"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @RequestMapping(value = \"/{uuid}\", method = RequestMethod.PATCH)"));
        assertThat(list.get(i++), is("    public ExtendDetail updateExtendDetail(@PathVariable String masterUuid, @PathVariable String detailUuid, @PathVariable String uuid, @Valid @RequestBody ExtendDetailUpdateInput input) throws BindException {"));
        assertThat(list.get(i++), is("        checkParentExist(masterUuid, detailUuid);"));
        assertThat(list.get(i++), is("        ExtendDetail detail = service.findOne(uuid).orElseThrow(() -> new ResourceNotFoundException(ExtendDetail.class, uuid));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        DomainUtil.copyNotNullProperties(input, detail);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        // FIXME  add references"));
        assertThat(list.get(i++), is("        // if (StringUtils.isNotBlank(input.getXxxUuid())) {"));
        assertThat(list.get(i++), is("        //    Xxx xxx = xxxService.findOne(input.getXxxUuid()).orElseThrow(() -> new ResourceNotFoundException(Xxx.class, input.getXxxUuid()));"));
        assertThat(list.get(i++), is("        //    ExtendDetail.setXxx(xxx);"));
        assertThat(list.get(i++), is("        // }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        BindException exception = new BindException(detail, detail.getClass().getSimpleName());"));
        assertThat(list.get(i++), is("        Validators.validateBean(exception, detail);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        if (exception.hasErrors()) {"));
        assertThat(list.get(i++), is("            throw exception;"));
        assertThat(list.get(i++), is("        }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        service.update(detail);"));
        assertThat(list.get(i++), is("        return detail;"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @ResponseStatus(HttpStatus.NO_CONTENT)"));
        assertThat(list.get(i++), is("    @RequestMapping(value = \"/{uuid}\", method = RequestMethod.DELETE)"));
        assertThat(list.get(i++), is("    public void deleteExtendDetail(@PathVariable String masterUuid, @PathVariable String detailUuid, @PathVariable String uuid) {"));
        assertThat(list.get(i++), is("        checkParentExist(masterUuid, detailUuid);"));
        assertThat(list.get(i++), is("        service.delete(uuid);"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    private void checkParentExist(String masterUuid, String detailUuid) {"));
        assertThat(list.get(i++), is("        masterService.findOne(masterUuid).orElseThrow(() -> new ResourceNotFoundException(Master.class, masterUuid));"));
        assertThat(list.get(i++), is("        detailService.findOne(detailUuid).orElseThrow(() -> new ResourceNotFoundException(Detail.class, detailUuid));"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("}"));

    }


    @Test
    public void testApplyTemplate_enableFilter() throws Exception {
        Table table = Tables.extendDetail();
        generator.getExtraParams().put("enableFilter", true);
        List<String> list = generator.applyTemplate(table);

        AbstractGenerator.dump(list);

        int i = 50;

        assertThat(list.get(i++), is("    @RequestMapping(method = RequestMethod.GET)"));
        assertThat(list.get(i++), is("    public Page<ExtendDetail> listExtendDetails(@PathVariable String masterUuid, @PathVariable String detailUuid, @Valid ExtendDetailQuery query) {"));
        assertThat(list.get(i++), is("        checkParentExist(masterUuid, detailUuid);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        query.setDetailUuid(detailUuid);"));
        assertThat(list.get(i++), is("        setFilter(query);"));
        assertThat(list.get(i++), is("        return service.findAll(query);"));
        assertThat(list.get(i++), is("    }"));


    }

}