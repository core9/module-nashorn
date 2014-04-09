package io.core9.plugin.nashorn;

import io.core9.plugin.admin.plugins.AdminConfigRepository;
import io.core9.plugin.filesmanager.FileRepository;
import io.core9.plugin.server.request.Request;
import io.core9.plugin.server.vertx.VertxServer;
import io.core9.plugin.widgets.datahandler.DataHandler;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import com.google.common.io.ByteStreams;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

@PluginImplementation
public class NashornDataHandlerImpl implements NashornDataHandler<NashornDataHandlerConfig> {

	@InjectPlugin
	private AdminConfigRepository configRepository;



	@InjectPlugin
	private VertxServer server;

	@InjectPlugin
	private FileRepository repository;

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

/*			private File tmp;
			
			*/
			private Object data;
			private String js;

			@Override
			public Map<String, Object> handle(Request req) {



				Map<String, Object> result = new HashMap<String, Object>();
				Map<String, Object> nashorn = configRepository.readConfig(req.getVirtualHost(), ((NashornDataHandlerConfig) options).getNashornID(req));
				if (nashorn == null) {
					nashorn = new HashMap<String, Object>();
					String id = ((NashornDataHandlerConfig) options).getNashornID(req);
				
					Map<String,Object> file = repository.getFileContentsByName(req.getVirtualHost(), id);
					
					try {
						byte[] str = ByteStreams.toByteArray((InputStream) file.get("stream"));
						
						js = new String(str);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					/*try {
						js = new Scanner(tmp).useDelimiter("\\A").next();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
	
					NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
					// secure
					ScriptEngine sengine = factory.getScriptEngine(new String[] { "--no-java" });

					if (js != null) {
						try {
	                        sengine.eval(js);
                        } catch (ScriptException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
                        }
					    try {
					    	data = sengine.eval("response();");
	                        System.out.println(data);
                        } catch (ScriptException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
                        }
						
					} else {
						System.out.println("Java: JavaScript not found!");
					}
					
					nashorn.put("nashorn", js + data);
					
					
					//nashorn.put("nashorn", "test");
					
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
