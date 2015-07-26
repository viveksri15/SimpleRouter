package in.viveksrivastava.simplerouter;

import in.viveksrivastava.simplerouter.routes.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by viveksrivastava on 25/07/15.
 * <p/>
 */
public class RouteFileParser {
	public RouteBuilder parse(File file) {
		try {
			String className = FileUtils.readLines(file).get(0);
			@SuppressWarnings("unchecked") Class<Route> builderClass = (Class<Route>) Class.forName(className.replaceAll("^#!", ""));
			Route route = builderClass.newInstance();
			PropertiesConfiguration configuration = new PropertiesConfiguration(file);
			configuration.setReloadingStrategy(new FileChangedReloadingStrategy());
			route.setConfig(configuration);
			return route;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
