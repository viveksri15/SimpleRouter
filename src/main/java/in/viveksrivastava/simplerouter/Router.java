package in.viveksrivastava.simplerouter;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.List;

/**
 * Created by viveksrivastava on 25/07/15.
 */
public class Router {

	private static Object object = new Object();

	public static void main(String[] args) throws Exception {
		final String routeDirectory = args[0];
		startRouterSync(routeDirectory);
	}

	public static void startRouterASync(String routeDirectory) {
		CamelContext camelContext = new DefaultCamelContext();
		try {
			List<RouteBuilder> routes = new SimpleRouteBuilder().getRoutes(routeDirectory);
			for (RouteBuilder routeBuilder : routes) {
				System.out.println("routeBuilder = " + routeBuilder);
				camelContext.addRoutes(routeBuilder);
			}
			camelContext.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void startRouterSync(String routeDirectory) throws InterruptedException {
		startRouterASync(routeDirectory);
		synchronized (object) {
			//noinspection InfiniteLoopStatement
			while (true)
				object.wait();
		}
	}
}
