package com.dxn.model.extend;

import com.dxn.model.entity.DatacolumninfoEntity;
import com.dxn.system.Framework;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

public class DataColumnName {
    /** 排序号. */
    private Integer sortNO;

    public void setSortNO(Integer sortNO) {
        this.sortNO = sortNO;
    }

    public Integer getSortNO() {
        return this.sortNO;
    }

    /** 显示名称. */
    private String columnDisplayName;

    public void setColumnDisplayName(String columnDisplayName) {
        this.columnDisplayName = columnDisplayName;
    }

    public String getColumnDisplayName() {
        return this.columnDisplayName;
    }

    /** 字段名称. */
    private String columnName;

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return this.columnName;
    }

    /**
     * 是否填充
     */
    private String isFill;

    public void setIsFill(String isFill) {
        this.isFill = isFill;
    }

    public String getIsFill() {
        return isFill;
    }

    /**
     * 是否查询
     */
    private String isQuery;

    public void setIsQuery(String isQuery) {
        this.isQuery = isQuery;
    }

    public String getIsQuery() {
        return isQuery;
    }

    /**
     * 是否是本期
     */
    public String isCurrent;

    public String getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(String isCurrent) {
        this.isCurrent = isCurrent;
    }
}
