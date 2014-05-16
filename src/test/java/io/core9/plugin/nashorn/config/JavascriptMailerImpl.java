package io.core9.plugin.nashorn.config;

import io.core9.plugin.nashorn.JavascriptModule;

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

}
