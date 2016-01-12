package com.kentchiu.jpa.generator;

import com.google.common.collect.ImmutableMap;
import com.kentchiu.jpa.domain.Table;
import com.kentchiu.jpa.domain.Tables;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

public class AbstractGeneratorTest {


    @Test
    public void testContext() throws Exception {

        Map<String, Object> ctx = getContext();

        Assert.assertThat(ctx.get("topPackage"), is("com.kentchiu"));
        Assert.assertThat(ctx.get("moduleName"), is("module"));

        Assert.assertThat(ctx.get("Domain"), is("MyDetailDomain"));
        Assert.assertThat(ctx.get("MasterDomain"), is("MyMasterDomain"));
        Assert.assertThat(ctx.get("DetailDomain"), is("MyDetailDomain"));
        Assert.assertThat(ctx.get("MasterName"), is("Parent"));
        Assert.assertThat(ctx.get("DetailName"), is("Child"));


        Assert.assertThat(ctx.get("domain"), is("myDetailDomain"));
        Assert.assertThat(ctx.get("masterDomain"), is("myMasterDomain"));
        Assert.assertThat(ctx.get("detailDomain"), is("myDetailDomain"));
        Assert.assertThat(ctx.get("detailName"), is("child"));
        Assert.assertThat(ctx.get("masterName"), is("parent"));


        Assert.assertThat(ctx.get("DomainPlural"), is("MyDetailDomains"));
        Assert.assertThat(ctx.get("MasterDomainPlural"), is("MyMasterDomains"));
        Assert.assertThat(ctx.get("DetailDomainPlural"), is("MyDetailDomains"));
        Assert.assertThat(ctx.get("MasterNamePlural"), is("Parents"));
        Assert.assertThat(ctx.get("DetailNamePlural"), is("Children"));

        Assert.assertThat(ctx.get("domainPlural"), is("myDetailDomains"));
        Assert.assertThat(ctx.get("masterDomainPlural"), is("MyMasterDomains"));
        Assert.assertThat(ctx.get("detailDomainPlural"), is("MyDetailDomains"));
        Assert.assertThat(ctx.get("masterNamePlural"), is("parents"));
        Assert.assertThat(ctx.get("detailNamePlural"), is("children"));
    }

    private Map<String, Object> getContext() {
        Table table = Tables.detail();
        Transformer transformer = new Transformer();
        Map<String, String> mapper = new HashMap<>();
        mapper.put("TBL_MASTER", "com.kentchiu.module.domain.MyMasterDomain");
        mapper.put("TBL_DETAIL", "com.kentchiu.module.domain.MyDetailDomain");
        transformer.setTableNameMapper(mapper);
        DetailConfig config = new DetailConfig("parent", "child", "TBL_MASTER", "TBL_DETAIL");
        transformer.setMasterDetailMapper(ImmutableMap.of(table.getName(), config));

        DetailControllerGenerator generator = new DetailControllerGenerator(transformer);

        return generator.getBaseContext(table);
    }


}