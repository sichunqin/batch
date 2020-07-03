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
public class TaskDefinitionEntity extends EntityBase<com.dxn.model.extend.TaskDefinition> {

    /** 任务逻辑代码. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(250)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 任务定义. */
    @Column(name = "name", columnDefinition = "nvarchar(500)")
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

    /** 任务接口参数列表. */
    @OneToMany(mappedBy = "taskDefinitionId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.TaskInterfaceParameters> taskInterfaceParameterss = new java.util.ArrayList<>();

    public void setTaskInterfaceParameterss(java.util.List<com.dxn.model.extend.TaskInterfaceParameters> taskInterfaceParameterss) {
        this.taskInterfaceParameterss = taskInterfaceParameterss;
    }

    public java.util.List<com.dxn.model.extend.TaskInterfaceParameters> getTaskInterfaceParameterss() {
        if (this.taskInterfaceParameterss != null) {
           for (com.dxn.model.extend.TaskInterfaceParameters item : this.taskInterfaceParameterss) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.taskInterfaceParameterss;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.TaskInterfaceParameters> readTaskInterfaceParameterss() {
        return this.taskInterfaceParameterss;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.TaskInterfaceParameters> getTaskInterfaceParameterssReadOnly() {
        return this.taskInterfaceParameterss;
    }

    @JsonIgnore
    public static com.dxn.model.extend.TaskDefinition findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.TaskDefinition.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TaskDefinition findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.TaskDefinition.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TaskDefinition findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.TaskDefinition.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TaskDefinition findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.TaskDefinition.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TaskDefinition createNew() throws Exception {
        com.dxn.model.extend.TaskDefinition entity = new com.dxn.model.extend.TaskDefinition();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.TaskDefinition> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.TaskDefinition.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_TaskDefinition";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "任务定义";
    }

}
