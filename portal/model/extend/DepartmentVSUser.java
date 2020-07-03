package com.dxn.model.extend;

import com.dxn.model.entity.DepartmentVSUserEntity;
import com.dxn.system.Framework;
import com.dxn.system.context.AppContext;
import com.dxn.system.exception.LoginException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Entity
@Table(name = "Base_DepartmentVSUser")
public class DepartmentVSUser extends DepartmentVSUserEntity {

    @Override
    public void onUpdating() throws Exception {
        super.onUpdating();
        this.updateModifyAuthorizedUsers();
    }

    @Override
    public void onInserting() throws Exception {
        super.onInserting();
        if (Framework.isNullOrEmpty(this.getIsSystemIncrease())){
            this.setIsSystemIncrease(false);
        }

        this.updateModifyAuthorizedUsers();
    }

//    private void modifyAuthorizedUsers(Role see, Role edit, Role special) throws Exception {
//        if (Framework.isNotNullOrEmpty(this.getIview())) {
//            if (this.getIview() == 10) {
//                UserDeptRole userDeptRole = UserDeptRole.createNew();
//                userDeptRole.setRoleId(see);
//                userDeptRole.setUserId(this.getUserId());
//                userDeptRole.setDepartmentVSUserId(this);
//                repository().add(userDeptRole);
//            }
//        }
//        if (Framework.isNotNullOrEmpty(this.getEdit())) {
//            if (this.getEdit() == 10) {
//                UserDeptRole userDeptRole = UserDeptRole.createNew();
//                userDeptRole.setRoleId(edit);
//                userDeptRole.setUserId(this.getUserId());
//                userDeptRole.setDepartmentVSUserId(this);
//                repository().add(userDeptRole);
//            }
//        }
//        if (Framework.isNotNullOrEmpty(this.getSpecialPermission())) {
//            if (this.getSpecialPermission() == 10) {
//                UserDeptRole userDeptRole = UserDeptRole.createNew();
//                userDeptRole.setRoleId(special);
//                userDeptRole.setUserId(this.getUserId());
//                userDeptRole.setDepartmentVSUserId(this);
//                repository().add(userDeptRole);
//            }
//        }
//    }

    private void updateModifyAuthorizedUsers() throws Exception {

        //特殊权限
        if (Framework.isNullOrEmpty(this.getSpecialPermission())) throw new LoginException("特殊权限请填写！");
        Role special = Role.createNew().getRoleForCode(36190, "请配置特殊权限组,权限组编码!");
        restUserDeptRole(special, this.getSpecialPermission() == 20);

        //查看权限
        if (Framework.isNullOrEmpty(this.getIview())) throw new LoginException("查看权限请填写！！");
        Role see = Role.createNew().getRoleForCode(49705, "请配置查看权限组,权限组编码!");
        restUserDeptRole(see, this.getIview() == 20);

        //编辑权限
        if (Framework.isNullOrEmpty(this.getEdit())) throw new LoginException("编辑权限请填写！！");
        Role edit = Role.createNew().getRoleForCode(49706, "请配置编辑权限组,权限组编码!");
        restUserDeptRole(edit, this.getEdit() == 20);
    }

    private void restUserDeptRole(Role role, boolean isDelete) throws Exception {

        Long userId = this.getUserId().getId();

        if (!isDelete) {
            Long i = this.getUserDeptRoles().stream().filter(w -> w.getUserId().getId() == userId && w.getRoleId().getId() == role.getId()).count();
            if (i == 0) {
                UserDeptRole userDeptRole = UserDeptRole.createNew();
                userDeptRole.setRoleId(role);
                userDeptRole.setUserId(this.getUserId());
                userDeptRole.setDepartmentVSUserId(this);
                repository().add(userDeptRole);
            }
        } else {
            List<UserDeptRole> delMember = new ArrayList<>();
            Stream<UserDeptRole> projectMemberList = this.getUserDeptRoles().stream().filter(w -> w.getUserId().getId() == userId && w.getRoleId().getId() == role.getId());
            projectMemberList.forEach(f -> {
                delMember.add(f);
            });

            for (UserDeptRole item : delMember) {
                item.initialization();
                this.getUserDeptRoles().remove(item);
                this.repository().remove(item);
            }
        }
    }

    public static boolean isHaveSpecialAuthority() throws Exception {
        Long userId = AppContext.current().getProfile().getId();
        if (Framework.isNullOrEmpty(userId)) return false;
        Optional<DepartmentVSUser> first = DepartmentVSUser.stream().where(w -> w.getUserId().getId() == userId && (w.getEdit() == 10 || w.getIview() == 10 || w.getSpecialPermission() == 10)).findFirst();
        return first.isPresent();
    }
}


