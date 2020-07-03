package com.dxn.model.extend;

import com.alibaba.fastjson.JSONObject;
import com.dxn.dto.NewsHomeDTO;
import com.dxn.model.entity.AdviseEntity;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.configuration.DxnConfig;
import com.dxn.system.context.AppContext;
import com.dxn.system.io.FileHelper;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Table(name = "Sys_Advise")
public class Advise extends AdviseEntity {
    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
//        更新子表数据
        this.putReceiveDepts();

        if (Framework.isNullOrEmpty(this.getDeptId())) {
            this.setDept(AppContext.current().getProfile().getDeptId());
        }
        if (Framework.isNullOrEmpty(this.getReleaser())) {
            Long userid = AppContext.current().getProfile().getId();
            this.setReleaser(userid);
        }
        if (Framework.isNullOrEmpty(this.getReleaseDate())) {
            GregorianCalendar gs = new GregorianCalendar();
            gs.setTime(new Date());
            this.setReleaseDate(gs.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }


        String con = "<h2 style=\"text-align: center;\">" + this.getTitle() + "</h2><h4 style=\"text-align: center;\"><span style=\"color: rgb(127, 127, 127);\">发布人：" + this.getReleaser().getName()
                + "&nbsp; &nbsp; 发布单位：" + this.getDeptId().getName() + "&nbsp; &nbsp; 发布日期：" + this.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "</span></h4> <p><br/></p>\n";
//        this.setContentFinal(con + this.getContent());
        this.setContentFinal(con);
    }

    @Override
    public void onInserting() throws Exception {
        super.onInserting();
        if (Framework.isNullOrEmpty(this.getReleaseState())) {
            this.setReleaseState(20);
        }
        Long userid = AppContext.current().getProfile().getId();
        this.setReleaser(userid);
        this.setDept(AppContext.current().getProfile().getDeptId());
        //
        GregorianCalendar gs = new GregorianCalendar();
        gs.setTime(new Date());
        this.setReleaseDate(gs.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

    @Override
    public void onUpdating() throws Exception {
        super.onUpdating();
        Advise adv = this.getOriginalValue();
        //下发时 改下发日期
        if (Framework.isNotNullOrEmpty(adv) && Framework.isNotNullOrEmpty(this.getReleaseState()) && !this.getReleaseState().equals(adv.getReleaseState())
                && this.getReleaseState() == 10)//下发
        {
            GregorianCalendar gs = new GregorianCalendar();
            gs.setTime(new Date());
            this.setReleaseDate(gs.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        }
    }

    @SystemService()
    public List<NewsHomeDTO> gainHomeNewsList() throws Exception {

        String DeptName = "\"" + AppContext.current().getProfile().getDeptId() + "\"";
        List<Advise> notices = this.stream().where(w -> w.getReceiveDept().contains(DeptName) && w.getReleaseState() == 10).sortedDescendingBy(s -> s.getModifiedOn()).limit(10).toList();
        List<NewsHomeDTO> list = new ArrayList<>();

        String csfileUpload = "image.img?photoUrl=";


        for (Advise item : notices) {
            NewsHomeDTO dto = new NewsHomeDTO();
            ModelHelper.copyModel(item, dto);

            if (Framework.isNotNullOrEmpty(item.getHomeComponent()))
                dto.setImg(csfileUpload + item.getHomeComponent().replace("\\", "/"));


            dto.setName(item.getTitle());
            dto.setContent(Framework.clearHtml(item.getContent()));
            dto.setReleaseDate(item.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            list.add(dto);
        }

        return list;
    }

    // 更新子表数据
    private void putReceiveDepts() throws Exception {

        if (Framework.isNullOrEmpty(this.getReceiveDept())) return;

        List<Long> deptIds = Framework.parseJsonArray(this.getReceiveDept(), Long.class);

        List<AdviseReceiveDept> deleteList = new ArrayList();
        deleteList.addAll(this.getAdviseReceiveDepts());
        for (AdviseReceiveDept item : deleteList) {
            Long dept = item.getDeptId().getId();
            Optional<Long> id = deptIds.stream().filter(w -> w.equals(dept)).findFirst();
            if (!id.isPresent()) {
                this.getAdviseReceiveDepts().remove(item);
                this.repository().remove(item);
            }
        }


        for (Long id : deptIds) {
            Optional<AdviseReceiveDept> rec = this.getAdviseReceiveDepts().stream().filter(d -> d.getDeptId().getId().equals(id)).findFirst();
            if (!rec.isPresent()) {
                AdviseReceiveDept receive = AdviseReceiveDept.createNew();
                receive.setAdviseId(this);
                receive.setDept(id);
                this.getAdviseReceiveDepts().add(receive);
                this.repository().add(receive);
            }
        }
    }
}
