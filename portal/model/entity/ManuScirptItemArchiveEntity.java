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
public class ManuScirptItemArchiveEntity extends EntityBase<com.dxn.model.extend.ManuScirptItemArchive> {

    /** 报告号. */
    @Column(name = "reportNumber", columnDefinition = "nvarchar(50)")
    private String reportNumber;

    public void setReportNumber(String reportNumber) {
        this.reportNumber = reportNumber;
    }

    public String getReportNumber() {
        return this.reportNumber;
    }

    /** 项目id. */
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

    /** 存放地. */
    @Column(name = "depositAddress", columnDefinition = "nvarchar(100)")
    private String depositAddress;

    public void setDepositAddress(String depositAddress) {
        this.depositAddress = depositAddress;
    }

    public String getDepositAddress() {
        return this.depositAddress;
    }

    /** 借出总盒数. */
    @Column(name = "borrowNumbers")
    private Integer borrowNumbers;

    public void setBorrowNumbers(Integer borrowNumbers) {
        this.borrowNumbers = borrowNumbers;
    }

    public Integer getBorrowNumbers() {
        return this.borrowNumbers;
    }

    /** 总册数. */
    @Column(name = "bookNumber", nullable = false)
    private int bookNumber;

    public void setBookNumber(int bookNumber) {
        this.bookNumber = bookNumber;
    }

    public int getBookNumber() {
        return this.bookNumber;
    }

    /** 总盒数. */
    @Column(name = "boxNumber", nullable = false)
    private int boxNumber;

    public void setBoxNumber(int boxNumber) {
        this.boxNumber = boxNumber;
    }

    public int getBoxNumber() {
        return this.boxNumber;
    }

    /** 归档日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "archiveDate", nullable = false)
    private LocalDateTime archiveDate;

    public void setArchiveDate(LocalDateTime archiveDate) {
        this.archiveDate = archiveDate;
    }

    public LocalDateTime getArchiveDate() {
        return this.archiveDate;
    }

    /** 档案接收人. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "acceptStaffId")
    private com.dxn.model.extend.User acceptStaffId;

    public void setAcceptStaffId(com.dxn.model.extend.User acceptStaffId) {
        this.acceptStaffId = acceptStaffId;
    }

    public com.dxn.model.extend.User getAcceptStaffId() {
        if (acceptStaffId != null) acceptStaffId.initialization();
        return this.acceptStaffId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getAcceptStaffIdReadOnly() {
        return this.acceptStaffId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readAcceptStaffId() {
        return this.acceptStaffId;
    }

    public void setAcceptStaff(long id) throws Exception {
        this.setAcceptStaffId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getAcceptStaff() {
        return this.getAcceptStaffId();
    }

    @JsonIgnore
    public boolean getAcceptStaffIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.acceptStaffId);
    }

    /** 档案移交人. */
    @Column(name = "transfererStaff", nullable = false, columnDefinition = "nvarchar(200)")
    private String transfererStaff;

    public void setTransfererStaff(String transfererStaff) {
        this.transfererStaff = transfererStaff;
    }

    public String getTransfererStaff() {
        return this.transfererStaff;
    }

    /** 被审计单位. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compostionCustomerId")
    private com.dxn.model.extend.CompositionCustomer compostionCustomerId;

    public void setCompostionCustomerId(com.dxn.model.extend.CompositionCustomer compostionCustomerId) {
        this.compostionCustomerId = compostionCustomerId;
    }

    public com.dxn.model.extend.CompositionCustomer getCompostionCustomerId() {
        if (compostionCustomerId != null) compostionCustomerId.initialization();
        return this.compostionCustomerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer getCompostionCustomerIdReadOnly() {
        return this.compostionCustomerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer readCompostionCustomerId() {
        return this.compostionCustomerId;
    }

    public void setCompostionCustomer(long id) throws Exception {
        this.setCompostionCustomerId(com.dxn.model.extend.CompositionCustomer.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer getCompostionCustomer() {
        return this.getCompostionCustomerId();
    }

    @JsonIgnore
    public boolean getCompostionCustomerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.compostionCustomerId);
    }

    /** 名称. */
    @Column(name = "name", columnDefinition = "nvarchar(200)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 档案号. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(200)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
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

    /** 底稿盒数和册数列表. */
    @OneToMany(mappedBy = "manuScirptItemArchiveId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.BoxAndNumberBook> boxAndNumberBooks = new java.util.ArrayList<>();

    public void setBoxAndNumberBooks(java.util.List<com.dxn.model.extend.BoxAndNumberBook> boxAndNumberBooks) {
        this.boxAndNumberBooks = boxAndNumberBooks;
    }

    public java.util.List<com.dxn.model.extend.BoxAndNumberBook> getBoxAndNumberBooks() {
        if (this.boxAndNumberBooks != null) {
           for (com.dxn.model.extend.BoxAndNumberBook item : this.boxAndNumberBooks) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.boxAndNumberBooks;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BoxAndNumberBook> readBoxAndNumberBooks() {
        return this.boxAndNumberBooks;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BoxAndNumberBook> getBoxAndNumberBooksReadOnly() {
        return this.boxAndNumberBooks;
    }

    @JsonIgnore
    public static com.dxn.model.extend.ManuScirptItemArchive findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.ManuScirptItemArchive.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ManuScirptItemArchive findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.ManuScirptItemArchive.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ManuScirptItemArchive findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.ManuScirptItemArchive.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ManuScirptItemArchive findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.ManuScirptItemArchive.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ManuScirptItemArchive createNew() throws Exception {
        com.dxn.model.extend.ManuScirptItemArchive entity = new com.dxn.model.extend.ManuScirptItemArchive();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.ManuScirptItemArchive> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.ManuScirptItemArchive.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_ManuScirptItemArchive";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "底稿归档表";
    }

}
