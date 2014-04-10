package io.core9.plugin.nashorn;

import io.core9.plugin.admin.plugins.AdminConfigRepository;
import io.core9.plugin.database.mongodb.MongoDatabase;
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

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

@PluginImplementation
public class NashornDataHandlerImpl implements
		NashornDataHandler<NashornDataHandlerConfig> {

	@InjectPlugin
	private AdminConfigRepository configRepository;

	@InjectPlugin
	private VertxServer server;
	
	@InjectPlugin
	private MongoDatabase database;

	@InjectPlugin
	private FileRepository repository;

	@Override
	public String getName() {
		return "Nashorn";
	}

	private NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
	// secure
	private ScriptEngine sengine = factory
			.getScriptEngine(new String[] { "--no-java" });

	@Override
	public Class<? extends DataHandlerFactoryConfig> getConfigClass() {
		return NashornDataHandlerConfig.class;
	}

	@Override
	public DataHandler<NashornDataHandlerConfig> createDataHandler(
			final DataHandlerFactoryConfig options) {
		return new DataHandler<NashornDataHandlerConfig>() {

			private Object data;
			private String js;
			private Object request;
			private Object execute;
			private Object response;
			private Object requestObject;
			private Object reqObj;

			@Override
			public Map<String, Object> handle(Request req) {

				Map<String, Object> result = new HashMap<String, Object>();
				Map<String, Object> nashorn = configRepository.readConfig(
						req.getVirtualHost(),
						((NashornDataHandlerConfig) options).getNashornID(req));
				if (nashorn == null) {
					nashorn = new HashMap<String, Object>();
					String id = ((NashornDataHandlerConfig) options)
							.getNashornID(req);

					Map<String, Object> file = repository
							.getFileContentsByName(req.getVirtualHost(), id);

					try {
						js = new String(
								ByteStreams.toByteArray((InputStream) file
										.get("stream")));
					} catch (IOException e) {
						e.printStackTrace();
					}

					try {
						sengine.eval(js);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try {
/*						
						db = (String) vhost.getContext("database");
						coll = (String) vhost.getContext("prefix") + contenttype;
						query =  new HashMap<String,Object>();
						database.getMultipleResults(db, coll, query, fields); 
						*/
						
						
						requestObject = sengine.eval("requestObject");

						reqObj = sengine.eval("reqObj");

						JSONObject obj = (JSONObject) JSONValue
								.parse((String) reqObj);

						request = sengine.eval("request();");
						

						sengine.eval("request = ' " + request + " : someJson "
								+ requestObject.toString() + " ';");

						response = sengine.eval("response();");

						data = obj;

						System.out.println(data);
					} catch (ScriptException e) {
						e.printStackTrace();
					}

					nashorn.put("nashorn", data);

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
