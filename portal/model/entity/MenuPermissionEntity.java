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
public class MenuPermissionEntity extends EntityBase<com.dxn.model.extend.MenuPermission> {

    /** 角色. */
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

    /** 菜单. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menuId")
    private com.dxn.model.extend.Menu menuId;

    public void setMenuId(com.dxn.model.extend.Menu menuId) {
        this.menuId = menuId;
    }

    public com.dxn.model.extend.Menu getMenuId() {
        if (menuId != null) menuId.initialization();
        return this.menuId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Menu getMenuIdReadOnly() {
        return this.menuId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Menu readMenuId() {
        return this.menuId;
    }

    public void setMenu(long id) throws Exception {
        this.setMenuId(com.dxn.model.extend.Menu.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Menu getMenu() {
        return this.getMenuId();
    }

    @JsonIgnore
    public boolean getMenuIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.menuId);
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
    public static com.dxn.model.extend.MenuPermission findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.MenuPermission.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.MenuPermission findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.MenuPermission.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.MenuPermission findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.MenuPermission.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.MenuPermission findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.MenuPermission.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.MenuPermission createNew() throws Exception {
        com.dxn.model.extend.MenuPermission entity = new com.dxn.model.extend.MenuPermission();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.MenuPermission> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.MenuPermission.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_MenuPermission";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "菜单权限";
    }

}
