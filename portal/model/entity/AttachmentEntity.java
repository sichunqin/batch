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
public class AttachmentEntity extends EntityBase<com.dxn.model.extend.Attachment> {

    /** 名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(500)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 大小. */
    @Column(name = "size", nullable = false, columnDefinition = "nvarchar(50)")
    private String size;

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return this.size;
    }

    /** 后缀. */
    @Column(name = "suffix", nullable = false, columnDefinition = "nvarchar(50)")
    private String suffix;

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    /** 路径. */
    @Column(name = "path", columnDefinition = "nvarchar(500)")
    private String path;

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    /** Md5. */
    @Column(name = "md5", nullable = false, columnDefinition = "nvarchar(50)")
    private String md5;

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getMd5() {
        return this.md5;
    }

    /** 是否删除. */
    @Column(name = "isDelete", nullable = false)
    private boolean isDelete;

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public boolean getIsDelete() {
        return this.isDelete;
    }

    /** 状态. */
    @Transient
    private String stateType;

    public void setStateType(String stateType) {
        this.stateType = stateType;
    }

    public String getStateType() {
        return this.stateType;
    }

    /** 备注. */
    @Column(name = "remark", columnDefinition = "nvarchar(max)")
    private String remark;

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    /** EntityId. */
    @Column(name = "entityId")
    private Long entityId;

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getEntityId() {
        return this.entityId;
    }

    /** 实体. */
    @Column(name = "entityName", columnDefinition = "nvarchar(500)")
    private String entityName;

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityName() {
        return this.entityName;
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
    public static com.dxn.model.extend.Attachment findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.Attachment.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Attachment findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.Attachment.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Attachment findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.Attachment.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Attachment findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.Attachment.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Attachment createNew() throws Exception {
        com.dxn.model.extend.Attachment entity = new com.dxn.model.extend.Attachment();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.Attachment> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.Attachment.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_Attachment";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "附件";
    }

}
