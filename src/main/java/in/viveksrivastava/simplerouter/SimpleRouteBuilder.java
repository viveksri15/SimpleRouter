package in.viveksrivastava.simplerouter;

import org.apache.camel.builder.RouteBuilder;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viveksrivastava on 25/07/15.
 */
public class SimpleRouteBuilder {
	public List<RouteBuilder> getRoutes(String directory) throws Exception {
		File dir = new File(directory);

		if (!dir.isDirectory())
			throw new Exception(directory + " is not directory");

		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".simpleroute");
			}
		});
		List<RouteBuilder> RouteBuilders = new ArrayList<RouteBuilder>();
		RouteFileParser routeFileParser = new RouteFileParser();
		for (File file : files) {
			RouteBuilder builder = routeFileParser.parse(file);
			RouteBuilders.add(builder);
		}
		return RouteBuilders;
	}
}