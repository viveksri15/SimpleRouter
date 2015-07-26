package in.viveksrivastava.simplerouter;

import in.viveksrivastava.simplerouter.Router;
import org.junit.Test;

/**
 * Created by viveksrivastava on 26/07/15.
 */
public class RouterTest {
	@Test
	public void testRoute() throws InterruptedException {
		Router.startRouterASync("simpleroutes/test/");
		Thread.sleep(100000);
	}
}