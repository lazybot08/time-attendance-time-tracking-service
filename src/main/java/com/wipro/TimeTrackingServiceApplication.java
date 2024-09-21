package com.wipro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class TimeTrackingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeTrackingServiceApplication.class, args);
	}

}
