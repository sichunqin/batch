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
public class CPAQualificationEntity extends EntityBase<com.dxn.model.extend.CPAQualification> {

    /** 注师关系所在地. */
    @Column(name = "locationOfNoterRelations")
    private Integer locationOfNoterRelations;

    public void setLocationOfNoterRelations(Integer locationOfNoterRelations) {
        this.locationOfNoterRelations = locationOfNoterRelations;
    }

    public Integer getLocationOfNoterRelations() {
        return this.locationOfNoterRelations;
    }

    /** 注册会计师类型. */
    @Column(name = "accountantType")
    private Integer accountantType;

    public void setAccountantType(Integer accountantType) {
        this.accountantType = accountantType;
    }

    public Integer getAccountantType() {
        return this.accountantType;
    }

    /** 证书编号. */
    @Column(name = "code", columnDefinition = "nvarchar(250)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 批准文号. */
    @Column(name = "approvalNumber", columnDefinition = "nvarchar(250)")
    private String approvalNumber;

    public void setApprovalNumber(String approvalNumber) {
        this.approvalNumber = approvalNumber;
    }

    public String getApprovalNumber() {
        return this.approvalNumber;
    }

    /** 通过方式. */
    @Column(name = "wayOfPassing")
    private Integer wayOfPassing;

    public void setWayOfPassing(Integer wayOfPassing) {
        this.wayOfPassing = wayOfPassing;
    }

    public Integer getWayOfPassing() {
        return this.wayOfPassing;
    }

    /** 全科合格证号	. */
    @Column(name = "qualifiedNumber", columnDefinition = "nvarchar(250)")
    private String qualifiedNumber;

    public void setQualifiedNumber(String qualifiedNumber) {
        this.qualifiedNumber = qualifiedNumber;
    }

    public String getQualifiedNumber() {
        return this.qualifiedNumber;
    }

    /** 关系是否已转入本所. */
    @Column(name = "isRelationshipTransferOffice")
    private Integer isRelationshipTransferOffice;

    public void setIsRelationshipTransferOffice(Integer isRelationshipTransferOffice) {
        this.isRelationshipTransferOffice = isRelationshipTransferOffice;
    }

    public Integer getIsRelationshipTransferOffice() {
        return this.isRelationshipTransferOffice;
    }

    /** 开始执业时间. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "sPTime")
    private LocalDateTime sPTime;

    public void setSPTime(LocalDateTime sPTime) {
        this.sPTime = sPTime;
    }

    public LocalDateTime getSPTime() {
        return this.sPTime;
    }

    /** 批准日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "approvalDate")
    private LocalDateTime approvalDate;

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public LocalDateTime getApprovalDate() {
        return this.approvalDate;
    }

    /** 转入所名称. */
    @Column(name = "transferName", columnDefinition = "nvarchar(250)")
    private String transferName;

    public void setTransferName(String transferName) {
        this.transferName = transferName;
    }

    public String getTransferName() {
        return this.transferName;
    }

    /** 转出名称. */
    @Column(name = "transferoutName", columnDefinition = "nvarchar(250)")
    private String transferoutName;

    public void setTransferoutName(String transferoutName) {
        this.transferoutName = transferoutName;
    }

    public String getTransferoutName() {
        return this.transferoutName;
    }

    /** 是否是资深会员. */
    @Column(name = "isMember")
    private Integer isMember;

    public void setIsMember(Integer isMember) {
        this.isMember = isMember;
    }

    public Integer getIsMember() {
        return this.isMember;
    }

    /** 是否从事过证券. */
    @Column(name = "isSecurities")
    private Integer isSecurities;

    public void setIsSecurities(Integer isSecurities) {
        this.isSecurities = isSecurities;
    }

    public Integer getIsSecurities() {
        return this.isSecurities;
    }

    /** 是否是领军人物. */
    @Column(name = "isArmyHead")
    private Integer isArmyHead;

    public void setIsArmyHead(Integer isArmyHead) {
        this.isArmyHead = isArmyHead;
    }

    public Integer getIsArmyHead() {
        return this.isArmyHead;
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

    /** 员工信息. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "staffId")
    private com.dxn.model.extend.Staff staffId;

    public void setStaffId(com.dxn.model.extend.Staff staffId) {
        this.staffId = staffId;
    }

    public com.dxn.model.extend.Staff getStaffId() {
        if (staffId != null) staffId.initialization();
        return this.staffId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Staff getStaffIdReadOnly() {
        return this.staffId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Staff readStaffId() {
        return this.staffId;
    }

    public void setStaff(long id) throws Exception {
        this.setStaffId(com.dxn.model.extend.Staff.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Staff getStaff() {
        return this.getStaffId();
    }

    @JsonIgnore
    public boolean getStaffIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.staffId);
    }

    /** 人员执业附件表列表. */
    @OneToMany(mappedBy = "cPAQualificationId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.PracticeAppendix> practiceAppendixs = new java.util.ArrayList<>();

    public void setPracticeAppendixs(java.util.List<com.dxn.model.extend.PracticeAppendix> practiceAppendixs) {
        this.practiceAppendixs = practiceAppendixs;
    }

    public java.util.List<com.dxn.model.extend.PracticeAppendix> getPracticeAppendixs() {
        if (this.practiceAppendixs != null) {
           for (com.dxn.model.extend.PracticeAppendix item : this.practiceAppendixs) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.practiceAppendixs;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.PracticeAppendix> readPracticeAppendixs() {
        return this.practiceAppendixs;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.PracticeAppendix> getPracticeAppendixsReadOnly() {
        return this.practiceAppendixs;
    }

    @JsonIgnore
    public static com.dxn.model.extend.CPAQualification findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.CPAQualification.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CPAQualification findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.CPAQualification.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CPAQualification findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.CPAQualification.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CPAQualification findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.CPAQualification.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CPAQualification createNew() throws Exception {
        com.dxn.model.extend.CPAQualification entity = new com.dxn.model.extend.CPAQualification();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.CPAQualification> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.CPAQualification.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "HR";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "HR_StaffCPAQualification";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "CPA资质";
    }

}
