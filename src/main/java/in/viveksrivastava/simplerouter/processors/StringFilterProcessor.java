package in.viveksrivastava.simplerouter.processors;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by viveksrivastava on 26/07/15.
 */
public class StringFilterProcessor extends AProcessor {
	private Logger logger = LoggerFactory.getLogger(StringFilterProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {

		String pattern = getParam("pattern");
		String body = (String) exchange.getIn().getBody();
		Pattern patternToMatch = Pattern.compile(pattern);
		Matcher matcher = patternToMatch.matcher(body);
		if (!matcher.find()) {
			logger.info("NOT_MATCH " + body + " " + pattern);
			exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
		}
	}
}