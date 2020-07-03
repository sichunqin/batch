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
public class QuickLinkGroupEntity extends EntityBase<com.dxn.model.extend.QuickLinkGroup> {

    /** 编码. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(250)")
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

    /** 我的工作菜单项列表. */
    @OneToMany(mappedBy = "quickLinkGroupId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.QuickLinkGroupItem> quickLinkGroupItems = new java.util.ArrayList<>();

    public void setQuickLinkGroupItems(java.util.List<com.dxn.model.extend.QuickLinkGroupItem> quickLinkGroupItems) {
        this.quickLinkGroupItems = quickLinkGroupItems;
    }

    public java.util.List<com.dxn.model.extend.QuickLinkGroupItem> getQuickLinkGroupItems() {
        if (this.quickLinkGroupItems != null) {
           for (com.dxn.model.extend.QuickLinkGroupItem item : this.quickLinkGroupItems) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.quickLinkGroupItems;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.QuickLinkGroupItem> readQuickLinkGroupItems() {
        return this.quickLinkGroupItems;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.QuickLinkGroupItem> getQuickLinkGroupItemsReadOnly() {
        return this.quickLinkGroupItems;
    }

    /** 我的工作菜单用户列表. */
    @OneToMany(mappedBy = "quickLinkGroupId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.QuickLinkGroupUser> quickLinkGroupUsers = new java.util.ArrayList<>();

    public void setQuickLinkGroupUsers(java.util.List<com.dxn.model.extend.QuickLinkGroupUser> quickLinkGroupUsers) {
        this.quickLinkGroupUsers = quickLinkGroupUsers;
    }

    public java.util.List<com.dxn.model.extend.QuickLinkGroupUser> getQuickLinkGroupUsers() {
        if (this.quickLinkGroupUsers != null) {
           for (com.dxn.model.extend.QuickLinkGroupUser item : this.quickLinkGroupUsers) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.quickLinkGroupUsers;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.QuickLinkGroupUser> readQuickLinkGroupUsers() {
        return this.quickLinkGroupUsers;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.QuickLinkGroupUser> getQuickLinkGroupUsersReadOnly() {
        return this.quickLinkGroupUsers;
    }

    @JsonIgnore
    public static com.dxn.model.extend.QuickLinkGroup findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.QuickLinkGroup.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.QuickLinkGroup findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.QuickLinkGroup.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.QuickLinkGroup findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.QuickLinkGroup.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.QuickLinkGroup findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.QuickLinkGroup.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.QuickLinkGroup createNew() throws Exception {
        com.dxn.model.extend.QuickLinkGroup entity = new com.dxn.model.extend.QuickLinkGroup();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.QuickLinkGroup> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.QuickLinkGroup.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_QuickLinkGroup";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "我的工作菜单组";
    }

}
