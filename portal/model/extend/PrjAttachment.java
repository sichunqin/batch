package com.dxn.model.extend;

import com.dxn.forms.dto.UIPagedList;
import com.dxn.model.entity.PrjAttachmentEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.exception.BusinessException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Entity
@Table(name = "PM_PrjAttachment")
public class PrjAttachment extends PrjAttachmentEntity {
    @SystemService(args = "filesId,PrjId,flag,engagId")
    public void savePrjAttachment(List<Long> filesId, Long PrjId, int flag,Long engagId) throws Exception {


        for (Long lId : filesId) {
            Attachment att= repository().findById(lId,Attachment.class);
            String attname = att.getName();
            Optional<PrjAttachment>  opt = this.stream().where(w->w.getAttachmentId().getName().equals(attname) && w.getFlag()==flag && w.getProjectId().getId() == PrjId ).findFirst();

            if(opt.isPresent())
            {
                throw new BusinessException(attname+" 已经上传了，不能再次上传！");
            }

            PrjAttachment model = PrjAttachment.createNew();
            model.setAttachment(lId);
            model.setFlag(flag);
            if(Framework.isNotNullOrEmpty(PrjId))
            {
                model.setProject(PrjId);
            }
            if(Framework.isNotNullOrEmpty(engagId))
            {
                model.setEngagement(engagId);
            }

            this.repository().add(model);

        }
        repository().commit();
    }

    @SystemService(args = "PrjId,flag,formId,engagId,sPrjId")
    public UIPagedList getPrjAttachment( Long PrjId, int flag, Long formId,Long engagId,String sPrjId) throws Exception {
        List<PrjAttachment> lPrjAtt;

        if(Framework.isNotNullOrEmpty(PrjId) || Framework.isNotNullOrEmpty(sPrjId))
        {
            if(Framework.isNotNullOrEmpty(PrjId))
            {lPrjAtt = this.stream().where(w -> w.getProjectId().getId() == PrjId && w.getFlag() == flag).toList();}
            else
            {lPrjAtt = this.stream().where(w -> w.getProjectId().getUuid().equals(sPrjId) && w.getFlag() == flag).toList();}

        }else//业务约定书对应的附件，
        {
            lPrjAtt= this.stream().where(w -> w.getEngagementId().getId() == engagId && w.getFlag() == flag).toList();
        }

        List<Object> list = new ArrayList<>();
        lPrjAtt.forEach(f -> {
            f.initialization();
            list.add(f);
        });

        return UIPagedList.band(list, FormPage.getFormColumn(formId));
    }

    public void abd(){

    }
}
