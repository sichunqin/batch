package com.dxn.model.extend;

import com.dxn.forms.dto.UIAuthorityDTO;
import com.dxn.forms.dto.UIAuthorityTypeEnum;
import com.dxn.model.entity.RoleAuthorityEntity;
import com.dxn.repository.IRepository;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jinq.jpa.JPAJinqStream;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "Base_RoleAuthority")
public class RoleAuthority extends RoleAuthorityEntity {

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Authority);
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Schema);
    }

    @JsonIgnore
    public static List<UIAuthorityDTO> getAuthorityConfig() throws Exception {

        List<UIAuthorityDTO> getList = AppHelper.manage().getCacheUtil().getList("getAuthorityConfig", () -> {
            try {
                String sql = "Select a.Id,a.Code,a.Name,a.SuperCode,a.DataId,a.DataType as typeEnumNum,r.Id as RoleId,r.IsSystemRole as IsSystemRole from Base_RoleAuthority a inner join Base_Role r on a.RoleId=r.Id;";
                List<UIAuthorityDTO> list = repository().sqlQuery(sql, UIAuthorityDTO.class);

                for (UIAuthorityDTO item : list) {
                    item.setType(UIAuthorityTypeEnum.parse(item.getTypeEnumNum()));
                }

                return list;
            } catch (Exception e) {
                Framework.putSystemMes(e);
                Framework.log.info(Framework.getMes(e));
                return null;
            }

        }, UIAuthorityDTO.class, CacheGroup.Authority);

        if (Framework.isNullOrEmpty(getList)) {
            throw new BusinessException("获取权限失败!");
        }

        return getList;
    }

    public static void clearRoleAuthority(Long dataId) throws Exception {

        StringBuilder sql = new StringBuilder();
        JPAJinqStream<RoleAuthority> list = RoleAuthority.stream().where(w -> w.getDataId() == dataId);
        list.forEach(f -> {
            try {
                sql.append(f.toDeleteSql());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        repository().executeCommand(sql.toString());
    }

    public static void clearRoleAuthority(Long dataId, UIAuthorityTypeEnum type, String code) throws Exception {

        StringBuilder sql = new StringBuilder();
        int intType = type.getIndex();
        JPAJinqStream<RoleAuthority> list = RoleAuthority.stream().where(w -> w.getDataId() == dataId && w.getDataType() == intType && w.getCode() == code);
        list.forEach(f -> {
            try {
                sql.append(f.toDeleteSql());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        repository().executeCommand(sql.toString());
    }

    public static void clearRoleAuthorityForMenu(Long menuId) throws Exception {

        StringBuilder sql = new StringBuilder();
        JPAJinqStream<RoleAuthority> list = RoleAuthority.stream().where(w -> w.getMenuId() == menuId);
        list.forEach(f -> {
            try {
                sql.append(f.toDeleteSql());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        repository().executeCommand(sql.toString());
    }

    public static void modifyRoleAuthority(Long dataId, UIAuthorityTypeEnum type, String code, String newCode, String newSuperCode, String newName) throws Exception {
        IRepository repository = repository();
        int intType = type.getIndex();
        JPAJinqStream<RoleAuthority> list = RoleAuthority.stream().where(w -> w.getDataId() == dataId && w.getDataType() == intType && w.getCode() == code);
        list.forEach(f -> {
            f.initialization();
            if (Framework.isNotNullOrEmpty(newCode)) f.setCode(newCode);
            if (Framework.isNotNullOrEmpty(newSuperCode)) f.setSuperCode(newSuperCode);
            if (Framework.isNotNullOrEmpty(newName)) f.setName(newName);
            repository.add(f);
        });
    }
}
