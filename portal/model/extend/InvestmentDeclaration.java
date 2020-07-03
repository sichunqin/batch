package com.dxn.model.extend;

import com.dxn.dto.AiUnfilledPersonnelListDTO;
import com.dxn.dto.InvestmentDTO;
import com.dxn.model.entity.InvestmentDeclarationEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.dto.workFlow.MyTodoListDTO;
import com.dxn.system.dto.workFlow.WorkFlowNode;
import com.dxn.system.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Ai_InvestmentDeclaration")
public class InvestmentDeclaration extends InvestmentDeclarationEntity {


    //验证方法
    @Override
    public void onValidate() throws Exception {
        //业务代码
        super.onValidate();
        this.preservationCheck();
        //this.addingCheck();
        if (Framework.isNullOrEmpty(this.getDocState())) {
            this.setDocState(10);
        }
    }

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        this.setDeptId(this.getUserId().getDept());
        //业务代码
    }


    //流程结束给人员修改字段
    @Override
    public void onEndWorkflow(WorkFlowNode node) throws Exception {
        Long staffId = this.getUserId().getStaffId();
        if (Framework.isNullOrEmpty(staffId)) throw new BusinessException("审批用户没有找到对应的员工信息");
        Optional<Staff> staffFirstl = Staff.stream().where(w -> w.getId() == staffId).findFirst();
        if (staffFirstl.isPresent()) {
            Staff staff = staffFirstl.get();
            staff.setIsDeclare(10);
            repository().add(staff);
        }
    }

    @Override
    public void onInserted() throws Exception {
        super.onInserted();
        this.investmentDeclaration();
    }

    //提交时给字段赋值
    @Override
    public void onSubmitWorkflow(WorkFlowNode node) throws Exception {
        Long userId = this.getId();
        List<InvestmentSituation> investmentSituationList = InvestmentSituation.stream().where(w -> w.getInvestmentDeclarationId().getId() == userId).toList();
        for (InvestmentSituation investmentSituation : investmentSituationList) {
            String handOverAccount = investmentSituation.getHandOverAccount();
            if (Framework.isNullOrEmpty(handOverAccount)) investmentSituation.setHandOverAccount("无");
            String deepAccount = investmentSituation.getDeepAccount();
            if (Framework.isNullOrEmpty(deepAccount)) investmentSituation.setDeepAccount("无");
            String nationalAccounts = investmentSituation.getNationalAccounts();
            if (Framework.isNullOrEmpty(nationalAccounts)) investmentSituation.setNationalAccounts("无");
            String securitiesTrading = investmentSituation.getSecuritiesTrading();
            if (Framework.isNullOrEmpty(securitiesTrading)) investmentSituation.setSecuritiesTrading("无");
            String securitiesName = investmentSituation.getSecuritiesName();
            if (Framework.isNullOrEmpty(securitiesName)) investmentSituation.setSecuritiesName("无");
            String securitiesCode = investmentSituation.getSecuritiesCode();
            if (Framework.isNullOrEmpty(securitiesCode)) investmentSituation.setSecuritiesCode("无");
            String investmentSituation11 = investmentSituation.getInvestmentSituation();
            if (Framework.isNullOrEmpty(investmentSituation11)) investmentSituation.setInvestmentSituation("无");
            String customerList = investmentSituation.getCustomerList();
            if (Framework.isNullOrEmpty(customerList)) investmentSituation.setCustomerList("无");
            String remarks = investmentSituation.getRemarks();
            if (Framework.isNullOrEmpty(remarks)) investmentSituation.setRemarks("无");

        }
    }

    @SystemService(args = "userId")
    public List<InvestmentDTO> GetCustomerByCustomerId(Long userId) throws Exception {
        //获取当前用户Id
        //setUser(AppContext.current().getProfile().getId());
        List<InvestmentDTO> investmentDTOList = new ArrayList<>();
        InvestmentDTO investmentDTO = new InvestmentDTO();
        if (userId != 2) {
            Optional<User> ugroupList = User.stream().where(w -> w.getId() == userId).findFirst();
            if (ugroupList.isPresent()) {
                User ugroup = ugroupList.get();
                Long staffId = ugroup.getStaffId();//333767
                Optional<Staff> staffList = Staff.stream().where(w -> w.getId() == staffId).findFirst();
                if (staffList.isPresent()) {
                    Staff staff = staffList.get();
                    investmentDTO.setDeclarant(staff.getName());
                    Integer iSCPA = staff.getISCPA();
                    if (Framework.isNullOrEmpty(iSCPA)) {
                        iSCPA = 20;
                    }
                    investmentDTO.setIsCPA(iSCPA);
                    StaffPosition staffPositionId = staff.getStaffPositionId();
                    //                investmentDTO.setPosition(staff.getStaffPositionId().getName());
                    if (Framework.isNotNullOrEmpty(staffPositionId)) {
                        investmentDTO.setPosition(staffPositionId.getName());
                    }
                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
                    String year = dateFormat.format(date);
                    String a = year.substring(0, 4);
                    int i = Integer.parseInt(a) - 1;
                    String s = String.valueOf(i);
                    investmentDTO.setYear("截止到" + s + "年12月31日");
//                String  annualValue=a+"-12-31";
//                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                LocalDateTime ldt = LocalDateTime.parse(annualValue,dateTimeFormatter);
//                investmentDTO.setAnnualValue(ldt);
                    Integer isPartner = staff.getWAPartner();
                    if (Framework.isNullOrEmpty(isPartner)) {
                        isPartner = 20;
                    }
                    investmentDTO.setIsPartner(isPartner);
                    investmentDTO.setUserId(userId);
                    investmentDTOList.add(investmentDTO);
                }
            }
        } else {
            investmentDTO.setDeclarant("admin");
            investmentDTO.setIsCPA(10);
            investmentDTO.setIsPartner(10);
            investmentDTO.setPosition("系统管理员");
            investmentDTO.setUserId(userId);
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
            String year = dateFormat.format(date);
            String a = year.substring(0, 4);
            int i = Integer.parseInt(a) - 1;
            String s = String.valueOf(i);
            investmentDTO.setYear("截止到" + s + "年12月31日");
            investmentDTOList.add(investmentDTO);
        }
        return investmentDTOList;
    }


    @SystemService(args = "userId")
    public String BringOutYourID(Long userId) throws Exception {
        String aa = null;

        return aa;
    }

    //投资申报添加
    private void investmentDeclaration() throws Exception {
        Long staffId = this.getUserId().getStaffId();
        Optional<Staff> staffFind = Staff.stream().where(w -> w.getId() == staffId).findFirst();
        if (staffFind.isPresent()) {
            Staff staff = staffFind.get();
            staff.initialization();
            staff.setIsDeclare(10);
            repository().add(staff);
        }
    }


    @SystemService(args = "userId")
    public void DeclarationOfIndependence(Long userId) throws Exception {
        Optional<User> userFind = User.stream().where(w -> w.getId() == userId).findFirst();
        if (userFind.isPresent()) {
            User user = userFind.get();
            Long staffId = user.getStaffId();
            if (Framework.isNullOrEmpty(staffId)) throw new BusinessException("超级管理员无法点击确定");
            Optional<Staff> staffFind = Staff.stream().where(w -> w.getId() == staffId).findFirst();
            if (staffFind.isPresent()) {
                Staff staff = staffFind.get();
                staff.initialization();
                staff.setIsIndependence(10);
                repository().add(staff);
            }
        }
    }

    private void addingCheck() throws Exception {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        String year = dateFormat.format(date);
        String a = "截止到" + year.substring(0, 4) + "年12月31日";
        Long userId = this.getUserId().getId();
        Long id = this.getId();
        Long number = 0L;
        if (Framework.isNullOrEmpty(id)) {
            number = this.stream().where(w -> w.getUserId().getId().equals(userId) && w.getYear().equals(a)).count();
        } else {
            number = this.stream().where(w -> w.getUserId().getId().equals(userId) && !w.getId().equals(id) && w.getYear().equals(a)).count();
        }
        // if (number > 0) throw new BusinessException("用户添加不能重复");
    }

    @JsonIgnore
    public MyTodoListDTO gainWorkFlowDTO(MyTodoListDTO value) throws Exception {
        value.setDepartmentType(this.getUserId().getName());
        User user = this.getCreatedBy();
        if (Framework.isNotNullOrEmpty(user)) {
            if (Framework.isNotNullOrEmpty(user.getDeptReadOnly())) {
                value.setDepartment(user.getDeptReadOnly().getTreeName());
            }
        }
        String processId = value.getProcess_Id();
        Optional<WorkFlowConfig> workFlowConfiglist = WorkFlowConfig.stream().where(w -> w.getProcessId().equals(processId)).findFirst();
        if (workFlowConfiglist.isPresent()) {
            String deptIdName = user.getDeptReadOnly().getOrgReadOnly().getNickname();
            WorkFlowConfig workFlowConfig = workFlowConfiglist.get();
            value.setWorkFlowName(deptIdName + "-" + workFlowConfig.getWorkFlowTypeId().getName());
        }
        return value;
    }

    private void preservationCheck() throws Exception {
        List<InvestmentSituation> investmentSituationList = this.getInvestmentSituations();
        int conunt = 0;
        for (InvestmentSituation investmentSituation : investmentSituationList) {
            Integer relationsWithApplicants = investmentSituation.getRelationsWithApplicants();
            if (relationsWithApplicants == 10) {
                conunt++;
            }
        }
        if (conunt < 1) {
            throw new BusinessException("请填写本人的投资情况");
        }
//        if (conunt > 1) {
//            throw new BusinessException("投资情况表中不能有多个本人");
//        }
    }

    @SystemService(args = "query_year")
    public List<AiUnfilledPersonnelListDTO> gainAiUnfilledPersonnelListDTO(String year) throws Exception {
        List<AiUnfilledPersonnelListDTO> aiUnfilledPersonnelListDTOList = new ArrayList<>();
        List<Staff> staffList = Staff.stream().where(w -> w.getIsDeclare().equals(20) || w.getIsIndependence().equals(20)).toList();
        for (Staff staff : staffList) {
            AiUnfilledPersonnelListDTO aiUnfilledPersonnelListDTO = new AiUnfilledPersonnelListDTO();
            Long departmentId = staff.getDeptId();
            Optional<Department> dpartmentFirst = Department.stream().where(w -> w.getId().equals(departmentId)).findFirst();
            if (Framework.isNotNullOrEmpty(dpartmentFirst)) {
                aiUnfilledPersonnelListDTO.setDepartment(dpartmentFirst.get().getTreeName());
            }
            aiUnfilledPersonnelListDTO.setNameOfPractitioner(staff.getName());
            StaffPosition staffPosition = staff.getStaffPositionId();
            if (Framework.isNotNullOrEmpty(staffPosition)) {
                aiUnfilledPersonnelListDTO.setPosition(staffPosition.getName());
            }
            Long staffId = staff.getId();
            if (staff.getIsDeclare() == 10) {
                aiUnfilledPersonnelListDTO.setConfirmationOfPeriodicDeclarationsOfIndependence("是");
            } else {
                aiUnfilledPersonnelListDTO.setConfirmationOfPeriodicDeclarationsOfIndependence("否");
            }

            Long count = InvestmentDeclaration.stream().where(w -> w.getUserId().getStaffId().equals(staffId) && w.getYear().equals(year)).count();
            if (count > 0) {
                aiUnfilledPersonnelListDTO.setWhetherToFillInInvestmentDeclaration("是");
            } else {
                aiUnfilledPersonnelListDTO.setWhetherToFillInInvestmentDeclaration("否");
            }
            aiUnfilledPersonnelListDTO.setYear(year);
            aiUnfilledPersonnelListDTOList.add(aiUnfilledPersonnelListDTO);
        }
        return aiUnfilledPersonnelListDTOList;
    }


}
