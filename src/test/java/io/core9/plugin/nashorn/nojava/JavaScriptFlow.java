package io.core9.plugin.nashorn.nojava;


import java.util.Map.Entry;

import io.core9.plugin.nashorn.LoadFile;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;


import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.junit.Test;

public class JavaScriptFlow {

	@Test
	public void test() throws ScriptException, NoSuchMethodException {
		NashornScriptEngineFactory factory = new NashornScriptEngineFactory();

		ScriptEngine sengine = factory
				.getScriptEngine(new String[] { "--no-java" });
		sengine.eval(LoadFile
				.loadFile("src/test/java/io/core9/plugin/nashorn/nojava/test.js"));
		Invocable invocable = (Invocable) sengine;


		JSONObject server = getServerObject();

		Object preDatabase = invocable.invokeFunction("preDatabaseFilter",
				server);
		System.out.println(preDatabase);

		getDatabaseResults(sengine, invocable, preDatabase);

		JSONArray jsonRes = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		JSONObject paul = new JSONObject();
		paul.put("name", "paul");
		jsonObj.put("Paul", paul);
		jsonRes.add(jsonObj);

		Object postDatabase = invocable.invokeFunction("postDatabaseFilter",
				jsonRes);
		System.out.println(postDatabase);

	}

	private JSONObject getServerObject() {
		JSONObject server = new JSONObject();
		JSONObject request = new JSONObject();
		request.put("path", "/nashorn");
		server.put("request", request);
		return server;
	}

	private JSONObject getDatabaseResults(ScriptEngine sengine, Invocable invocable,
			Object preDatabase) {
		JSONObject jsonObject = new JSONObject();
		ScriptObjectMirror queries = null;
		try {
			queries = (ScriptObjectMirror) invocable.invokeFunction("databaseQueries",
					preDatabase);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		for (Entry<String, Object> object : queries.entrySet()) {
			String key = object.getKey();
			String value = (String)object.getValue();
			System.out.println(key + " : " + value);
		}
		return jsonObject;
	}

}
