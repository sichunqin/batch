package com.dxn.model.extend;

import com.dxn.model.entity.TaskSchedulingEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.typeFinder.TypeFinderHelper;
import com.dxn.system.exception.BusinessException;


import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Base_TaskScheduling")
public class TaskScheduling extends TaskSchedulingEntity {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        this.setState(10);
        this.restNextRunTime();
    }

    //计算下一次可用时间
    public void restNextRunTime() throws Exception {
        if (Framework.isNullOrEmpty(this.getStartDate())) throw new BusinessException("开始日期不能为空!");
        if (Framework.isNullOrEmpty(this.getStartupTime())) throw new BusinessException("启动时间不能为空!");
        if (Framework.isNullOrEmpty(this.getPlanningType())) throw new BusinessException("计划类型不能为空!");
        //执行一次
        if (this.getPlanningType() == 20) {
            this.restNextRunTimeOne();
        } else {//重复执行
            if (Framework.isNullOrEmpty(this.getTriggerType())) throw new BusinessException("触发类型不能为空!");
            this.restNextRunTimeDay();
            this.restNextRunTimeWeek();
            this.restNextRunTimeMonth();
            this.setIsEnd(false);
        }
    }

    //执行一次
    public void restNextRunTimeOne() {
        if (this.getPlanningType() != 20) return;
        LocalDateTime runTime = gainConfigTime();
        LocalDateTime now = LocalDateTime.now();
        this.setIsEnd(runTime.isBefore(now));
        this.setNextRunTime(runTime);
    }

    //按天执行
    public void restNextRunTimeDay() throws Exception {
        if (this.getTriggerType() != 10) return;
        this.setNextRunTime(gainNextUseTimeDay());
    }

    //按月周执行
    public void restNextRunTimeWeek() throws Exception {
        if (this.getTriggerType() != 20) return;
        this.setNextRunTime(gainNextUseTimeWeek());
    }

    //按月执行
    public void restNextRunTimeMonth() throws Exception {
        if (this.getTriggerType() != 30) return;
        this.setNextRunTime(gainNextUseTimeMonth());
    }

    //返回配置时间
    private LocalDateTime gainConfigTime() {
        LocalDateTime startDate = this.getStartDate();
        Integer startupTime = this.getStartupTime();
        return startDate.plusSeconds(startupTime);
    }

    //计算下一天可用时间
    private LocalDateTime gainNextUseTime() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = this.getStartDate();
        LocalDateTime runTime = startDate.isAfter(now) ? startDate : Framework.getDate(now);
        runTime = runTime.plusSeconds(this.getStartupTime());
        if (runTime.isBefore(now)) runTime = runTime.plusDays(1);
        return runTime;
    }

    //计算下一天可用时间
    private LocalDateTime gainNextUseTimeDay() throws Exception {
        if (Framework.isNullOrEmpty(this.getIntervalDays())) throw new BusinessException("间隔天数不能为空!");
        LocalDateTime runTime = gainNextUseTime();
        if (this.getIntervalDays() <= 0) return runTime;
        LocalDateTime configTime = gainConfigTime();
        Duration duration = Duration.between(configTime, runTime);
        long days = duration.toDays();
        Long num = days % this.getIntervalDays();
        if (num == 0) return runTime;
        return runTime.plusDays(this.getIntervalDays() - num);
    }

    //计算下一周可用时间
    private LocalDateTime gainNextUseTimeWeek() throws Exception {
        LocalDateTime runTime = gainNextUseTime();
        for (int i = 1; i <= 7; i++) {
            if (gainIsWeekDay(runTime.getDayOfWeek())) break;
            runTime = runTime.plusDays(1);
        }
        return runTime;
    }

    //判断当前周
    private boolean gainIsWeekDay(DayOfWeek week) throws Exception {

        if (!(this.getMonday() || this.getTuesday() || this.getWednesday() || this.getThursday() || this.getFriday() || this.getSaturday() || this.getSunday()))
            throw new BusinessException("前选择要执行的周!");

        switch (week) {
            case MONDAY:
                return this.getMonday();
            case TUESDAY:
                return this.getTuesday();
            case WEDNESDAY:
                return this.getWednesday();
            case THURSDAY:
                return this.getThursday();
            case FRIDAY:
                return this.getFriday();
            case SATURDAY:
                return this.getSaturday();
            case SUNDAY:
                return this.getSunday();
            default:
                break;
        }
        return false;
    }

    //计算下一个月可用时间
    private LocalDateTime gainNextUseTimeMonth() throws Exception {
        LocalDateTime runTime = gainNextUseTime();
        LocalDateTime nextTime = gainNextUseTimeMonth(runTime);
        if (nextTime.isBefore(runTime)) return gainNextUseTimeMonth(runTime.plusMonths(1));
        return nextTime;
    }

    //计算下一个可用的月度时间
    private LocalDateTime gainNextUseTimeMonth(LocalDateTime runTime) throws Exception {
        LocalDateTime nowTime = Framework.getFirstDateTimeOfMonth(runTime);

        for (int i = 1; i <= 12; i++) {
            if (gainIsMonth(nowTime.getMonth())) break;
            nowTime = nowTime.plusMonths(1);
        }

        if (Framework.isNullOrEmpty(this.getMonthType())) throw new BusinessException("请选择月份类型!");

        //月份按天
        if (this.getMonthType() == 10) {
            if (Framework.isNullOrEmpty(this.getDayOfTheMonth())) throw new BusinessException("请选择这个月的第几天!");
            nowTime = nowTime.plusDays(this.getDayOfTheMonth() - 1);
        } else {
            if (Framework.isNullOrEmpty(this.getWeekOfTheMonth())) throw new BusinessException("请选择这个月的第几周!");
            //月份按周
            Integer weekOfTheMonth = this.getWeekOfTheMonth();
            switch (weekOfTheMonth) {
                case 10://第一周
                    nowTime = Framework.getFirstMondayDateTimeOfMonth(nowTime);
                    break;
                case 20://第二周
                    nowTime = Framework.getFirstMondayDateTimeOfMonth(nowTime).plusDays(7);
                    break;
                case 30://第三周
                    nowTime = Framework.getFirstMondayDateTimeOfMonth(nowTime).plusDays(14);
                    break;
                case 40://第四周
                    nowTime = Framework.getFirstMondayDateTimeOfMonth(nowTime).plusDays(21);
                    break;
                case 50://每个月的最后一周，可能是第五周，也可能是第四周
                    nowTime = Framework.getLastMondayDateTimeOfMonth(nowTime);
                    break;
                default:
                    break;
            }
        }

        //判断是否有效(当前月没有有效的日期，比如2月按天选择的31天，则2月不会执行)
        if (!nowTime.getMonth().equals(runTime.getMonth())) return gainNextUseTimeMonth(runTime.plusMonths(1));

        return nowTime;
    }

    //判断当前月
    private boolean gainIsMonth(Month month) throws Exception {

        if (!(this.getJanuary() || this.getFebruary() || this.getMarch() || this.getApril() || this.getMay() || this.getJune() || this.getJuly() || this.getAugust() || this.getSeptember() || this.getOctober() || this.getNovember() || this.getDecember()))
            throw new BusinessException("前选择要执行的月!");

        switch (month) {
            case JANUARY:
                return this.getJanuary();
            case FEBRUARY:
                return this.getFebruary();
            case MARCH:
                return this.getMarch();
            case APRIL:
                return this.getApril();
            case MAY:
                return this.getMay();
            case JUNE:
                return this.getJune();
            case JULY:
                return this.getJuly();
            case AUGUST:
                return this.getAugust();
            case SEPTEMBER:
                return this.getSeptember();
            case OCTOBER:
                return this.getOctober();
            case NOVEMBER:
                return this.getNovember();
            case DECEMBER:
                return this.getDecember();
            default:
                break;
        }
        return false;
    }

    //运行任务定义
    public void runAction() throws Exception {
        if (Framework.isNullOrEmpty(this.getTaskDefinitionId())) return;
        this.getTaskDefinitionId().runAction();
    }

    //检查执行当前任务调度
    public static void taskRun() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        List<TaskScheduling> taskList = TaskScheduling.stream().where(w -> w.getNextRunTime().isBefore(now) && w.getIsEnd() == false).toList();
        for (TaskScheduling item : taskList) {
            item.runAction();
            item.restNextRunTime();
        }
    }

    @SystemService()
    public String toEnable() {
        this.setState(10);
        return "启用完成!";
    }

    @SystemService()
    public String toDisabled() {
        this.setState(20);
        return "停用完成!";
    }

    @SystemService()
    public String Implement() throws Exception {
        this.runAction();
        return "执行成功!";
    }
}
