package com.dxn.model.extend;

import com.dxn.dto.AuditClientDTO;
import com.dxn.model.entity.AuditCustomerListEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "Ai_AuditCustomerList")
public class AuditCustomerList extends AuditCustomerListEntity {
    @Override
    public void onUpdating() throws Exception {
        super.onUpdating();
        //业务代码
        LocalDateTime confirmationTime = LocalDateTime.now();
    }

    @SystemService(args = "auditCustomerListId")
    public void undoButton(Long auditCustomerListId) throws Exception {

        List<SignatureInformationList> signatureInformationList = SignatureInformationList.stream().where(w -> w.getAuditCustomerListId().getId() == auditCustomerListId).toList();
        for (SignatureInformationList signatureInformation : signatureInformationList) {
            if (signatureInformation.getConfirmationStatus()==20){
                signatureInformation.setConfirmationTime(null);
                signatureInformation.setConfirmationStatus(10);
                repository().add(signatureInformation);
            }
        }
    }

    @SystemService(args = "auditCustomerListId")
    public void ConfirmationOfModifications(Long auditCustomerListId) throws Exception {
        List<SignatureInformationList> signatureInformationList = SignatureInformationList.stream().where(w -> w.getAuditCustomerListId().getId() == auditCustomerListId).toList();
        for (SignatureInformationList signatureInformation : signatureInformationList) {
            if (signatureInformation.getConfirmationStatus()==10){
                LocalDateTime confirmationTime = LocalDateTime.now();
                signatureInformation.setConfirmationTime(confirmationTime);
                signatureInformation.setConfirmationStatus(20);
                repository().add(signatureInformation);
            }
        }
    }

    @SystemService(args = "query_confirmationStatus,query_year")
    public List<AuditClientDTO> getAuditClientDTO(Integer confirmationStatus, Integer year) throws Exception {
        List<AuditClientDTO> auditClientDTOList = new ArrayList<>();
        List<Long> longList = new ArrayList<>();
        List<SignatureInformationList> signatureInformationList1 = SignatureInformationList.stream().where(w -> w.getConfirmationStatus() == confirmationStatus && w.getYear().equals(year)).toList();
        for (SignatureInformationList signatureInformation : signatureInformationList1) {
            longList.add(signatureInformation.getAuditCustomerListId().getId());
        }
        Set set2 = new HashSet();
        List<Long> newList3 = new ArrayList();
        set2.addAll(longList);
        newList3.addAll(set2);
        for (Long longACId : newList3) {
            Optional<AuditCustomerList> auditCustomerList = AuditCustomerList.stream().where(w -> w.getId() == longACId).findFirst();
            if (auditCustomerList.isPresent()) {
                AuditCustomerList auditCustomer = auditCustomerList.get();
                AuditClientDTO auditClientDTO = new AuditClientDTO();
                Long id = auditCustomer.getId();
                Long count = SignatureInformationList.stream().where(w -> w.getAuditCustomerListId().getId() == id && w.getYear().equals(year)).count();
                if (count > 0) {
                    auditClientDTO.setId(id);
//                    LocalDateTime auditStart = auditCustomer.getCompositionCustomerPage().getAuditStart();
//                    LocalDateTime auditEnd = auditCustomer.getCompositionCustomerPage().getAuditEnd();
//                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");// HH:mm:ss
//                    String auditStartString = df.format(auditStart);
//                    String auditEndString = df.format(auditEnd);
//                    String accountingPeriod = auditStartString + "至" + auditEndString;
//                    auditClientDTO.setAccountingPeriod(accountingPeriod);
                    auditClientDTO.setCompositionCustomerPageId(auditCustomer.getCompositionCustomerPage().getId());
                    auditClientDTO.setCustomerCode(auditCustomer.getCustomer().getCode());
                    auditClientDTO.setAuditReportDate(this.getAuditReportDate());
                    auditClientDTO.setCustomerName(auditCustomer.getCompositionCustomerPage().getName());
                    auditClientDTO.setCustomerType(auditCustomer.getCompositionCustomerPage().getCustomerTypeValue());
                    auditClientDTO.setSecuritiesCode(auditCustomer.getCompositionCustomerPage().getProject().getCustomerNameId().getSecuritiesCode());
                    auditClientDTO.setSecuritiesVariety(auditCustomer.getSecuritiesVariety());
                    Long deptId = auditCustomer.getCustomer().getUndertakesId().getDeptId();
                    Optional<Department> departmentFirst = Department.stream().where(w -> w.getId() == deptId).findFirst();
                    if (departmentFirst.isPresent()) {
                        Department department = departmentFirst.get();
                        String departmentName = department.getParentId().getName();
                        if (department.getUnitAttributes()!=30){
                            departmentName=department.getName();
                        }

                        auditClientDTO.setUndertakingUnitId(departmentName);
                    }
                    auditClientDTO.setTimeToMarket(auditCustomer.getCompositionCustomerPage().getProject().getCustomerNameId().getTimeMarket());
                    List<SignatureInformationList> signatureInformationList = SignatureInformationList.stream().where(w -> w.getAuditCustomerListId().getId() == id).toList();
                    auditClientDTO.setYear(year);
                    for (SignatureInformationList item : signatureInformationList) {
                        Integer year11 = item.getYear();
                            if (year11.equals(year)) {
                                auditClientDTO.setConfirmationTime(item.getConfirmationTime());//确认时间
                                auditClientDTO.settYear(item.getAuditPeriod());
                                auditClientDTO.settSigningPartner(Framework.isNullOrEmpty(item.getSigningPartnerId())?"":item.getSigningPartnerId().getName());
                                auditClientDTO.settSignatureCPA(Framework.isNullOrEmpty(item.getSigningAccountantId())?"":item.getSigningAccountantId().getName());
                                User user = item.getSigningAccountantOtherId();
                                if (Framework.isNotNullOrEmpty(user)) {
                                    auditClientDTO.settSignatureCPA1(user.getName());
                                }
                            }
                            if (year11.equals(year - 1)) {
                                auditClientDTO.setT1Year(item.getAuditPeriod());
                                auditClientDTO.setT1SigningPartner(Framework.isNullOrEmpty(item.getSigningPartnerId())?"":item.getSigningPartnerId().getName());
                                auditClientDTO.setT1SignatureCPA(Framework.isNullOrEmpty(item.getSigningAccountantId())?"":item.getSigningAccountantId().getName());
                                User user = item.getSigningAccountantOtherId();
                                if (Framework.isNotNullOrEmpty(user)) {
                                    auditClientDTO.setT1SignatureCPA1(user.getName());
                                }
                            }
                            if (year11.equals(year - 2)) {
                                auditClientDTO.setT2Year(item.getAuditPeriod());
                                auditClientDTO.setT2SigningPartner(Framework.isNullOrEmpty(item.getSigningPartnerId())?"":item.getSigningPartnerId().getName());
                                auditClientDTO.setT2SignatureCPA(Framework.isNullOrEmpty(item.getSigningAccountantId())?"":item.getSigningAccountantId().getName());
                                User user = item.getSigningAccountantOtherId();
                                if (Framework.isNotNullOrEmpty(user)) {
                                    auditClientDTO.setT2SignatureCPA1(user.getName());
                                }
                            }
                            if (year11.equals(year - 3)) {
                                auditClientDTO.setT3Year(item.getAuditPeriod());
                                auditClientDTO.setT3SigningPartner(Framework.isNullOrEmpty(item.getSigningPartnerId())?"":item.getSigningPartnerId().getName());
                                auditClientDTO.setT3SignatureCPA(Framework.isNullOrEmpty(item.getSigningAccountantId())?"":item.getSigningAccountantId().getName());
                                User user = item.getSigningAccountantOtherId();
                                if (Framework.isNotNullOrEmpty(user)) {
                                    auditClientDTO.setT3SignatureCPA1(user.getName());
                                }
                            }
                            if (year11.equals(year - 4)) {
                                auditClientDTO.setT4Year(item.getAuditPeriod());
                                auditClientDTO.setT4SigningPartner(Framework.isNullOrEmpty(item.getSigningPartnerId())?"":item.getSigningPartnerId().getName());
                                auditClientDTO.setT4SignatureCPA(Framework.isNullOrEmpty(item.getSigningAccountantId())?"":item.getSigningAccountantId().getName());
                                User user = item.getSigningAccountantOtherId();
                                if (Framework.isNotNullOrEmpty(user)) {
                                    auditClientDTO.setT4SignatureCPA1(user.getName());
                                }
                            }
                            if (year11.equals(year - 5)) {
                                auditClientDTO.setT5Year(item.getAuditPeriod());
                                auditClientDTO.setT5SigningPartner(Framework.isNullOrEmpty(item.getSigningPartnerId())?"":item.getSigningPartnerId().getName());

                                auditClientDTO.setT5SignatureCPA(Framework.isNullOrEmpty(item.getSigningAccountantId())?"":item.getSigningAccountantId().getName());
                                User user = item.getSigningAccountantOtherId();
                                if (Framework.isNotNullOrEmpty(user)) {
                                    auditClientDTO.setT5SignatureCPA1(user.getName());
                                }
                                break;
                            }
                        }
                    }
                    auditClientDTOList.add(auditClientDTO);
                }
            }
        return auditClientDTOList;
    }

//    @SystemService(args = "query_dateType")
//    public List<AuditClientDTO> getAuditClientDateType() throws Exception {
//        List<AuditClientDTO> auditClientDTOList = new ArrayList<>();
//        Date date = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
//        String year1 = dateFormat.format(date);
//        Integer year = Integer.parseInt(year1);
//        List<AuditCustomerList> auditCustomerList = AuditCustomerList.stream().toList();
//        for (AuditCustomerList auditCustomer : auditCustomerList) {
//            Long id = auditCustomer.getId();
//            Long count = SignatureInformationList.stream().where(w -> w.getAuditCustomerListId().getId() == id).count();
//            if (count < 1) {
//                continue;
//            }
//            AuditClientDTO auditClientDTO = new AuditClientDTO();
//            auditClientDTO.setId(auditCustomer.getId());
//            LocalDateTime auditStart = auditCustomer.getCompositionCustomerPage().getAuditStart();
//            LocalDateTime auditEnd = auditCustomer.getCompositionCustomerPage().getAuditEnd();
//            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");// HH:mm:ss
//            String auditStartString = df.format(auditStart);
//            String auditEndString = df.format(auditEnd);
//            String accountingPeriod = auditStartString + "至" + auditEndString;
//            auditClientDTO.setAccountingPeriod(accountingPeriod);
//            auditClientDTO.setCompositionCustomerPageId(auditCustomer.getCompositionCustomerPage().getId());
//            auditClientDTO.setCustomerCode(auditCustomer.getCustomer().getCode());
//            auditClientDTO.setCustomerName(auditCustomer.getCompositionCustomerPage().getName());
//            auditClientDTO.setCustomerType(auditCustomer.getCompositionCustomerPage().getCustomerTypeValue());
//            auditClientDTO.setSecuritiesCode(auditCustomer.getCompositionCustomerPage().getProject().getCustomerNameId().getSecuritiesCode());
//            auditClientDTO.setSecuritiesVariety(auditCustomer.getSecuritiesVariety());
//            auditClientDTO.setDataType(auditCustomer.getDataType());
//            Long deptId = auditCustomer.getCustomer().getUndertakesId().getDeptId();
//            Optional<Department> departmentFirst = Department.stream().where(w -> w.getId() == deptId).findFirst();
//            if (departmentFirst.isPresent()) {
//                Department department = departmentFirst.get();
//                String departmentName = department.getTreeName();
//                auditClientDTO.setUndertakingUnitId(departmentName);
//            }
//            auditClientDTO.setTimeToMarket(auditCustomer.getCompositionCustomerPage().getProject().getCustomerNameId().getTimeMarket());
//            Long auditCustomerId = auditCustomer.getId();
//            List<SignatureInformationList> signatureInformationList = SignatureInformationList.stream().where(w -> w.getAuditCustomerListId().getId() == auditCustomerId).toList();
//            auditClientDTO.setYear(year);
//            for (SignatureInformationList item : signatureInformationList) {
//                Integer year11 = item.getYear();
//                Long reportId = item.getReportId();
//                Optional<Report> reportFirst = Report.stream().where(w -> w.getId() == reportId).findFirst();
//                if (reportFirst.isPresent()) {
//                    Report report = reportFirst.get();
//                    if (year11.equals(year)) {
//                        auditClientDTO.setConfirmationTime(item.getConfirmationTime());//确认时间
//                        auditClientDTO.setAuditReportDate(report.getAuditReportDate());
//                        auditClientDTO.settYear(item.getAuditPeriod());
//                        auditClientDTO.settSigningPartner(report.getSigningPartner().getName());
//                        auditClientDTO.settSignatureCPA(report.getSigningAccountant().getName());
//                        User user = report.getSigningAccountantOther();
//                        if (Framework.isNotNullOrEmpty(user)) {
//                            auditClientDTO.settSignatureCPA1(user.getName());
//                        }
//                    }
//                    if (year11.equals(year - 1)) {
//                        auditClientDTO.setT1Year(item.getAuditPeriod());
//                        auditClientDTO.setT1SigningPartner(report.getSigningPartner().getName());
//                        auditClientDTO.setT1SignatureCPA(report.getSigningAccountant().getName());
//                        User user = report.getSigningAccountantOther();
//                        if (Framework.isNotNullOrEmpty(user)) {
//                            auditClientDTO.setT1SignatureCPA1(user.getName());
//                        }
//                    }
//                    if (year11.equals(year - 2)) {
//                        auditClientDTO.setT2Year(item.getAuditPeriod());
//                        auditClientDTO.setT2SigningPartner(report.getSigningPartner().getName());
//                        auditClientDTO.setT2SignatureCPA(report.getSigningAccountant().getName());
//                        User user = report.getSigningAccountantOther();
//                        if (Framework.isNotNullOrEmpty(user)) {
//                            auditClientDTO.setT2SignatureCPA1(user.getName());
//                        }
//                    }
//                    if (year11.equals(year - 3)) {
//                        auditClientDTO.setT3Year(item.getAuditPeriod());
//                        auditClientDTO.setT3SigningPartner(report.getSigningPartner().getName());
//                        auditClientDTO.setT3SignatureCPA(report.getSigningAccountant().getName());
//                        User user = report.getSigningAccountantOther();
//                        if (Framework.isNotNullOrEmpty(user)) {
//                            auditClientDTO.setT3SignatureCPA1(user.getName());
//                        }
//                    }
//                    if (year11.equals(year - 4)) {
//                        auditClientDTO.setT4Year(item.getAuditPeriod());
//                        auditClientDTO.setT4SigningPartner(report.getSigningPartner().getName());
//                        auditClientDTO.setT4SignatureCPA(report.getSigningAccountant().getName());
//                        User user = report.getSigningAccountantOther();
//                        if (Framework.isNotNullOrEmpty(user)) {
//                            auditClientDTO.setT4SignatureCPA1(user.getName());
//                        }
//                    }
//                    if (year11.equals(year - 5)) {
//                        auditClientDTO.setT5Year(item.getAuditPeriod());
//                        auditClientDTO.setT5SigningPartner(report.getSigningPartner().getName());
//                        auditClientDTO.setT5SignatureCPA(report.getSigningAccountant().getName());
//                        User user = report.getSigningAccountantOther();
//                        if (Framework.isNotNullOrEmpty(user)) {
//                            auditClientDTO.setT5SignatureCPA1(user.getName());
//                        }
//                        break;
//                    }
//                }
//            }
//            auditClientDTOList.add(auditClientDTO);
//        }
//        return auditClientDTOList;
//    }


}
