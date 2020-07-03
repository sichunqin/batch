package com.dxn.model.extend;

import com.dxn.model.entity.PrintPageChildrenEntity;
import com.dxn.system.Framework;
import com.dxn.system.Print.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Sys_PrintPageChildren")
public class PrintPageChildren extends PrintPageChildrenEntity {
    public PrintRow toPrintRow() {
        if (Framework.isNullOrEmpty(this.getControl())) return null;
        if (!this.getControl().equalsIgnoreCase("Row")) return null;
        PrintRow dto = new PrintRow();

        this.getChildren().forEach(f -> {
            PrintColumn column = null;
            try {
                column = f.toPrintColumn();
            } catch (Exception e) {
                Framework.printStackTrace(e);
            }
            if (Framework.isNotNullOrEmpty(column)) dto.getColumn().add(column);
        });

        this.getPrintPageChildrenPropertys().forEach(f -> {
            PrintProperty property = f.toPrintProperty();
            if (Framework.isNotNullOrEmpty(property)) dto.getProperties().add(property);
        });

        return dto;
    }

    public PrintColumn toPrintColumn() {
        if (Framework.isNullOrEmpty(this.getControl())) return null;
        if (!this.getControl().equalsIgnoreCase("Column")) return null;
        PrintColumn dto = new PrintColumn();
        dto.setValue(this.getValue());

        this.getPrintPageChildrenPropertys().forEach(f -> {
            PrintProperty property = f.toPrintProperty();
            if (Framework.isNotNullOrEmpty(property)) dto.getProperties().add(property);
        });

        this.getChildren().forEach(f -> {
            PrintDiv div = null;
            try {
                div = f.toPrintDiv();
            } catch (Exception e) {
                Framework.printStackTrace(e);
            }
            if (Framework.isNotNullOrEmpty(div)) dto.getDiv().add(div);
        });

        this.getChildren().forEach(f -> {
            PrintTable table = null;
            try {
                table = f.toPrintTable();
            } catch (Exception e) {
                Framework.printStackTrace(e);
            }
            if (Framework.isNotNullOrEmpty(table)) dto.getTable().add(table);
        });

        return dto;
    }

    public PrintDiv toPrintDiv() {
        if (Framework.isNullOrEmpty(this.getControl())) return null;
        if (!this.getControl().equalsIgnoreCase("Div")) return null;
        PrintDiv dto = new PrintDiv();
        dto.setValue(this.getValue());

        this.getPrintPageChildrenPropertys().forEach(f -> {
            PrintProperty property = f.toPrintProperty();
            if (Framework.isNotNullOrEmpty(property)) dto.getProperties().add(property);
        });

        return dto;
    }

    public PrintTable toPrintTable() {
        if (Framework.isNullOrEmpty(this.getControl())) return null;
        if (!this.getControl().equalsIgnoreCase("Table")) return null;
        PrintTable dto = new PrintTable();

        this.getChildren().forEach(f -> {
            PrintRow row = f.toPrintRow();
            if (Framework.isNotNullOrEmpty(row)) dto.getRows().add(row);
        });

        this.getPrintPageChildrenPropertys().forEach(f -> {
            PrintProperty property = f.toPrintProperty();
            if (Framework.isNotNullOrEmpty(property)) dto.getProperties().add(property);
        });

        return dto;
    }
}
