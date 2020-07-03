package com.dxn.model.extend;

import com.dxn.dto.CodeRuleItemDTO;
import com.dxn.model.entity.CodeRuleSegEntity;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Base_CodeRuleSeg")
public class CodeRuleSeg extends CodeRuleSegEntity {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        if (Framework.isNotNullOrEmpty(this.getValue())) this.setValue(this.getValue().trim());
    }


    @JsonIgnore
    public CodeRuleItemDTO toCodeRuleItemDTO() throws Exception {
        CodeRuleItemDTO codeRuleItemDTO = new CodeRuleItemDTO();
        ModelHelper.copyModel(this, codeRuleItemDTO);
        return codeRuleItemDTO;
    }
}
