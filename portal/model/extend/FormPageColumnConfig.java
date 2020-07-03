package com.dxn.model.extend;

import com.dxn.model.entity.FormPageColumnConfigEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.context.AppContext;
import com.dxn.system.context.Profile;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Optional;

@Entity
@Table(name = "Sys_FormPageColumnConfig")
public class FormPageColumnConfig extends FormPageColumnConfigEntity {

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Schema);
    }

    @SystemService(args = "formId,config")
    public void saveColumnConfig(Long formId, String config) throws Exception {
        Profile profile = AppContext.current().getProfile();
        if (profile.getIsSuperAdmin()) return;

        Long userId = profile.getId();
        FormPageColumnConfig columnConfig;

        Optional<FormPageColumnConfig> first = this.stream().where(w -> w.getFormPageId().getId() == formId && w.getUserId() == userId).sortedDescendingBy(o -> o.getCreatedOn()).findFirst();
        columnConfig = first.isPresent() ? first.get() : this.createNew();
        columnConfig.initialization();
        columnConfig.setConfig(config);
        columnConfig.setUserId(userId);
        columnConfig.setFormPage(formId);
        this.repository().add(columnConfig);
    }

    public String getColumnConfig(Long formId) throws Exception {
        Profile profile = AppContext.current().getProfile();
        if (profile.getIsSuperAdmin()) return null;

        Long userId = profile.getId();
        if (Framework.isNullOrEmpty(userId)) return null;
        Optional<FormPageColumnConfig> first = this.stream().where(w -> w.getFormPageId().getId() == formId && w.getUserId() == userId).sortedDescendingBy(o -> o.getCreatedOn()).findFirst();
        if (!first.isPresent()) return null;
        return first.get().getConfig();
    }
}
