package com.dxn.model.entity;

import com.dxn.entity.EntityTreeBase;
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
public class StaffPositionEntity extends EntityTreeBase<com.dxn.model.extend.StaffPosition> {

    /** 首页表单配置Id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homeFormConfigurationId")
    private com.dxn.model.extend.HomeFormConfiguration homeFormConfigurationId;

    public void setHomeFormConfigurationId(com.dxn.model.extend.HomeFormConfiguration homeFormConfigurationId) {
        this.homeFormConfigurationId = homeFormConfigurationId;
    }

    public com.dxn.model.extend.HomeFormConfiguration getHomeFormConfigurationId() {
        if (homeFormConfigurationId != null) homeFormConfigurationId.initialization();
        return this.homeFormConfigurationId;
    }

    @JsonIgnore
    public com.dxn.model.extend.HomeFormConfiguration getHomeFormConfigurationIdReadOnly() {
        return this.homeFormConfigurationId;
    }

    @JsonIgnore
    public com.dxn.model.extend.HomeFormConfiguration readHomeFormConfigurationId() {
        return this.homeFormConfigurationId;
    }

    public void setHomeFormConfiguration(long id) throws Exception {
        this.setHomeFormConfigurationId(com.dxn.model.extend.HomeFormConfiguration.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.HomeFormConfiguration getHomeFormConfiguration() {
        return this.getHomeFormConfigurationId();
    }

    @JsonIgnore
    public boolean getHomeFormConfigurationIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.homeFormConfigurationId);
    }

    /** 职级编码. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(20)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 职级名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(200)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 职级类型. */
    @Column(name = "positionType")
    private Integer positionType;

    public void setPositionType(Integer positionType) {
        this.positionType = positionType;
    }

    public Integer getPositionType() {
        return this.positionType;
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

    /** 员工流程部门职级表列表. */
    @OneToMany(mappedBy = "staffPositionId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.EmployeeProcessDepartmentRankTable> employeeProcessDepartmentRankTables = new java.util.ArrayList<>();

    public void setEmployeeProcessDepartmentRankTables(java.util.List<com.dxn.model.extend.EmployeeProcessDepartmentRankTable> employeeProcessDepartmentRankTables) {
        this.employeeProcessDepartmentRankTables = employeeProcessDepartmentRankTables;
    }

    public java.util.List<com.dxn.model.extend.EmployeeProcessDepartmentRankTable> getEmployeeProcessDepartmentRankTables() {
        if (this.employeeProcessDepartmentRankTables != null) {
           for (com.dxn.model.extend.EmployeeProcessDepartmentRankTable item : this.employeeProcessDepartmentRankTables) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.employeeProcessDepartmentRankTables;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.EmployeeProcessDepartmentRankTable> readEmployeeProcessDepartmentRankTables() {
        return this.employeeProcessDepartmentRankTables;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.EmployeeProcessDepartmentRankTable> getEmployeeProcessDepartmentRankTablesReadOnly() {
        return this.employeeProcessDepartmentRankTables;
    }

    /** 快捷链接列表. */
    @OneToMany(mappedBy = "staffPositionId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.StaffPositionLink> staffPositionLinks = new java.util.ArrayList<>();

    public void setStaffPositionLinks(java.util.List<com.dxn.model.extend.StaffPositionLink> staffPositionLinks) {
        this.staffPositionLinks = staffPositionLinks;
    }

    public java.util.List<com.dxn.model.extend.StaffPositionLink> getStaffPositionLinks() {
        if (this.staffPositionLinks != null) {
           for (com.dxn.model.extend.StaffPositionLink item : this.staffPositionLinks) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.staffPositionLinks;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.StaffPositionLink> readStaffPositionLinks() {
        return this.staffPositionLinks;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.StaffPositionLink> getStaffPositionLinksReadOnly() {
        return this.staffPositionLinks;
    }

    /** 职级权限列表. */
    @OneToMany(mappedBy = "staffPositionId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.StaffPositionRole> staffPositionRoles = new java.util.ArrayList<>();

    public void setStaffPositionRoles(java.util.List<com.dxn.model.extend.StaffPositionRole> staffPositionRoles) {
        this.staffPositionRoles = staffPositionRoles;
    }

    public java.util.List<com.dxn.model.extend.StaffPositionRole> getStaffPositionRoles() {
        if (this.staffPositionRoles != null) {
           for (com.dxn.model.extend.StaffPositionRole item : this.staffPositionRoles) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.staffPositionRoles;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.StaffPositionRole> readStaffPositionRoles() {
        return this.staffPositionRoles;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.StaffPositionRole> getStaffPositionRolesReadOnly() {
        return this.staffPositionRoles;
    }

    public void setParent(long id) throws Exception {
        this.setParentId(com.dxn.model.extend.StaffPosition.findById(id));
    }

    @JsonIgnore
    public static com.dxn.model.extend.StaffPosition findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.StaffPosition.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.StaffPosition findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.StaffPosition.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.StaffPosition findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.StaffPosition.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.StaffPosition findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.StaffPosition.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.StaffPosition createNew() throws Exception {
        com.dxn.model.extend.StaffPosition entity = new com.dxn.model.extend.StaffPosition();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.StaffPosition> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.StaffPosition.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "HR";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "HR_StaffPosition";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "员工职级";
    }

}
