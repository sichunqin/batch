package com.dxn.model.extend;

import com.dxn.model.entity.WorkCalenderEntity;
import com.dxn.system.Framework;
import com.dxn.system.exception.BusinessException;
import com.dxn.system.typeFinder.TypeFinderHelper;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Sys_WorkCalender")
public class WorkCalender extends WorkCalenderEntity {
    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
    }

    @Override
    public void onValidate() throws Exception {
        super.onValidate();
        Integer year = this.getYear();
        Long count;
        if (Framework.isNullOrEmpty(this.getId())) {
            count = this.stream().where(d -> d.getYear() == year).count();
        } else {
            Long id = this.getId();
            count = this.stream().where(d -> d.getYear() == year && !d.getId().equals(id)).count();
        }
        if (count > 0) {
            throw new BusinessException("已存在当前年度的日历");
        }
    }

    @Override
    public void onInserted() throws Exception {
        super.onInserted();

        LocalDateTime sDate = (LocalDateTime) TypeFinderHelper.toConvert(String.format("%s-01-01", this.getYear()), LocalDateTime.class);
        LocalDateTime eDate = (LocalDateTime) TypeFinderHelper.toConvert(String.format("%s-12-31", this.getYear()), LocalDateTime.class);

        List<LocalDateTime> lists = dateSplit(sDate, eDate);
        if (lists.isEmpty()) return;
        StringBuilder sql = new StringBuilder();
        WorkYearDate yearDate = WorkYearDate.createNew();

        for (LocalDateTime item : lists) {
            int week = item.getDayOfWeek().getValue();
            int dateType = week == 6 || week == 7 ? 20 : 10;//工作日
            yearDate.setWorkCalender(this.getId());
            yearDate.setYear(item.getYear());
            yearDate.setMonth(item.getMonthValue());
            yearDate.setDateType(dateType);
            yearDate.setWeek(week * 10);
            yearDate.setWorkDate(item);
            yearDate.setUuid(Framework.createGuid().toString());
            sql.append(yearDate.toInsertSql());
        }
        if (Framework.isNotNullOrEmpty(sql)) {
            this.repository().executeCommand(sql.toString());
        }
    }

    private static List<LocalDateTime> dateSplit(LocalDateTime start, LocalDateTime end) throws Exception {

        List<LocalDateTime> dateList = new ArrayList<>();

        while (true) {
            dateList.add(start);
            start = start.plusDays(1);
            if (start.isAfter(end)) break;
        }

        return dateList;
    }

    /**
     * 校验该年是否生成工作日历
     *
     * @param year --年度
     */
    public void validWorkCalender(Integer year) throws Exception {
        Optional<WorkCalender> first = this.stream().where(d -> d.getYear() == year).findAny();
        if (!first.isPresent())
            throw new BusinessException(String.format("请先设置[%s]年度日历!", year));
    }

    /**
     * 获取日期对应的类型
     *
     * @param date --日期
     * @return 10:工作日，20:休息日，30:节假日
     */
    public Integer gainDateType(LocalDateTime date) throws Exception {
        int year = date.getYear();
        Optional<WorkCalender> first = this.stream().where(d -> d.getYear() == year).findFirst();
        if (first.isPresent()) {
            Integer dateType = 10;
            WorkCalender workCalender = first.get();
            Optional<WorkSetDate> dateFirst = workCalender.getWorkSetDates().stream().filter(d -> d.getWorkDate().equals(date)).findFirst();
            if (dateFirst.isPresent()) {
                dateType = dateFirst.get().getDateType();
            } else {
                Optional<WorkYearDate> yearFirst = workCalender.getWorkYearDates().stream().filter(d -> d.getWorkDate().equals(date)).findFirst();
                if (yearFirst.isPresent()) {
                    dateType = yearFirst.get().getDateType();
                }
            }

            return dateType;

        } else {
            throw new BusinessException(String.format("请先设置[%s]年度日历!", year));
        }
    }
}
