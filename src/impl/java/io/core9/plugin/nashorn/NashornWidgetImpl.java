package io.core9.plugin.nashorn;


import java.io.IOException;
import java.io.InputStreamReader;

import com.google.common.io.CharStreams;

import io.core9.plugin.widgets.datahandler.DataHandler;
import io.core9.plugin.widgets.datahandler.DataHandlerGlobalString;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;


@PluginImplementation
public class NashornWidgetImpl implements NashornWidget {

	@InjectPlugin
	private NashornDataHandler<NashornDataHandlerConfig> nashornDataHandler;
	
	private DataHandler<NashornDataHandlerConfig> handler;
	
	@Override
	public DataHandler<?> getDataHandler() {
		return handler;
	}

	@Override
    public void execute() {
		NashornDataHandlerConfig options = new NashornDataHandlerConfig();
		DataHandlerGlobalString NashornId = new DataHandlerGlobalString();
		NashornId.setGlobal(true);
		options.setNashornID(NashornId);
		handler = nashornDataHandler.createDataHandler(options);
    }


	@Override
    public String getName() {
	    return "nashorn_js";
    }

	@Override
    public String getTemplate() {
		try {
			return CharStreams.toString(new InputStreamReader(this.getClass().getResourceAsStream("/nashorn/template.soy")));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    }

	@Override
    public String getTemplateName() {
		return "io.core9.nashorn.script";
    }
}
