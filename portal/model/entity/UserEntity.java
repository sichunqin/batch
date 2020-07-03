package com.dxn.model.entity;

import com.dxn.entity.EntityBase;
import com.dxn.system.Framework;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.dxn.system.typeFinder.TypeFinderHelper;
import org.springframework.format.annotation.DateTimeFormat;
import com.dxn.system.context.RepositoryContext;
import org.jinq.jpa.JPAJinqStream;
import javax.persistence.*;
import java.time.*;
import java.math.BigDecimal;

@MappedSuperclass
public class UserEntity extends EntityBase<com.dxn.model.extend.User> {

    /** 企业微信号. */
    @Column(name = "weChatCode", columnDefinition = "nvarchar(50)")
    private String weChatCode;

    public void setWeChatCode(String weChatCode) {
        this.weChatCode = weChatCode;
    }

    public String getWeChatCode() {
        return this.weChatCode;
    }

    /** 是否在线. */
    @Column(name = "isOnLine", nullable = false)
    private boolean isOnLine;

    public void setIsOnLine(boolean isOnLine) {
        this.isOnLine = isOnLine;
    }

    public boolean getIsOnLine() {
        return this.isOnLine;
    }

    /** LoginKey. */
    @Column(name = "loginKey", columnDefinition = "nvarchar(50)")
    private String loginKey;

    public void setLoginKey(String loginKey) {
        this.loginKey = loginKey;
    }

    public String getLoginKey() {
        return this.loginKey;
    }

    /** 强制下线时间. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "offlineTime")
    private LocalDateTime offlineTime;

    public void setOfflineTime(LocalDateTime offlineTime) {
        this.offlineTime = offlineTime;
    }

    public LocalDateTime getOfflineTime() {
        return this.offlineTime;
    }

    /** 职位状态. */
    @Column(name = "positionStatus")
    private Integer positionStatus;

    public void setPositionStatus(Integer positionStatus) {
        this.positionStatus = positionStatus;
    }

    public Integer getPositionStatus() {
        return this.positionStatus;
    }

    /** 编码. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(200)", unique = true)
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 登录名. */
    @Column(name = "loginName", nullable = false, columnDefinition = "nvarchar(50)", unique = true)
    private String loginName;

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginName() {
        return this.loginName;
    }

    /** 名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(200)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 密码. */
    @Column(name = "loginPassword", nullable = false, columnDefinition = "nvarchar(1000)")
    private String loginPassword;

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getLoginPassword() {
        return this.loginPassword;
    }

    /** 部门. */
    @Column(name = "deptId")
    private Long deptId;

    public void setDeptId(Long deptid) {
        this.deptId = deptid;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public void setDept(com.dxn.model.extend.Department entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setDeptId(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getDept() throws Exception {
        if(Framework.isNullOrEmpty(this.getDeptId())) return null;
        return com.dxn.model.extend.Department.findById(this.getDeptId());
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getDeptReadOnly() throws Exception {
        if(Framework.isNullOrEmpty(this.getDeptId())) return null;
        return com.dxn.model.extend.Department.findByIdReadOnly(this.getDeptId());
    }

    /** 部门名称. */
    @Column(name = "deptName", columnDefinition = "nvarchar(500)")
    private String deptName;

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptName() {
        return this.deptName;
    }

    /** 部门树名称. */
    @Column(name = "deptTreeName", columnDefinition = "nvarchar(2000)")
    private String deptTreeName;

    public void setDeptTreeName(String deptTreeName) {
        this.deptTreeName = deptTreeName;
    }

    public String getDeptTreeName() {
        return this.deptTreeName;
    }

    /** 部门编码. */
    @Column(name = "deptCode", columnDefinition = "nvarchar(200)")
    private String deptCode;

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptCode() {
        return this.deptCode;
    }

    /** 部门树编码. */
    @Column(name = "deptTreeCode", columnDefinition = "nvarchar(2000)")
    private String deptTreeCode;

    public void setDeptTreeCode(String deptTreeCode) {
        this.deptTreeCode = deptTreeCode;
    }

    public String getDeptTreeCode() {
        return this.deptTreeCode;
    }

    /** 性别. */
    @Column(name = "gender")
    private Integer gender;

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getGender() {
        return this.gender;
    }

    /** 身份证号. */
    @Column(name = "cardID", columnDefinition = "nvarchar(100)")
    private String cardID;

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String getCardID() {
        return this.cardID;
    }

    /** 电子邮件. */
    @Column(name = "email", columnDefinition = "nvarchar(100)")
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    /** 电话. */
    @Column(name = "phone", columnDefinition = "nvarchar(100)")
    private String phone;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    /** 状态. */
    @Column(name = "status", nullable = false)
    private int status;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    /** 是否管理员. */
    @Column(name = "isSuperAdmin", nullable = false)
    private boolean isSuperAdmin;

    public void setIsSuperAdmin(boolean isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }

    public boolean getIsSuperAdmin() {
        return this.isSuperAdmin;
    }

    /** 登录时间. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "loginTime")
    private LocalDateTime loginTime;

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public LocalDateTime getLoginTime() {
        return this.loginTime;
    }

    /** 登录IP. */
    @Column(name = "loginIP", columnDefinition = "nvarchar(50)")
    private String loginIP;

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    public String getLoginIP() {
        return this.loginIP;
    }

    /** 登录服务器IP. */
    @Column(name = "serverIP", columnDefinition = "nvarchar(50)")
    private String serverIP;

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getServerIP() {
        return this.serverIP;
    }

    /** 是否可以盖章. */
    @Column(name = "iSCanBeStamped")
    private Integer iSCanBeStamped;

    public void setISCanBeStamped(Integer iSCanBeStamped) {
        this.iSCanBeStamped = iSCanBeStamped;
    }

    public Integer getISCanBeStamped() {
        return this.iSCanBeStamped;
    }

    /** 备注. */
    @Column(name = "remark", columnDefinition = "nvarchar(3000)")
    private String remark;

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    /** 人员. */
    @Column(name = "staffId")
    private Long staffId;

    public void setStaffId(Long staffid) {
        this.staffId = staffid;
    }

    public Long getStaffId() {
        return this.staffId;
    }

    public void setStaff(com.dxn.model.extend.Staff entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setStaffId(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.Staff getStaff() throws Exception {
        if(Framework.isNullOrEmpty(this.getStaffId())) return null;
        return com.dxn.model.extend.Staff.findById(this.getStaffId());
    }

    @JsonIgnore
    public com.dxn.model.extend.Staff getStaffReadOnly() throws Exception {
        if(Framework.isNullOrEmpty(this.getStaffId())) return null;
        return com.dxn.model.extend.Staff.findByIdReadOnly(this.getStaffId());
    }

    public void setCreatedBy(com.dxn.model.extend.User entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setCreatedById(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.User getCreatedBy() throws Exception {
        if(Framework.isNullOrEmpty(this.getCreatedById())) return null;
        return com.dxn.model.extend.User.findById(this.getCreatedById());
    }

    @JsonIgnore
    public com.dxn.model.extend.User getCreatedByReadOnly() throws Exception {
        if(Framework.isNullOrEmpty(this.getCreatedById())) return null;
        return com.dxn.model.extend.User.findByIdReadOnly(this.getCreatedById());
    }

    public void setModifiedBy(com.dxn.model.extend.User entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setModifiedById(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.User getModifiedBy() throws Exception {
        if(Framework.isNullOrEmpty(this.getModifiedById())) return null;
        return com.dxn.model.extend.User.findById(this.getModifiedById());
    }

    @JsonIgnore
    public com.dxn.model.extend.User getModifiedByReadOnly() throws Exception {
        if(Framework.isNullOrEmpty(this.getModifiedById())) return null;
        return com.dxn.model.extend.User.findByIdReadOnly(this.getModifiedById());
    }

    /** 单位管理范围列表. */
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.DeptManagementScope> deptManagementScopes = new java.util.ArrayList<>();

    public void setDeptManagementScopes(java.util.List<com.dxn.model.extend.DeptManagementScope> deptManagementScopes) {
        this.deptManagementScopes = deptManagementScopes;
    }

    public java.util.List<com.dxn.model.extend.DeptManagementScope> getDeptManagementScopes() {
        if (this.deptManagementScopes != null) {
           for (com.dxn.model.extend.DeptManagementScope item : this.deptManagementScopes) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.deptManagementScopes;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DeptManagementScope> readDeptManagementScopes() {
        return this.deptManagementScopes;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DeptManagementScope> getDeptManagementScopesReadOnly() {
        return this.deptManagementScopes;
    }

    /** 用户权限列表. */
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.UserRole> userRoles = new java.util.ArrayList<>();

    public void setUserRoles(java.util.List<com.dxn.model.extend.UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public java.util.List<com.dxn.model.extend.UserRole> getUserRoles() {
        if (this.userRoles != null) {
           for (com.dxn.model.extend.UserRole item : this.userRoles) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.userRoles;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.UserRole> readUserRoles() {
        return this.userRoles;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.UserRole> getUserRolesReadOnly() {
        return this.userRoles;
    }

    @JsonIgnore
    public static com.dxn.model.extend.User findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.User.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.User findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.User.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.User findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.User.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.User findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.User.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.User createNew() throws Exception {
        com.dxn.model.extend.User entity = new com.dxn.model.extend.User();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.User> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.User.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "HR";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_User";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "用户";
    }

}
