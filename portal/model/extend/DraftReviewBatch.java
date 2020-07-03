package com.dxn.model.extend;

import com.dxn.dto.DraftReviewBatchDTO;
import com.dxn.dto.DraftReviewBatchToDoDTO;
import com.dxn.dto.EnumValueDTO;
import com.dxn.dto.HomePageManuscriptReviewDTO;
import com.dxn.dto.enums.Constants;
import com.dxn.model.entity.DraftReviewBatchEntity;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.context.AppContext;
import com.dxn.system.dto.workFlow.MyTodoListDTO;
import com.dxn.system.dto.workFlow.WorkFlowNode;
import com.dxn.system.entityCode.EntityCodeConfigColumn;
import com.dxn.system.exception.BusinessException;
import com.dxn.system.exception.WorkFlowException;
import com.dxn.system.typeFinder.TypeFinderHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "QR_DraftReviewBatch")
public class DraftReviewBatch extends DraftReviewBatchEntity {


    @Override
    public void onUpdating() throws Exception {
        super.onUpdating();
    }

    @Override
    public boolean canDelete() {
        return this.getDocState() == 100 || this.getDocState() == 10;
    }

    @Override
    public String getUseWorkflowDefine(String json) throws WorkFlowException {
        Department org = this.getProjectId().getDeptId().getOrg();
        if (Framework.isNullOrEmpty(org))
            throw new WorkFlowException("所属分所不能为空");
        WorkFlowConfig workFlow = this.getProjectId().getHeadquartersReview() == 10 ? org.getGDraftProcessId() : org.getBDraftProcessId();
        if (Framework.isNullOrEmpty(workFlow))
            throw new WorkFlowException(String.format("[%s]未设置底稿复核流程！", org.getName()));
        return workFlow.getUuid();

    }


    private void modifyWorkflow(String docState) throws Exception {
        List<DraftReviewBatchItem> listItem = this.getDraftReviewBatchItems();
        this.setHasComments(false);
        Project p = repository().findById(this.getProjectId().getId(), Project.class);
        p.setReviewOpinionState(Constants.AlreadyApplied);
        if (Framework.isNotNullOrEmpty(p.getReviewOpinionState30())) {
            p.setReviewOpinionState30(Constants.Replied);
        }
        if (Framework.isNotNullOrEmpty(p.getReviewOpinionState40())) {
            p.setReviewOpinionState40(Constants.Replied);
        }
        if (Framework.isNotNullOrEmpty(p.getReviewOpinionState50())) {
            p.setReviewOpinionState50(Constants.Replied);
        }
        if (Framework.isNotNullOrEmpty(p.getReviewOpinionState60())) {
            p.setReviewOpinionState60(Constants.Replied);
        }
        if (docState.contains("30")) {
            p.setReviewOpinionState30(Constants.AlreadyApplied);
        }
        if (docState.contains("40")) {
            p.setReviewOpinionState40(Constants.AlreadyApplied);
        }

       /* if(docState.contains("50") || docState.contains("60"))
        {
            if(Framework.isNotNullOrEmpty(p.getDeptId().getOrg().getQualityControlGroup()))
            {
                Long groupid = p.getDeptId().getOrg().getQualityControlGroup().getId();
                List<UserGroupItem> items=UserGroupItem.stream().where(w->w.getUserGroupId().getId() == groupid).toList();
                if(items.size() == 1)
                {
                    ReviewAllocation review = new ReviewAllocation();
                    if (docState.contains("50")){
                        review.saveReviewAllocation(this.getId(),items.get(0).getUserId().getId().toString(),50);
                    }
                    if (docState.contains("60")){
                        review.saveReviewAllocation(this.getId(),items.get(0).getUserId().getId().toString(),60);
                    }
                }
            }
        }*/

        if (docState.contains("50")) {
            p.setReviewOpinionState50(Constants.AlreadyApplied);
        }
        if (docState.contains("60")) {
            p.setReviewOpinionState60(Constants.AlreadyApplied);
        }
        p.setWhetherShow(false);
        for (DraftReviewBatchItem item : listItem) {
            CompositionCustomer customer = repository().findById(item.getCompositionCustomerId(), CompositionCustomer.class);

            if (!docState.contains("/") && !customer.getDocStateAllE().contains("/"))//如果状态不包含/ 说明当前状态 和之前状态都不是并行
            {
                customer.setManuscriptReviewState(Integer.parseInt(docState));
            } else {
                if (docState.contains("/")) //当前状态是并行
                {
                    if (docState.startsWith("40")) //40 项目合伙人复核  即 三级复核
                    {
                        String temp = docState.substring(3);
                        customer.setManuscriptReviewState(Integer.parseInt(temp));
                    } else if (docState.endsWith("40")) {
                        String temp = docState.substring(0, 2);
                        customer.setManuscriptReviewState(Integer.parseInt(temp));
                    }

                } else if (customer.getDocStateAllE().contains("/") && docState == "40") {
                    customer.setManuscriptReviewState(Integer.parseInt(docState));
                }

            }

            customer.setDocStateAllE(this.getDocStateAll());
            if (Framework.isNullOrEmpty(customer.getParentId())) //主被审计单位提交了，更新主项目状态
            {
                if (Framework.isNotNullOrEmpty(p)) {
                    p.setManuscriptReviewState(docState);
                }
            }
        }
        if (Framework.isNotNullOrEmpty(p)) {
            p.setWhetherShow(false);
        }
    }

    public String getEnumName(String code, Integer index) throws Exception {
        EnumValueDTO enumDTO = EnumValue.createNew().getEnumDTO(code, index);
        if (Framework.isNotNullOrEmpty(enumDTO) && Framework.isNotNullOrEmpty(enumDTO.getName())) {
            return enumDTO.getName();
        }
        return "";
    }

    @Override
    public MyTodoListDTO gainWorkFlowDTO(MyTodoListDTO value) throws Exception {
        DraftReviewBatchToDoDTO dto = new DraftReviewBatchToDoDTO();
        ModelHelper.copyModel(value, dto);
        User user = this.getCreatedBy();
        if (Framework.isNotNullOrEmpty(user)) {
            if (Framework.isNotNullOrEmpty(user.getDept())) {
                value.setDepartment(user.getDept().getTreeName());
            }
        }
        String processId = value.getProcess_Id();
        Optional<WorkFlowConfig> workFlowConfiglist = WorkFlowConfig.stream().where(w -> w.getProcessId().equals(processId)).findFirst();
        if (workFlowConfiglist.isPresent()) {
            String deptIdName = user.getDept().getOrg().getNickname();
            WorkFlowConfig workFlowConfig = workFlowConfiglist.get();
            value.setWorkFlowName(deptIdName + "-" + workFlowConfig.getWorkFlowTypeId().getName());
        }
        dto.setCode(this.getProjectId().getCode());
        dto.setName(this.getProjectId().getName());
        dto.setCustomerType(this.getProjectId().getCustomerTypeValue());
        dto.setBusinessType(this.getProjectId().getBusinessTypeValue());
        dto.setRiskLevel(getEnumName("RiskLevel", this.getProjectId().getRiskLevel()));
        dto.setProjectPartnerId(this.getProjectId().getProjectPartnerId().getName());
        dto.setProjectManager(this.getProjectId().getProjectManager().getName());
        if (Framework.isNotNullOrEmpty(this.getDocState())) {
            dto.setReviewStatus(getEnumName("ManuscriptReviewStateType", this.getDocState()));
        }
        return dto;
    }

    private void modifyWorkflowNotice(String content) throws Exception {
        createNotice(content, "底稿复核审批", new ArrayList<>());
    }

    /**
     * 获取提交的下属单位个数，
     *
     * @return
     */
    public Integer gainDraftReviewBatchItemsnum() throws Exception {
        Integer i = 0;
        for (DraftReviewBatchItem item : this.getDraftReviewBatchItems()) {
            if (Framework.isNotNullOrEmpty(item.getCompositionCustomer().getParentId())) {
                i++;
            }
        }

        return i;
    }

    private void createNotice(String content, String title, List<Long> users) throws Exception {

        content += ":" + this.getProjectId().getShowName() + "-下属" + gainDraftReviewBatchItemsnum() + "个项目";
        Notice notice = Notice.createNew();
        if (users.size() == 0) {
            users.add(this.getCreatedById());
        }

        //发消息，传项目ID,用批表实体（批表实体 只是用来标记消息需打开的表单）
        notice.createNotice(AppContext.current().getProfile().getId(), users, title, content, this.getProjectId().getId(), "DraftReviewBatch", null);
    }

    @Override
    public void onWorkFlowBack(WorkFlowNode node) throws Exception {//退回时触发
        super.onWorkFlowBack(node);
        modifyWorkflow(this.getDocState().toString());
        //退回时 删除批数据
        //       repository().remove(this); 20200302 不删除数据了，保留退回记录
        //发消息
        modifyWorkflowNotice("底稿复核退回（" + AppContext.current().getProfile().getName() + "）");
    }

    @Override
    public void onSubmitWorkflow(WorkFlowNode node) throws Exception {//提交时 触发
        super.onSubmitWorkflow(node);
        modifyWorkflow(node.gainDocStateAll());
        List<Long> user = new ArrayList<>();
        user.add(this.getProjectId().getDeptId().getMasterId().getId());//部门经理接收

        createNotice("底稿复核提交（" + AppContext.current().getProfile().getName() + "）", "底稿复核提交", user);

    }

    //通过前校验
    @Override
    public void onApproveWorkflowing(WorkFlowNode node) throws Exception {
        judgeComments(node.getDocState());
    }

    //结束前校验
    @Override
    public void onEndWorkflowing(WorkFlowNode node) throws Exception {
        judgeComments(node.getDocState());
    }

    @Override
    public void onApproveWorkflow(WorkFlowNode node) throws Exception {//通过时触发
        super.onApproveWorkflow(node);

        modifyWorkflow(node.gainDocStateAll());
        modifyWorkflowNotice("底稿复核通过（" + AppContext.current().getProfile().getName() + "）");
    }

    @Override
    public void onEndWorkflow(WorkFlowNode node) throws Exception {//结束时触发
        judgeComments(node.getDocState());
        super.onEndWorkflow(node);
        modifyWorkflow(node.gainDocStateAll());
    }

    private void judgeComments(String docStateAll) throws Exception {
        // String docState = this.getOriginalValue().getDocState().toString();

        String sdoc = "";
        String docState = this.getOriginalValue().getDocStateAll();
        String[] oldlist = docState.split("/");


        if (oldlist.length == 1) {
            sdoc = oldlist[0];
        } else {
            for (String docs : oldlist) {
                if (!docStateAll.contains(docs)) {
                    sdoc = docs;
                    break;
                }

            }
        }

        //类型转换，转换失败后返回null
        Integer doc = TypeFinderHelper.parseInteger(sdoc);

        if (Framework.isNullOrEmpty(doc))
            throw new WorkFlowException("流程错误！");

        List<DraftReviewBatchItem> draftReviewBatchItems = this.getDraftReviewBatchItems();
        int count = 0;
        /*
        List<ReviewComments> coms=ReviewComments.stream().where(w->w.getProjectId().getId()==prjid
        && w.getManuscriptReviewState() == doc && w.getLevel() == 1).toList();
        for(ReviewComments item:coms)
        {
            if(Framework.isNullOrEmpty(item.getIsPass()))
            {
                Long comid = item.getId();
                Optional<ReviewComments> hascom =ReviewComments.stream().where(w->w.getProjectId().getId()==prjid
                        && w.getManuscriptReviewState() == doc && w.getLevel() == 2 && w.getParentId().getId() == comid).findFirst();
                if (!hascom.isPresent())
                {
                    throw new WorkFlowException("请先回复复核意见!");
                }
            }

        }
        if(coms.size() ==0)
        {
            throw new WorkFlowException("请先添加复核意见,并回复！");
        }

*/
        List<CompositionCustomer> clist = new ArrayList<>();
        for (DraftReviewBatchItem batchItem : draftReviewBatchItems) {
            Long compositionCustomerId = batchItem.getCompositionCustomer().getId();
            List<ReviewComments> reviewCommentsList = ReviewComments.stream().where(c -> c.getCompositionCustomerId().getId() == compositionCustomerId && c.getManuscriptReviewState().equals(doc)).toList();

            int value;
            if (reviewCommentsList.size() != 0) {
                value = 10;
                count++;

                for (ReviewComments item : reviewCommentsList) {
                    if (Framework.isNullOrEmpty(item.getIsSubmit())) {
                        if (item.getQuestionType() == 10) {
                            throw new WorkFlowException("存在未下发的复核意见!");
                        } else if (item.getQuestionType() == 20) {
                            throw new WorkFlowException("复核意见尚未提交回复!");
                        }

                    }
                    if (item.getQuestionType().equals(10)) {
                        if (item.getLevel() == 1) {
                            Long parentId = item.getId();
                            Optional<ReviewComments> comments = ReviewComments.stream().where(c -> c.getParentId().getId().equals(parentId)).findAny();
                            if (!comments.isPresent()) {
                                throw new WorkFlowException("请先回复复核意见!");
                            }
                        } else {
                            Long nextLevelParentId = item.getParentId().getId();
                            Optional<ReviewComments> nextLevelComments = ReviewComments.stream().where(c -> c.getParentId().getId().equals(nextLevelParentId) && !c.getQuestionType().equals(10)).findAny();
                            if (!nextLevelComments.isPresent()) {
                                throw new WorkFlowException("请先回复复核意见!");
                            }
                        }
                    }
                }
            } else {
                value = 20;
            }
            CompositionCustomer cust = batchItem.getCompositionCustomer();
            cust.initialization();
            if (doc == 30) {
                cust.setHasReviewOpinion30(value);
            } else if (doc == 40) {
                cust.setHasReviewOpinion40(value);
            } else if (doc == 50) {
                cust.setHasReviewOpinion50(value);
            } else if (doc == 60) {
                cust.setHasReviewOpinion60(value);
            }
            clist.add(cust);
            //
        }
        repository().commit();

        if (count == 0) {
            // throw new WorkFlowException("请先添加复核意见,并回复！");

        }


    }

    @JsonIgnore
    @SystemService(args = "projectId,compositionCustomerId")
    public DraftReviewBatchDTO saveDraftReviewBatch(Long projectId, List<Long> compositionCustomerId) throws Exception {

        if (compositionCustomerId.size() == 0) {
            throw new BusinessException("没有选择要提交的被审计单位！");
        }
        Long userId = AppContext.current().getProfile().getId();
        LocalDateTime dateTime = LocalDateTime.now().plusDays(-1);
        List<DraftReviewBatch> list = this.stream().where(w -> w.getSubmitOn() == null && w.getCreatedById() == userId).toList();
        for (DraftReviewBatch item : list) {
            if (item.getCreatedOn().isBefore(dateTime)) {
                item.initialization();
                repository().remove(item);
            }
        }

        String customer = Framework.toJson(compositionCustomerId);
        String md5 = Framework.getMD5(String.format("%s-%s-%s", userId, projectId, customer));
        Optional<DraftReviewBatch> first = this.stream().where(w -> w.getSubmitOn() == null && w.getMd5() == md5).findFirst();

        DraftReviewBatch batch;
        if (first.isPresent()) {
            batch = first.get();
        } else {
            batch = DraftReviewBatch.createNew();
            Project project = Project.findById(projectId);
            if (Framework.isNotNullOrEmpty(project) && Framework.isNotNullOrEmpty(project.getDeptId())) {
                batch.setDeptId(project.getDeptId());
            }
            batch.setProject(projectId);
            batch.setMd5(md5);
            repository().add(batch);
            for (Long item : compositionCustomerId) {
                DraftReviewBatchItem model = DraftReviewBatchItem.createNew();
                model.setCompositionCustomerId(item);
                model.setDraftReviewBatchId(batch);
                batch.getDraftReviewBatchItems().add(model);
            }
            repository().commit();
        }

        DraftReviewBatchDTO dto = new DraftReviewBatchDTO();
        dto.setId(batch.getId());
        dto.setEntityType(batch.gainTypeName());
        dto.setTimestamp(batch.getTimestamp());
        return dto;
    }

    @SystemService(args = "pid")
    public Long getProjectidFromWfid(Long pid) throws Exception {

        DraftReviewBatch d = repository().findById(pid, DraftReviewBatch.class);
        return d.getProjectId().getId();
    }

    //总所复核和分所复核 的复核分配用到
    @SystemService(args = "dId,formId")
    public Object gainDraftReviewBatch(Long dId, Long formId) throws Exception {
        DraftReviewBatch entity = DraftReviewBatch.findById(dId);
        // DraftReviewBatch entity=repository().findById(dId,DraftReviewBatch.class);
        entity.initialization();
        List<EntityCodeConfigColumn> configColumns = FormPage.getFormColumn(formId);
        return entity.toDictionary(configColumns);
    }

    private Boolean gainindependentReview() throws Exception {
        Long userId = AppContext.current().getProfile().getId();
        Long orgId = AppContext.current().getProfile().getOrgId();
        if (Framework.isNullOrEmpty(orgId)) return false;

        Department org = Department.findByIdReadOnly(orgId);
        if (Framework.isNullOrEmpty(org.getQualityControlGroupId())) return false;

        String userGroupCode = org.getQualityControlGroupId().getCode();
        Optional<UserGroupItem> exists = UserGroupItem.stream().where(w -> w.getUserId().getId() == userId && w.getUserGroupId().getCode() == userGroupCode).findFirst();
        return exists.isPresent();
    }

    @SystemService()
    public HomePageManuscriptReviewDTO gainMoreHomePageManuscriptReviewDTO() throws Exception {
        Long userid = AppContext.current().getProfile().getId();
        Boolean independentReview = gainindependentReview();
        HomePageManuscriptReviewDTO dto = new HomePageManuscriptReviewDTO();
        dto.setCode("more");
        dto.setName("复核中");
        dto.setReview(true);
        if (independentReview) //独立复核
        {
            dto.setSchemaCode("Approval5Independent");
            dto.setSchemaType("Panel");
        } else {
            dto.setSchemaCode("ManuscriptToReviewMyMore");

        }
        return dto;
    }

    @SystemService()
    public List<HomePageManuscriptReviewDTO> gainHomePageManuscriptReviewDTO() throws Exception {
        //回复待处理 待分配 待回复 复核中 已完成
        List<HomePageManuscriptReviewDTO> list = new ArrayList<>();

        Long userid = AppContext.current().getProfile().getId();
        Boolean independentReview = gainindependentReview();


        for (Integer i = 1; i <= 5; i++) {
            HomePageManuscriptReviewDTO dto = new HomePageManuscriptReviewDTO();
            List<Object> p = new ArrayList<>();
            p.add(userid);

            String sql;

            switch (i) {
                case 1: //回复待处理
                {
                    dto.setCode(i.toString());
                    dto.setName("回复待处理");
                    dto.setStateParm(30);
                    dto.setReview(true);


                    if (independentReview) //独立复核
                    {
                        dto.setSchemaCode("GeneralInstitutionReviewIndependent");
                        sql = "select p.* \n" +
                                "from wf_todo t\n" +
                                "inner join  qr_draftreviewbatch b  on t.entityid=b.id\n" +
                                "inner join prj_project p on b.projectid=p.id\n" +
                                "where t.workitem_state = 10 and t.flowcode is not  null  \n" +
                                "and exists (select code from Base_WorkFlowConfig where workflowtypeid =24615 and flowcode =code) \n" +
                                "and t.entityStatus in('50','60') \n" +
                                "and ( ReviewOpinionState50 =30 or ReviewOpinionState60 =30)\n" +
                                "and (exists \n" +
                                "(select *\n" +
                                "from dbo.Base_Department d,Base_Department df,base_usergroupitem item\n" +
                                "where p.deptid = d.id and d.orgId =df.id and item.usergroupid = df.qualityControlGroupId and item.userid =" + userid + ") or actor_id =" + userid + "  )\n" +
                                "and exists (select * from qr_reviewallocation a where  b.id=a.draftreviewbatchid and a.manuscriptReviewState = t.entityStatus \n" +
                                "and (a.userid=" + userid + " or actor_id = ? ))";
                    } else {
                        dto.setSchemaCode("ManuscriptToReviewMyhfdcl");
                        sql = "select p.*\n" +
                                "from wf_todo t\n" +
                                "inner join  qr_draftreviewbatch b  on t.entityid=b.id\n" +
                                "inner join prj_project p on b.projectid=p.id\n" +
                                "where t.ACTOR_ID = ? and t.workitem_state = 10 and t.flowcode is not  null  \n" +
                                "and exists (select code from Base_WorkFlowConfig where workflowtypeid =24615 and flowcode =code) \n" +
                                "and t.entityStatus in('30','40') \n" +
                                "and ((ReviewOpinionState30 =30  and t.entityStatus ='30' ) or (ReviewOpinionState40 =30  and t.entityStatus ='40') )";
                    }
                    int projects = excuteSql(sql, p);
                    dto.setNum(projects);
                    break;
                }
                case 2: //待分配
                {
                    dto.setCode(i.toString());
                    dto.setName("待分配");
                    dto.setReview(true);


                    if (independentReview) //独立复核
                    {
                        Long deptid = AppContext.current().getProfile().getDeptId();
                        Department dept = Department.findByIdReadOnly(deptid);

                        dto.setSchemaCode("DistributionOfManuscriptReviewIndependent");

                        List<Object> p2 = new ArrayList<>();
                        p2.add(userid);
                        p2.add(userid);

                        if (Framework.isNotNullOrEmpty(dept.getOrgId())) {
                            Department org = Department.findByIdReadOnly(dept.getOrgId());
                            Long groupid = org.getQualityControlGroup().getId();

                            List<UserGroupItem> items = UserGroupItem.stream().where(w -> w.getUserGroupId().getId() == groupid).toList();
                            if (items.size() == 1) {
                                List<DraftReviewBatch> dlist = repository().sqlQuery("select b.* \n" +
                                        "from wf_todo t\n" +
                                        "inner join  qr_draftreviewbatch b  on t.entityid=b.id\n" +
                                        "inner join prj_project p on b.projectid=p.id\n" +
                                        "\n" +
                                        "where  t.workitem_state = 10 and t.flowcode is not  null  \n" +
                                        "and exists (select code from Base_WorkFlowConfig where workflowtypeid =24615 and flowcode =code) \n" +
                                        "and t.entityStatus in('50','60')\n" +
                                        "and (exists \n" +
                                        "(select *\n" +
                                        "from dbo.Base_Department d,Base_Department df,base_usergroupitem item\n" +
                                        "where p.deptid = d.id and d.orgId =df.id and item.usergroupid = df.qualityControlGroupId and item.userid = ? ) or actor_id = ? )\n" +
                                        "and not exists (select * from qr_reviewallocation a where  b.id=a.draftreviewbatchid and a.manuscriptReviewState = t.entityStatus)", p2, DraftReviewBatch.class);

                                for (DraftReviewBatch dr : dlist) {
                                    String users = "[{\"Key\":" + items.get(0).getUserId().getId().toString() + ",\"Value\":\"Add\"}]";
                                    ReviewAllocation review = new ReviewAllocation();
                                    if (dr.getDocStateAll().contains("50")) {
                                        review.saveReviewAllocation(dr.getId(), users, 50);
                                    }
                                    if (dr.getDocStateAll().contains("60")) {
                                        review.saveReviewAllocation(dr.getId(), users, 60);
                                    }
                                }

                                repository().commit();
                            }
                        }

                        int projects = repository().sqlQuery("select p.* \n" +
                                "from wf_todo t\n" +
                                "inner join  qr_draftreviewbatch b  on t.entityid=b.id\n" +
                                "inner join prj_project p on b.projectid=p.id\n" +
                                "\n" +
                                "where  t.workitem_state = 10 and t.flowcode is not  null  \n" +
                                "and exists (select code from Base_WorkFlowConfig where workflowtypeid =24615 and flowcode =code) \n" +
                                "and t.entityStatus in('50','60')\n" +
                                "and (exists \n" +
                                "(select *\n" +
                                "from dbo.Base_Department d,Base_Department df,base_usergroupitem item\n" +
                                "where p.deptid = d.id and d.orgId =df.id and item.usergroupid = df.qualityControlGroupId and item.userid = ? ) or actor_id = ? )\n" +
                                "and not exists (select * from qr_reviewallocation a where  b.id=a.draftreviewbatchid and a.manuscriptReviewState = t.entityStatus)", p2, Project.class).size();

                        dto.setNum(projects);

                    } else {
                        //       dto.setSchemaCode("ManuscriptToReviewMy");
                        dto.setNum(0);
                    }
                    break;
                }
                case 3: //待回复
                {
                    dto.setCode(i.toString());
                    dto.setName("待回复");
                    dto.setStateParm(20);
                    dto.setReview(true);
                    if (independentReview) //独立复核
                    {
                        dto.setSchemaCode("GeneralInstitutionReviewIndependentdhf");
                        sql = "select p.* \n" +
                                "from wf_todo t\n" +
                                "inner join  qr_draftreviewbatch b  on t.entityid=b.id\n" +
                                "inner join prj_project p on b.projectid=p.id\n" +
                                "where t.ACTOR_ID = ?  and t.workitem_state = 10 and t.flowcode is not  null  \n" +
                                "and exists (select code from Base_WorkFlowConfig where workflowtypeid =24615 and flowcode =code) \n" +
                                "and t.entityStatus in('50','60') \n" +
                                "and ( \n" +
                                " (ReviewOpinionState50 = 20 and  t.entityStatus ='50')  or (ReviewOpinionState60 = 20 and t.entityStatus ='60') )";


                    } else {
                        dto.setSchemaCode("ManuscriptToReviewMy");
                        sql = "select p.* \n" +
                                "from wf_todo t\n" +
                                "inner join  qr_draftreviewbatch b  on t.entityid=b.id\n" +
                                "inner join prj_project p on b.projectid=p.id\n" +
                                "where t.ACTOR_ID = ?  and t.workitem_state = 10 and t.flowcode is not  null  \n" +
                                "and exists (select code from Base_WorkFlowConfig where workflowtypeid =24615 and flowcode =code) \n" +
                                "and t.entityStatus in('30','40') \n" +
                                "and ((ReviewOpinionState30 =20 and t.entityStatus ='30') or (ReviewOpinionState40 = 20 and t.entityStatus ='40') \n" +
                                " )";


                    }


                    int projects = excuteSql(sql, p);
                    dto.setNum(projects);
                    break;
                }
                case 4: //复核中
                {
                    dto.setCode(i.toString());
                    dto.setName("复核中");
                    dto.setStateParm(10);
                    dto.setReview(true);

                    if (independentReview) //独立复核
                    {
                        dto.setSchemaCode("GeneralInstitutionReviewIndependent");
                        //  dto.setSchemaType("Panel");
                        sql = "select p.* \n" +
                                "from wf_todo t\n" +
                                "inner join  qr_draftreviewbatch b  on t.entityid=b.id\n" +
                                "inner join prj_project p on b.projectid=p.id\n" +
                                "where t.workitem_state = 10 and t.flowcode is not  null  \n" +
                                "and exists (select code from Base_WorkFlowConfig where workflowtypeid =24615 and flowcode =code) \n" +
                                "and t.entityStatus in('50','60') \n" +
                                "and ( ((ReviewOpinionState50 =10 or ReviewOpinionState50 is null) and t.entityStatus ='50' )  \n" +
                                "or ((ReviewOpinionState60 =10  or ReviewOpinionState60  is null ) and t.entityStatus ='60')) \n" +
                                "and (exists \n" +
                                "(select *\n" +
                                "from dbo.Base_Department d,Base_Department df,base_usergroupitem item\n" +
                                "where p.deptid = d.id and d.orgId =df.id and item.usergroupid = df.qualityControlGroupId and item.userid =" + userid + ") or actor_id =" + userid + "  )\n" +
                                "and exists (select * from qr_reviewallocation a where  b.id=a.draftreviewbatchid and a.manuscriptReviewState = t.entityStatus \n" +
                                "and (a.userid=" + userid + " or actor_id = ? ))";
                    } else {
                        dto.setSchemaCode("ManuscriptToReviewMyhfdcl");
                        sql = "select p.* \n" +
                                "from wf_todo t\n" +
                                "inner join  qr_draftreviewbatch b  on t.entityid=b.id\n" +
                                "inner join prj_project p on b.projectid=p.id\n" +
                                "where t.ACTOR_ID = ?  and t.workitem_state = 10 and t.flowcode is not  null  \n" +
                                "and exists (select code from Base_WorkFlowConfig where workflowtypeid =24615 and flowcode =code) \n" +
                                "and t.entityStatus in('30','40') \n" +
                                "and ((ReviewOpinionState30 =10  and t.entityStatus ='30' ) or (ReviewOpinionState40 =10  and t.entityStatus ='40') )";
                    }
                    int projects = excuteSql(sql, p);
                    dto.setNum(projects);
                    break;
                }
                case 5: //已完成
                {

                    dto.setCode(i.toString());
                    dto.setName("已完成");
                    dto.setSchemaCode("ProjectListQualityMore");//ProjectListQualityMore
                    sql = "select p.*\n" +
                            "from wf_todolog t\n" +
                            "inner join  qr_draftreviewbatch b  on t.entityid=b.id\n" +
                            "inner join prj_project p on b.projectid=p.id\n" +
                            "\n" +
                            "where  t.workitem_state in (60,100) and t.flowcode is not  null  \n" +
                            "and exists (select code from Base_WorkFlowConfig where workflowtypeid =24615 and flowcode =code) \n" +
                            "and t.entityStatus in ('60','50','30','40')\n" +
                            "\n" +
                            " and (t.ACTOR_ID = ? \n" +
                            //  " or t.entityid in (select draftReviewBatchId from QR_ReviewAllocation where userid =t.ACTOR_ID)\n" +
                            " )";
                    int projects = excuteSql(sql, p);
                    dto.setNum(projects);
                    break;
                }
            }

            list.add(dto);
        }

        return list;
    }

    private int excuteSql(String sql, List<Object> p) throws Exception {

        int projects = repository().sqlQuery(sql, p, Project.class).size();
        return projects;
    }


    @SystemService(args = "projectId")
    public String gainManuscriptReviewState(Long projectId) throws Exception {
        String strReturn = "";

        List<CompositionCustomer> custs = CompositionCustomer.stream().where(w -> w.getProjectId().getId() == projectId).toList();

        for (CompositionCustomer cust : custs) {
            String item;
            if (Framework.isNullOrEmpty(cust.getDocStateAllE())) {
                item = "";
            } else {
                item = cust.getDocStateAllE();
            }
            item = item + "/";
            if (!strReturn.contains(item)) {
                strReturn += item;
            }
            if (Framework.isNotNullOrEmpty(cust.getHasReviewOpinion30()) && !strReturn.contains("30/")) {
                strReturn += "30/";
            }
            if (Framework.isNotNullOrEmpty(cust.getHasReviewOpinion40()) && !strReturn.contains("40/")) {
                strReturn += "40/";
            }
            if (Framework.isNotNullOrEmpty(cust.getHasReviewOpinion50()) && !strReturn.contains("50/")) {
                strReturn += "50/";
            }
            if (Framework.isNotNullOrEmpty(cust.getHasReviewOpinion60()) && !strReturn.contains("60/")) {
                strReturn += "60/";
            }

        }


        List<String> list = CompositionCustomer.stream().where(w -> w.getProjectId().getId() == projectId).select(s -> s.getDocStateAllE()).distinct().toList();
        for (String item : list) {
            if (Framework.isNullOrEmpty(item)) item = "";
            item = item + "/";
            if (!strReturn.contains(item)) {
                strReturn += item;
            }
        }

        List<Integer> listcoms = ReviewComments.stream().where(w -> w.getProjectId().getId() == projectId).select(s -> s.getManuscriptReviewState()).distinct().toList();
        for (Integer item : listcoms) {
            String tmp = item.toString();
            if (Framework.isNullOrEmpty(tmp))
                break;
            tmp += "/";
            if (!strReturn.contains(tmp)) {
                strReturn += tmp;
            }
        }

        return strReturn;

    }

    /**
     * 判断当前登录人是不是复核经理
     *
     * @return
     * @throws Exception
     */
    @SystemService()
    public Boolean judgeReviewManager() throws Exception {
        Long deptid = AppContext.current().getProfile().getOrgId();
        Department dept = repository().findById(deptid, Department.class);
        if (Framework.isNotNullOrEmpty(dept.getReviewManagerId())) {
            return AppContext.current().getProfile().getId().equals(dept.getReviewManagerId().getId());
        } else if (Framework.isNotNullOrEmpty(dept.getHeadquartersManagerId())) {
            return AppContext.current().getProfile().getId().equals(dept.getHeadquartersManagerId().getId());
        }
        return false;
    }

}






