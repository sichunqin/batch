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
public class ProjectIndependenceMemberEntity extends EntityBase<com.dxn.model.extend.ProjectIndependenceMember> {

    /** 状态. */
    @Column(name = "isEnable")
    private Integer isEnable;

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }

    public Integer getIsEnable() {
        return this.isEnable;
    }

    /** 姓名. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nameId")
    private com.dxn.model.extend.User nameId;

    public void setNameId(com.dxn.model.extend.User nameId) {
        this.nameId = nameId;
    }

    public com.dxn.model.extend.User getNameId() {
        if (nameId != null) nameId.initialization();
        return this.nameId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getNameIdReadOnly() {
        return this.nameId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readNameId() {
        return this.nameId;
    }

    public void setName(long id) throws Exception {
        this.setNameId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getName() {
        return this.getNameId();
    }

    @JsonIgnore
    public boolean getNameIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.nameId);
    }

    /** 职级. */
    @Column(name = "rank", columnDefinition = "nvarchar(50)")
    private String rank;

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return this.rank;
    }

    /** 证券名称. */
    @Column(name = "securitiesName", columnDefinition = "nvarchar(200)")
    private String securitiesName;

    public void setSecuritiesName(String securitiesName) {
        this.securitiesName = securitiesName;
    }

    public String getSecuritiesName() {
        return this.securitiesName;
    }

    /** 证券代码. */
    @Column(name = "securitiesCode", columnDefinition = "nvarchar(100)")
    private String securitiesCode;

    public void setSecuritiesCode(String securitiesCode) {
        this.securitiesCode = securitiesCode;
    }

    public String getSecuritiesCode() {
        return this.securitiesCode;
    }

    /** 数量. */
    @Column(name = "quantity")
    private Integer quantity;

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    /** 是否从事过证券. */
    @Column(name = "isSecurities")
    private Integer isSecurities;

    public void setIsSecurities(Integer isSecurities) {
        this.isSecurities = isSecurities;
    }

    public Integer getIsSecurities() {
        return this.isSecurities;
    }

    /** 处理意见. */
    @Column(name = "processingOpinions")
    private Integer processingOpinions;

    public void setProcessingOpinions(Integer processingOpinions) {
        this.processingOpinions = processingOpinions;
    }

    public Integer getProcessingOpinions() {
        return this.processingOpinions;
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

    /** 项目. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "projectId")
    private com.dxn.model.extend.Project projectId;

    public void setProjectId(com.dxn.model.extend.Project projectId) {
        this.projectId = projectId;
    }

    public com.dxn.model.extend.Project getProjectId() {
        if (projectId != null) projectId.initialization();
        return this.projectId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Project getProjectIdReadOnly() {
        return this.projectId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Project readProjectId() {
        return this.projectId;
    }

    public void setProject(long id) throws Exception {
        this.setProjectId(com.dxn.model.extend.Project.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Project getProject() {
        return this.getProjectId();
    }

    @JsonIgnore
    public boolean getProjectIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.projectId);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProjectIndependenceMember findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.ProjectIndependenceMember.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProjectIndependenceMember findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.ProjectIndependenceMember.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProjectIndependenceMember findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.ProjectIndependenceMember.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProjectIndependenceMember findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.ProjectIndependenceMember.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProjectIndependenceMember createNew() throws Exception {
        com.dxn.model.extend.ProjectIndependenceMember entity = new com.dxn.model.extend.ProjectIndependenceMember();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.ProjectIndependenceMember> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.ProjectIndependenceMember.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_ProjectIndependenceMember";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "独立性项目组成员";
    }

}
