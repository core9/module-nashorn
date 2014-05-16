package io.core9.plugin.nashorn;

import java.util.List;
import org.apache.commons.lang3.ClassUtils;
import io.core9.core.boot.CoreBootStrategy;
import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

@PluginImplementation
public class JavascriptModulesBootStrategyImpl extends CoreBootStrategy
		implements JavascriptModulesBootStrategy {

	@InjectPlugin
	private JavascriptModuleRegistry javascriptModuleRegistry;

	@Override
	public void processPlugins() {
		for (Plugin plugin : this.registry.getPlugins()) {
			List<Class<?>> interfaces = ClassUtils.getAllInterfaces(plugin
					.getClass());
			if (interfaces.contains(JavascriptModuleProvider.class)) {
				javascriptModuleRegistry
						.setModules(((JavascriptModuleProvider) plugin)
								.getModules());
			}

		}
	}

	@Override
	public Integer getPriority() {
		return 400;
	}

}
