package com.dxn.model.extend;

import com.alibaba.fastjson.JSON;
import com.dxn.dto.DocumentCenterListDTO;
import com.dxn.dto.DocumentCenterTreeDTO;
import com.dxn.model.entity.DocumentCenterListJurisdictionEntity;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.context.AppContext;
import org.apache.commons.beanutils.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.swing.text.Document;
import java.util.*;

@Entity
@Table(name = "PM_DocumentCenterListJurisdiction")
public class DocumentCenterListJurisdiction extends DocumentCenterListJurisdictionEntity {
    //新增维护列表下载权限
    public void newAddMaintenance(DocumentCenterTreeJurisdiction documentCenterTreeJurisdiction) throws Exception{
        Long   documentCodeId=documentCenterTreeJurisdiction.getDocumentCenterTreeId().getId();
        Long selectedId=documentCenterTreeJurisdiction.getSelectedId();
        List<DocumentCenterList> documentCenterList =DocumentCenterList.stream().where(w->w.getDocumentCodeId()==documentCodeId).toList();
        for (DocumentCenterList item:documentCenterList){
            Long  documentCenterListId=item.getId();
            Long count=DocumentCenterListJurisdiction.stream().where(w->w.getDocumentCenterListId().getId()==documentCenterListId && w.getSelectedId()==selectedId).count();
            if (count>0){
                continue;
            }
            DocumentCenterListJurisdiction documentCenterListJurisdiction=DocumentCenterListJurisdiction.createNew();
            documentCenterListJurisdiction.setDocumentCenterList(documentCenterListId);
            documentCenterListJurisdiction.setSelectedId(documentCenterTreeJurisdiction.getSelectedId());
            documentCenterListJurisdiction.setSelectedName(documentCenterTreeJurisdiction.getSelectedName());
            documentCenterListJurisdiction.setClassification(documentCenterTreeJurisdiction.getClassification());
            documentCenterListJurisdiction.setDownloadViewPermission(true);
            this.repository().add(documentCenterListJurisdiction);
        }


    }
    //添加权限确定功能
    @SystemService(args = "selectedId,type,jurisdictionName,jurisdictionId,treeOrGrid")
    public void savePermission(Long selectedId, Integer type, String jurisdictionName, String jurisdictionId, String treeOrGrid) throws Exception {
        String[] nameArray = jurisdictionName.split(",");
        String[] idArray = jurisdictionId.split(",");

        if (treeOrGrid.equals("tree")) {// 在文档树上设置权限
            //Optional<DocumentCenterTree> optionalDocumentCenterTree = DocumentCenterTree.stream().where(data -> data.getId().equals(selectedId)).findFirst();
            //Long tree = optionalDocumentCenterTree.get().getId();
            for (int i = 0, size = nameArray.length; i < size; i++) {
                long jId = Long.parseLong(idArray[i]);
                Optional<DocumentCenterTreeJurisdiction> optionalDocumentCenterTreeJurisdiction = DocumentCenterTreeJurisdiction.stream().where(data -> data.getDocumentCenterTreeId().getId().equals(selectedId) && data.getSelectedId() == jId).findFirst();
                if(optionalDocumentCenterTreeJurisdiction.isPresent()){
                    continue;
                }
                DocumentCenterTreeJurisdiction documentCenterTreeJurisdiction = DocumentCenterTreeJurisdiction.createNew();
                documentCenterTreeJurisdiction.setSelectedId(Long.parseLong(idArray[i]));
                documentCenterTreeJurisdiction.setSelectedName(nameArray[i]);
                documentCenterTreeJurisdiction.setDocumentCenterTree(selectedId);
                documentCenterTreeJurisdiction.setClassification(type);
                documentCenterTreeJurisdiction.setDownloadViewPermission(true);
                this.repository().add(documentCenterTreeJurisdiction);
                newAddMaintenance(documentCenterTreeJurisdiction);

            }

        } else {            // 在文档列表设置权限
            //Optional<DocumentCenterList> optionalDocumentCenterList = DocumentCenterList.stream().where(data -> data.getId().equals(selectedId)).findFirst();
            //Long treeId = optionalDocumentCenterList.get().getDocumentCodeId();
            //Optional<DocumentCenterTree> optionalDocumentCenterTree =  DocumentCenterTree.stream().where(data -> data.getId().equals(treeId)).findFirst();
            for (int i = 0, size = nameArray.length; i < size; i++) {
                long jId = Long.parseLong(idArray[i]);
                Optional<DocumentCenterListJurisdiction> optionalDocumentCenterListJurisdiction = DocumentCenterListJurisdiction.stream().where(data -> data.getDocumentCenterListId().getId().equals(selectedId) && data.getSelectedId() == jId).findFirst();
                if(optionalDocumentCenterListJurisdiction.isPresent()){
                    continue;
                }
                DocumentCenterListJurisdiction documentCenterListJurisdiction = DocumentCenterListJurisdiction.createNew();
                documentCenterListJurisdiction.setSelectedId(Long.parseLong(idArray[i]));
                documentCenterListJurisdiction.setSelectedName(nameArray[i]);
                documentCenterListJurisdiction.setDocumentCenterList(selectedId);
                documentCenterListJurisdiction.setClassification(type);
                documentCenterListJurisdiction.setDownloadViewPermission(true);
                this.repository().add(documentCenterListJurisdiction);
            }
            //updateTreeNodeStatus(treeId);
        }


    }

    private void updateTreeNodeStatus(Long treeId) throws Exception {
        Optional<DocumentCenterTree> optionalDocumentCenterTree = DocumentCenterTree.stream().where(data -> data.getId().equals(treeId)).findFirst();
        DocumentCenterTree documentCenterTree = optionalDocumentCenterTree.get();
        if (documentCenterTree.getParentId() != null) {
            if (!documentCenterTree.getShowOrNot().equals("1") && !documentCenterTree.getShowOrNot().equals("2")) {
                documentCenterTree.setShowOrNot("2");//2 表示临时展示，1 表示节点分配权限
                this.repository().add(documentCenterTree);
            }
            updateTreeNodeStatus(documentCenterTree.getParentId().getId());
        } else {
            return;
        }
    }

    @SystemService(args = "selectedId,checked")
    public void updateButtonStatus(Long selectedId, Boolean checked) throws Exception{
        Optional<DocumentCenterListJurisdiction> optionalDocumentCenterListJurisdiction = DocumentCenterListJurisdiction.stream().where(data -> data.getId().equals(selectedId)).findFirst();
        optionalDocumentCenterListJurisdiction.get().setDownloadViewPermission(checked);
        this.repository().add(optionalDocumentCenterListJurisdiction.get());
    }

    @SystemService(args = "treeId")    //查询文档列表权限内容
    public List<DocumentCenterListDTO> getListData(Long treeId) throws Exception{
        Long userId = AppContext.current().getProfile().getId();
        List<DocumentCenterListDTO> documentCenterListDTOList = new ArrayList<>();
        List<DocumentCenterList> resultList = new ArrayList<>();

        if(AppContext.current().getProfile().getIsSuperAdmin()){

            resultList =  treeId == null ? DocumentCenterList.stream().toList() : DocumentCenterList.stream().where(data -> data.getDocumentCodeId().equals(treeId)).toList();

        }else{
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

           // if(idSet.size() > 0){
                if(treeId == null){
                    if(idSet.size() > 0 && manageTreeIdList.size() > 0){
                        resultList = DocumentCenterList.stream().where(data -> idSet.contains(data.getId()) || manageTreeIdList.contains(data.getDocumentCodeId())).toList();
                    }else if(idSet.size() == 0 && manageTreeIdList.size() > 0){
                        resultList = DocumentCenterList.stream().where(data -> manageTreeIdList.contains(data.getDocumentCodeId())).toList();
                    }else if(idSet.size() > 0 && manageTreeIdList.size() == 0){
                        resultList = DocumentCenterList.stream().where(data -> idSet.contains(data.getId())).toList();
                    }

                }else{
                    if(idSet.size() > 0 && manageTreeIdList.size() > 0){
                        resultList = DocumentCenterList.stream().where(data -> (idSet.contains(data.getId()) || manageTreeIdList.contains(data.getDocumentCodeId())) && data.getDocumentCodeId().equals(treeId)).toList();
                    }else if(idSet.size() == 0 && manageTreeIdList.size() > 0){
                        resultList = DocumentCenterList.stream().where(data -> manageTreeIdList.contains(data.getDocumentCodeId()) && data.getDocumentCodeId().equals(treeId)).toList();
                    }else if(idSet.size() > 0 && manageTreeIdList.size() == 0){
                        resultList = DocumentCenterList.stream().where(data -> idSet.contains(data.getId()) && data.getDocumentCodeId().equals(treeId)).toList();
                    }

                }
           // }

        }
        for(int i = 0,size = resultList.size();i < size;i++){
            DocumentCenterListDTO documentCenterListDTO = new DocumentCenterListDTO();
            documentCenterListDTO.setId(resultList.get(i).getId());
            documentCenterListDTO.setAttachmentId(resultList.get(i).getAttachmentId());
            documentCenterListDTO.setDocumentCodeId(resultList.get(i).getDocumentCodeId());
            documentCenterListDTO.setName(resultList.get(i).getName());
            documentCenterListDTO.setReleaseDateCode(resultList.get(i).getReleaseDateCode());
            documentCenterListDTO.setRemarks(resultList.get(i).getRemarks());
            documentCenterListDTO.setSerialNumber(resultList.get(i).getSerialNumber());
            documentCenterListDTO.setSubmitterId(resultList.get(i).getSubmitterId().getId());
            documentCenterListDTO.setSubmitterName(resultList.get(i).getSubmitterId().getName());
            documentCenterListDTOList.add(documentCenterListDTO);
        }
        return documentCenterListDTOList;
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
    public List<DocumentCenterTreeDTO> queryJurisdiction(Long dataId) throws Exception{
        List<DocumentCenterTreeDTO> listJurisdictionDTOS = new ArrayList<>();
        List<DocumentCenterListJurisdiction> listJurisdictions = DocumentCenterListJurisdiction.stream().where(data -> data.getDocumentCenterListId().getId().equals(dataId)).toList();
        for(int i = 0,size = listJurisdictions.size();i < size;i++){
            DocumentCenterTreeDTO documentCenterListJurisdictionDTO = new DocumentCenterTreeDTO();
            documentCenterListJurisdictionDTO.setId(listJurisdictions.get(i).getId());
            documentCenterListJurisdictionDTO.setClassification(listJurisdictions.get(i).getClassification());
            documentCenterListJurisdictionDTO.setDocumentCenterListId(listJurisdictions.get(i).getDocumentCenterListId().getId());
            documentCenterListJurisdictionDTO.setDownloadViewPermission(listJurisdictions.get(i).getDownloadViewPermission());
            documentCenterListJurisdictionDTO.setSelectedId(listJurisdictions.get(i).getSelectedId());
            documentCenterListJurisdictionDTO.setSelectedName(listJurisdictions.get(i).getSelectedName());
            listJurisdictionDTOS.add(documentCenterListJurisdictionDTO);
        }
        return listJurisdictionDTOS;
    }

    @SystemService(args = "dataId")
    public void insertData(Long dataId) throws Exception{
        Optional<DocumentCenterList> optionalDocumentCenterList = DocumentCenterList.stream().where(data ->data.getId().equals(dataId)).findFirst();
        List<DocumentCenterListJurisdiction> documentCenterListJurisdictions = DocumentCenterListJurisdiction.stream().where(data -> data.getDocumentCenterListId().getId().equals(dataId)).toList();

        if(documentCenterListJurisdictions.size() == 0){
            List<DocumentCenterTreeJurisdiction>  documentCenterTreeJurisdictionList = queryParentData(optionalDocumentCenterList.get().getDocumentCodeId());
            for(int i = 0,size = documentCenterTreeJurisdictionList.size();i < size;i++){
                DocumentCenterListJurisdiction documentCenterListJurisdiction = DocumentCenterListJurisdiction.createNew();
                BeanUtils.copyProperties(documentCenterListJurisdiction,documentCenterTreeJurisdictionList.get(i));
                documentCenterListJurisdiction.setId(null);
                documentCenterListJurisdiction.setDocumentCenterList(dataId);
                this.repository().add(documentCenterListJurisdiction);

            }

        }

    }

    private List<DocumentCenterTreeJurisdiction> queryParentData(Long treeId) throws Exception{
        Optional<DocumentCenterTree> optionalDocumentCenterTree = DocumentCenterTree.stream().where(data ->data.getId().equals(treeId)).findFirst();
        List<DocumentCenterTreeJurisdiction> documentCenterTreeJurisdictionList = DocumentCenterTreeJurisdiction.stream().where(data -> data.getDocumentCenterTreeId().getId().equals(treeId)).toList();
        if(documentCenterTreeJurisdictionList.size() == 0 && optionalDocumentCenterTree.get().getParentId() != null){
            documentCenterTreeJurisdictionList = queryParentData(optionalDocumentCenterTree.get().getParentId().getId());
        }
        return documentCenterTreeJurisdictionList;

    }

    @SystemService(args = "dataId")
    public void deleteData(Long dataId) throws Exception{
        if(dataId != null){
            Optional<DocumentCenterListJurisdiction> optionalDocumentDocumentCenterListJurisdiction = DocumentCenterListJurisdiction.stream().where(data -> data.getId().equals(dataId)).findFirst();
            if(optionalDocumentDocumentCenterListJurisdiction.isPresent()){
                DocumentCenterListJurisdiction documentCenterListJurisdiction = DocumentCenterListJurisdiction.createNew();
                documentCenterListJurisdiction.setId(dataId);
                this.repository().remove(documentCenterListJurisdiction);
            }
        }


    }
}
