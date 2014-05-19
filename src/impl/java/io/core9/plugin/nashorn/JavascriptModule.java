package io.core9.plugin.nashorn;

import net.minidev.json.JSONObject;

public interface JavascriptModule {

	String getName();
	void setName(String name);
	JSONObject getJson();
	void setJson(JSONObject json);
}
