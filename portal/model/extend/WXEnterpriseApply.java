package com.dxn.model.extend;

import com.dxn.model.entity.WXEnterpriseApplyEntity;
import com.dxn.system.Framework;
import com.dxn.system.exception.BusinessException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Optional;

@Entity
@Table(name = "Base_WX_EnterpriseApply")
public class WXEnterpriseApply extends WXEnterpriseApplyEntity {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        if (Framework.isNotNullOrEmpty(this.getAgentId())) {
            this.setAgentId(this.getAgentId().trim());
        }

        if (Framework.isNotNullOrEmpty(this.getCorpSecret())) {
            this.setCorpSecret(this.getCorpSecret().trim());
        }
    }

    @Override
    public void onValidate() throws Exception {
        super.onValidate();
        int type = this.getCredentialType();
        Long id = this.getId();
        Optional<WXEnterpriseApply> first = WXEnterpriseApply.stream().where(w -> w.getCredentialType() == type && w.getId() != id).findFirst();
        if (first.isPresent()) {
            throw new BusinessException("已存在该应用，不能重复添加！");
        }
    }
}
