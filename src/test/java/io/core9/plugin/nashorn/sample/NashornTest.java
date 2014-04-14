package io.core9.plugin.nashorn.sample;

import io.core9.plugin.nashorn.LoadFile;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;



import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

public class NashornTest {

	public static void main(String[] args) {

		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		
		NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
	    //secure
		ScriptEngine sengine = factory.getScriptEngine(new String[] { "--no-java" });

		try {
			String js = LoadFile.loadFile("src/test/java/io/core9/plugin/nashorn/sample/NashornTest.js");
			
			// print needs io thus not working :-)
			String js2 = LoadFile.loadFile("src/test/java/io/core9/plugin/nashorn/sample/NashornTest2.js");

			if (js != null) {
				engine.eval(js);

				sengine.eval("var ph = 100;");
				sengine.eval(js2);
			    System.out.println(sengine.eval("sum2(1, 2);"));
				
				// no io requirred thus working
				sengine.eval("function sum(a, b) { return a + b; }");
			    System.out.println(sengine.eval("sum(1, 2);"));
				
				NashornTest.callJavascript(engine, "Die Methode 'calledByJava' wurde von Java aufgerufen!");
			} else {
				System.out.println("Java: JavaScript not found!");
				return;
			}
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	private static void callJavascript(ScriptEngine engine, String input) {
		Invocable inv = (Invocable) engine;
		try {
			System.out.println("Java: " + inv.invokeFunction("calledByJava", input));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	public static String calledByJavascript(String output) {
		System.out.println("Java: " + output);
		return "Der Aufruf war erfolgreich!";
	}


}