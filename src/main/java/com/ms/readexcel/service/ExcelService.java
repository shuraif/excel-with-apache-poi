package com.ms.readexcel.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.readexcel.model.ExcelRow;

@Service
public class ExcelService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExcelService.class);
	
	public void readExcel() {

		// create object of jackson object mnapper (Better configure as a bean)
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);

		List<String> header = new ArrayList<>(Arrays.asList("id", "name", "age"));
		
		try {
			
			File initialFile = new File("./EmployeeList.xlsx");
			InputStream stream = new FileInputStream(initialFile);
			// open workbook using the inputstream
			XSSFWorkbook workbook = new XSSFWorkbook(stream);

			XSSFSheet sheet = workbook.getSheetAt(0);

			List<ExcelRow> excelRows = new ArrayList<ExcelRow>();

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			 Row row;
			 Cell cell;
			rowIterator.next();
			while (rowIterator.hasNext()) {

				row = rowIterator.next();

				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();
			
				// map for holding cell values of current row as key value pair
				Map<String, String> rowDataMap = new HashMap<>();

				for (int k = 0; k < row.getLastCellNum(); k++) {

					if (null != (cell = row.getCell(k))) {
						switch (cell.getCellType()) {
						case NUMERIC:
							// Get the column name from header array and push the cell value into hashmap
							rowDataMap.put(header.get(k), NumberToTextConverter.toText(cell.getNumericCellValue()));
							break;
						case STRING:
							// Get the column name from header array and push the cell value into hashmap
							rowDataMap.put(header.get(k), cell.getStringCellValue());
							break;

						}
					}
				}

				// after reading all the cells of current row map the hashmap into
				// custom pojo class using object mapper
				ExcelRow rawData = mapper.convertValue(rowDataMap, ExcelRow.class);
				// add the cusom pojo object into the list
				excelRows.add(rawData);

			}
			workbook.close();
			excelRows.forEach(r->{
				LOG.info("id :{} , name :{} , age :{}",r.getId(),r.getName(),r.getAge());
			});
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
