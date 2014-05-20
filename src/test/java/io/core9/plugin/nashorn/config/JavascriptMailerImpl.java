package io.core9.plugin.nashorn.config;

import net.minidev.json.JSONObject;
import io.core9.plugin.javascript.JavascriptModule;

public class JavascriptMailerImpl implements JavascriptModule {

	private String name;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public JSONObject getJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setJson(JSONObject json) {
		// TODO Auto-generated method stub
		
	}

}
