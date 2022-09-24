package com.msa.biz.service;

import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EveryJobService {
    public void start(com.msa.scheduler.common.entity.TimeEvent event, MessageHeaders header) {
        log.info(">>>>> Biz Every Job Service  = [" + event.toJson() + "]" + " started.");
    }
    
}
