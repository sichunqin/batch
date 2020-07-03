package com.dxn.model.extend;

import com.dxn.model.entity.IndependencePolicyEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "Ai_IndependencePolicy")
public class IndependencePolicy extends IndependencePolicyEntity {

    @Override
    public void onSetDefaultValue() throws Exception {

        super.onSetDefaultValue();

        //业务代码
        this.setPublisher(this.getCreatedById());
    }

}
