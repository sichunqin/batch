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
public class RoleAuthorityEntity extends EntityBase<com.dxn.model.extend.RoleAuthority> {

    /** 编号. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(100)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(250)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 超级编码. */
    @Column(name = "superCode", columnDefinition = "nvarchar(100)")
    private String superCode;

    public void setSuperCode(String superCode) {
        this.superCode = superCode;
    }

    public String getSuperCode() {
        return this.superCode;
    }

    /** 权限组. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "roleId")
    private com.dxn.model.extend.Role roleId;

    public void setRoleId(com.dxn.model.extend.Role roleId) {
        this.roleId = roleId;
    }

    public com.dxn.model.extend.Role getRoleId() {
        if (roleId != null) roleId.initialization();
        return this.roleId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Role getRoleIdReadOnly() {
        return this.roleId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Role readRoleId() {
        return this.roleId;
    }

    public void setRole(long id) throws Exception {
        this.setRoleId(com.dxn.model.extend.Role.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Role getRole() {
        return this.getRoleId();
    }

    @JsonIgnore
    public boolean getRoleIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.roleId);
    }

    /** 类型. */
    @Column(name = "dataType", nullable = false)
    private int dataType;

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getDataType() {
        return this.dataType;
    }

    /** 数据ID. */
    @Column(name = "dataId", nullable = false)
    private long dataId;

    public void setDataId(long dataId) {
        this.dataId = dataId;
    }

    public long getDataId() {
        return this.dataId;
    }

    /** 菜单. */
    @Column(name = "menuId", nullable = false)
    private long menuId;

    public void setMenuId(long menuId) {
        this.menuId = menuId;
    }

    public long getMenuId() {
        return this.menuId;
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
    public static com.dxn.model.extend.RoleAuthority findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.RoleAuthority.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.RoleAuthority findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.RoleAuthority.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.RoleAuthority findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.RoleAuthority.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.RoleAuthority findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.RoleAuthority.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.RoleAuthority createNew() throws Exception {
        com.dxn.model.extend.RoleAuthority entity = new com.dxn.model.extend.RoleAuthority();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.RoleAuthority> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.RoleAuthority.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_RoleAuthority";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "权限";
    }

}
