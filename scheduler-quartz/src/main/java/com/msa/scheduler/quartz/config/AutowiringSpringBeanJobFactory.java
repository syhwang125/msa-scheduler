/*===================================================================================
 *                    Copyright(c) 2020 POSCO ICT
 *
 * Project            : workcenter-app
 * Source File Name   : com.aworks.workcenter.config.AutowiringSpringBeanJobFactory.java
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

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * AutowiringSpringBeanJobFactory.java
 *
 * @author ddurung
 * @version 1.0.0
 * @since 2019. 9. 6.
 */
public class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

    private AutowireCapableBeanFactory beanFactory;

    /*
     * (non-Javadoc)
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext( ApplicationContext applicationContext ) {
        this.beanFactory = applicationContext.getAutowireCapableBeanFactory();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.scheduling.quartz.SpringBeanJobFactory#createJobInstance(org.quartz.spi.TriggerFiredBundle)
     */
    @Override
    protected Object createJobInstance( final TriggerFiredBundle bundle ) throws Exception {
        final Object job = super.createJobInstance( bundle );
        beanFactory.autowireBean( job );
        return job;
    }

}
