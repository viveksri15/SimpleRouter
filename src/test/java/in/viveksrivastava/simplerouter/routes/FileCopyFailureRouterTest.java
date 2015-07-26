package in.viveksrivastava.simplerouter.routes;

import in.viveksrivastava.simplerouter.RouteFileParser;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Created by viveksrivastava on 25/07/15.
 */
public class FileCopyFailureRouterTest {

	private String testFile = "/tmp/testdata.txt";
	private String testFile_out = testFile + "_out";

	@Before
	public void init() throws Exception {
		int count = 0;
		while (count < 100000) {
			try {
				FileUtils.write(new File(testFile), count + "\n", true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			count++;
		}

		RouteBuilder routeBuilder = new RouteFileParser().parse(new File("simpleroutes/generic_fail.simpleroute"));
		CamelContext camelContext = new DefaultCamelContext();
		camelContext.addRoutes(routeBuilder);
		camelContext.start();
		Thread.sleep(10000);
	}

	@Test
	public void testConfigure() {
		File outFile = new File(testFile_out);
		if (outFile.exists())
			Assert.assertNotEquals(FileUtils.sizeOf(new File(testFile)), FileUtils.sizeOf(outFile));
	}

	@After
	public void end() {
		new File(testFile).delete();
		new File(testFile_out).delete();
	}
}