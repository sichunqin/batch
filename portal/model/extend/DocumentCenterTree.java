package com.dxn.model.extend;

import com.dxn.model.entity.DocumentCenterTreeEntity;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "PM_DocumentCenterTree")
public class DocumentCenterTree extends DocumentCenterTreeEntity {
    @SystemService(args = "id")
    public void deleteTreeCode(Long id) throws Exception{
        if(id != null){
            Optional<DocumentCenterTree> documentCenterTree = DocumentCenterTree.stream().where(data -> data.getId().equals(id)).findFirst();
            List<DocumentCenterTree> allNode = new ArrayList();
            List<DocumentCenterList> documentList = new ArrayList();
            List<Attachment> fileList = new ArrayList();
            List<DocumentCenterTreeJurisdiction> documentCenterTreeJurisdictionList = new ArrayList();
            List<DocumentCenterTreeButtonJurisdiction> documentCenterTreeButtonJurisdictionList = new ArrayList();
            List<DocumentCenterListJurisdiction> documentCenterListJurisdictions = new ArrayList();
            if(documentCenterTree.isPresent()){
                allNode.add(documentCenterTree.get());
                Long nodeId = documentCenterTree.get().getId();
                List<DocumentCenterTreeJurisdiction> treeJurisdictionList = DocumentCenterTreeJurisdiction.stream().where(data -> data.getDocumentCenterTreeId().getId().equals(nodeId)).toList();
                documentCenterTreeJurisdictionList.addAll(treeJurisdictionList);
                List<DocumentCenterTreeButtonJurisdiction> treeButtonJurisdiction = DocumentCenterTreeButtonJurisdiction.stream().where(data -> data.getDocumentCenterTreeId().getId().equals(nodeId)).toList();
                documentCenterTreeButtonJurisdictionList.addAll(treeButtonJurisdiction);
                List<DocumentCenterList> childDocumentDataList = DocumentCenterList.stream().where(data -> data.getDocumentCodeId().equals(nodeId)).toList();
                documentList.addAll(childDocumentDataList);
                for(DocumentCenterList item : childDocumentDataList){
                    Long documentId = item.getId();
                    List<DocumentCenterListJurisdiction> centerListJurisdiction = DocumentCenterListJurisdiction.stream().where(data -> data.getDocumentCenterListId().getId().equals(documentId)).toList();
                    documentCenterListJurisdictions.addAll(centerListJurisdiction);
                    Long attachmentId = item.getAttachmentId();
                    Optional<Attachment> file = Attachment.stream().where(data -> data.getId().equals(attachmentId)).findFirst();
                    if(file.isPresent()){
                        fileList.add(file.get());
                    }
                }

                recursiveFunction(allNode,documentList,fileList,documentCenterTree.get().getChildren(),documentCenterTreeJurisdictionList,documentCenterTreeButtonJurisdictionList,documentCenterListJurisdictions);
            }
//            for(DocumentCenterTreeJurisdiction item: documentCenterTreeJurisdictionList){
//                this.repository().remove(item);
//            }
//            for(DocumentCenterTreeButtonJurisdiction item : documentCenterTreeButtonJurisdictionList){
//                this.repository().remove(item);
//            }
//            for(DocumentCenterListJurisdiction item : documentCenterListJurisdictions){
//                this.repository().remove(item);
//            }
            for(DocumentCenterTree item : allNode){
                this.repository().remove(item);
            }
            for(DocumentCenterList item : documentList){
                this.repository().remove(item);
            }
            for(Attachment item : fileList){
                this.repository().remove(item);
            }
        }

    }

    private void recursiveFunction(List<DocumentCenterTree> allNode, List<DocumentCenterList> documentList,List<Attachment> fileList,List<DocumentCenterTree> dataList,List<DocumentCenterTreeJurisdiction> documentCenterTreeJurisdictionList,List<DocumentCenterTreeButtonJurisdiction> documentCenterTreeButtonJurisdictionList, List<DocumentCenterListJurisdiction> documentCenterListJurisdictions) throws  Exception{

        if(dataList != null && dataList.size() > 0){
            for(int i = 0,size = dataList.size();i < size;i++){
                allNode.add(dataList.get(i));
                Long nodeId = dataList.get(i).getId();
                List<DocumentCenterList> childDocumentDataList = DocumentCenterList.stream().where(data -> data.getDocumentCodeId().equals(nodeId)).toList();
                documentList.addAll(childDocumentDataList);
                List<DocumentCenterTreeJurisdiction> treeJurisdictionList = DocumentCenterTreeJurisdiction.stream().where(data -> data.getDocumentCenterTreeId().getId().equals(nodeId)).toList();
                documentCenterTreeJurisdictionList.addAll(treeJurisdictionList);
                List<DocumentCenterTreeButtonJurisdiction> treeButtonJurisdiction = DocumentCenterTreeButtonJurisdiction.stream().where(data -> data.getDocumentCenterTreeId().getId().equals(nodeId)).toList();
                documentCenterTreeButtonJurisdictionList.addAll(treeButtonJurisdiction);
                for(int j = 0,childSize = childDocumentDataList.size();j < childSize;j++){
                    Long documentId = childDocumentDataList.get(j).getId();
                    List<DocumentCenterListJurisdiction> centerListJurisdiction = DocumentCenterListJurisdiction.stream().where(data -> data.getDocumentCenterListId().getId().equals(documentId)).toList();
                    documentCenterListJurisdictions.addAll(centerListJurisdiction);
                    Long attachmentId = childDocumentDataList.get(j).getAttachmentId();
                    Optional<Attachment> file = Attachment.stream().where(data -> data.getId().equals(attachmentId)).findFirst();
                    if(file.isPresent()){
                        fileList.add(file.get());
                    }
                }
                recursiveFunction(allNode,documentList,fileList,dataList.get(i).getChildren(),documentCenterTreeJurisdictionList,documentCenterTreeButtonJurisdictionList,documentCenterListJurisdictions);
            }
        }
    }


}
