# RESTful API 테스트용 프로젝트
---
## 프로젝트 정의
 * [ 블록체인 기반 수출채권 매입 및 한도관리 시스템 ]의 수출계약서류 조회 및 등록 목적이며  API 테스트용으로 사용

---
## 개발환경
* IDE : STS 3.x버전(lombok 라이브러리 호환성 문제로 STS4 는 사용하지 않음)
* lombok 1.16.20 설치
* Framework : SpringBoot 2.1.6, Mybatis
* JAVA : JDK 1.8
* DB : MySQL 8.0.11
* Front-End Stack : React, JavaScript, jQuery
* BootStrap : metronic v4.5.4 UI Template 사용
 
 
## 데이터베이스 SQL 작업목록

- Database System : MySQL 8.0.11
- ip : localhost:3306
- schema : 임의명
- user : 유저 ID
- pw : 유저 PW

---

* 데이터베이스 스키마 생성 및 유저생성 SQL

```
mysql> use mysql;
mysql> create database 스키마명 default character set utf8;
mysql> create user 유저명@localhost identified by '패스워드';
mysql> grant all privileges on 스키마명.* to 유저명@localhost identified by '패스워드';
mysql> flush privileges;
```

* Dummy API 데이터베이스 정보 및 외부 접속 권한부여

```	
mysql> grant all privileges on 스키마명.* to 유저명@외부ip identified by '패스워드' with grant option;
```
    
* 수출계약서류 조회 용 테이블 생성 및 Dummy Data 입력 SQL

```

* export_contract.sql 파일참조
```

 
 
   
  
  