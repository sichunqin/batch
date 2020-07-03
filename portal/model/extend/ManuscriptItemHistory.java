package com.dxn.model.extend;

import com.dxn.forms.dto.UIPagedList;
import com.dxn.model.entity.ManuscriptItemHistoryEntity;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Prj_ManuscriptItemHistory")
public class ManuscriptItemHistory extends ManuscriptItemHistoryEntity {

    @SystemService(args = "query_auditworkProjectID,query_fiscalYear,query_parentStructureCode,query_splitForFiscalYear,formId")
    public UIPagedList getManuscriptItems(String auditworkProjectID, Integer fiscalYear, String parentStructureCode,String splitForFiscalYear, Long formId) throws Exception {
        Integer fiscalYearVal = fiscalYear;

        List<ManuscriptItem> itemList;
        if(splitForFiscalYear.equals("0")) //不拆分财年
        {
            itemList = ManuscriptItem.stream().where(w -> w.getAuditworkProjectID().equals(auditworkProjectID)
                    && w.getIsGroup().equals("0") && w.getParentStructureCode().equals(parentStructureCode) && w.getIsSelected().equals("1")
                    && w.getIsDisplay().equals("1") && (w.getModifyStatus() ==30 ||w.getModifyStatus() ==20 )).sortedBy(s -> s.getSortNO()).toList();
        }else
        {
            itemList = ManuscriptItem.stream().where(w -> w.getFiscalYear().equals(fiscalYearVal) && w.getAuditworkProjectID().equals(auditworkProjectID)
                    && w.getIsGroup().equals("0") && w.getParentStructureCode().equals(parentStructureCode) && w.getIsSelected().equals("1")
                    && w.getIsDisplay().equals("1") && (w.getModifyStatus() ==30 ||w.getModifyStatus() ==20 )).sortedBy(s -> s.getSortNO()).toList();
        }
        List<Object> list = new ArrayList<>();
        itemList.forEach(f -> {
            f.initialization();
            list.add(f);
        });

        return UIPagedList.band(list, FormPage.getFormColumn(formId));
    }
}
