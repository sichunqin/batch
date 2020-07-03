package com.dxn.model.extend;

import com.dxn.model.entity.ProjectExperienceEntity;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "HR_StaffProjectExperience")
public class ProjectExperience extends ProjectExperienceEntity {

    @Override
    public void onValidate() throws Exception {
        //业务代码
        super.onValidate();
//        CustomerEntity customer= new CustomerEntity();
//        Integer aa=customer.getCustomerNature();
//        if (aa==10||aa==30||aa==40){
//            //查询id对应主项目和客户类型和业务类型和是否CPA
//        }
    }



}
