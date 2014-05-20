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

	private NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
	// secure
	private ScriptEngine sengine = factory
			.getScriptEngine(new String[] { "--no-java" });

	private Map<String, Object> resultRegistry = new HashMap<>();
	private static JavascriptExecutor instance;
	private String js;
	private FileRepository repository;
	
	public JSONObject getServerObject() {
		JSONObject server = new JSONObject();
		JSONObject request = new JSONObject();
		request.put("path", "/nashorn");
		server.put("request", request);
		return server;
	}

	public String evalJavascript(Map<String, Object> file) {
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
		return js;
	}

	public Map<String, Object> execVars(JSONArray vars) {
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
		return resultRegistry;

	}

	public Object invokeFunctionOnObject(Object object, String invoked,
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

	public Object invokeFunction(String invoked, String arg, Object res,
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
			evalJavascript(repository.getFileContentsByName(
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

}
