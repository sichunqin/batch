package com.dxn.model.extend;

import com.dxn.forms.dto.UIAuthorityDTO;
import com.dxn.forms.dto.UIAuthorityTypeEnum;
import com.dxn.forms.dto.UISchemaPropertyDTO;
import com.dxn.model.entity.FormPageChildrenPropertyEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.context.AppContext;
import com.dxn.system.exception.BusinessException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Optional;

@Entity
@Table(name = "Sys_FormPageChildrenProperty")
public class FormPageChildrenProperty extends FormPageChildrenPropertyEntity {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        this.synchronizationAuthority();
    }

    public void synchronizationAuthority() throws Exception {
        if (!this.getName().equalsIgnoreCase("AuthorityRemark")) return;
        FormPageChildrenProperty originalValue = this.getOriginalValue();
        if (Framework.isNotNullOrEmpty(originalValue) && Framework.equalsIgnoreCase(originalValue.getValue(), this.getValue()))
            return;

        UIAuthorityDTO dto = UIAuthorityDTO.parse(this.getValue());
        RoleAuthority.modifyRoleAuthority(this.getFormPageChildrenId().getFormPageId().getId(), UIAuthorityTypeEnum.Control, this.getFormPageChildrenId().getCode(), this.getFormPageChildrenId().getCode(), dto.getSuperCode(), String.format("%s(控件)", dto.getName()));
    }

    @Override
    public void onDeleting() throws Exception {
        super.onDeleting();
        this.deleteRoleAuthority();
    }

    public void deleteRoleAuthority() throws Exception {
        if (!this.getName().equalsIgnoreCase("AuthorityRemark")) return;
        RoleAuthority.clearRoleAuthority(this.getFormPageChildrenId().getFormPageId().getId(), UIAuthorityTypeEnum.Control, this.getFormPageChildrenId().getCode());
    }

    public UISchemaPropertyDTO toUISchemaPropertyDTO() throws Exception {

        UISchemaPropertyDTO dto = new UISchemaPropertyDTO();

        //复制属性
        ModelHelper.copyModel(this, dto);

        //容器
        dto.setContainer(this.getContainer());

        //复制值
        dto.setValue(this.getValue());

        return dto;
    }

    @SystemService(args = "formId,parentCode,name,value")
    public void createPropertyValue(Long formId, String parentCode, String name, String value) throws Exception {

        if (!AppContext.current().getProfile().getIsSuperAdmin()) return;
        if (Framework.isNullOrEmpty(name)) return;
        if (Framework.isNullOrEmpty(formId)) return;
        if (Framework.isNullOrEmpty(parentCode)) return;

        Optional<FormPageChildrenProperty> pageChildrenProperty =
                this.stream().where(w -> w.getName() == name && w.getFormPageChildrenId().getCode() == parentCode && w.getFormPageChildrenId().getFormPageId().getId() == formId).findFirst();

        if (Framework.isNullOrEmpty(value)) {
            if (pageChildrenProperty.isPresent())
                this.repository().remove(pageChildrenProperty.get());
        } else {
            FormPageChildrenProperty property;
            if (pageChildrenProperty.isPresent()) {
                property = pageChildrenProperty.get();
            } else {
                property = new FormPageChildrenProperty();
                property.setName(name);

                Optional<FormPageChildren> first = FormPageChildren.createNew().stream().where(w -> w.getCode() == parentCode && w.getFormPageId().getId() == formId).findFirst();
                if (!first.isPresent()) throw new BusinessException("未找到相关插件，无法创建帮助信息!");
                property.setFormPageChildrenId(first.get());
            }

            property.setValue(value);
            this.repository().add(property);
        }

        //清理缓存
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Schema);
    }

    @SystemService(args = "formId,parentCode,name,isModify")
    public String gainPropertyValue(Long formId, String parentCode, String name, Boolean isModify) throws Exception {

        if (Framework.isNotNullOrEmpty(name) && Framework.isNotNullOrEmpty(formId) && Framework.isNotNullOrEmpty(parentCode)) {
            Optional<FormPageChildrenProperty> pageChildrenProperty =
                    this.stream().where(w -> w.getName() == name && w.getFormPageChildrenId().getCode() == parentCode && w.getFormPageChildrenId().getFormPageId().getId() == formId).findFirst();

            if (pageChildrenProperty.isPresent()) {
                if (isModify) return pageChildrenProperty.get().getValue();
                return Framework.getShowHtml(pageChildrenProperty.get().getValue());
            }
        }

        return null;
    }
}
