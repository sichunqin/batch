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
public class AuditFilesEntity extends EntityBase<com.dxn.model.extend.AuditFiles> {

    /** 添加人姓名. */
    @Column(name = "loginUserName", columnDefinition = "nvarchar(3000)")
    private String loginUserName;

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

    public String getLoginUserName() {
        return this.loginUserName;
    }

    /** 附件ID. */
    @Column(name = "auditFilesId", columnDefinition = "nvarchar(3000)")
    private String auditFilesId;

    public void setAuditFilesId(String auditFilesId) {
        this.auditFilesId = auditFilesId;
    }

    public String getAuditFilesId() {
        return this.auditFilesId;
    }

    /** 添加人ID. */
    @Column(name = "loginUserId", columnDefinition = "nvarchar(3000)")
    private String loginUserId;

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getLoginUserId() {
        return this.loginUserId;
    }

    /** 添加时间. */
    @Column(name = "fileCreateTime", columnDefinition = "nvarchar(3000)")
    private String fileCreateTime;

    public void setFileCreateTime(String fileCreateTime) {
        this.fileCreateTime = fileCreateTime;
    }

    public String getFileCreateTime() {
        return this.fileCreateTime;
    }

    /** 路径. */
    @Column(name = "filePath", columnDefinition = "nvarchar(3000)")
    private String filePath;

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return this.filePath;
    }

    /** 文件大小. */
    @Column(name = "fileSize")
    private Integer fileSize;

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getFileSize() {
        return this.fileSize;
    }

    /** 上传批次号. */
    @Column(name = "batchNumber")
    private Integer batchNumber;

    public void setBatchNumber(Integer batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Integer getBatchNumber() {
        return this.batchNumber;
    }

    /** 文件类别. */
    @Column(name = "fileType", columnDefinition = "nvarchar(3000)")
    private String fileType;

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileType() {
        return this.fileType;
    }

    /** 文件重命名名称. */
    @Column(name = "fileNewName", columnDefinition = "nvarchar(3000)")
    private String fileNewName;

    public void setFileNewName(String fileNewName) {
        this.fileNewName = fileNewName;
    }

    public String getFileNewName() {
        return this.fileNewName;
    }

    /** 文件源名称. */
    @Column(name = "fileSourceName", columnDefinition = "nvarchar(3000)")
    private String fileSourceName;

    public void setFileSourceName(String fileSourceName) {
        this.fileSourceName = fileSourceName;
    }

    public String getFileSourceName() {
        return this.fileSourceName;
    }

    /** 组内文件排序号. */
    @Column(name = "groupFilesSortNO")
    private Integer groupFilesSortNO;

    public void setGroupFilesSortNO(Integer groupFilesSortNO) {
        this.groupFilesSortNO = groupFilesSortNO;
    }

    public Integer getGroupFilesSortNO() {
        return this.groupFilesSortNO;
    }

    /** 文件所属组ID. */
    @Column(name = "belongedToGroupID", columnDefinition = "nvarchar(3000)")
    private String belongedToGroupID;

    public void setBelongedToGroupID(String belongedToGroupID) {
        this.belongedToGroupID = belongedToGroupID;
    }

    public String getBelongedToGroupID() {
        return this.belongedToGroupID;
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

    /** 作业项目ID. */
    @Column(name = "auditworkProjectID", columnDefinition = "nvarchar(3000)")
    private String auditworkProjectID;

    public void setAuditworkProjectID(String auditworkProjectID) {
        this.auditworkProjectID = auditworkProjectID;
    }

    public String getAuditworkProjectID() {
        return this.auditworkProjectID;
    }

    /** 排序号. */
    @Column(name = "sortNO")
    private Integer sortNO;

    public void setSortNO(Integer sortNO) {
        this.sortNO = sortNO;
    }

    public Integer getSortNO() {
        return this.sortNO;
    }

    /** 是否是组. */
    @Column(name = "isGroup", columnDefinition = "nvarchar(3000)")
    private String isGroup;

    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }

    public String getIsGroup() {
        return this.isGroup;
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditFiles findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.AuditFiles.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditFiles findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.AuditFiles.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditFiles findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.AuditFiles.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditFiles findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.AuditFiles.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditFiles createNew() throws Exception {
        com.dxn.model.extend.AuditFiles entity = new com.dxn.model.extend.AuditFiles();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.AuditFiles> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.AuditFiles.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_AuditFiles";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "审计附件";
    }

}
