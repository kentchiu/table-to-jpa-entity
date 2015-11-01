package com.kentchiu.jpa.generator;

public class DetailConfig {

    private String masterName;
    private String detailName;
    private String detailTable;
    private String masterTable;

    public DetailConfig(String masterName, String detailName, String masterTable, String detailTable) {
        this.masterName = masterName;
        this.detailName = detailName;
        this.detailTable = detailTable;
        this.masterTable = masterTable;
    }

    public DetailConfig(String masterName, String detailName) {
        this.masterName = masterName;
        this.detailName = detailName;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getDetailTable() {
        return detailTable;
    }

    public void setDetailTable(String detailTable) {
        this.detailTable = detailTable;
    }

    public String getMasterTable() {
        return masterTable;
    }

    public void setMasterTable(String masterTable) {
        this.masterTable = masterTable;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DetailConfig{");
        sb.append("masterName='").append(masterName).append('\'');
        sb.append(", detailName='").append(detailName).append('\'');
        sb.append(", detailTable='").append(detailTable).append('\'');
        sb.append(", masterTable='").append(masterTable).append('\'');
        sb.append('}');
        return sb.toString();
    }

    String getMasterDomain(Transformer transformer) {
        return transformer.getDomainName(getMasterTable());
    }

    String getDetailDomain(Transformer transformer) {
        return transformer.getDomainName(getDetailTable());
    }

}
