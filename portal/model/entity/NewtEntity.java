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
public class NewtEntity extends EntityBase<com.dxn.model.extend.Newt> {

    /** 名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(800)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 金额. */
    @Column(name = "money", nullable = false, columnDefinition = "decimal(20,2)")
    private BigDecimal money;

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getMoney() {
        return this.money;
    }

    /** 图片. */
    @Column(name = "perId", nullable = false, columnDefinition = "nvarchar(600)")
    private String perId;

    public void setPerId(String perId) {
        this.perId = perId;
    }

    public String getPerId() {
        return this.perId;
    }

    /** 编码. */
    @Column(name = "fire", nullable = false)
    private int fire;

    public void setFire(int fire) {
        this.fire = fire;
    }

    public int getFire() {
        return this.fire;
    }

    /** 类型. */
    @Column(name = "flag")
    private Double flag;

    public void setFlag(Double flag) {
        this.flag = flag;
    }

    public Double getFlag() {
        return this.flag;
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
    public static com.dxn.model.extend.Newt findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.Newt.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Newt findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.Newt.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Newt findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.Newt.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Newt findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.Newt.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Newt createNew() throws Exception {
        com.dxn.model.extend.Newt entity = new com.dxn.model.extend.Newt();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.Newt> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.Newt.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_Newt";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "测试";
    }

}
