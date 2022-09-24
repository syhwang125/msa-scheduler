package com.msa.scheduler.app.service;

import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;

import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.ParseException;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.msa.scheduler.app.entity.SchedulerJobInfo;
import com.msa.scheduler.app.repository.SchedulerRepository;

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
	private ApplicationContext context;
	
	public SchedulerMetaData getMetaData() throws SchedulerException {
	    SchedulerMetaData metaData = scheduler.getMetaData();
	    return metaData;
	}
	
	public List<SchedulerJobInfo> getAllJobList() {
		return schedulerRepository.findAll();
	}

    public boolean startJob( SchedulerJobInfo jobInfo ) {
        try {
            SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName( jobInfo.getJobName() );
            getJobInfo.setJobStatus("STARTED");
            schedulerRepository.save( getJobInfo );
            schedulerFactoryBean.getScheduler().triggerJob( new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()) );
            log.info( " # jobName = [ " + jobInfo.getJobName() + " ] " + " scheduled and started." );
            return true;
        } catch (SchedulerException e) {
            log.error( "Failed to start new job - {}", jobInfo.getJobName() , e);
            return false;
        }
    }

    public void saveOrUpdate( SchedulerJobInfo jobInfo ) throws Exception {
        log.info( "# jobInfo - {} ", jobInfo.getJobName()  );
        log.info( "# jobInfo - {} ", jobInfo.getJobGroup()  );
        if(jobInfo.getCron_Expression().length() > 0 ) {
            jobInfo.setCron_Expression( jobInfo.getCron_Expression() );
            jobInfo.setJobName( jobInfo.getJobName() );
            jobInfo.setJobGroup( jobInfo.getJobGroup() );
            jobInfo.setDescription( jobInfo.getDescription() );
        } 
        if(StringUtils.isEmpty( jobInfo.getJobId() )) {
            insertJob(jobInfo);
        } else {
            updateJob(jobInfo);
        }
    }

    private void updateJob( SchedulerJobInfo jobInfo ) {
        Trigger newTrigger =  createCronTrigger(jobInfo.getJobName(), new Date(),
                jobInfo.getCron_Expression(),  SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        
        try {
            schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName()), newTrigger);
            jobInfo.setJobStatus( " RESCHEDULED " );
            schedulerRepository.save( jobInfo );
            log.info( " # jobName = [ " + jobInfo.getJobName() + " ] " + " updated. " );
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        
    }
    
    public CronTrigger createCronTrigger(String triggerName, Date startTime, String cronExpression, int misFireInstruction) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setName(triggerName);
        factoryBean.setStartTime(startTime);
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setMisfireInstruction(misFireInstruction);
        try {
            factoryBean.afterPropertiesSet();
        } catch ( java.text.ParseException e ) {
            log.error(e.getMessage(), e);
        }
        return factoryBean.getObject();
    }
    

    private void insertJob( SchedulerJobInfo jobInfo ) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            
            /* @formatter:off */
            JobDetail jobDetail = JobBuilder.newJob( (Class<? extends QuartzJobBean>) Class.forName( "SimpleCronJob.class" ) ) 
                                            .withIdentity( jobInfo.getJobName(), jobInfo.getJobGroup()  )
                                            .storeDurably()
                                            .build();
            /* @formatter:on */

            /* @formatter:off */
            Trigger trigger = TriggerBuilder.newTrigger()
                                                  .forJob(  jobInfo.getJobName(), jobInfo.getJobGroup() )
                                                  .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup() )
                                                  .withDescription( "SimpleCronJob" )
                                                  .usingJobData( "SimpleCronJob", false )
                                                  .startNow()
                                                  .build();
            /* @formatter:on */
            scheduler.scheduleJob( jobDetail, trigger  );    
            jobInfo.setJobStatus( "SCHEDULED" );
            schedulerRepository.save( jobInfo );
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }


	public boolean deleteJob(SchedulerJobInfo jobInfo) {
		try {
			SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
			schedulerRepository.delete(getJobInfo);
			log.info("# jobName = [" + jobInfo.getJobName() + "]" + "deleted. ");
			return schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
		} catch(SchedulerException e) {
			log.error("Failed to delete job - {}", jobInfo.getJobName(), e);
			return false;
		}		
	}
	
	public boolean pauseJob(SchedulerJobInfo jobInfo) {
	    try {
	        SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName( jobInfo.getJobName() );
	        getJobInfo.setJobStatus( "PAUSED" );
	        schedulerRepository.save( getJobInfo );
	        schedulerFactoryBean.getScheduler().pauseJob( new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()) );
	        log.info( " # jobName = [ " + jobInfo.getJobName() + "]" + " paused." );
	        return true; 
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public boolean resumeJob(SchedulerJobInfo jobInfo) {
	    try {
	        SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName( jobInfo.getJobName() );
	        getJobInfo.setJobStatus( "RESUMED" );
	        schedulerRepository.save( getJobInfo );
	        schedulerFactoryBean.getScheduler().resumeJob( new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()) );
	        log.info( " # jobName =  [ " + jobInfo.getJobName() + " ] " + " resumed. " );
	        return true;
	    } catch ( SchedulerException e) {
	        e.printStackTrace();
	        return false;
	    }
	}


}
