package com.msa.scheduler.quartz.service;

import java.util.List;

import javax.transaction.Transactional;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.msa.scheduler.quartz.component.SchedulerJobCreatot;
import com.msa.scheduler.quartz.entity.SchedulerJobInfo;
import com.msa.scheduler.quartz.repository.SchedulerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class SchedulerJobService {

	@Autowired
	private Scheduler scheduler;
	
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@Autowired
	private SchedulerRepository schedulerRepository;
	
	@Autowired
	private SchedulerJobCreatot jobSchedulerCreator;
	
	@Autowired 
	private ApplicationContext applicationContext;
	
	
	public SchedulerMetaData getMetaData() throws SchedulerException {
		SchedulerMetaData metaData = scheduler.getMetaData();
		return metaData;
	}
	
	public List<SchedulerJobInfo> getAllJobList() {
		return schedulerRepository.findAll();
	}
	
	public boolean deleteJob(SchedulerJobInfo jobInfo) {
		try {
			SchedulerJobInfo deletejobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
			schedulerRepository.delete(deletejobInfo);
			log.info("### jobName : " + jobInfo.getJobName() + " deleted. "); 
			return schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
			
		} catch (SchedulerException e) {
			log.error("Failed to delete job - {}", jobInfo.getJobName(), e);
			return false;
		}
	}
	
	public boolean pauseJob(SchedulerJobInfo jobInfo) {
		try {
			SchedulerJobInfo pauseJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
			pauseJobInfo.setJobStatus("PAUSED");
			schedulerRepository.save(pauseJobInfo);
			schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
			log.info("### jobName : " + jobInfo.getJobName() + " paused. "); 
			return true;
		} catch (SchedulerException e) {
			log.error("Failed to pause job - {}", jobInfo.getJobName(), e);
			return false;
		}
	}

	public boolean resumeJob(SchedulerJobInfo jobInfo) {
		try {
			SchedulerJobInfo resumeJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
			resumeJobInfo.setJobStatus("RESUMED");
			schedulerRepository.save(resumeJobInfo);
			schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
			log.info("### jobName : " + jobInfo.getJobName() + " resumed. ");
			return true;
		} catch (SchedulerException e) {
			log.error("Failed to resume job - {} ", jobInfo.getJobName(), e);
			return false;
		}
	}
	
	public boolean startJobNow(SchedulerJobInfo jobInfo) {
		try {
			SchedulerJobInfo startJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
			startJobInfo.setJobStatus("STARTED");
			schedulerRepository.save(startJobInfo);
			schedulerFactoryBean.getScheduler().triggerJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
			log.info("### jobName : " + jobInfo.getJobName() + " started.");
			return true;
		} catch (SchedulerException e) {
			log.error(" Failed to start new job - {}", jobInfo.getJobName(), e);
			return false;
		}
	}
	
	
	
	
}
