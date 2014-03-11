package io.core9.plugin.nashorn;

import io.core9.plugin.server.request.Request;
import io.core9.plugin.widgets.Core9GlobalConfiguration;
import io.core9.plugin.widgets.datahandler.DataHandlerDefaultConfig;
import io.core9.plugin.widgets.datahandler.DataHandlerGlobalString;

public class NashornDataHandlerConfig extends DataHandlerDefaultConfig {
	
	@Core9GlobalConfiguration(type = "nashorn")
	private DataHandlerGlobalString nashornID;

	/**
	 * @return the menuName
	 */
	public DataHandlerGlobalString getNashornID() {
		return nashornID;
	}
	
	/**
	 * Return the nashornID from a global
	 * @param request
	 * @return
	 */
	public String getNashornID(Request request) {
		if(nashornID.isGlobal()) {
			return request.getContext(this.getComponentId() + ".nashornID", nashornID.getValue());
		}
		return nashornID.getValue();
	}

	/**
	 * @param menuName the menuName to set
	 */
	public void setNashornID(DataHandlerGlobalString nashornID) {
		this.nashornID = nashornID;
	}

}
