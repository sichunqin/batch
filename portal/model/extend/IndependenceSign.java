package com.dxn.model.extend;

import com.dxn.model.entity.IndependenceSignEntity;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.configuration.DxnConfig;
import com.dxn.system.context.AppContext;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "AI_IndependenceSign")
public class IndependenceSign extends IndependenceSignEntity {
    public boolean gainIsIndependenceSign() throws Exception {
        if (!DxnConfig.getIsIndependence()) return true;
        if (AppContext.current().getProfile().getIsSuperAdmin()) return true;
        Long userId = AppContext.current().getProfile().getId();
        int year = LocalDateTime.now().getYear();
        Optional<IndependenceSign> first = this.stream().where(w -> w.getCreatedById() == userId && w.getYear() == year).findFirst();
        return first.isPresent();
    }

    @SystemService(args = "content")
    public void putIsIndependenceSign(String content) throws Exception {
        AppContext.current().getProfile().setIsIndependenceSign(true);
        if (gainIsIndependenceSign()) return;
        long userId = AppContext.current().getProfile().getId();
        int year = LocalDateTime.now().getYear();
        IndependenceSign independenceSign = this.createNew();
        independenceSign.setYear(year);
        independenceSign.setContent(content);
        independenceSign.setCreatedById(userId);
        repository().add(independenceSign);
    }
}
