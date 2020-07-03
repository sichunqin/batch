package com.dxn.model.extend;

import com.dxn.dto.*;
import com.dxn.dto.ManuScirptItemArchive.ManuscriptCautalog;
import com.dxn.dto.ManuScirptItemArchive.ManuscriptCautalogDTO;
import com.dxn.dto.WorkFlow.ManuscriptItemTreeDTO;
import com.dxn.dto.enums.*;
import com.dxn.forms.dto.TreeDTO;
import com.dxn.model.entity.CompositionCustomerEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.configuration.DxnConfig;
import com.dxn.system.context.AppContext;
import com.dxn.system.dto.workFlow.MyTodoListDTO;
import com.dxn.system.dto.workFlow.WorkFlowNode;
import com.dxn.system.exception.BusinessException;
import com.dxn.system.io.FileHelper;
import com.dxn.util.ExcelUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "Prj_ComprositionCustome")
public class CompositionCustomer extends CompositionCustomerEntity {

    @Override
    public void onInserting() throws Exception {
        super.onInserting();
        this.setDocStateAllE("10");
        this.setManuscriptReviewState(10);//上传底稿时用到
        if (Framework.isNotNullOrEmpty(this.getLevel()) && this.getLevel() < this.getProjectId().getProcessRiskLevel()) {
            Long projectId = this.getProjectId().getId();
            Optional<Project> projectFirst = Project.stream().where(p -> p.getId() == projectId).findFirst();
            if (projectFirst.isPresent()) {
                Project project = projectFirst.get();
                project.setProcessRiskLevel(this.getLevel());
                repository().add(project);
            }
        }

    }

    @Override
    public void onDeleting() throws Exception {
        Long projectId = this.getProjectId().getId();
        if (this.getProjectId().getDocState() == 11) {
            Optional<Project> projectEntity = Project.stream().where(c -> c.getId() == projectId).findFirst();
            if (projectEntity.isPresent()) {
                Project entity = projectEntity.get();
                entity.setRandomCode(Framework.createGuid().toString());
                repository().add(entity);
            }
        }
    }

    //新增后
    @Override
    public void onInserted() throws Exception {

        //业务代码
        super.onInserted();
        if (Framework.isNullOrEmpty(this.getProjectId().getCode())) {
            this.getProjectId().setCode(this.getCode());
            this.getProjectId().copyExcel();
            if (Framework.isNotNullOrEmpty(this.getProjectId().getDeptId()))
                this.setDeptId(this.getProjectId().getDeptId());
        }

    }

    @Override
    public void onUpdating() throws Exception {
        super.onUpdating();
        updateDrag();
        projectStatusModification();
        //维护流程风险级别字段
        Long projectId = this.getProjectId().getId();
        Optional<Project> projectFirst = Project.stream().where(p -> p.getId() == projectId).findFirst();
        if (projectFirst.isPresent()) {
            Project project = projectFirst.get();
            project.setProcessRiskLevel(project.GeneralMethodOfRiskLevel(project));
            repository().add(project);
        }
        Long id = this.getId();
        Integer[] stateArray = new Integer[]{11, 30, 31, 32, 33};
        //只有申请修改的时候才可以  新增或者修改 都认为是项目架构发生变化
        if (Arrays.asList(stateArray).contains(this.getProjectId().getDocState())) {
            //if (this.getProjectId().getDocState() == ProjectEnum.Amendment.getIndex() || this.getProjectId().getDocState() == ProjectEnum.AlreadyStarted.getIndex()) {

            String RandomCode = Framework.createGuid().toString();
            if (this.getOriginalValue().getRiskLevel() > 0 && this.getOriginalValue().getRiskLevel() != this.getRiskLevel()) {
                // this.getProjectId().setDocState(ProjectEnum.NotSubmitted.getIndex()); //如果现在的风险
                Project prject = Project.createNew();
                prject.applyMotifySpecificOperation(this.getProjectId().getId());
                //this.setRandomCode(RandomCode);
                comterModifyType(RandomCode, 30, this.getId());
            }

            if (!this.getAuditStart().equals(this.getOriginalValue().getAuditStart()) || !this.getAuditEnd().equals(this.getOriginalValue().getAuditEnd())) {
                //this.setStateCode(StateCodeEnum.auditTime.getIndex());
                //this.setRandomCode(RandomCode);
                // 审计期间发生变化  像变更表 更改数据 或者插入
                comterModifyType(Framework.createGuid().toString(), 10, this.getId());
            }
            if (Framework.isNotNullOrEmpty(this.getManuscriptTemplate()) && !this.getManuscriptTemplate().equals(this.getOriginalValue().getManuscriptTemplate())) {
                //  this.setStateCode(StateCodeEnum.manuscript.getIndex());
                //this.setRandomCode(RandomCode);
                // 底稿模板发生变化  像变更表 更改数据 或者插入
                comterModifyType(Framework.createGuid().toString(), 10, this.getId());
                // 底稿模板发生变化  单独处理  只改变一级项目的随机码   2020-03-11 高新杰提的需求， 张敬研发送的eamil
                updateProjectAndCompostionCustomerRandomCode(this.getProjectId().getId(), -1);
            }
            if (this.getParentId() == null) {
                if (this.getOriginalValue().getIsSubsidiaryReport() != null && !this.getIsSubsidiaryReport().equals(this.getOriginalValue().getIsSubsidiaryReport())) {
                    // 以下俩行 是更新自己本身的随机码 及上一级和顶级的随机码
                    this.setRandomCode(Framework.createGuid().toString());
                    long parentId = this.getParentId() == null ? -1L : this.getParentId().getId();
                    updateProjectAndCompostionCustomerRandomCode(this.getProjectId().getId(), parentId);

                }
            }
            if (this.getOriginalValue() != null && this.getIsReport() != null && this.getOriginalValue().getIsReport() != null && !this.getIsReport().equals(this.getOriginalValue().getIsReport())) {
                //  是否出具报告  单独处理  只改变一级项目的随机码
                // this.setRandomCode(Framework.createGuid().toString());
                updateProjectAndCompostionCustomerRandomCode(this.getProjectId().getId(), -1);
            }

            if (Framework.isNotNullOrEmpty(this.getCustomerType()) && this.getCustomerType() > 0) {
                String projectName = this.getName() + "-" + this.getBusinessTypeValue() + "-" + Framework.localDateToString(this.getAuditStart()) + "至" + Framework.localDateToString(this.getAuditEnd());
                this.setShowName(projectName);
            }

        }
    }

    public void onUpdated() throws Exception {

    }


    @Override
    public void onEndWorkflow(WorkFlowNode node) throws Exception {//结束时触发
        super.onEndWorkflow(node);
        if (Framework.isNotNullOrEmpty(node.getDocColumn()) && node.getDocColumn().equalsIgnoreCase("DocState") && "30".equals(node.getDocState())) {
            updateWhetherUpload(node.getEntityId(), 10);
        }
        if (Framework.isNotNullOrEmpty(node.getDocColumn()) && node.getDocColumn().equalsIgnoreCase("Submission") && "30".equals(node.getDocState())) {
            updateWhetherUpload(node.getEntityId(), 20);
        }

    }

    private void updateDrag() throws Exception {//更新底稿修改状态
        CompositionCustomer oldcust = this.getOriginalValue();
        if (Framework.isNullOrEmpty(oldcust))
            return;
        if (Framework.isNotNullOrEmpty(oldcust.getManuscriptReviewState()) && Framework.isNotNullOrEmpty(this.getManuscriptReviewState()) && !oldcust.getManuscriptReviewState().equals(this.getManuscriptReviewState()) && !oldcust.getManuscriptReviewState().equals("10")) {//底稿复核状态修改后,修改底稿状态
            pushDragState(this.getUuid());

        }
    }

    public void projectStatusModification() throws Exception {//状态修改
        Project project = this.getProjectId();
        if (Framework.isNullOrEmpty(project.getDocState())) return;
        if (project.getDocState() <= 11) return;
        if (project.getDocState() >= 100) return;

        CompositionCustomer oldData = this.getOriginalValue();
        LocalDateTime originalAuditEnd = oldData.getAuditEnd();
        LocalDateTime originalAuditStart = oldData.getAuditStart();
        Integer originalRiskLevel = oldData.getRiskLevel();
        LocalDateTime auditEnd = this.getAuditEnd();
        LocalDateTime auditStart = this.getAuditStart();
        Integer riskLevel = this.getRiskLevel();
        if (!originalAuditEnd.equals(auditEnd) || !originalAuditStart.equals(auditStart) || !originalRiskLevel.equals(riskLevel)) {
            project.setDocState(11);
            repository().add(project);
        }
    }


    /**
     * 设置底稿上传状态
     *
     * @param uuid
     * @throws Exception
     */
    public void pushDragState(String uuid) throws Exception {
        List<ManuscriptItem> list = ManuscriptItem.stream().where(w -> w.getAuditworkProjectID().equals(uuid)).toList();
        for (ManuscriptItem item : list) {
            String[] split = item.getManuscriptFileRelativePath().split("\\\\");
            String templateFile = DxnConfig.getFileUploadPath();
            if (item.getModifyStatus() == 30) {
                Path path = Paths.get(templateFile + "\\\\" + item.csUploadPath(this.getCode(), 10).toString() + "\\\\ManuscriptFile", split);
                File file = path.toFile();
                repository().remove(item);
                file.delete();
            } else if (item.getModifyStatus() != 0) {
                Path path = Paths.get(templateFile + "\\\\" + item.csUploadPath(this.getCode(), 20).toString() + "\\\\ManuscriptFile", split);
                File file = path.toFile();
                item.setModifyStatus(0);
                repository().add(item);
                file.delete();
            }
        }
    }

    /**
     * /质量复核左边的树 数据源
     *
     * @param projectId
     * @return
     * @throws Exception
     */
    @SystemService(args = "query_projectId,query_isReview")
    public List<CompositionCustomerTreeDTO> getCompositionCustomerTreeDTO(Long projectId, Boolean isReview) throws Exception {
        ArrayList result = new ArrayList();

        List<CompositionCustomer> compList = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId)).sortedBy(c -> c.getLevel()).sortedDescendingBy(c -> c.getCompanyRelationship()).sortedBy(c -> c.getSortCode()).toList();

        if (compList.size() > 0) {

            MCompositionCustomerDTOE dtoMain = new MCompositionCustomerDTOE();
            dtoMain.setIsReview(isReview);
            List<CompositionCustomerTreeDTO> mainList = parseTreeDTO(compList, dtoMain);
            return CompositionCustomerTreeDTO.ParseTreeList(mainList, dtoMain);
        }

//        MCompositionCustomerDTOE rdata = getCompositionCustomerDTOE(projectId);
//        List<TreeDTO> treelist = getTreeDTO(rdata, null);

        return result;
    }


    /**
     * 生成底稿目录
     * GBY
     *
     * @throws Exception
     */
    public void createManuscriptCautalog(String FileName) throws Exception {

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

        ManuscriptItem item = new ManuscriptItem();
        List<TreeDTO> list = item.getManuscriptItemDTO(this.getId(), "2");

        ManuscriptCautalogDTO dto = new ManuscriptCautalogDTO();
        List<ManuscriptCautalog> mnuscriptCautalogItem = new ArrayList<>();

        for (TreeDTO t : list) {
            pushManuscriptCautalog(mnuscriptCautalogItem, t);
        }
        dto.setMnuscriptCautalogItem(mnuscriptCautalogItem);

        ExcelUtils.writeXLSX(templateFile, newFile, dto);
    }

    @Override
    public String gainAttachmentPath() throws Exception {
        String path = super.gainAttachmentPath();
        return String.format("%s/%s", path, this.getCode());
    }

    /**
     *
     */

    private void pushManuscriptCautalog(List<ManuscriptCautalog> mnuscriptCautalogItem, TreeDTO tree) {
        ManuscriptCautalog item = new ManuscriptCautalog();
        if (tree instanceof ManuscriptItemTreeDTO) {
            ManuscriptItemTreeDTO mitem = (ManuscriptItemTreeDTO) tree;
            item.setFileName(mitem.getText());
            if (Framework.isNotNullOrEmpty(mitem.getIndexNumber())) {
                item.setIndex(mitem.getIndexNumber());
            } else {
                item.setIndex("");
            }
            item.setHook("1");
            mnuscriptCautalogItem.add(item);
            if (mitem.getChildren().size() > 0) {
                for (TreeDTO t : mitem.getChildren()) {
                    pushManuscriptCautalog(mnuscriptCautalogItem, t);
                }

            } else
                return;
        }

    }


    /**
     * 通用获取跟自己相关的组成部分树
     * swq
     */
    private List<CompositionCustomerTreeDTO> parseTreeDTO(List<CompositionCustomer> data, MCompositionCustomerDTOE dtoE) throws Exception {
        ArrayList result = new ArrayList();
        for (CompositionCustomer item : data) {
            CompositionCustomerTreeDTO dto = new CompositionCustomerTreeDTO();
            Long itemId = item.getId();
            dto.setId(itemId.toString());
            dto.setText(item.getName());
            dto.setCode(item.getCode());
            dto.setComplete(10);
            dto.setCompanyRelationship(item.getCompanyRelationship());

            List<DraftReviewBatchItem> Dlist = DraftReviewBatchItem.stream().where(w -> w.getCompositionCustomerId() == itemId).toList();
            List<Long> longlist = new ArrayList<>();

            for (DraftReviewBatchItem ditem : Dlist) {
                if (Framework.isNotNullOrEmpty(ditem.getDraftReviewBatchId().getDocStateAll()) && (ditem.getDraftReviewBatchId().getDocStateAll().contains("30") || ditem.getDraftReviewBatchId().getDocStateAll().contains("40") || ditem.getDraftReviewBatchId().getDocStateAll().contains("50")
                        || ditem.getDraftReviewBatchId().getDocStateAll().contains("60") || ditem.getDraftReviewBatchId().getDocStateAll().contains("70"))) {
                    dto.setDraftReviewBatchId(ditem.getDraftReviewBatchId().getId());
                    continue;
                }
                if (longlist.size() > 0) {
                    boolean lb = false;
                    for (Long lid : longlist) {
                        if (lid == ditem.getDraftReviewBatchId().getId()) {
                            lb = true;
                            break;
                        }
                    }
                    if (!lb) {
                        longlist.add(ditem.getDraftReviewBatchId().getId());
                    }
                } else {
                    longlist.add(ditem.getDraftReviewBatchId().getId());
                }
            }
            dto.setDraftReviewBatchIdhis(longlist);


//            Optional<DraftReviewBatchItem> firstDra = DraftReviewBatchItem.stream().where(w -> w.getCompositionCustomerId() == itemId).findFirst();
//            if (firstDra.isPresent()) {
//                dto.setDraftReviewBatchId(firstDra.get().getDraftReviewBatchId().getId());
//            }

            if (Framework.isNotNullOrEmpty(item.getIsComplete())) {
                dto.setComplete(item.getIsComplete());
            }

            String state = item.getDocStateAllE();
            if (Framework.isNotNullOrEmpty(state)) {
                // dto.setManuscriptReviewState(f.getManuscriptReviewState());
                if (state.contains("30") || state.contains("40") || state.contains("50") || state.contains("60")) {
                    dtoE.setHasReviewing(true);

                }
                if (!state.equals("10")) {
                    dto.setStateText(getEnumName("ManuscriptReviewStateType", state));
                    dto.setDocStateAllE(state);
                }

                if (!state.contains("70")) {
                    dto.setChecked(true);
                }
                if (Framework.isNullOrEmpty(state) || state.startsWith("10") || state.startsWith("100"))
                    dto.setChecked(false);
            }

            if (Framework.isNullOrEmpty(state) || state.startsWith("10") || state.startsWith("100")) {
                dto.setReadOnly(false);
            } else {
                dto.setReadOnly(true);
            }

//            dto.setState("closed");

            if (Framework.isNotNullOrEmpty(item.getProjectManagerId())) {
                //项目页面是新增没有关闭的组成部分有权限调整
                if (item.getProjectManagerId().getId().equals(AppContext.current().getProfile().getId())) {
//                    dto.setState("open");
                    dto.setIsCurrentProjectManage(true);
                } else {
                    dto.setChecked(false);
                    dto.setReadOnly(true);//非项目经理登录 只读
                }
            }
            //末级不需要设置打开状态
            if (item.getChildren().size() == 0) {
                dto.setState(null);
            }


            if (Framework.isNotNullOrEmpty(item.getParentId())) {
                dto.setParentId(item.getParentId().getId().toString());
            }

            result.add(dto);
        }

        return result;
    }

    public String getEnumName(String code, String value) throws Exception {
        return EnumValue.createNew().gainEnumDataDTO(code, value);
    }

    private MCompositionCustomerDTOE getCompositionCustomerDTOE(Long prjId) throws Exception {

        MCompositionCustomerDTOE rdata = new MCompositionCustomerDTOE();
        rdata.setHasReviewing(false);
        List<CompositionCustomerDTOE> list = new ArrayList<>();
        List<CompositionCustomer> customers = this.stream().where(w -> w.getProjectId().getId() == prjId).sortedBy(s -> s.getSort()).toList();
        if (customers.size() > 0) {
            for (CompositionCustomer f : customers) {
                CompositionCustomerDTOE dto = new CompositionCustomerDTOE();
                dto.setId(f.getId());
                dto.setName(f.getName());
                dto.setCode(f.getCode());

                Long cid = f.getId();
                Optional<DraftReviewBatchItem> hasItem = DraftReviewBatchItem.stream().where(w -> w.getCompositionCustomerId() == cid).findFirst();
                if (hasItem.isPresent()) {
                    dto.setDraftReviewBatchId(hasItem.get().getDraftReviewBatchId().getId());
                }


                if (Framework.isNotNullOrEmpty(f.getIsComplete())) {
                    dto.setComplete(f.getIsComplete());
                }
                if (Framework.isNotNullOrEmpty(f.getDocStateAllE())) {
                    // dto.setManuscriptReviewState(f.getManuscriptReviewState());
                    String state = f.getDocStateAllE();
                    if (state.contains("30") || state.contains("40") || state.contains("50") || state.contains("60")) {
                        rdata.setHasReviewing(true);

                    }
                    if (Framework.isNotNullOrEmpty(f.getDocStateAllE())) {
                        dto.setState(getEnumName("ManuscriptReviewStateType", f.getDocStateAllE()));
                        dto.setDocStateAllE(f.getDocStateAllE());
                    }

                }

                if (Framework.isNotNullOrEmpty(f.getParentId())) {
                    dto.setParentId(f.getParentId().getId());
                }

                list.add(dto);
            }
        }

        rdata.setList(list);

        return rdata;
    }

    private List<TreeDTO> getTreeDTO(MCompositionCustomerDTOE rdata, Long parentId) {
        List<CompositionCustomerDTOE> clist = rdata.getList();
        List<TreeDTO> list = new ArrayList<>();
        Stream<CompositionCustomerDTOE> childless;
        if (Framework.isNullOrEmpty(parentId)) {
            childless = clist.stream().filter(f -> f.getParentId() == null);
        } else {
            childless = clist.stream().filter(f -> f.getParentId() != null && f.getParentId() == parentId);
        }
        childless.forEach(f -> {
            TreeDTO dto = f.toTreeDTO(rdata.getHasReviewing());
            dto.setChildren(getTreeDTO(rdata, f.getId()));
            list.add(dto);
        });

        return list;
    }

    /**
     * //通过被审计单位的UUID获取对应项目经理的CODE
     *
     * @param prjUUid
     * @return
     * @throws Exception
     */
    @SystemService(args = "prjUUid")
    public String getprojectManagerCode(String prjUUid) throws Exception {
        Optional<CompositionCustomer> customer = this.stream().where(w -> w.getUuid().equals(prjUUid)).findFirst();
        if (customer.isPresent()) {
            return customer.get().getProjectManagerId().getCode();
        }
        return "";
    }

    /**
     * 通过ID查当前被审计单位的底稿复核状态，如果是复核成功 返回false, 用于控制 添加一条复核意见 不可用
     *
     * @param cId
     * @return
     * @throws Exception
     */
    @SystemService(args = "cId")
    public boolean getManuscriptReviewStateboolean(Long cId) throws Exception {
        CompositionCustomer c = CompositionCustomer.findById(cId);
        if (c.getDocStateAllE().contains(String.valueOf(ManuscriptReviewStateEnum.ReviewCompleted.getIndex()))) {
            return false;
        }
        return true;
    }

    /**
     * 通过项目id获取项目组成员中的 角色等于项目经理
     *
     * @param projectId
     * @return
     * @throws Exception
     */
    @SystemService(args = "projectId,level")
    public List<ProjectUserDTO> getProjectMangerByProjectId(Long projectId, Integer level) throws Exception {
        List<ProjectUserDTO> listDTO = new ArrayList<>();
        Integer state = IsEnableEnum.Enable.getIndex();
        Integer small = RoleLevelEnum.SmallManger.getIndex();
        Integer big = RoleLevelEnum.BigManager.getIndex();
        List<ProjectMembers> userList;
        if (level == 20) {
            userList = ProjectMembers.stream().where(c -> (c.getProjectRole().equals(small) || c.getProjectRole().equals(big)) && c.getProjectId().getId() == projectId && c.getIsEnable() == state).toList();
        } else {
            userList = ProjectMembers.stream().where(c -> c.getProjectRole().equals(level) && c.getProjectId().getId() == projectId && c.getIsEnable() == state).toList();
        }
        for (ProjectMembers item : userList) {
            ProjectUserDTO dto = new ProjectUserDTO();
            dto.setMemberId(item.getId());
            dto.setId(item.getNameId().getId());
            dto.setCode(item.getNameId().getCode());
            dto.setName(item.getNameId().getName());
            EnumValueDTO enumDTO = EnumValue.createNew().getEnumDTO("ProjectRoleLevel", item.getProjectRole());
            if (Framework.isNotNullOrEmpty(enumDTO) && Framework.isNotNullOrEmpty(enumDTO.getName())) {
                dto.setRoleName(enumDTO.getName());
            }
            listDTO.add(dto);
        }
        return listDTO;
    }

    /**
     * 获取被审计单位的几个字段
     *
     * @param compostionId
     * @return
     * @throws Exception
     */
    @SystemService(args = "compostionId")
    public CompositionCustomerDTO getCompostionCustomer(Long compostionId) throws Exception {
        CompositionCustomerDTO dto = new CompositionCustomerDTO();
        CompositionCustomer model = CompositionCustomer.findById(compostionId);
        dto.setId(model.getId());
        dto.setName(model.getName());
        if (Framework.isNotNullOrEmpty(model.getCompanyRelationship())) {
            dto.setCompanyRelationship(model.getCompanyRelationship());
        }
        if (Framework.isNotNullOrEmpty(model.getParentId()))
            dto.setParentId(model.getParentId().getId());
        dto.setOrgCode(model.getOrgCode());
        return dto;
    }

    /**
     * 验证客户类型是否存在
     *
     * @param compostionCustomerId
     * @throws Exception
     */
    @SystemService(args = "compostionCustomerId")
    public void getIsCustomer(Long compostionCustomerId) throws Exception {
        Optional<CompositionCustomer> compostionCustomer = this.stream().where(c -> c.getId().equals(compostionCustomerId)).findFirst();
        if (compostionCustomer.isPresent()) {
            if (Framework.isNullOrEmpty(compostionCustomer.get().getCustomerType()) ||
                    compostionCustomer.get().getCustomerType() == 0) {
                throw new BusinessException("请完善并保存您选中的项目架构信息后再新增旗下子分公司!");
            }
        }
    }

    /**
     * GBY
     * 更新是否允许上传 底稿字段的值
     *
     * @param cid
     * @param val
     * @throws Exception
     */
    @SystemService(args = "cid,val")
    public void updateWhetherUpload(Long cid, Integer val) throws Exception {
        CompositionCustomer cust = CompositionCustomer.findById(cid);
        cust.setWhetherUpload(val);
        cust.setRandomCode(Framework.createGuid().toString());
        comterModifyType(Framework.createGuid().toString(), 20, cid);
    }

    @JsonIgnore
    public MyTodoListDTO gainWorkFlowDTO(MyTodoListDTO value) throws Exception {
        if (Framework.isNullOrEmpty(this.getCustomerNameId())) {
            value.setDepartmentType(this.getName());
        } else {
            value.setDepartmentType(this.getCustomerNameId().getCustomerFullName());
        }
        User user = this.getCreatedBy();
        String processId = value.getProcess_Id();
        Optional<WorkFlowConfig> workFlowConfiglist = WorkFlowConfig.stream().where(w -> w.getProcessId().equals(processId)).findFirst();
        if (workFlowConfiglist.isPresent()) {
            String deptIdName = user.getDeptReadOnly().getOrgReadOnly().getNickname();
            WorkFlowConfig workFlowConfig = workFlowConfiglist.get();
            value.setWorkFlowName(deptIdName + "-" + workFlowConfig.getWorkFlowTypeId().getName());
        }
        if (Framework.isNotNullOrEmpty(user)) {
            if (Framework.isNotNullOrEmpty(user.getDeptReadOnly())) {
                value.setDepartment(user.getDeptReadOnly().getTreeName());
            }
        }
        return value;
    }

    //根据项目ID获取主项目ID ，定位到主项目时使用
    @JsonIgnore
    @SystemService(args = "prjId")
    public CompositionCustomerDTOE getCompositionCustomerByPrjId(Long prjId) throws Exception {
        Optional<CompositionCustomer> first = CompositionCustomer.stream().where(w -> w.getProjectId().getId().equals(prjId) && w.getParentId() == null).findFirst();
        if (!first.isPresent()) return new CompositionCustomerDTOE();
        return toCompositionCustomerDTOE(first.get());
    }

    private CompositionCustomerDTOE toCompositionCustomerDTOE(CompositionCustomer item) {
        CompositionCustomerDTOE dto = new CompositionCustomerDTOE();
        dto.setCode(item.getCode());
        dto.setId(item.getId());
        dto.setName(item.getName());

        return dto;
    }

    /**
     * 通过被审计单位UUID 获取项目和被审计单位ID信息
     * C端接口用到
     * GBY
     */
    @SystemService(args = "cUUid")
    public CompositionCustomerProjectDTO gainCompositionCustomerProjectDTObyUUid(String cUUid) throws Exception {
        CompositionCustomerProjectDTO dto = new CompositionCustomerProjectDTO();
        String docstate = "";
        Optional<CompositionCustomer> opt = CompositionCustomer.stream().where(w -> w.getUuid().equals(cUUid)).findFirst();
        if (opt.isPresent()) {
            CompositionCustomer customer = opt.get();
            dto.setCustid(customer.getId());
            dto.setProjectid(customer.getProjectId().getId());

            Long custid = customer.getId();

            String items = customer.getDocStateAll();
            if (Framework.isNotNullOrEmpty(items)) docstate = items + "/";
            ;

            List<Integer> listcoms = ReviewComments.stream().where(w -> w.getCompositionCustomerId().getId() == custid).select(s -> s.getManuscriptReviewState()).distinct().toList();
            for (Integer item : listcoms) {
                String tmp = item.toString();
                if (Framework.isNullOrEmpty(tmp))
                    break;
                tmp += "/";
                if (!docstate.contains(tmp)) {
                    docstate += tmp;
                }
            }
            dto.setManuscriptReviewState(docstate);

        }

        return dto;
    }

    /**
     * 通用获取跟自己相关的组成部分树
     * isAdd 只有在项目新增页面没有关闭的时候才是true
     * isApproval: 是否审批，数据在审批流程中的为true 不进行只读控制
     * SWQ
     */
    @SystemService(args = "query_projectId,query_isAdd,query_isApproval")
    public List<CompositionCompetenceTreeDTO> getCompositionCompetenceTreeDTO(Long projectId, Boolean isAdd, Boolean isApproval) throws Exception {
        ArrayList result = new ArrayList();
        List<CompositionCustomer> compList = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId)).sortedBy(c -> c.getLevel()).sortedDescendingBy(c -> c.getCompanyRelationship()).sortedBy(c -> c.getSortCode()).toList();
        if (compList.size() > 0) {
            isAdd = Framework.isNullOrEmpty(isAdd) ? false : isAdd;
            isApproval = Framework.isNullOrEmpty(isApproval) ? false : isApproval;
            return CompositionCompetenceTreeDTO.Parse(compList, isAdd, isApproval);
        }

        return result;
    }

    /**
     * 获取我的借阅项目树，被借阅的审计单位 非只读，其他只读
     *
     * @param projectId 项目ID
     * @param custId    不只读的被审计单位ID
     * @return
     * @throws Exception
     */
    @SystemService(args = "query_projectId,query_custId")
    public List<CompositionCompetenceTreeDTO> gainCompositionCompetenceTreeDTO(Long projectId, Long custId) throws Exception {
        ArrayList result = new ArrayList();
        if (Framework.isNullOrEmpty(projectId)) {
            CompositionCustomer c = repository().findById(custId, CompositionCustomer.class);
            projectId = c.getProjectId().getId();
        }
        Long pid = projectId;
        List<CompositionCustomer> compList = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(pid)).sortedBy(c -> c.getLevel()).sortedDescendingBy(c -> c.getCompanyRelationship()).sortedBy(c -> c.getSortCode()).toList();
        if (compList.size() > 0) {
            return CompositionCompetenceTreeDTO.Parse(compList, custId);
        }
        return result;
    }

    /**
     * 统一信用代码是否重复
     *
     * @param projectId
     * @param orgCode
     * @return
     * @throws Exception
     */
    private static Boolean IsorgCodeRepeat(Long projectId, String orgCode) throws Exception {
        Optional<CompositionCustomer> result = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId) && c.getOrgCode().equals(orgCode)).findAny();
        if (result.isPresent())
            return true;
        return false;
    }

    /**
     * 项目架构树的保存按钮方法
     * <p>
     * SWQ
     */
    @SystemService(args = "dto")
    public Long saveCompositionCustomer(CompositionCustomerDTO dto) throws Exception {
        String name = dto.getName();
        boolean isAdd = false;
        Long projectId = dto.getProjectId();

        //项目的组织架构发生变更,是否重新走流程的状态 1为需要重新走流程，否则不需要
        Integer addAfterCompletion = dto.getAddAfterCompletion();
        Project project = Project.findById(projectId);

        CompositionCustomer model = CompositionCustomer.findById(dto.getId());
        if (Framework.isNotNullOrEmpty(model)) {//修改的时候
            if (!model.getOriginalValue().getOrgCode().equals(dto.getOrgCode())) {
                if (IsorgCodeRepeat(projectId, dto.getOrgCode())) {
                    throw new BusinessException("同一个项目里，母，子，分公司的社会统一信用代码不能重复！");
                }
            }
            if (Framework.isNotNullOrEmpty(addAfterCompletion) && addAfterCompletion == 1) {
                if (Framework.isNotNullOrEmpty(project)) {
                    project.setDocState(11);//重新走走流程
                }
            }
        } else {
            //region 新增的赋值
            if (IsorgCodeRepeat(projectId, dto.getOrgCode())) {
                throw new BusinessException("同一个项目里，母，子，分公司的社会统一信用代码不能重复！");
            }
            //新增的时候
            model = CompositionCustomer.createNew();
            model.setIsMainProject(20);
            model.setIsMemberChanged(IsChangeEnum.No.getIndex());
            model.setStateCode(StateCodeEnum.normal.getIndex());
            model.setRandomCode(Framework.createGuid().toString());
            model.setTerminationProject(0);
            model.setRandomCode(Framework.createGuid().toString());
            model.setIsComplete(IsEnableEnum.Enable.getIndex());
            model.setAuditStart(project.getAuditStart());
            model.setAuditEnd(project.getAuditEnd());
            model.setBusinessType(project.getBusinessType());
            model.setBusinessType(project.getBusinessType());
            model.setBusinessTypeValue(project.getBusinessTypeValue());
            model.setFirstOrContinuous(project.getFirstOrContinuous());
            model.setTotalAssets(0);
            model.setProject(projectId);
            if (Framework.isNotNullOrEmpty(project.getDeptId()))
                model.setDeptId(project.getDeptId());

            model.setIsReport(true);
            project.setIsChange(IsChangeEnum.Yes.getIndex());
            project.setRandomCode(Framework.createGuid().toString());
            Optional<CompositionCustomer> firstData = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId) && c.getLevel() == 1).findFirst();
            if (firstData.isPresent() && Framework.isNotNullOrEmpty(firstData.get().getManuscriptTemplate()) && firstData.get().getManuscriptTemplate() > 0)
                model.setManuscriptTemplate(firstData.get().getManuscriptTemplate());
            isAdd = true;
            //endregion
        }
        String compositionName = name + "-" + model.getBusinessTypeValue() + "-" + Framework.localDateToString(model.getAuditStart()) + "至" + Framework.localDateToString(model.getAuditEnd());
        model.setShowName(compositionName);
        model.setName(name);
        model.setCompanyRelationship(dto.getCompanyRelationship());
        model.setOrgCode(dto.getOrgCode());
        model.setParent(dto.getParentId());

        //region 校验数据信息

        Long id = !isAdd ? model.getId() : -1L;
        Optional<CompositionCustomer> repeatComp = project.getCompositionCustomers().stream().filter(c -> !c.getId().equals(id) && c.getName().equals(name)).findAny();
        if (repeatComp.isPresent()) {
            throw new BusinessException("被审计单位名称重复！");
        }
        CompositionCustomer parent = model.getParent();

        if (isAdd) {//新增的时候
            //  parent.getCompanyRelationship() 为空的时候是根节点，根节点为母公司，可以增加任何公司
            if (Framework.isNotNullOrEmpty(parent) && Framework.isNotNullOrEmpty(parent.getCompanyRelationship()) && parent.getCompanyRelationship() == 20)
                throw new BusinessException("分公司下不能在加任何公司！");
        } else {//修改的时候

            if (model.getCompanyRelationship() == 20 && model.getChildren().size() > 0) {
                throw new BusinessException(String.format("[%s]公司下已存在其他公司，公司类型不能修改为分公司!", model.getName()));
            }
            //修改时重新计算长名称
            if (Framework.isNotNullOrEmpty(model.getCustomerType()) && model.getCustomerType() > 0) {
                String projectName = dto.getName() + "-" + model.getBusinessTypeValue() + "-" + Framework.localDateToString(model.getAuditStart()) + "至" + Framework.localDateToString(model.getAuditEnd());
                model.setShowName(projectName);
            }

        }
        Integer[] stateArray = new Integer[]{11, 30, 31, 32, 33};
        //只有申请修改的时候才可以  新增或者修改 都认为是项目架构发生变化
        if (Arrays.asList(stateArray).contains(project.getDocState())) {
            //if (project.getDocState() == 11 || project.getDocState() == 30) {
            //子分公司 名称 级次（暂缺）  增
            if (Framework.isNotNullOrEmpty(model.getOriginalValue())) {
                if (model.getOriginalValue().getCompanyRelationship() != dto.getCompanyRelationship()) {
                    // 这行代码暂时保留
                    project.setRandomCode(Framework.createGuid().toString());
                    //  更新顶级和当前级的上一级的随机码
                    model.setRandomCode(Framework.createGuid().toString());
                    updateProjectAndCompostionCustomerRandomCode(model.getProjectId().getId(), model.getParentId().getId());
                }
                if (!model.getOriginalValue().getName().equals(dto.getName())) {
                    model.setRandomCode(Framework.createGuid().toString());
                    comterModifyType(Framework.createGuid().toString(), 40, model.getId());
                    updateProjectAndCompostionCustomerRandomCode(model.getProjectId().getId(), model.getParentId().getId());
                }
            }
            //新增的时候
            if (isAdd) {
                model.setRandomCode(Framework.createGuid().toString());
                updateProjectAndCompostionCustomerRandomCode(dto.getProjectId(), dto.getParentId());
            }

        }


        if (Framework.isNotNullOrEmpty(parent)) {
            if (Framework.isNullOrEmpty(parent.getCustomerType()) || parent.getCustomerType() == 0)
                throw new BusinessException("上级单位客户类型不能为空！");
        }

        //endregion

        //region 设置排序SortCode 属性值
        String sortCode = model.getSortCode();
        if (Framework.isNotNullOrEmpty(parent))
            sortCode = parent.getSortCode();

        Long parentId = dto.getParentId();
        // 排序
        List<CompositionCustomer> compList = CompositionCustomer.stream().where(c -> c.getParentId().getId() == parentId).toList();
        if (Framework.isNullOrEmpty(model.getId()))
            compList.add(model);
        if (compList.size() > 0) {
            List<String> nameList = new ArrayList<>();
            for (CompositionCustomer item : compList) {
                String company = item.getCompanyRelationship() == 20 ? "10" : "20";
                String value = String.format("%s%s:%s", company, Framework.getPinYin(item.getName()), item.getName());
                nameList.add(value);
            }
            Collections.sort(nameList);
            int sort = 1;
            for (int i = 0; nameList.size() > i; i++) {
                String nameFor = nameList.get(i);
                String[] starr = nameFor.split(":");
                for (CompositionCustomer item : compList) {
                    if (starr[1].toLowerCase().equals(item.getName().toLowerCase())) {
                        item.setSort(sort);
                        item.setSortCode(String.format("%s1000%s", sortCode, sort));
                    }
                }
                sort++;
            }
        }

        //endregion
        repository().add(model);

        if (isAdd) {//新增的时候
            //项目合伙人添加到组成部分成员中
            addCompositionMember(model, project);
        }
        repository().commit();//提交一下数据
        return model.getId();
    }

    //添加组成部分成员
    public void addCompositionMember(CompositionCustomer customer, Project project) throws Exception {
        User partner = project.getProjectPartnerId();
        Long partnerId = partner.getId();
        Long projectId = customer.getProjectId().getId();
        Optional<ProjectMembers> projectMembersFirst = ProjectMembers.stream().where(c -> c.getNameId().getId() == partnerId && c.getProjectId().getId() == projectId).findFirst();
        Integer signatureOfIndependence = 20;
        if (projectMembersFirst.isPresent()) {
            signatureOfIndependence = projectMembersFirst.get().getSignatureOfIndependence();
        }
        if (Framework.isNullOrEmpty(partner)) return;
        CompositionMembers entity = CompositionMembers.createNew();
        entity.setCompositionCustomerId(customer);
        entity.setNameId(partner);
        entity.setCode(partner.getCode());
        entity.setSignatureOfIndependence(signatureOfIndependence);
        Staff staff = partner.getStaff();
        if (Framework.isNotNullOrEmpty(staff)) {
            if (Framework.isNotNullOrEmpty(staff.getStaffPositionId()))
                entity.setRank(staff.getStaffPositionId().getName());   //职级后期需要改变类型
            entity.setQualifications(staff.getPractisingQualification());
        }
        entity.setWorkingYears(projectMembersFirst.get().getWorkingYears());
        entity.setIsExperience(10);
        entity.setIsEnable(IsEnableEnum.Enable.getIndex()); //10是启用 20是禁用
        entity.setSort(0);
        entity.setRoleName(RoleLevelEnum.Partner.getIndex());
        repository().add(entity);
    }

    // 被审计单位变更表
    public void comterModifyType(String randomCode, int modifyType, long auditProjectId) throws Exception {
        Optional<CompositionCustomerModifyRecord> compostitiongCustomerListOne = CompositionCustomerModifyRecord.stream().where(s -> s.getModifyType() == modifyType && s.getCompositionCustomerId().getId().equals(auditProjectId)).findFirst();
        if (compostitiongCustomerListOne.isPresent()) {
            compostitiongCustomerListOne.get().setRandomCode(randomCode);
            repository().add(compostitiongCustomerListOne.get());
        } else {
            CompositionCustomerModifyRecord ccmde = CompositionCustomerModifyRecord.createNew();
            ccmde.setCompositionCustomerId(getCompostionCustomer(auditProjectId));
            ccmde.setModifyType(modifyType);
            ccmde.setRandomCode(randomCode);
            //this.getCompositionCustomerModifyRecords().add(ccmde);
            repository().add(ccmde);
        }
    }

    private CompositionCustomer getCompostionCustomer(long id) throws Exception {
        CompositionCustomer customer = CompositionCustomer.findById(id);
        return customer;
    }

    /**
     * 更改被审计单位一级的随机码 和当前添加的被审计单位的上一级
     *
     * @param projectId
     * @throws Exception
     */
    public void updateProjectAndCompostionCustomerRandomCode(long projectId, long compostionCustomerParentId) throws Exception {
        List<CompositionCustomer> compostion = CompositionCustomer.stream().where(c -> c.getProjectId().getId().equals(projectId) && c.getLevel() == 1).toList();
        if (compostion.size() > 0) {
            compostion.get(0).setRandomCode(Framework.createGuid().toString());
            repository().add(compostion.get(0));
        }
        if (compostionCustomerParentId != -1L) {
            CompositionCustomer parentCompostionCustomer = CompositionCustomer.findById(compostionCustomerParentId);
            parentCompostionCustomer.setRandomCode(Framework.createGuid().toString());
        }
    }

    @SystemService(args = "dto")
    public void saveTreeSort(List<TreeDTO> dto) throws Exception {
        if (Framework.isNullOrEmpty(dto)) return;


        for (TreeDTO item : dto) {
            CompositionCustomer customer = this.findById(new Long(item.getId()));
            customer.setSort(item.getSort());
            customer.setTreeCode(item.getTreeCode());

            //只有等于修改中或者已完成的
            if (customer != null && customer.getProjectId().getDocState() == ProjectEnum.Amendment.getIndex() || customer.getProjectId().getDocState() == ProjectEnum.AlreadyStarted.getIndex()) {
                if (Framework.isNotNullOrEmpty(customer.getParentId())) {
                    if (Framework.isNotNullOrEmpty(customer.getParentId())
                            && customer.getParentId().getId() != Integer.parseInt(item.getParentId())) {

                        customer.setRandomCode(Framework.createGuid().toString());
                        long parentId = customer.getParentId() == null ? -1L : customer.getParentId().getId();
                        updateProjectAndCompostionCustomerRandomCode(customer.getProjectId().getId(), parentId);
                    }
                }
            }


            if (Framework.isNullOrEmpty(item.getParentId())) {
                customer.setParentId(null);
            } else {
                customer.setParent(new Long(item.getParentId()));
            }

        }
    }


    private List<ManuscriptItemFileDTO> gainManuscriptItemFileDTO(String uuid) throws Exception {

        List<ManuscriptItemFileDTO> list = repository().sqlQuery("with abc(manuscriptItemID,ManuscriptFile,ManuscriptDisplayName,StructureCode,ParentStructureCode,SortNO,IsGroup,manuscriptType,IndexNumber,ManuscriptFileRelativePath,ArchiveDisplay,fiscalyear) \n" +
                "as \n" +
                "(\n" +
                "select  uuid as manuscriptItemID,ManuscriptFile,ManuscriptDisplayName,StructureCode,ParentStructureCode,SortNO,IsGroup,manuscriptType,IndexNumber,ManuscriptFileRelativePath,ArchiveDisplay,fiscalyear from Prj_ManuscriptItem\n" +
                "where IsGroup='0' and IsSelected='1' and IsDisplay ='1' and Modifystatus in(10,20,0) and  (TheColor in('Blue','Black') or   ArchiveDisplay ='1'" +
                "or uuid in (select manuscriptItemID  from Prj_AuditFilesVSManuscript where auditworkProjectID ='" + uuid + "'))  and AuditworkProjectID ='" + uuid + "'\n" +
                "union all \n" +
                "select dt.uuid,dt.ManuscriptFile,dt.ManuscriptDisplayName,dt.StructureCode,dt.ParentStructureCode,dt.SortNO,dt.IsGroup,dt.manuscriptType,dt.IndexNumber,dt.ManuscriptFileRelativePath,dt.ArchiveDisplay,dt.fiscalyear from Prj_ManuscriptItem as dt \n" +
                "inner join abc as c on dt.StructureCode = c.ParentStructureCode and dt.AuditworkProjectID ='" + uuid + "' and (c.fiscalyear = dt.fiscalyear or dt.fiscalyear =0)\n" +
                ") \n" +
                " select distinct * from abc   order by StructureCode,SortNO", ManuscriptItemFileDTO.class);

        return list;
    }

    private List<FileDetailedListDTO> gainFileDetailedListDTOList(List<ManuscriptItemFileDTO> list, String parentId, String code, String Uuid) throws Exception {
        List<FileDetailedListDTO> dtoList = new ArrayList<>();


        List<ManuscriptItemFileDTO> childless;

        if (Framework.isNullOrEmpty(parentId)) {
            childless = list.stream().filter(f -> f.getParentStructureCode() == null || f.getParentStructureCode().equals("")).collect(Collectors.toList());
        } else {
            childless = list.stream().filter(f -> f.getParentStructureCode() != null && f.getParentStructureCode().equals(parentId)).collect(Collectors.toList());
        }

        for (ManuscriptItemFileDTO item : childless) {
            FileDetailedListDTO dto = new FileDetailedListDTO();
            dto.setDisplayName(item.getManuscriptDisplayName());
            dto.setSortNo(item.getSortNO().toString());
            dto.setIndexNumber(item.getIndexNumber());
            dto.setManuscriptFile(item.getManuscriptFile());


            if ("1".equals(item.getIsGroup())) {
                dto.setIsFile("0");
                dto.setFileFullName("");
                dto.setChildManuscriptItems(gainFileDetailedListDTOList(list, item.getStructureCode(), code, Uuid));
            } else {
                dto.setIsFile("1");
                if ("1".equals(item.getArchiveDisplay())) {
                    if (item.getManuscriptFileRelativePath().contains(code)) {
                        dto.setFileFullName(item.getManuscriptFileRelativePath().substring(item.getManuscriptFileRelativePath().indexOf(code) + code.length() + 1));
                    } else {
                        dto.setFileFullName(item.getManuscriptFileRelativePath());
                    }
                } else {
                    dto.setFileFullName("ManuscriptItem//Original//ManuscriptFile//" + item.getManuscriptFileRelativePath());
                }
                dto.setChildManuscriptItems(gainAttFileDetailedListDTO(item.getManuscriptItemID(), Uuid));
            }
            dto.setFileType("0");
            dto.setFileSourceName(item.getManuscriptFileRelativePath());
            dtoList.add(dto);
        }

        return dtoList;
    }

    private List<FileDetailedListDTO> gainAttFileDetailedListDTO(String ManuscriptItemID, String uuid) throws Exception {
        List<FileDetailedListDTO> list = new ArrayList<>();
        List<ManuscriptItemFileDTO> list2 = repository().sqlQuery("select  fileSourceName as ManuscriptFile,left(fileSourceName,CHARINDEX(filetype,fileSourceName)-1) as ManuscriptDisplayName,\n" +
                "'' as StructureCode,'' as ParentStructureCode \n" +
                ",  ROW_NUMBER()  over (order by a.id) as SortNO ,IsGroup,'' as manuscriptType,'' as IndexNumber,\n" +
                "'ManuscriptItem//Original//' + substring(filePath,CHARINDEX(a.auditworkProjectID+'//',filePath) + len(a.auditworkProjectID+'//'),1000) as ManuscriptFileRelativePath,'' as ArchiveDisplay,0 as fiscalyear\n" +
                " from Prj_AuditFiles a,Prj_AuditFilesVSManuscript b where a.auditworkProjectID ='" + uuid + "' \n" +
                " and a.uuid = b.auditFilesId and b.manuscriptItemID='" + ManuscriptItemID + "'", ManuscriptItemFileDTO.class);
        // String path="";
        for (ManuscriptItemFileDTO item : list2) {
            FileDetailedListDTO dto = new FileDetailedListDTO();
            dto.setDisplayName(item.getManuscriptDisplayName());
            dto.setSortNo(item.getSortNO().toString());
            dto.setIndexNumber(item.getIndexNumber());
            dto.setManuscriptFile(item.getManuscriptFile());
            dto.setFileFullName(item.getManuscriptFileRelativePath());

            dto.setFileType("1");
            dto.setFileSourceName(item.getManuscriptFileRelativePath());
            dto.setIsFile("1");
            list.add(dto);
        }
        return list;
    }

    @SystemService(args = "uuid")
    public List<FileDetailedListDTO> gainFileDetailedList(String uuid) throws Exception {
        List<FileDetailedListDTO> dtoList = new ArrayList<>();

        Optional<CompositionCustomer> cust = CompositionCustomer.stream().where(w -> w.getUuid().equals(uuid)).findFirst();
        if (!cust.isPresent()) {
            throw new BusinessException("没有找到相关审计单位信息！");
        }
        String code = cust.get().getCode();

        List<ManuscriptItemFileDTO> list = gainManuscriptItemFileDTO(uuid);
        dtoList = gainFileDetailedListDTOList(list, "", code, uuid);

        return dtoList;

    }
}
