package io.core9.plugin.nashorn.nojava;


import io.core9.plugin.nashorn.LoadFile;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
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


		JSONObject server = new JSONObject();
		JSONObject request = new JSONObject();
		request.put("path", "/nashorn");
		server.put("request", request);

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

	private void getDatabaseResults(ScriptEngine sengine, Invocable invocable,
			Object preDatabase) {

		JSObject queries = null;
		try {
			queries = (JSObject) invocable.invokeFunction("databaseQueries",
					preDatabase);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		for (Object object : ((JSObject) queries).values()) {
			JSObject obj = (JSObject) object;
			for (String item : obj.keySet()) {
				System.out.println(item);
				System.out.println(obj.getMember(item));
			}

		}

	}

}
