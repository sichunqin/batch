package com.dxn.model.extend;

import com.dxn.dto.BorrowGrandsonDTO;
import com.dxn.dto.enums.Constants;
import com.dxn.model.entity.BorrowGrandsonEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "prj_BorrowGrandson")
public class BorrowGrandson extends BorrowGrandsonEntity {


    @Override
    public void onInserted() throws Exception {
        super.onInserted();
        myUpdate();
    }

    @Override
    public void onUpdating() throws Exception {
        super.onUpdating();
        myUpdate();
    }

    @Override
    public void onDeleted() throws Exception {
        super.onDeleted();
        myUpdate();
    }

    /**
     * 插入后更新 底稿盒数和册数（Prj_BoxAndNumberBook） 的 借出状态
     *
     * @throws Exception
     */
    private void myUpdate() throws Exception {
        BorrowGrandson oldson = this.getOriginalValue();
        if (Framework.isNullOrEmpty(oldson) || !oldson.getBoxAndNumberBookId().equals(this.getBoxAndNumberBookId())) //新增 或修改 底稿盒数和册数
        {
            Long boxid = this.getBoxAndNumberBookId().getId();
            setBoxState(boxid, Constants.LEND);
            if (Framework.isNotNullOrEmpty(oldson)) {
                Long boxid2 = oldson.getBoxAndNumberBookId().getId();
                setBoxState(boxid2, Constants.NOTLEND);
            }
        }
    }

    public void setBoxState(Long boxid, Integer state) throws Exception {
        Optional<BoxAndNumberBook> optbook = BoxAndNumberBook.stream().where(w -> w.getId() == boxid).findFirst();
        if (optbook.isPresent()) {
            BoxAndNumberBook book = optbook.get();
            book.setState(state);
            if (state == Constants.LEND) {
                book.setEscheatDate(this.getBorrowChildId().getExpectedReturnDate());
            }
        }
    }

    /*
    flag:0 走申请借阅打开逻辑  1：走我的借阅的只读逻辑 2:
     */
    @SystemService(args = "query_manuScirptItemArchiveId,query_borrowChildId,query_flag")
    public List<BorrowGrandsonDTO> gainBorrowGrandsonDTOList(Long manuScirptItemArchiveId, Long borrowChildId, Integer flag) throws Exception {
        List<BorrowGrandsonDTO> list = new ArrayList<>();
        List<BoxAndNumberBook> boxlist = BoxAndNumberBook.stream().where(w -> w.getManuScirptItemArchiveId().getId() == manuScirptItemArchiveId).sortedBy(s -> s.getCreatedOn()).toList();
        if (boxlist.size() > 0) {
            for (BoxAndNumberBook item : boxlist) {
                BorrowGrandsonDTO dto = new BorrowGrandsonDTO();
                dto.setBox(item.getBox() + "/共" + boxlist.size() + "盒");
                dto.setBookletRange(item.getBookRange() + "/共" + item.getManuScirptItemArchiveId().getBookNumber() + "册");
                dto.setContent(item.getContent());
                if (flag == 1) {
                    dto.setId(0L);
                } else if (flag == 2) {
                    dto.setId(-1L);
                }

                if (item.getState() == Constants.LEND) {//借出
                    if (Framework.isNullOrEmpty(borrowChildId))//新增
                    {
                        dto.setPrompt("纸质底稿已借出，预计归还时间：" + Framework.formatDateTime(item.getEscheatDate(), "yyy-MM-dd"));
                        dto.setReadOnlyState(true);
                    } else {
                        //借出 判断是被当前借阅借出，还是被其他借阅借出
                        Long itemid = item.getId();
                        Optional<BorrowGrandson> son = this.stream().where(w -> w.getBorrowChildId().getId() == borrowChildId && w.getBoxAndNumberBookId().getId() == itemid).findFirst();
                        if (son.isPresent()) {
                            //当前借阅申请 借出 打勾 不只读
                            dto.setReadOnlyState(false);
                            dto.setId(son.get().getId());
                            if (flag == 1 || flag == 2) {
                                dto.setPrompt("纸质底稿已借出，预计归还时间：" + Framework.formatDateTime(item.getEscheatDate(), "yyy-MM-dd"));
                            }

                        } else {
                            //被其他借阅借出  不打勾 只读

                            dto.setPrompt("纸质底稿已借出，预计归还时间：" + Framework.formatDateTime(item.getEscheatDate(), "yyy-MM-dd"));
                            dto.setReadOnlyState(true);
                        }
                    }


                } else {
                    dto.setReadOnlyState(false);
                }

                dto.setBoxAndNumberBookId(item.getId());
                dto.setUuid(Framework.createGuid().toString());
                list.add(dto);
            }
        }
        return list;

    }

    @SystemService(args = "query_manuScirptItemArchiveId,query_borrowChildId")
    public List<BorrowGrandsonDTO> getBorrowGrandsonDTOList(Long manuScirptItemArchiveId, Long borrowChildId) throws Exception {
        return gainBorrowGrandsonDTOList(manuScirptItemArchiveId, borrowChildId, 0);
    }

    /**
     * 借阅数量界面用到
     *
     * @param borrowChildId
     * @return
     * @throws Exception
     */
    @SystemService(args = "query_borrowChildId")
    public List<BorrowGrandsonDTO> gainBorrowingQuantity(Long borrowChildId) throws Exception {
        List<BorrowGrandsonDTO> list = new ArrayList<>();
        BorrowChild child = BorrowChild.findById(borrowChildId);
        List<BorrowGrandson> sons = child.getBorrowGrandsons();
        if (Framework.isNullOrEmpty(sons))
            return list;
        List<BoxAndNumberBook> boxlist = child.getManuScirptItemArchiveId().getBoxAndNumberBooks();
        for (BorrowGrandson son : sons) {
            BorrowGrandsonDTO dto = new BorrowGrandsonDTO();
            BoxAndNumberBook item = son.getBoxAndNumberBookId();
            dto.setBox(item.getBox() + "/共" + boxlist.size() + "盒");
            dto.setBookletRange(item.getBookRange() + "/共" + item.getManuScirptItemArchiveId().getBookNumber() + "册");
            dto.setContent(item.getContent());
            dto.setPrompt("纸质底稿已借出，预计归还时间：" + Framework.formatDateTime(item.getEscheatDate(), "yyy-MM-dd"));
            dto.setReadOnlyState(true);
            list.add(dto);
        }

        return list;
    }
}
