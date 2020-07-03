package com.dxn.model.extend;

import com.dxn.custom.util.csfile.CsUploadFileUtil;
import com.dxn.dto.AuditResultsDTO;
import com.dxn.dto.FinancialreportitemDTO;
import com.dxn.dto.OwnerequitychangedDTO;
import com.dxn.dto.RegistFormFileDTO;
import com.dxn.model.entity.RegistFormEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.ResponseEntity;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.configuration.DxnConfig;
import com.dxn.system.context.AppContext;
import com.dxn.system.dto.workFlow.MyTodoListDTO;
import com.dxn.system.dto.workFlow.WorkFlowNode;
import com.dxn.system.exception.BusinessException;
import com.dxn.system.export.ExportCellDTO;
import com.dxn.system.io.FileHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "Rpt_RegistForm")
public class RegistForm extends RegistFormEntity {

    private static final String COLUMN_NAME = "---ColumnName---";
    private static final String REPORT_ITEM_DISPLAY_NAME = "报表项目显示名称";
    private static final String REPORT_ITEM_NAME = "报表项目名称";
    private static final String REPORT_FILE_NAME = "报表项目名称";
    private static final String REPORT_SHEET_NAME = "权益变动";

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        if (Framework.isNotNullOrEmpty(this.getReporter()) && Framework.isNullOrEmpty(this.getTaskName()) && this.getDocState() != 100)
            this.setDocState(10);

        Report r = this.getReportId();
        Project p = r.getProjectId();
        if (p.getCustomerTypeValue().indexOf("非证券") != -1) {
            this.setIsSecuritiesBusiness(20);
        } else
            this.setIsSecuritiesBusiness(10);
        CompositionCustomer cc = r.getCompositionCustomerId();
//        Long projectId = cc.getProjectId().getId();
//        Optional<Project> optionalProject = Project.stream().where(data -> data.getId().equals(projectId)).findFirst();
//        Long customerId = optionalProject.get().getCustomerNameId().getId();
//        Optional<Customer> optionalCustomer = Customer.stream().where(data -> data.getId().equals(customerId)).findFirst();
        Long id = cc.getId();
        List<Engagement> engagementList = Engagement.stream().where(w -> w.getCompositionCustomerId().getId() == id && w.getDocState() == 30 && w.getAddOrReference() == 10).sortedDescendingBy(o -> o.getCreatedOn()).toList();//

//        if (Framework.isNullOrEmpty(this.getUSCCode()))
//            this.setUSCCode(optionalCustomer.get().getUSCCode());
//        if(Framework.isNullOrEmpty(this.getSecuritiesCode()))
//            this.setSecuritiesCode(optionalCustomer.get().getSecuritiesCode());
        if (Framework.isNotNullOrEmpty(cc.getCustomerNameId())) {
            this.setDistrict(p.getCustomerNameId().getDistrict());
            this.setCity(p.getCustomerNameId().getCity());
            this.setProvince(p.getCustomerNameId().getProvince());
            this.setCounty(p.getCustomerNameId().getCounty());
            this.setIsMainProject(10);
        } else {
            this.setIsMainProject(20);
        }
        this.setCustomerName(cc.getName());
        this.setAuditStart(p.getAuditStart());
        this.setAuditEnd(p.getAuditEnd());
        this.setFirstOrContinuous(p.getFirstOrContinuous());
        this.setUndertakesDate(p.getCustomerNameId().getUndertakesDate());
        if (p.getFirstOrContinuous() == 10) {
            this.setUndertakYears(0);
        } else {
            this.setUndertakYears(setUndertakYears(p.getCustomerNameId().getUndertakesDate()));
        }
        this.setFormerFirm(p.getCustomerNameId().getFormerFirm());

        this.setReportId(r);
        this.setReportCode(r.getReportCode());
        this.setReportTypeId(r.getReportTypeId());
        this.setMergingMonomer(r.getMergingMonomer());
        this.setAuditOpinion(r.getAuditOpinion());
        this.setBusinessType(p.getBusinessType());
        this.setBusinessTypeValue(p.getBusinessTypeValue());
        this.setBusinessSubclasses(r.getBusinessSubclasses());
        this.setBusinessSubclassesValue(r.getBusinessSubclassesValue());
        if (engagementList.size() > 0) {
            this.setEngagementCode(engagementList.get(0).getCode());
            this.setDateOfSign(engagementList.get(0).getDateOfSign());
            this.setClassification(engagementList.get(0).getClassification());
            Double tolAmount = 0.00;
            for (Engagement item : engagementList) {
                if (Framework.isNotNullOrEmpty(item.getAmount()))
                    tolAmount += item.getAmount();
            }
            this.setAmount(setRegistFormAmount(r, p, tolAmount));
        } else {
            this.setAmount(0.0);
        }
        this.setDeptId(p.getDeptId());
        this.setPracticeId(p.getDeptId().getOrg());
        this.setAuditReportDate(r.getAuditReportDate());
        this.setSigningPartnerId(r.getSigningPartnerId());
        if (Framework.isNotNullOrEmpty(r.getSigningPartnerId())) {
            this.setSigningPartnerDeptId(r.getSigningPartnerId().getDept().getOrg());
            if (Framework.isNotNullOrEmpty(r.getSigningPartnerId().getStaff().getStaffPositionId()))
                this.setSigningPartnerPosition(r.getSigningPartnerId().getStaff().getStaffPositionId().getName());
            this.setSigningPartnerCPAQualification(SetCPAQualification(r.getSigningPartnerId().getStaff()));
        }
        this.setSigningAccountantId(r.getSigningAccountantId());
        if (Framework.isNotNullOrEmpty(r.getSigningAccountantId())) {
            this.setSigningAccountantDeptId(r.getSigningAccountantId().getDept().getOrg());
            if (Framework.isNotNullOrEmpty(r.getSigningAccountantId().getStaff().getStaffPositionId()))
                this.setSigningAccountantPosition(r.getSigningAccountantId().getStaff().getStaffPositionId().getName());
            this.setSigningAccountantCPAQualification(SetCPAQualification(r.getSigningAccountantId().getStaff()));
        }
        this.setSigningAccountantOtherId(r.getSigningAccountantOtherId());
        if (Framework.isNotNullOrEmpty(r.getSigningAccountantOtherId())) {
            this.setSigningAccountantOtherDeptId(r.getSigningAccountantOtherId().getDept().getOrg());
            if (Framework.isNotNullOrEmpty(r.getSigningAccountantOtherId().getStaff().getStaffPositionId()))
                this.setSigningAccountantOtherPosition(r.getSigningAccountantOtherId().getStaff().getStaffPositionId().getName());
            this.setSigningAccountantOtherCPAQualification(SetCPAQualification(r.getSigningAccountantOtherId().getStaff()));
        }
        if ((r.getCreatedBy().getDept().getOrg().getUnitAttributes() == 20 && r.getReportIssue() == 20) || (r.getCreatedBy().getDept().getOrg().getUnitAttributes() == 10 && r.getReportIssue() == 20)) {
            this.setQualityReviewManagerId(r.getCreatedBy().getDept().getOrg().getHeadquartersManagerId());
            if (Framework.isNotNullOrEmpty(r.getCreatedBy().getDept().getOrg().getHeadquartersManagerId())) {
                this.setQualityReviewManagerDeptId(r.getCreatedBy().getDept().getOrg().getHeadquartersManagerId().getDept().getOrg());
                if (r.getCreatedBy().getDept().getOrg().getHeadquartersManagerId().getStaff() != null) {
                    if (Framework.isNotNullOrEmpty(r.getCreatedBy().getDept().getOrg().getHeadquartersManagerId().getStaff().getStaffPositionId()))
                        this.setQualityReviewManagerPosition(r.getCreatedBy().getDept().getOrg().getHeadquartersManagerId().getStaff().getStaffPositionId().getName());
                }
            }
        } else {
            this.setQualityReviewManagerId(r.getCreatedBy().getDept().getOrg().getReviewManagerId());
            if (Framework.isNotNullOrEmpty(r.getCreatedBy().getDept().getOrg().getReviewManagerId())) {
                this.setQualityReviewManagerDeptId(r.getCreatedBy().getDept().getOrg().getReviewManagerId().getDept().getOrg());
                if (Framework.isNotNullOrEmpty(r.getCreatedBy().getDept().getOrg().getReviewManagerId().getStaff())) {
                    if (Framework.isNotNullOrEmpty(r.getCreatedBy().getDept().getOrg().getReviewManagerId().getStaff().getStaffPositionId()))
                        this.setQualityReviewManagerPosition(r.getCreatedBy().getDept().getOrg().getReviewManagerId().getStaff().getStaffPositionId().getName());
                }


            }
        }
        this.setIsInternalControlOrAnnualReportAudit(20);
        this.setIsInternalControlConsultation(20);
        this.setFinancialReportType(AppHelper.manage().getFinancialReportTypeUtil().getFinancialReportType(cc.getCustomerType(), r.getBusinessSubclasses() - 2));
        if (r.getBusinessSubclasses() == 13 && r.getReportType().getName().equals("审字")) {
            this.setSecuritiesBusinessType(40);
        } else if (r.getBusinessSubclasses() == 13 && r.getReportType().getName().equals("专审字")) {
            this.setSecuritiesBusinessType(110);
        } else {
            this.setSecuritiesBusinessType(AppHelper.manage().getSecuritiesBusinessTypeUtil().getSecuritiesBusinessType(cc.getCustomerType(), r.getBusinessSubclasses() - 2));
        }
    }

    @Override
    public void onValidate() throws Exception {
        super.onValidate();
//        if(!AppContext.current().getProfile().getDepartmentalAttributes() && StringUtils.isBlank(this.getQueryOrBusiness())){
//            throw new BusinessException("职能部门,不允许维护报备");
//        }

    }

    //插入前
    @Override
    public void onInserting() throws Exception {
        super.onInserting();
        this.setDocState(5);
    }

    @Override
    public void onInserted() throws Exception {
        super.onInserted();
        if (Framework.isNullOrEmpty(this.getReport().getRegistFormId())) {
            this.getReport().setRegistFormId(this.getId());
            this.repository().commit();
        }
    }

    @Override
    public void onEndWorkflow(WorkFlowNode node) throws Exception {
        super.onEndWorkflow(node);
        String content = "报备审批通过：" + this.getReport().getCompositionCustomerId().getName() + "-" + this.getReport().getProjectId().getBusinessTypeValue();
        CreateNotice(content);
    }

    @Override
    public void onWorkFlowBack(WorkFlowNode node) throws Exception {//退回时触发
        super.onWorkFlowBack(node);
        String content = "报备审批拒绝（" + AppContext.current().getProfile().getName() + "）：" + this.getReport().getCompositionCustomerId().getName() + "-" + this.getReport().getProjectId().getBusinessTypeValue();
        CreateNotice(content);
    }


    @Override
    public void onUpdating() throws Exception {
        super.onUpdating();
        this.getReport().setReportingStatus(this.getDocState());
//        if (Framework.isNotNullOrEmpty(this.getOriginalValue()))
//            if (this.getDocState() == 10 && this.getOriginalValue().getDocState() == 30)
//                this.getReport().setDocState(10);

    }

    @Override
    public boolean canDelete() {
        super.canDelete();
        if (this.getDocState() == 30) return true;
        return false;
    }

    public void CreateNotice(String content) throws Exception {
        String auditStar = this.getReport().getProjectId().getAuditStart().getYear() + "/" + this.getReport().getProjectId().getAuditStart().getDayOfMonth();
        String auditEnd = this.getReport().getProjectId().getAuditEnd().getYear() + "/" + this.getReport().getProjectId().getAuditEnd().getDayOfMonth();
        content += "-" + auditStar + "至" + auditEnd;
        Notice notice = Notice.createNew();
        List<Long> users = new ArrayList<>();
        users.add(this.getSubmitById());
        notice.createNotice(AppContext.current().getProfile().getId(), users, "报备审批流程", content, this.getId(), "RegistForm", null);

    }

    private String SetCPAQualification(Staff s) throws Exception {
        Long staffid = s.getId();
        List<CPAQualification> cpa = CPAQualification.stream().where(w -> w.getStaffId().getId() == staffid && w.getAccountantType() == 10).toList();
        if (cpa.size() > 0)
            return cpa.get(0).getCode();
        else
            return "";
    }


    private double setRegistFormAmount(Report r, Project p, Double tolAmount) throws Exception {
        Double Amount;
        long compositionCustomerId = r.getCompositionCustomerId().getId();
        Long id = this.getId();
        Optional<RegistForm> rFormList = RegistForm.stream().where(w -> w.getReportId().getCompositionCustomerId().getId() == compositionCustomerId && w.getId() != id && w.getAmount() > 0).findFirst();
        if (!rFormList.isPresent()) {
            Amount = tolAmount;
        } else {
            if (rFormList.get().getReportId().getMergingMonomer() == 20 && rFormList.get().getReportTypeId().getShortName().equals("审字"))
                Amount = 0.00;
            else {
                if (r.getMergingMonomer() == 20 && r.getReportTypeId().getShortName().equals("审字")) {
                    Amount = tolAmount;
                    rFormList.get().setAmount(0.00);
                } else if (rFormList.get().getReportId().getMergingMonomer() == 10 && rFormList.get().getReportTypeId().getShortName().equals("审字"))
                    Amount = 0.00;
                else if (r.getMergingMonomer() == 10 && r.getReportTypeId().getShortName().equals("审字")) {
                    Amount = tolAmount;
                    rFormList.get().setAmount(0.00);
                } else
                    Amount = 0.00;
            }
        }
        return Amount;
    }

    //连续承接年限计算
    private int setUndertakYears(LocalDateTime dt) throws Exception {
        int num = 0;
        if (LocalDateTime.now().getYear() > dt.getYear()) {
            num = LocalDateTime.now().getYear() - dt.getYear();
        }
        if (LocalDateTime.now().getMonth().equals(12) && LocalDateTime.now().getDayOfMonth() == 31) {
            num += 1;
        }


        return num;

    }

    /**
     * 维护业务报备审计结果
     *
     * @param
     * @return
     * @throws Exception http://192.168.40.14:8080/RegistForm-getAuditResultsUploadCsData-Query.do?auditProjectId=eabbec1d-f55e-47a3-b7f4-8ab4c6b01509
     */
    @SystemService(args = "auditProjectId,mergingMonomer")
    public List<AuditResultsDTO> getAuditResultsUploadCsData(Long auditProjectId, Integer mergingMonomer) throws Exception {
        List<AuditResultsDTO> AuditResultss = new ArrayList();
        String num = mergingMonomer == 10 ? "0" : "2";//审计后单体合并
        String numBefore = mergingMonomer == 10 ? "0" : "3";//审计前单体合并

        try {
            CsUploadFileUtil csUploadFileUtil = (CsUploadFileUtil) AppHelper.getBean("csUploadFileUtil");
            CompositionCustomer compositionCustomer = CompositionCustomer.createNew();
            CompositionCustomer cc = compositionCustomer.findById(auditProjectId);
            ManuscriptItem item = ManuscriptItem.createNew();
            Map<Integer, String> maplist = new HashMap<Integer, String>();
            maplist.put(10, item.csUploadPath(cc.getCode(), 10));
            Integer year = cc.getProjectId().getAuditEnd().getYear();
            List<FinancialreportitemDTO> FinancialreportitemList = csUploadFileUtil.processFinancialreportitems(maplist);
            AuditResultsDTO o = new AuditResultsDTO();
            Optional<FinancialreportitemDTO> zc = getDataOption(FinancialreportitemList, num, "资产总计", year);
            Optional<FinancialreportitemDTO> fz = getDataOption(FinancialreportitemList, num, "负债合计", year);
            Optional<FinancialreportitemDTO> lr = getDataOption(FinancialreportitemList, num, "利润总额", year);
            Optional<FinancialreportitemDTO> yj = getDataOption(FinancialreportitemList, num, "应交税费", year);
            Optional<FinancialreportitemDTO> gd = getDataOption(FinancialreportitemList, num, "所有者权益(或股东权益)合计", year);
            Optional<FinancialreportitemDTO> ss = getDataOption(FinancialreportitemList, num, "实收资本", year);
            Optional<FinancialreportitemDTO> yy = getDataOption(FinancialreportitemList, num, "营业收入", year);
            Optional<FinancialreportitemDTO> jlr = getDataOption(FinancialreportitemList, num, "净利润", year);

            Optional<FinancialreportitemDTO> zcBefore = getDataOption(FinancialreportitemList, numBefore, "资产总计", year);
            Optional<FinancialreportitemDTO> fzBefore = getDataOption(FinancialreportitemList, numBefore, "负债合计", year);
            Optional<FinancialreportitemDTO> lrBefore = getDataOption(FinancialreportitemList, numBefore, "利润总额", year);
            Optional<FinancialreportitemDTO> yjBefore = getDataOption(FinancialreportitemList, numBefore, "应交税费", year);
            Optional<FinancialreportitemDTO> gdBefore = getDataOption(FinancialreportitemList, numBefore, "所有者权益(或股东权益)合计", year);
            Optional<FinancialreportitemDTO> ssBefore = getDataOption(FinancialreportitemList, numBefore, "实收资本", year);
            Optional<FinancialreportitemDTO> yyBefore = getDataOption(FinancialreportitemList, numBefore, "营业收入", year);
            Optional<FinancialreportitemDTO> jlrBefore = getDataOption(FinancialreportitemList, numBefore, "净利润", year);

            if (zcBefore.isPresent()) {
                o.setTotalAssetsPreaudit(zcBefore.get().getNoAuditCYFinalAmounts());
            }
            if (zc.isPresent()) {
                o.setTotalAssetsAfterAudit(zc.get().getAlreadyAuditCYFinalAmounts());
            }

            if (fzBefore.isPresent()) {
                o.setTotalLiabilitiesPreaudit(fzBefore.get().getNoAuditCYFinalAmounts());
            }
            if (fz.isPresent()) {
                o.setTotalLiabilitiesAfterAudit(fz.get().getAlreadyAuditCYFinalAmounts());
            }
            if (lrBefore.isPresent()) {
                o.setTotalProfitPreaudit(lrBefore.get().getNoAuditCYAmounts());
            }
            if (lr.isPresent()) {
                o.setTotalProfitAfterAudit(lr.get().getAlreadyAuditCYAmounts());
            }
            if (yjBefore.isPresent()) {
                o.setDutyPayablePreaudit(yjBefore.get().getNoAuditCYFinalAmounts());
            }
            if (yj.isPresent()) {
                o.setDutyPayableAfterAudit(yj.get().getAlreadyAuditCYFinalAmounts());
            }
            if (gdBefore.isPresent()) {
                o.setShareholderEquityPreaudit(gdBefore.get().getNoAuditCYFinalAmounts());
            }
            if (gd.isPresent()) {
                o.setShareholderEquityAfterAudit(gd.get().getAlreadyAuditCYFinalAmounts());
            }
            if (ssBefore.isPresent()) {
                o.setEquityPreaudit(ssBefore.get().getNoAuditCYFinalAmounts());
            }
            if (ss.isPresent()) {
                o.setEquityAfterAudit(ss.get().getAlreadyAuditCYFinalAmounts());
            }
            if (yyBefore.isPresent()) {
                o.setOperatIncomePreaudit(yyBefore.get().getNoAuditCYAmounts());
            }
            if (yy.isPresent()) {
                o.setOperatIncomeAfterAudit(yy.get().getAlreadyAuditCYAmounts());
            }
            if (jlrBefore.isPresent()) {
                o.setNetProfitPreaudit(jlrBefore.get().getNoAuditCYAmounts());
            }
            if (jlr.isPresent()) {
                o.setNetProfitAfterAudit(jlr.get().getAlreadyAuditCYAmounts());
            }
            AuditResultss.add(o);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return AuditResultss;
    }

    private Optional<FinancialreportitemDTO> getDataOption(List<FinancialreportitemDTO> FinancialreportitemList, String adjustmentType, String reportItemName, Integer fiscalYear) {
        return FinancialreportitemList.stream().filter(f -> f.getAdjustmentType().equals(adjustmentType) && f.getReportItemName().equals(reportItemName) && f.getFiscalYear().equals(fiscalYear)).findFirst();
    }

    /**
     * 生成财务报表
     *
     * @param
     * @return
     * @throws Exception http://192.168.40.28:8899/RegistForm-getFinancialStatementsCsData-Query.do?auditProjectId=3&mergingMonomer=20
     */
    @SystemService(args = "registFormId,auditProjectId,mergingMonomer,attid")
    public List<RegistFormFileDTO> getFinancialStatementsCsData(Long registFormId, Long auditProjectId, Integer mergingMonomer, Long attid) throws Exception {
        List<RegistFormFileDTO> RegistFormFiles = new ArrayList();

        Optional<RegistFormFile> rff = RegistFormFile.stream().where(d -> d.getRegistFormId().getId() == registFormId && d.getAttachmentType().equals("report")).findFirst();
        RegistFormFileDTO f = null;
        Attachment atth = null;

        Optional<CompositionCustomer> optionalCompositionCustomer = CompositionCustomer.stream().where(data -> data.getId().equals(auditProjectId)).findFirst();

        if (Framework.isNotNullOrEmpty(attid)) {
            if (rff.isPresent()) {
                RegistFormFile ref = rff.get();
                attid = ref.getAttachmentId().getId();
            }
            //Optional<Attachment> att = Attachment.stream().where(d ->d.getId() == attid).findFirst();
            atth = Attachment.createNew();
            atth = atth.findById(attid);

            f = getRepostExcelData(atth, auditProjectId, mergingMonomer, optionalCompositionCustomer);
            f.setAttachmentId(attid);
            f.setAttachmentName(atth.getName());
            f.setIsEx("1");
        } else {
            f = getRepostExcelData(atth, auditProjectId, mergingMonomer, optionalCompositionCustomer);
            File file = new File(f.getFilePath());

            long fileS = file.length();
            DecimalFormat df = new DecimalFormat("#.00");


            String size = df.format((double) fileS / 1024) + "KB";

            Attachment attachment = Attachment.createNew();
            attachment.setName(f.getFileNAme());
            attachment.setPath(f.getPath());
            attachment.setMd5(getFileNameNoEx(file.getName()));
            attachment.setSize(size);
            String suffix = f.getFileNAme().substring(f.getFileNAme().lastIndexOf("."));
            attachment.setSuffix(suffix);

            repository().add(attachment);
            repository().commit();

            f.setAttachmentName(attachment.getName());
            f.setAttachmentId(attachment.getId());


//            RegistFormFile rf = RegistFormFile.createNew();
//            rf.setAttachmentId(attachment);
//            rf.setRegistFormId(registForm.get());
//            rf.setAttachmentType("report");
//            repository().add(rf);
//            repository().commit();
        }
        f.setAttachmentType("report");
        RegistFormFiles.add(f);
        return RegistFormFiles;
    }

    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }


    /**
     * 生成财务报表
     *
     * @param
     * @return
     * @throws Exception http://192.168.40.28:8899/RegistForm-getRepostExcelData-Query.do?auditProjectId=3&mergingMonomer=20
     */
//    @SystemService(args = "auditProjectId,mergingMonomer")
    public RegistFormFileDTO getRepostExcelData(Attachment atth, Long auditProjectId, Integer mergingMonomer, Optional<CompositionCustomer> optionalCompositionCustomer) throws Exception {
        ResponseEntity response = new ResponseEntity();
        //单体还是合并
        String num = mergingMonomer == 10 ? "0" : "2";


        RegistFormFileDTO f = new RegistFormFileDTO();
//        try {
        CsUploadFileUtil csUploadFileUtil = (CsUploadFileUtil) AppHelper.getBean("csUploadFileUtil");
        CompositionCustomer compositionCustomer = CompositionCustomer.createNew();
        CompositionCustomer cc = compositionCustomer.findById(auditProjectId);
        ManuscriptItem item = ManuscriptItem.createNew();
        Map<Integer, String> maplist = new HashMap<Integer, String>();
        maplist.put(10, item.csUploadPath(cc.getCode(), 10));
        //审计年度

        Integer year = cc.getProjectId().getAuditEnd().getYear();
//            Integer year = 2018;
        //数据源
        List<FinancialreportitemDTO> FinancialreportitemList = csUploadFileUtil.processFinancialreportitems(maplist);
        List<OwnerequitychangedDTO> ownerequitychangedList = csUploadFileUtil.processOwnerequitychanged(maplist);

        String newfile = "";
        String filename = "";
        if (num.equals("2")) {
            filename = "财政-业务报备财务报表2019模板(合并).xlsx";
            newfile = copyTemplate("财政-业务报备财务报表2019模板(合并).xlsx", cc.getCode(), atth);
        } else {
            filename = "财政-业务报备财务报表2019模板(单体).xlsx";
            newfile = copyTemplate("财政-业务报备财务报表2019模板(单体).xlsx", cc.getCode(), atth);
        }

        readExcel(newfile, FinancialreportitemList, ownerequitychangedList, num, year, cc);
        f.setPath(String.format("%s/%s/报备", super.gainAttachmentPath(), cc.getCode()));
        f.setFileNAme(filename);
        f.setFilePath(newfile);
//            response.setCode(200);
//        } catch (Exception ex) {
//            response.setMessages(ex.getMessage());
//            response.setCode(500);
//        }
        return f;
    }

    public void readExcel(String file, List<FinancialreportitemDTO> FinancialreportitemList, List<OwnerequitychangedDTO> ownerequitychangedList, String num, Integer year, CompositionCustomer compositionCustomer) {
        File fi = new File(file);
        InputStream is = null;
        FileOutputStream out = null;
        try {
            // 验证文件是否合法
            if (!validateExcel(fi.getName())) {
                Framework.log.info("非法文件！！！！");
            }
            // 判断文件的类型，是2003还是2007
            boolean is2003Excel = true;
            if (is2007Excel(fi.getName())) {
                is2003Excel = false;
            }
            // 调用本类提供的根据流读取的方法
            is = new FileInputStream(fi);
            // 根据版本选择创建Workbook的方式
            Workbook wb = null;
            if (is2003Excel) {
                wb = new HSSFWorkbook(is);
            } else {
                wb = new XSSFWorkbook(is);
            }
            wb.setForceFormulaRecalculation(true);
            Map<Sheet, Integer> hiddenMap = readFile(FinancialreportitemList, ownerequitychangedList, wb, num, year, compositionCustomer);
            for (Sheet item : hiddenMap.keySet()) {
                item.shiftRows(hiddenMap.get(item) + 1, item.getPhysicalNumberOfRows(), -1);
            }
            out = new FileOutputStream(file);
            wb.write(out);
            is.close();
            out.flush();
            out.close();
            Framework.log.info("----------------------完成--------------------------");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                    out.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }
    }

    private Map<Sheet, Integer> readFile(List<FinancialreportitemDTO> financialreportitemList, List<OwnerequitychangedDTO> ownerequitychangedList, Workbook wb, String num, Integer year, CompositionCustomer compositionCustomer) throws Exception {

        // sheet循环
        int sheetNum = sheetCirculation(wb);
        Map<Sheet, Integer> hiddenMap = new HashMap();
        for (int i = 0; i < sheetNum; i++) {
            //表头字段
            List<DataColumnName> dataColumnNamelist = new ArrayList<>();
            Sheet sheet = wb.getSheetAt(i);
            String sheetName = sheet.getSheetName();
            //是否是权益变动页签
            int change = 0;
            if (sheetName.indexOf(REPORT_SHEET_NAME) != -1) {
                change = 1;
            }
            dataColumnNamelist = readTabRows(sheet, dataColumnNamelist, financialreportitemList, ownerequitychangedList, num, year, change, hiddenMap, compositionCustomer);
        }
        return hiddenMap;
    }

    /**
     * 读取表头
     *
     * @param sheet
     * @param dataColumnNamelist
     * @return
     * @throws Exception
     */
    private List<DataColumnName> readTabRows(Sheet sheet, List<DataColumnName> dataColumnNamelist, List<FinancialreportitemDTO> financialreportitemList, List<OwnerequitychangedDTO> ownerequitychangedList, String num, Integer year, int change, Map<Sheet, Integer> hiddenMap, CompositionCustomer compositionCustomer) throws Exception {
        List<Datacolumninfo> datacolumninfos = new ArrayList<>();
        if (change == 0) {
            datacolumninfos = Datacolumninfo.stream().where(d -> d.getTableCode().equals("B0001")).toList();
        } else {
            datacolumninfos = Datacolumninfo.stream().where(d -> d.getTableCode().equals("B0020")).toList();
        }
        //表头集合
        List<String> tabList = new ArrayList<>();
        //是否存在
        boolean isflag = false;
        // 总行数
        int totalRows = sheet.getPhysicalNumberOfRows();
        // 总列数
        int totalCells = 0;
        //数据开始行
        int startRow = 0;
        //结束列
        //int endColumn
        for (int i = 0; i < totalRows; i++) {
            CellAddress ca = new CellAddress(i, 0);
            Comment comment = sheet.getCellComment(ca);
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(0);
            if (comment != null && cell.getCellComment() != null && Framework.isNotNullOrEmpty(cell.getCellComment().getString().toString()) && cell.getCellComment().getString().toString().indexOf(COLUMN_NAME) != -1) {
                totalCells = sheet.getRow(i).getPhysicalNumberOfCells();
                startRow = i;
                hiddenMap.put(sheet, i);
            }
            if (comment != null && cell.getCellComment() != null && Framework.isNotNullOrEmpty(cell.getCellComment().getString().toString()) && cell.getCellComment().getString().toString().indexOf("---CompanyName---") != -1) {

                if (compositionCustomer != null) {
                    if (CellType.FORMULA != cell.getCellTypeEnum()) {
                        cell.setCellValue(cell.getStringCellValue() + compositionCustomer.getName());
                    }
                }

            }
            for (int j = 0; j < totalCells; j++) {
                Cell celld = row.getCell(j);


                if (celld.getCellComment() == null) continue;
                String commentd = celld.getCellComment().getString().toString();
                String fild = getParamString(commentd, COLUMN_NAME);
                //list.contains(o);list中是否包含某一值
                if (Framework.isNullOrEmpty(fild)) continue;

                if (tabList.contains(fild)) {
                    isflag = true;
                } else {
                    tabList.add(fild);
                }
                for (Datacolumninfo df : datacolumninfos) {
                    DataColumnName dcn = new DataColumnName();
                    if (Framework.isNotNullOrEmpty(df.getColumnDisplayName()) && df.getColumnDisplayName().equals(fild)) {
                        dcn.setColumnDisplayName(df.getColumnDisplayName());
                        dcn.setColumnName(df.getColumnAsName());
                        dcn.setSortNO(j);
                        if (df.getColumnDisplayName().equals(REPORT_ITEM_DISPLAY_NAME) || df.getColumnDisplayName().equals(REPORT_ITEM_NAME)) {
                            dcn.setIsFill("0");
                        } else {
                            dcn.setIsFill("1");
                        }
                        if (df.getColumnDisplayName().equals(REPORT_ITEM_NAME) && change == 0) {
                            dcn.setIsQuery("1");
                        } else {
                            dcn.setIsQuery("0");
                        }
                        //所有者权益变动表特殊处理
                        if (change == 1 && isflag) {
                            if (df.getColumnDisplayName().equals(REPORT_ITEM_DISPLAY_NAME)) {
                                dcn.setIsQuery("1");
                            } else {
                                dcn.setIsQuery("0");
                            }
                            dcn.setIsCurrent("1");
                        } else {
                            dcn.setIsCurrent("0");
                        }
                    } else {
                        continue;
                    }
                    dataColumnNamelist.add(dcn);
//                    Framework.log.info("----------------------"+dataColumnNamelist.contains(REPORT_ITEM_DISPLAY_NAME)+"--------------------------");
                }
            }
            if (totalCells > 0) {
                break;
            }
        }
        if (dataColumnNamelist.size() == 0) return null;
        //循环赋值
        for (int j = startRow + 1; j < totalRows; j++) {
            Row row = sheet.getRow(j);
            readRows(row, dataColumnNamelist, financialreportitemList, ownerequitychangedList, num, year, change);
        }
        return dataColumnNamelist;
    }

    private void readRows(Row row, List<DataColumnName> dataColumnNamelist, List<FinancialreportitemDTO> financialreportitemList, List<OwnerequitychangedDTO> ownerequitychangedList, String num, Integer year, int change) throws InvocationTargetException, IllegalAccessException, Exception {
        FinancialreportitemDTO fitemD = null;
        OwnerequitychangedDTO ochager = null;
        Map<String, Object> map = new HashMap<>();
        for (DataColumnName dc1 : dataColumnNamelist) {
            String projectname = null;
            if (Framework.isNotNullOrEmpty(dc1.getColumnDisplayName()) && dc1.getColumnDisplayName().equals(REPORT_ITEM_NAME) && "1".equals(dc1.getIsQuery()) && change == 0) {
                Cell celld = row.getCell(dc1.getSortNO());
                projectname = getStringValue(celld);
                if (null != fitemD) {
                    fitemD = new FinancialreportitemDTO();
                    map = new HashMap<>();
                }
                fitemD = getFinancialreportitem(financialreportitemList, projectname, num, year);
                if (null == fitemD) continue;
                try {
                    map = convertBeanToMap(fitemD);
                } catch (IntrospectionException e) {
                    e.printStackTrace();
                }
            } else if (Framework.isNotNullOrEmpty(dc1.getColumnDisplayName()) && dc1.getColumnDisplayName().equals(REPORT_ITEM_DISPLAY_NAME) && "1".equals(dc1.getIsQuery()) && change == 1) {
                Cell celld = row.getCell(dc1.getSortNO());
                projectname = getStringValue(celld);
                if (null != ochager) {
                    ochager = new OwnerequitychangedDTO();
                    map = new HashMap<>();
                }
                if (dc1.getIsCurrent().equals("1")) {
                    year = year - 1;
                }
                ochager = getOwnerequitychanged(ownerequitychangedList, projectname, num, year);
                if (null == ochager) continue;
                try {
                    map = convertBeanToMap(ochager);
                } catch (IntrospectionException e) {
                    e.printStackTrace();
                }
            }
            if (map.isEmpty()) continue;
            if ("1".equals(dc1.getIsFill())) {
                String UTitle = lowerFirst(dc1.getColumnName());
                Cell cells = row.getCell(dc1.getSortNO());
//                Framework.log.info("-------------------单元格类型："+cells.getCellType()+"--------------------------");
                CellType cellType = cells.getCellTypeEnum();
                //判断单元格是否有公式
                if (cellType.FORMULA == cells.getCellTypeEnum()) continue;
                cells.setCellType(CellType.NUMERIC);

//                if(Framework.isNullOrEmpty(map.get(UTitle))||"null".equals(String.valueOf(map.get(UTitle)))){
//                    val = 1.0;
//                }else{
////                    Framework.log.info("----------------------"+String.valueOf(map.get(UTitle))+"--------------------------");
////                    Framework.log.info("----------------------结束--------------------------");
//                    val = Double.valueOf(String.valueOf(map.get(UTitle))) ;
//                }
                String valString = String.valueOf(map.get(UTitle));
                if (Framework.isNotNullOrEmpty(map.get(UTitle)) || !"".equals(valString)) {
                    try {
                        Double val = Double.valueOf(valString);
                        cells.setCellValue(val);
                    } catch (Exception e) {
                        cells.setCellValue(valString);
                    }
                }
//                else
//                    cells.setCellValue(0.00);
            } else {
                continue;
            }
        }
    }

    private OwnerequitychangedDTO getOwnerequitychanged(List<OwnerequitychangedDTO> ownerequitychangedList, String projectname, String num, Integer year) throws Exception {
        if (Framework.isNullOrEmpty(projectname)) return null;
        String[] split = projectname.split(",");


        if (split.length > 1) {
            OwnerequitychangedDTO fitemD = null;
            for (String item : split) {
                Optional<OwnerequitychangedDTO> first = ownerequitychangedList.stream().filter(f -> f.getReportItemDisplayName().equals(item) && f.getFiscalYear().equals(year)).findFirst();
                if (first.isPresent()) {
                    if (Framework.isNullOrEmpty(fitemD))
                        fitemD = first.get();
                    else {
                        OwnerequitychangedDTO dto = first.get();
                        Double add1 = Framework.addDouble(fitemD.getOwnerEquityColumn1(), dto.getOwnerEquityColumn1());
                        fitemD.setOwnerEquityColumn1(add1);

                        Double add2 = Framework.addDouble(fitemD.getOwnerEquityColumn2(), dto.getOwnerEquityColumn2());
                        fitemD.setOwnerEquityColumn2(add2);

                        Double add3 = Framework.addDouble(fitemD.getOwnerEquityColumn3(), dto.getOwnerEquityColumn3());
                        fitemD.setOwnerEquityColumn3(add3);

                        Double add4 = Framework.addDouble(fitemD.getOwnerEquityColumn4(), dto.getOwnerEquityColumn4());
                        fitemD.setOwnerEquityColumn4(add4);

                        Double add5 = Framework.addDouble(fitemD.getOwnerEquityColumn5(), dto.getOwnerEquityColumn5());
                        fitemD.setOwnerEquityColumn5(add5);

                        Double add6 = Framework.addDouble(fitemD.getOwnerEquityColumn6(), dto.getOwnerEquityColumn6());
                        fitemD.setOwnerEquityColumn6(add6);

                        Double add7 = Framework.addDouble(fitemD.getOwnerEquityColumn7(), dto.getOwnerEquityColumn7());
                        fitemD.setOwnerEquityColumn7(add7);

                        Double add8 = Framework.addDouble(fitemD.getOwnerEquityColumn8(), dto.getOwnerEquityColumn8());
                        fitemD.setOwnerEquityColumn8(add8);

                        Double add9 = Framework.addDouble(fitemD.getOwnerEquityColumn9(), dto.getOwnerEquityColumn9());
                        fitemD.setOwnerEquityColumn9(add9);

                        Double add10 = Framework.addDouble(fitemD.getOwnerEquityColumn10(), dto.getOwnerEquityColumn10());
                        fitemD.setOwnerEquityColumn10(add10);

                        Double add11 = Framework.addDouble(fitemD.getOwnerEquityColumn11(), dto.getOwnerEquityColumn11());
                        fitemD.setOwnerEquityColumn11(add11);

                        Double add12 = Framework.addDouble(fitemD.getOwnerEquityColumn12(), dto.getOwnerEquityColumn12());
                        fitemD.setOwnerEquityColumn12(add12);

                        Double add13 = Framework.addDouble(fitemD.getOwnerEquityColumn13(), dto.getOwnerEquityColumn13());
                        fitemD.setOwnerEquityColumn13(add13);

                        Double add14 = Framework.addDouble(fitemD.getOwnerEquityColumn14(), dto.getOwnerEquityColumn14());
                        fitemD.setOwnerEquityColumn14(add14);

                        Double add15 = Framework.addDouble(fitemD.getOwnerEquityColumn15(), dto.getOwnerEquityColumn15());
                        fitemD.setOwnerEquityColumn15(add15);
                    }
                }
            }
            return fitemD;
        } else {
            Optional<OwnerequitychangedDTO> first = ownerequitychangedList.stream().filter(f -> f.getReportItemDisplayName().equals(projectname) && f.getFiscalYear().equals(year)).findFirst();
            if (first.isPresent())
                return first.get();
            else
                return null;
        }

//        OwnerequitychangedDTO oc = null;
//        for (OwnerequitychangedDTO ochange : ownerequitychangedList) {
//            if (Framework.isNotNullOrEmpty(projectname) && ochange.getReportItemDisplayName().equals(projectname) && ochange.getFiscalYear().equals(year)) {
//                oc = ochange;
//                break;
//            }
//        }
//        return oc;
    }

    private FinancialreportitemDTO getFinancialreportitem(List<FinancialreportitemDTO> financialreportitemList, String projectname, String num, Integer year) throws Exception {
        if (Framework.isNullOrEmpty(projectname)) return null;
//        Framework.log.info("----"+projectname+"-------------");

        String[] split = projectname.split(",");
        if (split.length > 1) {
            FinancialreportitemDTO fitemD = null;
            for (String item : split) {
                Optional<FinancialreportitemDTO> first = financialreportitemList.stream().filter(f -> f.getReportItemName().equals(item) && f.getAdjustmentType().equals(num) && f.getFiscalYear().equals(year)).findFirst();
                if (first.isPresent()) {
                    if (Framework.isNullOrEmpty(fitemD))
                        fitemD = first.get();
                    else {
                        FinancialreportitemDTO dto = first.get();
                        Double add1 = Framework.addDouble(fitemD.getFinalAccountsAdjustAmounts(), dto.getFinalAccountsAdjustAmounts());
                        fitemD.setFinalAccountsAdjustAmounts(add1);

                        Double add2 = Framework.addDouble(fitemD.getFinalAdjustDebitAmounts(), dto.getFinalAdjustDebitAmounts());
                        fitemD.setFinalAdjustDebitAmounts(add2);

                        Double add3 = Framework.addDouble(fitemD.getFinalAdjustCreditAmounts(), dto.getFinalAdjustCreditAmounts());
                        fitemD.setFinalAdjustCreditAmounts(add3);

                        Double add4 = Framework.addDouble(fitemD.getFinalAdjustAmounts(), dto.getFinalAdjustAmounts());
                        fitemD.setFinalAdjustAmounts(add4);

                        Double add5 = Framework.addDouble(fitemD.getDiffAdjustDebitAmounts(), dto.getDiffAdjustDebitAmounts());
                        fitemD.setDiffAdjustDebitAmounts(add5);

                        Double add6 = Framework.addDouble(fitemD.getDiffAdjustCreditAmounts(), dto.getDiffAdjustCreditAmounts());
                        fitemD.setDiffAdjustCreditAmounts(add6);

                        Double add7 = Framework.addDouble(fitemD.getAuditAdjustDebitAmounts(), dto.getAuditAdjustDebitAmounts());
                        fitemD.setAuditAdjustDebitAmounts(add7);

                        Double add8 = Framework.addDouble(fitemD.getAuditAdjustCreditAmounts(), dto.getAuditAdjustCreditAmounts());
                        fitemD.setAuditAdjustCreditAmounts(add8);

                        Double add9 = Framework.addDouble(fitemD.getAlreadyAuditLYDebitAmounts(), dto.getAlreadyAuditLYDebitAmounts());
                        fitemD.setAlreadyAuditLYDebitAmounts(add9);

                        Double add10 = Framework.addDouble(fitemD.getAlreadyAuditLYCreditAmounts(), dto.getAlreadyAuditLYCreditAmounts());
                        fitemD.setAlreadyAuditLYCreditAmounts(add10);

                        Double add11 = Framework.addDouble(fitemD.getAlreadyAuditLYAmounts(), dto.getAlreadyAuditLYAmounts());
                        fitemD.setAlreadyAuditLYAmounts(add11);

                        Double add12 = Framework.addDouble(fitemD.getAlreadyAuditCYInitialAmounts(), dto.getAlreadyAuditCYInitialAmounts());
                        fitemD.setAlreadyAuditCYInitialAmounts(add12);

                        Double add13 = Framework.addDouble(fitemD.getAlreadyAuditCYDebitAmounts(), dto.getAlreadyAuditCYDebitAmounts());
                        fitemD.setAlreadyAuditCYDebitAmounts(add13);

                        Double add14 = Framework.addDouble(fitemD.getAlreadyAuditCYCreditAmounts(), dto.getAlreadyAuditCYCreditAmounts());
                        fitemD.setAlreadyAuditCYCreditAmounts(add14);

                        Double add15 = Framework.addDouble(fitemD.getAlreadyAuditCYAmounts(), dto.getAlreadyAuditCYAmounts());
                        fitemD.setAlreadyAuditCYAmounts(add15);

                        Double add16 = Framework.addDouble(fitemD.getAlreadyAuditCYFinalAmounts(), dto.getAlreadyAuditCYFinalAmounts());
                        fitemD.setAlreadyAuditCYFinalAmounts(add16);

                        Double add17 = Framework.addDouble(fitemD.getInitialUncorrectedAmounts(), dto.getInitialUncorrectedAmounts());
                        fitemD.setInitialUncorrectedAmounts(add17);

                        Double add18 = Framework.addDouble(fitemD.getFinalUncorrectedAmounts(), dto.getFinalUncorrectedAmounts());
                        fitemD.setFinalUncorrectedAmounts(add18);

                        Double add19 = Framework.addDouble(fitemD.getProportion(), dto.getProportion());
                        fitemD.setProportion(add19);

                        Double add20 = Framework.addDouble(fitemD.getNoAuditCYInitialStructRatio(), dto.getNoAuditCYInitialStructRatio());
                        fitemD.setNoAuditCYInitialStructRatio(add20);

                        Double add21 = Framework.addDouble(fitemD.getNoAuditVariationAmounts(), dto.getNoAuditVariationAmounts());
                        fitemD.setNoAuditVariationAmounts(add21);

                        Double add22 = Framework.addDouble(fitemD.getNoAuditVariation(), dto.getNoAuditVariation());
                        fitemD.setNoAuditVariation(add22);

                        Double add23 = Framework.addDouble(fitemD.getConvertedAmount(), dto.getConvertedAmount());
                        fitemD.setConvertedAmount(add23);

                        Double add24 = Framework.addDouble(fitemD.getInitialUncorrectedAmounts(), dto.getInitialUncorrectedAmounts());
                        fitemD.setInitialUncorrectedAmounts(add24);

                        Double add25 = Framework.addDouble(fitemD.getOriginalMergeNumber(), dto.getOriginalMergeNumber());
                        fitemD.setOriginalMergeNumber(add25);
                    }
                }
            }
            return fitemD;
        } else {
            Optional<FinancialreportitemDTO> first = financialreportitemList.stream().filter(f -> f.getReportItemName().equals(projectname) && f.getAdjustmentType().equals(num) && f.getFiscalYear().equals(year)).findFirst();
            if (first.isPresent())
                return first.get();
            else
                return null;
        }

        //swq 调整
        //   FinancialreportitemDTO fitemD = null;
//        for (FinancialreportitemDTO fitem : financialreportitemList) {
////            Framework.log.info("----"+fitem.getReportItemName()+"-------------");
////           if(Framework.isNotNullOrEmpty(projectname) && fitem.getReportItemName().equals("实收资本（或股本）净额") ){
////               Framework.log.info("----进入程序-------------");
////           }
//            if (fitem.getReportItemName().equals(projectname) && num.equals(fitem.getAdjustmentType()) && fitem.getFiscalYear().equals(year)) {
//                fitemD = fitem;
//                break;
//            }
        //     }
        //  return fitemD;
    }

    /**
     * 实体转Map
     *
     * @param bean
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Map<String, Object> convertBeanToMap(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }

    /**
     * 首字母小写
     *
     * @param oldStr
     * @return
     */
    public static String lowerFirst(String oldStr) {

        char[] chars = oldStr.toCharArray();

        chars[0] += 32;

        return String.valueOf(chars);

    }

    /**
     * 获取单元格的值的字符串
     *
     * @param cell 单元格对象
     * @return cell单元格的值的字符串
     */
    private static String getStringValue(Cell cell) {
        if (cell == null || cell.getStringCellValue().equals("")) {
            return null;
        }
        CellType cellType = cell.getCellTypeEnum();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                double value = cell.getNumericCellValue();
                return String.valueOf(Math.round(value));
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }


    public String getParamString(String strSrc, String strDesc) {
        int nPos1, nPos2;

        if (strSrc.length() == 0 || strDesc.length() == 0) {
            return strSrc;
        }

        nPos1 = strSrc.indexOf(strDesc);
        if (-1 == nPos1) {
            return "";
        }

        nPos1 = strSrc.indexOf("\n", nPos1 + 1);
        if (-1 == nPos1) {
            return "";
        }

        nPos1 += 1;

        nPos2 = strSrc.indexOf("\n---", nPos1);
        if (-1 == nPos2) {
            return "";
        } else {
            return strSrc.substring(nPos1, nPos2);
        }
    }

    private int sheetCirculation(Workbook wb) {
        int sheetCount = -1;
        sheetCount = wb.getNumberOfSheets();
        return sheetCount;
    }


    // 验证excel文件
    public boolean validateExcel(String filePath) {
        // 检查文件名是否为空或者是否是Excel格式的文件
        if (filePath == null
                || !(is2003Excel(filePath) || is2007Excel(filePath))) {
            return false;
        }
        return true;
    }

    // 是否是2007的excel，返回true是2007
    public static boolean is2007Excel(String filePath) {

        return filePath.matches("^.+\\.(?i)(xlsx)$");

    }

    // 是否是2003的excel，返回true是2003

    public static boolean is2003Excel(String filePath) {

        return filePath.matches("^.+\\.(?i)(xls)$");

    }

    /**
     * GBY
     * 拷贝 被审计单位业务情况调查记录 模板
     * 返回模板的相对路径
     */
    private String copyTemplate(String fileName, String code, Attachment atth) throws Exception {
        String templateFile = DxnConfig.getFileUploadPath();
        String md5 = "";
        String suffix = "";
        String path = "";
        if (null != atth) {
            md5 = atth.getMd5();
            suffix = atth.getSuffix();
            path = atth.getPath();
        } else {
            md5 = Framework.createGuid().toString();
            suffix = fileName.substring(fileName.lastIndexOf("."));
            path = String.format("%s/%s/报备", super.gainAttachmentPath(), code);
        }

        String newFile = "";

        if (Framework.isNotNullOrEmpty(path)) {
            newFile = FileHelper.combine(templateFile, path);
        }

        FileHelper.ensureDirtectoryExists(newFile);
        newFile = FileHelper.combine(newFile, md5 + suffix);
        FileHelper.copyFile(templateFile, fileName, newFile);
        return FileHelper.combine(templateFile, path, md5 + suffix);
    }

    /**
     * 修改业务报备状态
     *
     * @param
     * @return
     * @throws Exception http://192.168.40.14:8080/RegistForm-uptRegistFormDocState-Query.do?registFormId=eabbec1d-f55e-47a3-b7f4-8ab4c6b01509
     */
    @SystemService(args = "registFormId,queryOrBusiness")
    public void uptRegistFormDocState(Long registFormId, String queryOrBusiness) throws Exception {

        RegistForm registForm = RegistForm.findById(registFormId);
        if (Framework.isNotNullOrEmpty(registForm)) {
            registForm.setDocState(40);
            registForm.setFailureToReport(20);
            registForm.setQueryOrBusiness(queryOrBusiness);
            this.repository().add(registForm);
        }
    }

    /**
     * 获取业务报备状态
     *
     * @param
     * @return
     * @throws Exception http://192.168.40.14:8080/RegistForm-gainRegistFormDocState-Query.do?registFormId=eabbec1d-f55e-47a3-b7f4-8ab4c6b01509
     */
    @SystemService(args = "registFormId")
    public Integer gainRegistFormDocState(Long registFormId) throws Exception {
        Integer docState = 0;
        RegistForm registForm = RegistForm.findById(registFormId);
        if (Framework.isNotNullOrEmpty(registForm)) {
            docState = registForm.getDocState();
        }
        return docState;
    }

    @JsonIgnore
    public MyTodoListDTO gainWorkFlowDTO(MyTodoListDTO value) throws Exception {
        value.setDepartmentType(this.getEngagementCode());
        String processId = value.getProcess_Id();
        User user = this.getCreatedBy();
        Optional<WorkFlowConfig> workFlowConfiglist = WorkFlowConfig.stream().where(w -> w.getProcessId().equals(processId)).findFirst();
        if (workFlowConfiglist.isPresent()) {
            String deptIdName = this.getDeptIdReadOnly().getOrgReadOnly().getNickname();
            WorkFlowConfig workFlowConfig = workFlowConfiglist.get();
            value.setWorkFlowName(deptIdName + "-" + workFlowConfig.getWorkFlowTypeId().getName());
        }
        if (Framework.isNotNullOrEmpty(user)) {
            if (Framework.isNotNullOrEmpty(user.getDeptReadOnly())) {
                value.setDepartment(this.getDeptIdReadOnly().getTreeName());
            }
        }
        return value;
    }

    @SystemService(args = "registFormId,queryOrBusiness")
    public void uptRegistFormStatus(Long registFormId, String queryOrBusiness) throws Exception {

        RegistForm registForm = RegistForm.findById(registFormId);
        if (Framework.isNotNullOrEmpty(registForm)) {
            registForm.setFailureToReport(10);
            registForm.setQueryOrBusiness(queryOrBusiness);
            this.repository().add(registForm);
        }
    }

    public ExportCellDTO attributeExportProjectName() throws Exception {
        ExportCellDTO dto = new ExportCellDTO();
        Long reportId = this.getReportId().getId();
        Optional<Report> report = Report.stream().where(data -> data.getId().equals(reportId)).findFirst();
        dto.setValue(report.isPresent() ? report.get().getProjectShowName() : "");
        return dto;
    }


    public ExportCellDTO attributeChangeOffice() throws Exception {
        ExportCellDTO dto = new ExportCellDTO();
        Optional<RegistForm> registForm = RegistForm.stream().where(data -> data.getFirstOrContinuous() == 10 && data.getFormerFirm().length() > 0).findFirst();
        dto.setValue(registForm.isPresent() ? "是" : "否");
        return dto;
    }

    public ExportCellDTO attributeRelationshipOne() throws Exception {
        ExportCellDTO dto = new ExportCellDTO();
        Long id = this.getSigningPartnerId().getStaffId();
        List<CPAQualification> cPAQualificationList = CPAQualification.stream().where(data -> data.getStaffId().getId() == id && data.getAccountantType().equals(10)).toList();
        if (cPAQualificationList != null && cPAQualificationList.size() > 0) {
            CPAQualification cPAQualification = cPAQualificationList.get(cPAQualificationList.size() - 1);
            Long enumId = EnumType.stream().where(data -> data.getCode().equals("InstituteOfInjectionEnumeration")).findFirst().get().getId();
            int value = cPAQualification.getLocationOfNoterRelations();
            Optional<EnumValue> enumValue = EnumValue.stream().where(data -> data.getValue() == value && data.getEnumTypeId().getId() == enumId).findFirst();
            dto.setValue(enumValue.isPresent() ? enumValue.get().getName() : null);
        }


        return dto;
    }

    public ExportCellDTO attributeRelationshipTwo() throws Exception {
        ExportCellDTO dto = new ExportCellDTO();
        Long id = this.getSigningAccountantId().getStaffId();
        List<CPAQualification> cPAQualificationList = CPAQualification.stream().where(data -> data.getStaffId().getId() == id && data.getAccountantType().equals(10)).toList();
        if (cPAQualificationList != null && cPAQualificationList.size() > 0) {
            CPAQualification cPAQualification = cPAQualificationList.get(cPAQualificationList.size() - 1);
            Long enumId = EnumType.stream().where(data -> data.getCode().equals("InstituteOfInjectionEnumeration")).findFirst().get().getId();
            int value = cPAQualification.getLocationOfNoterRelations();
            Optional<EnumValue> enumValue = EnumValue.stream().where(data -> data.getValue() == value && data.getEnumTypeId().getId() == enumId).findFirst();
            dto.setValue(enumValue.isPresent() ? enumValue.get().getName() : null);
        }
        return dto;
    }

    public ExportCellDTO attributeReportTextUrl() throws Exception {
        ExportCellDTO dto = new ExportCellDTO();
        Long id = this.getId();
        Optional<RegistForm> registForm = RegistForm.stream().where(data -> data.getId() == id).findFirst();

        Long reportId = registForm.isPresent() ? registForm.get().getReportId().getId() : null;
        Optional<ReportFile> reportFile = ReportFile.stream().where(data -> data.getReportId().getId().equals(reportId) && data.getFileType() == 10).findFirst();

        Long attachmentId = reportFile.isPresent() && reportFile.get().getAttachmentId() != null ? reportFile.get().getAttachmentId().getId() : null;
        Optional<Attachment> attachment = Attachment.stream().where(data -> data.getId().equals(attachmentId)).findFirst();
        if (attachment.isPresent()) {
            //String url = "http://192.168.40.30:8088/downloadFileForRegist.json?id=" + attachment.get().getId() + "&fileName=" + Base64Util.encode(attachment.get().getName()) + "&filePath=" + Base64Util.encode(attachment.get().getPath()) + "&fileMd5=" + attachment.get().getMd5();
            //url = url.replaceAll(" ","%20");
            String url = DxnConfig.getServerName() + "/downloadFileForRegist.json?id=" + attachment.get().getId();
//            dto.setUrl(url);
//            dto.setValue(attachment.get().getName());
            dto.setValue(url);
        }
        return dto;
    }

    public ExportCellDTO attributeReportFormUrl() throws Exception {
        ExportCellDTO dto = new ExportCellDTO();
        Long id = this.getId();
        Optional<RegistFormFile> registFormFile = RegistFormFile.stream().where(data -> data.getRegistFormId().getId().equals(id)).findFirst();
        Long attachmentId = registFormFile.isPresent() && registFormFile.get().getAttachmentId() != null ? registFormFile.get().getAttachmentId().getId() : null;
        Optional<Attachment> attachment = Attachment.stream().where(data -> data.getId().equals(attachmentId)).findFirst();
        if (attachment.isPresent()) {
            String url = DxnConfig.getServerName() + "/downloadFileForRegist.json?id=" + attachment.get().getId();
//            dto.setUrl(url);
//            dto.setValue(attachment.get().getName());
            dto.setValue(url);
        }
        return dto;
    }


}
