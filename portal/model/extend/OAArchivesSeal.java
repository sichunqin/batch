package com.dxn.model.extend;

import com.dxn.dto.OA.OAWorkFlowDTO;
import com.dxn.dto.enums.Constants;
import com.dxn.model.entity.OAArchivesSealEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.context.AppContext;
import com.dxn.system.dto.workFlow.MyTodoListDTO;
import com.dxn.system.dto.workFlow.WorkFlowNode;
import com.dxn.system.exception.BusinessException;
import com.dxn.system.exception.WorkFlowException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Prj_OAArchivesSeal")
public class OAArchivesSeal extends OAArchivesSealEntity {
    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        Long deptId = AppContext.current().getProfile().getDeptId();
        Optional<Department> deptment = Department.stream().where(c -> c.getId().equals(deptId)).findFirst();
        if (deptment.isPresent()) {
            this.setDeptId(deptment.get());
        }
        if (this.getOAType() == 30 && Framework.isNotNullOrEmpty(this.getCustomerId()))
            this.setPurpose(this.getCustomerId().getName());
    }

    @JsonIgnore
    public MyTodoListDTO gainWorkFlowDTO(MyTodoListDTO value) throws Exception {
        User user = this.getCreatedBy();
        String deptIdName = user.getDeptReadOnly().getOrgReadOnly().getNickname();
        value.setWorkFlowName(value.getProcess_Id());
        String code = "";
        if (Framework.isNotNullOrEmpty(this.getCustomerId())) {
            code = this.getCustomerId().getCode();
        }
        String workflowUuid = value.getWorkflowUuid();
        Optional<WorkFlowConfig> workFlowConfigList = WorkFlowConfig.stream().where(w -> w.getUuid().equals(workflowUuid)).findFirst();
        if (workFlowConfigList.isPresent()) {
            String workFlowConfigName = workFlowConfigList.get().getWorkFlowTypeId().getName();
            value.setWorkFlowName(deptIdName + "-" + workFlowConfigName);
        }
        value.setDepartment(user.getDeptReadOnly().getTreeName());
        value.setDepartmentType(code);
        value.setToDoDescription(this.getPurpose());
        return value;//WorkFlowName
    }

    //验证方法
    @Override
    public void onValidate() throws Exception {
        super.onValidate();
        if (Framework.isNullOrEmpty(this.getDocState())) {
            this.setDocState(10);
        }
        if (this.getOAType() == 30) {
            if (Framework.isNullOrEmpty(this.getCustomerId())) throw new BusinessException("请输入客户名称");
            if (this.getCustomerId().getCustomerNature() == 10 || this.getCustomerId().getCustomerNature() == 30) {
                if (Framework.isNullOrEmpty(this.getProjectId())) throw new BusinessException("请输入关联项目");
            }
        }
    }

    //插入前
    @Override
    public void onInserting() throws Exception {
        super.onInserting();
        this.setSignCompletion(20);
        this.setPrintedNumber(0);
    }

    @Override
    public void onSubmitWorkflow(WorkFlowNode node) throws Exception {
        Long id = this.getId();
        Long count = OAArchivesSealAttachment.stream().where(c -> c.getOAArchivesSealId().getId().equals(id)).count();
        if (count == 0) throw new BusinessException("没有附件不能提交!");
    }

    @SystemService()
    public List<OAWorkFlowDTO> gainWorkFlowByUserId() throws Exception {
        Long deptId = AppContext.current().getProfile().getDeptId();
        Optional<Department> first = Department.stream().where(c -> c.getId().equals(deptId)).findFirst();
        if (!first.isPresent()) throw new BusinessException("当前登录人部门不能为空!");
        Department department = first.get();
        if (department.getUnitAttributes() == Constants.B10 || department.getUnitAttributes() == Constants.B20) {
            return gainWorFlowList(department);
        } else {
            Department org = department.getOrg();
            if (Framework.isNullOrEmpty(org)) throw new BusinessException("当前登录人分所不能为空!");
            return gainWorFlowList(org);
        }
    }

    public List<OAWorkFlowDTO> gainWorFlowList(Department dept) throws Exception {
        List<OAWorkFlowDTO> list = new ArrayList<>();
        if (Framework.isNotNullOrEmpty(dept.getAdministrationApprovalId())) {
            list.add(gainWorkNodel(dept.getAdministrationApprovalId().getId(), 10));
        }
        if (Framework.isNotNullOrEmpty(dept.getLicenseExaminationApprovalId())) {
            list.add(gainWorkNodel(dept.getLicenseExaminationApprovalId().getId(), 20));
        }
        if (Framework.isNotNullOrEmpty(dept.getAgreementApprovalId())) {
            list.add(gainWorkNodel(dept.getAgreementApprovalId().getId(), 30));
        }
        return list;
    }

    public OAWorkFlowDTO gainWorkNodel(Long workFlowId, int index) throws Exception {
        Optional<WorkFlowConfig> workConfig = WorkFlowConfig.stream().where(c -> c.getId().equals(workFlowId)).findFirst();
        if (!workConfig.isPresent()) throw new BusinessException("找不到可用的流程!");
        OAWorkFlowDTO dto = new OAWorkFlowDTO();
        dto.setId(index);
        dto.setText(oATypeEnum(index));
        return dto;
    }

    public String oATypeEnum(int index) throws Exception {

        switch (index) {
            case 10:
                return "行政";
            case 20:
                return "大信证照";
            case 30:
                return "业务约定书";
        }
        return "";
    }

    @Override
    public String getUseWorkflowDefine(String json) throws WorkFlowException {
        Department dept = this.getDeptId();
        Integer oaType = this.getOAType();
        String node;
        if (oaType == Constants.B10) {
            node = Framework.isNotNullOrEmpty(dept.getOrg().getAdministrationApprovalId()) ? dept.getOrg().getAdministrationApprovalId().getUuid() : "";
            if (Framework.isNullOrEmpty(node))
                throw new WorkFlowException("没有找到对应的行政审批流程!");
        } else if (oaType == Constants.B20) {
            node = Framework.isNotNullOrEmpty(dept.getOrg().getLicenseExaminationApprovalId()) ? dept.getOrg().getLicenseExaminationApprovalId().getUuid() : "";
            if (Framework.isNullOrEmpty(node))
                throw new WorkFlowException("没有找到对应的大信证件审批流程!");
        } else {
            node = Framework.isNotNullOrEmpty(dept.getOrg().getAgreementApprovalId()) ? dept.getOrg().getAgreementApprovalId().getUuid() : "";
            if (Framework.isNullOrEmpty(node))
                throw new WorkFlowException("没有找到对应的行政业务审批流程!");
        }
        return node;
    }

    @SystemService(args = "oAArchivesSealId")
    public void putOAArchivesSealIsSign(Long oAArchivesSealId) throws Exception {
        OAArchivesSeal oAArchivesSeal = this.findById(oAArchivesSealId);
        oAArchivesSeal.setSignCompletion(10);
    }

    @Override
    public void onEndWorkflow(WorkFlowNode node) throws Exception {//结束时触发
        super.onEndWorkflow(node);
        node.setName("结束");
        String conetnt = String.format("OA文档用印申请审批通过：%s", oATypeEnum(this.getOAType()) + this.getPurpose());
        sendNotice(this, conetnt, conetnt);
    }

    private void sendNotice(OAArchivesSeal oAArchivesSeal, String title, String conetnt) throws Exception {
        Notice notice = Notice.createNew();
        List<Long> users = new ArrayList<>();
        users.add(oAArchivesSeal.getCreatedById());
        notice.createNotice(AppContext.current().getProfile().getId(), users, title, conetnt, oAArchivesSeal.getId(), "OAArchivesSeal", null);
    }


}
