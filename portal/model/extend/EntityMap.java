package com.dxn.model.extend;

import com.dxn.dto.PropertyBoolDTO;
import com.dxn.dto.PropertyGridDTO;
import com.dxn.forms.dto.TreeDTO;
import com.dxn.forms.dto.UIPagedList;
import com.dxn.forms.dto.UISchemaDTO;
import com.dxn.model.entity.EntityMapEntity;
import com.dxn.repository.IRepository;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.configuration.DxnConfig;
import com.dxn.system.entityCode.EntityCodeConfig;
import com.dxn.system.entityCode.EntityCodeConfigColumn;
import com.dxn.system.entityCode.EntityCodeHelper;
import com.dxn.system.exception.BusinessException;
import com.dxn.system.exception.DxnException;
import com.dxn.system.interfaces.IEntityMapService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jinq.jpa.JPAJinqStream;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Entity
@Table(name = "Base_EntityMap")
public class EntityMap extends EntityMapEntity implements IEntityMapService {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();

        if (Framework.isNotNullOrEmpty(this.getCode())) this.setCode(this.getCode().trim());
        if (Framework.isNotNullOrEmpty(this.getName())) this.setName(this.getName().trim());
        if (Framework.isNotNullOrEmpty(this.getTableName())) this.setTableName(this.getTableName().trim());
        if (Framework.isNotNullOrEmpty(this.getTableName())) this.setIsBaseType(false);
        if (Framework.isNullOrEmpty(this.getNamespaceId())) this.setNamespace(100);

        this.createBaseChildren();
        this.setSort();
        this.setQuoteNamespace();
    }

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Entity);
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Schema);
    }

    @Override
    public void onValidate() throws Exception {
        super.onValidate();

        this.validateCode();
        this.validateTableName();
    }

    @Override
    public void onDeleted() throws Exception {
        super.onDeleted();
        this.deleteEntity();
    }

    private void validateCode() throws Exception {

        if (this.getCode().equalsIgnoreCase("New") || Framework.isNullOrEmpty(this.getTableName())) return;

        String code = this.getCode();
        List<EntityMap> list = this.stream().where(w -> w.getCode() == code).toList();
        if (list.size() <= 0) return;

        EntityMap first = list.get(0);
        if (Framework.isNullOrEmpty(first)) return;
        if (first.getId().equals(this.getId())) return;

        throw new BusinessException(String.format("编码 %s 不能重复!", this.getCode()));
    }

    private void validateTableName() throws Exception {

        if (Framework.isNullOrEmpty(this.getTableName())) return;

        String tableName = this.getTableName();
        List<EntityMap> list = this.stream().where(w -> w.getTableName() == tableName).toList();
        if (list.size() <= 0) return;

        EntityMap first = list.get(0);
        if (Framework.isNullOrEmpty(first)) return;
        if (first.getId().equals(this.getId())) return;

        throw new BusinessException(String.format("表名 %s 不能重复!", this.getTableName()));
    }

    private void setQuoteNamespace() throws Exception {
        if (Framework.isNullOrEmpty(this.getNamespaceId())) return;
        Stream<EntityMap> list = this.stream().filter(f -> f.getMainEntityMapId() != null && f.getMainEntityMapId().getId() == this.getId());
        IRepository repository = this.repository();
        list.forEach(f -> {
            f.initialization();
            f.setNamespaceId(this.getNamespaceId());
            repository.add(f);
        });
    }

    private void setSort() throws Exception {

        Integer sort = 0;

        //排序
        Collections.sort(this.getEntityMapItems(), Comparator.comparing(o -> o.getSort()));

        for (EntityMapItem item : this.getEntityMapItems()) {
            item.setSort(sort);
            sort++;
        }
    }

    private void createBaseChildren() throws Exception {

        if (Framework.isNullOrEmpty(this.getTableName())) return;

        createMapItems("Id", "主键", 16, "Long", false, -100, false);
        createMapItems("Timestamp", "时间戳", 8, "String", true, -99, false);

        createMapItems("CreatedById", "创建人", 16, "User", false, 1000, false, false);
        createMapItems("ModifiedById", "修改人", 16, "User", false, 1002, false, false);
        createMapItems("CreatedOn", "创建时间", 23, "LocalDateTime", false, 1001, false);
        createMapItems("ModifiedOn", "修改时间", 23, "LocalDateTime", false, 1003, false);

        if (this.getIsTree()) {
            createMapItems("ParentId", "上级", 16, this.getCode(), true, 994, false);
            createMapItems("TreeCode", "树编码", -1, "String", true, 995, true);
            createMapItems("TreeName", "树名称", -1, "String", true, 996, true);
            createMapItems("Sort", "排序", 10, "Integer", false, 997, true);
            createMapItems("Leaf", "叶子节点", 1, "Boolean", false, 998, true);
            createMapItems("Level", "级次", 10, "Integer", false, 999, true);
        } else {
            deleteMapItems("ParentId");
            deleteMapItems("TreeCode");
            deleteMapItems("TreeName");
            //deleteMapItems("Sort"); 排序就不自动删除了，普通列表也会有排序，如果需要删除，手动删除。
            deleteMapItems("Leaf");
            deleteMapItems("Level");
        }

        if (this.getIsWorkflow()) {
            createMapItems("DocStateAll", "并行状态", 1000, "String", true, 889, true, true, "DocStatusEnum");
            createMapItems("DocState", "运营状态", 16, "Integer", true, 990, true, true, "DocStatusEnum");
            createMapItems("TaskName", "节点名称", 1000, "String", true, 991, true);
            createMapItems("SubmitById", "发起人", 16, "User", true, 993, false, false);
            createMapItems("ApprovedById", "审批人", 16, "User", true, 994, false, false);
            createMapItems("SubmitOn", "发起时间", 23, "LocalDateTime", true, 995, false);
            createMapItems("ApprovedOn", "审批时间", 23, "LocalDateTime", true, 996, false);
        } else {
            deleteMapItems("DocStateAll");
            deleteMapItems("DocState");
            deleteMapItems("TaskName");
        }

        //处理主子关系
        if (Framework.isNullOrEmpty(this.getMainEntityMapId())) {
            if (Framework.isNotNullOrEmpty(this.getOriginalValue()) && Framework.isNotNullOrEmpty(this.getOriginalValue().getMainEntityMapId())) {
                EntityMap mainEntityMap = EntityMap.findById(this.getOriginalValue().getMainEntityMapId().getId());
                if (Framework.isNotNullOrEmpty(mainEntityMap) && Framework.isNotNullOrEmpty(mainEntityMap.getCode())) {
                    deleteMapItems(String.format("%sId", mainEntityMap.getCode()));
                }
            }
        } else {
            if (Framework.isNotNullOrEmpty(this.getMainEntityMapId()) && Framework.isNotNullOrEmpty(this.getMainEntityMapId().getCode()))
                createMapItems(String.format("%sId", this.getMainEntityMapId().getCode()), this.getMainEntityMapId().getName(), 16, this.getMainEntityMapId().getCode(), false, 990, false);
        }
    }

    private void createMapItems(String code, String name, int length, String type, boolean isNull, int sort, boolean isReadOnly) throws Exception {
        createMapItems(code, name, length, type, isNull, sort, isReadOnly, true);
    }

    private void createMapItems(String code, String name, int length, String type, boolean isNull, int sort, boolean isReadOnly, boolean isCorrelation) throws Exception {
        createMapItems(code, name, length, type, isNull, sort, isReadOnly, isCorrelation, null);
    }

    private void createMapItems(String code, String name, int length, String type, boolean isNull, int sort, boolean isReadOnly, boolean isCorrelation, String enumType) throws Exception {
        createMapItems(code, name, length, type, isNull, sort, isReadOnly, isCorrelation, enumType, null);
    }

    private void createMapItems(String code, String name, int length, String type, boolean isNull, int sort, boolean isReadOnly, boolean isCorrelation, String enumType, EntityMap defaultMap) throws Exception {

        Optional<EntityMapItem> itemFirst = this.getEntityMapItems().stream().filter(w -> w.getCode().equalsIgnoreCase(code)).findFirst();

        EntityMapItem first;

        if (itemFirst.isPresent()) {
            first = itemFirst.get();
        } else {
            first = EntityMapItem.createNew();
            first.setEntityMapId(this);
            first.setCode(code);
            first.setName(name);
            first.setLength(length);
            first.setDataTypeId(getDataType(code, type, defaultMap));
            first.setIsNull(isNull);
            first.setSort(sort);
            first.setIsReadOnly(isReadOnly);
            first.setIsTransient(false);
            first.setPrecision(0);
            first.setEnumType(enumType);
            first.setIsCorrelation(isCorrelation);

            this.getEntityMapItems().add(first);
        }

        first.synchroControl();

        if (Framework.isNotNullOrEmpty(enumType) && type.equalsIgnoreCase("Integer"))
            first.setControl("Combobox");
    }

    @JsonIgnore
    private EntityMap getDataType(String code, String type, EntityMap defaultMap) throws Exception {

        List<EntityMap> list = this.stream().where(w -> w.getCode() == type).toList();
        if (list.size() <= 0) {
            if (Framework.isNotNullOrEmpty(defaultMap)) return defaultMap;
            throw new BusinessException(String.format("系统中找不到 “%s” 对应的类型!", type));
        }

        return list.get(0);
    }

    private void deleteMapItems(String code) throws Exception {
        Optional<EntityMapItem> first = this.getEntityMapItems().stream().filter(w -> w.getCode().equalsIgnoreCase(code)).findFirst();
        if (!first.isPresent()) return;
        this.getEntityMapItems().remove(first.get());
        this.repository().remove(first.get());
    }

    @JsonIgnore
    @SystemService
    public String synchroFormPage() throws Exception {

        if (!this.getIsBaseType()) {
            FormPage formPage = FormPage.createNew();
            formPage.createFormPageFromEntityMap(this);
        }

        return "表单同步完成！";
    }

    @JsonIgnore
    @SystemService
    public String createEntityCodeAll() throws Exception {

        List<EntityMap> maps = this.stream().where(w -> w.getIsBaseType() == false).toList();

        for (EntityMap item : maps) {
            item.initialization();
            item.createEntityCode();
        }

        return "代码生成完成！";
    }

    @JsonIgnore
    @SystemService
    public String createEntityCode() throws Exception {

        if (this.getIsBaseType()) return "";
        if (Framework.isNullOrEmpty(this.getTableName())) return "";
        if (this.getCode().equalsIgnoreCase("New")) return "";

        EntityCodeConfig config = this.toEntityCodeConfig();
        EntityCodeHelper helper = new EntityCodeHelper(DxnConfig.getProjectModelPath());

        helper.createCode(config);
        //helper.InsertFileProj(config);
        //helper.CreateDataBase(config);

        config = this.toEntityCodeConfig(true);

        //生成历史表
        if (this.getIsCreateHistory()) {
            helper.createCode(config);
        } else {
            helper.deleteCode(config);
        }

        return "代码生成完成";
    }

    private void deleteEntity() throws Exception {
        if (Framework.isNullOrEmpty(this.getTableName())) return;

        EntityCodeConfig config = this.toEntityCodeConfig();
        EntityCodeHelper helper = new EntityCodeHelper(DxnConfig.getProjectModelPath());

        helper.deleteCode(config);

        config = this.toEntityCodeConfig(true);

        helper.deleteCode(config);

//        helper.DeleteFileProj(config);
//        helper.DeleteDataBase(config);
    }

    @JsonIgnore
    public EntityCodeConfig toEntityCodeConfig(Boolean... isHistory) throws Exception {
        EntityCodeConfig config = new EntityCodeConfig();

        //复制属性
        ModelHelper.copyModel(this, config);

        config.setCode(this.getCode());
        config.setName(this.getName());
        config.setTableName(this.getTableName());
        config.setIsTree(this.getIsTree());
        config.setIsWorkflow(this.getIsWorkflow());
        config.setNamespace(this.getNamespaceId().getCode());

        //this.SetMainEntityMap();

        if (this.getMainEntityMap() != null) {
            config.setMainCode(this.getMainEntityMap().getCode());
        }

        //排序
        Collections.sort(this.getEntityMapItems(), Comparator.comparing(o -> o.getSort()));

        for (EntityMapItem item : this.getEntityMapItems()) {
            config.getColumn().add(item.toEntityCodeConfigColumn());
        }

        boolean history = false;
        if (isHistory.length > 0) history = isHistory[0];

        if (!history) {
            Long mainId = this.getId();
            List<EntityMap> children = this.stream().where(w -> w.getMainEntityMapId().getId() == mainId).toList();

            //排序
            Collections.sort(children, Comparator.comparing(o -> o.getCode()));

            for (EntityMapEntity item : children) {
                if (!item.getCode().equalsIgnoreCase("New")) {
                    config.getColumn().add(((EntityMap) item).toEntityCodeConfigColumn());
                }
            }
        } else {
            //处理历史
            config.setCode(String.format("%sHistory", this.getCode()));
            config.setTableName(String.format("%sHistory", this.getTableName()));

            EntityCodeConfigColumn column = new EntityCodeConfigColumn();
            column.setCode(this.getCode());
            column.setName(this.getName());
            column.setDisplayName(this.getName());
            column.setDescription(this.getDescription());
            column.setType(this.getCode());
            column.setIsRelative(true);
            config.getColumn().add(column);
        }

        return config;
    }

    @JsonIgnore
    public EntityCodeConfigColumn toEntityCodeConfigColumn() throws Exception {

        EntityCodeConfigColumn column = new EntityCodeConfigColumn();

        //复制属性
        ModelHelper.copyModel(this, column);

        column.setCode(this.getCode());
        column.setName(this.getName());
        column.setDisplayName(this.getName());
        column.setDescription(this.getDescription());
        column.setType(this.getCode());
        column.setChildren(true);
        column.setIsTree(this.getIsTree());

        return column;
    }

    @JsonIgnore
    public UISchemaDTO findEntityMapItemSchema(String code) throws Exception {
        Optional<EntityMapItem> item = this.getEntityMapItems().stream().filter(f -> f.getCode().equalsIgnoreCase(code)).findFirst();
        if (item.isPresent()) return item.get().toUISchemaColumnDTO();
        return new UISchemaDTO();
    }

    @JsonIgnore
    @SystemService
    public Object getEntityTreeJson() throws Exception {
        return this.toTreeDTO();
    }

    private TreeDTO toTreeDTO() throws Exception {
        TreeDTO tree = new TreeDTO();
        tree.setId(this.getId().toString());
        tree.setText(this.getName());
        tree.setCode(this.getCode());

        Long mainId = this.getId();
        List<EntityMap> children = this.stream().where(w -> w.getMainEntityMapId().getId() == mainId).toList();

        for (EntityMap item : children) {
            item.initialization();
            TreeDTO dto = item.toTreeDTO();
            dto.setParentId(tree.getId());
            tree.getChildren().add(item.toTreeDTO());
        }

        return tree;
    }

    @JsonIgnore
    @SystemService
    public Object getEntityPropertyValue() throws Exception {
        UIPagedList page = new UIPagedList();

        EntityMap map = this;
        if (Framework.isNullOrEmpty(map)) throw new BusinessException("未找到要打开的实体！");

        page.addRow(PropertyGridDTO.Parse("id", "id", map.getId(), "基础", "text"));
        page.addRow(PropertyGridDTO.Parse("timestamp", "timestamp", map.getTimestamp(), "基础", "text"));
        page.addRow(PropertyGridDTO.Parse("code", "编码", map.getCode(), "基础", "text"));
        page.addRow(PropertyGridDTO.Parse("name", "名称", map.getName(), "基础", "text"));
        page.addRow(PropertyGridDTO.Parse("tableName", "表名", map.getTableName(), "扩展", "text"));
        page.addRow(PropertyGridDTO.Parse("isTree", "是否是树", map.getIsTree(), "扩展", new PropertyBoolDTO()));
        page.addRow(PropertyGridDTO.Parse("isWorkflow", "是否走工作流", map.getIsWorkflow(), "扩展", new PropertyBoolDTO()));
        page.addRow(PropertyGridDTO.Parse("description", "描述", map.getDescription(), "扩展", "text"));
        //总数
        page.setTotal(page.getRows().size());

        return page;
    }

    @Override
    public EntityCodeConfig gainEntityConfig(String entityCode) throws Exception {
        String key = String.format("gainEntityConfig_%s", entityCode);
        EntityCodeConfig entityCodeConfig = AppHelper.manage().getCacheUtil().get(key, () -> {
            try {
                Boolean isHistory = false;
                String newCode;

                if (entityCode.endsWith("History")) {
                    isHistory = true;
                    newCode = entityCode.substring(0, entityCode.length() - 7);
                } else {
                    newCode = entityCode;
                }

                EntityCodeConfig config = null;
                Optional<EntityMap> map = this.stream().where(w -> w.getCode() == newCode).findFirst();
                if (map.isPresent()) {
                    EntityMap entityMap = map.get();
                    entityMap.initialization();
                    config = entityMap.toEntityCodeConfig(isHistory);
                }
                return config;
            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }
        }, EntityCodeConfig.class, CacheGroup.Entity);

        if (Framework.isNullOrEmpty(entityCodeConfig))
            throw new BusinessException(String.format("找不到 %s 实体配置!", entityCode));

        return entityCodeConfig;
    }

    private List<EntityCodeConfig> gainEntityCodeConfigAll() throws Exception {
        String key = String.format("gainEntityCodeConfigAll");
        List<EntityCodeConfig> list = AppHelper.manage().getCacheUtil().getList(key, () -> {
            return null;
        }, EntityCodeConfig.class, CacheGroup.Entity);

        return list;
    }

    @SystemService
    public String copyEntityMap() throws Exception {
        if (this.getIsBaseType()) return "基础类型不能复制!";
        EntityMap page = this.toCopy();
        this.repository().add(page);
        return "复制完成！";
    }

    public EntityMap toCopy() throws Exception {

        EntityMap map = super.toCopy();
        map.setCode(map.getCode() + "1");
        map.setName(map.getName() + "复制");
        map.setTableName(map.getTableName() + "1");

        for (EntityMapItem item : this.getEntityMapItems()) {
            EntityMapItem newItem = item.toCopy();
            newItem.setEntityMapId(map);
            newItem.setDataTypeId(item.getDataTypeId());
            map.getEntityMapItems().add(newItem);
        }

        return map;
    }

    @Override
    public String gainUsingMoveSql(String entityType, Long id, Long newId) throws Exception {
        Optional<EntityMap> map = EntityMap.stream().where(w -> w.getCode() == entityType).findFirst();
        if (!map.isPresent()) throw new Exception(String.format("未找到实体 %s !", entityType));
        return map.get().gainUsingMoveSql(id.toString(), newId);
    }

    private String gainUsingMoveSql(String condition, Long newId) throws Exception {
        StringBuilder sql = new StringBuilder();
        String entityType = this.getCode();
        JPAJinqStream<EntityMapItem> mapItems = EntityMapItem.stream().where(w -> w.getDataTypeId().getCode() == entityType);
        mapItems.forEach(f -> {
            try {
                EntityMap map = f.getEntityMapId();
                String where = map.toEntityUsingCondition(f.getCode(), condition);
                if (isHaveEntityData(where)) {
                    sql.append(map.toMoveUsingEntitySql(f.getCode(), newId, where));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return sql.toString();
    }

    @Override
    public String gainUsingDeleteSql(String entityType, Long id) throws Exception {
        Optional<EntityMap> map = EntityMap.stream().where(w -> w.getCode() == entityType).findFirst();
        if (!map.isPresent()) throw new Exception(String.format("未找到实体 %s !", entityType));
        return map.get().gainUsingDeleteSql(id.toString());
    }

    private String gainUsingDeleteSql(String condition) throws Exception {
        StringBuilder sql = new StringBuilder();
        String entityType = this.getCode();
        JPAJinqStream<EntityMapItem> mapItems = EntityMapItem.stream().where(w -> w.getDataTypeId().getCode() == entityType);
        mapItems.forEach(f -> {
            try {
                if (!f.getEntityMap().getCode().equalsIgnoreCase(entityType)) {
                    EntityMap map = f.getEntityMapId();
                    String where = map.toEntityUsingCondition(f.getCode(), condition);
                    if (isHaveEntityData(where)) {
                        if (f.getIsCorrelation()) {
                            sql.append(map.gainUsingDeleteSql(where));
                            sql.append(map.toDeleteUsingEntitySql(where));
                        } else {
                            if (f.getIsNull()) {
                                sql.append(map.toClearUsingEntitySql(f.getCode(), where));
                            } else {
                                throw new DxnException(String.format("非强关联数据清理失败，编码 %s !", map.getCode()));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return sql.toString();
    }

    private String toEntityUsingCondition(String column, String condition) {
        return String.format("Select id from [%s] where %s in (%s)", this.getTableName(), column, condition);
    }

    private String toDeleteUsingEntitySql(String condition) {
        return String.format("Delete [%s] where id in (%s);", this.getTableName(), condition);
    }

    private String toMoveUsingEntitySql(String column, Long newId, String condition) {
        return String.format("Update [%s] set %s=%s where id in (%s);", this.getTableName(), column, newId, condition);
    }

    private String toClearUsingEntitySql(String column, String condition) {
        return String.format("Update [%s] set %s=null where id in (%s);", this.getTableName(), column, condition);
    }

    private boolean isHaveEntityData(String condition) throws Exception {
        Integer count = repository().sqlQueryFirst(String.format("Select Count(*) from (%s) as c", condition), Integer.class);
        return count > 0;
    }
}
