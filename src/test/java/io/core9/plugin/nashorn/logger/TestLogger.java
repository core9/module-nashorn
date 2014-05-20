package io.core9.plugin.nashorn.logger;

import java.util.Map;
import java.util.Map.Entry;

import io.core9.plugin.javascript.JavascriptModule;
import io.core9.plugin.nashorn.log.LogProviderImpl;
import net.minidev.json.JSONObject;

import org.junit.Test;

public class TestLogger {
	
	
	@SuppressWarnings("unused")
	@Test
	public void testLogger(){
		
		LogProviderImpl logProvider = new LogProviderImpl();
		
		Map<String, JavascriptModule> modules = logProvider.getModules();
		
		
		System.out.println(modules);
		
		for( Entry<String, JavascriptModule> entry : modules.entrySet()){
			
			JavascriptModule module = entry.getValue();
			
			JSONObject json = new JSONObject();
			
			json.put("info", "sending mail");
			
			module.setJson(json);
			
			JSONObject result = module.getJson();
			
			
			
		}
		
		
	}
	

}
