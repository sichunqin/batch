package com.dxn.model.extend;

import com.dxn.dto.UserMenuDTO;
import com.dxn.forms.dto.TreeDTO;
import com.dxn.model.entity.UserQuickLinkEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.context.AppContext;
import com.dxn.system.dto.PagedList;
import org.jinq.jpa.JPAJinqStream;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;
import java.util.stream.Stream;

@Entity
@Table(name = "Base_UserQuickLink")
public class UserQuickLink extends UserQuickLinkEntity {

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Authority);
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Schema);
    }

    public static void clearUserQuickLinkForMenu(Long menuId) throws Exception {

        StringBuilder sql = new StringBuilder();
        JPAJinqStream<UserQuickLink> list = UserQuickLink.stream().where(w -> w.getMenuId().getId() == menuId);
        list.forEach(f -> {
            try {
                sql.append(f.toDeleteSql());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        repository().executeCommand(sql.toString());
    }

    @SystemService
    public PagedList gainUserMenuSource() throws Exception {
        List<TreeDTO> list = Menu.createNew().getMenu();
        PagedList getList = new PagedList();
        for (TreeDTO item : list) {
            item.forEachChildren((treeDTO) -> {
                try {
                    if (treeDTO.getChildren().size() <= 0) {
                        UserMenuDTO dto = new UserMenuDTO();
                        ModelHelper.copyModel(treeDTO, dto);
                        dto.setName(treeDTO.getText());
                        getList.addRow(dto);
                    }
                } catch (Exception e) {
                    Framework.printStackTrace(e);
                }
            });
        }

        return getList;
    }

    @SystemService(args = "list")
    public void saveUserLinkSource(List<Long> list) throws Exception {
        Long userId = AppContext.current().getProfile().getId();
        List<UserQuickLink> userMenu = this.stream().where(w -> w.getCreatedById() == userId).toList();
        for (Long id : list) {
            if (!userMenu.stream().anyMatch(f -> f.getMenuId().getId().equals(id))) {
                UserQuickLink model = this.createNew();
                model.setMenu(id);
                repository().add(model);
            }
        }

        this.onClearCache();
    }

    @SystemService(args = "list")
    public void deleteUserLinkSource(List<Long> list) throws Exception {
        Long userId = AppContext.current().getProfile().getId();
        List<UserQuickLink> userMenu = this.stream().where(w -> w.getCreatedById() == userId).toList();
        for (Long id : list) {
            Optional<UserQuickLink> first = userMenu.stream().filter(f -> f.getId().equals(id)).findFirst();
            if (first.isPresent()) {
                repository().remove(first.get());
            }
        }

        this.onClearCache();
    }

    @SystemService
    public List<TreeDTO> gainUserLinkSource() throws Exception {
        Long userId = AppContext.current().getProfile().getId();
        String key = String.format("gainUserLinkSource-%s", userId);

        List<TreeDTO> getList = AppHelper.manage().getCacheUtil().getList(key, () -> {
            try {

                List<TreeDTO> list = new ArrayList<>();

                List<TreeDTO> authorityMenus = User.findById(userId).gainAuthorityMenus();
                //List<UserQuickLink> userMenu = this.stream().where(w -> w.getCreatedById() == userId).toList();
                List<TreeDTO> menuList = Menu.getMenuAll();

                for (TreeDTO item : menuList) {
                    item.forEachMyAndChildren((treeDTO) -> {
                        try {
                            Optional<TreeDTO> first = authorityMenus.stream().filter(f -> f.getId().equalsIgnoreCase(treeDTO.getId())).findFirst();
                            if (first.isPresent()) {
                                treeDTO.setText(first.get().getText());
                                treeDTO.setSort(first.get().getSort());
                                treeDTO.putMainUrl();

                                if (treeDTO.getChildren().size() > 0) {
                                    treeDTO.setChildren(new ArrayList<>());
                                }

                                list.add(treeDTO);
                            }
                        } catch (Exception e) {
                            Framework.printStackTrace(e);
                        }
                    });
                }

                Collections.sort(list, Comparator.comparing(o -> o.getSort()));

                return list;

            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }

        }, TreeDTO.class, CacheGroup.Authority);

        return getList;
    }
}
