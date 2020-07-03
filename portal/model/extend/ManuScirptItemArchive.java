package com.dxn.model.extend;

import com.dxn.custom.util.coderule.CodeRuleFactory;
import com.dxn.custom.util.coderule.ICodeRule;
import com.dxn.dto.ManuScirptItemArchive.*;
import com.dxn.dto.ReportFilesDTO;
import com.dxn.dto.enums.Constants;
import com.dxn.dto.reportDTO.ReportListDTO;
import com.dxn.model.entity.ManuScirptItemArchiveEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.configuration.DxnConfig;
import com.dxn.system.context.AppContext;
import com.dxn.system.dto.EntityDataDTO;
import com.dxn.system.dto.SubmitModel;
import com.dxn.system.exception.BusinessException;
import com.dxn.system.io.FileHelper;
import com.dxn.util.ExcelUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Table(name = "Prj_ManuScirptItemArchive")
public class ManuScirptItemArchive extends ManuScirptItemArchiveEntity {
    /**
     * 获取项目基本信息
     *
     * @param compostionCustomerId 被审计单位id
     * @throws Exception
     */
    @SystemService(args = "compostionCustomerId")
    public ProjectInformationDTO geCompostionCustomerAndArchiveInformation(Long compostionCustomerId) throws Exception {
        ProjectInformationDTO dto = new ProjectInformationDTO();
        Optional<CompositionCustomer> customer = CompositionCustomer.stream().where(c -> c.getId().equals(compostionCustomerId)).findFirst();
        if (customer.isPresent()) {
            dto.setProjectCode(customer.get().getCode());
            dto.setProjectName(customer.get().getShowName());
            if (Framework.isNotNullOrEmpty(customer.get().getProjectManagerId()))
                dto.setProjectManagerName(customer.get().getProjectManagerId().getName());
            dto.setBusinessCode(getEngagementCode(customer.get().getId()));
            dto.setCompostionCustomerId(customer.get().getId());
            dto.setProjectId(customer.get().getProjectId().getId());
            ManuscriptItem manuscriptItem = ManuscriptItem.createNew();
            dto.setArchivePath(manuscriptItem.archiveUpLoadPath(customer.get().getCode()));

            DraftReviewBatch dbath = new DraftReviewBatch();

            dto.setReviewStateAll(dbath.gainManuscriptReviewState(customer.get().getProjectId().getId()));
            Long Id = customer.get().getId();
            dto.setRepostList(getReportListByCompostionId(Id));
            Optional<ManuScirptItemArchive> archive = this.stream().where(c -> c.getCompostionCustomerId().getId().equals(Id)).findFirst();
            if (archive.isPresent()) {
                //档案号
                dto.setId(archive.get().getId());
                dto.setCode(archive.get().getCode());
                dto.setArchiveDate(archive.get().getArchiveDate());
                dto.setBoxNumber(archive.get().getBoxNumber());
                dto.setBookNumber(archive.get().getBookNumber());
                dto.setTransfererStaff(archive.get().getTransfererStaff());
                if (Framework.isNotNullOrEmpty(archive.get().getAcceptStaffId())) {
                    EntityDataDTO AcceptStaffId = new EntityDataDTO();
                    ModelHelper.copyModel(archive.get().getAcceptStaffId(), AcceptStaffId);
                    dto.setAcceptStaffId(AcceptStaffId);
                }
            } else {
                Long deptId = AppContext.current().getProfile().getDeptId();
                Optional<Department> department = Department.stream().where(c -> c.getId().equals(deptId)).findFirst();
                if (department.isPresent()) {
                    if (department.get().getUnitAttributes() == Constants.B10 || department.get().getUnitAttributes() == Constants.B20) {
                        if (Framework.isNotNullOrEmpty(department.get().getArchivistId())) {
                            EntityDataDTO AcceptStaffId = new EntityDataDTO();
                            ModelHelper.copyModel(department.get().getArchivistId(), AcceptStaffId);
                            dto.setAcceptStaffId(AcceptStaffId);
                        }
                    } else {
                        Long parentdeptId = department.get().getParentId().getId();
                        Optional<Department> parentMent = Department.stream().where(c -> c.getId().equals(parentdeptId)).findFirst();
                        if (parentMent.isPresent()) {
                            if (Framework.isNotNullOrEmpty(parentMent.get().getArchivistId())) {
                                EntityDataDTO AcceptStaffId = new EntityDataDTO();
                                ModelHelper.copyModel(parentMent.get().getArchivistId(), AcceptStaffId);
                                dto.setAcceptStaffId(AcceptStaffId);
                            }
                        }
                    }
                }
            }

            if (archive.isPresent()) {
                Long bookId = archive.get().getId();
                if (Framework.isNotNullOrEmpty(bookId)) {
                    List<BoxAndNumberBook> books = BoxAndNumberBook.stream().where(c -> c.getManuScirptItemArchiveId().getId().equals(bookId)).toList();
                    List<BoxAndBookDTO> boxAndBookDTOS = new ArrayList<>();
                    for (BoxAndNumberBook item : books) {
                        BoxAndBookDTO book = new BoxAndBookDTO();
                        ModelHelper.copyModel(item, book);
                        book.setBookRange(item.getBookRange());
                        boxAndBookDTOS.add(book);
                    }
                    dto.setBoxAndBookDTOList(boxAndBookDTOS);
                }
            }

        }
        dto.setReviewOpinionsDataDTOList(reviewOpinionsData(compostionCustomerId));
        dto.setReviewState(isShowReview(customer.get().getProjectId()));


        return dto;
    }

    public String getEngagementCode(Long compostionCustomerId) throws Exception {
        String str = "";
        List<Engagement> engagementsList = Engagement.stream().where(c -> c.getCompositionCustomerId().getId().equals(compostionCustomerId)).toList();
        if (engagementsList.size() > 0) {
            for (Engagement item : engagementsList) {
                str += item.getCode() + ",";
            }
        }

        return str == "" ? "" : str.substring(0, str.length() - 1);
    }

    /**
     * 根据被审计单位id 获取报告1
     *
     * @param compostionCustomerId
     * @return
     * @throws Exception
     */
    public List<ReportListDTO> getReportListByCompostionId(Long compostionCustomerId) throws Exception {
        List<ReportListDTO> report = new ArrayList<>();
        List<Report> list = Report.stream().where(c -> c.getCompositionCustomerId().getId().equals(compostionCustomerId) && c.getBusinessSubclasses() != 1000 && c.getBusinessSubclasses() != 1001).toList();
        for (Report item : list) {
            ReportListDTO dto = new ReportListDTO();
            if (Framework.isNotNullOrEmpty(item.getSigningAccountantId()))
                dto.setSigningAccountantId(item.getSigningAccountantId().getName());
            if (Framework.isNotNullOrEmpty(item.getSigningPartnerId()))
                dto.setSigningPartnerId(item.getSigningPartnerId().getName());
            if (Framework.isNotNullOrEmpty(item.getSigningAccountantOtherId()))
                dto.setSigningAccountantOtherId(item.getSigningAccountantOtherId().getName());
            dto.setReportCode(item.getReportCode());
            dto.setAuditReportDate(Framework.localDateToString(item.getAuditReportDate()));
            report.add(dto);
        }
        return report;
    }

    @SystemService(args = "archive")
    public void saveDataArchive(ProjectInformationDTO archive) throws Exception {
        Long archiveId = archive.getId();
        ManuScirptItemArchive model;

        Long custid = archive.getCompostionCustomerId();
        Optional<ManuScirptItemArchive> archiveEntity = this.stream().where(c -> c.getCompostionCustomerId().getId() == custid).findFirst();
        if(archiveEntity.isPresent())
        {
            model = archiveEntity.get();
            model.initialization();
        }else
        {
            model = ManuScirptItemArchive.createNew();
        }
        /*
        if (Framework.isNotNullOrEmpty(archive.getId())) {
            Optional<ManuScirptItemArchive> archiveEntity = this.stream().where(c -> c.getId() == archiveId).findFirst();
            model = archiveEntity.get();
            model.initialization();
        } else {
            model = ManuScirptItemArchive.createNew();
        }*/
        model.setTransfererStaff(archive.getTransfererStaff());
        Long userId = archive.getAcceptStaffId().getId();
        Optional<User> user = User.stream().where(c -> c.getId().equals(userId)).findFirst();
        if (user.isPresent()) {
            model.setAcceptStaffId(user.get());
        }
        Report report = gainReport(archive.getCompostionCustomerId());
        if (Framework.isNullOrEmpty(archive.getId())) {
            //Retrieve unique sequence number
            CodeRuleFactory codeRuleFactory = (com.dxn.custom.util.coderule.CodeRuleFactory) AppHelper.getBean(
                    "codeRuleFactory");
            ICodeRule codeRule = codeRuleFactory.getCodeRule(ICodeRule.ENTITY_NAME_ITEMARCHIVE);
            SubmitModel submitModel = new SubmitModel();
            Map<String, Object> hmap = new HashMap<String, Object>();
            submitModel.setParaList(hmap);
            submitModel.getParaList().put(ICodeRule.ENTITY_ITEMARCHIVE_ID, archive.getId());
            submitModel.getParaList().put(ICodeRule.ENTITY_COMPOSITIONCUSTOMER_ID, archive.getCompostionCustomerId());
            String sequencePattern = codeRule.getSequencePattern(ICodeRule.ENTITY_NAME_ITEMARCHIVE, submitModel);
            String sequenceNumber = codeRule.getNewCode(sequencePattern);
            model.setCode(sequenceNumber);

        }

        model.setArchiveDate(archive.getArchiveDate());
        if(Framework.isNullOrEmpty(archive.getBookNumber()))
            throw new BusinessException("请维护盒数/册数内容！");
        model.setBookNumber(archive.getBookNumber());
        model.setBoxNumber(archive.getBoxNumber());
        model.setDocState(Constants.ArchiveNotSubmitted);
        model.setCompostionCustomer(archive.getCompostionCustomerId());
        model.setProject(archive.getProjectId());
        if (Framework.isNotNullOrEmpty(report)) {
            model.setReportNumber(report.getReportCode());
        }
        List<BoxAndNumberBook> bookList = BoxAndNumberBook.stream().where(c -> c.getManuScirptItemArchiveId().getId().equals(archiveId)).toList();

        for (BoxAndNumberBook item : bookList) {
            item.initialization();
            this.repository().remove(item);
        }

        List<BoxAndNumberBook> listModel = new ArrayList<>();
        for (BoxAndBookDTO item : archive.getBoxAndBookDTOList()) {
            BoxAndNumberBook entity = BoxAndNumberBook.createNew();
            entity.setBox(item.getBox());
            entity.setBookRange(item.getBookRange());
            entity.setBook(item.getBook());
            entity.setContent(item.getContent());
            entity.setManuScirptItemArchiveId(model);
            listModel.add(entity);
        }
        model.setBoxAndNumberBooks(listModel);
        this.repository().add(model);

        DraftReviewBatch dbath = new DraftReviewBatch();

        String states = dbath.gainManuscriptReviewState(archive.getProjectId());//gainManuscriptReviewState

        if (states.contains("30"))
        {
            //二级底稿刷数据
            setTemplateValue(archive.getCompostionCustomerId(), 10, "30", "40", Paths.get(DxnConfig.getFileUploadPath(), "Archive", "11-G11部门经理复核意见.xlsx").toString());

        }
        if (states.contains("40"))
        { //三级底稿刷数据
            setTemplateValue(archive.getCompostionCustomerId(), 20, "40", "50", Paths.get(DxnConfig.getFileUploadPath(), "Archive", "12-G12项目合伙人复核意见.xlsx").toString());

        }
        if(states.contains("50"))
        {
            //分所底稿刷数据
            setTemplateValue(archive.getCompostionCustomerId(), 30, "50", "60", Paths.get(DxnConfig.getFileUploadPath(), "Archive", "13-G13项目质量控制复核意见(分所).xlsx").toString());

        }
        if(states.contains("60"))
        {
            //总所底稿刷数据
            setTemplateValue(archive.getCompostionCustomerId(), 40, "60", "70", Paths.get(DxnConfig.getFileUploadPath(), "Archive", "13-G13项目质量控制复核意见(总所).xlsx").toString());

        }

        saveReportFileToManuscripItem(archive.getCompostionCustomerId());

    }




    /**
     * 生成文件
     *
     * @param compostionCustomerId 被审计单位
     * @param personState          当前复核等级
     * @param reviewState          下一级复核等级
     * @param importFilePath       模板路径
     * @throws Exception
     */
    @SystemService(args = "compostionCustomerId")
    public void setTemplateValue(Long compostionCustomerId, int level, String personState, String reviewState, String importFilePath) throws Exception {

        List<TodoLogDTO> todoLogDTOS = gainTodoLog(compostionCustomerId);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Optional<CompositionCustomer> compostion = CompositionCustomer.stream().where(c -> c.getId().equals(compostionCustomerId)).findFirst();
        if (compostion.isPresent()) {
            CompositionCustomer compostionmodel = compostion.get();
            ReviewOpinionsProjectPartnerDTO dto = new ReviewOpinionsProjectPartnerDTO();
            dto.setCompositionCustomerName(compostionmodel.getName());
            dto.setIndexNumber("G11");
            dto.setAuditDate(compostionmodel.getAuditEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "至" + compostionmodel.getAuditEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            dto.setPageNumber(1);
            String strname = todoLogDTOS.stream().filter(c -> c.getEntitystatus().equals(personState)).findFirst().isPresent() == true ? todoLogDTOS.stream().filter(c -> c.getEntitystatus().equals(personState)).findFirst().get().getActor_name() : "";
            String comppilingTime = todoLogDTOS.stream().filter(c -> c.getEntitystatus().equals(personState)).findFirst().isPresent() == true ? todoLogDTOS.stream().filter(c -> c.getEntitystatus().equals(personState)).findFirst().get().getCreated_time() : "";

            dto.setCompilingPersonSignName(strname);
            //Framework.localDateTimeToString(comppilingTime);
            dto.setCompilingPersonSignTime(Framework.isNullOrEmpty(comppilingTime) ? "" : df.format(df.parse(comppilingTime)));

            String name3="";
            String time="";
            for(TodoLogDTO item :todoLogDTOS)
            {
                if(reviewState.equals(item.getEntitystatus()))
                {
                    name3 =item.getActor_name();
                    time = item.getCreated_time();
                }
            }
            dto.setReviewerName(name3);
           // dto.setReviewerName(todoLogDTOS.stream().filter(c -> c.getEntitystatus().equals(reviewState)).findFirst().isPresent() == false ? "" : todoLogDTOS.stream().filter(c -> c.getEntitystatus().equals(reviewState)).findFirst().get().getActor_name());
           // String time = todoLogDTOS.stream().filter(c -> c.getEntitystatus().equals(reviewState)).findFirst().isPresent() == false ? "" : todoLogDTOS.stream().filter(c -> c.getEntitystatus().equals(reviewState)).findFirst().get().getCreated_time();
            dto.setReviewerTime(Framework.isNullOrEmpty(time) ? "" : df.format(df.parse(time)));
            dto.setAutograph(strname);
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy年MM月dd日");//设置日期格式
            dto.setYear(df1.format(new Date()));
            String exportFilePath = "";
            ManuscriptItem item = ManuscriptItem.createNew();
            item.archiveUpLoadPath(compostionmodel.getCode());
            Path path = Paths.get(DxnConfig.getFileUploadPath(), item.archiveUpLoadPath(compostionmodel.getCode()));
            FileHelper.ensureDirtectoryExists(path.toString());
            String manuscriptType;
            String filepath;
            if (level == 10) {
                exportFilePath = Paths.get(DxnConfig.getFileUploadPath(), item.archiveUpLoadPath(compostionmodel.getCode()), "部门经理复核意见.xlsx").toString();
                filepath=Paths.get( item.archiveUpLoadPath(compostionmodel.getCode()), "部门经理复核意见.xlsx").toString();
                manuscriptType="100";
            } else if (level == 20) {
                exportFilePath = Paths.get(DxnConfig.getFileUploadPath(), item.archiveUpLoadPath(compostionmodel.getCode()), "项目合伙人复核意见.xlsx").toString();
                filepath= Paths.get( item.archiveUpLoadPath(compostionmodel.getCode()), "项目合伙人复核意见.xlsx").toString();
                manuscriptType="101";
            } else if (level == 30) {
                exportFilePath = Paths.get(DxnConfig.getFileUploadPath(), item.archiveUpLoadPath(compostionmodel.getCode()), "项目质量控制复核意见(分所).xlsx").toString();
                filepath= Paths.get( item.archiveUpLoadPath(compostionmodel.getCode()), "项目质量控制复核意见(分所).xlsx").toString();
                manuscriptType="102";
            } else {
                exportFilePath = Paths.get(DxnConfig.getFileUploadPath(), item.archiveUpLoadPath(compostionmodel.getCode()), "项目质量控制复核意见(总所).xlsx").toString();
                filepath= Paths.get( item.archiveUpLoadPath(compostionmodel.getCode()), "项目质量控制复核意见(总所).xlsx").toString();
                manuscriptType="102";
            }

            ExcelUtils.writeXLSX(importFilePath, exportFilePath, dto);
            item.createManuscripItem(compostionCustomerId,manuscriptType,filepath,1L,"",strname,name3);

        }
    }

    /**
     *
     * @param compostionCustomerId
     * @throws Exception
     */
    private void saveReportFileToManuscripItem(Long compostionCustomerId ) throws Exception{

        List<Object> p = new ArrayList<>();
        p.add(compostionCustomerId);

        ManuscriptItem item = ManuscriptItem.createNew();

        List<ReportFilesDTO> files =repository().sqlQuery("select path+'/'+md5+suffix as path,name from dbo.Base_Attachment \n" +
                "where id in (select attachmentId from PM_ReportFile \n" +
                "where  Reportid in (select id from PM_Report  where compositionCustomerId =? \n" +
                ")and FileType =10)",p,ReportFilesDTO.class);

        for(int i = 0;i<files.size();i++)
        {
            Long j = new Long((long)i + 1L) ;
            item.createManuscripItem(compostionCustomerId,"110",files.get(i).getPath(),j,files.get(i).getName(),"","");
        }


    }

    /**
     * 删除盒数册数
     *
     * @param ArchiveId
     * @throws Exception
     */
    @SystemService(args = "ArchiveId")
    public void deleteDataRow(Long ArchiveId) throws Exception {
        Optional<BoxAndNumberBook> boxs = BoxAndNumberBook.stream().where(c -> c.getId().equals(ArchiveId)).findFirst();
        repository().remove(boxs.get());
    }

    /**
     * 获取纸质底稿存放地
     *
     * @return
     * @throws Exception
     */
    @SystemService()
    public List<HashMap<String, String>> gainArchiveAddress() throws Exception {
        List<HashMap<String, String>> hashMapList = new ArrayList<>();
        Long deptId = AppContext.current().getProfile().getDeptId();
        Long userId = AppContext.current().getProfile().getId();
        Department deptmentQuery = new Department();
        Optional<Department> department = Department.stream().where(c -> c.getId().equals(deptId)).findFirst();
        if (department.isPresent()) {
            if (department.get().getUnitAttributes() == Constants.B10 || department.get().getUnitAttributes() == Constants.B20) {
                if (Framework.isNotNullOrEmpty(department.get().getArchivistId()) && department.get().getArchivistId().equals(userId)) {
                    deptmentQuery = department.get();
                }
            } else {
                Long parentdeptId = department.get().getParentId().getId();
                Optional<Department> parentMent = Department.stream().where(c -> c.getId().equals(parentdeptId)).findFirst();
                if (parentMent.isPresent()) {
                    if (Framework.isNotNullOrEmpty(parentMent.get().getArchivistId()) && parentMent.get().getArchivistId().getId().equals(userId)) {
                        deptmentQuery = parentMent.get();
                    }
                }
            }
        }
        if (Framework.isNotNullOrEmpty(deptmentQuery)) {
            HashMap<String, String> hashMap = new HashMap<>();
            if (Framework.isNotNullOrEmpty(deptmentQuery.getFileStoragePlace1())) {
                hashMap.put("name", deptmentQuery.getFileStoragePlace1());
                hashMapList.add(hashMap);
            }
            HashMap<String, String> hashMap1 = new HashMap<>();
            if (Framework.isNotNullOrEmpty(deptmentQuery.getFileStoragePlace2())) {
                hashMap1.put("name", deptmentQuery.getFileStoragePlace2());
                hashMapList.add(hashMap1);
            }
            HashMap<String, String> hashMap2 = new HashMap<>();
            if (Framework.isNotNullOrEmpty(deptmentQuery.getFileStoragePlace3())) {
                hashMap2.put("name", deptmentQuery.getFileStoragePlace3());
                hashMapList.add(hashMap2);
            }
            HashMap<String, String> hashMap3 = new HashMap<>();
            if (Framework.isNotNullOrEmpty(deptmentQuery.getFileStoragePlace4())) {
                hashMap3.put("name", deptmentQuery.getFileStoragePlace4());
                hashMapList.add(hashMap3);
            }

        }
        return hashMapList;
    }

    /**
     * 纸质底稿存放地
     *
     * @param archiveId
     * @throws Exception
     */
    @SystemService(args = "archiveId,content")
    public void saveArchiveAddress(Long archiveId, String content) throws Exception {
        ManuScirptItemArchive model = this.findById(archiveId);
        model.setDepositAddress(content);
    }

    /**
     * 申请销毁
     *
     * @param dto
     * @throws Exception
     */
    @SystemService(args = "dto")
    public void saveDestruction(DestructionDTO dto) throws Exception {
        DestructionRecords records = DestructionRecords.createNew();
        records.setContent(dto.getContent());
        records.setUser(AppContext.current().getProfile().getId());
        records.setDept(AppContext.current().getProfile().getDeptId());
        List<DestructionRecordsAttachment> arrachmentlist = new ArrayList<>();

        for (Long item : dto.getAttachmentIdList()) {
            DestructionRecordsAttachment arrachment = DestructionRecordsAttachment.createNew();
            arrachment.setAttachment(item);
            arrachment.setDestructionRecordsId(records);
            arrachmentlist.add(arrachment);
        }
        records.setDestructionRecordsAttachments(arrachmentlist);
        repository().add(records);
        repository().commit();
        for (Long item : dto.getCompostionCustomerIdList()) {
            Optional<CompositionCustomer> model = CompositionCustomer.stream().where(c -> c.getId().equals(item)).findFirst();
            model.get().initialization();
            model.get().setDestructionRecordsState(Constants.YES);
            model.get().setDestructionRecordsId(records.getId());
            repository().add(model.get());
        }


    }

    @SystemService(args = "auditProjectId,reivewState,rcType")
    private List<ReviewOpinionsDataDTO> reviewOpinionsData1(Long auditProjectId, int reivewState, int rcType) throws Exception {
        List<ReviewOpinionsDataDTO> opinionsDataDTOS = new ArrayList<>();
        //原数据
        List<ReviewComments> reviewCommentsList = ReviewComments.stream().where(c -> c.getCompositionCustomerId().getId().equals(auditProjectId) && c.getLevel() == 1).toList();
        for (ReviewComments item : reviewCommentsList) {
            if (item.getManuscriptReviewState() == reivewState && item.getRCType() == rcType) {
                opinionsDataDTOS.add(copyData(item));
            }
        }
        return opinionsDataDTOS;
    }

    /**
     * 获取复核意见
     *
     * @param auditProjectId
     * @throws Exception
     */
    private List<ReviewOpinionsDataDTO> reviewOpinionsData(Long auditProjectId) throws Exception {
        List<ReviewOpinionsDataDTO> opinionsDataDTOS = new ArrayList<>();
        // ManuscriptReviewState 等于50 分所    ManuscriptReviewState 等于60 总所
        //原数据
        List<ReviewComments> reviewCommentsList = ReviewComments.stream().where(c -> c.getCompositionCustomerId().getId().equals(auditProjectId) && c.getLevel() == 1).toList();
        for (ReviewComments item : reviewCommentsList) {
            // 三级复核
            if (item.getManuscriptReviewState() == 50 && item.getRCType() == 10) {
                opinionsDataDTOS.add(copyData(item));
            } else if (item.getManuscriptReviewState() == 50 && item.getRCType() == 20) {
                opinionsDataDTOS.add(copyData(item));
            } else if (item.getManuscriptReviewState() == 50 && item.getRCType() == 30) {
                opinionsDataDTOS.add(copyData(item));
            }
            // 四级复核
            else if (item.getManuscriptReviewState() == 60 && item.getRCType() == 10) {
                opinionsDataDTOS.add(copyData(item));
            } else if (item.getManuscriptReviewState() == 60 && item.getRCType() == 20) {
                opinionsDataDTOS.add(copyData(item));
            } else if (item.getManuscriptReviewState() == 60 && item.getRCType() == 30) {
                opinionsDataDTOS.add(copyData(item));
            }
        }
        return opinionsDataDTOS;
    }

    public ReviewOpinionsDataDTO copyData(ReviewComments model) throws Exception {
        ReviewOpinionsDataDTO dto = new ReviewOpinionsDataDTO();
        ModelHelper.copyModel(model, dto);
        dto.setPageChecked(model.getIsSelection());
        // String str="<p>one</p>";
        dto.setContent(dto.getContent().replaceAll("<p>", "").replaceAll("</p>", ""));
        return dto;
    }

    /**
     * 判断复核意见怎么显示
     *
     * @param project
     * @return
     */
    private Integer isShowReview(Project project) throws Exception {

        Department department = deptType(project);
        //等于10 总部复核  等于20 不需要   project.getHeadquartersReview()==10 &&
        Integer result = 0;
        //如果是1   当前项目属于总部  直接显示总部复核意见
        if (department.getUnitAttributes() == 10) {
            result = 1;
        } // 如果 项目属于分所 并且 项目那勾选要走总部复核 那么就 总分所的复核意见都放开
        else if (department.getUnitAttributes() == 20 && project.getHeadquartersReview() == 10) {
            result = 2;
        } //如果 项目属于分所 并且   项目那勾选不要总不符合  那么就之分开 分所
        else if (department.getUnitAttributes() == 20 && project.getHeadquartersReview() == 20) {
            result = 3;
        }
        return result;
    }

    private Department deptType(Project project) throws Exception {
        Long deptId = project.getDeptId().getId();
        Department deptmentQuery = new Department();
        Optional<Department> department = Department.stream().where(c -> c.getId().equals(deptId)).findFirst();
        if (department.isPresent()) {
            if (department.get().getUnitAttributes() == Constants.B10 || department.get().getUnitAttributes() == Constants.B20) {
                deptmentQuery = department.get();
            } else {
                Long parentdeptId = department.get().getParentId().getId();
                Optional<Department> parentMent = Department.stream().where(c -> c.getId().equals(parentdeptId)).findFirst();
                if (parentMent.isPresent())
                    deptmentQuery = parentMent.get();
            }
        }
        return deptmentQuery;
    }


    /**
     * 获取流程节点状态 和名称
     *
     * @param compostionCustomerId
     * @return
     * @throws Exception
     */
    private List<TodoLogDTO> gainTodoLog(Long compostionCustomerId) throws Exception {
        List<Object> p = new ArrayList<>();
        p.add(compostionCustomerId);
        List<TodoLogDTO> projects = repository().sqlQuery("select t.actor_name,t.created_time,isnull(t.entitystatus,10) as entitystatus\n" +
                "from  dbo.WF_TODOLOG as t\n" +
                "where entityid in(\n" +
                "select entityid from dbo.WF_TODOLOG where \n" +
                "entityid in (select draftReviewBatchId from dbo.QR_DraftReviewBatchItem  where compositionCustomerId =?)and \n" +
                "entitytype ='DraftReviewBatch'\n" +
                "and entityStatus =60\n" +
                ")  and entitytype ='DraftReviewBatch'", p, TodoLogDTO.class);
        return projects;
    }


    @SystemService(args = "dtos")
    public void exportExcel(List<ExportExcelDto> dtos) throws Exception {
        Report report = gainReport(dtos.get(0).getCompostionCustomerId());
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("sheet1");
        for (int i = 0; i < 5; i++) {
            Row row = sheet.createRow(i);
            for (int j = 0; j < dtos.size(); j++) {
                Cell cell = row.createCell(j);
                row.getCell(j).setCellValue(cellValue(i, report, dtos.get(j)));

                sheet.setColumnWidth(j, 7000);

                XSSFCellStyle cellStyle = wb.createCellStyle();

                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cellStyle.setWrapText(true);
                cell.setCellStyle(cellStyle);
            }
        }
        FileOutputStream out = new FileOutputStream("D:\\盒数.xlsx");
        wb.write(out);
    }

    private String cellValue(int i, Report report, ExportExcelDto dto) throws Exception {
        String str = "";
        switch (i) {
            case 0:
                str = report.getReportCode();
                break;
            case 1:
                str = report.getCompositionCustomerId().getName();
                StringBuilder sb = new StringBuilder();
                for (String s : str.split("")) {
                    sb.append(s);
                    sb.append("\r\n");
                }
                str = sb.toString();
                break;
            case 2:
                str = dto.getBox() + "/" + "共" + dto.getBoxCount() + "盒";
                break;
            case 3:

                str = dto.getBook() + "/" + "共" + dto.getBookCount() + "册";
                break;
        }
        return str;
    }

    private Report gainReport(Long compostionCustomerId) throws Exception {
        List<Report> reports = Report.stream().where(c -> c.getCompositionCustomerId().getId().equals(compostionCustomerId)).sortedBy(c -> c.getCreatedOn()).toList();
        if (reports.size() > 0) {
            return reports.get(0);
        }
        return new Report();
    }

//    public void gainDraftOpinionForm(Integer rCType,Integer manuscriptReviewState) throws Exception {
//        String problemId="问题Id";
//        if (manuscriptReviewState==10){
//            if (rCType==10){
//                additiveExtraction(rCType,manuscriptReviewState,problemId);
//            }else if (rCType==20){
//                additiveExtraction(rCType,manuscriptReviewState,problemId);
//            }else if (rCType==30){
//                additiveExtraction(rCType,manuscriptReviewState,problemId);
//            }
//        }else if (manuscriptReviewState==20){
//            if (rCType==10){
//                additiveExtraction(rCType,manuscriptReviewState,problemId);
//            }else if (rCType==20){
//                additiveExtraction(rCType,manuscriptReviewState,problemId);
//            }else if (rCType==30){
//                additiveExtraction(rCType,manuscriptReviewState,problemId);
//            }
//        }
//    }
//    //问题中间表添加过程的通用公式
//    private void additiveExtraction(Integer rCType,Integer manuscriptReviewState,String problemId) throws Exception {
//        Long manuScirptItemArchiveId=this.getId();
//        List<DraftOpinionForm> draftOpinionFormList = DraftOpinionForm.stream().where(w -> w.getManuScirptItemArchiveId().getId()==manuScirptItemArchiveId&&w.getRCType()==rCType&&w.getManuscriptReviewState()==manuscriptReviewState).toList();
//        draftOpinionFormList.forEach((c) -> repository().remove(c));
//        DraftOpinionForm draftOpinionFormEntity = DraftOpinionForm.createNew();
//        draftOpinionFormEntity.setCompositionCustomerId(this.getCompostionCustomerId());//被审计的单位
//        draftOpinionFormEntity.setProjectId(this.getProjectId());//项目Id
//        draftOpinionFormEntity.setManuScirptItemArchiveId(this);//归档Id
//        draftOpinionFormEntity.setManuscriptReviewState(manuscriptReviewState);//分所or总所
//        draftOpinionFormEntity.setProblemId(problemId);//问题Id;
//        draftOpinionFormEntity.setRCType(rCType);//问题类型;
//        repository().add(draftOpinionFormEntity);
//    }
}
