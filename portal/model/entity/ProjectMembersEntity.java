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
public class ProjectMembersEntity extends EntityBase<com.dxn.model.extend.ProjectMembers> {

    /** 角色排序. */
    @Column(name = "roleSort")
    private Integer roleSort;

    public void setRoleSort(Integer roleSort) {
        this.roleSort = roleSort;
    }

    public Integer getRoleSort() {
        return this.roleSort;
    }

    /** 独立性显示日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "signingDate")
    private LocalDateTime signingDate;

    public void setSigningDate(LocalDateTime signingDate) {
        this.signingDate = signingDate;
    }

    public LocalDateTime getSigningDate() {
        return this.signingDate;
    }

    /** 独立性签署内容. */
    @Column(name = "signatureOfIndependenceString", columnDefinition = "nvarchar(max)")
    private String signatureOfIndependenceString;

    public void setSignatureOfIndependenceString(String signatureOfIndependenceString) {
        this.signatureOfIndependenceString = signatureOfIndependenceString;
    }

    public String getSignatureOfIndependenceString() {
        return this.signatureOfIndependenceString;
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

    /** 员工职级Id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staffPositionId")
    private com.dxn.model.extend.StaffPosition staffPositionId;

    public void setStaffPositionId(com.dxn.model.extend.StaffPosition staffPositionId) {
        this.staffPositionId = staffPositionId;
    }

    public com.dxn.model.extend.StaffPosition getStaffPositionId() {
        if (staffPositionId != null) staffPositionId.initialization();
        return this.staffPositionId;
    }

    @JsonIgnore
    public com.dxn.model.extend.StaffPosition getStaffPositionIdReadOnly() {
        return this.staffPositionId;
    }

    @JsonIgnore
    public com.dxn.model.extend.StaffPosition readStaffPositionId() {
        return this.staffPositionId;
    }

    public void setStaffPosition(long id) throws Exception {
        this.setStaffPositionId(com.dxn.model.extend.StaffPosition.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.StaffPosition getStaffPosition() {
        return this.getStaffPositionId();
    }

    @JsonIgnore
    public boolean getStaffPositionIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.staffPositionId);
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

    /** 状态. */
    @Column(name = "isEnable")
    private Integer isEnable;

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }

    public Integer getIsEnable() {
        return this.isEnable;
    }

    /** 独立性是否受到影响. */
    @Column(name = "isIndependence")
    private Integer isIndependence;

    public void setIsIndependence(Integer isIndependence) {
        this.isIndependence = isIndependence;
    }

    public Integer getIsIndependence() {
        return this.isIndependence;
    }

    /** 备注. */
    @Column(name = "remark", columnDefinition = "nvarchar(2000)")
    private String remark;

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    /** 姓名. */
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

    /** 职级. */
    @Column(name = "rank", nullable = false, columnDefinition = "nvarchar(20)")
    private String rank;

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return this.rank;
    }

    /** 专业资质. */
    @Column(name = "pQualifications", columnDefinition = "nvarchar(200)")
    private String pQualifications;

    public void setPQualifications(String pQualifications) {
        this.pQualifications = pQualifications;
    }

    public String getPQualifications() {
        return this.pQualifications;
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
    @Column(name = "projectRole")
    private Integer projectRole;

    public void setProjectRole(Integer projectRole) {
        this.projectRole = projectRole;
    }

    public Integer getProjectRole() {
        return this.projectRole;
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

    /** 项目. */
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

    @JsonIgnore
    public static com.dxn.model.extend.ProjectMembers findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.ProjectMembers.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProjectMembers findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.ProjectMembers.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProjectMembers findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.ProjectMembers.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProjectMembers findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.ProjectMembers.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProjectMembers createNew() throws Exception {
        com.dxn.model.extend.ProjectMembers entity = new com.dxn.model.extend.ProjectMembers();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.ProjectMembers> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.ProjectMembers.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_ProjectMembers";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "项目项目组成员";
    }

}
