package io.core9.plugin.nashorn;

public class JavascriptModuleImpl implements JavascriptModule{

	private String name;
	
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name){
		this.name = name;
	}
}
