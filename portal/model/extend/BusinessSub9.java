package com.dxn.model.extend;

import com.dxn.model.entity.BusinessSub9Entity;
import com.dxn.system.Framework;
import com.dxn.system.exception.BusinessException;

import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "PM_BusinessSub9")
public class BusinessSub9 extends BusinessSub9Entity {
    @Override
    public void onValidate() throws Exception {
        if(Framework.isNotNullOrEmpty(this.getHoldRatio()) && (this.getHoldRatio() <= 0 || this.getHoldRatio() >100)){
            throw new BusinessException("持股比例只能在0和100之间！");
        }
    }
}
