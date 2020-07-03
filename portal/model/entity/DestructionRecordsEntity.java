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
public class DestructionRecordsEntity extends EntityBase<com.dxn.model.extend.DestructionRecords> {

    /** 销毁理由. */
    @Column(name = "content", nullable = false, columnDefinition = "nvarchar(1000)")
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    /** 摧毁人所在部门. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "deptId")
    private com.dxn.model.extend.Department deptId;

    public void setDeptId(com.dxn.model.extend.Department deptId) {
        this.deptId = deptId;
    }

    public com.dxn.model.extend.Department getDeptId() {
        if (deptId != null) deptId.initialization();
        return this.deptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getDeptIdReadOnly() {
        return this.deptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department readDeptId() {
        return this.deptId;
    }

    public void setDept(long id) throws Exception {
        this.setDeptId(com.dxn.model.extend.Department.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getDept() {
        return this.getDeptId();
    }

    @JsonIgnore
    public boolean getDeptIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.deptId);
    }

    /** 摧毁人. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId")
    private com.dxn.model.extend.User userId;

    public void setUserId(com.dxn.model.extend.User userId) {
        this.userId = userId;
    }

    public com.dxn.model.extend.User getUserId() {
        if (userId != null) userId.initialization();
        return this.userId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getUserIdReadOnly() {
        return this.userId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readUserId() {
        return this.userId;
    }

    public void setUser(long id) throws Exception {
        this.setUserId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getUser() {
        return this.getUserId();
    }

    @JsonIgnore
    public boolean getUserIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.userId);
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

    /** 并行状态. */
    @Column(name = "docStateAll", columnDefinition = "nvarchar(1000)")
    private String docStateAll;

    public void setDocStateAll(String docStateAll) {
        this.docStateAll = docStateAll;
    }

    public String getDocStateAll() {
        return this.docStateAll;
    }

    /** 运营状态. */
    @Column(name = "docState")
    private Integer docState;

    public void setDocState(Integer docState) {
        this.docState = docState;
    }

    public Integer getDocState() {
        return this.docState;
    }

    /** 节点名称. */
    @Column(name = "taskName", columnDefinition = "nvarchar(1000)")
    private String taskName;

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return this.taskName;
    }

    /** 发起人. */
    @Column(name = "submitById")
    private Long submitById;

    public void setSubmitById(Long submitByid) {
        this.submitById = submitByid;
    }

    public Long getSubmitById() {
        return this.submitById;
    }

    public void setSubmitBy(com.dxn.model.extend.User entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setSubmitById(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.User getSubmitBy() throws Exception {
        if(Framework.isNullOrEmpty(this.getSubmitById())) return null;
        return com.dxn.model.extend.User.findById(this.getSubmitById());
    }

    /** 审批人. */
    @Column(name = "approvedById")
    private Long approvedById;

    public void setApprovedById(Long approvedByid) {
        this.approvedById = approvedByid;
    }

    public Long getApprovedById() {
        return this.approvedById;
    }

    public void setApprovedBy(com.dxn.model.extend.User entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setApprovedById(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.User getApprovedBy() throws Exception {
        if(Framework.isNullOrEmpty(this.getApprovedById())) return null;
        return com.dxn.model.extend.User.findById(this.getApprovedById());
    }

    /** 发起时间. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "submitOn")
    private LocalDateTime submitOn;

    public void setSubmitOn(LocalDateTime submitOn) {
        this.submitOn = submitOn;
    }

    public LocalDateTime getSubmitOn() {
        return this.submitOn;
    }

    /** 审批时间. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "approvedOn")
    private LocalDateTime approvedOn;

    public void setApprovedOn(LocalDateTime approvedOn) {
        this.approvedOn = approvedOn;
    }

    public LocalDateTime getApprovedOn() {
        return this.approvedOn;
    }

    /** 销毁详情列表. */
    @OneToMany(mappedBy = "destructionRecordsId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.DestructionDetail> destructionDetails = new java.util.ArrayList<>();

    public void setDestructionDetails(java.util.List<com.dxn.model.extend.DestructionDetail> destructionDetails) {
        this.destructionDetails = destructionDetails;
    }

    public java.util.List<com.dxn.model.extend.DestructionDetail> getDestructionDetails() {
        if (this.destructionDetails != null) {
           for (com.dxn.model.extend.DestructionDetail item : this.destructionDetails) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.destructionDetails;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DestructionDetail> readDestructionDetails() {
        return this.destructionDetails;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DestructionDetail> getDestructionDetailsReadOnly() {
        return this.destructionDetails;
    }

    /** 销毁记录附件表列表. */
    @OneToMany(mappedBy = "destructionRecordsId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.DestructionRecordsAttachment> destructionRecordsAttachments = new java.util.ArrayList<>();

    public void setDestructionRecordsAttachments(java.util.List<com.dxn.model.extend.DestructionRecordsAttachment> destructionRecordsAttachments) {
        this.destructionRecordsAttachments = destructionRecordsAttachments;
    }

    public java.util.List<com.dxn.model.extend.DestructionRecordsAttachment> getDestructionRecordsAttachments() {
        if (this.destructionRecordsAttachments != null) {
           for (com.dxn.model.extend.DestructionRecordsAttachment item : this.destructionRecordsAttachments) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.destructionRecordsAttachments;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DestructionRecordsAttachment> readDestructionRecordsAttachments() {
        return this.destructionRecordsAttachments;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DestructionRecordsAttachment> getDestructionRecordsAttachmentsReadOnly() {
        return this.destructionRecordsAttachments;
    }

    @JsonIgnore
    public static com.dxn.model.extend.DestructionRecords findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DestructionRecords.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DestructionRecords findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DestructionRecords.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DestructionRecords findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DestructionRecords.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DestructionRecords findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DestructionRecords.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DestructionRecords createNew() throws Exception {
        com.dxn.model.extend.DestructionRecords entity = new com.dxn.model.extend.DestructionRecords();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DestructionRecords> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DestructionRecords.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_DestructionRecords";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "销毁记录";
    }

}
