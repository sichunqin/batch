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
public class BoxAndNumberBookEntity extends EntityBase<com.dxn.model.extend.BoxAndNumberBook> {

    /** 盒号. */
    @Column(name = "box", columnDefinition = "nvarchar(100)")
    private String box;

    public void setBox(String box) {
        this.box = box;
    }

    public String getBox() {
        return this.box;
    }

    /** 状态. */
    @Column(name = "state", nullable = false)
    private int state;

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
    }

    /** 归还日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "escheatDate")
    private LocalDateTime escheatDate;

    public void setEscheatDate(LocalDateTime escheatDate) {
        this.escheatDate = escheatDate;
    }

    public LocalDateTime getEscheatDate() {
        return this.escheatDate;
    }

    /** 内容. */
    @Column(name = "content", columnDefinition = "nvarchar(1000)")
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    /** 册数范围. */
    @Column(name = "bookRange", nullable = false, columnDefinition = "nvarchar(200)")
    private String bookRange;

    public void setBookRange(String bookRange) {
        this.bookRange = bookRange;
    }

    public String getBookRange() {
        return this.bookRange;
    }

    /** 册数. */
    @Column(name = "book", nullable = false, columnDefinition = "nvarchar(100)")
    private String book;

    public void setBook(String book) {
        this.book = book;
    }

    public String getBook() {
        return this.book;
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

    /** 底稿归档表. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manuScirptItemArchiveId")
    private com.dxn.model.extend.ManuScirptItemArchive manuScirptItemArchiveId;

    public void setManuScirptItemArchiveId(com.dxn.model.extend.ManuScirptItemArchive manuScirptItemArchiveId) {
        this.manuScirptItemArchiveId = manuScirptItemArchiveId;
    }

    public com.dxn.model.extend.ManuScirptItemArchive getManuScirptItemArchiveId() {
        if (manuScirptItemArchiveId != null) manuScirptItemArchiveId.initialization();
        return this.manuScirptItemArchiveId;
    }

    @JsonIgnore
    public com.dxn.model.extend.ManuScirptItemArchive getManuScirptItemArchiveIdReadOnly() {
        return this.manuScirptItemArchiveId;
    }

    @JsonIgnore
    public com.dxn.model.extend.ManuScirptItemArchive readManuScirptItemArchiveId() {
        return this.manuScirptItemArchiveId;
    }

    public void setManuScirptItemArchive(long id) throws Exception {
        this.setManuScirptItemArchiveId(com.dxn.model.extend.ManuScirptItemArchive.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.ManuScirptItemArchive getManuScirptItemArchive() {
        return this.getManuScirptItemArchiveId();
    }

    @JsonIgnore
    public boolean getManuScirptItemArchiveIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.manuScirptItemArchiveId);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BoxAndNumberBook findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.BoxAndNumberBook.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BoxAndNumberBook findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.BoxAndNumberBook.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BoxAndNumberBook findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.BoxAndNumberBook.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BoxAndNumberBook findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.BoxAndNumberBook.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BoxAndNumberBook createNew() throws Exception {
        com.dxn.model.extend.BoxAndNumberBook entity = new com.dxn.model.extend.BoxAndNumberBook();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.BoxAndNumberBook> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.BoxAndNumberBook.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_BoxAndNumberBook";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "底稿盒数和册数";
    }

}
