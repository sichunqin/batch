package com.dxn.model.extend;

import com.dxn.dto.DraftReviewBatchDTO;
import com.dxn.model.entity.DraftReviewBatchItemEntity;
import com.dxn.system.annotation.SystemService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "QR_DraftReviewBatchItem")
public class DraftReviewBatchItem extends DraftReviewBatchItemEntity {


}
