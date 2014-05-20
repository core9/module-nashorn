package io.core9.plugin.javascript;

import net.minidev.json.JSONObject;

public interface JavascriptModule {

	String getName();
	void setName(String name);
	JSONObject getJson();
	void setJson(JSONObject json);
}
