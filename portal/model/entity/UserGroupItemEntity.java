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
public class UserGroupItemEntity extends EntityBase<com.dxn.model.extend.UserGroupItem> {

    /** 用户. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId")
    private com.dxn.model.extend.User userId;

    public void setUserId(com.dxn.model.extend.User userId) {
        this.userId = userId;
    }

    public com.dxn.model.extend.User getUserId() {
        if (userId != null) userId.initialization();
        return this.userId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getUserIdReadOnly() {
        return this.userId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readUserId() {
        return this.userId;
    }

    public void setUser(long id) throws Exception {
        this.setUserId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getUser() {
        return this.getUserId();
    }

    @JsonIgnore
    public boolean getUserIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.userId);
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

    /** 用户组. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userGroupId")
    private com.dxn.model.extend.UserGroup userGroupId;

    public void setUserGroupId(com.dxn.model.extend.UserGroup userGroupId) {
        this.userGroupId = userGroupId;
    }

    public com.dxn.model.extend.UserGroup getUserGroupId() {
        if (userGroupId != null) userGroupId.initialization();
        return this.userGroupId;
    }

    @JsonIgnore
    public com.dxn.model.extend.UserGroup getUserGroupIdReadOnly() {
        return this.userGroupId;
    }

    @JsonIgnore
    public com.dxn.model.extend.UserGroup readUserGroupId() {
        return this.userGroupId;
    }

    public void setUserGroup(long id) throws Exception {
        this.setUserGroupId(com.dxn.model.extend.UserGroup.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.UserGroup getUserGroup() {
        return this.getUserGroupId();
    }

    @JsonIgnore
    public boolean getUserGroupIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.userGroupId);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UserGroupItem findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.UserGroupItem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UserGroupItem findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.UserGroupItem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UserGroupItem findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.UserGroupItem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UserGroupItem findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.UserGroupItem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UserGroupItem createNew() throws Exception {
        com.dxn.model.extend.UserGroupItem entity = new com.dxn.model.extend.UserGroupItem();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.UserGroupItem> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.UserGroupItem.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_UserGroupItem";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "用户组用户";
    }

}
