package com.dxn.model.extend;

import com.dxn.entity.EntityBase;
import com.dxn.model.entity.CodeRuleRecycledEntity;
import com.dxn.system.Framework;
import com.dxn.system.exception.DxnException;
import com.dxn.system.transaction.TransactionHelper;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Base_CodeRuleRecycled")
public class CodeRuleRecycled extends CodeRuleRecycledEntity {

    public static String gainMaxSerialNumber(String usingCode, EntityBase entity, String attribute) throws Exception {

        String code = TransactionHelper.newTransaction(() -> {
            try {
                return CodeRuleRecycled.gainNotUsingCode(entity.gainTypeName(), attribute, usingCode);
            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }
        });

        //事物提交后，在另一个事物认领
        if (Framework.isNotNullOrEmpty(code)) {
            Framework.log.info(String.format("字段 %s ,key %s,找到回收编码 %s!", attribute, usingCode, code));
            CodeRuleRecycled.claimCode(code, usingCode, entity.getId(), entity.gainTypeName(), attribute);
        } else {
            Framework.log.info(String.format("字段 %s ,key %s,未找到回收编码!", attribute, usingCode));
        }

        return code;
    }


    private static String gainNotUsingCode(String entityType, String attribute, String sysKey) throws Exception {
        String sql = "Select top 1 * from Base_CodeRuleRecycled with(TABLOCKX) where EntityId is null and EntityType=? and Attribute=? and sysKey=? and ModifiedOn<=? order by code asc ";

        List<Object> parameters = new ArrayList<>();
        parameters.add(entityType);
        parameters.add(attribute);
        parameters.add(sysKey);
        parameters.add(LocalDateTime.now().plusMinutes(-5));

        CodeRuleRecycled codeRuleRecycled = repository().sqlQueryFirst(sql, parameters, CodeRuleRecycled.class);
        if (Framework.isNullOrEmpty(codeRuleRecycled)) return null;

        codeRuleRecycled.setModifiedOn(LocalDateTime.now());
        repository().executeCommand(codeRuleRecycled.toUpdateSql());

        return codeRuleRecycled.getCode();
    }

    public static void createCode(String code, String sysKey, String entityType, String attribute) throws Exception {
        Optional<CodeRuleRecycled> first = CodeRuleRecycled.stream().where(w -> w.getCode() == code && w.getSysKey() == sysKey && w.getAttribute() == attribute && w.getEntityType() == entityType).findFirst();
        if (first.isPresent()) {
            if (Framework.isNotNullOrEmpty(first.get().getEntityId()))
                throw new DxnException(String.format("当前编号生成重复,请更新最大流水!"));
        } else {
            CodeRuleRecycled codeRuleRecycled = new CodeRuleRecycled();
            codeRuleRecycled.setCode(code);
            codeRuleRecycled.setSysKey(sysKey);
            codeRuleRecycled.setAttribute(attribute);
            codeRuleRecycled.setEntityType(entityType);
            codeRuleRecycled.setDefaultValue();

            repository().executeCommand(codeRuleRecycled.toInsertSql());
        }
    }

    public static void claimCode(String code, String sysKey, Long entityId, String entityType, String attribute) throws Exception {

        Optional<CodeRuleRecycled> first = CodeRuleRecycled.stream().where(w -> w.getEntityType() == entityType && w.getAttribute() == attribute && w.getCode() == code && w.getSysKey() == sysKey).findFirst();
        if (!first.isPresent()) throw new DxnException("未找到要认领的编号!");
        StringBuilder sql = new StringBuilder();

        List<CodeRuleRecycled> list = CodeRuleRecycled.stream().where(w -> w.getEntityType() == entityType && w.getAttribute() == attribute && w.getEntityId() == entityId).toList();
        for (CodeRuleRecycled item : list) {
            item.setEntityId(null);
            sql.append(item.toUpdateSql());
        }

        CodeRuleRecycled ruleRecycled = first.get();
        ruleRecycled.setEntityId(entityId);
        sql.append(ruleRecycled.toUpdateSql());

        repository().executeCommand(sql.toString());
    }

    public static void releaseCodeRule(Long entityId, String entityType) throws Exception {
        StringBuilder sql = new StringBuilder();
        List<CodeRuleRecycled> list = CodeRuleRecycled.stream().where(w -> w.getEntityType() == entityType && w.getEntityId() == entityId).toList();
        for (CodeRuleRecycled item : list) {
            item.setEntityId(null);
            sql.append(item.toUpdateSql());
        }
        repository().executeCommand(sql.toString());
    }
}
