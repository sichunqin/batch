package com.dxn.model.extend;

import com.dxn.dto.AuditClientDTO;
import com.dxn.model.entity.SignatureInformationListEntity;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Year;
import java.util.List;

@Entity
@Table(name = "Ai_SignatureInformationList")
public class SignatureInformationList extends SignatureInformationListEntity {



    @SystemService()
    public void independenceYearearEndTask() throws Exception {
        List<SignatureInformationList> signatureInformationList20=this.stream().where(c->c.getConfirmationStatus()!=30).toList();
        for (SignatureInformationList item:signatureInformationList20){
            if (item.getConfirmationStatus()==20){
                item.setConfirmationStatus(30);
            }else {
                repository().remove(item);
            }
            repository().add(item);
        }
    }

}
