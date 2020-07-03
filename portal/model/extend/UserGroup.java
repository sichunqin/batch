package com.dxn.model.extend;

import com.dxn.model.entity.UserGroupEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.cache.CacheGroup;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Base_UserGroup")
public class UserGroup extends UserGroupEntity {
    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Authority);
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Schema);
    }
}
