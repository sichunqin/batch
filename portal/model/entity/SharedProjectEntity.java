package com.dxn.model.entity;

import com.dxn.entity.EntityBase;
import com.dxn.system.Framework;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.dxn.system.typeFinder.TypeFinderHelper;
import org.springframework.format.annotation.DateTimeFormat;
import com.dxn.system.context.RepositoryContext;
import org.jinq.jpa.JPAJinqStream;
import javax.persistence.*;
import java.time.*;
import java.math.BigDecimal;

@MappedSuperclass
public class SharedProjectEntity extends EntityBase<com.dxn.model.extend.SharedProject> {

    /** 父Id. */
    @Column(name = "pParentID", columnDefinition = "nvarchar(100)")
    private String pParentID;

    public void setPParentID(String pParentID) {
        this.pParentID = pParentID;
    }

    public String getPParentID() {
        return this.pParentID;
    }

    /** 客户端id. */
    @Column(name = "clientGuid", nullable = false, columnDefinition = "nvarchar(100)")
    private String clientGuid;

    public void setClientGuid(String clientGuid) {
        this.clientGuid = clientGuid;
    }

    public String getClientGuid() {
        return this.clientGuid;
    }

    /** 生成文件名称. */
    @Column(name = "fileNewName", columnDefinition = "nvarchar(200)")
    private String fileNewName;

    public void setFileNewName(String fileNewName) {
        this.fileNewName = fileNewName;
    }

    public String getFileNewName() {
        return this.fileNewName;
    }

    /** 原文件名称. */
    @Column(name = "fileSourceName", columnDefinition = "nvarchar(200)")
    private String fileSourceName;

    public void setFileSourceName(String fileSourceName) {
        this.fileSourceName = fileSourceName;
    }

    public String getFileSourceName() {
        return this.fileSourceName;
    }

    /** 是否组. */
    @Column(name = "isGroup")
    private Integer isGroup;

    public void setIsGroup(Integer isGroup) {
        this.isGroup = isGroup;
    }

    public Integer getIsGroup() {
        return this.isGroup;
    }

    /** 审计执行名称. */
    @Column(name = "showName", columnDefinition = "nvarchar(300)")
    private String showName;

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowName() {
        return this.showName;
    }

    /** 是否存在文件. */
    @Column(name = "isExist")
    private Integer isExist;

    public void setIsExist(Integer isExist) {
        this.isExist = isExist;
    }

    public Integer getIsExist() {
        return this.isExist;
    }

    /** 是否手动添加. */
    @Column(name = "isManuallyAdd")
    private Integer isManuallyAdd;

    public void setIsManuallyAdd(Integer isManuallyAdd) {
        this.isManuallyAdd = isManuallyAdd;
    }

    public Integer getIsManuallyAdd() {
        return this.isManuallyAdd;
    }

    /** 文件路径. */
    @Column(name = "filePath", columnDefinition = "nvarchar(500)")
    private String filePath;

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return this.filePath;
    }

    /** 编号. */
    @Column(name = "sortNo")
    private Integer sortNo;

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public Integer getSortNo() {
        return this.sortNo;
    }

    /** 用户编号. */
    @Column(name = "jobNumber", nullable = false, columnDefinition = "nvarchar(100)")
    private String jobNumber;

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getJobNumber() {
        return this.jobNumber;
    }

    /** 用户名称. */
    @Column(name = "fullName", nullable = false, columnDefinition = "nvarchar(36)")
    private String fullName;

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return this.fullName;
    }

    /** 作业项目ID. */
    @Column(name = "auditworkProjectID", nullable = false, columnDefinition = "nvarchar(36)")
    private String auditworkProjectID;

    public void setAuditworkProjectID(String auditworkProjectID) {
        this.auditworkProjectID = auditworkProjectID;
    }

    public String getAuditworkProjectID() {
        return this.auditworkProjectID;
    }

    /** 管理项目ID. */
    @Column(name = "managerProjectID", nullable = false, columnDefinition = "nvarchar(36)")
    private String managerProjectID;

    public void setManagerProjectID(String managerProjectID) {
        this.managerProjectID = managerProjectID;
    }

    public String getManagerProjectID() {
        return this.managerProjectID;
    }

    public void setCreatedBy(com.dxn.model.extend.User entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setCreatedById(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.User getCreatedBy() throws Exception {
        if(Framework.isNullOrEmpty(this.getCreatedById())) return null;
        return com.dxn.model.extend.User.findById(this.getCreatedById());
    }

    public void setModifiedBy(com.dxn.model.extend.User entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setModifiedById(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.User getModifiedBy() throws Exception {
        if(Framework.isNullOrEmpty(this.getModifiedById())) return null;
        return com.dxn.model.extend.User.findById(this.getModifiedById());
    }

    @JsonIgnore
    public static com.dxn.model.extend.SharedProject findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.SharedProject.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SharedProject findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.SharedProject.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SharedProject findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.SharedProject.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SharedProject findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.SharedProject.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SharedProject createNew() throws Exception {
        com.dxn.model.extend.SharedProject entity = new com.dxn.model.extend.SharedProject();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.SharedProject> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.SharedProject.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "HR";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_SharedProject";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "共享项目";
    }

}
