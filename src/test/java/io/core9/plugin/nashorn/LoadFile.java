package io.core9.plugin.nashorn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadFile {
	public static String loadFile(String path) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

			String line = "";
			StringBuffer sb = new StringBuffer();

			do {
				line = bufferedReader.readLine();
				if (line != null && !line.isEmpty()) {
					sb.append(line + System.getProperty("line.separator"));
				}
			} while (line != null);

			bufferedReader.close();

			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
