package com.dxn.model.extend;

import com.dxn.model.entity.RenewEntity;
import com.dxn.system.Framework;
import com.dxn.system.WorkFlowHelper;
import com.dxn.system.context.AppContext;
import com.dxn.system.context.Profile;
import com.dxn.system.dto.workFlow.MyTodoListDTO;
import com.dxn.system.dto.workFlow.UpdateTodoLog;
import com.dxn.system.dto.workFlow.WorkFlowNode;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Optional;

@Entity
@Table(name = "prj_renew")
public class Renew extends RenewEntity {

    @Override
    public void onSubmitWorkflow(WorkFlowNode node) throws Exception {//提交时 触发
        super.onSubmitWorkflow(node);
        node.setName("提交");
        modifyWorkflow(node);
    }

    @Override
    public void onWorkFlowBack(WorkFlowNode node) throws Exception {//退回时触发
        super.onWorkFlowBack(node);
        node.setName("退回");
        modifyWorkflow(node);
    }

    @Override
    public void onApproveWorkflow(WorkFlowNode node) throws Exception {//批准触发
        super.onApproveWorkflow(node);

        modifyWorkflow(node);
    }

    @Override
    public void onEndWorkflow(WorkFlowNode node) throws Exception {//结束时触发
        super.onEndWorkflow(node);
        node.setName("结束");
        modifyWorkflow(node);
        myUpdate();
    }

    private void modifyWorkflow(WorkFlowNode node) throws Exception {
        //审批流中增加一条记录
        UpdateTodoLog todoLog = new UpdateTodoLog();
        Profile profile = AppContext.current().getProfile();
        todoLog.setActorId(profile.getId().toString());
        todoLog.setActorName(profile.getName());
        todoLog.setEntityId(this.getBorrowChildId().getId().toString());
        todoLog.setEntityType("BorrowChild");
        todoLog.setXmid("我的借阅续借:" + node.getName());
        todoLog.setTaskInstanceName("我的借阅续借:" + node.getName());
        todoLog.setProcessId("续借审批流");
        todoLog.setJdtype(this.getBorrowReason());
        if ("提交".equals(node.getName())) {
            todoLog.setComments("续借天数：" + this.getBorrowDays() + "天，续借理由：" + this.getBorrowReason());
        }

        WorkFlowHelper.saveTodoLog(todoLog);
    }

    @Override
    public void onInserting() throws Exception {
        super.onInserting();
        this.setDeptId(this.getBorrowChildId().getDeptId());
        // myUpdate();
    }

    private void myUpdate() throws Exception {
        //审批通过时 再修改预计归还日期
//        Renew oldrenew = this.getOriginalValue();
//        //新增 或者修改续借天数
//        if (Framework.isNullOrEmpty(oldrenew) || !oldrenew.getBorrowDays().equals(this.getBorrowDays())) {
        BorrowChild bchild = this.getBorrowChild();
        bchild.setBorrowDays(bchild.getBorrowDays() + this.getBorrowDays());
        bchild.setBorrowReason(this.getBorrowReason());
        this.repository().add(bchild);
//        }
    }

    @JsonIgnore
    public MyTodoListDTO gainWorkFlowDTO(MyTodoListDTO value) throws Exception {
        value.setDepartmentType("续借流程");
        String processId = value.getProcess_Id();
        User user = this.getCreatedBy();
        Optional<WorkFlowConfig> workFlowConfiglist = WorkFlowConfig.stream().where(w -> w.getProcessId().equals(processId)).findFirst();
        if (workFlowConfiglist.isPresent()) {
            String deptIdName=user.getDept().getTreeName();
            if (Framework.isNullOrEmpty(deptIdName)){
                deptIdName=user.getDept().getName();
            }
            if (workFlowConfiglist.isPresent()){
                String workFlowConfigName=workFlowConfiglist.get().getWorkFlowTypeId().getName();
                value.setWorkFlowName(deptIdName + "-" +workFlowConfigName);
            }
        }
        if (Framework.isNotNullOrEmpty(user)) {
            if (Framework.isNotNullOrEmpty(user.getDept())) {
                value.setDepartment(user.getDept().getTreeName());
            }
        }
        return value;
    }

}
