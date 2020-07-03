package com.dxn.model.extend;

import com.dxn.dto.workFlowConfigIdIdDTO;
import com.dxn.model.entity.RankDepartmentProcessInformationEntity;
import com.dxn.system.ResponseEntity;
import com.dxn.system.annotation.SystemService;
import jdk.nashorn.internal.runtime.options.Option;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "HR_RankDepartmentProcessInformation")
public class RankDepartmentProcessInformation extends RankDepartmentProcessInformationEntity {

    @SystemService(args = "workFlowConfigIdId")
    public void ChangeAuditProjectState(List<workFlowConfigIdIdDTO> workFlowConfigIdId) throws Exception {
        workFlowConfigIdId.get(0);
        for (workFlowConfigIdIdDTO item:workFlowConfigIdId){
            Long id=item.getId();
            Optional<RankDepartmentProcessInformation> rankDepartmentProcessInformationFirst=this.stream().where(c->c.getId()==id).findFirst();
            if (rankDepartmentProcessInformationFirst.isPresent()){
                RankDepartmentProcessInformation rankDepartmentProcessInformation=rankDepartmentProcessInformationFirst.get();
                rankDepartmentProcessInformation.setWorkFlowConfig(item.getWorkFlowConfigId());
                repository().add(rankDepartmentProcessInformation);
            }
        }
    }

}
