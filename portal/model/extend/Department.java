package com.dxn.model.extend;

import com.dxn.forms.dto.TreeDTO;
import com.dxn.model.entity.DepartmentEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.annotation.CacheEntity;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.context.AppContext;
import com.dxn.system.dto.KeyValue;
import com.dxn.system.exception.BusinessException;
import com.dxn.system.exception.WorkFlowException;
import com.dxn.weChat.DTO.WeChatDeptDTO;
import com.dxn.weChat.WeChatHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;
import java.util.stream.Stream;

@CacheEntity
@Entity
@Table(name = "Base_Department")
public class Department extends DepartmentEntity {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();

        if (Framework.isNullOrEmpty(this.getParentId()) && Framework.isNullOrEmpty(this.getWeChatDeptId())) {
            this.setWeChatDeptId(1);//设置默认微信根部门
        }
    }

    @Override
    public void onInserting() throws Exception {
        super.onInserting();

        if (Framework.isNullOrEmpty(this.getParentId())) {
            Optional<Department> first = Department.stream().where(d -> d.getParentId() == null).findAny();
            if (first.isPresent()) {
                throw new BusinessException("已存在根部门，请选择上一级部门");
            }
        }
        //质控组新增
        if (this.getUnitAttributes() == 20) {
            if (Framework.isNullOrEmpty(this.getNickname())) throw new BusinessException("分所必须填写别名");
            UserGroup userGroup = new UserGroup();
            String name = String.format("%s-质控组", this.getNickname());
            userGroup.setCode(name);
            userGroup.setName(name);
            repository().add(userGroup);
            this.setQualityControlGroupId(userGroup);
        }
        //syncWeChatDept(10);

    }

    @Override
    public void onInserted() throws Exception {
        super.onInserted();
        this.addOrgId();
        //业务代码
        Long masterId = Framework.isNotNullOrEmpty(this.getMasterId()) ? this.getMasterId().getId() : null;
        Long inChargeOfPartnerId = Framework.isNotNullOrEmpty(this.getInChargeOfPartnerId()) ? this.getInChargeOfPartnerId().getId() : null;
        addToProjectAuthorization(masterId, this);
        addToProjectAuthorization(inChargeOfPartnerId, this);
    }

    private void modifyProjectAuthorization() throws Exception {
        Long masterId = Framework.isNotNullOrEmpty(this.getMasterId()) ? this.getMasterId().getId() : null;
        Long inChargeOfPartnerId = Framework.isNotNullOrEmpty(this.getInChargeOfPartnerId()) ? this.getInChargeOfPartnerId().getId() : null;
        Long oldMasterId = Framework.isNotNullOrEmpty(this.getOriginalValue().getMasterId()) ? this.getOriginalValue().getMasterId().getId() : null;
        Long oldInChargeOfPartnerId = Framework.isNotNullOrEmpty(this.getOriginalValue().getInChargeOfPartnerId()) ? this.getOriginalValue().getInChargeOfPartnerId().getId() : null;
        List<DepartmentVSUser> departmentVSUserList = Framework.toList(this.getDepartmentVSUsers().stream().filter(d -> Framework.isNotNull(d.getIsSystemIncrease()) && d.getIsSystemIncrease() && d.getUserId().getId() == oldMasterId));
        if (departmentVSUserList.size() == 0 || masterId != oldMasterId) {
            deleteProjectAuthorization(oldMasterId);
            addToProjectAuthorization(masterId, this);
        }
        List<DepartmentVSUser> departmentVSUserList1 = Framework.toList(this.getDepartmentVSUsers().stream().filter(d -> Framework.isNotNull(d.getIsSystemIncrease()) && d.getIsSystemIncrease() && d.getUserId().getId() == oldInChargeOfPartnerId));
        if (departmentVSUserList1.size() == 0 || inChargeOfPartnerId != oldInChargeOfPartnerId) {
            deleteProjectAuthorization(oldInChargeOfPartnerId);
            addToProjectAuthorization(inChargeOfPartnerId, this);
        }


    }

    private void deleteProjectAuthorization(Long userId) throws Exception {
        List<DepartmentVSUser> departmentVSUserList = Framework.toList(this.getDepartmentVSUsers().stream().filter(d -> Framework.isNotNull(d.getIsSystemIncrease()) && d.getIsSystemIncrease() && d.getUserId().getId() == userId));
        for (DepartmentVSUser item : departmentVSUserList) {
            //item.initialization();
            this.getDepartmentVSUsers().remove(item);
            repository().remove(item);
        }
    }

    private void addToProjectAuthorization(Long UserId, Department dept) throws Exception {
        Long deptId = dept.getId();
        if (Framework.isNotNullOrEmpty(UserId)) {
            DepartmentVSUser departmentVSUser = new DepartmentVSUser();
            departmentVSUser.setUser(UserId);
            departmentVSUser.setIview(10);
            departmentVSUser.setEdit(20);
            departmentVSUser.setSpecialPermission(20);
            departmentVSUser.setDepartmentId(this);
            departmentVSUser.setIsSystemIncrease(true);
            repository().add(departmentVSUser);
        }
    }


    @Override
    public void onUpdating() throws Exception {
        super.onUpdating();

        if (Framework.isNullOrEmpty(this.getParentId())) {
            Long deptId = this.getId();
            Optional<Department> first = Department.stream().where(d -> d.getParentId() == null && d.getId() != deptId).findAny();
            if (first.isPresent()) {
                throw new BusinessException("已存在根部门，请选择上一级部门");
            }
        }
        modifyProjectAuthorization();

    }

    @Override
    public void onUpdated() throws Exception {
        super.onUpdated();
        this.addOrgId();
        //业务代码

        //更新企业微信的部门
        //syncWeChatDept(20);
    }

    @Override
    public void onDeleted() throws Exception {
        super.onDeleted();
        //删除企业微信的部门
        //syncWeChatDept(30);
    }

    @Override
    public void onValidate() throws Exception {
        super.onValidate();
        if (Framework.isNullOrEmpty(this.getParentId()) && this.getUnitAttributes() != 10)
            throw new WorkFlowException(String.format("【%s】单位属性必须是总所部门", this.getName()));

        this.manuscriptReviewUserGroup();

        if (Framework.isNullOrEmpty(this.getOrg()))
            throw new WorkFlowException("此部门必须有总或分公司！");
//        if (this.getUnitAttributes() == 30) {
//            if (Framework.isNullOrEmpty(this.getMasterId()))
//                throw new WorkFlowException("高级经理不能为空！");
//            if (Framework.isNullOrEmpty(this.getInChargeOfPartnerId()))
//                throw new WorkFlowException("分管合伙人不能为空！");
//        }
    }

    private void manuscriptReviewUserGroup() throws Exception {
        if (Framework.isNotNullOrEmpty(this.getQualityControlGroupId())) {
            Long userGroupId = this.getQualityControlGroupId().getId();
            User reviewManager = this.getReviewManagerId();
            User headquartersManager = this.getHeadquartersManagerId();
            intoUserGroup(headquartersManager, userGroupId);
            intoUserGroup(reviewManager, userGroupId);
        }
    }

    private void intoUserGroup(User user, Long userGroupId) throws Exception {
        if (Framework.isNotNullOrEmpty(user)) {
            Long count = 0L;
            Long userId = user.getId();
            count = UserGroupItem.stream().where(w -> w.getUserId().getId() == userId && w.getUserGroupId().getId() == userGroupId).count();
            if (count < 1) {
                Optional<UserGroup> userGroupFirst = UserGroup.stream().where(w -> w.getId() == userGroupId).findFirst();
                if (userGroupFirst.isPresent()) {
                    UserGroup userGroup = userGroupFirst.get();
                    UserGroupItem userGroupItem = new UserGroupItem();
                    userGroupItem.setUserId(user);
                    userGroupItem.setUserGroupId(userGroup);
                    repository().add(userGroupItem);
                    repository().commit();
                }
            }
        }
    }

    @Override
    public String getTreeNameStr() {
        if (Framework.isNullOrEmpty(this.getParentId())) return null;//第一级不拼接到树名称中
        if (this.getLevel() == 2) return null;
        //if (Framework.isNotNullOrEmpty(this.getNickname())) return this.getNickname();
        return this.getParentId().getNickname() + this.getName();
    }

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Schema);
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Authority);
    }

    public static String getWorkflowDefine(Long workFlowTypeId) throws Exception {
        if (Framework.isNullOrEmpty(workFlowTypeId)) return null;

        Long userId = AppContext.current().getProfile().getDeptId();
        String key = String.format("getWorkflowDefine-%s-%s", userId, workFlowTypeId);

        String workFlowUser = AppHelper.manage().getCacheUtil().get(key, () -> {
            try {

                WorkFlowType workFlowType = WorkFlowType.findById(workFlowTypeId);

                Long deptId = AppContext.current().getProfile().getDeptId();
                if (Framework.isNullOrEmpty(deptId)) throw new WorkFlowException("您的部门不能为空！");

                Department org = Department.findById(deptId).getOrg();
                if (Framework.isNullOrEmpty(org)) throw new WorkFlowException("您的分所不能为空！");

                List<String> list = new ArrayList<>();

                org.getUserWrokFlow().stream().filter(f -> f.getKey().equalsIgnoreCase(workFlowType.getCode())).forEach(e -> {
                    if (!list.stream().anyMatch(f -> f.equalsIgnoreCase(e.getValue()))) list.add(e.getValue());
                });

                if (list.size() <= 0) {
                    //  部门   人员职级   输出的是流程名称L   workflowdefine    WorkingHours
                    Optional<User> userFirst = User.stream().where(u -> u.getId() == userId).findFirst();
                    if (userFirst.isPresent()) {
                        Long staffId = userFirst.get().getStaffId();
                        Optional<Department> departmentFirst = Department.stream().where(c -> c.getId() == deptId).findFirst();
                        Optional<Staff> staffFirst = Staff.stream().where(s -> s.getId() == staffId).findFirst();
                        if (staffFirst.isPresent() && departmentFirst.isPresent()) {
                            Long deptIdParentId = departmentFirst.get().getParentId().getId();
                            Long staffPositionId = staffFirst.get().getStaffPositionId().getId();
                            //获取职级部门ID  需要职级和部门
                            Optional<EmployeeProcessDepartmentRankTable> EmployeeProcessDepartmentRankFirst = EmployeeProcessDepartmentRankTable.stream().where(e -> e.getDeptId().getId() == deptIdParentId && e.getStaffPositionId().getId() == staffPositionId).findFirst();
                            if (EmployeeProcessDepartmentRankFirst.isPresent()) {
                                Long EmployeeProcessDepartmentRankId = EmployeeProcessDepartmentRankFirst.get().getId();
                                //职级部门ID和流程类型  workFlowType.getCode()
                                String workFlowTypeCode = workFlowType.getCode();
                                Optional<RankDepartmentProcessInformation> rankDepartmentProcessInformationFirt = RankDepartmentProcessInformation.stream().where(c -> c.getEmployeeProcessDepartmentRankTableId().getId() == EmployeeProcessDepartmentRankId && c.getCustomStaffFlowTableId().getWorkFlowTypeId().getCode().equals(workFlowTypeCode)).findFirst();
                                if (rankDepartmentProcessInformationFirt.isPresent()) {
                                    RankDepartmentProcessInformation rankDepartmentProcessInformation = rankDepartmentProcessInformationFirt.get();
                                    if (Framework.isNotNullOrEmpty(rankDepartmentProcessInformation.getWorkFlowConfigId())) {
                                        list.add(rankDepartmentProcessInformation.getWorkFlowConfigId().getUuid());
                                    }
                                }
                            }
                        }
                    }
                }

                if (list.size() <= 0) return null;

                return list.get(0);

            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }

        }, String.class, CacheGroup.Authority);

        Framework.showSystemMes();

        return workFlowUser;
    }

    @JsonIgnore
    public Department getOrg() {
        Integer type = getUnitAttributes();
        if (type == 10 || type == 20) return this;
        if (Framework.isNotNullOrEmpty(this.getParentId())) return this.getParentId().getOrg();
        return null;
    }

    @JsonIgnore
    public List<KeyValue> getUserWrokFlow() throws Exception {
        List<KeyValue> list = new ArrayList<>();

        ModelHelper.eachModelAttribute(this, (obj) -> {
            if (Framework.isNotNullOrEmpty(obj) && obj instanceof WorkFlowConfig) {
                WorkFlowConfig config = (WorkFlowConfig) obj;
                KeyValue value = config.toWorkFlowKeyValue();
                if (Framework.isNotNullOrEmpty(value))
                    list.add(value);
            }
        });

        return list;
    }

    @SystemService(args = "dto")
    public void saveTreeSort(List<TreeDTO> dto) throws Exception {
        if (Framework.isNullOrEmpty(dto)) return;
        for (TreeDTO item : dto) {
            Department department = this.findById(new Long(item.getId()), item.getTimestamp());
            department.setSort(item.getSort());
            if (Framework.isNullOrEmpty(item.getParentId())) {
                department.setParentId(null);
            } else {
                department.setParent(new Long(item.getParentId()));
            }
        }
    }

    private void addOrgId() throws Exception {
        Long id = this.getId();
        Long orgId = addunitAttributes().getId();
        List<Department> departmentList = this.stream().where(w -> w.getId() == id).toList();
        Department department = departmentList.get(0);
        department.initialization();
        department.setOrgId(orgId);
        repository().add(department);

    }

    private Department addunitAttributes() throws Exception {
        Integer type = getUnitAttributes();
        if (type == 10 || type == 20) {
            return this;
        } else {
            if (Framework.isNotNullOrEmpty(this.getParentId()))
                return this.getParentId().addunitAttributes();

        }
        return this;
    }

    @SystemService(args = "deptId")
    public Long departmentalNodeAcquisition(Long deptId) throws Exception {
        Long parentId = 0L;
        Optional<Department> departmentFirst = this.stream().where(w -> w.getId() == deptId).findFirst();
        if (departmentFirst.isPresent()) {
            Department department = departmentFirst.get();
            if (Framework.isNotNullOrEmpty(department.getParentId())) {
                parentId = department.getParentId().getId();
            }
        }
        return parentId;
    }

    @JsonIgnore
    public static String gainUserDataAuthority(Long userId) throws Exception {

        //超级管理员不使用权限
        if (AppContext.current().getProfile().getIsSuperAdmin()) return "";
        if (Framework.isNullOrEmpty(userId)) return "";

        String sql = "Select d.treeCode from Base_DeptManagementScope s inner join Base_Department d on d.id=s.deptId where s.userId=? union Select d.treeCode from Base_DepartmentVSUser u inner join Base_Department d on d.id=u.departmentId where u.iview=10 and u.userId=?;";

        List<Object> parameters = new ArrayList<>();
        parameters.add(userId);
        parameters.add(userId);

        List<String> list = repository().sqlQuery(sql, parameters, String.class);
        Collections.sort(list, Comparator.comparing(o -> o.length()));
        List<String> treeCodes = new ArrayList<>();

        list.forEach(f -> {
            boolean isHave = treeCodes.stream().anyMatch(m -> f.startsWith(m));
            if (!isHave) treeCodes.add(f);
        });

        List<Long> departments = new ArrayList<>();

        for (String item : treeCodes) {
            Stream<Department> deptList = Department.stream().filter(f -> f.getTreeCode().startsWith(item));
            deptList.forEach(f -> {
                departments.add(f.getId());
            });
        }

        return Framework.toMosaicString(departments);
    }

    @SystemService()
    public List<TreeDTO> gainDepartmentOrg() throws Exception {
        List<TreeDTO> list = new ArrayList<>();
        TreeDTO General = new TreeDTO();
        General.setId("1");
        General.setText("总所");
        General.setCode("General");
        General.setSort(1);
        TreeDTO Branch = new TreeDTO();
        Branch.setId("1");
        Branch.setText("分所");
        Branch.setCode("Branch");
        Branch.setSort(2);
        list.add(General);
        list.add(Branch);
        List<Department> departmentList = this.stream().where(d -> d.getUnitAttributes() == 10 || d.getUnitAttributes() == 20).toList();
        for (Department d : departmentList
        ) {
            TreeDTO dto = new TreeDTO();
            dto.setId(d.getId().toString());
            dto.setText(d.getName());
            dto.setCode(d.getCode());
            if (d.getUnitAttributes() == 10) {
                dto.setParentId("1");
                General.getChildren().add(dto);

            } else {
                dto.setParentId("2");
                Branch.getChildren().add(dto);

            }
        }
        return list;
    }

    @SystemService()
    public List<TreeDTO> gainDepartmentAllOrg() throws Exception {
        List<TreeDTO> list = new ArrayList<>();
        List<Department> departmentList = this.stream().where(d -> d.getUnitAttributes() == 10 || d.getUnitAttributes() == 20).toList();
        for (Department d : departmentList
        ) {
            TreeDTO dto = new TreeDTO();
            dto.setId(d.getId().toString());
            dto.setText(d.getName());
            dto.setCode(d.getCode());
            list.add(dto);
        }
        return list;
    }

    //assignedDepartmentId要合并到的部门  needToAllocateDepartmentId需要合并的部门
    @SystemService(args = "needToAllocateDepartmentId,assignedDepartmentId")
    public boolean moveDeptUsingData(Long needToAllocateDepartmentId, Long assignedDepartmentId) throws Exception {

        Optional<Department> departmentFirst = this.stream().where(d -> d.getId() == needToAllocateDepartmentId).findFirst();
        if (departmentFirst.isPresent()) {
            Department department = departmentFirst.get();
            department.moveUsingData(assignedDepartmentId);
            repository().remove(department);
            return true;
        } else {
            throw new WorkFlowException("没有找到需要合并的部门");
        }
    }

    public List<Long> gainAuthorityMenus() {
        List<Long> list = new ArrayList<>();
        this.getDeptQuickLinks().forEach(f -> {
            list.add(f.getMenuId().getId());
        });
        return list;
    }

    //region WeChat相关接口

    //企业微信号 operatingType--10：新增；20：修改，30：删除
    public void syncWeChatDept(int operatingType) throws Exception {
        switch (operatingType) {
            case 10:
                Integer weChatDeptId = WeChatHelper.synWeChatDeptInfo(this.toWeChatDept());
                if (Framework.isNotNullOrEmpty(weChatDeptId)) this.setWeChatDeptId(weChatDeptId);
                break;
            case 20:
                WeChatHelper.updateWeChatDeptInfo(this.toWeChatDept());
                break;
            case 30:
                WeChatHelper.deleteWeChatDeptInfo(this.getWeChatDeptId());
                break;
            default:
                break;
        }
    }

    public WeChatDeptDTO toWeChatDept() {
//        if (Framework.isNullOrEmpty(this.getWeChatDeptId())) return null;
        WeChatDeptDTO dto = new WeChatDeptDTO();
        dto.setId(this.getWeChatDeptId());
        dto.setName(this.getName());
        if (Framework.isNotNullOrEmpty(this.getParentId())) {
            dto.setParentid(this.getParentId().getWeChatDeptId());
        }
        dto.setOrder(this.getSort());
        return dto;
    }

    //endregion
}

