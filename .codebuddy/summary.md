# Project Summary

## Overview of Technologies Used
This project is primarily developed using the Java programming language. The following frameworks and libraries are utilized:

- **Spring Boot**: For building the application and managing dependencies.
- **MyBatis**: For database interactions.
- **JUnit**: For unit testing.
- **Docker**: For containerization of the application.

## Purpose of the Project
The project is a management system for a pet hospital, designed to handle various functionalities such as user management, authentication, and data storage. It aims to provide an organized platform for managing pet-related services, including user registration, login, and data retrieval.

## Configuration and Build Files
The following files are relevant for the configuration and building of the project:

- `/Dockerfile`
- `/mvnw`
- `/mvnw.cmd`
- `/pom.xml`

## Source Files Location
Source files can be found in the following directory:

- `/src/main/java/com/wjb/pethospitalmanage`

## Documentation Files Location
Documentation files are located in the following directory:

- `/src/main/resources/META-INF`
  - `additional-spring-configuration-metadata.json`
  - `application-prod.yml`
  - `application-test.yml`
  - `application.yml`
  - `banner.txt` 

## Summary of Directory Structure
- **SQL Files**: 
  - `/sql/create_table.sql`
  
- **Java Source Files**: 
  - `/src/main/java/com/wjb/pethospitalmanage/MainApplication.java`
  - `/src/main/java/com/wjb/pethospitalmanage/annotation/AuthCheck.java`
  - `/src/main/java/com/wjb/pethospitalmanage/aop/AuthInterceptor.java`
  - `/src/main/java/com/wjb/pethospitalmanage/aop/LogInterceptor.java`
  - `/src/main/java/com/wjb/pethospitalmanage/common/*.java`
  - `/src/main/java/com/wjb/pethospitalmanage/config/*.java`
  - `/src/main/java/com/wjb/pethospitalmanage/constant/*.java`
  - `/src/main/java/com/wjb/pethospitalmanage/controller/UserController.java`
  - `/src/main/java/com/wjb/pethospitalmanage/exception/*.java`
  - `/src/main/java/com/wjb/pethospitalmanage/generate/CodeGenerator.java`
  - `/src/main/java/com/wjb/pethospitalmanage/mapper/UserMapper.java`
  - `/src/main/java/com/wjb/pethospitalmanage/model/dto/user/*.java`
  - `/src/main/java/com/wjb/pethospitalmanage/model/entity/User.java`
  - `/src/main/java/com/wjb/pethospitalmanage/model/enums/*.java`
  - `/src/main/java/com/wjb/pethospitalmanage/model/vo/*.java`
  - `/src/main/java/com/wjb/pethospitalmanage/service/UserService.java`
  - `/src/main/java/com/wjb/pethospitalmanage/service/impl/UserServiceImpl.java`
  - `/src/main/java/com/wjb/pethospitalmanage/utils/*.java`
  
- **Resources**:
  - `/src/main/resources/META-INF/*`
  - `/src/main/resources/mapper/UserMapper.xml`
  - `/src/main/resources/templates/*`
  
- **Test Files**:
  - `/src/test/java/com/wjb/pethospitalmanage/MainApplicationTests.java`
  - `/src/test/java/com/wjb/pethospitalmanage/service/UserServiceTest.java`
  - `/src/test/java/com/wjb/pethospitalmanage/utils/EasyExcelTest.java`