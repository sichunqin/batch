package com.dxn.model.extend;

import com.dxn.forms.dto.UIAuthorityDTO;
import com.dxn.forms.dto.UIAuthorityTypeEnum;
import com.dxn.forms.dto.UISchemaActionDTO;
import com.dxn.model.entity.FormPageActionEntity;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Sys_FormPageAction")
public class FormPageAction extends FormPageActionEntity {

    @Override
    public void onUpdating() throws Exception {
        super.onUpdating();
        this.synchronizationAuthority();
    }

    public void synchronizationAuthority() throws Exception {
        FormPageAction originalValue = this.getOriginalValue();
        if (Framework.isNullOrEmpty(originalValue)) return;
        if (originalValue.getCode().equalsIgnoreCase(this.getCode())) return;
        if (!this.getFormPageActionPropertys().stream().anyMatch(a -> a.getName().equalsIgnoreCase("AuthorityRemark")))
            return;
        RoleAuthority.modifyRoleAuthority(this.getFormPageId().getId(), UIAuthorityTypeEnum.Action, originalValue.getCode(), this.getCode(), null, null);
    }

    @Override
    public void onDeleting() throws Exception {
        super.onDeleting();
        this.deleteRoleAuthority();
    }

    public void deleteRoleAuthority() throws Exception {
        if (!this.getFormPageActionPropertys().stream().anyMatch(a -> a.getName().equalsIgnoreCase("AuthorityRemark")))
            return;
        RoleAuthority.clearRoleAuthority(this.getFormPageId().getId(), UIAuthorityTypeEnum.Action, this.getCode());
    }

    public UISchemaActionDTO toUISchemaActionDTO() throws Exception {

        UISchemaActionDTO dto = new UISchemaActionDTO();

        //复制属性
        ModelHelper.copyModel(this, dto);

        //数据类型
        //dto.DataType = this.DataType.ToDataType();

        //类型
        dto.setControl(this.getControl());

        //容器
        dto.setContainer(this.getContainer());

        //复制属性
        for (FormPageActionProperty item : this.getFormPageActionPropertys()) {
            dto.getProperty().add(item.toUISchemaPropertyDTO());
        }

        //增加属性
        if (Framework.isNotNullOrEmpty(this.getReference()))
            dto.addProperty("ReferenceId", this.getReference().getId());

        if (Framework.isNotNullOrEmpty(this.getOnClick())) dto.addProperty("OnClick", this.getOnClick());
        if (Framework.isNotNullOrEmpty(this.getIcon())) dto.addProperty("Icon", this.getIcon());
        if (Framework.isNotNullOrEmpty(this.getJavaScript()))
            dto.addProperty("JavaScript", "var registerJavaScript = function () {\r\n \r\n" + this.getJavaScript() + "\r\n \r\n};");

        return dto;
    }

    public FormPageAction toCopy() throws Exception {
        FormPageAction action = super.toCopy();
        Iterator list = this.getFormPageActionPropertys().iterator();

        while (list.hasNext()) {
            FormPageActionProperty item = (FormPageActionProperty) list.next();
            FormPageActionProperty newItem = item.toCopy();
            newItem.setFormPageActionId(action);
            action.getFormPageActionPropertys().add(newItem);
        }

        return action;
    }

    @JsonIgnore
    public List<UIAuthorityDTO> getAuthorityConfig() throws Exception {

        List<UIAuthorityDTO> list = new ArrayList<>();
        Optional<FormPageActionProperty> frist = this.getFormPageActionPropertys().stream().filter(f -> f.getName().equalsIgnoreCase("AuthorityRemark")).findFirst();

        if (frist.isPresent()) {
            UIAuthorityDTO dto = UIAuthorityDTO.parse(frist.get().getValue());
            if (Framework.isNotNullOrEmpty(dto)) {
                dto.setDataId(this.getFormPageId().getId());
                dto.setCode(this.getCode());
                dto.setType(UIAuthorityTypeEnum.Action);
                dto.setName(String.format("%s(按钮)", dto.getName()));
                list.add(dto);
            }
        }

        if (Framework.isNotNullOrEmpty(this.getReferenceId())) {
            list.addAll(this.getReferenceId().getAuthorityConfig());
        } else {
            Optional<FormPageActionProperty> schemaCode = this.getFormPageActionPropertys().stream().filter(f -> f.getName().toLowerCase().startsWith("schemacode")).findFirst();
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
