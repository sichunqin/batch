package com.dxn.model.extend;

import com.dxn.dto.DocumentCenterTreeDTO;
import com.dxn.model.entity.DocumentCenterTreeButtonJurisdictionEntity;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.context.AppContext;
import com.itextpdf.text.ExceptionConverter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

@Entity
@Table(name = "PM_DocumentCenterTreeButtonJurisdiction")
public class DocumentCenterTreeButtonJurisdiction extends DocumentCenterTreeButtonJurisdictionEntity {
    @SystemService(args = "belongId")
    public List<DocumentCenterTreeDTO> queryManageData(Long belongId) throws Exception{
        List<DocumentCenterTreeDTO> documentCenterTreeDTOList = new ArrayList();
        List<DocumentCenterTreeButtonJurisdiction> documentCenterTreeButtonJurisdictionList = DocumentCenterTreeButtonJurisdiction.stream().where(data -> data.getDocumentCenterTreeId().getId().equals(belongId)).toList();
        for(int i = 0,size = documentCenterTreeButtonJurisdictionList.size();i < size;i++){
            DocumentCenterTreeDTO documentCenterTreeDTO = new DocumentCenterTreeDTO();
            documentCenterTreeDTO.setId(documentCenterTreeButtonJurisdictionList.get(i).getId());
            documentCenterTreeDTO.setClassification(documentCenterTreeButtonJurisdictionList.get(i).getClassification());
            documentCenterTreeDTO.setDocumentCenterTreeId(documentCenterTreeButtonJurisdictionList.get(i).getDocumentCenterTreeId().getId());
            documentCenterTreeDTO.setWholeButton(documentCenterTreeButtonJurisdictionList.get(i).getWholeButton());
            documentCenterTreeDTO.setAddButton(documentCenterTreeButtonJurisdictionList.get(i).getAddButton());
            documentCenterTreeDTO.setEditButton(documentCenterTreeButtonJurisdictionList.get(i).getEditButton());
            documentCenterTreeDTO.setDeleteButton(documentCenterTreeButtonJurisdictionList.get(i).getDeleteButton());
            documentCenterTreeDTO.setSelectedId(documentCenterTreeButtonJurisdictionList.get(i).getSelectedId());
            documentCenterTreeDTO.setSelectedName(documentCenterTreeButtonJurisdictionList.get(i).getSelectedName());
            documentCenterTreeDTOList.add(documentCenterTreeDTO);
        }
        return documentCenterTreeDTOList;

    }


    @SystemService(args = "selectedId,type,jurisdictionName,jurisdictionId,treeOrGrid")
    public void savePermission(Long selectedId, Integer type, String jurisdictionName, String jurisdictionId, String treeOrGrid) throws Exception {
        String[] nameArray = jurisdictionName.split(",");
        String[] idArray = jurisdictionId.split(",");           // 在文档列表设置权限
        for (int i = 0, size = nameArray.length; i < size; i++) {
            long jId = Long.parseLong(idArray[i]);
            Optional<DocumentCenterTreeButtonJurisdiction> optionalDocumentCenterTreeButtonJurisdiction = DocumentCenterTreeButtonJurisdiction.stream().where(data -> data.getDocumentCenterTreeId().getId().equals(selectedId) && data.getSelectedId() == jId).findFirst();
            if(optionalDocumentCenterTreeButtonJurisdiction.isPresent()){
                continue;
            }
            DocumentCenterTreeButtonJurisdiction documentCenterTreeButtonJurisdiction = DocumentCenterTreeButtonJurisdiction.createNew();
            documentCenterTreeButtonJurisdiction.setSelectedId(Long.parseLong(idArray[i]));
            documentCenterTreeButtonJurisdiction.setSelectedName(nameArray[i]);
            documentCenterTreeButtonJurisdiction.setDocumentCenterTree(selectedId);
            documentCenterTreeButtonJurisdiction.setClassification(type);
            documentCenterTreeButtonJurisdiction.setWholeButton(true);
            documentCenterTreeButtonJurisdiction.setAddButton(true);
            documentCenterTreeButtonJurisdiction.setEditButton(true);
            documentCenterTreeButtonJurisdiction.setDeleteButton(true);
            this.repository().add(documentCenterTreeButtonJurisdiction);

            Long selectedUserId = Long.parseLong(idArray[i]);
            Optional<DocumentCenterTreeJurisdiction> optionalDocumentCenterTreeJurisdiction = DocumentCenterTreeJurisdiction.stream().where(data -> data.getSelectedId() == selectedUserId && data.getDocumentCenterTreeId().getId().equals(selectedId)).findFirst();
            if(!optionalDocumentCenterTreeJurisdiction.isPresent()){
                DocumentCenterTreeJurisdiction documentCenterTreeJurisdiction = DocumentCenterTreeJurisdiction.createNew();
                documentCenterTreeJurisdiction.setSelectedId(Long.parseLong(idArray[i]));
                documentCenterTreeJurisdiction.setSelectedName(nameArray[i]);
                documentCenterTreeJurisdiction.setDocumentCenterTree(selectedId);
                documentCenterTreeJurisdiction.setClassification(type);
                documentCenterTreeJurisdiction.setDownloadViewPermission(true);
                this.repository().add(documentCenterTreeJurisdiction);
            }
        }
     }

    @SystemService(args = "selectedId,checked,buttonType")
    public void updateButtonStatus(Long selectedId, Boolean checked,String buttonType) throws Exception{
        Optional<DocumentCenterTreeButtonJurisdiction> optionalDocumentCenterTreeButtonJurisdiction = DocumentCenterTreeButtonJurisdiction.stream().where(data -> data.getId().equals(selectedId)).findFirst();
        if("whole".equals(buttonType)){
            optionalDocumentCenterTreeButtonJurisdiction.get().setWholeButton(checked);
            optionalDocumentCenterTreeButtonJurisdiction.get().setAddButton(checked);
            optionalDocumentCenterTreeButtonJurisdiction.get().setEditButton(checked);
            optionalDocumentCenterTreeButtonJurisdiction.get().setDeleteButton(checked);
        }else if("add".equals(buttonType)){
            optionalDocumentCenterTreeButtonJurisdiction.get().setAddButton(checked);
            if(optionalDocumentCenterTreeButtonJurisdiction.get().getEditButton().equals(checked) && optionalDocumentCenterTreeButtonJurisdiction.get().getDeleteButton().equals(checked)){
                optionalDocumentCenterTreeButtonJurisdiction.get().setWholeButton(checked);

            }
            if(!optionalDocumentCenterTreeButtonJurisdiction.get().getEditButton().equals(checked) || !optionalDocumentCenterTreeButtonJurisdiction.get().getDeleteButton().equals(checked)){
                optionalDocumentCenterTreeButtonJurisdiction.get().setWholeButton(false);
            }
        }else if("edit".equals(buttonType)){
            optionalDocumentCenterTreeButtonJurisdiction.get().setEditButton(checked);
            if(optionalDocumentCenterTreeButtonJurisdiction.get().getAddButton().equals(checked) && optionalDocumentCenterTreeButtonJurisdiction.get().getDeleteButton().equals(checked)){
                optionalDocumentCenterTreeButtonJurisdiction.get().setWholeButton(checked);

            }
            if(!optionalDocumentCenterTreeButtonJurisdiction.get().getAddButton().equals(checked) || !optionalDocumentCenterTreeButtonJurisdiction.get().getDeleteButton().equals(checked)){
                optionalDocumentCenterTreeButtonJurisdiction.get().setWholeButton(false);
            }
        }else if("delete".equals(buttonType)){
            optionalDocumentCenterTreeButtonJurisdiction.get().setDeleteButton(checked);
            if(optionalDocumentCenterTreeButtonJurisdiction.get().getAddButton().equals(checked) && optionalDocumentCenterTreeButtonJurisdiction.get().getEditButton().equals(checked)){
                optionalDocumentCenterTreeButtonJurisdiction.get().setWholeButton(checked);

            }
            if(!optionalDocumentCenterTreeButtonJurisdiction.get().getAddButton().equals(checked) || !optionalDocumentCenterTreeButtonJurisdiction.get().getEditButton().equals(checked)){
                optionalDocumentCenterTreeButtonJurisdiction.get().setWholeButton(false);
            }
        }

        this.repository().add(optionalDocumentCenterTreeButtonJurisdiction.get());
    }

    @SystemService(args = "nodeId,type")
    public Map<String,String> buttonShowOrNot(Long nodeId,String type) throws Exception{
        Long userId = AppContext.current().getProfile().getId();

        Map<String,String> buttonMap = new HashMap();
        if(AppContext.current().getProfile().getIsSuperAdmin()){
            buttonMap.put("admin","admin");

        }else{

            Set<Long> idSet = new HashSet<>();
            List<Long> longList = selectedIdList(userId);

            List<DocumentCenterTreeButtonJurisdiction> documentCenterTreeButtonJurisdictionList = DocumentCenterTreeButtonJurisdiction.stream().where(data -> longList.contains(data.getSelectedId()) && data.getDocumentCenterTreeId().getId().equals(nodeId)).toList();


            if("tree".equals(type)){
                if(documentCenterTreeButtonJurisdictionList.size() > 0){
                    buttonMap.put("choosetype","true");
                    buttonMap.put("downOrLook","true");
                }

            }else if("grid".equals(type)){
                for(int i = 0 ,size = documentCenterTreeButtonJurisdictionList.size();i < size;i++) {

                    if (documentCenterTreeButtonJurisdictionList.get(i).getAddButton().equals(true) && !buttonMap.containsKey("add")) {
                        buttonMap.put("add", "true");
                    }
                    if (documentCenterTreeButtonJurisdictionList.get(i).getEditButton().equals(true) && !buttonMap.containsKey("edit")) {
                        buttonMap.put("edit", "true");
                    }
                    if (documentCenterTreeButtonJurisdictionList.get(i).getDeleteButton().equals(true) && !buttonMap.containsKey("delete")) {
                        buttonMap.put("delete", "true");
                    }
                }

            }

        }

        return buttonMap;
    }


    private List<Long> selectedIdList(Long userId) throws Exception{            //獲取當前登錄人所屬分類信息

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

        return longList;
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
        if(dataId != null){
            Optional<DocumentCenterTreeButtonJurisdiction> optionalDocumentCenterTreeButtonJurisdiction = DocumentCenterTreeButtonJurisdiction.stream().where(data ->data.getId().equals(dataId)).findFirst();
            if(optionalDocumentCenterTreeButtonJurisdiction.isPresent()){
                DocumentCenterTreeButtonJurisdiction documentCenterTreeButtonJurisdiction = DocumentCenterTreeButtonJurisdiction.createNew();
                documentCenterTreeButtonJurisdiction.setId(dataId);
                this.repository().remove(documentCenterTreeButtonJurisdiction);
            }
        }

    }

    @SystemService(args = "dataId")
    public  Map<String,String> innerButtonControl(Long dataId) throws Exception{
        Map<String,String> buttonMap = new HashMap();
        Long userId = AppContext.current().getProfile().getId();
        if(AppContext.current().getProfile().getIsSuperAdmin()){
            buttonMap.put("admin","admin");

        }else{
            Optional<DocumentCenterList> optionalDocumentCenterList = DocumentCenterList.stream().where(data -> data.getId().equals(dataId)).findFirst();
            if(optionalDocumentCenterList.isPresent()){
                List<Long> idList = selectedIdList(userId);
                Long treeNodeId = optionalDocumentCenterList.get().getDocumentCodeId();
                List<DocumentCenterTreeButtonJurisdiction> documentCenterTreeButtonJurisdictionList = DocumentCenterTreeButtonJurisdiction.stream().where(data -> idList.contains(data.getSelectedId()) && data.getDocumentCenterTreeId().getId().equals(treeNodeId)).toList();
                for(int i = 0,size = documentCenterTreeButtonJurisdictionList.size();i < size;i++){
                    if(documentCenterTreeButtonJurisdictionList.get(i).getEditButton().equals(true) && !buttonMap.containsKey("edit")){
                        buttonMap.put("edit","true");
                    }
                }

            }
        }

        return buttonMap;
    }
}
