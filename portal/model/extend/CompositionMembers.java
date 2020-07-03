package com.dxn.model.extend;

import com.dxn.dto.enums.IsChangeEnum;
import com.dxn.dto.enums.IsEnableEnum;
import com.dxn.dto.enums.RoleLevelEnum;
import com.dxn.model.entity.CompositionMembersEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Entity
@Table(name = "Prj_CompositionMembers")
public class CompositionMembers extends CompositionMembersEntity {

    @SystemService(args = "memberId,compositionId")
    public void insertCompositionMember(List<Long> memberId, Long compositionId) throws Exception {
        for (Long item : memberId) {
            ProjectMembers projectMember = ProjectMembers.stream().where(c -> c.getId() == item).toList().get(0);
            Long userId = projectMember.getNameId().getId();
            List<CompositionMembers> compostionList = this.stream().where(c -> c.getNameId() != null && c.getNameId().getId() == userId && c.getCompositionCustomerId().getId() == compositionId && c.getRoleName() == 10).toList();
            if (compostionList.size() > 0) {
                compostionList.get(0).setIsEnable(IsEnableEnum.Enable.getIndex());
                repository().add(compostionList.get(0));
            } else {
                CompositionMembers entity = this.createNew();
                entity.setCompositionCustomer(compositionId);
                entity.setNameId(projectMember.getNameId());
                entity.setSort(projectMember.getSort());
                entity.setIsEnable(IsEnableEnum.Enable.getIndex());
                entity.setCode(projectMember.getNameId().getCode());
                entity.setRank(projectMember.getRank());   //职级后期需要改变类型
                entity.setSignatureOfIndependence(projectMember.getSignatureOfIndependence());
                Optional<User> userListf = User.stream().where(c -> c.getId() == userId).findFirst();
                if (userListf.isPresent()) {
                    Long staffId = userListf.get().getStaffId();
                    Optional<Staff> staffListf = Staff.stream().where(c -> c.getId() == staffId).findFirst();
                    if (staffListf.isPresent()) {
                        Staff staff = staffListf.get();
                        String practisingQualification = staff.getPractisingQualification();
                        entity.setQualifications(practisingQualification);
                    }
                }
                entity.setWorkingYears(projectMember.getWorkingYears());
                entity.setIsExperience(projectMember.getIsExperience());
                entity.setRoleName(RoleLevelEnum.CompilingPerson.getIndex());//角色后期需要改变类型  编制人
                repository().add(entity);
            }
        }
        Optional<CompositionCustomer> compostionEntity = CompositionCustomer.stream().where(c -> c.getId() == compositionId).findFirst();
        CompositionCustomer entity = compostionEntity.get();
        entity.setIsMemberChanged(IsChangeEnum.Yes.getIndex());
        repository().add(entity);

        if (entity.getProjectId().getDocState() == 11 || entity.getProjectId().getDocState() == 30) {
            CompositionCustomer compositionCustomer = CompositionCustomer.createNew();
            compositionCustomer.comterModifyType(Framework.createGuid().toString(), 30, compositionId);
        }
    }

    @SystemService(args = "compostionId,userId,signatureOfindependence")
    public void SaveMember(Long compostionId, Long userId, Integer signatureOfindependence) throws Exception {
        Long uuserId = userId;

        List<CompositionCustomer> compositionList = CompositionCustomer.stream().where(c -> c.getId() == compostionId).toList();
        //先替换原有被审计单位中项目经理
        CompositionCustomer compotionEntity = compositionList.get(0);
        compotionEntity.initialization();
        compotionEntity.setProjectManager(uuserId);
        compotionEntity.setIsMemberChanged(IsChangeEnum.Yes.getIndex());
        repository().add(compotionEntity);

        //先删除被审计单位成员表中原有的项目经理数据，在添加一条刚选择完的人员数据
        List<CompositionMembers> memberList = this.stream().where(c -> c.getCompositionCustomerId().getId() == compostionId).toList();
        Integer smallManger = RoleLevelEnum.SmallManger.getIndex();
        Optional<CompositionMembers> member = memberList.stream().filter(c -> c.getRoleName() == smallManger).findFirst();
        if (member.isPresent()) {
            repository().remove(member.get());
        }
        Integer CompilingPerson = RoleLevelEnum.CompilingPerson.getIndex();
        Optional<CompositionMembers> compilingPersonMember = memberList.stream().filter(c -> c.getRoleName() == CompilingPerson).findFirst();
        if (compilingPersonMember.isPresent()) {
            repository().remove(compilingPersonMember.get());
        }
        CompositionMembers members = CompositionMembers.createNew();
        List<User> user = User.stream().where(c -> c.getId() == uuserId).toList();
        if (user.size() == 0) return;
        members.setCompositionCustomer(compostionId);
        members.setNameId(user.get(0));
        members.setCode(user.get(0).getCode());
        members.setRank(getRankName(user.get(0).getId()));   //职级后期需要改变类型
        members.setSignatureOfIndependence(signatureOfindependence);
        Optional<User> userListf = User.stream().where(c -> c.getId() == userId).findFirst();
        if (userListf.isPresent()) {
            Long staffId = userListf.get().getStaffId();
            Optional<Staff> staffListf = Staff.stream().where(c -> c.getId() == staffId).findFirst();
            if (staffListf.isPresent()) {
                Staff staff = staffListf.get();
                String practisingQualification = staff.getPractisingQualification();
                members.setQualifications(practisingQualification);

            }
        }
        Project project1=new Project();
        members.setWorkingYears(project1.gainWorkingYears(user.get(0)));
        members.setIsExperience(10);
        members.setSort(0);
        members.setIsEnable(IsEnableEnum.Enable.getIndex());
        members.setRoleName(RoleLevelEnum.SmallManger.getIndex());//角色后期需要改变类型   30是大项目经理
        repository().add(members);

        //根据所选人员的id找出部门经理添加进去， 并且查看当前表中是否存在这条数据，如果存在先删除，在添加
        int deptManger = RoleLevelEnum.DeptManger.getIndex();
        Optional<CompositionMembers> deptMemeberList = memberList.stream().filter(c -> c.getRoleName() == deptManger).findFirst();
        if (deptMemeberList.isPresent()) {
            repository().remove(deptMemeberList.get());
        }

        if (Framework.isNotNullOrEmpty(user.get(0).getDeptId())) {
            Long deptid = user.get(0).getDeptId();
            List<Department> deptModel = Department.stream().where(c -> c.getId() == deptid).toList();
            User deptUser = deptModel.get(0).getMasterId();
            if (Framework.isNotNullOrEmpty(deptUser)) {
                SaveDeptUser(deptUser, compostionId, signatureOfindependence);
            }
        }
        if (compositionList.get(0).getProjectId().getDocState() == 11 || compositionList.get(0).getProjectId().getDocState() == 30) {
            CompositionCustomer compositionCustomer = CompositionCustomer.createNew();
            compositionCustomer.comterModifyType(Framework.createGuid().toString(), 30, compostionId);
        }
    }

    public void SaveDeptUser(User user, Long compostionId, Integer signatureOfindependence) throws Exception {
        CompositionMembers deptmembers = CompositionMembers.createNew();
        Long userId = user.getId();
        deptmembers.setCompositionCustomer(compostionId);
        deptmembers.setNameId(user);
        deptmembers.setCode(user.getCode());
        deptmembers.setRank(getRankName(userId));   //职级后期需要改变类型
        Optional<User> userListf = User.stream().where(c -> c.getId() == userId).findFirst();
        if (userListf.isPresent()) {
            Long staffId = userListf.get().getStaffId();
            Optional<Staff> staffListf = Staff.stream().where(c -> c.getId() == staffId).findFirst();
            if (staffListf.isPresent()) {
                Staff staff = staffListf.get();
                String practisingQualification = staff.getPractisingQualification();
                deptmembers.setQualifications(practisingQualification);
                Project project1=new Project();
                deptmembers.setWorkingYears(project1.gainWorkingYears(userListf.get()));
            }
        }
        deptmembers.setIsExperience(10);
        deptmembers.setRoleName(RoleLevelEnum.DeptManger.getIndex());//部门经理
        deptmembers.setSort(0);
        deptmembers.setIsEnable(IsEnableEnum.Enable.getIndex());
        deptmembers.setSignatureOfIndependence(signatureOfindependence);
        repository().add(deptmembers);
    }

    /**
     * 删除 被审计单位项目组成员
     *
     * @param memeberId
     * @param compostionId
     * @throws Exception
     */
    @SystemService(args = "memeberId,compostionId")
    public void delCompositonMember(Long memeberId, Long compostionId) throws Exception {
        CompositionMembers compostionModel = this.stream().where(c -> c.getId() == memeberId && c.getCompositionCustomerId().getId() == compostionId).toList().get(0);
        compostionModel.setIsEnable(IsEnableEnum.DisEnable.getIndex());
        repository().add(compostionModel);
        if (compostionModel.getCompositionCustomerId().getProjectId().getDocState() == 11 || compostionModel.getCompositionCustomerId().getProjectId().getDocState() == 30) {
            CompositionCustomer customer = CompositionCustomer.createNew();
            customer.comterModifyType(Framework.createGuid().toString(), 30, compostionId);
        }
    }

    /**
     * 获取职级名称,
     *
     * @param userId 用户id
     * @return
     * @throws Exception
     */
    public String getRankName(Long userId) throws Exception {
        String name = " ";
        Optional<User> user = User.stream().where(c -> c.getId().equals(userId)).findFirst();
        if (user.isPresent()) {
            Long staffId = user.get().getStaffId();
            if (Framework.isNullOrEmpty(staffId)) {
                return name;
            }
            Optional<Staff> staffFirst = Staff.stream().where(c -> c.getId().equals(staffId)).findFirst();
            if (staffFirst.isPresent()) {
                StaffPosition staffPosition = staffFirst.get().getStaffPositionId();
                if (Framework.isNotNullOrEmpty(staffPosition)) {
                    name = staffPosition.getName();
                }
            }
        }
        return name;
    }
}
