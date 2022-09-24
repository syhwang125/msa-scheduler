package com.msa.scheduler.app.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="SCHEDULER_JOB_INFO")
public class SchedulerJobInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long jobId;
	private String jobName;
	private String jobGroup;
	private String jobStatus;
<<<<<<< HEAD
	private String cron_Expression;
	private String description;
=======
	private String jobClass;
	private String cronExpression;
	private String description;
//	private String interfaceName;
//	private String repeatTime;
//	private Boolean cronJob;
>>>>>>> 815c33c2ce07774a19e3522e335963ea990d1f42

}
