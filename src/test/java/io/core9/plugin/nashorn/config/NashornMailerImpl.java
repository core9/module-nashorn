package io.core9.plugin.nashorn.config;

import io.core9.plugin.nashorn.JavascriptModule;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;


public class NashornMailerImpl implements NashornMailer {
	private NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
	private ScriptEngine sengine = factory
			.getScriptEngine(new String[] { "--no-java" }); //Ensures that only javascript -> java is possible.

	private File jsJsonConf;
	private ConfigReader config;
	private NashornJSExecuter jsExecuter;
	
	public NashornMailerImpl(String jsJsonConfPath){
		jsJsonConf = new File(jsJsonConfPath);
		config = readConfig();
		jsExecuter = new NashornJSExecuter(sengine);
	}

	public ConfigReader readConfig() {
		JSONObject configuration = null;
		config = new ConfigReader();
		
		if (jsJsonConf.exists()) {
			config.setJavascript(readFile(jsJsonConf.getAbsolutePath(), StandardCharsets.UTF_8)); //Setting JSON source
			evalJsonConf(jsJsonConf);
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
				jsExecuter.execVars((JSONArray)param.getValue());
			}

		}
		return config;
	}

	private void importFiles(JSONArray files) {

		for(Object file : files){
			File jsFile = new File((String)file);

			if (jsFile.exists()) {
				evalJsonConf(jsFile);
			}
		}
	}

	private void evalJsonConf(File jsFile) {
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

	@Override
	public Map<String, JavascriptModule> getModules() {
		Map<String, JavascriptModule> modules = new HashMap<>();
		
		JavascriptModule javascriptMailer = new JavascriptMailerImpl();
		javascriptMailer.setName("javascriptMailer");
		modules.put(javascriptMailer.getName(), javascriptMailer);

		return modules;
	}
}