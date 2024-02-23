//package com.webatrio.atrio;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
//import org.springframework.context.annotation.Bean;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.utility.DockerImageName;
//
//@TestConfiguration(proxyBeanMethods = false)
//public class TestWebatrioApplication {
//
//	@Bean
//	@ServiceConnection
//	PostgreSQLContainer<?> mysqlContainer() {
//		return new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"));
//	}
//
//	public static void main(String[] args) {
//		SpringApplication.from(WebatrioApplication::main).with(TestWebatrioApplication.class).run(args);
//	}
//
//}
