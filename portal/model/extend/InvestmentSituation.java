package com.dxn.model.extend;

import com.dxn.dto.EnumerationDTO;
import com.dxn.dto.InvestmentSituationDTO;
import com.dxn.model.entity.InvestmentSituationEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.context.AppContext;
import com.dxn.system.dto.workFlow.WorkFlowNode;
import com.dxn.system.exception.BusinessException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "Ai_InvestmentSituation")
public class InvestmentSituation extends InvestmentSituationEntity {

    //插入前
    @Override
    public void onInserting() throws Exception {
        super.onInserting();
        //本人填写校验
        if (addingCheck()) throw new BusinessException("本人信息以填写");
    }

    //验证方法
    @Override
    public void onValidate() throws Exception {
        //业务代码
        super.onValidate();
        if (Framework.isNullOrEmpty(this.getRelationsWithApplicants()))throw new BusinessException("请录入与申报人关系字段");
        if (Framework.isNullOrEmpty(this.getNameOfSecurItiesHolder()))throw new BusinessException("请录入证券持有人姓名字段");
        if (Framework.isNullOrEmpty(this.getIdentificationNumber()))throw new BusinessException("请录入身份证或护照号码字段");
        if (Framework.isNotNullOrEmpty(this.getSecuritiesName())){
            if (Framework.isNullOrEmpty(this.getSecuritiesCode()))throw new BusinessException("证券代码需要填写");
        }
    }

    @Override
    public void onDeleting() throws Exception {
        //业务代码
        super.onDeleting();

    }

    //默认值
    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        this.setYear(defaultYear());
        this.setIsDisplay(10);
        //业务代码
    }




    //当前年
    private String defaultYear() throws Exception {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        String year = dateFormat.format(date);
        String a = year.substring(0, 4);
        return a;
    }

    //本人填写校验
    private boolean addingCheck() throws Exception {
        Integer relationsWithApplicants = this.getRelationsWithApplicants();
        Long number = 0L;
        if (relationsWithApplicants == 10) {
            Long aa = this.getInvestmentDeclarationId().getId();
            number = this.stream().where(w -> w.getInvestmentDeclarationId().getId() == aa && w.getRelationsWithApplicants() == 10).count();
        }
        if (number == 0) {
            return false;
        } else {
            return true;
        }
    }



    @SystemService(args = "userId")
    public List<String> gainReportFiles(Long userId) throws Exception {
        List<String> stringList=new  ArrayList<>();
        if (userId!=2){
            Optional<User> userFirst=User.stream().where(c -> c.getId() == userId).findFirst();
            if(userFirst.isPresent()) {
                User user=userFirst.get();
                Long staffId=user.getStaffId();
                Optional<Staff> staffFirst=Staff.stream().where(c -> c.getId() == staffId).findFirst();
                if(userFirst.isPresent()) {
                    Staff staff=staffFirst.get();
                    String identificationNumber=staff.getIdentificationNumber();
                    stringList.add(identificationNumber);
                }
            }
        }else {
            stringList.add("系统管理员身份证");
        }
        return stringList;
    }


    @SystemService(args = "userId")
    public List<InvestmentSituationDTO> lastYearInformation(Long userId) throws Exception {
        List<InvestmentSituationDTO> investmentSituationDTOList=new  ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, -1);
        Date date=calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        String year = dateFormat.format(date);
        String yserss1 = year.substring(0, 4);
        Optional<InvestmentDeclaration> investmentDeclarationFirst=InvestmentDeclaration.stream().where(c -> c.getUserId().getId()==userId).findFirst();
        if (investmentDeclarationFirst.isPresent()) {
            InvestmentDeclaration investmentDeclaration = investmentDeclarationFirst.get();
            Long investmentDeclarationId = investmentDeclaration.getId();
            List<InvestmentSituation> investmentSituation = this.stream().where(c -> c.getYear().equals(yserss1) && c.getInvestmentDeclarationId().getId().equals(investmentDeclarationId)).toList();
            for (InvestmentSituation item : investmentSituation) {
                InvestmentSituationDTO investmentSituationDTO=new InvestmentSituationDTO();
                investmentSituationDTO.setRelationsWithApplicants(item.getRelationsWithApplicants());
                investmentSituationDTO.setNameOfSecurItiesHolder(item.getNameOfSecurItiesHolder());
                investmentSituationDTO.setIdentificationNumber(item.getIdentificationNumber());
                investmentSituationDTO.setHandOverAccount(item.getHandOverAccount());
                investmentSituationDTO.setDeepAccount(item.getDeepAccount());
                investmentSituationDTO.setNationalAccounts(item.getNationalAccounts());
                investmentSituationDTO.setSecuritiesTrading(item.getSecuritiesTrading());
                investmentSituationDTO.setSecuritiesName(item.getSecuritiesName());
                investmentSituationDTO.setSecuritiesCode(item.getSecuritiesCode());
                investmentSituationDTO.setNumber(item.getNumber());
                investmentSituationDTO.setInvestmentSituation(item.getInvestmentSituation());
                investmentSituationDTO.setCustomerList(item.getCustomerList());
                investmentSituationDTO.setRemarks(item.getRemarks());
                investmentSituationDTO.setIsDisplay(20);
                investmentSituationDTOList.add(investmentSituationDTO);
            }
        }
        return investmentSituationDTOList;
    }

    @SystemService(args = "userId")
    public List<InvestmentSituationDTO> gainDefaultRow(Long userId) throws Exception {
        List<InvestmentSituationDTO>  investmentSituationDTOList=new ArrayList<>();
        InvestmentSituationDTO investmentSituationDTO=new InvestmentSituationDTO();
        Optional<User> userFirst=User.stream().where(c -> c.getId()==userId).findFirst();
        if (userFirst.isPresent()){
            Long staffId=userFirst.get().getStaffId();
            Optional<Staff> staffFirst = Staff.stream().where(c -> c.getId() == staffId).findFirst();
            if (staffFirst.isPresent()){
                Staff staff=staffFirst.get();
                String identificationNumber=staff.getIdentificationNumber();
                investmentSituationDTO.setRelationsWithApplicants(10);
                investmentSituationDTO.setNameOfSecurItiesHolder(staff.getName());
                investmentSituationDTO.setIdentificationNumber(identificationNumber);
                investmentSituationDTOList.add(investmentSituationDTO);
            }

        }
        return  investmentSituationDTOList;
    }

    @SystemService(args = "timeString")
    public  List<EnumerationDTO> gainInvestmentSituation(String timeString)throws Exception {
        List<EnumerationDTO> listDate = new ArrayList<>();
        List<InvestmentDeclaration> investmentDeclarationList=InvestmentDeclaration.stream().sortedDescendingBy(o -> o.getYear()).toList();
        for (InvestmentDeclaration investmentDeclaration:investmentDeclarationList){
            Integer id=10;
            int count=0;
            EnumerationDTO enumerationDTO=new EnumerationDTO();
            String year=investmentDeclaration.getYear();
            for(EnumerationDTO item:listDate){
                if (item.getText().equals(year)){
                    count=10;
                }
            }
            if (count!=10){
                enumerationDTO.setText(year);
                enumerationDTO.setId(year);
                listDate.add(enumerationDTO);
                id=id+10;
            }
        }
        return  listDate;
    }
}
