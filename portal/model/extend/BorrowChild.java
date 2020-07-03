package com.dxn.model.extend;

import com.dxn.dto.BorrowChildDTO;
import com.dxn.dto.BorrowGrandsonDTO;
import com.dxn.dto.enums.Constants;
import com.dxn.model.entity.BorrowChildEntity;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.context.AppContext;
import com.dxn.system.dto.EntityDataDTO;
import com.dxn.system.dto.workFlow.WorkFlowNode;
import com.dxn.system.exception.BusinessException;
import com.dxn.system.typeFinder.TypeFinderHelper;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.ZoneId;
import java.util.*;

@Entity
@Table(name = "prj_BorrowChild")
public class BorrowChild extends BorrowChildEntity {

    @Override
    public void onValidate() throws Exception {
        super.onValidate();
        if(Framework.isNotNullOrEmpty(this.getOriginalValue())) return;
        //  if ((Framework.isNullOrEmpty(this.getElectronicDraft()) || !this.getElectronicDraft()) && (Framework.isNullOrEmpty(this.getPaperDraft()) || !this.getPaperDraft()))
        //     throw new BusinessException("请在要借阅的项目后，勾选电子底稿或纸质底稿！");

        if (Framework.isNotNullOrEmpty(this.getPaperDraft()) && this.getPaperDraft() && (Framework.isNullOrEmpty(this.getBorrowNumbers()) || this.getBorrowNumbers() <= 0))
            throw new BusinessException(" 请选择需要借阅的纸质底稿借阅盒数！");

        if (Framework.isNotNullOrEmpty(this.getElectronicDraft()) && this.getElectronicDraft()) {
            //校验 当前人是否借阅过当前项目的电子底稿
            Long user = AppContext.current().getProfile().getId();
            Long archivedId = this.getManuScirptItemArchiveId().getId();
            Long id = this.getId();

            Long count = 0L;
            if (Framework.isNullOrEmpty(this.getId())) {
                count = this.stream().where(w -> w.getElectronicDraft() == true
                        && w.getManuScirptItemArchiveId().getId() == archivedId && w.getCreatedById() == user
                        && w.getDocState() != null && (w.getDocState() == 10 || w.getDocState() == 20)).count();
            } else {
                count = this.stream().where(w -> w.getElectronicDraft() == true
                        && w.getManuScirptItemArchiveId().getId() == archivedId && w.getCreatedById() == user
                        && w.getDocState() != null && (w.getDocState() == 10 || w.getDocState() == 20) && !w.getId().equals(id)).count();
            }

            if (count > 0) {
                throw new BusinessException(" 当前登录人已经借阅过当前档案的电子底稿，不能借阅！");
            }

        }
    }

    @Override
    public void onEndWorkflow(WorkFlowNode node) throws Exception {//流程结束时触发
        super.onEndWorkflow(node);
        endWorkflow(this);
    }

    private void endWorkflow(BorrowChild child) throws Exception {
        //流程结束时 更新  myUpdate 中修改了归档表中的 setBorrowNumbers（借出数量）
        child.initialization();
        List<BorrowGrandson> sons = child.getBorrowGrandsons();

        GregorianCalendar gs = new GregorianCalendar();
        gs.setTime(new Date());
        child.setActualReturnDate(gs.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        if (Framework.isNotNullOrEmpty(sons)) {
            for (BorrowGrandson son : sons) {
                son.setBoxState(son.getBoxAndNumberBookId().getId(), Constants.NOTLEND);
            }
        }
    }

    @Override
    public void onInserted() throws Exception {
        super.onInserted();
        this.setDeptId(this.getBorrowMasterId().getDeptId());
        myUpdate();
    }

    @Override
    public void onUpdating() throws Exception {
        super.onUpdating();
        myUpdate();
    }

    /*
     * 插入或者更新
     * GBY
     * */
    private void myUpdate() throws Exception {
        BorrowChild oldchild = this.getOriginalValue();
        //更新借出数量
        if (Framework.isNullOrEmpty(oldchild) || (Framework.isNotNullOrEmpty(oldchild.getBorrowNumbers()) && !oldchild.getBorrowNumbers().equals(this.getBorrowNumbers()))
                || (Framework.isNotNullOrEmpty(oldchild.getDocState()) && !oldchild.getDocState().equals(this.getDocState()))) {
            Long archid = this.getManuScirptItemArchiveId().getId();
            // Integer num=this.stream().where(w->w.getManuScirptItemArchiveId().getId()==archid && w.getDocState() != null &&(w.getDocState() ==10 || w.getDocState() == 20) && w.getBorrowNumbers() != null).sumInteger(s->s.getBorrowNumbers()).intValue();
            Optional<ManuScirptItemArchive> optarc = ManuScirptItemArchive.stream().where(w -> w.getId() == archid).findFirst();
            if (optarc.isPresent()) {
                ManuScirptItemArchive arc = optarc.get();
                Integer num = arc.getBorrowNumbers();
                if (Framework.isNullOrEmpty(num)) {
                    num = 0;
                }
                if (Framework.isNullOrEmpty(oldchild)) //新增
                {
                    num += (Framework.isNullOrEmpty(this.getBorrowNumbers())) ? 0 : this.getBorrowNumbers();
                } else {
                    Integer old = (Framework.isNullOrEmpty(oldchild.getBorrowNumbers())) ? 0 : oldchild.getBorrowNumbers();
                    Integer newv;
                    if (Constants.Returned.equals(this.getDocState())) {
                        newv = 0;
                    } else {
                        newv = (Framework.isNullOrEmpty(this.getBorrowNumbers())) ? 0 : this.getBorrowNumbers();
                    }

                    num = num - old + newv;
                }
                arc.setBorrowNumbers(num);
            }

        }
        //更新预计归还日期
        if (Framework.isNullOrEmpty(oldchild)) {
            this.setBorrowDays(this.getBorrowMasterId().getBorrowDays());
            this.setBorrowReason(this.getBorrowMasterId().getBorrowReason());
            this.setExpectedReturnDate(this.getBorrowMasterId().getExpectedReturnDate());
            pushBoxAndNumberBook();
        } else if (Framework.isNotNullOrEmpty(oldchild.getBorrowDays())&&!oldchild.getBorrowDays().equals(this.getBorrowDays())) {
            GregorianCalendar gs = new GregorianCalendar();
            gs.setTime(Date.from(oldchild.getCreatedOn().atZone(ZoneId.systemDefault()).toInstant()));
            gs.add(5, this.getBorrowDays());
            this.setExpectedReturnDate(gs.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            pushBoxAndNumberBook();
        }

    }

    /**
     * GBY
     * 更新BoxAndNumberBook表中的归还日期
     */
    private void pushBoxAndNumberBook() {
        List<BorrowGrandson> sons = this.getBorrowGrandsons();
        if (Framework.isNotNullOrEmpty(sons)) {
            for (BorrowGrandson son : sons) {
                BoxAndNumberBook book = son.getBoxAndNumberBook();
                book.setEscheatDate(this.getExpectedReturnDate());
            }
        }

    }


    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();

        if (Framework.isNotNullOrEmpty(this.getBorrowGrandsons()))
            this.setBorrowNumbers(this.getBorrowGrandsons().size());
    }

    @SystemService(args = "query_projectId,query_borrowMasterId")
    public List<BorrowChildDTO> getBorrowChildDTOList(Long projectId, Long borrowMasterId) throws Exception {
        List<BorrowChildDTO> list = new ArrayList<>();
        //档案归档表
        List<ManuScirptItemArchive> archives = ManuScirptItemArchive.stream().where(w -> w.getCompostionCustomerId() != null && w.getCompostionCustomerId().getProjectId().getId() == projectId).sortedBy(f -> f.getCompostionCustomerId().getId()).toList();

        if (Framework.isNullOrEmpty(borrowMasterId)) {
            //新增的时候
            if (archives.size() > 0) {
                for (ManuScirptItemArchive item : archives) {
                    BorrowChildDTO dto = new BorrowChildDTO();
                    if (Framework.isNotNullOrEmpty(item.getCompostionCustomerId())) {
                        dto.setProjectName(item.getCompostionCustomerId().getShowName());
                    }
                    dto.setArchiveCode(item.getCode());
                    dto.setBoxNumber(item.getBoxNumber());
                    dto.setBorrowNumbers(item.getBorrowNumbers());
                    dto.setMainBorrowNumbers(item.getBorrowNumbers());
                    dto.setManuScirptItemArchiveId(item.getId());
                    dto.setUuid(Framework.createGuid().toString());
                    dto.setElectronicDraft(false);
                    dto.setPaperDraft(false);
                    list.add(dto);
                }
            }
        } else {
            //修改的时候
            List<BorrowChild> borrowChilds = this.stream().where(w -> w.getBorrowMasterId().getId() == borrowMasterId
             && (w.getElectronicDraft().booleanValue() == true || w.getPaperDraft().booleanValue() == true)).sortedBy(f -> f.getManuScirptItemArchiveId().getCompostionCustomerId().getId()).toList();
            if (borrowChilds.size() > 0) {
                for (BorrowChild item : borrowChilds) {
                    BorrowChildDTO dto = new BorrowChildDTO();
                    dto.setId(item.getId());
                    dto.setUuid(item.getUuid());
                    dto.setTimestamp(item.getTimestamp());
                    dto.setBorrowMasterId(item.getBorrowMasterId().getId());
                    dto.setMainBorrowNumbers(item.getManuScirptItemArchiveId().getBorrowNumbers());

                    ManuScirptItemArchive archive = item.getManuScirptItemArchiveId();
                    dto.setManuScirptItemArchiveId(archive.getId());
                    if (Framework.isNotNullOrEmpty(archive.getCompostionCustomerId())) {
                        dto.setProjectName(archive.getCompostionCustomerId().getShowName());
                    }
                    dto.setArchiveCode(archive.getCode());
                    dto.setBoxNumber(archive.getBoxNumber());
                    dto.setBorrowNumbers(item.getBorrowNumbers());

                    if (Framework.isNotNullOrEmpty(item.getBorrowNumbers()) && item.getBorrowNumbers() > 0) {
                        EntityDataDTO borrowDto = new EntityDataDTO();
                        borrowDto.setId(TypeFinderHelper.parseLong(item.getBorrowNumbers()));
                        borrowDto.setName(Framework.format("已选择盒数:{0}", item.getBorrowNumbers()));
                        dto.setBorrowData(borrowDto);
                    }

                    dto.setElectronicDraft(item.getElectronicDraft());
                    dto.setPaperDraft(item.getPaperDraft());

                    //处理孙表
                    List<BorrowGrandson> grandsons = item.getBorrowGrandsons();
                    if (Framework.isNotNullOrEmpty(grandsons) && grandsons.size() > 0) {
                        List<BorrowGrandsonDTO> sonList = new ArrayList();
                        for (BorrowGrandson grand : grandsons) {
                            BorrowGrandsonDTO sonDto = new BorrowGrandsonDTO();
                            ModelHelper.copyModel(grand, sonDto);
                            sonDto.setBorrowChildId(item.getId());
                            if (Framework.isNotNullOrEmpty(grand.getBoxAndNumberBookId()))
                                sonDto.setBoxAndNumberBookId(grand.getBoxAndNumberBookId().getId());
                            sonList.add(sonDto);
                        }
                        dto.setBorrowGrandsons(Framework.toJson(sonList));
                        dto.setSons(sonList);
                    }

                    list.add(dto);
                }
            }
        }

        return list;
    }

    /**
     * 我的借阅用到
     */
    @SystemService(args = "query_borrowChildId")
    public List<BorrowChildDTO> gainBorrowChildDTO(Long borrowChildId) throws Exception {
        BorrowChildDTO dto = new BorrowChildDTO();
        List<BorrowChildDTO> list = new ArrayList<>();

        Optional<BorrowChild> opchild = this.stream().where(w -> w.getId() == borrowChildId).findFirst();
        if (opchild.isPresent()) {
            BorrowChild child = opchild.get();
            dto.setId(child.getId());
            dto.setProjectName(child.getManuScirptItemArchiveId().getCompostionCustomerId().getShowName());
            dto.setArchiveCode(child.getManuScirptItemArchiveId().getCode());
            dto.setBoxNumber(child.getManuScirptItemArchiveId().getBoxNumber());
            dto.setMainBorrowNumbers(child.getManuScirptItemArchiveId().getBorrowNumbers());
            dto.setElectronicDraft(child.getElectronicDraft());
            dto.setPaperDraft(child.getPaperDraft());
            dto.setBorrowNumbers(child.getBorrowNumbers());
            dto.setBorrowMasterId(child.getBorrowMasterId().getId());
            dto.setManuScirptItemArchiveId(child.getManuScirptItemArchiveId().getId());

            if (Framework.isNotNullOrEmpty(child.getBorrowNumbers()) && child.getBorrowNumbers() > 0) {
                EntityDataDTO borrowDto = new EntityDataDTO();
                borrowDto.setId(TypeFinderHelper.parseLong(child.getBorrowNumbers()));
                borrowDto.setName(Framework.format("已选择盒数:{0}", child.getBorrowNumbers()));
                dto.setBorrowData(borrowDto);
            }


            BorrowGrandson son = BorrowGrandson.createNew();
            List<BorrowGrandsonDTO> sonList = new ArrayList();
            sonList = son.gainBorrowGrandsonDTOList(child.getManuScirptItemArchiveId().getId(), child.getId(), 1);
            dto.setSons(sonList);
            dto.setBorrowGrandsons(Framework.toJson(sonList));
            list.add(dto);
        }

        return list;
    }

    /**
     * 归还电子底稿，只需要修改状态即可
     *
     * @param childId
     * @throws Exception
     */
    @SystemService(args = "childId")
    public void returnElectronicDraft(Long childId) throws Exception {
        BorrowChild child = BorrowChild.findById(childId);

        child.setDocState(Constants.Returned);
        GregorianCalendar gs = new GregorianCalendar();
        gs.setTime(new Date());
        child.setActualReturnDate(gs.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

    /**
     * 归还纸质底稿，不走流程的
     * GBY
     */
    public  void returnPaperDraft(BorrowChild child) throws Exception{
        child.setDocState(Constants.Returned);
        GregorianCalendar gs = new GregorianCalendar();
        gs.setTime(new Date());
        child.setActualReturnDate(gs.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        endWorkflow(child);
    }

    /**
     * 批量归还底稿
     * GBY
     */
    @SystemService(args = "childIds")
    public void returnDraft(List<Long> childIds) throws Exception {
        if (Framework.isNullOrEmpty(childIds) || childIds.size() == 0) return;
        for (Long item : childIds) {
            BorrowChild child = BorrowChild.findById(item);
            if(child.getPaperDraft())
            {
                returnPaperDraft(child);
            }else{
                returnElectronicDraft(item);
            }

        }
    }

}
