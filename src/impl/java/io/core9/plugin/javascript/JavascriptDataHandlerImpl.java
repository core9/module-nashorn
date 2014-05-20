package io.core9.plugin.javascript;

import io.core9.plugin.filesmanager.FileRepository;
import io.core9.plugin.server.request.Request;
import io.core9.plugin.server.vertx.VertxServer;
import io.core9.plugin.widgets.datahandler.DataHandler;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

@PluginImplementation
public class JavascriptDataHandlerImpl implements
		JavascriptDataHandler<JavascriptDataHandlerConfig> {

	@InjectPlugin
	private VertxServer server;

	@InjectPlugin
	private JavascriptModuleRegistry javascriptModuleRegistry;

	@InjectPlugin
	private FileRepository repository;

	@Override
	public String getName() {
		return "Javascript";
	}

	private JavascriptExecutor jsExecutor = JavascriptExecutor.getInstance();

	private Map<String, Object> result = new HashMap<String, Object>();

	@Override
	public Class<? extends DataHandlerFactoryConfig> getConfigClass() {
		return JavascriptDataHandlerConfig.class;
	}

	@SuppressWarnings("unused")
	@Override
	public DataHandler<JavascriptDataHandlerConfig> createDataHandler(
			final DataHandlerFactoryConfig options) {
		return new DataHandler<JavascriptDataHandlerConfig>() {

			@Override
			public Map<String, Object> handle(Request request) {

				jsExecutor.setFileRepository(repository);
				jsExecutor.setJavascriptModuleRegistry(javascriptModuleRegistry);

				JSONObject configuration = null;
				Map<String, Object> nashorn = new HashMap<String, Object>();
				Map<String, Object> file = getJavascriptFileFromMongoDb(options, request);
				JSONObject server = jsExecutor.getServerObject(request);
				String js = jsExecutor.evalJavascriptFromMongoDb(file);

				ConfigReader config = new ConfigReader();

				config.setJavascript(js);

				if (config.hasConfig()) {
					configuration = config.getConfiguration();
					for (Entry<String, Object> param : configuration.entrySet()) {

						if (param.getKey().equals("include")) {
							jsExecutor.importFiles(
									(JSONArray) param.getValue(), request);
						}

						if (param.getKey().equals("var")) {
							nashorn = jsExecutor.execVars((JSONArray) param
									.getValue());
						}

					}
				}

				result.put("javascript", nashorn);
				return result;
			}

			private Map<String, Object> getJavascriptFileFromMongoDb(
					final DataHandlerFactoryConfig options, Request req) {
				return repository.getFileContentsByName(req.getVirtualHost(),
						getOptions().getJsFile().substring(7));
			}

			@Override
			public JavascriptDataHandlerConfig getOptions() {
				return (JavascriptDataHandlerConfig) options;
			}
		};
	}

}
