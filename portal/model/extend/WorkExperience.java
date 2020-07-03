package com.dxn.model.extend;

import com.dxn.model.entity.WorkExperienceEntity;
import com.dxn.system.exception.BusinessException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "HR_StaffWorkExperience")
public class WorkExperience extends WorkExperienceEntity {


    //验证方法
    @Override
    public void onValidate() throws Exception {
        //业务代码
        super.onValidate();
        this.timeValidation();
    }

    private  void timeValidation() throws Exception{
        LocalDateTime StartDate=this.getStartDate();
        LocalDateTime endTime=this.getEndTime();
        if (StartDate!=null) {
            if (!StartDate.isBefore(endTime)) {
                throw new BusinessException("开始时间不能大于结束时间");
            }
        }
    }
}
