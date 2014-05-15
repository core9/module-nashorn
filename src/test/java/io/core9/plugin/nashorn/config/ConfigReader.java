package io.core9.plugin.nashorn.config;

public class ConfigReader {

	private String javascript;

	public String getJavascript() {
		return javascript;
	}

	public void setJavascript(String javascript) {
		this.javascript = javascript;
	}

	public boolean hasConfig() {
		getConfig(javascript);
		return false;
	}

	private void getConfig(String js) {
		String[] lines = js.split(System.getProperty("line.separator"));

		String configString = "";

		boolean config = false;
		
		for(String line : lines){
			if(line.startsWith("{") && !config){
				System.out.println("start config");
				config = true;
			}
			
			if(config){
				System.out.println(line);
				configString += line + "\n";
			}
			
			if(line.startsWith("}") && config ){
				System.out.println("end config");
				config = false;
			}

			
		}
		

		System.out.println(configString);
		

	}


	
	
	
}
