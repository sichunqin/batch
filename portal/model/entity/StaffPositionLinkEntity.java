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
public class StaffPositionLinkEntity extends EntityBase<com.dxn.model.extend.StaffPositionLink> {

    /** 员工职级. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
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

    /** 是否快捷菜单. */
    @Column(name = "isQuickLink", nullable = false)
    private boolean isQuickLink;

    public void setIsQuickLink(boolean isQuickLink) {
        this.isQuickLink = isQuickLink;
    }

    public boolean getIsQuickLink() {
        return this.isQuickLink;
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
    public static com.dxn.model.extend.StaffPositionLink findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.StaffPositionLink.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.StaffPositionLink findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.StaffPositionLink.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.StaffPositionLink findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.StaffPositionLink.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.StaffPositionLink findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.StaffPositionLink.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.StaffPositionLink createNew() throws Exception {
        com.dxn.model.extend.StaffPositionLink entity = new com.dxn.model.extend.StaffPositionLink();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.StaffPositionLink> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.StaffPositionLink.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "HR";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "HR_StaffPositionLink";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "我的工作";
    }

}
