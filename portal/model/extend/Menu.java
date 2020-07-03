package com.dxn.model.extend;

import com.dxn.dto.MenuDTO;
import com.dxn.forms.dto.*;
import com.dxn.model.entity.MenuEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.context.AppContext;
import com.dxn.system.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Entity
@Table(name = "Base_Menu")
public class Menu extends MenuEntity {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        this.synchronizationAuthority();
    }

    public void synchronizationAuthority() throws Exception {
        Menu originalValue = this.getOriginalValue();
        if (Framework.isNullOrEmpty(originalValue)) return;
        RoleAuthority.modifyRoleAuthority(this.getId(), UIAuthorityTypeEnum.Menu, originalValue.getCode(), this.getCode(), null, String.format("%s(菜单)", this.getName()));
    }

    @Override
    public void onDeleting() throws Exception {
        super.onDeleting();
        this.deleteRoleAuthority();
    }

    public void deleteRoleAuthority() throws Exception {
        UserQuickLink.clearUserQuickLinkForMenu(this.getId());
        RoleAuthority.clearRoleAuthorityForMenu(this.getId());
        RoleAuthority.clearRoleAuthority(this.getId());
    }

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Authority);
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Schema);
    }

    @JsonIgnore
    public static List<TreeDTO> getMenuTree(Long parentId) throws Exception {
        if (Framework.isNullOrEmpty(parentId)) return getMenu();
        List<TreeDTO> getList = new ArrayList<>();
        TreeDTO cItem = getMenuTreeDTO(getMenu(), parentId);
        if (Framework.isNotNullOrEmpty(cItem)) {
            for (TreeDTO item : cItem.getChildren()) {
                item.setParentId(null);
                getList.add(item);
            }
        }
        return getList;
    }

    @JsonIgnore
    private static TreeDTO getMenuTreeDTO(List<TreeDTO> list, Long parentId) throws Exception {
        for (TreeDTO item : list) {
            if (item.getId().equalsIgnoreCase(parentId.toString())) return item;
            if (Framework.isNotNullOrEmpty(item.getChildren())) {
                TreeDTO cItem = getMenuTreeDTO(item.getChildren(), parentId);
                if (Framework.isNotNullOrEmpty(cItem)) return cItem;
            }
        }
        return null;
    }

    @JsonIgnore
    public static List<TreeDTO> getMenu() throws Exception {

        Long userId = AppContext.current().getProfile().getId();
        if (Framework.isNullOrEmpty(userId)) return new ArrayList<>();

        String key = String.format("menu-getMenu-%s", userId);

        List<TreeDTO> getList = AppHelper.manage().getCacheUtil().getList(key, () -> {
            try {
                List<MenuDTO> menus = getMenuDTO();
                List<UIAuthorityDTO> getMenuAuthority = User.createNew().getMenuAuthority();
                List<TreeDTO> list = getTreeDTO(menus, null, getMenuAuthority);
                return list;
            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }
        }, TreeDTO.class, CacheGroup.Authority);

        return getList;
    }

    @JsonIgnore
    private static List<TreeDTO> getTreeDTO(List<MenuDTO> menuList, Long parentId, List<UIAuthorityDTO> getMenuAuthority) {

        List<TreeDTO> list = new ArrayList<>();
        Stream<MenuDTO> childless;

        if (Framework.isNullOrEmpty(parentId)) {
            childless = menuList.stream().filter(w -> w.getParentId() == null);
        } else {
            childless = menuList.stream().filter(w -> w.getParentId() != null && w.getParentId() == parentId);
        }

        childless.forEach(f -> {
            boolean isHaveMenu = true;

            //非管理员渲染权限
            if (!AppContext.current().getProfile().getIsSuperAdmin() && f.getHaveRelation() && f.getParentId() != null)
                isHaveMenu = getMenuAuthority.stream().anyMatch(a -> a.getDataId().equals(f.getId()));

            if (isHaveMenu || f.getCode().equalsIgnoreCase("MyWork")) {
                TreeDTO dto = f.toTreeDTO();
                dto.setChildren(getTreeDTO(menuList, f.getId(), getMenuAuthority));
                dto.setIsEnable(dto.getIsHaveRelation() || dto.getChildren().size() > 0 || Framework.isNotNullOrEmpty(dto.getUrl()));
                if (!dto.getIsEnable()) dto.setIco(String.format("%sRead", dto.getIco()));
                if (dto.getIsEnable() || dto.getParentId() == null)
                    list.add(dto);
            }
        });

        return list;
    }

    @JsonIgnore
    public static List<TreeDTO> getMenuAll() throws Exception {

        String key = String.format("menu-getMenu-all");

        List<TreeDTO> getList = AppHelper.manage().getCacheUtil().getList(key, () -> {
            try {
                List<MenuDTO> menus = getMenuDTO();
                List<TreeDTO> list = getTreeDTOAll(menus, null);
                return list;
            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }
        }, TreeDTO.class, CacheGroup.Authority);

        return getList;
    }

    @JsonIgnore
    private static List<TreeDTO> getTreeDTOAll(List<MenuDTO> menuList, Long parentId) {

        List<TreeDTO> list = new ArrayList<>();
        Stream<MenuDTO> childless;

        if (Framework.isNullOrEmpty(parentId)) {
            childless = menuList.stream().filter(w -> w.getParentId() == null);
        } else {
            childless = menuList.stream().filter(w -> w.getParentId() != null && w.getParentId() == parentId);
        }

        childless.forEach(f -> {
            boolean isHaveMenu = true;

            if (isHaveMenu || f.getCode().equalsIgnoreCase("MyWork")) {
                TreeDTO dto = f.toTreeDTO();
                dto.setChildren(getTreeDTOAll(menuList, f.getId()));
                dto.setIsEnable(dto.getIsHaveRelation() || dto.getChildren().size() > 0 || Framework.isNotNullOrEmpty(dto.getUrl()));
                if (!dto.getIsEnable()) dto.setIco(String.format("%sRead", dto.getIco()));
                if (dto.getIsEnable() || dto.getParentId() == null)
                    list.add(dto);
            }
        });

        return list;
    }

    @JsonIgnore
    private static List<MenuDTO> getMenuDTO() throws Exception {
        List<MenuDTO> list = new ArrayList<>();
        Menu.stream().sortedBy(s -> s.getSort()).forEach(f -> {
            list.add(f.toMenuDTO());
        });
        return list;
    }

    @JsonIgnore
    public MenuDTO getMenuDTO(Long menuId) throws Exception {

        String key = String.format("menu-getMenuDTO-%s", menuId);

        MenuDTO dto = AppHelper.manage().getCacheUtil().get(key, () -> {
            try {
                Optional<Menu> first = this.stream().where(w -> w.getId() == menuId).findFirst();
                if (first.isPresent()) return first.get().toMenuDTO();
                return null;
            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }
        }, MenuDTO.class, CacheGroup.Authority);

        return dto;
    }

    @JsonIgnore
    private MenuDTO toMenuDTO() {
        MenuDTO dto = new MenuDTO();
        dto.setId(this.getId());
        dto.setName(this.getName());
        dto.setTreeName(this.getTreeName());
        dto.setCode(this.getCode());
        dto.setSort(this.getSort());
        dto.setIsWorkCenter(this.getIsWorkCenter());
        dto.setHaveRelation(!this.getUIFormIsNull());
        dto.setIco(this.getIco());
        dto.setIsDefault(this.getIsDefault());
        if (Framework.isNotNullOrEmpty(this.getParentId())) dto.setParentId(this.getParentId().getId());
        if (Framework.isNotNullOrEmpty(this.getUrl())) {
            dto.setUrl(this.getUrl());
        } else {
            if (dto.getHaveRelation())
                dto.setUrl(String.format("/Web-GroupPage.html?menuId=%s", this.getId()));
        }

        return dto;
    }

    @JsonIgnore
    public Long gainFormId(Long formId) throws Exception {

        if (Framework.isNullOrEmpty(this.getUIFormIdReadOnly()))
            throw new BusinessException("您无权操作此表单!");

        if (Framework.isNullOrEmpty(formId)) formId = this.getUIFormIdReadOnly().getId();

        //判断权限（是否有使用权）

        return formId;
    }

    //创建表单
    @JsonIgnore
    public void createMenu(Menu parent, FormPage formPage, String name) throws Exception {

        Long id = formPage.getId();
        if (Framework.isNotNullOrEmpty(id) && this.stream().anyMatch(w -> w.getUIFormId() != null && w.getUIFormId().getId() == id))
            return;

        Menu formMenu = Menu.createNew();

        formMenu.setCode(formPage.getCode());
        formMenu.setName(name);
        formMenu.setIsEnabled(true);
        formMenu.setUIFormId(formPage);
        formMenu.setIsWorkCenter(false);

        //上一级
        formMenu.setParentId(parent);

        this.repository().add(formMenu);
    }

    @JsonIgnore
    public Menu getGroup(EntityMap map) throws Exception {
        String groupCode = String.format("%sGroup", map.getCode());

        Menu mapGroup = null;

        List<Menu> list = this.stream().where(w -> w.getCode() == groupCode).toList();
        if (list.size() > 0) mapGroup = list.get(0);

        if (mapGroup == null) {
            mapGroup = Menu.createNew();
            mapGroup.setCode(groupCode);
            mapGroup.setName(String.format("%s管理", map.getName()));
            mapGroup.setIsEnabled(true);
            this.repository().add(mapGroup);
        }

        mapGroup.initialization();

        return mapGroup;
    }

    @JsonIgnore
    @SystemService
    public UIPagedList getAuthorityConfig() throws Exception {

        List<UIAuthorityDTO> list = new ArrayList<>();

        if (Framework.isNotNullOrEmpty(this.getUIFormId())) {
            UIAuthorityDTO dto = UIAuthorityDTO.parse(this.getName());
            if (Framework.isNotNullOrEmpty(dto)) {
                dto.setDataId(this.getId());
                dto.setCode(this.getCode());
                dto.setType(UIAuthorityTypeEnum.Menu);
                dto.setName(String.format("%s(菜单)", dto.getName()));
                list.add(dto);
            }

            List<UIAuthorityDTO> authorityConfig = this.getUIFormId().getAuthorityConfig();
            for (UIAuthorityDTO item : authorityConfig) {
                if (!Framework.contains(list, item))
                    list.add(item);
            }
        }

        List<Object> getList = new ArrayList<>();

        list.forEach(f -> {
            f.setMenuId(this.getId());
            getList.add(f);
        });

        return UIPagedList.band(getList);
    }

    @SystemService(args = "dto")
    public void saveTreeSort(List<TreeDTO> dto) throws Exception {
        if (Framework.isNullOrEmpty(dto)) return;

        for (TreeDTO item : dto) {
            Menu menu = this.findById(new Long(item.getId()), item.getTimestamp());
            menu.setSort(item.getSort());
            if (Framework.isNullOrEmpty(item.getParentId())) {
                menu.setParentId(null);
            } else {
                menu.setParent(new Long(item.getParentId()));
            }
        }
    }
}
