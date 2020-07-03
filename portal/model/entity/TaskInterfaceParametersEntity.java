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
public class TaskInterfaceParametersEntity extends EntityBase<com.dxn.model.extend.TaskInterfaceParameters> {

    /** 参数名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(200)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 参数值. */
    @Column(name = "value", nullable = false, columnDefinition = "nvarchar(200)")
    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    /** 参数类型. */
    @Column(name = "type", nullable = false)
    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    /** 参数数据源. */
    @Column(name = "dataSource", nullable = false)
    private int dataSource;

    public void setDataSource(int dataSource) {
        this.dataSource = dataSource;
    }

    public int getDataSource() {
        return this.dataSource;
    }

    /** 参数描述. */
    @Column(name = "description", columnDefinition = "nvarchar(200)")
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    /** 任务定义. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "taskDefinitionId")
    private com.dxn.model.extend.TaskDefinition taskDefinitionId;

    public void setTaskDefinitionId(com.dxn.model.extend.TaskDefinition taskDefinitionId) {
        this.taskDefinitionId = taskDefinitionId;
    }

    public com.dxn.model.extend.TaskDefinition getTaskDefinitionId() {
        if (taskDefinitionId != null) taskDefinitionId.initialization();
        return this.taskDefinitionId;
    }

    @JsonIgnore
    public com.dxn.model.extend.TaskDefinition getTaskDefinitionIdReadOnly() {
        return this.taskDefinitionId;
    }

    @JsonIgnore
    public com.dxn.model.extend.TaskDefinition readTaskDefinitionId() {
        return this.taskDefinitionId;
    }

    public void setTaskDefinition(long id) throws Exception {
        this.setTaskDefinitionId(com.dxn.model.extend.TaskDefinition.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.TaskDefinition getTaskDefinition() {
        return this.getTaskDefinitionId();
    }

    @JsonIgnore
    public boolean getTaskDefinitionIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.taskDefinitionId);
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
    public static com.dxn.model.extend.TaskInterfaceParameters findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.TaskInterfaceParameters.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TaskInterfaceParameters findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.TaskInterfaceParameters.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TaskInterfaceParameters findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.TaskInterfaceParameters.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TaskInterfaceParameters findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.TaskInterfaceParameters.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TaskInterfaceParameters createNew() throws Exception {
        com.dxn.model.extend.TaskInterfaceParameters entity = new com.dxn.model.extend.TaskInterfaceParameters();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.TaskInterfaceParameters> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.TaskInterfaceParameters.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_TaskInterfaceParameters";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "任务接口参数";
    }

}
