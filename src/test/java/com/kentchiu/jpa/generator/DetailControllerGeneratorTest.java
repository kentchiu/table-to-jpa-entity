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

public class DetailControllerGeneratorTest {

    private DetailControllerGenerator generator;
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
        generator = new DetailControllerGenerator(transformer);
    }

    @Test
    public void testExport() throws Exception {
        Optional<Path> export = generator.exportToFile(table, ImmutableList.of());
        assertThat(export.isPresent(), Is.is(true));
        assertThat(export.get().toString(), containsString("/src/main/java/com/kentchiu/module/web/DetailController.java"));
    }


    @Test
    public void testApplyTemplate() throws Exception {
        List<String> list = generator.applyTemplate(table);

        int i = 0;

        assertThat(list.get(i++), is("package com.kentchiu.module.web;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import com.kentchiu.base.web.AbstractController;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.Master;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.Detail;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.MasterService;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.DetailService;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.query.DetailQuery;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.web.dto.DetailInput;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.web.dto.DetailUpdateInput;"));
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
        assertThat(list.get(i++), is("@RequestMapping(\"/masters/{masterUuid}/details\")"));
        assertThat(list.get(i++), is("public class DetailController extends AbstractController {"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    private MasterService masterService;"));
        assertThat(list.get(i++), is("    private DetailService service;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Autowired"));
        assertThat(list.get(i++), is("    public void setMasterService(MasterService masterService) {"));
        assertThat(list.get(i++), is("        this.masterService = masterService;"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Autowired"));
        assertThat(list.get(i++), is("    public void setService(DetailService service) {"));
        assertThat(list.get(i++), is("        this.service = service;"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @RequestMapping(method = RequestMethod.GET)"));
        assertThat(list.get(i++), is("    public Page<Detail> listDetails(@PathVariable String masterUuid, @Valid DetailQuery query) {"));
        assertThat(list.get(i++), is("        checkMasterExistOrThrow(masterUuid);"));
        assertThat(list.get(i++), is("        query.setMasterUuid(masterUuid);"));
        assertThat(list.get(i++), is("        return service.findAll(query);"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @ResponseStatus(HttpStatus.CREATED)"));
        assertThat(list.get(i++), is("    @RequestMapping(method = RequestMethod.POST)"));
        assertThat(list.get(i++), is("    public Detail addDetail(@PathVariable String masterUuid, @Valid @RequestBody DetailInput input) throws BindException {"));
        assertThat(list.get(i++), is("        checkMasterExistOrThrow(masterUuid);"));
        assertThat(list.get(i++), is("        Detail detail = new Detail();"));
        assertThat(list.get(i++), is("        DomainUtil.copyNotNullProperties(input, detail);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        // master"));
        assertThat(list.get(i++), is("        Master master = masterService.findOne(masterUuid).orElseThrow(() -> new ResourceNotFoundException(Master.class, masterUuid));"));
        assertThat(list.get(i++), is("        detail.setMaster(master);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        // FIXME  add references"));
        assertThat(list.get(i++), is("        // if (StringUtils.isNotBlank(input.getXxxUuid())) {"));
        assertThat(list.get(i++), is("        //    Xxx xxx = xxxService.findOne(input.getXxxUuid()).orElseThrow(() -> new ResourceNotFoundException(Xxx.class, input.getXxxUuid()));"));
        assertThat(list.get(i++), is("        //    Detail.setXxx(xxx);"));
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
        assertThat(list.get(i++), is("    public Detail getDetail(@PathVariable String masterUuid, @PathVariable String uuid) {"));
        assertThat(list.get(i++), is("        checkMasterExistOrThrow(masterUuid);"));
        assertThat(list.get(i++), is("        return service.findOne(uuid).orElseThrow(() -> new ResourceNotFoundException(Detail.class, uuid));"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @RequestMapping(value = \"/{uuid}\", method = RequestMethod.PATCH)"));
        assertThat(list.get(i++), is("    public Detail updateDetail(@PathVariable String masterUuid, @PathVariable String uuid, @Valid @RequestBody DetailUpdateInput input) throws BindException {"));
        assertThat(list.get(i++), is("        checkMasterExistOrThrow(masterUuid);"));
        assertThat(list.get(i++), is("        Detail detail = service.findOne(uuid).orElseThrow(() -> new ResourceNotFoundException(Detail.class, uuid));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        DomainUtil.copyNotNullProperties(input, detail);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        // FIXME  add references"));
        assertThat(list.get(i++), is("        // if (StringUtils.isNotBlank(input.getXxxUuid())) {"));
        assertThat(list.get(i++), is("        //    Xxx xxx = xxxService.findOne(input.getXxxUuid()).orElseThrow(() -> new ResourceNotFoundException(Xxx.class, input.getXxxUuid()));"));
        assertThat(list.get(i++), is("        //    Detail.setXxx(xxx);"));
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
        assertThat(list.get(i++), is("    public void deleteDetail(@PathVariable String masterUuid, @PathVariable String uuid) {"));
        assertThat(list.get(i++), is("        checkMasterExistOrThrow(masterUuid);"));
        assertThat(list.get(i++), is("        service.delete(uuid);"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    private void checkMasterExistOrThrow(@PathVariable String masterUuid) {"));
        assertThat(list.get(i++), is("        masterService.findOne(masterUuid).orElseThrow(() -> new ResourceNotFoundException(Detail.class, masterUuid));"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is("}"));
    }


    @Test
    public void testApplyTemplate_enableFilter() throws Exception {
        generator.getExtraParams().put("enableFilter", true);
        List<String> list = generator.applyTemplate(table);

        int i = 42;

        assertThat(list.get(i++), is("    @RequestMapping(method = RequestMethod.GET)"));
        assertThat(list.get(i++), is("    public Page<Detail> listDetails(@PathVariable String masterUuid, @Valid DetailQuery query) {"));
        assertThat(list.get(i++), is("        checkMasterExistOrThrow(masterUuid);"));
        assertThat(list.get(i++), is("        query.setMasterUuid(masterUuid);"));
        assertThat(list.get(i++), is("        setFilter(query);"));
        assertThat(list.get(i++), is("        return service.findAll(query);"));
        assertThat(list.get(i++), is("    }"));
    }

}