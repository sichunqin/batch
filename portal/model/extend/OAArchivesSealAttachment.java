package com.dxn.model.extend;

import com.dxn.model.entity.OAArchivesSealAttachmentEntity;
import com.dxn.system.Framework;
import com.dxn.system.context.AppContext;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "Prj_OAArchivesSealAttachment")
public class OAArchivesSealAttachment extends OAArchivesSealAttachmentEntity {

    @Override
    public void onSignatureFile(Boolean isSign) throws Exception {
        if (!isSign) return;
        this.setSignedBy(AppContext.current().getProfile().getId());
        this.setSignedOn(LocalDateTime.now());
        this.setIsSigned(isSign);
        repository().commit();
    }

    @Override
    public void onPrintFile(Integer printNum) throws Exception {

        Integer filePrintNum = printNum;
        if (Framework.isNotNullOrEmpty(this.getPrintedNumber()))
            filePrintNum = filePrintNum + this.getPrintedNumber();
        this.setPrintedNumber(filePrintNum);

        if (Framework.isNotNullOrEmpty(this.getOAArchivesSealId())) {
            filePrintNum = printNum;
            if (Framework.isNotNullOrEmpty(this.getOAArchivesSealId().getPrintedNumber()))
                filePrintNum = filePrintNum + this.getOAArchivesSealId().getPrintedNumber();

            this.getOAArchivesSealId().setPrintedNumber(filePrintNum);
        }

        repository().commit();
    }
}
