package com.msa.scheduler.quartz.component;

import static lombok.AccessLevel.PROTECTED;

import java.util.UUID;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.msa.scheduler.quartz.service.ScheduleService;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ScheduleWorkJob.java
 *
 * @author ddurung
 * @version 1.0.0
 * @since 2019. 12. 30.
 */
@Slf4j
@NoArgsConstructor( access = PROTECTED )
public class ScheduleWorkJob implements Job {

    @Autowired
    private ScheduleService service;

    /*
     * (non-Javadoc)
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute( JobExecutionContext context ) throws JobExecutionException {
        UUID scheduleId = (UUID) context.getMergedJobDataMap().get( "scheduleId" );
        String scheduleName = (String) context.getMergedJobDataMap().get( "schedulerName" );

        log.info( "Schedule[{}] was executed.", scheduleName );
    }

}
