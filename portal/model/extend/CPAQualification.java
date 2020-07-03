package com.dxn.model.extend;

import com.dxn.dto.CPAQualificationDTO;
import com.dxn.model.entity.CPAQualificationEntity;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "HR_StaffCPAQualification")
public class CPAQualification extends CPAQualificationEntity {

    @Override
    public void onInserting() throws Exception {
        super.onInserting();

    }


    @SystemService(args = "staffId")
    public CPAQualificationDTO getCPAQualificationDTO(Long staffId) throws Exception{
        CPAQualificationDTO cPAQualificationDTO=new CPAQualificationDTO();
        List<CPAQualification> cpalist = CPAQualification.stream().where(c -> c.getStaffId().getId()==staffId&& c.getAccountantType()==10).toList();
       for(CPAQualification cpaQualification :cpalist) {
           cPAQualificationDTO.setAccountantType(cpaQualification.getAccountantType());
           cPAQualificationDTO.setCode(cpaQualification.getCode());
           cPAQualificationDTO.setApprovalDate(cpaQualification.getApprovalDate());
           cPAQualificationDTO.setWayOfPassing(cpaQualification.getWayOfPassing());
           cPAQualificationDTO.setSPTime(cpaQualification.getSPTime());
           cPAQualificationDTO.setLocationOfNoterRelations(cpaQualification.getLocationOfNoterRelations());
           cPAQualificationDTO.setIsRelationshipTransferOffice(cpaQualification.getIsRelationshipTransferOffice());
           cPAQualificationDTO.setTransferName(cpaQualification.getTransferName());
           cPAQualificationDTO.setTransferoutName(cpaQualification.getTransferoutName());
           cPAQualificationDTO.setIsMember(cpaQualification.getIsMember());
           cPAQualificationDTO.setIsSecurities(cpaQualification.getIsSecurities());
           cPAQualificationDTO.setIsArmyHead(cpaQualification.getIsArmyHead());
           List<Staff> stafflist = Staff.stream().where(c -> c.getId().equals(staffId)).toList();
           Staff staff = stafflist.get(0);
           cPAQualificationDTO.setPartnershipTypes(staff.getPartnershipType());
           cPAQualificationDTO.setPartnerLevels(staff.getPartnerLevel());
           cPAQualificationDTO.setPromotionOfPartnersTimes(staff.getPromotionOfPartnersTime());
           cPAQualificationDTO.setContributionAmounts(staff.getContributionAmount());
           cPAQualificationDTO.setWithdrawalDates(staff.getWithdrawalDate());
       }
        return cPAQualificationDTO;
    }

}
