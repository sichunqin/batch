package com.dxn.model.extend;

import com.dxn.forms.dto.TreeDTO;
import com.dxn.forms.dto.UIAuthorityDTO;
import com.dxn.forms.dto.UIAuthorityTypeEnum;
import com.dxn.model.entity.StaffPositionEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.exception.BusinessException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;
import java.util.stream.Stream;

@Entity
@Table(name = "HR_StaffPosition")
public class StaffPosition extends StaffPositionEntity {
    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();

        this.setSort();

//需求改变了，目前只需要手动添加，不需要进行自动填充数据了。（2020-03-03）
//        List<Long> menuIds = new ArrayList<>();
//        List<UIAuthorityDTO> authorityList = RoleAuthority.createNew().getAuthorityConfig();
//        for (StaffPositionRole item : this.getStaffPositionRoles()) {
//            Stream<UIAuthorityDTO> list = authorityList.stream().filter(f -> f.getRoleId().equals(item.getRole().getId()) && f.getType().equals(UIAuthorityTypeEnum.Menu));
//            list.forEach(f -> {
//                menuIds.add(f.getDataId());
//            });
//        }
//
//        List<StaffPositionLink> delList = new ArrayList<>();
//
//        //清理
//        for (StaffPositionLink item : this.getStaffPositionLinks()) {
//            boolean isHave = menuIds.stream().anyMatch(f -> f.equals(item.getMenuId().getId()));
//            if (!isHave) {
//                delList.add(item);
//            } else {
//                Long count = this.getStaffPositionLinks().stream().filter(f -> f.getMenuId().getId().equals(item.getMenuId().getId())).count();
//                if (count > 1) {
//                    delList.add(item);
//                    menuIds.add(item.getMenuId().getId());
//                }
//            }
//        }
//
//        for (StaffPositionLink item : delList) {
//            item.initialization();
//            this.getStaffPositionLinks().remove(item);
//            this.repository().remove(item);
//        }
//
//        //新增和修改
//        for (Long item : menuIds) {
//            boolean isHave = this.getStaffPositionLinks().stream().anyMatch(f -> f.getMenuId().getId().equals(item));
//            if (!isHave) {
//                StaffPositionLink link = StaffPositionLink.createNew();
//                link.setMenu(item);
//                link.setStaffPositionId(this);
//                link.setIsQuickLink(false);
//                this.getStaffPositionLinks().add(link);
//                repository().add(link);
//            }
//        }
    }

    @Override
    public void onValidate() throws Exception {
        super.onValidate();
        Optional<StaffPosition> staffPosition;
        String code = this.getCode();
        Long id = this.getId();
        if (Framework.isNullOrEmpty(id)) {
            staffPosition = StaffPosition.stream().where(c -> c.getCode() == code).findFirst();
        } else {
            staffPosition = StaffPosition.stream().where(c -> c.getCode() == code && c.getId() != id).findFirst();
        }

        if (staffPosition.isPresent()) {
            throw new BusinessException("职级编码重复");
        }
    }

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Authority);
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Schema);
    }

    public List<TreeDTO> gainAuthorityMenus() {
        List<TreeDTO> list = new ArrayList<>();
        this.getStaffPositionLinks().forEach(f -> {
            Menu menu = f.getMenuId();
            TreeDTO dto = new TreeDTO();
            dto.setId(menu.getId().toString());
            dto.setSort(f.getSort());
            dto.setText(Framework.isNotNullOrEmpty(menu.getLinkName()) ? menu.getLinkName() : menu.getName());
            list.add(dto);
        });
        return list;
    }

    //插入后
    @Override
    public void onInserting() throws Exception {
        super.onInserting();
        employeeProcessDepartmentRankTableADD();
    }

//    @Override
//    public void onUpdating() throws Exception {
//        super.onUpdating();
//        employeeProcessDepartmentRankTableADD();
//        //业务代码
//    }


    //职级增加给部门职级表加数据
    private void employeeProcessDepartmentRankTableADD() throws Exception {
        List<Department> departments = Department.stream().where(c -> c.getUnitAttributes() == 20).toList();
        List<CustomStaffFlowTable> customStaffFlowTables = CustomStaffFlowTable.stream().toList();
        for (Department item : departments) {
            EmployeeProcessDepartmentRankTable employeeProcessDepartmentRankTableEntity = new EmployeeProcessDepartmentRankTable();
            employeeProcessDepartmentRankTableEntity.setDeptId(item);
            employeeProcessDepartmentRankTableEntity.setStaffPositionId(this);
            repository().add(employeeProcessDepartmentRankTableEntity);
        }
    }

    private void setSort() throws Exception {

        Integer sort = 0;

        //排序
        Collections.sort(this.getStaffPositionLinks(), Comparator.comparing(o -> o.getSort()));

        for (StaffPositionLink item : this.getStaffPositionLinks()) {
            item.setSort(sort);
            sort++;
        }
    }
}
