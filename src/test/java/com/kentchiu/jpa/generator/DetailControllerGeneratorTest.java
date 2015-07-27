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

public class DetailControllerGeneratorTest {

    private DetailControllerGenerator generator;

    @Before
    public void setUp() throws Exception {
        Table table = Tables.table1();
        Transformer transformer = new Transformer();
        transformer.setTableNameMapper(ImmutableMap.of(table.getName(), "com.kentchiu.module.domain.FooBar"));
        generator = new DetailControllerGenerator(transformer);
    }

    @Test
    public void testExport() throws Exception {
        Table table = Tables.table1();
        Optional<Path> export = generator.exportToFile(table, ImmutableList.of());
        assertThat(export.isPresent(), Is.is(true));
        assertThat(export.get().toString(), containsString("/src/main/java/com/kentchiu/module/web/FooBarController.java"));
    }


    @Test
    public void testApplyTemplate() throws Exception {
        Table table = Tables.table1();
        List<String> list = generator.applyTemplate(table);

        list.stream().forEach(System.out::println);
        int i = 0;

        assertThat(list.get(i++), is("package com.kentchiu.module.web;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import com.kentchiu.base.web.AbstractController;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.DeviceKind;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.FooBar;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.DeviceKindService;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.FooBarService;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.query.FooBarQuery;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.web.dto.FooBarInput;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.web.dto.FooBarUpdateInput;"));
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
        assertThat(list.get(i++), is("@RequestMapping(\"/deviceKinds/{deviceKindUuid}/detections\")"));
        assertThat(list.get(i++), is("public class DeviceDetectionController extends AbstractController {"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    private DeviceKindService deviceKindService;"));
        assertThat(list.get(i++), is("    private DeviceDetectionService service;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Autowired"));
        assertThat(list.get(i++), is("    public void setDeviceKindService(DeviceKindService deviceKindService) {"));
        assertThat(list.get(i++), is("        this.deviceKindService = deviceKindService;"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Autowired"));
        assertThat(list.get(i++), is("    public void setService(DeviceDetectionService service) {"));
        assertThat(list.get(i++), is("        this.service = service;"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @RequestMapping(method = RequestMethod.GET)"));
        assertThat(list.get(i++), is("    public Page<DeviceDetection> listDeviceDetections(@PathVariable String deviceKindUuid, @Valid DeviceDetectionQuery query) {"));
        assertThat(list.get(i++), is("        checkMasterExistOrThrow(deviceKindUuid);"));
        assertThat(list.get(i++), is("        return service.findAll(query);"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @ResponseStatus(HttpStatus.CREATED)"));
        assertThat(list.get(i++), is("    @RequestMapping(method = RequestMethod.POST)"));
        assertThat(list.get(i++), is("    public DeviceDetection addDeviceDetection(@PathVariable String deviceKindUuid, @Valid @RequestBody DeviceDetectionInput input) throws BindException {"));
        assertThat(list.get(i++), is("        checkMasterExistOrThrow(deviceKindUuid);"));
        assertThat(list.get(i++), is("        DeviceDetection detection = new DeviceDetection();"));
        assertThat(list.get(i++), is("        DomainUtil.copyNotNullProperties(input, detection);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        if (StringUtils.isNotBlank(input.getDeviceKindUuid())) {"));
        assertThat(list.get(i++), is("            DeviceKind deviceKind = deviceKindService.findOne(input.getDeviceKindUuid()).orElseThrow(() -> new ResourceNotFoundException(DeviceKind.class, input.getDeviceKindUuid()));"));
        assertThat(list.get(i++), is("            detection.setDeviceKind(deviceKind);"));
        assertThat(list.get(i++), is("        }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        // FIXME  add references"));
        assertThat(list.get(i++), is("        // if (StringUtils.isNotBlank(input.getXxxUuid())) {"));
        assertThat(list.get(i++), is("        //    Xxx xxx = xxxService.findOne(input.getXxxUuid()).orElseThrow(() -> new ResourceNotFoundException(Xxx.class, input.getXxxUuid()));"));
        assertThat(list.get(i++), is("        //    DeviceDetection.setXxx(xxx);"));
        assertThat(list.get(i++), is("        // }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        BindException exception = new BindException(detection, detection.getClass().getSimpleName());"));
        assertThat(list.get(i++), is("        Validators.validateBean(exception, detection);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        if (exception.hasErrors()) {"));
        assertThat(list.get(i++), is("            throw exception;"));
        assertThat(list.get(i++), is("        }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        service.add(detection);"));
        assertThat(list.get(i++), is("        return detection;"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @RequestMapping(value = \"/{uuid}\", method = RequestMethod.GET)"));
        assertThat(list.get(i++), is("    public DeviceDetection getDeviceDetection(@PathVariable String deviceKindUuid, @PathVariable String uuid) {"));
        assertThat(list.get(i++), is("        checkMasterExistOrThrow(deviceKindUuid);"));
        assertThat(list.get(i++), is("        return service.findOne(uuid).orElseThrow(() -> new ResourceNotFoundException(DeviceDetection.class, uuid));"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @RequestMapping(value = \"/{uuid}\", method = RequestMethod.PATCH)"));
        assertThat(list.get(i++), is("    public DeviceDetection updateDeviceDetection(@PathVariable String deviceKindUuid, @PathVariable String uuid, @Valid @RequestBody DeviceDetectionUpdateInput input) throws BindException {"));
        assertThat(list.get(i++), is("        checkMasterExistOrThrow(deviceKindUuid);"));
        assertThat(list.get(i++), is("        DeviceDetection detection = service.findOne(uuid).orElseThrow(() -> new ResourceNotFoundException(DeviceDetection.class, uuid));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        DomainUtil.copyNotNullProperties(input, detection);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        // FIXME  add references"));
        assertThat(list.get(i++), is("        // if (StringUtils.isNotBlank(input.getXxxUuid())) {"));
        assertThat(list.get(i++), is("        //    Xxx xxx = xxxService.findOne(input.getXxxUuid()).orElseThrow(() -> new ResourceNotFoundException(Xxx.class, input.getXxxUuid()));"));
        assertThat(list.get(i++), is("        //    DeviceDetection.setXxx(xxx);"));
        assertThat(list.get(i++), is("        // }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        BindException exception = new BindException(detection, detection.getClass().getSimpleName());"));
        assertThat(list.get(i++), is("        Validators.validateBean(exception, detection);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        if (exception.hasErrors()) {"));
        assertThat(list.get(i++), is("            throw exception;"));
        assertThat(list.get(i++), is("        }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        service.update(detection);"));
        assertThat(list.get(i++), is("        return detection;"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @ResponseStatus(HttpStatus.NO_CONTENT)"));
        assertThat(list.get(i++), is("    @RequestMapping(value = \"/{uuid}\", method = RequestMethod.DELETE)"));
        assertThat(list.get(i++), is("    public void deleteDeviceDetection(@PathVariable String deviceKindUuid, @PathVariable String uuid) {"));
        assertThat(list.get(i++), is("        checkMasterExistOrThrow(deviceKindUuid);"));
        assertThat(list.get(i++), is("        service.delete(uuid);"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    private void checkMasterExistOrThrow(@PathVariable String deviceKindUuid) {"));
        assertThat(list.get(i++), is("        deviceKindService.findOne(deviceKindUuid).orElseThrow(() -> new ResourceNotFoundException(DeviceDetection.class, deviceKindUuid));"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is("}"));

    }


}