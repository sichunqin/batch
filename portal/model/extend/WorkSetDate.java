package com.dxn.model.extend;

import com.dxn.dto.workCalendar.WorkSetDateDTO;
import com.dxn.model.entity.WorkSetDateEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.exception.BusinessException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Sys_WorkSetDate")
public class WorkSetDate extends WorkSetDateEntity {
    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        this.setYear(this.getWorkDate().getYear());
        this.setMonth(this.getWorkDate().getMonthValue());
    }

    @Override
    public void onValidate() throws Exception {
        super.onValidate();
        LocalDateTime date = this.getWorkDate();
        Long count;
        if (Framework.isNullOrEmpty(this.getId())) {
            count = this.stream().where(d -> d.getWorkDate().equals(date)).count();
        } else {
            Long id = this.getId();
            count = this.stream().where(d -> d.getWorkDate().equals(date) && !d.getId().equals(id)).count();
        }
        if (count > 0) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            throw new BusinessException(String.format("已设置[%s]日期类型!", format.format(this.getWorkDate())));
        }
    }

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        String key = Framework.format("{0}_{1}_gainHolidays", this.getYear(), this.getMonth());
        AppHelper.manage().getCacheUtil().clear(key);
    }

    @SystemService(args = "year,month")
    public List<WorkSetDateDTO> gainHolidays(int year, int month) throws Exception {

        String key = Framework.format("{0}_{1}_gainHolidays", year, month);

        List<WorkSetDateDTO> gainList = AppHelper.manage().getCacheUtil().getList(key, () -> {
            try {
                List<WorkSetDateDTO> list = new ArrayList<>();
                List<WorkSetDate> workSetDates = WorkSetDate.stream().where(d -> d.getYear() == year && d.getMonth() == month).toList();
                if (workSetDates.size() > 0) {
                    for (WorkSetDate item : workSetDates) {
                        WorkSetDateDTO dto = new WorkSetDateDTO();
                        dto.setDateTime(item.getWorkDate());
                        dto.setDateType(item.getDateType());
                        list.add(dto);
                    }
                }
                return list;
            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }
        }, WorkSetDateDTO.class, CacheGroup.Data);

        return gainList;
    }

    /**
     * 设置日期
     *
     * @param date           --日期
     * @param dateType--日期类型 10:工作日，20:休息日，30:节假日
     */
    @SystemService(args = "date,dateType")
    public void putSetDateType(LocalDateTime date, int dateType) throws Exception {
        Optional<WorkSetDate> workSetDates = WorkSetDate.stream().where(d -> d.getWorkDate().equals(date)).findFirst();
        if (workSetDates.isPresent()) {
            WorkSetDate workSetDate = workSetDates.get();
            workSetDate.setDateType(dateType);
        } else {
            int year = date.getYear();
            Optional<WorkCalender> workCalender = WorkCalender.stream().where(d -> d.getYear() == year).findFirst();
            if (workCalender.isPresent()) {
                WorkSetDate workSetDate = this.createNew();
                workSetDate.setDateType(dateType);
                workSetDate.setWorkDate(date);
                workSetDate.setYear(date.getYear());
                workSetDate.setMonth(date.getMonthValue());
                workSetDate.setWorkCalender(workCalender.get().getId());
                this.repository().add(workSetDate);
            } else {
                throw new BusinessException(String.format("请先设置[%s]年度日历!", year));
            }
        }
    }
}
