package com.dxn.model.extend;

import com.dxn.dto.*;
import com.dxn.dto.enums.Constants;
import com.dxn.dto.enums.ManuscriptReviewStateEnum;
import com.dxn.forms.dto.TreeDTO;
import com.dxn.forms.dto.UIPagedList;
import com.dxn.model.entity.ReviewCommentsEntity;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.context.AppContext;
import com.dxn.system.dto.AttachmentDTO;
import com.dxn.system.dto.EntityDataDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import org.jinq.tuples.Pair;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "QR_ReviewComments")
public class ReviewComments extends ReviewCommentsEntity {

    //二级复核用到
    @JsonIgnore
    @SystemService(args = "query_customerID,query_rCType,query_manuscriptReviewState,query_isReview,query_isHaveOper")
    public List<TreeDTO> gainTwolevelReviewReviewCommentsDTO(Long customerID, Integer rCType, Integer manuscriptReviewState, Boolean isReview, Boolean isHaveOper) throws Exception {
        List<ReviewCommentsDTO> dtoLIst = gainTwolevelReviewReviewCommentsDTO(customerID, rCType, manuscriptReviewState, isReview);
        if (dtoLIst.size() == 0) return new ArrayList<>();

        List<EntityDataDTO> reviewer=gainCompositionReviewer(customerID);

        List<TreeDTO> tlist = getTreeDTO(dtoLIst, null, isReview, isHaveOper, rCType, 0,0L,false,reviewer);
        //给是否有附件节点赋值
        for (TreeDTO tree : tlist) {
            if (tree instanceof ReviewCommentsTreeDTO) {
                ReviewCommentsTreeDTO item = (ReviewCommentsTreeDTO) tree;
                Boolean bReturn = setAnnext(item);
                item.setAnnexShow(bReturn);
            }
        }
        return tlist;
    }

/**
 * 获取指定回复人被审计单位列表
 * Gby
 */
private List<EntityDataDTO> gainCompositionReviewer(Long cid) throws Exception{
    List<EntityDataDTO> dto=new ArrayList<>();

//编制人
    CompositionMembers.stream().where(w->w.getCompositionCustomerId().getId()==cid &&
            w.getIsEnable() ==10 && w.getRoleName() == 10).select(s-> new Pair<>(s.getNameId().getId(),s.getNameId().getName())).distinct().forEach(f->{
        EntityDataDTO item=new EntityDataDTO();
        item.setId(f.getOne());
        item.setName(f.getTwo());
        dto.add(item);
    });


    return dto;
}

    /**
     * 获取指定回复人项目列表
     * Gby
     */
    private List<EntityDataDTO> gainProjectReviewer(Long Pid) throws Exception{
        List<EntityDataDTO> dto=new ArrayList<>();

        ProjectMembers.stream().where(w->w.getProjectId().getId()==Pid &&
                w.getIsEnable() ==10).select(s-> new Pair<>(s.getNameId().getId(),s.getNameId().getName())).distinct().forEach(f->{
            EntityDataDTO item=new EntityDataDTO();
            item.setId(f.getOne());
            item.setName(f.getTwo());
            dto.add(item);
        });

        return dto;
    }



    //复核意见树数据源 //isReview true 复核界面，false 回复界面  isHaveOper ture 操作页面
    @JsonIgnore
    @SystemService(args = "query_auditworkProjectID,query_rCType,query_manuscriptReviewState,query_isReview,query_isHaveOper")
    public List<TreeDTO> ReviewCommentsDTO(Long auditworkProjectID, Integer rCType, Integer manuscriptReviewState, Boolean isReview, Boolean isHaveOper) throws Exception {

        List<ReviewCommentsDTO> dtoLIst = getReviewCommentsDTO(auditworkProjectID, rCType, manuscriptReviewState, isReview);

//        //获取当前项目所处的复核状态
//        Optional<CompositionCustomer> isHave = CompositionCustomer.stream().where(w -> w.getProjectId().getId() == auditworkProjectID &&
        List<EntityDataDTO> reviewer=new ArrayList<>();
        //reviewer=gainProjectReviewer(auditworkProjectID);//  三级复核及独立复核 重点问题和报告问题不指定回复人，底稿指定回复人在 getTreeDTO获取

        if (dtoLIst.size() == 0) return new ArrayList<>();
        List<TreeDTO> tlist = getTreeDTO(dtoLIst, null, isReview, isHaveOper, rCType, 0,0L,true,reviewer);
        //给是否有附件节点赋值
        for (TreeDTO tree : tlist) {
            if (tree instanceof ReviewCommentsTreeDTO) {
                ReviewCommentsTreeDTO item = (ReviewCommentsTreeDTO) tree;
                Boolean bReturn = setAnnext(item);
                if (rCType != 30) //底稿问题 附件挂在二级上
                {
                    item.setAnnexShow(bReturn);
                }

                if (rCType == 30) {
                    if (Framework.isNotNullOrEmpty(item.getManuscriptReviewState()) && item.getManuscriptReviewState().contains(String.valueOf(ManuscriptReviewStateEnum.ReviewCompleted.getIndex()))) {
                        setBtn(item);
                    }
                    if (item.getChildren().size() > 0) {
                        Integer i = 0;
                        for (TreeDTO t : item.getChildren()) //底稿问题，按照被审计单位，重新排序
                        {
                            if (t instanceof ReviewCommentsTreeDTO) {

                                i++;
                                ReviewCommentsTreeDTO it = (ReviewCommentsTreeDTO) t;
                                Boolean bReturnDraf = setAnnext(it);
                                it.setAnnexShow(bReturnDraf);
                                it.setText(i.toString() + '.' + it.getText());
                            }
                        }
                    }

                }
            }
        }

        return tlist;
    }

    //复核通过的底稿问题，上的操作按钮都设成不可见
    private void setBtn(TreeDTO tree) {
        if (tree instanceof ReviewCommentsTreeDTO) {
            ReviewCommentsTreeDTO item = (ReviewCommentsTreeDTO) tree;
            item.setDeletesShow(false);

            item.setReplyShow(false);
            item.getComboboxs().get(0).setWhetherHide(true);

            item.setReviewShow(false);
            item.setCheckBoxShow(false);
            if (item.getChildren().size() > 0) {
                for (TreeDTO t : item.getChildren()) {
                    setBtn(t);
                }
            }
        }
    }

    //给是否有附件节点赋值
    private Boolean setAnnext(TreeDTO tree) {
        if (tree instanceof ReviewCommentsTreeDTO) {
            ReviewCommentsTreeDTO item = (ReviewCommentsTreeDTO) tree;
            if (Framework.isNotNullOrEmpty(item.getHasAnnex())) {
                if (item.getHasAnnex()) return true;
            }

            if (item.getChildren().size() > 0) {
                for (TreeDTO t : item.getChildren()) {
                    if (setAnnext(t)) {
                        return true;
                    }
                }
            } else {
                return item.getHasAnnex();
            }
        }
        return false;
    }

    //isDraf true 底稿问题 单独处理(原来流程)， false 底稿问题 不单独处理
    private List<ReviewCommentsDTO> gainReviewCommentsDTOList(List<ReviewComments> rctemp,Boolean isReview ,Boolean isDraf) throws Exception
    {
        List<ReviewCommentsDTO> rcList = new ArrayList<>();
        Integer j = 0;
        Long userid = AppContext.current().getProfile().getId();
        for (Integer i = 0; i < rctemp.size(); i++) {
            ReviewComments f = rctemp.get(i);
            if (Framework.isNullOrEmpty(f.getIsSubmit())) //没有提交的
            {
                //if (!f.getCreatedById().equals(userid)) continue; //没有提交的，不是自己添加的不显示
                if (isReview) //复核界面只检索复核数据
                {
                    if (f.getQuestionType().equals(Constants.REPLY)) {
                        continue;
                    }
                } else //非复核界面只检索回复数据
                {
                    if (f.getQuestionType().equals(Constants.REVIEW)) {
                        continue;
                    }
                }
            }
            f.initialization();
            if (f.getLevel() == 1) {
                j++;
                rcList.add(f.toReviewCommentsDTO(j,isDraf));
            } else {
                rcList.add(f.toReviewCommentsDTO(0,isDraf));
            }
        }
        return rcList;
    }

    @JsonIgnore
    private List<ReviewCommentsDTO> gainTwolevelReviewReviewCommentsDTO(Long customerID, Integer rCType, Integer manuscriptReviewState, Boolean isReview) throws Exception {
        List<ReviewCommentsDTO> rcList = new ArrayList<>();


        List<ReviewComments> rctemp = this.stream().where(w -> ( w.getCompositionCustomerId() != null && w.getCompositionCustomerId().getId().equals(customerID) )&& w.getRCType().equals(rCType)
                && w.getManuscriptReviewState().equals(manuscriptReviewState)).sortedBy(f -> f.getId()).toList();
        if(rctemp.size()>0)
        {
            rcList = gainReviewCommentsDTOList(rctemp,isReview,false);
        }

        return rcList;
    }

    @JsonIgnore
    private List<ReviewCommentsDTO> getReviewCommentsDTO(Long auditworkProjectID, Integer rCType, Integer manuscriptReviewState, Boolean isReview) throws Exception {
        List<ReviewCommentsDTO> rcList = new ArrayList<>();

        if (rCType == 30) //底稿问题
        {//ReviewCommentsDTO 的第一级是组织架构
            //先判断提交的被审计单位中是否有主单位，如果有，显示所有的单位底稿问题，只是复核完成的不能编辑，其他都能编辑，如果提交的没有主单位，就只显示提交的单位底稿问题
            Optional<CompositionCustomer> isHave = CompositionCustomer.stream().where(w -> w.getProjectId().getId() == auditworkProjectID &&
                    w.getParentId() == null &&
                    (w.getDocStateAllE().contains("30") || w.getDocStateAllE().contains("40") || w.getDocStateAllE().contains("50") || w.getDocStateAllE().contains("60"))).findFirst();
            List<CompositionCustomer> customers;
            if (isHave.isPresent()) //存在
            {
                customers = CompositionCustomer.stream().where(w -> w.getProjectId().getId() == auditworkProjectID).sortedBy(o->o.getSort()).toList();
            } else {
                if (isReview) {
                    customers = CompositionCustomer.stream().where(w -> w.getProjectId().getId() == auditworkProjectID &&
                            (w.getDocStateAllE().contains("30") || w.getDocStateAllE().contains("40") || w.getDocStateAllE().contains("50") || w.getDocStateAllE().contains("60") || w.getDocStateAllE().contains("70"))).sortedBy(o->o.getSort()).toList();

                } else  //大项目经理 可以看到所有底稿问题
                {
                    customers = CompositionCustomer.stream().where(w -> w.getProjectId().getId() == auditworkProjectID).sortedBy(o->o.getSort()).toList();

                }
            }


            if (customers.size() > 0) {
                for (CompositionCustomer item : customers) {
                    ReviewCommentsDTO dto = new ReviewCommentsDTO();
                    dto.setrCType(rCType);
                    dto.setId(item.getId());//被审计单位表ID和复核意见表ID有重复 +"C" 区分
                    dto.setNo(0);
                    dto.setLevel(1);
                    dto.setContent(item.getName());
                    dto.setManuscriptReviewState(item.getDocStateAllE());
                    rcList.add(dto);
                }
            }
        }
        List<ReviewComments> rctemp = this.stream().where(w -> w.getProjectId().getId().equals(auditworkProjectID) && w.getRCType().equals(rCType)
                && w.getManuscriptReviewState().equals(manuscriptReviewState)).sortedBy(f -> f.getId()).toList();

        List<ReviewCommentsDTO> rcList2 = new ArrayList<>();
        rcList2 = gainReviewCommentsDTOList(rctemp,isReview,true);

        rcList.addAll(rcList2);
        return rcList;
    }

    @JsonIgnore
    private ReviewCommentsDTO toReviewCommentsDTO(Integer no,Boolean isDraf) throws Exception {
        ReviewCommentsDTO dto = new ReviewCommentsDTO();
        dto.setCreater(this.getCreatedBy().getName());
        dto.setProjectstate(this.getProjectId().getManuscriptReviewState());
        String name = this.getCreatedBy().getName();
        String content = this.getContent();
//        if ( this.getQuestionType()==10) {//不分独立复核了，都这样整(this.getManuscriptReviewState().equals(50) || this.getManuscriptReviewState().equals(60)) &&
           // content =this.getDescription() + "("+name + "):" + content;
//        }
        if(this.getManuscriptReviewState() >30)
        {
            dto.setDeptManagerId(this.getProjectId().getDeptId().getMasterId().getId());
        }
        if(Framework.isNotNullOrEmpty(this.getCompositionCustomerId()) && Framework.isNotNullOrEmpty(this.getCompositionCustomerId().getProjectManagerId()))
        {
            dto.setCompositionManagerId(this.getCompositionCustomerId().getProjectManagerId().getId());
        }
        dto.setProjectManagerId(this.getProjectId().getProjectManagerId().getId());



        EntityDataDTO combobox = new EntityDataDTO();
        if(Framework.isNullOrEmpty(this.getDesignatedReviewerId()))
        {
            combobox.setId(0L);
            combobox.setName("指定回复人");
        }
        else{
            combobox.setId(this.getDesignatedReviewerId().getId());
            combobox.setName(this.getDesignatedReviewerId().getName());
        }
        dto.setComboboxs(combobox);
        dto.setContent(content);
        dto.setDescription(this.getDescription());
        dto.setManuscriptReviewState(this.getManuscriptReviewState().toString());
        dto.setId(this.getId());
        if (this.getRCType() == 30 && isDraf)//底稿问题 级次设置为 级次+1
        {
            dto.setLevel(this.getLevel() + 1);
        } else {
            dto.setLevel(this.getLevel());
        }

        dto.setIsSubmit(this.getIsSubmit());
        dto.setPass(this.getIsPass());
        dto.setQuestionType(this.getQuestionType());
        dto.setNo(no);
        dto.setCreatedById(this.getCreatedBy().getId());
        Long compositionCustomerId = 0L;
        if (Framework.isNotNullOrEmpty(this.getCompositionCustomerId())) {
            compositionCustomerId = this.getCompositionCustomerId().getId();
            dto.setCompositionCustomerId(compositionCustomerId);
        }


        dto.setrCType(this.getRCType());

        if (Framework.isNotNullOrEmpty(this.getParentId())) {
            dto.setParentId(this.getParentId().getId());
        } else if (this.getRCType() == 30 && isDraf)//底稿问题的parentId设置 被审计单位ID
        {
            dto.setParentId(compositionCustomerId);
        }
        String enttityname = this.gainTypeName();
        Long id = this.getId();
        Long count = Attachment.stream().where(w -> w.getEntityName().equals(enttityname) && w.getEntityId() == id).count();
        if (count > 0) {
            dto.setHasAnnex(true);
        } else {
            dto.setHasAnnex(false);
        }
        return dto;
    }

    //isDraf true 底稿问题 单独处理(原来流程)， false 底稿问题 不单独处理
    @JsonIgnore
    private List<TreeDTO> getTreeDTO(List<ReviewCommentsDTO> rvcList, Long parentId, Boolean isReview, Boolean isHaveOper, Integer rCType, Integer level,Long currentId,Boolean isDraf,List<EntityDataDTO> reviewer) throws Exception{
        List<TreeDTO> treeList = new ArrayList<>();
        //Stream<ReviewCommentsDTO> childless;
        List<ReviewCommentsDTO> childless;
        Integer level2 = level + 1;

        if (Framework.isNullOrEmpty(parentId)) {
            childless = rvcList.stream().filter(f -> f.getParentId() == null).collect(Collectors.toList());
        } else {
            childless = rvcList.stream().filter(f -> f.getParentId() != null && f.getParentId().equals(parentId) && f.getLevel() == level2 ).collect(Collectors.toList());//&& f.getId() !=currentId 先去掉试试
        }

        if(childless.size() >0)
        {
            Collections.sort(childless);
            for(int i =0;i<childless.size();i++)
            {
                ReviewCommentsDTO item = childless.get(i);
                if(isDraf && rCType ==30 && Framework.isNullOrEmpty(parentId))
                {
                    reviewer=gainCompositionReviewer(item.getId());

                }
                TreeDTO dto = item.toTreeDTO(isReview, isHaveOper,isDraf,reviewer);

                dto.setChildren(getTreeDTO(rvcList, item.getId(), isReview, isHaveOper, rCType, item.getLevel(),item.getId(),isDraf,reviewer));
                boolean isHaveChildren = true;//是否有子节点
                if (dto.getChildren().size() == 0 && Framework.isNotNullOrEmpty(item.getIsSubmit())  ) //没有子节点
                {

                    if((i < childless.size() -1 && ((rCType !=30 && item.getLevel() >=2 ) || (rCType ==30 && item.getLevel() >2))))
                    {
                        if(i == childless.size() -2 )
                        {
                            Integer submit =childless.get(childless.size() -1).getIsSubmit();
                            if(Framework.isNullOrEmpty(submit) || submit==20)
                            {
                                isHaveChildren = false;
                            }else
                            {
                                isHaveChildren = true;
                            }


                        }else{
                            isHaveChildren = true;
                        }

                    }else{
                        isHaveChildren = false;
                    }

                } else if (dto.getChildren().size() == 1) {
                    TreeDTO t = dto.getChildren().get(0);
                    if (t.getChildren().size() == 0 && Framework.isNotNullOrEmpty(item.getIsSubmit())) {
                        if (t instanceof ReviewCommentsTreeDTO) {
                            ReviewCommentsTreeDTO tree = (ReviewCommentsTreeDTO) t;
                            if (Framework.isNullOrEmpty(tree.getIsSubmit())) {
                                isHaveChildren = false;
                            }
                        }

                    }
                }
                if (!isHaveChildren) {
                    if (dto instanceof ReviewCommentsTreeDTO) {

                        if (isReview && !isHaveOper) {
                            ReviewCommentsTreeDTO d = (ReviewCommentsTreeDTO) dto;
                            d.setReplyShow(false);//回复
                            d.setReviewShow(false);
                        } else {

                            item.setButton((ReviewCommentsTreeDTO) dto, isReview, item.getQuestionType());
                        }
                    }
                }

                if("70".equals(item.getProjectstate()))
                {
                    ReviewCommentsTreeDTO d = (ReviewCommentsTreeDTO) dto;
                    d.setReplyShow(false);//回复
                    d.setReviewShow(false);
                    d.setDeletesShow(false);
                    d.setCheckBoxReadonly(true);
                }


                treeList.add(dto);
            }
        }


     /*   Iterator<ReviewCommentsDTO> iterator = childless.iterator();
        while (iterator.hasNext()) {
            ReviewCommentsDTO item = iterator.next();
            if(isDraf && rCType ==30 && Framework.isNullOrEmpty(parentId))
            {
                reviewer=gainCompositionReviewer(item.getId());

            }
            TreeDTO dto = item.toTreeDTO(isReview, isHaveOper,isDraf,reviewer);

            dto.setChildren(getTreeDTO(rvcList, item.getId(), isReview, isHaveOper, rCType, item.getLevel(),item.getId(),isDraf,reviewer));
            boolean isHaveChildren = true;//是否有子节点
            if (dto.getChildren().size() == 0 && Framework.isNotNullOrEmpty(item.getIsSubmit())  ) //没有子节点
            {

                if((iterator.hasNext() && ((rCType !=30 && item.getLevel() >=2 ) || (rCType ==30 && item.getLevel() >2))))
                {
                    isHaveChildren = true;
                }else{
                    isHaveChildren = false;
                }

            } else if (dto.getChildren().size() == 1) {
                TreeDTO t = dto.getChildren().get(0);
                if (t.getChildren().size() == 0 && Framework.isNotNullOrEmpty(item.getIsSubmit())) {
                    if (t instanceof ReviewCommentsTreeDTO) {
                        ReviewCommentsTreeDTO tree = (ReviewCommentsTreeDTO) t;
                        if (Framework.isNullOrEmpty(tree.getIsSubmit())) {
                            isHaveChildren = false;
                        }
                    }

                }
            }
            if (!isHaveChildren) {
                if (dto instanceof ReviewCommentsTreeDTO) {

                    if (isReview && !isHaveOper) {
                        ReviewCommentsTreeDTO d = (ReviewCommentsTreeDTO) dto;
                        d.setReplyShow(false);//回复
                        d.setReviewShow(false);
                    } else {

                        item.setButton((ReviewCommentsTreeDTO) dto, isReview, item.getQuestionType());
                    }
                }
            }

            if("70".equals(item.getProjectstate()))
            {
                ReviewCommentsTreeDTO d = (ReviewCommentsTreeDTO) dto;
                d.setReplyShow(false);//回复
                d.setReviewShow(false);
                d.setDeletesShow(false);
                d.setCheckBoxReadonly(true);
            }


            treeList.add(dto);
        }
*/
        return treeList;
    }

    //根据项目ID获取一条处于审计中的被审计单位 添加一条复核意见，默认项目时使用
    @JsonIgnore
    @SystemService(args = "prjId,cId")
    public CompositionCustomerDTOE getCompositionCustomerByPrjId(Long prjId, Long cId) throws Exception {

        List<CompositionCustomer> first = CompositionCustomer.stream().where(w -> w.getProjectId().getId() == prjId &&
                (w.getDocStateAllE().contains("30") || w.getDocStateAllE().contains("40") || w.getDocStateAllE().contains("50") ||
                        w.getDocStateAllE().contains("60"))).sortedBy(s -> s.getId()).toList();
        if (first.size() == 0 && Framework.isNullOrEmpty(cId) )  return new CompositionCustomerDTOE();

        if (Framework.isNullOrEmpty(cId) ) return toCompositionCustomerDTOE(first.get(0));
        CompositionCustomer customer;
        if(first.size() == 0 && Framework.isNotNullOrEmpty(cId))
        {
            customer = repository().findById(cId,CompositionCustomer.class);
        }else
        {
            customer = first.get(0);
            for (CompositionCustomer item : first) {
                if (item.getId().equals(cId)) {
                    customer = item;
                    break;
                }
            }
        }



        return toCompositionCustomerDTOE(customer);
    }

    //根据复核意见ID 查找对应的项目项目ID,底稿问题回复时用到
    @JsonIgnore
    @SystemService(args = "rcId")
    public CompositionCustomerDTOE getgetCompositionCustomerById(Long rcId) throws Exception {
        Optional<ReviewComments> first = this.stream().where(w -> w.getId() == rcId).findFirst();
        if (!first.isPresent()) return new CompositionCustomerDTOE();
        Long id = first.get().getCompositionCustomerId().getId();
        Optional<CompositionCustomer> c = CompositionCustomer.stream().where(f -> f.getId() == id).findFirst();
        if (!c.isPresent()) return new CompositionCustomerDTOE();
        return toCompositionCustomerDTOE(c.get());
    }




    private CompositionCustomerDTOE toCompositionCustomerDTOE(CompositionCustomer item) {
        CompositionCustomerDTOE dto = new CompositionCustomerDTOE();
        dto.setCode(item.getCode());
        dto.setId(item.getId());
        dto.setName(item.getName());

        return dto;
    }


    //校验是否有未下发的复核意见
    @JsonIgnore
    @SystemService(args = "priId,reviewOpinionState,manuscriptReviewState,reviewer")
    public Boolean checkAComment(Long priId, Integer reviewOpinionState, Integer manuscriptReviewState, Long reviewer) throws Exception {

        Integer QuestionType;
        List<ReviewComments> list =new ArrayList<>();
        if (reviewOpinionState ==20)
        {
            QuestionType =10;//复核问题
            list = this.stream().where(w -> w.getProjectId().getId() == priId &&
                    w.getManuscriptReviewState() == manuscriptReviewState && w.getIsSubmit() == null && w.getQuestionType() == QuestionType).toList();
        }else
        {
            List<ReviewComments> qlist = this.stream().where(w->w.getProjectId().getId()==priId &&
                    w.getIsSubmit() !=null && w.getIsSubmit() ==10 && w.getQuestionType() == 10).toList();
            QuestionType =20;//回复问题
            for(ReviewComments item :qlist)
            {
                Long parentid = item.getId();
                Long parentid2;
                if(Framework.isNotNullOrEmpty(item.getParentId()))
                {
                    parentid2=item.getParentId().getId();
                }else
                {
                    parentid2=0L;
                }

                Optional<ReviewComments> qt20 = this.stream().where(w -> w.getProjectId().getId() == priId &&
                        w.getQuestionType() == QuestionType && (w.getParentId().getId() == parentid || w.getParentId().getId() == parentid2)).findAny();
               if(qt20.isPresent())
               {
                   list.add(qt20.get());
               }else
               {
                   return false;
               }
            }
        }

        if (list.size() == 0) {
            return false;
        }

        return  true;
    }


    //下发意见 提交回复 用到  reviewOpinionState  20：下发意见  reviewer:复核人
    //返回值  1 成功  0：没有复核意见
    @JsonIgnore
    @SystemService(args = "priId,reviewOpinionState,manuscriptReviewState,reviewer")
    public Integer sendAComment(Long priId, Integer reviewOpinionState, Integer manuscriptReviewState, Long reviewer) throws Exception {
        Project p = repository().findById(priId, Project.class);


        List<CompositionCustomer> cusList = CompositionCustomer.stream().where(w -> w.getProjectId().getId() == priId &&
                (w.getDocStateAllE().contains("30") || w.getDocStateAllE().contains("40") || w.getDocStateAllE().contains("50") ||
                        w.getDocStateAllE().contains("60"))).toList();

        List<ReviewComments> list;
        Integer manuscriptReviewState2 = manuscriptReviewState;
        String SpecialConditions="";
        if(Framework.isNotNullOrEmpty(manuscriptReviewState))
        {
            SpecialConditions=manuscriptReviewState.toString();//发消息时标记底稿复核状态
        }

        if (reviewOpinionState == 30) {//提交回复
            list = this.stream().where(w -> w.getProjectId().getId() == priId &&
                    w.getQuestionType() == 20 && w.getIsSubmit() == null).toList();

        } else {
            list = this.stream().where(w -> w.getProjectId().getId() == priId &&
                    w.getManuscriptReviewState() == manuscriptReviewState2 && w.getIsSubmit() == null).toList();
        }
        if (list.size() == 0) {
            return 0;
        }

        p.setReviewOpinionState(reviewOpinionState);
        p.setWhetherShow(true);

        String strDocState = "";

        List<Long> users = new ArrayList<>();

        for (ReviewComments item : list) {
            item.setIsSubmit(10);
            repository().add(item);
            if (!strDocState.contains(item.getManuscriptReviewState().toString())) {
                strDocState += item.getManuscriptReviewState().toString();

            }
            if (reviewOpinionState != 20) //提交回复 二级/三级 复核人 为回复的上级复核问题的创建人
            {
                if (item.getManuscriptReviewState() == 30 || item.getManuscriptReviewState() == 40) {
                    if (users.size() == 0) {
                        users.add(item.getParentId().getCreatedById());
                        SpecialConditions=item.getManuscriptReviewState().toString();
                    }
                }
            }
        }

        if(Framework.isNotNullOrEmpty(manuscriptReviewState))
        {
            if(manuscriptReviewState == 30) //二级复核
            {
                p.setReviewOpinionState30(reviewOpinionState);
            }else if(manuscriptReviewState ==40) //三级复核
            {
                p.setReviewOpinionState40(reviewOpinionState);
            }else if(manuscriptReviewState == 50) //分所复核
            {
                p.setReviewOpinionState50(reviewOpinionState);
            }else if(manuscriptReviewState == 60)
            {
                p.setReviewOpinionState60(reviewOpinionState);
            }
        }else
        {
            if(strDocState.contains("30"))
            {
                p.setReviewOpinionState30(reviewOpinionState);
            }
            if(strDocState.contains("40"))
            {
                p.setReviewOpinionState40(reviewOpinionState);
            }
            if(strDocState.contains("50"))
            {
                p.setReviewOpinionState50(reviewOpinionState);
            }
            if(strDocState.contains("60"))
            {
                p.setReviewOpinionState60(reviewOpinionState);
            }
        }


        if (list.size() > 0) {
            if (cusList.size() > 0) {
                for (CompositionCustomer item : cusList) {
                    item.initialization();
                    item.setReviewOpinionState(reviewOpinionState);
                    if (reviewOpinionState == 20) {
                        item.pushDragState(item.getUuid());
                    }
                    repository().add(item);
                }
            }
            Optional<DraftReviewBatch> d = DraftReviewBatch.stream().where(w -> w.getProjectId().getId() == priId && (w.getDocStateAll().contains("30") || w.getDocStateAll().contains("40") || w.getDocStateAll().contains("50") ||
                    w.getDocStateAll().contains("60"))).sortedBy(f -> f.getCreatedOn()).findFirst();
            if (d.isPresent()) {
                DraftReviewBatch batch = d.get();
                batch.initialization();
                String content = batch.getProjectId().getShowName() + "-下属" + batch.gainDraftReviewBatchItemsnum() + "个项目";

                Notice notice = Notice.createNew();


                if (reviewOpinionState == 20) {
                    if (manuscriptReviewState == 50 && Framework.isNullOrEmpty(batch.getBranchReviewerId())) //分所复核
                    {
                        batch.setBranchReviewer(reviewer);
                    }
                    if (manuscriptReviewState == 60 && Framework.isNullOrEmpty(batch.getGeneralReviewerId())) //总所复核
                    {
                        batch.setGeneralReviewer(reviewer);
                    }
                    batch.setHasComments(true);
                    content = "收到新的复核意见（" + AppContext.current().getProfile().getName() + "）" + content;
                    users.add(batch.getProjectId().getProjectManagerId().getId());//下发意见 接收人 是项目经理

                } else {
                    batch.setHasComments(false);

                    content = "收到新的回复（" + AppContext.current().getProfile().getName() + "）" + content;


                    if (strDocState.contains("50") || strDocState.contains("60"))//分所复核 和总所复核 需要给项目的分配人员及项目经理发消息
                    {
                        if(users.size()>0)//如果有二级或三级复核意见 就先发复核意见的消息
                        {
                            notice.createNotice(AppContext.current().getProfile().getId(), users, "底稿复核意见", content,  priId, "ReviewComments", null,SpecialConditions);
                            users.clear();
                        }

                        Long rever;
                        List<Long> allocation;
                        List<Long> revers = new ArrayList<>();
                        Boolean lb = false;
                        Long did = batch.getId();
                        if (strDocState.contains("50")) {
                            SpecialConditions="50";
                            rever = batch.getBranchReviewerId().getId();
                            allocation = ReviewAllocation.stream().where(w -> w.getDraftReviewBatchId().getId() == did && w.getManuscriptReviewState() == 50)
                                    .select(s -> s.getUserId().getId()).distinct().toList();
                            for (Long item : allocation) {
                                if (item.equals(rever)) {
                                    lb = true;
                                }
                                users.add(item);
                            }
                            if (!lb)//如果复核经理没有分配到项目上 就单独消息提示
                            {
                                revers.add(rever);
                            }
                            if (revers.size() > 0) {
                                notice.createNotice(AppContext.current().getProfile().getId(), revers, "底稿复核意见", content,  priId, "ReviewComments", 20,SpecialConditions);
                                revers.clear();
                            }

                        }
                        lb = false;
                        if (strDocState.contains("60")) {
                            SpecialConditions="60";
                            rever = batch.getGeneralReviewerId().getId();
                            allocation = ReviewAllocation.stream().where(w -> w.getDraftReviewBatchId().getId() == did && w.getManuscriptReviewState() == 60)
                                    .select(s -> s.getUserId().getId()).distinct().toList();
                            for (Long item : allocation) {
                                if (item.equals(rever)) {
                                    lb = true;
                                }
                                users.add(item);
                            }
                            if (!lb)//如果复核经理没有分配到项目上 就单独消息提示
                            {
                                revers.add(rever);
                            }
                        }
                        if (revers.size() > 0) {
                            notice.createNotice(AppContext.current().getProfile().getId(), revers, "底稿复核意见", content,  priId, "ReviewComments", 20,SpecialConditions);
                        }
                    }
                }
                if (users.size() > 0) {

                    notice.createNotice(AppContext.current().getProfile().getId(), users, "底稿复核意见", content, priId, "ReviewComments", null,SpecialConditions);
                }
            }

        }

        //   repository().commit();
        return 1;
    }

    private void createNotice(String content, String title) throws Exception {

        // content += ":"+this.getProjectId().getShowName() +"-下属"+this.getDraftReviewBatchItems().size() +"个项目";
    }

    //检索复核分配
    @JsonIgnore
    @SystemService(args = "formId")
    public UIPagedList getDraftReview(Long formId) throws Exception {
        List<UserGroupItem> itemList = UserGroupItem.stream().where(w -> w.getUserGroupId().getCode().equals("DraftReview")).toList();
        List<Object> list = new ArrayList<>();
        itemList.forEach(f -> {
            f.initialization();
            list.add(f);
        });
        return UIPagedList.band(list, FormPage.getFormColumn(formId));
    }


    private ReviewCommentsQueryDTO toReviewCommentsQueryDTO() throws Exception {
        ReviewCommentsQueryDTO dto = new ReviewCommentsQueryDTO();
        dto.setReview(this.getDescription());
        dto.setContent(this.getContent());
        dto.setId(this.getId());
        dto.setManuscriptReviewState(this.getManuscriptReviewState());

        if (Framework.isNotNullOrEmpty(this.getCompositionCustomerId())) {
            dto.setCompositionCustomerId(this.getCompositionCustomerId().getId());
            dto.setCompositionCustomerName(this.getCompositionCustomer().getName());
        }
        if (Framework.isNotNullOrEmpty(this.getRelatedManuscript())) {
            dto.setRelatedManuscript(this.getRelatedManuscript());
        }
        dto.setProjectId(this.getProjectId().getId());
        dto.setQuestionType(this.getQuestionType());
        dto.setLevel(this.getLevel());
        dto.setrCType(this.getRCType());
        if (Framework.isNotNullOrEmpty(this.getParentId())) {
            dto.setParentId(this.getParentId().getId());
        }

        Long id = this.getId();

        List<AttachmentDTO> files = new ArrayList<>();
        Attachment.stream().where(w -> w.getEntityName().equals("ReviewComments") && w.getEntityId() == id).forEach(f -> {
            AttachmentDTO file = new AttachmentDTO();
            file.setId(f.getId());
            file.setName(f.getName());
            file.setSize(f.getSize());
            file.setSuffix(f.getSuffix());
            file.setPath(f.getPath());
            file.setMd5(f.getMd5());
            file.setUuid(f.getUuid());
            files.add(file);
        });
        dto.setFiles(files);

        return dto;
    }

    private ReviewCommentsQueryDTO getReviewCommentsQueryDTO(Long rcId) throws Exception {
        ReviewCommentsQueryDTO dto = new ReviewCommentsQueryDTO();
        Optional<ReviewComments> rc = this.stream().where(w -> w.getId() == rcId).findFirst();
        if (rc.isPresent()) {
            ReviewComments item = rc.get();
            item.initialization();
            dto = item.toReviewCommentsQueryDTO();
        }
        return dto;
    }

    private void getReviewCommentsQueryDTOFromParentId(Long rcId, List<ReviewCommentsQueryDTO> list,Boolean issubmit) throws Exception {
        ReviewCommentsQueryDTO dto = new ReviewCommentsQueryDTO();
        List<ReviewComments> rc ;
        if(issubmit)
        {
            rc= this.stream().where(w -> w.getParentId() != null && w.getParentId().getId() == rcId && w.getIsSubmit() !=null && w.getIsSubmit() ==10).toList();
        }else
        {
            rc= this.stream().where(w -> w.getParentId() != null && w.getParentId().getId() == rcId ).toList();
        }

        if (rc.size() > 0) {
            for (ReviewComments item : rc) {
                item.initialization();
                list.add(item.toReviewCommentsQueryDTO());
                getReviewCommentsQueryDTOFromParentId(item.getId(), list,issubmit);
            }
        } else {
            return;
        }
    }

    //点击第一级复核 查看复核数据
    @SystemService(args = "rcId,issubmit")
    public List<ReviewCommentsQueryDTO> getRCQuerList(Long rcId,Boolean issubmit) throws Exception {
        List<ReviewCommentsQueryDTO> list = new ArrayList<>();
        ReviewCommentsQueryDTO item = getReviewCommentsQueryDTO(rcId);
        list.add(item);
        getReviewCommentsQueryDTOFromParentId(item.getId(), list,issubmit);

        return list;
    }

    //二次复核检索数据
    @SystemService(args = "rcId")
    public ReviewCommentsShowDTO getReviewCommentsQueryDTOList(Long rcId) throws Exception {
        ReviewCommentsShowDTO show = new ReviewCommentsShowDTO();
        List<ReviewCommentsQueryDTO> list = new ArrayList<>();
        ReviewCommentsQueryDTO item = getReviewCommentsQueryDTO(rcId);
        ReviewCommentsQueryDTO first = item;
        list.add(item);
        while (Framework.isNotNullOrEmpty(item.getParentId())) {
            item = getReviewCommentsQueryDTO(item.getParentId());
            list.add(0, item);
        }

        ReviewCommentsQueryDTO dto = new ReviewCommentsQueryDTO();

        Optional<ReviewComments> rc = this.stream().where(w -> w.getParentId() != null && w.getParentId().getId() == rcId).findFirst();
        if (rc.isPresent()) {
            ReviewComments t = rc.get();
            t.initialization();
            dto = t.toReviewCommentsQueryDTO();
        } else //如果数据库中还没有复核数据，设置默认值
        {
            dto.setrCType(first.getrCType());
            dto.setQuestionType(10);//复核
            dto.setProjectId(first.getProjectId());
            dto.setCompositionCustomerId(first.getCompositionCustomerId());
            dto.setManuscriptReviewState(first.getManuscriptReviewState());
            dto.setParentId(first.getId());
            dto.setLevel(first.getLevel() + 1);
            String tmp;
            switch (first.getLevel()) {
                case 1: {
                    tmp = "一";
                    break;
                }

                case 2: {
                    tmp = "二";
                    break;
                }
                case 3: {
                    tmp = "三";
                    break;
                }
                case 4: {
                    tmp = "四";
                    break;
                }
                default: {
                    tmp = "";
                    break;
                }
            }
            dto.setReview(tmp + "次复核");
        }

        show.setList(list);
        show.setItem(dto);

        return show;
    }

    //保存 复核意见，复核时用到
    @JsonIgnore
    @SystemService(args = "dto")
    public Long saveReviewComments(ReviewCommentsQueryDTO dto) throws Exception {
        ReviewComments reviewComments;
        if (Framework.isNullOrEmpty(dto.getId())) {
            reviewComments = ReviewComments.createNew();
        } else {
            reviewComments = repository().findById(dto.getId(), ReviewComments.class);
        }

        ModelHelper.copyModel(dto, reviewComments);
        reviewComments.setDescription(dto.getReview());
        if (Framework.isNotNullOrEmpty(dto.getCompositionCustomerId())) {
            reviewComments.setCompositionCustomer(dto.getCompositionCustomerId());
        }

        reviewComments.setProject(dto.getProjectId());
        reviewComments.setParent(dto.getParentId());

        if (Framework.isNullOrEmpty(dto.getId())) {
            this.repository().add(reviewComments);
            //先提交获得实体ID
            this.repository().commit();

        }

        if (Framework.isNotNullOrEmpty(dto.getFiles())) {
            for (AttachmentDTO item : dto.getFiles()) {
                Attachment attachment = Attachment.createNew();
                ModelHelper.copyModel(item, attachment);
                attachment.setEntityId(reviewComments.getId());
                attachment.setEntityName(reviewComments.gainTypeName());
                this.repository().add(attachment);
            }
        }
        return reviewComments.getId();
    }

    //返回当前复核数据有没有回复数据，有返回ID,没有返回0
    @JsonIgnore
    @SystemService(args = "rcId,parentId")
    public Long getReplyId(Long rcId, long parentId) throws Exception {
        long id = 0;
        Optional<ReviewComments> rc;
        if (Framework.isNullOrEmpty(parentId) || (Framework.isNotNullOrEmpty(parentId) && parentId == 0)) {
            rc = this.stream().where(w -> w.getParentId() != null && w.getParentId().getId() == rcId && w.getQuestionType() == 20).findFirst();
        } else {
            rc = this.stream().where(w -> w.getParentId().getId() == parentId && w.getQuestionType() == 20).findFirst();
        }
        if (rc.isPresent()) {
            ReviewComments item = rc.get();
            id = item.getId();
        }
        return id;
    }

    @SystemService(args = "cid,pass")
    public void saveIsPass(Long cid, boolean pass) throws Exception {
        ReviewComments rc = repository().findById(cid, ReviewComments.class);
        rc.setIsPass(pass);
    }

    /**
     * 指定回复人 保存
     * @param cid
     * @throws Exception
     */
    @SystemService(args = "cid,val")
    public void saveDesignatedReviewerId(Long cid, Long val) throws Exception {
        ReviewComments rc = repository().findById(cid, ReviewComments.class);
        if(val ==0)
        {
            rc.setDesignatedReviewerId(null);
        }else{
            rc.setDesignatedReviewer(val);
        }

    }

    //删除
    @SystemService(args = "rcId")
    public void deleteRC(Long rcId) throws Exception {
        ReviewComments rc = repository().findById(rcId, ReviewComments.class);
        repository().remove(rc);
    }
}


