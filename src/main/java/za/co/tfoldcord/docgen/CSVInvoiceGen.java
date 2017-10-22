package za.co.tfoldcord.docgen;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 

public class CSVInvoiceGen {
	
        public static final int AMOUNT_INDEX=7, TEAM_TOTAL=8;	
	public  ByteArrayOutputStream getExcelFile(List<String> headers, Map<String, String[]> reportData, List<Integer> amountColIndex, String regex) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Workbook workbook = new XSSFWorkbook();
		CellStyle style;
		Sheet sheet = workbook.createSheet("Report");
        style = workbook.createCellStyle();                
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,###.00"));	
		CellStyle totalsStyle, subTotalStyle;
		Font font = workbook.createFont();//Create font
		Font subTotalFont = workbook.createFont();
		subTotalFont.setBold(true);
		subTotalFont.setUnderline(Font.U_SINGLE_ACCOUNTING);
	    font.setBold(true);//Make font bold
	    font.setUnderline(Font.U_DOUBLE_ACCOUNTING);
	    totalsStyle = workbook.createCellStyle();  
	    subTotalStyle = workbook.createCellStyle();
	    subTotalStyle.setDataFormat(format.getFormat("#,###.00"));
	    subTotalStyle.setFont(subTotalFont);
	    totalsStyle.setDataFormat(format.getFormat("#,###.00"));
	    totalsStyle.setFont(font);
		int rowIndex = 0;
		int colIndex = 0;
		Map<Integer, BigDecimal> totals = new HashMap<>();
		resetGrandTotals(totals, amountColIndex);
		resetTotals(totals, amountColIndex);
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
		    	String  [] rowData = reportData.get(teamName);
				for(String dataItem : rowData) {	
					if(rowData == null || dataItem == null) {
						continue;
					}
					String [] columns = dataItem.split(",");
					xcelrowData = sheet.createRow(rowIndex++);
					colIndex = 0;
					for(String colData : columns) {
						Cell cellData = xcelrowData.createCell(colIndex++);
						int amountIndex = colIndex-1;
						if(amountColIndex.contains(amountIndex)){
	                        cellData.setCellStyle(style);
	                        try {
	                        	totals.put(amountIndex, totals.get(amountIndex).add(new BigDecimal(colData.trim())));//subTotals
	                        }catch (Exception e) {
	                        	totals.put(amountIndex, totals.get(amountIndex).add(BigDecimal.ZERO));//subTotals
	                        	System.err.println("1 Invalid Data Occured --> " + colData.trim());
							}
	                        try {
	                        	totals.put(-amountIndex, totals.get(-amountIndex).add(new BigDecimal(colData.trim())));//grandTotals
	                        }catch (Exception e) {
	                        	totals.put(-amountIndex, totals.get(-amountIndex).add(BigDecimal.ZERO));//grandTotals
	                        	System.err.println("2 Invalid Data Occured --> " + colData.trim());
							}
	                        try {
	                        	cellData.setCellValue(new BigDecimal(colData.trim()).doubleValue());
	                        }catch (Exception e) {
	                        	cellData.setCellValue(BigDecimal.ZERO.doubleValue());
	                        	System.err.println("3 Invalid Data Occured --> " + colData.trim());
							}
	                    }
						else {
							 cellData.setCellValue(colData.trim());
						}
						
					}
				}
		       xcelrowData = sheet.createRow(rowIndex++);//create row for subtotals
		       
		       for(int index = 0; index < headers.size(); index++) {
		    	   Cell rowCellTotal = xcelrowData.createCell(index);
		    	   if(amountColIndex.contains(index)){
		    		   rowCellTotal.setCellStyle(subTotalStyle);		    	      
		    	       rowCellTotal.setCellValue(totals.get(index).doubleValue());		    	      
		    	   }
		    	   else {
		    		   rowCellTotal.setCellValue(""); 
		    	   }
		       }
		       resetTotals(totals, amountColIndex);//reset totals for new row group
	    }
	    
	    
	    xcelrowData = sheet.createRow(rowIndex++);
		xcelrowData = sheet.createRow(rowIndex++);
		
		 for(int index = 0; index < headers.size(); index++) {
    	   Cell rowCellTotal = xcelrowData.createCell(index);
    	   if(amountColIndex.contains(index)){
    		   rowCellTotal.setCellStyle(totalsStyle);		    	      	    	       
    	       rowCellTotal.setCellValue(totals.get(-index).doubleValue());
    	   }
    	   else {
    		   rowCellTotal.setCellValue(""); 
    	   }
        }
	    
		workbook.write(baos);
		workbook.close();
		return baos;
		
	}
	// for testing only
	private  FileOutputStream getExcelPhysicalFile(List<String> headers, Map<String, String[]> reportData, List<Integer> amountColIndex, String regex) throws IOException {
		FileOutputStream file = new FileOutputStream("/tmp/Qhekwana.xlsx");
		Workbook workbook = new XSSFWorkbook();
        CellStyle style;
		Sheet sheet = workbook.createSheet("Report");
              style = workbook.createCellStyle();                
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,###.00"));	
        CellStyle totalsStyle, subTotalStyle;
		Font font = workbook.createFont();//Create font
		Font subTotalFont = workbook.createFont();
		subTotalFont.setBold(true);
		subTotalFont.setUnderline(Font.U_SINGLE_ACCOUNTING);
	    font.setBold(true);//Make font bold
	    font.setUnderline(Font.U_DOUBLE_ACCOUNTING);
	    totalsStyle = workbook.createCellStyle();  
	    subTotalStyle = workbook.createCellStyle();
	    subTotalStyle.setDataFormat(format.getFormat("#,###.00"));
	    subTotalStyle.setFont(subTotalFont);
	    totalsStyle.setDataFormat(format.getFormat("#,###.00"));
	    totalsStyle.setFont(font);
		int rowIndex = 0;
		int colIndex = 0;
		Row row = sheet.createRow(rowIndex++);
		Map<Integer, BigDecimal> totals = new HashMap<>();
		resetGrandTotals(totals, amountColIndex);
		resetTotals(totals, amountColIndex);
		// populate column headers
		for(String columnLabel : headers) {
			Cell cell = row.createCell(colIndex++);
			cell.setCellValue(columnLabel);
		}
		
		 Iterator<String> teams = reportData.keySet().iterator();
	    Row xcelrowData;
	    while(teams.hasNext()) {
			// now populate actual data
		    	String teamName = (String)teams.next();
				for(String rowData : reportData.get(teamName)) {					
					String [] columns = rowData.split(",");
					xcelrowData = sheet.createRow(rowIndex++);
					colIndex = 0;
					for(String colData : columns) {
						Cell cellData = xcelrowData.createCell(colIndex++);
						int amountIndex = colIndex-1;
						if(amountColIndex.contains(amountIndex)){
	                        cellData.setCellStyle(style);
	                        totals.put(amountIndex, totals.get(amountIndex).add(new BigDecimal(colData.trim())));//subTotals
	                        totals.put(-amountIndex, totals.get(-amountIndex).add(new BigDecimal(colData.trim())));//grandTotals
	                        cellData.setCellValue(new BigDecimal(colData.trim()).doubleValue());
	                    }
						else {
							 cellData.setCellValue(colData.trim());
						}
						
					}
				}
		       xcelrowData = sheet.createRow(rowIndex++);//create row for subtotals
		       
		       for(int index = 0; index < headers.size(); index++) {
		    	   Cell rowCellTotal = xcelrowData.createCell(index);
		    	   if(amountColIndex.contains(index)){
		    		   rowCellTotal.setCellStyle(subTotalStyle);		    	      
		    	       rowCellTotal.setCellValue(totals.get(index).doubleValue());		    	      
		    	   }
		    	   else {
		    		   rowCellTotal.setCellValue(""); 
		    	   }
		       }
		       resetTotals(totals, amountColIndex);//reset totals for new row group
	    }
		xcelrowData = sheet.createRow(rowIndex++);
		xcelrowData = sheet.createRow(rowIndex++);
		
		 for(int index = 0; index < headers.size(); index++) {
	    	   Cell rowCellTotal = xcelrowData.createCell(index);
	    	   if(amountColIndex.contains(index)){
	    		   rowCellTotal.setCellStyle(totalsStyle);		    	      	    	       
	    	       rowCellTotal.setCellValue(totals.get(-index).doubleValue());
	    	   }
	    	   else {
	    		   rowCellTotal.setCellValue(""); 
	    	   }
       }
		
		workbook.write(file);
		workbook.close();
		return file;
		
	}
	
	private void resetTotals(Map<Integer, BigDecimal> totals, List<Integer> amountColIndex) {
		for(Integer index : amountColIndex) {
			totals.put(index, BigDecimal.ZERO);
		}
	}
	
	private void resetGrandTotals(Map<Integer, BigDecimal> totals, List<Integer> amountColIndex) {
		for(Integer grandTotalsIndex : amountColIndex) {
			totals.put(-grandTotalsIndex, BigDecimal.ZERO);
		}
	}
	
	//for testing only
	public static void main(String [] args) throws Exception{
		List<String> headers = new ArrayList<>();
		
			headers.add("Header 1");
			headers.add("Header 2");
			headers.add("Header 3");
			headers.add("Header 4");
			headers.add("Header 5");
			headers.add("Header 6");
			headers.add("Header 7");
			headers.add("Amount 8");
			headers.add("Amount 9");
			headers.add("Packages");
                        
		Map<String, String[]> reportData = new HashMap<>();
		String [] team1 = new String [5];
		
		team1 [0] = "row 1 header 1, row 1 header 2, row 1 header 3, row 1 header 4, row 1 header 5, row 1 header 6, row 1 header 7,45445.55, 2344334, some package 1";
		team1 [1] ="row 2 header 1, row 2 header 2, row 2 header 3, row 2 header 4, row 2 header 5, row 2 header 6, row 2 header 7,576454.55, 223.234, some package 2";
		team1 [2] ="row 3 header 3, row 3 header 2, row 3 header 3, row 3 header 4, row 3 header 5, row 3 header 6, row 3 header 7,2323.55, 2432.23, some package 3";
		team1 [3] ="row 4 header 1, row 1 header 2, row 1 header 3, row 1 header 4, row 1 header 5, row 4 header 6, row 4 header 7,343434.55, 24324.21, some package 4";
		team1 [4] = "row 5 header 1, row 5 header 2, row 5 header 3, row 5 header 4, row 5 header 5, row 5 header 6, row 5 header 7,5646.55, 5765765.12, some package 5";
		
		String []  team2 = new String [5] ;
		
		team2 [0] ="Team 2, row 1 header 2, row 1 header 3, row 1 header 4, row 1 header 5, row 1 header 6, row 1 header 7,3543.33, 233.22, some package 1";
		team2 [1] ="Team 2, row 2 header 2, row 2 header 3, row 2 header 4, row 2 header 5, row 2 header 6, row 2 header 7,343.33, -123.22, some package 2";
		team2 [2] ="Team 2, row 3 header 2, row 3 header 3, row 3 header 4, row 3 header 5, row 3 header 6, row 3 header 7,-3232.22, 2432.23, some package 3";
		team2 [3] ="Team 2, row 1 header 2, row 1 header 3, row 1 header 4, row 1 header 5, row 4 header 6, row 4 header 7,233.55, -34.23, some package 4";
		team2 [4] ="Team 2, row 5 header 2, row 5 header 3, row 5 header 4, row 5 header 5, row 5 header 6, row 5 header 7,5646.55, -433232.22, some package 5";
		
		reportData.put("Team 1", team1);
		reportData.put("Team 2", team2);
		List<Integer> indexes = new ArrayList<>();
		indexes.add(7);
		indexes.add(8);
		String regEx = ",";
		CSVInvoiceGen gen = new CSVInvoiceGen();
			gen.getExcelPhysicalFile(headers, reportData, indexes, regEx);
	}
	
	
	

}
