package com.dxn.model.extend;

import com.dxn.forms.dto.UIAuthorityDTO;
import com.dxn.forms.dto.UIAuthorityTypeEnum;
import com.dxn.forms.dto.UISchemaPropertyDTO;
import com.dxn.model.entity.FormPageActionPropertyEntity;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Sys_FormPageActionProperty")
public class FormPageActionProperty extends FormPageActionPropertyEntity {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        this.synchronizationAuthority();
    }

    public void synchronizationAuthority() throws Exception {
        if (!this.getName().equalsIgnoreCase("AuthorityRemark")) return;
        FormPageActionProperty originalValue = this.getOriginalValue();
        if (Framework.isNotNullOrEmpty(originalValue) && Framework.equalsIgnoreCase(originalValue.getValue(), this.getValue()))
            return;

        UIAuthorityDTO dto = UIAuthorityDTO.parse(this.getValue());
        RoleAuthority.modifyRoleAuthority(this.getFormPageActionId().getFormPageId().getId(), UIAuthorityTypeEnum.Action, this.getFormPageActionId().getCode(), this.getFormPageActionId().getCode(), dto.getSuperCode(), String.format("%s(按钮)", dto.getName()));
    }

    @Override
    public void onDeleting() throws Exception {
        super.onDeleting();
        this.deleteRoleAuthority();
    }

    public void deleteRoleAuthority() throws Exception {
        if (!this.getName().equalsIgnoreCase("AuthorityRemark")) return;
        RoleAuthority.clearRoleAuthority(this.getFormPageActionId().getFormPageId().getId(), UIAuthorityTypeEnum.Action, this.getFormPageActionId().getCode());
    }

    public UISchemaPropertyDTO toUISchemaPropertyDTO() throws Exception {
        UISchemaPropertyDTO dto = new UISchemaPropertyDTO();
        //按钮复制属性
        ModelHelper.copyModel(this, dto);
        //容器
        dto.setContainer(this.getContainer());
        //复制值
        dto.setValue(this.getValue());
        return dto;
    }
}
