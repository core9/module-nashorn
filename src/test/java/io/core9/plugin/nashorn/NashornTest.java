package io.core9.plugin.nashorn;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

public class NashornTest {

	public static void main(String[] args) {

		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		
		NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
	    //secure
		ScriptEngine sengine = factory.getScriptEngine(new String[] { "--no-java" });

		try {
			String js = NashornTest.loadFile("src/test/java/io/core9/plugin/nashorn/NashornTest.js");
			
			// print needs io thus not working :-)
			String js2 = NashornTest.loadFile("src/test/java/io/core9/plugin/nashorn/NashornTest2.js");

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

	private static String loadFile(String path) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

			String line = "";
			StringBuffer sb = new StringBuffer();

			do {
				line = bufferedReader.readLine();
				if (line != null && !line.isEmpty()) {
					sb.append(line + System.getProperty("line.separator"));
				}
			} while (line != null);

			bufferedReader.close();

			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}