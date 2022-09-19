/*===================================================================================
 *                    Copyright(c) 2020 POSCO ICT
 *
 * Project            : workcenter-app
 * Source File Name   : com.aworks.workcenter.service.schedule.quartz.QuartzScheduler.java
 * Description        :
 * Author             : ddurung
 * Version            : 1.0.0
 * File Name related  :
 * Class Name related :
 * Created Date       : 2019. 12. 30.
 * Updated Date       : 2019. 12. 30.
 * Last modifier      : ddurung
 * Updated content    : 최초작성
 *
 *==================================================================================*/
package com.msa.scheduler.quartz.component;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.substring;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.repeatHourlyForever;
import static org.quartz.SimpleScheduleBuilder.repeatMinutelyForever;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.service.spi.ServiceException;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.OperableTrigger;
import org.springframework.stereotype.Component;

import com.msa.scheduler.quartz.entity.Schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * QuartzScheduler.java
 *
 * @author ddurung
 * @version 1.0.0
 * @since 2019. 12. 30.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QuartzScheduler {

    private final Scheduler scheduler;

    /**
     * Start the schedule
     *
     * @param schedule the Schedule
     */
    public void start( Schedule schedule ) {
        try {
            this.scheduleJob( schedule );
        } catch ( SchedulerException ex ) {
            log.error( "Fail to start the schedule[" + schedule.getName() + "].", ex );
            throw new ServiceException( "Fail to start the schedule[" + schedule.getName() + "].", ex );
        }
    }

    /**
     * Stop the schedule
     *
     * @param schedule the Schedule
     */
    public void stop( Schedule schedule ) {
        try {
            this.unscheduleJob( schedule );
        } catch ( SchedulerException ex ) {
            log.error( "Fail to stop the schedule[" + schedule.getName() + "].", ex );
            throw new ServiceException( "Fail to stop the schedule[" + schedule.getName() + "].", ex );
        }
    }

    /**
     * Delete the schedule
     *
     * @param schedule the Schedule
     */
    public void delete( Schedule schedule ) {
        try {
            scheduler.deleteJob( this.getJobKey( schedule ) );
        } catch ( SchedulerException ex ) {
            log.error( "Fail to delete the schedule : " + schedule, ex );
        }
    }

    /**
     * Add the calendar
     *
     * @param calendarName the calendar name
     * @param calendar the Calendar
     */
    public void addCalendar( String calendarName, org.quartz.Calendar calendar ) {
        try {
            scheduler.addCalendar( calendarName, calendar, true, true );
        } catch ( SchedulerException ex ) {
            log.error( "Fail to add the calendar : " + calendarName, ex );
        }
    }

    /**
     * Delete the calendar
     *
     * @param calendarName the calendar name
     */
    public void deleteCalendar( String calendarName ) {
        try {
            scheduler.deleteCalendar( calendarName );
        } catch ( SchedulerException ex ) {
            log.error( "Fail to delete the calendar : " + calendarName, ex );
        }
    }

    /**
     * Return the calendar
     *
     * @param calendarName the calendar name
     * @return the Calendar
     */
    public org.quartz.Calendar getCalendar( String calendarName ) {
        try {
            return scheduler.getCalendar( calendarName );
        } catch ( SchedulerException ex ) {
            log.warn( "Fail to get calendar.", ex );
        }
        return null;
    }

    /**
     * Return the trigger list
     *
     * @param group the trigger group name
     * @return the list of trigger
     */
    public List<Trigger> getTriggers( String group ) {
        List<Trigger> triggers = new ArrayList<>();
        try {
            GroupMatcher<TriggerKey> matcher = null;
            if ( group != null ) {
                matcher = GroupMatcher.triggerGroupEquals( group );
            } else {
                matcher = GroupMatcher.anyTriggerGroup();
            }

            for ( TriggerKey triggerKey : scheduler.getTriggerKeys( matcher ) ) {
                TriggerState state = scheduler.getTriggerState( triggerKey );
                if ( state == TriggerState.NORMAL || state == TriggerState.BLOCKED ) {
                    triggers.add( scheduler.getTrigger( triggerKey ) );
                }
            }
        } catch ( SchedulerException ex ) {
            log.warn( "Fail to get trigger.", ex );
        }
        return triggers;
    }

    /**
     * Return the trigger
     *
     * @param schedule the schedule
     * @return the Trigger
     */
    public Trigger getTrigger( Schedule schedule ) {
        try {
            TriggerKey triggerKey = this.getTriggerKey( schedule );
            TriggerState state = scheduler.getTriggerState( triggerKey );
            if ( state == TriggerState.NORMAL || state == TriggerState.BLOCKED ) {
                return scheduler.getTrigger( triggerKey );
            }
        } catch ( SchedulerException ex ) {
            log.warn( "Fail to get trigger.", ex );
        }
        return null;
    }

    /**
     * Schedule the job
     *
     * @param schedule the Schedule
     * @throws SchedulerException
     */
    private void scheduleJob( Schedule schedule ) throws SchedulerException {
        /* @formatter:off */
        JobDetail jobDetail = JobBuilder.newJob( ScheduleWorkJob.class )
                                        .withIdentity( this.getJobKey( schedule ) )
                                        .storeDurably()
                                        .setJobData( this.getJobDataMap( schedule ) )
                                        .build();
        /* @formatter:on */

        LocalDateTime startDatetime = schedule.getStartDatetime();
        LocalDateTime endDateTime = schedule.getEndDatetime();
        Date startDate = startDatetime != null ? java.sql.Timestamp.valueOf( startDatetime ) : new Date();
        Date endDate = endDateTime != null ? java.sql.Timestamp.valueOf( endDateTime ) : null;

        /* @formatter:off */
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger()
                                                               .withIdentity( this.getTriggerKey( schedule ) )
                                                               .forJob( jobDetail )
                                                               .usingJobData( "scheduleJob", true )
                                                               .startAt( startDate )
                                                               .endAt( endDate )
                                                               .withDescription( substring( schedule.getDescription(), 0, 250 ) );
        /* @formatter:on */


        ScheduleBuilder<?> scheduleBuilder = null;
        scheduleBuilder = cronSchedule( schedule.getCronExpression() );

        if ( scheduleBuilder != null ) {
            // set misfireHandlingInstruction : all misfired executions are discarded.
            if ( scheduleBuilder instanceof SimpleScheduleBuilder ) {
                ( (SimpleScheduleBuilder) scheduleBuilder ).withMisfireHandlingInstructionNextWithRemainingCount();
            } else if ( scheduleBuilder instanceof CronScheduleBuilder ) {
                ( (CronScheduleBuilder) scheduleBuilder ).withMisfireHandlingInstructionDoNothing();
            }
            triggerBuilder.withSchedule( scheduleBuilder );
        }

        Trigger trigger = triggerBuilder.build();

        String scheduleName = schedule.getName();
        Date firstSchedule = this.getFirstSchedule( trigger );
        // check the first schedule time
        if ( firstSchedule != null ) {
            // update the job detail
            scheduler.addJob( jobDetail, true );
            // unschedule job
            scheduler.unscheduleJob( trigger.getKey() );
            // schedule job
            scheduler.scheduleJob( trigger );
        } else {
            throw new ServiceException( "The schedule[" + scheduleName + "] will never fire." );
        }

    }

    /**
     * Unschedule the job
     *
     * @param schedule the Schedule
     * @throws SchedulerException
     */
    private void unscheduleJob( Schedule schedule ) throws SchedulerException {
        /* @formatter:off */
        scheduler.unscheduleJobs( scheduler.getTriggersOfJob( this.getJobKey( schedule ) )
                                           .stream()
                                           .map( Trigger::getKey )
                                           .collect( toList() ) );
        /* @formatter:on */
    }

    /**
     * Return the JobKey with schedule
     *
     * @param schedule the Schedule
     * @return the JobKey
     */
    private JobKey getJobKey( Schedule schedule ) {
        return this.getJobKey( schedule);
    }

    /**
     * Return the TriggerKey with schedule
     *
     * @param schedule the Schedule
     * @return the TriggerKey
     */
    private TriggerKey getTriggerKey( Schedule schedule ) {
        return this.getTriggerKey( schedule.getId() );
    }

    /**
     * Return the TriggerKey with schedule id and organization id
     *
     * @param scheduleId the schedule id
     * @param organizationId the organization id
     * @return the TriggerKey
     */
    private TriggerKey getTriggerKey( UUID scheduleId ) {
        return TriggerKey.triggerKey( scheduleId.toString() );
    }

    /**
     * Return the first schedule time
     *
     * @param trigger the trigger
     * @param calendar the calendar
     * @return the first schedule time
     */
//    private Date getFirstSchedule( Trigger trigger, Calendar calendar ) {
    private Date getFirstSchedule( Trigger trigger ) {        
        org.quartz.Calendar cal = null;
        return ( (OperableTrigger) trigger ).computeFirstFireTime( cal );
    }

}
