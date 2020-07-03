package com.dxn.model.extend;

import com.dxn.model.entity.ContactUsEntity;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Entity
@Table(name = "PM_ContactUs")
public class ContactUs extends ContactUsEntity {

    @SystemService
    public Map<String,String> getDisplayData() throws Exception{
        Map<String,String> resultMap = new HashMap();
        Optional<ContactUs> contactUsOptional = ContactUs.stream().findFirst();
        if(contactUsOptional.isPresent()){
            ContactUs contactUs = contactUsOptional.get();
            resultMap.put("HotLine",contactUs.getAfterSaleServiceHotline());
            resultMap.put("DutyTel",contactUs.getAfterSaleDutyTelephone());
            resultMap.put("QQNumber",contactUs.getAfterSaleQQServiceNumber());
        }
        return resultMap;
    }


    @SystemService
    public ContactUs queryData() throws Exception{
        Optional<ContactUs> optionalContactUs = ContactUs.stream().findFirst();
        if(optionalContactUs.isPresent()){
            return optionalContactUs.get();
        }
        return null;
    }

}
