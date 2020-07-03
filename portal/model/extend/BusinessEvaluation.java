package com.dxn.model.extend;

import com.dxn.model.entity.BusinessEvaluationEntity;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.entityCode.EntityCodeConfigColumn;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "PM_BusinessEvaluation")

public class BusinessEvaluation extends BusinessEvaluationEntity {

    @Override
    public void onInserted() throws Exception {
        super.onInserted();
        if (this.getSubmissionDate() != null && this.getSubmissionDate().getYear() == 2000)
            this.setSubmissionDate(null);
    }

    @Override
    public void onUpdated() throws Exception {
        super.onUpdated();
        if (this.getSubmissionDate() != null && this.getSubmissionDate().getYear() == 2000)
            this.setSubmissionDate(null);
    }

    //项目立项 业务承接表用到
    @SystemService(args = "projectId,formId")
    public Object getIdByProjectId(Long projectId, Long formId) throws Exception {
        Optional<BusinessEvaluation> projects = this.stream().where(w -> w.getProjectId().getId() == projectId).findFirst();
        if (projects.isPresent()) {
            BusinessEvaluation entity = projects.get();
            entity.initialization();
            List<EntityCodeConfigColumn> configColumns = FormPage.getFormColumn(formId);
            return entity.toDictionary(configColumns);
        }
        return this.createNew();
    }

    //给CS端提供URL访问接口时用到
    @SystemService(args = "projectId,formId")
    public Object getIdByProjectIdCs(String projectId, Long formId) throws Exception {
        Optional<BusinessEvaluation> projects = this.stream().where(w -> w.getProjectId().getUuid() == projectId).findFirst();
        if (projects.isPresent()) {
            BusinessEvaluation entity = projects.get();
            entity.initialization();
            return entity.toDictionary(FormPage.getFormColumn(formId));
        }
        return this.createNew();
    }
}
