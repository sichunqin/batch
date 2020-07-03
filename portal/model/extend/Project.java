package com.dxn.model.extend;

import com.dxn.ChangeAuditStateDTO;
import com.dxn.custom.util.csfile.CsUploadFileUtil;
import com.dxn.dto.*;
import com.dxn.dto.ManuScirptItemArchive.*;
import com.dxn.dto.enums.*;
import com.dxn.forms.dto.UIPagedList;
import com.dxn.model.entity.ProjectEntity;
import com.dxn.service.WorkflowService;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.ResponseEntity;
import com.dxn.system.WorkFlowHelper;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.configuration.DxnConfig;
import com.dxn.system.context.AppContext;
import com.dxn.system.context.Profile;
import com.dxn.system.dto.EnumDataDTO;
import com.dxn.system.dto.QueryModel;
import com.dxn.system.dto.workFlow.MyTodoListDTO;
import com.dxn.system.dto.workFlow.UpdateTodoLog;
import com.dxn.system.dto.workFlow.WorkFlowNode;
import com.dxn.system.exception.BusinessException;
import com.dxn.system.io.FileHelper;
import com.dxn.util.ExcelUtils;
import com.dxn.util.WordHelperUtils;
import com.dxn.util.WordUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Table(name = "Prj_Project")
public class Project extends ProjectEntity {

    private static final int STATUS_CREATE = 10;
    private static final int STATUS_UPDATE = 20;

    @Override
    public void onValidate() throws Exception {
        super.onValidate();

        if (this.getAuditStart().isAfter(this.getAuditEnd()))
            throw new BusinessException("审计开始日不能大于审计结束日.");
        if (this.getProjectPartnerId().getId().equals(this.getProjectManagerId().getId()))
            throw new BusinessException("项目合伙人不能跟项目经理是同一个人");
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


    private boolean isProjectName(String showName) throws Exception {

        if (Framework.isNotNullOrEmpty(this.getShowName()) && this.getShowName().equals(showName)) {
            return false;
        } else {
            Long result = this.stream().where(c -> c.getShowName().equals(showName)).count();
            if (result <= 0)
                return false;
            return true;
        }

    }


    @Override
    public void onInserting() throws Exception {
        super.onInserting();
        this.setManuscriptReviewState("10");
        this.setObsolete(IsEnableEnum.Enable.getIndex());
        this.setTermination(IsEnableEnum.Enable.getIndex());
        this.setDocState(ProjectEnum.NotSubmitted.getIndex());
        this.setDept(AppContext.current().getProfile().getDeptId());
        this.setRandomCode(Framework.createGuid().toString());

        if (this.getFinancialReportProject() == ProjectCommon.NotFinancialReport.getIndex()) {
            this.setProjectStatus(ProjectCommon.formalProject.getIndex());
        }
        String projectName = this.getCustomerNameId().getName() + "-" + this.getBusinessTypeValue() + "-" + Framework.localDateToString(this.getAuditStart()) + "至" + Framework.localDateToString(this.getAuditEnd());
        if (isProjectName(projectName))
            throw new BusinessException("项目名称重复");
        this.setShowName(projectName);
        this.setName(this.getCustomerNameId().getName());

        CompositionCustomer model = CompositionCustomer.createNew();
        model.setIsMainProject(10);
        model.setCustomerNameId(this.getCustomerNameId());
        model.setName(this.getCustomerNameId().getName());
        model.setShowName(projectName);
        model.setIsMemberChanged(IsChangeEnum.No.getIndex());
        model.setProjectId(this);
        model.setAuditStart(this.getAuditStart());
        model.setAuditEnd(this.getAuditEnd());
        //model.setCode(this.getCode());
        model.setSort(0);
        model.setSortCode("0");
        model.setCustomerType(this.getCustomerType());
        model.setCustomerTypeValue(this.getCustomerTypeValue());
        model.setBusinessType(this.getBusinessType());
        model.setBusinessTypeValue(this.getBusinessTypeValue());
        model.setFirstOrContinuous(this.getFirstOrContinuous());
        model.setRiskLevel(this.getRiskLevel());
        model.setProjectManagerId(this.getProjectManagerId());
        model.setTotalAssets(this.getTotalAssets());
        model.setCompanyRelationship(0);
//        if (this.getFinancialReportProject() == ProjectCommon.NotFinancialReport.getIndex()) {
//            model.setManuscriptTemplate(0);
//        } else {
//            model.setManuscriptTemplate(10);
//        }

        model.setStateCode(StateCodeEnum.normal.getIndex());
        model.setRandomCode(Framework.createGuid().toString());
        model.setTerminationProject(0);
        model.setIsSubsidiaryReport(true);
        model.setOrgCode(this.getCustomerNameId().getUSCCode());
        model.setIsComplete(IsEnableEnum.Enable.getIndex());
        this.setProcessRiskLevel(this.getRiskLevel());
        repository().add(model);
        insertCompositionMember(model, this, 0);
        insertBusinessEvaluation();

        //项目组成员  -- 李一
        ProjectMembers member = ProjectMembers.createNew();
        member.insertProjectMemeber(this, this.getProjectPartner().getId(), this.getProjectManager().getId());

    }

    @Override
    public void onInserted() throws Exception {
        super.onInserted();
        //GBY 拷贝  被审计单位业务情况调查记录.xlsx 项目组资源和专业胜任能力的调查记录
        copyExcel();
    }

    /**
     * GBY
     * 底稿复核状态修改后
     *
     * @throws Exception
     */
    private void updatestate() throws Exception {
        Project p = this.getOriginalValue();
        Framework.log.info("updatestate" + this.getCode());
        if (Framework.isNullOrEmpty(p)) {
            Framework.log.info("p为空");
            return;
        }
        if (Framework.isNotNullOrEmpty(this.getManuscriptReviewState())) {
            Framework.log.info(this.getManuscriptReviewState() + ":4");
            Framework.log.info(p.getManuscriptReviewState() + ":5");
        } else {
            Framework.log.info("3");

        }
        if (Framework.isNotNullOrEmpty(this.getManuscriptReviewState()) && !this.getManuscriptReviewState().equals(p.getManuscriptReviewState())) {
            //Long projectId = this.getId();

            if (this.getManuscriptReviewState().equals("30")) {
                this.setDocState(31);
                //2020年4月4日 下午15:03分 在群里跟 需求 张波 高兴杰确认过  提交复核不需要给项目架构随机码赋值
//                List<CompositionCustomer> compositionCustomers = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId)).toList();
//                compositionCustomers.forEach(c -> {
//                    c.initialization();
//                    c.setIsChange(IsChangeEnum.Yes.getIndex());//变化
//                    c.setRandomCode(Framework.createGuid().toString());
//                });
            }
            if (this.getManuscriptReviewState().equals("70")) {
                this.setDocState(32);
//                List<CompositionCustomer> compositionCustomers = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId)).toList();
//                compositionCustomers.forEach(c -> {
//                    c.initialization();
//                    c.setIsChange(IsChangeEnum.Yes.getIndex());//变化
//                    c.setRandomCode(Framework.createGuid().toString());
//                });
            }
            if (this.getManuscriptReviewState().equals("100")) {
                this.setDocState(34);
            }
        }
    }

    @Override
    public void onUpdating() throws Exception {
        super.onUpdating();
        //维护流程风险级别字段
        this.setProcessRiskLevel(GeneralMethodOfRiskLevel(this));
        Long projectid = this.getId();
        Optional<CompositionCustomer> compostion = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectid) && c.getLevel() == 1).findFirst();
        if (this.getDocState() >= ProjectEnum.AlreadyStarted.getIndex() && this.getDocState() <= ProjectEnum.InTheIssuanceOfTheReport.getIndex()) {
            // 禅道需求号  19672
            if (Framework.isNotNullOrEmpty(this.getOriginalValue()) && this.getBusinessType() != this.getOriginalValue().getBusinessType()) {
                Long compostionId = compostion.get().getId();
                List<Report> result = Report.stream().where(c -> c.getCompositionCustomerId().getId().equals(compostionId)
                        && c.getBusinessSubclasses() != 1000 && c.getBusinessSubclasses
                        () != 1001).toList();
                if (result.stream().filter(c -> c.getApplySignStatus() < 30).count() > 0)
                    throw new BusinessException("用印属于有未完成的");

                List<RegistFormFile> RegistFormFilelist = RegistFormFile.stream().where(c -> c.getRegistFormId().getReportId().getProjectId().equals(projectid)).toList();
                for (RegistFormFile item : RegistFormFilelist) {
                    item.initialization();
                    this.repository().remove(item);
                }
                List<RegistForm> resgisFromList = RegistForm.stream().where(c -> c.getReportId().getProjectId().getId().equals(projectid)).toList();
                for (RegistForm item : resgisFromList) {
                    item.initialization();
                    this.repository().remove(item);
                }

                List<Report> reportList = Report.stream().where(c -> c.getProjectId().getId().equals(projectid)).toList();
                for (Report item : reportList) {
                    item.setDocState(10);
                    item.setApplySignStatus(10);
                    item.setIsSign(false);
                    repository().add(item);
                }
                if (result.stream().filter(c -> c.getDocState() == 30).count() > 0)
                    throw new BusinessException("主项目出具报告之后，不允许修改业务类型");
            }
        }


        //申请修改需要满足111  已开工后点击保存按钮触发的 并且 现有的风险等级和历史风险等级不一致的时候需要重新走流程
        if (this.getDocState() == ProjectEnum.AlreadyStarted.getIndex()) {

            if (Framework.isNotNullOrEmpty(this.getOriginalValue()) && this.getOriginalValue().getCustomerType() != this.getCustomerType()) {
                workFlowTrajectory(this.getId(), "客户类型修改");
            }
            if (Framework.isNotNullOrEmpty(this.getOriginalValue()) && this.getOriginalValue().getBusinessType() != this.getBusinessType()) {
                workFlowTrajectory(this.getId(), "业务类型修改");
            }
            if (Framework.isNotNullOrEmpty(this.getOriginalValue()) && this.getOriginalValue().getTotalAssets() != this.getTotalAssets()) {
                workFlowTrajectory(this.getId(), "资产总额修改");
            }
        }


        String projectName = this.getCustomerNameId().getName() + "-" + this.getBusinessTypeValue() + "-" + Framework.localDateToString(this.getAuditStart()) + "至" + Framework.localDateToString(this.getAuditEnd());
        if (isProjectName(projectName))
            throw new BusinessException("项目名称重复");
        this.setShowName(projectName);
        this.setName(this.getCustomerNameId().getName());
        Long Id = this.getId();
        CompositionCustomer model = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(Id) && c.getLevel() == 1).toList().get(0);
        model.setAuditStart(this.getAuditStart());
        model.setAuditEnd(this.getAuditEnd());
        model.setCode(this.getCode());
        model.setShowName(projectName);
        model.setName(this.getCustomerNameId().getName());
        model.setCustomerType(this.getCustomerType());
        model.setCustomerTypeValue(this.getCustomerTypeValue());
        model.setBusinessType(this.getBusinessType());
        model.setBusinessTypeValue(this.getBusinessTypeValue());
        model.setFirstOrContinuous(this.getFirstOrContinuous());
        model.setRiskLevel(this.getRiskLevel());
        model.setProjectManagerId(this.getProjectManagerId());
        model.setTotalAssets(this.getTotalAssets());
        repository().add(model);

        if (this.getDocState() == 11) {
            if (this.getOriginalValue().getRiskLevel() != this.getRiskLevel() || (this.getAuditStart() != this.getOriginalValue().getAuditStart() || this.getAuditEnd() != this.getOriginalValue().getAuditEnd())) {
                updateRandomCompostionCustomer(this.getId());
            }

        }
        if (Framework.isNotNullOrEmpty(this.getOriginalValue()) && this.getBusinessType() != this.getOriginalValue().getBusinessType()) {
            List<CompositionCustomer> customerArrays = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(Id) && c.getLevel() != 1).toList();
            if (customerArrays.size() > 0) {
                for (CompositionCustomer t : customerArrays) {
                    t.setCustomerType(0);
                    t.setCustomerTypeValue("");
                    t.setBusinessType(this.getBusinessType());
                    t.setBusinessTypeValue(this.getBusinessTypeValue());
                    t.setRiskLevel(0);
                    if (Framework.isNotNullOrEmpty(this.getCustomerType()) && this.getCustomerType() > 0) {
                        String projectNames = t.getName() + "-" + this.getBusinessTypeValue() + "-" + Framework.localDateToString(t.getAuditStart()) + "至" + Framework.localDateToString(t.getAuditEnd());
                        t.setShowName(projectNames);
                    }
                    repository().add(t);
                }
            }
            // 业务类型改变的时候项目需要编程新创建状态

            if (this.getDocState() > 30 && this.getDocState() != 100)
                this.setDocState(10);
        }

        //处理项目组成员
        Long projectPartnerId = this.getProjectPartner().getId();
        Long projectManagerId = this.getProjectManager().getId();
        Long oldProjectPartnerId = this.getOriginalValue().getProjectPartner().getId();
        Long oldProjectManagerId = this.getOriginalValue().getProjectManager().getId();
        //删除前是否修改判断集合
        List<Long> longList = new ArrayList<>();
        //删除用集合
        List<ProjectMembers> delMember = new ArrayList<>();
        if (projectPartnerId == oldProjectPartnerId) {
            projectPartnerId = null;
        } else {
            longList.add(oldProjectPartnerId);
        }
        if (projectManagerId == oldProjectManagerId) {
            projectManagerId = null;
        } else {
            longList.add(oldProjectManagerId);
        }
        Integer bigManager = RoleLevelEnum.BigManager.getIndex();
        Integer deptManger = RoleLevelEnum.DeptManger.getIndex();
        Integer partner = RoleLevelEnum.Partner.getIndex();
        for (Long item : longList) {
            List<ProjectMembers> projectMemberList = ProjectMembers.stream().where(c -> c.getNameId().getId() == item && c.getProjectId().getId().equals(projectid)).toList();
            projectMemberList.forEach(f -> {
                delMember.add(f);
            });
        }
        for (ProjectMembers item : delMember) {
            item.initialization();
            this.getProjectMemberss().remove(item);
            this.repository().remove(item);
        }
        ProjectMembers member = ProjectMembers.createNew();
        member.insertProjectMemeber(this, projectPartnerId, projectManagerId);

        List<CompositionMembers> compostionCustomerList = CompositionMembers.stream().where(c -> c.getCompositionCustomerId().getProjectId().getId() == Id).toList();
        for (CompositionMembers item : compostionCustomerList) {
            if (item.getRoleName() == bigManager) {
                saveMemeberFunction(item, item.getCompositionCustomerId(), bigManager, this.getProjectManagerId());
                if (Framework.isNotNullOrEmpty(this.getProjectManagerId()) && Framework.isNotNullOrEmpty(this.getProjectManagerId().getDept().getMaster())) {
                    Optional<CompositionMembers> deptMangerEntity = compostionCustomerList.stream().filter(c -> c.getRoleName() == deptManger).findFirst();
                    if (deptMangerEntity.isPresent()) {
                        saveMemeberFunction(deptMangerEntity.get(), item.getCompositionCustomerId(), deptManger, this.getProjectManagerId());
                    }

                }
            } else if (item.getRoleName() == partner) {
                saveMemeberFunction(item, item.getCompositionCustomerId(), partner, this.getProjectPartnerId());
            }
        }

        if (Framework.isNotNullOrEmpty(this.getOriginalValue()) && Framework.isNotNullOrEmpty(this.getOriginalValue().getDeptId()) && Framework.isNotNullOrEmpty(this.getDeptId()) && this.getOriginalValue().getDeptId().getId() != this.getDeptId().getId()) {
            Long originalDeptId = this.getOriginalValue().getDeptId().getId();
            Long currentDeptId = this.getDeptId().getId();
            List<Report> reportList = Report.stream().where(data -> data.getIssueDeptId().getId().equals(originalDeptId)).toList();
            for (int i = 0, size = reportList.size(); i < size; i++) {
                Report report = reportList.get(i);
                report.setIssueDept(currentDeptId);
                this.repository().add(report);
            }
        }
        if (this.getDocState() == 30) {
            if (Framework.isNotNullOrEmpty(this.getOriginalValue()) && this.getOriginalValue().getProjectPartnerId().getId() != this.getProjectPartnerId().getId())
                compositionMeneberSign(this.getId());
            if (Framework.isNotNullOrEmpty(this.getOriginalValue()) && this.getOriginalValue().getProjectManagerId().getId() != this.getProjectManagerId().getId()) {
                Optional<CompositionCustomer> customers = CompositionCustomer.stream().where(c -> c.getProjectId().getId() == projectid && c.getLevel() == 1).findFirst();
                if (customers.isPresent()) {
                    CompositionCustomer compositionCustomer = CompositionCustomer.createNew();
                    compositionCustomer.comterModifyType(Framework.createGuid().toString(), 30, customers.get().getId());
                }
            }
        }

    }

    private void compositionMeneberSign(Long projectId) throws Exception {
        List<CompositionCustomer> compostionCustomerList = CompositionCustomer.stream().where(c -> c.getProjectId().getId() == projectId).toList();
        for (CompositionCustomer item : compostionCustomerList) {
            CompositionCustomer compositionCustomer = CompositionCustomer.createNew();
            compositionCustomer.comterModifyType(Framework.createGuid().toString(), 30, item.getId());
        }
    }

    @Override
    public void onUpdated() throws Exception {
        super.onUpdated();
        updatestate();
        //当修改业务承接类型时，加载不同的EXCEL文件 GBY
        copyExcel();

    }


    private void updateRandomCompostionCustomer(long projectId) throws Exception {
        List<CompositionCustomer> customer = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId) && c.getLevel() == 1).toList();
        if (customer.size() > 0) {

            customer.get(0).setRandomCode(Framework.createGuid().toString());
            repository().add(customer.get(0));
        }

    }

    private void workFlowTrajectory(Long projectId, String typeName) throws Exception {
        UpdateTodoLog todoLog = new UpdateTodoLog();
        Profile profile = AppContext.current().getProfile();

        todoLog.setActorId(profile.getId().toString());
        todoLog.setActorName(profile.getName());
        todoLog.setEntityId(projectId.toString());
        todoLog.setEntityType("Project");
        //  客户类型修改  业务类型修改  资产规模修改
        todoLog.setXmid(typeName);
        todoLog.setTaskInstanceName("项目经理");
        todoLog.setProcessId("报告申请修改流程");
        todoLog.setComments(typeName);
        //todoLog.setJdtype(String.format("{entityId:%s,displayName:'第 %s 次修改'}", this.getId(), modifyNumber));
        WorkFlowHelper.saveTodoLog(todoLog);
    }

    @Override
    public String gainAttachmentPath() throws Exception {
        String path = super.gainAttachmentPath();

        return String.format("%s/%s", path, this.getCode());//
    }

    @Override
    public void onDeleting() throws Exception {
        this.deleteProjectInfo();
    }

    @SystemService(args = "projectId")
    public void deleteProjectall(Long projectId) throws Exception {
        Project p = repository().findById(projectId, Project.class);
        if (p.getDocState() != 10 && p.getDocState() != 100) {
            throw new BusinessException("只有未提交的项目才可以删除!");
        }
        //被审计单位
        long engagementAny = Engagement.stream().where(c -> c.getProjectId().getId().equals(projectId)).count();
        if (engagementAny > 0)
            throw new BusinessException("项目中存在业务约定书，不能删除");

        List<CompositionCustomer> compositionCustomersList = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId)).toList();

        for (CompositionCustomer item : compositionCustomersList) {
            String uuid = item.getUuid();
            Long compostionId = item.getId();
            if (ManuscriptItem.stream().where(c -> c.getAuditworkProjectID().equals(uuid)).count() > 0) {
                throw new BusinessException("有被审计单位中存在底稿，不能进行删除!");
            } else if (Report.stream().where(c -> c.getCompositionCustomerId().getId().equals(compostionId)).count() > 0) {
                throw new BusinessException("有被审计单位中存在报告，不能进行删除!");
            }
        }

        clearDataByProjectId(projectId);

    }

    private void deleteProjectInfo() throws Exception {
        Long projectId = this.getId();
        if (this.getDocState() != 10 && this.getDocState() != 100) {
            throw new BusinessException("只有未提交的项目才可以删除!");
        }
        //被审计单位
        long engagementAny = Engagement.stream().where(c -> c.getProjectId().getId().equals(projectId)).count();
        if (engagementAny > 0)
            throw new BusinessException("项目中存在业务约定书，不能删除");

        List<CompositionCustomer> compositionCustomersList = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId)).toList();

        for (CompositionCustomer item : compositionCustomersList) {
            String uuid = item.getUuid();
            Long compostionId = item.getId();
            if (ManuscriptItem.stream().where(c -> c.getAuditworkProjectID().equals(uuid)).count() > 0) {
                throw new BusinessException("有被审计单位中存在底稿，不能进行删除!");
            } else if (Report.stream().where(c -> c.getCompositionCustomerId().getId().equals(compostionId)).count() > 0) {
                throw new BusinessException("有被审计单位中存在报告，不能进行删除!");
            } else {
               /* repository().remove(item);

                List<CompositionMembers> memeberList = CompositionMembers.stream().where(c -> c.getCompositionCustomerId().getId().equals(compostionId)).toList();
                for (CompositionMembers member : memeberList) {
                    member.initialization();
                    this.repository().remove(member);
                }

                List<Engagement> listengament = Engagement.stream().where(c -> c.getCompositionCustomerId().getId().equals(compostionId)).toList();
                for (Engagement engagement : listengament) {
                    engagement.initialization();
                    this.repository().remove(engagement);
                }*/
            }
        }
        deleteProjectothers(projectId);

/*
        List<BusinessEvaluation> businessEvaluationlist = BusinessEvaluation.stream().where(c -> c.getProjectId().getId().equals(projectId)).toList();
        for (BusinessEvaluation item : businessEvaluationlist) {
            item.initialization();
            this.repository().remove(item);
        }

        List<PrjAttachment> prjAttachments = PrjAttachment.stream().where(c -> c.getProjectId().getId().equals(projectId)).toList();
        for (PrjAttachment item : prjAttachments) {
            item.initialization();
            this.repository().remove(item);
        }*/
    }

    public void saveMemeberFunction(CompositionMembers members, CompositionCustomer customer, Integer roleType, User u) throws Exception {
        CompositionMembers entity = members;
        entity.initialization();
        entity.setCompositionCustomerId(customer);
        if (roleType == RoleLevelEnum.DeptManger.getIndex()) {
            entity.setNameId(u.getDept().getMaster());
            entity.setCode(u.getDept().getMaster().getCode());
            entity.setWorkingYears(gainWorkingYears(u.getDept().getMaster()));
        } else {
            entity.setNameId(u);
            entity.setCode(u.getCode());
            entity.setWorkingYears(gainWorkingYears(u));
        }
        entity.setRank(getRankName(u.getId()));   //职级后期需要改变类型
        entity.setQualifications(getQualificationUser(u));
        Long staffId = u.getStaffId();
        Optional<Staff> staffListf = Staff.stream().where(c -> c.getId() == staffId).findFirst();

        entity.setIsExperience(10);
        entity.setIsEnable(IsEnableEnum.Enable.getIndex());
        entity.setRoleName(roleType);
    }

    public void insertCompositionMember(CompositionCustomer customer, Project project, int bigOrSmall) throws Exception {
        List<User> userList = new ArrayList<>();
        userList.add(project.getProjectManagerId());
        userList.add(project.getProjectPartnerId());
        for (User u : userList) {
            if (u == project.getProjectManagerId() && bigOrSmall == 1) continue;
            CompositionMembers entity = CompositionMembers.createNew();
            entity.setCompositionCustomerId(customer);
            entity.setNameId(u);
            entity.setCode(u.getCode());
            entity.setRank(getRankName(u.getId()));   //职级后期需要改变类型
            entity.setQualifications(getQualificationUser(u));
            Long staffId = u.getStaffId();
            Optional<Staff> staffListf = Staff.stream().where(c -> c.getId() == staffId).findFirst();
            if (staffListf.isPresent()) {
                Staff staff = staffListf.get();
                int workingYears = 0;
                if (Framework.isNotNullOrEmpty(staff.getPractitionersDate())) {
                    LocalDateTime sd3 = LocalDateTime.now();
                    int currentTime = sd3.getYear();
                    int practitionersDate = staff.getPractitionersDate().getYear();
                    workingYears = currentTime - practitionersDate + 1;
                }
                entity.setWorkingYears(gainWorkingYears(u));
            }
            entity.setIsExperience(10);
            entity.setIsEnable(IsEnableEnum.Enable.getIndex()); //10是启用 20是禁用
            entity.setSignatureOfIndependence(20);
            entity.setSort(0);
            if (u == project.getProjectManagerId()) {
                if (bigOrSmall == 0) {
                    entity.setRoleName(RoleLevelEnum.BigManager.getIndex());//角色后期需要改变类型   30是大项目经理
                    if (Framework.isNotNullOrEmpty(u.getDeptId()) && Framework.isNotNullOrEmpty(u.getDept().getMaster())) {
                        CompositionMembers deptmembers = CompositionMembers.createNew();
                        deptmembers.setCompositionCustomerId(customer);
                        deptmembers.setNameId(u.getDept().getMaster());
                        deptmembers.setCode(u.getDept().getMaster().getCode());
                        deptmembers.setRank(getRankName(u.getDept().getMaster().getId()));   //职级后期需要改变类型
                        deptmembers.setQualifications(getQualificationUser(u));
                        deptmembers.setWorkingYears(gainWorkingYears(u.getDept().getMaster()));
                        deptmembers.setIsExperience(10);
                        deptmembers.setSort(0);
                        deptmembers.setSignatureOfIndependence(20);
                        deptmembers.setIsEnable(IsEnableEnum.Enable.getIndex()); //1是启用 0是禁用
                        deptmembers.setRoleName(RoleLevelEnum.DeptManger.getIndex());//部门经理
                        deptmembers.setRoleSort(RoleLevelEnum.sort2.getIndex());
                        repository().add(deptmembers);
                    }
                } else {
                    entity.setRoleName(RoleLevelEnum.SmallManger.getIndex());//角色后期需要改变类型   20是小项目经理
                }
            }
            if (u == project.getProjectPartnerId()) {
                entity.setRoleName(RoleLevelEnum.Partner.getIndex());//角色后期需要改变类型  30是项目合伙人
                entity.setRoleSort(RoleLevelEnum.sort3.getIndex());
            }
            repository().add(entity);
        }
    }

    /**
     * 获取业务类型
     *
     * @param customerType
     * @param busnissType
     * @return
     * @throws Exception
     */
    @SystemService(args = "customerType,busnissType")
    public String getBusinessTypeName(Integer customerType, Integer busnissType) throws Exception {
//        List<SimpleTreeEntity> simple = repository().getRiskLevelUtil().getBusinessTypeListByClientTypeId(customerType);
//        Integer businetypeValue = busnissType;
//        Optional<SimpleTreeEntity> first = simple.stream().filter(c -> c.getId().equals(businetypeValue)).findFirst();
        return this.getBusinessTypeValue();
    }

    /**
     * GBY
     */
    public void copyExcel() throws Exception {
        Project oldProject = this.getOriginalValue(); //获取项目的老值
        if (Framework.isNullOrEmpty(oldProject))//新增
        {
            if (Framework.isNotNullOrEmpty(this.getFirstOrContinuous())) {
                ExcelFunction();
            }
        } else//修改
        {
            if (Framework.isNotNullOrEmpty(this.getFirstOrContinuous())) {
                if (oldProject.getFirstOrContinuous() != this.getFirstOrContinuous()) {
                    ExcelFunction();
                    deleteBusinessEvaluation();
                    insertBusinessEvaluation();

                }
            }

        }
    }

    private void ExcelFunction() throws Exception {
        if (this.getFirstOrContinuous() == UndertakeCountEnum.FirstUndertaking.getIndex()) {
            this.setCompetencyConclusionPath(copyTemplate("项目组资源和专业胜任能力的调查记录(首次承接).xlsx"));
            this.setBusinessConclusionPath(copyTemplate("被审计单位业务情况调查记录(首次承接).xlsx"));
            this.setProgramSheetPath(copyTemplate("1-A初步业务活动程序表—首次承接.xlsx"));
        } else {
            this.setCompetencyConclusionPath(copyTemplate("项目组资源和专业胜任能力的调查记录.xlsx"));
            this.setBusinessConclusionPath(copyTemplate("被审计单位业务情况调查记录.xlsx"));
            this.setProgramSheetPath(copyTemplate("1-A初步业务活动程序表—连续审计.xlsx"));
        }
    }


    /**
     * GBY
     * 拷贝 被审计单位业务情况调查记录 模板
     * 返回模板的相对路径
     */
    private String copyTemplate(String fileName, String filePath) throws Exception {


        String path = gainAttachmentPath();

        String templateFile = DxnConfig.getFileUploadPath();
        String newFile = "";

        if (Framework.isNotNullOrEmpty(path)) {
            if (Framework.isNotNullOrEmpty(filePath)) {
                newFile = FileHelper.combine(templateFile, path, filePath);
            } else {
                newFile = FileHelper.combine(templateFile, path);
            }

        }

        FileHelper.ensureDirtectoryExists(newFile);
        newFile = FileHelper.combine(newFile, fileName);
        FileHelper.copyFile(templateFile, fileName, newFile);
        if (Framework.isNotNullOrEmpty(filePath)) {
            return FileHelper.combine(path, filePath, fileName);
        } else {
            return FileHelper.combine(path, fileName);
        }

    }


    /**
     * GBY
     * 拷贝 被审计单位业务情况调查记录 模板
     * 返回模板的相对路径
     */
    private String copyTemplate(String fileName) throws Exception {

        return copyTemplate(fileName, "");


    }

    private void insertBusinessEvaluation() throws Exception {
        BusinessEvaluation model = BusinessEvaluation.createNew();
        model.setProjectId(this);
        model.setIndependCommitment("A1-4");
        model.setIsApplicable6(false);
        model.setIsApplicable7(false);
        model.setIsApplicable8(false);
        model.setIsApplicable9(false);
        model.setIsApplicable10(false);
        model.setIsApplicable11(false);
        model.setIsApplicable13(false);
        model.setIsApplicable14(false);
        model.setIsApplicable15(false);
        model.setIsApplicable20(false);
        repository().add(model);
        repository().commit();
    }

    //删除业务承接数据
    private void deleteBusinessEvaluation() throws Exception {
        Long projectId = this.getId();
        String deleteSql;
        deleteSql = "delete from PM_BusinessSub where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub10 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub11 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub12 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub13 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub14 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub20 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub5 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub6 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub7 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub8 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub9 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);


        deleteSql = "delete from PM_BusinessEvaluation where projectid = " + projectId;
        repository().executeCommand(deleteSql);
        //  deleteSql ="delete from Base_Attachment  where id in (select attachmentid from PM_PrjAttachment where projectid ="+projectId+")";
        //  repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_PrjAttachment where projectid =" + projectId;
        repository().executeCommand(deleteSql);
        deleteSql = "update prj_project set BusinessConclusion ='' ,CompetencyConclusion ='' where id =" + projectId;
        repository().executeCommand(deleteSql);
    }


    /**
     * 添加子被审计单位
     *
     * @param dto
     * @throws Exception
     */
    @SystemService(args = "dto")
    public Long SaveChildrenProject(CompositionCustomerDTO dto) throws Exception {
        CompositionCustomerDTO entity = dto;
        String name = entity.getName();
        Long id = entity.getId();
        Integer addAfterCompletion = entity.getAddAfterCompletion();//这个是判断需要改状态的

        Optional<CompositionCustomer> optional = CompositionCustomer.stream().where(c -> c.getId() == id).findFirst();
        if (optional.isPresent()) {
            CompositionCustomer first = optional.get();
            Long projectId = first.getProjectId().getId();
            if (Framework.isNotNullOrEmpty(addAfterCompletion)) {
                Optional<Project> projectFirst = this.stream().where(c -> c.getId().equals(projectId)).findFirst();
                if (projectFirst.isPresent()) {
                    Project project = projectFirst.get();
                    project.setDocState(11);
                    repository().add(project);
                }
            }
            if (dto.getCompostionType() != 0) {
                if (!first.getName().equals(name)) {
                    Long count = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId) && c.getName().equals(name)).count();
                    if (count > 0) {
                        throw new BusinessException("被审计单位名称重复");
                    }
                    MotifyChildrenProject(dto);
                }
                return 0L;
            }

            if (Framework.isNotNullOrEmpty(first.getParentId())) {
                if (first.getCompanyRelationship() == 20) {
                    throw new BusinessException("分公司下不能在加任何公司");
                }
            }
            Long count = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId) && c.getName().equals(name)).count();
            if (count > 0) {
                throw new BusinessException("被审计单位名称重复");
            }
            //List<CompositionCustomer> compostionList = CompositionCustomer.stream().where(c -> c.getId().equals(id)).toList();
            //List<CompositionCustomer> compostionList = CompositionCustomer.stream().toList();
            CompositionCustomer model = CompositionCustomer.createNew();
            model.setIsMainProject(20);
            model.setParent(entity.getId());
            model.setIsMemberChanged(IsChangeEnum.No.getIndex());
            model.setName(name);
            model.setCompanyRelationship(entity.getCompanyRelationship());
            model.setStateCode(StateCodeEnum.normal.getIndex());
            model.setRandomCode(Framework.createGuid().toString());
            model.setTerminationProject(0);
            model.setRandomCode(Framework.createGuid().toString());
            model.setIsComplete(IsEnableEnum.Enable.getIndex());

            model.setOrgCode(entity.getOrgCode());

            Project project = this.findById(projectId);
            model.setAuditStart(project.getAuditStart());
            model.setAuditEnd(project.getAuditEnd());
            model.setBusinessType(project.getBusinessType());
            model.setBusinessType(project.getBusinessType());
            model.setBusinessTypeValue(project.getBusinessTypeValue());
            model.setFirstOrContinuous(project.getFirstOrContinuous());
            model.setTotalAssets(0);
            model.setLevel(first.getLevel() + 1);
            // -- 生成编码 --
//            String projectCode = project.getCode();
//            String projectCodePrefix = projectCode.substring(0, projectCode.length() - 5);
//            String sequenceKey = projectCodePrefix + "\\d{5}";
//            CodeRuleSequenceNumberRepository codeRuleSequenceNumberRepository = (CodeRuleSequenceNumberRepository) AppHelper.getBean("codeRuleSequenceNumberRepository");
//            CodeRuleSequenceNumber sequenceNumber = codeRuleSequenceNumberRepository.findDistinctByEntityNameEqualsAndSequenceKeyEquals("Project", sequenceKey);
//            if (sequenceNumber == null) {
//                throw new Exception("项目编号流水未找到！");
//            }
//            Long newSequenceNumber = sequenceNumber.getSequenceNumber() + 1;
//            sequenceNumber.setSequenceNumber(newSequenceNumber);
//            codeRuleSequenceNumberRepository.save(sequenceNumber);
//
//            String padNewSequenceNumber = String.format("%05d", newSequenceNumber);
//            model.setCode(projectCodePrefix + padNewSequenceNumber);
            // -- 生成编码结束 --

            model.setProject(projectId);
            model.setManuscriptTemplate(10);
            model.setIsReport(true);
            if (Framework.isNullOrEmpty(first.getCustomerType()) || first.getCustomerType() == 0)
                throw new BusinessException("客户类型不能为空~.");
            String compostionName = entity.getName() + "-" + first.getBusinessTypeValue() + "-" + Framework.localDateToString(first.getAuditStart()) + "至" + Framework.localDateToString(first.getAuditEnd());
            model.setShowName(compostionName);
            //赋sort
            String aString = optional.get().getSortCode();
            Long parentId = first.getId();
            List<CompositionCustomer> compositionCustomerList = CompositionCustomer.stream().where(c -> c.getParentId().getId() == parentId).toList();
            compositionCustomerList.add(model);
            if (compositionCustomerList.size() > 0) {
                List<String> nameList = new ArrayList<>();
                for (CompositionCustomer item : compositionCustomerList) {
                    int companyRelationship = item.getCompanyRelationship() == 10 ? 20 : 10;

                    String c = String.valueOf(companyRelationship);

                    nameList.add(c + Framework.getPinYin(item.getName()) + ":" + item.getName());
                }
                Collections.sort(nameList);
                int sort = 1;
                for (int i = 0; nameList.size() > i; i++) {
                    String nameFor = nameList.get(i);
                    String[] starr = nameFor.split(":");
                    for (CompositionCustomer item : compositionCustomerList) {
                        if (starr[1].toLowerCase().equals(item.getName().toLowerCase())) {
                            item.setSort(sort);
                            String treeCode = aString + String.valueOf(1000 + sort);
                            item.setSortCode(treeCode);
                        }
                    }
                    sort++;
                }
            } else {
                String treeCode = aString + String.valueOf(1000);
                model.setSort(1);
                model.setSortCode(treeCode);
            }
            //赋sort结束
            repository().add(model);
            insertCompositionMember(model, project, 1);
            project.setIsChange(IsChangeEnum.Yes.getIndex());
            project.setRandomCode(Framework.createGuid().toString());
//            if (project.getDocState().equals(ProjectEnum.AlreadyStarted.getIndex())) {
//                project.setDocState(ProjectEnum.NotSubmitted.getIndex());
//            }
            //repository().commit();
            return model.getId();
        }
        return 0L;
    }

    public void MotifyChildrenProject(CompositionCustomerDTO dto) throws Exception {
        Long id = dto.getId();
        Optional<CompositionCustomer> compostionList = CompositionCustomer.stream().where(c -> c.getId().equals(id)).findFirst();
        CompositionCustomer model = compostionList.get();
        model.setName(dto.getName());
        model.setCompanyRelationship(dto.getCompanyRelationship());
        if (Framework.isNullOrEmpty(model.getCustomerType()) || model.getCustomerType() == 0)
            throw new BusinessException("客户类型不能为空~.");
        String compostionName = dto.getName() + "-" + model.getBusinessTypeValue() + "-" + Framework.localDateToString(model.getAuditStart()) + "至" + Framework.localDateToString(model.getAuditEnd());
        model.setShowName(compostionName);
        String aString = model.getParentId().getSortCode();
        Long parentId = model.getParentId().getId();
        List<CompositionCustomer> compositionCustomerList = CompositionCustomer.stream().where(c -> c.getParentId().getId() == parentId).toList();
        compositionCustomerList.add(model);
        if (compositionCustomerList.size() > 0) {
            List<String> nameList = new ArrayList<>();
            for (CompositionCustomer item : compositionCustomerList) {
                int companyRelationship = item.getCompanyRelationship();
                String c = String.valueOf(companyRelationship);
                nameList.add(c + Framework.getPinYin(item.getName()) + ":" + item.getName());
            }
            Collections.sort(nameList);
            int sort = 1;
            for (int i = 0; nameList.size() > i; i++) {
                String nameFor = nameList.get(i);
                String[] starr = nameFor.split(":");
                for (CompositionCustomer item : compositionCustomerList) {
                    if (starr[1].equals(item.getName())) {
                        item.setSort(sort);
                        String treeCode = aString + String.valueOf(1000 + sort);
                        item.setSortCode(treeCode);
                    }
                }
                sort++;
            }
        } else {
            String treeCode = aString + String.valueOf(1000);
            model.setSort(1);
            model.setSortCode(treeCode);
        }
        this.repository().add(model);
    }

    /**
     * 点击作废或者终止按钮
     *
     * @param projectId  项目id
     * @param buttonType 0代表作废 1代表终止
     * @param value      10 true 20 禁用
     * @throws Exception
     */
    @SystemService(args = "projectId,ClickValue,value")
    public Integer terminationOrObsoleteFunc(Long projectId, Integer buttonType, Integer value) throws Exception {
        Integer code = TerminationCode(projectId, buttonType);
        if (code != 0) {
            return code;
        }

        Integer result;
        Project project = this.stream().where(c -> c.getId() == projectId).toList().get(0);
        project.setIsChange(IsChangeEnum.Yes.getIndex());//变化
        //c project.setRandomCode(Framework.createGuid().toString());
        List<CompositionCustomer> compositionCustomers = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId)).toList();
        compositionCustomers.forEach(c -> {
            c.initialization();
            c.setIsChange(IsChangeEnum.Yes.getIndex());//变化
            // c.setRandomCode(Framework.createGuid().toString());
        });
        if (buttonType == 0) {
            if (project.getDocState() == ProjectEnum.Invalid.getIndex()) {
                project.setDocState(ProjectEnum.AlreadyStarted.getIndex());
                result = ProjectEnum.AlreadyStarted.getIndex();
            } else {
                project.setDocState(ProjectEnum.Invalid.getIndex());
                result = ProjectEnum.Invalid.getIndex();
            }
        } else {
            if (project.getDocState() == ProjectEnum.Terminated.getIndex()) {
                project.setDocState(ProjectEnum.AlreadyStarted.getIndex());
                result = ProjectEnum.AlreadyStarted.getIndex();
            } else {
                project.setDocState(ProjectEnum.Terminated.getIndex());
                result = ProjectEnum.Terminated.getIndex();
            }
        }

        return result;
    }

    public Integer TerminationCode(Long projectId, Integer buttonType) throws Exception {
        Integer docStates = Constants.NORMAL_STATE;

        Long docState = CompositionCustomer.stream().where(w -> w.getProjectId().getId() == projectId && !w.getDocStateAllE().equals("10") && !w.getDocStateAllE().equals("70")).count();
        if (docState > 0) {
            docStates = Constants.MANUSCRIPT;
            return docStates;
        }
        if (buttonType == 0) {
            docState = CompositionCustomer.stream().where(w -> w.getProjectId().getId() == projectId && w.getDocStateAllE().equals("70")).count();
            if (docState > 0) {
                docStates = Constants.MANUSCRIPTREVIEWCOMPIETED;
                return docStates;
            }
        }
        if (buttonType == 1) {
            docState = Report.stream().where(w -> w.getProjectId().getId() == projectId && w.getDocState() == 20).count();
            if (docState > 0) {
                docStates = Constants.REPORT;
                return docStates;
            }
            docState = Engagement.stream().where(w -> w.getProjectId().getId() == projectId && w.getDocState() == 20).count();
            if (docState > 0) {
                docStates = Constants.BUSINESS_AGREEMENT;
                return docStates;
            }
            docState = Report.stream().where(w -> w.getProjectId().getId() == projectId && w.getApplySignStatus() == 20).count();
            if (docState > 0) {
                docStates = Constants.SEAL_STATE;
                return docStates;
            }
            docState = Report.stream().where(w -> w.getProjectId().getId() == projectId && w.getReportingStatus() == 20).count();
            if (docState > 0) {
                docStates = Constants.NEWSPAPERPREPARATION;
                return docStates;
            }
        }
        return docStates;
    }

    /**
     * 删除被审计单位
     *
     * @param dto
     * @throws Exception
     */
    @SystemService(args = "dto")
    public void DeleteCompositionCustomer(CompositionCustomerDTO dto) throws Exception {
        Long id = dto.getId();

        Optional<Report> report = Report.stream().where(c -> c.getCompositionCustomerId().getId().equals(id)).findFirst();
        if (report.isPresent()) {
            throw new BusinessException("该被审计单位被报告引用!");
        }


        //被审计单位
        CompositionCustomer customerEntity = CompositionCustomer.findById(id);
        if (customerEntity == null) return;
        Integer[] stateArray = new Integer[]{11, 30, 31, 32, 33};
        //只有申请修改的时候才可以  新增或者修改 都认为是项目架构发生变化
        if (Arrays.asList(stateArray).contains(customerEntity.getProjectId().getDocState())) {
            //if (customerEntity.getProjectId().getDocState() == ProjectEnum.Amendment.getIndex() || customerEntity.getProjectId().getDocState() == ProjectEnum.AlreadyStarted.getIndex()) {
            CompositionCustomer cc = CompositionCustomer.createNew();
            long parentId = customerEntity.getParentId() == null ? -1L : customerEntity.getParentId().getId();
            cc.updateProjectAndCompostionCustomerRandomCode(customerEntity.getProjectId().getId(), parentId);
        }
        Long projectId = customerEntity.getProjectId().getId();
        Project project = Project.findById(projectId);
        if (project != null && (project.getDocState() == 11 || project.getDocState() == 30)) {
            //删除时差一条审批记录
//            if (project.getDocState() == 30) {
//                saveToDo(projectId, "删除被审计单位");
//            }
            project.setDocState(11);
            project.getCompositionCustomers().remove(customerEntity);
        }
        repository().remove(customerEntity);

        List<CompositionMembers> memeberList = CompositionMembers.stream().where(c -> c.getCompositionCustomerId().getId() == id).toList();
        for (CompositionMembers item : memeberList) {
            item.initialization();
            this.repository().remove(item);
        }

        List<Engagement> listengament = Engagement.stream().where(c -> c.getCompositionCustomerId().getId().equals(id)).toList();
        for (Engagement item : listengament) {
            item.initialization();
            this.repository().remove(item);
        }


    }

    /**
     * 删除被审计单位 差一条数据
     *
     * @param projectId
     * @param comments
     * @throws Exception
     */
    private void saveToDo(Long projectId, String comments) throws Exception {
        UpdateTodoLog todoLog = new UpdateTodoLog();
        Profile profile = AppContext.current().getProfile();

        todoLog.setActorId(profile.getId().toString());
        todoLog.setActorName(profile.getName());
        todoLog.setEntityId(projectId.toString());
        todoLog.setEntityType("Project");
        todoLog.setXmid("删除");
        todoLog.setTaskInstanceName("项目经理");
        todoLog.setProcessId("项目立项");
        todoLog.setComments(comments);
        WorkFlowHelper.saveTodoLog(todoLog);
    }

    /**
     * 根据多个被审计单位。获取多个被审计单位详情
     *
     * @param ListId
     * @return
     * @throws Exception
     */
    @SystemService(args = "listId")
    public ResponseEntity projectByProjectId(List<String> ListId) throws Exception {
        ResponseEntity csResponseEntity = new ResponseEntity();
        try {
            List<TaskProjectDTO> listDTO = new ArrayList<>();
            for (String item : ListId) {
                TaskProjectDTO task = new TaskProjectDTO();
                List<CompositionCustomer> compositionCustomer = CompositionCustomer.stream().where(c -> c.getUuid() == item).toList();
                CompositionCustomer model = compositionCustomer.get(0);
                task.setID(model.getUuid());
                task.setBusinessInformationID(model.getProject().getUuid());
                task.setManagerProjectID(model.getProject().getUuid());
                task.setProjectName(model.getShowName());
                task.setBasicCustomerID(model.getProject().getCustomerName().getUuid());
                task.setProjectCode(model.getCode());
                //task.setProjectName(model.getName());
                task.setManager(model.getProjectManager().getName());
                task.setPartner(model.getProject().getProjectPartner().getName());
                task.setRiskLevel(model.getRiskLevel());
                task.setBusinessCustomerName(model.getName());
                task.setManuscriptTemplateCode(model.getManuscriptTemplate());
                if (Framework.isNotNullOrEmpty(model.getParentId())) {
                    task.setParentId(model.getParentId().getUuid());
                    task.setProjectGrade(model.getLevel());
                } else {
                    task.setProjectGrade(model.getLevel());
                }
                task.setCustomerCode(model.getProject().getCustomerName().getCode());
                if (model.getProjectId().getFinancialReportProject() == ProjectCommon.NotFinancialReport.getIndex()) {
                    task.setManuscriptTemplateName("专项审计工作底稿");
                } else {
                    task.setManuscriptTemplateName(getEnumName("ManuscriptTemplate", model.getManuscriptTemplate()).replaceAll("\\s*", ""));
                }
                task.setBusinessTypeCode(model.getBusinessType());
                task.setBusinessTypeName(model.getBusinessTypeValue());
                task.setAuditStartDate(Framework.localDateTimeToString(model.getAuditStart()));
                task.setAuditEndDate(Framework.localDateTimeToString(model.getAuditEnd()));
                task.setCreateDate(Framework.localDateTimeToString(model.getCreatedOn()));
                task.setReviewStatus(model.getProjectId().getDocState());
                task.setWhetherArchiving(10);
                task.setStateCode(model.getStateCode());
                //管理项目随机码
                task.setRandomCode(model.getRandomCode());
                //被审计单位随机码
                task.setUniqueCode(model.getProjectId().getRandomCode());
                task.setTerminationProject(model.getTerminationProject());
                task.setSortCode(model.getSortCode());
                listDTO.add(task);
            }
            csResponseEntity.setCode(200);
            csResponseEntity.setData(listDTO);

        } catch (Exception ex) {
            csResponseEntity.setCode(500);
            csResponseEntity.setMessages(ex.getMessage());

        }

        return csResponseEntity;
    }

    /**
     * 下载项目组成员
     *
     * @param TaskProjectId
     * @return
     * @throws Exception
     */
    @SystemService(args = "TaskProjectId")
    public ResponseEntity TaskMemberByTaskProjectId(String TaskProjectId) throws Exception {
        ResponseEntity csResponseEntity = new ResponseEntity();

        try {
            List<ACEAuditworkProjectMembersDTO> apms = new ArrayList<>();
            //Long GuidToId = CompositionCustomer.createNew().gainLongId(TaskProjectId);
            Integer isEable = IsEnableEnum.Enable.getIndex();
            List<CompositionMembers> lcms = CompositionMembers.stream().where(w -> w.getCompositionCustomerId().getUuid().equals(TaskProjectId) && w.getIsEnable() == isEable).toList();
            for (CompositionMembers item : lcms) {
                if (Framework.isNullOrEmpty(item)) {
                    break;
                }
                ACEAuditworkProjectMembersDTO dtoitem = new ACEAuditworkProjectMembersDTO();
                dtoitem.setID(item.getUuid());
                dtoitem.setAuditworkProjectID(TaskProjectId);
                dtoitem.setJobNumber(item.getNameId().getCode());
                dtoitem.setFullName(item.getNameId().getLoginName());
                dtoitem.setDisable(item.getNameId().getStatus());
                dtoitem.setProjectRoles(item.getRoleName());
                dtoitem.setLoginUserId(item.getNameId().getUuid());
                dtoitem.setPassWord(item.getNameId().getLoginPassword());
                dtoitem.setDisable(item.getIsEnable());
                dtoitem.setSignIndependenceAgreement(item.getSignatureOfIndependence() == 20 ? 0 : 1);
                dtoitem.setDeptCode(item.getNameId().getDeptCode());
                dtoitem.setDeptName(item.getName().getDeptName());
                apms.add(dtoitem);
            }
            csResponseEntity.setCode(200);
            csResponseEntity.setData(apms);
        } catch (Exception ex) {
            csResponseEntity.setCode(500);
            csResponseEntity.setMessages(ex.getMessage());
        }
        return csResponseEntity;

    }

    /**
     * 通过客户id获取客户信息
     *
     * @param CustomerId
     * @return
     * @throws Exception
     */
    @SystemService(args = "CustomerId")
    public ResponseEntity GetCustomerByCustomerId(String CustomerId) throws Exception {
        ResponseEntity csResponseEntity = new ResponseEntity();
        try {
            //Long GuidToId = Customer.createNew().gainLongId(CustomerId);
            List<ACECustomerDTO> CustomerList = new ArrayList<>();
            List<Customer> Custs = Customer.stream().where(w -> w.getUuid().equals(CustomerId)).toList();

            for (Customer item : Custs) {
                if (Framework.isNullOrEmpty(item)) {
                    break;
                }
                ACECustomerDTO CustDto = new ACECustomerDTO();
                CustDto.setID(CustomerId);
                CustDto.setCustomerCode(item.getCode());
                CustDto.setCustomerName(item.getName());
                CustDto.setCustomerGrade(1);
                CustDto.setFiscalStartMonth(1);
                CustDto.setFiscalStartDay(1);
                CustDto.setFiscalEndMonth(12);
                CustDto.setFiscalEndDay(31);
                EnumValueDTO enumDTO = EnumValue.createNew().getEnumDTO("Industry", item.getIndustryType());
                if (Framework.isNotNullOrEmpty(enumDTO) && Framework.isNotNullOrEmpty(enumDTO.getValue())) {
                    CustDto.setIndustryCode(enumDTO.getValue().toString());
                    CustDto.setIndustryName(enumDTO.getName());
                }
                CustDto.setIsEnable("10");
                CustDto.setTopCustomerId(CustomerId);
                CustomerList.add(CustDto);
            }
            csResponseEntity.setCode(200);
            csResponseEntity.setData(CustomerList);
        } catch (Exception ex) {
            csResponseEntity.setCode(500);
            csResponseEntity.setMessages(ex.getMessage());
        }
        return csResponseEntity;

    }

    /**
     * 通过客户id获取客户信息
     *
     * @param ProjectId
     * @return
     * @throws Exception
     */
    @SystemService(args = "ProjectId")
    public ResponseEntity BusinessByProjectId(String ProjectId) throws Exception {
        ResponseEntity entity = new ResponseEntity();
        try {

            //Long projectuuid = this.createNew().gainLongId(ProjectId);
            List<Project> projectlist = this.stream().where(c -> c.getUuid().equals(ProjectId)).toList();
            if (projectlist.size() <= 0) {
                entity.setCode(501);
                entity.setMessages("提供的项目Id有误!");
                return entity;
            }
            List<BusinessDTO> listdto = new ArrayList<>();
            for (Project project : projectlist) {
                BusinessDTO dto = new BusinessDTO();
                dto.setID(project.getUuid());
                dto.setBusinessCode(project.getCode());
                dto.setBusinessName(project.getShowName());
                dto.setBusinessType(project.getBusinessTypeValue());
                dto.setAuditedCompanyID(project.getCustomerNameId().getUuid());
                dto.setIsFirstUndertaking("10");
                dto.setBusinessStartYear(Integer.toString(project.getAuditStart().getYear()));
                dto.setBusinessStartMonth(Integer.toString(project.getAuditStart().getMonthValue()));

                dto.setBusinessStartDay(Integer.toString(project.getAuditStart().getDayOfMonth()));
                dto.setBusinessEndYear(Integer.toString(project.getAuditEnd().getYear()));
                dto.setBusinessEndMonth(Integer.toString(project.getAuditEnd().getMonthValue()));
                dto.setBusinessEndDay(Integer.toString(project.getAuditEnd().getDayOfMonth()));
                listdto.add(dto);
            }
            entity.setCode(200);
            entity.setData(listdto);
        } catch (Exception ex) {
            entity.setCode(501);
            entity.setMessages(ex.getMessage());
            return entity;
        }
        return entity;
    }

    /**
     * 下载业务客户信息
     *
     * @param ProjectId
     * @return
     * @throws Exception
     */
    @SystemService(args = "ProjectId")
    public ResponseEntity GetCustomerByProjectId(String ProjectId) throws Exception {
        ResponseEntity respons = new ResponseEntity();
        try {
            List<ProjectCustomerDTO> dtolist = new ArrayList<>();
            List<CompositionCustomer> compostionList = CompositionCustomer.stream().where(c -> c.getProjectId().getUuid().equals(ProjectId)).toList();
            if (compostionList.size() <= 0) {
                respons.setCode(500);
                respons.setMessages("提供的项目Id有误!");
                return respons;
            }
            for (CompositionCustomer item : compostionList) {
                ProjectCustomerDTO dto = new ProjectCustomerDTO();
                dto.setID(item.getUuid());
                dto.setCustomerCode(item.getCode());
                dto.setCustomerName(item.getShowName());
                dto.setCustomerID(item.getProjectId().getCustomerNameId().getUuid());
                dto.setManagerProjectID(item.getProject().getUuid());
                dto.setBusinessInformationID(item.getProject().getUuid());
                dto.setCustomerGrade("1");
                dto.setIndustryCode(Integer.toString(item.getProjectId().getCustomerNameId().getIndustryType()));

                EnumValueDTO enumDTO = EnumValue.createNew().getEnumDTO("Industry", item.getProjectId().getCustomerNameId().getIndustryType());
                if (Framework.isNotNullOrEmpty(enumDTO) && Framework.isNotNullOrEmpty(enumDTO.getName())) {
                    dto.setIndustryName(enumDTO.getName());
                }
                dto.setIsEnabled("10");
                dto.setFiscalStartMonth("01");
                dto.setFiscalStartDay("01");
                dto.setFiscalEndMonth("12");
                dto.setFiscalEndDay("31");
                dtolist.add(dto);

            }
            respons.setCode(200);
            respons.setData(dtolist);
        } catch (Exception ex) {
            respons.setCode(500);
            respons.setMessages(ex.getMessage());
        }
        return respons;
    }


    private TaskProjectDTO insertTaskProject(CompositionCustomer model) throws Exception {
        TaskProjectDTO task = new TaskProjectDTO();
        task.setID(model.getUuid());
        task.setBusinessInformationID(model.getProject().getUuid());
        task.setManagerProjectID(model.getProject().getUuid());
        task.setProjectName(model.getShowName());
        task.setBasicCustomerID(model.getProject().getCustomerName().getUuid());
        task.setProjectCode(model.getCode());
        task.setProjectName(model.getShowName());
        task.setManager(model.getProjectManager().getName());
        task.setPartner(model.getProject().getProjectPartner().getName());
        task.setRiskLevel(model.getRiskLevel());
        task.setRandomCode(model.getRandomCode());
        task.setStateCode(model.getStateCode());
        task.setProjectGrade(model.getLevel());
        task.setManuscriptTemplateCode(model.getManuscriptTemplate());
        if (Framework.isNotNullOrEmpty(model.getParentId())) {
            task.setParentId(model.getParentId().getUuid());
        }
        task.setCustomerCode(model.getProject().getCustomerName().getCode());
        EnumValueDTO enumDTO = EnumValue.createNew().getEnumDTO("ManuscriptTemplate", model.getManuscriptTemplate());
        if (Framework.isNotNullOrEmpty(enumDTO) && Framework.isNotNullOrEmpty(enumDTO.getName())) {
            task.setManuscriptTemplateName(enumDTO.getName());
        }
        task.setBusinessTypeCode(model.getBusinessType());
        task.setBusinessTypeName(model.getBusinessTypeValue());

//        EnumValueDTO businessType = EnumType.createNew().getEnumDTO("BusinessType", model.getBusinessType());
//        if (Framework.isNotNullOrEmpty(businessType) && Framework.isNotNullOrEmpty(businessType.getName())) {
//            task.setBusinessTypeName(businessType.getName());
//        }
        task.setAuditStartDate(Framework.localDateTimeToString(model.getAuditStart()));
        task.setAuditEndDate(Framework.localDateTimeToString(model.getAuditEnd()));
        task.setReviewStatus(10);
        task.setWhetherArchiving(10);
        return task;
    }

    /**
     * 待下载
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @SystemService(args = "userId")
    public ResponseEntity taskProject(String userId) throws Exception {
        ResponseEntity responseEntity = new ResponseEntity();
        try {

            List<TaskProjectDTO> taskList = new ArrayList<>();
            List<Long> longList = new ArrayList<>();
            List<CompositionMembers> memberList = CompositionMembers.stream().where(c -> c.getNameId().getUuid().equals(userId) && c.getIsEnable() == 10).toList();
            for (CompositionMembers item : memberList) {
                CompositionCustomer model = item.getCompositionCustomer();

                if (model.getProjectId().getDocState() >= ProjectEnum.AlreadyStarted.getIndex() && model.getProjectId().getDocState() < 100) {
                    if (model.getProjectId().getDocState() != ProjectEnum.Filed.getIndex() && model.getProjectId().getDocState() != ProjectEnum.Invalid.getIndex() && model.getProjectId().getDocState() != ProjectEnum.Terminated.getIndex()) {
                        Long count = longList.stream().filter(c -> c == model.getProjectId().getId()).count();
                        if (count == 0) {
                            longList.add(model.getProjectId().getId());
                        }
                    } else {
                        if (Framework.isNotNullOrEmpty(model.getWhetherUpload())) {
                            if (model.getWhetherUpload() == 10) {
                                Long count = longList.stream().filter(c -> c == model.getProjectId().getId()).count();
                                if (count == 0) {
                                    longList.add(model.getProjectId().getId());
                                }
                            }
                        }
                    }
                }
            }
            for (Long item : longList) {
                List<CompositionCustomer> compositionCustomersList = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(item)).toList();
                Optional<CompositionCustomer> compostionCustomer = compositionCustomersList.stream().filter(c -> c.getLevel() == 1 && c.getParentId() == null).findFirst();
                TaskProjectDTO task = setCompostionCustomer(compostionCustomer.get(), userId);
                List<TaskProjectDTO> taskChild = new ArrayList<>();
                for (CompositionCustomer model : compositionCustomersList) {
                    if (Framework.isNotNullOrEmpty(model.getParentId()) && model.getLevel() != 1) {
                        Long compostionCustomerId = model.getId();
                        List<CompositionMembers> compsitionMember = CompositionMembers.stream().where(c -> c.getCompositionCustomerId().getId() == compostionCustomerId && c.getNameId().getUuid() == userId).toList();
                        if (compsitionMember.size() == 0) continue;

                        taskChild.add(setCompostionCustomer(model, userId));
                        task.setChildProjects(taskChild);
                    }
                }
                taskList.add(task);
            }
            responseEntity.setData(taskList);
            responseEntity.setCode(200);

        } catch (Exception ex) {
            responseEntity.setCode(500);
            responseEntity.setMessages(Framework.getMes(ex));
        }
        return responseEntity;
    }

    public TaskProjectDTO setCompostionCustomer(CompositionCustomer model, String userId) throws Exception {
        TaskProjectDTO task = new TaskProjectDTO();
        task.setID(model.getUuid());
        task.setBusinessInformationID(model.getProject().getUuid());
        task.setManagerProjectID(model.getProject().getUuid());
        task.setProjectName(model.getShowName());
        task.setBasicCustomerID(model.getProject().getCustomerName().getUuid());
        task.setProjectCode(model.getCode());
        task.setProjectName(model.getShowName());
        task.setManager(model.getProjectManager().getName());
        task.setPartner(model.getProject().getProjectPartner().getName());
        task.setRiskLevel(model.getRiskLevel());
        task.setRandomCode(model.getProjectId().getRandomCode());
        task.setUniqueCode(model.getRandomCode());
        task.setStateCode(model.getStateCode());
        task.setReviewStatus(model.getProjectId().getDocState());
        task.setBusinessCustomerName(model.getName());

        task.setProjectGrade(model.getLevel());

        task.setManuscriptTemplateCode(model.getManuscriptTemplate());
        if (Framework.isNotNullOrEmpty(model.getParentId())) {
            task.setParentId(model.getParentId().getUuid());
        }
        task.setCustomerCode(model.getProject().getCustomerName().getCode());
        if (model.getProjectId().getFinancialReportProject() == ProjectCommon.NotFinancialReport.getIndex()) {
            task.setManuscriptTemplateName("专项审计工作底稿");
        } else {
            EnumValueDTO enumDTO = EnumValue.createNew().getEnumDTO("ManuscriptTemplate", model.getManuscriptTemplate());
            if (Framework.isNotNullOrEmpty(enumDTO) && Framework.isNotNullOrEmpty(enumDTO.getName())) {
                task.setManuscriptTemplateName(enumDTO.getName());
            }

        }
        task.setBusinessTypeCode(model.getBusinessType());
        task.setBusinessTypeName(model.getBusinessTypeValue());
        task.setAuditStartDate(Framework.localDateTimeToString(model.getAuditStart()));
        task.setAuditEndDate(Framework.localDateTimeToString(model.getAuditEnd()));
        task.setCreateDate(Framework.localDateTimeToString(model.getCreatedOn()));
        task.setReviewStatus(model.getProjectId().getDocState());
        task.setWhetherArchiving(10);
        task.setEnableDownParent(isBelongToMySelf(model.getId(), userId));
        task.setSortCode(model.getSortCode());
        return task;
    }

    /**
     * 该被审计单位是否数据自己
     *
     * @param id
     * @return
     */
    private Boolean isBelongToMySelf(Long id, String userId) throws Exception {
        long result = CompositionMembers.stream().where(c -> c.getCompositionCustomerId().getId().equals(id) && c.getNameId().getUuid().equals(userId)).count();
        return result <= 0 ? false : true;
    }

    /**
     * 待下载列表  备用
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @SystemService(args = "userId")
    public ResponseEntity test11(String userId) throws Exception {
        ResponseEntity response = new ResponseEntity();
        try {
            List<TaskProjectDTO> taskList = new ArrayList<>();
            List<CompositionCustomer> listcustomer = new ArrayList<>();
            List<CompositionMembers> memberList = CompositionMembers.stream().where(c -> c.getNameId().getUuid().equals(userId)).toList();
            for (CompositionMembers item : memberList) {
                CompositionCustomer model = item.getCompositionCustomer();
                //复核状态为已开工并且项目是正式项目
                if (model.getProjectId().getDocState() == ProjectEnum.AlreadyStarted.getIndex()) {
                    if (Framework.isNullOrEmpty(model.getParentId())) {
                        Long Id = model.getId();
                        Optional<CompositionCustomer> modeList = listcustomer.stream().filter((CompositionCustomer c) -> c.getId() == Id).findFirst();
                        if (!modeList.isPresent()) {
                            listcustomer.add(model);
                        }
                    } else {
                        Long id = model.getParentId().getId();
                        Optional<CompositionCustomer> modeList = listcustomer.stream().filter((CompositionCustomer c) -> c.getId() == id).findFirst();
                        if (!modeList.isPresent()) {
                            List<CompositionCustomer> customerList = CompositionCustomer.stream().where(c -> c.getId() == id).toList();
                            listcustomer.add(customerList.get(0));
                        }
                    }
                }
            }
            for (CompositionCustomer customer : listcustomer) {
                taskList.add(chidrenTaskProject(customer, userId));
            }
            response.setCode(200);
            response.setData(taskList);

        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages(ex.getMessage());
        }
        return response;
    }

    public TaskProjectDTO chidrenTaskProject(CompositionCustomer model, String userId) throws Exception {
        TaskProjectDTO task = new TaskProjectDTO();
        task.setID(model.getUuid());
        task.setBusinessInformationID(model.getProject().getUuid());
        task.setManagerProjectID(model.getProject().getUuid());
        task.setProjectName(model.getShowName());
        task.setBasicCustomerID(model.getProject().getCustomerName().getUuid());
        task.setProjectCode(model.getCode());
        task.setProjectName(model.getShowName());
        task.setManager(model.getProjectManager().getName());
        task.setPartner(model.getProject().getProjectPartner().getName());
        task.setRiskLevel(model.getRiskLevel());
        task.setRandomCode(model.getProjectId().getRandomCode());
        task.setUniqueCode(model.getRandomCode());
        task.setStateCode(model.getStateCode());
        task.setReviewStatus(model.getProjectId().getDocState());
        task.setBusinessCustomerName(model.getName());
        if (Framework.isNullOrEmpty(model.getParentId())) {
            task.setProjectGrade(1);
        } else {
            task.setProjectGrade(2);
        }
        //task.setProjectGrade(model.getLevel());
        task.setManuscriptTemplateCode(model.getManuscriptTemplate());
        if (Framework.isNotNullOrEmpty(model.getParentId())) {
            task.setParentId(model.getParentId().getUuid());
        }
        task.setCustomerCode(model.getProject().getCustomerName().getCode());
        EnumValueDTO enumDTO = EnumValue.createNew().getEnumDTO("ManuscriptTemplate", model.getManuscriptTemplate());
        if (Framework.isNotNullOrEmpty(enumDTO) && Framework.isNotNullOrEmpty(enumDTO.getName())) {
            task.setManuscriptTemplateName(enumDTO.getName());
        }
        task.setBusinessTypeCode(model.getBusinessType());
        task.setBusinessTypeName(model.getBusinessTypeValue());
        task.setAuditStartDate(Framework.localDateTimeToString(model.getAuditStart()));
        task.setAuditEndDate(Framework.localDateTimeToString(model.getAuditEnd()));
        task.setReviewStatus(model.getProjectId().getDocState());
        task.setWhetherArchiving(10);
        Long id = model.getId();
        List<CompositionCustomer> chidrenCustomer = CompositionCustomer.stream().where(c -> c.getParentId().getId() == id).toList();

        if (chidrenCustomer.size() > 0) {
            List<TaskProjectDTO> chidTask = new ArrayList<>();
            for (CompositionCustomer customer : chidrenCustomer) {
                Long compositonId = customer.getId();
                List<CompositionMembers> compsitionMember = CompositionMembers.stream().where(c -> c.getCompositionCustomerId().getId() == compositonId && c.getNameId().getUuid() == userId).toList();
                if (compsitionMember.size() == 0) continue;
                chidTask.add(chidrenTaskProject(customer, userId));
                task.setChildProjects(chidTask);
            }
        }
        return task;
    }

    /**
     * 根据作业项目id获取作业项目信息（主要是风险，底稿，会计期间，客户类型会对下边字段信息更改）
     *
     * @param auditProjectId
     * @return
     * @throws Exception
     */
    @SystemService(args = "auditProjectId")
    public ResponseEntity auditProjectInfoById(String auditProjectId) throws Exception {
        ResponseEntity response = new ResponseEntity();
        try {

            AuditworkProjectChangeDTO dto = new AuditworkProjectChangeDTO();
            List<CompositionCustomer> custList = CompositionCustomer.stream().where(c -> c.getUuid() == auditProjectId).toList();
            if (custList.size() > 0) {
                CompositionCustomer model = custList.get(0);
                dto.setID(model.getUuid());
                dto.setProjectGrade(model.getLevel());
                dto.setRiskLevel(String.valueOf(model.getRiskLevel()));
                dto.setAuditStartDate(Framework.localDateTimeToString(model.getAuditStart()));
                dto.setAuditEndDate(Framework.localDateTimeToString(model.getAuditEnd()));
                dto.setManuscriptTemplateCode(model.getManuscriptTemplate().toString());
                dto.setManuscriptTemplateName(getEnumName("ManuscriptTemplate", model.getManuscriptTemplate()));
                dto.setTerminationProject(model.getTerminationProject());
                if (model.getStateCode() == StateCodeEnum.manuscript.getIndex()) {
                    dto.setIsManuscriptTemplateChanged(1);
                } else {
                    dto.setIsManuscriptTemplateChanged(0);
                }
                if (model.getStateCode() == StateCodeEnum.auditTime.getIndex()) {
                    dto.setIsAuditPeriodChanged(1);
                } else {
                    dto.setIsAuditPeriodChanged(0);
                }
                dto.setReviewStatus(model.getProjectId().getDocState());
                dto.setIsMembersChanged(model.getIsMemberChanged());
                Integer changed = model.getIsChange() == null ? 0 : model.getIsChange();
                dto.setUniqueCode(model.getRandomCode());
                if (Framework.isNullOrEmpty(model.parentId)) dto.setIsChanged(changed.toString());
                response.setCode(200);
                response.setData(dto);
            }

        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages(ex.getMessage());
        }
        return response;
    }

    private String getEnumName(String code, Integer index) throws Exception {
        EnumValueDTO enumDTO = EnumValue.createNew().getEnumDTO(code, index);
        if (Framework.isNotNullOrEmpty(enumDTO) && Framework.isNotNullOrEmpty(enumDTO.getName())) {
            return enumDTO.getName();
        }
        return "";
    }

    /**
     * 获取项目的随机码
     *
     * @return
     * @throws Exception
     */
    @SystemService(args = "projectId")
    public ResponseEntity getProjectRandomById(String projectId) throws Exception {
        ResponseEntity response = new ResponseEntity();
        try {
            List<Project> projectList = this.stream().where(c -> c.getUuid() == projectId).toList();
            if (projectList.size() > 0) {
                response.setCode(200);
                response.setMessages(projectList.get(0).getRandomCode());
            } else {
                response.setCode(200);
                response.setMessages("找不到对应的项目");
            }

        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages(ex.getMessage());
        }
        return response;
    }

    /**
     * 从整个被审计单位表中查询所对应的数据
     *
     * @param key
     * @param value
     * @return
     * @throws Exception
     */
    @SystemService(args = "key,value")
    public ResponseEntity getCompositionCustomerList(String key, String value) throws Exception {
        ResponseEntity response = new ResponseEntity();
        try {
            String param = sqlWhere(key, value);
            if (Framework.isNotNullOrEmpty(param)) {
                List<CompostionCustomerListDTO> data = sqlData(param);
                response.setCode(200);
                response.setData(data);
            }

        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages("服务器内部错误");
        }
        return response;
    }

    private String sqlWhere(String key, String value) {
        HashMap<String, String> map = new HashMap<>();
        map.put("1", " and p.code like '%" + value + "%'"); //项目code
        map.put("2", " and p.showname like '%" + value + "%'"); //场名称
        map.put("3", " and e.name like '%" + value + "%'");//部门名称
        map.put("4", " and u1.name like '%" + value + "%'"); //项目合伙人
        map.put("5", " and u.name like '%" + value + "%'"); // 项目经理
        return map.get(key);
    }

    private List<CompostionCustomerListDTO> sqlData(String value) throws Exception {
        List<Object> p = new ArrayList<>();
        p.add(value);
        String sql = "select distinct\n" +
                " p.uuid as ID,\n" +
                "u.name as ProjectManager, \n" +
                "(select uuid from Prj_ComprositionCustome as n where projectid = t.id and n.level=1) as FirstLevelID, \n" +
                "e.name as Department,  \n" +
                " u1.name as ProjectPartner, \n" +
                "(select uuid from prj_project where id=p.projectid) as ManagerProjectID, \n" +
                "p.code as ProjectCode,\n" +
                "p.name as ProjectName,\n" +
                "p.showName as ProjectFullName \n" +
                " from dbo.Prj_ComprositionCustome as p  \n" +
                "  left join prj_project as t\n" +
                " on t.id =p.projectid\n" +
                " inner join Prj_CompositionMembers as o\n" +
                " on o.compositionCustomerId = p.id\n" +
                " left join base_user u\n" +
                " on u.id = o.nameid\n" +
                " \n" +
                " left join base_user u1\n" +
                " on u1.id = t.projectPartnerId\n" +
                " inner join  Base_Department e\n" +
                " on e.id= t.deptId\n" +
                " where t.docstate not in (10,20,40,50,60,70,100)" + value + "";


        List<CompostionCustomerListDTO> projects = repository().sqlQuery(sql, CompostionCustomerListDTO.class);
        return projects;
    }

    /**
     * 获取初步业务活动底稿列表(为CS端提供接口)
     * GBY
     */
    @SystemService(args = "projectId")
    public ResponseEntity getInitiateBusinessActivityList(String projectId) throws Exception {
        ResponseEntity response = new ResponseEntity();
        try {
            List<InitiateBusinessActivityDTO> list = new ArrayList<>();
            Optional<Project> p = this.stream().where(w -> w.getUuid().equals(projectId)).findFirst();
            if (p.isPresent()) {
                Project item = p.get();
                for (Integer i = 1; i < 9; i++) {
                    InitiateBusinessActivityDTO dto = new InitiateBusinessActivityDTO();
                    dto.setSortNO(i.toString());
                    switch (i) {
                        case 1:
                            dto.setDisplayText("初步业务活动程序表");
                            dto.setUrl("Web-Page.html?schemaCode=ProgramSheetExcelReadOnlyUrl&schemaType=FormPanel&projectGuid=" + projectId);
                            break;
                        case 2:
                            if (item.getFirstOrContinuous() == UndertakeCountEnum.FirstUndertaking.getIndex())//首次承接
                            {
                                dto.setDisplayText("业务承接评价表");
                                dto.setUrl("Web-Page.html?schemaCode=MaintainReadOnlyUrl&schemaType=FormPanel&projectGuid=" + projectId);
                            } else {
                                dto.setDisplayText("业务保持评价表");
                                dto.setUrl("Web-Page.html?schemaCode=Maintain1ReadOnlyUrl&schemaType=FormPanel&projectGuid=" + projectId);
                            }
                            break;
                        case 3:
                            dto.setDisplayText("被审计单位业务情况调查记录");
                            dto.setUrl("Web-Page.html?schemaCode=UnitVSSurveyExcelReadOnlyUrl&schemaType=FormPanel&projectGuid=" + projectId);
                            break;
                        case 4:
                            dto.setDisplayText("被审计单位财务报表");
                            dto.setUrl("Web-Page.html?schemaCode=PrjAttachmentReadOnlyUrl&schemaType=GridPanel&projectGuid=" + projectId);
                            break;
                        case 5:
                            dto.setDisplayText("财务尽职调查报告");
                            dto.setUrl("Web-Page.html?schemaCode=PrjAttachmentrReadOnlyUrl&schemaType=GridPanel&projectGuid=" + projectId);
                            break;
                        case 6:
                            dto.setDisplayText("项目组资源和专业胜任能力的调查记录");
                            dto.setUrl("Web-Page.html?schemaCode=CompetencyExcelReadOnlyUrl&schemaType=FormPanel&projectGuid=" + projectId);
                            break;
                        case 7:
                            dto.setDisplayText("项目组构成人员情况表");
                            dto.setUrl("Web-Page.html?schemaCode=ProjectMembersDrafturl&schemaType=GridPanel&projectGuid=" + projectId);
                            break;
                        case 8:
                            dto.setDisplayText("项目进点前的技术准备会议记录");
                            dto.setUrl("Web-Page.html?schemaCode=MinutesMeetingAttReadOnlyUrl&schemaType=GridPanel&projectGuid=" + projectId);
                            break;

                    }
                    list.add(dto);
                }

                response.setCode(200);
                response.setData(list);
            } else {
                response.setCode(200);
                response.setMessages("项目ID为" + projectId + "不存在");
            }
        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages(ex.getMessage());
        }
        return response;
    }

    /**
     * 通过项目id获取所有的人被审计单位
     *
     * @param projectId
     * @return ResponseEntity
     * @throws Exception
     */
    @SystemService(args = "projectId")
    public ResponseEntity compositionCustomerList(String projectId) throws Exception {
        ResponseEntity response = new ResponseEntity();
        try {
            List<ProjectComponentDTO> listdto = new ArrayList<>();
            // Long proId = this.createNew().gainLongId(projectId);
            List<CompositionCustomer> compositionList = CompositionCustomer.stream().where(c -> c.getProjectId().getUuid().equals(projectId)).sortedBy(c -> c.getSortCode()).toList();
            if (compositionList.size() > 0) {
                for (CompositionCustomer item : compositionList) {
                    ProjectComponentDTO dto = new ProjectComponentDTO();
//                    dto.setId(item.getUuid());
                    dto.setProjectComponentName(item.getName());
                    dto.setManagerProjectID(item.getProjectId().getUuid());
                    dto.setProjectGrade(item.getLevel());
                    if (Framework.isNotNullOrEmpty(item.getParentId())) {
                        dto.setIsMakeAuditReport(String.valueOf(item.getIsReport() == true ? 1 : 0));
                        dto.setParentAuditworkProjectID(item.getParentId().getUuid());
                    } else {
                        dto.setIsMakeAuditReport(String.valueOf(item.getIsSubsidiaryReport() == true ? 1 : 0));
                        dto.setParentAuditworkProjectID("");
                    }
                    dto.setAuditworkProjectID(item.getUuid());
                    dto.setSortCode(item.getSortCode());
                    EnumValueDTO enumDTO = EnumValue.createNew().getEnumDTO("ManuscriptTemplate", item.getManuscriptTemplate());
                    if (Framework.isNotNullOrEmpty(enumDTO) && Framework.isNotNullOrEmpty(enumDTO.getName())) {
                        dto.setManuscriptTemplateName(enumDTO.getName().replaceAll("\\s*", ""));
                    }
                    dto.setManuscriptTemplateCode(item.getManuscriptTemplate().toString());
                    listdto.add(dto);
                }
                response.setCode(200);
                response.setData(listdto);
            } else {
                response.setCode(200);
                response.setMessages("找不到对应的数据~");
            }

        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages(ex.getMessage());
        }
        return response;
    }

    /**
     * 获取重要性和特大账户列表
     *
     * @param projectId 项目id
     * @return ResponseEntity
     */
    @SystemService(args = "projectId")
    public ResponseEntity auditImportanceList(String projectId) {
        ResponseEntity respon = new ResponseEntity();
        try {
            AuditImportanceListDTO auditdto = new AuditImportanceListDTO();
            List<AuditImportance> importList = AuditImportance.stream().where(c -> c.getManagerProjectID() == projectId).toList();
            List<Auditspecificimportance> specificList = Auditspecificimportance.stream().where(c -> c.getProjectID() == projectId).toList();
            Optional<AuditImportance> model = importList.stream().filter(c -> c.getParentAuditworkProjectID() == null).findFirst();
            if (model.isPresent()) {
                auditdto.setIsExistAuditspecificImportance(model.get().getIsExistAuditspecificImportance());
            }
            List<AuditImportanceDTO> importlist = new ArrayList<>();
            for (AuditImportance item : importList) {
                AuditImportanceDTO importdto = new AuditImportanceDTO();
                importdto.setID(item.getAuditImportanceId());
                importdto.setProjectComponentName(item.getProjectComponentName());
                importdto.setProjectID(item.getManagerProjectID());
                importdto.setAuditworkProjectID(item.getAuditworkProjectID());
                importdto.setProjectGrade(item.getProjectGrade());
                importdto.setIsMakeAuditReport(item.getIsMakeAuditReport());
                importdto.setSelectBenchmarkID(item.getSelectBenchmarkID());
                importdto.setSelectBenchmarkCode(item.getSelectBenchmarkCode());
                importdto.setSelectBenchmarkName(item.getSelectBenchmarkName());
                importdto.setSelectBenchmarkExplain(item.getSelectBenchmarkExplain());
                importdto.setSelectBenchmarkNumerical(item.getSelectBenchmarkNumerical());
                importdto.setImportanceRatio(item.getImportanceRatio());
                importdto.setCalFinanImportance(item.getCalFinanImportance());
                importdto.setConfFinanImportance(item.getConfFinanImportance());
                importdto.setSelectReason(item.getSelectReason());
                importdto.setTheExecImporRatio(item.getTheExecImporRatio());
                importdto.setGroupAllocationImportance(item.getGroupAllocationImportance());
                importdto.setTheExecImportance(item.getTheExecImportance());
                importdto.setConfExecImportance(item.getConfExecImportance());
                importdto.setTheExecImportExplain(item.getTheExecImportExplain());
                importdto.setTheMisstaValueRatio(item.getTheMisstaValueRatio());
                importdto.setTheMisstaValue(item.getTheMisstaValue());
                importdto.setConfMisstaValue(item.getConfMisstaValue());
                importdto.setTheMisstaValueExplain(item.getTheMisstaValueExplain());
                importdto.setFirstImportanceFlag(item.getFirstImportanceFlag());
                importdto.setModifyEffect(item.getModifyEffect());
                importdto.setRemark(item.getRemark());
                importdto.setIsExistAuditspecificImportance(item.getIsExistAuditspecificImportance());
                importdto.setJobNumber(item.getJobNumber());
                importdto.setFullName(item.getFullName());
                importdto.setStateCode(item.getStateCode());
                importdto.setUniqueCode(item.getUniqueCode());
                importlist.add(importdto);
                auditdto.setImportList(importlist);
            }
            List<auditspecificimportanceDTO> auditlist = new ArrayList<>();
            for (Auditspecificimportance t : specificList) {
                auditspecificimportanceDTO specificdto = new auditspecificimportanceDTO();
                specificdto.setID(t.getAuditspecificimportanceId());
                specificdto.setProjectID(t.getProjectID());
                specificdto.setProjectComponentName(t.getProjectComponentName());
                specificdto.setAuditworkProjectID(t.getAuditworkProjectID());
                specificdto.setProjectGrade(t.getProjectGrade());
                specificdto.setAuditImportanceID(t.getAuditImportanceID());
                specificdto.setSortNO(t.getSortNo());
                specificdto.setTradingAccountCode(t.getTradingAccountCode());
                specificdto.setTradingAccountID(t.getTradingAccountID());
                specificdto.setTradingAccountName(t.getTradingAccountName());
                specificdto.setTheAccountImportance(t.getTheAccountImportance());
                specificdto.setTheActualExecImportance(t.getTheActualExecImportance());
                specificdto.setTheExplain(t.getTheExplain());
                specificdto.setFirstImportanceFlag(t.getFirstImportanceFlag());
                specificdto.setJobNumber(t.getJobNumber());
                specificdto.setFullName(t.getFullName());
                auditlist.add(specificdto);
                auditdto.setSpecificList(auditlist);
            }
            respon.setCode(200);
            respon.setData(auditdto);

        } catch (Exception ex) {
            respon.setCode(500);

        }
        return respon;
    }

    /**
     * 保存 重要性和特大账户
     *
     * @return
     * @throws Exception
     */
    @SystemService(args = "model")
    public ResponseEntity saveAuditImportance(AuditImportanceListDTO model) throws Exception {
        ResponseEntity respons = new ResponseEntity();
        try {
            String projectId = model.getImportList().get(0).getProjectID();
            List<AuditImportance> auditImportaceList = AuditImportance.stream().where(c -> c.getManagerProjectID() == projectId).toList();
            List<Auditspecificimportance> specificList = Auditspecificimportance.stream().where(c -> c.getProjectID() == projectId).toList();

            for (AuditImportance item : auditImportaceList) {
                item.initialization();
                this.repository().remove(item);
            }

            for (Auditspecificimportance item : specificList) {
                item.initialization();
                this.repository().remove(item);
            }

            for (AuditImportanceDTO item : model.getImportList()) {
                AuditImportance importModel = AuditImportance.createNew();
                if (Framework.isNullOrEmpty(item.getParentAuditworkProjectID())) {
                    importModel.setIsExistAuditspecificImportance(item.getIsExistAuditspecificImportance());
                }
                importModel.setParentAuditworkProjectID(item.getParentAuditworkProjectID());
                importModel.setAuditImportanceId(item.getID());
                importModel.setProjectComponentName(item.getProjectComponentName());
                importModel.setManagerProjectID(item.getProjectID());
                importModel.setAuditworkProjectID(item.getAuditworkProjectID());
                importModel.setProjectGrade(item.getProjectGrade());
                importModel.setIsMakeAuditReport(item.getIsMakeAuditReport());
                importModel.setSelectBenchmarkID(item.getSelectBenchmarkID());
                importModel.setSelectBenchmarkCode(item.getSelectBenchmarkCode());
                importModel.setSelectBenchmarkName(item.getSelectBenchmarkName());
                importModel.setSelectBenchmarkExplain(item.getSelectBenchmarkExplain());
                importModel.setSelectBenchmarkNumerical(item.getSelectBenchmarkNumerical());
                importModel.setImportanceRatio(item.getImportanceRatio());
                importModel.setCalFinanImportance(item.getCalFinanImportance());
                importModel.setConfFinanImportance(item.getConfFinanImportance());
                importModel.setSelectReason(item.getSelectReason());
                importModel.setTheExecImporRatio(item.getTheExecImporRatio());
                importModel.setGroupAllocationImportance(item.getGroupAllocationImportance());
                importModel.setTheExecImportance(item.getTheExecImportance());
                importModel.setConfExecImportance(item.getConfExecImportance());
                importModel.setTheExecImportExplain(item.getTheExecImportExplain());
                importModel.setTheMisstaValueRatio(item.getTheMisstaValueRatio());
                importModel.setTheMisstaValue(item.getTheMisstaValue());
                importModel.setConfMisstaValue(item.getConfMisstaValue());
                importModel.setTheMisstaValueExplain(item.getTheMisstaValueExplain());
                importModel.setFirstImportanceFlag(item.getFirstImportanceFlag());
                importModel.setModifyEffect(item.getModifyEffect());
                importModel.setRemark(item.getRemark());
                importModel.setJobNumber(item.getJobNumber());
                importModel.setFullName(item.getFullName());
                importModel.setStateCode(item.getStateCode());
                importModel.setUniqueCode(item.getUniqueCode());
                repository().add(importModel);

            }
            if (model.getSpecificList().size() > 0) {
                for (auditspecificimportanceDTO item : model.getSpecificList()) {
                    Auditspecificimportance entity = Auditspecificimportance.createNew();
                    entity.setAuditspecificimportanceId(item.getID());
                    entity.setProjectComponentName(item.getProjectComponentName());
                    entity.setProjectID(item.getProjectID());
                    entity.setAuditworkProjectID(item.getAuditworkProjectID());
                    entity.setProjectGrade(item.getProjectGrade());
                    entity.setAuditImportanceID(item.getAuditImportanceID());
                    entity.setSortNo(item.getSortNO());
                    entity.setTradingAccountID(item.getTradingAccountID());
                    entity.setTradingAccountCode(item.getTradingAccountCode());
                    entity.setTradingAccountName(item.getTradingAccountName());
                    entity.setTheAccountImportance(item.getTheAccountImportance());
                    entity.setTheActualExecImportance(item.getTheActualExecImportance());
                    entity.setTheExplain(item.getTheExplain());
                    entity.setFirstImportanceFlag(item.getFirstImportanceFlag());
                    entity.setJobNumber(item.getJobNumber());
                    entity.setFullName(item.getFullName());
                    repository().add(entity);
                }
            }
            respons.setCode(200);
            respons.setMessages("ok");
        } catch (Exception ex) {
            respons.setCode(500);
            respons.setMessages(ex.getMessage());
        }
        return respons;
    }

    /**
     * 保存 组成部分重要性
     *
     * @param listdto
     * @return
     * @throws Exception
     */
    @SystemService(args = "listdto")
    public ResponseEntity saveConstituentPartImportance(List<ConstituentPartImportanceDTO> listdto) throws Exception {
        ResponseEntity resonse = new ResponseEntity();
        try {
            String projectId = listdto.get(0).getManagerProjectID();
            List<ConstituentPartImportance> constituentPartImportanceList = ConstituentPartImportance.stream().where(c -> c.getManagerProjectID() == projectId).toList();
            if (constituentPartImportanceList.size() > 0) {
                //查到库中有数据则都删除
                for (ConstituentPartImportance item : constituentPartImportanceList) {
                    item.initialization();
                    this.repository().remove(item);
                }
            }
            extractConstituentPartImportance(listdto);
            resonse.setCode(200);
            resonse.setMessages("保存成功~");
        } catch (Exception ex) {
            resonse.setCode(500);
            resonse.setMessages(ex.getMessage());
        }
        return resonse;
    }

    /**
     * 组成部分 封装方法
     *
     * @param list
     * @throws Exception
     */
    private void extractConstituentPartImportance(List<ConstituentPartImportanceDTO> list) throws Exception {
        for (ConstituentPartImportanceDTO dto : list) {
            ConstituentPartImportance model = ConstituentPartImportance.createNew();
            if (Framework.isNotNullOrEmpty(dto.getParentAuditworkProjectID())) {
                model.setAuditNotes(dto.getAuditNotes());
            }
            model.setRevenue(dto.getRevenue());
            model.setIsManuallyAdd(dto.getIsManuallyAdd());
            model.setConstituentPartImportanceId(dto.getID());
            model.setManagerProjectID(dto.getManagerProjectID());
            model.setAuditworkProjectID(dto.getAuditworkProjectID());
            model.setProjectGrade(dto.getProjectGrade());
            model.setMainBusiness(dto.getMainBusiness());
            model.setParentAuditworkProjectID(dto.getParentAuditworkProjectID());
            model.setTotalAssets(dto.getTotalAssets());
            model.setTotalProfit(dto.getTotalProfit());
            model.setAssetBenchmarkRatio(dto.getAssetBenchmarkRatio());
            model.setRevenueBenchmarkRatio(dto.getRevenueBenchmarkRatio());
            model.setTotalProfitBenchmarkRatio(dto.getTotalProfitBenchmarkRatio());
            model.setIsFinancialImportance(dto.getIsFinancialImportance());
            model.setIsImportantConstituentPart(dto.getIsImportantConstituentPart());
            model.setIsExistSpecialRisks(dto.getIsExistSpecialRisks());
            model.setIsMakeAuditReport(dto.getIsMakeAuditReport());
            model.setReasonsOfSpecialRisks(dto.getReasonsOfSpecialRisks());
            model.setAuditStrategyID(dto.getAuditStrategyID());
            model.setAuditStrategyName(dto.getAuditStrategyName());
            model.setUnderstandingUnitsAndEnvironment(dto.getUnderstandingUnitsAndEnvironment());
            model.setUnderstandingUnitsAndEnvironmentByControl(dto.getUnderstandingUnitsAndEnvironmentByControl());
            model.setUnderstandingAndTesting1(dto.getUnderstandingAndTesting1());
            model.setUnderstandingAndTesting2(dto.getUnderstandingAndTesting2());
            model.setUnderstandingAndTesting3(dto.getUnderstandingAndTesting3());
            model.setUnderstandingAndTesting4(dto.getUnderstandingAndTesting4());
            model.setUnderstandingAndTesting5(dto.getUnderstandingAndTesting5());
            model.setUnderstandingAndTesting6(dto.getUnderstandingAndTesting6());
            model.setUnderstandingAndTesting7(dto.getUnderstandingAndTesting7());
            model.setUnderstandingAndTesting8(dto.getUnderstandingAndTesting8());
            model.setUnderstandingAndTesting9(dto.getUnderstandingAndTesting9());
            model.setUnderstandingAndTesting10(dto.getUnderstandingAndTesting10());
            model.setAuditNotes(dto.getAuditNotes());
            model.setGroupBenchmarkName(dto.getGroupBenchmarkName());
            model.setGroupFinanImportance(dto.getGroupFinanImportance());
            model.setGroupExecImporRatio(dto.getGroupExecImporRatio());
            model.setGroupMisstaValueRatio(dto.getGroupMisstaValueRatio());
            model.setBusinessCustomerID(dto.getBusinessCustomerID());
            model.setBusinessCustomerName(dto.getBusinessCustomerName());
            model.setBasicCustomerID(dto.getBasicCustomerID());
            model.setAuditType(dto.getAuditType());
            model.setBenchmarkID(dto.getBenchmarkID());
            model.setBenchmarkName(dto.getBenchmarkName());
            model.setGroupRate(dto.getGroupRate());
            model.setRiskJudgment(dto.getRiskJudgment());
            model.setGroupExecuteRate(dto.getGroupExecuteRate());
            model.setGroupExecuteValue(dto.getGroupExecuteValue());
            model.setTheMisstaValue(dto.getTheMisstaValue());
            model.setJobNumber(dto.getJobNumber());
            model.setFullName(dto.getFullName());
            model.setStateCode(dto.getStateCode());
            model.setUniqueCode(dto.getUniqueCode());
            model.setSortNO(dto.getSortNO());
            model.setConstituentPartImportanceRatio(dto.getConstituentPartImportanceRatio());
            model.setManuscriptTemplateCode(dto.getManuscriptTemplateCode());
            model.setManuscriptTemplateName(dto.getManuscriptTemplateName());
            model.setUnderstandingAndTesting11(dto.getUnderstandingAndTesting11());
            model.setUnderstandingAndTesting12(dto.getUnderstandingAndTesting12());
            model.setUnderstandingAndTesting13(dto.getUnderstandingAndTesting13());
            model.setUnderstandingAndTesting14(dto.getUnderstandingAndTesting14());
            model.setUnderstandingAndTesting15(dto.getUnderstandingAndTesting15());
            model.setUnderstandingAndTesting16(dto.getUnderstandingAndTesting16());
            model.setUnderstandingAndTesting17(dto.getUnderstandingAndTesting17());
            model.setUnderstandingAndTesting18(dto.getUnderstandingAndTesting18());
            model.setUnderstandingAndTesting19(dto.getUnderstandingAndTesting19());
            model.setUnderstandingAndTesting20(dto.getUnderstandingAndTesting20());
            repository().add(model);
        }

    }

    /**
     * 列表 组成部分重要性
     *
     * @param projectId
     * @return
     * @throws Exception
     */
    @SystemService(args = "projectId")
    public ResponseEntity constituentPartImportanceList(String projectId) throws Exception {
        ResponseEntity response = new ResponseEntity();
        try {
            List<ConstituentPartImportanceDTO> listdto = new ArrayList<>();
            List<ConstituentPartImportance> constList = ConstituentPartImportance.stream().where(c -> c.getManagerProjectID() == projectId).toList();
            if (constList.size() > 0) {
                for (ConstituentPartImportance item : constList) {
                    ConstituentPartImportanceDTO model = new ConstituentPartImportanceDTO();
                    if (Framework.isNotNullOrEmpty(item.getParentAuditworkProjectID()))
                        model.setAuditNotes(item.getAuditNotes());
                    model.setID(item.getConstituentPartImportanceId());
                    model.setManagerProjectID(item.getManagerProjectID());
                    model.setRevenue(item.getRevenue());
                    model.setIsManuallyAdd(item.getIsManuallyAdd());
                    model.setAuditworkProjectID(item.getAuditworkProjectID());
                    model.setProjectGrade(item.getProjectGrade());
                    model.setMainBusiness(item.getMainBusiness());
                    model.setParentAuditworkProjectID(item.getParentAuditworkProjectID());
                    model.setTotalAssets(item.getTotalAssets());
                    model.setTotalProfit(item.getTotalProfit());
                    model.setAssetBenchmarkRatio(item.getAssetBenchmarkRatio());
                    model.setRevenueBenchmarkRatio(item.getRevenueBenchmarkRatio());
                    model.setTotalProfitBenchmarkRatio(item.getTotalProfitBenchmarkRatio());
                    model.setIsFinancialImportance(item.getIsFinancialImportance());
                    model.setIsImportantConstituentPart(item.getIsImportantConstituentPart());
                    model.setIsExistSpecialRisks(item.getIsExistSpecialRisks());
                    model.setIsMakeAuditReport(item.getIsMakeAuditReport());
                    model.setReasonsOfSpecialRisks(item.getReasonsOfSpecialRisks());
                    model.setAuditStrategyID(item.getAuditStrategyID());
                    model.setAuditStrategyName(item.getAuditStrategyName());
                    model.setUnderstandingUnitsAndEnvironment(item.getUnderstandingUnitsAndEnvironment());
                    model.setUnderstandingUnitsAndEnvironmentByControl(item.getUnderstandingUnitsAndEnvironmentByControl());
                    model.setUnderstandingAndTesting1(item.getUnderstandingAndTesting1());
                    model.setUnderstandingAndTesting2(item.getUnderstandingAndTesting2());
                    model.setUnderstandingAndTesting3(item.getUnderstandingAndTesting3());
                    model.setUnderstandingAndTesting4(item.getUnderstandingAndTesting4());
                    model.setUnderstandingAndTesting5(item.getUnderstandingAndTesting5());
                    model.setUnderstandingAndTesting6(item.getUnderstandingAndTesting6());
                    model.setUnderstandingAndTesting7(item.getUnderstandingAndTesting7());
                    model.setUnderstandingAndTesting8(item.getUnderstandingAndTesting8());
                    model.setUnderstandingAndTesting9(item.getUnderstandingAndTesting9());
                    model.setUnderstandingAndTesting10(item.getUnderstandingAndTesting10());
                    model.setAuditNotes(item.getAuditNotes());
                    model.setGroupBenchmarkName(item.getGroupBenchmarkName());
                    model.setGroupFinanImportance(item.getGroupFinanImportance());
                    model.setGroupExecImporRatio(item.getGroupExecImporRatio());
                    model.setGroupMisstaValueRatio(item.getGroupMisstaValueRatio());
                    model.setBusinessCustomerID(item.getBusinessCustomerID());
                    model.setBusinessCustomerName(item.getBusinessCustomerName());
                    model.setBasicCustomerID(item.getBasicCustomerID());
                    model.setAuditType(item.getAuditType());
                    model.setBenchmarkID(item.getBenchmarkID());
                    model.setBenchmarkName(item.getBenchmarkName());
                    model.setGroupRate(item.getGroupRate());
                    model.setRiskJudgment(item.getRiskJudgment());
                    model.setGroupExecuteRate(item.getGroupExecuteRate());
                    model.setGroupExecuteValue(item.getGroupExecuteValue());
                    model.setTheMisstaValue(item.getTheMisstaValue());
                    model.setJobNumber(item.getJobNumber());
                    model.setFullName(item.getFullName());
                    model.setStateCode(item.getStateCode());
                    model.setUniqueCode(item.getUniqueCode());
                    model.setSortNO(item.getSortNO());
                    model.setConstituentPartImportanceRatio(item.getConstituentPartImportanceRatio());
                    model.setManuscriptTemplateCode(item.getManuscriptTemplateCode());
                    model.setManuscriptTemplateName(item.getManuscriptTemplateName());
                    model.setUnderstandingAndTesting11(item.getUnderstandingAndTesting11());
                    model.setUnderstandingAndTesting12(item.getUnderstandingAndTesting12());
                    model.setUnderstandingAndTesting13(item.getUnderstandingAndTesting13());
                    model.setUnderstandingAndTesting14(item.getUnderstandingAndTesting14());
                    model.setUnderstandingAndTesting15(item.getUnderstandingAndTesting15());
                    model.setUnderstandingAndTesting16(item.getUnderstandingAndTesting16());
                    model.setUnderstandingAndTesting17(item.getUnderstandingAndTesting17());
                    model.setUnderstandingAndTesting18(item.getUnderstandingAndTesting18());
                    model.setUnderstandingAndTesting19(item.getUnderstandingAndTesting19());
                    model.setUnderstandingAndTesting20(item.getUnderstandingAndTesting20());
                    listdto.add(model);
                }
                response.setCode(200);
                response.setData(listdto);
            } else {
                response.setCode(200);
                response.setMessages("没有查到响应的数据~");
            }
        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages(ex.getMessage());
        }
        return response;
    }

    /**
     * 获取当前这一级和下一级的 项目架构
     *
     * @param auditProjectId
     * @return
     * @throws Exception
     */
    @SystemService(args = "auditProjectId")
    public ResponseEntity conpositionCustomerInfo(String auditProjectId) throws Exception {
        ResponseEntity response = new ResponseEntity();
        try {
            List<CompositionCustomer> conpositionList = CompositionCustomer.stream().where(c -> c.getUuid() == auditProjectId).toList();
            if (conpositionList.size() > 0) {
                CompositionCustomer model = conpositionList.get(0);
                AuditworkProjectInfoDTO listdto = new AuditworkProjectInfoDTO();
                listdto.setID(model.getUuid());
                Customer custModel = model.getProject().getCustomerNameId();
                if (Framework.isNotNullOrEmpty(custModel)) {
                    listdto.setBasicCustomerID(custModel.getUuid());
                    listdto.setBusinessCustomerCode(custModel.getCode());
                } else {
                    listdto.setBasicCustomerID("");
                    listdto.setBusinessCustomerCode("");
                }
                listdto.setManagerProjectID(model.getProject().getUuid());
                listdto.setManagerProjectName(model.getProject().getName());
                listdto.setParentId(Framework.isNotNullOrEmpty(model.getParentId()) ? model.getParentId().getUuid() : "");
                listdto.setProjectCode(model.getCode());
                listdto.setProjectName(model.getName());
                listdto.setProjectGrade("1");
                listdto.setCompanyType(model.getCompanyRelationship());
                listdto.setMergeStartDate(Framework.localDateTimeToString(model.getAuditStart()));
                listdto.setExecutor(model.getProjectManagerId().getName());
                listdto.setMergeEndDate(Framework.localDateTimeToString(model.getAuditEnd()));
                listdto.setSortCode(model.getSortCode());
                Long parentId = model.getId();
                boolean result = CompositionCustomer.stream().anyMatch(c -> c.getParentId() != null && c.getParentId().getId() == parentId);
                listdto.setLastLevelFlag(result ? 0 : 1);
                if (result) {
                    List<CompositionCustomer> chidrenList = CompositionCustomer.stream().where(c -> c.getParentId().getId() == parentId).toList();
                    if (chidrenList.size() > 0) {
                        List<AuditworkProjectInfoDTO> chidrenlist = new ArrayList<>();
                        for (CompositionCustomer chidrenModel : chidrenList) {
                            AuditworkProjectInfoDTO chidren = new AuditworkProjectInfoDTO();
                            chidren.setID(chidrenModel.getUuid());
                            chidren.setParentId(chidrenModel.getParentId().getUuid());
                            chidren.setBasicCustomerID("");
                            chidren.setBusinessCustomerCode("");
                            chidren.setProjectCode(chidrenModel.getCode());
                            chidren.setProjectName(chidrenModel.getName());
                            chidren.setProjectGrade("2");
                            chidren.setCompanyType(chidrenModel.getCompanyRelationship());
                            chidren.setManagerProjectID(chidrenModel.getProject().getUuid());
                            chidren.setManagerProjectName(chidrenModel.getProject().getName());
                            chidren.setCompanyType(chidrenModel.getCompanyRelationship());
                            chidren.setMergeStartDate(Framework.localDateTimeToString(chidrenModel.getAuditStart()));
                            chidren.setMergeEndDate(Framework.localDateTimeToString(chidrenModel.getAuditEnd()));
                            chidren.setExecutor(chidrenModel.getProjectManagerId().getName());
                            chidren.setSortCode(chidrenModel.getSortCode());
                            Long nodeParentId = chidrenModel.getId();
                            boolean downResult = CompositionCustomer.stream().anyMatch(c -> c.getParentId() != null && c.getParentId().getId() == nodeParentId);
                            chidren.setLastLevelFlag(downResult ? 0 : 1);
                            chidrenlist.add(chidren);
                            listdto.setChildProjects(chidrenlist);
                        }
                    }
                }
                response.setCode(200);
                response.setData(listdto);
            } else {
                response.setCode(200);
                response.setMessages("找不到对应的数据~");
            }
        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages(ex.getMessage());
        }
        return response;
    }


    @SystemService(args = "projectId,formId,sPrjid")
    public Object getProject(Long projectId, Long formId, String sPrjid) throws Exception {
        Optional<Project> projects;
        if (Framework.isNullOrEmpty(sPrjid)) {
            projects = this.stream().where(w -> w.getId() == projectId).findFirst();
        } else {
            projects = this.stream().where(w -> w.getUuid().equals(sPrjid)).findFirst();
        }

        if (projects.isPresent()) {
            Project entity = projects.get();
            entity.initialization();
            return entity.toDictionary(FormPage.getFormColumn(formId));
        }
        return this.createNew();
    }

    /**
     * 保存  风险
     *
     * @param model model
     * @return
     * @throws Exception
     */
    @SystemService(args = "model")
    public ResponseEntity saveRisk(RiskDTO model) throws Exception {
        ResponseEntity response = new ResponseEntity();
        try {
            String id = model.getMRAList().get(0).getAuditworkProjectID();
            List<MajorRiskArea> majorList = MajorRiskArea.stream().where(c -> c.getAuditworkProjectID() == id).toList();
            List<MajorRiskTradingAndDisclosure> majorRisk = MajorRiskTradingAndDisclosure.stream().where(c -> c.getAuditworkProjectID() == id).toList();

            for (MajorRiskArea item : majorList) {
                item.initialization();
                this.repository().remove(item);
            }

            for (MajorRiskTradingAndDisclosure item : majorRisk) {
                item.initialization();
                this.repository().remove(item);
            }

            for (MajorRiskAreaDTO item : model.getMRAList()) {
                MajorRiskArea entity = MajorRiskArea.createNew();
                entity.setMARID(item.getID()); //cs端的主键id
                entity.setAuditworkProjectID(item.getAuditworkProjectID());
                entity.setRiskContent(item.getRiskContent());
                entity.setRiskStage(item.getRiskStage());
                entity.setIsSpecia(item.getIsSpeciaRisk());
                entity.setIsFraud(item.getIsFraudRisk());
                entity.setAffectAccIdent(item.getAffectAccIdent());
                entity.setCounterMeasures(item.getCounterMeasures());
                entity.setComponent(item.getComponent());
                entity.setSortNO(item.getSortNO());
                repository().add(entity);
            }
            for (MajorRiskTradingAndDisclosureDTO item : model.getMRTADList()) {
                MajorRiskTradingAndDisclosure entity = MajorRiskTradingAndDisclosure.createNew();
                entity.setMRTADID(item.getID());
                entity.setAuditworkProjectID(item.getAuditworkProjectID());
                entity.setSortNO(item.getSortNO());
                entity.setReportItemType(item.getReportItemType());
                entity.setMajorMattersDescribe(item.getMajorMattersDescribe());
                entity.setIndexNo(item.getIndexNo());
                repository().add(entity);
            }
            response.setCode(200);
            response.setMessages("ok");
        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages(ex.getMessage());
        }
        return response;
    }

    /**
     * 查询 风险列表
     *
     * @param auditProjectId 被审计单位id
     * @return
     */
    @SystemService(args = "auditProjectId")
    public ResponseEntity riskList(String auditProjectId) {
        ResponseEntity response = new ResponseEntity();
        try {
            RiskDTO dtolist = new RiskDTO();
            List<MajorRiskAreaDTO> mradtoList = new ArrayList<>();
            List<MajorRiskTradingAndDisclosureDTO> mrtaddtoList = new ArrayList<>();
            List<MajorRiskArea> mraList = MajorRiskArea.stream().where(c -> c.getAuditworkProjectID() == auditProjectId).toList();
            List<MajorRiskTradingAndDisclosure> mrtadList = MajorRiskTradingAndDisclosure.stream().where(c -> c.getAuditworkProjectID() == auditProjectId).toList();
            for (MajorRiskArea item : mraList) {
                MajorRiskAreaDTO model = new MajorRiskAreaDTO();
                model.setID(item.getMARID());
                model.setAuditworkProjectID(item.getAuditworkProjectID());
                model.setRiskContent(item.getRiskContent());
                model.setAffectAccIdent(item.getAffectAccIdent());
                model.setSortNO(item.getSortNO());
                model.setRiskStage(item.getRiskStage());
                model.setIsFraudRisk(item.getIsFraud());
                model.setIsSpeciaRisk(item.getIsSpecia());
                model.setComponent(item.getComponent());
                model.setCounterMeasures(item.getCounterMeasures());
                mradtoList.add(model);
            }
            dtolist.setMRAList(mradtoList);
            for (MajorRiskTradingAndDisclosure item : mrtadList) {
                MajorRiskTradingAndDisclosureDTO model = new MajorRiskTradingAndDisclosureDTO();
                model.setID(item.getMRTADID());
                model.setAuditworkProjectID(item.getAuditworkProjectID());
                model.setSortNO(item.getSortNO());
                model.setReportItemType(item.getReportItemType());
                model.setMajorMattersDescribe(item.getMajorMattersDescribe());
                model.setIndexNo(item.getIndexNo());
                mrtaddtoList.add(model);
            }
            dtolist.setMRTADList(mrtaddtoList);
            response.setCode(200);
            response.setData(dtolist);
        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages(ex.getMessage());
        }
        return response;
    }

    public String getEnumName(String code, String value) throws Exception {
        return EnumValue.createNew().gainEnumDataDTO(code, value);
    }

    /**
     * 获取项目的状态 项目流程状态 底稿流程状态 复核意见状态 来决定cs端是否可以上传 11
     *
     * @param auditProjectId 作业项目。 后期使用
     * @param projectId      项目id
     * @return
     * @throws Exception
     */
//    @SystemService(args = "auditProjectId,projectId")
//    public ResponseEntity getAuditProjectState(String auditProjectId, String projectId) throws Exception {
//        ResponseEntity response = new ResponseEntity();
//        try {
//            UpLoadProjectDataDTO dto = new UpLoadProjectDataDTO();
//            CompositionCustomer model = CompositionCustomer.stream().where(c->c.getUuid().equals(auditProjectId)).toList().get(0);
//            if (Framework.isNotNullOrEmpty(model)) {
//                if (Framework.isNullOrEmpty(model.getManuscriptReviewState())) {
//                    if (model.getProjectId().getDocState() == ProjectEnum.AlreadyStarted.getIndex()) {
//                        dto = UploadChidrenFac(true, AuditUploadState.AlreadyStarted.getIndex(), "已开工");
//                    } else if (model.getProjectId().getDocState() == ProjectEnum.Rejected.getIndex()) {
//                        dto = UploadChidrenFac(false, AuditUploadState.Rejected.getIndex(), "已拒绝");
//                    } else if (model.getProjectId().getDocState() == ProjectEnum.Invalid.getIndex()) {
//                        dto = UploadChidrenFac(false, AuditUploadState.Invalid.getIndex(), "已作废");
//                    } else if (model.getProjectId().getDocState() == ProjectEnum.Terminated.getIndex()) {
//                        dto = UploadChidrenFac(false, AuditUploadState.Terminated.getIndex(), "已终止");
//                    } else if (model.getProjectId().getDocState() == ProjectEnum.Filed.getIndex()) {
//                        dto = UploadChidrenFac(false, AuditUploadState.Filed.getIndex(), "已归档");
//                    }
//                } else {
//                    //  >10  <70   只需要getReviewOpinionState
//                    if (model.getManuscriptReviewState() == ManuscriptReviewStateEnum.NotSubmitted.getIndex()) {
//                        dto = UploadChidrenFac(true, AuditUploadState.NotSubmitted.getIndex(), "未提交");
//                    } else if (model.getManuscriptReviewState() == ManuscriptReviewStateEnum.Returned.getIndex()) {
//                        dto = UploadChidrenFac(true, AuditUploadState.Returned.getIndex(), "已退回");
//                    } else if (model.getManuscriptReviewState() == ManuscriptReviewStateEnum.SecondaryReview.getIndex()) {
//                        if (Framework.isNotNullOrEmpty(model.getReviewOpinionState())) {
//                            if (model.getReviewOpinionState() > 0 && model.getReviewOpinionState() == ReviewOpinionStateEnum.PendingReply.getIndex()) {
//                                dto = UploadChidrenFac(true, AuditUploadState.PendingReply.getIndex(), "二级复核--待回复");
//                            } else {
//                                dto = UploadChidrenFac(false, AuditUploadState.Replied.getIndex(), "二级复核-已回复");
//                            }
//                        } else {
//                            dto = UploadChidrenFac(false, AuditUploadState.Replied.getIndex(), "二级复核中");
//                        }
//                    } else if (model.getManuscriptReviewState() == ManuscriptReviewStateEnum.ThirdReview.getIndex()) {
//                        if (Framework.isNotNullOrEmpty(model.getReviewOpinionState())) {
//                            if (model.getReviewOpinionState() == ReviewOpinionStateEnum.PendingReply.getIndex()) {
//                                dto = UploadChidrenFac(true, AuditUploadState.PendingReply.getIndex(), "三级复核--待回复");
//                            } else {
//                                dto = UploadChidrenFac(false, AuditUploadState.Replied.getIndex(), "三级复核-已回复");
//                            }
//                        } else {
//                            dto = UploadChidrenFac(false, AuditUploadState.Replied.getIndex(), "三级复核");
//                        }
//                    } else if (model.getManuscriptReviewState() == ManuscriptReviewStateEnum.BranchReview.getIndex()) {
//                        if (Framework.isNotNullOrEmpty(model.getReviewOpinionState())) {
//                            if (model.getReviewOpinionState() == ReviewOpinionStateEnum.PendingReply.getIndex()) {
//                                dto = UploadChidrenFac(true, AuditUploadState.PendingReply.getIndex(), "分所复核--待回复");
//                            } else {
//                                dto = UploadChidrenFac(false, AuditUploadState.Replied.getIndex(), "分所复核-已回复");
//                            }
//                        } else {
//                            dto = UploadChidrenFac(false, AuditUploadState.Replied.getIndex(), "分所复核");
//                        }
//                    } else if (model.getManuscriptReviewState() == ManuscriptReviewStateEnum.GeneralReview.getIndex()) {
//                        if (Framework.isNotNullOrEmpty(model.getReviewOpinionState())) {
//                            if (model.getReviewOpinionState() == ReviewOpinionStateEnum.PendingReply.getIndex()) {
//                                dto = UploadChidrenFac(true, AuditUploadState.PendingReply.getIndex(), "总所复核--待回复");
//                            } else {
//                                dto = UploadChidrenFac(false, AuditUploadState.Replied.getIndex(), "总所复核-已回复");
//                            }
//                        } else {
//                            dto = UploadChidrenFac(false, AuditUploadState.Replied.getIndex(), "总所复核");
//                        }
//                    } else if (model.getManuscriptReviewState() == AuditUploadState.ReviewCompleted.getIndex()) {
//                        dto = UploadChidrenFac(false, AuditUploadState.ReviewCompleted.getIndex(), "底稿流程-复核完成");
//                    }
//                }
//
//            } else {
//                response.setCode(200);
//                response.setMessages("没找到相关数据~");
//            }
//            response.setCode(200);
//            response.setData(dto);
//        } catch (Exception ex) {
//            response.setCode(500);
//            response.setMessages(ex.getMessage());
//        }
//        return response;
//    }
    @SystemService(args = "auditProjectId,projectId,jobNumber")
    public ResponseEntity getAuditProjectState(String auditProjectId, String projectId, String jobNumber) throws Exception {
        ResponseEntity response = new ResponseEntity();
        try {
            UpLoadProjectDataDTO dto = new UpLoadProjectDataDTO();
            Integer isEnable = IsEnableEnum.Enable.getIndex();
            CompositionCustomer model = CompositionCustomer.stream().where(c -> c.getUuid().equals(auditProjectId)).toList().get(0);
            Optional<CompositionMembers> members = CompositionMembers.stream().where(c -> c.getCompositionCustomerId().getUuid().equals(auditProjectId) && c.getNameId().getCode().equals(jobNumber) && c.getIsEnable() == isEnable).findFirst();
            //Optional<CompositionMembers> members = CompositionMembers.stream().where(c -> c.getCompositionCustomerId().getUuid().equals(auditProjectId)&& c.getNameId().getCode().equals(jobNumber)).findFirst();
            if (!members.isPresent()) {
                dto = UploadChidrenFac(false, AuditUploadState.Invalid.getIndex(), "该用户被禁用或者不存在", csPathList(model));
                response.setCode(200);
                response.setData(dto);
                return response;
            }
            if (Framework.isNotNullOrEmpty(model)) {
                String[] strArray = {};
                if (model.getIsComplete() == IsEnableEnum.DisEnable.getIndex()) {
                    if (Framework.isNotNullOrEmpty(model.getDocStateAllE())) {
                        strArray = model.getDocStateAllE().split("\\/");
                    }
                    //已作废
                    if (model.getProjectId().getDocState() == ProjectEnum.Invalid.getIndex()) {
                        dto = UploadChidrenFac(false, AuditUploadState.Invalid.getIndex(), "已作废", csPathList(model));
                    } else if (model.getProjectId().getDocState() == ProjectEnum.Filed.getIndex()) {
                        if (Framework.isNotNullOrEmpty(model.getWhetherUpload())) {
                            if (model.getWhetherUpload() == 10) //可以上传底稿  郭碧原的申请修改
                            {
                                dto = UploadChidrenFac(true, AuditUploadState.AlreadyStarted.getIndex(), "申请借阅--申请修改", csPathList(model));
                            } else {
                                dto = UploadChidrenFac(false, AuditUploadState.Filed.getIndex(), "已归档", csPathList(model));
                            }
                        } else {
                            dto = UploadChidrenFac(false, AuditUploadState.Filed.getIndex(), "已归档", csPathList(model));
                        }
                    } else if (model.getProjectId().getDocState() == ProjectEnum.ManuscriptAmendment.getIndex()) {
                        dto = UploadChidrenFac(true, AuditUploadState.Rejected.getIndex(), "底稿拒绝中", csPathList(model));
                    } else if (model.getProjectId().getDocState() == ProjectEnum.refuse.getIndex()) {
                        dto = UploadChidrenFac(true, AuditUploadState.Rejected.getIndex(), "已拒绝", csPathList(model));
                    } else if (model.getProjectId().getDocState() == ProjectEnum.Terminated.getIndex()) {
                        dto = UploadChidrenFac(false, AuditUploadState.Terminated.getIndex(), "已终止", csPathList(model));
                    } else if (model.getProjectId().getDocState() == ProjectEnum.InTheIssuanceOfTheReport.getIndex()) {
                        dto = UploadChidrenFac(false, AuditUploadState.Terminated.getIndex(), "报告出具中", csPathList(model));
                    } else if (model.getProjectId().getDocState() == ProjectEnum.FilingOfManuscripts.getIndex()) {
                        dto = UploadChidrenFac(false, AuditUploadState.Terminated.getIndex(), "底稿归档中", csPathList(model));
                    } else if (strArray[0].equals(String.valueOf(ManuscriptReviewStateEnum.ReviewCompleted.getIndex()))) {
                        dto = UploadChidrenFac(false, AuditUploadState.Returned.getIndex(), EnumValue.createNew().gainEnumDataDTO("ManuscriptReviewStateType", model.getDocStateAllE()), csPathList(model));
                    } else {
                        if (model.getProjectId().getDocState() == ProjectEnum.AlreadyStarted.getIndex() || model.getProjectId().getDocState() == ProjectEnum.ManuscriptReview.getIndex()) { //已开工
                            if (Integer.valueOf(strArray[0]) > 10) {
                                if (Framework.isNotNullOrEmpty(model.getProjectId().getReviewOpinionState())) {
                                    if (model.getProjectId().getReviewOpinionState() == ReviewOpinionStateEnum.PendingReply.getIndex()) {
                                        dto = UploadChidrenFac(true, AuditUploadState.Returned.getIndex(), EnumValue.createNew().gainEnumDataDTO("ManuscriptReviewStateType", model.getDocStateAllE()), csPathList(model));
                                    } else {
                                        dto = UploadChidrenFac(false, AuditUploadState.Returned.getIndex(), EnumValue.createNew().gainEnumDataDTO("ManuscriptReviewStateType", model.getDocStateAllE()), csPathList(model));
                                    }
                                } else {
                                    dto = UploadChidrenFac(false, AuditUploadState.Returned.getIndex(), EnumValue.createNew().gainEnumDataDTO("ManuscriptReviewStateType", model.getDocStateAllE()), csPathList(model));
                                }
                            } else {
                                dto = UploadChidrenFac(true, AuditUploadState.AlreadyStarted.getIndex(), "已开工", csPathList(model));
                            }
                        }
                    }
                } else {
                    if (model.getProjectId().getDocState() == ProjectEnum.Invalid.getIndex()) {
                        dto = UploadChidrenFac(false, AuditUploadState.Invalid.getIndex(), "已作废", csPathList(model));
                    } else if (model.getProjectId().getDocState() == ProjectEnum.Terminated.getIndex()) {
                        dto = UploadChidrenFac(false, AuditUploadState.Terminated.getIndex(), "已终止", csPathList(model));
                    } else if (model.getProjectId().getDocState() == ProjectEnum.Filed.getIndex()) {
                        if (model.getWhetherUpload() == 10) //可以上传底稿  郭碧原的申请修改
                        {
                            dto = UploadChidrenFac(true, AuditUploadState.AlreadyStarted.getIndex(), "申请借阅--申请修改", csPathList(model));
                        } else {
                            dto = UploadChidrenFac(false, AuditUploadState.Filed.getIndex(), "已归档", csPathList(model));
                        }
                    } else {
                        dto = UploadChidrenFac(true, AuditUploadState.AlreadyStarted.getIndex(), "已开工", csPathList(model));
                    }
                }
            } else {
                response.setCode(200);
                response.setMessages("没找到相关数据~");
            }
            response.setCode(200);
            response.setData(dto);
        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages(Framework.getMes(ex));
        }
        return response;

    }

    public UpLoadProjectDataDTO UploadChidrenFac(Boolean allowSync, Integer statusCode, String StatusDescription, List<CsUploadPathDTO> CsUploadPathList) throws Exception {
        UpLoadProjectDataDTO dto = new UpLoadProjectDataDTO();
        dto.setIsAllowSync(allowSync);
        dto.setStatusCode(statusCode);
        dto.setStatusDescription(StatusDescription);
        dto.setPathList(CsUploadPathList);
        return dto;
    }

    public List<CsUploadPathDTO> csPathList(CompositionCustomer model) throws Exception {

        ManuscriptItem item = ManuscriptItem.createNew();
        List<CsUploadPathDTO> listdto = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            int num = i == 1 ? 10 : 20;
            CsUploadPathDTO dto = new CsUploadPathDTO();
            dto.setPath(item.csUploadPath(model.getCode(), num));
            dto.setType(num);
            listdto.add(dto);
        }
        return listdto;

    }


    /**
     * 保存  总体时间安排
     *
     * @param listdto
     * @return
     * @throws Exception
     */
    @SystemService(args = "listdto")
    public ResponseEntity saveOverallSchedule(List<OverallScheduleDTO> listdto) throws Exception {
        ResponseEntity response = new ResponseEntity();
        try {
            String auditProjectId = listdto.get(0).getAuditWorkProjectId();
            List<OverallSchedule> OverallScheduleList = OverallSchedule.stream().where(c -> c.getAuditworkProjectID() == auditProjectId).toList();

            for (OverallSchedule item : OverallScheduleList) {
                item.initialization();
                this.repository().remove(item);
            }

            for (OverallScheduleDTO item : listdto) {
                OverallSchedule entity = OverallSchedule.createNew();
                entity.setDLSId(item.getId());
                entity.setAuditworkProjectID(item.getAuditWorkProjectId());
                entity.setScheduleType(item.getScheduleType());
                entity.setSortNO(item.getSortNO());
                entity.setContent(item.getContent());
                entity.setCommunicationObject(item.getCommunicationObject());
                entity.setCommunicationCharger(item.getCommunicationCharger());
                entity.setScheduleTime(item.getScheduleTime());
                entity.setFullName(item.getFullName());
                entity.setJobNumber(item.getJobNumber());
                repository().add(entity);
            }
            response.setCode(200);
            response.setMessages("ok");
        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages(ex.getMessage());
        }
        return response;
    }

    /**
     * 列表  总体时间安排
     *
     * @param auditProjectId
     * @return
     * @throws Exception
     */
    @SystemService(args = "auditProjectId")
    public ResponseEntity overallScheduleList(String auditProjectId) throws Exception {
        ResponseEntity response = new ResponseEntity();
        try {
            List<OverallScheduleDTO> listdto = new ArrayList<>();
            List<OverallSchedule> OverallScheduleList = OverallSchedule.stream().where(c -> c.getAuditworkProjectID() == auditProjectId).toList();
            if (OverallScheduleList.size() > 0) {
                for (OverallSchedule item : OverallScheduleList) {
                    OverallScheduleDTO dto = new OverallScheduleDTO();
                    dto.setId(item.getDLSId());
                    dto.setAuditWorkProjectId(item.getAuditworkProjectID());
                    dto.setScheduleType(item.getScheduleType());
                    dto.setScheduleTime(item.getScheduleTime());
                    dto.setSortNO(item.getSortNO());
                    dto.setContent(item.getContent());
                    dto.setCommunicationObject(item.getCommunicationObject());
                    dto.setCommunicationCharger(item.getCommunicationCharger());
                    dto.setFullName(item.getFullName());
                    dto.setJobNumber(item.getJobNumber());
                    listdto.add(dto);
                }
            }
            response.setCode(200);
            response.setData(listdto);
        } catch (Exception ex) {
            response.setMessages(ex.getMessage());
            response.setCode(500);
        }
        return response;
    }

    /**
     * 保存 业务特征
     *
     * @param listdto
     * @return
     * @throws Exception
     */
    @SystemService(args = "listdto")
    public ResponseEntity saveBusinessCharacteristic(List<BusinessCharacteristicDTO> listdto) throws Exception {
        ResponseEntity response = new ResponseEntity();
        try {
            String managerId = listdto.get(0).getManagerProjectID();
            List<BusinessCharacteristic> businessCharacterList = BusinessCharacteristic.stream().where(c -> c.getManagerProjectID() == managerId).toList();

            for (BusinessCharacteristic item : businessCharacterList) {
                item.initialization();
                this.repository().remove(item);
            }

            for (BusinessCharacteristicDTO item : listdto) {
                BusinessCharacteristic entity = BusinessCharacteristic.createNew();
                entity.setBussnessId(item.getId());
                entity.setSuitableForItemID(item.getSuitableForItemID());
                entity.setCode(item.getCode());
                entity.setAuditworkProjectID(item.getAuditworkProjectID());
                entity.setBusinessDataType(item.getBusinessDataType());
                entity.setSortNO(item.getSortNO());
                entity.setContent(item.getContent());
                entity.setSuitableForItem(item.getSuitableForItem());
                entity.setSelectTpye(item.getSelectTpye());
                entity.setIsSelected(item.getIsSelected());
                entity.setIndustrySpecificProvisions(item.getIndustrySpecificProvisions());
                entity.setOrtherBusinessCharacteristic(item.getOrtherBusinessCharacteristic());
                entity.setJobNumber(item.getJobNumber());
                entity.setFullName(item.getFullName());
                entity.setManagerProjectID(item.getManagerProjectID());
                entity.setAuditPrompt(item.getAuditPrompt());
                repository().add(entity);
            }
            response.setCode(200);
            response.setMessages("ok");
        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages(ex.getMessage());
        }
        return response;
    }

    /**
     * 列表 业务特征
     *
     * @param auditProjectId
     * @return
     * @throws Exception
     */
    @SystemService(args = "auditProjectId")
    public ResponseEntity businessCharacteristicList(String auditProjectId) throws Exception {
        ResponseEntity response = new ResponseEntity();
        try {
            List<BusinessCharacteristicDTO> listdto = new ArrayList<>();
            List<BusinessCharacteristic> businessCharacteristicList = BusinessCharacteristic.stream().where(c -> c.getManagerProjectID() == auditProjectId).toList();
            if (businessCharacteristicList.size() > 0) {
                for (BusinessCharacteristic item : businessCharacteristicList) {
                    BusinessCharacteristicDTO dto = new BusinessCharacteristicDTO();
                    dto.setId(item.getBussnessId());
                    dto.setManagerProjectID(item.getManagerProjectID());
                    dto.setSuitableForItemID(item.getSuitableForItemID());
                    dto.setCode(item.getCode());
                    dto.setAuditworkProjectID(item.getAuditworkProjectID());
                    dto.setBusinessDataType(item.getBusinessDataType());
                    dto.setSortNO(item.getSortNO());
                    dto.setContent(item.getContent());
                    dto.setSuitableForItem(item.getSuitableForItem());
                    dto.setSelectTpye(item.getSelectTpye());
                    dto.setIsSelected(item.getIsSelected());
                    dto.setIndustrySpecificProvisions(item.getIndustrySpecificProvisions());
                    dto.setOrtherBusinessCharacteristic(item.getOrtherBusinessCharacteristic());
                    dto.setJobNumber(item.getJobNumber());
                    dto.setFullName(item.getFullName());
                    dto.setAuditPrompt(item.getAuditPrompt());
                    listdto.add(dto);
                }
            }
            response.setCode(200);
            response.setData(listdto);
        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages(ex.getMessage());
        }
        return response;
    }

    /**
     * 根据项目id获取所有的被审计单位
     *
     * @param projectId
     * @return
     * @throws Exception
     */
    @SystemService(args = "projectId")
    public ResponseEntity taskProjectList(String projectId) throws Exception {
        ResponseEntity response = new ResponseEntity();
        try {
            List<TaskProjectDTO> tasklist = new ArrayList<>();
            //Long id = this.createNew().gainLongId(projectId);
            CompositionCustomer.stream().where(c -> c.getUuid() == projectId).toList();
            List<CompositionCustomer> list = CompositionCustomer.stream().where(c -> c.getProjectId().getUuid().equals(projectId)).toList();
            for (CompositionCustomer item : list) {
                TaskProjectDTO dto = new TaskProjectDTO();
                dto.setID(item.getUuid());
                dto.setProjectName(item.getName());
                if (Framework.isNullOrEmpty(item.getParentId())) {
                    dto.setParentId("");
                } else {
                    dto.setParentId(item.getParentId().getUuid());
                }
                dto.setBusinessTypeName(item.getBusinessTypeValue());
                dto.setAuditStartDate(Framework.localDateTimeToString(item.getAuditStart()));
                dto.setAuditEndDate(Framework.localDateTimeToString(item.getAuditEnd()));
                tasklist.add(dto);
            }
            response.setData(tasklist);
            response.setCode(200);
        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages(ex.getMessage());
        }

        return response;
    }

    /**
     * 上传至服务器
     *
     * @param auditProjectId
     * @return
     * @throws Exception http://192.168.40.28:8899/Project-analysisUploadCsData.do?auditProjectId=eabbec1d-f55e-47a3-b7f4-8ab4c6b01509
     *                   auditfiles    auditfilesvsmanuscript   manuscriptitem
     */
    @SystemService(args = "auditProjectId")
    public ResponseEntity analysisUploadCsData(String auditProjectId) throws Exception {
        ResponseEntity response = new ResponseEntity();
        CsUploadFileUtil csUploadFileUtil = (CsUploadFileUtil) AppHelper.getBean("csUploadFileUtil");
        CompositionCustomer compositionCustomer = csUploadFileUtil.processCompositionCustomer(auditProjectId);
        ManuscriptItem item = ManuscriptItem.createNew();
        try {
            Map<Integer, String> maplist = new HashMap<Integer, String>();
            maplist.put(STATUS_CREATE, item.csUploadPath(compositionCustomer.getCode(), STATUS_CREATE));
            maplist.put(STATUS_UPDATE, item.csUploadPath(compositionCustomer.getCode(), STATUS_UPDATE));

            csUploadFileUtil.processAuditFiles(auditProjectId, maplist);
            csUploadFileUtil.processManuscriptItem(auditProjectId, maplist, compositionCustomer);
            csUploadFileUtil.processAuditFilesVsManuscript(auditProjectId, maplist);
            response.setCode(200);
            response.setMessages("ok");
        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages("服务内部错误!" + Framework.getMes(ex));
            Framework.log.info(Framework.getMes(ex));
//            针对空文件夹，上传一直报错问题修改
//            String str = Paths.get(DxnConfig.getCsfileUploadPath(),item.gainAttachmentPath(), compositionCustomer.getCode()).toString();
//            delFolder(str);
        }
        return response;
    }

    /**
     * 针对空文件夹，上传一直报错问题修改
     *
     * @param folderPath
     */
    //删除文件夹
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            Framework.printStackTrace(e);
        }
    }
    //删除指定文件夹下的所有文件

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }


    //GBY 获取当前项目处于底稿复核中的状态  0 没有处于底稿复核中的数据
    @SystemService(args = "prjId")
    public Integer getProjectState(Long prjId) throws Exception {
        Integer state = 0;
        Optional<CompositionCustomer> citem = CompositionCustomer.stream().where(w -> w.getProjectId().getId() == prjId &&
                (w.getDocStateAllE().contains("30") || w.getDocStateAllE().contains("40") || w.getDocStateAllE().contains("50") ||
                        w.getDocStateAllE().contains("60"))).findFirst();
        if (citem.isPresent()) {
            state = 1;
        }

        return state;
    }

    /**
     * GBY
     * 返回当前登录人是否有权限对底稿复核界面 进行提交 操作
     */
    @SystemService(args = "prjId")
    public Boolean getSubmitPermission(Long prjId) throws Exception {
        Long userId = AppContext.current().getProfile().getId();
        Project p = Project.findById(prjId);
        if (p.getProjectManagerId().getId().equals(userId))
            return true;

        return false;

    }

    /**
     * 获取项目的财报类型，默认为财报项目
     */
    @SystemService(args = "prjId")
    public Integer gainFinancialReportProject(Long prjId) throws Exception {
        Project p = Project.findById(prjId);
        if (Framework.isNotNullOrEmpty(p.getFinancialReportProject())) {
            return p.getFinancialReportProject();
        }
        return 10;
    }

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
//        if (this.getFirstOrContinuous() == 20) {
//            copyTemplate("2-A1业务保持评价表.xlsx");
//        } else {
//            copyTemplate("2-A1业务承接评价表.xlsx");
//        }
//        if (this.getFirstOrContinuous() == 20) {
//            BusinessEvaluation("2-A1业务保持评价表.xlsx");
//        } else {
//            BusinessEvaluation("2-A1业务承接评价表.xlsx");
//        }

    }

    /**
     * 获取底稿索引号
     * GBY
     *
     * @param custId         被审计单位ID
     * @param manuscriptType 底稿类型
     *                       CompilingPersonSignName 编制人
     *                       FirstLevelReviewerSignName 复核人
     * @throws Exception
     */
    public static String gainManuscripItemIndexNumber(Long custId, String manuscriptType, String importFilePath, Long sortNO, String name) throws Exception {
        CompositionCustomer customer = repository().findById(custId, CompositionCustomer.class);
        String uuid = customer.getUuid();
        Optional<ManuscriptItem> optmanuscript = ManuscriptItem.stream().where(w -> w.getAuditworkProjectID().equals(uuid) && w.getManuscriptType().equals(manuscriptType)
                && w.getIsGroup().equals("1")).findFirst();

        if (optmanuscript.isPresent()) {
            Path filepath = Paths.get(importFilePath);
            String filename;
            if (Framework.isNullOrEmpty(name)) {
                filename = filepath.getFileName().toString();
            } else {
                filename = name;
            }
            ManuscriptItem manuscriptGroup = optmanuscript.get();
            String structureCode = manuscriptGroup.getStructureCode();

            Optional<ManuscriptItem> has = ManuscriptItem.stream().where(w -> w.getAuditworkProjectID().equals(uuid) && w.getManuscriptType().equals(manuscriptType)
                    && w.getIsGroup().equals("0") && w.getParentStructureCode().equals(structureCode) && w.getManuscriptFile().equals(filename)).findAny();
            if (has.isPresent()) //如果已经存在就不用再次保存了
            {
                return "";
            }

            return structureCode + String.format("%03d", sortNO);


        }
        return "";
    }

    //项目与约定书校验
    @Override
    public void onSubmitWorkflowing(WorkFlowNode node) throws Exception {
        Long projectId = this.getId();
        //提交审计报告日期
        Optional<BusinessEvaluation> businessEvaluationFirst = BusinessEvaluation.stream().where(c -> c.getProjectId().getId().equals(projectId)).findFirst();
        if (businessEvaluationFirst.isPresent()) {
            LocalDateTime sd3 = LocalDateTime.now();
            BusinessEvaluation bus = businessEvaluationFirst.get();
            bus.initialization();
            if (Framework.isNotNullOrEmpty(bus.getSubmissionDate())) { //提交审计报告的日期改成只读，第一次提交的时候赋值提交时间。如果已经有值了就不需要赋值了。
//                if (businessEvaluationFirst.get().getSubmissionDate().isBefore(sd3)) {
//                    throw new BusinessException("初步业务活动底稿-提交审计报告日期 不能小于项目的提交日期");
//                }
            } else {
                GregorianCalendar gs = new GregorianCalendar();
                gs.setTime(new Date());
                bus.setSubmissionDate(gs.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                repository().commit();
            }
        }
        //提交归档时校验
        if (node.getDocColumn().equals("ArchiveState")) {
            //1 母子分所有底稿必须复核完成，否则按原规则提示
            // 2.报告:  查看每个被审计单位下 是否单独出具报告， 如果是的时候校验 报告是否都走完流程， 否择不管。
            //
            // 4 完成后清理 底稿 报告数据 --- 后期做

            //获取主被审计单位ID，为将初步业务活动底稿写入到底稿目录中
            CompositionCustomer customer = new CompositionCustomer();
            CompositionCustomerDTOE cust = customer.getCompositionCustomerByPrjId(projectId);
            Long custid = cust.getId();

            String ls_path;
            ManuscriptItem mitem = new ManuscriptItem();


            // 提交归档之前校验 报备 报告都处于复核完成的状态  新需求 5月1日的需求 5月3号更改， 大信反馈


            List<Report> reportList = Report.stream().where(w -> w.getProjectId().getId() == projectId).toList();
            if (reportList.size() > 0) {
                Long reportState = reportList.stream().filter(w -> w.getDocState() < 30).count();
                if (reportState > 0) {
                    throw new BusinessException("该项目下有业务报告尚未完成，不能进行归档操作");
                }
                Long reportingStatus = reportList.stream().filter(w -> w.getReportingStatus() < 30).count();
                if (reportingStatus > 0) {
                    throw new BusinessException("该项目下有业务报备尚未完成，不能进行归档操作");
                }
                Long ApplySignStatus = reportList.stream().filter(w -> w.getApplySignStatus() < 30 || w.getIsSign() == false).count();
                if (ApplySignStatus > 0) {
                    throw new BusinessException("该项目下有报告用印尚未完成，不能进行归档操作");
                }
            }

            List<ProjectMembers> projectMembersList = ProjectMembers.stream().where(c -> c.getSignatureOfIndependence() == 20 && c.getProjectId().getId() == projectId && c.getIsEnable() == 10).toList();
            if (projectMembersList.size() > 0) throw new BusinessException("有项目成员没有签署独立声明不能归档");
            if (this.getFirstOrContinuous() == 20) {
                ls_path = copyTemplate("2-A1业务保持评价表.xlsx", "ManuscriptItemArchive");//2.,"",""
                mitem.createManuscripItem(custid, "11", ls_path, 2L, "", this.getProjectManagerId().getName(), this.getProjectPartnerId().getName());
                ls_path = copyTemplate("7-A3项目组构成人员情况表.xlsx", "ManuscriptItemArchive");//5
                mitem.createManuscripItem(custid, "11", ls_path, 5L, "", this.getProjectManagerId().getName(), this.getProjectPartnerId().getName());
                ls_path = copyTemplate("1-A初步业务活动程序表-连续审计.xlsx", "ManuscriptItemArchive");//1
                mitem.createManuscripItem(custid, "11", ls_path, 1L, "", this.getProjectManagerId().getName(), this.getProjectPartnerId().getName());
            } else {
                ls_path = copyTemplate("2-A1业务承接评价表.xlsx", "ManuscriptItemArchive");//2
                mitem.createManuscripItem(custid, "11", ls_path, 2L, "", this.getProjectManagerId().getName(), this.getProjectPartnerId().getName());
                ls_path = copyTemplate("7-A3项目组构成人员审批表.xlsx", "ManuscriptItemArchive");//5
                mitem.createManuscripItem(custid, "11", ls_path, 5L, "", this.getProjectManagerId().getName(), this.getProjectPartnerId().getName());
                ls_path = copyTemplate("1-A初步业务活动程序表-首次承接.xlsx", "ManuscriptItemArchive");//1
                mitem.createManuscripItem(custid, "11", ls_path, 1L, "", this.getProjectManagerId().getName(), this.getProjectPartnerId().getName());
            }
            ls_path = copyTemplate("3-A1-1被审计单位业务情况调查记录.xlsx", "ManuscriptItemArchive");//3
            mitem.createManuscripItem(custid, "11", ls_path, 3L, "", this.getProjectManagerId().getName(), this.getProjectPartnerId().getName());
            ls_path = copyTemplate("6-A1-5项目组资源和专业胜任能力的调查记录.xlsx", "ManuscriptItemArchive");//4
            mitem.createManuscripItem(custid, "11", ls_path, 4L, "", this.getProjectManagerId().getName(), this.getProjectPartnerId().getName());

            this.setManuscriptItemArchiveState(ProjectEnum.PendingApproval.getIndex());

            List<CompositionCustomer> compositionCustomerss = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId)).toList();
            compositionCustomerss.forEach(c -> {
                c.initialization();
                c.setIsChange(IsChangeEnum.Yes.getIndex());//变化
                c.setRandomCode(Framework.createGuid().toString());
            });
//设置 docstate之前  先将旧值保存起来
            if (Framework.isNotNullOrEmpty(this.getDocState())) {
                this.setDocStateBak(this.getDocState());
            }
            this.setDocState(ProjectEnum.FilingOfManuscripts.getIndex());
            List<CompositionCustomer> compositionCustomers = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId)).toList();

            for (CompositionCustomer f : compositionCustomers) {
                Long ids = f.getId();
                Optional<ManuScirptItemArchive> archive = ManuScirptItemArchive.stream().where(c -> c.getCompostionCustomerId().getId().equals(ids)).findFirst();
                if (!archive.isPresent())
                    throw new BusinessException("该项目下有归档信息没有完成，请查看每一个被审计单位下的档案信息是否完成!");
            }

            for (CompositionCustomer item : compositionCustomers) {
                String[] array = item.getDocStateAllE().split(",");
                if (array.length != 1) {
                    throw new BusinessException("该项目下有被审计单位没有走完底稿的审批流程!");
                } else if (Integer.valueOf(item.getDocStateAllE()) < 70) {
                    throw new BusinessException("该项目下有被审计单位没有走完底稿的审批流程!");
                }
            }
            for (CompositionCustomer item : compositionCustomers) {
                Long comid = item.getId();

                List<Report> reports = Report.stream().where(c -> c.getCompositionCustomerId().getId().equals(comid)).toList();
                if (reports.size() > 0) {
                    for (Report report : reports) {
                        if (report.getDocState() != ProjectEnum.AlreadyStarted.getIndex()) {
                            throw new BusinessException("该项目下有报告没有走完流程!");
                        }
                    }
                }
            }
            this.setDocState(33);
            repository().commit();//归档时 有些数据没有保存到数据库中，暂时加一个手动提交
        } else {
            if (this.getIsMotifyChange() == 1) { //等于1表示  改变的过程中 风险等级发生变化   需要把状态走成修改中
                // this.setDocState(ProjectEnum.Amendment.getIndex());
                this.setIsMotifySubmit(1);
            }
            if (this.getFinancialReportProject() == 10) {
                Integer riskLevel = this.getProcessRiskLevel();
                Long id = this.getId();
                List<CompositionCustomer> customerList = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(id)).toList();
                for (CompositionCustomer item : customerList) {
                    if (Framework.isNullOrEmpty(item.getProjectManager())) {
                        throw new BusinessException("请选择子分公司的项目经理！");
                    }
                    if (Framework.isNullOrEmpty(item.getCustomerType()) || item.getCustomerType() <= 0) {
                        throw new BusinessException("请选择子分公司的客户类型！");
                    }
                    if (Framework.isNullOrEmpty(item.getCustomerType()) || item.getRiskLevel() <= 0) {
                        throw new BusinessException("请选择子分公司的风险级别！");
                    }
                    if (Framework.isNullOrEmpty(item.getManuscriptTemplate()) || item.getManuscriptTemplate() <= 0) {
                        throw new BusinessException("请确认组织架构中底稿模板是否填写！");
                    }
                }

                if (riskLevel == 10 || riskLevel == 20) {
                    Integer firstOrContinuous = this.getFirstOrContinuous();
                    if (firstOrContinuous == 10) {
                        List<BusinessEvaluation> businessEvaluation = BusinessEvaluation.stream().where(w -> w.getProjectId().getId() == id).toList();
                        BusinessEvaluation evaluation = businessEvaluation.get(0);
                        if (Framework.isNullOrEmpty(evaluation.getSubmissionDate()))
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-提交审计报告的日期未填");
                        if (Framework.isNullOrEmpty(evaluation.getAdress()))
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-被审计单位地址未填");
                        if (Framework.isNullOrEmpty(evaluation.getTel()))
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-电话未填");
                        if (Framework.isNullOrEmpty(evaluation.getFax()))
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-传真未填");
                        if (Framework.isNullOrEmpty(evaluation.getEmail()))
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-电子邮箱未填");
                        if (Framework.isNullOrEmpty(evaluation.getUrl()))
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-网址未填");
                        if (Framework.isNullOrEmpty(evaluation.getContact()))
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-联系人未填");
                        if (Framework.isNullOrEmpty(evaluation.getMainBusiness()))
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-业务性质与主要业务未填");
                        if (Framework.isNullOrEmpty(evaluation.getStaffReferral()))
                            //throw new BusinessException("初步业务活动底稿-业务承接评价表-本所职工引荐未填");
                            if (Framework.isNullOrEmpty(evaluation.getExtermalReferral()))
                                //throw new BusinessException("初步业务活动底稿-业务承接评价表-外部人员引荐未填");
                                if (Framework.isNullOrEmpty(evaluation.getOthers()))
                                    throw new BusinessException("初步业务活动底稿-业务承接评价表-其他方式（详细说明）未填");
                        if (Framework.isNullOrEmpty(evaluation.getAuditPurpose()))
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-审计目的未填");
                        if (Framework.isNullOrEmpty(evaluation.getExpectedUser()))
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-审计报告的预期使用者未填");
                        if (Framework.isNullOrEmpty(evaluation.getRiskAssessment()))
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-风险评价未填");
                        if (Framework.isNullOrEmpty(evaluation.getIsAcceptable()))
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-管理层在编制财务报表时采用的财务报告编制基础是否是可以接受的");
                        if (Framework.isNullOrEmpty(evaluation.getIndependCommitment()))
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-独立性声明与保密承诺未填");
                        if (Framework.isNullOrEmpty(evaluation.getEstimatedChargeLower()))
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-预计审计收费未填");
                        if (Framework.isNullOrEmpty(evaluation.getEstimatedCostLower()))
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-预计成本未填");
                        if (Framework.isNotNullOrEmpty(evaluation.getApplicable()) && evaluation.getApplicable() == 20) {
                            if (Framework.isNullOrEmpty(evaluation.getMeetingIndex()))
                                throw new BusinessException("初步业务活动底稿-业务承接评价表-会议纪要索引号未填");
                            if (Framework.isNullOrEmpty(evaluation.getMeetingMinutes()))
                                throw new BusinessException("初步业务活动底稿-业务承接评价表-会议纪要名称未填");
                        }
                        if (Framework.isNullOrEmpty(evaluation.getOtherMatters()))
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-其他事项说明未填");
                        if (Framework.isNullOrEmpty(evaluation.getEvaluationConclusion()))
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-客户保持评价结论未填");

                        Long bEId = evaluation.getId();

                        Long businessSub5 = BusinessSub5.stream().where(w -> w.getBusinessEvaluationId().getId() == bEId).count();
                        if (businessSub5 < 1)
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-5.治理层及管理层关键人员没有数据");

                        if (evaluation.getIsApplicable6()) {
                            Long businessSub6 = BusinessSub6.stream().where(w -> w.getBusinessEvaluationId().getId() == bEId).count();
                            if (businessSub6 < 1)
                                throw new BusinessException("初步业务活动底稿-业务承接评价表-6.实际控制人基本情况没有数据");
                        }

                        if (evaluation.getIsApplicable7()) {
                            Long businessSub7 = BusinessSub7.stream().where(w -> w.getBusinessEvaluationId().getId() == bEId).count();
                            if (businessSub7 < 1)
                                throw new BusinessException("初步业务活动底稿-业务承接评价表-合并范围内子公司及结构化主体基本情况没有数据");
                        }
                        if (evaluation.getIsApplicable8()) {
                            Long businessSub8 = BusinessSub8.stream().where(w -> w.getBusinessEvaluationId().getId() == bEId).count();
                            if (businessSub8 < 1) throw new BusinessException("初步业务活动底稿-业务承接评价表-合营企业基本情况没有数据");
                        }
                        if (evaluation.getIsApplicable9()) {
                            Long businessSub9 = BusinessSub9.stream().where(w -> w.getBusinessEvaluationId().getId() == bEId).count();
                            if (businessSub9 < 1) throw new BusinessException("初步业务活动底稿-业务承接评价表-联营企业基本情况没有数据");
                        }
                        if (evaluation.getIsApplicable10()) {
                            Long businessSub10 = BusinessSub10.stream().where(w -> w.getBusinessEvaluationId().getId() == bEId).count();
                            if (businessSub10 < 1) throw new BusinessException("初步业务活动底稿-业务承接评价表-共同经营情况没有数据");
                        }
                        if (evaluation.getIsApplicable11()) {
                            Long businessSub11 = BusinessSub11.stream().where(w -> w.getBusinessEvaluationId().getId() == bEId).count();
                            if (businessSub11 < 1) throw new BusinessException("初步业务活动底稿-业务承接评价表-分公司基本情况没有数据");
                        }
                        Long businessSub12 = BusinessSub12.stream().where(w -> w.getBusinessEvaluationId().getId() == bEId).count();
                        if (businessSub12 < 1)
                            throw new BusinessException("初步业务活动底稿-业务承接评价表-12.被审计单位主管税务机关没有数据");
                        if (evaluation.getIsApplicable13()) {
                            Long businessSub13 = BusinessSub13.stream().where(w -> w.getBusinessEvaluationId().getId() == bEId).count();
                            if (businessSub13 < 1) throw new BusinessException("初步业务活动底稿-业务承接评价表-被审计单位法律顾问或委托律师没有数据");
                        }

                        if (evaluation.getIsApplicable14()) {
                            Long businessSub14 = BusinessSub14.stream().where(w -> w.getBusinessEvaluationId().getId() == bEId).count();
                            if (businessSub14 < 1)
                                throw new BusinessException("初步业务活动底稿-业务承接评价表-14.被审计单位常年会计或税务顾问没有数据");
                        }
                        if (evaluation.getIsApplicable15()) {
                            Long businessSub15 = BusinessSub.stream().where(w -> w.getBusinessEvaluationId().getId() == bEId).count();
                            if (businessSub15 < 1) throw new BusinessException("初步业务活动底稿-业务承接评价表-15.前任注册会计师(近三年)没有数据");
                        }
                        if (evaluation.getIsApplicable20()) {
                            Long businessSub20 = BusinessSub20.stream().where(w -> w.getBusinessEvaluationId().getId() == bEId).count();
                            if (businessSub20 < 1)
                                throw new BusinessException("初步业务活动底稿-业务承接评价表-20.技术与风险控制委员会会议记录没有数据");
                        }
                    }
                    if (firstOrContinuous == 20) {
                        List<BusinessEvaluation> businessEvaluation = BusinessEvaluation.stream().where(w -> w.getProjectId().getId() == id).toList();
                        for (BusinessEvaluation item : businessEvaluation) {
                            if (Framework.isNullOrEmpty(item.getAdress()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-被审计单位地址未填");
                            if (Framework.isNullOrEmpty(item.getTel()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-电话未填");
                            if (Framework.isNullOrEmpty(item.getFax()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-传真未填");
                            if (Framework.isNullOrEmpty(item.getEmail()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-电子邮箱未填");
                            if (Framework.isNullOrEmpty(item.getUrl()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-网址未填");
                            if (Framework.isNullOrEmpty(item.getContact()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-联系人未填");
                            if (Framework.isNullOrEmpty(item.getMainBusiness()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-业务性质与主要业务未填");
                            if (Framework.isNullOrEmpty(item.getMBChanges()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-被审计单位所有权结构未填");
                            if (Framework.isNullOrEmpty(item.getSubChanges()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-子公司、合营企业、联营企业未填");
                            if (Framework.isNullOrEmpty(item.getAuditPurpose()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-审计目的未填");
                            if (Framework.isNullOrEmpty(item.getExpectedUser()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-审计报告的预期使用者未填");
                            if (Framework.isNullOrEmpty(item.getSubmissionDate()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-提交审计报告的日期未填");
//                            if (Framework.isNullOrEmpty(item.getInvestigationConclusion()))//郭碧原说不用这个属性了
//                                throw new BusinessException("初步业务活动底稿-业务保持评价表-调查结论未填");
                            if (Framework.isNullOrEmpty(item.getRiskAssessment()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-风险评价未填");
                            if (Framework.isNullOrEmpty(item.getIsAcceptable()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-管理层在编制财务报表时采的财务报告编制基础是否可以接受未填");
                            if (Framework.isNullOrEmpty(item.getIndependCommitment()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-独立性声明与保密承诺未填");
//                            if (Framework.isNullOrEmpty(item.getCapabilitySurvey()))//郭碧原说不用这个属性了
//                                throw new BusinessException("初步业务活动底稿-业务保持评价表-项目组资源和专业胜任能力未填");
                            if (Framework.isNullOrEmpty(item.getEstimatedChargeLower()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-预计审计收费未填");
                            if (Framework.isNullOrEmpty(item.getEstimatedCostLower()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-预计成本未填");
                            if (Framework.isNotNullOrEmpty(item.getApplicable()) && item.getApplicable() == 10) {
                                if (Framework.isNullOrEmpty(item.getMeetingMinutes()))
                                    throw new BusinessException("初步业务活动底稿-业务保持评价表-会议纪要名称未填");
                                if (Framework.isNullOrEmpty(item.getMeetingIndex()))
                                    throw new BusinessException("初步业务活动底稿-业务保持评价表-会议纪要索引号未填");
                            }
                            if (Framework.isNullOrEmpty(item.getOtherMatters()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-其他事项说明未填");
                            if (Framework.isNullOrEmpty(item.getEvaluationConclusion()))
                                throw new BusinessException("初步业务活动底稿-业务保持评价表-客户保持评价结论未填");
//                            if (Framework.isNullOrEmpty(item.getExtermalReferral()))
//                                throw new BusinessException("初步业务活动底稿-业务保持评价表-外部人员引荐未填");
//                            if (Framework.isNullOrEmpty(item.getStaffReferral()))
//                                throw new BusinessException("初步业务活动底稿-业务保持评价表-本所职工引荐未填");
                        }
                    }
                    //Long project = PrjAttachment.stream().where(w -> w.getProjectId().getId() == id).count();
                    //     if (project < 1) throw new BusinessException("存值必填项");
                    if (this.getCompetencyConclusion() == null)
                        throw new BusinessException("初步业务活动底稿-项目组资源和专业胜任能力的调查记录未填");
                    if (this.getBusinessConclusion() == null) throw new BusinessException("初步业务活动底稿-被审计单位业务情况调查记录未填");


                }

            }
        }
    }


    //流程提交中
    @Override
    public void onApproveWorkflow(WorkFlowNode node) throws Exception {
        if (!node.getDocColumn().equals("ArchiveState")) {
//            if(this.getIsMotifyChange()==1)  //等于1表示  改变的过程中 风险等级发生变化   需要把状态走成修改中
//                this.setDocState(ProjectEnum.Amendment.getIndex());
        }
    }

    @Override
    public void onWorkFlowBack(WorkFlowNode node) throws Exception {//退回时触发
        if (node.getDocColumn().equals("ArchiveState")) {

            if (Framework.isNotNullOrEmpty(this.getDocStateBak())) {
                this.setDocState(this.getDocStateBak());
            } else {
                this.setDocState(ProjectEnum.InTheIssuanceOfTheReport.getIndex());
            }


            Long projectId = this.getId();
//            this.setIsChange(IsChangeEnum.Yes.getIndex());//变化
//            this.setRandomCode(Framework.createGuid().toString());
//            List<CompositionCustomer> compositionCustomers = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId)).toList();
//            compositionCustomers.forEach(c -> {
//                c.initialization();
//                c.setIsChange(IsChangeEnum.Yes.getIndex());//变化
//                c.setRandomCode(Framework.createGuid().toString());
//            });

            this.setManuscriptItemArchiveState(ProjectEnum.NotSubmitted.getIndex());
            //this.setManuscriptReviewState(ProjectEnum.PendingApproval.getIndex());

            List<ManuScirptItemArchive> manuScirptItemArchives = ManuScirptItemArchive.stream().where(c -> c.getCompostionCustomerId().getProjectId().getId().equals(projectId)).toList();
            Project project = manuScirptItemArchives.get(0).getProject();
            sendNotice(project, "业务底稿归档", String.format("业务归档审批退回:%s", project.getShowName()));
        } else {
            this.setDocState(ProjectEnum.Rejected.getIndex());
            sendNotice(this, "项目立项", String.format("项目立项审批退回:%s", this.getShowName()));
        }
    }

    private String gainPath(String fileName) throws Exception {
        String path1 = Paths.get(DxnConfig.getFileUploadPath(), "2-独立性及保密声明.docx").toString();
        String path = gainAttachmentPath();
        String templateFile = DxnConfig.getFileUploadPath();
        String newFile = "";
        if (Framework.isNotNullOrEmpty(path)) {
            newFile = FileHelper.combine(templateFile, path);
        }
        newFile = FileHelper.combine(newFile, "ManuscriptItemArchive");
        FileHelper.ensureDirtectoryExists(newFile);
        newFile = FileHelper.combine(newFile, fileName);
        return newFile;
    }

    /**
     * 流程结束
     *
     * @param node
     * @throws Exception
     */
    @Override
    public void onEndWorkflow(WorkFlowNode node) throws Exception {
        //维护被审计单位
        // maintainAuditee();
        if (node.getDocColumn().equals("ArchiveState")) {

            Long projectId = this.getId();
            List<ProjectMembers> projectMembersList = ProjectMembers.stream().where(c -> c.getProjectId().getId() == projectId && c.getIsEnable() == 10).toList();
            String path = Paths.get(DxnConfig.getFileUploadPath(), "2-独立性及保密声明.docx").toString();
            String newPath = gainPath("2-独立性及保密声明.docx");
            // String newPath = Paths.get(DxnConfig.getFileUploadPath(), "Archive", "2-独立性及保密声明.docx").toString();
            writeDoc(projectMembersList, path, newPath);
            this.setDocState(ProjectEnum.Filed.getIndex());
            this.setManuscriptItemArchiveState(ProjectEnum.AlreadyStarted.getIndex());

            List<CompositionCustomer> compositionCustomers = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId)).toList();
            compositionCustomers.forEach(c -> {
                c.initialization();
                c.setIsChange(IsChangeEnum.Yes.getIndex());//变化
                c.setRandomCode(Framework.createGuid().toString());
            });
            int boxs = 0;
            int books = 0;
            List<ManuScirptItemArchive> manuScirptItemArchives = ManuScirptItemArchive.stream().where(c -> c.getCompostionCustomerId().getProjectId().getId().equals(projectId)).toList();
            if (manuScirptItemArchives.size() > 0) {
                for (ManuScirptItemArchive item : manuScirptItemArchives) {
                    books += item.getBookNumber();
                    boxs += item.getBoxNumber();
                }
            }
            this.setBookNumber(books);
            this.setBoxNumber(boxs);
            Project project = manuScirptItemArchives.get(0).getProject();
            //获取审批记录第一条时间 和最后一条时间   需求提的
            List<MyTodoListDTO> workToDo = getWorkFlowTime(this.getId());
            String startTime = workToDo.get(0).getCreated_Time().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String endTime = workToDo.get(workToDo.size() - 1).getCreated_Time().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            sendNotice(project, "业务底稿归档", String.format("业务归档审批通过:%s", project.getShowName()));
            if (this.getFirstOrContinuous() == 20) {
                BusinessEvaluation("2-A1业务保持评价表.xlsx", startTime, endTime);
                ProjectPersonnelSituation("7-A3项目组构成人员情况表.xlsx", startTime);
                if (Framework.isNotNullOrEmpty(this.getProgramSheetPath()))
                    BusinessSurveyRecord("1-A初步业务活动程序表-连续审计.xlsx", this.getBusinessConclusionPath(), 8, startTime, endTime, 1L);
            } else {
                BusinessEvaluation("2-A1业务承接评价表.xlsx", startTime, endTime);
                ProjectPersonnelSituation("7-A3项目组构成人员审批表.xlsx", startTime);
                if (Framework.isNotNullOrEmpty(this.getProgramSheetPath()))
                    BusinessSurveyRecord("1-A初步业务活动程序表-首次承接.xlsx", this.getBusinessConclusionPath(), 8, startTime, endTime, 1L);
            }
            if (Framework.isNotNullOrEmpty(this.getBusinessConclusionPath()))
                BusinessSurveyRecord("3-A1-1被审计单位业务情况调查记录.xlsx", this.getBusinessConclusionPath(), 5, startTime, endTime, 3L);
            if (Framework.isNotNullOrEmpty(this.getCompetencyConclusionPath()))
                BusinessSurveyRecord("6-A1-5项目组资源和专业胜任能力的调查记录.xlsx", this.getCompetencyConclusionPath(), 5, startTime, endTime, 4L);

            clearManuscriptData(projectId);
            //归档后 像变更表插入一条数据
            CompositionCustomer customer = CompositionCustomer.createNew();
            // customer.comterModifyType(Framework.createGuid().toString(),20);

            for (CompositionCustomer item : compositionCustomers) {
                item.createManuscriptCautalog("审计工作底稿当期档案目录.xlsx");
            }


        } else {
            if (this.getIsMotifyChange() == 1) {
                if (Framework.isNotNullOrEmpty(this.getOriginalState())) {
                    this.setDocState(this.getOriginalState());
                    this.setIsMotifyChange(0);
                    this.setOriginalState(0);
                    this.setIsMotifySubmit(0);
                }
            }
            this.setProjectStatus(ProjectCommon.formalProject.getIndex());
            sendNotice(this, "项目立项", String.format("项目立项审批通过:%s", this.getShowName()));
        }
        addtoWorkExperience();
    }

    private List<MyTodoListDTO> getWorkFlowTime(Long Id) throws Exception {

        QueryModel model = new QueryModel();
        Map<String, Object> paraList = new HashedMap();
        paraList.put("Id", Id);
        paraList.put("entityType", "Project");
        paraList.put("processId", "");
        model.setParaList(paraList);
        WorkflowService work = new WorkflowService();
        List<MyTodoListDTO> listdtos = new ArrayList<>();
        UIPagedList pagedList = work.getApprovalRecord(model, "Project", Id, null, "", null);
        pagedList.getRows().forEach(c -> {
            listdtos.add((MyTodoListDTO) c);
        });
        Collections.sort(listdtos, new Comparator<MyTodoListDTO>() {

            @Override
            public int compare(MyTodoListDTO o1, MyTodoListDTO o2) {
                // 按照学生的年龄进行升序排列
                if (o1.getCreated_Time().toString().compareTo(o2.getCreated_Time().toString()) > 0) {
                    return 1;
                }

                return -1;
            }
        });
        return listdtos;
    }

    private void clearManuscriptData(Long projectId) throws Exception {
        ManuscriptItem manuscriptItem = ManuscriptItem.createNew();
        List<CompositionCustomer> compostionCustomerArray = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId)).toList();
        for (CompositionCustomer item : compostionCustomerArray) {
            String compostionCustomerId = item.getUuid();
            List<ManuscriptItem> manuscriptList = ManuscriptItem.stream().where(c -> c.getAuditworkProjectID().equals(compostionCustomerId) && c.getModifyStatus() == 30).toList();

            for (ManuscriptItem manuscript : manuscriptList) {
                manuscript.initialization();
                this.repository().remove(manuscript);
            }

            String str = Paths.get(DxnConfig.getCsfileUploadPath(), manuscriptItem.csUploadPath(item.getCode(), 20)).toString();
            ExcelUtils.DeleteFolder(str);
        }

    }

    private void sendNotice(Project project, String title, String conetnt) throws Exception {
        Notice notice = Notice.createNew();
        List<Long> users = new ArrayList<>();
        users.add(project.getCreatedById());
        String entityType = title.equals("业务底稿归档") ? "ManuscriptItemArchives" : "Project";
        if (entityType.equals("Project") && project.getFinancialReportProject() == 20) {
            entityType = "NonFinancialQuotation";
        }
        notice.createNotice(AppContext.current().getProfile().getId(), users, title, conetnt, project.getId(), entityType, null);
    }

    //维护被审计单位
    private void maintainAuditee() throws Exception {
        Long projectId = this.getId();
        List<CompositionCustomer> compositionCustomerList = CompositionCustomer.stream().where(w -> w.getProjectId().getId().equals(projectId) && w.getDocState().equals(null)).toList();
        for (CompositionCustomer item : compositionCustomerList) {
            item.setDocState(30);
            repository().add(item);
        }

    }

    @JsonIgnore
    public MyTodoListDTO gainWorkFlowDTO(MyTodoListDTO value) throws Exception {
        value.setDepartmentType(this.getCode());
        User user = this.getCreatedBy();
        if (Framework.isNotNullOrEmpty(user)) {
            if (Framework.isNotNullOrEmpty(user.getDeptReadOnly())) {
                value.setDepartment(user.getDeptReadOnly().getTreeName());
            }
        }
        String processId = value.getProcess_Id();
        Optional<WorkFlowConfig> workFlowConfigList = WorkFlowConfig.stream().where(w -> w.getProcessId().equals(processId)).findFirst();
        if (workFlowConfigList.isPresent()) {
            WorkFlowConfig workFlowConfig = workFlowConfigList.get();
            String deptIdName = user.getDeptReadOnly().getOrgReadOnly().getNickname();
            String workFlowConfigName = workFlowConfig.getWorkFlowTypeId().getName();
            value.setWorkFlowName(deptIdName + "-" + workFlowConfigName);

        }
        if (Framework.isNotNullOrEmpty(this.getFinancialReportProject()) && this.getFinancialReportProject() == 20 && !value.getProcess_Id().equals("归档流程")) {
            value.setUiFormCode("NonReportProjectManage");
        }
        return value;
    }

    @SystemService(args = "userId")
    public ResponseEntity getPath(String userId) throws Exception {
        ResponseEntity response = new ResponseEntity();
        try {
            ManuscriptItem manuscriptItem = ManuscriptItem.createNew();
            String path = manuscriptItem.gainAttachmentPath();
            List<BackupInfoDTO> listStr = new ArrayList<>();
            List<CompositionMembers> memberList = CompositionMembers.stream().where(c -> c.getNameId().getUuid().equals(userId)).toList();
            for (CompositionMembers item : memberList) {
                CompositionCustomer model = item.getCompositionCustomer();
                String str = Paths.get(DxnConfig.getCsfileUploadPath(), path, model.getCode(), "ManuscriptItem", "Original").toString();
                if (FileHelper.isExists(str)) {
                    BackupInfoDTO dto = new BackupInfoDTO();
                    dto.setBackupPath(Paths.get(path, model.getCode(), "ManuscriptItem", "Original").toString());
                    dto.setProjectName(model.getName());
                    dto.setProjectShowName(model.getShowName());
                    dto.setAuditProjectId(model.getUuid());
                    dto.setProjectManagerId(model.getProjectId().getUuid());
                    listStr.add(dto);
                }
            }
            response.setCode(200);
            response.setData(listStr);

        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages(Framework.getMes(ex));
        }

        return response;
    }

    /**
     * 获取我的项目 项目申请完成 未归档 项目组成员包含当前登录人的项目
     *
     * @param formId
     * @return
     */
    @SystemService(args = "formId")
    public UIPagedList gainMyProject(Long formId) throws Exception {
        Long userid = AppContext.current().getProfile().getId();
        List<Object> p = new ArrayList<>();
        p.add(userid);

        List<Project> projects = repository().sqlQuery("select * from prj_project  \n" +
                "where docstate =30 and id in ( select projectid from Prj_ProjectMembers where NameId =? )", p, Project.class);

        List<Object> list = new ArrayList<>();
        projects.forEach(f -> {
            f.initialization();
            list.add(f);
        });
        return UIPagedList.band(list, FormPage.getFormColumn(formId));
    }

    public String getQualificationUser(User user) throws Exception {
        Long staffId = user.getStaffId();
        Optional<Staff> staffListf = Staff.stream().where(c -> c.getId() == staffId).findFirst();
        if (staffListf.isPresent()) {
            Staff staff = staffListf.get();
            return staff.getPractisingQualification();
        }
        return "";
    }

    /**
     * 校验复核是否都完成  其中包含 底稿、报告、约定书、报备、用印审批
     *
     * @param projectId
     * @throws Exception
     */
    @SystemService(args = "projectId")
    public void checkReviewIsComplete(Long projectId) throws Exception {
        long docState = 0L;
        StringBuilder stringBuilder = new StringBuilder();
        docState = CompositionCustomer.stream().where(w -> w.getProjectId().getId() == projectId &&
                (w.getDocStateAllE().contains("30") || w.getDocStateAllE().contains("40") || w.getDocStateAllE().contains("50") || w.getDocStateAllE().contains("60"))).count();
        if (docState > 0) {
//          原提示信息
//            stringBuilder.append("该项目下有<被审计单位>底稿处于复核中，不能进行申请修改操作,请先完成相对应的流程;\r\n");
//            2019-12-04需求修改
            stringBuilder.append("该项目的底稿在复核中，不能进行项目提示信息申请修改操作;\r\n");
            //throw new BusinessException("该项目下有<被审计单位>底稿处于复核中，不能进行申请修改操作,请先完成相对应的流程!");
        }
        docState = Report.stream().where(w -> w.getProjectId().getId() == projectId && w.getDocState() == 20).count();
        if (docState > 0) {
            stringBuilder.append("该项目下有<报告>处于复核中，不能进行申请修改操作,请先完成相对应的流程;\r\n");
            //throw new BusinessException("该项目下有<报告>处于复核中，不能进行申请修改操作,请先完成相对应的流程!");
        }

//        docState = Engagement.stream().where(w -> w.getProjectId().getId() == projectId && w.getDocState() == 20).count();
//        if (docState > 0) {
//            stringBuilder.append("该项目下有<业务约定书>处于复核中，不能进行申请修改操作,请先完成相对应的流程;\r\n");
//            //throw new BusinessException("该项目下有<业务约定书>处于复核中，不能进行申请修改操作,请先完成相对应的流程!");
//        }
        docState = Report.stream().where(w -> w.getProjectId().getId() == projectId && w.getApplySignStatus() == 20).count();
        if (docState > 0) {
            stringBuilder.append("该项目下有<报告用印>处于复核中，不能进行申请修改操作,请先完成相对应的流程;\r\n");
            //throw new BusinessException("该项目下有<报告用印>处于复核中，不能进行申请修改操作,请先完成相对应的流程!");
        }
        docState = Report.stream().where(w -> w.getProjectId().getId() == projectId && w.getReportingStatus() == 20).count();
        if (docState > 0) {
            stringBuilder.append("该项目下有<报备>处于复核中，不能进行申请修改操作,请先完成相对应的流程;\r\n");
            //throw new BusinessException("该项目下有<报备>处于复核中，不能进行申请修改操作,请先完成相对应的流程!");
        }
        if (stringBuilder.length() > 0) {
            throw new BusinessException(stringBuilder.toString());
        }
    }

    public void applyMotifySpecificOperation(Long projectId) throws Exception {
        List<Report> reports = Report.stream().where(c -> c.getCompositionCustomerId().getProjectId().getId().equals(projectId)).toList();
        for (Report item : reports) {
            Report report = Report.findById(item.getId());
            if (Framework.isNotNullOrEmpty(report)) {
                report.setDocState(10);
                report.setReportingStatus(5);
                report.setRegistFormId(null);
                Long reportid = item.getId();
                Optional<RegistForm> registForm = RegistForm.stream().where(c -> c.getReportId() != null && c.getReportId().getId().equals(reportid)).findFirst();
                if (registForm.isPresent())
                    repository().remove(registForm.get());
            }
        }
    }

    @SystemService()
    public void tt4() throws Exception {
        List<MyTodoListDTO> workToDo = getWorkFlowTime(1L);
        String startTime = workToDo.get(0).getCreated_Time().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String endTime = workToDo.get(workToDo.size() - 1).getCreated_Time().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        BusinessEvaluation("2-A1业务承接评价表.xlsx", startTime, endTime);
        // ProjectPersonnelSituation("7-A3项目组构成人员情况表.xlsx", startTime);
        //getWorkFlowTime(1L);
        // String newPath = gainPath("2-独立性及保密声明.docx");
    }

    //业务保持评价表
    public void BusinessEvaluation(String FileName, String startTime, String endTime) throws Exception {
        //获取模板文件路径和刷底稿路径
        String path = gainAttachmentPath();
        String templateFile = DxnConfig.getFileUploadPath();
        String newFile = "";
        if (Framework.isNotNullOrEmpty(path)) {
            newFile = FileHelper.combine(templateFile, path);
        }
        newFile = FileHelper.combine(newFile, "ManuscriptItemArchive");
        FileHelper.ensureDirtectoryExists(newFile);
        newFile = FileHelper.combine(newFile, FileName);
        templateFile = FileHelper.combine(templateFile, FileName);
        //获取要刷的底稿数据
        BusinessEvaluationDTO dto = new BusinessEvaluationDTO();
        Long id = this.getId();

        CompositionCustomer customer = new CompositionCustomer();
        CompositionCustomerDTOE cust = customer.getCompositionCustomerByPrjId(this.getId());
        Long custid = cust.getId();
        String ls_path = copyTemplate(FileName, "ManuscriptItemArchive");//2.,"",""
        String indecCode = gainManuscripItemIndexNumber(custid, "11", ls_path, 2L, "");
        Optional<BusinessEvaluation> businessEvaluation = BusinessEvaluation.stream().where(b -> b.getProjectId().getId() == id).findFirst();
        if (businessEvaluation.isPresent()) {
            dto.setCompositionCustomerName(businessEvaluation.get().getProjectId().getName());
            dto.setIndexNumber(indecCode);
            if (Framework.isNotNullOrEmpty(businessEvaluation.get().getSubmissionDate()))
                dto.setAuditDate(businessEvaluation.get().getSubmissionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            else
                dto.setAuditDate("");
            dto.setPageNumber(1);
            dto.setCompilingPersonSignName(businessEvaluation.get().getProjectId().getProjectManagerId().getName());
            dto.setCompilingPersonSignTime(startTime);
            dto.setReviewerName(businessEvaluation.get().getProjectId().getProjectPartnerId().getName());
            dto.setReviewerTime(endTime);
            dto.setProjectName(businessEvaluation.get().getProjectId().getName());
            dto.setProjectAdress(businessEvaluation.get().getAdress());
            dto.setTel(businessEvaluation.get().getTel());
            dto.setUrl(businessEvaluation.get().getUrl());
            dto.setEmail(businessEvaluation.get().getEmail());
            dto.setFax(businessEvaluation.get().getFax());
            dto.setContact(businessEvaluation.get().getContact());
            dto.setCustomerTypeValue(businessEvaluation.get().getProjectId().getCustomerTypeValue());
            if (Framework.isNotNullOrEmpty(businessEvaluation.get().getProjectId().getCustomerNameId().getIndustryType())) {
                EnumValue enumValue = EnumValue.createNew();
                EnumDataDTO enumData = enumValue.gainEnumDataDTO("Industry", businessEvaluation.get().getProjectId().getCustomerNameId().getIndustryType());
                dto.setIndustryType(enumData.getText());
            }
            dto.setConclusion(businessEvaluation.get().getProjectId().getBusinessConclusion());
            dto.setMainBusiness(businessEvaluation.get().getMainBusiness());
            dto.setMBChanges(businessEvaluation.get().getMBChanges());
            dto.setSubChanges(businessEvaluation.get().getSubChanges());
            dto.setAuditPurpose(businessEvaluation.get().getAuditPurpose());
            dto.setExpectedUser(businessEvaluation.get().getExpectedUser());
            dto.setBusinessConclusion(businessEvaluation.get().getProjectId().getBusinessConclusion());
            if (Framework.isNotNullOrEmpty(businessEvaluation.get().getSubmissionDate()))
                dto.setSubmissionDate(businessEvaluation.get().getSubmissionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            else
                dto.setSubmissionDate("");
            dto.setInvestigationConclusion(businessEvaluation.get().getInvestigationConclusion());
            if (Framework.isNotNullOrEmpty(businessEvaluation.get().getRiskAssessment())) {
                EnumValue enumValue = EnumValue.createNew();
                EnumDataDTO enumData = enumValue.gainEnumDataDTO("RiskAssessmentType", businessEvaluation.get().getRiskAssessment());
                dto.setRiskAssessment(enumData.getText());
            }
            if (Framework.isNotNullOrEmpty(businessEvaluation.get().getIsAcceptable())) {
                EnumValue enumValue = EnumValue.createNew();
                EnumDataDTO enumData = enumValue.gainEnumDataDTO("YesOrNo", businessEvaluation.get().getIsAcceptable());
                dto.setIsAcceptable(enumData.getText());
            }
            dto.setIndependCommitment(businessEvaluation.get().getIndependCommitment());
            dto.setCapabilitySurvey(businessEvaluation.get().getCapabilitySurvey());
            dto.setEstimatedChargeLower(businessEvaluation.get().getEstimatedChargeLower());
            dto.setEstimatedCostLower(businessEvaluation.get().getEstimatedCostLower());

            //3.业务引荐途径（详细说明） 界面显示与EXCEL底稿显示不同，特殊处理
            //根据界面选择的引荐途径 显示引荐说明
            if (Framework.isNotNullOrEmpty(businessEvaluation.get().getReferralChannel())) {
                if (businessEvaluation.get().getReferralChannel() == 10) //本所职工引荐
                {
                    dto.setStaffReferral(businessEvaluation.get().getOthers());
                } else if (businessEvaluation.get().getReferralChannel() == 20)//外部人员引荐
                {
                    dto.setExtermalReferral(businessEvaluation.get().getOthers());
                } else //其他方式（详细说明）
                {
                    dto.setOthers(businessEvaluation.get().getOthers());
                }
            } else //其他方式（详细说明）
            {
                dto.setOthers(businessEvaluation.get().getOthers());
            }


            //  dto.setExtermalReferral(businessEvaluation.get().getExtermalReferral());
            //  dto.setOthers(businessEvaluation.get().getOthers());
            dto.setBusinessConclusion(this.getBusinessConclusion());
            dto.setCompetencyConclusion(this.getCompetencyConclusion());
            if (Framework.isNotNullOrEmpty(businessEvaluation.get().getEstimatedChargeLower())) {
                dto.setDEstimatedChargeLower(Framework.toHantr(businessEvaluation.get().getEstimatedChargeLower()));
            }
            if (Framework.isNotNullOrEmpty(businessEvaluation.get().getEstimatedCostLower())) {
                dto.setDEstimatedCostLower(Framework.toHantr(businessEvaluation.get().getEstimatedCostLower()));
            }


            List<MinutesMeetDTO> MinutesMeetList = new ArrayList<>();
            Long businessEvaluationid = businessEvaluation.get().getId();
            List<BusinessSub20> businessSub20List = BusinessSub20.stream().where(b -> b.getBusinessEvaluationId().getId() == businessEvaluationid).toList();
            for (BusinessSub20 item : businessSub20List) {
                MinutesMeetDTO minutesMeet = new MinutesMeetDTO();
                //minutesMeet.setApplicable(item.getApplicable20() == false ? "否" : "是");
                minutesMeet.setCode(item.getCode());
                minutesMeet.setName(item.getName());
                MinutesMeetList.add(minutesMeet);
            }
            dto.setMinutesMeet(MinutesMeetList);
            if (businessSub20List.size() > 0) {
                if (Framework.isNotNullOrEmpty(businessSub20List) && Framework.isNotNullOrEmpty(businessSub20List.get(0))) {
                    dto.setApplicable20(Framework.isNullOrEmpty(businessSub20List.get(0).getApplicable20()) || businessSub20List.get(0).getApplicable20() == false ? "否" : "是");
                }
            }

            List<BusinessSub5DTO> BusinessSub5List = new ArrayList<>();
            List<BusinessSub5> businessSub5List = BusinessSub5.stream().where(b -> b.getBusinessEvaluationId().getId() == businessEvaluationid).toList();
            for (BusinessSub5 item : businessSub5List) {
                BusinessSub5DTO businessSub5 = new BusinessSub5DTO();
                businessSub5.setString1(item.getName());
                businessSub5.setString2(item.getAddress());
                businessSub5.setString3(item.getContactInfo());
                businessSub5.setString4(item.getRemark());
                BusinessSub5List.add(businessSub5);
            }
            dto.setBusinessSub5(BusinessSub5List);
            List<BusinessSub5DTO> BusinessSub6List = new ArrayList<>();
            List<BusinessSub6> businessSub6List = BusinessSub6.stream().where(b -> b.getBusinessEvaluationId().getId() == businessEvaluationid).toList();
            for (BusinessSub6 item : businessSub6List) {
                BusinessSub5DTO businessSub6 = new BusinessSub5DTO();
                businessSub6.setString1(item.getName());
                businessSub6.setString2(item.getAddress());
                businessSub6.setString3(item.getContactInfo());
                if (Framework.isNotNullOrEmpty(item.getHoldRatio())) {
                    businessSub6.setString4(item.getHoldRatio().toString());
                }
                BusinessSub6List.add(businessSub6);
            }
            dto.setBusinessSub6(BusinessSub6List);
            List<BusinessSub5DTO> BusinessSub7List = new ArrayList<>();
            List<BusinessSub7> businessSub7List = BusinessSub7.stream().where(b -> b.getBusinessEvaluationId().getId() == businessEvaluationid).toList();
            for (BusinessSub7 item : businessSub7List) {
                BusinessSub5DTO businessSub7 = new BusinessSub5DTO();
                businessSub7.setString1(item.getName());
                businessSub7.setString2(item.getAddress());
                businessSub7.setString3(item.getContactInfo());
                if (Framework.isNotNullOrEmpty(item.getHoldRatio())) {
                    businessSub7.setString4(item.getHoldRatio().toString());
                }
                BusinessSub7List.add(businessSub7);
            }
            dto.setBusinessSub7(BusinessSub7List);
            List<BusinessSub5DTO> BusinessSub8List = new ArrayList<>();
            List<BusinessSub8> businessSub8List = BusinessSub8.stream().where(b -> b.getBusinessEvaluationId().getId() == businessEvaluationid).toList();
            for (BusinessSub8 item : businessSub8List) {
                BusinessSub5DTO businessSub8 = new BusinessSub5DTO();
                businessSub8.setString1(item.getName());
                businessSub8.setString2(item.getAddress());
                businessSub8.setString3(item.getContactInfo());
                if (Framework.isNotNullOrEmpty(item.getHoldRatio())) {
                    businessSub8.setString4(item.getHoldRatio().toString());
                }
                BusinessSub8List.add(businessSub8);
            }
            dto.setBusinessSub8(BusinessSub8List);
            List<BusinessSub5DTO> BusinessSub9List = new ArrayList<>();
            List<BusinessSub9> businessSub9List = BusinessSub9.stream().where(b -> b.getBusinessEvaluationId().getId() == businessEvaluationid).toList();
            for (BusinessSub9 item : businessSub9List) {
                BusinessSub5DTO businessSub9 = new BusinessSub5DTO();
                businessSub9.setString1(item.getName());
                businessSub9.setString2(item.getAddress());
                businessSub9.setString3(item.getContactInfo());
                if (Framework.isNotNullOrEmpty(item.getHoldRatio())) {
                    businessSub9.setString4(item.getHoldRatio().toString());
                }
                BusinessSub9List.add(businessSub9);
            }
            dto.setBusinessSub9(BusinessSub9List);
            List<BusinessSub5DTO> BusinessSub10List = new ArrayList<>();
            List<BusinessSub10> businessSub10List = BusinessSub10.stream().where(b -> b.getBusinessEvaluationId().getId() == businessEvaluationid).toList();
            for (BusinessSub10 item : businessSub10List) {
                BusinessSub5DTO businessSub10 = new BusinessSub5DTO();
                businessSub10.setString1(item.getName());
                businessSub10.setString2(item.getAddress());
                businessSub10.setString3(item.getContactInfo());
                businessSub10.setString4(item.getRemark());
                BusinessSub10List.add(businessSub10);
            }
            dto.setBusinessSub10(BusinessSub10List);
            List<BusinessSub5DTO> BusinessSub11List = new ArrayList<>();
            List<BusinessSub11> businessSub11List = BusinessSub11.stream().where(b -> b.getBusinessEvaluationId().getId() == businessEvaluationid).toList();
            for (BusinessSub11 item : businessSub11List) {
                BusinessSub5DTO businessSub11 = new BusinessSub5DTO();
                businessSub11.setString1(item.getName());
                businessSub11.setString2(item.getAddress());
                businessSub11.setString3(item.getContactInfo());
                BusinessSub11List.add(businessSub11);
            }
            dto.setBusinessSub11(BusinessSub11List);

            List<BusinessSub5DTO> BusinessSub12List = new ArrayList<>();
            List<BusinessSub12> businessSub12List = BusinessSub12.stream().where(b -> b.getBusinessEvaluationId().getId() == businessEvaluationid).toList();
            for (BusinessSub12 item : businessSub12List) {
                BusinessSub5DTO businessSub12 = new BusinessSub5DTO();
                businessSub12.setString1(item.getName());
                businessSub12.setString2(item.getAddress());
                businessSub12.setString3(item.getContactInfo());
                businessSub12.setString4(item.getRemark());
                BusinessSub12List.add(businessSub12);
            }
            dto.setBusinessSub12(BusinessSub12List);

            List<BusinessSub5DTO> BusinessSub13List = new ArrayList<>();
            List<BusinessSub13> businessSub13List = BusinessSub13.stream().where(b -> b.getBusinessEvaluationId().getId() == businessEvaluationid).toList();
            for (BusinessSub13 item : businessSub13List) {
                BusinessSub5DTO businessSub13 = new BusinessSub5DTO();
                businessSub13.setString1(item.getName());
                businessSub13.setString2(item.getAddress());
                businessSub13.setString3(item.getContactInfo());
                businessSub13.setString4(item.getRemark());
                BusinessSub13List.add(businessSub13);
            }
            dto.setBusinessSub13(BusinessSub13List);

            List<BusinessSub5DTO> BusinessSub14List = new ArrayList<>();
            List<BusinessSub14> businessSub14List = BusinessSub14.stream().where(b -> b.getBusinessEvaluationId().getId() == businessEvaluationid).toList();
            for (BusinessSub14 item : businessSub14List) {
                BusinessSub5DTO businessSub14 = new BusinessSub5DTO();
                businessSub14.setString1(item.getName());
                businessSub14.setString2(item.getAddress());
                businessSub14.setString3(item.getContactInfo());
                businessSub14.setString4(item.getRemark());
                BusinessSub14List.add(businessSub14);
            }
            dto.setBusinessSub14(BusinessSub14List);

            List<BusinessSub5DTO> BusinessSub15List = new ArrayList<>();
            List<BusinessSub> businessSub15List = BusinessSub.stream().where(b -> b.getBusinessEvaluationId().getId() == businessEvaluationid).toList();
            for (BusinessSub item : businessSub15List) {
                BusinessSub5DTO businessSub15 = new BusinessSub5DTO();
                businessSub15.setString1(item.getName());
                businessSub15.setString2(item.getAddress());
                businessSub15.setString3(item.getContactInfo());
                businessSub15.setString4(item.getRemark());
                BusinessSub15List.add(businessSub15);
            }
            dto.setBusinessSub15(BusinessSub15List);


            ExcelUtils.writeXLSX(templateFile, newFile, dto);
        }
    }

    //项目人员情况表
    public void ProjectPersonnelSituation(String FileName, String startTime) throws Exception {
        //获取模板文件路径和刷底稿路径
        String path = gainAttachmentPath();
        String templateFile = DxnConfig.getFileUploadPath();
        String newFile = "";
        if (Framework.isNotNullOrEmpty(path)) {
            newFile = FileHelper.combine(templateFile, path);
        }
        newFile = FileHelper.combine(newFile, "ManuscriptItemArchive");
        FileHelper.ensureDirtectoryExists(newFile);
        newFile = FileHelper.combine(newFile, FileName);
        templateFile = FileHelper.combine(templateFile, FileName);
        //
        ProjectPersonnelSituationDTO ProjectPersonnelSituation = new ProjectPersonnelSituationDTO();
        List<ProjectMemberDTO> MinutesMeetList = new ArrayList<>();
        Long id = this.getId();
        CompositionCustomer customer = new CompositionCustomer();
        CompositionCustomerDTOE cust = customer.getCompositionCustomerByPrjId(this.getId());
        Long custid = cust.getId();
        String ls_path = copyTemplate(FileName, "ManuscriptItemArchive");//2.,"",""
        String indecCode = gainManuscripItemIndexNumber(custid, "11", ls_path, 5L, "");


        List<ProjectMembers> businessEvaluationList = ProjectMembers.stream().where(b -> b.getProjectId().getId() == id).toList();
        ProjectPersonnelSituation.setCompositionCustomerName(businessEvaluationList.get(0).getProjectId().getProjectManagerId().getName());
        ProjectPersonnelSituation.setIndexNumber(indecCode);
        ProjectPersonnelSituation.setAuditDate(startTime);
        ProjectPersonnelSituation.setPageNumber(1);
        if (businessEvaluationList.size() > 0) {
            for (ProjectMembers item : businessEvaluationList) {
                ProjectMemberDTO projectMember = new ProjectMemberDTO();
                projectMember.setUserName(item.getNameId().getName());
                projectMember.setRank(item.getRank());
                projectMember.setPQualifications(item.getPQualifications());
                if (Framework.isNotNullOrEmpty(item.getIsExperience())) {
                    EnumValue enumValue = EnumValue.createNew();
                    EnumDataDTO enumData = enumValue.gainEnumDataDTO("YesOrNo", item.getIsExperience());
                    projectMember.setIsExperience(enumData.getText());
                }
                if (Framework.isNotNullOrEmpty(item.getIsIndependence())) {
                    EnumValue enumValue = EnumValue.createNew();
                    EnumDataDTO enumData = enumValue.gainEnumDataDTO("YesOrNo", item.getIsIndependence());
                    projectMember.setIsIndependence(enumData.getText());
                }
                projectMember.setWorkingYears(item.getWorkingYears());
                projectMember.setRemark(item.getRemark());
                MinutesMeetList.add(projectMember);
            }
            ProjectPersonnelSituation.setProjectMemberList(MinutesMeetList);
        }
        ExcelUtils.writeXLSX(templateFile, newFile, ProjectPersonnelSituation);
    }


    //业务情况调查记录
    public void BusinessSurveyRecord(String FileName, String TypePath, Integer RowNum, String startTime, String endTime, Long num) throws Exception {
        //获取模板文件路径和刷底稿路径
        String path = gainAttachmentPath();
        String templateFile = DxnConfig.getFileUploadPath();
        String newFile = "";
        if (Framework.isNotNullOrEmpty(path)) {
            newFile = FileHelper.combine(templateFile, path);
        }
        newFile = FileHelper.combine(newFile, "ManuscriptItemArchive");
        if (Framework.isNotNullOrEmpty(TypePath)) {
            TypePath = FileHelper.combine(templateFile, TypePath);
            FileHelper.ensureDirtectoryExists(newFile);
            newFile = FileHelper.combine(newFile, FileName);
            templateFile = FileHelper.combine(templateFile, FileName);

            CompositionCustomer customer = new CompositionCustomer();
            CompositionCustomerDTOE cust = customer.getCompositionCustomerByPrjId(this.getId());
            Long custid = cust.getId();
            String ls_path = copyTemplate(FileName, "ManuscriptItemArchive");//2.,"",""
            String indecCode = gainManuscripItemIndexNumber(custid, "11", ls_path, num, "");

            BusinessSurveyRecordDTO businessSurveyRecord = new BusinessSurveyRecordDTO();
            businessSurveyRecord.setCompositionCustomerName(this.getName());
            businessSurveyRecord.setIndexNumber(indecCode);
            businessSurveyRecord.setAuditDate(this.getAuditEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            businessSurveyRecord.setPageNumber(1);
            //编制人 取 大项目经理  //2020 、4/5  张敬研提出
            businessSurveyRecord.setCompilingPersonSignName(this.getProjectManagerId().getName());
            businessSurveyRecord.setCompilingPersonSignTime(startTime);
            //复核人 取 项目合伙人  //2020 、4/5  张敬研提出
            businessSurveyRecord.setReviewerName(this.getProjectPartnerId().getName());
            businessSurveyRecord.setReviewerTime(endTime);
            businessSurveyRecord.setBusinessConclusion(this.getBusinessConclusion());
            businessSurveyRecord.setCompetencyConclusion(this.getCompetencyConclusion());

            ExcelUtils.writeXLSX(templateFile, newFile, TypePath, RowNum, businessSurveyRecord);
        }
    }


    public void putreviewOpinionsData(String ProjectCode, Integer TypeValue, List<Long> ids) throws Exception {
        List<ReviewComments> reviewCommentsList = ReviewComments.stream().where(c -> c.getCompositionCustomerId().getCode().equals(ProjectCode) && c.getManuscriptReviewState() == TypeValue).toList();
        for (ReviewComments item : reviewCommentsList) {
            if (ids.contains(item.getId()))
                item.setIsSelection(true);
            else
                item.setIsSelection(false);

        }

    }

    //项目质量控制复核反馈意见及回复
    @SystemService(args = "FileName,ProjectCode,ReviewCommentZDList,ReviewCommentBGList,ReviewCommentDGList")
    public void CreateReviewOpinion(String FileName, String ProjectCode, List<Long> ReviewCommentZDList, List<Long> ReviewCommentBGList, List<Long> ReviewCommentDGList) throws Exception {
        List<Long> ReviewCommentid = new ArrayList<>();

        //获取模板文件路径和刷底稿路径
        String path = gainAttachmentPath();
        path = path.replace("null", ProjectCode);
        String templateFile = DxnConfig.getFileUploadPath();
        String newFile = "";
        if (Framework.isNotNullOrEmpty(path)) {
            newFile = FileHelper.combine(templateFile, path);
        }
        newFile = FileHelper.combine(newFile, "ManuscriptItemArchive");
        FileHelper.ensureDirtectoryExists(newFile);
        newFile = FileHelper.combine(newFile, FileName);
        templateFile = FileHelper.combine(templateFile, FileName);
        HashMap<Integer, List<ReviewOpinionsDataDTO>> ZDmap = new HashMap<>();
        HashMap<Integer, List<ReviewOpinionsDataDTO>> BGmap = new HashMap<>();
        HashMap<Integer, List<ReviewOpinionsDataDTO>> DGmap = new HashMap<>();
        Integer num = 1;
        String strbdr = "　　　";
        for (Long item : ReviewCommentZDList) {
            String strHidder = strbdr;
            List<ReviewOpinionsDataDTO> str = new ArrayList<>();
            Optional<ReviewComments> s = ReviewComments.stream().filter(r -> r.getId() == item).findFirst();
            if (s.isPresent()) {
                ReviewCommentid.add(item);
                ReviewOpinionsDataDTO dto = new ReviewOpinionsDataDTO();
                dto.setContent(s.get().getDescription() + ":" + s.get().getContent());
                dto.setRemarks(strHidder + num.toString() + ".");
                if (Framework.isNotNullOrEmpty(s.get().getAttachmentId())) {
                    dto.setSubordinateTab(DxnConfig.getFileUploadPath() + s.get().getAttachmentId().getPath() + s.get().getAttachmentId().getName());
                    dto.setCompositionCustomerId(s.get().getAttachmentId().getName());
                }
                str.add(dto);
                String TreeCode = s.get().getTreeCode();
                List<ReviewComments> ss = ReviewComments.stream().where(r -> r.getTreeCode().startsWith(TreeCode) && r.getTreeCode() != TreeCode).sortedBy(o -> o.getTreeCode()).toList();
                for (ReviewComments items : ss) {
                    strHidder = strHidder + strbdr;
                    ReviewOpinionsDataDTO childrendto = new ReviewOpinionsDataDTO();
                    childrendto.setRemarks(strHidder + items.getDescription() + ":");
                    childrendto.setContent(items.getContent());
                    if (Framework.isNotNullOrEmpty(items.getAttachmentId())) {
                        childrendto.setSubordinateTab(DxnConfig.getFileUploadPath() + items.getAttachmentId().getPath() + items.getAttachmentId().getName());
                        childrendto.setCompositionCustomerId(s.get().getAttachmentId().getName());
                    }
                    str.add(childrendto);
                }
            }
            ZDmap.put(num, str);//存放键值对
            num++;
        }
        num = 1;
        for (Long item : ReviewCommentBGList) {
            List<ReviewOpinionsDataDTO> str = new ArrayList<>();
            Optional<ReviewComments> s = ReviewComments.stream().filter(r -> r.getId() == item).findFirst();
            if (s.isPresent()) {
                ReviewCommentid.add(item);
                String strHidder = strbdr;
                ReviewOpinionsDataDTO dto = new ReviewOpinionsDataDTO();
                dto.setContent(s.get().getDescription() + ":" + s.get().getContent());
                dto.setRemarks(strHidder + num.toString() + ".");
                if (Framework.isNotNullOrEmpty(s.get().getAttachmentId())) {
                    dto.setSubordinateTab(DxnConfig.getFileUploadPath() + s.get().getAttachmentId().getPath() + s.get().getAttachmentId().getName());
                    dto.setCompositionCustomerId(s.get().getAttachmentId().getName());
                }
                str.add(dto);
                String TreeCode = s.get().getTreeCode();
                List<ReviewComments> ss = ReviewComments.stream().where(r -> r.getTreeCode().startsWith(TreeCode) && r.getTreeCode() != TreeCode).sortedBy(o -> o.getTreeCode()).toList();
                for (ReviewComments items : ss) {
                    strHidder = strHidder + strbdr;
                    ReviewOpinionsDataDTO childrendto = new ReviewOpinionsDataDTO();
                    childrendto.setContent(items.getContent());
                    childrendto.setRemarks(strHidder + items.getDescription() + ":");
                    if (Framework.isNotNullOrEmpty(items.getAttachmentId())) {
                        childrendto.setSubordinateTab(DxnConfig.getFileUploadPath() + items.getAttachmentId().getPath() + items.getAttachmentId().getName());
                        childrendto.setCompositionCustomerId(s.get().getAttachmentId().getName());
                    }
                    str.add(childrendto);
                }
            }
            BGmap.put(num, str);//存放键值对
            num++;
        }
        num = 1;
        for (Long item : ReviewCommentDGList) {
            List<ReviewOpinionsDataDTO> str = new ArrayList<>();
            Optional<ReviewComments> s = ReviewComments.stream().filter(r -> r.getId() == item).findFirst();
            if (s.isPresent()) {
                ReviewCommentid.add(item);
                String strHidder = strbdr;
                ReviewOpinionsDataDTO dto = new ReviewOpinionsDataDTO();
                dto.setContent(s.get().getDescription() + ":" + s.get().getContent());
                dto.setRemarks(strHidder + num.toString() + ".");
                if (Framework.isNotNullOrEmpty(s.get().getAttachmentId())) {
                    dto.setSubordinateTab(DxnConfig.getFileUploadPath() + s.get().getAttachmentId().getPath() + s.get().getAttachmentId().getName());
                    dto.setCompositionCustomerId(s.get().getAttachmentId().getName());
                }
                str.add(dto);
                String TreeCode = s.get().getTreeCode();
                List<ReviewComments> ss = ReviewComments.stream().where(r -> r.getTreeCode().startsWith(TreeCode) && r.getTreeCode() != TreeCode).sortedBy(o -> o.getTreeCode()).toList();
                for (ReviewComments items : ss) {
                    strHidder = strHidder + strbdr;
                    ReviewOpinionsDataDTO childrendto = new ReviewOpinionsDataDTO();
                    childrendto.setContent(items.getContent());
                    childrendto.setRemarks(strHidder + items.getDescription() + ":");
                    if (Framework.isNotNullOrEmpty(items.getAttachmentId())) {
                        childrendto.setSubordinateTab(DxnConfig.getFileUploadPath() + items.getAttachmentId().getPath() + items.getAttachmentId().getName());
                        childrendto.setCompositionCustomerId(s.get().getAttachmentId().getName());
                    }
                    str.add(childrendto);
                }
            }
            DGmap.put(num, str);//存放键值对
            num++;
        }
        if (FileName.indexOf("总所") != -1)
            putreviewOpinionsData(ProjectCode, 60, ReviewCommentid);
        else
            putreviewOpinionsData(ProjectCode, 50, ReviewCommentid);
        WordUtils.CreateWord(templateFile, newFile, ZDmap, BGmap, DGmap);
    }

    //  大信版      项目质量控制复核反馈意见及回复
    @SystemService(args = "FileName,ProjectCode,ReviewCommentZDList,ReviewCommentBGList,ReviewCommentDGList")
    public void DxCreateReviewOpinion(String FileName, String ProjectCode, List<Long> ReviewCommentZDList, List<Long> ReviewCommentBGList, List<Long> ReviewCommentDGList) throws Exception {
        List<Long> ReviewCommentid = new ArrayList<>();
        Map<String, List<ReviewOpinionsDataDTO>> maps = new HashMap<>();

        Integer num = 1;
        String strbdr = "　　　";
        for (Long item : ReviewCommentZDList) {
            String strHidder = strbdr;
            List<ReviewOpinionsDataDTO> str = new ArrayList<>();
            Optional<ReviewComments> s = ReviewComments.stream().where(r -> r.getId() == item).findFirst();
            if (s.isPresent()) {
                ReviewCommentid.add(item);
                ReviewOpinionsDataDTO dto = new ReviewOpinionsDataDTO();
                dto.setLevel(1);
                dto.setContent(num + "," + s.get().getCreatedBy().getName() + ":" + Framework.urlDecode(s.get().getContent()).replaceAll("<p>", "").replaceAll("</p>", ""));
                dto.setRemarks(strHidder + num.toString() + ".");
                if (Framework.isNotNullOrEmpty(s.get().getAttachmentId())) {
                    dto.setSubordinateTab(DxnConfig.getFileUploadPath() + s.get().getAttachmentId().getPath() + s.get().getAttachmentId().getName());
                    dto.setCompositionCustomerId(s.get().getAttachmentId().getName());
                }
                if (Framework.isNullOrEmpty(maps.get("ZD"))) {
                    str.add(dto);
                    maps.put("ZD", str);
                } else {
                    maps.get("ZD").add(dto);
                }
                // str.add(dto);
                String TreeCode = s.get().getTreeCode();
                List<ReviewComments> ss = ReviewComments.stream().where(r -> r.getTreeCode().startsWith(TreeCode) && r.getTreeCode() != TreeCode).sortedBy(o -> o.getTreeCode()).toList();
                for (ReviewComments items : ss) {
                    strHidder = strHidder + strbdr;
                    ReviewOpinionsDataDTO childrendto = new ReviewOpinionsDataDTO();
                    childrendto.setLevel(2);
                    childrendto.setRemarks(strHidder + items.getDescription() + ":");
                    childrendto.setContent(items.getDescription() + "(" + items.getCreatedBy().getName() + "):" + Framework.urlDecode(items.getContent()).replaceAll("<p>", "").replaceAll("</p>", ""));
                    if (Framework.isNotNullOrEmpty(items.getAttachmentId())) {
                        childrendto.setSubordinateTab(DxnConfig.getFileUploadPath() + items.getAttachmentId().getPath() + items.getAttachmentId().getName());
                        childrendto.setCompositionCustomerId(s.get().getAttachmentId().getName());
                    }
                    maps.get("ZD").add(childrendto);
                    //str.add(childrendto);
                }
            }

            // maps.put("ZD", str);
            num++;
        }
        num = 1;
        for (Long item : ReviewCommentBGList) {
            List<ReviewOpinionsDataDTO> str = new ArrayList<>();
            Optional<ReviewComments> s = ReviewComments.stream().where(r -> r.getId() == item).findFirst();
            if (s.isPresent()) {
                ReviewCommentid.add(item);
                String strHidder = strbdr;
                ReviewOpinionsDataDTO dto = new ReviewOpinionsDataDTO();
                dto.setLevel(1);
                dto.setContent(num + "," + s.get().getCreatedBy().getName() + ":" + Framework.urlDecode(s.get().getContent()).replaceAll("<p>", "").replaceAll("</p>", ""));
                dto.setRemarks(strHidder + num.toString() + ".");
                if (Framework.isNotNullOrEmpty(s.get().getAttachmentId())) {
                    dto.setSubordinateTab(DxnConfig.getFileUploadPath() + s.get().getAttachmentId().getPath() + s.get().getAttachmentId().getName());
                    dto.setCompositionCustomerId(s.get().getAttachmentId().getName());
                }

                if (Framework.isNullOrEmpty(maps.get("BG"))) {
                    str.add(dto);
                    maps.put("BG", str);
                } else {
                    maps.get("BG").add(dto);
                }

                // str.add(dto);
                String TreeCode = s.get().getTreeCode();
                List<ReviewComments> ss = ReviewComments.stream().where(r -> r.getTreeCode().startsWith(TreeCode) && r.getTreeCode() != TreeCode).sortedBy(o -> o.getTreeCode()).toList();
                for (ReviewComments items : ss) {
                    strHidder = strHidder + strbdr;
                    ReviewOpinionsDataDTO childrendto = new ReviewOpinionsDataDTO();
                    childrendto.setContent(items.getDescription() + "(" + items.getCreatedBy().getName() + "):" + Framework.urlDecode(items.getContent()).replaceAll("<p>", "").replaceAll("</p>", ""));
                    childrendto.setLevel(2);
                    childrendto.setRemarks(strHidder + items.getDescription() + ":");
                    if (Framework.isNotNullOrEmpty(items.getAttachmentId())) {
                        childrendto.setSubordinateTab(DxnConfig.getFileUploadPath() + items.getAttachmentId().getPath() + items.getAttachmentId().getName());
                        childrendto.setCompositionCustomerId(s.get().getAttachmentId().getName());
                    }
                    maps.get("BG").add(childrendto);
                    //str.add(childrendto);
                }
            }

            // maps.put("BG", str);
            num++;
        }
        num = 1;
        for (Long item : ReviewCommentDGList) {
            List<ReviewOpinionsDataDTO> str = new ArrayList<>();
            Optional<ReviewComments> s = ReviewComments.stream().where(r -> r.getId() == item).findFirst();
            if (s.isPresent()) {
                ReviewCommentid.add(item);
                String strHidder = strbdr;
                ReviewOpinionsDataDTO dto = new ReviewOpinionsDataDTO();
                dto.setLevel(1);
                dto.setContent(num + "," + s.get().getCreatedBy().getName() + ":" + Framework.urlDecode(s.get().getContent()).replaceAll("<p>", "").replaceAll("</p>", ""));
                dto.setRemarks(strHidder + num.toString() + ".");
                if (Framework.isNotNullOrEmpty(s.get().getAttachmentId())) {
                    dto.setSubordinateTab(DxnConfig.getFileUploadPath() + s.get().getAttachmentId().getPath() + s.get().getAttachmentId().getName());
                    dto.setCompositionCustomerId(s.get().getAttachmentId().getName());
                }

                if (Framework.isNullOrEmpty(maps.get("DG"))) {
                    str.add(dto);
                    maps.put("DG", str);
                } else {
                    maps.get("DG").add(dto);
                }

                //str.add(dto);
                String TreeCode = s.get().getTreeCode();
                List<ReviewComments> ss = ReviewComments.stream().where(r -> r.getTreeCode().startsWith(TreeCode) && r.getTreeCode() != TreeCode).sortedBy(o -> o.getTreeCode()).toList();
                for (ReviewComments items : ss) {
                    strHidder = strHidder + strbdr;
                    ReviewOpinionsDataDTO childrendto = new ReviewOpinionsDataDTO();
                    childrendto.setContent(items.getDescription() + "(" + items.getCreatedBy().getName() + "):" + Framework.urlDecode(items.getContent()).replaceAll("<p>", "").replaceAll("</p>", ""));
                    childrendto.setLevel(2);
                    childrendto.setRemarks(strHidder + items.getDescription() + ":");
                    if (Framework.isNotNullOrEmpty(items.getAttachmentId())) {
                        childrendto.setSubordinateTab(DxnConfig.getFileUploadPath() + items.getAttachmentId().getPath() + items.getAttachmentId().getName());
                        childrendto.setCompositionCustomerId(s.get().getAttachmentId().getName());
                    }
                    maps.get("DG").add(childrendto);
                    //str.add(childrendto);
                }
            }

            // maps.put("DG", str);
            num++;
        }

        if (FileName.indexOf("总所") != -1) {
            putreviewOpinionsData(ProjectCode, 60, ReviewCommentid);
        } else {
            putreviewOpinionsData(ProjectCode, 50, ReviewCommentid);
        }


        Optional<CompositionCustomer> customefirst = CompositionCustomer.stream().where(c -> c.getCode().equals(ProjectCode)).findFirst();
        CompositionCustomer custome = customefirst.get();
        Map<String, String> textMap = new HashMap<>();
        textMap.put("deptumentName", custome.getProjectId().getDeptId().getName());
        textMap.put("projectLManuager", custome.getProjectManagerId().getName());
        textMap.put("auditProjectName", custome.getName());
        textMap.put("auditTimeAndTypeName", Framework.localDateToString(custome.getAuditEnd()));
        textMap.put("businessType", custome.getBusinessTypeValue());

        Long projectid = custome.getProjectId().getId();
        Optional<DraftReviewBatch> batch = DraftReviewBatch.stream().where(c -> c.getProjectId().getId() == projectid).findFirst();
        if (batch.isPresent()) {
            Long dId = batch.get().getId();
            List<ReviewAllocation> allocation = ReviewAllocation.stream().where(c -> c.getDraftReviewBatchId().getId() == dId).toList();
            if (allocation.size() > 0) {
                String str = "";
                for (ReviewAllocation item : allocation) {
                    str += item.getUser().getName() + ",";
                }
                textMap.put("reviewName", str.substring(0, str.length() - 1));
            } else {
                textMap.put("reviewName", "");
            }
        } else {
            textMap.put("reviewName", "");
        }
        textMap.put("managerName", custome.getProjectManagerId().getName());  //项目负责人
        textMap.put("PartnerName", custome.getProjectId().getProjectPartnerId().getName());  //项目合伙人

        textMap.put("firstTime", "");  //第一次下发意见的日期
        textMap.put("ZFDeptName", "");  //下发意见人的所树部门
        textMap.put("HFTime", "");  //下发意见人的所树部门
        Integer rr = FileName.indexOf("总所") != -1 ? 60 : 50;
        Long auditProjectid = custome.getId();
        List<ReviewComments> reviewCommentsList = ReviewComments.stream().where(c -> c.getCompositionCustomerId().getId().equals(auditProjectid)
                && c.getManuscriptReviewState().equals(rr)).toList();
        if (reviewCommentsList.size() > 0) {
            //第一次下发的数据
            Optional<ReviewComments> firstModel = reviewCommentsList.stream().filter(c -> c.getLevel() == 1).findFirst();
            if (firstModel.isPresent()) {
                textMap.put("firstTime", Framework.localDateToString(firstModel.get().getCreatedOn()));//第一次下发意见的日期
                Long deptId = firstModel.get().getCreatedBy().getDeptId();
                Department dept = Department.findById(deptId);
                textMap.put("ZFDeptName", dept.getTreeName());  //下发意见人的所树部门
            }

            //第一次回复的的数据
            Optional<ReviewComments> secodModel = reviewCommentsList.stream().filter(c -> c.getLevel() == 2).findFirst();
            if (secodModel.isPresent()) {
                textMap.put("HFTime", Framework.localDateToString(secodModel.get().getCreatedOn()));  //第一次回复
            }
        }
        String path = gainAttachmentPath();
        path = path.replace("null", ProjectCode);
        String templateFile = DxnConfig.getFileUploadPath();
        String newFile = "";
        if (Framework.isNotNullOrEmpty(path)) {
            newFile = FileHelper.combine(templateFile, path);
        }
        newFile = FileHelper.combine(newFile, "ManuscriptItemArchive");
        FileHelper.ensureDirtectoryExists(newFile);
        newFile = FileHelper.combine(newFile, FileName);
        templateFile = FileHelper.combine(templateFile, FileName);


        // 模板文件输入输出地址
        String filePath = templateFile;
        String outPath = newFile;

        WordHelperUtils wordReporter = new WordHelperUtils();
        wordReporter.setTempLocalPath(filePath);    //设置模板的路径
        wordReporter.init();            //初始化工具类
        wordReporter.export(textMap, maps);   //写入相关数据
        wordReporter.generate(outPath);   //导出到目标文档
    }


    public List<ReviewCommentsDTO> ReviewCommentsDTO(Long auditworkProjectID) throws Exception {
        List<ReviewCommentsDTO> dtoLIst = new ArrayList<>();
        List<ReviewComments> reviewCommentsList = ReviewComments.stream().where(w -> w.getProjectId().getId().equals(auditworkProjectID)).sortedBy(f -> f.getId()).toList();
        for (ReviewComments item : reviewCommentsList) {
            ReviewCommentsDTO reviewCommentsdto = new ReviewCommentsDTO();
            reviewCommentsdto.setId(item.getId());
            if (Framework.isNotNullOrEmpty(item.getParentId()))
                reviewCommentsdto.setParentId(item.getParentId().getId());
            reviewCommentsdto.setDescription(item.getDescription());
            reviewCommentsdto.setContent(item.getContent());
            reviewCommentsdto.setQuestionType(item.getQuestionType());
            reviewCommentsdto.setrCType(item.getRCType());
            dtoLIst.add(reviewCommentsdto);
        }
        return dtoLIst;
    }

    @SystemService()
    public List<ProjectStateDTO> gainProjectStateList() throws Exception {
        List<ProjectStateDTO> list = new ArrayList<>();
        list.add(gainProjectStateDTO("Create", "立项中", 20, null));
        list.add(gainProjectStateDTO("Execute", "项目执行中", 30, null));
        list.add(gainProjectStateDTO("Review", "底稿复核中", 31, null));
        list.add(gainProjectStateDTO("Issue", "报告出具中", 32, null));
        list.add(gainProjectStateDTO("OnFile", "底稿归档中", 33, " and manuscriptReviewState=70 and ManuscriptItemArchiveState<30"));
        list.add(gainProjectStateDTO("NoFile", "未归档", 33, " and manuscriptReviewState=70"));
        return list;
    }

    private ProjectStateDTO gainProjectStateDTO(String code, String name, int docState, String where) throws Exception {
        Long userId = AppContext.current().getProfile().getId();
        List<Object> p = new ArrayList<>();

        String sql = String.format("select Count(*) from prj_project where docState=%s ", docState);
        if (!AppContext.current().getProfile().getIsSuperAdmin()) {
            p.add(userId);
            p.add(userId);
            sql = sql + "and (id in (select projectid from dbo.Prj_ComprositionCustome where projectManagerId  =? ) or createdById =?) ";
        }

        if (Framework.isNotNullOrEmpty(where)) sql = sql + where;

        ProjectStateDTO dto = new ProjectStateDTO();
        dto.setCode(code);
        dto.setName(name);
        dto.setNumber(repository().sqlQueryFirst(sql, p, int.class));
        dto.setState(docState);

        return dto;
    }

    private void addtoWorkExperience() throws Exception {
        String customerTypeValue = this.getCustomerTypeValue();
        if (customerTypeValue.indexOf("IPO") != -1 || customerTypeValue.indexOf("上市公司") != -1 || customerTypeValue.indexOf("新三板") != -1 || customerTypeValue.indexOf("金融") != -1) {
            Long projectId = this.getId();
            List<Long> longList = new ArrayList<>();
            List<ProjectMembers> projectMembersList = ProjectMembers.stream().where(w -> w.getProjectId().getId().equals(projectId)).toList();
            for (ProjectMembers item : projectMembersList) {
                if (Framework.isNotNullOrEmpty(item.getNameId())) {
                    Long staffId = item.getNameId().getStaffId();
                    if (!longList.contains(staffId)) {
                        longList.add(staffId);
                        Optional<Staff> staffFirst = Staff.stream().where(w -> w.getId().equals(staffId)).findFirst();
                        if (staffFirst.isPresent()) {
                            Long count = ProjectExperience.stream().where(w -> w.getStaffId().getId() == staffId && w.getProjectId().getId() == projectId).count();
                            if (count == 0) {
                                ProjectExperience projectExperience = ProjectExperience.createNew();
                                Staff staff = staffFirst.get();
                                projectExperience.setWTSCPA(staff.getISCPA());
                                projectExperience.setProjectId(this);
                                projectExperience.setPEName(this.getShowName());
                                projectExperience.setCustomerType(this.getCustomerTypeValue());
                                projectExperience.setBusinessType(this.getBusinessTypeValue());
                                projectExperience.setDeleteIdentity(10);
                                projectExperience.setStaffId(staff);
                                repository().add(projectExperience);
                            }
                        }
                    }
                }
            }
        }
    }

    @SystemService(args = "puuid")
    public Long gainProjectIdbyUUid(String puuid) throws Exception {
        Long pid = 0L;
        Optional<Project> has = this.stream().where(w -> w.getUuid().equals(puuid)).findFirst();
        if (has.isPresent()) {
            pid = has.get().getId();
        }
        return pid;
    }

    @SystemService(args = "id")
    public String gainManuScirptItemArchiveByProject(Long id) throws Exception {
        String str = "";
        //Long projectid=Long.parseLong(id);
        List<CompositionCustomer> compositionCustomers = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(id)).toList();

        for (CompositionCustomer f : compositionCustomers) {
            Long ids = f.getId();
            Optional<ManuScirptItemArchive> archive = ManuScirptItemArchive.stream().where(c -> c.getCompostionCustomerId().getId().equals(ids)).findFirst();
            if (!archive.isPresent())
                str = "存在没有录入归档信息的单位，请完善其他单位的归档信息";
        }
        return str;
    }

    @SystemService()
    public String deleteProjectAndUsing() throws Exception {
        this.deleteAndUsing();
        return "项目数据清理完成!";
    }


    public void writeDoc(List<ProjectMembers> list, String path, String newPath) throws Exception {

        //File fi = new File("D:\\2-独立性及保密声明.docx");
        File fi = new File(path);
        InputStream in = new FileInputStream(fi);
        XWPFDocument doc = new XWPFDocument(in);

        XWPFTable tableContent = doc.createTable(list.size() + 10, 4);

        tableContent.setWidth(500000);
        //tableContent.setCellMargins(0, 0,50,0);
        tableContent.getRow(0).getCell(0).setText("签名");
        tableContent.getRow(0).getCell(1).setText("时间");
        tableContent.getRow(0).getCell(2).setText("签名");
        tableContent.getRow(0).getCell(3).setText("时间");

        cellWidth(tableContent.getRow(0).getCell(0));
        cellWidth(tableContent.getRow(0).getCell(1));
        cellWidth(tableContent.getRow(0).getCell(2));
        cellWidth(tableContent.getRow(0).getCell(3));

        border(tableContent);
        int index = 0;
        int indexC = 0;  //   2
        for (int i = 1; i <= list.size(); i++) {
            if (list.size() - 1 < indexC) break;
            tableContent.getRow(index + 1).getCell(0).setText(list.get(indexC).getNameId().getName());
            tableContent.getRow(index + 1).getCell(1).setText(list.get(indexC).getSigningDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            int num = indexC + 1;
            if (list.size() - 1 < num) break;
            tableContent.getRow(index + 1).getCell(2).setText(list.get(num).getNameId().getName());
            tableContent.getRow(index + 1).getCell(3).setText(list.get(num).getSigningDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            indexC = num + 1;
            index++;
            //index = index + 2;
        }
        FileOutputStream out = null;
        out = new FileOutputStream(newPath);
        doc.write(out);
    }

    private void cellWidth(XWPFTableCell cell) {
        CTTcPr tcpr = cell.getCTTc().addNewTcPr();
        CTTblWidth cellw = tcpr.addNewTcW();
        cellw.setType(STTblWidth.DXA);
        cellw.setW(BigInteger.valueOf(540 * 5));
    }

    private void border(XWPFTable tableContent) {
        CTTblBorders borders = tableContent.getCTTbl().getTblPr().addNewTblBorders();
        CTBorder vBorder = borders.addNewInsideV();
        vBorder.setVal(STBorder.Enum.forString("none"));
        vBorder.setSz(new BigInteger("1"));
        vBorder.setColor("000000");

        CTBorder hBorder = borders.addNewInsideH();
        hBorder.setVal(STBorder.Enum.forString("none"));
        hBorder.setSz(new BigInteger("1"));
        hBorder.setColor("000000");

        CTBorder lBorder = borders.addNewLeft();
        lBorder.setVal(STBorder.Enum.forString("none"));
        lBorder.setSz(new BigInteger("1"));
        lBorder.setColor("000000");

        CTBorder rBorder = borders.addNewRight();
        rBorder.setVal(STBorder.Enum.forString("none"));
        rBorder.setSz(new BigInteger("1"));
        rBorder.setColor("000000");

        CTBorder tBorder = borders.addNewTop();
        tBorder.setVal(STBorder.Enum.forString("none"));
        tBorder.setSz(new BigInteger("1"));
        tBorder.setColor("000000");

        CTBorder bBorder = borders.addNewBottom();
        bBorder.setVal(STBorder.Enum.forString("none"));
        bBorder.setSz(new BigInteger("1"));
        bBorder.setColor("000000");

    }

    /**
     * 通过项目ID删除项目相关的所有数据
     *
     * @param projectId
     * @throws Exception
     */


    @SystemService(args = "projectId")
    public void clearDataByProjectId(Long projectId) throws Exception {
        if (Framework.isNullOrEmpty(projectId)) return;
        deleteProjectothers(projectId);

        deleteProject(projectId);

    }

    @SystemService(args = "projectId")
    public void reportSituation(Long projectId) throws Exception {


    }

    //删除项目相关数据
    private void deleteProjectothers(Long projectId) throws Exception {
        String deleteSql = "delete from Prj_ProjectMembers where projectid = " + projectId;
        repository().executeCommand(deleteSql);

        deleteSql = "delete from PM_BusinessSub where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub10 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub11 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub12 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub13 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub14 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub20 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub5 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub6 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub7 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub8 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_BusinessSub9 where businessEvaluationId in (select id from PM_BusinessEvaluation where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);


        deleteSql = "delete from PM_BusinessEvaluation where projectid = " + projectId;
        repository().executeCommand(deleteSql);
        // deleteSql = "delete from Base_Attachment  where id in (select attachmentid from PM_PrjAttachment where projectid =" + projectId + ")";
        // repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_PrjAttachment where projectid =" + projectId;
        repository().executeCommand(deleteSql);
        deleteSql = "delete from Base_Attachment where entityname ='Engagement' \n" +
                "and entityId in (select id from PM_Engagement where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);

        deleteSql = "delete from PM_Engagement where engagementId in (select id from PM_Engagement where projectid =" + projectId + ")";
        repository().executeCommand(deleteSql);

        deleteSql = "delete from PM_Engagement where projectid =" + projectId;
        repository().executeCommand(deleteSql);

        deleteSql = "delete  from Prj_AuditFiles \n" +
                "where auditworkProjectID in (select UUID from dbo.Prj_ComprositionCustome where projectid=" + projectId + ")";
        repository().executeCommand(deleteSql);


        deleteSql = "delete  from Prj_CompositionCustomerModifyRecord \n" +
                "where CompositionCustomerId in (select id from dbo.Prj_ComprositionCustome where projectid=" + projectId + ")";
        repository().executeCommand(deleteSql);

        deleteSql = "delete from Prj_AuditFilesVSManuscript\n" +
                "where auditworkProjectID in (select UUID from dbo.Prj_ComprositionCustome where projectid=" + projectId + ")";
        repository().executeCommand(deleteSql);

        deleteSql = "delete from Prj_ManuscriptItem\n" +
                "where auditworkProjectID in (select UUID from dbo.Prj_ComprositionCustome where projectid=" + projectId + ")";
        repository().executeCommand(deleteSql);

        deleteSql = "delete from QR_DraftReviewBatchItem \n" +
                "where compositionCustomerId in  (select id from dbo.Prj_ComprositionCustome where projectid=" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from wf_todo  where entitytype ='DraftReviewBatch' \n" +
                "and entityid in (select id from QR_DraftReviewBatch where projectid=" + projectId + ")";
        repository().executeCommand(deleteSql);

        deleteSql = "delete from QR_Attention where projectid= " + projectId;
        repository().executeCommand(deleteSql);
        deleteSql = "delete from QR_ReviewAllocation where DraftReviewBatchid in (select id from QR_DraftReviewBatch where projectid=" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from Base_Attachment where entityname='ReviewComments' \n" +
                "and entityid in (select id from QR_ReviewComments  where projectid=" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from QR_ReviewComments  where projectid=" + projectId;
        repository().executeCommand(deleteSql);
        deleteSql = "delete from QR_DraftReviewBatch where projectid= " + projectId;
        repository().executeCommand(deleteSql);

        deleteSql = "delete from Base_Attachment  where id in \n" +
                "(select AttachmentId from PM_ReportHistoryFile where reportid in (select id from PM_Report where projectid=" + projectId + "))";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_ReportHistoryFile where reportid in (select id from PM_Report where projectid=" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_ReportModify where reportid in (select id from PM_Report where projectid=" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from wf_todo where  entitytype ='Report' and entityid in (select id from PM_Report where projectid=" + projectId + ")";
        repository().executeCommand(deleteSql);

        deleteSql = "delete from Base_Attachment where id in (\n" +
                "select AttachmentId from Rpt_RegistFormFile where RegistFormid in (\n" +
                "select id from Rpt_RegistForm where ReportId in (select id from PM_Report where projectid=" + projectId + ")\n" +
                ")\n" +
                ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from Rpt_RegistFormFile where RegistFormid in (\n" +
                "select id from Rpt_RegistForm where ReportId in (select id from PM_Report where projectid=" + projectId + ")\n" +
                ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from wf_todo where  entitytype ='RegistForm' and entityid in \n" +
                "(select id from Rpt_RegistForm where ReportId in (select id from PM_Report where projectid=" + projectId + "))";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from Rpt_RegistForm where ReportId in (select id from PM_Report where projectid=" + projectId + ")";
        repository().executeCommand(deleteSql);


        deleteSql = "delete from PM_ReportFile where reportId in (select id from PM_Report where projectid=" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_Report where projectid=" + projectId;
        repository().executeCommand(deleteSql);

        deleteSql = "delete from prj_BorrowGrandson\n" +
                "where borrowChildId in (select id from prj_BorrowChild where borrowMasterId in (select id from prj_BorrowMaster where projectid=" + projectId + "))";
        repository().executeCommand(deleteSql);

        deleteSql = "delete from prj_renew\n" +
                "where borrowChildId in (select id from prj_BorrowChild where borrowMasterId in (select id from prj_BorrowMaster where projectid=" + projectId + "))";
        repository().executeCommand(deleteSql);

        deleteSql = "delete from prj_BorrowChild where borrowMasterId in (select id from prj_BorrowMaster where projectid=" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from prj_BorrowMaster where projectid=" + projectId;
        repository().executeCommand(deleteSql);
        deleteSql = "delete from Prj_BoxAndNumberBook where manuScirptItemArchiveId \n" +
                "in (select id from Prj_ManuScirptItemArchive where projectid=" + projectId + ")";
        repository().executeCommand(deleteSql);

        deleteSql = "delete from Prj_ManuScirptItemArchive where projectid=" + projectId;
        repository().executeCommand(deleteSql);

        deleteSql = "delete from Prj_CompositionMembers where CompositionCustomerId in (\n" +
                "select id from Prj_ComprositionCustome where projectid=" + projectId + ")";
        repository().executeCommand(deleteSql);
        deleteSql = "delete from Prj_ComprositionCustome where projectid=" + projectId;
        repository().executeCommand(deleteSql);

        deleteSql = "delete HR_StaffProjectExperience where ProjectId =" + projectId;
        repository().executeCommand(deleteSql);

        deleteSql = "delete from Prj_ProjectIndependenceMember where ProjectId =" + projectId;
        repository().executeCommand(deleteSql);
        deleteSql = "delete from PM_TechnicalConsultation where ProjectNameId =" + projectId;
        repository().executeCommand(deleteSql);
    }

    //删除主项目
    private void deleteProject(Long projectId) throws Exception {
        String deleteSql = "delete from prj_project where id =" + projectId;
        repository().executeCommand(deleteSql);
    }


    /**
     * 变更情况 cs端
     *
     * @param projectId
     * @param auditProjectId
     * @return
     * @throws Exception
     */
    @SystemService(args = "projectId,auditProjectId")
    public ResponseEntity ChangeAuditProjectState(String projectId, String auditProjectId) throws Exception {
        ResponseEntity response = new ResponseEntity();
        ChangeAuditStateDTO changlist = new ChangeAuditStateDTO();
        try {
            List<CompositionCustomer> compostionArray = CompositionCustomer.stream().where(c -> c.getProjectId().getUuid().equals(projectId)).toList();
            List<ChangeCompostionCustomerDTO> clist = new ArrayList<>();
            for (CompositionCustomer item : compostionArray) {
                ChangeCompostionCustomerDTO dto = new ChangeCompostionCustomerDTO();
                dto.setID(item.getUuid());
                dto.setProjectGrade(item.getLevel());
                dto.setCompanyType(item.getCompanyRelationship() == null ? "" : item.getCompanyRelationship().toString());
                dto.setLongName(item.getShowName());
                dto.setProjectName(item.getName());
                dto.setRiskLevel(item.getRiskLevel());
                dto.setRandomCode(item.getRandomCode());
                dto.setUniqueCode(item.getProjectId().getRandomCode());
                dto.setManuscriptTemplateCode(item.getManuscriptTemplate() == null ? "" : item.getManuscriptTemplate().toString());
                dto.setManuscriptTemplateName(getEnumName("ManuscriptTemplate", item.getManuscriptTemplate() == null ? "" : item.getManuscriptTemplate().toString()));
                dto.setReviewStatus(item.getProjectId().getDocState());
                String str = "";
                if (item.getIsReport() != null)
                    str = item.getIsReport().toString() == "true" ? "1" : "0";

                dto.setIsMakeAuditReport(str);
                dto.setIsSubsidiaryReport(item.getIsSubsidiaryReport() == null ? "" : item.getIsSubsidiaryReport().toString());
                dto.setAuditStartDate(item.getAuditStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                dto.setAuditEndDate(item.getAuditEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                dto.setWhetherUpload(item.getWhetherUpload());
                dto.setSortCode(item.getSortCode());
                dto.setPartner(item.getProjectId().getProjectPartnerId().getName());
                clist.add(dto);
                changlist.setAuditLst(clist);
            }
            List<AuditworkProjectModifyDetaileDTO> tlist = new ArrayList<>();
            List<CompositionCustomerModifyRecord> recordArray = CompositionCustomerModifyRecord.stream().where(c -> c.getCompositionCustomerId().getUuid().equals(auditProjectId)).toList();
            for (CompositionCustomerModifyRecord item : recordArray) {//变更
                AuditworkProjectModifyDetaileDTO dto = new AuditworkProjectModifyDetaileDTO();
                dto.setID(item.getUuid());
                dto.setAuditworkProjectID(item.getCompositionCustomerId().getUuid());
                dto.setModifyType(item.getModifyType());
                dto.setRandomCode(item.getRandomCode());
                tlist.add(dto);
                changlist.setChangeAuditList(tlist);
            }
            response.setData(changlist);
            response.setCode(200);
        } catch (Exception ex) {
            response.setMessages("服务器内部错误500");
            response.setCode(500);
        }
        return response;

    }

    @SystemService()
    public String tt1() throws Exception {
        //拷贝文件夹 压缩成非加密压缩包
        //new ZipEncrypt().encryptZip("D:\\Ce1\\2020-20", "D:\\ARr\\1.zip", "报告,业务约定书,报备");
        String str = "JTNDcCUzRTMzMyUzQyUyRnAlM0U=";

        final Base64.Decoder decoder = Base64.getDecoder();
        final Base64.Encoder encoder = Base64.getEncoder();
        final String text = "字串文字";
        final byte[] textByte = text.getBytes("UTF-8");
//编码

//解码
        System.out.println(new String(decoder.decode(str), "UTF-8"));
        String st1 = new String(decoder.decode(str), "UTF-8");
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(st1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "项目数据清理完成!";
    }

    //冗余风险等级通用方法
    public Integer GeneralMethodOfRiskLevel(Project project) throws Exception {
        Integer riskLevel = project.getRiskLevel();
        Long projectId = project.getId();
        List<CompositionCustomer> compositionCustomerList = CompositionCustomer.stream().where(c -> c.getProjectId().getId() == projectId).toList();
        for (CompositionCustomer item : compositionCustomerList) {
            if (riskLevel > item.getRiskLevel()) {
                riskLevel = item.getRiskLevel();
            }
        }
        return riskLevel;
    }

    public int gainWorkingYears(User user) throws Exception {
        int workingYears = 0;
        Long staffId = user.getStaffId();
        Optional<Staff> staffFirst = Staff.stream().where(s -> s.getId() == staffId).findFirst();
        if (staffFirst.isPresent()) {
            Staff staff = staffFirst.get();
            if (Framework.isNotNullOrEmpty(staff.getPractitionersDate())) {
                LocalDateTime sd3 = LocalDateTime.now();
                int currentTime = sd3.getYear();
                int practitionersDate = staff.getPractitionersDate().getYear();
                workingYears = currentTime - practitionersDate + 1;
            }
        }
        return workingYears;
    }

}
