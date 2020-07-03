package com.dxn.model.extend;

import com.alibaba.fastjson.JSON;
import com.dxn.dto.TechnicalConsultationDTO;
import com.dxn.model.entity.TechnicalConsultationEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.context.AppContext;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "PM_TechnicalConsultation")
public class TechnicalConsultation extends TechnicalConsultationEntity {
    @SystemService(args = "dataList")
    public void batchSave(String dataList) throws Exception {

        List<TechnicalConsultationDTO> technicalConsultationDTOList = JSON.parseArray(dataList, TechnicalConsultationDTO.class);

        Long currentUserId = AppContext.current().getProfile().getId();
        Optional<User> optionalUser = User.stream().where(data -> data.getId().equals(currentUserId)).findFirst();
        Optional<Project> optionalProject = Optional.empty();
        Long count = TechnicalConsultation.stream().count();
        for (int i = 0, size = technicalConsultationDTOList.size(); i < size; i++) {
            Long dataId = technicalConsultationDTOList.get(i).getId();
            if(dataId != null && !TechnicalConsultation.stream().where(data -> data.getId().equals(dataId)).findFirst().isPresent()){
                continue;
            }
            TechnicalConsultation technicalConsultation = this.createNew();
            technicalConsultation.setId(technicalConsultationDTOList.get(i).getId());
            technicalConsultation.setConsultationType(technicalConsultationDTOList.get(i).getConsultationType());
            technicalConsultation.setConsultationMatter(technicalConsultationDTOList.get(i).getConsultationMatter());
            if (technicalConsultationDTOList.get(i).getAttachmentId() != null) {
                technicalConsultation.setAttachment(technicalConsultationDTOList.get(i).getAttachmentId());
            }
            technicalConsultation.setRemake(technicalConsultationDTOList.get(i).getRemake());
            if (technicalConsultationDTOList.get(i).getAttachmentAnswerId() != null) {
                technicalConsultation.setAttachmentAnswer(technicalConsultationDTOList.get(i).getAttachmentAnswerId());
            }
            if (technicalConsultationDTOList.get(i).getProjectNameId() != null) {
                Long projectId = technicalConsultationDTOList.get(i).getProjectNameId();
                technicalConsultation.setProjectName(projectId);
                optionalProject = Project.stream().where(data -> data.getId().equals(projectId)).findFirst();
            }
            technicalConsultation.setAdoptOrNot(technicalConsultationDTOList.get(i).getAdoptOrNot());
            technicalConsultation.setRiskLevel(optionalProject.isPresent() ? optionalProject.get().getRiskLevel() : null);

            if (optionalProject.isPresent()) {
                technicalConsultation.setUnits(optionalProject.get().getDeptId().getId());
            }
//            if(technicalConsultationDTOList.get(i).getUnitsId() != null){
//                technicalConsultation.setUnits(technicalConsultationDTOList.get(i).getUnitsId());
//            }
            if (technicalConsultationDTOList.get(i).getConsultationBoyId() != null) {
                technicalConsultation.setConsultationBoy(technicalConsultationDTOList.get(i).getConsultationBoyId());
            } else {
                technicalConsultation.setConsultationBoy(currentUserId);
            }
            technicalConsultation.setMobilePhone(optionalUser.get().getPhone());
            technicalConsultation.setEmail(optionalUser.get().getEmail());
            if (technicalConsultationDTOList.get(i).getAnswerBoyId() != null) {
                technicalConsultation.setAnswerBoy(technicalConsultationDTOList.get(i).getAnswerBoyId());
            }
            technicalConsultation.setSubmissionDate(technicalConsultationDTOList.get(i).getSubmissionDate() == null ? LocalDateTime.now() : technicalConsultationDTOList.get(i).getSubmissionDate());
            technicalConsultation.setConsultationStatus(technicalConsultationDTOList.get(i).getConsultationStatus());
            if(technicalConsultationDTOList.get(i).getOrderNumber() == null && dataId == null){
                technicalConsultation.setOrderNumber(count.intValue() + technicalConsultationDTOList.get(i).getBaseRowsNumber() + 1);
            }else{
                technicalConsultation.setOrderNumber(technicalConsultationDTOList.get(i).getOrderNumber());
            }
            this.repository().add(technicalConsultation);
        }

//        JSONArray jsonArray = JSONArray.fromObject(dataList);
//        List<Map<Object,String>> mapList = (List<Map<Object,String>>)jsonArray;
//        if(mapList != null && mapList.size() > 0){
//            for(Map<Object,String> item : mapList){
//                TechnicalConsultation tc = new TechnicalConsultation();
//
//                tc.setId(JSONNull.getInstance().equals(item.get("id")) ? null : Long.parseLong(String.valueOf(item.get("id"))));
//
//                if(JSONNull.getInstance().equals(item.get("consultationType"))){
//                    Map<Object,String> map = (Map<Object,String>)JSONObject.fromObject(item.get("consultationType"));
//                    tc.setConsultationType(Integer.parseInt(map.get("id")));
//                }
//
//                tc.setConsultationMatter(JSONNull.getInstance().equals(item.get("consultationMatter")) ? null : item.get("consultationMatter"));
//
//                if(JSONNull.getInstance().equals(item.get("attachmentId")) && StringUtils.isNoneBlank(item.get("attachmentId"))){
//                    Attachment attachment = new Attachment();
//                    attachment.setId(Long.parseLong(item.get("attachmentId")));
//                    tc.setAttachmentId(attachment);
//                }
//
//                tc.setRemake(JSONNull.getInstance().equals(item.get("remake")) ? null : item.get("remake"));
//
//                if(JSONNull.getInstance().equals(item.get("attachmentAnswerId")) && StringUtils.isNoneBlank(item.get("attachmentAnswerId"))){
//                    Attachment attachment = new Attachment();
//                    attachment.setId(Long.parseLong(item.get("attachmentAnswerId")));
//                    tc.setAttachmentAnswerId(attachment);
//                }
//
//                if(JSONNull.getInstance().equals(item.get("projectNameId")) && StringUtils.isNoneBlank(item.get("projectNameId"))){
//                    Project project = new Project();
//                    project.setId(Long.parseLong(item.get("projectNameId")));
//                    tc.setProjectNameId(project);
//                }
//
//                if(JSONNull.getInstance().equals(item.get("adoptOrNot"))){
//                    Map<Object,String> map = (Map<Object,String>)JSONObject.fromObject(item.get("adoptOrNot"));
//                    tc.setAdoptOrNot(Integer.parseInt(map.get("id")));
//                }
//
//                if(JSONNull.getInstance().equals(item.get("riskLevel"))){
//                    Map<Object,String> map = (Map<Object,String>)JSONObject.fromObject(item.get("adoptOrNot"));
//                    tc.setRiskLevel(Integer.parseInt(map.get("id")));
//                }
//
//                if(JSONNull.getInstance().equals(item.get("unitsId")) && StringUtils.isNoneBlank(item.get("unitsId"))){
//                    Department department = new Department();
//                    department.setId(Long.parseLong(item.get("unitsId")));
//                    tc.setUnitsId(department);
//                }
//
//                if(JSONNull.getInstance().equals(item.get("consultationBoyId")) && StringUtils.isNoneBlank(item.get("consultationBoyId"))){
//                    User user = new User();
//                    user.setId(Long.parseLong(item.get("consultationBoyId")));
//                    tc.setConsultationBoyId(user);
//                }
//
//                tc.setMobilePhone(JSONNull.getInstance().equals(item.get("mobilePhone")) ? null : item.get("mobilePhone"));
//
//                tc.setEmail(JSONNull.getInstance().equals(item.get("email")) ? null : item.get("email"));
//
//                if(JSONNull.getInstance().equals(item.get("answerBoyId")) && StringUtils.isNoneBlank(item.get("answerBoyId"))){
//                    User user = new User();
//                    user.setId(Long.parseLong(item.get("answerBoyId")));
//                    tc.setAnswerBoyId(user);
//                }
//
//                tc.setConsultationStatus(JSONNull.getInstance().equals(item.get("consultationStatus")) && StringUtils.isNoneBlank(item.get("consultationStatus")) ? null : Integer.parseInt(item.get("consultationStatus")));
//
//
//
//
//                this.repository().add(tc);
//            }
//        }
    }

    @SystemService(args = "id")
    public void deleteMethod(Long id) throws Exception {
        TechnicalConsultation tc = new TechnicalConsultation();
        tc.setId(id);
        this.repository().remove(tc);
        //重新排序
        List<TechnicalConsultation> technicalConsultationList = TechnicalConsultation.stream().where(data -> !data.getId().equals(id)).sortedBy(data -> data.getSubmissionDate()).sortedBy(data -> data.getOrderNumber()).toList();
        for(int i = 0,size = technicalConsultationList.size();i < size;i ++){
            technicalConsultationList.get(i).setOrderNumber(i + 1);
            this.repository().add(technicalConsultationList.get(i));
        }
    }

    @SystemService(args = "type")
    public List<TechnicalConsultationDTO> queryData(String type) throws Exception {
        List<TechnicalConsultation> dataResult = new ArrayList();
        List<TechnicalConsultationDTO> technicalConsultationDTOList = new ArrayList();
        Long currentUserId = AppContext.current().getProfile().getId();
        if ("1".equals(type)) {               //待分配
            dataResult = TechnicalConsultation.stream().where(data -> data.getAnswerBoyId().getId() == null).sortedDescendingBy(data -> data.getSubmissionDate()).sortedDescendingBy(data ->data.getOrderNumber()).toList();
        } else if ("2".equals(type)) {         //咨询中
            dataResult = TechnicalConsultation.stream().where(data -> currentUserId.equals(data.getAnswerBoyId().getId()) && data.getAttachmentAnswerId().getId() == null).sortedDescendingBy(data -> data.getSubmissionDate()).sortedDescendingBy(data ->data.getOrderNumber()).toList();
        } else if ("3".equals(type)) {         //已完成
            dataResult = TechnicalConsultation.stream().where(data -> currentUserId.equals(data.getAnswerBoyId().getId()) && data.getAttachmentAnswerId().getId() != null).sortedDescendingBy(data -> data.getSubmissionDate()).sortedDescendingBy(data ->data.getOrderNumber()).toList();
        }
        for (int i = 0; i < dataResult.size(); i++) {
            TechnicalConsultationDTO technicalConsultationDTO = new TechnicalConsultationDTO();
            technicalConsultationDTO.setId(dataResult.get(i).getId());
            technicalConsultationDTO.setConsultationType(dataResult.get(i).getConsultationType());
            technicalConsultationDTO.setConsultationMatter(dataResult.get(i).getConsultationMatter());
            technicalConsultationDTO.setAttachmentId(dataResult.get(i).getAttachmentId() == null ? null : dataResult.get(i).getAttachmentId().getId());
            technicalConsultationDTO.setAttachmentReturnId(dataResult.get(i).getAttachmentId() == null ? null : dataResult.get(i).getAttachmentId());
            technicalConsultationDTO.setRemake(dataResult.get(i).getRemake());
            technicalConsultationDTO.setAttachmentAnswerId(dataResult.get(i).getAttachmentAnswerId() == null ? null : dataResult.get(i).getAttachmentAnswerId().getId());
            technicalConsultationDTO.setAttachmentAnswerReturnId(dataResult.get(i).getAttachmentAnswerId() == null ? null : dataResult.get(i).getAttachmentAnswerId());
            technicalConsultationDTO.setProjectNameId(dataResult.get(i).getProjectNameId() == null ? null : dataResult.get(i).getProjectNameId().getId());
            technicalConsultationDTO.setProjectName(dataResult.get(i).getProjectNameId() == null ? null : dataResult.get(i).getProjectNameId().getName());
            technicalConsultationDTO.setAdoptOrNot(dataResult.get(i).getAdoptOrNot());
            technicalConsultationDTO.setRiskLevel(dataResult.get(i).getRiskLevel());

            technicalConsultationDTO.setUnitsId(dataResult.get(i).getUnitsId() == null ? null : dataResult.get(i).getUnitsId().getId());
            technicalConsultationDTO.setUnitsName(dataResult.get(i).getUnitsId() == null ? null : dataResult.get(i).getUnitsId().getName());
            technicalConsultationDTO.setConsultationBoyId(dataResult.get(i).getConsultationBoyId() == null ? null : dataResult.get(i).getConsultationBoyId().getId());
            technicalConsultationDTO.setConsultationBoyName(dataResult.get(i).getConsultationBoyId() == null ? null : dataResult.get(i).getConsultationBoyId().getName());
            technicalConsultationDTO.setMobilePhone(dataResult.get(i).getMobilePhone());
            technicalConsultationDTO.setEmail(dataResult.get(i).getEmail());
            technicalConsultationDTO.setAnswerBoyId(dataResult.get(i).getAnswerBoyId() == null ? null : dataResult.get(i).getAnswerBoyId().getId());
            technicalConsultationDTO.setAnswerBoyName(dataResult.get(i).getAnswerBoyId() == null ? null : dataResult.get(i).getAnswerBoyId().getName());
            technicalConsultationDTO.setSubmissionDate(dataResult.get(i).getSubmissionDate());
            technicalConsultationDTO.setConsultationStatus(dataResult.get(i).getConsultationStatus());
            technicalConsultationDTO.setUuid(dataResult.get(i).getUuid());
            technicalConsultationDTOList.add(technicalConsultationDTO);
        }
        return technicalConsultationDTOList;
    }

    @SystemService(args = "technicalConsultationId,userId")
    public void setUser(List<Long> technicalConsultationId, Long userId) throws Exception {
        if (Framework.isNotNullOrEmpty(userId)) {
            for (Long id : technicalConsultationId) {
                TechnicalConsultation tc = TechnicalConsultation.findById(id);
                tc.setAnswerBoy(userId);
            }
        }
    }

    @SystemService(args = "dataId,attachmentId")
    public void businessHandler(Long dataId, Long attachmentId) throws Exception {
        Optional<TechnicalConsultation> optionalTechnicalConsultation = TechnicalConsultation.stream().where(data -> data.getId().equals(dataId)).findFirst();
        if (optionalTechnicalConsultation.isPresent()) {
            TechnicalConsultation technicalConsultation = optionalTechnicalConsultation.get();
            Attachment attachment = new Attachment();
            attachment.setId(attachmentId);
            technicalConsultation.setAttachmentAnswerId(attachment);
            technicalConsultation.setAnswerBoy(AppContext.current().getProfile().getId());
            this.repository().add(technicalConsultation);
        }
    }


    @SystemService(args = "userId")
    public void insertGroupUser(String userId) throws Exception{
        if(StringUtils.isNotEmpty(userId)){
            String [] userIdArray = userId.split(",");
            Optional<UserGroup> optionalUserGroup = UserGroup.stream().where(data -> data.getCode().equals("TechnologyConsultationGroup")).findFirst();
            for(int i = 0,size = userIdArray.length;i < size;i++){
                Long userItemId = Long.parseLong(userIdArray[i]);
                Optional<User> optionalUser = User.stream().where(data -> data.getId().equals(userItemId)).findFirst();
                if(optionalUser.isPresent()){
                    UserGroupItem userGroupItem = UserGroupItem.createNew();
                    userGroupItem.setUserId(optionalUser.get());
                    userGroupItem.setUserGroupId(optionalUserGroup.get());
                    this.repository().add(userGroupItem);
                }
            }

        }
    }
}
