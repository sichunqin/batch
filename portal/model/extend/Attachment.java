package com.dxn.model.extend;

import com.dxn.entity.EntityBase;
import com.dxn.model.entity.AttachmentEntity;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.configuration.DxnConfig;
import com.dxn.system.dto.AttachmentDTO;
import com.dxn.system.dto.AttachmentJsonDTO;
import com.dxn.system.interfaces.IAttachmentService;
import com.dxn.system.io.FileHelper;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Base_Attachment")
public class Attachment extends AttachmentEntity implements IAttachmentService {

    @Override
    public void onInserting() throws Exception {
        super.onInserting();
        //清理过期文件 主子那种存在点问题
//        this.deleteIsDeleteAttachment();
    }

    @Override
    public void onDeleted() throws Exception {
        //数据删除后把文件也删掉
        String uploadsFolder = DxnConfig.getFileUploadPath();
        String fileName = this.getMd5() + this.getSuffix();
        String filePath = FileHelper.combine(uploadsFolder, this.getPath());
        FileHelper.deleteFile(filePath, fileName);
    }

    @Override
    public void createChildrenAttachment(List<AttachmentJsonDTO> attachmentMap) throws Exception {

        for (AttachmentJsonDTO item : attachmentMap) {

            String path = item.getEntity().gainAttachmentPath();

            //子列表
            if (Framework.isNotNullOrEmpty(item.getJson())) {
                List<Attachment> attachments = Framework.parseJsonArray(item.getJson(), Attachment.class);
                for (Attachment attachment : attachments) {
                    attachment.setEntityId(item.getEntity().getId());
                    attachment.setEntityName(item.getEntity().gainTypeName());
                    if (!attachment.getStateType().equalsIgnoreCase("Deleted")) {
                        attachment.moveFilePath(path);
                        this.repository().add(attachment);
                    } else {
                        this.repository().remove(attachment);
                    }
                }
            }

            //引用附件
            if (Framework.isNotNullOrEmpty(item.getAttachment())) {
                Attachment attachment = (Attachment) item.getAttachment();
                attachment.moveFilePath(path);
                this.repository().add(attachment);
            }
        }

        this.repository().commit();
    }

    @Override
    public void deleteAttachment(EntityBase entity) throws Exception {
        if (entity instanceof Attachment) return;

        List<Attachment> source = entity.gainValue(Attachment.class);
        for (Attachment item : source) {
            item.setIsDelete(true);
            repository().add(item);
        }

        Long entityId = entity.getId();
        String entityType = entity.gainTypeName();
        List<Attachment> list = Attachment.stream().where(w -> w.getEntityId() == entityId && w.getEntityName() == entityType).toList();
        for (Attachment item : list) {
            item.setIsDelete(true);
            repository().add(item);
        }
    }

    private void moveFilePath(String path) throws Exception {
        //移动附件
        if (Framework.isNullOrEmpty(path)) return;
        String uploadsFolder = DxnConfig.getFileUploadPath();
        String sourcePath = FileHelper.combine(uploadsFolder, this.getPath());
        String newPath = FileHelper.combine(uploadsFolder, path);
        String fileName = this.getMd5() + this.getSuffix();
        FileHelper.moveFile(sourcePath, newPath, fileName);
        this.setPath(path);
    }

    public static void deleteIsDeleteAttachment() throws Exception {
        LocalDateTime now = LocalDateTime.now().plusDays(-1);
        List<Attachment> list = Attachment.stream().where(w -> (w.getIsDelete() || w.getPath() == "temp") && w.getModifiedOn().isBefore(now)).toList();
        for (Attachment item : list) {
            repository().remove(item);
        }
    }

    @SystemService(args = "entityId,entityType")
    public List<AttachmentDTO> getAttachments(Long entityId, String entityType) throws Exception {
        List<AttachmentDTO> list = new ArrayList<>();
        List<Attachment> attachments = this.stream().where(w -> w.getEntityId() == entityId && w.getEntityName().equals(entityType)).toList();
        for (Attachment item : attachments) {
            AttachmentDTO dto = new AttachmentDTO();
            ModelHelper.copyModel(item, dto);
            list.add(dto);
        }
        return list;
    }

    public List<AttachmentDTO> gainAttachments(Long entityId, String entityType) throws Exception {
        return getAttachments(entityId, entityType);
    }
}
