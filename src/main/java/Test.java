import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Created by viveksrivastava on 22/07/15.
 */
public class Test {
	public static void main(String[] args) throws Exception {
		CamelContext camelContext = new DefaultCamelContext();
		camelContext.addRoutes(new TestRouter());
		camelContext.start();
		Thread.sleep(10000);
		camelContext.stop();
	}
}