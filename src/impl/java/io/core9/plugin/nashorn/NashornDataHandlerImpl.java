package io.core9.plugin.nashorn;

import io.core9.plugin.admin.plugins.AdminConfigRepository;
import io.core9.plugin.server.request.Request;
import io.core9.plugin.server.vertx.VertxServer;
import io.core9.plugin.session.Session;
import io.core9.plugin.session.SessionManager;
import io.core9.plugin.statichandler.StaticHandler;
import io.core9.plugin.widgets.datahandler.DataHandler;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

@PluginImplementation
public class NashornDataHandlerImpl implements NashornDataHandler<NashornDataHandlerConfig> {
	
	@InjectPlugin
	private AdminConfigRepository configRepository;

	@InjectPlugin
	private SessionManager sessionManager;
	
	@InjectPlugin
	private VertxServer server;
	
	@InjectPlugin
	private StaticHandler handler;
	
	@Override
	public String getName() {
		return "Nashorn";
	}
	
	@Override
	public Class<? extends DataHandlerFactoryConfig> getConfigClass() {
		return NashornDataHandlerConfig.class;
	}

	@Override
	public DataHandler<NashornDataHandlerConfig> createDataHandler(final DataHandlerFactoryConfig options) {
		return new DataHandler<NashornDataHandlerConfig>() {

			private File tmp;
			private String js;

			@Override
			public Map<String, Object> handle(Request req) {
				
				//FIXME session in the menu widget is a quick fix for proper sessions need to be removed
				@SuppressWarnings("unused")
                Session session = sessionManager.getVertxSession(req, server);
				
				Map<String,Object> result = new HashMap<String,Object>();
				Map<String, Object> nashorn = configRepository.readConfig(req.getVirtualHost(), ((NashornDataHandlerConfig) options).getNashornID(req));
				if(nashorn == null) {
					nashorn = new HashMap<String,Object>();
					String id = ((NashornDataHandlerConfig) options).getNashornID(req);
					try {
	                    tmp = handler.getStaticFile(id);
	                    
                    } catch (IOException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
                    }
					
					 try {
	                   js = new Scanner( tmp).useDelimiter("\\A").next();
                    } catch (FileNotFoundException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
                    }
					
					nashorn.put("nnn", js);
				}
				result.put("nashorn", nashorn);
				return result;
			}

			@Override
			public NashornDataHandlerConfig getOptions() {
				return (NashornDataHandlerConfig) options;
			}
		};
	}
}
