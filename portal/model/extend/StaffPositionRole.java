package com.dxn.model.extend;

import com.dxn.model.entity.StaffPositionRoleEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "HR_StaffPositionRole")
public class StaffPositionRole extends StaffPositionRoleEntity {
    @SystemService(args = "roleId,staffPositionId")
    public void getStaffPositionList(List<Long> roleId, Long staffPositionId) throws Exception {
        if (Framework.isNotNullOrEmpty(staffPositionId)) {
            List<StaffPositionRole> staffPositionList =
                    StaffPositionRole.stream().where(c -> c.getStaffPositionId().getId() == staffPositionId).toList();

            for (StaffPositionRole item : staffPositionList) {
                for (int i = 0; i < roleId.size(); i++)
                    if (item.getRoleId().getId().equals(roleId.get(i))) {
                        roleId.remove(roleId.get(i));
                        i--;
                    }
            }

            for (Long id : roleId) {
                StaffPositionRole model = StaffPositionRole.createNew();
                List<Role> list = Role.stream().where(c -> c.getId() == id).toList();
                List<StaffPosition> StaffPositionList = StaffPosition.stream().where(c -> c.getId() == staffPositionId).toList();
                model.setRoleId(list.get(0));
                model.setStaffPositionId(StaffPositionList.get(0));
                repository().add(model);
                repository().commit();
            }
            List<Staff> staffList = Staff.stream().where(c -> c.getStaffPositionId().getId() == staffPositionId).toList();//该职位的所有人员
            for (Staff staff : staffList) {
                Long staffId = staff.getId();
                Optional<User> user = User.stream().where(c -> c.getStaffId() == staffId).findFirst();
                if (user.isPresent()) {
                    Long userId = user.get().getId();
                    List<UserRole> userRoleList = UserRole.stream().where(c -> c.getUserId().getId() == userId).toList();
                    for (UserRole item : userRoleList) {
                        for (int i = 0; i < roleId.size(); i++)
                            if (item.getRoleId().getId().equals(roleId.get(i)) && item.getIsPositionRole() != null && item.getIsPositionRole().equals(10)) {
                                roleId.remove(roleId.get(i));
                                i--;
                            }
                    }
                    for (Long id : roleId) {
                        List<Role> list = Role.stream().where(c -> c.getId() == id).toList();
                        UserRole model = UserRole.createNew();
                        model.setIsPositionRole(10);
                        model.setRoleId(list.get(0));
                        model.setUserId(user.get());
                        repository().add(model);
                        repository().commit();
                    }
                }
            }
        }
    }

    @Override
    public void onDeleting() throws Exception {
        Long id = this.getRoleId().getId();
        List<UserRole> userRoleList =
                UserRole.stream().where(c -> c.getRoleId().getId() == id && c.getIsPositionRole() == 10).toList();
        for (UserRole item : userRoleList) {
            item.initialization();
            this.repository().remove(item);
        }
    }

    @Override
    public String gainDisplayName() throws Exception {
        return this.getRoleId().getName();
    }
}
