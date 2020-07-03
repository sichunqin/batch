package com.dxn.model.extend;

import com.dxn.model.entity.DestructionRecordsEntity;
import com.dxn.system.Framework;
import com.dxn.system.dto.workFlow.MyTodoListDTO;
import com.dxn.system.dto.workFlow.WorkFlowNode;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Optional;

@Entity
@Table(name = "Prj_DestructionRecords")
public class DestructionRecords extends DestructionRecordsEntity {
    @JsonIgnore
    public MyTodoListDTO gainWorkFlowDTO(MyTodoListDTO value) throws Exception {
        value.setDepartmentType("档案销毁");
        User user = this.getCreatedBy();
        if (Framework.isNotNullOrEmpty(user)) {
            if (Framework.isNotNullOrEmpty(user.getDeptReadOnly())) {
                value.setDepartment(user.getDeptReadOnly().getTreeName());
            }
        }
        String processId = value.getProcess_Id();
        Optional<WorkFlowConfig> workFlowConfiglist = WorkFlowConfig.stream().where(w -> w.getProcessId().equals(processId)).findFirst();
        if (workFlowConfiglist.isPresent()) {
            WorkFlowConfig workFlowConfig = workFlowConfiglist.get();
            value.setWorkFlowName(user.getDeptReadOnly().getOrgReadOnly().getNickname() + "-" + workFlowConfig.getWorkFlowTypeId().getName());
        }
        return value;
    }

    @Override
    public void onSubmitWorkflow(WorkFlowNode node) throws Exception {
        super.onSubmitWorkflow(node);
        for (DestructionDetail item : this.getDestructionDetails()) {
            Long id = item.getManuScirptItemArchiveId().getCompostionCustomerId().getId();
            Optional<CompositionCustomer> model = CompositionCustomer.stream().where(c -> c.getId().equals(id)).findFirst();
            model.get().initialization();
            model.get().setDestructionRecordsState(20);
            model.get().setDestructionRecordsId(this.getId());
            repository().add(model.get());
        }

    }

    @Override
    public void onEndWorkflow(WorkFlowNode node) throws Exception {
        super.onEndWorkflow(node);

        for (DestructionDetail item : this.getDestructionDetails()) {
            Long id = item.getManuScirptItemArchiveId().getCompostionCustomerId().getId();
            Optional<CompositionCustomer> model = CompositionCustomer.stream().where(c -> c.getId().equals(id)).findFirst();
            model.get().initialization();
            model.get().setDestructionRecordsState(10);
            model.get().setDestructionRecordsId(this.getId());
            repository().add(model.get());
        }
    }
}
