package in.viveksrivastava.simplerouter.routes;

import in.viveksrivastava.simplerouter.RouteFileParser;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by viveksrivastava on 26/07/15.
 */
public class ExceptionToAWSCWTest {
	private String testFile = "/tmp/exception.txt";

	@Before
	public void init() throws Exception {
		FileUtils.write(new File(testFile), "STARTING\n", true);
		//Simulate Streaming
		new Thread() {
			public void run() {
				int count = 0;
				while (count < 100000) {
					try {
						throw new Exception("Test Exception " + count);
					} catch (Exception e) {
						try {
							FileUtils.write(new File(testFile), ExceptionUtils.getStackTrace(e), true);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					if(count<100)
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					count++;
				}

			}
		}.start();

		RouteBuilder routeBuilder = new RouteFileParser().parse(new File("simpleroutes/test/streamToStringFilterToConsole.simpleroute"));
		CamelContext camelContext = new DefaultCamelContext();
		camelContext.addRoutes(routeBuilder);
		camelContext.start();
		Thread.sleep(60000);
	}

	@Test
	public void testConfigure() {
	}

	@After
	public void end() {
//		new File(testFile).delete();
	}
}