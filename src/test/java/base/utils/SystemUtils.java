package base.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SystemUtils {
	
	public static String getCurrentDir() {
		String currentDir = System.getProperty("user.dir") + File.separator;
		return currentDir;
	}
	
	public static String createFilePath(String path) {
		Path filePath = Paths.get(getCurrentDir());
		filePath = filePath.resolve(path);
		return filePath.toString();
	}
}
