package com.dxn.model.extend;

import com.dxn.model.entity.WXEnterpriseWeChatEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.exception.BusinessException;
import com.dxn.weChat.DTO.WeChatApplyDTO;
import com.dxn.weChat.WeChatHelper;


import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Base_WX_EnterpriseWeChat")
public class WXEnterpriseWeChat extends WXEnterpriseWeChatEntity {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        if (Framework.isNotNullOrEmpty(this.getCorpId())) {
            this.setCorpId(this.getCorpId().trim());
        }
    }

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.WeChat);
    }

    @SystemService()
    public Long gainEnterpriseWeChatId() throws Exception {
        Optional<WXEnterpriseWeChat> first = WXEnterpriseWeChat.stream().findFirst();
        if (first.isPresent())
            return first.get().getId();

        return 0L;
    }

    //获取企业应用相关配置以及秘钥
    public static WeChatApplyDTO gainWeChatApply(int tokenType) throws Exception {
        Optional<WXEnterpriseApply> first = WXEnterpriseApply.stream().where(w -> w.getCredentialType() == tokenType).findFirst();
        if (first.isPresent()) {
            WXEnterpriseApply apply = first.get();
            WeChatApplyDTO dto = new WeChatApplyDTO();
            dto.setCorpId(apply.getWXEnterpriseWeChatId().getCorpId());
            dto.setAgentId(apply.getAgentId());
            dto.setCorpSecret(apply.getCorpSecret());
            return dto;
        } else {
            //  throw new BusinessException("请先在企业微信中设置企业应用");
            return null;
        }
    }

    //获取企业应用ID
    public static String gainWeChatCorpId() throws Exception {
        String corpId = "";
        Optional<WXEnterpriseWeChat> first = WXEnterpriseWeChat.stream().findFirst();
        if (first.isPresent()) {
            corpId = first.get().getCorpId();
        }
        return corpId;
    }

    //批量同步企业微信
    @SystemService()
    public void syncBathWeChatDept() throws Exception {
        //企业微信只是支持一级
        Optional<Department> fisrt = Department.stream().where(d -> d.getParentId() == null).findFirst();
        if (!fisrt.isPresent()) return;
        Department dept = fisrt.get();
        //企业微信默认Id，第一级部门不需要同步到企业微信中
        dept.setWeChatDeptId(1);
        syncChildrenDept(dept.getChildren());
    }

    private void syncChildrenDept(List<Department> dept) throws Exception {
        if (dept.size() <= 0) return;
        for (Department item : dept) {
            Integer weChatDeptId = WeChatHelper.synWeChatDeptInfo(item.toWeChatDept());

            if (Framework.isNotNullOrEmpty(weChatDeptId))
                item.setWeChatDeptId(weChatDeptId);

            if (Framework.isNotNullOrEmpty(item.getChildren()))
                syncChildrenDept(item.getChildren());
        }
    }
}
