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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag( name = "Schedule", description = "스케줄" )
public class ScheduleController {

    private final ScheduleService service;

    /**
     * Search the schedule
     *
     * @param organizationId the organization id
     * @param managedByMe managed by login account
     * @param status the schedule status
     * @param name the schedule name
     * @param assembler the PagedResourcesAssembler
     * @param pageable the Pageable
     * @return the PagedResources
     */
    @GetMapping
//    @ApiSortable( description = "조직 이름: organization.name<br/>담당자 이름: manager.nickname<br/>Schedule 이름: name<br/>Schedule 상태: status<br/>Schedule 구분: type<br/>시작일시: startDate<br/>종료일시: endDate" )
//    @Operation( summary = "Schedule 목록 조회 (search)" )
//    public PagedModel<EntityModel<ScheduleResponse>> search( //
      public ScheduleInfo search (      
            @Parameter( description = "Schedule 상태" ) @RequestParam( required = false ) ScheduleStatus status, //
            @Parameter( description = "Schedule 이름" ) @RequestParam( required = false ) String name, //
//            @Parameter( hidden = true ) PagedResourcesAssembler<ScheduleResponse> assembler, //
//            @Parameter( hidden = true ) @PageableDefault( page = 0, size = 15, sort = "createdDatetime", direction = DESC ) Pageable pageable ) {
            {
//        organizationId = principalContext.getCurrentOrganizationId( organizationId );

//        UUID managerId = null;
//        if ( managedByMe ) {
//            managerId = principalContext.getCurrentAccountId();
//        }
//
//        Specification<Schedule> searchSpec = searchSchedule( WorkCenterContext.getDomainId(), organizationId, managerId, status, type,
//                name );

        return assembler.toModel( service.search( searchSpec, pageable ) );
    }

    /**
     * Return the schedules simply
     *
     * @param organizationId the organization id
     * @return List<LabelValue>
     */
    @GetMapping( value = "/list" )
//    @Operation( summary = "Schedule 간편 목록 조회 (list)" )
    public List<Schedule> list( ) {
        return service.list( );
    }

    /**
     * Create the schedule
     *
     * @param request the ScheduleRequest
     */
    @PostMapping
    public void create( @Parameter( required = true, description = "Schedule 생성 json" ) @RequestBody ScheduleInfo request ) {
        UUID organizationId = request..getOrganizationId() );
        request.setManagerId( principalContext.checkManagerId( request.getManagerId() ) );

        service.create( request, organizationId );
    }

    /**
     * Return the schedule
     *
     * @param id the schedule id
     * @return the CalendarResponse
     */
    @GetMapping( value = "/{id}" )
    public ScheduleInfo get( @Parameter( required = true, description = "Schedule UUID" ) @PathVariable UUID id ) {
        return service.get( id );
    }

    /**
     * Update the schedule
     *
     * @param id the schedule id
     * @param request the ScheduleRequest
     */
    @PutMapping( value = "/{id}" )
//    @Operation( summary = "Schedule 수정 (update)" )
    public void update( //
            @Parameter( required = true, description = "Schedule UUID" ) @PathVariable UUID id, //
            @Parameter( required = true, description = "Schedule 데이터 변경 json" ) @RequestBody ScheduleInfo request ) {
        service.update( id, request );
    }

    /**
     * Delete the schedule
     *
     * @param id the schedule id
     */
    @DeleteMapping( value = "/{id}" )
    @RoleUserAuthorize
    @Operation( summary = "Schedule 삭제 (delete)" )
    public void delete( @Parameter( required = true, description = "Schedule UUID" ) @PathVariable UUID id ) {
        service.delete( id );
    }

    /**
     * Control the schedule
     *
     * @param id the schedule id
     * @param start is start
     */
    @PatchMapping( value = "/{id}/control" )
    @RoleUserAuthorize
    @Operation( summary = "Schedule 제어 (control)" )
    public void control( //
            @Parameter( required = true, description = "Schedule UUID" ) @PathVariable UUID id, //
            @Parameter( required = true, description = "Start 여부" ) @RequestParam boolean start ) {
        service.controlSchedule( id, start );
    }

}
