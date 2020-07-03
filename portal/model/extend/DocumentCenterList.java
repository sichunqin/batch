package com.dxn.model.extend;

import com.dxn.dto.DocumentCenterListDTO;
import com.dxn.dto.DocumentCenterTreeDTO;
import com.dxn.model.entity.DocumentCenterListEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.context.AppContext;
import org.apache.commons.lang.StringUtils;
import reactor.core.Exceptions;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.swing.text.Document;
import javax.swing.text.html.Option;
import java.util.*;

@Entity
@Table(name = "PM_DocumentCenterList")
public class DocumentCenterList extends DocumentCenterListEntity {

    /**
     * 返回文档列表
     *
     * @return
     * @throws Exception
     */
    @SystemService(args = "treeId")
    public Map<String,List<DocumentCenterListDTO>> queryDocumentData (Long treeId) throws Exception{
        Map<String,List<DocumentCenterListDTO>> resultMap = new HashMap();
        List<DocumentCenterList> documentCenterListList = new ArrayList();
        List<DocumentCenterListDTO> documentCenterListDTOList = new ArrayList();
        if(AppContext.current().getProfile().getIsSuperAdmin()){
            documentCenterListList =  treeId == null ? DocumentCenterList.stream().toList() : DocumentCenterList.stream().where(data -> data.getDocumentCodeId().equals(treeId)).toList();
        }else{
            documentCenterListList = getShowData(treeId);
        }

//        for(int i = 0,size = documentCenterListList.size();i < size;i++){
//            Long attachmentId = documentCenterListList.get(i).getAttachmentId();
//            if(Framework.isNotNullOrEmpty(attachmentId)){
//                Optional<Attachment> optionalAttachment = Attachment.stream().where(data -> data.getId().equals(attachmentId)).findFirst();
//                attachmentList.add(optionalAttachment.get());
//            }
//        }
        for(int i = 0,size = documentCenterListList.size();i < size;i++){
            DocumentCenterListDTO documentCenterListDTO = new DocumentCenterListDTO();
            documentCenterListDTO.setId(documentCenterListList.get(i).getId());
            documentCenterListDTO.setAttachmentId(documentCenterListList.get(i).getAttachmentId());
            documentCenterListDTO.setDocumentCodeId(documentCenterListList.get(i).getDocumentCodeId());
            documentCenterListDTO.setName(documentCenterListList.get(i).getName());
            documentCenterListDTO.setReleaseDateCode(documentCenterListList.get(i).getReleaseDateCode());
            documentCenterListDTO.setRemarks(documentCenterListList.get(i).getRemarks());
            documentCenterListDTO.setSerialNumber(documentCenterListList.get(i).getSerialNumber());
            documentCenterListDTO.setSubmitterId(documentCenterListList.get(i).getSubmitterId().getId());
            documentCenterListDTO.setSubmitterName(documentCenterListList.get(i).getSubmitterId().getName());
            documentCenterListDTOList.add(documentCenterListDTO);
        }
        resultMap.put("rows",documentCenterListDTOList);
        return resultMap;
    }

    /**
     * 根据文档返回附件
     *
     * @return
     * @throws Exception
     */

    @SystemService(args = "documentIds")
    public Map<String,List<Attachment>> queryAttachment(String documentIds) throws Exception{
        Map<String,List<Attachment>> resultMap = new HashMap();
        List<Attachment> attachmentList = new ArrayList();
        if(StringUtils.isNotEmpty(documentIds)){
            String [] documentIdsArray = documentIds.split(",");
            for(int i = 0,size = documentIdsArray.length;i < size;i++){
                Long documentId = Long.parseLong(documentIdsArray[i]);
                Optional<DocumentCenterList> optionalDocumentCenterList = DocumentCenterList.stream().where(data -> data.getId().equals(documentId)).findFirst();
                if(optionalDocumentCenterList.isPresent()){
                    Long attachmentId = optionalDocumentCenterList.get().getAttachmentId();
                    if(attachmentId != null){
                        Optional<Attachment> optionalAttachment = Attachment.stream().where(data -> data.getId().equals(attachmentId)).findFirst();
                        attachmentList.add(optionalAttachment.get());
                    }else{
                        continue;
                    }

                }
            }
        }
        resultMap.put("rows",attachmentList);


        return resultMap;
    }

    private List<DocumentCenterList> getShowData(Long treeId) throws Exception{
        Long userId = AppContext.current().getProfile().getId();
        Set<Long> idSet = new HashSet<>();
        List<Long> longList = new ArrayList<>();
        Optional<User> optionalUser = User.stream().where(data -> data.getId().equals(userId)).findFirst();
        longList.add(userId);
        //longList.add(optionalUser.get().getDept().getId());

        longList.addAll(recursionDeptId(optionalUser.get().getDept(),new ArrayList<Long>()));
        List<UserGroupItem> userGroupItemList = UserGroupItem.stream().where(data -> data.getUserId().getId().equals(userId)).toList();
        for(int i = 0,size = userGroupItemList.size();i < size;i++){
            Long userGroupId = userGroupItemList.get(i).getUserGroupId().getId();
            longList.add(userGroupId);
        }
        Long staffId = optionalUser.get().getStaffId();
        Optional<Staff> optionalStaff = Staff.stream().where(data -> data.getId().equals(staffId)).findFirst();
        longList.add(optionalStaff.get().getStaffPositionId().getId());

        List<DocumentCenterTreeButtonJurisdiction> documentCenterTreeButtonJurisdictionList = DocumentCenterTreeButtonJurisdiction.stream().where(data -> longList.contains(data.getSelectedId())).toList();
        List<Long> manageTreeIdList = new ArrayList();
        for(int i = 0,size = documentCenterTreeButtonJurisdictionList.size();i < size;i++){
            Long treeNodeId = documentCenterTreeButtonJurisdictionList.get(i).getDocumentCenterTreeId().getId();
            manageTreeIdList.add(treeNodeId);
        }

        List<DocumentCenterListJurisdiction> listJurisdictions = DocumentCenterListJurisdiction.stream().where(data -> longList.contains(data.getSelectedId()) && data.getDownloadViewPermission().equals(true)).toList();
        for(int i = 0 ,size = listJurisdictions.size();i < size;i++){
            idSet.add(listJurisdictions.get(i).getDocumentCenterListId().getId());
        }

        if(treeId == null){
            if(idSet.size() > 0 && manageTreeIdList.size() > 0){
                return DocumentCenterList.stream().where(data -> idSet.contains(data.getId()) || manageTreeIdList.contains(data.getDocumentCodeId())).toList();
            }else if(idSet.size() == 0 && manageTreeIdList.size() > 0){
                return DocumentCenterList.stream().where(data -> manageTreeIdList.contains(data.getDocumentCodeId())).toList();
            }else if(idSet.size() > 0 && manageTreeIdList.size() == 0){
                return DocumentCenterList.stream().where(data -> idSet.contains(data.getId())).toList();
            }else{
                return null;
            }

        }else{
            if(idSet.size() > 0 && manageTreeIdList.size() > 0){
                return DocumentCenterList.stream().where(data -> (idSet.contains(data.getId()) || manageTreeIdList.contains(data.getDocumentCodeId())) && data.getDocumentCodeId().equals(treeId)).toList();
            }else if(idSet.size() == 0 && manageTreeIdList.size() > 0){
                return DocumentCenterList.stream().where(data -> manageTreeIdList.contains(data.getDocumentCodeId()) && data.getDocumentCodeId().equals(treeId)).toList();
            }else if(idSet.size() > 0 && manageTreeIdList.size() == 0){
                return DocumentCenterList.stream().where(data -> idSet.contains(data.getId()) && data.getDocumentCodeId().equals(treeId)).toList();
            }else{
                return null;
            }

        }

    }

    private List<Long> recursionDeptId(Department department,List<Long> deptIdList) throws Exception{
        if(department.getId() != null){
            deptIdList.add(department.getId());
            if(department.getParentId() != null){
                recursionDeptId(department.getParentId(),deptIdList);
            }
        }
        return deptIdList;
    }

    @SystemService(args = "dataId")
    public void deleteData(Long dataId) throws Exception{
        List<DocumentCenterListJurisdiction> documentCenterListJurisdictions = DocumentCenterListJurisdiction.stream().where(data -> data.getDocumentCenterListId().getId().equals(dataId)).toList();
        for(int i = 0,size = documentCenterListJurisdictions.size();i < size;i++){
            this.repository().remove(documentCenterListJurisdictions.get(i));
        }
        DocumentCenterList documentCenterList = DocumentCenterList.createNew();
        documentCenterList.setId(dataId);
        this.repository().remove(documentCenterList);
    }

    @SystemService(args = "attachmentId")
    public Boolean judgeDownloadOrNot(Long attachmentId) throws Exception{
        Boolean flag = false;
        if(AppContext.current().getProfile().getIsSuperAdmin()){
            flag = true;
        }else{
            List<DocumentCenterList> documentCenterLists = getShowData(null);
            for(int i = 0,size = documentCenterLists.size();i < size;i++){
                if(attachmentId.equals(documentCenterLists.get(i).getAttachmentId())){
                    flag = true;
                }
            }
        }

        return flag;
    }
}
