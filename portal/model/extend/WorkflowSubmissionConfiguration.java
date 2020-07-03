package com.dxn.model.extend;

import com.dxn.model.entity.WorkflowSubmissionConfigurationEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.context.AppContext;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Base_WorkflowSubmissionConfiguration")
public class WorkflowSubmissionConfiguration extends WorkflowSubmissionConfigurationEntity {

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        String key = String.format("gainAuthorityConfigForWorkFlow");
        AppHelper.manage().getCacheUtil().clear(key);
    }

    @SystemService
    public boolean getAuthorityConfigExtract() throws Exception {
        Long userId = AppContext.current().getProfile().getId();
        return gainAuthorityConfig().stream().anyMatch(a -> a.equals(userId));
    }

    private List<Long> gainAuthorityConfig() throws Exception {
        String key = String.format("gainAuthorityConfigForWorkFlow");
        List<Long> users = AppHelper.manage().getCacheUtil().getList(key, () -> {
            try {
                return this.stream().select(s -> s.getUserId().getId()).toList();
            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }
        }, Long.class, CacheGroup.WorkFlow);

        return users;
    }

    //新增用户用
    @SystemService(args = "userList")
    public void garnUserWork(List<Long> userList) throws Exception {
        for (Long id : userList) {
            Optional<WorkflowSubmissionConfiguration> workflowSubmissionConfigurationFirst=WorkflowSubmissionConfiguration.stream().where(c->c.getUserId().getId()==id).findFirst();
            if (workflowSubmissionConfigurationFirst.isPresent()) continue;
            WorkflowSubmissionConfiguration workflowSubmissionConfiguration = WorkflowSubmissionConfiguration.createNew();
            List<User> list = User.stream().where(c -> c.getId() == id).toList();
            workflowSubmissionConfiguration.setUserId(list.get(0));
            repository().add(workflowSubmissionConfiguration);
            repository().commit();
        }
    }
}
