package com.dxn.model.extend;

import com.dxn.dto.NewsHomeDTO;
import com.dxn.model.entity.NoticeEntity;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.context.AppContext;
import com.dxn.weChat.WeChatHelper;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Sys_Notice")
public class Notice extends NoticeEntity {

    /**
     * 创建消息
     *
     * @param fromId 发送人，List<Long>  接受人，title 标题 ，content 内容 ，entityId  业务实体Id，entityType 业务实体类型,Status 是否显示状态(null或10显示，否则隐藏),SpecialConditions 特殊条件
     * @return
     */
    public void createNotice(Long fromId, List<Long> toIdList, String title, String content, Long entityId, String entityType, Integer Status) throws Exception {
        createNotice(fromId, toIdList, title, content, entityId, entityType, Status, "");
    }

    public void createNotice(Long fromId, List<Long> toIdList, String title, String content, Long entityId, String entityType, Integer Status, String SpecialConditions) throws Exception {
        if (Framework.isNullOrEmpty(Status))
            Status = 10;
        for (Long item : toIdList) {
            Notice notice = Notice.createNew();
            notice.setFrom(fromId);
            notice.setTo(item);
            notice.setTitle(title);
            notice.setContent(content);
            notice.setEntityId(entityId);
            notice.setEntityType(entityType);
            notice.setStatus(20);
            notice.setShowStatus(Status);
            notice.setSpecialConditions(SpecialConditions);
            notice.setBeginTime(LocalDateTime.now());
            notice.setEndTime(LocalDateTime.now());
            this.repository().add(notice);
        }

        //向类型为30（消息类型）的应用中发送消息
        WeChatHelper.sendTextMessage(30, content, toIdList);
    }

    /**
     * 修改当前人所有消息状态
     *
     * @return
     */
    @SystemService()
    public void uptNoticeStatus() throws Exception {
        Long userId = AppContext.current().getProfile().getId();
        List<Notice> NoticeList = this.stream().where(n -> n.getToId().getId() == userId && n.getStatus() == 20).toList();
        for (Notice item : NoticeList) {
            item.setStatus(10);
        }
    }

    /**
     * 修改当前消息状态
     *
     * @param id 消息Id
     * @return
     */
    @SystemService(args = "id")
    public void uptNoticeStatusFirst(Long id) throws Exception {
        if (Framework.isNullOrEmpty(id)) return;
        Notice notice = this.findById(id);
        notice.setStatus(10);
    }

    /**
     * 获取当前人未读消息条数
     *
     * @return
     */
    @SystemService()
    public Long gainNoticeNumberByUser() throws Exception {
        Long userId = AppContext.current().getProfile().getId();
        return this.stream().where(n -> n.getToId().getId() == userId && n.getStatus() == 20 && n.getShowStatus() == 10).count();
    }

    @SystemService()
    public List<NewsHomeDTO> gainHomeNewsList() throws Exception {
        Long userId = AppContext.current().getProfile().getId();
        List<Notice> notices = this.stream().where(n -> n.getToId().getId() == userId).limit(5).toList();
        List<NewsHomeDTO> list = new ArrayList<>();

        for (Notice item : notices) {
            NewsHomeDTO dto = new NewsHomeDTO();
            ModelHelper.copyModel(item, dto);
            dto.setName(item.getTitle());
            list.add(dto);
        }

        return list;
    }
}
