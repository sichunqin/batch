package com.dxn.model.extend;

import com.dxn.dto.EnumerationDTO;
import com.dxn.dto.TimeIntervalDTO;
import com.dxn.model.entity.TimeIntervalEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.annotation.SystemTask;
import com.dxn.system.exception.BusinessException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Entity
@Table(name = "HR_TimeInterval")
public class TimeInterval extends TimeIntervalEntity {


    @SystemService(args = "startingDate,endDate,n")
    public List<TimeIntervalDTO> gainTimeInterval(String startingDate,String endDate,Long n)throws Exception {
        if (Framework.isNullOrEmpty(n))throw new BusinessException("时间间隔不能为空");
        if (Framework.isNullOrEmpty(startingDate))throw new BusinessException("起始时间不能重复");
        if (Framework.isNullOrEmpty(endDate))throw new BusinessException("结束时间不能重复");
        List<TimeIntervalDTO>  timeIntervalDTOList=new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long aLong = sdf.parse(startingDate).getTime();
        Long bLong = sdf.parse(endDate).getTime();
        while(aLong<bLong){
            TimeIntervalDTO timeIntervalDTO=new TimeIntervalDTO();
            timeIntervalDTO.setState(10);//状态
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(aLong);
            SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String startingDate1=format.format(gc.getTime());
            timeIntervalDTO.setStartingDate(startingDate1);//起始日期
            aLong=aLong+(n-1)*86400000;
            if (aLong<bLong){
                gc.setTimeInMillis(aLong);
            }else {
                gc.setTimeInMillis(bLong);
            }
            String startingDate2=format.format(gc.getTime());
            timeIntervalDTO.setEndDate(startingDate2);//结束日期
            String periodField=startingDate1+"至"+startingDate2;
            timeIntervalDTO.setPeriodField(periodField);
            timeIntervalDTO.setRemarks("");//备注
            timeIntervalDTOList.add(timeIntervalDTO);
            aLong=aLong+86400000;
        }
        return timeIntervalDTOList;
    }


    //任务定义锁时间
    @SystemTask(args = "n")
    public void restLoginTime(Integer n) throws Exception{
        this.getStartingDate();
        this.getEndDate();
        //把所有未锁定的全锁
        String closeSql=String.format("Update %s set State=20 where State=10",this.gainTableName());
        repository().executeCommand(closeSql);
        //当前时间
        LocalDateTime currentTime =LocalDateTime.now();
        List<TimeInterval>  timeIntervalList=this.stream().where(w -> w.getStartingDate().isBefore(currentTime)).sortedDescendingBy(w -> w.getStartingDate()).toList();
        //查到的长度
        int timeIntervalLength=timeIntervalList.size();
        //比较n和查的长度防止报错
        if (n>timeIntervalLength){
            for (TimeInterval timeInterval:timeIntervalList){
                timeInterval.setState(10);
                repository().add(timeInterval);
            }
        }else {
            for (int a=0;n>a;a++){
                TimeInterval timeInterval=timeIntervalList.get(a);
                timeInterval.setState(10);
                repository().add(timeInterval);
            }
        }
    }


}
