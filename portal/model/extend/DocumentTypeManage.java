package com.dxn.model.extend;

import com.dxn.dto.DocumentCenterListDTO;
import com.dxn.model.entity.DocumentTypeManageEntity;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.context.AppContext;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

@Entity
@Table(name = "PM_DocumentTypeManage")
public class DocumentTypeManage extends DocumentTypeManageEntity {
    @SystemService(args = "id,selectedId")
    public boolean validationSelectedItem(Long id, Integer selectedId) throws Exception{
        List<DocumentTypeManage> documentTypeManageList = DocumentTypeManage.stream().where(data -> data.getCorrespondingPage().equals(selectedId) && !data.getId().equals(id == null ? -1L: id)).toList();
        if(documentTypeManageList != null && documentTypeManageList.size() > 0){
            return true;
        }
        return false;
    }

    @SystemService(args = "correspondingPage")
    public Map<String,Object> queryDocumentList(Integer correspondingPage) throws Exception{
        Map<String,Object> map = new HashMap();
        Optional<DocumentTypeManage> documentTypeManage = DocumentTypeManage.stream().where(data -> data.getCorrespondingPage().equals(correspondingPage) && data.getDocumentTypeStatus().equals(10)).findFirst();
        List<DocumentCenterList> documentCenterLists = new ArrayList();
        List<DocumentCenterListDTO> documentCenterListDTOList = new ArrayList();
        if(documentTypeManage.isPresent()){
            map.put("titleName",documentTypeManage.get().getDocumentType());
            String documentCodeId = documentTypeManage.get().getCorrespondingDocument();
            if(!StringUtils.isNoneBlank(documentCodeId)) return map;
            String [] documentCodeIdArray = documentCodeId.substring(2,documentCodeId.length() - 2).split("\",\"");
            for(String item : documentCodeIdArray){
                if(StringUtils.isNoneBlank(item)){
                    Long codeId = Long.parseLong(item);
                    List<DocumentCenterList> resultDocumentCenterList = AppContext.current().getProfile().getIsSuperAdmin() ? DocumentCenterList.stream().where(data -> data.getDocumentCodeId().equals(codeId)).toList() : getShowData(codeId);
                    if(resultDocumentCenterList != null && resultDocumentCenterList.size() > 0){
                        documentCenterLists.addAll(resultDocumentCenterList);
                    }
                }
            }
            for(int i = 0,size = documentCenterLists.size();i < size;i++){
                DocumentCenterListDTO documentCenterListDTO = new DocumentCenterListDTO();
                documentCenterListDTO.setId(documentCenterLists.get(i).getId());
                documentCenterListDTO.setAttachmentId(documentCenterLists.get(i).getAttachmentId());
                documentCenterListDTO.setDocumentCodeId(documentCenterLists.get(i).getDocumentCodeId());
                documentCenterListDTO.setName(documentCenterLists.get(i).getName());
                documentCenterListDTO.setReleaseDateCode(documentCenterLists.get(i).getReleaseDateCode());
                documentCenterListDTO.setRemarks(documentCenterLists.get(i).getRemarks());
                documentCenterListDTO.setSerialNumber(documentCenterLists.get(i).getSerialNumber());
                documentCenterListDTO.setSubmitterId(documentCenterLists.get(i).getSubmitterId().getId());
                documentCenterListDTO.setSubmitterName(documentCenterLists.get(i).getSubmitterId().getName());
                documentCenterListDTOList.add(documentCenterListDTO);
            }
            map.put("data",documentCenterListDTOList);
        }
        return map;
    }

    private List<DocumentCenterList> getShowData( Long treeId) throws Exception{
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

    private List<Long> recursionDeptId(Department department,List<Long> deptIdList) throws Exception{
        if(department.getId() != null){
            deptIdList.add(department.getId());
            if(department.getParentId() != null){
                recursionDeptId(department.getParentId(),deptIdList);
            }
        }
        return deptIdList;
    }


    @SystemService(args = "documentTreeId,documentTypeId")
    public void chooseDocumentType(Long documentTreeId, Long documentTypeId) throws Exception{
        Optional<DocumentCenterTree> documentCenterTree = DocumentCenterTree.stream().where(data -> data.getId().equals(documentTreeId)).findFirst();
        Optional<DocumentTypeManage> documentTypeManage = DocumentTypeManage.stream().where(data -> data.getId().equals(documentTypeId)).findFirst();
        String param = "\"" + documentTreeId + "\"";
        Optional<DocumentTypeManage> repeatDocumentTypeManage = DocumentTypeManage.stream().where(data -> !data.getId().equals(documentTypeId) && data.getCorrespondingDocument().contains(param)).findFirst();

        if(documentCenterTree.isPresent() && documentTypeManage.isPresent()){
            String correspondingDocument = documentTypeManage.get().getCorrespondingDocument();
            String documentChangedId;
            DocumentTypeManage dtm = documentTypeManage.get();
            if(!StringUtils.isNoneBlank(correspondingDocument)){
                documentChangedId = "[\"" + documentTreeId + "\",\"\"]";
            }else{
//                String [] documentCodeIdArray = correspondingDocument.substring(2,correspondingDocument.length() - 2).split("\",\"");
//                for(int i = 0,size = documentCodeIdArray.length;i < size;i++){
//                    if(documentCodeIdArray[i].equals(documentTreeId.toString())){
//                        return;
//                    }
//                }
//                documentChangedId = correspondingDocument.replace("\"\"","\"" + documentTreeId + "\",\"\"");

                documentChangedId = correspondingDocument.replace("[","[\"" + documentTreeId + "\",");
            }
            if(repeatDocumentTypeManage.isPresent()){
                DocumentTypeManage repeatDtm = repeatDocumentTypeManage.get();
                String document = repeatDocumentTypeManage.get().getCorrespondingDocument();
                String replaceDocument = document.replace("\"" + documentTreeId + "\"","");
                if(replaceDocument.charAt(1) == ','){
                    char [] charArray = replaceDocument.toCharArray();
                    String newResult = "";
                    for(int i = 0;i < charArray.length;i++){
                        if(i == 1){
                            continue;
                        }
                        newResult = newResult + charArray[i];
                    }
                    replaceDocument = newResult;
                }


                if(replaceDocument.length() > 4 && replaceDocument.charAt(replaceDocument.length() -4) == ',' && replaceDocument.charAt(replaceDocument.length() -5) == ','){

                    char [] charArray = replaceDocument.toCharArray();
                    String newResult = "";
                    for(int i = 0;i < charArray.length;i++){
                        if(i == charArray.length - 4){
                            continue;
                        }
                        newResult = newResult + charArray[i];
                    }
                    replaceDocument = newResult;
                }
                repeatDtm.setCorrespondingDocument(replaceDocument.length() == 4 ? "" : replaceDocument);
                this.repository().add(repeatDtm);

            }

            dtm.setCorrespondingDocument(documentChangedId);
            dtm.setId(documentTypeId);

            this.repository().add(dtm);
        }
    }

    @SystemService(args = "id,nodeId")
    public boolean validateTreeNodeSelect(Long id,String nodeId) throws Exception{
        boolean flag = false;
        Optional<DocumentTypeManage> documentTypeManage = DocumentTypeManage.stream().where(data -> !data.getId().equals(id == null ? -1 : id) && data.getCorrespondingDocument().contains("\"" + nodeId + "\"")).findFirst();
        if(documentTypeManage.isPresent()){
            flag = true;
        }
        return flag;
    }
}
