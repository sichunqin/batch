package com.dxn.model.extend;

import com.dxn.forms.dto.UIAuthorityDTO;
import com.dxn.forms.dto.UIAuthorityTypeEnum;
import com.dxn.model.entity.DataAuthorityGroupEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.context.AppContext;
import com.dxn.system.exception.BusinessException;
import com.dxn.system.exception.DxnException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Base_DataAuthorityGroup")
public class DataAuthorityGroup extends DataAuthorityGroupEntity {
    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Authority);
    }

    @Override
    public void onDeleting() throws Exception {
        super.onDeleting();
        if (this.getIsSystem()) throw new DxnException("系统权限默认权限组不能删除！");
    }

    @JsonIgnore
    public static List<UIAuthorityDTO> getAuthorityConfig(Long formId) throws Exception {
        List<UIAuthorityDTO> list = new ArrayList<>();

        //控件权限
        List<UIAuthorityDTO> authorityList = getAuthorityConfig();
        List<Long> roles = getUserAuthorityGroup();

        for (Long item : roles) {
            authorityList.stream().filter(a -> a.getId().equals(item) && a.getDataId().equals(formId)).forEach(e -> {
                list.add(e);
            });
        }

        return list;
    }

    @JsonIgnore
    private static List<Long> getUserAuthorityGroup() throws Exception {
        Long userId = AppContext.current().getProfile().getId();
        if (Framework.isNullOrEmpty(userId)) return new ArrayList<>();

        String key = String.format("getDataAuthorityConfig-%s", userId);
        List<Long> getList = AppHelper.manage().getCacheUtil().getList(key, () -> {
            try {
                //用户权限
                List<Long> list = new ArrayList<>();
                List<DataAuthorityUser> groupUser = DataAuthorityUser.stream().where(w -> w.getUserId().getId() == userId).toList();
                groupUser.forEach(f -> {
                    if (Framework.isNotNullOrEmpty(f)) list.add(f.getDataAuthorityGroupId().getId());
                });

                //特殊权限
                if (DepartmentVSUser.isHaveSpecialAuthority()) {
                    List<DataAuthorityGroup> group = DataAuthorityGroup.stream().where(w -> w.getIsSystem()).toList();
                    group.forEach(f -> {
                        if (Framework.isNotNullOrEmpty(f)) list.add(f.getId());
                    });
                }

                return list;
            } catch (Exception e) {
                Framework.putSystemMes(e);
                Framework.log.info(Framework.getMes(e));
                return null;
            }
        }, Long.class, CacheGroup.Authority);

        if (Framework.isNullOrEmpty(getList)) {
            throw new BusinessException("获取数据权限组失败!");
        }

        return getList;
    }

    @JsonIgnore
    private static List<UIAuthorityDTO> getAuthorityConfig() throws Exception {

        List<UIAuthorityDTO> getList = AppHelper.manage().getCacheUtil().getList("getDataAuthorityConfig", () -> {
            try {
                String sql = "Select a.dataAuthorityGroupId as Id,p.Code as Code,p.authorityRemark as Name,f.id as DataId from Base_DataAuthorityMenu a inner join Base_Menu m on a.menuId=m.id inner join Sys_FormPage f on m.uiFormId=f.id inner join Sys_FormPageDataPermission p on p.formPageId=f.id where p.authorityRemark is not null;";
                List<UIAuthorityDTO> list = repository().sqlQuery(sql, UIAuthorityDTO.class);

                for (UIAuthorityDTO item : list) {
                    item.setType(UIAuthorityTypeEnum.DataPermission);
                }

                return list;
            } catch (Exception e) {
                Framework.putSystemMes(e);
                Framework.log.info(Framework.getMes(e));
                return null;
            }

        }, UIAuthorityDTO.class, CacheGroup.Authority);

        if (Framework.isNullOrEmpty(getList)) {
            throw new BusinessException("获取数据权限失败!");
        }

        return getList;
    }
}
