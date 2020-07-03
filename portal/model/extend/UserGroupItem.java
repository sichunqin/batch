package com.dxn.model.extend;

import com.dxn.dto.UserGroupItemDTO;
import com.dxn.dto.WorkFlow.WorkFlowUser;
import com.dxn.model.entity.UserGroupItemEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.context.AppContext;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Base_UserGroupItem")
public class UserGroupItem extends UserGroupItemEntity {

    //    //验证方法
//    @Override
//    public void onValidate() throws Exception {
//        super.onValidate();
//        repeatedTestUserGroupUser();
//    }
//
//    private void repeatedTestUserGroupUser() throws Exception {
//        Long userId=this.getUser().getId();
//        Long userGroupId=this.getUserGroupId().getId();
//        Long count=UserGroupItem.stream().where(c -> c.getId() == userId&&c.getUserGroupId().getId()==userGroupId).count();
//        if (count>0) throw new DxnException("用户组用户不能重复");
//    }
    //提供数据用的还有BUG,时间和创建人设置可空后期还要改
    @SystemService(args = "userId,userGroupId")
    public void getUserGroups(Long userId, Long userGroupId) throws Exception {
        UserGroupItem userGroupItem = this.createNew();
        List<User> userList = User.stream().where(c -> c.getId() == userId).toList();
        for (User a : userList) {
            userGroupItem.setUserId(a);
            userGroupItem.setUserGroup(userGroupId);
            repository().add(userGroupItem);
            repository().commit();
        }

    }

    @SystemService(args = "userId")
    public UserGroupItemDTO getUserGroupPage(Long userId) throws Exception {
        UserGroupItemDTO userGroupItemDTO = new UserGroupItemDTO();
        List<User> userList = User.stream().where(c -> c.getId() == userId).toList();
        for (User a : userList) {
            userGroupItemDTO.setUserId(a);
            userGroupItemDTO.setStaffCoding(a.getCode());
            userGroupItemDTO.setName(a.getName());
            userGroupItemDTO.setTelephone(a.getPhone());
            userGroupItemDTO.setMailbox(a.getEmail());
            userGroupItemDTO.setDepartmentId(a.getDeptId());
        }
        return userGroupItemDTO;
    }

    @SystemService()
    public Integer checkraftReviewUser() throws Exception{
        Long deptid = AppContext.current().getProfile().getOrgId();

        Department dept = Department.findById(deptid);
        if (Framework.isNullOrEmpty(dept.getQualityControlGroupId())) {
            return  -1;
            //throw new DxnException("该部门没有底稿复核用户组！请联系管理员");

        }

        return 1;
    }

    //添加 用户组为“底稿复核用户组”的用户组用户，“底稿复核用户组”的编号是DraftReview，不能改变的
    // （后来改了，DraftReview 不再固定）
    @SystemService(args = "userId")
    public void saveDraftReviewUser(Long userId) throws Exception {

        Long deptid = AppContext.current().getProfile().getOrgId();

        Department dept = Department.findById(deptid);
//        if (Framework.isNullOrEmpty(dept.getQualityControlGroupId())) {
//            throw new DxnException("该部门没有底稿复核用户组！请联系管理员");
//        }

        Long dgroup = dept.getQualityControlGroupId().getId();

        int count = this.stream().where(w -> w.getUserId().getId() == userId && w.getUserGroupId().getId() == dgroup).toList().size();
        if (count == 0)//如果列表中存在就不重复添加了
        {
            getUserGroups(userId, dgroup);
        }

    }

    public List<WorkFlowUser> getWorkFlowUsers(Long id) throws Exception {
        String key = String.format("getWorkFlowUsers-%s", id);
        List<WorkFlowUser> workFlowUsers = AppHelper.manage().getCacheUtil().getList(key, () -> {
            try {
                List<WorkFlowUser> list = new ArrayList<>();
                List<UserGroupItem> userGroupItemList = this.stream().where(w -> w.getUserId() != null && w.getUserGroupId().getId() == id).toList();
                for (UserGroupItem userGroupItem : userGroupItemList) {
                    list.add(userGroupItem.getUser().toWorkFlowUser());
                }
                for (int i = 0; i < list.size(); i++) {
                    Long userId = list.get(i).getId();
                    Optional<User> userOptional = User.stream().where(w -> w.getId() == userId).findFirst();
                    if (userOptional.isPresent()) {
                        User user = userOptional.get();
                        Long staffId = user.getStaffId();
                        Optional<Staff> staffOptional = Staff.stream().where(c -> c.getId() == staffId).findFirst();
                        if (staffOptional.isPresent()) {
                            Staff staff = staffOptional.get();
                            if (Framework.isNotNullOrEmpty(staff.getStaffPositionId())) {
                                list.get(i).setPosition(staff.getStaffPositionId().getName());
                            }

                        }
                    }
                }
                return list;
            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }
        }, WorkFlowUser.class, CacheGroup.Authority);

        return workFlowUsers;
    }

    //用户组新增用户用
    @SystemService(args = "userList,userGroup")
    public void getUserGroupList(List<Long> userList, Long userGroup) throws Exception {
        if (Framework.isNotNullOrEmpty(userGroup)) {
            List<UserGroupItem> userGroupItemList =
                    UserGroupItem.stream().where(c -> c.getUserGroupId().getId() == userGroup).toList();
            for (UserGroupItem item : userGroupItemList) {
                for (int i = 0; i < userList.size(); i++)
                    if (item.getUserId().getId().equals(userList.get(i))) {
                        userList.remove(userList.get(i));
                        i--;
                    }
            }
            for (Long id : userList) {
                UserGroupItem model = UserGroupItem.createNew();
                List<User> list = User.stream().where(c -> c.getId() == id).toList();
                List<UserGroup> userGroupList = UserGroup.stream().where(c -> c.getId() == userGroup).toList();
                model.setUserGroupId(userGroupList.get(0));
                model.setUserId(list.get(0));
                repository().add(model);
                repository().commit();
            }
        }
    }
}
