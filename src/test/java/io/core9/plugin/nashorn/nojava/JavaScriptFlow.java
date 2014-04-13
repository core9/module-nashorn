package io.core9.plugin.nashorn.nojava;

//import static org.junit.Assert.*;
import io.core9.plugin.nashorn.LoadFile;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

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
		
		//Object result = sengine.eval("preDatabase()");


		System.out.println(result);
		
	}

}
