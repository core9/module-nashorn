package io.core9.plugin.nashorn.config;


import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class ConfigReader {

	private String javascript;
	private JSONObject configuration;

	public String getJavascript() {
		return javascript;
	}

	public void setJavascript(String javascript) {
		extractConfig(javascript);
		this.javascript = javascript;
	}

	public boolean hasConfig() {
		if (configuration.isEmpty()) {
			return false;
		}
		return true;
	}
	
	public JSONObject getConfiguration(){
		return configuration;
	}

	private JSONObject extractConfig(String js) {
		String[] lines = js.split(System.getProperty("line.separator"));
		String configString = "";
		boolean config = false;


		
		for (String line : lines) {
			if (line.startsWith("/**") && !config) {
				config = true;
			}

			if (config && !line.startsWith("/**") && !line.startsWith("**/")) {
				configString += line + "\n";
			}

			if (line.startsWith("**/") && config) {
				config = false;
			}

		}
		
		
		
		

		if (JSONValue.isValidJsonStrict(configString)) {
			configuration = (JSONObject) JSONValue.parse(configString);
			return configuration;
		} else {
			configuration = (JSONObject) JSONValue.parse("{}");
			return configuration;
		}

	}

}
