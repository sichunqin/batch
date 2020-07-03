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
public class SystemLogEntity extends EntityBase<com.dxn.model.extend.SystemLog> {

    /** 名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(50)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 用户. */
    @ManyToOne(fetch = FetchType.LAZY)
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

    /** 类型. */
    @Column(name = "type", nullable = false, columnDefinition = "nvarchar(50)")
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    /** 消息. */
    @Column(name = "message", columnDefinition = "nvarchar(50)")
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    /** 时间戳. */
    @Column(name = "ticks", nullable = false)
    private double ticks;

    public void setTicks(double ticks) {
        this.ticks = ticks;
    }

    public double getTicks() {
        return this.ticks;
    }

    /** 数据. */
    @Column(name = "data", columnDefinition = "nvarchar(max)")
    private String data;

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return this.data;
    }

    /** 队列. */
    @Column(name = "stackTrace", columnDefinition = "nvarchar(max)")
    private String stackTrace;

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getStackTrace() {
        return this.stackTrace;
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
    public static com.dxn.model.extend.SystemLog findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.SystemLog.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SystemLog findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.SystemLog.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SystemLog findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.SystemLog.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SystemLog findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.SystemLog.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SystemLog createNew() throws Exception {
        com.dxn.model.extend.SystemLog entity = new com.dxn.model.extend.SystemLog();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.SystemLog> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.SystemLog.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_SystemLog";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "系统日志";
    }

}
