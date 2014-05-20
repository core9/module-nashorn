package io.core9.plugin.javascript;

import net.minidev.json.JSONObject;

public class JavascriptModuleImpl implements JavascriptModule{

	private String name;
	private JSONObject json;
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name){
		this.name = name;
	}

	@Override
	public JSONObject getJson() {
		return json;
	}

	@Override
	public void setJson(JSONObject json) {
		this.json = json;
	}
}
