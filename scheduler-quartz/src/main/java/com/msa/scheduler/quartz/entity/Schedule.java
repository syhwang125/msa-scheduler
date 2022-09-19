/*===================================================================================
 *                    Copyright(c) 2020 POSCO ICT
 *
 * Project            : workcenter-app
 * Source File Name   : com.aworks.workcenter.domain.Schedule.java
 * Description        :
 * Author             : ddurung
 * Version            : 1.0.0
 * File Name related  :
 * Class Name related :
 * Created Date       : 2019. 10. 1.
 * Updated Date       : 2019. 10. 1.
 * Last modifier      : ddurung
 * Updated content    : 최초작성
 *
 *==================================================================================*/
package com.msa.scheduler.quartz.entity;

import static javax.persistence.ConstraintMode.NO_CONSTRAINT;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.hibernate.annotations.NotFoundAction.IGNORE;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;

import kafka.common.BaseEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Schedule.java
 *
 * @author ddurung
 * @version 1.0.0
 * @since 2019. 10. 1.
 */
@Entity
@Table( name = "SCHEDULE" )
@Getter
@NoArgsConstructor( access = PROTECTED )
public class Schedule {   // extends BaseUUIDEntity {


    @Id
    @GeneratedValue( generator = "UUID" )
    @Column( name = "ID", updatable = false, length = 16 )
    protected UUID id;
    
    public enum ScheduleStatus implements BaseEnum {
        ACTIVE( "Active" ), //
        INACTIVE( "Inactive" ), //
        STOPPED( "Stopped" );

        private String text;

        /**
         * Constructor
         *
         * @param text the text
         */
        ScheduleStatus( String text ) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        @Override
        public int productArity() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public Object productElement( int n ) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean canEqual( Object that ) {
            // TODO Auto-generated method stub
            return false;
        }

    }

    @Setter
    @Column( name = "NAME", length = 100, nullable = false )
    private String name;

    @Setter
    @Column( name = "DESCRIPTION", length = 500 )
    private String description;

    @Setter
    @Column( name = "STATUS", length = 15, nullable = false )
    @Enumerated( STRING )
    private ScheduleStatus status = ScheduleStatus.STOPPED;

    @Setter
    @Column( name = "CRON_EXPRESSION", length = 50 )
    private String cronExpression;


    @Setter
    @Column( name = "START_DATETIME" )
    private LocalDateTime startDatetime;

    @Setter
    @Column( name = "END_DATETIME" )
    private LocalDateTime endDatetime;


    /**
     * Constructor
     *
     * @param name the schedule name
     * @param description the schedule description
     * @param type the schedule type
     * @param organization the organization link
     */
    public Schedule( String name, String description) { 
        this.name = name;
        this.description = description;
    }

}
