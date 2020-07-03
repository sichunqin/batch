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
public class QuickLinkGroupItemEntity extends EntityBase<com.dxn.model.extend.QuickLinkGroupItem> {

    /** 我的工作菜单组. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quickLinkGroupId")
    private com.dxn.model.extend.QuickLinkGroup quickLinkGroupId;

    public void setQuickLinkGroupId(com.dxn.model.extend.QuickLinkGroup quickLinkGroupId) {
        this.quickLinkGroupId = quickLinkGroupId;
    }

    public com.dxn.model.extend.QuickLinkGroup getQuickLinkGroupId() {
        if (quickLinkGroupId != null) quickLinkGroupId.initialization();
        return this.quickLinkGroupId;
    }

    @JsonIgnore
    public com.dxn.model.extend.QuickLinkGroup getQuickLinkGroupIdReadOnly() {
        return this.quickLinkGroupId;
    }

    @JsonIgnore
    public com.dxn.model.extend.QuickLinkGroup readQuickLinkGroupId() {
        return this.quickLinkGroupId;
    }

    public void setQuickLinkGroup(long id) throws Exception {
        this.setQuickLinkGroupId(com.dxn.model.extend.QuickLinkGroup.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.QuickLinkGroup getQuickLinkGroup() {
        return this.getQuickLinkGroupId();
    }

    @JsonIgnore
    public boolean getQuickLinkGroupIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.quickLinkGroupId);
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

    /** 排序. */
    @Column(name = "sort", nullable = false)
    private int sort;

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getSort() {
        return this.sort;
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
    public static com.dxn.model.extend.QuickLinkGroupItem findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.QuickLinkGroupItem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.QuickLinkGroupItem findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.QuickLinkGroupItem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.QuickLinkGroupItem findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.QuickLinkGroupItem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.QuickLinkGroupItem findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.QuickLinkGroupItem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.QuickLinkGroupItem createNew() throws Exception {
        com.dxn.model.extend.QuickLinkGroupItem entity = new com.dxn.model.extend.QuickLinkGroupItem();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.QuickLinkGroupItem> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.QuickLinkGroupItem.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_QuickLinkGroupItem";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "我的工作菜单项";
    }

}
