package com.dxn.model.extend;

import com.dxn.model.entity.EmployeeGeneralEnclosureEntity;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "HR_EmployeeGeneralEnclosure")
public class EmployeeGeneralEnclosure extends EmployeeGeneralEnclosureEntity {




    @SystemService(args = "type")
    public void insertProjectMemeber(String type) throws Exception {
        if(type.equals(10)) this.setFlag(10);
        if(type.equals(20)) this.setFlag(20);
        if(type.equals(30)) this.setFlag(30);
        if(type.equals(40)) this.setFlag(40);
        if(type.equals(50)) this.setFlag(50);
        if(type.equals(60)) this.setFlag(60);
    }

}
