package com.mislbd.report_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReportManagerApplication {
	// in project structure > project> java-22
	//  in setting>build>gradle> Java-22

	public static void main(String[] args) {
		SpringApplication.run(ReportManagerApplication.class, args);
		System.out.println("Hello Report Manager");
	}

}
