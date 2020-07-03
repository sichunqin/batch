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
public class AdviseEntity extends EntityBase<com.dxn.model.extend.Advise> {

    /** 最终页面展示内容. */
    @Column(name = "contentFinal", columnDefinition = "nvarchar(max)")
    private String contentFinal;

    public void setContentFinal(String contentFinal) {
        this.contentFinal = contentFinal;
    }

    public String getContentFinal() {
        return this.contentFinal;
    }

    /** 首页组件. */
    @Column(name = "homeComponent", columnDefinition = "nvarchar(200)")
    private String homeComponent;

    public void setHomeComponent(String homeComponent) {
        this.homeComponent = homeComponent;
    }

    public String getHomeComponent() {
        return this.homeComponent;
    }

    /** 通知编码. */
    @Column(name = "code", columnDefinition = "nvarchar(30)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 发布状态. */
    @Column(name = "releaseState")
    private Integer releaseState;

    public void setReleaseState(Integer releaseState) {
        this.releaseState = releaseState;
    }

    public Integer getReleaseState() {
        return this.releaseState;
    }

    /** 发布日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "releaseDate")
    private LocalDateTime releaseDate;

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDateTime getReleaseDate() {
        return this.releaseDate;
    }

    /** 发布部门. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deptId")
    private com.dxn.model.extend.Department deptId;

    public void setDeptId(com.dxn.model.extend.Department deptId) {
        this.deptId = deptId;
    }

    public com.dxn.model.extend.Department getDeptId() {
        if (deptId != null) deptId.initialization();
        return this.deptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getDeptIdReadOnly() {
        return this.deptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department readDeptId() {
        return this.deptId;
    }

    public void setDept(long id) throws Exception {
        this.setDeptId(com.dxn.model.extend.Department.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getDept() {
        return this.getDeptId();
    }

    @JsonIgnore
    public boolean getDeptIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.deptId);
    }

    /** 发布人. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "releaserId")
    private com.dxn.model.extend.User releaserId;

    public void setReleaserId(com.dxn.model.extend.User releaserId) {
        this.releaserId = releaserId;
    }

    public com.dxn.model.extend.User getReleaserId() {
        if (releaserId != null) releaserId.initialization();
        return this.releaserId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getReleaserIdReadOnly() {
        return this.releaserId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readReleaserId() {
        return this.releaserId;
    }

    public void setReleaser(long id) throws Exception {
        this.setReleaserId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getReleaser() {
        return this.getReleaserId();
    }

    @JsonIgnore
    public boolean getReleaserIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.releaserId);
    }

    /** 接收单位. */
    @Column(name = "receiveDept", nullable = false, columnDefinition = "nvarchar(max)")
    private String receiveDept;

    public void setReceiveDept(String receiveDept) {
        this.receiveDept = receiveDept;
    }

    public String getReceiveDept() {
        return this.receiveDept;
    }

    /** 接收单位. */
    @Column(name = "receiveDeptString", columnDefinition = "nvarchar(max)")
    private String receiveDeptString;

    public void setReceiveDeptString(String receiveDeptString) {
        this.receiveDeptString = receiveDeptString;
    }

    public String getReceiveDeptString() {
        return this.receiveDeptString;
    }

    /** 内容. */
    @Column(name = "content", columnDefinition = "nvarchar(max)")
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    /** 标题. */
    @Column(name = "title", columnDefinition = "nvarchar(500)")
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
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

    /** 通知接受部门列表. */
    @OneToMany(mappedBy = "adviseId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.AdviseReceiveDept> adviseReceiveDepts = new java.util.ArrayList<>();

    public void setAdviseReceiveDepts(java.util.List<com.dxn.model.extend.AdviseReceiveDept> adviseReceiveDepts) {
        this.adviseReceiveDepts = adviseReceiveDepts;
    }

    public java.util.List<com.dxn.model.extend.AdviseReceiveDept> getAdviseReceiveDepts() {
        if (this.adviseReceiveDepts != null) {
           for (com.dxn.model.extend.AdviseReceiveDept item : this.adviseReceiveDepts) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.adviseReceiveDepts;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.AdviseReceiveDept> readAdviseReceiveDepts() {
        return this.adviseReceiveDepts;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.AdviseReceiveDept> getAdviseReceiveDeptsReadOnly() {
        return this.adviseReceiveDepts;
    }

    @JsonIgnore
    public static com.dxn.model.extend.Advise findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.Advise.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Advise findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.Advise.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Advise findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.Advise.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Advise findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.Advise.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Advise createNew() throws Exception {
        com.dxn.model.extend.Advise entity = new com.dxn.model.extend.Advise();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.Advise> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.Advise.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_Advise";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "通知";
    }

}
