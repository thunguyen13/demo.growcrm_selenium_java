package base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtils {

	private Workbook wb;
	private Sheet sh;
	private String excelFilePath;
	public Map<String, Integer> columns = new HashMap<String, Integer>();
 	
	public void setExcelFile(String filePath, String sheetName) throws FileNotFoundException, IOException {
		excelFilePath = filePath;
		try(FileInputStream fis = new FileInputStream(excelFilePath)){
			wb = WorkbookFactory.create(fis);
			sh = wb.getSheet(sheetName);
			
			//Check if sheet exists or not
			if(sh == null) {
				throw new IOException("Sheet: " + sheetName + "not found!");
			}
			
			//Add columns headers to Map 'columns'
			sh.getRow(0).forEach(cell -> columns.put(cell.getStringCellValue(), cell.getColumnIndex()));
		}
	}
	
	public Object[][] getExcelDataArray(){
		int totalRows = sh.getLastRowNum(); //if has 4 rows, return 3
		int totalCols = sh.getRow(0).getLastCellNum(); //if has 3 cells, return 3
		
		Object[][] data = new Object[totalRows][totalCols];
		
		System.out.println("last rows: " + totalRows + " | total cols: " + totalCols + " | object length: " + data.length);
		
		//Read data into a two-dimensional array: i represent index of row, j represent index of cell in each row
		// i,j < totalRows,totalCols bcs i,j start from 0
		for(int i = 0; i < totalRows; i++) {
			for(int j = 0; j < totalCols; j++) {
				data[i][j] = getCellData(i + 1,j); // +1 bcs skip the first row [0]
			}
		}
		
		return data;
	}
	
	public Iterator<Object[]> getExcelDataIterator(){
		Iterator<Row> rowIterator = sh.iterator();
		
		//Skip the first row - header
		if(rowIterator.hasNext()) {
			rowIterator.next();
		}
		
		return new Iterator<Object[]>() {
			int totalCols = columns.size();
			
			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return rowIterator.hasNext();
			}
			@Override
			public Object[] next() {
				// TODO Auto-generated method stub
				//throw error when try to read next row if there is no next row
				if(!rowIterator.hasNext()) {
					throw new NoSuchElementException("No more data in Excel sheet");
				}
				
				Row row = rowIterator.next();
				int rowNum = row.getRowNum();
				//int totalCols = row.getLastCellNum(); //if there is 3 cols, return 3 and it will be flexible according to cell has data in each row
				Object[] data = new Object[totalCols];
				
				//Read data into an object array for each row: i represent index of cell
				//i < totalCols bcs i start from 0
				for(int i = 0; i < totalCols; i++) {
					data[i] = getCellData(rowNum, i);
				}
				
				return data;
			}
			
		};
	}
	
	public Object getCellData(int rowNum, int colNum) {
		Row row = sh.getRow(rowNum);
		Cell cell = row.getCell(colNum);
		if(cell == null) {
			return "";
		}
		
		switch (cell.getCellType()) {
			case STRING:
				return cell.getStringCellValue();
			case NUMERIC:
				if(DateUtil.isCellDateFormatted(cell)) {
					return cell.getDateCellValue();
				}else {
					return cell.getNumericCellValue();
				}
			case BOOLEAN:
				return cell.getBooleanCellValue();
			case ERROR:
				return cell.getErrorCellValue();
			case FORMULA:
				try {
					return cell.getStringCellValue();
				} catch (IllegalStateException  e) {
					return cell.getNumericCellValue();
				}
			case BLANK:
				return "";
			default:
				return cell.toString();
		}
	}
	
	
	
}
