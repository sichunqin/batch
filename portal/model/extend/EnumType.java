package com.dxn.model.extend;

import com.dxn.dto.EnumValueDTO;
import com.dxn.forms.dto.ComboboxDTO;
import com.dxn.model.entity.EnumTypeEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

@Entity
@Table(name = "Sys_EnumType")
public class EnumType extends EnumTypeEntity {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        this.setSort();
    }

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Enums);
    }

    private void setSort() {

        Integer sort = 0;

        //排序
        Collections.sort(this.getEnumValues(), Comparator.comparing(o -> o.getSort()));

        for (EnumValue item : this.getEnumValues()) {
            item.setSort(sort);
            sort++;
        }
    }

    @JsonIgnore
    public List<ComboboxDTO> getTreeEnum(String code) throws Exception {

        String key = String.format("getTreeEnum_%s", code);

        List<ComboboxDTO> dto = AppHelper.manage().getCacheUtil().getList(key, () -> {
            try {
                Optional<EnumType> enumType = this.stream().where(w -> w.getCode() == code).findFirst();
                if (!enumType.isPresent()) return null;

                EnumType first = enumType.get();

                List<EnumValueDTO> list = new ArrayList<>();

                first.getEnumValues().forEach(f -> {
                    try {
                        list.add(f.toEnumValueDTO());
                    } catch (Exception e) {
                        Framework.printStackTrace(e);
                    }
                });

                //排序
                Collections.sort(list, Comparator.comparing(o -> o.getSort()));

                return getTreeChildrenEnum(list, -1);
            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }

        }, ComboboxDTO.class, CacheGroup.Enums);

        if (Framework.isNullOrEmpty(dto)) throw new BusinessException(String.format("类型 %s 不存在！", code));

        return dto;
    }

    @JsonIgnore
    public List<ComboboxDTO> getTreeChildrenEnum(List<EnumValueDTO> mainList, int parentValue) {
        List<ComboboxDTO> list = new ArrayList<>();

        for (EnumValueDTO e : mainList) {
            if (e.getParentValue().equals(parentValue)) {
                ComboboxDTO item = new ComboboxDTO();
                item.setId(e.getValue().toString());
                item.setText(e.getName());
                item.setSort(e.getSort());
                item.setTreeName(e.getTreeName());
                item.setChildren(getTreeChildrenEnum(mainList, e.getValue()));
                list.add(item);
            }
        }

        return list;
    }

//    //流程枚举过滤
//    @SystemService(args = "entityMapId")
//    public List<EntityMapItem> enumerationFiltering(Long entityMapId) throws Exception {
//        List<EntityMapItem> formPagelist = EntityMapItem.stream().where(c -> c.getEntityMapId().getId() == entityMapId && c.getEnumType()!=null).toList();
//        return  formPagelist;
//    }

}
