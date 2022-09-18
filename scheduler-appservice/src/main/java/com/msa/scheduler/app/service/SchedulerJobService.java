package com.msa.scheduler.app.service;

//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.msa.scheduler.app.entity.SchedulerJobInfo;
import com.msa.scheduler.app.repository.SchedulerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class SchedulerJobService {
	
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@Autowired
	private SchedulerRepository schedulerRepository;
	
	public List<SchedulerJobInfo> getAllJobList() {
		return schedulerRepository.findAll();
	}

	/*
	public boolean deleteJob(SchedulerJobInfo jobInfo) {
		try {
			SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
			schedulerRepository.delete(getJobInfo);
			log.info(">>>> jobName = [" + jobInfo.getJobName() + "]" + "deleted. ");
			return schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
		} catch(SchedulerException e) {
			log.error("Failed to delete job - {}", jobInfo.getJobName(), e);
			return false;
		}
	}
	*/
}
