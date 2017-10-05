package za.co.tfoldcord.docgen;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 

public class CSVInvoiceGen {
	
        public static final int AMOUNT_INDEX=7, TEAM_TOTAL=8;	
	public  ByteArrayOutputStream getExcelFile(List<String> headers, Map<String, String[]> reportData, List<Integer> amountColIndex, String regex) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Report");
	
		CellStyle style;
		style = workbook.createCellStyle();                
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,###.00"));	
		int rowIndex = 0;
		int colIndex = 0;
		Row row = sheet.createRow(rowIndex++);
		
		// populate column headers
		for(String columnLabel : headers) {
			Cell cell = row.createCell(colIndex++);
			cell.setCellValue(columnLabel);
		}
		// for each team 
	    Iterator<String> teams = reportData.keySet().iterator();
	    Row xcelrowData;
	    while(teams.hasNext()) {
		// now populate actual data
	    	String teamName = (String)teams.next();
			for(String rowData : reportData.get(teamName)) {
				String [] columns = rowData.split(regex);
				xcelrowData = sheet.createRow(rowIndex++);
				colIndex = 0;
				for(String colData : columns) {
					Cell cellData = xcelrowData.createCell(colIndex++);
					if(amountColIndex.contains(colIndex-1)){
                        cellData.setCellStyle(style);
                    }
					cellData.setCellValue(colData);
				}
			}
	       xcelrowData = sheet.createRow(rowIndex++);
	    }
		workbook.write(baos);
		workbook.close();
		return baos;
		
	}
	// for testing only
	private  FileOutputStream getExcelPhysicalFile(List<String> headers, List<String> reportData ) throws IOException {
		FileOutputStream file = new FileOutputStream("/tmp/Qhekwana.xlsx");
		Workbook workbook = new XSSFWorkbook();
                CellStyle style;
		Sheet sheet = workbook.createSheet("Report");
                style = workbook.createCellStyle();                
                DataFormat format = workbook.createDataFormat();
                style.setDataFormat(format.getFormat("#,###.00"));	
		int rowIndex = 0;
		int colIndex = 0;
		Row row = sheet.createRow(rowIndex++);
		
		// populate column headers
		for(String columnLabel : headers) {
			Cell cell = row.createCell(colIndex++);
			cell.setCellValue(columnLabel);
		}
		
		// now populate actual data
		for(String rowData : reportData) {
			String [] columns = rowData.split(",");
			Row xcelrowData = sheet.createRow(rowIndex++);
			colIndex = 0;
			for(String colData : columns) {
				Cell cellData = xcelrowData.createCell(colIndex++);
				
                                if(AMOUNT_INDEX==colIndex-1 || TEAM_TOTAL == colIndex-1){
                                    cellData.setCellStyle(style);
                                }
                cellData.setCellValue(colData.trim());
			}
		}
		
		workbook.write(file);
		workbook.close();
		return file;
		
	}
	//for testing only
	public static void main(String [] args) throws Exception{
		List<String> headers = new ArrayList<>();
		
			headers.add("Header 1");
			headers.add("Header 2");
			headers.add("Header 3");
			headers.add("Header 4");
			headers.add("Header 5");
			headers.add("Header 7");
			headers.add("Amount 8");
			headers.add("Amount 9");
			headers.add("Package 10");
                        
		List<String> contents = new ArrayList<>();
		
		contents.add("row 1 header 1, row 1 header 2, row 1 header 3, row 1 header 4, row 1 header 5, row 1 header 6, row 1 header 7 ,45445.55, 2344334, some package 1");
		contents.add("row 2 header 1, row 2 header 2, row 2 header 3, row 2 header 4, row 2 header 5, row 2 header 6, row 2 header 7 ,576454.55, 223.234, some package 2");
		contents.add("row 3 header 3, row 3 header 2, row 3 header 3, row 3 header 4, row 3 header 5, row 3 header 6, row 3 header 7 ,2323.55, 2432.23, some package 3");
		contents.add("row 4 header 1, row 1 header 2, row 1 header 3, row 1 header 4, row 1 header 5, row 4 header 6, row 4 header 7 ,343434.55, 24324.21, some package 4");
		contents.add("row 5 header 1, row 5 header 2, row 5 header 3, row 5 header 4, row 5 header 5, row 5 header 6, row 5 header 7 ,5646.55, 5765765.12, some package 5");
		
		CSVInvoiceGen gen = new CSVInvoiceGen();
			gen.getExcelPhysicalFile(headers, contents);
	}
	
	

}
