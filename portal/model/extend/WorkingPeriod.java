package com.dxn.model.extend;

import com.dxn.dto.EnumerationDTO;
import com.dxn.model.entity.WorkingPeriodEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.exception.BusinessException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

@Entity
@Table(name = "HR_WorkingPeriod")
public class WorkingPeriod extends WorkingPeriodEntity {


    @SystemService(args = "year")
    public List<EnumerationDTO> yearOfAcquisition() throws Exception {
        List<EnumerationDTO> listDate = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            String a = "2018";
            int b = Integer.valueOf(a).intValue();
            int c = b + i;
            //String.valueOf(c);
            EnumerationDTO enumerationDTO = new EnumerationDTO();
            enumerationDTO.setText(String.valueOf(c));
            enumerationDTO.setId(String.valueOf(c));
            listDate.add(enumerationDTO);
        }
        return listDate;
    }

    @Override
    public void onValidate() throws Exception {
        super.onValidate();
        //业务代码

        List<TimeInterval> timeIntervals = this.getTimeIntervals();
        Collections.sort(timeIntervals, new Comparator<TimeInterval>() {
            @Override
            public int compare(TimeInterval o1, TimeInterval o2) {
                int a = o1.getStartingDate().isBefore(o2.getStartingDate()) ? -1 : 1;
                return a;
            }
        });
        for (int i = 0; i < timeIntervals.size() - 1; i++) {
            if (!timeIntervals.get(i).getEndDate().isBefore(timeIntervals.get(i + 1).getStartingDate()))
                throw new BusinessException("不能出现重复时间");
            ;
        }
        Long number = 0L;
        Long id = this.getId();
        Integer year = this.getYear();
        if (Framework.isNullOrEmpty(id)) {
            number = this.stream().where(w -> w.getYear() == year).count();
        } else {
            number = this.stream().where(w -> w.getYear() == year && !w.getId().equals(id)).count();
        }
        if (number > 0) throw new BusinessException("年度不能重复");
    }

}
