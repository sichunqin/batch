package com.dxn.model.extend;

import com.dxn.model.entity.BorrowMasterEntity;
import com.dxn.system.Framework;
import com.dxn.system.dto.workFlow.MyTodoListDTO;
import com.dxn.system.dto.workFlow.WorkFlowNode;
import com.dxn.system.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "prj_BorrowMaster")
public class BorrowMaster extends BorrowMasterEntity {

    @Override
    public void onValidate() throws Exception {
        super.onValidate();
        if (Framework.isNotNullOrEmpty(this.getOriginalValue())) return;
        if (this.getBorrowDays() <= 0) {
            throw new BusinessException("借阅天数不能为0或负数！");
        }
        Boolean has = false;
        List<BorrowChild> childs = this.getBorrowChilds();
        for (BorrowChild child : childs) {
            if (Framework.isNotNullOrEmpty(child.getElectronicDraft()) && child.getElectronicDraft() || (Framework.isNotNullOrEmpty(child.getPaperDraft()) && child.getPaperDraft())) {
                has = true;
                break;
            }
        }
        if (!has) {
            throw new BusinessException("请在要借阅的项目后，勾选电子底稿或纸质底稿！");
        }
    }


    @Override
    public void onSubmitWorkflow(WorkFlowNode node) throws Exception {//提交时 触发
        super.onSubmitWorkflow(node);
        modifyWorkflow();
    }

    @Override
    public void onWorkFlowBack(WorkFlowNode node) throws Exception {//退回时触发
        super.onWorkFlowBack(node);
        modifyWorkflow();
    }

    @Override
    public void onApproveWorkflow(WorkFlowNode node) throws Exception {//批注触发
        super.onApproveWorkflow(node);
        modifyWorkflow();
    }

    @Override
    public void onEndWorkflow(WorkFlowNode node) throws Exception {//结束时触发
        super.onEndWorkflow(node);
        modifyWorkflow();
    }

    private void modifyWorkflow() throws Exception {
        for (BorrowChild child : this.getBorrowChilds()) {
            child.setDocState(this.getDocState());
        }
    }

    @Override
    public void onInserting() throws Exception {
        super.onInserting();
        myUpdate();

    }

    @Override
    public void onInserted() throws Exception {
        super.onInserted();
        Long prj = this.getProjectId().getId();
        Long count = this.stream().where(w -> w.getProjectId().getId() == prj).count();
        Project p = Project.findById(prj);
        p.setNumberOfBorrowings(count.intValue());
        this.setDeptId(p.getDeptId());

    }


    @Override
    public void onUpdating() throws Exception {
        super.onUpdating();
        myUpdate();
    }

    /**
     * 插入和更新时调用，更新预计归还日期
     *
     * @throws Exception
     */
    private void myUpdate() throws Exception {
        BorrowMaster oldmaster = this.getOriginalValue();
        if (Framework.isNullOrEmpty(this.getIsExternal()) || !this.getIsExternal()) {
            this.setIsExternal(false);
            this.setInternalAndExternalUse(20);
        } else {
            this.setInternalAndExternalUse(10);
        }


        if (Framework.isNullOrEmpty(oldmaster) || oldmaster.getBorrowDays() != this.getBorrowDays())//新增 或者修改借阅天数
        {
            GregorianCalendar gs = new GregorianCalendar();
            gs.setTime(new Date());
            gs.add(5, this.getBorrowDays());
            this.setExpectedReturnDate(gs.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            if (Framework.isNotNullOrEmpty(oldmaster)) //更新子表
            {
                for (BorrowChild child : this.getBorrowChilds()) {
                    child.setBorrowDays(this.getBorrowDays());
                    child.setExpectedReturnDate(this.getExpectedReturnDate());
                    repository().add(child);
                }
            }
        }
    }

    @JsonIgnore
    public MyTodoListDTO gainWorkFlowDTO(MyTodoListDTO value) throws Exception {
        User user = this.getCreatedBy();
        String deptIdName = user.getDeptReadOnly().getOrgReadOnly().getNickname();
        value.setDepartmentType(this.getProjectId().getCode());
        value.setWorkFlowName(deptIdName + "-" + value.getToDoDescription());
        value.setDepartment(user.getDeptReadOnly().getTreeName());
        return value;
    }
}
