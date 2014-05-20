package io.core9.plugin.javascript;

import io.core9.core.plugin.Core9Plugin;
import io.core9.plugin.widgets.datahandler.DataHandlerFactory;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;

public interface JavascriptDataHandler<T extends DataHandlerFactoryConfig> extends DataHandlerFactory<T>, Core9Plugin {

}
