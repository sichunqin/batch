package com.dxn.model.extend;

import com.dxn.dto.BusinessSub7DTO;
import com.dxn.model.entity.BusinessSub7Entity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.exception.BusinessException;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "PM_BusinessSub7")
public class BusinessSub7 extends BusinessSub7Entity {

    @Override
    public void onValidate() throws Exception {
        if(Framework.isNotNullOrEmpty(this.getHoldRatio()) && (this.getHoldRatio() <= 0 || this.getHoldRatio() >100)){
            throw new BusinessException("持股比例只能在0和100之间！");
        }
    }
    @SystemService(args = "prjId")
    public List<BusinessSub7DTO> gainBusinessSub7(Long prjId) throws Exception {
        List<BusinessSub7> list = this.stream().where(w -> w.getBusinessEvaluationId().getProjectId().getId() == prjId).toList();
        for (BusinessSub7 item : list) {
            repository().remove(item);
        }
        List<BusinessSub7DTO> dtos = new ArrayList<>();
        List<CompositionCustomer> customerList = CompositionCustomer.stream().where(w -> w.getProjectId().getId() == prjId && w.getParentId() != null ).toList();
        if (customerList.size() > 0) {
            for (CompositionCustomer item : customerList) {
                BusinessSub7DTO dto = new BusinessSub7DTO();
                dto.setName(item.getName());
                dtos.add(dto);
            }
        }
        return dtos;
    }

}
