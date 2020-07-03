package com.dxn.model.extend;

import com.dxn.entity.EntityBase;
import com.dxn.model.entity.TaskDefinitionEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemTask;
import com.dxn.system.dto.ArgsModel;
import com.dxn.system.exception.DxnException;
import com.dxn.system.transaction.TransactionHelper;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Stream;

@Entity
@Table(name = "Base_TaskDefinition")
public class TaskDefinition extends TaskDefinitionEntity {

    public void runAction() throws Exception {
        Framework.log.info(String.format("执行任务：%s", this.getName()));
        ArgsModel argsModel = new ArgsModel();
        this.getTaskInterfaceParameterss().forEach(f -> {
            argsModel.getParaList().put(f.getName(), f.getValue());
        });
        TransactionHelper.newTransaction(() -> {
            try {
                TaskDefinition.taskRun(this.getCode(), argsModel);
                repository().commit();
                return null;
            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }
        });
    }

    public static void taskRun(String code, ArgsModel args) throws Exception {
        if (Framework.isNullOrEmpty(code)) throw new DxnException("任务逻辑代码不能为空!");
        String[] codeList = code.split("-");
        if (codeList.length != 2) throw new DxnException("任务逻辑代码不正确!");

        String type = codeList[0].trim();
        String action = codeList[1].trim();

        if (Framework.isNullOrEmpty(type)) throw new DxnException("任务逻辑代码不正确!");
        if (Framework.isNullOrEmpty(action)) throw new DxnException("任务逻辑代码不正确!");

        EntityBase entity = repository().createObjectByName(type);

        Class<?> listClass = entity.getClass();
        long count = Arrays.stream(listClass.getMethods()).filter(f -> f.getName().equalsIgnoreCase(action)).count();

        if (count <= 0) throw new DxnException("未找到相应服务！");
        if (count > 1) throw new DxnException("服务不支持重载！");

        Stream<Method> methods = Arrays.stream(listClass.getMethods()).filter(f -> f.getName().equalsIgnoreCase(action));
        Method method = methods.findFirst().get();

        //判断是否权限
        SystemTask annotation = method.getAnnotation(SystemTask.class);
        if (annotation == null) throw new DxnException("当前任务非系统任务！");

        Parameter[] parameter = method.getParameters();
        method.invoke(entity, args.toArray(parameter, annotation.args()));
    }
}
