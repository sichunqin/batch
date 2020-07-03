package com.dxn.model.extend;

import com.dxn.model.entity.PrintPageChildrenPropertyEntity;
import com.dxn.system.Print.PrintProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Sys_PrintPageChildrenProperty")
public class PrintPageChildrenProperty extends PrintPageChildrenPropertyEntity {
    public PrintProperty toPrintProperty() {
        PrintProperty dto = new PrintProperty();
        dto.setName(this.getName());
        dto.setValue(this.getValue());
        return dto;
    }
}
