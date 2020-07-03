package com.dxn.model.extend;

import com.dxn.model.entity.DeptManagementScopeEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Base_DeptManagementScope")
public class DeptManagementScope extends DeptManagementScopeEntity {


    @SystemService(args = "departmentId,userId")
    public void getdepartmentIdList(Long departmentId, Long userId) throws Exception {
        if (Framework.isNullOrEmpty(userId)) return;
        Optional<DeptManagementScope> deptList =
                DeptManagementScope.stream().where(c -> c.getUserId().getId() == userId && c.getDeptId().getId() == departmentId).findFirst();
        if (deptList.isPresent()) return;
        DeptManagementScope model = DeptManagementScope.createNew();
        model.setDept(departmentId);
        model.setUser(userId);
        repository().add(model);
    }

}
