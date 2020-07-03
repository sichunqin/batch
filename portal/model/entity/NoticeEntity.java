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
public class NoticeEntity extends EntityBase<com.dxn.model.extend.Notice> {

    /** 特殊条件. */
    @Column(name = "specialConditions", columnDefinition = "nvarchar(100)")
    private String specialConditions;

    public void setSpecialConditions(String specialConditions) {
        this.specialConditions = specialConditions;
    }

    public String getSpecialConditions() {
        return this.specialConditions;
    }

    /** 标题. */
    @Column(name = "title", nullable = false, columnDefinition = "nvarchar(500)")
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    /** 内容. */
    @Column(name = "content", nullable = false, columnDefinition = "nvarchar(max)")
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    /** 发送人. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fromId")
    private com.dxn.model.extend.User fromId;

    public void setFromId(com.dxn.model.extend.User fromId) {
        this.fromId = fromId;
    }

    public com.dxn.model.extend.User getFromId() {
        if (fromId != null) fromId.initialization();
        return this.fromId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getFromIdReadOnly() {
        return this.fromId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readFromId() {
        return this.fromId;
    }

    public void setFrom(long id) throws Exception {
        this.setFromId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getFrom() {
        return this.getFromId();
    }

    @JsonIgnore
    public boolean getFromIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.fromId);
    }

    /** 接收人. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "toId")
    private com.dxn.model.extend.User toId;

    public void setToId(com.dxn.model.extend.User toId) {
        this.toId = toId;
    }

    public com.dxn.model.extend.User getToId() {
        if (toId != null) toId.initialization();
        return this.toId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getToIdReadOnly() {
        return this.toId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readToId() {
        return this.toId;
    }

    public void setTo(long id) throws Exception {
        this.setToId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getTo() {
        return this.getToId();
    }

    @JsonIgnore
    public boolean getToIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.toId);
    }

    /** 状态. */
    @Column(name = "status", nullable = false)
    private int status;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    /** 显示状态. */
    @Column(name = "showStatus")
    private Integer showStatus;

    public void setShowStatus(Integer showStatus) {
        this.showStatus = showStatus;
    }

    public Integer getShowStatus() {
        return this.showStatus;
    }

    /** 开始时间. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "beginTime")
    private LocalDateTime beginTime;

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getBeginTime() {
        return this.beginTime;
    }

    /** 结束时间. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "endTime")
    private LocalDateTime endTime;

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    /** 业务实体Id. */
    @Column(name = "entityId")
    private Long entityId;

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getEntityId() {
        return this.entityId;
    }

    /** 业务实体类型. */
    @Column(name = "entityType", columnDefinition = "nvarchar(100)")
    private String entityType;

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityType() {
        return this.entityType;
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
    public static com.dxn.model.extend.Notice findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.Notice.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Notice findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.Notice.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Notice findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.Notice.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Notice findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.Notice.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Notice createNew() throws Exception {
        com.dxn.model.extend.Notice entity = new com.dxn.model.extend.Notice();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.Notice> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.Notice.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_Notice";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "消息通知";
    }

}
