package io.core9.plugin.nashorn;

import java.util.Map;

public interface JavascriptModuleProvider {

	Map<String, JavascriptModule> getModules();
	
}
