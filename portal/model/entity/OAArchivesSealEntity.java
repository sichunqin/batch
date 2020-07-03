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
public class OAArchivesSealEntity extends EntityBase<com.dxn.model.extend.OAArchivesSeal> {

    /** 金额. */
    @Column(name = "amount", columnDefinition = "decimal(20,0)")
    private BigDecimal amount;

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    /** 已打印份数. */
    @Column(name = "printedNumber")
    private Integer printedNumber;

    public void setPrintedNumber(Integer printedNumber) {
        this.printedNumber = printedNumber;
    }

    public Integer getPrintedNumber() {
        return this.printedNumber;
    }

    /** 签章完成. */
    @Column(name = "signCompletion")
    private Integer signCompletion;

    public void setSignCompletion(Integer signCompletion) {
        this.signCompletion = signCompletion;
    }

    public Integer getSignCompletion() {
        return this.signCompletion;
    }

    /** 部门id. */
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

    /** 关联项目. */
    @ManyToOne(fetch = FetchType.LAZY)
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

    /** 客户性质. */
    @Column(name = "customerNature")
    private Integer customerNature;

    public void setCustomerNature(Integer customerNature) {
        this.customerNature = customerNature;
    }

    public Integer getCustomerNature() {
        return this.customerNature;
    }

    /** 客户. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId")
    private com.dxn.model.extend.Customer customerId;

    public void setCustomerId(com.dxn.model.extend.Customer customerId) {
        this.customerId = customerId;
    }

    public com.dxn.model.extend.Customer getCustomerId() {
        if (customerId != null) customerId.initialization();
        return this.customerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Customer getCustomerIdReadOnly() {
        return this.customerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Customer readCustomerId() {
        return this.customerId;
    }

    public void setCustomer(long id) throws Exception {
        this.setCustomerId(com.dxn.model.extend.Customer.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Customer getCustomer() {
        return this.getCustomerId();
    }

    @JsonIgnore
    public boolean getCustomerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.customerId);
    }

    /** 描述内容. */
    @Column(name = "describeContent", columnDefinition = "nvarchar(500)")
    private String describeContent;

    public void setDescribeContent(String describeContent) {
        this.describeContent = describeContent;
    }

    public String getDescribeContent() {
        return this.describeContent;
    }

    /** 用途. */
    @Column(name = "purpose", columnDefinition = "nvarchar(500)")
    private String purpose;

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPurpose() {
        return this.purpose;
    }

    /** 名称. */
    @Column(name = "name", columnDefinition = "nvarchar(100)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 文件号. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(200)", unique = true)
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 总所/分所用印. */
    @Column(name = "sealType", nullable = false)
    private int sealType;

    public void setSealType(int sealType) {
        this.sealType = sealType;
    }

    public int getSealType() {
        return this.sealType;
    }

    /** 类型. */
    @Column(name = "oAType", nullable = false)
    private int oAType;

    public void setOAType(int oAType) {
        this.oAType = oAType;
    }

    public int getOAType() {
        return this.oAType;
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

    /** OA文档用印附件列表. */
    @OneToMany(mappedBy = "oAArchivesSealId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.OAArchivesSealAttachment> oAArchivesSealAttachments = new java.util.ArrayList<>();

    public void setOAArchivesSealAttachments(java.util.List<com.dxn.model.extend.OAArchivesSealAttachment> oAArchivesSealAttachments) {
        this.oAArchivesSealAttachments = oAArchivesSealAttachments;
    }

    public java.util.List<com.dxn.model.extend.OAArchivesSealAttachment> getOAArchivesSealAttachments() {
        if (this.oAArchivesSealAttachments != null) {
           for (com.dxn.model.extend.OAArchivesSealAttachment item : this.oAArchivesSealAttachments) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.oAArchivesSealAttachments;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.OAArchivesSealAttachment> readOAArchivesSealAttachments() {
        return this.oAArchivesSealAttachments;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.OAArchivesSealAttachment> getOAArchivesSealAttachmentsReadOnly() {
        return this.oAArchivesSealAttachments;
    }

    @JsonIgnore
    public static com.dxn.model.extend.OAArchivesSeal findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.OAArchivesSeal.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.OAArchivesSeal findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.OAArchivesSeal.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.OAArchivesSeal findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.OAArchivesSeal.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.OAArchivesSeal findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.OAArchivesSeal.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.OAArchivesSeal createNew() throws Exception {
        com.dxn.model.extend.OAArchivesSeal entity = new com.dxn.model.extend.OAArchivesSeal();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.OAArchivesSeal> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.OAArchivesSeal.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_OAArchivesSeal";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "OA档案用印";
    }

}
