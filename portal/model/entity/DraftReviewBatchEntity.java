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
public class DraftReviewBatchEntity extends EntityBase<com.dxn.model.extend.DraftReviewBatch> {

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

    /** 总所复核人. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generalReviewerId")
    private com.dxn.model.extend.User generalReviewerId;

    public void setGeneralReviewerId(com.dxn.model.extend.User generalReviewerId) {
        this.generalReviewerId = generalReviewerId;
    }

    public com.dxn.model.extend.User getGeneralReviewerId() {
        if (generalReviewerId != null) generalReviewerId.initialization();
        return this.generalReviewerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getGeneralReviewerIdReadOnly() {
        return this.generalReviewerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readGeneralReviewerId() {
        return this.generalReviewerId;
    }

    public void setGeneralReviewer(long id) throws Exception {
        this.setGeneralReviewerId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getGeneralReviewer() {
        return this.getGeneralReviewerId();
    }

    @JsonIgnore
    public boolean getGeneralReviewerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.generalReviewerId);
    }

    /** 分所复核人. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branchReviewerId")
    private com.dxn.model.extend.User branchReviewerId;

    public void setBranchReviewerId(com.dxn.model.extend.User branchReviewerId) {
        this.branchReviewerId = branchReviewerId;
    }

    public com.dxn.model.extend.User getBranchReviewerId() {
        if (branchReviewerId != null) branchReviewerId.initialization();
        return this.branchReviewerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getBranchReviewerIdReadOnly() {
        return this.branchReviewerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readBranchReviewerId() {
        return this.branchReviewerId;
    }

    public void setBranchReviewer(long id) throws Exception {
        this.setBranchReviewerId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getBranchReviewer() {
        return this.getBranchReviewerId();
    }

    @JsonIgnore
    public boolean getBranchReviewerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.branchReviewerId);
    }

    /** 有复核意见. */
    @Column(name = "hasComments")
    private Boolean hasComments;

    public void setHasComments(Boolean hasComments) {
        this.hasComments = hasComments;
    }

    public Boolean getHasComments() {
        return this.hasComments;
    }

    /** 分所复核复核情况备注. */
    @Column(name = "branchReviewNotes", columnDefinition = "nvarchar(max)")
    private String branchReviewNotes;

    public void setBranchReviewNotes(String branchReviewNotes) {
        this.branchReviewNotes = branchReviewNotes;
    }

    public String getBranchReviewNotes() {
        return this.branchReviewNotes;
    }

    /** 分所公告日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "branchDate")
    private LocalDateTime branchDate;

    public void setBranchDate(LocalDateTime branchDate) {
        this.branchDate = branchDate;
    }

    public LocalDateTime getBranchDate() {
        return this.branchDate;
    }

    /** 分所可出报告. */
    @Column(name = "branchReport")
    private Integer branchReport;

    public void setBranchReport(Integer branchReport) {
        this.branchReport = branchReport;
    }

    public Integer getBranchReport() {
        return this.branchReport;
    }

    /** 总所复核复核情况备注. */
    @Column(name = "generalReviewNotes", columnDefinition = "nvarchar(max)")
    private String generalReviewNotes;

    public void setGeneralReviewNotes(String generalReviewNotes) {
        this.generalReviewNotes = generalReviewNotes;
    }

    public String getGeneralReviewNotes() {
        return this.generalReviewNotes;
    }

    /** 总所公告日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "generalDate")
    private LocalDateTime generalDate;

    public void setGeneralDate(LocalDateTime generalDate) {
        this.generalDate = generalDate;
    }

    public LocalDateTime getGeneralDate() {
        return this.generalDate;
    }

    /** 总所可出报告. */
    @Column(name = "generalReport")
    private Integer generalReport;

    public void setGeneralReport(Integer generalReport) {
        this.generalReport = generalReport;
    }

    public Integer getGeneralReport() {
        return this.generalReport;
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

    /** 并行状态. */
    @Column(name = "docStateAll", columnDefinition = "nvarchar(1000)")
    private String docStateAll;

    public void setDocStateAll(String docStateAll) {
        this.docStateAll = docStateAll;
    }

    public String getDocStateAll() {
        return this.docStateAll;
    }

    /** md5. */
    @Column(name = "md5", columnDefinition = "nvarchar(50)")
    private String md5;

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getMd5() {
        return this.md5;
    }

    /** 底稿复核工作流子表列表. */
    @OneToMany(mappedBy = "draftReviewBatchId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.DraftReviewBatchItem> draftReviewBatchItems = new java.util.ArrayList<>();

    public void setDraftReviewBatchItems(java.util.List<com.dxn.model.extend.DraftReviewBatchItem> draftReviewBatchItems) {
        this.draftReviewBatchItems = draftReviewBatchItems;
    }

    public java.util.List<com.dxn.model.extend.DraftReviewBatchItem> getDraftReviewBatchItems() {
        if (this.draftReviewBatchItems != null) {
           for (com.dxn.model.extend.DraftReviewBatchItem item : this.draftReviewBatchItems) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.draftReviewBatchItems;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DraftReviewBatchItem> readDraftReviewBatchItems() {
        return this.draftReviewBatchItems;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DraftReviewBatchItem> getDraftReviewBatchItemsReadOnly() {
        return this.draftReviewBatchItems;
    }

    /** 复核分配表列表. */
    @OneToMany(mappedBy = "draftReviewBatchId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.ReviewAllocation> reviewAllocations = new java.util.ArrayList<>();

    public void setReviewAllocations(java.util.List<com.dxn.model.extend.ReviewAllocation> reviewAllocations) {
        this.reviewAllocations = reviewAllocations;
    }

    public java.util.List<com.dxn.model.extend.ReviewAllocation> getReviewAllocations() {
        if (this.reviewAllocations != null) {
           for (com.dxn.model.extend.ReviewAllocation item : this.reviewAllocations) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.reviewAllocations;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ReviewAllocation> readReviewAllocations() {
        return this.reviewAllocations;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ReviewAllocation> getReviewAllocationsReadOnly() {
        return this.reviewAllocations;
    }

    @JsonIgnore
    public static com.dxn.model.extend.DraftReviewBatch findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DraftReviewBatch.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DraftReviewBatch findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DraftReviewBatch.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DraftReviewBatch findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DraftReviewBatch.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DraftReviewBatch findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DraftReviewBatch.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DraftReviewBatch createNew() throws Exception {
        com.dxn.model.extend.DraftReviewBatch entity = new com.dxn.model.extend.DraftReviewBatch();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DraftReviewBatch> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DraftReviewBatch.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "QR_DraftReviewBatch";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "底稿复核工作流批表";
    }

}
