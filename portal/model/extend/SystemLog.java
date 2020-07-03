package com.dxn.model.extend;

import com.dxn.model.entity.SystemLogEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Sys_SystemLog")
public class SystemLog extends SystemLogEntity {

}
