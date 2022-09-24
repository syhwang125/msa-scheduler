package com.msa.biz.service;

import org.springframework.stereotype.Component;
import org.springframework.messaging.MessageHeaders;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OneJobService {

    public void start(com.msa.scheduler.common.entity.TimeEvent event, MessageHeaders header) {
        log.info(">>>>> Biz Daily Job Service  = [" + event.toJson() + "]" + " started.");
    }
    
}
