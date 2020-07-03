package com.dxn.model.extend;

import com.dxn.dto.CommonWebDTO;
import com.dxn.forms.dto.TreeDTO;
import com.dxn.model.entity.CommonWebEntity;
import com.dxn.system.ResponseEntity;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.exception.BusinessException;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Sys_CommonWeb")
public class CommonWeb extends CommonWebEntity {

    @Override
    public void onValidate() throws Exception {
        super.onValidate();
        String name = this.getName();
        Long id=this.getId();
        Optional<CommonWeb> web = this.stream().where(w -> w.getName().equals(name) && w.getId() !=id).findFirst();
        if (web.isPresent()) throw new BusinessException("名称：" + name + " 重复！");
    }

    @SystemService()
    public ResponseEntity gainCommonWebList() throws Exception {
        ResponseEntity response = new ResponseEntity();
        try {
            List<CommonWebDTO> list = new ArrayList<>();
            List<CommonWeb> webs = this.stream().toList();
            if (webs.size() > 0) {
                for (CommonWeb web : webs) {
                    CommonWebDTO dto = new CommonWebDTO();
                    dto.setDisplayText(web.getName());
                    dto.setUrl(web.getHyperlink());
                    list.add(dto);
                }
                response.setCode(200);
                response.setData(list);
            } else {
                response.setCode(200);
                response.setMessages("常用网站没有初始化数据！");
            }
        } catch (Exception ex) {
            response.setCode(500);
            response.setMessages(ex.getMessage());
        }
        return response;
    }

    @SystemService()
    public List<TreeDTO> gainCommonWebDTO() throws Exception {
        List<TreeDTO> list = new ArrayList<>();
        List<CommonWeb> webs = this.stream().toList();
        if (webs.size() > 0) {
            for (CommonWeb web : webs) {
                TreeDTO dto = new TreeDTO();
                dto.setId(web.getId().toString());
                dto.setText(web.getName());
                dto.setIconName(web.getPictureName());
                dto.setUrl(web.getHyperlink());
                list.add(dto);
            }
        }
        return list;
    }
}
