package com.dxn.model.extend;

import com.dxn.model.entity.HomeFormConfigurationEntity;
import com.dxn.system.Framework;

import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "Sys_HomeFormConfiguration")
public class HomeFormConfiguration extends HomeFormConfigurationEntity {

    @Override
    public void onValidate() throws Exception {
        super.onValidate();
        //业务代码
        this.getFormPageId().getCode();

    }


}
