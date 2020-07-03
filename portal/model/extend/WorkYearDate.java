package com.dxn.model.extend;

import com.dxn.dto.WorkYearDateDTO;
import com.dxn.model.entity.WorkYearDateEntity;
import com.dxn.system.ResponseEntity;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Sys_WorkYearDate")
public class WorkYearDate extends WorkYearDateEntity {


    //独立性声明接口
    @SystemService
    public ResponseEntity GetSystemWorkYearDate() throws Exception {
        ResponseEntity response = new ResponseEntity();
        List<WorkYearDateDTO> workYearDateDTOList1 = new ArrayList<>();
        List<WorkYearDateDTO> workYearDateDTOList2 = new ArrayList<>();
        try {
            List<WorkSetDate> workSetDateList = WorkSetDate.stream().where(c ->c.getDateType()!=10).toList();
            for (WorkSetDate workSetDate : workSetDateList) {
                WorkYearDateDTO workYearDateDTO = new WorkYearDateDTO();
                workYearDateDTO.setDateType(workSetDate.getDateType());
                workYearDateDTO.setMonth(workSetDate.getMonth());
                workYearDateDTO.setWorkDate(workSetDate.getWorkDate());
                workYearDateDTO.setYear(workSetDate.getYear());
                workYearDateDTO.setID(workSetDate.getUuid());
                workYearDateDTOList1.add(workYearDateDTO);
            }
            List<WorkYearDate> workYearDateList = this.stream().where(c -> c.getDateType()!=10).toList();
            for (WorkYearDate item : workYearDateList) {
                boolean isYesNo= true;
                for (WorkYearDateDTO workYearDateDTO1 : workYearDateDTOList1) {
                    if (workYearDateDTO1.getWorkDate().isEqual(item.getWorkDate())) {
                        isYesNo=false;
                    }
                }
                if (isYesNo){
                    WorkYearDateDTO workYearDateDTO = new WorkYearDateDTO();
                    workYearDateDTO.setDateType(item.getDateType());
                    workYearDateDTO.setMonth(item.getMonth());
                    workYearDateDTO.setWeek(item.getWeek());
                    workYearDateDTO.setWorkDate(item.getWorkDate());
                    workYearDateDTO.setYear(item.getYear());
                    workYearDateDTO.setID(item.getUuid());
                    workYearDateDTOList2.add(workYearDateDTO);
                }
            }
            for (WorkYearDateDTO workYearDateDTO1 : workYearDateDTOList1) {
                workYearDateDTOList2.add(workYearDateDTO1);
            }
            response.setCode(200);
            response.setData(workYearDateDTOList2);
            response.setMessages("成功连接");
        }catch (Exception ex) {
            response.setCode(200);
            response.setData(false);
            response.setMessages(ex.getMessage());
        }
        return response;
    }

}
