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
public class WorkFlowTypeEntity extends EntityTreeBase<com.dxn.model.extend.WorkFlowType> {

    /** 是否启用企业微信审批. */
    @Column(name = "enableWeChatApproval", nullable = false)
    private int enableWeChatApproval;

    public void setEnableWeChatApproval(int enableWeChatApproval) {
        this.enableWeChatApproval = enableWeChatApproval;
    }

    public int getEnableWeChatApproval() {
        return this.enableWeChatApproval;
    }

    /** 待办描述. */
    @Column(name = "toDoDescription", nullable = false, columnDefinition = "nvarchar(max)")
    private String toDoDescription;

    public void setToDoDescription(String toDoDescription) {
        this.toDoDescription = toDoDescription;
    }

    public String getToDoDescription() {
        return this.toDoDescription;
    }

    /** 流程状态类型. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processStateTypeId")
    private com.dxn.model.extend.EntityMapItem processStateTypeId;

    public void setProcessStateTypeId(com.dxn.model.extend.EntityMapItem processStateTypeId) {
        this.processStateTypeId = processStateTypeId;
    }

    public com.dxn.model.extend.EntityMapItem getProcessStateTypeId() {
        if (processStateTypeId != null) processStateTypeId.initialization();
        return this.processStateTypeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMapItem getProcessStateTypeIdReadOnly() {
        return this.processStateTypeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMapItem readProcessStateTypeId() {
        return this.processStateTypeId;
    }

    public void setProcessStateType(long id) throws Exception {
        this.setProcessStateTypeId(com.dxn.model.extend.EntityMapItem.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMapItem getProcessStateType() {
        return this.getProcessStateTypeId();
    }

    @JsonIgnore
    public boolean getProcessStateTypeIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.processStateTypeId);
    }

    /** 编码. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(50)")
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

    /** 实体. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entityMapId")
    private com.dxn.model.extend.EntityMap entityMapId;

    public void setEntityMapId(com.dxn.model.extend.EntityMap entityMapId) {
        this.entityMapId = entityMapId;
    }

    public com.dxn.model.extend.EntityMap getEntityMapId() {
        if (entityMapId != null) entityMapId.initialization();
        return this.entityMapId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap getEntityMapIdReadOnly() {
        return this.entityMapId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap readEntityMapId() {
        return this.entityMapId;
    }

    public void setEntityMap(long id) throws Exception {
        this.setEntityMapId(com.dxn.model.extend.EntityMap.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap getEntityMap() {
        return this.getEntityMapId();
    }

    @JsonIgnore
    public boolean getEntityMapIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.entityMapId);
    }

    /** 默认表单. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formPageId")
    private com.dxn.model.extend.FormPage formPageId;

    public void setFormPageId(com.dxn.model.extend.FormPage formPageId) {
        this.formPageId = formPageId;
    }

    public com.dxn.model.extend.FormPage getFormPageId() {
        if (formPageId != null) formPageId.initialization();
        return this.formPageId;
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage getFormPageIdReadOnly() {
        return this.formPageId;
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage readFormPageId() {
        return this.formPageId;
    }

    public void setFormPage(long id) throws Exception {
        this.setFormPageId(com.dxn.model.extend.FormPage.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage getFormPage() {
        return this.getFormPageId();
    }

    @JsonIgnore
    public boolean getFormPageIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.formPageId);
    }

    /** 是否允许重复提交. */
    @Column(name = "isRepeatedInitiation", nullable = false)
    private boolean isRepeatedInitiation;

    public void setIsRepeatedInitiation(boolean isRepeatedInitiation) {
        this.isRepeatedInitiation = isRepeatedInitiation;
    }

    public boolean getIsRepeatedInitiation() {
        return this.isRepeatedInitiation;
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

    /** 条件分支列表. */
    @OneToMany(mappedBy = "workFlowTypeId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.ConditionalBranch> conditionalBranchs = new java.util.ArrayList<>();

    public void setConditionalBranchs(java.util.List<com.dxn.model.extend.ConditionalBranch> conditionalBranchs) {
        this.conditionalBranchs = conditionalBranchs;
    }

    public java.util.List<com.dxn.model.extend.ConditionalBranch> getConditionalBranchs() {
        if (this.conditionalBranchs != null) {
           for (com.dxn.model.extend.ConditionalBranch item : this.conditionalBranchs) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.conditionalBranchs;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ConditionalBranch> readConditionalBranchs() {
        return this.conditionalBranchs;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ConditionalBranch> getConditionalBranchsReadOnly() {
        return this.conditionalBranchs;
    }

    /** 审批人列表. */
    @OneToMany(mappedBy = "workFlowTypeId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.ExaminerAndApprover> examinerAndApprovers = new java.util.ArrayList<>();

    public void setExaminerAndApprovers(java.util.List<com.dxn.model.extend.ExaminerAndApprover> examinerAndApprovers) {
        this.examinerAndApprovers = examinerAndApprovers;
    }

    public java.util.List<com.dxn.model.extend.ExaminerAndApprover> getExaminerAndApprovers() {
        if (this.examinerAndApprovers != null) {
           for (com.dxn.model.extend.ExaminerAndApprover item : this.examinerAndApprovers) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.examinerAndApprovers;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ExaminerAndApprover> readExaminerAndApprovers() {
        return this.examinerAndApprovers;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ExaminerAndApprover> getExaminerAndApproversReadOnly() {
        return this.examinerAndApprovers;
    }

    public void setParent(long id) throws Exception {
        this.setParentId(com.dxn.model.extend.WorkFlowType.findById(id));
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkFlowType findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.WorkFlowType.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkFlowType findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.WorkFlowType.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkFlowType findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.WorkFlowType.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkFlowType findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.WorkFlowType.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkFlowType createNew() throws Exception {
        com.dxn.model.extend.WorkFlowType entity = new com.dxn.model.extend.WorkFlowType();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.WorkFlowType> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.WorkFlowType.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_WorkFlowType";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "流程类型";
    }

}
