package io.core9.plugin.nashorn.nojava;


import java.util.Collection;
import java.util.Set;

import io.core9.plugin.nashorn.LoadFile;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

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
		sengine.eval("server = {'path':'/nashorn'}");
		
		Object result = invocable.invokeFunction("preDatabase", "java");
		System.out.println(result);
		
		getDatabaseResults(sengine);

		
	}

	private void getDatabaseResults(ScriptEngine sengine) {
		Object json = sengine.get("databaseQueries");
		JSObject jsonObjFromJS = (JSObject)json;

		Collection<Object> arr = jsonObjFromJS.values();
		
		for (Object object : arr) {
			
			System.out.println(object);
			JSObject obj = (JSObject) object;
			System.out.println(obj);
			
			Set<String> test = obj.keySet();
			for(String item :test){
				System.out.println(item);
			}
			System.out.println(test);
			
		}
	}

}
