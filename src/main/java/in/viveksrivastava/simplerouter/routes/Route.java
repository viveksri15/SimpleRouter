package in.viveksrivastava.simplerouter.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Created by viveksrivastava on 25/07/15.
 */
public abstract class Route extends RouteBuilder {
	private PropertiesConfiguration propertiesConfiguration;

	public PropertiesConfiguration getConfig() {
		return propertiesConfiguration;
	}

	public void setConfig(PropertiesConfiguration configuration) {
		propertiesConfiguration = configuration;
	}
}
