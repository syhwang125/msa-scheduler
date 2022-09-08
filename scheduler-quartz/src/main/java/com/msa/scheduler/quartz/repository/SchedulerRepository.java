package com.msa.scheduler.quartz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.msa.scheduler.quartz.entity.SchedulerJobInfo;

@Repository
public interface SchedulerRepository extends JpaRepository<SchedulerJobInfo, Long> {

	public List<SchedulerJobInfo> findAll();

	public 	SchedulerJobInfo findByJobName(String jobName);

}
