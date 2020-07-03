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
public class DataAuthorityMenuEntity extends EntityBase<com.dxn.model.extend.DataAuthorityMenu> {

    /** 数据权限组. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dataAuthorityGroupId")
    private com.dxn.model.extend.DataAuthorityGroup dataAuthorityGroupId;

    public void setDataAuthorityGroupId(com.dxn.model.extend.DataAuthorityGroup dataAuthorityGroupId) {
        this.dataAuthorityGroupId = dataAuthorityGroupId;
    }

    public com.dxn.model.extend.DataAuthorityGroup getDataAuthorityGroupId() {
        if (dataAuthorityGroupId != null) dataAuthorityGroupId.initialization();
        return this.dataAuthorityGroupId;
    }

    @JsonIgnore
    public com.dxn.model.extend.DataAuthorityGroup getDataAuthorityGroupIdReadOnly() {
        return this.dataAuthorityGroupId;
    }

    @JsonIgnore
    public com.dxn.model.extend.DataAuthorityGroup readDataAuthorityGroupId() {
        return this.dataAuthorityGroupId;
    }

    public void setDataAuthorityGroup(long id) throws Exception {
        this.setDataAuthorityGroupId(com.dxn.model.extend.DataAuthorityGroup.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.DataAuthorityGroup getDataAuthorityGroup() {
        return this.getDataAuthorityGroupId();
    }

    @JsonIgnore
    public boolean getDataAuthorityGroupIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.dataAuthorityGroupId);
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
    public static com.dxn.model.extend.DataAuthorityMenu findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DataAuthorityMenu.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DataAuthorityMenu findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DataAuthorityMenu.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DataAuthorityMenu findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DataAuthorityMenu.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DataAuthorityMenu findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DataAuthorityMenu.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DataAuthorityMenu createNew() throws Exception {
        com.dxn.model.extend.DataAuthorityMenu entity = new com.dxn.model.extend.DataAuthorityMenu();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DataAuthorityMenu> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DataAuthorityMenu.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_DataAuthorityMenu";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "数据权限菜单";
    }

}
