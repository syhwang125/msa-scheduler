/*===================================================================================
 *                    Copyright(c) 2020 POSCO ICT
 *
 * Project            : workcenter-app
 * Source File Name   : com.aworks.workcenter.service.schedule.ScheduleService.java
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
package com.msa.scheduler.quartz.service;

import static java.util.stream.Collectors.toList;
import static org.quartz.CronExpression.isValidExpression;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.msa.scheduler.quartz.entity.Schedule;
import com.msa.scheduler.quartz.entity.ScheduleInfo;
import com.msa.scheduler.quartz.repository.ScheduleRepository;
import com.msa.scheduler.quartz.repository.SchedulerRepository;
import com.msa.scheduler.quartz.component.QuartzScheduler;

import lombok.RequiredArgsConstructor;

/**
 * ScheduleService.java
 *
 * @author ddurung
 * @version 1.0.0
 * @since 2019. 12. 31.
 */
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository repository;
    private final QuartzScheduler quartzScheduler;

    /**
     * Search the schedule
     *
     * @param spec the specification of queue
     * @param pageable the Pageable
     * @return the Page<ScheduleResponse>
     */
    @Transactional( readOnly = true )
    public List<Schedule> search( Specification<Schedule> spec, Pageable pageable ) {
        /* @formatter:off */
        return repository.findAll();
        /* @formatter:on */
    }

    /**
     * Create the schedule
     *
     * @param request the ScheduleRequest
     * @param organizationId the organization id
     */
    @Transactional
    public Schedule create( Schedule request ) {

        Schedule schedule = new Schedule( request.getName(), request.getDescription() );
        schedule.setStartDatetime( request.getStartDatetime() );
        schedule.setEndDatetime( request.getEndDatetime() );

        this.setScheduleInfo( schedule, request.getCronExpression() );
        this.setScheduleStatus( schedule );

        schedule = repository.saveAndFlush( schedule );

        // start the schedule
        quartzScheduler.start( schedule );

        return schedule;
    }

    /**
     * Return the schedule
     *
     * @param id the schedule id
     * @return the ScheduleResponse
     */
    @Transactional( readOnly = true )
    public ScheduleInfo get( UUID id ) {
        /* @formatter:off */
        return repository.findById( id )
                         .map( this::convert )
                         .orElseThrow( () -> new EntityNotFoundException( id ) );
        /* @formatter:on */
    }

    /**
     * Return the schedules simply
     *
     * @param spec the specification of schedule
     * @return List<LabelValue>
     */
    @Transactional( readOnly = true )
    public List<LabelValue> list( Specification<Schedule> spec ) {
        /* @formatter:off */
        return repository.findAll( spec, Sort.by( Direction.ASC, "name" ) )
                         .stream()
                         .map( schedule -> LabelValue.of( schedule.getName(), schedule.getId() ) )
                         .collect( toList() );
        /* @formatter:on */
    }

    /**
     * Update the schedule
     *
     * @param id the schedule id
     * @param request the ScheduleRequest
     */
    @Transactional
    public void update( UUID id, ScheduleRequest request ) {
        Optional<Schedule> optional = repository.findById( id );

        if ( optional.isPresent() ) {
            Schedule schedule = optional.get();

            if ( !principalContext.isWritable( schedule.getManager() ) ) {
                throw new ServiceException( "You do not have permission to update.", ACCESS_DENIED );
            }

            String name = request.getName();
            if ( name != null && repository.existsByDomainIdAndOrganizationAndNameIgnoreCaseAndIdNot( schedule.getDomainId(),
                    schedule.getOrganization(), name, schedule.getId() ) ) {
                throw new ServiceException( "Already exists schedule name.", EXIST_SCHEDULE_NAME );
            }

            whenHasValue( name, schedule::setName );
            whenHas( request::getStartDatetime, schedule::setStartDatetime );
            schedule.setManager( ManagerLink.instanceOf( request.getManagerId() ) );
            schedule.setDescription( request.getDescription() );
            schedule.setEndDatetime( request.getEndDatetime() );

            this.setScheduleInfo( schedule, request.getType(), request.getRepeatInterval(), request.getCronExpression() );
            this.setScheduleStatus( schedule );

            if ( !StringUtils.isEmpty( request.getCalendarId() ) ) {
                this.setBaseCalendar( schedule, request.getCalendarId() );
            } else {
                schedule.setCalendar( null );
            }

            schedule = repository.save( schedule );

            // start the schedule
            quartzScheduler.start( schedule );
        } else {
            throw new EntityNotFoundException( id );
        }
    }

    /**
     * Delete the schedule
     *
     * @param id the schedule id
     */
    @Transactional
    public void delete( UUID id ) {
        Optional<Schedule> optional = repository.findById( id );
        if ( optional.isPresent() ) {
            Schedule schedule = optional.get();

            if ( !principalContext.isWritable( schedule.getManager() ) ) {
                throw new ServiceException( "You do not have permission to delete.", ACCESS_DENIED );
            }

            if ( ACTIVE == schedule.getStatus() ) {
                throw new ServiceException( "This schedule is running.", SCHEDULE_IS_RUNNING );
            }

            if ( !schedule.getWorks().isEmpty() ) {
                throw new ServiceException( "This schedule has the associated work.", SCHEDULE_IN_USE );
            }

            quartzScheduler.delete( schedule );
            repository.delete( schedule );
        } else {
            throw new EntityNotFoundException( id );
        }
    }

    /**
     * Return the work id and name map
     *
     * @param id the schedule id
     * @return List<LabelValue>
     */
    @Transactional( readOnly = true )
    public List<LabelValue> getScheduleWorks( UUID id ) {
        /* @formatter:off */
        return repository.findById( id )
                         .map( schedule -> schedule.getWorks()
                                                   .stream()
                                                   .map( work -> LabelValue.of( work.getName(), work.getId() ) )
                                                   .collect( toList() ) )
                         .orElse( new ArrayList<>() );
        /* @formatter:on */
    }

    /**
     * Clear the manager
     *
     * @param manager the manager link
     */
    @Transactional
    public void clearManager( ManagerLink manager ) {
        repository.clearManager( manager );
    }

    /**
     * Return the existence of data by organization
     *
     * @param organization the organization link
     * @return the existence
     */
    public boolean existsByOrganization( OrganizationLink organization ) {
        return repository.existsByOrganization( organization );
    }

    /**
     * Control(Start/Stop) the schedule
     *
     * @param scheduleId the schedule id
     * @param start is start
     */
    @Transactional
    public void controlSchedule( UUID scheduleId, boolean start ) {
        Optional<Schedule> optional = repository.findById( scheduleId );
        if ( optional.isPresent() ) {
            Schedule schedule = optional.get();
            
            if ( start ) {
                // start quartz job
                quartzScheduler.start( schedule );

                // change the schedule status
                this.setScheduleStatus( schedule );

                repository.save( schedule );
            } else {
                // stop quartz job
                quartzScheduler.stop( schedule );

                // change the schedule status
                schedule.setStatus( STOPPED );
                repository.save( schedule );
            }
        } else {
            throw new EntityNotFoundException( scheduleId );
        }
    }

    /**
     * Find by the schedule id list
     *
     * @param ids the schedule's id list
     * @return schedule list
     */
    @Transactional( readOnly = true )
    public List<Schedule> findByIdList( List<UUID> ids ) {
        return repository.findByIdIn( ids ).orElse( Collections.emptyList() );
    }

    /**
     * Convert the schedule to schedule dto
     *
     * @param schedule the Schedule
     * @return the schedule dto
     */
    public ScheduleInfo convert( Schedule schedule ) {
        /* @formatter:off */
        OrganizationLink organization = schedule.getOrganization();
        ManagerLink manager = schedule.getManager();
        Calendar calendar = schedule.getCalendar();

        ScheduleInfo response = ScheduleInfo.builder()
                                                    .with( schedule::getName, ScheduleInfo::setName )
                                                    .with( schedule::getDescription, ScheduleInfo::setDescription )
                                                    .withValue( LabelValue.of( schedule.getType().getText(), schedule.getType() ), ScheduleInfo::setType )
                                                    .withValue( LabelValue.of( schedule.getStatus().getText(), schedule.getStatus() ), ScheduleInfo::setStatus )
                                                    .with( schedule::getCronExpression, ScheduleInfo::setCronExpression )
                                                    .with( schedule::getRepeatInterval, ScheduleInfo::setRepeatInterval )
                                                    .with( schedule::getStartDatetime, ScheduleInfo::setStartDatetime )
                                                    .with( schedule::getEndDatetime, ScheduleInfo::setEndDatetime )
                                                    .with( schedule::getCreatedDatetime, ScheduleInfo::setCreatedDatetime )
                                                    .with( schedule::getModifiedDatetime, ScheduleInfo::setModifiedDatetime )
                                                    .with( organization::getLabelValue, ScheduleInfo::setOrganization )
                                                    .withValue( this.isStartable( schedule ), ScheduleInfo::setStartable )
                                                    .withValue( schedule.getWorks()
                                                                        .stream()
                                                                        .map( work -> LabelValue.of( work.getName(), work.getId() ) )
                                                                        .collect( toList() ), ScheduleInfo::setWorks )
                                                    .build();
        /* @formatter:on */

        response.add( linkTo( ScheduleController.class ).slash( schedule.getId() ).withSelfRel(),
                      linkTo( methodOn( OrganizationController.class ).get( organization.getId() ) ).withRel( "organization" ) );

        if ( manager != null ) {
            response.setManager( manager.getLabelValue() );
            response.add( linkTo( methodOn( AccountController.class ).get( manager.getId() ) ).withRel( "manager" ) );
        }

        if ( calendar != null ) {
            response.setCalendar( LabelValue.of( calendar.getName(), calendar.getId() ) );
            response.add( linkTo( methodOn( CalendarController.class ).get( calendar.getId() ) ).withRel( "calendar" ) );
        }

        // ROLE_USER 인 경우 sign-in user와 manager가 동일해야 writable
        if ( !principalContext.isWritable( manager ) ) {
            response.setEditable( false );
            response.setDeletable( false );
        }

        return response;
    }

    /**
     * Set the schedule information by type
     *
     * @param schedule the Schedule
     * @param type the schedule type
     * @param repeatInterval the repeat interval
     * @param cronExpression the cron expression
     */
    private void setScheduleInfo( Schedule schedule, String cronExpression ) {
                this.checkCronExpression( cronExpression );
                schedule.setCronExpression( cronExpression );
    }

     /**
     * Set the schedule status
     *
     * @param schedule the Schedule
     */
    private void setScheduleStatus( Schedule schedule ) {
        LocalDateTime startDatetime = schedule.getStartDatetime();
        LocalDateTime endDatetime = schedule.getEndDatetime();
        LocalDateTime now = LocalDateTime.now();

            if ( startDatetime.isBefore( now ) ) {
                if ( endDatetime != null && endDatetime.isBefore( now ) ) {
                    schedule.setStatus( INACTIVE );
                } else {
                    schedule.setStatus( ACTIVE );
                }
            } else {
                schedule.setStatus( INACTIVE );
     }

    /**
     * Check the schedule cron expression by quartz
     *
     * @param cronExpression the cron expression
     */
    private void checkCronExpression( String cronExpression ) {
        if ( !isValidExpression( cronExpression ) ) {
            throw new ServiceException( "It's not a cron expression.", INVALID_CRON_EXPRESSION );
        }
    }

    /**
     * Check the schedule whether or not startable
     *
     * @return whether or not startable
     */
    private boolean isStartable( Schedule schedule ) {
        return schedule.getStatus() == STOPPED;
    }

}
