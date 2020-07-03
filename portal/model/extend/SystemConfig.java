package com.dxn.model.extend;

import com.dxn.model.entity.SystemConfigEntity;
import com.dxn.system.ComputerHelper;
import com.dxn.system.ModelHelper;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Sys_SystemConfig")
public class SystemConfig extends SystemConfigEntity {
    @SystemService
    public String version() throws Exception {
        return ComputerHelper.getComputerKey();
    }

    @SystemService
    public String testExpression() throws Exception {

        User user = User.findById(2L);
        String code = User.findById(2L).getCode().toString().substring(0, 1);
        Object code1 = ModelHelper.runJavaExpression(user, "Enum(Address:Id_toString_substring(0,1))");

        if (code.equalsIgnoreCase(code1.toString())) {

        }


        StringBuilder str = new StringBuilder();

//        str.append(this.repository().any("User", "Name=='Admin' and ( Name=='111' or Code LikeIn ('a,d,min'))"));
//        str.append("\r\n");
//        str.append(this.repository().any("User", "Code=='admin'"));
//        str.append("\r\n");
//        str.append(this.repository().any("User", "Code=='admin' && name=='超级管理员'"));
//        str.append("\r\n");
//        str.append(this.repository().any("User", "Code like 'admin'"));
//        str.append("\r\n");
//        str.append(this.repository().any("User", "Code!='admin'"));
//        str.append("\r\n");
//        str.append(this.repository().any("User", "Status>10"));
//        str.append("\r\n");
//        str.append(this.repository().any("User", "Status>=10"));
//        str.append("\r\n");
//        str.append(this.repository().any("User", "Status<10"));
//        str.append("\r\n");
//        str.append(this.repository().any("User", "Status<=10"));
//        str.append("\r\n");
//        str.append(this.repository().any("User", "Status in (10,20)"));
//        str.append("\r\n");
//        str.append(this.repository().any("User", "Status notIn (10,20)"));
//        str.append("\r\n");
//        str.append(this.repository().any("User", "Status Between (10,90)"));
//        str.append("\r\n");
//        str.append(this.repository().any("User", "Status==10 || Code=='admin' || Name=='111'"));
//        str.append("\r\n");
//        str.append(this.repository().any("User", "Status==10 && Code=='admin' || Name=='111'"));
//        str.append("\r\n");
//        str.append(this.repository().any("User", "Status==10 && Code=='admin' || Name=='111' || Email=='222'"));
//        str.append("\r\n");
//        str.append(this.repository().any("User", "Status==10 && (Code=='admin' || Name=='111')"));
//        str.append("\r\n");
//        str.append(this.repository().any("Project", "RiskLevel!=40 && (DeptId_Name like '大信' or DeptId_Name like '大信1')"));
//        str.append("\r\n");
//        str.append(this.repository().any("EntityMap", "EntityMapItems_Name like '主键'"));
//        str.append("\r\n");
//        str.append(this.repository().any("Project", "DeptId_Id==#AppContext.Profile.DeptId#"));
//        str.append("\r\n");
//        str.append(this.repository().any("Project", "DeptId in #AppContext.Profile.DataAuthority#"));
//        str.append("\r\n");
//        str.append(this.repository().any("Report", "projectId_customerNameId_undertakesId_Name like '中国' && projectId_customerNameId_undertakesId_Name like '中国1' || projectId_customerNameId_Name like '中国'"));
//        str.append("\r\n");
//        str.append(this.repository().any("Report", "createdOn>#AppContext.Date# || createdOn==#AppContext.DateTime# || applySignStatus==#AppContext.Year#"));
//        str.append("\r\n");

        return str.toString();
    }
}
