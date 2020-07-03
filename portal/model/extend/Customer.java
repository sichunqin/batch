package com.dxn.model.extend;

import com.dxn.dto.CustomerAgreementFormDTO;
import com.dxn.dto.RelatedItemsDTO;
import com.dxn.model.entity.CustomerEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.context.AppContext;
import com.dxn.system.dto.workFlow.MyTodoListDTO;
import com.dxn.system.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "Cust_Customer")
public class Customer extends CustomerEntity {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        this.setDocState();
        Long deptId = AppContext.current().getProfile().getDeptId();
        Optional<Department> departmentFirst = Department.stream().where(c -> c.getId() == deptId).findFirst();
        if (departmentFirst.isPresent()) {
            this.setDeptId(departmentFirst.get());
        }
    }


    @Override
    public void onInserting() throws Exception {
        String num = this.getCounty() + "/" + this.getProvince() + "/" + this.getCity();
        String value = EnumValue.createNew().gainEnumDataDTO("Address", num);
        if (Framework.isNullOrEmpty(this.getDetailedAddress())) {
            this.setShowAddress(value);
        } else {
            this.setShowAddress(value + "/" + this.getDetailedAddress());
        }
    }

    @Override
    public void onDeleting() throws Exception {
        super.onDeleting();
        Long userId=AppContext.current().getProfile().getId();
        if ( userId!=2 && !userId.equals(this.getCreatedById())){
            throw new BusinessException("只有此客户创建人才能删除");
        };
        //业务代码
    }


    @Override
    public void onUpdating() throws Exception {
        String num = this.getCounty() + "/" + this.getProvince() + "/" + this.getCity();
        String value = EnumValue.createNew().gainEnumDataDTO("Address", num);

        if (Framework.isNullOrEmpty(this.getDetailedAddress())) {
            this.setShowAddress(value);
        } else {
            this.setShowAddress(value + "/" + this.getDetailedAddress());
        }
    }

    @Override
    public void onValidate() throws Exception {
        super.onValidate();

        checkDuplication();
//        if (Framework.isNotNullOrEmpty(this.getPostalCode())){
//            if (!isPostCode(this.getPostalCode()))throw new BusinessException("邮政编码不符合格式");
//        }

        //this.validateCheckBankCard();
//        if (!Framework.isNullOrEmpty(this.getEamil())) {
//            if (!Framework.isEmail(this.getEamil())) throw new BusinessException("联系人email不符合格式");
//        }

//        if (this.getPhone().indexOf("-") == -1) {
//            if (this.getPhone().length() != 11) {
//                throw new BusinessException("手机号不符合规则");
//            }
//        } else {
//            if (this.getPhone().indexOf("-") < 0) {
//                throw new BusinessException("固定电话缺少 - 号");
//            }
//        }

//        if (Framework.isNotNullOrEmpty(this.getTaxpayerPhone()) && this.getTaxpayerPhone().indexOf("-") == -1) {
//            if (this.getTaxpayerPhone().length() != 11) {
//                throw new BusinessException("手机号不符合规则");
//            }
//        } else {
//            if (Framework.isNotNullOrEmpty(this.getTaxpayerPhone()) && this.getTaxpayerPhone().indexOf("-") < 0) {
//                throw new BusinessException("固定电话缺少 - 号");
//            }
//        }
        if (!isUSCCode(this.getUSCCode()))
            throw new BusinessException("统一信征代码必须是15位或18位");
        if (Framework.isNotNullOrEmpty(this.getTaxpayerCode()) &&!isUSCCode(this.getTaxpayerCode()))
            throw new BusinessException("纳税人识别号必须是15位或18位");

    }

    //匹配中国邮政编码
    public  boolean isPostCode(String postCode){
        //定义正则
        String str= "^[0-9]\\d{5}$";
        Pattern p =Pattern.compile(str);
        Matcher m=p.matcher(postCode);
        return m.matches();
    }
    //匹配统一信征代码
    public  boolean isUSCCode(String uSCCode){
        //定义正则
        String str= "(^[a-zA-Z0-9]{15}$)|(^[a-zA-Z0-9]{18}$)";
        Pattern p =Pattern.compile(str);
        Matcher m=p.matcher(uSCCode);
        return m.matches();
    }

    @Override
    public boolean canDelete() {
        return true;
    }

    private void setDocState() {
        if (Framework.isNotNullOrEmpty(this.getDocState()) && this.getDocState() >= 10) return;
        this.setDocState(10);
    }

    @SystemService(args = "customerId")
    public List<RelatedItemsDTO> getCustomer(Long customerId) throws Exception {
        List<RelatedItemsDTO> list = new ArrayList<>();
        List<Project> projectlist = Project.stream().where(c -> c.getCustomerNameId().getId() == customerId).toList();
        for (Project item : projectlist) {
            RelatedItemsDTO relatedItemsDTO = new RelatedItemsDTO();
            relatedItemsDTO.setId(item.getId());
            relatedItemsDTO.setCode(item.getCode());
            relatedItemsDTO.setName(item.getName());
            relatedItemsDTO.setProjectGrade(1);
            relatedItemsDTO.setCustomerType(item.getCustomerType());
            relatedItemsDTO.setBusinessType(item.getBusinessType());
            relatedItemsDTO.setRiskLevel(item.getRiskLevel());
            relatedItemsDTO.setDepartment(null);
            relatedItemsDTO.setProjectPartnerId(item.getProjectPartnerId().getName());
            relatedItemsDTO.setProjectManagerId(item.getProjectManagerId().getName());
            relatedItemsDTO.setProjectStatus(item.getProjectStatus());
            list.add(relatedItemsDTO);
        }
        return list;
    }

    @SystemService(args = "customerId")
    public List<CustomerAgreementFormDTO> getCustomerAgreement(Long customerId) throws Exception {
        List<CustomerAgreementFormDTO> list = new ArrayList<>();
        List<CompositionCustomer> compositionCustomerList = CompositionCustomer.stream().where(c -> c.getProjectId().getId() == customerId).toList();
        for (CompositionCustomer compositionCustomer : compositionCustomerList) {
            List<Engagement> EngagementList = Engagement.stream().where(c -> c.getCompositionCustomerId() == compositionCustomer).toList();
            for (Engagement item : EngagementList) {
                CustomerAgreementFormDTO customerAgreementFormDTO = new CustomerAgreementFormDTO();
                customerAgreementFormDTO.setEntryName(compositionCustomer.getName());
                customerAgreementFormDTO.setCode(item.getCode());
                customerAgreementFormDTO.setDocState(item.getDocState());
                customerAgreementFormDTO.setDateOfSign(item.getDateOfSign());
                customerAgreementFormDTO.setAmount(item.getAmount());
                customerAgreementFormDTO.setPrintUnit(item.getPrintUnit());
                customerAgreementFormDTO.setDescription(item.getDescription());
                list.add(customerAgreementFormDTO);
            }
        }
        this.gainIsHaveJurisdiction(customerId);
        return list;
    }

    //用户权限测试
    public boolean gainIsHaveJurisdiction(Long projectId) throws Exception {
        Long currentLoginId = AppContext.current().getProfile().getId();
        List<ProjectMembers> projectMembersList = ProjectMembers.stream().where(c -> c.getProjectId().getId() == projectId).toList();
        for (ProjectMembers a : projectMembersList) {
            if (currentLoginId.equals(a.getNameId().getId())) {
                Framework.log.info("用户属于这个项目");
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public MyTodoListDTO gainWorkFlowDTO(MyTodoListDTO value) throws Exception {
        value.setDepartmentType(this.getCode());
        User user = this.getCreatedBy();
        if (Framework.isNotNullOrEmpty(user)) {
            if (Framework.isNotNullOrEmpty(user.getDept())) {
                value.setDepartment(user.getDept().getTreeName());
            }
        }
        String processId = value.getProcess_Id();
        Optional<WorkFlowConfig> workFlowConfiglist = WorkFlowConfig.stream().where(w -> w.getProcessId().equals(processId)).findFirst();
        if (workFlowConfiglist.isPresent()) {
            String deptIdName=user.getDept().getTreeName();
            if (Framework.isNullOrEmpty(deptIdName)){
                deptIdName=user.getDept().getName();
            }
            String  workflowUuid=value.getWorkflowUuid();
            Optional<WorkFlowConfig> workFlowConfigList=WorkFlowConfig.stream().where(w->w.getUuid().equals(workflowUuid)).findFirst();
            if (workFlowConfigList.isPresent()){
                String workFlowConfigName=workFlowConfigList.get().getWorkFlowTypeId().getName();
                value.setWorkFlowName(deptIdName + "-" +workFlowConfigName);
            }
        }
        return value;
    }
    //重复校验
    private void checkDuplication() throws Exception {
        Long number = 0L;
        Long number1 = 0L;
        Long number2 = 0L;
        Long id = this.getId();
        String name=this.getName();
        String uSCCode=this.getUSCCode();
        String securitiesCode=this.getSecuritiesCode();
        if (Framework.isNullOrEmpty(id)) {
            number = this.stream().where(w -> w.getName().equals(name)).count();
        } else {
            number = this.stream().where(w -> w.getName().equals(name) && !w.getId().equals(id)).count();
        }
        if (number > 0) throw new BusinessException("客户名称不能重复");
        if (Framework.isNullOrEmpty(id)) {
            number1 = this.stream().where(w -> w.getUSCCode().equals(uSCCode)).count();
        } else {
            number1 = this.stream().where(w -> w.getUSCCode().equals(uSCCode) && !w.getId().equals(id)).count();
        }
        if (number1 > 0) throw new BusinessException("统一社会信用代码不能重复");
        if (Framework.isNullOrEmpty(id)) {
            number2 = this.stream().where(w -> w.getSecuritiesCode().equals(securitiesCode)).count();
        } else {
            number2 = this.stream().where(w -> w.getSecuritiesCode().equals(securitiesCode) && !w.getId().equals(id)).count();
        }
        if (number2 > 0) throw new BusinessException("证券代码不能重复");


    }

    //银行卡号校验
    private void validateCheckBankCard() throws Exception {
        String bankAccount = this.getBankAccount();
        if (Framework.isNullOrEmpty(bankAccount)) return;
        if (bankAccount.length() > 15 && bankAccount.length() < 20) {
            char bit = getBankCardCheckCode(bankAccount.substring(0, bankAccount.length() - 1));
            if (bit != 'N' && bankAccount.charAt(bankAccount.length() - 1) == bit) return;
        }
        throw new BusinessException("银行格式不符合");
    }

    public static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    @SystemService(args = "projectId")
    public boolean associatedProjectPermission(Long projectId) throws Exception {
        List<CompositionMembers> members = CompositionMembers.stream().where(c -> c.getCompositionCustomerId().getProjectId().getId() == projectId).toList();
        for (CompositionMembers item : members) {
            Long userId = item.getNameId().getId();
            Long login = AppContext.current().getProfile().getId();
            if (login.equals(userId)) {
                return false;
            }
        }
        return true;
    }

    public String attributePeriodDecline()throws Exception{
        Long id = this.getId();
        Long count = Project.stream().where(w->w.getCustomerNameId().getId()==id).count();
        if(count ==0)
        {
            count=CompositionCustomer.stream().where(w->w.getCustomerNameId() !=null && w.getCustomerNameId().getId() == id).count();
        }
        if(count == 0)
        {
            return "本期减少";
        }
        return "本期增加";
    }

    public String attributeReportDept() throws Exception{
        String strDept="";
        Long id = this.getId();
        List<Report> reports = Report.stream().where(w->w.getCompositionCustomerId().getCustomerNameId() !=null && w.getCompositionCustomerId().getCustomerNameId().getId() ==id).toList();
        for(Report item:reports)
        {
            if(Framework.isNullOrEmpty(strDept))
            {
                strDept = item.getDeptId().getTreeName();
            }else{
                if( !strDept.equals(item.getDeptId().getTreeName()) && !strDept.contains(","+item.getDeptId().getTreeName()))
                    strDept +=","+item.getDeptId().getTreeName();
            }
        }
        return strDept;
    }
}
