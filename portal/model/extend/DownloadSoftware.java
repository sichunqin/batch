package com.dxn.model.extend;

import com.dxn.model.entity.DownloadSoftwareEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Base_DownloadSoftware")
public class DownloadSoftware extends DownloadSoftwareEntity {

    @Override
    public String gainAttachmentPath() throws Exception {
        return "RelatedPlugin";
    }

}
