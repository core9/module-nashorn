package io.core9.plugin.javascript;

import io.core9.plugin.filesmanager.FileRepository;
import io.core9.plugin.server.request.Request;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.google.common.io.ByteStreams;

public class JavascriptExecutor {

	private static JavascriptExecutor instance; // needs vhost scope
	private NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
	private ScriptEngine sengine = factory
			.getScriptEngine(new String[] { "--no-java" });
	private Invocable invocable = (Invocable) sengine;

	private Map<String, Object> resultRegistry = new HashMap<>();

	private FileRepository repository;
	private JavascriptModuleRegistry javascriptModuleRegistry;

	public Map<String, Object> execVars(JSONArray vars) {

		Object resultObject = null;

		System.out.println(vars);

		for (Object var : vars) {

			JSONObject variableJsonObject = (JSONObject) var;
			String methodArgument = (String) variableJsonObject.get("arg");
			String javascriptVariableName = (String) variableJsonObject.get("name");
			String invokeMethod = (String) variableJsonObject.get("invoke");
			String invokeObject = "";
			
			if(invokeMethod != null){
				resultObject = invokeJavascript(resultObject, methodArgument,
						invokeMethod, invokeObject);
			}else{
				resultObject = invokeModule();
			}

			String[] invoke = invokeMethod.split("\\.");
			if (invoke.length > 1) {
				invokeObject = invoke[0];
				invokeMethod = invoke[1];
			}

			


			System.out.println(javascriptVariableName);
			System.out.println(invokeMethod);
			System.out.println(methodArgument);
			System.out.println("Results : " + resultObject);

			resultRegistry.put(javascriptVariableName, resultObject);

		}
		return resultRegistry;

	}

	private Object invokeModule() {

		return null;
	}

	private Object invokeJavascript(Object resultObject, String methodArgument,
			String invokeMethod, String invokeObject) {
		if (invokeObject != "") {
			Object javascriptObject = null;
			try {
				javascriptObject = sengine.eval(invokeObject);
			} catch (ScriptException e) {
				e.printStackTrace();
			}
			resultObject = invokeFunctionOnObject(javascriptObject,
					invokeMethod, methodArgument, resultObject, invocable);
		} else {
			resultObject = invokeFunction(invokeMethod, methodArgument,
					resultObject, invocable);
		}
		return resultObject;
	}

	private JavascriptExecutor() {
	}

	public JSONObject getServerObject(Request request) {
		JSONObject result = new JSONObject();
		JSONObject requestJson = new JSONObject();
		requestJson.put("path", "/nashorn");
		result.put("request", requestJson);
		return result;
	}

	public String evalJavascriptFromMongoDb(Map<String, Object> file) {
		String js = null;
		try {
			js = new String(ByteStreams.toByteArray((InputStream) file
					.get("stream")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return evalJavascript(js);
	}

	private String evalJavascript(String js) {
		try {
			sengine.eval(js);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return js;
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

	public void importFiles(JSONArray files, Request req) {
		for (Object file : files) {
			evalJavascriptFromMongoDb(repository.getFileContentsByName(
					req.getVirtualHost(), (String) file));
		}
	}

	public static JavascriptExecutor getInstance() {
		if (instance == null) {
			instance = new JavascriptExecutor();
		}
		return instance;
	}

	public void setFileRepository(FileRepository repository) {
		this.repository = repository;
	}

	public void setJavascriptModuleRegistry(
			JavascriptModuleRegistry javascriptModuleRegistry) {
		this.javascriptModuleRegistry = javascriptModuleRegistry;
	}

}
