/*===================================================================================
 *                    Copyright(c) 2020 POSCO ICT
 *
 * Project            : workcenter-app
 * Source File Name   : com.aworks.workcenter.config.props.ConfigProperties.java
 * Description        :
 * Author             : ddurung
 * Version            : 1.0.0
 * File Name related  :
 * Class Name related :
 * Created Date       : 2019. 9. 6.
 * Updated Date       : 2019. 9. 6.
 * Last modifier      : ddurung
 * Updated content    : 최초작성
 *
 *==================================================================================*/
package com.msa.scheduler.quartz.config.props;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

/**
 * ConfigProperties.java
 *
 * @author ddurung
 * @version 1.0.0
 * @since 2019. 9. 6.
 */
@Getter
@Setter
@Component
@ConfigurationProperties( "spring" )
@Validated
public class ConfigProperties {

//    private Account account = new Account();
//    private Cluster cluster = new Cluster();
//    private Domain domain = new Domain();
//    private Mail mail = new Mail();
//    private Message message = new Message();
//    private Netty netty = new Netty();
//    private Oauth2 oauth2 = new Oauth2();
//    private Purge purge = new Purge();
//    private kafka kafka = new Rabbitmq();
    private Scheduler scheduler = new Scheduler();
//    private Swagger swagger = new Swagger();
//    private Threadpool threadpool = new Threadpool();
//    private Threshold threshold = new Threshold();

 /*   @Getter
    @Setter
    public static class Account {
        private Lock lock = new Lock();
        private Password password = new Password();
        private Onetime onetime = new Onetime();
        private String[] notAllowedNames = {};

        @Getter
        @Setter
        public static class Lock {
            private int failCount = 5;
            private long timeoutMinute = 30;
        }

        @Getter
        @Setter
        public static class Password {
            private int expireDay = 90;
            private int authExpireMinute = 60;
        }

        @Getter
        @Setter
        public static class Onetime {
            private int tokenTimeout = 1440;
        }
    }

    @Getter
    @Setter
    public static class Cluster {
        private boolean enable = false;
        private List<String> members = new ArrayList<>();
    }*/

 /*   @Getter
    @Setter
    public static class Domain {
        @Pattern( regexp = "(?:[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]{0,61}[a-z0-9]" )
        private String name;
        private String description;
        @Pattern( regexp = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-zA-Z]{2,})$" )
        private String supervisor;
        private String supervisorId;
        private String initPassword;
        private String webUrl;
    }*/

/*    @Getter
    @Setter
    public static class Mail {
        private Setting setting = new Setting();
        private Content content = new Content();
        private Schedule schedule = new Schedule();

        @Getter
        @Setter
        public static class Setting {
            private String host;
            private int port = 25;
            private String username;
            private String password;
            private Properties properties = new Properties();
        }

        @Getter
        @Setter
        public static class Content {
            private String sender = "super@aworks.com";
            private String forgotSubject = "[a.works] 비밀번호 재설정 안내입니다.";
        }

        @Getter
        @Setter
        public static class Schedule {
            private boolean enable = false;
            private int period = 10;
        }
    }

    @Getter
    @Setter
    public static class Netty {
        private int bossCount = 1;
        private int workerCount = 10;
        private int tcpPort = 9091;
        private int backlog = 100;
    }

    @Getter
    @Setter
    public static class Oauth2 {
        private TokenValidity tokenValidity = new TokenValidity();

        @Getter
        @Setter
        public static class TokenValidity {
            private int access = 1800;
            private int refresh = 28800;
        }
    }

    @Getter
    @Setter
    public static class Purge {
        private RetentionPolicy alarm = new AlarmRetention();
        private RetentionPolicy processReport = new ProcessReportRetention();
        private RetentionPolicy queueMessage = new QueueMessageRetention();
        private RetentionPolicy robotLog = new RobotLogRetention();
        private RetentionPolicy robotUsage = new RobotUsageRetention();
        private RetentionPolicy scheduleLog = new ScheduleLogRetention();
        private RetentionPolicy userLog = new UserLogRetention();
        private RetentionPolicy workReport = new WorkReportRetention();

        @Getter
        @Setter
        public static class RetentionPolicy {
            private int month = -1;

            public boolean isUse() {
                return month > 0;
            }
        }

        public static class AlarmRetention extends RetentionPolicy {
        }

        public static class ProcessReportRetention extends RetentionPolicy {
        }

        public static class QueueMessageRetention extends RetentionPolicy {
        }

        public static class RobotLogRetention extends RetentionPolicy {
        }

        public static class RobotUsageRetention extends RetentionPolicy {
        }

        public static class ScheduleLogRetention extends RetentionPolicy {
        }

        public static class UserLogRetention extends RetentionPolicy {
        }

        public static class WorkReportRetention extends RetentionPolicy {
        }
    }*/

    @Getter
    @Setter
    public static class Scheduler {
        private String name = "Spring Quartz Scheduler";
        private boolean autoStartup = true;
        private boolean jmxExport = true;
        private boolean exposeSchedulerInRepository = true;
        private boolean overwriteExistingJobs = true;
        private boolean waitForJobsToCompleteOnShutdown = true;
        private JobStore jobStore = new JobStore();

        @Getter
        @Setter
        public static class JobStore {
            private String className = "org.quartz.impl.jdbcjobstore.JobStoreTX";
            private String driverDelegateClass = "org.quartz.impl.jdbcjobstore.StdJDBCDelegate";
            private String useProperties = "false";
            private String misfireThreshold = "60000";
            private String tablePrefix = "QRTZ_";
            private String isClustered = "true";
            private String clusterCheckinInterval = "10000";
            private String acquireTriggersWithinLock;
            private String lockHandlerClass;
        }
    }

/*    @Getter
    @Setter
    public static class Rabbitmq {
        private String managementUrl = "http://localhost:15672/api";
        private String exchange = "aworks.direct";
    }

    @Getter
    @Setter
    public static class Message {
        private String providerName = "com.aworks.workcenter.service.message.spi.FileMessageServiceProvider";
    }

    @Getter
    @Setter
    public static class Swagger {
        private String title = "a.works WorkCenter Application";
        private String description = "Cloud RPA a.works WorkCenter Application";
        private String basePackage = "com.aworks.workcenter.service";
        private String license = "";
        private String licenseUrl = "";
        private String termOfServiceUrl = "";
        private String tokenUrl = "http://localhost:8080/oauth/token";
    }

    @Getter
    @Setter
    public static class Threadpool {
        private int nettyPoolSize = 10;
    }

    @Getter
    @Setter
    public static class Threshold {
        private StatusChange statusChange = new StatusChange();

        @Getter
        @Setter
        public static class StatusChange {
            private int queueMessage = 24;
        }
    }*/

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
/*    @Override
    public String toString() {
        return JacksonUtils.writeToYaml( this );
    }*/
}
