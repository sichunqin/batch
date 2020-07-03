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
public class IndependenceSignEntity extends EntityBase<com.dxn.model.extend.IndependenceSign> {

    /** 年度. */
    @Column(name = "year", nullable = false)
    private int year;

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return this.year;
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
    public static com.dxn.model.extend.IndependenceSign findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.IndependenceSign.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.IndependenceSign findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.IndependenceSign.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.IndependenceSign findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.IndependenceSign.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.IndependenceSign findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.IndependenceSign.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.IndependenceSign createNew() throws Exception {
        com.dxn.model.extend.IndependenceSign entity = new com.dxn.model.extend.IndependenceSign();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.IndependenceSign> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.IndependenceSign.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "AI_IndependenceSign";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "独立性签字";
    }

}
