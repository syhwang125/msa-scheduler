package com.msa.scheduler.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TimeEvent implements Serializable {

    private String job_id;
    private String job_name; 
    private Date fire_date;
    private Date scheduled_fire_date;
    private Date previous_fire_date;
    private Date next_fire_date;
    
    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
    
    public static TimeEvent fromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        TimeEvent obj = null;
        try {
            obj = objectMapper.readValue( json, new TypeReference<TimeEvent>() {
                
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
