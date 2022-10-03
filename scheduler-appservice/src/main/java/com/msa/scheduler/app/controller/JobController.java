package com.msa.scheduler.app.controller;

import java.util.HashMap;
import java.util.List;

import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.msa.scheduler.app.entity.SchedulerJobInfo;
import com.msa.scheduler.app.service.SchedulerJobService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class JobController {

    private final SchedulerJobService jobService;
    
    @RequestMapping(value = "/metaData")
    public Object metaData() throws SchedulerException {
        SchedulerMetaData metaData = jobService.getMetaData();
        return metaData;
    }
    
    @RequestMapping(value = "/getAllJobs")
    public Object getAllJobs() throws SchedulerException {
        List<SchedulerJobInfo> jobList = jobService.getAllJobList();
        HashMap<String, List<SchedulerJobInfo>> map = new HashMap<String, List<SchedulerJobInfo>>();
        map.put( "jobs", jobList );
        return map;
    }
    
    
    @RequestMapping(value = "/saveOrUpdate", method = { RequestMethod.GET, RequestMethod.POST } )
    public boolean saveOrUpdate(SchedulerJobInfo jobInfo) {
        log.info( "params, job = {}", jobInfo );
        try {
            jobService.saveOrUpdate(jobInfo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
    }
    @RequestMapping(value = "/runJob", method = { RequestMethod.GET, RequestMethod.POST } )
    public boolean runJob(SchedulerJobInfo jobInfo ) {
        log.info( "params, job = {}, {}", jobInfo.getJobId(), jobInfo.getJobName() );
        try {
            return jobService.startJob( jobInfo );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @RequestMapping(value = "/pauseJob", method = { RequestMethod.GET, RequestMethod.POST } ) 
    public boolean pauseJob( SchedulerJobInfo jobInfo ) {
        log.info( "params, job = {}, {}", jobInfo.getJobId(), jobInfo.getJobName() );
        try {
            return jobService.pauseJob( jobInfo );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @RequestMapping(value = "/resumeJob", method = {RequestMethod.GET, RequestMethod.POST }) 
    public boolean resumeJob(SchedulerJobInfo jobInfo) {
        log.info( "params, job = {}, {}", jobInfo.getJobId(), jobInfo.getJobName()  );
        try {
            return jobService.resumeJob( jobInfo );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
