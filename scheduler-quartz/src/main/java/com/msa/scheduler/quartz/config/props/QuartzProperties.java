/*===================================================================================
 *                    Copyright(c) 2020 POSCO ICT
 *
 * Project            : workcenter-app
 * Source File Name   : com.aworks.workcenter.config.props.QuartzProperties.java
 * Description        :
 * Author             : ddurung
 * Version            : 1.0.0
 * File Name related  :
 * Class Name related :
 * Created Date       : 2019. 12. 31.
 * Updated Date       : 2019. 12. 31.
 * Last modifier      : ddurung
 * Updated content    : 최초작성
 *
 *==================================================================================*/
package com.msa.scheduler.quartz.config.props;

import static org.quartz.impl.StdSchedulerFactory.PROP_JOB_STORE_PREFIX;
import static org.quartz.impl.StdSchedulerFactory.PROP_SCHED_INSTANCE_ID;
import static org.quartz.impl.StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME;
import static org.quartz.impl.StdSchedulerFactory.PROP_SCHED_JMX_EXPORT;

import java.util.Properties;

/**
 * QuartzProperties.java
 *
 * @author ddurung
 * @version 1.0.0
 * @since 2019. 12. 31.
 */
public class QuartzProperties extends Properties {

    private static final long serialVersionUID = 74564380936775789L;

    /**
     * Return the instance name
     * 
     * @return the instance name
     */
    public String getInstanceName() {
        return super.getProperty( PROP_SCHED_INSTANCE_NAME );
    }

    /**
     * Set the instance name
     * 
     * @param instanceName the instance name
     */
    public void setInstanceName( String instanceName ) {
        super.setProperty( PROP_SCHED_INSTANCE_NAME, instanceName );
    }

    /**
     * Return the instance id
     * 
     * @return the instance id
     */
    public String getInstanceId() {
        return super.getProperty( PROP_SCHED_INSTANCE_ID );
    }

    /**
     * Set the instance id
     * 
     * @param instanceId the instance id
     */
    public void setInstanceId( String instanceId ) {
        super.setProperty( PROP_SCHED_INSTANCE_ID, instanceId );
    }

    /**
     * Set the jmx export
     * 
     * @param jmxExport the jmx export
     */
    public void setJmxExport( String jmxExport ) {
        super.setProperty( PROP_SCHED_JMX_EXPORT, jmxExport );
    }

    /**
     * Set the job store group
     * 
     * @param type the job store group type
     * @param value the job store group value
     */
    public void setJobStoreGroup( String type, String value ) {
        super.setProperty( PROP_JOB_STORE_PREFIX + "." + type, value );
    }

    /**
     * Return the job store is clustered
     * 
     * @return whether or not clustered
     */
    public boolean isClustered() {
        return Boolean.parseBoolean( super.getProperty( PROP_JOB_STORE_PREFIX + ".isClustered" ) );
    }
}
