package com.msa.scheduler.quartz.entity;

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
@Table(name="schedulerJobInfo")
public class SchedulerJobInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long jobId;
	private String jobName;
	private String jobGroup;
	private String jobStatus;
	private String jobClass;
	private String cronExpression;
	private String desc;
	private String interfaceName;
	private Long repeatTime;
	private Boolean cronJob;
}
