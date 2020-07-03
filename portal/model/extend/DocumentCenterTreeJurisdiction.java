package com.dxn.model.extend;


import com.dxn.dto.DocumentCenterTreeDTO;
import com.dxn.forms.dto.TreeDTO;
import com.dxn.model.entity.DocumentCenterTreeJurisdictionEntity;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.context.AppContext;
import org.apache.commons.beanutils.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

@Entity
@Table(name = "PM_DocumentCenterTreeJurisdiction")
public class DocumentCenterTreeJurisdiction extends DocumentCenterTreeJurisdictionEntity {
    @SystemService(args = "belongId")
    public List<DocumentCenterTreeDTO> queryTreeJurisdiction(Long belongId) throws Exception{
        List<DocumentCenterTreeDTO> listJurisdictions = new ArrayList<>();
        //Optional<DocumentCenterTree> optionalDocumentCenterTree = DocumentCenterTree.stream().where(data ->data.getId().equals(belongId)).findFirst();
        List<DocumentCenterTreeJurisdiction> documentCenterTreeJurisdictionList = DocumentCenterTreeJurisdiction.stream().where(data -> data.getDocumentCenterTreeId().getId().equals(belongId)).toList();

        for(int i = 0,size = documentCenterTreeJurisdictionList.size();i < size;i++){
            DocumentCenterTreeDTO documentCenterTreeDTO = new DocumentCenterTreeDTO();
            documentCenterTreeDTO.setId(documentCenterTreeJurisdictionList.get(i).getId());
            documentCenterTreeDTO.setClassification(documentCenterTreeJurisdictionList.get(i).getClassification());
            documentCenterTreeDTO.setDocumentCenterTreeId(documentCenterTreeJurisdictionList.get(i).getDocumentCenterTreeId().getId());
            documentCenterTreeDTO.setDownloadViewPermission(documentCenterTreeJurisdictionList.get(i).getDownloadViewPermission());
            documentCenterTreeDTO.setSelectedId(documentCenterTreeJurisdictionList.get(i).getSelectedId());
            documentCenterTreeDTO.setSelectedName(documentCenterTreeJurisdictionList.get(i).getSelectedName());
            listJurisdictions.add(documentCenterTreeDTO);
        }
        return listJurisdictions;
    }

    @SystemService(args = "belongId")
    public void insertData(Long belongId) throws Exception{
        Optional<DocumentCenterTree> optionalDocumentCenterTree = DocumentCenterTree.stream().where(data ->data.getId().equals(belongId)).findFirst();
        List<DocumentCenterTreeJurisdiction> documentCenterTreeJurisdictionList = DocumentCenterTreeJurisdiction.stream().where(data -> data.getDocumentCenterTreeId().getId().equals(belongId)).toList();


        if(documentCenterTreeJurisdictionList.size() == 0){ //如果当前节点不存在数据，则递归查找上级数据
            documentCenterTreeJurisdictionList = queryParentData(optionalDocumentCenterTree.get().getParentId().getId());
            for(int i = 0,size = documentCenterTreeJurisdictionList.size();i < size;i++){
                DocumentCenterTreeJurisdiction documentCenterTreeJurisdiction = DocumentCenterTreeJurisdiction.createNew();
                BeanUtils.copyProperties(documentCenterTreeJurisdiction,documentCenterTreeJurisdictionList.get(i));
                documentCenterTreeJurisdiction.setId(null);
                documentCenterTreeJurisdiction.setDocumentCenterTree(belongId);
                this.repository().add(documentCenterTreeJurisdiction);

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

    @SystemService(args = "selectedId,checked")
    public void updateButtonStatus(Long selectedId, Boolean checked) throws Exception{
        Optional<DocumentCenterTreeJurisdiction> optionalDocumentCenterTreeJurisdiction = DocumentCenterTreeJurisdiction.stream().where(data -> data.getId().equals(selectedId)).findFirst();
        optionalDocumentCenterTreeJurisdiction.get().setDownloadViewPermission(checked);
        this.repository().add(optionalDocumentCenterTreeJurisdiction.get());
    }

    @SystemService
    public List<TreeDTO> queryTree() throws Exception{
        List<TreeDTO> resultList = new ArrayList<>();
        List<DocumentCenterTree> dataList = new ArrayList<>();
        Set<Long> idSet = new HashSet<>();

        Long userId = AppContext.current().getProfile().getId();

        if(AppContext.current().getProfile().getIsSuperAdmin()){
            dataList = DocumentCenterTree.stream().toList();
            for(int i = 0,size = dataList.size();i < size;i++){
                idSet.add(dataList.get(i).getId());
            }

        }else{

            Optional<DocumentCenterTree> optionalDocumentCenterTree = DocumentCenterTree.stream().where(data ->data.getParentId().getId() == null).findFirst();
             idSet.add(optionalDocumentCenterTree.get().getId()); //默认添加根节点

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

            List<DocumentCenterTreeButtonJurisdiction> documentCenterTreeButtonJurisdictions = DocumentCenterTreeButtonJurisdiction.stream().where(data -> longList.contains(data.getSelectedId())).toList();

            List<DocumentCenterTreeJurisdiction> documentCenterTreeJurisdictionList = DocumentCenterTreeJurisdiction.stream().where(data -> longList.contains(data.getSelectedId())).toList();

            List<DocumentCenterListJurisdiction> listJurisdictions = DocumentCenterListJurisdiction.stream().where(data -> longList.contains(data.getSelectedId()) && data.getDownloadViewPermission().equals(true)).toList();

            for(int i = 0 ,size = documentCenterTreeButtonJurisdictions.size();i < size;i++){
                handle(idSet,documentCenterTreeButtonJurisdictions.get(i).getDocumentCenterTreeId().getId());
            }
            for(int i = 0 ,size = documentCenterTreeJurisdictionList.size();i < size;i++){
                handle(idSet,documentCenterTreeJurisdictionList.get(i).getDocumentCenterTreeId().getId());
            }

            for(int i = 0 ,size = listJurisdictions.size();i < size;i++){
                Long centerListId = listJurisdictions.get(i).getDocumentCenterListId().getId();
                Optional<DocumentCenterList> optionalDocumentCenterList = DocumentCenterList.stream().where(data -> data.getId().equals(centerListId)).findFirst();
                Long treeId = optionalDocumentCenterList.get().getDocumentCodeId();
                handle(idSet,treeId);

            }




//            dataList = DocumentCenterTree.stream().where(data -> idSet.contains(data.getId())).toList();


        }


//        for(int i = 0,size = dataList.size();i < size;i++){
//            TreeDTO treeDTO = new TreeDTO();
//            treeDTO.setId(dataList.get(i).getId().toString());
//            treeDTO.setTimestamp(dataList.get(i).getTimestamp());
//            treeDTO.setText(dataList.get(i).getName());
//            treeDTO.setParentId(dataList.get(i).getParentId() == null ? null : dataList.get(i).getParentId().toString());
//            treeDTO.setState(dataList.get(i).getState());
//            treeDTO.setSort(dataList.get(i).getSort());
//            treeDTO.setTreeName(dataList.get(i).getTreeName());
//            treeDTO.setTreeCode(dataList.get(i).getTreeCode());
//            treeDTO.setTreeName(dataList.get(i).getTreeName());
//
//
//            resultList.add(treeDTO);
//        }
 //       Optional<DocumentCenterTree> optionalDocumentCenterTree = DocumentCenterTree.stream().where(data -> Framework.isNullOrEmpty(data.getParentId().getId())).findFirst();


//        TreeDTO treeDTO = new TreeDTO();
//        treeDTO.setId(optionalDocumentCenterTree.get().getId().toString());
//        treeDTO.setTimestamp(optionalDocumentCenterTree.get().getTimestamp());
//        treeDTO.setText(optionalDocumentCenterTree.get().getName());
//        treeDTO.setParentId(optionalDocumentCenterTree.get().getParentId() == null ? null : optionalDocumentCenterTree.get().getParentId().toString());
//        treeDTO.setState(optionalDocumentCenterTree.get().getState());
//        treeDTO.setSort(optionalDocumentCenterTree.get().getSort());
//        treeDTO.setTreeName(optionalDocumentCenterTree.get().getTreeName());
//        treeDTO.setTreeCode(optionalDocumentCenterTree.get().getTreeCode());
//        treeDTO.setTreeName(optionalDocumentCenterTree.get().getTreeName());
//        treeDTO.setChildren(recursionTree(idSet,Long.parseLong(treeDTO.getId())));
        return recursionTree(idSet,null);
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

    private List<TreeDTO> recursionTree(Set<Long> idSet,Long parentId) throws Exception{
        List<DocumentCenterTree> documentCenterTreeList = parentId == null ? DocumentCenterTree.stream().where(data -> data.getParentId().getId() == null).toList() : DocumentCenterTree.stream().where(data -> data.getParentId().getId().equals(parentId)).toList();

        List<TreeDTO> treeDTOList = new ArrayList();
        if(documentCenterTreeList.size() > 0){
            for(int i = 0,size = documentCenterTreeList.size();i < size ;i++){
                if(idSet.contains(documentCenterTreeList.get(i).getId())) {
                    TreeDTO tDTO = new TreeDTO();
                    tDTO.setId(documentCenterTreeList.get(i).getId().toString());
                    tDTO.setTimestamp(documentCenterTreeList.get(i).getTimestamp());
                    tDTO.setText(documentCenterTreeList.get(i).getName());
                    tDTO.setParentId(documentCenterTreeList.get(i).getParentId() == null ? null : documentCenterTreeList.get(i).getParentId().toString());
                    tDTO.setState(documentCenterTreeList.get(i).getState());
                    tDTO.setSort(documentCenterTreeList.get(i).getSort());
                    tDTO.setTreeName(documentCenterTreeList.get(i).getTreeName());
                    tDTO.setTreeCode(documentCenterTreeList.get(i).getTreeCode());
                    tDTO.setTreeName(documentCenterTreeList.get(i).getTreeName());
                    tDTO.setChildren(recursionTree(idSet, documentCenterTreeList.get(i).getId()));
                    treeDTOList.add(tDTO);
                }
            }
        }


        return treeDTOList;
    }

    private void handle(Set<Long> idSet,Long treeId) throws Exception{
//        List<DocumentCenterTreeButtonJurisdiction> listCenterTree = DocumentCenterTreeButtonJurisdiction.stream().where(data -> data.getDocumentCenterTreeId().getId().equals(treeId)).toList();
//        List<DocumentCenterTreeJurisdiction> documentCenterTreeJurisdictionList = DocumentCenterTreeJurisdiction.stream().where(data -> data.getDocumentCenterTreeId().getId().equals(treeId)).toList();
//
//        if(listCenterTree.size() ==  0 && documentCenterTreeJurisdictionList.size() == 0){
        Optional<DocumentCenterTree> optionalDocumentCenterTree = DocumentCenterTree.stream().where(data -> data.getId().equals(treeId)).findFirst();
        if(optionalDocumentCenterTree.isPresent() && optionalDocumentCenterTree.get().getParentId() != null){
            idSet.add(treeId);
            handle(idSet,optionalDocumentCenterTree.get().getParentId().getId());
        }

        //}

    }

    @SystemService(args = "dataId")
    public void deleteData(Long dataId) throws Exception{
        if(dataId != null){
            Optional<DocumentCenterTreeJurisdiction> optionalDocumentCenterTreeJurisdiction = DocumentCenterTreeJurisdiction.stream().where(data ->data.getId().equals(dataId)).findFirst();
            if(optionalDocumentCenterTreeJurisdiction.isPresent()){
                DocumentCenterTreeJurisdiction documentCenterTreeJurisdiction = DocumentCenterTreeJurisdiction.createNew();
                documentCenterTreeJurisdiction.setId(dataId);
                this.repository().remove(documentCenterTreeJurisdiction);
            }
        }


    }


    @SystemService(args = "dataId")
    public Map<String,String> ManageOrNot(Long dataId) throws Exception{
        Long userId = AppContext.current().getProfile().getId();
        Map<String,String> resultMap = new HashMap();

        if(AppContext.current().getProfile().getIsSuperAdmin()){
            resultMap.put("admin","admin");
        }else{
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




            Optional<DocumentCenterList> optionalDocumentCenterList = DocumentCenterList.stream().where(data -> data.getId().equals(dataId)).findFirst();
            if(optionalDocumentCenterList.isPresent()){
                Long treeNodeId = optionalDocumentCenterList.get().getDocumentCodeId();
                List<DocumentCenterTreeButtonJurisdiction> documentCenterTreeButtonJurisdictions = DocumentCenterTreeButtonJurisdiction.stream().where(data -> longList.contains(data.getSelectedId()) && data.getDocumentCenterTreeId().getId().equals(treeNodeId)).toList();
                if(documentCenterTreeButtonJurisdictions != null && documentCenterTreeButtonJurisdictions.size() > 0){
                    resultMap.put("downLookButton","true");
                }
            }

        }

        return resultMap;

    }
}
