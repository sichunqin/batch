package com.dxn.model.extend;

import com.dxn.model.entity.RoleEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.exception.BusinessException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Optional;

@Entity
@Table(name = "Base_Role")
public class Role extends RoleEntity {

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Authority);
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Schema);
    }

    public Role getRoleForCode(long id, String mes) throws Exception {
        Optional<Role> role = Role.stream().where(w -> w.getId() == id).findFirst();
        if (!role.isPresent()) throw new BusinessException(mes);
        return role.get();
    }

    @Override
    public void onDeleting() throws Exception {
        super.onDeleting();
        if (this.getIsSystemRole()) throw new BusinessException("系统默认权限组不能删除！");
    }

    @SystemService
    public String copyRole() throws Exception {
        Role role = this.toCopy();
        this.repository().add(role);
        return "复制完成！";
    }

    public Role toCopy() throws Exception {
        Role role = super.toCopy();
        role.setCode(role.getCode() + "Copy");
        role.setName(role.getName() + "复制");

        this.getRoleAuthoritys().forEach(f -> {
            try {
                RoleAuthority newItem = f.toCopy();
                newItem.setRoleId(role);
                role.getRoleAuthoritys().add(newItem);
            } catch (Exception e) {
                Framework.printStackTrace(e);
            }
        });

        return role;
    }
}
