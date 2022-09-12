package com.msa.scheduler.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.msa.scheduler.app.entity.SchedulerJobInfo;


@Repository
public interface SchedulerRepository extends JpaRepository<SchedulerJobInfo, Long> {
	SchedulerJobInfo findByJobName(String jobName);
}

