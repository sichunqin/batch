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
public class BorrowMasterEntity extends EntityBase<com.dxn.model.extend.BorrowMaster> {

    /** 工作流用. */
    @Column(name = "internalAndExternalUse")
    private Integer internalAndExternalUse;

    public void setInternalAndExternalUse(Integer internalAndExternalUse) {
        this.internalAndExternalUse = internalAndExternalUse;
    }

    public Integer getInternalAndExternalUse() {
        return this.internalAndExternalUse;
    }

    /** 是否外部单位使用. */
    @Column(name = "isExternal")
    private Boolean isExternal;

    public void setIsExternal(Boolean isExternal) {
        this.isExternal = isExternal;
    }

    public Boolean getIsExternal() {
        return this.isExternal;
    }

    /** 流程部门Id. */
    @ManyToOne(fetch = FetchType.LAZY)
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

    /** 预计归还日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "expectedReturnDate")
    private LocalDateTime expectedReturnDate;

    public void setExpectedReturnDate(LocalDateTime expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public LocalDateTime getExpectedReturnDate() {
        return this.expectedReturnDate;
    }

    /** 主项目. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "projectId")
    private com.dxn.model.extend.Project projectId;

    public void setProjectId(com.dxn.model.extend.Project projectId) {
        this.projectId = projectId;
    }

    public com.dxn.model.extend.Project getProjectId() {
        if (projectId != null) projectId.initialization();
        return this.projectId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Project getProjectIdReadOnly() {
        return this.projectId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Project readProjectId() {
        return this.projectId;
    }

    public void setProject(long id) throws Exception {
        this.setProjectId(com.dxn.model.extend.Project.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Project getProject() {
        return this.getProjectId();
    }

    @JsonIgnore
    public boolean getProjectIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.projectId);
    }

    /** 借阅事由. */
    @Column(name = "borrowReason", columnDefinition = "nvarchar(2000)")
    private String borrowReason;

    public void setBorrowReason(String borrowReason) {
        this.borrowReason = borrowReason;
    }

    public String getBorrowReason() {
        return this.borrowReason;
    }

    /** 借阅天数. */
    @Column(name = "borrowDays", nullable = false)
    private int borrowDays;

    public void setBorrowDays(int borrowDays) {
        this.borrowDays = borrowDays;
    }

    public int getBorrowDays() {
        return this.borrowDays;
    }

    /** 借阅部门. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "borrowDeptId")
    private com.dxn.model.extend.Department borrowDeptId;

    public void setBorrowDeptId(com.dxn.model.extend.Department borrowDeptId) {
        this.borrowDeptId = borrowDeptId;
    }

    public com.dxn.model.extend.Department getBorrowDeptId() {
        if (borrowDeptId != null) borrowDeptId.initialization();
        return this.borrowDeptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getBorrowDeptIdReadOnly() {
        return this.borrowDeptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department readBorrowDeptId() {
        return this.borrowDeptId;
    }

    public void setBorrowDept(long id) throws Exception {
        this.setBorrowDeptId(com.dxn.model.extend.Department.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getBorrowDept() {
        return this.getBorrowDeptId();
    }

    @JsonIgnore
    public boolean getBorrowDeptIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.borrowDeptId);
    }

    /** 借阅人. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "borrowerId")
    private com.dxn.model.extend.User borrowerId;

    public void setBorrowerId(com.dxn.model.extend.User borrowerId) {
        this.borrowerId = borrowerId;
    }

    public com.dxn.model.extend.User getBorrowerId() {
        if (borrowerId != null) borrowerId.initialization();
        return this.borrowerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getBorrowerIdReadOnly() {
        return this.borrowerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readBorrowerId() {
        return this.borrowerId;
    }

    public void setBorrower(long id) throws Exception {
        this.setBorrowerId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getBorrower() {
        return this.getBorrowerId();
    }

    @JsonIgnore
    public boolean getBorrowerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.borrowerId);
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

    /** 借阅申请子表列表. */
    @OneToMany(mappedBy = "borrowMasterId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.BorrowChild> borrowChilds = new java.util.ArrayList<>();

    public void setBorrowChilds(java.util.List<com.dxn.model.extend.BorrowChild> borrowChilds) {
        this.borrowChilds = borrowChilds;
    }

    public java.util.List<com.dxn.model.extend.BorrowChild> getBorrowChilds() {
        if (this.borrowChilds != null) {
           for (com.dxn.model.extend.BorrowChild item : this.borrowChilds) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.borrowChilds;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BorrowChild> readBorrowChilds() {
        return this.borrowChilds;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BorrowChild> getBorrowChildsReadOnly() {
        return this.borrowChilds;
    }

    @JsonIgnore
    public static com.dxn.model.extend.BorrowMaster findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.BorrowMaster.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BorrowMaster findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.BorrowMaster.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BorrowMaster findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.BorrowMaster.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BorrowMaster findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.BorrowMaster.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BorrowMaster createNew() throws Exception {
        com.dxn.model.extend.BorrowMaster entity = new com.dxn.model.extend.BorrowMaster();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.BorrowMaster> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.BorrowMaster.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "prj_BorrowMaster";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "借阅申请主表";
    }

}
