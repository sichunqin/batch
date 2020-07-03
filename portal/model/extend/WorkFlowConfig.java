package com.dxn.model.extend;

import com.dxn.dto.WorkFlowConfigDTO;
import com.dxn.model.entity.WorkFlowConfigEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.WorkFlowHelper;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.dto.KeyValue;
import com.dxn.system.exception.BusinessException;
import com.dxn.system.exception.WorkFlowException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Base_WorkFlowConfig")
public class WorkFlowConfig extends WorkFlowConfigEntity {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        WorkFlowHelper.newProcess(this.getTechnologicalProcess());
    }

    @Override
    public void onValidate() throws Exception {
        this.validateCode();
        this.getTechnologicalProcess();
    }

    @Override
    public void onInserting() throws Exception {
        super.onInserting();
        this.setProcessId(this.getName());
    }

    private void validateCode() throws Exception {
        String code = this.getCode();
        String name = this.getName();
        if (Framework.isNullOrEmpty(code)) throw new BusinessException("流程编码不能为空!");
        if (Framework.isNullOrEmpty(name)) throw new BusinessException("流程名称不能为空!");

        Boolean isHave;
        Long id = this.getId();
        if (Framework.isNullOrEmpty(id)) {
            isHave = this.stream().anyMatch(w -> w.getCode().equals(code));
        } else {
            isHave = this.stream().anyMatch(w -> w.getCode().equals(code) && !w.getId().equals(id));
        }
        if (isHave) throw new BusinessException("流程编码不能重复!");

    }

    private String getTechnologicalProcess() {
        WorkFlowConfigDTO workFlowConfigDTO = new WorkFlowConfigDTO();
        workFlowConfigDTO.setCode(this.getCode());
        workFlowConfigDTO.setName(this.getName());
        workFlowConfigDTO.setProcessId(this.getName());
        workFlowConfigDTO.setEntityCode(this.getWorkFlowType().getEntityMapId().getCode());
        workFlowConfigDTO.setFlowType(this.getWorkFlowType().getName());
        workFlowConfigDTO.setPublishUser("admin");
        workFlowConfigDTO.setUuid(this.getUuid());
        String ProcessStateTypeCode;
        if (this.getWorkFlowTypeId().getProcessStateTypeId() == null) {
            ProcessStateTypeCode = "DocState";
        } else {
            ProcessStateTypeCode = this.getWorkFlowTypeId().getProcessStateTypeId().getCode();
        }
        workFlowConfigDTO.setProcessStateTypeCode(ProcessStateTypeCode);
        workFlowConfigDTO.setToDoDescription(this.getWorkFlowTypeId().getToDoDescription());


        return Framework.toJson(workFlowConfigDTO);
    }

    @JsonIgnore
    public String getWrokFlowFormCode(String entityType, String workflowDefine) throws Exception {

        String key = String.format("getWrokFlowFormCode-%s-%s", entityType, workflowDefine);

        String formCode = AppHelper.manage().getCacheUtil().get(key, () -> {

            try {

                List<WorkFlowConfig> list = this.stream().where(w -> w.getProcessId() == workflowDefine && w.getWorkFlowTypeId() != null).toList();
                if (list.size() > 1) throw new WorkFlowException(String.format("流程 %s 配置不能超过一个!", workflowDefine));
                if (list.size() <= 0) throw new WorkFlowException(String.format("未找到流程 %s 配置！", workflowDefine));

                WorkFlowConfig config = list.get(0);

                if (Framework.isNotNullOrEmpty(config.getWorkFlowTypeId())) {
                    if (Framework.isNotNullOrEmpty(config.getWorkFlowTypeId().getFormPageId())) {
                        FormPage formPage = config.getWorkFlowTypeId().getFormPageId();
                        if (Framework.isNotNullOrEmpty(formPage.getCode())) return formPage.getCode();
                    }
                }

                throw new WorkFlowException(String.format("请配置流程 %s 表单！", workflowDefine));

            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }

        }, String.class, CacheGroup.WorkFlow);

        if (Framework.isNullOrEmpty(formCode))
            throw new WorkFlowException(String.format("流程 %s 表单配置不正确！", workflowDefine));

        return formCode;
    }

    @JsonIgnore
    public KeyValue toWorkFlowKeyValue() {
        if (Framework.isNullOrEmpty(this.getWorkFlowTypeId())) return null;
        KeyValue value = new KeyValue();
        value.setKey(this.getWorkFlowTypeId().getCode());
        value.setValue(this.getUuid());
        return value;
    }

    @SystemService(args = "processId ,uuid")
    public String userReprint(String processId, String uuid) throws Exception {
        //List<WorkFlowConfig> workFlowConfigList = WorkFlowConfig.stream().where(w -> w.getName().equals(processId)).toList();
        return WorkFlowHelper.userReprint(processId, uuid);
    }

    //项目状态修改
    public void projectStatus(Long projectId) throws Exception {
        List<Project> list = Project.stream().where(w -> w.getId() == projectId).toList();
        if (list.size() > 0) {
            Project p = list.get(0);
            p.initialization();
            p.setCompositionState(20);
            repository().add(p);
            repository().commit();
        }
    }

    public String gainToDoDescription(String uuid) throws Exception {

        String key = String.format("gainToDoDescription-%s", uuid);

        String description = AppHelper.manage().getCacheUtil().get(key, () -> {

            try {
                if (Framework.isNullOrEmpty(uuid)) throw new BusinessException("流程名称不能为空，无法查询待办描述配置!");
                Optional<WorkFlowConfig> fisrt = this.stream().filter(f -> f.getUuid().equalsIgnoreCase(uuid)).findFirst();
                //if (!fisrt.isPresent()) throw new BusinessException(String.format("未找到 %s 对应的流程!", name));//根据名称查询
                if (!fisrt.isPresent()) throw new BusinessException("未找到对应的流程!");
                WorkFlowConfig config = fisrt.get();
                if (Framework.isNullOrEmpty(config.getWorkFlowTypeId()))
                    // throw new BusinessException(String.format("流程 %s 找不到流程类型!", name));
                    throw new BusinessException("该流程找不到流程类型!");
                return config.getWorkFlowTypeId().getToDoDescription();

            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }

        }, String.class, CacheGroup.WorkFlow);

        Framework.showSystemMes();

        if (Framework.isNullOrEmpty(description))
            //throw new WorkFlowException(String.format("流程 %s 未找到待办描述配置！", name));
            throw new WorkFlowException("流程未找到待办描述配置！");

        return description;
    }

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.WorkFlow);
    }

    //获取企业微信审批的代办类型编码
    public static String gainWeChatWorkFlowCode() throws Exception {
        String workFlowCode = AppHelper.manage().getCacheUtil().get("gainWeChatWorkFlowCode", () -> {
            String weChatWorkFlowCode = "";
            try {
                List<WorkFlowType> list = WorkFlowType.stream().where(w -> w.getEnableWeChatApproval() == 10).toList();
                if (list.size() > 0) {
                    for (WorkFlowType item : list) {
                        Long workFlowId = item.getId();
                        List<WorkFlowConfig> configs = WorkFlowConfig.stream().where(w -> w.getWorkFlowTypeId() != null && w.getWorkFlowTypeId().getId().equals(workFlowId)).toList();
                        if (configs.size() > 0) {
                            for (WorkFlowConfig first : configs) {
                                if (Framework.isNotNullOrEmpty(weChatWorkFlowCode))
                                    weChatWorkFlowCode += ",";
                                weChatWorkFlowCode += first.getCode();
                            }
                        }
                    }
                }
                return weChatWorkFlowCode;
            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }

        }, String.class, CacheGroup.WorkFlow);

        return workFlowCode;
    }
}

