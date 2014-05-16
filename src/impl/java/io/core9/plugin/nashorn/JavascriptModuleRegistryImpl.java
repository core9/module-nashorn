package io.core9.plugin.nashorn;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class JavascriptModuleRegistryImpl implements JavascriptModuleRegistry {

	private Map<String, JavascriptModule> modules = new HashMap<>();

	public Map<String, JavascriptModule> getModules() {
		return modules;
	}

	@Override
	public void setModules(Map<String, JavascriptModule> modules) {
		if (modules != null) {
			modules.putAll(modules);
		}
	}

	@Override
	public JavascriptModule getModule(String module) {
		return modules.get(module);
	}

}
