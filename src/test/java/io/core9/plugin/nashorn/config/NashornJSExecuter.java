package io.core9.plugin.nashorn.config;

import java.util.HashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class NashornJSExecuter {
	private Map<String, Object>resultRegistry = new HashMap<>();
	private ScriptEngine sengine;
	
	public NashornJSExecuter(ScriptEngine scriptEngine){
		sengine = scriptEngine;
	}
			
			
	public void execVars(JSONArray vars) {
		System.out.println(vars);
		String name = "";
		String invoked = "";
		String arg = null;
		Object res = null;
		String invokeObject = "";

		Object object = null;

		for(Object var : vars){

			JSONObject obj = (JSONObject)var;

			name = (String) obj.get("name");
			System.out.println(name);
			invoked = (String) obj.get("invoke");

			String[] invoke = invoked.split("\\.");
			if(invoke.length > 1){
				invokeObject = invoke[0];
				invoked = invoke[1];
			}

			System.out.println(invoked);
			arg = (String) obj.get("arg");
			System.out.println(arg);

			Invocable invocable = (Invocable) sengine;

			if(invokeObject != null){

				try {
					object = sengine.eval(invokeObject);
				} catch (ScriptException e) {
					e.printStackTrace();
				}
				res = invokeFunctionOnObject(object, invoked, arg, res, invocable);
			}else{
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
			if(args == null){
				res = invocable.invokeMethod(object, invoked);
			}else{
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
			res = invocable.invokeFunction(
					invoked, args);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return res;
	}
}
