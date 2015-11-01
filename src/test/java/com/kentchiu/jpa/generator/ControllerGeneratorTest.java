package com.kentchiu.jpa.generator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kentchiu.jpa.domain.Table;
import com.kentchiu.jpa.domain.Tables;
import org.atteo.evo.inflector.English;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class ControllerGeneratorTest {

    private ControllerGenerator generator;
    private Table table;

    @Before
    public void setUp() throws Exception {
        table = Tables.table1();
        Transformer transformer = new Transformer();
        transformer.setTableNameMapper(ImmutableMap.of(table.getName(), "com.kentchiu.module.domain.FooBar"));
        generator = new ControllerGenerator(transformer);
    }

    @Test
    public void testExport() throws Exception {
        Optional<Path> export = generator.exportToFile(table, ImmutableList.of());
        assertThat(export.isPresent(), Is.is(true));
        assertThat(export.get().toString(), containsString("/src/main/java/com/kentchiu/module/web/FooBarController.java"));
    }

    @Test
    public void testPlural() throws Exception {
        assertThat(English.plural("foo", 2), is("foos"));
        assertThat(English.plural("city", 2), is("cities"));
        assertThat(English.plural("kiss", 2), is("kisses"));
    }

    @Test
    public void testApplyTemplate() throws Exception {
        List<String> list = generator.applyTemplate(table);

        list.stream().forEach(System.out::println);
        int i = 0;

        assertThat(list.get(i++), is("package com.kentchiu.module.web;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("import com.kentchiu.base.web.AbstractController;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.domain.FooBar;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.FooBarService;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.service.query.FooBarQuery;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.web.dto.FooBarInput;"));
        assertThat(list.get(i++), is("import com.kentchiu.module.web.dto.FooBarUpdateInput;"));
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
        assertThat(list.get(i++), is("import javax.validation.Valid;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("@RestController"));
        assertThat(list.get(i++), is("@RequestMapping(\"/fooBars\")"));
        assertThat(list.get(i++), is("public class FooBarController extends AbstractController {"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    private FooBarService service;"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @Autowired"));
        assertThat(list.get(i++), is("    public void setService(FooBarService service) {"));
        assertThat(list.get(i++), is("        this.service = service;"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @RequestMapping(method = RequestMethod.GET)"));
        assertThat(list.get(i++), is("    public Page<FooBar> listFooBars(@Valid FooBarQuery query) {"));
        assertThat(list.get(i++), is("        return service.findAll(query);"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @ResponseStatus(HttpStatus.CREATED)"));
        assertThat(list.get(i++), is("    @RequestMapping(method = RequestMethod.POST)"));
        assertThat(list.get(i++), is("    public FooBar addFooBar(@Valid @RequestBody FooBarInput input) throws BindException {"));
        assertThat(list.get(i++), is("        FooBar fooBar = new FooBar();"));
        assertThat(list.get(i++), is("        DomainUtil.copyNotNullProperties(input, fooBar);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        // FIXME add references if need"));
        assertThat(list.get(i++), is("        // if (StringUtils.isNotBlank(input.getXxxUuid())) {"));
        assertThat(list.get(i++), is("        //     Xxx xxx = xxxService.findOne(input.getXxxUuid()).orElseThrow(() -> new ResourceNotFoundException(Xxx.class, input.getXxxUuid()));"));
        assertThat(list.get(i++), is("        //     fooBar.setXxx(xxx);"));
        assertThat(list.get(i++), is("        // }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        BindException exception = new BindException(fooBar, fooBar.getClass().getSimpleName());"));
        assertThat(list.get(i++), is("        Validators.validateBean(exception, fooBar);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        if (exception.hasErrors()) {"));
        assertThat(list.get(i++), is("            throw exception;"));
        assertThat(list.get(i++), is("        }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        service.add(fooBar);"));
        assertThat(list.get(i++), is("        return fooBar;"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @RequestMapping(value = \"/{uuid}\", method = RequestMethod.GET)"));
        assertThat(list.get(i++), is("    public FooBar getFooBar(@PathVariable String uuid) {"));
        assertThat(list.get(i++), is("        return service.findOne(uuid).orElseThrow(() -> new ResourceNotFoundException(FooBar.class, uuid));"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @RequestMapping(value = \"/{uuid}\", method = RequestMethod.PATCH)"));
        assertThat(list.get(i++), is("    public FooBar updateFooBar(@PathVariable String uuid, @Valid @RequestBody FooBarUpdateInput input) throws BindException {"));
        assertThat(list.get(i++), is("        FooBar fooBar = service.findOne(uuid).orElseThrow(() -> new ResourceNotFoundException(FooBar.class, uuid));"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        DomainUtil.copyNotNullProperties(input, fooBar);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        // FIXME add references if need"));
        assertThat(list.get(i++), is("        // if (StringUtils.isNotBlank(input.getXxxUuid())) {"));
        assertThat(list.get(i++), is("        //     Xxx xxx = xxxService.findOne(input.getXxxUuid()).orElseThrow(() -> new ResourceNotFoundException(Xxx.class, input.getXxxUuid()));"));
        assertThat(list.get(i++), is("        //     fooBar.setXxx(xxx);"));
        assertThat(list.get(i++), is("        // }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        BindException exception = new BindException(fooBar, fooBar.getClass().getSimpleName());"));
        assertThat(list.get(i++), is("        Validators.validateBean(exception, fooBar);"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        if (exception.hasErrors()) {"));
        assertThat(list.get(i++), is("            throw exception;"));
        assertThat(list.get(i++), is("        }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("        service.update(fooBar);"));
        assertThat(list.get(i++), is("        return fooBar;"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("    @ResponseStatus(HttpStatus.NO_CONTENT)"));
        assertThat(list.get(i++), is("    @RequestMapping(value = \"/{uuid}\", method = RequestMethod.DELETE)"));
        assertThat(list.get(i++), is("    public void deleteFooBar(@PathVariable String uuid) {"));
        assertThat(list.get(i++), is("        service.delete(uuid);"));
        assertThat(list.get(i++), is("    }"));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is(""));
        assertThat(list.get(i++), is("}"));
    }

    @Test
    public void testApplyTemplate_enableFilter() throws Exception {
        generator.getExtraParams().put("enableFilter", true);
        List<String> list = generator.applyTemplate(table);

        AbstractGenerator.dump(list);
        int i = 32;
        assertThat(list.get(i++), is("    @RequestMapping(method = RequestMethod.GET)"));
        assertThat(list.get(i++), is("    public Page<FooBar> listFooBars(@Valid FooBarQuery query) {"));
        assertThat(list.get(i++), is("        setFilter(query);"));
        assertThat(list.get(i++), is("        return service.findAll(query);"));
        assertThat(list.get(i++), is("    }"));

    }
}