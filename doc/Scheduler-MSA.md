# Eclipse 에서 Maven POM ( module 생성 ) 


# Git hub 에서 Repository 생성 및 로컬과 연동 

https://github.com/syhwang125/msa-scheduler


<  Git Bash >
$git config --global user.name 'syhwang125'
$git config --global user.password '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'

$git pull / push 


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



# Kafka Docker

1. Kafka Docker에 설치 후 spring boot 연동 

2. Kafka-Docker install  
   > https://hub.docker.com/r/wurstmeister/kafka/ 

3. Eclipse 에서 kafka 연동 자바 프로젝트 생성
   > pom.xml 파일에 kafka 라이브러리 정보 추가 
   
   > application.yml 파일에 kafka 포트 등  정보 매핑 

4. docker-compose.yml 파일 생성

  * 가다
    + 나 
       - 다 

< mobaxterm > SSH 192.168.193.180  ( syhwang125 / ymcar ) 

$syhwang125@rndg8c15:~$ pwd
/home/syhwang125
$syhwang125@rndg8c15:~$ ls
docker-compose.yml

$sudo docker-compose up -d 

$sudo docker ps 
syhwang125@rndg8c15:~$ sudo docker ps
CONTAINER ID   IMAGE                      COMMAND                  CREATED      STATUS          PORTS                                                                     NAMES
629d134b9ca0   bitnami/kafka:latest       "/opt/bitnami/script…"   5 days ago   Up 16 seconds   0.0.0.0:9092->9092/tcp, :::9092->9092/tcp                                 syhwang125_kafka_1
88158d9de143   bitnami/zookeeper:latest   "/opt/bitnami/script…"   5 days ago   Up 16 seconds   2888/tcp, 3888/tcp, 0.0.0.0:2181->2181/tcp, :::2181->2181/tcp, 8080/tcp   syhwang125_zookeeper_1
a34b7a6cf283   mariadb:10.4               "docker-entrypoint.s…"   6 days ago   Up 6 days       0.0.0.0:3306->3306/tcp, :::3306->3306/tcp                                 mariadbtest

syhwang125@rndg8c15:~$