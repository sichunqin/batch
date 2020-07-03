package com.dxn.model.extend;

import com.dxn.model.entity.CustomStaffFlowTableEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.awt.*;
import java.util.List;

@Entity
@Table(name = "HR_CustomStaffFlowTable")
public class CustomStaffFlowTable extends CustomStaffFlowTableEntity {

    @Override
    public void onInserted() throws Exception {
        super.onInserted();
        //业务代码
        //新增后给职级部门流程表增加数据
        newlyAddedRankDepartmentProcessInformation();
    }

    @Override
    public void onDeleting() throws Exception {
        super.onDeleting();
        //业务代码
        deleteAddedRankDepartmentProcessInformation();
    }

    public void deleteAddedRankDepartmentProcessInformation() throws Exception {
        Long customStaffFlowTableId =this.getId();
        List<RankDepartmentProcessInformation>  rankDepartmentProcessInformationList=RankDepartmentProcessInformation.stream().where(c->c.getCustomStaffFlowTableId().getId()==customStaffFlowTableId).toList();
        for (RankDepartmentProcessInformation item:rankDepartmentProcessInformationList){
            item.initialization();
            this.repository().remove(item);
        }
    }

    public void newlyAddedRankDepartmentProcessInformation() throws Exception {

        List<EmployeeProcessDepartmentRankTable> employeeProcessDepartmentRankTablesList = EmployeeProcessDepartmentRankTable.stream().toList();
        for (EmployeeProcessDepartmentRankTable item:employeeProcessDepartmentRankTablesList){
            RankDepartmentProcessInformation rankDepartmentProcessInformation =new RankDepartmentProcessInformation();
            rankDepartmentProcessInformation.setCustomStaffFlowTableId(this);
            rankDepartmentProcessInformation.setEmployeeProcessDepartmentRankTableId(item);
            repository().add(rankDepartmentProcessInformation);
        }
        repository().commit();
    }

}
