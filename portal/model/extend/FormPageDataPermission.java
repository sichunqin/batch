package com.dxn.model.extend;

import com.dxn.forms.dto.UIAuthorityDTO;
import com.dxn.forms.dto.UIAuthorityTypeEnum;
import com.dxn.forms.dto.UISchemaDataPermissionDTO;
import com.dxn.model.entity.FormPageDataPermissionEntity;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Sys_FormPageDataPermission")
public class FormPageDataPermission extends FormPageDataPermissionEntity {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        this.synchronizationAuthority();
    }

    public void synchronizationAuthority() throws Exception {
        if (Framework.isNullOrEmpty(this.getAuthorityRemark())) {
            FormPageDataPermission originalValue = this.getOriginalValue();
            if (Framework.isNotNullOrEmpty(originalValue) && Framework.isNullOrEmpty(originalValue.getAuthorityRemark()))
                return;

            String code = Framework.isNotNullOrEmpty(originalValue) ? originalValue.getCode() : this.getCode();
            RoleAuthority.clearRoleAuthority(this.getFormPageId().getId(), UIAuthorityTypeEnum.DataPermission, code);
        } else {
            FormPageDataPermission originalValue = this.getOriginalValue();
            if (Framework.isNotNullOrEmpty(originalValue) && Framework.equalsIgnoreCase(originalValue.getAuthorityRemark(), this.getAuthorityRemark()))
                return;

            String code = Framework.isNotNullOrEmpty(originalValue) ? originalValue.getCode() : this.getCode();
            UIAuthorityDTO dto = UIAuthorityDTO.parse(this.getAuthorityRemark());
            RoleAuthority.modifyRoleAuthority(this.getFormPageId().getId(), UIAuthorityTypeEnum.DataPermission, code, this.getCode(), dto.getSuperCode(), String.format("%s(数据)", dto.getName()));
        }
    }

    @Override
    public void onDeleting() throws Exception {
        super.onDeleting();
        this.deleteRoleAuthority();
    }

    public void deleteRoleAuthority() throws Exception {
        if (Framework.isNullOrEmpty(this.getAuthorityRemark())) return;
        RoleAuthority.clearRoleAuthority(this.getFormPageId().getId(), UIAuthorityTypeEnum.DataPermission, this.getCode());
    }

    @JsonIgnore
    public List<UIAuthorityDTO> getAuthorityConfig() {
        List<UIAuthorityDTO> list = new ArrayList<>();

        UIAuthorityDTO dto = UIAuthorityDTO.parse(this.getAuthorityRemark());
        if (Framework.isNotNullOrEmpty(dto)) {
            dto.setDataId(this.getFormPageId().getId());
            dto.setCode(this.getCode());
            dto.setType(UIAuthorityTypeEnum.DataPermission);
            dto.setName(String.format("%s(数据)", dto.getName()));
            list.add(dto);
        }

        return list;
    }

    @JsonIgnore
    public UISchemaDataPermissionDTO toUISchemaDataPermissionDTO() throws Exception {
        UISchemaDataPermissionDTO dto = new UISchemaDataPermissionDTO();
        ModelHelper.copyModel(this, dto);
        return dto;
    }
}
