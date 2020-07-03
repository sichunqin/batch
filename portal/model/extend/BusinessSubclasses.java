package com.dxn.model.extend;

import com.dxn.dto.BusinessSubclassesDTO;
import com.dxn.model.entity.BusinessSubclassesEntity;
import com.dxn.system.ResponseEntity;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Base_BusinessSubclasses")
public class BusinessSubclasses extends BusinessSubclassesEntity {

    @SystemService()
    public ResponseEntity gainBusinessList() throws Exception {
        ResponseEntity response = new ResponseEntity();
        try {
            List<BusinessSubclassesDTO> list = new ArrayList<>();
            List<BusinessSubclasses> businesslist = this.stream().toList();
            if (businesslist.size() > 0) {
                for (BusinessSubclasses bls : businesslist) {
                    BusinessSubclassesDTO dto = new BusinessSubclassesDTO();
                    dto.setName(bls.getName());
                    dto.setParentName(bls.getBusinessCategory().getName());
                    list.add(dto);
                }
                response.setCode(200);
                response.setData(list);

            } else {
                response.setCode(200);
                response.setMessages("业务类型没有初始化数据！");
            }
        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages(ex.getMessage());
        }
        return response;
    }
}

