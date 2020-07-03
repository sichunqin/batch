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
public class InvestmentDeclarationEntity extends EntityBase<com.dxn.model.extend.InvestmentDeclaration> {

    /** 年度埋点. */
    @Column(name = "annualValue")
    private Integer annualValue;

    public void setAnnualValue(Integer annualValue) {
        this.annualValue = annualValue;
    }

    public Integer getAnnualValue() {
        return this.annualValue;
    }

    /** 填写说明文本. */
    @Column(name = "instructionTextArea", columnDefinition = "nvarchar(max)")
    private String instructionTextArea;

    public void setInstructionTextArea(String instructionTextArea) {
        this.instructionTextArea = instructionTextArea;
    }

    public String getInstructionTextArea() {
        return this.instructionTextArea;
    }

    /** 用户Id. */
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

    /** 状态. */
    @Column(name = "state", nullable = false)
    private int state;

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
    }

    /** 申报人. */
    @Column(name = "declarant", nullable = false, columnDefinition = "nvarchar(100)")
    private String declarant;

    public void setDeclarant(String declarant) {
        this.declarant = declarant;
    }

    public String getDeclarant() {
        return this.declarant;
    }

    /** 部门. */
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

    /** 职位. */
    @Column(name = "position", columnDefinition = "nvarchar(50)")
    private String position;

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return this.position;
    }

    /** 年度. */
    @Column(name = "year", nullable = false, columnDefinition = "nvarchar(50)")
    private String year;

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return this.year;
    }

    /** 是否合伙人. */
    @Column(name = "isPartner", nullable = false)
    private int isPartner;

    public void setIsPartner(int isPartner) {
        this.isPartner = isPartner;
    }

    public int getIsPartner() {
        return this.isPartner;
    }

    /** 是否CPA. */
    @Column(name = "isCPA", nullable = false)
    private int isCPA;

    public void setIsCPA(int isCPA) {
        this.isCPA = isCPA;
    }

    public int getIsCPA() {
        return this.isCPA;
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

    /** 投资情况列表. */
    @OneToMany(mappedBy = "investmentDeclarationId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.InvestmentSituation> investmentSituations = new java.util.ArrayList<>();

    public void setInvestmentSituations(java.util.List<com.dxn.model.extend.InvestmentSituation> investmentSituations) {
        this.investmentSituations = investmentSituations;
    }

    public java.util.List<com.dxn.model.extend.InvestmentSituation> getInvestmentSituations() {
        if (this.investmentSituations != null) {
           for (com.dxn.model.extend.InvestmentSituation item : this.investmentSituations) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.investmentSituations;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.InvestmentSituation> readInvestmentSituations() {
        return this.investmentSituations;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.InvestmentSituation> getInvestmentSituationsReadOnly() {
        return this.investmentSituations;
    }

    @JsonIgnore
    public static com.dxn.model.extend.InvestmentDeclaration findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.InvestmentDeclaration.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.InvestmentDeclaration findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.InvestmentDeclaration.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.InvestmentDeclaration findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.InvestmentDeclaration.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.InvestmentDeclaration findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.InvestmentDeclaration.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.InvestmentDeclaration createNew() throws Exception {
        com.dxn.model.extend.InvestmentDeclaration entity = new com.dxn.model.extend.InvestmentDeclaration();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.InvestmentDeclaration> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.InvestmentDeclaration.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Ai_InvestmentDeclaration";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "投资情况申报";
    }

}
