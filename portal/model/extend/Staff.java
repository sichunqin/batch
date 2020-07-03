package com.dxn.model.extend;

import com.dxn.forms.dto.TreeDTO;
import com.dxn.model.entity.StaffEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.ResponseEntity;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.configuration.DxnConfig;
import com.dxn.system.context.AppContext;
import com.dxn.system.exception.BusinessException;
import com.dxn.system.webUrl.PostParameter;
import com.dxn.system.webUrl.WebUrlRequest;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


@Entity
@Table(name = "HR_Staff")
public class Staff extends StaffEntity {

    //修改前
    @Override
    public void onUpdating() throws Exception {
        super.onUpdating();
        //业务代码
        this.addAndmodifyUser(this);
        this.setUpadteCpa();
        this.getOriginalValue().getPositionStatus();
        if (this.getPositionStatus() == 30 && this.getPositionStatus() != this.getOriginalValue().getPositionStatus()) {
            this.setEmployeeNumber("");
        }
        if (this.getPositionStatus() != 30 && this.getOriginalValue().getPositionStatus() == 30) {
            this.setEmployeeNumber("");
        }
//        if (this.getDept().getParentId().getId()!=this.getOriginalValue().getDept().getParentId().getId()){
//            this.setEmployeeNumber("");
//        }
        //变更离职状态业务校验
        if (this.getPositionStatus() == 30) {
            this.quitValidate();
        }
    }

    //    private void quitValidate() throws Exception{
//        Long id = this.getId();
//        List<Department> departmentList = Department.stream().where(data -> data.getHeadquartersManagerId().getId().equals(id) || data.getIndependentManagerId().getId().equals(id) || data.getPersonInChargeId().getId().equals(id) || data.getFinancialManagerId().getId().equals(id) || data.getArchivistId().getId().equals(id) || data.getReportingStaffId().getId().equals(id) || data.getMasterId().getId().equals(id) || data.getInChargeOfPartnerId().getId().equals(id) || data.getReviewManagerId().equals(id)).toList();
//        List<UserGroupItem> userGroupItemList = UserGroupItem.stream().where(data -> data.getId().equals(id)).toList();
//
//        if(departmentList != null && departmentList.size() > 0){
//            throw new BusinessException(departmentList.get(0).getName() + "下存在该人员无法变更为离职状态");
//        }
//        if(userGroupItemList != null && userGroupItemList.size() > 0){
//            Long userGroupId = userGroupItemList.get(0).getUserGroupId().getId();
//            Optional<UserGroup> optional = UserGroup.stream().where(data -> data.getId().equals(userGroupId)).findFirst();
//            if(optional.isPresent()) throw new BusinessException(optional.get().getName() + "该用户组下存在该人员，无法变更为离职状态");
//        }
//
//
//
//    }
    private void quitValidate() throws Exception {

        Long staffId = this.getId();
        Optional<User> optionalUser = User.stream().where(data -> data.getStaffId().equals(staffId)).findFirst();
        Long id = optionalUser.get().getId();
        Long staffDeptId = this.getDeptId();
        List<Department> departmentList = Department.stream().where(data -> !staffDeptId.equals(data.getId()) && (id.equals(data.getHeadquartersManagerId().getId()) || id.equals(data.getIndependentManagerId().getId()) || id.equals(data.getPersonInChargeId().getId()) || id.equals(data.getFinancialManagerId().getId()) || id.equals(data.getArchivistId().getId()) || id.equals(data.getReportingStaffId().getId()) || id.equals(data.getMasterId().getId()) || id.equals(data.getInChargeOfPartnerId().getId()) || id.equals(data.getReviewManagerId().getId()))).toList();
        List<UserGroupItem> userGroupItemList = UserGroupItem.stream().where(data -> data.getId().equals(id)).toList();

        if (departmentList != null && departmentList.size() > 0) {
            throw new BusinessException(departmentList.get(0).getName() + "下存在该人员无法变更为离职状态");
        }
        if (userGroupItemList != null && userGroupItemList.size() > 0) {
            Long userGroupId = userGroupItemList.get(0).getUserGroupId().getId();
            Optional<UserGroup> optional = UserGroup.stream().where(data -> data.getId().equals(userGroupId)).findFirst();
            if (optional.isPresent()) throw new BusinessException(optional.get().getName() + "该用户组下存在该人员，无法变更为离职状态");
        }

        PostParameter parameter = new PostParameter();
        parameter.add("userId", id);
//        List<WorkFlowNode> workFlowNodeList = Framework.parseJsonArray(requestUrl("checkUserInWorkflow",parameter), WorkFlowNode.class);

        // Optional<User> optionalUser = User.stream().where(data -> data.getId().equals(id)).findFirst();
        if (optionalUser.isPresent()) {
            Long deptId = optionalUser.get().getDeptId();
            Optional<Department> optionalDepartment = Department.stream().where(data -> data.getId().equals(deptId)).findFirst();
            Long toUserId = null;
            Department department = optionalDepartment.get();

            Long parentId = department.getParentId().getId();
            Optional<Department> parentDepartment = Department.stream().where(data -> parentId.equals(data.getId())).findFirst();

            if (department.getMasterId() != null && id.equals(department.getMasterId().getId())) {                    //部门经理
                if (department.getInChargeOfPartnerId() == null) {
                    if (department.getPersonInChargeId() == null) {
                        if (parentDepartment.get().getPersonInChargeId() == null) {
                            throw new BusinessException("当前部门及其上级没有设置单位负责人，无法进行移交");
                        } else {
                            toUserId = parentDepartment.get().getPersonInChargeId().getId();
                        }
                    }
                } else {
                    toUserId = department.getInChargeOfPartnerId().getId();
                }

            } else if (department.getInChargeOfPartnerId() != null && id.equals(department.getInChargeOfPartnerId().getId())) {   //分管合伙人
                if (department.getPersonInChargeId() == null) {
                    if (parentDepartment.get().getPersonInChargeId() == null) {
                        throw new BusinessException("当前部门及其上级没有设置单位负责人，无法进行移交");
                    } else {
                        toUserId = parentDepartment.get().getPersonInChargeId().getId();
                    }
                } else {
                    toUserId = department.getPersonInChargeId().getId();
                }
            } else {                                                              //项目经理
                if (department.getMasterId() == null) {
                    if (department.getInChargeOfPartnerId() == null) {
                        if (department.getPersonInChargeId() == null) {
                            if (parentDepartment.get().getPersonInChargeId() == null) {
                                throw new BusinessException("当前部门及其上级没有设置单位负责人，无法进行移交");
                            } else {
                                toUserId = parentDepartment.get().getPersonInChargeId().getId();
                            }
                        }
                    } else {
                        toUserId = department.getInChargeOfPartnerId().getId();
                    }
                } else {
                    toUserId = department.getMasterId().getId();
                }
            }


            PostParameter postParameter = new PostParameter();
            postParameter.add("fromUserId", id);
            postParameter.add("toUserId", toUserId);
            requestUrl("handOverTodo", postParameter);

//            if(workFlowNodeList != null && workFlowNodeList.size() > 0){
//                for(WorkFlowNode workFlowNode : workFlowNodeList){
//                    this.gainEntity(workFlowNode.getEntityType(),workFlowNode.getEntityId());
//                }
//            }
        }


    }


    private String requestUrl(String requestMapping, PostParameter parameter) throws Exception {
        return WebUrlRequest.get(String.format("%s/workflow-design/wf/restful/%s", DxnConfig.getWorkFlowPath(), requestMapping), parameter);
    }


    //4.1.1	设置默认值
    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        //业务代码
        this.setDocState(10);
    }

    //插入前
    @Override
    public void onInserting() throws Exception {
        super.onInserting();
        this.setIsDeclare(20);
        this.setIsIndependence(20);
    }

    //插入后
    @Override
    public void onInserted() throws Exception {
        super.onInserted();
        this.addAndmodifyUser(this);
        this.insertStaffPositionRoleForUser();
    }

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Authority);
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Schema);
    }

    private void insertStaffPositionRoleForUser() {
        Long staffId = this.getId();
        try {
            Optional<User> userFirst = User.stream().where(c -> c.getStaffId() == staffId).findFirst();
            User user = null;
            if (userFirst.isPresent()) {
                user = userFirst.get();
            }
            Long staffPositionId = this.getStaffPositionId().getId();
            List<StaffPositionRole> staffPositionList = StaffPositionRole.stream().where(c -> c.getStaffPositionId().getId() == staffPositionId).toList();
            for (StaffPositionRole item : staffPositionList) {
                UserRole model = UserRole.createNew();
                model.setIsPositionRole(10);
                model.setRoleId(item.getRoleId());
                model.setUserId(user);
                repository().add(model);
                repository().commit();
            }
        } catch (Exception e) {
            Framework.printStackTrace(e);
        }
    }

    @Override
    public void onUpdated() throws Exception {
        super.onUpdated();
        this.updateStaffPositionRoleForUser();
        this.updatePassword();
        if (!Framework.isNullOrEmpty(this.getIdentificationNumber())) {
            this.setDateOfBirth(idNumber(this.getIdentificationNumber()));
        }
        this.professionalQualificationGeneration();
        this.setUpadteCpa();
    }

    @Override
    public void onDeleting() throws Exception {
        super.onDeleting();
        this.userRemove();
    }


    //    //验证方法
    @Override
    public void onValidate() throws Exception {

        //业务代码
        super.onValidate();
        //员工类型
        gainEmployeeType();
        Long Id = this.getId();
        //验证身份证
        this.validateCardID();
        //姓名校验
        this.duplicateName();
        //邮箱验证
        this.emailCheck();
        //独立性不为空自动添加
        this.independence();
        if (!Framework.isPassword(this.getPassword())) {
            //密码强制验证
            mandatoryVerification(this.getPassword());
            //密码建议验证
            proposedVerification(this.getPassword());
        }

        if (this.getMobilePhone() != null) {
            //手机号验证
            if (!Framework.isPhone(this.getMobilePhone())) {
                throw new BusinessException("手机号格式不合法");
            }
        }
        if (this.getECPhone() != null) {
            if (!Framework.isPhone(this.getECPhone())) {
                throw new BusinessException("紧急联系人电话不合法");
            }
        }
        //日期类型的校验

        //当前时间
        LocalDateTime sd3 = LocalDateTime.now();
        //入职日期
        LocalDateTime sd1 = this.getDateOfEntry();
        //转正日期
        LocalDateTime sd2 = this.getPositiveDate();
        if (sd2 != null) {
            if (!sd2.isBefore(sd3)) {
                throw new BusinessException("转正时间不能大于当前时间");
            }
        }
    }

    //姓名不能重复校验
    private void duplicateName() throws Exception {
        Long number = 0L;
        Long id = this.getId();
        String name = this.getName();
        if (Framework.isNullOrEmpty(id)) {
            number = this.stream().where(w -> w.getName().equals(name)).count();
        } else {
            number = this.stream().where(w -> w.getName().equals(name) && !w.getId().equals(id)).count();
        }
        if (number > 0) throw new BusinessException("姓名不能重复");
    }

    //邮箱校验
    private void emailCheck() throws Exception {
        String email = this.getMailbox();
        if (email != null) {
            //验证身份证
            if (!Framework.isEmail(email)) throw new BusinessException("邮箱格式错误");
            Long number = 0L;
            Long id = this.getId();
            if (Framework.isNullOrEmpty(id)) {
                number = this.stream().where(w -> w.getMailbox().equals(email)).count();
            } else {
                number = this.stream().where(w -> w.getMailbox().equals(email) && !w.getId().equals(id)).count();
            }
            if (number > 0) throw new BusinessException("邮箱账号重复");
        }

    }

    private void validateCardID() throws Exception {

        //身份证教研
        String number = this.getIdentificationNumber();
        if (number != null) {
            //验证身份证
            if (!Framework.isCardID(number)) throw new BusinessException("身份证错误");
            Long count = 0L;
            Long id = this.getId();
            if (Framework.isNullOrEmpty(this.getId())) {
                count = this.stream().where(w -> w.getIdentificationNumber().equals(number)).count();
            } else {
                count = this.stream().where(w -> w.getIdentificationNumber().equals(number) && !w.getId().equals(id)).count();
            }
            if (count > 0) throw new BusinessException("身份证重复");
        }
    }

    private void updatePassword() {
        String a = this.getPassword();
        String b = this.getOriginalValue().getPassword();
        if (b != null) {
            if (!a.equals(b)) {
                Framework.log.info("密码修改了");
            }
        }
    }


    //人员删除用户删除
    private void userRemove() throws Exception {
        Long id = this.getId();
//        String code = this.getEmployeeNumber();
        List<User> userList = User.stream().where(w -> w.getStaffId() == id).toList();
        //List<User> userList = User.stream().where(w -> w.getStaffId() == id).toList();
        for (User item : userList) {
            //item.initialization();
            repository().remove(item);
        }
    }


    //员工 用户同步修改新增
    private void addAndmodifyUser(Staff staff) throws Exception {
        Long staffId = this.getId();
        List<User> userList = User.stream().where(w -> w.getStaffId() == staffId).toList();
        if (userList.size() > 0) {
            User u = userList.get(0);
            u.initialization();
            u.setName(staff.getEmployeeNumber());
            //名称
            u.setLoginName(staff.getName());
            //密码
            if (!Framework.isPassword(this.getPassword())) {
                u.setLoginPassword(Framework.getPassword(staff.getPassword()));
            }
            //性别
            u.setGender(staff.getGener());
            // 登入名
            u.setName(staff.getName());
            //创建人（先写死了，等要求完后修改）
            u.setCode(staff.getEmployeeNumber());
            //身份证号
            u.setCardID(staff.getIdentificationNumber());
            // 电子邮箱
            u.setEmail(staff.getMailbox());
            // 电话
            u.setPhone(staff.getMobilePhone());
            // 登入时间(现在设的是出生日期)
            u.setLoginTime(staff.getDateOfBirth());
            // 登入ip(现在是静态的 等要求后设置动态)
            u.setLoginIP("192.168.30.11");
            // 备注
            //职位状态
            u.setPositionStatus(staff.getPositionStatus());
            //部门
            u.setDeptId(staff.getDept().getId());
            if (staff.getPositionStatus() == 30 || staff.getPositionStatus() == 40) {
                u.setStatus(20);
            } else {
                u.setStatus(10);
            }
            repository().add(u);
            //岗位调动
            if (this.getOriginalValue().getDeptId() != this.getDeptId()) {
                JobChange jobChange = new JobChange();
                jobChange.setStaffId(this);
                jobChange.setTransactionType(10);
                jobChange.setDOSubmission(LocalDateTime.now());
                jobChange.setApprovalDate(LocalDateTime.now());
                jobChange.setOperator(AppContext.current().getProfile().getName());
                repository().add(jobChange);
            }
            if (this.getOriginalValue().getStaffPositionId().getId() != this.getStaffPositionId().getId()) {
                JobChange jobChange = new JobChange();
                jobChange.setStaffId(this);
                jobChange.setTransactionType(20);
                jobChange.setDOSubmission(LocalDateTime.now());
                jobChange.setApprovalDate(LocalDateTime.now());
                jobChange.setOperator(AppContext.current().getProfile().getName());
                repository().add(jobChange);
            }

        } else {
            userAutomaticGeneration(staff);
        }
    }

    //用户自动增加
    private void userAutomaticGeneration(Staff staff) throws Exception {
        //业务代码
        User userEntity = User.createNew();
        //名称
        userEntity.setLoginName(staff.getName());
        //密码
        userEntity.setLoginPassword(Framework.getPassword(staff.getPassword()));
        //性别
        userEntity.setGender(staff.getGener());
        // 登入名
        userEntity.setName(staff.getName());
        // 状态
        userEntity.setStatus(10);
        //创建人（先写死了，等要求完后修改）
        userEntity.setCode(staff.getEmployeeNumber());
        //身份证号
        userEntity.setCardID(staff.getIdentificationNumber());
        // 电子邮箱
        userEntity.setEmail(staff.getMailbox());
        // 电话
        userEntity.setPhone(staff.getMobilePhone());
        // 登入时间(现在设的是出生日期)
        userEntity.setLoginTime(staff.getDateOfBirth());
        // 登入ip(现在是静态的 等要求后设置动态)
        userEntity.setLoginIP("192.168.30.11");
        // 备注
        //职位状态
        userEntity.setPositionStatus(staff.getPositionStatus());
        if (staff.getPositionStatus() == 30 || staff.getPositionStatus() == 40) {
            userEntity.setStatus(20);
        } else {
            userEntity.setStatus(10);
        }
        //部门
        userEntity.setDeptId(staff.getDept().getId());
        userEntity.setStaffId(staff.getId());

        repository().add(userEntity);
        repository().commit();

    }

    private void professionalQualificationGeneration() throws Exception {
        List<EnumValue> lisenumValue = new ArrayList<>();
        List<EnumValue> lisenumValues = new ArrayList<>();
        List<Integer> listInteger = new ArrayList<>();
        List<Integer> listInteger2 = new ArrayList<>();
        List<String> listString = new ArrayList<>();
        List<String> listStrings = new ArrayList<>();
        Long d = this.getId();
        List<CPAQualification> listCpa = CPAQualification.stream().where(w -> w.getStaffId() != null && w.getStaffId().getId() == d).toList();
        List<ProfessionalTechnology> listProfessionalTechnology = ProfessionalTechnology.stream().where(w -> w.getStaffId() != null && w.getStaffId().getId() == d).toList();
        if (Framework.isNullOrEmpty(listCpa)) {
            return;
        } else {
            for (CPAQualification cpa : listCpa) {
                listInteger.add(cpa.getAccountantType());
            }
        }
        if (Framework.isNullOrEmpty(listProfessionalTechnology)) {
            return;
        } else {
            for (ProfessionalTechnology professionalTechnology : listProfessionalTechnology) {
                listInteger2.add(professionalTechnology.getTechnicalTitle());
            }
        }

        for (Integer accountantType : listInteger) {
            lisenumValue = EnumValue.stream().where(w -> w.getValue() == accountantType && w.getEnumTypeId().equals(16327)).toList();

            if (lisenumValue.size() < 1) return;
            listString.add(lisenumValue.get(0).getName());
        }

        for (Integer technicalTitle : listInteger2) {
            lisenumValues = EnumValue.stream().where(w -> w.getValue() == technicalTitle && w.getEnumTypeId().equals(16327)).toList();

            if (lisenumValues.size() < 1) return;
            listStrings.add(lisenumValues.get(0).getName());
        }

        String pq = listToString1(listString);
        String pt = listToString1(listStrings);
        this.setPractisingQualification(pq);
        this.setProfessionalTechnicalTitle(pt);
    }

    public static String listToString1(List<String> list) {
        StringBuilder sB = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sB.append(list.get(i) + "/");
                } else {
                    sB.append(list.get(i));
                }
            }
        }
        return sB.toString();
    }

    //身份证转换成Date格式的日期
    public static LocalDateTime idNumber(String identificationNumber) throws Exception {
        String Id = identificationNumber;
        String year = Id.substring(6, 10);
        String month = Id.substring(10, 12);
        String day = Id.substring(12, 14);
        String birthday = year + "-" + month + "-" + day;
        birthday += " 00:00:00";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(birthday, df);
        return ldt;
    }

    private void setUpadteCpa() throws Exception {
        Long id = this.getId();
        Long AccountantTypeLong;
        List<Staff> staffList = this.stream().where(c -> c.getId() == id).toList();
        Staff staff = staffList.get(0);
        AccountantTypeLong = CPAQualification.stream().where(c -> c.getStaffId().getId() == id && c.getAccountantType() == 10).count();
        if (AccountantTypeLong > 0) {
            staff.initialization();
            staff.setISCPA(10);
            repository().add(staff);
        } else {
            staff.initialization();
            staff.setISCPA(20);
            repository().add(staff);
        }
    }

    private void independence() throws Exception {
        Integer declare = this.getIsDeclare();
        Integer independence = this.getIsIndependence();
        if (Framework.isNullOrEmpty(declare)) {
            this.setIsDeclare(20);
        }
        if (Framework.isNullOrEmpty(independence)) {
            this.setIsIndependence(20);
        }
    }


    private void updateStaffPositionRoleForUser() {
        Long staffId = this.getId();
        try {
            Optional<User> userFirst = User.stream().where(c -> c.getStaffId() == staffId).findFirst();
            User user = null;
            if (userFirst.isPresent()) {
                user = userFirst.get();
            }
            Long staffPositionId = this.getStaffPositionId().getId();
            Long userId = user.getId();
            List<StaffPositionRole> staffPositionList = StaffPositionRole.stream().where(c -> c.getStaffPositionId().getId() == staffPositionId).toList();
            List<UserRole> userRoleList = UserRole.stream().where(c -> c.getUserId().getId() == userId && c.getIsPositionRole().equals(10)).toList();

            List<UserRole> delMember = new ArrayList<>();
            userRoleList.forEach(f -> {
                delMember.add(f);
            });

            for (UserRole item : delMember) {
                item.initialization();
                user.getUserRoles().remove(item);
                repository().remove(item);
            }
            for (StaffPositionRole item : staffPositionList) {
                UserRole model = UserRole.createNew();
                model.setIsPositionRole(10);
                model.setRoleId(item.getRoleId());
                model.setUserId(user);
                repository().add(model);
                repository().commit();
            }
        } catch (Exception e) {
            Framework.printStackTrace(e);
        }
    }


    //员工类型
    private void gainEmployeeType() throws Exception {
        Long staffPositionId = this.getStaffPositionId().getId();
        if (Framework.isNullOrEmpty(staffPositionId)) {
            this.setEmployeeType(null);
        } else if (staffPositionId == 1) {
            this.setEmployeeType(10);
        } else if (staffPositionId == 2) {
            this.setEmployeeType(20);
        } else if (staffPositionId == 3) {
            this.setEmployeeType(30);
        }
    }


    @SystemService()
    public List<TreeDTO> staffShortcutInterface() throws Exception {
        List<TreeDTO> list = new ArrayList<>();
        TreeDTO dto = new TreeDTO();
        dto.setId("1");
        dto.setText("修改密码");
        list.add(dto);
        TreeDTO dto1 = new TreeDTO();
        dto1.setId("2");
        dto1.setText("修改个人信息");
        list.add(dto1);
        return list;
    }


    //    @SystemService()
//    public String toEnable1() throws Exception {
//        List<Staff> staffList = this.stream().toList();
//        for (Staff item : staffList) {
//            addAndmodifyUser(item);
//
//        }
//        repository().commit();
//        return "启用完成!";
//    }
//    }
    //密码规则强制校验
    public void mandatoryVerification(String pwd) throws Exception {
        forcePasswordRuleValidation1(pwd);
        forcePasswordRuleValidation2(pwd);
        forcePasswordRuleValidation3(pwd);

    }

    //密码规则强制校验
    public void proposedVerification(String pwd) throws Exception {
        proposalPasswordRuleValidation1(pwd);
        proposalPasswordRuleValidation2(pwd);
    }

    // 密码是否存在3顺位
    public void forcePasswordRuleValidation1(String pwd) throws Exception {
        int count = 0;//正序次数
        String[] strArr = pwd.split("");
        for (int i = 1; i < strArr.length - 1; i++) {//从1开始是因为划分数组时，第一个为空
            if (positiveSequenceJudgement(strArr[i - 1], strArr[i])) {
                count = count + 1;
            } else {
                count = 0;
            }
        }
        if (count >= 2) throw new BusinessException("3位以上数字或字母不能连号");

    }

    //正序比较
    public boolean positiveSequenceJudgement(String str1, String str2) throws Exception {
        if (str2.hashCode() - str1.hashCode() == 1) return true;
        return false;
    }

    //判断字符串包含数字 字母  8-20位
    public void forcePasswordRuleValidation2(String company) throws Exception {

        String regex = "^(?![A-Za-z]+$)(?!\\d+$)(?![\\W_]+$)\\S{8,20}$";
        if (!company.matches(regex)) {
            throw new BusinessException("密码必须包含数字字母字符其中两种且在8-20位之间");
        }
    }

    //判断密码是否存在用户名
    public void forcePasswordRuleValidation3(String pwd) throws Exception {
        //存在报错
        if (pwd.contains(this.getName())) {
            throw new BusinessException("密码不能包含用户名");
        }
    }


    //判断不能3个重复
    public void proposalPasswordRuleValidation1(String company) throws Exception {
        int count = 0;//正序次数
        String[] strArr = company.split("");
        for (int i = 1; i < strArr.length - 1; i++) {
            if (strArr[i - 1].equals(strArr[i])) {
                count++;
            } else {
                count = 0;
            }
            if (count >= 2) throw new BusinessException("密码不能包含3位以上重复");
        }
    }

    //判断密码是否包含用户名大小写
    public void proposalPasswordRuleValidation2(String pwd) throws Exception {
        //小写
        String lowercaseUser = ToPinyin(this.getName());
        String uppercaseUser = lowercaseUser.toUpperCase();
        if (pwd.contains(uppercaseUser)) {
            throw new BusinessException("密码不能包含姓名大小写");
        }
        if (pwd.contains(lowercaseUser)) {
            throw new BusinessException("密码不能包含姓名大小写");
        }

    }

    //汉字转为拼音
    public String ToPinyin(String chinese) throws Exception {
        String pinyinStr = "";
        char[] newChar = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0];
            } else {
                pinyinStr += newChar[i];
            }
        }
        return pinyinStr;
    }

    @SystemService(args = "userId")
    public ResponseEntity ganiUserHeadPortrait(String userId) throws Exception {
        ResponseEntity responseEntity = new ResponseEntity();
        try {

            Optional<User> user = User.stream().where(c -> c.getUuid().equals(userId)).findFirst();
            if (user.isPresent()) {
                Staff staff = this.findBaseById(user.get().getStaffId());
                if (Framework.isNotNullOrEmpty(staff)) {
                    byte[] b2 = Base64.getEncoder().encode(image2Bytes(Paths.get(DxnConfig.getFileUploadPath(), staff.getHeadPortraitId()).toString()));
                    byte[] b3 = Base64.getDecoder().decode(b2);
                    responseEntity.setCode(200);
                    responseEntity.setData(b3);
                }
            }
        } catch (Exception ex) {
            responseEntity.setCode(500);
            responseEntity.setMessages("服务器内部错误");
        }
        return responseEntity;

    }

    static byte[] image2Bytes(String imgSrc) throws Exception {
        FileInputStream fin = new FileInputStream(new File(imgSrc));
        //可能溢出,简单起见就不考虑太多,如果太大就要另外想办法，比如一次传入固定长度byte[]
        byte[] bytes = new byte[fin.available()];
        //将文件内容写入字节数组，提供测试的case
        fin.read(bytes);

        fin.close();
        return bytes;
    }

    /**
     * 解码
     *
     * @param b
     * @param tagSrc
     * @throws Exception
     */
    static void buff2Image(byte[] b, String tagSrc) throws Exception {
        FileOutputStream fout = new FileOutputStream(tagSrc);
        //将字节写入文件
        fout.write(b);
        fout.close();
    }

}
