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
public class BorrowChildEntity extends EntityBase<com.dxn.model.extend.BorrowChild> {

    /** 部门Id. */
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
    @Column(name = "borrowDays")
    private Integer borrowDays;

    public void setBorrowDays(Integer borrowDays) {
        this.borrowDays = borrowDays;
    }

    public Integer getBorrowDays() {
        return this.borrowDays;
    }

    /** 实际归还日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "actualReturnDate")
    private LocalDateTime actualReturnDate;

    public void setActualReturnDate(LocalDateTime actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public LocalDateTime getActualReturnDate() {
        return this.actualReturnDate;
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

    /** 纸质底稿借阅盒数. */
    @Column(name = "borrowNumbers")
    private Integer borrowNumbers;

    public void setBorrowNumbers(Integer borrowNumbers) {
        this.borrowNumbers = borrowNumbers;
    }

    public Integer getBorrowNumbers() {
        return this.borrowNumbers;
    }

    /** 纸质底稿. */
    @Column(name = "paperDraft")
    private Boolean paperDraft;

    public void setPaperDraft(Boolean paperDraft) {
        this.paperDraft = paperDraft;
    }

    public Boolean getPaperDraft() {
        return this.paperDraft;
    }

    /** 电子底稿. */
    @Column(name = "electronicDraft")
    private Boolean electronicDraft;

    public void setElectronicDraft(Boolean electronicDraft) {
        this.electronicDraft = electronicDraft;
    }

    public Boolean getElectronicDraft() {
        return this.electronicDraft;
    }

    /** 底稿归档Id. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manuScirptItemArchiveId")
    private com.dxn.model.extend.ManuScirptItemArchive manuScirptItemArchiveId;

    public void setManuScirptItemArchiveId(com.dxn.model.extend.ManuScirptItemArchive manuScirptItemArchiveId) {
        this.manuScirptItemArchiveId = manuScirptItemArchiveId;
    }

    public com.dxn.model.extend.ManuScirptItemArchive getManuScirptItemArchiveId() {
        if (manuScirptItemArchiveId != null) manuScirptItemArchiveId.initialization();
        return this.manuScirptItemArchiveId;
    }

    @JsonIgnore
    public com.dxn.model.extend.ManuScirptItemArchive getManuScirptItemArchiveIdReadOnly() {
        return this.manuScirptItemArchiveId;
    }

    @JsonIgnore
    public com.dxn.model.extend.ManuScirptItemArchive readManuScirptItemArchiveId() {
        return this.manuScirptItemArchiveId;
    }

    public void setManuScirptItemArchive(long id) throws Exception {
        this.setManuScirptItemArchiveId(com.dxn.model.extend.ManuScirptItemArchive.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.ManuScirptItemArchive getManuScirptItemArchive() {
        return this.getManuScirptItemArchiveId();
    }

    @JsonIgnore
    public boolean getManuScirptItemArchiveIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.manuScirptItemArchiveId);
    }

    /** 借阅申请主表. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "borrowMasterId")
    private com.dxn.model.extend.BorrowMaster borrowMasterId;

    public void setBorrowMasterId(com.dxn.model.extend.BorrowMaster borrowMasterId) {
        this.borrowMasterId = borrowMasterId;
    }

    public com.dxn.model.extend.BorrowMaster getBorrowMasterId() {
        if (borrowMasterId != null) borrowMasterId.initialization();
        return this.borrowMasterId;
    }

    @JsonIgnore
    public com.dxn.model.extend.BorrowMaster getBorrowMasterIdReadOnly() {
        return this.borrowMasterId;
    }

    @JsonIgnore
    public com.dxn.model.extend.BorrowMaster readBorrowMasterId() {
        return this.borrowMasterId;
    }

    public void setBorrowMaster(long id) throws Exception {
        this.setBorrowMasterId(com.dxn.model.extend.BorrowMaster.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.BorrowMaster getBorrowMaster() {
        return this.getBorrowMasterId();
    }

    @JsonIgnore
    public boolean getBorrowMasterIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.borrowMasterId);
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

    /** 借阅申请孙表列表. */
    @OneToMany(mappedBy = "borrowChildId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.BorrowGrandson> borrowGrandsons = new java.util.ArrayList<>();

    public void setBorrowGrandsons(java.util.List<com.dxn.model.extend.BorrowGrandson> borrowGrandsons) {
        this.borrowGrandsons = borrowGrandsons;
    }

    public java.util.List<com.dxn.model.extend.BorrowGrandson> getBorrowGrandsons() {
        if (this.borrowGrandsons != null) {
           for (com.dxn.model.extend.BorrowGrandson item : this.borrowGrandsons) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.borrowGrandsons;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BorrowGrandson> readBorrowGrandsons() {
        return this.borrowGrandsons;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BorrowGrandson> getBorrowGrandsonsReadOnly() {
        return this.borrowGrandsons;
    }

    @JsonIgnore
    public static com.dxn.model.extend.BorrowChild findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.BorrowChild.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BorrowChild findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.BorrowChild.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BorrowChild findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.BorrowChild.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BorrowChild findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.BorrowChild.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BorrowChild createNew() throws Exception {
        com.dxn.model.extend.BorrowChild entity = new com.dxn.model.extend.BorrowChild();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.BorrowChild> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.BorrowChild.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "prj_BorrowChild";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "借阅申请子表";
    }

}
