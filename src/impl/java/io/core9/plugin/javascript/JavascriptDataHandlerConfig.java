package io.core9.plugin.javascript;

import io.core9.plugin.widgets.Core9File;
import io.core9.plugin.widgets.datahandler.DataHandlerDefaultConfig;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;

public class JavascriptDataHandlerConfig extends DataHandlerDefaultConfig implements DataHandlerFactoryConfig {
	
	@Core9File
	private String jsFile;

	public String getJsFile() {
		return jsFile;
	}

	public void setJsFile(String jsFile) {
		this.jsFile = jsFile;
	}


}
