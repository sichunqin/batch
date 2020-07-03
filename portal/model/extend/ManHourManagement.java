package com.dxn.model.extend;

import com.dxn.dto.EnumerationDTO;
import com.dxn.dto.ManpowerHoursDTO;
import com.dxn.dto.WorkingSoursChildrenDTO;
import com.dxn.model.entity.ManHourManagementEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.context.AppContext;
import com.dxn.system.dto.workFlow.MyTodoListDTO;
import com.dxn.system.dto.workFlow.WorkFlowNode;
import com.dxn.system.exception.BusinessException;
import com.dxn.system.typeFinder.TypeFinderHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "HR_ManHourManagement")
public class ManHourManagement extends ManHourManagementEntity {


    //验证方法
    @Override
    public void onValidate() throws Exception {
        super.onValidate();
        duplicateCheck();
        if (Framework.isNullOrEmpty(this.getDocState())) {
            this.setDocState(10);
        }
    }

    @Override
    public void onInserted() throws Exception {
        super.onInserted();
        this.setDept(AppContext.current().getProfile().getDeptId());
        askingForTimeAnd(this.getId());
        //业务代码
    }
    @Override
    public void onUpdated() throws Exception {
        super.onUpdated();
        askingForTimeAnd(this.getId());
        //业务代码
    }




    private void askingForTimeAnd(Long id) throws Exception {
        List<WorkingSoursChildren> workingSoursChildrenList = WorkingSoursChildren.stream().where(w -> w.getManHourManagementId().getId() == id).toList();
        double totalTime=0;
        Integer hour=0;
        for (WorkingSoursChildren item:workingSoursChildrenList){
            if (Framework.isNotNullOrEmpty(item.getCompositionCustomerId())){
                Integer correspondingTime=item.getCorrespondingTime();
                if (correspondingTime==20){
                    totalTime=totalTime+1;
                }else
                if (correspondingTime==10){
                    totalTime=totalTime+0.5;
                }else {
                    hour=hour+correspondingTime;
                }
            }

        }
        String totalTimeTotal = totalTime + "天" + hour + "小时";
        this.setTotalTime(totalTimeTotal);
    }

    private void duplicateCheck() throws Exception {
        Long userId = this.getUserId().getId();
        String period = this.getPeriod();
        Long count = 0L;
        Long id = this.getId();
        if (Framework.isNullOrEmpty(id)) {
            count = ManHourManagement.stream().where(w -> w.getUserId().getId() == userId && w.getPeriod().equals(period)).count();
        } else {
            count = ManHourManagement.stream().where(w -> w.getUserId().getId() == userId && w.getPeriod().equals(period) && !w.getId().equals(id)).count();
        }
        if (count > 0) throw new BusinessException("已存在期间" + period + "的工时信息，不能重复填报工时！");
    }

    @SystemService(args = "userId")
    public List<ManpowerHoursDTO> GetCustomerByCustomerId(Long userId) throws Exception {
        List<ManpowerHoursDTO> investmentDTOList = new ArrayList<>();

        Optional<User> ugroupList = User.stream().where(w -> w.getId() == userId).findFirst();
        if (ugroupList.isPresent()) {
            User user = ugroupList.get();
            Long staffId = user.getStaffId();
            Optional<Staff> staffFirst = Staff.stream().where(w -> w.getId() == staffId).findFirst();
            if (staffFirst.isPresent()) {
                Staff staff = staffFirst.get();
                ManpowerHoursDTO investmentDTO = new ManpowerHoursDTO();
                investmentDTO.setManHourNumber("自动生成");
                LocalDateTime dateTime = LocalDateTime.now();
                investmentDTO.setCreatedOn(dateTime);
                String name = staff.getName();
                String employeeNumber = staff.getEmployeeNumber();
                investmentDTO.setNameOfEmployee(employeeNumber + "-" + name);
//                List<EnumerationDTO> enumerationDTOList=new ArrayList<>();
//                List<TimeInterval> timeIntervalList=TimeInterval.stream().where(w -> w.getWorkingPeriodId().getYear().equals(2019)&&w.getState()==10).toList();
//                for (TimeInterval item:timeIntervalList){
//                    EnumerationDTO enumerationDTO=new EnumerationDTO();
//                    String periodField=item.getPeriodField();
//                    enumerationDTO.setId(periodField);
//                    enumerationDTO.setText(periodField);
//                    enumerationDTOList.add(enumerationDTO);
//                }
                //investmentDTO.setPeriod(enumerationDTOList);
                investmentDTO.setUserId(userId);
                investmentDTOList.add(investmentDTO);
            } else if (userId == 2) {
                ManpowerHoursDTO investmentDTO = new ManpowerHoursDTO();
                investmentDTO.setManHourNumber("自动生成");
                LocalDateTime dateTime = LocalDateTime.now();
                investmentDTO.setCreatedOn(dateTime);
                investmentDTO.setNameOfEmployee("admin-2");
//                List<EnumerationDTO> enumerationDTOList=new ArrayList<>();
//                List<TimeInterval> timeIntervalList=TimeInterval.stream().where(w -> w.getWorkingPeriodId().getYear().equals(2019)&&w.getState()==10).toList();
//                for (TimeInterval item:timeIntervalList){
//                    EnumerationDTO enumerationDTO=new EnumerationDTO();
//                    String periodField=item.getPeriodField();
//                    enumerationDTO.setId(periodField);
//                    enumerationDTO.setText(periodField);
//                    enumerationDTOList.add(enumerationDTO);
//                }
//                investmentDTO.setPeriod(enumerationDTOList);
                investmentDTO.setUserId(userId);
                investmentDTOList.add(investmentDTO);
            }
        }
        return investmentDTOList;
    }

    /**
     * 切割時間段
     */
    @SystemService(args = "timeString")
    public List<EnumerationDTO> cutDate(String timeString) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (Framework.isNullOrEmpty(timeString)) throw new BusinessException("时间获取错误");
        String start = timeString.substring(0, 10);
        String end = timeString.substring(11, 21);
        Date dBegin = sdf.parse(start);
        Date dEnd = sdf.parse(end);
        return findDates("D", dBegin, dEnd);
    }

    /**
     * 工时填写添加
     */
    @SystemService(args = "period")
    public List<WorkingSoursChildrenDTO> timeFillInAdd(String period) throws Exception {
        List<WorkingSoursChildrenDTO> workingSoursChildrenDTOList = new ArrayList<>();
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (Framework.isNullOrEmpty(period)) return null;
        String start = period.substring(0, 10);
        LocalDateTime startTime = (LocalDateTime) TypeFinderHelper.toConvert(start, LocalDateTime.class);
        String end = period.substring(11, 21);
        LocalDateTime endTime = (LocalDateTime) TypeFinderHelper.toConvert(end, LocalDateTime.class);
        //Date dBegin = sdf.parse(start);
        //Date dEnd = sdf.parse(end);
        List<LocalDateTime> localDateTimeList = dateSplit(startTime, endTime);
        //List<EnumerationDTO> enumerationDTOList = findDates("D", dBegin, dEnd);
        for (LocalDateTime item : localDateTimeList) {
            WorkingSoursChildrenDTO workingSoursChildrenDTO = new WorkingSoursChildrenDTO();
            workingSoursChildrenDTO.setManHourDate(item);//(LocalDateTime) TypeFinderHelper.toConvert(item.getText(), LocalDateTime.class)
            workingSoursChildrenDTO.setTime(10);
//            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            String time=item.getText()+" 00:00:00";
//            LocalDateTime ldt = LocalDateTime.parse(time,df);
//            WorkCalender workCalender = WorkCalender.createNew();
//            Integer a = workCalender.gainDateType(ldt);
            WorkCalender workCalender = WorkCalender.createNew();
            Integer a = workCalender.gainDateType(item);
            workingSoursChildrenDTO.setDateTypeField(a);
            workingSoursChildrenDTOList.add(workingSoursChildrenDTO);
        }
        return workingSoursChildrenDTOList;
    }


    public List<EnumerationDTO> findDates(String dateType, Date dBegin, Date dEnd) throws Exception {
        List<EnumerationDTO> listDate = new ArrayList<>();
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);
        Integer id = 10;
        EnumerationDTO enumerationDTO = new EnumerationDTO();
        enumerationDTO.setText(new SimpleDateFormat("yyyy-MM-dd").format(calBegin.getTime()));
        enumerationDTO.setId(new SimpleDateFormat("yyyy-MM-dd").format(calBegin.getTime()));
        listDate.add(enumerationDTO);
        do {
            calBegin.add(Calendar.DAY_OF_YEAR, 1);
            EnumerationDTO enumerationDTO1 = new EnumerationDTO();
            enumerationDTO1.setText(new SimpleDateFormat("yyyy-MM-dd").format(calBegin.getTime()));
            enumerationDTO1.setId(new SimpleDateFormat("yyyy-MM-dd").format(calBegin.getTime()));
            listDate.add(enumerationDTO1);
            id = id + 10;
        } while (calEnd.after(calBegin));
        return listDate;
    }

    @SystemService(args = "year")
    public List<EnumerationDTO> acquisitionPeriod(Integer year) throws Exception {

        List<EnumerationDTO> enumerationDTOList = new ArrayList<>();
        List<TimeInterval> timeIntervalList = TimeInterval.stream().where(w -> w.getWorkingPeriodId().getYear() == year && w.getState() == 10).toList();
        if (timeIntervalList.size() < 1) throw new BusinessException("请设置当前年度的工时期间");
        for (TimeInterval item : timeIntervalList) {
            EnumerationDTO enumerationDTO = new EnumerationDTO();
            String periodField = item.getPeriodField();
            enumerationDTO.setId(periodField);
            enumerationDTO.setText(periodField);
            enumerationDTOList.add(enumerationDTO);
        }
        return enumerationDTOList;
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

    @Override
    public void onEndWorkflow(WorkFlowNode node) throws Exception {//结束时触发
        super.onEndWorkflow(node);
        node.setName("结束");
        String conetnt = String.format("工时审批通过：%s", this.getPeriod());
        sendNotice(this, conetnt, this.getNameOfEmployee());
    }

    private void sendNotice(ManHourManagement manHourManagement, String title, String conetnt) throws Exception {
        Notice notice = Notice.createNew();
        List<Long> users = new ArrayList<>();
        users.add(manHourManagement.getCreatedById());
        notice.createNotice(AppContext.current().getProfile().getId(), users, title, conetnt, manHourManagement.getId(), "ManHourManagement", null);
    }

    @JsonIgnore
    public MyTodoListDTO gainWorkFlowDTO(MyTodoListDTO value) throws Exception {
        value.setDepartmentType(this.getManHourNumber());
        value.setWorkFlowName(value.getProcess_Id());
        return value;
    }

}
