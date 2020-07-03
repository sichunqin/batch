package com.dxn.model.extend;

import com.dxn.model.entity.UserRoleEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.exception.BusinessException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Base_UserRole")
public class UserRole extends UserRoleEntity {

    @Override
    public String gainDisplayName() throws Exception {
        return this.getRoleId().getName();
    }

    @SystemService(args = "roleId,userId")
    public void getUserRoleList(List<Long> roleId, Long userId) throws Exception {
        if (Framework.isNullOrEmpty(userId)) return;

        Optional<User> userFirst = User.stream().where(c -> c.getId() == userId).findFirst();
        if (!userFirst.isPresent()) throw new BusinessException(String.format("用户ID %s 未找到！", userId));

        List<UserRole> userRoleList = UserRole.stream().where(c -> c.getUserId().getId() == userId && c.getIsPositionRole().equals(20)).toList();

        List<Long> roleIds = new ArrayList<>();
        for (Long id : roleId) {
            Optional<UserRole> isHave = userRoleList.stream().filter(f -> f.getRoleId().getId().equals(id)).findFirst();
            if (!roleIds.contains(id) && !isHave.isPresent()) {
                roleIds.add(id);
            }
        }
        for (Long id : roleIds) {
            UserRole model = UserRole.createNew();
            Optional<Role> roleFirst = Role.stream().where(c -> c.getId() == id).findFirst();
            if (roleFirst.isPresent()) {
                model.setRoleId(roleFirst.get());
                model.setUserId(userFirst.get());
                model.setIsPositionRole(20);
                repository().add(model);
            }
        }
    }
}
