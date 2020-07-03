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
public class DeptQuickLinkEntity extends EntityBase<com.dxn.model.extend.DeptQuickLink> {

    /** 部门. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departmentId")
    private com.dxn.model.extend.Department departmentId;

    public void setDepartmentId(com.dxn.model.extend.Department departmentId) {
        this.departmentId = departmentId;
    }

    public com.dxn.model.extend.Department getDepartmentId() {
        if (departmentId != null) departmentId.initialization();
        return this.departmentId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getDepartmentIdReadOnly() {
        return this.departmentId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department readDepartmentId() {
        return this.departmentId;
    }

    public void setDepartment(long id) throws Exception {
        this.setDepartmentId(com.dxn.model.extend.Department.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getDepartment() {
        return this.getDepartmentId();
    }

    @JsonIgnore
    public boolean getDepartmentIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.departmentId);
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
    public static com.dxn.model.extend.DeptQuickLink findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DeptQuickLink.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DeptQuickLink findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DeptQuickLink.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DeptQuickLink findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DeptQuickLink.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DeptQuickLink findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DeptQuickLink.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DeptQuickLink createNew() throws Exception {
        com.dxn.model.extend.DeptQuickLink entity = new com.dxn.model.extend.DeptQuickLink();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DeptQuickLink> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DeptQuickLink.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_DeptQuickLink";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "我的工作菜单";
    }

}
