package io.core9.plugin.nashorn.log;

import net.minidev.json.JSONObject;
import io.core9.plugin.javascript.JavascriptModule;

public class LoggerImpl implements JavascriptModule {

	private JSONObject json;

	@Override
	public String getName() {
		return "logger";
	}

	@Override
	public void setName(String name) {
	}

	@Override
	public JSONObject getJson() {
		json.put("mmm", "rrrr");
		return json;
	}

	@Override
	public void setJson(JSONObject json) {
		this.json = json;
	}


	
}
