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
public class WXEnterpriseWeChatEntity extends EntityBase<com.dxn.model.extend.WXEnterpriseWeChat> {

    /** 企业标识. */
    @Column(name = "corpId", nullable = false, columnDefinition = "nvarchar(50)")
    private String corpId;

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpId() {
        return this.corpId;
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

    /** 企业应用列表. */
    @OneToMany(mappedBy = "wXEnterpriseWeChatId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.WXEnterpriseApply> wXEnterpriseApplys = new java.util.ArrayList<>();

    public void setWXEnterpriseApplys(java.util.List<com.dxn.model.extend.WXEnterpriseApply> wXEnterpriseApplys) {
        this.wXEnterpriseApplys = wXEnterpriseApplys;
    }

    public java.util.List<com.dxn.model.extend.WXEnterpriseApply> getWXEnterpriseApplys() {
        if (this.wXEnterpriseApplys != null) {
           for (com.dxn.model.extend.WXEnterpriseApply item : this.wXEnterpriseApplys) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.wXEnterpriseApplys;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.WXEnterpriseApply> readWXEnterpriseApplys() {
        return this.wXEnterpriseApplys;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.WXEnterpriseApply> getWXEnterpriseApplysReadOnly() {
        return this.wXEnterpriseApplys;
    }

    @JsonIgnore
    public static com.dxn.model.extend.WXEnterpriseWeChat findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.WXEnterpriseWeChat.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WXEnterpriseWeChat findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.WXEnterpriseWeChat.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WXEnterpriseWeChat findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.WXEnterpriseWeChat.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WXEnterpriseWeChat findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.WXEnterpriseWeChat.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WXEnterpriseWeChat createNew() throws Exception {
        com.dxn.model.extend.WXEnterpriseWeChat entity = new com.dxn.model.extend.WXEnterpriseWeChat();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.WXEnterpriseWeChat> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.WXEnterpriseWeChat.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_WX_EnterpriseWeChat";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "企业微信";
    }

}
