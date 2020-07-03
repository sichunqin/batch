package com.dxn.model.extend;

import com.dxn.model.entity.WorkingSoursChildrenEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "HR_WorkingSoursChildren")
public class WorkingSoursChildren extends WorkingSoursChildrenEntity {
    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        addCorrespondingTime(this.getTime());
        addDateTypeField(this.getManHourDate());
    }

    private void addDateTypeField(LocalDateTime time) throws Exception {
        WorkCalender workCalender = WorkCalender.createNew();
        Integer a = workCalender.gainDateType(time);
        this.setDateTypeField(a);
    }

    private void addCorrespondingTime(Integer time) throws Exception {
        if (time == 10) {
            this.setCorrespondingTime(20);
        } else if (time == 20) {
            this.setCorrespondingTime(10);
        } else if (time == 30) {
            this.setCorrespondingTime(10);
        } else if (time == 40) {
            this.setCorrespondingTime(1);
        } else if (time == 50) {
            this.setCorrespondingTime(2);
        } else if (time == 60) {
            this.setCorrespondingTime(3);
        } else if (time == 70) {
            this.setCorrespondingTime(4);
        } else if (time == 80) {
            this.setCorrespondingTime(5);
        }
    }


}
