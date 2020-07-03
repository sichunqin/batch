package com.dxn.model.extend;

import com.dxn.model.entity.ReportFileEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.configuration.DxnConfig;
import com.dxn.system.context.AppContext;
import com.dxn.util.orcode.QRCodeToPdfUtil;
//import com.dxn.util.orcode.QRCodeToPdfUtil;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "PM_ReportFile")
public class ReportFile extends ReportFileEntity {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        this.setExistFile(Framework.isNotNullOrEmpty(this.getAttachmentId()));
        this.saveHistoryFile();
    }

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

        if (Framework.isNotNullOrEmpty(this.getReportId())) {
            filePrintNum = printNum;
            if (Framework.isNotNullOrEmpty(this.getReportId().getPrintedNumber()))
                filePrintNum = filePrintNum + this.getReportId().getPrintedNumber();

            this.getReportId().setPrintedNumber(filePrintNum);
        }

        repository().commit();
    }

    @Override
    public String gainAttachmentPath() throws Exception {
        String path = super.gainAttachmentPath();
        String projectCode = this.getReport().getProjectId().getCode();
        return String.format("%s/%s/报告", path, projectCode);
    }

    //保存的时候该文件是否需要存历史
    private void saveHistoryFile() throws Exception {
        if (!this.getSaveHistory()) return;
        if (Framework.isNullOrEmpty(this.getReportModifyId())) return;
//        if (Framework.isNullOrEmpty(this.getAttachmentId())) return;
        ReportFile old = this.getOriginalValue();
        if (Framework.isNullOrEmpty(old)) return;
        if (Framework.isNullOrEmpty(old.getAttachmentId())) return;
        if (Framework.isNotNullOrEmpty(this.getAttachmentId()) && old.getAttachmentId().getId() == this.getAttachmentId().getId())
            return;

        ReportHistoryFile history = ReportHistoryFile.createNew();
        history.setFileType(old.getFileType());
        history.setAttachmentId(old.getAttachmentId());
        history.setReportFileId(old.getId());
        history.setReportModify(old.getReportModifyId());
        Report report = old.getReportId();
        history.setReportId(report.getId());
        history.setModifyNumber(report.getModificatioTimes());
        this.setSaveHistory(false);//每次修改只增加一次文件记录
        this.setModifyNumber(report.getModificatioTimes());
        this.repository().add(history);
    }

    @SystemService(args = "entityId")
    public void callQRCodeInterface(Long entityId) throws Exception{
        Optional<ReportFile> reportFileOptional = ReportFile.stream().where(data -> data.getId().equals(entityId)).findFirst();
        if(reportFileOptional.isPresent()){

            Long attachmentId = reportFileOptional.get().getAttachmentId().getId();
            Optional<Attachment> optionalAttachment = Attachment.stream().where(data -> data.getId().equals(attachmentId)).findFirst();
            if(optionalAttachment.isPresent()){
                Attachment attachment = optionalAttachment.get();
                String fileUrl = DxnConfig.getFileUploadPath() + "\\" + attachment.getPath().replace("/","\\") + "\\" + attachment.getMd5() + attachment.getSuffix();
                String pageUrl = DxnConfig.getWebServer() + "/QRCodeShowForm-Schema.html?schemaType=FormPanel&reportId=" + reportFileOptional.get().getReportId().getId();

               QRCodeToPdfUtil.pdfInsertQrCode(fileUrl, pageUrl);
            }


        }
    }
}
