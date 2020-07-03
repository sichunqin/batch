package com.dxn.model.extend;

import com.dxn.entity.EntityBase;
import com.dxn.model.entity.PrintPageEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.Print.PrintProperty;
import com.dxn.system.Print.PrintRow;
import com.dxn.system.Print.PrintTable;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.exception.BusinessException;
import com.dxn.system.interfaces.IPrintPageService;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "Sys_PrintPage")
public class PrintPage extends PrintPageEntity implements IPrintPageService {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        this.setSort();
    }

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Print);
    }

    private void setSort() throws Exception {

        Integer sort = 0;

        //排序
        this.getPrintPageChildrens().sort(Comparator.comparing(o -> o.getSort()));

        for (PrintPageChildren item : this.getPrintPageChildrens()) {
            item.setSort(sort);
            sort++;
        }
    }

    @Override
    public PrintTable gainPrintTableConfig(EntityBase entity) throws Exception {
        if (Framework.isNullOrEmpty(entity)) throw new BusinessException("实体不能为空!");
        String entityType = entity.gainTypeName();

        String key = String.format("gainPrintTableConfig-%s", entityType);

        PrintTable config = AppHelper.manage().getCacheUtil().get(key, () -> {
            try {
                PrintPage printPage = null;
                List<PrintPage> list = this.stream().where(w -> w.getEntityMapId().getCode() == entityType).toList();
                for (PrintPage item : list) {
                    if (Framework.isNullOrEmpty(item.getFilter())) {
                        printPage = item;
                    } else {
                        String exp = String.format("Id==%s && (%s)", item.getId(), item.getFilter().trim());
                        if (repository().any(item.gainTypeName(), exp)) printPage = item;
                    }
                    if (Framework.isNotNullOrEmpty(printPage)) break;
                }

                if (Framework.isNullOrEmpty(printPage))
                    throw new BusinessException(String.format("未找到实体 %s 打印配置！", entityType));

                return printPage.toPrintTable();

            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }
        }, PrintTable.class, CacheGroup.Print);

        Framework.showSystemMes();

        //加载数据
        config.loadData(entity);

        return config;

    }

    public PrintTable toPrintTable() {
        PrintTable dto = new PrintTable();

        this.getPrintPageChildrens().stream().filter(w -> w.getParentId() == null).forEach(f -> {
            PrintRow row = f.toPrintRow();
            if (Framework.isNotNullOrEmpty(row)) dto.getRows().add(row);
        });

        this.getPrintPagePropertys().forEach(f -> {
            PrintProperty property = f.toPrintProperty();
            if (Framework.isNotNullOrEmpty(property)) dto.getProperties().add(property);
        });

        return dto;
    }
}
