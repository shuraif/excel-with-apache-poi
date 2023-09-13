package com.ms.readexcel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.ms.readexcel.service.ExcelService;

@SpringBootApplication
public class ReadExcelApplication {
 
	@Autowired
	ExcelService excelService;
	
	public static void main(String[] args) {
		SpringApplication.run(ReadExcelApplication.class, args);
		
		
	}

	@EventListener(ApplicationReadyEvent.class)
	public void test() {
		
		excelService.readExcel();
	}
}
