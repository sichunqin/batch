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
public class WXEnterpriseApplyEntity extends EntityBase<com.dxn.model.extend.WXEnterpriseApply> {

    /** 应用Id. */
    @Column(name = "agentId", columnDefinition = "nvarchar(50)")
    private String agentId;

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentId() {
        return this.agentId;
    }

    /** 凭据类型. */
    @Column(name = "credentialType", nullable = false)
    private int credentialType;

    public void setCredentialType(int credentialType) {
        this.credentialType = credentialType;
    }

    public int getCredentialType() {
        return this.credentialType;
    }

    /** 启用接收消息. */
    @Column(name = "enableAgentMessage")
    private Integer enableAgentMessage;

    public void setEnableAgentMessage(Integer enableAgentMessage) {
        this.enableAgentMessage = enableAgentMessage;
    }

    public Integer getEnableAgentMessage() {
        return this.enableAgentMessage;
    }

    /** 秘钥. */
    @Column(name = "corpSecret", nullable = false, columnDefinition = "nvarchar(200)")
    private String corpSecret;

    public void setCorpSecret(String corpSecret) {
        this.corpSecret = corpSecret;
    }

    public String getCorpSecret() {
        return this.corpSecret;
    }

    /** 企业微信. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wXEnterpriseWeChatId")
    private com.dxn.model.extend.WXEnterpriseWeChat wXEnterpriseWeChatId;

    public void setWXEnterpriseWeChatId(com.dxn.model.extend.WXEnterpriseWeChat wXEnterpriseWeChatId) {
        this.wXEnterpriseWeChatId = wXEnterpriseWeChatId;
    }

    public com.dxn.model.extend.WXEnterpriseWeChat getWXEnterpriseWeChatId() {
        if (wXEnterpriseWeChatId != null) wXEnterpriseWeChatId.initialization();
        return this.wXEnterpriseWeChatId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WXEnterpriseWeChat getWXEnterpriseWeChatIdReadOnly() {
        return this.wXEnterpriseWeChatId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WXEnterpriseWeChat readWXEnterpriseWeChatId() {
        return this.wXEnterpriseWeChatId;
    }

    public void setWXEnterpriseWeChat(long id) throws Exception {
        this.setWXEnterpriseWeChatId(com.dxn.model.extend.WXEnterpriseWeChat.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WXEnterpriseWeChat getWXEnterpriseWeChat() {
        return this.getWXEnterpriseWeChatId();
    }

    @JsonIgnore
    public boolean getWXEnterpriseWeChatIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.wXEnterpriseWeChatId);
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
    public static com.dxn.model.extend.WXEnterpriseApply findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.WXEnterpriseApply.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WXEnterpriseApply findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.WXEnterpriseApply.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WXEnterpriseApply findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.WXEnterpriseApply.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WXEnterpriseApply findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.WXEnterpriseApply.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WXEnterpriseApply createNew() throws Exception {
        com.dxn.model.extend.WXEnterpriseApply entity = new com.dxn.model.extend.WXEnterpriseApply();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.WXEnterpriseApply> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.WXEnterpriseApply.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_WX_EnterpriseApply";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "企业应用";
    }

}
