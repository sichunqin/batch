package com.dxn.model.extend;

import com.dxn.dto.FileDetailedListDTO;
import com.dxn.dto.ManuscriptItemDTO;
import com.dxn.dto.WorkFlow.ManuscriptItemTreeDTO;
import com.dxn.dto.enums.Constants;
import com.dxn.forms.dto.TreeDTO;
import com.dxn.forms.dto.UIPagedList;
import com.dxn.model.entity.ManuscriptItemEntity;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.configuration.DxnConfig;
import com.dxn.system.exception.DxnException;
import com.dxn.system.io.FileHelper;
import com.dxn.util.ZipEncrypt;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jinq.tuples.Pair;
import org.jinq.tuples.Tuple3;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@Entity
@Table(name = "Prj_ManuscriptItem")
public class ManuscriptItem extends ManuscriptItemEntity {
    @Value("${dxn.file.cs-upload-path}")
    private String csUploadPath;

    /**
     * archiveDisplay 是否显示，归档后 在我的借阅显示，1显示，0不显示，2 显示，并且显示初步业务活动底稿到末级（归档生成底稿目录用到）
     *
     * @param auditworkProjectID
     * @param archiveDisplay
     * @return
     * @throws Exception
     */
    @JsonIgnore
    @SystemService(args = "query_auditworkProjectID,query_archiveDisplay")
    public List<TreeDTO> getManuscriptItemDTO(Long auditworkProjectID, String archiveDisplay) throws Exception {

        List<ManuscriptItemDTO> list;
        List<ManuscriptItemDTO> fisclYear = new ArrayList<>();
        //list = getManuscriptItemDTO1(auditworkProjectID, fisclYear); //需求改成 打开的底稿才显示
        if (Framework.isNullOrEmpty(archiveDisplay)) archiveDisplay = "0";

        Optional<CompositionCustomer> first = CompositionCustomer.stream().where(w -> w.getId().equals(auditworkProjectID)).findFirst();
        if (!first.isPresent()) return null;

        CompositionCustomer item = first.get();
        String uuid = item.getUuid();

        Long fisclYearcount = this.stream().where(w -> w.getAuditworkProjectID().equals(uuid) && w.getFiscalYear() > 0).select(s -> s.getFiscalYearInforID()).distinct().count();

        list = gainManuscriptItemDTO1(auditworkProjectID, fisclYear, archiveDisplay);


        List<TreeDTO> treeList = getTreeDTO(list, "", 0, fisclYear, new ManuscriptItemDTO(), fisclYearcount,false);
        for (TreeDTO t : treeList) {
            checkTree(t, treeList);
        }

        return treeList;
    }

    private void checkTree(TreeDTO tree, List<TreeDTO> treeList) throws Exception{
        if (tree instanceof ManuscriptItemTreeDTO) {
            ManuscriptItemTreeDTO item = (ManuscriptItemTreeDTO) tree;
            if (item.getFiscalYear().toString().equals(item.getCode()))//财年年
            {
                if (item.getChildren().size() == 0) {
                    treeList.remove(tree);
                }else
                {
                    if (item.getChildren().size() > 0) {
                        List<TreeDTO> newTreeList = new ArrayList();
                        newTreeList.addAll(item.getChildren());
                        for (TreeDTO t : newTreeList) {
                            checkTree(t, item.getChildren());
                        }
                    }
                }
            } else {
                if (item.getChildren().size() > 0) {
                    List<TreeDTO> newTreeList = new ArrayList();
                    newTreeList.addAll(item.getChildren());
                    for (TreeDTO t : newTreeList) {
                        checkTree(t, item.getChildren());
                    }
                }else//叶子节点 判断是否 有新增修改 或者删除的底稿
                {
                    String auditworkProjectID = item.getAuditworkProjectID();
                    Integer fiscalYear = item.getFiscalYear();
                    String parentStructureCode = item.getCode();

                    Optional<ManuscriptItem> opt ;
                    if(fiscalYear.equals(0))
                    {
                        opt = this.stream().where(w->w.getAuditworkProjectID().equals(auditworkProjectID)
                        && w.getIsGroup().equals("0") && w.getParentStructureCode().equals(parentStructureCode)
                        && w.getIsSelected().equals("1") && w.getIsDisplay().equals("1")
                        && (w.getModifyStatus() == 10 || w.getModifyStatus() == 20)
                        && ( w.getTheColor().equals("Blue") || w.getTheColor().equals("Black"))).findAny();
                    }else{
                        opt = this.stream().where(w->w.getAuditworkProjectID().equals(auditworkProjectID) && w.getFiscalYear() == fiscalYear
                        && w.getIsGroup().equals("0") && w.getParentStructureCode().equals(parentStructureCode)
                                && w.getIsSelected().equals("1") && w.getIsDisplay().equals("1")
                                && (w.getModifyStatus() == 10 || w.getModifyStatus() == 20)
                                && ( w.getTheColor().equals("Blue") || w.getTheColor().equals("Black"))).findAny();
                    }
                    if(opt.isPresent())
                    {
                        item.setAddModfy(true);
                    }
                    Optional<ManuscriptItem> del ;
                    if(fiscalYear.equals(0))
                    {
                        del = this.stream().where(w->w.getAuditworkProjectID().equals(auditworkProjectID)
                                && w.getIsGroup().equals("0") && w.getParentStructureCode().equals(parentStructureCode)
                                && w.getIsSelected().equals("1") && w.getIsDisplay().equals("1")
                                &&  w.getModifyStatus() == 30
                                && ( w.getTheColor().equals("Blue") || w.getTheColor().equals("Black"))).findAny();
                    }else{
                        del = this.stream().where(w->w.getAuditworkProjectID().equals(auditworkProjectID) && w.getFiscalYear() == fiscalYear
                                && w.getIsGroup().equals("0") && w.getParentStructureCode().equals(parentStructureCode)
                                && w.getIsSelected().equals("1") && w.getIsDisplay().equals("1")
                                && w.getModifyStatus() == 30
                                && ( w.getTheColor().equals("Blue") || w.getTheColor().equals("Black"))).findAny();
                    }
                    if(del.isPresent())
                    {
                        item.setDelete(true);
                    }


                }
            }

        }

    }


    private List<ManuscriptItemDTO> gainManuscriptItemDTO1(Long auditworkProjectID, List<ManuscriptItemDTO> fisclYear, String archiveDisplay) throws Exception {

        Optional<CompositionCustomer> first = CompositionCustomer.stream().where(w -> w.getId().equals(auditworkProjectID)).findFirst();
        if (!first.isPresent()) return null;

        CompositionCustomer item = first.get();
        String uuid = item.getUuid();

        String isarchiveDisplay = archiveDisplay;
        if (archiveDisplay.equals("2")) isarchiveDisplay = "1";


        List<Object> parameters = new ArrayList<>();

        List<ManuscriptItemDTO> list = repository().sqlQuery("with abc(ManuscriptDisplayName,StructureCode,ParentStructureCode,SortNO,FiscalYear,AuditworkProjectID,IsGroup,IsSplitForFiscalYear,Modifystatus,manuscriptType,IndexNumber) \n" +
                "as \n" +
                "(\n" +
                "select ManuscriptDisplayName,StructureCode,ParentStructureCode,SortNO,FiscalYear,AuditworkProjectID,IsGroup,IsSplitForFiscalYear,Modifystatus,manuscriptType,IndexNumber from Prj_ManuscriptItem\n" +
                "where IsGroup='0' and IsSelected='1' and IsDisplay ='1' and Modifystatus in(10,20,0) and  (TheColor in('Blue','Black') or ( '1' ='" + isarchiveDisplay + "' and ArchiveDisplay ='1'))  and AuditworkProjectID ='" + uuid + "'\n" +
                "union all \n" +
                "select dt.ManuscriptDisplayName,dt.StructureCode,dt.ParentStructureCode,dt.SortNO,dt.FiscalYear,dt.AuditworkProjectID,dt.IsGroup,dt.IsSplitForFiscalYear,dt.Modifystatus,dt.manuscriptType,dt.IndexNumber from Prj_ManuscriptItem as dt \n" +
                "inner join abc as c on dt.StructureCode = c.ParentStructureCode and dt.AuditworkProjectID ='" + uuid + "' and (c.fiscalyear = dt.fiscalyear or dt.fiscalyear =0)\n" +
                "\n" +
                ") \n" +
                " select distinct * from abc where IsGroup='1' or ('2' ='" + archiveDisplay + "' and manuscriptType ='11') order by SortNO", ManuscriptItemDTO.class);

        this.stream().where(w -> w.getAuditworkProjectID().equals(uuid) && w.getFiscalYear() > 0 && (w.getModifyStatus() == 20 || w.getModifyStatus() == 10 || w.getModifyStatus() == 0)
                && w.getTheColor() != null && (w.getTheColor().equals("Blue") || w.getTheColor().equals("Black"))).select(s -> new Tuple3<>(s.getFiscalYearInforName(), s.getFiscalYear(), s.getFiscalYearInforID())).distinct().forEach(f -> {
            ManuscriptItemDTO dto = new ManuscriptItemDTO();
            dto.setFiscalYear(f.getTwo()); //f.getFiscalYear()
            dto.setManuscriptDisplayName(f.getOne());//f.getFiscalYearInforName()
            dto.setParentStructureCode("FiscalYear");
            dto.setSortNO(0L);
            dto.setAuditworkProjectID(uuid);
            dto.setStructureCode(f.getTwo().toString()); //内部编码赋值为年度
            dto.setIsSplitForFiscalYear("3");
            dto.setIsGroup("0");
            dto.setFiscalYearInforid(f.getThree().toString());
            boolean b = false;
            for (ManuscriptItemDTO mitem : fisclYear) {
                if (dto.getFiscalYearInforid().equals(mitem.getFiscalYearInforid())) {
                    b = true;
                }
            }
            if (!b) {
                fisclYear.add(dto);
            }
        });
        return list;
    }

    //修订情况
    @JsonIgnore
    @SystemService(args = "query_auditworkProjectID")
    public List<TreeDTO> gainManuscriptHistoryItemDTO(Long auditworkProjectID) throws Exception {
        List<ManuscriptItemDTO> fisclYear = new ArrayList<>();
        List<ManuscriptItemDTO> list = gainManuscriptItemSource(auditworkProjectID, fisclYear);
        if (Framework.isNullOrEmpty(list)) return new ArrayList<>();

        Optional<CompositionCustomer> first = CompositionCustomer.stream().where(w -> w.getId().equals(auditworkProjectID)).findFirst();
        if (!first.isPresent()) return null;

        CompositionCustomer item = first.get();
        String uuid = item.getUuid();

        Long fisclYearcount = this.stream().where(w -> w.getAuditworkProjectID().equals(uuid) && w.getFiscalYear() > 0 && w.getModifyStatus() ==30).select(s -> s.getFiscalYearInforID()).distinct().count();

        List<TreeDTO> treeList = getTreeDTO(list, "", 0, fisclYear, new ManuscriptItemDTO(), fisclYearcount,false);
        for (TreeDTO t : treeList) {
            checkTree(t, treeList);
        }
        return treeList;
    }

    private List<ManuscriptItemDTO> gainManuscriptItemSource(Long auditworkProjectID, List<ManuscriptItemDTO> fisclYear) throws Exception {

        Optional<CompositionCustomer> first = CompositionCustomer.stream().where(w -> w.getId().equals(auditworkProjectID)).findFirst();
        if (!first.isPresent()) return null;

        CompositionCustomer item = first.get();
        String uuid = item.getUuid();

        List<Object> parameters = new ArrayList<>();

        List<ManuscriptItemDTO> list = repository().sqlQuery("with abc(ManuscriptDisplayName,StructureCode,ParentStructureCode,SortNO,FiscalYear,AuditworkProjectID,IsGroup,IsSplitForFiscalYear,Modifystatus,IndexNumber) \n" +
                "as \n" +
                "(\n" +
                "select ManuscriptDisplayName,StructureCode,ParentStructureCode,SortNO,FiscalYear,AuditworkProjectID,IsGroup,IsSplitForFiscalYear,Modifystatus,IndexNumber from Prj_ManuscriptItem\n" +
                "where IsGroup='0' and IsSelected='1' and IsDisplay ='1' and Modifystatus =30 and AuditworkProjectID ='" + uuid + "'\n" +
                "union all \n" +
                "select dt.ManuscriptDisplayName,dt.StructureCode,dt.ParentStructureCode,dt.SortNO,dt.FiscalYear,dt.AuditworkProjectID,dt.IsGroup,dt.IsSplitForFiscalYear,dt.Modifystatus,dt.IndexNumber from Prj_ManuscriptItem as dt \n" +
                "inner join abc as c on dt.StructureCode = c.ParentStructureCode and dt.AuditworkProjectID ='" + uuid + "' and (c.fiscalyear = dt.fiscalyear or dt.fiscalyear =0)\n" +
                ") \n" +
                " select distinct * from abc where IsGroup='1' order by SortNO", ManuscriptItemDTO.class);

        this.stream().where(w -> w.getAuditworkProjectID().equals(uuid) && w.getFiscalYear() > 0 &&  w.getModifyStatus() == 30).select(s -> new Pair<>(s.getFiscalYearInforName(), s.getFiscalYear())).distinct().forEach(f -> {
            ManuscriptItemDTO dto = new ManuscriptItemDTO();
            dto.setFiscalYear(f.getTwo()); //f.getFiscalYear()
            dto.setManuscriptDisplayName(f.getOne());//f.getFiscalYearInforName()
            dto.setParentStructureCode("FiscalYear");
            dto.setSortNO(0L);
            dto.setAuditworkProjectID(uuid);
            dto.setStructureCode(f.getTwo().toString()); //内部编码赋值为年度
            dto.setIsSplitForFiscalYear("3");
            dto.setIsGroup("0");
            fisclYear.add(dto);
        });
        return list;
    }


    @SystemService(args = "query_auditworkProjectID,query_fiscalYear,query_parentStructureCode,query_splitForFiscalYear,formId")
    public UIPagedList getManuscriptItems(String auditworkProjectID, Integer fiscalYear, String parentStructureCode, String splitForFiscalYear, Long formId) throws Exception {
        Integer fiscalYearVal = fiscalYear;
        List<ManuscriptItem> itemList;
        if (Framework.isNotNullOrEmpty(auditworkProjectID)) {
            if (splitForFiscalYear.equals("0")) //不拆分财年
            {
                itemList = this.stream().where(w -> w.getAuditworkProjectID().equals(auditworkProjectID)
                        && w.getIsGroup().equals("0") && w.getParentStructureCode().equals(parentStructureCode) && w.getIsSelected().equals("1")
                        && w.getIsDisplay().equals("1") && w.getModifyStatus() != 30 && (w.getTheColor().equals("Blue") || w.getTheColor().equals("Black") || w.getArchiveDisplay().equals("1"))).sortedBy(s -> s.getSortNO()).toList();
            } else {
                itemList = this.stream().where(w -> w.getFiscalYear().equals(fiscalYearVal) && w.getAuditworkProjectID().equals(auditworkProjectID)
                        && w.getIsGroup().equals("0") && w.getParentStructureCode().equals(parentStructureCode) && w.getIsSelected().equals("1")
                        && w.getIsDisplay().equals("1") && w.getModifyStatus() != 30 && (w.getTheColor().equals("Blue") || w.getTheColor().equals("Black") || w.getArchiveDisplay().equals("1"))).sortedBy(s -> s.getSortNO()).toList();
            }
        } else {
            itemList = new ArrayList<>();
        }

   /*
       if (parentStructureCode.equals(fiscalYear.toString()) )//上级内部编号如果是空，表示检索当前单位当前年的所有底稿
       {
           itemList=new ArrayList<>();
           //需求后来改了，不显示当年所有底稿了，改成叶子节点 才显示底稿 2019-5-24 熊辉改

//           itemList =this.stream().where(w->w.getFiscalYear().equals(fiscalYearVal) && w.getAuditworkProjectID().equals(auditworkProjectID)
//                   && w.getIsGroup().equals("0")).sortedBy(s->s.getSortNO()).toList();
       }
       else
       {
           //由 && w.getParentStructureCode().startsWith(parentStructureCode) 改成equals
           itemList =this.stream().where(w->w.getFiscalYear().equals(fiscalYearVal) && w.getAuditworkProjectID().equals(auditworkProjectID)
                   && w.getIsGroup().equals("0") && w.getParentStructureCode().equals(parentStructureCode)).sortedBy(s->s.getSortNO()).toList();


//           itemList =this.stream().where(w->w.getFiscalYear().equals(fiscalYearVal) && w.getAuditworkProjectID().equals(auditworkProjectID)
//                   && w.getIsGroup().equals("0") && w.getParentStructureCode().startsWith(parentStructureCode)).sortedBy(s->s.getSortNO()).toList();
       }
*/
        List<Object> list = new ArrayList<>();
        itemList.forEach(f -> {
            f.initialization();
            list.add(f);
        });

        return UIPagedList.band(list, FormPage.getFormColumn(formId));
    }


    public List<ManuscriptItemDTO> getManuscriptItemDTO1(Long auditworkProjectID, List<ManuscriptItemDTO> fisclYear) throws Exception {

        List<ManuscriptItemDTO> list = new ArrayList<>();
        String uuid;

        List<CompositionCustomer> cList = CompositionCustomer.stream().where(w -> w.getId().equals(auditworkProjectID)).toList();
        if (cList.size() == 1) {
            CompositionCustomer item = cList.get(0);
            uuid = item.getUuid();
        } else {
            return list;
        }
//删除的 数据 不检索， 正常的状态是0
        this.stream().where(w -> w.getAuditworkProjectID().equals(uuid) && w.getIsGroup().equals("1") && w.getIsDisplay().equals("1") && w.getIsSelected().equals("1") && w.getModifyStatus() != 30).sortedBy(s -> s.getSortNO()).forEach(f -> {
            list.add(f.toManuscriptItemDTO());
        });


        this.stream().where(w -> w.getAuditworkProjectID().equals(uuid) && w.getFiscalYear() > 0).select(s -> new Pair<>(s.getFiscalYearInforName(), s.getFiscalYear())).distinct().forEach(f -> {
            ManuscriptItemDTO dto = new ManuscriptItemDTO();
            dto.setFiscalYear(f.getTwo()); //f.getFiscalYear()
            dto.setManuscriptDisplayName(f.getOne());//f.getFiscalYearInforName()
            dto.setParentStructureCode("FiscalYear");
            dto.setSortNO(0L);
            dto.setAuditworkProjectID(uuid);
            dto.setStructureCode(f.getTwo().toString()); //内部编码赋值为年度
            dto.setIsSplitForFiscalYear("3");
            dto.setIsGroup("0");
            fisclYear.add(dto);
        });

        return list;
    }

    @JsonIgnore
    private ManuscriptItemDTO toManuscriptItemDTO() {
        ManuscriptItemDTO dto = new ManuscriptItemDTO();

        dto.setFiscalYear(this.getFiscalYear());


        dto.setManuscriptDisplayName(this.getManuscriptDisplayName());
       /* if (Framework.isNullOrEmpty(this.getParentStructureCode()) && this.getFiscalYear() > 0) //如果上级内部编码为空 说明是挂在年度下的树 上级编码赋值为年度  永久工作底稿 不用处理
        {
            dto.setParentStructureCode(this.getFiscalYear().toString());
        } else {
            dto.setParentStructureCode(this.getParentStructureCode());
        }*/
        if (Framework.isNotNullOrEmpty(this.getParentStructureCode())) {
            dto.setParentStructureCode(this.getParentStructureCode());
        }
        dto.setSortNO(this.getSortNO());
        dto.setStructureCode(this.getStructureCode());
        dto.setAuditworkProjectID(this.getAuditworkProjectID());
        dto.setIsGroup(this.getIsGroup());
        dto.setIsSplitForFiscalYear(this.getIsSplitForFiscalYear());
        return dto;
    }

    //fiscalYear:-1 表示不考虑财年
    //bIsSplit true  表示上级已经分了财年
    @JsonIgnore
    private List<TreeDTO> getTreeDTO(List<ManuscriptItemDTO> menuList, String parentId, Integer fiscalYear, List<ManuscriptItemDTO> fiscalYearList,
        ManuscriptItemDTO fisclYearitem, Long fisclYearcount,boolean bIsSplit) {

        List<TreeDTO> list = new ArrayList<>();
        Stream<ManuscriptItemDTO> childless;
        boolean ibParent = false;

        if (Framework.isNullOrEmpty(menuList)) {
            return list;
        }

        if (Framework.isNotNullOrEmpty(fisclYearitem.getIsSplitForFiscalYear()) && fisclYearitem.getIsSplitForFiscalYear().equals("3")) {
            childless = fiscalYearList.stream().filter(w -> w.getFiscalYear().equals(fiscalYear));
        } else {
            if (Framework.isNullOrEmpty(parentId)) {
                childless = menuList.stream().filter(w -> (w.getParentStructureCode() == null) || (w.getParentStructureCode() != null && w.getParentStructureCode().isEmpty())); //
                ibParent = true;
            } else {
                if (fiscalYear == -1) {
                    childless = menuList.stream().filter(w -> w.getParentStructureCode() != null && w.getParentStructureCode().equals(parentId));
                } else {
                    childless = menuList.stream().filter(w -> w.getParentStructureCode() != null && w.getParentStructureCode().equals(parentId) && w.getFiscalYear().equals(fiscalYear));
                }

            }
        }
        final boolean ib = ibParent;


        //childless.sorted(Comparator.comparing(ManuscriptItemDTO::getFiscalYear)).forEach(f -> {
        childless.forEach(f -> {
            TreeDTO dto = f.toTreeDTO();
           // if (ib && f.getIsSplitForFiscalYear().equals("1") && fisclYearcount > 1) //分财年  fiscalYearList.size()
             if ( !bIsSplit  && "1".equals(f.getIsSplitForFiscalYear()) && fiscalYearList.size() > 1 ) //分财年
            {
                for (ManuscriptItemDTO item : fiscalYearList) {
                    List<TreeDTO> tree = getTreeDTO(menuList, f.getStructureCode(), item.getFiscalYear(), fiscalYearList, item, fisclYearcount,true);
                    for (TreeDTO t : tree) {
                        dto.getChildren().add(t);
                    }
                }

            } else {

                if ("1".equals(f.getIsSplitForFiscalYear()) && fiscalYearList.size() > 1) //分财年
                {
                    dto.setChildren(getTreeDTO(menuList, f.getStructureCode(), f.getFiscalYear(), fiscalYearList, new ManuscriptItemDTO(), fisclYearcount,bIsSplit));
                } else if ("3".equals(f.getIsSplitForFiscalYear())) {
                    dto.setChildren(getTreeDTO(menuList, parentId, fiscalYear, fiscalYearList, new ManuscriptItemDTO(), fisclYearcount,bIsSplit));

                } else //不分财年
                {
                    // dto.setChildren(getTreeDTO(menuList, f.getStructureCode(), f.getFiscalYear(), fiscalYearList, new ManuscriptItemDTO()));
                    dto.setChildren(getTreeDTO(menuList, f.getStructureCode(), -1, fiscalYearList, new ManuscriptItemDTO(), fisclYearcount,bIsSplit));
                }
            }
            list.add(dto);
        });

        return list;
    }

    public ManuscriptItemHistory toManuscriptItemHistory() throws Exception {
        ManuscriptItemHistory history = ManuscriptItemHistory.createNew();

        ModelHelper.copyModel(this, history);
        history.setManuscriptItemId(this.getId());
        history.setUuid(this.getUuid());
        this.repository().add(history);

        return history;
    }

    @Override
    public String gainAttachmentPath() throws Exception {
        String path = super.gainAttachmentPath();
        return String.format("%s", path);
    }

    /**
     * cs端上传底稿路径
     *
     * @param code
     * @param type
     * @return
     * @throws Exception
     */
    @SystemService(args = "code,type")
    public String csUploadPath(String code, int type) throws Exception {
        String path = gainAttachmentPath();
        String str = Paths.get(DxnConfig.getCsfileUploadPath(), path, code, "ManuscriptItem", type == Constants.YES ? "Original" : "Change").toString();
        FileHelper.ensureDirtectoryExists(str);
        return Paths.get(path, code, "ManuscriptItem", type == Constants.YES ? "Original" : "Change").toString();
    }

    /**
     * 归档路径
     *
     * @param code
     * @return
     * @throws Exception
     */
    public String archiveUpLoadPath(String code) throws Exception {
        String path = gainAttachmentPath();
        String str = Paths.get(path, code, "ManuscriptItemArchive").toString();
        return str;
    }

    /**
     * 通过项目ID获取底稿相对路径
     * GBY
     */
    @SystemService(args = "prjUUid,type")
    public String gaincsUploadPath(String prjUUid, int type) throws Exception {
        Optional<CompositionCustomer> customer = CompositionCustomer.stream().where(w -> w.getUuid().equals(prjUUid)).findFirst();
        String code;
        if (!customer.isPresent()) {
            throw new DxnException("获取项目CODE错误！");
        }
        code = customer.get().getCode();
        return csUploadPath(code, type);
    }

    /**
     * 根据被审计单位ID和底稿类型 在归档时 生成底稿目录数据
     * GBY
     *
     * @param custId         被审计单位ID
     * @param manuscriptType 底稿类型
     *                       CompilingPersonSignName 编制人
     *                       FirstLevelReviewerSignName 复核人
     * @throws Exception
     */
    public void createManuscripItem(Long custId, String manuscriptType, String importFilePath, Long sortNO, String name,String  CompilingPersonSignName ,String FirstLevelReviewerSignName) throws Exception {
        CompositionCustomer customer = repository().findById(custId, CompositionCustomer.class);
        String uuid = customer.getUuid();
        Optional<ManuscriptItem> optmanuscript = ManuscriptItem.stream().where(w -> w.getAuditworkProjectID().equals(uuid) && w.getManuscriptType().equals(manuscriptType)
                && w.getIsGroup().equals("1")).findFirst();

        if (optmanuscript.isPresent()) {
            Path filepath = Paths.get(importFilePath);
            String filename;
            if (Framework.isNullOrEmpty(name)) {
                filename = filepath.getFileName().toString();
            } else {
                filename = name;
            }
            ManuscriptItem manuscriptGroup = optmanuscript.get();
            String structureCode = manuscriptGroup.getStructureCode();

            Optional<ManuscriptItem> has = ManuscriptItem.stream().where(w -> w.getAuditworkProjectID().equals(uuid) && w.getManuscriptType().equals(manuscriptType)
                    && w.getIsGroup().equals("0") && w.getParentStructureCode().equals(structureCode) && w.getManuscriptFile().equals(filename)).findAny();
            if (has.isPresent()) //如果已经存在就不用再次保存了
            {
                return;
            }


            String structureCodenew = structureCode + "-" + String.format("%03d", sortNO);

            manuscriptGroup.setArchiveDisplay("1");
            ManuscriptItem mode = ManuscriptItem.createNew();
            mode.setArchiveDisplay("1");
            mode.setFiscalYear(manuscriptGroup.getFiscalYear());
            mode.setAuditworkProjectID(uuid);
            mode.setStructureCode(structureCodenew);
            mode.setParentStructureCode(structureCode);
            mode.setSortNO(sortNO);
            mode.setIndexNumber(structureCode + String.format("%03d",sortNO));
            mode.setIsGroup("0");
            mode.setIsSelected("1");
            mode.setIsDisplay("1");
            mode.setModifyStatus(0);
            mode.setTheColor("");
            mode.setCompilingPersonSignName(CompilingPersonSignName);
            mode.setFirstLevelReviewerSignName(FirstLevelReviewerSignName);
            mode.setManuscriptType(manuscriptType);
            mode.setFiscalYearInforID(manuscriptGroup.getFiscalYearInforID());
            mode.setFiscalYearInforName(manuscriptGroup.getFiscalYearInforName());
            mode.setManuscriptFile(filename);
            mode.setManuscriptDisplayName(filename.substring(0, filename.lastIndexOf(".")));
            mode.setManuscriptFileRelativePath(importFilePath);
            repository().add(mode);

        }
    }

    /**
     * 生成压缩文件      //正式
     *
     * @param code
     * @throws Exception
     */
    @SystemService(args = "code")
    public String dataFileZip(String code) throws Exception {
        String str = Paths.get(DxnConfig.getCsfileUploadPath(), gainAttachmentPath(), code).toString();
        writeXml(code,str);
        String zipFileName = code;
        String newPath = DxnConfig.getCsfileUploadPath() + "\\" + "temporary";
        FileHelper.ensureDirtectoryExists(newPath);
       // ZipUtils.compressToZip(str, newPath, zipFileName, "报告,业务约定书,报备");
        //加密压缩包    path+"\\"
       // ZipUtils.encryption(Paths.get(newPath, zipFileName + ".zip").toString());

        //以下为新版加密

        new ZipEncrypt().encryptZip(str,Paths.get(newPath,code).toString(),"报告,业务约定书,报备");

        return Paths.get("temporary", zipFileName).toString();
    }

//    /**
//     * 生成压缩文件
//     *
//     * @param code
//     * @throws Exception
//     */
//    @SystemService(args = "code")
//    public String dataFileZip(String code) throws Exception {
//        String str = Paths.get(DxnConfig.getCsfileUploadPath(), gainAttachmentPath(), code).toString();
//        writeXml(code,str);
//        String zipFileName = code;
//        String newPath = DxnConfig.getCsfileUploadPath() + "\\" + "temporary";
//        ZipUtils.compressToZip(str, newPath, zipFileName, "报告,业务约定书,报备");
//        //加密压缩包    path+"\\"
//         ZipUtils.encryption(Paths.get(newPath, zipFileName + ".zip").toString());
//
//
//        return Paths.get("temporary", zipFileName + ".zip").toString();
//    }
    private static void gainElement(Element root,FileDetailedListDTO item) throws Exception{
        Element empEle = root.addElement("ManuscriptItem");
        empEle.addAttribute("ManuscriptName", item.getDisplayName());
        empEle.addAttribute("FileType", item.getFileType());
        empEle.addAttribute("ManuscriptFile", item.getManuscriptFile());
        empEle.addAttribute("IndexNumber", item.getIndexNumber());

        empEle.addAttribute("ManuscriptFileRelativePath", item.getFileFullName());
        empEle.addAttribute("IsFile", item.getIsFile());
        if ( Framework.isNotNullOrEmpty(item.getChildManuscriptItems())  && item.getChildManuscriptItems().size() >0)
        {
            List<FileDetailedListDTO>  child =item.getChildManuscriptItems();
            Element empEle2 = empEle.addElement("ChildManuscriptItems");
            for(FileDetailedListDTO item2 : child)
            {
                gainElement(empEle2,item2);
            }
        }

    }


    @SystemService()
    public void test() throws Exception{
        dataFileZip("XM1001-2020-00001");

    }

    private static void writeXml(String code, String path) throws Exception {
        Optional<CompositionCustomer> cust = CompositionCustomer.stream().where(c -> c.getCode().equals(code)).findFirst();
        if (cust.isPresent()) {
            CompositionCustomer c = CompositionCustomer.createNew();
            List<FileDetailedListDTO> array = c.gainFileDetailedList(cust.get().getUuid());
            if (array.size() > 0) {
                Document doc = DocumentHelper.createDocument();
                Element root = doc.addElement("root");
                for (FileDetailedListDTO item : array) {
                    gainElement(root,item);
                }
                try {
                    FileOutputStream fos = new FileOutputStream(Paths.get(path, "ManuscriptItemDetails.xml").toString());
                    XMLWriter writer = new XMLWriter(fos, OutputFormat.createCompactFormat());
                    writer.write(doc);
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
