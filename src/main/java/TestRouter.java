import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * Created by viveksrivastava on 22/07/15.
 */
public class TestRouter extends RouteBuilder {
	@Override
	public void configure() throws Exception {
//		from("timer:foo?period=5000").to("https4://www.google.co.in/search?q=flipkart+conman&oq=flipkart+conman").to("stream:out");
		from("file:///tmp?fileName=testdata.txt&noop=true").process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				Message in = exchange.getIn();
				Object body = in.getBody();
				System.out.println("in.getBody() = " + body);
				if (body != null && body instanceof String) {
					String s = ((String) body).toUpperCase();
					in.setBody(s);
					exchange.setProperty("q", s);
				}
				exchange.setOut(in);
			}
		}).to("file:///tmp?fileName=testdata.txt_out&idempotent=true");
	}
}