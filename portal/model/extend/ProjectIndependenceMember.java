package com.dxn.model.extend;

import com.dxn.model.entity.ProjectIndependenceMemberEntity;
import com.dxn.system.annotation.SystemService;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Prj_ProjectIndependenceMember")
public class ProjectIndependenceMember extends ProjectIndependenceMemberEntity {
    @SystemService(args = "projectId")
    public boolean hideProjectIndependenceMember(Long projectId) throws Exception {
        long count = this.stream().where(c -> c.getProjectId().getId() == projectId).count();
        return count < 1;
    }
}
