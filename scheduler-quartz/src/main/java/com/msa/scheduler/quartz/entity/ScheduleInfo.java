/*===================================================================================
 *                    Copyright(c) 2020 POSCO ICT
 *
 * Project            : workcenter-app
 * Source File Name   : com.aworks.workcenter.domain.dto.ScheduleResponse.java
 * Description        :
 * Author             : DHLEE
 * Version            : 1.0.0
 * File Name related  :
 * Class Name related :
 * Created Date       : Jan 17, 2020
 * Updated Date       : Jan 17, 2020
 * Last modifier      : DHLEE
 * Updated content    : 최초작성
 *
 *==================================================================================*/
package com.msa.scheduler.quartz.entity;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;


public class ScheduleInfo  {
    
    private String name;

    private String description;

//    private LabelValue status;
    private String status;

    private String cronExpression;

    private int repeatInterval;

//    @Schema( description = "시작일시", example = "2020.01.01 00:00:00" )
//    @JsonFormat( shape = STRING, pattern = "yyyy.MM.dd HH:mm:ss" )
    private LocalDateTime startDatetime;

//    @Schema( description = "종료일시", example = "2020.01.01 00:00:00" )
//    @JsonFormat( shape = STRING, pattern = "yyyy.MM.dd HH:mm:ss" )
    private LocalDateTime endDatetime;

//    public static GenericBuilder<ScheduleInfo> builder() {
//        return GenericBuilder.of( ScheduleInfo::new );
//    }
}
