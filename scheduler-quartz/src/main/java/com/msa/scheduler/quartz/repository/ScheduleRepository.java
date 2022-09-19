/*===================================================================================
 *                    Copyright(c) 2020 POSCO ICT
 *
 * Project            : workcenter-app
 * Source File Name   : com.aworks.workcenter.domain.repository.ScheduleRepository.java
 * Description        :
 * Author             : ddurung
 * Version            : 1.0.0
 * File Name related  :
 * Class Name related :
 * Created Date       : 2019. 10. 24.
 * Updated Date       : 2019. 10. 24.
 * Last modifier      : ddurung
 * Updated content    : 최초작성
 *
 *==================================================================================*/
package com.msa.scheduler.quartz.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.msa.scheduler.quartz.entity.Schedule;


/**
 * ScheduleRepository.java
 *
 * @author ddurung
 * @version 1.0.0
 * @since 2019. 10. 24.
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

    public Optional<List<Schedule>> findByIdIn( List<UUID> ids );

}
