package com.dxn.model.extend;

import com.dxn.dto.AuditImportanceDTO;
import com.dxn.dto.AuditImportanceListDTO;
import com.dxn.dto.auditspecificimportanceDTO;
import com.dxn.model.entity.AuditImportanceEntity;
import com.dxn.system.ModelHelper;
import com.dxn.system.ResponseEntity;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Prj_AuditImportance")
public class AuditImportance extends AuditImportanceEntity {


    /**
     * 获取重要性和特大账户列表    ------苏亚金诚
     *
     * @param auditProjectId 被审计单位项目id
     * @return ResponseEntity
     */
    @SystemService(args = "auditProjectId")
    public ResponseEntity syjcAuditImportanceList(String auditProjectId) {
        ResponseEntity respon = new ResponseEntity();
        try {
            AuditImportanceListDTO auditdto = new AuditImportanceListDTO();
            List<AuditImportance> importList = AuditImportance.stream().where(c -> c.getAuditworkProjectID() == auditProjectId).toList();
            List<Auditspecificimportance> specificList = Auditspecificimportance.stream().where(c -> c.getAuditworkProjectID().equals(auditProjectId)).toList();

            List<AuditImportanceDTO> importlist = new ArrayList<>();
            for (AuditImportance item : importList) {
                AuditImportanceDTO importdto = new AuditImportanceDTO();
                ModelHelper.copyModel(item, importdto);
                importlist.add(importdto);
            }
            List<auditspecificimportanceDTO> auditlist = new ArrayList<>();
            for (Auditspecificimportance t : specificList) {
                auditspecificimportanceDTO dto = new auditspecificimportanceDTO();
                ModelHelper.copyModel(t, dto);
                auditlist.add(dto);
            }
            auditdto.setSpecificList(auditlist);
            auditdto.setImportList(importlist);
            respon.setCode(200);
            respon.setData(auditdto);

        } catch (Exception ex) {
            respon.setCode(500);
        }
        return respon;
    }


    /**
     * 保存 重要性和特大账户   -- 苏亚金诚
     *
     * @return
     * @throws Exception
     */
    @SystemService(args = "model")
    public ResponseEntity syjcSaveAuditImportance(AuditImportanceListDTO model) throws Exception {
        ResponseEntity respons = new ResponseEntity();
        try {
            String auditImportanceId = model.getImportList().get(0).getAuditImportanceId();
            List<AuditImportance> auditImportaceList = AuditImportance.stream().where(c -> c.getAuditImportanceId().equals(auditImportanceId)).toList();

            for (AuditImportance item : auditImportaceList) {
                item.initialization();
                this.repository().remove(item);
            }

            List<Auditspecificimportance> specificList = Auditspecificimportance.stream().where(c -> c.getAuditImportanceID().equals(auditImportanceId)).toList();

            for (Auditspecificimportance item : specificList) {
                item.initialization();
                this.repository().remove(item);
            }

            for (AuditImportanceDTO item : model.getImportList()) {
                AuditImportance importance = this.createNew();
                ModelHelper.copyModel(item, importance);
                repository().add(importance);
            }

            if (model.getSpecificList().size() > 0) {
                for (auditspecificimportanceDTO item : model.getSpecificList()) {
                    Auditspecificimportance auditspecificimportance = Auditspecificimportance.createNew();
                    ModelHelper.copyModel(item, auditspecificimportance);
                    repository().add(auditspecificimportance);
                }
            }
            respons.setCode(200);
            respons.setMessages("ok");
        } catch (Exception ex) {
            respons.setCode(500);
            respons.setMessages("删除失败,服务器内部错误");
        }
        return respons;
    }

    @SystemService()
    public void excel() throws Exception {
        AuditImportanceListDTO model = new AuditImportanceListDTO();

        List<AuditImportanceDTO> importList = new ArrayList<>();

        AuditImportanceDTO importanceDTO = new AuditImportanceDTO();
        importanceDTO.setAuditworkProjectID("123213");
        importanceDTO.setAuditImportanceId("1");
        importList.add(importanceDTO);

        List<auditspecificimportanceDTO> auditspecificimportanceDTOS = new ArrayList<>();
        auditspecificimportanceDTO auditspecificimportanceDTO = new auditspecificimportanceDTO();
        auditspecificimportanceDTO.setAuditImportanceID("1");
        auditspecificimportanceDTO.setAuditworkProjectID("123213");
        auditspecificimportanceDTOS.add(auditspecificimportanceDTO);

        model.setImportList(importList);
        model.setSpecificList(auditspecificimportanceDTOS);
        syjcSaveAuditImportance(model);
        //syjcAuditImportanceList("123213");
    }


}
