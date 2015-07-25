package com.kentchiu.jpa;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.kentchiu.jpa.domain.Table;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DetailControllerGenerator extends AbstractGenerator {


    public Optional<Path> export(Table table) {
        return exportToFile(table, applyTemplate(table));
    }

    List<String> applyTemplate(Table table) {
        Map<String, Object> context = getBaseContext(table);

        context.put("masterName", "deviceKind");
        context.put("detailName", "detection");
        context.put("masterDomain", "DeviceKind");
        context.put("detailDomain", "DeviceDetection");

        Preconditions.checkNotNull(context.get("masterName"), "masterName is mandatory");
        Preconditions.checkNotNull(context.get("detailName"), "detailName is mandatory");
        Preconditions.checkNotNull(context.get("masterDomain"), "masterDomain is mandatory");
        Preconditions.checkNotNull(context.get("detailDomain"), "detailDomain is mandatory");

        String domain = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, transformer.getDomainName(table.getName()));
        context.put("domain", domain);
        return applyTemplate("detail_controller.mustache", context);
    }

    protected String getClassName(Table table) {
        return transformer.getDomainName(table.getName()) + "DetailController";
    }

    protected String getPackageName(Table table) {
        return transformer.getTopPackage(table.getName()) + "." + transformer.getModuleName(table.getName()) + ".web";
    }
}
