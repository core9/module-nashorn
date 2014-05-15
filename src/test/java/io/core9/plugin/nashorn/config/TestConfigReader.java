package io.core9.plugin.nashorn.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.junit.Test;

public class TestConfigReader {
	
	private NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
	private ScriptEngine sengine = factory
			.getScriptEngine(new String[] { "--no-java" });
	private Map<String, Object>resultRegistry = new HashMap<>();

	@Test
	public void readConfig() {

		JSONObject configuration = null;

		ConfigReader config = new ConfigReader();

		File jsFile = new File(
				"src/test/resources/io/core9/plugin/nashorn/config/file1.js");

		if (jsFile.exists()) {
			config.setJavascript(readFile(jsFile.getAbsolutePath(), StandardCharsets.UTF_8));
			evalJs(jsFile);
		}
		
		if(config.hasConfig()){
			configuration = config.getConfiguration();
		}

		for(Entry<String, Object> param : configuration.entrySet()){

			
			if(param.getKey().equals("include")){
				importFiles((JSONArray)param.getValue());
			}

			System.out.println(System.getProperty("line.separator"));

			
			if(param.getKey().equals("var")){
				execVars((JSONArray)param.getValue());
			}

		}
		
	}

	private void execVars(JSONArray vars) {
		System.out.println(vars);
		String name = "";
		String invoked = "";
		String arg = null;
		Object res = null;
		

		
		for(Object var : vars){
			
			JSONObject obj = (JSONObject)var;
			
			name = (String) obj.get("name");
			System.out.println(name);
			invoked = (String) obj.get("invoke");
			System.out.println(invoked);
			arg = (String) obj.get("arg");
			System.out.println(arg);
			
			Invocable invocable = (Invocable) sengine;
			try {
				String args = (String) resultRegistry.get(arg);
				res = invocable.invokeFunction(
						invoked, args);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (ScriptException e) {
				e.printStackTrace();
			}
			resultRegistry.put(name, res);
			System.out.println("Results : " + res);
			
		}
		

	}

	private void importFiles(JSONArray files) {
		
		for(Object file : files){
			File jsFile = new File((String)file);

			if (jsFile.exists()) {
				evalJs(jsFile);
			}
		}
	}

	private void evalJs(File jsFile) {
		try {
			sengine.eval(readFile(jsFile.getAbsolutePath(), StandardCharsets.UTF_8));
		} catch (ScriptException e) {
			e.printStackTrace();
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
