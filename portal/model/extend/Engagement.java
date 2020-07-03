package com.dxn.model.extend;

import com.dxn.forms.dto.UIPagedList;
import com.dxn.model.entity.EngagementEntity;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.context.AppContext;
import com.dxn.system.dto.AttachmentDTO;
import com.dxn.system.dto.workFlow.MyTodoListDTO;
import com.dxn.system.dto.workFlow.WorkFlowNode;
import com.dxn.system.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "PM_Engagement")
public class Engagement extends EngagementEntity {

    @Override
    public void onWorkFlowBack(WorkFlowNode node) throws Exception {//退回时触发
        super.onWorkFlowBack(node);
        modifyWorkflow("项目约定书审批拒绝（" + AppContext.current().getProfile().getName() + "）");
    }

    @Override
    public void onEndWorkflow(WorkFlowNode node) throws Exception {//结束时触发
        super.onEndWorkflow(node);
        SetReportHaveBusinessAgreement();//维护报告约定书状态
        modifyWorkflow("项目约定书审批通过");
    }

    private void modifyWorkflow(String content) throws Exception {
        createNotice(content, "业务约定书审批");
    }

    private void createNotice(String content, String title) throws Exception {

        content += ":" + this.getProjectId().getShowName();
        Notice notice = Notice.createNew();
        List<Long> users = new ArrayList<>();
        users.add(this.getCreatedById());
        notice.createNotice(AppContext.current().getProfile().getId(), users, title, content, this.getId(), "Engagement", null);
    }

    @SystemService(args = "query_projectId,formId")
    public UIPagedList getEngagementByprjId(Long projectId, Long formId) throws Exception {
        // List<Engagement> eggs = this.stream().where(w -> w.getCompositionCustomerId().getProjectId().getId() == projectId).toList();
        List<Engagement> eggs = this.stream().where(w -> w.getProjectId().getId() == projectId).toList();
        List<Object> list = new ArrayList<>();
        eggs.forEach(f -> {
            f.initialization();
            list.add(f);
        });
        return UIPagedList.band(list, FormPage.getFormColumn(formId));
    }

    @SystemService(args = "engId,prjectId")
    public void saveReferency(Long engId, Long prjectId) throws Exception {
        Optional<Engagement> eggs = this.stream().where(w -> w.getId() == engId).findFirst();
        Optional<CompositionCustomer> c = CompositionCustomer.stream().where(w -> w.getId() == prjectId).findFirst();
        Long prjId = 0L;
        if (c.isPresent()) {
            prjId = c.get().getProjectId().getId();
        }

        if (eggs.isPresent()) {
            Engagement source = eggs.get();
            Engagement model = Engagement.createNew();
            model.setCompositionCustomer(prjectId);
            model.setAddOrReference(20);
            model.setAmount(source.getAmount());
            model.setCode(source.getCode());
            model.setDateOfSign(source.getDateOfSign());
            model.setDescription(source.getDescription());
            model.setEngagement(source.getId());
            model.setPrintUnit(source.getPrintUnit());
            model.setDocState(source.getDocState());
            model.setProject(prjId);
            model.setClassification(source.getClassification());

            repository().add(model);
            repository().commit();

            Attachment att = Attachment.createNew();
            List<AttachmentDTO> attdtos = att.gainAttachments(source.getId(), this.gainTypeName());

            for (AttachmentDTO dto : attdtos) {
                Attachment item = Attachment.createNew();
                ModelHelper.copyModel(dto, item);
                item.setEntityName(this.gainTypeName());
                item.setId(0L);
                item.setEntityId(model.getId());
                repository().add(item);
            }
        }
    }


    @Override
    public boolean canDelete() {
        return this.getDocState() == 100 || this.getDocState() == 10 || this.getAddOrReference() == 20;
    }

    private void updateReferency() throws Exception {
        Engagement oldegg = this.getOriginalValue();
        if (Framework.isNullOrEmpty(oldegg)) {
            return;
        }
        Long eid = this.getId();
        List<Engagement> englist = this.stream().where(w -> w.getAddOrReference() == 20 && w.getEngagementId().getId() == eid).toList();
        if (englist.size() > 0) {
            for (Engagement item : englist) {
                item.setClassification(this.getClassification());
                item.setDocState(this.getDocState());
                item.setPrintUnit(this.getPrintUnit());
                item.setDescription(this.getDescription());
                item.setDateOfSign(this.getDateOfSign());
                item.setAmount(this.getAmount());

            }
        }

    }

    @Override
    public void onInserting() throws Exception {
        super.onInserting();
        this.SetCompositionCustomer(true);
    }

    @Override
    public void onInserted() throws Exception {
        super.onInserted();
        this.setDept(this.getProjectId().getDeptId().getId());
        //业务代码
    }


    @Override
    public void onUpdated() throws Exception {
        super.onUpdated();
        SetRegistFormAmount();//维护业务报备上的金额
        updateReferency();//修改添加类型的约定书时，同时修改引用里的数据
        this.SetCompositionCustomer(true);
    }

    @Override
    public void onDeleting() throws Exception {
        super.onDeleting();
        Long CurrentLogin = AppContext.current().getProfile().getId();
        if (!this.getCreatedById().equals(CurrentLogin)) {
            throw new BusinessException("约定书是由" + this.getCreatedBy().getName() + "创建，您不能删除！");
        }
        this.SetCompositionCustomer(false);
    }

    public void SetCompositionCustomer(Boolean iSTrue) throws Exception {
        String EngagementCode = "";
        String EngagementClassification = "";
        String EngagementPrintUnit = "";
        Double Amount = 0.0;
        Long CompositionCustomerId = this.getCompositionCustomerId().getId();
        Long id;
        if (Framework.isNullOrEmpty(this.getId())) {
            id = 0L;
        } else {
            id = this.getId();
        }
        Optional<CompositionCustomer> compositionCustomer = CompositionCustomer.stream().where(w -> w.getId() == CompositionCustomerId).findFirst();
        if (compositionCustomer.isPresent()) {
            List<Engagement> engagementList = Engagement.stream().where(w -> w.getCompositionCustomerId().getId() == CompositionCustomerId && w.getId() != id).toList();

            String spl = "";
            if (engagementList.size() > 0) {

                for (int i = 0; i < engagementList.size(); i++) {

                    Engagement item = engagementList.get(i);
                    if (item.getAddOrReference() == 10)//新增
                    {
                        Amount = Amount + item.getAmount();
                    }
                    if (i > 0) {
                        spl = ",";
                    }
                    EngagementCode += spl + item.getCode();//约定书编号
                    Integer classification = item.getClassification();//约定书分类
                    Optional<EnumValue> enumValueByClassification = EnumValue.stream().where(w -> w.getValue() == classification && w.getEnumTypeId().getCode() == "ClassificationType").findFirst();
                    if (enumValueByClassification.isPresent())
                        EngagementClassification += spl + enumValueByClassification.get().getName();
                    Integer PrintUnit = item.getPrintUnit();
                    Optional<EnumValue> EnumValueByPrintUnit = EnumValue.stream().where(w -> w.getValue() == PrintUnit && w.getEnumTypeId().getCode() == "PrintUnitType").findFirst();
                    if (EnumValueByPrintUnit.isPresent())
                        EngagementPrintUnit += spl + EnumValueByPrintUnit.get().getName();
                }
            }

            if (iSTrue) {
                if (Framework.isNotNullOrEmpty(EngagementCode) && Framework.isNullOrEmpty(spl)) {
                    spl = ",";
                }
                EngagementCode += spl + this.getCode();
                Integer classification = this.getClassification();

                Optional<EnumValue> enumValueByClassification = EnumValue.stream().where(w -> w.getValue() == classification && w.getEnumTypeId().getCode() == "ClassificationType").findFirst();
                if (enumValueByClassification.isPresent())
                    EngagementClassification += spl + enumValueByClassification.get().getName();

                Integer PrintUnit = this.getPrintUnit();
                Optional<EnumValue> EnumValueByPrintUnit = EnumValue.stream().where(w -> w.getValue() == PrintUnit && w.getEnumTypeId().getCode() == "PrintUnitType").findFirst();
                if (EnumValueByPrintUnit.isPresent())
                    EngagementPrintUnit += spl + EnumValueByPrintUnit.get().getName();
                if (this.getAddOrReference() == 10) {
                    Amount += this.getAmount();
                }
            }
            compositionCustomer.get().setEngagementCode(EngagementCode);
            compositionCustomer.get().setEngagementClassification(EngagementClassification);
            compositionCustomer.get().setEngagementPrintUnit(EngagementPrintUnit);
            if (Amount != 0.0) {
                compositionCustomer.get().setAmount(Amount);
            }

        }
    }


    public void SetRegistFormAmount() throws Exception {
        long CompositionCustomerId = this.getCompositionCustomerId().getId();

        Optional<RegistForm> formFirst = RegistForm.stream().where(w -> w.getReportId().getCompositionCustomerId().getId() == CompositionCustomerId && w.getAmount() > 0.00).findFirst();
        List<Engagement> engagementList = Engagement.stream().where(w -> w.getCompositionCustomerId().getId() == CompositionCustomerId && w.getDocState() == 30 && w.getAddOrReference() == 10).toList();
        if (formFirst.isPresent()) {
            Double totalAmount = 0.00;
            for (Engagement item : engagementList) {
                if (Framework.isNotNullOrEmpty(item.getAmount()))
                    totalAmount += item.getAmount();
            }

            formFirst.get().setAmount(totalAmount);
        }

    }


    public void SetReportHaveBusinessAgreement() throws Exception {
        long CompositionCustomerId = this.getCompositionCustomerId().getId();
        List<Report> reportList = Report.stream().where(r -> r.getCompositionCustomerId().getId() == CompositionCustomerId && r.getHaveBusinessAgreement() != 30).toList();
        for (Report item : reportList) {
            item.setHaveBusinessAgreement(this.getDocState());
        }

    }

    @SystemService(args = "customId")
    public int getAddReference(Long customId) throws Exception {
        CompositionCustomer cust = repository().findById(customId, CompositionCustomer.class);
        if (Framework.isNullOrEmpty(cust.getParentId())) //主项目
        {
            return 0;//新增，引用均可
        } else {
            return 10;//子项目只能  新增约定书
        }


//
//        List<Engagement> eggs = this.stream().where(w -> w.getCompositionCustomerId().getId() == customId).toList();
//        if (eggs.size() == 0) {
//            return 0;//没有类型
//        } else {
//            return eggs.get(0).getAddOrReference(); //一个审计单位只能存在一种类型的业务约定书
//        }
    }

    //重置状态用到
    @SystemService(args = "engId,value")
    public void resetDocState(Long engId, Integer value) throws Exception {
        Engagement e = Engagement.findById(engId);
        e.setDocState(value);
        repository().commit();

    }

    @JsonIgnore
    public MyTodoListDTO gainWorkFlowDTO(MyTodoListDTO value) throws Exception {
        value.setDepartmentType(this.getProjectId().getCode());
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


    @Override
    public String gainAttachmentPath() throws Exception {
        String path = super.gainAttachmentPath();
        String projectCode = this.getProjectId().getCode();
        return String.format("%s/%s/约定书", path, projectCode);
    }
}
