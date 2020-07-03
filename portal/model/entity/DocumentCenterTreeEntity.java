package com.dxn.model.entity;

import com.dxn.entity.EntityTreeBase;
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
public class DocumentCenterTreeEntity extends EntityTreeBase<com.dxn.model.extend.DocumentCenterTree> {

    /** 是否展示. */
    @Column(name = "showOrNot", columnDefinition = "nvarchar(50)")
    private String showOrNot;

    public void setShowOrNot(String showOrNot) {
        this.showOrNot = showOrNot;
    }

    public String getShowOrNot() {
        return this.showOrNot;
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

    /** 节点名称. */
    @Column(name = "name", columnDefinition = "nvarchar(500)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 文档中心树管理权限列表. */
    @OneToMany(mappedBy = "documentCenterTreeId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction> documentCenterTreeButtonJurisdictions = new java.util.ArrayList<>();

    public void setDocumentCenterTreeButtonJurisdictions(java.util.List<com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction> documentCenterTreeButtonJurisdictions) {
        this.documentCenterTreeButtonJurisdictions = documentCenterTreeButtonJurisdictions;
    }

    public java.util.List<com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction> getDocumentCenterTreeButtonJurisdictions() {
        if (this.documentCenterTreeButtonJurisdictions != null) {
           for (com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction item : this.documentCenterTreeButtonJurisdictions) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.documentCenterTreeButtonJurisdictions;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction> readDocumentCenterTreeButtonJurisdictions() {
        return this.documentCenterTreeButtonJurisdictions;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction> getDocumentCenterTreeButtonJurisdictionsReadOnly() {
        return this.documentCenterTreeButtonJurisdictions;
    }

    /** 文档中心树下载查看权限列表. */
    @OneToMany(mappedBy = "documentCenterTreeId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.DocumentCenterTreeJurisdiction> documentCenterTreeJurisdictions = new java.util.ArrayList<>();

    public void setDocumentCenterTreeJurisdictions(java.util.List<com.dxn.model.extend.DocumentCenterTreeJurisdiction> documentCenterTreeJurisdictions) {
        this.documentCenterTreeJurisdictions = documentCenterTreeJurisdictions;
    }

    public java.util.List<com.dxn.model.extend.DocumentCenterTreeJurisdiction> getDocumentCenterTreeJurisdictions() {
        if (this.documentCenterTreeJurisdictions != null) {
           for (com.dxn.model.extend.DocumentCenterTreeJurisdiction item : this.documentCenterTreeJurisdictions) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.documentCenterTreeJurisdictions;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DocumentCenterTreeJurisdiction> readDocumentCenterTreeJurisdictions() {
        return this.documentCenterTreeJurisdictions;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DocumentCenterTreeJurisdiction> getDocumentCenterTreeJurisdictionsReadOnly() {
        return this.documentCenterTreeJurisdictions;
    }

    public void setParent(long id) throws Exception {
        this.setParentId(com.dxn.model.extend.DocumentCenterTree.findById(id));
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterTree findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DocumentCenterTree.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterTree findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DocumentCenterTree.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterTree findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DocumentCenterTree.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterTree findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DocumentCenterTree.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterTree createNew() throws Exception {
        com.dxn.model.extend.DocumentCenterTree entity = new com.dxn.model.extend.DocumentCenterTree();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DocumentCenterTree> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DocumentCenterTree.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_DocumentCenterTree";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "文档中心树";
    }

}
