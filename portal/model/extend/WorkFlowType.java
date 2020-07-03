package com.dxn.model.extend;

import com.dxn.model.entity.WorkFlowTypeEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.WorkFlowHelper;
import com.dxn.system.cache.CacheGroup;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "Base_WorkFlowType")
public class WorkFlowType extends WorkFlowTypeEntity {

    @Override
    public void onUpdating() throws Exception {
        this.unused();
    }

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.WorkFlow);
    }

    //运行状态修改时
    private void unused() throws Exception {
        WorkFlowHelper workFlowHelper = new WorkFlowHelper();
        EntityMapItem processStateTypeId = this.getProcessStateTypeId();
        String processStateTypeCode;
        if (Framework.isNullOrEmpty(processStateTypeId)) {
            processStateTypeCode = "DocState";
        } else {
            processStateTypeCode = processStateTypeId.getCode();
        }
        String flowcode = "";
        Long Id = this.getId();
        List<WorkFlowConfig> workFlowConfigList = WorkFlowConfig.stream().where(w -> w.getWorkFlowTypeId().getId() == Id).toList();
        for (WorkFlowConfig item : workFlowConfigList) {
            flowcode += item.getCode() + ",";
        }
        if (Framework.isNotNullOrEmpty(flowcode)) {
            flowcode = flowcode.substring(0, flowcode.length() - 1);
        }
        workFlowHelper.getUnused(flowcode, processStateTypeCode);

    }

    //获取企业微信审批的代办类型编码
    public static String gainWeChatWorkFlowTypeCode() throws Exception {

        String workFlowCode = AppHelper.manage().getCacheUtil().get("gainWeChatWorkFlowTypeCode", () -> {
            try {
                String weChatWorkFlowTypeCode = "";
                List<WorkFlowType> list = WorkFlowType.stream().where(w -> w.getEnableWeChatApproval() == 10).toList();
                if (list.size() > 0) {
                    for (WorkFlowType item : list) {
                        if (Framework.isNotNullOrEmpty(weChatWorkFlowTypeCode))
                            weChatWorkFlowTypeCode += ",";
                        weChatWorkFlowTypeCode += item.getCode();
                    }
                }
                return weChatWorkFlowTypeCode;
            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }

        }, String.class, CacheGroup.WorkFlow);

        return workFlowCode;
    }
}
