package com.msa.scheduler.app.entity;

import java.util.stream.IntStream;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.msa.scheduler.app.service.SchedulerJobService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleCronJob implements Job {

    @Autowired 
    private SchedulerJobService service;
    
    @Override
    public void execute( JobExecutionContext context ) throws JobExecutionException {
      log.info( "SimpleCronJob start .... " );
      IntStream.range(0,10).forEach( i -> {
          log.info("Counting - {}", i);
          try {
              Thread.sleep( 1000 );
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      });
      log.info( "SimpleCronJob End ..." );
      
            
    }

}
