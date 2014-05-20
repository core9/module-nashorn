package io.core9.plugin.javascript;

import io.core9.core.plugin.Core9Plugin;

import java.util.Map;

public interface JavascriptModuleProvider extends Core9Plugin {

	Map<String, JavascriptModule> getModules();
	
}
