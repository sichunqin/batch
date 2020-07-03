package com.dxn.model.extend;

import com.dxn.model.entity.MainOptionEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

@Entity
@Table(name = "Base_MainOption")
public class MainOption extends MainOptionEntity {
    @JsonIgnore
    public static Integer gainOrgSealType(Long userId) throws Exception {
        Optional<MainOptionUser> first = MainOptionUser.stream().where(f -> f.getUserId().getId() == userId).findFirst();
        if (first.isPresent()) return 10;
        return 20;
    }
}
