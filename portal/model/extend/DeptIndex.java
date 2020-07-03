package com.dxn.model.extend;

import com.dxn.model.entity.DeptIndexEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.cache.CacheGroup;

import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "Base_DeptIndex")
public class DeptIndex extends DeptIndexEntity {
    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Authority);
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Schema);
    }
}
