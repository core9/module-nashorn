package io.core9.plugin.nashorn.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

public class TestConfigReader {

	@Test
	public void readConfig() {

		ConfigReader config = new ConfigReader();

		File jsFile = new File(
				"src/test/resources/io/core9/plugin/nashorn/config/file1.js");

		if (jsFile.exists()) {
			config.setJavascript(readFile(jsFile.getAbsolutePath(), StandardCharsets.UTF_8));
		}
		
		if(config.hasConfig()){
			
		}

	}

	private static String readFile(String path, Charset encoding) {
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(encoded, encoding);
	}
}
