package com.dxn.model.extend;

import com.dxn.dto.CodeRuleDTO;
import com.dxn.entity.EntityBase;
import com.dxn.model.entity.CodeRuleEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.exception.DxnException;
import com.dxn.system.interfaces.ICodeRuleGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Base_CodeRule")
public class CodeRule extends CodeRuleEntity implements ICodeRuleGenerator {

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Entity);
    }

    @Override
    public void initialization(EntityBase entity) throws Exception {
        List<CodeRuleDTO> list = gainCodeRule(entity);
        for (CodeRuleDTO item : list) {
            item.initializationCodeRule(entity);
        }
    }

    @Override
    public void createCodeRule(EntityBase entity) throws Exception {
        List<CodeRuleDTO> list = gainCodeRule(entity);
        for (CodeRuleDTO item : list) {
            item.createCodeRule(entity);
        }
    }

    @Override
    public void reductionCodeRule(EntityBase entity) throws Exception {
        CodeRuleRecycled.releaseCodeRule(entity.getId(), entity.gainTypeName());
    }

    @JsonIgnore
    private List<CodeRuleDTO> gainCodeRule(EntityBase entity) throws Exception {
        String entityType = entity.gainTypeName();
        List<CodeRuleDTO> list = Framework.toList(gainCodeRule().stream().filter(f -> f.getEntityType().equalsIgnoreCase(entityType)));

        List<CodeRuleDTO> getList = new ArrayList<>();
        for (CodeRuleDTO item : list) {
            if (Framework.isNullOrEmpty(item.getExpression())) {
                item.setIsEnable(true);
            } else {
                String exp = String.format("Id==%s && (%s)", entity.getId(), item.getExpression().trim());
                if (repository().any(entity.gainTypeName(), exp)) {
                    Framework.log.info(String.format("找到表达式 %s !", exp));
                    item.setIsEnable(true);
                }
            }

            Optional<CodeRuleDTO> first = getList.stream().filter(f -> f.getAttribute().equalsIgnoreCase(item.getAttribute())).findFirst();
            if (first.isPresent()) {
                CodeRuleDTO dto = first.get();
                if (dto.getIsEnable()) {
                    if (item.getIsEnable()) throw new DxnException("同一个字段出现多个编码规则，请检查编码条件!");
                } else {
                    getList.remove(dto);
                    getList.add(item);
                }
            } else {
                getList.add(item);
            }
        }

        Framework.log.info(String.format("找到表达式 %s 个!", getList.size()));

        return getList;
    }

    @JsonIgnore
    private List<CodeRuleDTO> gainCodeRule() throws Exception {

        String key = String.format("gainEntityCodeRuleConfig");

        List<CodeRuleDTO> dto = AppHelper.manage().getCacheUtil().getList(key, () -> {
            try {
                List<CodeRuleDTO> getList = new ArrayList<>();
                List<CodeRule> codeRules = CodeRule.stream().where(w -> w.getEffective() == true).toList();
                for (CodeRule item : codeRules) {
                    getList.add(item.toCodeRuleDTO());
                }
                return getList;
            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }

        }, CodeRuleDTO.class, CacheGroup.Entity);

        return dto;
    }

    @JsonIgnore
    public CodeRuleDTO toCodeRuleDTO() throws Exception {
        CodeRuleDTO codeRuleDTO = new CodeRuleDTO();
        ModelHelper.copyModel(this, codeRuleDTO);
        codeRuleDTO.setEntityType(this.getEntityTypeId().getCode());
        codeRuleDTO.setAttribute(this.getAttributeId().getCode());
        codeRuleDTO.setIsEnable(false);

        for (CodeRuleSeg item : this.getCodeRuleSegs()) {
            codeRuleDTO.getItems().add(item.toCodeRuleItemDTO());
        }

        return codeRuleDTO;
    }

}
