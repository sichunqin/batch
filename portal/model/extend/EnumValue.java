package com.dxn.model.extend;

import com.dxn.dto.EnumValueDTO;
import com.dxn.forms.dto.ComboboxDTO;
import com.dxn.model.entity.EnumValueEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.configuration.DxnConfig;
import com.dxn.system.dto.EnumDataDTO;
import com.dxn.system.exception.BusinessException;
import com.dxn.system.interfaces.IEnumService;
import com.dxn.system.typeFinder.TypeFinderHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Sys_EnumValue")
public class EnumValue extends EnumValueEntity implements IEnumService {

    @Override
    public void onValidate() throws Exception {
        super.onValidate();
        if (this.getValue() < 0) throw new BusinessException("枚举值不能小于0!");
    }

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Enums);
    }


    @JsonIgnore
    public List<ComboboxDTO> getEnum(String code, Integer parentValue, Integer level) throws Exception {

        String key = String.format("getEnum_%s%s%s", code, parentValue, level);

        List<ComboboxDTO> dto = AppHelper.manage().getCacheUtil().getList(key, () -> {
            try {
                if (level > 0) {
                    //搜索框用
                    String sql = "Select v.value as Id,v.name as [Text],v.sort as Sort from Sys_EnumValue v inner join Sys_EnumType t on v.enumTypeId=t.id where t.code=? and v.level=? and v.isEnabled='true' order by v.sort;";
                    List<Object> parameters = new ArrayList<>();
                    parameters.add(code);
                    parameters.add(level);
                    return repository().sqlQuery(sql, parameters, ComboboxDTO.class);
                }

                if (parentValue < 0) {
                    String sql = "Select v.value as Id,v.name as [Text],v.sort as Sort from Sys_EnumValue v inner join Sys_EnumType t on v.enumTypeId=t.id where t.code=? and v.parentId is null and v.isEnabled='true' order by v.sort;";
                    List<Object> parameters = new ArrayList<>();
                    parameters.add(code);
                    return repository().sqlQuery(sql, parameters, ComboboxDTO.class);
                } else {
                    String sql = "Select v.value as Id,v.name as [Text],v.sort as Sort from Sys_EnumValue v inner join Sys_EnumValue p on v.parentId=p.id inner join Sys_EnumType t on v.enumTypeId=t.id where t.code=? and p.value=? and v.isEnabled='true' order by v.sort;";
                    List<Object> parameters = new ArrayList<>();
                    parameters.add(code);
                    parameters.add(parentValue);
                    return repository().sqlQuery(sql, parameters, ComboboxDTO.class);
                }

            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }

        }, ComboboxDTO.class, CacheGroup.Enums);

        if (Framework.isNullOrEmpty(dto))
            throw new BusinessException(String.format("类型 %s 不存在！", code));

        return dto;

    }

    private static List<EnumValueDTO> enumValueAll = null;

    @JsonIgnore
    public List<EnumValueDTO> gainEnumValueAll() throws Exception {

        if (!DxnConfig.getIsUseCache() && Framework.isNotNullOrEmpty(enumValueAll)) return enumValueAll;

        String key = String.format("EnumValue_gainEnumValueAll");

        List<EnumValueDTO> list = AppHelper.manage().getCacheUtil().getList(key, () -> {
            try {
                String sql = "Select v.code as Code,v.name as Name,v.value as Value,v.sort as Sort,t.code as [Type],ISNULL(p.value,-1) as ParentValue from Sys_EnumValue v inner join Sys_EnumType t on v.enumTypeId=t.id left join Sys_EnumValue p on v.parentId=p.id;";
                return repository().sqlQuery(sql, EnumValueDTO.class);
            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }
        }, EnumValueDTO.class, CacheGroup.Enums);

        Framework.showSystemMes();

        if (Framework.isNullOrEmpty(list))
            throw new BusinessException("没有可用的枚举!");

        if (!DxnConfig.getIsUseCache()) enumValueAll = list;

        return list;
    }


    @JsonIgnore
    public EnumValueDTO getEnumDTO(String code, Integer value) throws Exception {

        String key = String.format("EnumValue_getEnumDTO%s_%s", code, value);

        EnumValueDTO dto = AppHelper.manage().getCacheUtil().get(key, () -> {
            try {
                List<EnumValueDTO> list = gainEnumValueAll();
                Optional<EnumValueDTO> first = list.stream().filter(f -> f.getType().equalsIgnoreCase(code) && f.getValue().equals(value)).findFirst();
                if (first.isPresent()) return first.get();
                return null;
            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }
        }, EnumValueDTO.class, CacheGroup.Enums);

        //if (Framework.isNullOrEmpty(dto)) throw new BusinessException(String.format("类型 %s 枚举值 %s 不存在！", code, value));

        return dto;
    }


    @Override
    public EnumDataDTO gainEnumDataDTO(String code, Integer value) throws Exception {
        return getEnumDTO(code, value).toEnumDataDTO();
    }

    @Override
    public String gainEnumDataDTO(String code, String value) throws Exception {
        List<String> list = new ArrayList<>();
        String chr = ",";
        if (Framework.isNotNullOrEmpty(value)) {
            chr = value.contains("/") ? "/" : ",";
            String[] keys = value.split(chr);
            for (String item : keys) {
                Integer enumValue = TypeFinderHelper.parseInteger(item);
                if (Framework.isNotNullOrEmpty(enumValue)) {
                    EnumValueDTO enumValueDTO = getEnumDTO(code, enumValue);
                    if (Framework.isNotNullOrEmpty(enumValueDTO)) {
                        EnumDataDTO dto = enumValueDTO.toEnumDataDTO();
                        if (Framework.isNotNullOrEmpty(dto)) {
                            list.add(dto.getText());
                        } else {
                            list.add(item);
                        }
                    } else {
                        list.add(item);
                    }
                }
            }
        }
        return Framework.toMosaicString(list, chr);
    }

    public EnumValueDTO toEnumValueDTO() throws Exception {
        EnumValueDTO dto = new EnumValueDTO();

        //赋值值
        ModelHelper.copyModel(this, dto);

        //类型
        dto.setType(this.getEnumType().getCode());

        //父类的值
        dto.setParentValue(-1);
        if (this.getParent() != null) dto.setParentValue(this.getParent().getValue());

        return dto;
    }
}
