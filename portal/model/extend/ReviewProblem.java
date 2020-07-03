package com.dxn.model.extend;

import com.dxn.model.entity.ReviewProblemEntity;
import com.dxn.system.dto.workFlow.MyTodoListDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Optional;

@Entity
@Table(name = "Prj_ReviewProblem")
public class ReviewProblem extends ReviewProblemEntity {

}
