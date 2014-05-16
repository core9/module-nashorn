package io.core9.plugin.nashorn;

import java.util.Map;

import io.core9.core.plugin.Core9Plugin;

public interface JavascriptModuleRegistry extends Core9Plugin {



	void setModules(Map<String, JavascriptModule> modules);

	JavascriptModule getModule(String module);

}
