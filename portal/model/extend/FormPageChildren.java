package com.dxn.model.extend;

import com.dxn.forms.dto.*;
import com.dxn.model.entity.FormPageChildrenEntity;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Sys_FormPageChildren")
public class FormPageChildren extends FormPageChildrenEntity {

    @Override
    public void onValidate() throws Exception {
        super.onValidate();

        if (Framework.isNullOrEmpty(this.getParent())) return;
        if (Framework.isNullOrEmpty(this.getFormPage())) return;

        if (this.getFormPage().equals(this.getParent().getFormPage())) return;

        throw new BusinessException(String.format("控件 %s 与父亲控件不在同一表单下，增加失败！", this.getCode()));
    }

    @Override
    public void onUpdating() throws Exception {
        super.onUpdating();
        this.synchronizationAuthority();
    }

    public void synchronizationAuthority() throws Exception {
        FormPageChildren originalValue = this.getOriginalValue();
        if (Framework.isNullOrEmpty(originalValue)) return;
        if (originalValue.getCode().equalsIgnoreCase(this.getCode())) return;
        if (!this.getFormPageChildrenPropertys().stream().anyMatch(a -> a.getName().equalsIgnoreCase("AuthorityRemark")))
            return;
        RoleAuthority.modifyRoleAuthority(this.getFormPageId().getId(), UIAuthorityTypeEnum.Control, originalValue.getCode(), this.getCode(), null, null);
    }

    @Override
    public void onDeleting() throws Exception {
        super.onDeleting();
        this.deleteRoleAuthority();
    }

    public void deleteRoleAuthority() throws Exception {
        if (!this.getFormPageChildrenPropertys().stream().anyMatch(a -> a.getName().equalsIgnoreCase("AuthorityRemark")))
            return;
        RoleAuthority.clearRoleAuthority(this.getFormPageId().getId(), UIAuthorityTypeEnum.Control, this.getCode());
    }

    public UISchemaDTO toUISchemaChildrenDTO() throws Exception {
        if (Framework.isNullOrEmpty(this.getReference())) {
            return this.toUiSchemaDTO();
        } else {
            UISchemaDTO dto = new UISchemaDTO();
            FormPage referenceForm = this.getReference();
            dto.addProperty("referenceFormId", referenceForm.getId(), true);

            if (Framework.isNotNullOrEmpty(this.getFormPage().getEntityMap()) && Framework.isNotNullOrEmpty(referenceForm.getEntityMap())) {
                dto.setIsReference(getIsReference(this.getFormPage().getEntityMap().getId(), referenceForm.getEntityMap().getId()));//引用表单
                if (dto.getIsReference()) {
                    String idValue = Framework.firstLowerCase(this.getFormPage().getEntityMapType()) + "Id";
                    dto.addProperty("MainParams", "[{param:'" + idValue + "',fieldId:'id'}]");
                    dto.addProperty("MustParam", "query_" + idValue);
                }
            }

            if (this.getControl().equalsIgnoreCase("Panel")) {
                UISchemaDTO panelDTO = this.toUiSchemaDTO();
                panelDTO.getChildren().add(dto);
                return panelDTO;
            }

            return dto;
        }
    }

    public UISchemaDTO toUiSchemaDTO() throws Exception {
        UISchemaDTO dto = new UISchemaDTO();

        if (this.getFormPage().getEntityMap() != null)
            dto = this.getFormPage().getEntityMap().findEntityMapItemSchema(this.getCode());

        //复制属性
        ModelHelper.copyModel(this, dto);

        //容器
        dto.setContainer(this.getContainer());

        //编号
        dto.setCode(Framework.firstLowerCase(this.getCode()));

        //名称
        dto.setName(this.getName());

        //设置控件类型
        dto.setControl(this.getControl());

        //复制属性
        for (FormPageChildrenProperty item : this.getFormPageChildrenPropertys()) {
            UISchemaPropertyDTO removeDto = dto.gainProperty(item.getName());
            if (removeDto != null) dto.getProperty().remove(removeDto);
            dto.getProperty().add(item.toUISchemaPropertyDTO());
        }

        if (Framework.isNotNullOrEmpty(this.getJavaScript())) {
            String value = "var registerJavaScript = function () {\r\n \r\n" + this.getJavaScript() + "\r\n \r\n};";
            dto.addProperty("JavaScript", value);
        }

        boolean isCheckBoxGroup = dto.getControl().equals("CheckBoxGroup");
        boolean isRadioButtonGroup = dto.getControl().equals("RadioButtonGroup");

        //处理枚举
        if (isCheckBoxGroup || isRadioButtonGroup) {
            UISchemaPropertyDTO enumType = dto.gainProperty("EnumType");
            if (Framework.isNotNullOrEmpty(enumType)) {
                String valueType = "";
                UISchemaPropertyDTO enumValueType = dto.gainProperty("EnumValueType");
                if (Framework.isNotNullOrEmpty(enumValueType)) valueType = enumValueType.getValue().toString();

                EnumValue et = EnumValue.createNew();

                List<ComboboxDTO> enumList = et.getEnum(enumType.getValue().toString(), -1, -1);

                for (ComboboxDTO e : enumList) {

                    UISchemaDTO itemDto = new UISchemaDTO();

                    itemDto.setCode(String.format("%sItem", this.getCode()));
                    itemDto.setName(e.getText());
                    itemDto.setSort(e.getSort());

                    if (isCheckBoxGroup) {
                        itemDto.setControl("CheckBoxItem");
                        if (Framework.isNullOrEmpty(valueType)) valueType = "Name";
                    }

                    if (isRadioButtonGroup) {
                        itemDto.setControl("RadioButtonItem");
                        if (Framework.isNullOrEmpty(valueType)) valueType = "Value";
                    }

                    if (valueType.equalsIgnoreCase("Code")) {
                        UISchemaPropertyDTO propertyDto = new UISchemaPropertyDTO();
                        propertyDto.setName("Value");
                        propertyDto.setValue(itemDto.getCode());
                        itemDto.getProperty().add(propertyDto);
                    }

                    if (valueType.equalsIgnoreCase("Name")) {
                        UISchemaPropertyDTO propertyDto = new UISchemaPropertyDTO();
                        propertyDto.setName("Value");
                        propertyDto.setValue(itemDto.getName());
                        itemDto.getProperty().add(propertyDto);
                    }

                    if (valueType.equalsIgnoreCase("Value")) {
                        UISchemaPropertyDTO propertyDto = new UISchemaPropertyDTO();
                        propertyDto.setName("Value");
                        propertyDto.setValue(e.getId());
                        itemDto.getProperty().add(propertyDto);
                    }

                    dto.getChildren().add(itemDto);
                }

            }
        }

        //排序
        //Collections.sort(this.getChildren(), Comparator.comparing(o -> o.getSort()));

        //复制子
        for (FormPageChildren item : this.getChildren()) {
            dto.getChildren().add(item.toUISchemaChildrenDTO());
        }

        return dto;
    }

    private boolean getIsReference(Long entityMapId, Long referenceEntityMapId) throws Exception {

        if (Framework.isNullOrEmpty(entityMapId)) return false;
        if (Framework.isNullOrEmpty(referenceEntityMapId)) return true;

        return EntityMap.createNew().stream().anyMatch(w -> w.getId() == referenceEntityMapId && w.getMainEntityMapId() != null && w.getMainEntityMapId().getId() == entityMapId);
    }

    public FormPageChildren toCopy(FormPage formPage) throws Exception {
        FormPageChildren children = this.toCopy();
        children.setFormPageId(formPage);

        for (FormPageChildrenProperty item : this.getFormPageChildrenPropertys()) {
            FormPageChildrenProperty newItem = item.toCopy();
            newItem.setFormPageChildrenId(children);
            children.getFormPageChildrenPropertys().add(newItem);
        }

        for (FormPageChildren item : this.getChildren()) {
            FormPageChildren newItem = item.toCopy(formPage);
            newItem.setParentId(children);
            formPage.getFormPageChildrens().add(newItem);
        }

        return children;
    }

    @JsonIgnore
    public List<UIAuthorityDTO> getAuthorityConfig() throws Exception {

        List<UIAuthorityDTO> list = new ArrayList<>();

        Optional<FormPageChildrenProperty> first = this.getFormPageChildrenPropertys().stream().filter(f -> f.getName().equalsIgnoreCase("AuthorityRemark")).findFirst();

        if (first.isPresent()) {
            UIAuthorityDTO dto = UIAuthorityDTO.parse(first.get().getValue());
            if (Framework.isNotNullOrEmpty(dto)) {
                dto.setDataId(this.getFormPageId().getId());
                dto.setCode(this.getCode());
                dto.setType(UIAuthorityTypeEnum.Control);
                dto.setName(String.format("%s(控件)", dto.getName()));
                list.add(dto);
            }
        }

        if (Framework.isNotNullOrEmpty(this.getReference())) {
            list.addAll(this.getReference().getAuthorityConfig());
        } else {
            Optional<FormPageChildrenProperty> schemaCode = this.getFormPageChildrenPropertys().stream().filter(f -> f.getName().toLowerCase().startsWith("schemacode")).findFirst();
            if (schemaCode.isPresent()) {
                String code = schemaCode.get().getValue();
                if (Framework.isNotNullOrEmpty(code)) {
                    Optional<FormPage> formPage = FormPage.stream().where(w -> w.getCode() == code).findFirst();
                    if (formPage.isPresent()) {
                        FormPage page = formPage.get();
                        page.initialization();
                        list.addAll(page.getAuthorityConfig());
                    }
                }
            }
        }

        return list;
    }
}
