package io.core9.plugin.nashorn.log;

import io.core9.plugin.javascript.JavascriptModule;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;


@PluginImplementation
public class LogProviderImpl implements LogProvider {

	@Override
	public Map<String, JavascriptModule> getModules() {
		
		Map<String, JavascriptModule> modules = new HashMap<>();
		
		JavascriptModule logger = new LoggerImpl();
		
		modules.put(logger.getName(), logger);
		
		return modules ;
	}

}
