package com.dxn.model.extend;

import com.alibaba.fastjson.JSONObject;
import com.dxn.dto.enums.Constants;
import com.dxn.model.entity.ReviewAllocationEntity;
import com.dxn.system.Framework;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.context.AppContext;
import com.dxn.system.dto.KeyValue;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "QR_ReviewAllocation")
public class ReviewAllocation extends ReviewAllocationEntity {
    //保存
    @JsonIgnore
    @SystemService(args = "reviewBatchId,users,manuscriptReviewState")
    public void saveReviewAllocation(Long reviewBatchId, String users, Integer manuscriptReviewState) throws Exception {
        if (Framework.isNullOrEmpty(users)) return;
        DraftReviewBatch batch = DraftReviewBatch.findById(reviewBatchId);
        Project p = batch.getProjectId();
        p.initialization();

        Optional<ReviewAllocation> has = this.stream().where(w -> w.getManuscriptReviewState() == manuscriptReviewState
                && w.getDraftReviewBatchId().getId() == reviewBatchId).findFirst();
        if (!has.isPresent()) {
            if (manuscriptReviewState == 50) {
                p.setReviewOpinionState50(Constants.AlreadyApplied);
            }
            if (manuscriptReviewState == 60) {
                p.setReviewOpinionState60(Constants.AlreadyApplied);
            }
        }


        if (users.equals("-1"))//如果-1 获取当前登录人
        {

            Long userid = AppContext.current().getProfile().getId();

            ReviewAllocation model = ReviewAllocation.createNew();
            model.setDraftReviewBatch(reviewBatchId);
            model.setManuscriptReviewState(manuscriptReviewState);
            model.setUser(userid);

            repository().add(model);
            return;
        }
        List<KeyValue> list = JSONObject.parseArray(users, KeyValue.class);

        for (KeyValue item : list) {
            Long used = Long.parseLong(item.getKey());
            Optional<ReviewAllocation> review = this.stream().where(r -> r.getDraftReviewBatchId().getId() == reviewBatchId && r.getUserId().getId() == used).findFirst();
            if (review.isPresent()) {
                if (item.getValue().equals("Delete")) {
                    repository().remove(review.get());
                }
            } else {
                if (item.getValue().equals("Add")) {
                    ReviewAllocation model = ReviewAllocation.createNew();
                    model.setDraftReviewBatch(reviewBatchId);
                    model.setManuscriptReviewState(manuscriptReviewState);
                    model.setUser(used);
                    repository().add(model);
                }
            }
        }
    }
}
