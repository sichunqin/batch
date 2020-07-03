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
public class EntityMapEntity extends EntityBase<com.dxn.model.extend.EntityMap> {

    /** 编码. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(50)", unique = true)
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(50)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 表名. */
    @Column(name = "tableName", columnDefinition = "nvarchar(50)")
    private String tableName;

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return this.tableName;
    }

    /** 主实体. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mainEntityMapId")
    private com.dxn.model.extend.EntityMap mainEntityMapId;

    public void setMainEntityMapId(com.dxn.model.extend.EntityMap mainEntityMapId) {
        this.mainEntityMapId = mainEntityMapId;
    }

    public com.dxn.model.extend.EntityMap getMainEntityMapId() {
        if (mainEntityMapId != null) mainEntityMapId.initialization();
        return this.mainEntityMapId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap getMainEntityMapIdReadOnly() {
        return this.mainEntityMapId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap readMainEntityMapId() {
        return this.mainEntityMapId;
    }

    public void setMainEntityMap(long id) throws Exception {
        this.setMainEntityMapId(com.dxn.model.extend.EntityMap.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap getMainEntityMap() {
        return this.getMainEntityMapId();
    }

    @JsonIgnore
    public boolean getMainEntityMapIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.mainEntityMapId);
    }

    /** 命名空间. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "namespaceId")
    private com.dxn.model.extend.Namespace namespaceId;

    public void setNamespaceId(com.dxn.model.extend.Namespace namespaceId) {
        this.namespaceId = namespaceId;
    }

    public com.dxn.model.extend.Namespace getNamespaceId() {
        if (namespaceId != null) namespaceId.initialization();
        return this.namespaceId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Namespace getNamespaceIdReadOnly() {
        return this.namespaceId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Namespace readNamespaceId() {
        return this.namespaceId;
    }

    public void setNamespace(long id) throws Exception {
        this.setNamespaceId(com.dxn.model.extend.Namespace.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Namespace getNamespace() {
        return this.getNamespaceId();
    }

    @JsonIgnore
    public boolean getNamespaceIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.namespaceId);
    }

    /** 是否是树. */
    @Column(name = "isTree", nullable = false)
    private boolean isTree;

    public void setIsTree(boolean isTree) {
        this.isTree = isTree;
    }

    public boolean getIsTree() {
        return this.isTree;
    }

    /** 是否走工作流. */
    @Column(name = "isWorkflow", nullable = false)
    private boolean isWorkflow;

    public void setIsWorkflow(boolean isWorkflow) {
        this.isWorkflow = isWorkflow;
    }

    public boolean getIsWorkflow() {
        return this.isWorkflow;
    }

    /** 是否生成历史表. */
    @Column(name = "isCreateHistory", nullable = false)
    private boolean isCreateHistory;

    public void setIsCreateHistory(boolean isCreateHistory) {
        this.isCreateHistory = isCreateHistory;
    }

    public boolean getIsCreateHistory() {
        return this.isCreateHistory;
    }

    /** 是否为基础类型. */
    @Column(name = "isBaseType", nullable = false)
    private boolean isBaseType;

    public void setIsBaseType(boolean isBaseType) {
        this.isBaseType = isBaseType;
    }

    public boolean getIsBaseType() {
        return this.isBaseType;
    }

    /** 描述. */
    @Column(name = "description", columnDefinition = "nvarchar(max)")
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
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

    /** 实体属性列表. */
    @OneToMany(mappedBy = "entityMapId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.EntityMapItem> entityMapItems = new java.util.ArrayList<>();

    public void setEntityMapItems(java.util.List<com.dxn.model.extend.EntityMapItem> entityMapItems) {
        this.entityMapItems = entityMapItems;
    }

    public java.util.List<com.dxn.model.extend.EntityMapItem> getEntityMapItems() {
        if (this.entityMapItems != null) {
           for (com.dxn.model.extend.EntityMapItem item : this.entityMapItems) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.entityMapItems;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.EntityMapItem> readEntityMapItems() {
        return this.entityMapItems;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.EntityMapItem> getEntityMapItemsReadOnly() {
        return this.entityMapItems;
    }

    @JsonIgnore
    public static com.dxn.model.extend.EntityMap findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.EntityMap.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EntityMap findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.EntityMap.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EntityMap findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.EntityMap.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EntityMap findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.EntityMap.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EntityMap createNew() throws Exception {
        com.dxn.model.extend.EntityMap entity = new com.dxn.model.extend.EntityMap();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.EntityMap> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.EntityMap.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_EntityMap";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "实体";
    }

}
