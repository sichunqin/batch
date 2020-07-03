package com.dxn.model.extend;

import com.dxn.dto.reportDTO.ReportAuditUnitTreeDTO;
import com.dxn.dto.reportDTO.ReportFileDTO;
import com.dxn.forms.dto.ComboboxDTO;
import com.dxn.model.entity.ReportEntity;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.context.AppContext;
import com.dxn.system.context.Profile;
import com.dxn.system.dto.AttachmentDTO;
import com.dxn.system.dto.EntityDataDTO;
import com.dxn.system.dto.EnumDataDTO;
import com.dxn.system.dto.workFlow.MyTodoListDTO;
import com.dxn.system.dto.workFlow.WorkFlowNode;
import com.dxn.system.exception.BusinessException;
import com.dxn.system.exception.WorkFlowException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Table(name = "PM_Report")
public class Report extends ReportEntity {
    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        if (this.getBusinessSubclasses() == 1000 || this.getBusinessSubclasses() == 1001) {
            this.setReportType(331122);//业务子类为重大材料是 报告类型默认为是业务材料
        }
        if (Framework.isNullOrEmpty(this.getMergingMonomer()))
            this.setMergingMonomer(0);
        if (Framework.isNullOrEmpty(this.getSignCompletion()))
            this.setSignCompletion(20);
        if (Framework.isNullOrEmpty(this.getReportTypeShortName()) && Framework.isNotNullOrEmpty(this.getReportTypeId()))
            this.setReportTypeShortName(this.getReportTypeId().getShortName());

        CompositionCustomer cust = this.getCompositionCustomerId();
        //冗余 项目名称
        this.setProjectShowName(Framework.format("{0}-{1}", cust.getCode(), cust.getShowName()));
    }

    @Override
    public void onUpdating() throws Exception {
        super.onUpdating();
        this.setPrintingConditions(BranchConditionsOfReportSeal(this));
        this.SetCompositionCustomerReportIssue(true);
        Report oldvalue = this.getOriginalValue();
        //新增，或者 修改业务子类更新被审计单位冗余的业务子类字段
        if (Framework.isNullOrEmpty(oldvalue) ||
                (Framework.isNotNullOrEmpty(oldvalue.getBusinessSubclasses()) && oldvalue.getBusinessSubclasses() != this.getBusinessSubclasses())
                || (Framework.isNotNullOrEmpty(this.getBusinessSubclasses()) && this.getBusinessSubclasses() != oldvalue.getBusinessSubclasses())) {
            updateCompositionCustomer();
        }
        if (this.getAuditReportDate() != null && this.getAuditReportDate().getYear() == 2000)
            this.setAuditReportDate(null);
    }

    private void updateCompositionCustomer() throws Exception {
        Long cid = this.getCompositionCustomerId().getId();
        CompositionCustomer comcust = repository().findById(cid, CompositionCustomer.class);
        String BusinessSubclasses = "";
        Long id;
        if (Framework.isNullOrEmpty(this.getId()))//新增
        {
            id = 0L;
            if (Framework.isNotNullOrEmpty(this.getBusinessSubclasses())) {
                BusinessSubclasses = this.getBusinessSubclasses() + ",";
            }

        } else {
            id = this.getId();
        }
        List<Report> reports = this.stream().where(w -> w.getId() != id && w.getCompositionCustomerId().getId() == cid).toList();
        for (Report item : reports) {
            if (Framework.isNotNullOrEmpty(item.getBusinessSubclasses())) {
                BusinessSubclasses += item.getBusinessSubclasses() + ",";
            }
        }
        comcust.setBusinessSubclasses(BusinessSubclasses);
    }

    @Override
    public void onValidate() throws Exception {
        super.onValidate();
//        if (!AppContext.current().getProfile().getDepartmentalAttributes()) {
//            throw new BusinessException("职能部门,不允许维护报告");
//        }
        validateReport();
    }

    @Override
    public void onInserting() throws Exception {
        super.onInserting();
        this.setDocState(10);
        this.setPrintingConditions(BranchConditionsOfReportSeal(this));
        this.setApplySignStatus(10);
        this.setIsSign(false);
        updateCompositionCustomer();
        this.SetCompositionCustomerReportIssue(true);
        if (Framework.isNull(this.getYear())) {
            Calendar date = Calendar.getInstance();
            String year = String.valueOf(date.get(Calendar.YEAR));
            Integer year1 = Integer.parseInt(year);
            this.setYear(year1);
        }
        if (this.getAuditReportDate() != null && this.getAuditReportDate().getYear() == 2000)
            this.setAuditReportDate(null);
    }


    @Override
    public void onDeleting() throws Exception {
        super.onDeleting();
        this.SetCompositionCustomerReportIssue(false);
        updateCompositionCustomer();
        //删除报告时同时删除报告修改表数据
        String sql = "delete PM_ReportHistoryFile where reportId=?;";
        List<Object> parameters = new ArrayList<>();
        parameters.add(this.getId());
        repository().executeCommand(sql, parameters);
        sql = "delete PM_ReportModify where reportId=?;";
        repository().executeCommand(sql, parameters);

        //维护业务报备上的金额
        if (Framework.isNotNullOrEmpty(this.getRegistFormId())) {
            long registFormId = this.getRegistFormId();
            Optional<RegistForm> registForm = RegistForm.stream().where(w -> w.getId() == registFormId && w.getAmount() > 0).findFirst();//
            if (registForm.isPresent()) {
                RegistForm RF = registForm.get();
                long compositionCustomerId = this.getCompositionCustomerId().getId();
                List<RegistForm> rFormList = RegistForm.stream().where(w -> w.getReportId().getCompositionCustomerId().getId() == compositionCustomerId && w.getId() != registFormId).toList();
                if (rFormList.size() > 0) {
                    Optional<RegistForm> a = rFormList.stream().filter(d -> d.getReport().getMergingMonomer() == 20 && d.getReportTypeId().getShortName().equals("审字")).findFirst();
                    boolean istrue = false;
                    if (a.isPresent()) {
                        RegistForm s = a.get();
                        s.setAmount(RF.getAmount());
                        istrue = true;
                    } else if (!istrue) {
                        a = rFormList.stream().filter(d -> d.getReport().getMergingMonomer() == 10 && d.getReportTypeId().getShortName().equals("审字")).findFirst();
                        if (a.isPresent()) {
                            RegistForm s = a.get();
                            s.setAmount(RF.getAmount());
                        }
                    } else {
                        rFormList.get(0).setAmount(RF.getAmount());
                    }
                }
                this.repository().remove(RF);
            }
        }
    }

    @Override
    public String getUseWorkflowDefine(String json) throws WorkFlowException {
        Department org = this.getDeptId().getOrg();

        if (Framework.isNullOrEmpty(org))
            throw new WorkFlowException("所属分所不能为空");

        if (Framework.isNotNullOrEmpty(this.getStatus())) {
            Optional<CompositionCustomer> compositionCustomer = Optional.empty();
//            try {
//                compositionCustomer = CompositionCustomer.stream().filter(item -> item.getId().equals(this.getCompositionCustomerId().getId())).findFirst();
//                if (compositionCustomer.isPresent() && compositionCustomer.get().getParentId() == null) {
//                    Predicate<Engagement> express = item -> this.getProjectId().getId().equals(item.getProjectId().getId());
//                    express = express.and(item -> item.getDocState().equals("30"));
//                    Optional<Engagement> engagement = Engagement.stream().filter(express).findAny();
//                    this.setHaveBusinessAgreement(engagement.isPresent() ? 30 : 10);
//                    this.setIsExistenceAgreement(engagement.isPresent());
//                    this.repository().add(this);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            //签章流程
            //if(this.getStatus().equals("Sign"))
            //bReportSpecialApprovalId,gReportSpecialApprovalId
            // if (Framework.isNotNullOrEmpty(this.getHaveBusinessAgreement()) && this.getHaveBusinessAgreement() == 30 || compositionCustomer.get().getParentId() != null) {
            WorkFlowConfig workFlow = this.getReportIssue() == 10 ? org.getBReportSpecialApprovalId() : org.getGReportSpecialApprovalId();
            if (Framework.isNullOrEmpty(workFlow))
                throw new WorkFlowException(String.format("[%s ]未设置报告用印流程！", org.getName()));
            return workFlow.getUuid();
            // }
            //else {
            //    WorkFlowConfig workFlow = this.getReportIssue() == 10 ? org.getBReportReportPrintingId() : org.getGeneralReportPrintingId();
            //     if (Framework.isNullOrEmpty(workFlow))
            //        throw new WorkFlowException(String.format("[%s ]未设置报告用印特殊流程！", org.getName()));
            //     return workFlow.getProcessId();
            // }

        } else {
            WorkFlowConfig workFlow = this.getReportIssue() == 10 ? org.getBReportApprovalId() : org.getGeneralApprovalId();
            if (Framework.isNullOrEmpty(workFlow))
                throw new WorkFlowException(String.format("[%s(分所盖章)]未设置报告流程！", org.getName()));

            return workFlow.getUuid();
        }
    }

    @Override
    public void onSubmitWorkflowing(WorkFlowNode node) throws Exception {
        super.onSubmitWorkflowing(node);
        if (this.getAuditReportDate() != null && this.getAuditReportDate().getYear() == 2000)
            this.setAuditReportDate(null);
        String[] array = this.getAuditReportDate() == null ? null : this.getAuditReportDate().toString().split("-");

        if (array != null && !String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).equals(array[0])) {
            throw new BusinessException("审计报告日不能选除了当前年度的其它年!");
        }

        if (Framework.isNullOrEmpty(this.getIsRegistForm()))
            this.setIsRegistForm(20);
        submitValidateReport();
        Optional<ReportFile> file = this.getReportFiles().stream().filter(f -> f.getFileType() == 10 && f.getAttachmentId() == null).findAny();
        if (file.isPresent())
            throw new BusinessException("请上传报告正文!");
    }

    //报告用印分支条件维护
    public Integer BranchConditionsOfReportSeal(Report report) throws Exception {
        CompositionCustomer compositionCustomerParentId = report.getCompositionCustomerId().getParentId();
        Long compositionCustomerId = report.getCompositionCustomerId().getId();
        List<Engagement> engagementList = Engagement.stream().where(c -> c.getCompositionCustomerId().getId() == compositionCustomerId).toList();
        if (Framework.isNullOrEmpty(compositionCustomerParentId)) {
            //主项目
            int a = Framework.toList(engagementList.stream().filter(c -> c.getCompositionCustomerId().getId() == compositionCustomerId && c.getDocState() != 30)).size();
            if (a < 1) {
                return 10;
            } else {
                return 20;
            }
        } else {
            //子项目
            int a = Framework.toList(engagementList.stream().filter(c -> c.getCompositionCustomerId().getId() == compositionCustomerId && c.getDocState() != 30)).size();
            if (a < 1) {
                return 30;
            } else {
                return 40;
            }
        }
    }

    @Override
    public void onEndWorkflow(WorkFlowNode node) throws Exception {
        super.onEndWorkflow(node);
//        if (1 == 1)
//            throw new BusinessException("测试1234");
        //流程结束的时候情况标记
        if (this.getApplySignStatus() == 30)
            auditCustomerData();
        if (Framework.isNotNullOrEmpty(this.getStatus())) {
            this.setStatus(null);
            Long id = this.getCompositionCustomerId().getId();
            List<Engagement> egList = Engagement.stream().where(w -> w.getCompositionCustomerId().getId() == id).toList();
            if (Framework.isNotNullOrEmpty(this.getDeptId().getOrg())) {//.getIsSendMessage()
                if (egList.size() == 0) {
                    String content = "报告无约定书申请盖章审批通过：" + this.getCompositionCustomerId().getName() + "-" + this.getProjectId().getBusinessTypeValue();
                    CreateNotice(content, "业务报告用印审批流程");
                }
            }
        } else {
            String content = "报告审批通过：" + this.getCompositionCustomerId().getName() + "-" + this.getProjectId().getBusinessTypeValue();
            CreateNotice(content, "业务报告审批流程");
        }
        if (Framework.isNotNullOrEmpty(this.getReportTypeShortName())) {
            if (this.getReportTypeShortName().equals("专审字") || this.getReportTypeShortName().equals("审字") || this.getReportTypeShortName().equals("验字")) {
                if (Framework.isNullOrEmpty(this.getRegistFormId())) {
                    RegistForm rf = RegistForm.createNew();
                    rf.setFailureToReport(20);
                    rf.setReportId(this);
                    Long projectId = this.getProjectId().getId();
                    Optional<Project> optionalProject = Project.stream().where(data -> data.getId().equals(projectId)).findFirst();
                    Long customerId = optionalProject.get().getCustomerNameId().getId();
                    Optional<Customer> optionalCustomer = Customer.stream().where(data -> data.getId().equals(customerId)).findFirst();
                    rf.setUSCCode(optionalCustomer.get().getUSCCode());
                    rf.setSecuritiesCode(optionalCustomer.get().getSecuritiesCode());
                    this.repository().add(rf);
                }
                this.setIsRegistForm(10);
            }
        }
    }

    @Override
    public void onWorkFlowBack(WorkFlowNode node) throws Exception {//退回时触发
        super.onWorkFlowBack(node);
        if (Framework.isNotNullOrEmpty(this.getStatus())) {
            Long id = this.getCompositionCustomerId().getId();
            List<Engagement> egList = Engagement.stream().where(w -> w.getCompositionCustomerId().getId() == id).toList();
            if (Framework.isNotNullOrEmpty(this.getDeptId().getOrg())) {//.getIsSendMessage()
                if (egList.size() == 0) {
                    String content = "报告无约定书申请盖章审批拒绝（" + AppContext.current().getProfile().getName() + "）：" + this.getCompositionCustomerId().getName() + "-" + this.getProjectId().getBusinessTypeValue();
                    CreateNotice(content, "业务报告用印审批流程");
                }
            }
        } else {
            String content = "报告审批拒绝（" + AppContext.current().getProfile().getName() + "）：" + this.getCompositionCustomerId().getName() + "-" + this.getProjectId().getBusinessTypeValue();
            CreateNotice(content, "业务报告审批流程");
        }
    }


    public void CreateNotice(String content, String title) throws Exception {
        String auditStar = this.getProjectId().getAuditStart().getYear() + "/" + this.getProjectId().getAuditStart().getDayOfMonth();
        String auditEnd = this.getProjectId().getAuditEnd().getYear() + "/" + this.getProjectId().getAuditEnd().getDayOfMonth();
        content += "-" + auditStar + "至" + auditEnd + " " + this.getReportCode();
        Notice notice = Notice.createNew();
        List<Long> users = new ArrayList<>();
        users.add(this.getCreatedById());
        notice.createNotice(AppContext.current().getProfile().getId(), users, title, content, this.getId(), "Report", null);
    }

    private void validateReport() throws Exception {
        //用印申请不走报告校验
        if (Framework.isNotNullOrEmpty(this.getStatus())) return;

        if (this.getBusinessSubclasses() == 1000 || this.getBusinessSubclasses() == 1001) return;

        if (Framework.isNotNullOrEmpty(this.getSigningPartnerId()) && Framework.isNotNullOrEmpty(this.getSigningAccountantId())) {
            Long partnerId = this.getSigningPartnerId().getId();
            Long accountantId = this.getSigningAccountantId().getId();
            if (Framework.isNotNullOrEmpty(this.getSigningAccountantOtherId())) {
                Long otherId = this.getSigningAccountantOtherId().getId();
                if (partnerId == otherId && accountantId == otherId)
                    throw new BusinessException("签字合伙人、签字会计师、签字会计师（可选）不能为同一个人!");
                if (partnerId == otherId)
                    throw new BusinessException("签字合伙人、签字会计师（可选）不能为同一个人!");
                if (accountantId == otherId)
                    throw new BusinessException("签字会计师、签字会计师（可选）不能为同一个人!");
            }
            if (partnerId == accountantId)
                throw new BusinessException("签字合伙人、签字会计师 不能为同一个人!");
        }

//        Optional<ReportFile> file = this.getReportFiles().stream().filter(f -> f.getFileType() == 10 && f.getAttachmentId() == null).findAny();
//        if (file.isPresent())
//            throw new BusinessException("请上传报告正文!");
    }


    private void submitValidateReport() throws Exception {
        Integer docState = 10;
        Long compositionCustomerId = this.getCompositionCustomerId().getId();
        List<Engagement> engagementList = Engagement.stream().where(w -> w.getCompositionCustomerId().getId() == compositionCustomerId).toList();
        if (Framework.isNotNullOrEmpty(this.getStatus())) return;

//        Long projectId = this.getProjectId().getId();
        Optional<CompositionCustomer> compositionCustomer = CompositionCustomer.stream().where(c -> c.getId() == compositionCustomerId /*&& c.getLevel() == 1*/).findAny();
        if (this.getBusinessSubclasses() != 1000 && this.getBusinessSubclasses() != 1001) {
            if (engagementList.size() == 0 && Framework.isNullOrEmpty(compositionCustomer.get().getParentId())) {
//            if (Framework.isNullOrEmpty(this.getCompositionCustomerId().getCustomerNameId())) {


//                if (compositionCustomer.isPresent()) {
//                    Long MainProjectCustomerId = compositionCustomer.get().getId();
//                    Optional<Engagement> engagementMainProject = Engagement.stream().where(w -> w.getCompositionCustomerId().getId() == MainProjectCustomerId).findAny();
//                    if (!engagementMainProject.isPresent())
//                        throw new BusinessException("此项目未生成业务约定书，无法提交!");
//                    else
//                        docState = engagementMainProject.get().getDocState();
//                }
//            } else {
                throw new BusinessException("请在项目中上传业务约定书后，再提交走审批流程");
//            }
            }
        }

        //给业务约定书赋值
        Optional<Engagement> engagementfrist = engagementList.stream().filter(w -> w.getDocState() == 30).findAny();
        if (engagementfrist.isPresent())
            this.setHaveBusinessAgreement(30);
        else {
            if (engagementList.size() != 0)
                this.setHaveBusinessAgreement(engagementList.stream().findAny().get().getDocState());
            else
                this.setHaveBusinessAgreement(docState);
        }
        if (this.getBusinessSubclasses() == 1000 || this.getBusinessSubclasses() == 1001) return;
        if (this.getReportTypeId().getCode().equals("005") || this.getReportTypeId().getCode().equals("006"))
            return;
        String reviewState = this.getCompositionCustomerId().getDocStateAllE();
        if (Framework.isNullOrEmpty(reviewState) || !reviewState.equals("70"))
            throw new BusinessException("底稿复核未完成，不能出具报告!");

    }

    //用印申请需要校验的
    private void validateSignReport() throws Exception {
        if (Framework.isNullOrEmpty(this.getStatus()) && this.getStatus().equals("Sign")) return;
    }

    @SystemService(args = "deptId,riskLevel")
    public List<EnumDataDTO> getSignOrg(Long deptId, Integer riskLevel) throws Exception {
        List<EnumDataDTO> list = new ArrayList<>();
        Optional<Department> dept = Department.stream().where(r -> r.getId().equals(deptId)).findFirst();
        if (dept.isPresent()) {
            Department org = dept.get().getOrg();
            //分所
            if (Framework.isNotNullOrEmpty(org.getBReportApprovalId()) && riskLevel != 10 && riskLevel != 20) {
                EnumDataDTO dto1 = new EnumDataDTO();
                dto1.setId(10);
                dto1.setText(String.format("%s-分所用印", org.getName()));
                list.add(dto1);
            }
            //总所
            if (Framework.isNotNullOrEmpty(org.getGeneralApprovalId())) {
                EnumDataDTO dto2 = new EnumDataDTO();
                dto2.setId(20);
                dto2.setText(String.format("%s-总所用印", org.getName()));
                list.add(dto2);
            }
        }
        return list;
    }

    @SystemService(args = "query_projectId")
    public List<ReportAuditUnitTreeDTO> getCompositionCustomerTree(Long projectId) throws Exception {
        ArrayList list = new ArrayList();
        List<CompositionCustomer> customers = CompositionCustomer.stream().where(r -> r.getProjectId().getId() == projectId).sortedDescendingBy(o -> o.getSort()).toList();
        if (customers.size() > 0) {
            //Collections.sort(customers, Comparator.comparing(o -> o.getSort()));
            return ReportAuditUnitTreeDTO.Parse(customers);
        }
        return list;
    }

    @SystemService(args = "query_reportId")
    public List<ReportFileDTO> gainReportFiles(Long reportId) throws Exception {
        ArrayList list = new ArrayList();
        Report report = this.findById(reportId);
        if (Framework.isNullOrEmpty(report)) return list;
        Profile pro = AppContext.current().getProfile();
        String PartnerIDCard = "";
        String CPA1IDCard = "";
        String CPA2IDCard = "";
        //当前登录人是签字合伙人
        Boolean isPartner = Framework.isNotNullOrEmpty(report.getSigningPartner());
        if (Framework.isNotNullOrEmpty(report.getSigningPartner())) {
            if (Framework.isNotNullOrEmpty(report.getSigningPartnerId().getStaff().getISCanBeStamped()))
                if (report.getSigningPartnerId().getStaff().getISCanBeStamped() == 10) {
                    isPartner = true;
                    if (Framework.isNotNullOrEmpty(report.getSigningPartnerId().getStaff().getOnlyIdentification()))
                        PartnerIDCard = report.getSigningPartnerId().getStaff().getOnlyIdentification().trim();
                } else {
                    isPartner = false;
                }
            else
                isPartner = false;
        }
        //当前登录人是cpa1
        Boolean isCPA1 = Framework.isNotNullOrEmpty(report.getSigningAccountantId());
        if (Framework.isNotNullOrEmpty(report.getSigningAccountantId())) {
            if (Framework.isNotNullOrEmpty(report.getSigningAccountantId().getStaff().getISCanBeStamped()))
                if (report.getSigningAccountantId().getStaff().getISCanBeStamped() == 10) {
                    isCPA1 = true;
                    if (Framework.isNotNullOrEmpty(report.getSigningAccountantId().getStaff().getOnlyIdentification()))
                        CPA1IDCard = report.getSigningAccountantId().getStaff().getOnlyIdentification().trim();
                } else {
                    isCPA1 = false;
                }
            else
                isCPA1 = false;

        }

        //当前登录人是cpa2
        Boolean isCPA2 = Framework.isNotNullOrEmpty(report.getSigningAccountantOtherId());
        if (Framework.isNotNullOrEmpty(report.getSigningAccountantOtherId())) {
            if (Framework.isNotNullOrEmpty(report.getSigningAccountantOtherId().getStaff().getISCanBeStamped()))
                if (report.getSigningAccountantOtherId().getStaff().getISCanBeStamped() == 10) {
                    isCPA2 = true;
                    if (Framework.isNotNullOrEmpty(report.getSigningAccountantOtherId().getStaff().getOnlyIdentification()))
                        CPA2IDCard = report.getSigningAccountantOtherId().getStaff().getOnlyIdentification().trim();
                } else {
                    isCPA2 = false;
                }
            else
                isCPA2 = false;
        }

        if (report.getReportFiles().size() > 0) {
            Collections.sort(report.getReportFiles(), Comparator.comparing(o -> o.getFileType()));
            for (ReportFile item : report.getReportFiles()) {
                if (Framework.isNotNullOrEmpty(item.getAttachmentId())) {
                    ReportFileDTO dto = new ReportFileDTO();
                    ModelHelper.copyModel(item, dto);
                    AttachmentDTO att = new AttachmentDTO();
                    ModelHelper.copyModel(item.getAttachmentId(), att);
                    dto.setAttachmentId(att);
                    EnumValue enumType = EnumValue.createNew();
                    EnumDataDTO fileType = enumType.gainEnumDataDTO("ReportFileType", item.getFileType());
                    dto.setFileType(fileType);
                    dto.setSignedOn(item.getAttachmentId().getCreatedOn());
                    dto.setReportId(reportId);
                    dto.setProjectManagerName(report.getCompositionCustomerId().getProjectManagerId().getName());
                    if (Framework.isNotNullOrEmpty(item.getSignedById())) {
                        EntityDataDTO signedById = new EntityDataDTO();
                        ModelHelper.copyModel(item.getSignedById(), signedById);
                        dto.setSignedById(signedById);
                    }
                    dto.setIsPartner(isPartner);
                    dto.setIsCPA1(isCPA1);
                    dto.setIsCPA2(isCPA2);
                    dto.setPartnerIDCard(PartnerIDCard);
                    dto.setCpa1IDCard(CPA1IDCard);
                    dto.setCpa2IDCard(CPA2IDCard);
                    list.add(dto);
                }
            }
        }
        return list;
    }

    @JsonIgnore
    public MyTodoListDTO gainWorkFlowDTO(MyTodoListDTO value) throws Exception {
        value.setDepartmentType(this.getReportCode());
        User user = this.getCreatedBy();
        if (Framework.isNotNullOrEmpty(user)) {
            if (Framework.isNotNullOrEmpty(user.getDeptReadOnly())) {
                value.setDepartment(user.getDeptReadOnly().getTreeName());
            }
        }
        String processId = value.getProcess_Id();
        Optional<WorkFlowConfig> workFlowConfiglist = WorkFlowConfig.stream().where(w -> w.getProcessId().equals(processId)).findFirst();
        if (workFlowConfiglist.isPresent()) {
            String deptIdName = user.getDeptReadOnly().getOrgReadOnly().getNickname();
            WorkFlowConfig workFlowConfig = workFlowConfiglist.get();
            value.setWorkFlowName(deptIdName + "-" + workFlowConfig.getWorkFlowTypeId().getName());
        }
        return value;
    }

    @SystemService(args = "reportId")
    public void putReportIsSign(Long reportId) throws Exception {
        Report report = this.findById(reportId);
        report.setIsSign(true);
        report.setSignCompletion(10);
        report.setSignDate(LocalDateTime.now());
    }


    //审计客户数据增加  李一
    private void auditCustomerData() throws Exception {
        Integer businessSubclasses = this.getBusinessSubclasses();
        String businessSubclassesValue = this.getBusinessSubclassesValue();
        LocalDateTime auditStart = this.getProjectId().getAuditStart();//开始时间
        LocalDateTime auditEnd = this.getProjectId().getAuditEnd();//结束时间
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String auditStartString = df.format(auditStart);
        String auditEndString = df.format(auditEnd);
        String startyear = auditStartString.substring(0, 4);
        String endyear = auditEndString.substring(0, 4);
        int aa = Integer.decode(startyear);
        int bb = Integer.decode(endyear);
        String accountingPeriod = auditStartString + "-" + auditEndString;
        //年度财务报表审计  新三板申报财务报表审计  IPO申报财务报表审计  年度财务报表审计
        if (isMeetIndependenceConditions(this)) {
            CompositionCustomer compositionCustomer = this.getCompositionCustomerId();//所属审计Id
            Customer customer = compositionCustomer.getProject().getCustomerNameId();//客户Id
            String customerTypeValue = compositionCustomer.getCustomerTypeValue();//客户类型值
            Integer customerType = compositionCustomer.getProject().getCustomerType();//客户类型
            LocalDateTime auditReportDate = this.getAuditReportDate();//审计报告日
            if (Framework.isNotNullOrEmpty(customer)) {
                Long id = customer.getId();
                Long count = SignatureInformationList.stream().where(s -> s.getAuditCustomerListId().getCustomerId() == id && s.getConfirmationStatus() != 30).count();
//                Long count = AuditCustomerList.stream().where(w -> w.getCustomerId() == id).count();
                if (count < 1) {
                    // List<AuditCustomerList>  = AuditCustomerList.stream().where(w -> w.getCustomerId() == id).toList();
                    AuditCustomerList auditCustomerList3 = AuditCustomerList.createNew();
                    auditCustomerList3.setAuditReportDate(auditReportDate);
                    auditCustomerList3.setAccountingPeriod(accountingPeriod);
                    auditCustomerList3.setCompositionCustomerPageId(this.getCompositionCustomerId().getId());
                    if (customerTypeValue.contains("IPO")) {
                        auditCustomerList3.setSecuritiesVariety("IPO");//证券
                        auditCustomerList3.setDataType(30);//数据类型做类型筛选
                    } else if (customerTypeValue.contains("新三板")) {
                        auditCustomerList3.setSecuritiesVariety("股票");//证券
                        auditCustomerList3.setDataType(20);//数据类型做类型筛选
                    } else if (customerTypeValue.contains("上市公司")) {
                        auditCustomerList3.setSecuritiesVariety("股票");//证券
                        auditCustomerList3.setDataType(10);//数据类型做类型筛选
                    }
                    auditCustomerList3.setReportDeptId(this.getDeptId());//报告部门
                    auditCustomerList3.setTimeMarket(customer.getTimeMarket());//客户上市时间
                    auditCustomerList3.setSecuritiesCode(customer.getSecuritiesCode());//客户证券代码
                    auditCustomerList3.setName(customer.getName());//客户名称
                    auditCustomerList3.setCustomerId(customer.getId());//客户
                    auditCustomerList3.setCustomerType(customerType);//客户类型
                    repository().add(auditCustomerList3);
                    for (int i = 0; i < 5; i++) {
                        String yseraa = bb + ".01-" + bb + ".12";
                        User signingAccountantOtherId = this.getSigningAccountantOtherId();
                        SignatureInformationList auditCustomerList30 = SignatureInformationList.createNew();
                        if (bb < aa) {
                            auditCustomerList30.setSigningPartnerId(null);
                            auditCustomerList30.setSigningAccountantId(null);
                            auditCustomerList30.setSigningAccountantOtherId(null);
                        } else {
                            auditCustomerList30.setSigningPartnerId(this.getSigningPartnerId());
                            auditCustomerList30.setSigningAccountantId(this.getSigningAccountantId());
                            if (Framework.isNotNullOrEmpty(signingAccountantOtherId)) {
                                auditCustomerList30.setSigningAccountantOtherId(signingAccountantOtherId);
                            }
                        }
                        auditCustomerList30.setAuditCustomerListId(auditCustomerList3);//主表
                        auditCustomerList30.setYear(bb);//年度
                        auditCustomerList30.setAuditPeriod(yseraa);//审计期间
                        auditCustomerList30.setConfirmationStatus(10);//确认状态  待确认 已确认 历史记录
                        repository().add(auditCustomerList30);
                        bb = bb - 1;
                    }
                }
            }
        }
    }

    public boolean isMeetIndependenceConditions(Report report) throws Exception {
        LocalDateTime nowTime = LocalDateTime.now();
        int year = nowTime.getYear();
        if (report.getCreatedOn().getYear() != year) {
            return false;
        }
        // 1.年度财务报表    businessSubclassesValue.contains("上市公司");                     年度财务报表审计
        //3. businessSubclassesValue.contains("新三板");    新三板挂牌审计     年度报告审计    申报财务报表审计
        //3. IPO企业    首次申报  申报加期  首次申报（科创板）   申报加期（科创板）   IPO审计  申报财务报表审计
        Project project = report.getProjectId();
        //业务子类
        String businessSubclassesValue = report.getBusinessSubclassesValue();
        //客户类型值名称
        String customerTypeValue = project.getCustomerTypeValue();
        //业务类型
        String businessTypeValue = this.getBusinessSubclassesValue();
        if (Framework.isNullOrEmpty(businessSubclassesValue) || Framework.isNullOrEmpty(customerTypeValue) || Framework.isNullOrEmpty(businessTypeValue))
            return false;
        if (businessSubclassesValue.contains("年度财务报表审计") || businessSubclassesValue.contains("申报财务报表审计")) {
            if (customerTypeValue.contains("上市公司")) {
                if (businessTypeValue.contains("年度财务报表")) {
                    return true;
                }
                return false;
            } else if (customerTypeValue.contains("新三板")) {
                if (businessTypeValue.contains("新三板挂牌审计") || businessTypeValue.contains("年度报告审计")) {
                    return true;
                }
                return false;
            } else if (customerTypeValue.contains("IPO企业") && (customerTypeValue.contains("首次申报") || customerTypeValue.contains("申报加期") || customerTypeValue.contains("年度报告审计") || customerTypeValue.contains("年度报告审计"))) {
                if (businessTypeValue.contains("IPO审计")) {
                    return true;
                }
                return false;
            } else {
                return false;
            }
        }

        return false;
    }

    public void SetCompositionCustomerReportIssue(Boolean iSTrue) throws Exception {
        String ReportIssue = "";
        Long CompositionCustomerId = this.getCompositionCustomerId().getId();
        Long id = this.getId();

        Optional<CompositionCustomer> compositionCustomer = CompositionCustomer.stream().where(w -> w.getId() == CompositionCustomerId).findFirst();
        if (compositionCustomer.isPresent()) {
            List<Report> reportList = this.stream().where(w -> w.getCompositionCustomerId().getId() == CompositionCustomerId && w.getId() != id).toList();
            for (Report r : reportList
            ) {
                ReportIssue += "," + r.getReportIssue();
            }
            if (Framework.isNotNullOrEmpty(ReportIssue))
                ReportIssue = ReportIssue.substring(1, ReportIssue.length() - 1);
            if (iSTrue) {
                ReportIssue += "," + this.getReportIssue();
            }
            compositionCustomer.get().setReportIssue(ReportIssue);
        }
    }

    @SystemService(args = "parentId,managerId,userId")
    public Boolean addReportControl(Long parentId, Long managerId, Long userId) throws Exception {
        Optional<CompositionCustomer> cc = parentId == null ? Optional.empty() : CompositionCustomer.stream().filter(item -> item.getId().equals(parentId)).findFirst();
        return cc.isPresent() ? cc.get().getProjectManagerId().getId().equals(userId) ? false : !userId.equals(managerId) : !userId.equals(managerId);
    }

    @SystemService(args = "compositionCustomerId,sealId")
    public String mainOrSubProject(Long compositionCustomerId, Long sealId) throws Exception {
        if (!AppContext.current().getProfile().getDepartmentalAttributes()) {
            throw new BusinessException("职能部门,不允许维护用印");
        }
        Optional<CompositionCustomer> compositionCustomer = CompositionCustomer.stream().filter(item -> item.getId().equals(compositionCustomerId)).findFirst();

        Optional<Engagement> engagementApproved = Engagement.stream().filter(w -> w.getDocState() == 30 && w.getCompositionCustomerId().getId().equals(compositionCustomerId)).findAny();//当前项目下审批通过的业务约定书
        Optional<Engagement> engagementUnsubmitted = Engagement.stream().filter(w -> (w.getDocState() == 10 || w.getDocState() == 100) && w.getCompositionCustomerId().getId().equals(compositionCustomerId)).findAny();//当前项目未提交或已拒绝的业务约定书
        Optional<Engagement> engagementPending = Engagement.stream().filter(w -> w.getDocState() == 20 && w.getCompositionCustomerId().getId().equals(compositionCustomerId)).findAny();//当前项目下审批中的业务约定书


        if (engagementApproved.isPresent()) {
            saveSealData(sealId, true);
            return "1";  //正常审批
        } else if (engagementPending.isPresent()) {
            return "3";// 业务约定书审批中，无法提交
        } else if (engagementUnsubmitted.isPresent()) {
            saveSealData(sealId, false);
            return "2"; //特殊流程
        } else {
            if (compositionCustomer.get().getParentId() == null) {
                return "4";     //主项目没有业务约定书，不允许提交用印
            } else {
                return "1";
            }
//            else {   //子项目
//                Long parentId = compositionCustomer.get().getParentId().getId();
//                Optional<Engagement> engagementParent = Engagement.stream().filter(w -> w.getCompositionCustomerId().getId().equals(parentId)).findAny();
//                if (engagementParent.isPresent()) {
//                    saveSealData(sealId, true);
//                    return "1";
//                } else {
//                    return "4";//不存在业务约定书，无法提交
//                }
//            }

        }
    }

    private void saveSealData(Long sealId, Boolean flag) throws Exception {
        Report report = Report.stream().where(data -> data.getId().equals(sealId)).findFirst().get();
        report.setIsExistenceAgreement(flag);
        this.repository().add(report);
    }

    @SystemService(args = "renderForm")
    public List<ComboboxDTO> queryItemByRenderForm(String renderForm) throws Exception {

        Optional<FormPage> formPage = FormPage.stream().where(w -> w.getCode() == "RegistFormQuery").findFirst();
        if (!formPage.isPresent())
            throw new BusinessException(String.format("您无权操作此表单,或表单不存在 “%s”!", "RegistFormQuery"));

        Long formPageId = formPage.get().getId();
        List<FormPage> list = FormPage.stream().where(w -> w.getFormPageId() != null && w.getFormPageId().getId() == formPageId).toList();

        List<ComboboxDTO> gainList = new ArrayList<>();

        list.forEach(f -> {
            ComboboxDTO dto = new ComboboxDTO();
            dto.setId(f.getId().toString());
            dto.setText("财政RPA-" + f.getName());
            gainList.add(dto);
        });

        //排序
        Collections.sort(gainList, Comparator.comparing(o -> o.getText()));

        return gainList;
    }

    @SystemService(args = "type,compositionId")
    public Long queryReportOfficeId(String type, Long compositionId) throws Exception {
        if ("10".equals(type)) {   //报告选择分所
            type = "20";
        } else {              //报告选择总所
            type = "10";
        }
        Optional<CompositionCustomer> compositionCustomer = CompositionCustomer.stream().where(data -> data.getId().equals(compositionId)).findFirst();
        Long projectDeptId = compositionCustomer.get().getProjectId().getDeptId().getId();
        return getOfficeId(type, projectDeptId);
    }

    private Long getOfficeId(String type, Long deptId) throws Exception {
        Optional<Department> optionalDepartment = Department.stream().where(data -> data.getId().equals(deptId)).findFirst();
        if (optionalDepartment.get().getUnitAttributes() == Integer.parseInt(type)) {
            return optionalDepartment.get().getId();
        } else {
            if (optionalDepartment.get().getParentId() != null)
                return getOfficeId(type, optionalDepartment.get().getParentId().getId());
            else
                return null;
        }

    }

//    private void getParentDept(Department department,Long deptId,String type) throws Exception{
//
//        Optional<Department> optionalDepartment = Department.stream().where(data -> data.getId().equals(deptId)).findFirst();
//        if(optionalDepartment.isPresent() && optionalDepartment.get().getParentId() != null){
//            if(optionalDepartment.get().getParentId().getUnitAttributes() == Integer.parseInt(type)){
//                department.setId(optionalDepartment.get().getParentId().getId());
//            }else{
//                getParentDept(department,optionalDepartment.get().getParent().getId(),type);
//            }
//        }
//    }

//    private void getChildren(StringBuilder sb, Long id) throws Exception{
//        sb.append(id + "','");
//        List<Department> childrenList = Department.stream().where(data -> data.getParentId().getId().equals(id)).toList();
//        for(int i = 0,size = childrenList.size();i < size;i++){
//            Long childId = childrenList.get(i).getId();
//            getChildren(sb,childId);
//        }
//    }

    @SystemService(args = "reportId")
    public Map<String, String> getShowData(Long reportId) throws Exception {
        Map<String, String> resultMap = new HashMap();
        Optional<Report> optionalReport = Report.stream().where(data -> data.getId().equals(reportId)).findFirst();
        if (optionalReport.isPresent()) {
            Report report = optionalReport.get();
            resultMap.put("projectShowName", report.getProjectShowName());
            resultMap.put("reportCode", report.getReportCode());
            resultMap.put("signingAccountant", report.getSigningAccountantId().getName());
            resultMap.put("signingAccountantOther", report.getSigningAccountantOtherId() == null ? "" : report.getSigningAccountantOtherId().getName());
        }

        return resultMap;
    }
}
