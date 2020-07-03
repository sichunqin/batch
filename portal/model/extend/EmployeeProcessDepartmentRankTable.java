package com.dxn.model.extend;

import com.dxn.model.entity.EmployeeProcessDepartmentRankTableEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "HR_EmployeeProcessDepartmentRankTable")
public class EmployeeProcessDepartmentRankTable extends EmployeeProcessDepartmentRankTableEntity {

    //插入后
    @Override
    public void onInserted() throws Exception {
        super.onInserted();
        CustomStaffFlowTableAdd();
    }

    //职级增加给部门职级表加数据
    private void CustomStaffFlowTableAdd() throws Exception {
        //y员工
        List<CustomStaffFlowTable> customStaffFlowTableList=CustomStaffFlowTable.stream().toList();
        for (CustomStaffFlowTable item:customStaffFlowTableList){
            RankDepartmentProcessInformation rankDepartmentProcessInformationEntity = RankDepartmentProcessInformation.createNew();
            rankDepartmentProcessInformationEntity.setCustomStaffFlowTable(item.getId());
            rankDepartmentProcessInformationEntity.setEmployeeProcessDepartmentRankTable(this.getId());
            repository().add(rankDepartmentProcessInformationEntity);
            repository().commit();
        }
    }
}
