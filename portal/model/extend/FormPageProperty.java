package com.dxn.model.extend;

import com.dxn.forms.dto.UISchemaPropertyDTO;
import com.dxn.model.entity.FormPagePropertyEntity;
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
@Table(name = "Sys_FormPageProperty")
public class FormPageProperty extends FormPagePropertyEntity {

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

    @SystemService(args = "formId,name,value")
    public void createPropertyValue(Long formId, String name, String value) throws Exception {

        if (!AppContext.current().getProfile().getIsSuperAdmin()) return;
        if (Framework.isNullOrEmpty(name)) return;
        if (Framework.isNullOrEmpty(formId)) return;

        Optional<FormPageProperty> pageProperty =
                this.stream().where(w -> w.getName() == name && w.getFormPageId().getId() == formId).findFirst();

        FormPageProperty property;
        if (pageProperty.isPresent()) {
            property = pageProperty.get();
        } else {
            property = new FormPageProperty();
            property.setName(name);

            Optional<FormPage> first = FormPage.createNew().stream().where(w -> w.getId() == formId).findFirst();
            if (!first.isPresent()) throw new BusinessException("未找到相关表单，无法创建帮助信息!");
            property.setFormPageId(first.get());
        }

        property.setValue(value);
        if (Framework.isNullOrEmpty(value)) property.setValue("");
        this.repository().add(property);
        //清理缓存
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Schema);
    }
}