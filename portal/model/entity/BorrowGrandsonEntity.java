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
public class BorrowGrandsonEntity extends EntityBase<com.dxn.model.extend.BorrowGrandson> {

    /** 盒号. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boxAndNumberBookId")
    private com.dxn.model.extend.BoxAndNumberBook boxAndNumberBookId;

    public void setBoxAndNumberBookId(com.dxn.model.extend.BoxAndNumberBook boxAndNumberBookId) {
        this.boxAndNumberBookId = boxAndNumberBookId;
    }

    public com.dxn.model.extend.BoxAndNumberBook getBoxAndNumberBookId() {
        if (boxAndNumberBookId != null) boxAndNumberBookId.initialization();
        return this.boxAndNumberBookId;
    }

    @JsonIgnore
    public com.dxn.model.extend.BoxAndNumberBook getBoxAndNumberBookIdReadOnly() {
        return this.boxAndNumberBookId;
    }

    @JsonIgnore
    public com.dxn.model.extend.BoxAndNumberBook readBoxAndNumberBookId() {
        return this.boxAndNumberBookId;
    }

    public void setBoxAndNumberBook(long id) throws Exception {
        this.setBoxAndNumberBookId(com.dxn.model.extend.BoxAndNumberBook.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.BoxAndNumberBook getBoxAndNumberBook() {
        return this.getBoxAndNumberBookId();
    }

    @JsonIgnore
    public boolean getBoxAndNumberBookIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.boxAndNumberBookId);
    }

    /** 借阅申请子表. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "borrowChildId")
    private com.dxn.model.extend.BorrowChild borrowChildId;

    public void setBorrowChildId(com.dxn.model.extend.BorrowChild borrowChildId) {
        this.borrowChildId = borrowChildId;
    }

    public com.dxn.model.extend.BorrowChild getBorrowChildId() {
        if (borrowChildId != null) borrowChildId.initialization();
        return this.borrowChildId;
    }

    @JsonIgnore
    public com.dxn.model.extend.BorrowChild getBorrowChildIdReadOnly() {
        return this.borrowChildId;
    }

    @JsonIgnore
    public com.dxn.model.extend.BorrowChild readBorrowChildId() {
        return this.borrowChildId;
    }

    public void setBorrowChild(long id) throws Exception {
        this.setBorrowChildId(com.dxn.model.extend.BorrowChild.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.BorrowChild getBorrowChild() {
        return this.getBorrowChildId();
    }

    @JsonIgnore
    public boolean getBorrowChildIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.borrowChildId);
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
    public static com.dxn.model.extend.BorrowGrandson findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.BorrowGrandson.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BorrowGrandson findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.BorrowGrandson.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BorrowGrandson findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.BorrowGrandson.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BorrowGrandson findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.BorrowGrandson.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BorrowGrandson createNew() throws Exception {
        com.dxn.model.extend.BorrowGrandson entity = new com.dxn.model.extend.BorrowGrandson();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.BorrowGrandson> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.BorrowGrandson.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "prj_BorrowGrandson";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "借阅申请孙表";
    }

}
