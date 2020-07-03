package com.dxn.model.extend;

import com.dxn.model.entity.BusinessSub20Entity;
import com.dxn.system.Framework;
import com.dxn.system.exception.BusinessException;

import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "PM_BusinessSub20")
public class BusinessSub20 extends BusinessSub20Entity {
    @Override
    public void onValidate() throws Exception {
       /* if(Framework.isNullOrEmpty(this.getApplicable20()))
        {
            throw new BusinessException("请先选择是否适用！");
        }
        if(this.getApplicable20().equals(true) &&(Framework.isNullOrEmpty(this.getCode()) || Framework.isNullOrEmpty(this.getName()))){
            throw new BusinessException("请填写 技术与风险控制委员会会议记录 中的纪要索引号或纪要名称！");
        }else if (this.getApplicable20().equals(true))
        {
            if(Framework.isNotNullOrEmpty(this.getCode()) && this.getCode().trim().equals(""))
            {
                throw new BusinessException("请填写 技术与风险控制委员会会议记录 中的纪要索引号！");
            }
            if(Framework.isNotNullOrEmpty(this.getName()) && this.getName().trim().equals(""))
            {
                throw new BusinessException("请填写 技术与风险控制委员会会议记录 中的纪要名称！");
            }
        }*/
    }
}
