package com.kentchiu.jpa.generator;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.kentchiu.jpa.domain.Column;
import com.kentchiu.jpa.domain.Columns;
import com.kentchiu.jpa.domain.Table;
import com.kentchiu.jpa.domain.Tables;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class QueryGeneratorTest extends DomainObjectGeneratorTest {

    @Before
    public void setUp() throws Exception {
        generator = new EntityGenerator(new Transformer(), new Config(Type.QUERY));
        generator.setProjectHome(Files.createTempDirectory("java"));
    }


    @Test
    public void testGenerate() throws Exception {
        generator.transformer.setTableNameMapper(ImmutableMap.of("MY_TABLE_1", "com.foobar.module.domain.MyTest"));
        generator.export(Tables.table1());
        assertThat(Files.exists(generator.getJavaSourceHome().resolve("com/foobar/module/service/query/MyTestQuery.java")), is(true));
    }

    @Test
    public void testTableMapping() throws Exception {
        generator.transformer.setTableNameMapper(ImmutableMap.of("MY_TABLE_1", "com.kentchiu.jpa.domain.FooBar"));
        List<String> lines = generator.exportTable(Tables.table1());
        assertThat(lines, hasItem("package com.kentchiu.jpa.service.query;"));
        assertThat(lines, hasItem("/**"));
        assertThat(lines, hasItem(" * a table comment"));
        assertThat(lines, hasItem(" */"));
        assertThat(lines, hasItem("public class FooBarQuery extends PageableQuery<FooBar> {"));
    }

    @Test
    public void testColumnMapping() throws Exception {
        Map<String, String> mapper = new HashMap<>();
        mapper.put("MY_TABLE", "com.kentchiu.jpa.FooBar");
        generator.transformer.setTableNameMapper(mapper);

        Column column = Columns.createStringColumn("prop", "comment", true);
        column.setReferenceTable("MY_TABLE");
        List<String> lines = generator.buildProperty(column);


        int i = 0;
        assertThat(lines.get(i++), is("//    private String fooBarUuid;"));

        i = 2;
        assertThat(lines.get(i++), is("//    @AttributeInfo(description = \"comment\")"));
        assertThat(lines.get(i++), is("//    public String getFooBarUuid() {"));
        assertThat(lines.get(i++), is("//        return fooBarUuid;"));
        assertThat(lines.get(i++), is("//    }"));

        i = 7;
        assertThat(lines.get(i++), is("//    public void setFooBarUuid(String fooBarUuid) {"));
        assertThat(lines.get(i++), is("//        this.fooBarUuid = fooBarUuid;"));
        assertThat(lines.get(i++), is("//    }"));
    }


    @Test
    public void testProperty_BigDecimal() throws Exception {
        List<String> lines = generator.buildProperty(Columns.bigDecimalColumn());

        int i = 0;
        // field
        assertThat(lines.get(i++), is("//    private BigDecimal bigDecimalProperty;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("//    @AttributeInfo(description = \"this is a big decimal property\")"));
        assertThat(lines.get(i++), is("//    public BigDecimal getBigDecimalProperty() {"));
        assertThat(lines.get(i++), is("//        return bigDecimalProperty;"));
        assertThat(lines.get(i++), is("//    }"));

        i = 7;
        // setter
        assertThat(lines.get(i++), is("//    public void setBigDecimalProperty(BigDecimal bigDecimalProperty) {"));
        assertThat(lines.get(i++), is("//        this.bigDecimalProperty = bigDecimalProperty;"));
        assertThat(lines.get(i++), is("//    }"));
    }


    @Test
    public void testProperty_date() throws Exception {
        List<String> lines = generator.buildProperty(Columns.dateColumn());

        int i = 0;
        // field
        assertThat(lines.get(i++), is("//    private Date dateProperty;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("//    @DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")"));
        assertThat(lines.get(i++), is("//    @AttributeInfo(description = \"this is a date property\", format = \"yyyy-MM-dd HH:mm:ss\")"));
        assertThat(lines.get(i++), is("//    public Date getDateProperty() {"));
        assertThat(lines.get(i++), is("//        return dateProperty;"));
        assertThat(lines.get(i++), is("//    }"));

        i = 8;
        // setter
        assertThat(lines.get(i++), is("//    public void setDateProperty(Date dateProperty) {"));
        assertThat(lines.get(i++), is("//        this.dateProperty = dateProperty;"));
        assertThat(lines.get(i++), is("//    }"));
    }


    @Test
    public void testProperty_string() throws Exception {
        List<String> lines = generator.buildProperty(Columns.stringColumn());

        int i = 0;
        // field
        assertThat(lines.get(i++), is("//    private String column1;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("//    @AttributeInfo(description = \"column comment\")"));
        assertThat(lines.get(i++), is("//    public String getColumn1() {"));
        assertThat(lines.get(i++), is("//        return column1;"));
        assertThat(lines.get(i++), is("//    }"));

        i = 7;
        // setter
        assertThat(lines.get(i++), is("//    public void setColumn1(String column1) {"));
        assertThat(lines.get(i++), is("//        this.column1 = column1;"));
        assertThat(lines.get(i++), is("//    }"));
    }

    @Test
    public void testProperty_options() throws Exception {
        Column column = Columns.stringColumn();
        column.getOptions().put("Y", "foo");
        column.getOptions().put("N", "bar");

        List<String> lines = generator.buildProperty(column);

        int i = 0;
        // field
        assertThat(lines.get(i++), is("//    private String column1;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("//    @Option(value = {\"Y\", \"N\"})"));
        assertThat(lines.get(i++), is("//    @AttributeInfo(description = \"column comment\", format = \"Y=foo/N=bar\")"));
        assertThat(lines.get(i++), is("//    public String getColumn1() {"));
        assertThat(lines.get(i++), is("//        return column1;"));
        assertThat(lines.get(i++), is("//    }"));

        i = 8;
        // setter
        assertThat(lines.get(i++), is("//    public void setColumn1(String column1) {"));
        assertThat(lines.get(i++), is("//        this.column1 = column1;"));
        assertThat(lines.get(i++), is("//    }"));
    }


    @Test
    public void testProperty_with_default_value() throws Exception {
        Column column = Columns.stringColumn();
        column.setComment("column comment(default=foo)");
        List<String> lines = generator.buildProperty(column);

        int i = 0;
        // field
        assertThat(lines.get(i++), is("//    private String column1;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("//    @AttributeInfo(description = \"column comment\")"));
        assertThat(lines.get(i++), is("//    public String getColumn1() {"));
        assertThat(lines.get(i++), is("//        return column1;"));
        assertThat(lines.get(i++), is("//    }"));

        i = 7;
        // setter
        assertThat(lines.get(i++), is("//    public void setColumn1(String column1) {"));
        assertThat(lines.get(i++), is("//        this.column1 = column1;"));
        assertThat(lines.get(i++), is("//    }"));
    }

    @Test
    public void testProperty_ManyToOne() throws Exception {
        Column column = Columns.stringColumn();
        column.setNullable(true);
        column.setReferenceTable("OTHER_TABLE");
        List<String> lines = generator.buildProperty(column);

        int i = 0;
        // field
        assertThat(lines.get(i++), is("//    private String otherTableUuid;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("//    @AttributeInfo(description = \"column comment\")"));
        assertThat(lines.get(i++), is("//    public String getOtherTableUuid() {"));
        assertThat(lines.get(i++), is("//        return otherTableUuid;"));
        assertThat(lines.get(i++), is("//    }"));

        i = 7;
        // setter
        assertThat(lines.get(i++), is("//    public void setOtherTableUuid(String otherTableUuid) {"));
        assertThat(lines.get(i++), is("//        this.otherTableUuid = otherTableUuid;"));
        assertThat(lines.get(i++), is("//    }"));
    }


    @Test
    public void testProperty_boolean() throws Exception {
        List<String> lines = generator.buildProperty(Columns.booleanColumn());

        int i = 0;
        // field
        assertThat(lines.get(i++), is("//    private Boolean boolProperty;"));

        i = 2;
        // getter
        assertThat(lines.get(i++), is("//    @AttributeInfo(description = \"this is a boolean property\")"));
        assertThat(lines.get(i++), is("//    public Boolean getBoolProperty() {"));
        assertThat(lines.get(i++), is("//        return boolProperty;"));
        assertThat(lines.get(i++), is("//    }"));

        i = 7;
        // setter
        assertThat(lines.get(i++), is("//    public void setBoolProperty(Boolean boolProperty) {"));
        assertThat(lines.get(i++), is("//        this.boolProperty = boolProperty;"));
        assertThat(lines.get(i++), is("//    }"));
    }


    @Test
    public void testProperty_string_not_null() throws Exception {
        List<String> lines = generator.buildProperty(Columns.createStringColumn("FOO_BAR", "The foo bar comment", false));

        // field
        assertThat(lines.get(0), is("//    private String fooBar;"));

        // getter
        int i = 2;
        assertThat(lines.get(i++), is("//    @AttributeInfo(description = \"The foo bar comment\")"));
        assertThat(lines.get(i++), is("//    public String getFooBar() {"));
        assertThat(lines.get(i++), is("//        return fooBar;"));
        assertThat(lines.get(i++), is("//    }"));

        // setter
        i = 7;
        assertThat(lines.get(i++), is("//    public void setFooBar(String fooBar) {"));
        assertThat(lines.get(i++), is("//        this.fooBar = fooBar;"));
        assertThat(lines.get(i++), is("//    }"));
    }


    @Test
    public void testExportTable() throws Exception {
        List<String> lines = generator.exportTable(Tables.table1());

        assertThat(lines, hasItem("import com.kentchiu.spring.attribute.AttributeInfo;"));
        assertThat(lines, hasItem("import com.kentchiu.spring.base.domain.Option;"));
        assertThat(lines, hasItem("import org.hibernate.validator.constraints.*;"));
        assertThat(lines, hasItem("import javax.validation.constraints.*;"));
        assertThat(lines, hasItem("import java.util.Date;"));
        assertThat(lines, hasItem("import java.math.BigDecimal;"));
        assertThat(lines, hasItem("import org.torpedoquery.jpa.OnGoingLogicalCondition;"));
        assertThat(lines, hasItem("import org.torpedoquery.jpa.Query;"));
        assertThat(lines, hasItem("import static org.torpedoquery.jpa.Torpedo.*;"));
        assertThat(lines, hasItem("import static org.torpedoquery.jpa.Torpedo.*;"));
        assertThat(lines, hasItem("import java.util.ArrayList;"));
        assertThat(lines, hasItem("import java.util.List;"));
        assertThat(lines, hasItem("import org.apache.commons.lang3.StringUtils;"));
        assertThat(lines, hasItem("import com.kentchiu.spring.base.service.query.PageableQuery;"));


        int i = 22;
        assertThat(lines.get(i++), is("/**"));
        assertThat(lines.get(i++), is(" * a table comment"));
        assertThat(lines.get(i++), is(" */"));
        assertThat(lines.get(i++), is("public class MyTable1Query extends PageableQuery<MyTable1> {"));

        String content = Joiner.on('\n').join(lines);

        assertThat(content, containsString("//    private String myColumn11;"));
        assertThat(content, containsString("//    private String myColumn12;"));
        assertThat(content, containsString("//    private Date myColumn13;"));
        assertThat(content, containsString("    public Query<MyTable1> buildQuery() {"));
        assertThat(content, containsString("        MyTable1 from = from(MyTable1.class);"));
        assertThat(content, containsString("        List<OnGoingLogicalCondition> conditions = new ArrayList<>();"));
        assertThat(content, not(containsString("        conditions.addAll(super.buildQuery(from));")));
        assertThat(content, containsString("        if (!conditions.isEmpty()) {"));
        assertThat(content, containsString("            where(and(conditions));"));
        assertThat(content, containsString("        }"));
        assertThat(content, containsString("        return select(from);"));

        assertThat(content, containsString("        return select(from);"));
    }


    @Test
    public void testExportTable_enableFilter() throws Exception {
        generator.getExtraParams().put("enableFilter", true);
        List<String> lines = generator.exportTable(Tables.table1());

        String content = Joiner.on('\n').join(lines);
        assertThat(content, containsString("import com.bq.i1.base.service.query.FilterQuery"));
        assertThat(content, containsString("public class MyTable1Query extends FilterQuery<MyTable1> {"));
        assertThat(content, containsString("        conditions.addAll(super.buildQuery(from));"));
    }


    @Test
    public void testAttributeInfo_with_options_and_default_value() throws Exception {
        Column column = new Column();
        column.setDescription("是否允许定制颜色");
        column.setDefaultValue("'Y'");
        Map<String, String> options = column.getOptions();
        options.put("Y", "允许");
        options.put("N", "不允许");

        assertThat(generator.buildAttributeInfo(column), is("@AttributeInfo(description = \"是否允许定制颜色\", format = \"Y=允许/N=不允许\")"));
    }

    @Test
    public void testProperty_master_uuid_should_not_be_comment() throws Exception {
        Table table = Tables.detail();
        Transformer transformer = new Transformer();
        Map<String, String> mapper = new HashMap<>();
        mapper.put("TBL_MASTER", "com.kentchiu.module.domain.Master");
        mapper.put("TBL_DETAIL", "com.kentchiu.module.domain.Detail");
        transformer.setTableNameMapper(mapper);
        DetailConfig config = new DetailConfig("master", "detail", "TBL_MASTER", "TBL_DETAIL");
        transformer.setMasterDetailMapper(ImmutableMap.of(table.getName(), config));

        generator = new EntityGenerator(transformer, new Config(Type.QUERY));
        generator.setProjectHome(Files.createTempDirectory("java"));

        List<String> lines = generator.exportTable(table);

        int i = 31;
        // FIXME tblMasterUuid should be masterUuid
        assertThat(lines.get(i++), is("      private String tblMasterUuid;"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("      @AttributeInfo(description = \"master uuid\")"));
        assertThat(lines.get(i++), is("      public String getTblMasterUuid() {"));
        assertThat(lines.get(i++), is("          return tblMasterUuid;"));
        assertThat(lines.get(i++), is("      }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("      public void setTblMasterUuid(String tblMasterUuid) {"));
        assertThat(lines.get(i++), is("          this.tblMasterUuid = tblMasterUuid;"));
        assertThat(lines.get(i++), is("      }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//    private String myColumn11;"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//    @AttributeInfo(description = \"my column 1-1 comment\")"));
        assertThat(lines.get(i++), is("//    public String getMyColumn11() {"));
        assertThat(lines.get(i++), is("//        return myColumn11;"));
        assertThat(lines.get(i++), is("//    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//    public void setMyColumn11(String myColumn11) {"));
        assertThat(lines.get(i++), is("//        this.myColumn11 = myColumn11;"));
        assertThat(lines.get(i++), is("//    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//    private String myColumn12;"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//    @AttributeInfo(description = \"my column 1-2 comment\")"));
        assertThat(lines.get(i++), is("//    public String getMyColumn12() {"));
        assertThat(lines.get(i++), is("//        return myColumn12;"));
        assertThat(lines.get(i++), is("//    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//    public void setMyColumn12(String myColumn12) {"));
        assertThat(lines.get(i++), is("//        this.myColumn12 = myColumn12;"));
        assertThat(lines.get(i++), is("//    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//    private Date myColumn13;"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//    @AttributeInfo(description = \"my column 1-3 comment\")"));
        assertThat(lines.get(i++), is("//    public Date getMyColumn13() {"));
        assertThat(lines.get(i++), is("//        return myColumn13;"));
        assertThat(lines.get(i++), is("//    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//    public void setMyColumn13(Date myColumn13) {"));
        assertThat(lines.get(i++), is("//        this.myColumn13 = myColumn13;"));
        assertThat(lines.get(i++), is("//    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    public Query<Detail> buildQuery() {"));
        assertThat(lines.get(i++), is("        Detail from = from(Detail.class);"));
        assertThat(lines.get(i++), is("        List<OnGoingLogicalCondition> conditions = new ArrayList<>();"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("        if (StringUtils.isNotBlank(masterUuid)) {"));
        assertThat(lines.get(i++), is("            conditions.add(condition(from.getMaster().getUuid()).eq(masterUuid));"));
        assertThat(lines.get(i++), is("        }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("//        if (StringUtils.isNotBlank(__ref__Uuid)) {"));
        assertThat(lines.get(i++), is("//            conditions.add(condition(from.get__Ref__().getUuid()).eq(__ref__Uuid));"));
        assertThat(lines.get(i++), is("//        }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("        if (!conditions.isEmpty()) {"));
        assertThat(lines.get(i++), is("            where(and(conditions));"));
        assertThat(lines.get(i++), is("        }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("        // sorting"));
        assertThat(lines.get(i++), is("        setDefaultSort(\"-createDate\");"));
        assertThat(lines.get(i++), is("        sorting(from);"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("        return select(from);"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("    @Override"));
        assertThat(lines.get(i++), is("    protected Function<Object> buildOrder(Object from, String property, Sort.Direction direction) {"));
        assertThat(lines.get(i++), is("        Detail domain = (Detail) from;"));
        assertThat(lines.get(i++), is("        switch (property) {"));
        assertThat(lines.get(i++), is("            case \"uuid\":"));
        assertThat(lines.get(i++), is("                return order(domain.getUuid(), direction);"));
        assertThat(lines.get(i++), is("            case \"createUserUuid\":"));
        assertThat(lines.get(i++), is("                return order(domain.getCreateUserUuid(), direction);"));
        assertThat(lines.get(i++), is("            case \"createDate\":"));
        assertThat(lines.get(i++), is("                return order(domain.getCreateDate(), direction);"));
        assertThat(lines.get(i++), is("            case \"modifierUuid\":"));
        assertThat(lines.get(i++), is("                return order(domain.getModifierUuid(), direction);"));
        assertThat(lines.get(i++), is("            case \"modifiedDate\":"));
        assertThat(lines.get(i++), is("                return order(domain.getModifiedDate(), direction);"));
        assertThat(lines.get(i++), is("            case \"tblMasterUuid\":"));
        assertThat(lines.get(i++), is("                return order(domain.getTblMasterUuid(), direction);"));
        assertThat(lines.get(i++), is("            case \"myColumn11\":"));
        assertThat(lines.get(i++), is("                return order(domain.getMyColumn11(), direction);"));
        assertThat(lines.get(i++), is("            case \"myColumn12\":"));
        assertThat(lines.get(i++), is("                return order(domain.getMyColumn12(), direction);"));
        assertThat(lines.get(i++), is("            case \"myColumn13\":"));
        assertThat(lines.get(i++), is("                return order(domain.getMyColumn13(), direction);"));
        assertThat(lines.get(i++), is("            default:"));
        assertThat(lines.get(i++), is("                return null;"));
        assertThat(lines.get(i++), is("        }"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
        assertThat(lines.get(i++), is("}"));
    }



    @Test
    public void testBuildOrder() throws Exception {
        List<String> lines = generator.exportTable(Tables.table1());

        int buildQueryStartLine = 65;
        int i = buildQueryStartLine + 14;
        assertThat(lines.get(i++), is("        // sorting"));
        assertThat(lines.get(i++), is("        setDefaultSort(\"-createDate\");"));
        assertThat(lines.get(i++), is("        sorting(from);"));


        int buildOrderStartLine = 86;
        i = buildOrderStartLine;

        assertThat(lines.get(i++), is("    @Override"));
        assertThat(lines.get(i++), is("    protected Function<Object> buildOrder(Object from, String property, Sort.Direction direction) {"));
        assertThat(lines.get(i++), is("        MyTable1 domain = (MyTable1) from;"));
        assertThat(lines.get(i++), is("        switch (property) {"));
        assertThat(lines.get(i++), is("            case \"uuid\":"));
        assertThat(lines.get(i++), is("                return order(domain.getUuid(), direction);"));
        assertThat(lines.get(i++), is("            case \"createUserUuid\":"));
        assertThat(lines.get(i++), is("                return order(domain.getCreateUserUuid(), direction);"));
        assertThat(lines.get(i++), is("            case \"createDate\":"));
        assertThat(lines.get(i++), is("                return order(domain.getCreateDate(), direction);"));
        assertThat(lines.get(i++), is("            case \"modifierUuid\":"));
        assertThat(lines.get(i++), is("                return order(domain.getModifierUuid(), direction);"));
        assertThat(lines.get(i++), is("            case \"modifiedDate\":"));
        assertThat(lines.get(i++), is("                return order(domain.getModifiedDate(), direction);"));
        assertThat(lines.get(i++), is("            case \"myColumn11\":"));
        assertThat(lines.get(i++), is("                return order(domain.getMyColumn11(), direction);"));
        assertThat(lines.get(i++), is("            case \"myColumn12\":"));
        assertThat(lines.get(i++), is("                return order(domain.getMyColumn12(), direction);"));
        assertThat(lines.get(i++), is("            case \"myColumn13\":"));
        assertThat(lines.get(i++), is("                return order(domain.getMyColumn13(), direction);"));
        assertThat(lines.get(i++), is("            default:"));
        assertThat(lines.get(i++), is("                return null;"));
        assertThat(lines.get(i++), is("        }"));
        assertThat(lines.get(i++), is("    }"));
        assertThat(lines.get(i++), is(""));
    }

    @Test
    public void testBuildOrder_uuid() throws Exception {
        List<String> lines = generator.exportTable(Tables.detail());

        assertThat(lines.get(112), is("            case \"tblMasterUuid\":"));
        assertThat(lines.get(113), is("                return order(domain.getTblMasterUuid(), direction);"));
    }

}
