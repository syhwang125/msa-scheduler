/*===================================================================================
 *                    Copyright(c) 2020 POSCO ICT
 *
 * Project            : workcenter-app
 * Source File Name   : com.aworks.workcenter.config.SchedulerConfiguration.java
 * Description        :
 * Author             : ddurung
 * Version            : 1.0.0
 * File Name related  :
 * Class Name related :
 * Created Date       : 2019. 9. 6.
 * Updated Date       : 2019. 9. 6.
 * Last modifier      : ddurung
 * Updated content    : 최초작성
 *
 *==================================================================================*/
package com.msa.scheduler.quartz.config;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import javax.sql.DataSource;

import org.quartz.spi.JobFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.msa.scheduler.quartz.config.props.ConfigProperties;
import com.msa.scheduler.quartz.config.props.ConfigProperties.Scheduler;
import com.msa.scheduler.quartz.config.props.ConfigProperties.Scheduler.JobStore;
import com.msa.scheduler.quartz.config.props.QuartzProperties;

import lombok.RequiredArgsConstructor;

/**
 * SchedulerConfiguration.java
 *
 * @author ddurung
 * @version 1.0.0
 * @since 2019. 9. 6.
 */
@Configuration
@RequiredArgsConstructor
public class SchedulerConfiguration {

    private final ConfigProperties configProps;

    /**
     * Return the job factory
     *
     * @param context the ApplicationContext
     * @return JobFactory
     */
    @Bean
    public JobFactory jobFactory( ApplicationContext context ) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext( context );
        return jobFactory;
    }

    /**
     * Return the scheduler factory bean
     *
     * @param jobFactory the JobFactory
     * @param dataSource the DataSource
     * @param schedulerListener the SchedulerListener
     * @param triggerListener the SchedulerListener
     * @return SchedulerFactoryBean
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean( JobFactory jobFactory, DataSource dataSource) { //, SchedulerListener schedulerListener, TriggerListener triggerListener ) {
        Scheduler scheduler = configProps.getScheduler();

        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setSchedulerName( scheduler.getName() );
        factory.setJobFactory( jobFactory );
        factory.setAutoStartup( scheduler.isAutoStartup() );
        factory.setDataSource( dataSource );
        factory.setOverwriteExistingJobs( scheduler.isOverwriteExistingJobs() );
        factory.setExposeSchedulerInRepository( scheduler.isExposeSchedulerInRepository() );
        factory.setQuartzProperties( this.quartzProperties() );
//        factory.setSchedulerListeners( schedulerListener );
//        factory.setGlobalTriggerListeners( triggerListener );

        return factory;
    }

    /**
     * Return the quartz properties
     *
     * @return QuartzProperties
     */
    @Bean
    public QuartzProperties quartzProperties() {
        Scheduler scheduler = configProps.getScheduler();
        JobStore jobStore = scheduler.getJobStore();

        QuartzProperties quartzProps = new QuartzProperties();
        quartzProps.setInstanceName( scheduler.getName() );
        if ( Boolean.parseBoolean( jobStore.getIsClustered() ) ) {
            quartzProps.setInstanceId( "AUTO" );
        }
        quartzProps.setJmxExport( String.valueOf( scheduler.isJmxExport() ) );
        quartzProps.setJobStoreGroup( "class", jobStore.getClassName() );
        quartzProps.setJobStoreGroup( "driverDelegateClass", jobStore.getDriverDelegateClass() );
        quartzProps.setJobStoreGroup( "useProperties", jobStore.getUseProperties() );
        quartzProps.setJobStoreGroup( "misfireThreshold", jobStore.getMisfireThreshold() );
        quartzProps.setJobStoreGroup( "tablePrefix", jobStore.getTablePrefix() );
        quartzProps.setJobStoreGroup( "isClustered", jobStore.getIsClustered() );
        quartzProps.setJobStoreGroup( "clusterCheckinInterval", jobStore.getClusterCheckinInterval() );

        String acquireTriggersWithinLock = jobStore.getAcquireTriggersWithinLock();
        if ( !isEmpty( acquireTriggersWithinLock ) ) {
            quartzProps.setJobStoreGroup( "acquireTriggersWithinLock", acquireTriggersWithinLock );
        }

        String lockHandlerClass = jobStore.getLockHandlerClass();
        if ( !isEmpty( lockHandlerClass ) ) {
            quartzProps.setJobStoreGroup( "lockHandler.class", lockHandlerClass );
            // org.quartz.impl.jdbcjobstore.StdRowLockSemaphore
            // org.quartz.impl.jdbcjobstore.UpdateLockRowSemaphore
        }

        return quartzProps;
    }

}
