package com.dxn.model.extend;

import com.dxn.dto.FunctionPointUploadControlDTO;
import com.dxn.dto.functionPointQueryDTO;
import com.dxn.model.entity.FunctionPointUploadControlEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.exception.BusinessException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Base_FunctionPointUploadControl")
public class FunctionPointUploadControl extends FunctionPointUploadControlEntity {

    @SystemService(args = "entityCode")
    public functionPointQueryDTO functionPointQuery(String entityCode) throws Exception {
        functionPointQueryDTO dto = new functionPointQueryDTO();
        Optional<FunctionPointUploadControl> entityData = FunctionPointUploadControl.stream().where(c -> c.getMainEntityMapId().getCode().equals(entityCode)).findFirst();
        if (entityData.isPresent()) {
            dto.setFileType(entityData.get().getFileType());
            dto.setFileSize(Framework.isNotNull(entityData.get().getUnitFileSize()) ? entityData.get().getUnitFileSize() : gainNofuntionData());
        } else {
            dto.setFileSize(gainNofuntionData());
        }
        return dto;
    }

    private String gainNofuntionData() throws Exception {
        Optional<NoFunctionalUploadControl> nofuntionData = NoFunctionalUploadControl.stream().findFirst();
        if (nofuntionData.isPresent())
            return nofuntionData.get().getFileSize();
        return "";
    }


    //验证方法
//    @Override
//    public void onValidate() throws Exception {
//        Long number = this.getMainEntityMapId().getId();
//        if (Framework.isNotNullOrEmpty(number)) {
//            Long count = 0L;
//            Long id = this.getId();
//            if (Framework.isNullOrEmpty(this.getId())) {
//                count = this.stream().where(w -> w.getMainEntityMapId().getId()==number).count();
//            } else {
//                count = this.stream().where(w -> w.getMainEntityMapId().getId()==number && !w.getId().equals(id)).count();
//            }
//            if (count > 0) throw new BusinessException("实体重复");
//        }
//    }


    //保存按钮
    @SystemService(args = "functionPointUploadControlList")
    public void savaFunctionPointUpload(List<FunctionPointUploadControlDTO> functionPointUploadControlList) throws Exception {
        for (FunctionPointUploadControlDTO item:functionPointUploadControlList){
            Long  itemId=item.getId();
            Optional<FunctionPointUploadControl>   functionPointUploadControlFirst=this.stream().where(c->c.getId()==itemId).findFirst();
            if (functionPointUploadControlFirst.isPresent()){
                FunctionPointUploadControl functionPointUploadControl=functionPointUploadControlFirst.get();
                functionPointUploadControl.setFileType(item.getFileType());
                functionPointUploadControl.setUnitFileSize(item.getUnitFileSize());
                repository().add(functionPointUploadControl);
            }
        }
        repository().commit();
    }
}