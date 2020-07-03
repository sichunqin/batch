package com.dxn.model.extend;

import com.dxn.dto.IndependentDataListDTO;
import com.dxn.dto.ProjectMembersSaveDTO;
import com.dxn.dto.enums.IsEnableEnum;
import com.dxn.dto.enums.RoleLevelEnum;
import com.dxn.forms.dto.UIPagedList;
import com.dxn.model.entity.ProjectMembersEntity;
import com.dxn.system.ExceptionMultilevelObject;
import com.dxn.system.Framework;
import com.dxn.system.ResponseEntity;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.context.AppContext;
import com.dxn.system.exception.BusinessException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Prj_ProjectMembers")
public class ProjectMembers extends ProjectMembersEntity {

    @Override
    public void onInserting() throws Exception {
        super.onInserting();

        this.setIsIndependence(20);//独立性是否影响 默认 否 GBY
    }


    //插入后
    @Override
    public void onInserted() throws Exception {
        super.onInserted();
        String a = deadline();
        Long userId = this.getNameId().getId();
        Long projectId = this.getProject().getId();
        Long count1 = ProjectIndependenceMember.stream().where(c -> c.getNameId().getId() == userId && c.getProjectId().getId().equals(projectId)).count();
        Long count = InvestmentDeclaration.stream().where(c -> c.getUserId().getId() == userId && c.getYear().equals(a)).count();
        if (count > 0 && count1 == 0) {
            this.userAutomaticGeneration(this);
        }
        SignatureMaintenanceOfProjectMembers(this);
    }


    @SystemService(args = "dto,nId")
    public void UserReprint(List<Long> dto, Long nId) throws Exception {
        List<ProjectMembers> projectMemberList = this.stream().where(c -> c.getProjectId().getId() == nId).toList();
        for (int i = 0; i < dto.size(); i++) {
            Long nameId = dto.get(i);
            Optional<ProjectMembers> projectmemberFirst = projectMemberList.stream().filter(c -> c.getNameId().getId().equals(nameId)).findFirst();
            if (projectmemberFirst.isPresent()) {
                ProjectMembers projectmember = projectmemberFirst.get();
                projectmember.setIsEnable(IsEnableEnum.Enable.getIndex());
                repository().add(projectmemberFirst.get());
                String a = deadline();
                //projectmember.setWorkingYears(gainWorkingYears(projectmember.getNameId()));
                Long userId = nameId;
                Long projectId = projectmember.getProjectId().getId();
                Long count = InvestmentDeclaration.stream().where(c -> c.getUserId().getId() == userId && c.getYear().equals(a)).count();
                Long count1 = ProjectIndependenceMember.stream().where(c -> c.getNameId().getId() == userId && c.getProjectId().getId().equals(projectId)).count();
                if (count > 0 && count1 == 0) {
                    this.userAutomaticGeneration(projectmember);
                }
            } else {
                ProjectMembers model = ProjectMembers.createNew();
                Optional<User> listFirst = User.stream().where(c -> c.getId() == nameId).findFirst();
                if (listFirst.isPresent()) {
                    User list = listFirst.get();
                    model.setNameId(list);
                    model.setRank(getRankName(list.getId()));
                    model.setStaffPositionId(getRankNameId(list.getId()));
                    model.setSort(1);
                    model.setProject(nId);
                    model.setIsExperience(10);
                    model.setIsEnable(IsEnableEnum.Enable.getIndex());
                    model.setSignatureOfIndependence(20);
                    Long staffId = list.getStaffId();
                    Optional<Staff> staffList = Staff.stream().where(c -> c.getId() == staffId).findFirst();
                    if (staffList.isPresent()) {
                        Staff staff = staffList.get();
                        String pQualifications = staff.getPractisingQualification();
                        model.setPQualifications(pQualifications);
                        model.setWorkingYears(gainWorkingYears(list));
                    }
                    model.setProjectRole(RoleLevelEnum.CompilingPerson.getIndex());
                    repository().add(model);
                }
            }
        }
    }
    public int gainWorkingYears(User user) throws Exception {
        int workingYears=0;
        Long staffId=user.getStaffId();
        Optional<Staff>  staffFirst=Staff.stream().where(s->s.getId()==staffId).findFirst();
        if (staffFirst.isPresent()){
            Staff staff =staffFirst.get();
            if (Framework.isNotNullOrEmpty(staff.getPractitionersDate())){
                LocalDateTime sd3 = LocalDateTime.now();
                int currentTime=sd3.getYear();
                int practitionersDate=staff.getPractitionersDate().getYear();
                workingYears=currentTime-practitionersDate+1;
            }
        }
        return workingYears;
    }

    //保存按钮
    @SystemService(args = "projectList,independentDataList")
    public void SavaUserMembers(List<ProjectMembersSaveDTO> projectList, List<IndependentDataListDTO> independentDataList) throws Exception {
        for (ProjectMembersSaveDTO projectMembersSaveDTO : projectList) {
            Integer isExperience = projectMembersSaveDTO.getIsExperience();
            //if (Framework.isNullOrEmpty(isExperience))throw new BusinessException("是否有工作经验有未填数据");
            Integer projectRole = projectMembersSaveDTO.getProjectRole();
            if (Framework.isNullOrEmpty(projectRole)) throw new BusinessException("项目角色有未填数据");
            Long id = projectMembersSaveDTO.getId();
            Optional<ProjectMembers> ProjectMembersList = ProjectMembers.stream().where(w -> w.getId() == id).findFirst();
            if (ProjectMembersList.isPresent()) {
                ProjectMembers projectMembers = ProjectMembersList.get();
                //projectMembers.setId(id);
                if (!isExperience.equals(projectMembers.getIsExperience()) || !projectRole.equals(projectMembers.getProjectRole())) {
                    if (Framework.isNotNullOrEmpty(isExperience)) {
                        projectMembers.setIsExperience(isExperience);
                    }
                    if (Framework.isNotNullOrEmpty(projectRole)) {
                        projectMembers.setProjectRole(projectRole);
                    }
                    repository().add(projectMembers);
                    synchronousModification(projectMembersSaveDTO.getProjectId(), isExperience, projectRole, projectMembersSaveDTO.getUserId());
                }
            }
        }
        for (IndependentDataListDTO item : independentDataList) {
            Long id = item.getId();
            Integer processingOpinions = item.getProcessingOpinions();
            Long projectId = item.getProjectId();
            Optional<ProjectIndependenceMember> projectIndependenceMemberFirst = ProjectIndependenceMember.stream().where(w -> w.getId() == id && w.getProjectId().getId().equals(projectId)).findFirst();
            if (projectIndependenceMemberFirst.isPresent()) {
                ProjectIndependenceMember projectIndependenceMember = projectIndependenceMemberFirst.get();
                projectIndependenceMember.setProcessingOpinions(processingOpinions);
                repository().add(projectIndependenceMember);
            }
        }
    }

    private void synchronousModification(Long projectId, Integer isExperience, Integer projectRole, Long userId) throws Exception {
        List<CompositionMembers> members = CompositionMembers.stream().where(c -> c.getCompositionCustomerId().getProjectId().getId().equals(projectId) && c.getNameId().getId().equals(userId)).toList();
        for (CompositionMembers item : members) {
            item.setIsExperience(isExperience);
            //item.setRoleName(projectRole);
            repository().add(item);
        }
    }


    @SystemService(args = "projectId,projectPartnerId,projectManagerId")
    public void insertProjectMemeber(Project projectId, Long projectPartnerId, Long projectManagerId) throws Exception {
        List<Long> list = new ArrayList<>();
        if (Framework.isNotNull(projectPartnerId)){
            list.add(projectPartnerId);
        }
        if (Framework.isNotNull(projectManagerId)){
            list.add(projectManagerId);
        }
        for (Long item : list) {
            ProjectMembers Partnermodel = this.createNew();
            List<User> listPartner = User.stream().where(c -> c.getId() == item).toList();
            Partnermodel.setNameId(listPartner.get(0));
            Partnermodel.setRank(getRankName(listPartner.get(0).getId()));
            Partnermodel.setStaffPositionId(getRankNameId(listPartner.get(0).getId()));
            Partnermodel.setProjectId(projectId);
            Partnermodel.setSignatureOfIndependence(20);
            Long staffId = listPartner.get(0).getStaffId();
            Optional<Staff> staffListf = Staff.stream().where(c -> c.getId() == staffId).findFirst();
            if (staffListf.isPresent()) {
                Staff staff = staffListf.get();
                String practisingQualification = staff.getPractisingQualification();
                Partnermodel.setPQualifications(practisingQualification);
                Partnermodel.setWorkingYears(gainWorkingYears(listPartner.get(0)));
            }
            Partnermodel.setSort(0);
            Partnermodel.setIsExperience(10);
            Partnermodel.setIsEnable(IsEnableEnum.Enable.getIndex());
            if (projectPartnerId == item) {
                Partnermodel.setProjectRole(RoleLevelEnum.Partner.getIndex());
                Partnermodel.setRoleSort(RoleLevelEnum.sort3.getIndex());
            }
            if (projectManagerId == item) {
                Partnermodel.setProjectRole(RoleLevelEnum.BigManager.getIndex());
                Partnermodel.setRoleSort(RoleLevelEnum.sort1.getIndex());
                if (Framework.isNotNullOrEmpty(listPartner.get(0).getDeptId()) && Framework.isNotNullOrEmpty(listPartner.get(0).getDept().getMaster())) {
                    ProjectMembers projectMembers = this.createNew();
                    projectMembers.setProjectId(projectId);
                    projectMembers.setNameId(listPartner.get(0).getDept().getMaster());
                    //projectMembers.setc(listPartner.get(0).getDept().getMaster().getCode());
                    projectMembers.setRank(getRankName(listPartner.get(0).getDept().getMaster().getId()));   //职级后期需要改变类型
                    if (staffListf.isPresent()) {
                        Staff staff = staffListf.get();
                        String practisingQualification = staff.getPractisingQualification();
                        projectMembers.setPQualifications(practisingQualification);
                        projectMembers.setWorkingYears(gainWorkingYears(listPartner.get(0).getDept().getMaster()));
                    }
                    projectMembers.setIsExperience(10);
                    projectMembers.setSort(0);
                    projectMembers.setIsEnable(IsEnableEnum.Enable.getIndex()); //1是启用 0是禁用
                    projectMembers.setProjectRole(RoleLevelEnum.DeptManger.getIndex());//部门经理
                    projectMembers.setSignatureOfIndependence(20);
                    projectMembers.setRoleSort(RoleLevelEnum.sort2.getIndex());
                    repository().add(projectMembers);
                }
            }
            repository().add(Partnermodel);
        }

    }


    //给CS接口用
    @SystemService(args = "projectId,formId")
    public UIPagedList getIdFromUuid(String projectId, Long formId) throws Exception {
        List<ProjectMembers> listp = this.stream().where(w -> w.getProjectId().getUuid().equals(projectId)).toList();
        List<Object> list = new ArrayList<>();
        listp.forEach(f -> {
            f.initialization();
            list.add(f);
        });
        return UIPagedList.band(list, FormPage.getFormColumn(formId));
    }

    /**
     * 删除项目组成员
     *
     * @param memeberId
     * @param projectId
     * @throws Exception
     */
    @SystemService(args = "memeberId,projectId,nameId")
    public void delProjectMember(Long memeberId, Long projectId, Long nameId) throws Exception {

        ProjectMembers projectMembers = this.stream().where(c -> c.getId() == memeberId && c.getProjectId().getId() == projectId).toList().get(0);
        projectMembers.setIsEnable(IsEnableEnum.DisEnable.getIndex());
        repository().add(projectMembers);
        Optional<ProjectIndependenceMember> projectIndependenceMemberFirst = ProjectIndependenceMember.stream().where(c -> c.getNameId().getId() == nameId && c.getProjectId().getId().equals(projectId)).findFirst();
        if (projectIndependenceMemberFirst.isPresent()) {
            ProjectIndependenceMember projectIndependenceMember = projectIndependenceMemberFirst.get();
            repository().remove(projectIndependenceMember);
        }
    }

    @SystemService(args = "memeberId,projectId")
    public String deletePrompt(Long memeberId, Long projectId) throws Exception {
        String Prompt = "1";
        List<CompositionMembers> compositionMembersList = CompositionMembers.stream().where(c -> c.getCompositionCustomerId().getProjectId().getId().equals(projectId)).toList();
        ProjectMembers projectMembers = this.stream().where(c -> c.getId() == memeberId && c.getProjectId().getId() == projectId).toList().get(0);
        for (CompositionMembers item : compositionMembersList) {
            if (item.getNameId().getId().equals(projectMembers.getNameId().getId())) {
                Prompt = "当前人员已被项目架构使用，是否删除？";
                return Prompt;
            }
        }
        return Prompt;
    }

    @SystemService(args = "memeberId,projectId,nameId")
    public void delProjectMemberAdditional(Long memeberId, Long projectId, Long nameId) throws Exception {

        ProjectMembers projectMembers = this.stream().where(c -> c.getId() == memeberId && c.getProjectId().getId() == projectId).toList().get(0);
        projectMembers.setIsEnable(IsEnableEnum.DisEnable.getIndex());
        repository().add(projectMembers);
        List<CompositionMembers> compositionMembersList = CompositionMembers.stream().where(c -> c.getCompositionCustomerId().getProjectId().getId().equals(projectId)).toList();
        for (CompositionMembers item : compositionMembersList) {
            Optional<CompositionCustomer> compositionCustomerList = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId) && c.getProjectManagerId().getId() == nameId).findFirst();
            if (compositionCustomerList.isPresent()) {
                CompositionCustomer compositionCustomeritem = compositionCustomerList.get();
                compositionCustomeritem.setProjectManagerId(null);
                repository().add(compositionCustomeritem);
            }
            if (item.getNameId().getId().equals(projectMembers.getNameId().getId())) {
                item.setIsEnable(IsEnableEnum.DisEnable.getIndex());
                repository().add(item);
                Optional<ProjectIndependenceMember> projectIndependenceMemberFirst = ProjectIndependenceMember.stream().where(c -> c.getNameId().getId() == nameId && c.getProjectId().getId().equals(memeberId)).findFirst();
                if (projectIndependenceMemberFirst.isPresent()) {
                    ProjectIndependenceMember projectIndependenceMember = projectIndependenceMemberFirst.get();
                    repository().remove(projectIndependenceMember);
                }
            }
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

    public StaffPosition getRankNameId(Long userId) throws Exception {
        Optional<User> user = User.stream().where(c -> c.getId().equals(userId)).findFirst();
        if (user.isPresent()) {
            Long staffId = user.get().getStaffId();
            if (Framework.isNullOrEmpty(staffId)) {
                return null;
            }
            Optional<Staff> staffFirst = Staff.stream().where(c -> c.getId().equals(staffId)).findFirst();
            if (staffFirst.isPresent()) {
                StaffPosition staffPosition = staffFirst.get().getStaffPositionId();
                return staffPosition;
            }
        }
        return null;
    }

    //    @SystemService(args = "dto")
//    public void gainIndependence(List<Long> dto) throws Exception {
//        for (int i = 0; i < dto.size(); i++) {
//            Long nameId = dto.get(i);
//            Optional<ProjectMembers> projectmemberFirst = ProjectMembers.stream().filter(c -> c.getNameId().getId().equals(nameId)).findFirst();
//            if (projectmemberFirst.isPresent()){
//                ProjectMembers projectmember=projectmemberFirst.get();
//                Long staffId = projectmember.getNameId().getStaffId();//.getIsDeclare();
//                Optional<Staff>  staffFirst=Staff.stream().filter(c -> c.getId().equals(staffId)).findFirst();
//                if (staffFirst.isPresent()){
//                    Staff staff=staffFirst.get();
//                    Integer declare=staff.getIsDeclare();
//                    if (declare == 10){
//                        Long count=ProjectIndependenceMember.stream().filter(c -> c.getNameId().getId().equals(nameId)).count();
//                        if (count<1){
//                            gainInvestmentSituation(nameId);
//                        }
//                    }
//                }
//            }
//        }
//    }
    private void userAutomaticGeneration(ProjectMembers projectMembers) throws Exception {
        ProjectIndependenceMember projectIndependenceMember = ProjectIndependenceMember.createNew();
        Long staffId = projectMembers.getNameId().getId();
        projectIndependenceMember.setNameId(projectMembers.getNameId());
        projectIndependenceMember.setIsEnable(projectMembers.getIsEnable());
        projectIndependenceMember.setRank(projectMembers.getRank());
        String a = deadline();
        Optional<InvestmentDeclaration> investmentDeclarationFirst = InvestmentDeclaration.stream().where(c -> c.getUserId().getId().equals(staffId) && c.getYear().equals(a)).findFirst();
        if (investmentDeclarationFirst.isPresent()) {
            InvestmentDeclaration investmentDeclaration = investmentDeclarationFirst.get();
            Long investmentDeclarationId = investmentDeclaration.getId();
            List<InvestmentSituation> investmentSituationList = InvestmentSituation.stream().where(c -> c.getInvestmentDeclarationId().getId() == investmentDeclarationId).toList();
            for (InvestmentSituation investmentSituation : investmentSituationList) {
                projectIndependenceMember.setSecuritiesName(investmentSituation.getSecuritiesName());
                projectIndependenceMember.setSecuritiesCode(investmentSituation.getSecuritiesCode());
                projectIndependenceMember.setQuantity(investmentSituation.getNumber());
                projectIndependenceMember.setIsSecurities(10);
            }
        }
        projectIndependenceMember.setProjectId(projectMembers.getProjectId());
        repository().add(projectIndependenceMember);
        repository().commit();
    }

    //截止日期
    private String deadline() throws Exception {
        String a = null;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        String year = dateFormat.format(date);
        a = "截止到" + year.substring(0, 4) + "年12月31日";
        return a;
    }

    //独立性声明接口
    @SystemService(args = "content,projectId")
    public void projectIndependenceInterface(String content, Long projectId) throws Exception {
        Long userId = AppContext.current().getProfile().getId();
        String userName = AppContext.current().getProfile().getName();
        String signatureOfIndependenceString = content + "<p>&nbsp;&nbsp;签字：" + userName + " &nbsp;<br/></p>";
        List<ProjectMembers> projectMembersList = this.stream().where(c -> c.getProjectId().getId().equals(projectId) && c.getNameId().getId() == userId).toList();
        for (ProjectMembers item : projectMembersList) {
            item.setSignatureOfIndependence(10);
            item.setSignatureOfIndependenceString(signatureOfIndependenceString);
            LocalDateTime currentTime = LocalDateTime.now();
            item.setSigningDate(currentTime);
            SignatureMaintenanceOfProjectMembers(item);
            repository().add(item);
        }
        List<CompositionMembers> compositionMembersList = CompositionMembers.stream().where(c -> c.getCompositionCustomerId().getProjectId().getId().equals(projectId) && c.getNameId().getId() == userId).toList();
        for (CompositionMembers item : compositionMembersList) {
            item.setSignatureOfIndependence(10);
            repository().add(item);
        }

    }

    //独立性声明接口
    @SystemService
    public boolean isShowProjectIndependenct() throws Exception {
        Long userId = AppContext.current().getProfile().getId();
        List<ProjectMembers> projectMembersList = this.stream().where(c -> c.getNameId().getId() == userId && c.getSignatureOfIndependence() != 10&&c.getIsEnable()==10&&c.getProjectId().getDocState()!=10).toList();
        return projectMembersList.size() > 0 ? true : false;
    }

    private String readContent() throws Exception {

        Optional<FormPageChildrenProperty> helpPromptFirst = FormPageChildrenProperty.stream().where(c -> c.getFormPageChildrenId().getId() == 358135 && c.getName().equals("HelpPrompt")).findFirst();
        if (helpPromptFirst.isPresent()) {
            String str = helpPromptFirst.get().getValue();
            return str;//str.replace("&nbsp;", " ");
        }
        return "";
    }

    private String garinProjectUuid(String projectId) throws Exception {

        Optional<CompositionCustomer> compositionCustomerFirst = CompositionCustomer.stream().where(c -> c.getUuid().equals(projectId)).findFirst();
        if (compositionCustomerFirst.isPresent()) {
            String projectUuid = compositionCustomerFirst.get().getProjectId().getUuid();
            return projectUuid;
        }
        return "";
    }

    @SystemService(args = "userCode,compositionCustomerUUid")
    public ResponseEntity contentBeforeSigningCS(String userCode, String compositionCustomerUUid) throws Exception {
        ResponseEntity response = new ResponseEntity();
        ExceptionMultilevelObject exceptionMultilevelObject = new ExceptionMultilevelObject();
        String projectUuid = garinProjectUuid(compositionCustomerUUid);
        List<ProjectMembers> projectMembersList = this.stream().where(c -> c.getNameId().getCode().equals(userCode) && c.getProjectId().getUuid().equals(projectUuid)).toList();
        if (projectMembersList.size() > 0) {
            String str = "";
            boolean result = true;
            for (ProjectMembers item : projectMembersList) {
                if (item.getSignatureOfIndependence() == 20) {
                    str = this.readContent();
                    result = false;
                }
            }
            exceptionMultilevelObject.setSignature(result);
            exceptionMultilevelObject.setSignatureContent(str);
            response.setData(exceptionMultilevelObject);
            response.setMessages("");
            response.setCode(200);
        } else {
            exceptionMultilevelObject.setSignature(false);
            exceptionMultilevelObject.setSignatureContent("");
            exceptionMultilevelObject.setWrongInformation("当前用户与对应项目不匹配");
            response.setData(exceptionMultilevelObject);
            response.setMessages("");
            response.setCode(200);
        }
        return response;
    }

    @SystemService(args = "userCode,compositionCustomerUUid")
    public ResponseEntity projectIndependentSignatureCS(String userCode, String compositionCustomerUUid) throws Exception {
        ResponseEntity response = new ResponseEntity();
        Optional<User> userFirst = User.stream().where(c -> c.getCode().equals(userCode)).findFirst();
        if (userFirst.isPresent()) {
            String userName = userFirst.get().getName();
            String projectUuid = garinProjectUuid(compositionCustomerUUid);
            List<ProjectMembers> projectMembersList = this.stream().where(c -> c.getNameId().getCode().equals(userCode) && c.getProjectId().getUuid().equals(projectUuid)).toList();
            if (projectMembersList.size() > 0) {
                boolean isProjectMembers = false;
                boolean isCompositionMembers = false;
                try {
                    Optional<FormPageChildrenProperty> helpPromptFirst = FormPageChildrenProperty.stream().where(c -> c.getFormPageChildrenId().getId() == 358135 && c.getName().equals("HelpPrompt")).findFirst();
                    if (helpPromptFirst.isPresent()) {
                        String signatureOfIndependenceString = helpPromptFirst.get().getValue() + "<p>&nbsp;&nbsp;签字：" + userName + " &nbsp;<br/></p>";
                        for (ProjectMembers item : projectMembersList) {
                            item.setSignatureOfIndependence(10);
                            item.setSignatureOfIndependenceString(signatureOfIndependenceString);
                            LocalDateTime currentTime = LocalDateTime.now();
                            item.setSigningDate(currentTime);
                            repository().add(item);
                            isProjectMembers = true;
                        }
                    }

                    List<CompositionMembers> compositionMembersList = CompositionMembers.stream().where(c -> c.getCompositionCustomerId().getUuid().equals(compositionCustomerUUid) && c.getNameId().getCode().equals(userCode)).toList();
                    for (CompositionMembers item : compositionMembersList) {
                        item.setSignatureOfIndependence(10);
                        repository().add(item);
                        isCompositionMembers = true;
                    }
                    if (isProjectMembers && isCompositionMembers) {
                        response.setMessages("签章成功");
                        response.setData(true);
                    } else {
                        response.setMessages("签字失败,为找到签章用户或项目");
                        response.setData(false);
                    }
                    response.setCode(200);
                } catch (Exception ex) {
                    response.setCode(200);
                    response.setData(false);
                    response.setMessages(ex.getMessage());
                }
            } else {
                response.setMessages("没有查到对应信息");
                response.setData(false);
                response.setCode(200);
            }
        } else {
            response.setMessages("没有查到当前用户");
            response.setData(false);
            response.setCode(200);
        }
        return response;
    }


    public void SignatureMaintenanceOfProjectMembers(ProjectMembers projectMembers) throws Exception {
        Long projectId=projectMembers.getProjectId().getId();
        String projectMemberNotSigned="";
        String projectMemberSigned="";
        List<ProjectMembers>  projectMembersList=this.stream().where(p->p.getProjectId().getId()==projectId&&p.getIsEnable()==10).toList();
        for (ProjectMembers item:projectMembersList){
            if (item.getSignatureOfIndependence()==10){
                projectMemberSigned=projectMemberSigned+item.getNameId().getName()+",";
            }else {
                projectMemberNotSigned=projectMemberNotSigned+item.getNameId().getName()+",";
            }
        }
        Optional<Project>  projectFirst=Project.stream().where(p->p.getId()==projectId).findFirst();
        if (projectFirst.isPresent()){
            Project project=projectFirst.get();
            project.setProjectMemberNotSigned(projectMemberNotSigned);
            project.setProjectMemberSigned(projectMemberSigned);
            repository().add(project);
        }
    }
}