package base.helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;

import base.utils.SystemUtil;

public class PropertiesHelper {
	private static Properties properties;
	private static String defaultFilePath = "src/test/resources/default_configs.properties";

	public static Properties loadAllProperties() {
		LinkedList<String> allPaths = new LinkedList<>();
		allPaths.add(defaultFilePath);

		// Load all properties in all files into the properties param
		try {
			properties = new Properties();
			for (String filePath : allPaths) {
				filePath = SystemUtil.createFilePath(filePath);
				Properties tempProperties = new Properties();

				try (FileInputStream file = new FileInputStream(filePath)) {
					tempProperties.load(file);
					properties.putAll(tempProperties);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties;
	}

	public static void loadFromFile(String filePath) {
		properties = new Properties();
		filePath = SystemUtil.createFilePath(filePath);
		try (FileInputStream file = new FileInputStream(filePath)) {
			properties.load(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void loadFromDefaultFile() {
		properties = new Properties();
		String filePath = SystemUtil.createFilePath(defaultFilePath);
		try (FileInputStream file = new FileInputStream(filePath)) {
			properties.load(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setProperties(String key, String value) {

		// Initiate properties object and file path
		try {
			properties = new Properties();
			String filePath = SystemUtil.createFilePath(defaultFilePath);

			// Read file content into properties
			try (FileInputStream input = new FileInputStream(filePath)) {
				properties.load(input);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Update or add new properties
			properties.setProperty(key, value);
			
			// Save content to file
			try (FileOutputStream output = new FileOutputStream(filePath)) {
				properties.store(output, null);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			 System.err.println("Error while setting property value: " + e.getMessage());
		}
	}
	
	public static String getValue(String key) {
		String value = null;
		
		// Get value from properties
		try {
			if(properties == null) {
				properties = new Properties();
				String filePath = SystemUtil.createFilePath(defaultFilePath);
				try (FileInputStream input = new FileInputStream(filePath)) {
					properties.load(input);
				}
			}
			
			// Get value of key from properties
			value = properties.getProperty(key);
			if (value == null) {
				System.out.println("Key '" + key + "' not found in properties file.");
			}
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("Error reading properties file: " + e.getMessage());
		}
		return value;
	}
}
