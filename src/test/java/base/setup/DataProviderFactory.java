package base.setup;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.testng.annotations.DataProvider;

import base.helpers.ExcelHelper;

public class DataProviderFactory {

	String signUp_filePath = "D:\\code\\code_maven\\Demo_demo.growcrm\\src\\test\\resources\\SignUp_data.xlsx";
	
	@DataProvider(name = "signInData_invalid")
	public Object[][] dataTest_signInData_invalid(){
		return new Object[][] {
			//valid email, invalid password
			{"admin@example.com", "growcrm123", "remember"},
			//invalid email, valid password
			{"admin1@example.com", "growcrm", "noRemember"},
			//invalid email, invalid password
			{"admin1@example.com", "growcrm123", "remember"},
			//email has blank space, valid password
			{"admin @example.com", "growcrm", "noRemember"},
			//email lack of domain, valid password
			{"admin", "growcrm", "remember"},
			//email lack of name, valid password
			{"@example.com", "growcrm", "noRemember"},
			//valid email, password in CAPS LOCK
			{"admin@example.com", "GROWCRM", "remember"},
			//email in CAPS LOCK, password in CAPS LOCK
			{"ADMIN@EXAMPLE.COM", "GROWCRM", "noRemember"}
		};
	}
	
	@DataProvider(name = "signInData_blank")
	public Object[][] dataTest_signInData_blank(){
		return new Object[][] {
			//valid email, blank password
			{"admin@example.com", "", "noRemember"},
			//blank email, valid password
			{"", "growcrm", "remember"},
			//blank email, invalid password
			{"", "growcrm123", "noRemember"},
			//blank email, blank password
			{"", "", "remember"}
		};
	}
	
	@DataProvider(name = "signInData_valid")
	public Object[][] dataTest_signInData_valid(){
		return new Object[][] {
			//valid email, valid password
			{"admin@example.com", "growcrm", "noRemember"},
			//email in CAPS LOCK, valid password
			{"ADMIN@EXAMPLE.COM", "growcrm", "remember"}
		};
	}
	
	@DataProvider(name = "signUpData_invalid")
	public Object[][] dataTest_signUpData_invalid() throws FileNotFoundException, IOException{
		Object[][] data;
		ExcelHelper excel = new ExcelHelper();
		excel.setExcelFile(signUp_filePath, "invalid");
		data = excel.getExcelDataArray();
		return data;
	}
	
	@DataProvider(name = "signUpData_invalid_i")
	public Iterator<Object[]> dataTest_signUpData_invalid_i() throws FileNotFoundException, IOException{
		Iterator<Object[]> data;
		ExcelHelper excel = new ExcelHelper();
		excel.setExcelFile(signUp_filePath, "invalid");
		data = excel.getExcelDataIterator();
		return data;
	}
	
}
