package com.dxn.model.extend;


import com.dxn.dto.IPUserStatistics;
import com.dxn.dto.WorkFlow.WorkFlowUser;
import com.dxn.forms.dto.TreeDTO;
import com.dxn.forms.dto.UIAuthorityDTO;
import com.dxn.forms.dto.UIAuthorityTypeEnum;
import com.dxn.model.entity.UserEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.MessageHelper;
import com.dxn.system.ModelHelper;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.annotation.SystemTask;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.configuration.DxnConfig;
import com.dxn.system.context.AppContext;
import com.dxn.system.context.Profile;
import com.dxn.system.dto.EntityDataDTO;
import com.dxn.system.dto.UserRequest;
import com.dxn.system.dto.UserResponse;
import com.dxn.system.exception.DxnException;
import com.dxn.system.exception.LoginException;
import com.dxn.system.interfaces.IUserLoginService;
import com.dxn.system.transaction.TransactionHelper;
import com.dxn.weChat.WeChatHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.fxml.LoadException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Base_User")
public class User extends UserEntity implements IUserLoginService {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        updateDeptInfo();
        //保存密码
        if (!Framework.isPassword(this.getLoginPassword()))
            this.setLoginPassword(Framework.getPassword(this.getLoginPassword()));
    }

    @Override
    public void onInserting() throws Exception {
        super.onInserting();
        this.putUserStatus();
    }

    //修改前
    @Override
    public void onUpdating() throws Exception {
        super.onUpdating();
        this.validateUserConut();
        this.updateStaff();
        this.updateUserWeChat(10);
    }

    @Override
    public void onDeleting() throws Exception {
        super.onDeleting();
        this.updateUserWeChat(20);
    }

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Authority);
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Schema);
    }

    public UserResponse loginOn(UserRequest user) throws Exception {

        if (Framework.isNullOrEmpty(user.getLoginName())) throw new LoginException("用户名不能为空！");
        if (Framework.isNullOrEmpty(user.getLoginPassword())) throw new LoginException("密码不能为空！");

        Long userCount = this.stream().where(w -> w.getStatus() == 10).count();
        DxnConfig.getConfig().verification(userCount);

        String loginName = user.getLoginName();

        List<User> list = this.stream().where(w -> w.getLoginName().equals(loginName)).toList();
        if (list.size() <= 0) throw new LoginException("用户名或密码错误！");

        User first = list.get(0);
        if (Framework.isNullOrEmpty(first)) throw new LoginException("用户名或密码错误！");
        if (!first.getLoginPassword().equalsIgnoreCase(Framework.getPassword(user.getLoginPassword())))
            throw new LoginException("用户名或密码错误！");

        first.initialization();

        return first.toUserRequest();
    }

    //region WeChat相关接口

    //微信号自动登陆
    public UserResponse weChatCodeLoginOn(String weChatCode) throws Exception {

        if (Framework.isNullOrEmpty(weChatCode)) return null; //throw new LoginException("用户名不能为空！");

        Long userCount = this.stream().where(w -> w.getStatus() == 10).count();
        DxnConfig.getConfig().verification(userCount);


        List<User> list = this.stream().where(w -> w.getWeChatCode() != null && w.getWeChatCode().equals(weChatCode)).toList();
        if (list.size() <= 0) return null;//throw new LoginException("未绑定微信！");

        User first = list.get(0);

        first.initialization();

        return first.toUserRequest();
    }

    //微信登陆
    public UserResponse weChatLoginOn(UserRequest user) throws Exception {

        if (Framework.isNullOrEmpty(user.getLoginName())) throw new LoginException("用户名不能为空！");
        if (Framework.isNullOrEmpty(user.getLoginPassword())) throw new LoginException("密码不能为空！");

        Long userCount = this.stream().where(w -> w.getStatus() == 10).count();
        DxnConfig.getConfig().verification(userCount);

        String loginName = user.getLoginName();

        List<User> list = this.stream().where(w -> w.getLoginName().equals(loginName)).toList();
        if (list.size() <= 0) throw new LoginException("用户名或密码错误！");

        User first = list.get(0);
        if (Framework.isNullOrEmpty(first)) throw new LoginException("用户名或密码错误！");
        if (!first.getLoginPassword().equalsIgnoreCase(Framework.getPassword(user.getLoginPassword())))
            throw new LoginException("用户名或密码错误！");

        first.initialization();

        if (Framework.isNotNullOrEmpty(user.getWeChatCode())) {
            first.setWeChatCode(user.getWeChatCode());
            repository().commit();
        }

        return first.toUserRequest();
    }

    //删除企业微信号 operatingType--10：修改；20：删除
    private void updateUserWeChat(int operatingType) throws Exception {
        if (Framework.isNullOrEmpty(this.getWeChatCode())) return;

        String weChatCode = "";

        if (operatingType == 10) {
            //用户状态把启用改成不启用时删除企业微信中人
            if (this.getOriginalValue() != null && this.getOriginalValue().getStatus() != this.getStatus() && this.getStatus() != 10) {
                weChatCode = this.getWeChatCode();
                this.setWeChatCode("");
            }
        } else {
            weChatCode = this.getWeChatCode();
        }

        if (Framework.isNotNullOrEmpty(weChatCode)) {
            WeChatHelper.deleteWeChatUserInfo(weChatCode);
        }
    }

    //endregion

    @Override
    public UserResponse userLoginByName(String loginName) throws Exception {
        if (Framework.isNullOrEmpty(loginName)) throw new LoginException("用户名不能为空！");

        Optional<User> list = this.stream().where(w -> w.getLoginName() == loginName).findFirst();
        if (!list.isPresent()) throw new LoginException("用户名或密码错误！");

        User first = list.get();
        first.initialization();
        first.verificationState();

        String loginKey = Framework.createGuid().toString();
        first.setLoginKey(loginKey);
        first.setLoginTime(LocalDateTime.now());
        first.setIsOnLine(true);
        first.setServerIP(Framework.getServerIp());

        repository().add(first);
        repository().commit();

        return first.toUserRequest();
    }

    private void verificationState() throws LoadException {
        if (this.getStatus() == 20) throw new LoadException("当前用户为禁用状态!");
        if (this.getStatus() == 30) throw new LoadException("当前用户为锁定状态!");
        if (this.getStatus() == 40) {
            if (Framework.isNotNullOrEmpty(this.getOfflineTime())) {
                if (this.getOfflineTime().isAfter(LocalDateTime.now())) {
                    throw new LoadException(String.format("当前用已锁定,截止时间为 %s !", Framework.localDateTimeToString(this.getOfflineTime())));
                } else {
                    this.setIsOnLine(true);
                    this.setStatus(10);
                    this.setOfflineTime(null);
                }
            } else {
                this.setStatus(10);
            }
        }
    }

    @Override
    public void onLine(Profile profile) {
        Framework.log.info(String.format("用户 %s 登录!", profile.getName()));
    }

    @Override
    public void offLine(Profile profile) {
        Framework.log.info(String.format("用户 %s 退出!", profile.getName()));

        try {
            User user = User.findById(profile.getId());
            if (Framework.isNotNullOrEmpty(user) && user.getLoginKey().equalsIgnoreCase(profile.getLoginKey())) {
                user.setIsOnLine(false);
                repository().commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void verificationLine() throws Exception {
        isOffLine();
        //Framework.log.info(String.format("用户 %s 验证是否有效!", profile.getName()));
    }

    private boolean isOffLine() throws Exception {
        Profile profile = AppContext.current().getProfile();
        Long userId = profile.getId();
        if (Framework.isNullOrEmpty(userId)) return false;

        String key = String.format("User-IsOffLine-%s", userId);
        boolean isOffLine = AppHelper.manage().getCacheUtil().get(key, () -> {
            try {
                User user = User.findById(userId);
                user.verificationState();
                repository().commit();
                return true;
            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }
        }, boolean.class, CacheGroup.Data);

        return isOffLine;
    }

    @SystemService
    public String userOffline() throws Exception {
        if (this.getIsSuperAdmin()) throw new DxnException("管理员不能强制下线!");
        if (this.getStatus() == 10) {
            this.setOfflineTime(LocalDateTime.now().plusMinutes(30));
            this.setStatus(40);//期间锁定'
            String key = String.format("User-IsOffLine-%s", this.getId());
            AppHelper.manage().getCacheUtil().clear(key, CacheGroup.Data);
            repository().add(this);
        }
        return "强制下线完成!";
    }

    private UserResponse toUserRequest() throws Exception {
        UserResponse userResponse = new UserResponse();

        //赋值属性
        ModelHelper.copyModel(this, userResponse);
        userResponse.setISCanBeStamped(Framework.isNotNullOrEmpty(this.getISCanBeStamped()) && this.getISCanBeStamped() == 10);

        Department dept = this.getDeptReadOnly();
        if (Framework.isNotNullOrEmpty(dept)) {
            userResponse.setDeptName(dept.getName());
            userResponse.setDeptTreeName(dept.getTreeName());
            userResponse.setUnitAttributes(dept.getUnitAttributes());
            User deptMaster = dept.getMasterId();

            if (Framework.isNotNullOrEmpty(deptMaster)) {
                EntityDataDTO entityDataDTO = new EntityDataDTO();
                ModelHelper.copyModel(deptMaster, entityDataDTO);
                userResponse.setDeptMaster(entityDataDTO);
            }

            if (Framework.isNotNullOrEmpty(dept.getOrgId())) {
                Department org = dept.getOrgReadOnly();
                userResponse.setOrgId(org.getId());
                userResponse.setOrgName(org.getName());
                userResponse.setOrgTreeName(org.getTreeName());
                userResponse.setOrgUnitAttributes(org.getUnitAttributes());
            }
            if (Framework.isNotNullOrEmpty(dept.getDepartmentalAttributes()))
                userResponse.setDepartmentalAttributes(dept.getDepartmentalAttributes() == 10 ? true : false);
            else
                userResponse.setDepartmentalAttributes(false);
        }

        Staff staff = this.getStaff();
        if (Framework.isNotNullOrEmpty(staff)) {
            userResponse.setPhotoPath(staff.getHeadPortraitId());
        }

        //用印类型
        userResponse.setOrgSealType(MainOption.gainOrgSealType(this.getId()));

        //数据权限
        userResponse.setDataAuthority(Department.gainUserDataAuthority(this.getId()));

        //确定是否签署独立性声明
        AppContext.current().getProfile().setId(userResponse.getId());
        AppContext.current().getProfile().setIsSuperAdmin(userResponse.getIsSuperAdmin());
        userResponse.setIsIndependenceSign(IndependenceSign.createNew().gainIsIndependenceSign());

        return userResponse;
    }

    @JsonIgnore
    public static List<UIAuthorityDTO> getAuthorityConfig(Long formId) throws Exception {

        List<UIAuthorityDTO> list = new ArrayList<>();
        Long userId = AppContext.current().getProfile().getId();
        if (Framework.isNullOrEmpty(userId)) return list;

        //控件权限
        List<UIAuthorityDTO> authorityList = RoleAuthority.getAuthorityConfig();

        User user = User.findById(userId);
        List<Long> roles = user.getUserRoleId();

        for (Long item : roles) {
            authorityList.stream().filter(a -> a.getRoleId().equals(item) && a.getDataId().equals(formId)).forEach(e -> {
                list.add(e);
            });
        }

        //数据权限
        list.addAll(DataAuthorityGroup.getAuthorityConfig(formId));

        return list;
    }

    @JsonIgnore
    public List<UIAuthorityDTO> getMenuAuthority() throws Exception {

        Long userId = AppContext.current().getProfile().getId();
        if (Framework.isNullOrEmpty(userId)) return new ArrayList<>();

        List<UIAuthorityDTO> authorityList = RoleAuthority.getAuthorityConfig();

        User user = this.findById(userId);
        List<Long> roles = user.getUserRoleId();
        List<UIAuthorityDTO> list = new ArrayList<>();

        for (Long item : roles) {
            authorityList.stream().filter(a -> a.getRoleId().equals(item) && a.getType().equals(UIAuthorityTypeEnum.Menu)).forEach(e -> {
                list.add(e);
            });
        }

        //快捷菜单
        List<TreeDTO> authorityMenus = User.findById(userId).gainAuthorityMenus();
        for (TreeDTO item : authorityMenus) {
            Long id = Long.parseLong(item.getId());
            Optional<UIAuthorityDTO> first = list.stream().filter(f -> f.getDataId() == id).findFirst();
            if (!first.isPresent()) {
                UIAuthorityDTO dto = new UIAuthorityDTO();
                dto.setId(id);
                dto.setDataId(id);
                dto.setName(item.getText());
                dto.setType(UIAuthorityTypeEnum.Menu);
                dto.setTypeEnumNum(10);
                dto.setIsSystemRole(false);
                list.add(dto);
            }
        }

        return list;
    }

    @JsonIgnore
    public List<Long> getUserRoleId() throws Exception {
        List<Long> list = new ArrayList<>();

        //用户上配置的角色
        this.getUserRoles().forEach(f -> {
            Long roleId = f.getRoleId().getId();
            if (!list.contains(roleId)) list.add(roleId);
        });

        Long userId = this.getId();

        //部门上配置的角色
        List<UserDeptRole> userDeptRoles = UserDeptRole.stream().where(w -> w.getUserId().getId().equals(userId)).toList();
        userDeptRoles.forEach(f -> {
            Long roleId = f.getRoleId().getId();
            if (!list.contains(roleId)) list.add(roleId);
        });

        //职级角色
        Staff staff = this.getStaff();
        if (Framework.isNotNullOrEmpty(staff)) {
            StaffPosition staffPosition = staff.getStaffPositionId();
            if (Framework.isNotNullOrEmpty(staffPosition)) {
                staffPosition.getStaffPositionRoles().forEach(f -> {
                    Long roleId = f.getRoleId().getId();
                    if (!list.contains(roleId)) list.add(roleId);
                });
            }
        }

        return list;
    }

    public String gainIndexPageCode() throws Exception {

        //管理员使用HomePanel页签,新需求(2020-03-03)
        //if (AppContext.current().getProfile().getIsSuperAdmin()) return "HomePanel";
        if (AppContext.current().getProfile().getIsSuperAdmin()) return "Index";

        Long userId = AppContext.current().getProfile().getId();
        String key = String.format("user-gainIndexPageCode-%s", userId);
//          用户首页优先级配置 从上到下
        String code = AppHelper.manage().getCacheUtil().get(key, () -> {
            try {

                //人员
                String pageCode = gainIndexPageCodeForStaff();
                if (Framework.isNotNullOrEmpty(pageCode)) return pageCode;

                //组织架构-质控经理
                pageCode = gainIndexManager();
                if (Framework.isNotNullOrEmpty(pageCode)) return pageCode;

                //组织架构-部门经理、合伙人
                pageCode = gainIndexPartner();
                if (Framework.isNotNullOrEmpty(pageCode)) return pageCode;

                //用户组
                pageCode = gainIndexPageUserGroup();
                if (Framework.isNotNullOrEmpty(pageCode)) return pageCode;

                //职级
                pageCode = gainIndexPageCodeForStaffPosition();
                if (Framework.isNotNullOrEmpty(pageCode)) return pageCode;

                return "Index_Administrative";

            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }
        }, String.class, CacheGroup.Authority);

        return code;
    }

    //人员配置
    public String gainIndexPageCodeForStaff() throws Exception {
        Staff staff = this.getStaff();
        if (Framework.isNotNullOrEmpty(staff) && Framework.isNotNullOrEmpty(staff.getHomeFormConfigurationId()) && Framework.isNotNullOrEmpty(staff.getHomeFormConfigurationId().getFormPageId().getCode())) {
            if (Framework.isNotNullOrEmpty(staff.getHomeFormConfigurationId().getFormPageId().getCode()))
                return staff.getHomeFormConfigurationId().getFormPageId().getCode();
        }
        return null;
    }

    //组织架构-质控经理
    public String gainIndexManager() throws Exception {
        Long userId = AppContext.current().getProfile().getId();
        Optional<Department> first = Department.createNew().stream().where(w -> w.getReviewManagerId().getId() == userId || w.getHeadquartersManagerId().getId() == userId).findFirst();
        if (first.isPresent()) {
            Optional<DeptIndex> index = DeptIndex.createNew().stream().where(w -> w.getHomeFormConfigurationId().getFormPageId().getCode() != null && w.getUserType() == 10).findFirst();
            if (index.isPresent() && Framework.isNotNullOrEmpty(index.get().getHomeFormConfigurationId())) {
                return index.get().getHomeFormConfigurationId().getFormPageId().getCode();
            }
        }
        return null;
    }

    //组织架构-部门经理、合伙人
    public String gainIndexPartner() throws Exception {
        Long userId = AppContext.current().getProfile().getId();
        Optional<Department> first = Department.createNew().stream().where(w -> w.getInChargeOfPartnerId().getId() == userId || w.getMasterId().getId() == userId).findFirst();
        if (first.isPresent()) {
            Optional<DeptIndex> index = DeptIndex.createNew().stream().where(w -> w.getHomeFormConfigurationId().getFormPageId().getCode() != null && w.getUserType() == 20).findFirst();
            if (index.isPresent() && Framework.isNotNullOrEmpty(index.get().getHomeFormConfigurationId())) {
                return index.get().getHomeFormConfigurationId().getFormPageId().getCode();
            }
        }
        return null;
    }

    //用户组
    public String gainIndexPageUserGroup() throws Exception {
        String sql = "Select top 1 p.Code \n" +
                "from Base_UserGroup g \n" +
                "inner join Base_UserGroupItem i on i.UserGroupId=g.Id \n" +
                "inner join Sys_HomeFormConfiguration s on g.homeFormConfigurationId=s.Id \n" +
                "inner join Sys_FormPage p on p.Id=s.formpageId\n" +
                "where g.homeFormConfigurationId is not null and i.userId=?\n";
        List<Object> parameters = new ArrayList<>();
        parameters.add(AppContext.current().getProfile().getId());
        String code = repository().sqlQueryFirst(sql, parameters, String.class);
        if (Framework.isNotNullOrEmpty(code)) return code;
        return null;
    }

    //职级配置
    public String gainIndexPageCodeForStaffPosition() throws Exception {
        Staff staff = this.getStaff();
        if (Framework.isNotNullOrEmpty(staff)) {
            StaffPosition staffPosition = staff.getStaffPositionId();
            if (Framework.isNotNullOrEmpty(staffPosition) && Framework.isNotNullOrEmpty(staffPosition.getHomeFormConfigurationId())) {
                if (Framework.isNotNullOrEmpty(staffPosition.getHomeFormConfigurationId().getFormPageId()))
                    return staffPosition.getHomeFormConfigurationId().getFormPageId().getCode();
            }
        }
        return null;
    }

    public List<TreeDTO> gainAuthorityMenus() throws Exception {

        List<TreeDTO> list = new ArrayList<>();

        //职级角色
        Staff staff = this.getStaff();
        if (Framework.isNotNullOrEmpty(staff) && Framework.isNotNullOrEmpty(staff.getStaffPositionId())) {
            list.addAll(staff.getStaffPositionId().gainAuthorityMenus());
        }

//        //部门
//        Department department = this.getDept();
//        if (Framework.isNotNullOrEmpty(department)) {
//            list.addAll(department.gainAuthorityMenus());
//        }

        //快捷菜单组
        list.addAll(QuickLinkGroup.gainAuthorityMenus());

        return list;
    }

    public WorkFlowUser getWorkFlowUser(Long userId) throws Exception {

        String key = String.format("getWorkFlowUser-%s", userId);

        WorkFlowUser workFlowUser = AppHelper.manage().getCacheUtil().get(key, () -> {
            try {
                User user = this.findById(userId);
                return user.toWorkFlowUser();
            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }

        }, WorkFlowUser.class, CacheGroup.Authority);

        Framework.showSystemMes();

        return workFlowUser;
    }

    public WorkFlowUser toWorkFlowUser() throws Exception {
        WorkFlowUser getUser = new WorkFlowUser();
        ModelHelper.copyModel(this, getUser);
        getUser.setPosition("");//职务后期补充
        return getUser;
    }

    private void updateStaff() throws Exception {
        String code = this.getCode();
        Long department = this.getDeptId();
        Optional<Staff> first = Staff.stream().where(w -> w.getEmployeeNumber().equals(code)).findFirst();
        if (first.isPresent()) {
            Staff staff = first.get();
            staff.initialization();
            staff.setDeptId(department);
            staff.setPassword(this.getLoginPassword());
            repository().add(staff);
        }
    }

    private void validateUserConut() throws Exception {
        if (this.getStatus() != 10) return;
        if (DxnConfig.getConfig().getUserNum() <= 0) return;

        Long userCount = this.stream().where(w -> w.getStatus() == 10).count();
        if (DxnConfig.getConfig().getUserNum() < userCount - 2)
            throw new DxnException(String.format(MessageHelper.gainMessage("IsUserSave"), userCount - 2, DxnConfig.getConfig().getUserNum()));
    }

    private void putUserStatus() throws Exception {
        if (this.getStatus() != 10) return;
        Long userCount = this.stream().where(w -> w.getStatus() == 10).count();
        if (DxnConfig.getConfig().getUserNum() < userCount) this.setStatus(20);
    }

    @SystemService
    public void loginValidate() throws Exception {
        Long userCount = this.stream().where(w -> w.getStatus() == 10).count();
        if (DxnConfig.getConfig().getUserNum() < userCount - 2)
            throw new DxnException(String.format(MessageHelper.gainMessage("IsUser"), userCount - 2, DxnConfig.getConfig().getUserNum()));
    }

    //更新人员上用户信息
    private void updateDeptInfo() throws Exception {
        if (Framework.isNullOrEmpty(this.getDeptReadOnly())) return;
        this.setDeptName(this.getDeptReadOnly().getName());
        this.setDeptTreeName(this.getDeptReadOnly().getTreeName());
        this.setDeptCode(this.getDeptReadOnly().getCode());
        this.setDeptTreeCode(this.getDeptReadOnly().getTreeCode());
    }

    @SystemService(args = "originalPassword,newPassword")
    public boolean changePassword(String originalPassword, String newPassword) throws Exception {

        Long userId = AppContext.current().getProfile().getId();
        Optional<User> userFirst = User.stream().where(c -> c.getId() == userId).findFirst();
        if (userFirst.isPresent()) {//Password:97034062d4fa28fbb7b7702399b07108a
            //Password:73ad6eeec055502d86a90595ac0de644a
            User user = userFirst.get();
            String password = user.getLoginPassword();
            if (!Framework.getPassword(originalPassword).equals(password)) throw new LoginException("原密码输入错误");
            user.setLoginPassword(Framework.getPassword(newPassword));
            repository().add(user);

        } else {
            throw new LoginException("登入信息出现问题");
        }
        return true;
    }

    @SystemService
    public Long openStaffViewModification() throws Exception {
        Long staffId = 0L;
        Long userId = AppContext.current().getProfile().getId();
        Optional<User> userFirst = this.stream().where(c -> c.getId() == userId).findFirst();
        if (userFirst.isPresent()) {
            staffId = userFirst.get().getStaffId();
        }
        return staffId;
    }

    @SystemTask(args = "id")
    public void restLoginTime(Long id) {
        Framework.log.info(String.format("处理UserId:%s", id));
    }

    public String gainAiCheckUrl() throws Exception {
        if (Framework.isNullOrEmpty(DxnConfig.getAiServer())) throw new DxnException("请配置AI核对地址！");
        String passWord = gainSystemPassWord();
        String key = Framework.gainCasLoginKey("System", passWord);
        if (Framework.isNullOrEmpty(key)) throw new DxnException("获取登录票据失败！");
        //if (key.length() != 50) throw new DxnException(key);
        if (Framework.isNullOrEmpty(this.getDeptReadOnly())) throw new DxnException("当前用户部门不能为空！");
        return String.format("%s/auth/authorize?loginName=%s&loginId=%s&departmentName=%s&ticket=%s", DxnConfig.getAiServer(), Framework.encode(this.getLoginName()), this.getUuid(), Framework.encode(this.getDeptReadOnly().getName()), Framework.encode(key));
    }

    private String gainSystemPassWord() throws Exception {
        String str = TransactionHelper.newTransaction(() -> {
            try {
                Optional<User> fist = User.stream().where(w -> w.getLoginName() == "System").findFirst();
                User user = fist.isPresent() ? fist.get() : new User();
                repository().add(user);

                user.setCode("System");
                user.setName("System");
                user.setLoginName("System");
                user.setIsSuperAdmin(false);
                user.setStatus(10);

                String passWord = Framework.getMD5(String.valueOf(Framework.getRandom()));
                user.setLoginPassword(passWord);

                repository().commit();

                return passWord;

            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }
        });
        return str;
    }

    @SystemService
    public List<IPUserStatistics> onlineUsers() throws Exception {
        List<IPUserStatistics> IPUserStatisticsList = new ArrayList<>();
        LocalDateTime data = Framework.getDate(LocalDateTime.now());
        List<User> userList = this.stream().where(c -> c.getIsOnLine() && c.getStatus() == 10 && c.getLoginTime().isAfter(data)).toList();
        int Total = userList.size();
        for (User item : userList) {
            int a = 1;
            for (IPUserStatistics item1 : IPUserStatisticsList) {
                if (item1.getTheServer().equals(item.getServerIP())) {
                    a = 2;
                }
            }
            if (a == 1) {
                IPUserStatistics iPUserStatistics = new IPUserStatistics();
                iPUserStatistics.setTheServer(item.getServerIP());
                IPUserStatisticsList.add(iPUserStatistics);
            }
        }
        for (IPUserStatistics item2 : IPUserStatisticsList) {
            String theServer = item2.getTheServer();
            int OnLineNumber = this.stream().where(c -> c.getLoginIP().equals(theServer) && c.getIsOnLine()).toList().size();
            String wholeNumber = String.valueOf(Total);
            String goOnlineNumber = String.valueOf(OnLineNumber);
            item2.setNumber(OnLineNumber);
            item2.setNumberOfUsers(goOnlineNumber + "/" + wholeNumber);
        }
        return IPUserStatisticsList;
    }

    @SystemService(args = "userIds")
    public void putWeChatUntieUser(List<Long> userIds) throws Exception {
        if (Framework.isNullOrEmpty(userIds)) return;
        List<Object> parameters = new ArrayList<>();
        String where = "";
        for (Long id : userIds) {
            parameters.add(id);
            if (Framework.isNotNullOrEmpty(where)) where += ",";
            where += "?";

            if (parameters.size() > 100) {
                repository().executeCommand(String.format("update Base_user set wechatCode=null where id in (%s);", where), parameters);
                parameters = new ArrayList<>();
                where = "";
            }

        }

        if (Framework.isNotNullOrEmpty(where))
            repository().executeCommand(String.format("update Base_user set wechatCode=null where id in (%s);", where), parameters);
    }

}



