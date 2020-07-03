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
public class NoFunctionalUploadControlEntity extends EntityBase<com.dxn.model.extend.NoFunctionalUploadControl> {

    /** 文件大小. */
    @Column(name = "fileSize", nullable = false, columnDefinition = "nvarchar(100)")
    private String fileSize;

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileSize() {
        return this.fileSize;
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
    public static com.dxn.model.extend.NoFunctionalUploadControl findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.NoFunctionalUploadControl.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.NoFunctionalUploadControl findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.NoFunctionalUploadControl.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.NoFunctionalUploadControl findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.NoFunctionalUploadControl.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.NoFunctionalUploadControl findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.NoFunctionalUploadControl.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.NoFunctionalUploadControl createNew() throws Exception {
        com.dxn.model.extend.NoFunctionalUploadControl entity = new com.dxn.model.extend.NoFunctionalUploadControl();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.NoFunctionalUploadControl> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.NoFunctionalUploadControl.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_NoFunctionalUploadControl";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "非功能上传控制";
    }

}
