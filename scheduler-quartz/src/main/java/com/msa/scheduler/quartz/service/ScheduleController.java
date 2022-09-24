package com.msa.scheduler.quartz.service;

import static org.springframework.data.domain.Sort.Direction.DESC;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.msa.scheduler.quartz.entity.Schedule;
import com.msa.scheduler.quartz.entity.Schedule.ScheduleStatus;
import com.msa.scheduler.quartz.entity.ScheduleInfo;

import lombok.RequiredArgsConstructor;

/**
 * ScheduleController.java
 *
 * @author ddurung
 * @version 1.0.0
 * @since 2019. 12. 31.
 */
@RestController
@RequestMapping( value = "/schedules" )
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService service;

  

    /**
     * Return the schedules simply
     *
     * @param organizationId the organization id
     * @return List<LabelValue>
     */
    @GetMapping( value = "/list" )
    public List<Schedule> list( ) {
        return service.list( );
    }

    /**
     * Create the schedule
     *
     * @param request the ScheduleRequest
     */
    @PostMapping
    public void create( @RequestBody ScheduleInfo request ) {

        service.create( request );
    }

    /**
     * Return the schedule
     *
     * @param id the schedule id
     * @return the CalendarResponse
     */
    @GetMapping( value = "/{id}" )
    public ScheduleInfo get( @PathVariable UUID id ) {
        return service.get( id );
    }

    /**
     * Update the schedule
     *
     * @param id the schedule id
     * @param request the ScheduleRequest
     */
    @PutMapping( value = "/{id}" )
    public void update( //
             @PathVariable UUID id, //
            @RequestBody ScheduleInfo request ) {
        service.update( id, request );
    }

    /**
     * Delete the schedule
     *
     * @param id the schedule id
     */
    @DeleteMapping( value = "/{id}" )
    public void delete(  @PathVariable UUID id ) {
        service.delete( id );
    }

    /**
     * Control the schedule
     *
     * @param id the schedule id
     * @param start is start
     */
    @PatchMapping( value = "/{id}/control" )
    public void control( //
            @PathVariable UUID id, //
             @RequestParam boolean start ) {
        service.controlSchedule( id, start );
    }

}
