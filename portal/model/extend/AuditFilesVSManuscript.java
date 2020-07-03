package com.dxn.model.extend;

import com.dxn.dto.AuditFilesDTO;
import com.dxn.forms.dto.UIPagedList;
import com.dxn.model.entity.AuditFilesVSManuscriptEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Prj_AuditFilesVSManuscript")
public class AuditFilesVSManuscript extends AuditFilesVSManuscriptEntity {

    @SystemService(args = "query_auditworkProjectID,query_manuscriptitemID,query_belongedToGroupID,query_syh")
    public  List<AuditFilesDTO> getAuditFilesList(String auditworkProjectID, String manuscriptitemID, String belongedToGroupID,String syh) throws Exception
    {
        List<AuditFilesDTO> itemList =new ArrayList<>();
        if(Framework.isNotNullOrEmpty(manuscriptitemID)) //第一层 查的是底稿对应附件表
        {
            List<Object> p = new ArrayList<>();

            p.add(auditworkProjectID);
            p.add(manuscriptitemID);
            itemList=repository().sqlQuery("select am.FilesIndexNOManuscriptSheetIndexNO + '-'+cast(am.FilesIndexNOManuscriptSheetSerialNO as varchar(20) ) as 'syh',\n" +
                    " a.uuid as 'GroupID',a.fileSourceName,a.isgroup,a.filePath,manuscriptitemID,a.auditworkProjectID,a.fileNewName\n" +
                    " from Prj_AuditFilesVSManuscript am,Prj_AuditFiles a" +
                    " where am.auditworkProjectID=?\n" +
                    " and am.manuscriptitemID =? and am.auditworkProjectID = a.auditworkProjectID\n" +
                    " and am.auditFilesID =a.uuid\n" +
                    " order by a.sortNO,am.FilesIndexNOManuscriptSheetSerialNO ",p,AuditFilesDTO.class);
        }else
        {
            List<Object> p = new ArrayList<>();
            p.add(syh);
            p.add(belongedToGroupID);
            itemList=repository().sqlQuery("select ? +'-'+ cast(ROW_NUMBER() OVER ( ORDER BY a.sortNO ) as varchar(20)) AS 'syh',  \n" +
                    " a.uuid as 'GroupID',a.fileSourceName,a.isgroup,a.filePath,'' as 'manuscriptitemID',a.auditworkProjectID\n" +
                    " from Prj_AuditFiles a\n" +
                    " where belongedToGroupID=?",p,AuditFilesDTO.class);

        }

        return itemList;

    }



}
