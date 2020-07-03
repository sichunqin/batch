package com.dxn.model.extend;

import com.dxn.forms.dto.TreeDTO;
import com.dxn.model.entity.QuickLinkGroupEntity;
import com.dxn.system.Framework;
import com.dxn.system.context.AppContext;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "Base_QuickLinkGroup")
public class QuickLinkGroup extends QuickLinkGroupEntity {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        this.setSort();
    }

    private void setSort() throws Exception {

        Integer sort = 0;

        //排序
        Collections.sort(this.getQuickLinkGroupItems(), Comparator.comparing(o -> o.getSort()));

        for (QuickLinkGroupItem item : this.getQuickLinkGroupItems()) {
            item.setSort(sort);
            sort++;
        }
    }

    public static List<TreeDTO> gainAuthorityMenus() throws Exception {
        List<TreeDTO> list = new ArrayList<>();
        Long userId = AppContext.current().getProfile().getId();
        if (Framework.isNullOrEmpty(userId)) return list;

        List<QuickLinkGroupUser> groups = QuickLinkGroupUser.stream().where(w -> w.getUserId().getId() == userId).toList();
        for (QuickLinkGroupUser item : groups) {
            if (Framework.isNotNullOrEmpty(item.getQuickLinkGroupId())) {
                item.getQuickLinkGroupId().getQuickLinkGroupItems().forEach(f -> {
                    Menu menu = f.getMenuId();
                    TreeDTO dto = new TreeDTO();
                    dto.setId(menu.getId().toString());
                    dto.setSort(f.getSort());
                    dto.setText(Framework.isNotNullOrEmpty(menu.getLinkName()) ? menu.getLinkName() : menu.getName());
                    list.add(dto);
                });
            }
        }

        return list;
    }
}
