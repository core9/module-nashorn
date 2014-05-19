package io.core9.plugin.javascript;

import io.core9.plugin.admin.plugins.AdminConfigRepository;
import io.core9.plugin.database.mongodb.MongoDatabase;
import io.core9.plugin.filesmanager.FileRepository;
import io.core9.plugin.server.request.Request;
import io.core9.plugin.server.vertx.VertxServer;
import io.core9.plugin.widgets.datahandler.DataHandler;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import com.google.common.io.ByteStreams;

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

	private NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
	// secure
	private ScriptEngine sengine = factory
			.getScriptEngine(new String[] { "--no-java" });

	private Map<String, Object> resultRegistry = new HashMap<>();

	private String js;

	@Override
	public Class<? extends DataHandlerFactoryConfig> getConfigClass() {
		return JavascriptDataHandlerConfig.class;
	}

	@SuppressWarnings("unused")
	@Override
	public DataHandler<JavascriptDataHandlerConfig> createDataHandler(
			final DataHandlerFactoryConfig options) {
		return new DataHandler<JavascriptDataHandlerConfig>() {

			private Map<String, Object> result = new HashMap<String, Object>();

			@Override
			public Map<String, Object> handle(Request req) {

				JSONObject configuration = null;
				Map<String, Object> nashorn = new HashMap<String, Object>();
				Map<String, Object> file = getJsFile(options, req);
				JSONObject server = getServerObject();
				evalJavascript(file);

				ConfigReader config = new ConfigReader();

				config.setJavascript(js);
	

				if (config.hasConfig()) {
					configuration = config.getConfiguration();
					for (Entry<String, Object> param : configuration.entrySet()) {

						if (param.getKey().equals("include")) {
							importFiles((JSONArray) param.getValue(), req);
						}

						System.out
								.println(System.getProperty("line.separator"));

						if (param.getKey().equals("var")) {
							execVars((JSONArray) param.getValue());
						}

					}
				}

				result.put("nashorn", nashorn);
				return result;
			}

			private JSONObject getServerObject() {
				JSONObject server = new JSONObject();
				JSONObject request = new JSONObject();
				request.put("path", "/nashorn");
				server.put("request", request);
				return server;
			}

			private String getContentFromFile(Map<String, Object> file) {
				String js = "";
				try {
					js = new String(ByteStreams.toByteArray((InputStream) file
							.get("stream")));
				} catch (IOException e) {
					e.printStackTrace();
				}

				return js;
			}

			private Map<String, Object> getJsFile(
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

	private void evalJavascript(Map<String, Object> file) {
		try {
			js = new String(ByteStreams.toByteArray((InputStream) file
					.get("stream")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			sengine.eval(js);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		
		
	}

	private void execVars(JSONArray vars) {
		System.out.println(vars);
		String name = "";
		String invoked = "";
		String arg = null;
		Object res = null;
		String invokeObject = "";

		Object object = null;

		for (Object var : vars) {

			JSONObject obj = (JSONObject) var;

			name = (String) obj.get("name");
			System.out.println(name);
			invoked = (String) obj.get("invoke");

			String[] invoke = invoked.split("\\.");
			if (invoke.length > 1) {
				invokeObject = invoke[0];
				invoked = invoke[1];
			}

			System.out.println(invoked);
			arg = (String) obj.get("arg");
			System.out.println(arg);

			Invocable invocable = (Invocable) sengine;

			if (invokeObject != null) {

				try {
					object = sengine.eval(invokeObject);
				} catch (ScriptException e) {
					e.printStackTrace();
				}
				res = invokeFunctionOnObject(object, invoked, arg, res,
						invocable);
			} else {
				res = invokeFunction(invoked, arg, res, invocable);
			}

			invokeObject = null;
			object = null;

			resultRegistry.put(name, res);
			System.out.println("Results : " + res);

		}

	}

	private Object invokeFunctionOnObject(Object object, String invoked,
			String arg, Object res, Invocable invocable) {

		try {
			String args = (String) resultRegistry.get(arg);
			if (args == null) {
				res = invocable.invokeMethod(object, invoked);
			} else {
				res = invocable.invokeMethod(object, invoked, args);
			}

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return res;
	}

	private Object invokeFunction(String invoked, String arg, Object res,
			Invocable invocable) {
		try {
			String args = (String) resultRegistry.get(arg);
			res = invocable.invokeFunction(invoked, args);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return res;
	}

	private void importFiles(JSONArray files, Request req) {

		for (Object file : files) {
			String path = (String) file;


			Map<String, Object> fileObj = repository.getFileContentsByName(
					req.getVirtualHost(), (String) file);

			localEvalJavascript(fileObj);

		}
	}

	private void localEvalJavascript(Map<String, Object> fileObj) {
		String js = "";
		try {
			js = new String(ByteStreams.toByteArray((InputStream) fileObj
					.get("stream")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			sengine.eval(js);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		
		
	}

}
