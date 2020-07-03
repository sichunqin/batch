package com.dxn.model.extend;

import com.dxn.forms.dto.UISchemaDTO;
import com.dxn.model.entity.EntityMapItemEntity;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.entityCode.EntityCodeConfigColumn;
import com.dxn.system.entityCode.EntityCodeConfigConstraint;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Base_EntityMapItem")
public class EntityMapItem extends EntityMapItemEntity {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();

        if (Framework.isNotNullOrEmpty(this.getCode())) this.setCode(this.getCode().trim());
        if (Framework.isNotNullOrEmpty(this.getName())) this.setName(this.getName().trim());

        this.setEntityId();
        this.setControlType();
    }

    private void setEntityId() throws Exception {
        if (Framework.isNullOrEmpty(this.getDataType())) return;
        if (this.getDataType().getIsBaseType()) return;
        if (this.getCode().endsWith("Id")) return;

        this.setCode(this.getCode() + "Id");
    }

    private void setControlType() throws Exception {
        if (Framework.isNotNullOrEmpty(this.getControl())) return;
        this.synchroControl();
    }

    public void synchroControl() throws Exception {
        this.setSelfControlType();
        this.setEntityConfig();
    }

    private void setSelfControlType() throws Exception {
        String control = "GridCombobox";

        switch (this.getDataType().getCode()) {
            case "String":
                control = this.getLength() < 0 || this.getLength() >= 3000 ? "TextArea" : "Text";
                break;
            case "Integer":
                control = "Number";
                break;
            case "Long":
                control = "Number";
                break;
            case "Boolean":
                control = "CheckBox";
                break;
            case "Date":
                control = "Date";
                break;
            case "LocalDateTime":
                control = "DateTime";
                break;
            case "Time":
                control = "Time";
                break;
            case "Double":
                control = "Number";
                break;
            case "Single":
                control = "Text";
                break;
            case "Void":
                control = "Text";
                break;
            case "Timestamp":
                control = "Hidden";
                break;
            default:
                break;
        }

        //特殊列处理
        if (this.getCode().equalsIgnoreCase("Sort") && control.equalsIgnoreCase("Number"))
            control = "Hidden";

        if (this.getCode().equalsIgnoreCase("Id")) control = "Hidden";
        if (this.getCode().equalsIgnoreCase("Timestamp")) control = "Hidden";

        this.setControl(control);
    }

    private void setEntityConfig() throws Exception {
        if (this.getCode().equalsIgnoreCase("CreatedById") || this.getCode().equalsIgnoreCase("ModifiedById")) {
            this.setEntityConfig(String.format("[Entity(%s|Code(Code)#Name(Name))]", "User"));
            this.setControl("GridCombobox");
        } else if (this.getCode().equalsIgnoreCase("ParentId")) {
            this.setControl("GridCombobox");
            this.setEntityConfig(String.format("[Entity(%s|Code(Code)#Name(Name))]", this.getEntityMap().getCode()));
        } else {
            if (Framework.isNotNullOrEmpty(this.getDataType()) && Framework.isNotNullOrEmpty(this.getDataType().getTableName())) {
                this.setEntityConfig(String.format("[Entity(%s|Code(Code)#Name(Name))]", this.getDataType().getCode()));
                this.setControl("GridCombobox");
            } else {
                this.setEntityConfig("");
            }
        }
    }

    @JsonIgnore
    public UISchemaDTO toUISchemaColumnDTO() throws Exception {
        UISchemaDTO dto = new UISchemaDTO();

        //复制属性
        ModelHelper.copyModel(this, dto);

        //设置控件类型
        dto.setControl(this.getControl());

        //查询列
        dto.setSelect(this.getCode().endsWith("Id") ? String.format("%s%s", this.getCode(), Framework.isNullOrEmpty(this.getEntityConfig()) ? "" : this.getEntityConfig()) : this.getCode());

        dto.addProperty("Length", this.getLength());
        dto.addProperty("Required", !this.getIsNull());
        dto.addProperty("IsReadOnly", this.getIsReadOnly());
        dto.addProperty("IsTransient", this.getIsTransient());

        if (Framework.isNotNullOrEmpty(this.getEnumType())) {
            dto.addProperty("EnumType", this.getEnumType());
            dto.addProperty("EntityConfig", String.format("[Enum(%s)]", this.getEnumType()));

            boolean isInt = this.getDataType().getCode().equalsIgnoreCase("Integer");
            if (isInt) dto.setSelect(String.format("%s[Enum(%s)]", this.getCode(), this.getEnumType()));
        }

        if (Framework.isNotNullOrEmpty(this.getEntityConfig())) dto.addProperty("EntityConfig", this.getEntityConfig());
        if (Framework.isNotNullOrEmpty(this.getDefaultValue())) dto.addProperty("DefaultValue", this.getDefaultValue());
        if (Framework.isNotNullOrEmpty(this.getDescription())) dto.addProperty("Description", this.getDescription());

        //拼实体
        if (Framework.isNotNullOrEmpty(this.getDataType().getTableName())) {
            dto.addProperty("EntityCode", this.getDataType().getCode());
        }

        return dto;
    }


    @JsonIgnore
    public EntityCodeConfigColumn toEntityCodeConfigColumn() throws Exception {
        EntityCodeConfigColumn column = new EntityCodeConfigColumn();

        //复制属性
        ModelHelper.copyModel(this, column);

        String type = this.getDataType().getCode();
        column.setType(type);

        if (!this.getDataType().getIsBaseType()) {
            column.setIsRelative(true);
            column.setCode(this.getCode().replace("Id", ""));
        }

        return column;
    }

    @JsonIgnore
    public EntityCodeConfigConstraint toEntityCodeConfigConstraint() throws Exception {
        if (this.getDataType() == null) return null;
        if (Framework.isNullOrEmpty(this.getDataType().getTableName()) && !this.getCode().equalsIgnoreCase("ParentId"))
            return null;
        if (this.getEntityMap() == null) return null;
        if (Framework.isNullOrEmpty(this.getEntityMap().getTableName())) return null;
        if (!this.getCode().endsWith("Id")) return null;
        if (this.getCode().equalsIgnoreCase("Id") || this.getCode().equalsIgnoreCase("CreatedById") || this.getCode().equalsIgnoreCase("ModifiedById"))
            return null;

        EntityCodeConfigConstraint constraint = new EntityCodeConfigConstraint();

        if (this.getCode().equalsIgnoreCase("ParentId")) {
            constraint.setName(String.format("%s_Children", this.getEntityMap().getCode()));
            constraint.setIsDelAction(false);
            constraint.setIsDelActionDesc("NO_ACTION");
            constraint.setMainTable(this.getEntityMap().getTableName());

        } else if (this.isMainUing()) {

            constraint.setName(String.format("%s_%ss", this.getEntityMap().getMainEntityMap().getCode(), this.getEntityMap().getCode()));
            constraint.setIsDelAction(true);
            constraint.setIsDelActionDesc("CASCADE");
            constraint.setMainTable(this.getEntityMap().getTableName());

        } else {
            constraint.setName(String.format("%s_%s", this.getEntityMap().getCode(), this.getCode().replace("Id", "")));
            constraint.setIsDelAction(false);
            constraint.setIsDelActionDesc("NO_ACTION");
            constraint.setMainTable(this.getEntityMap().getTableName());
        }

        constraint.setMinId("Id");
        constraint.setKeyTable(this.getEntityMap().getTableName());
        constraint.setKeyId(this.getCode());
        constraint.setType("F");

        return constraint;
    }

    @JsonIgnore
    private boolean isMainUing() throws Exception {
        if (Framework.isNullOrEmpty(this.getDataType())) return false;
        if (Framework.isNullOrEmpty(this.getDataType().getTableName())) return false;
        if (Framework.isNullOrEmpty(this.getEntityMap())) return false;
        if (Framework.isNullOrEmpty(this.getEntityMap().getTableName())) return false;

        if (!this.getCode().endsWith("Id") || this.getCode().equalsIgnoreCase("Id")) return false;
        if (Framework.isNullOrEmpty(this.getEntityMap().getMainEntityMap())) return false;

        return this.getCode().equalsIgnoreCase(String.format("%sId", this.getEntityMap().getMainEntityMap().getCode()));
    }

    @JsonIgnore
    public boolean gainIsSystemMap(String sysName) {

        if (this.getCode().equals("CreatedOn")) return true;
        if (this.getCode().equals("CreatedById")) return true;
        if (this.getCode().equals("ModifiedOn")) return true;
        if (this.getCode().equals("ModifiedById")) return true;

        if (this.getCode().equals("TreeCode")) return true;
        if (this.getCode().equals("TreeName")) return true;
        if (this.getCode().equals("Leaf")) return true;
        if (this.getCode().equals("Level")) return true;

        if (this.getCode().equals(sysName)) return true;

        return false;
    }
}