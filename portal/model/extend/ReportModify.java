package com.dxn.model.extend;

import com.dxn.model.entity.ReportModifyEntity;
import com.dxn.system.Framework;
import com.dxn.system.WorkFlowHelper;
import com.dxn.system.context.AppContext;
import com.dxn.system.context.Profile;
import com.dxn.system.dto.workFlow.UpdateTodoLog;
import com.dxn.system.exception.BusinessException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "PM_ReportModify")
public class ReportModify extends ReportModifyEntity {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
    }

    @Override
    public void onValidate() throws Exception {
        super.onValidate();
        if (Framework.isNotNullOrEmpty(this.getReportId())) {
            if (Framework.isNotNullOrEmpty(this.getReportId().getStatus())) {
                throw new BusinessException("正在进行用印申请流程，不能进行修改操作！");
            }
        }
    }

    @Override
    public void onInserting() throws Exception {
        super.onInserting();
    }

    @Override
    public void onInserted() throws Exception {
        super.onInserted();
        Report report = this.getReportId();

        if (Framework.isNotNullOrEmpty(report)) {
            Integer modifyNumber = report.getModificatioTimes() + 1;
            //审批流中增加一条记录
            UpdateTodoLog todoLog = new UpdateTodoLog();
            Profile profile = AppContext.current().getProfile();
            todoLog.setActorId(profile.getId().toString());
            todoLog.setActorName(profile.getName());
            todoLog.setEntityId(report.getId().toString());
            todoLog.setEntityType("Report");
            todoLog.setXmid("报告申请修改");
            todoLog.setTaskInstanceName("项目经理");
            todoLog.setProcessId("报告申请修改流程");
            todoLog.setJdtype(String.format("{entityId:%s,displayName:'第 %s 次修改'}", this.getId(), modifyNumber));
            WorkFlowHelper.saveTodoLog(todoLog);

            for (ReportFile item : report.getReportFiles()) {
                if (Framework.isNotNullOrEmpty(item.getAttachmentId())) {
                    item.setIsSigned(null);
                    item.setSignedById(null);
                    item.setSignedOn(null);
                    item.setReportModifyId(this.getId());
                    item.setSaveHistory(true);
                }
            }

            report.setModificatioTimes(modifyNumber);
            report.setDocState(10);
            //修改成功后状态回滚
            report.setApplySignStatus(10);
            report.setSignCompletion(20);
            report.setIsSign(false);
            Long RegistFormId = report.getRegistFormId();
            Optional<RegistForm> registForm = RegistForm.stream().where(w -> w.getId() == RegistFormId).findFirst();
            if (registForm.isPresent())
                this.repository().remove(registForm.get());
            report.setRegistFormId(null);
            report.setReportingStatus(5);
            report.setIsRegistForm(20);
            this.repository().commit();
        }
    }
}
