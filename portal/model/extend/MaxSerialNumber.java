package com.dxn.model.extend;

import com.dxn.entity.EntityBase;
import com.dxn.model.entity.MaxSerialNumberEntity;
import com.dxn.system.Framework;
import com.dxn.system.transaction.TransactionHelper;
import com.dxn.system.typeFinder.TypeFinderHelper;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Base_MaxSerialNumber")
public class MaxSerialNumber extends MaxSerialNumberEntity {
    public static String gainMaxSerialNumber(Long codeRuleId, String usingCode, String allCode, EntityBase entity, String attribute, boolean isRecycled) throws Exception {

        String code = TransactionHelper.newTransaction(() -> {
            try {
                Integer maxNum = gainMaxNum(codeRuleId, usingCode);
                if (Framework.isNullOrEmpty(maxNum)) {
                    maxNum = 1;
                    createMaxNum(codeRuleId, usingCode, maxNum);
                } else {
                    maxNum += 1;
                    addMaxNum(codeRuleId, usingCode, maxNum);
                }

                String maxCode = gainAllCode(allCode, maxNum);

                //加入回收池
                if (isRecycled)
                    CodeRuleRecycled.createCode(maxCode, usingCode, entity.gainTypeName(), attribute);

                return maxCode;
            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }
        });

        //事物提交后，在另一个事物认领
        if (isRecycled)
            CodeRuleRecycled.claimCode(code, usingCode, entity.getId(), entity.gainTypeName(), attribute);

        if (Framework.isNotNullOrEmpty(code)) {
            Framework.log.info(String.format("字段 %s ,key %s,生成编码 %s!", attribute, usingCode, code));
        } else {
            Framework.log.info(String.format("字段 %s ,key %s,生成编码失败!", attribute, usingCode));
        }

        return code;
    }

    private static Integer gainMaxNum(Long codeRuleId, String usingCode) throws Exception {
        String sql = "Select value from Base_MaxSerialNumber with (TABLOCKX) where CodeRuleId=? and sysKey=?";
        List<Object> parameters = new ArrayList<>();
        parameters.add(codeRuleId);
        parameters.add(usingCode);
        return repository().sqlQueryFirst(sql, parameters, Integer.class);
    }

    private static void createMaxNum(Long codeRuleId, String usingCode, Integer maxNum) throws Exception {
        MaxSerialNumber maxSerialNumber = MaxSerialNumber.createNew();
        maxSerialNumber.setCodeRule(codeRuleId);
        maxSerialNumber.setSysKey(usingCode);
        maxSerialNumber.setValue(maxNum);
        maxSerialNumber.setDefaultValue();
        repository().executeCommand(maxSerialNumber.toInsertSql());
    }

    private static void addMaxNum(Long codeRuleId, String usingCode, Integer maxNum) throws Exception {
        String updateSql = "update Base_MaxSerialNumber set value=?,ModifiedOn=GETDATE() where CodeRuleId=? and [sysKey]=?";
        List<Object> updateParameters = new ArrayList<>();
        updateParameters.add(maxNum);
        updateParameters.add(codeRuleId);
        updateParameters.add(usingCode);
        repository().executeCommand(updateSql, updateParameters);
    }

    private static String gainAllCode(String allCode, int num) {
        List<String> list = Framework.gainContent(allCode, "{0:d", "}");
        for (String item : list) {
            Integer length = TypeFinderHelper.parseInteger(item);
            String numStr = Framework.isNullOrEmpty(length) ? String.valueOf(num) : Framework.getMinStr(String.valueOf(num), length);
            allCode = allCode.replace("{0:d" + item + "}", numStr);
        }
        return allCode;
    }
}
