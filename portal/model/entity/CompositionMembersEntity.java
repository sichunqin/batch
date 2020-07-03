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
public class CompositionMembersEntity extends EntityBase<com.dxn.model.extend.CompositionMembers> {

    /** 角色排序. */
    @Column(name = "roleSort")
    private Integer roleSort;

    public void setRoleSort(Integer roleSort) {
        this.roleSort = roleSort;
    }

    public Integer getRoleSort() {
        return this.roleSort;
    }

    /** 独立性签署. */
    @Column(name = "signatureOfIndependence")
    private Integer signatureOfIndependence;

    public void setSignatureOfIndependence(Integer signatureOfIndependence) {
        this.signatureOfIndependence = signatureOfIndependence;
    }

    public Integer getSignatureOfIndependence() {
        return this.signatureOfIndependence;
    }

    /** 排序. */
    @Column(name = "sort")
    private Integer sort;

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getSort() {
        return this.sort;
    }

    /** 是否启用. */
    @Column(name = "isEnable", nullable = false)
    private int isEnable;

    public void setIsEnable(int isEnable) {
        this.isEnable = isEnable;
    }

    public int getIsEnable() {
        return this.isEnable;
    }

    /** 名称. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nameId")
    private com.dxn.model.extend.User nameId;

    public void setNameId(com.dxn.model.extend.User nameId) {
        this.nameId = nameId;
    }

    public com.dxn.model.extend.User getNameId() {
        if (nameId != null) nameId.initialization();
        return this.nameId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getNameIdReadOnly() {
        return this.nameId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readNameId() {
        return this.nameId;
    }

    public void setName(long id) throws Exception {
        this.setNameId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getName() {
        return this.getNameId();
    }

    @JsonIgnore
    public boolean getNameIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.nameId);
    }

    /** 编号. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(200)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 职级. */
    @Column(name = "rank", nullable = false, columnDefinition = "nvarchar(20)")
    private String rank;

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return this.rank;
    }

    /** 资质. */
    @Column(name = "qualifications", columnDefinition = "nvarchar(50)")
    private String qualifications;

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getQualifications() {
        return this.qualifications;
    }

    /** 从业年限. */
    @Column(name = "workingYears", nullable = false)
    private int workingYears;

    public void setWorkingYears(int workingYears) {
        this.workingYears = workingYears;
    }

    public int getWorkingYears() {
        return this.workingYears;
    }

    /** 是否有从业经验. */
    @Column(name = "isExperience")
    private Integer isExperience;

    public void setIsExperience(Integer isExperience) {
        this.isExperience = isExperience;
    }

    public Integer getIsExperience() {
        return this.isExperience;
    }

    /** 项目角色. */
    @Column(name = "roleName", nullable = false)
    private int roleName;

    public void setRoleName(int roleName) {
        this.roleName = roleName;
    }

    public int getRoleName() {
        return this.roleName;
    }

    /** 被审计单位. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "compositionCustomerId")
    private com.dxn.model.extend.CompositionCustomer compositionCustomerId;

    public void setCompositionCustomerId(com.dxn.model.extend.CompositionCustomer compositionCustomerId) {
        this.compositionCustomerId = compositionCustomerId;
    }

    public com.dxn.model.extend.CompositionCustomer getCompositionCustomerId() {
        if (compositionCustomerId != null) compositionCustomerId.initialization();
        return this.compositionCustomerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer getCompositionCustomerIdReadOnly() {
        return this.compositionCustomerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer readCompositionCustomerId() {
        return this.compositionCustomerId;
    }

    public void setCompositionCustomer(long id) throws Exception {
        this.setCompositionCustomerId(com.dxn.model.extend.CompositionCustomer.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer getCompositionCustomer() {
        return this.getCompositionCustomerId();
    }

    @JsonIgnore
    public boolean getCompositionCustomerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.compositionCustomerId);
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
    public static com.dxn.model.extend.CompositionMembers findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.CompositionMembers.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CompositionMembers findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.CompositionMembers.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CompositionMembers findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.CompositionMembers.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CompositionMembers findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.CompositionMembers.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CompositionMembers createNew() throws Exception {
        com.dxn.model.extend.CompositionMembers entity = new com.dxn.model.extend.CompositionMembers();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.CompositionMembers> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.CompositionMembers.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_CompositionMembers";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "被审计单位成员表";
    }

}
