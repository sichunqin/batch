package com.dxn.model.extend;

import com.dxn.model.entity.RegistFormFileEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "Rpt_RegistFormFile")
public class RegistFormFile extends RegistFormFileEntity {

    @Override
    public String gainAttachmentPath() throws Exception {
        String path = super.gainAttachmentPath();
        String projectCode = this.getRegistFormId().getReport().getProjectId().getCode();
        return String.format("%s/%s/报备", path, projectCode);
    }
}
