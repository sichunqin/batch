package com.dxn.model.entity;

import com.dxn.entity.EntityBase;
import com.dxn.system.Framework;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.dxn.system.typeFinder.TypeFinderHelper;
import org.springframework.format.annotation.DateTimeFormat;
import com.dxn.system.context.RepositoryContext;
import org.jinq.jpa.JPAJinqStream;
import javax.persistence.*;
import java.time.*;
import java.math.BigDecimal;

@MappedSuperclass
public class TaskSchedulingEntity extends EntityBase<com.dxn.model.extend.TaskScheduling> {

    /** 是否执行完成. */
    @Column(name = "isEnd", nullable = false)
    private boolean isEnd;

    public void setIsEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public boolean getIsEnd() {
        return this.isEnd;
    }

    /** 下次执行时间. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "nextRunTime", nullable = false)
    private LocalDateTime nextRunTime;

    public void setNextRunTime(LocalDateTime nextRunTime) {
        this.nextRunTime = nextRunTime;
    }

    public LocalDateTime getNextRunTime() {
        return this.nextRunTime;
    }

    /** 月份类型. */
    @Column(name = "monthType")
    private Integer monthType;

    public void setMonthType(Integer monthType) {
        this.monthType = monthType;
    }

    public Integer getMonthType() {
        return this.monthType;
    }

    /** 状态. */
    @Column(name = "state")
    private Integer state;

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return this.state;
    }

    /** 十二月. */
    @Column(name = "december")
    private Boolean december;

    public void setDecember(Boolean december) {
        this.december = december;
    }

    public Boolean getDecember() {
        return this.december;
    }

    /** 十一月. */
    @Column(name = "november")
    private Boolean november;

    public void setNovember(Boolean november) {
        this.november = november;
    }

    public Boolean getNovember() {
        return this.november;
    }

    /** 十月. */
    @Column(name = "october")
    private Boolean october;

    public void setOctober(Boolean october) {
        this.october = october;
    }

    public Boolean getOctober() {
        return this.october;
    }

    /** 九月. */
    @Column(name = "september")
    private Boolean september;

    public void setSeptember(Boolean september) {
        this.september = september;
    }

    public Boolean getSeptember() {
        return this.september;
    }

    /** 八月. */
    @Column(name = "august")
    private Boolean august;

    public void setAugust(Boolean august) {
        this.august = august;
    }

    public Boolean getAugust() {
        return this.august;
    }

    /** 七月. */
    @Column(name = "july")
    private Boolean july;

    public void setJuly(Boolean july) {
        this.july = july;
    }

    public Boolean getJuly() {
        return this.july;
    }

    /** 六月. */
    @Column(name = "june")
    private Boolean june;

    public void setJune(Boolean june) {
        this.june = june;
    }

    public Boolean getJune() {
        return this.june;
    }

    /** 五月. */
    @Column(name = "may")
    private Boolean may;

    public void setMay(Boolean may) {
        this.may = may;
    }

    public Boolean getMay() {
        return this.may;
    }

    /** 四月. */
    @Column(name = "april")
    private Boolean april;

    public void setApril(Boolean april) {
        this.april = april;
    }

    public Boolean getApril() {
        return this.april;
    }

    /** 三月. */
    @Column(name = "march")
    private Boolean march;

    public void setMarch(Boolean march) {
        this.march = march;
    }

    public Boolean getMarch() {
        return this.march;
    }

    /** 二月. */
    @Column(name = "february")
    private Boolean february;

    public void setFebruary(Boolean february) {
        this.february = february;
    }

    public Boolean getFebruary() {
        return this.february;
    }

    /** 一月. */
    @Column(name = "january")
    private Boolean january;

    public void setJanuary(Boolean january) {
        this.january = january;
    }

    public Boolean getJanuary() {
        return this.january;
    }

    /** 这个月的周数. */
    @Column(name = "weeksOfTheMonth")
    private Integer weeksOfTheMonth;

    public void setWeeksOfTheMonth(Integer weeksOfTheMonth) {
        this.weeksOfTheMonth = weeksOfTheMonth;
    }

    public Integer getWeeksOfTheMonth() {
        return this.weeksOfTheMonth;
    }

    /** 这个月的第几周. */
    @Column(name = "weekOfTheMonth")
    private Integer weekOfTheMonth;

    public void setWeekOfTheMonth(Integer weekOfTheMonth) {
        this.weekOfTheMonth = weekOfTheMonth;
    }

    public Integer getWeekOfTheMonth() {
        return this.weekOfTheMonth;
    }

    /** 这个月的第几天. */
    @Column(name = "dayOfTheMonth")
    private Integer dayOfTheMonth;

    public void setDayOfTheMonth(Integer dayOfTheMonth) {
        this.dayOfTheMonth = dayOfTheMonth;
    }

    public Integer getDayOfTheMonth() {
        return this.dayOfTheMonth;
    }

    /** 周日. */
    @Column(name = "sunday")
    private Boolean sunday;

    public void setSunday(Boolean sunday) {
        this.sunday = sunday;
    }

    public Boolean getSunday() {
        return this.sunday;
    }

    /** 周六. */
    @Column(name = "saturday")
    private Boolean saturday;

    public void setSaturday(Boolean saturday) {
        this.saturday = saturday;
    }

    public Boolean getSaturday() {
        return this.saturday;
    }

    /** 周五. */
    @Column(name = "friday")
    private Boolean friday;

    public void setFriday(Boolean friday) {
        this.friday = friday;
    }

    public Boolean getFriday() {
        return this.friday;
    }

    /** 周四. */
    @Column(name = "thursday")
    private Boolean thursday;

    public void setThursday(Boolean thursday) {
        this.thursday = thursday;
    }

    public Boolean getThursday() {
        return this.thursday;
    }

    /** 周三. */
    @Column(name = "wednesday")
    private Boolean wednesday;

    public void setWednesday(Boolean wednesday) {
        this.wednesday = wednesday;
    }

    public Boolean getWednesday() {
        return this.wednesday;
    }

    /** 周二. */
    @Column(name = "tuesday")
    private Boolean tuesday;

    public void setTuesday(Boolean tuesday) {
        this.tuesday = tuesday;
    }

    public Boolean getTuesday() {
        return this.tuesday;
    }

    /** 周一. */
    @Column(name = "monday")
    private Boolean monday;

    public void setMonday(Boolean monday) {
        this.monday = monday;
    }

    public Boolean getMonday() {
        return this.monday;
    }

    /** 间隔天数. */
    @Column(name = "intervalDays")
    private Integer intervalDays;

    public void setIntervalDays(Integer intervalDays) {
        this.intervalDays = intervalDays;
    }

    public Integer getIntervalDays() {
        return this.intervalDays;
    }

    /** 启动时间. */
    @Column(name = "startupTime", nullable = false)
    private int startupTime;

    public void setStartupTime(int startupTime) {
        this.startupTime = startupTime;
    }

    public int getStartupTime() {
        return this.startupTime;
    }

    /** 开始日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "startDate", nullable = false)
    private LocalDateTime startDate;

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    /** 触发类型. */
    @Column(name = "triggerType", nullable = false)
    private int triggerType;

    public void setTriggerType(int triggerType) {
        this.triggerType = triggerType;
    }

    public int getTriggerType() {
        return this.triggerType;
    }

    /** 计划类型. */
    @Column(name = "planningType", nullable = false)
    private int planningType;

    public void setPlanningType(int planningType) {
        this.planningType = planningType;
    }

    public int getPlanningType() {
        return this.planningType;
    }

    /** 任务定义. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "taskDefinitionId")
    private com.dxn.model.extend.TaskDefinition taskDefinitionId;

    public void setTaskDefinitionId(com.dxn.model.extend.TaskDefinition taskDefinitionId) {
        this.taskDefinitionId = taskDefinitionId;
    }

    public com.dxn.model.extend.TaskDefinition getTaskDefinitionId() {
        if (taskDefinitionId != null) taskDefinitionId.initialization();
        return this.taskDefinitionId;
    }

    @JsonIgnore
    public com.dxn.model.extend.TaskDefinition getTaskDefinitionIdReadOnly() {
        return this.taskDefinitionId;
    }

    @JsonIgnore
    public com.dxn.model.extend.TaskDefinition readTaskDefinitionId() {
        return this.taskDefinitionId;
    }

    public void setTaskDefinition(long id) throws Exception {
        this.setTaskDefinitionId(com.dxn.model.extend.TaskDefinition.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.TaskDefinition getTaskDefinition() {
        return this.getTaskDefinitionId();
    }

    @JsonIgnore
    public boolean getTaskDefinitionIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.taskDefinitionId);
    }

    public void setCreatedBy(com.dxn.model.extend.User entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setCreatedById(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.User getCreatedBy() throws Exception {
        if(Framework.isNullOrEmpty(this.getCreatedById())) return null;
        return com.dxn.model.extend.User.findById(this.getCreatedById());
    }

    public void setModifiedBy(com.dxn.model.extend.User entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setModifiedById(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.User getModifiedBy() throws Exception {
        if(Framework.isNullOrEmpty(this.getModifiedById())) return null;
        return com.dxn.model.extend.User.findById(this.getModifiedById());
    }

    @JsonIgnore
    public static com.dxn.model.extend.TaskScheduling findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.TaskScheduling.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TaskScheduling findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.TaskScheduling.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TaskScheduling findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.TaskScheduling.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TaskScheduling findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.TaskScheduling.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TaskScheduling createNew() throws Exception {
        com.dxn.model.extend.TaskScheduling entity = new com.dxn.model.extend.TaskScheduling();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.TaskScheduling> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.TaskScheduling.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_TaskScheduling";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "任务调度";
    }

}
