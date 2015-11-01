package com.kentchiu.jpa.generator;

public class ExtendDetailConfig extends DetailConfig {

    private String extendDetailName;
    private String extendDetailTable;

    public ExtendDetailConfig(String masterName, String detailName, String masterTable, String detailTable) {
        super(masterName, detailName, masterTable, detailTable);
    }

    public ExtendDetailConfig(String masterName, String detailName) {
        super(masterName, detailName);
    }

    public ExtendDetailConfig(DetailConfig detailConfig, String extendDetailName, String extendDetailTable) {
        this(detailConfig.getMasterName(), detailConfig.getDetailName(), detailConfig.getMasterTable(), detailConfig.getDetailTable());
        this.extendDetailName = extendDetailName;
        this.extendDetailTable = extendDetailTable;
    }

    public String getExtendDetailName() {
        return extendDetailName;
    }

    public void setExtendDetailName(String extendDetailName) {
        this.extendDetailName = extendDetailName;
    }

    public String getExtendDetailTable() {
        return extendDetailTable;
    }

    public void setExtendDetailTable(String extendDetailTable) {
        this.extendDetailTable = extendDetailTable;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ExtendDetailConfig{");
        sb.append("extendDetailName='").append(extendDetailName).append('\'');
        sb.append(", extendDetailTable='").append(extendDetailTable).append('\'');
        sb.append(super.toString());
        return sb.toString();
    }


    String getExtendDetailDomain(Transformer transformer) {
        return transformer.getDomainName(getExtendDetailTable());
    }


}
