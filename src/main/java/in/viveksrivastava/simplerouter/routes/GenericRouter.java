package in.viveksrivastava.simplerouter.routes;

import in.viveksrivastava.simplerouter.processors.AProcessor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.model.RouteDefinition;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by viveksrivastava on 25/07/15.
 * path is of the following format:
 * path="FROM:xyz""PROCESSOR:processClass extends AProcessor""TO:file://tmp.txt"
 * TO,FROM and PROCESSOR can be used any number of times to build route
 * <p/>
 * check
 * https://camel.apache.org/dead-letter-channel.html
 * https://camel.apache.org/error-handler.html
 */
public class GenericRouter extends Route {
	private Logger logger = LoggerFactory.getLogger(GenericRouter.class);

	@Override
	public void configure() throws Exception {

		addErrorChannel(logger);

		String path = getConfig().getString("path");

		Pattern pattern = Pattern.compile("\"[^\"]*\"");
		Matcher matcher = pattern.matcher(path);
		RouteDefinition routeDefinition = null;

		while (matcher.find()) {
			String route = matcher.group().replace("\"", "");
			String routeType = route.split(":")[0];
			route = route.replaceAll(String.format("^%s:", routeType), "");
			System.out.println("route = " + route);
			switch (ROUTE_TYPE.valueOf(routeType)) {
				case FROM:
					if (routeDefinition == null)
						routeDefinition = from(route);
					else
						routeDefinition.from(route);
					break;
				case TO:
					if (routeDefinition == null)
						throw new Exception("Unexpected Order");
					routeDefinition.to(route);
					break;
				case PROCESSOR:
					if (routeDefinition == null)
						throw new Exception("Unexpected Order");

					//noinspection unchecked
					String[] processParams = route.split("\\?");

					Class<AProcessor> processorClass = (Class<AProcessor>) Class.forName(processParams[0]);
					AProcessor processor = processorClass.newInstance();

					for (int i = 1; i < processParams.length; i++) {
						int equalIndex = processParams[i].indexOf("=");
						String key = processParams[i].substring(0, equalIndex);
						String value = processParams[i].substring(equalIndex + 1);
						processor.addParam(key, value);
					}

					routeDefinition.process(processor);
					break;
				default:
					throw new Exception("Unexpected Keyword");
			}
		}

		logger.info("LOADED " + ToStringBuilder.reflectionToString(routeDefinition));
	}

	protected void addErrorChannel(Logger logger) {
		String errorChannel = getConfig().getString("errorPath");

		if (errorChannel != null) {
			int backOffMultiplier = getConfig().getInt("backOffMultiplier");
			int maxRetries = getConfig().getInt("maxRetries");
			int redeliveryDelay = getConfig().getInt("maxRetries");
			errorHandler(deadLetterChannel(errorChannel).allowRedeliveryWhileStopping(true).backOffMultiplier(backOffMultiplier)
							.maximumRedeliveries(maxRetries)
							.maximumRedeliveryDelay(redeliveryDelay * maxRetries)
							.redeliveryDelay(redeliveryDelay)
							.retryAttemptedLogLevel(LoggingLevel.INFO)
							.useCollisionAvoidance()
							.logExhausted(true)
							.logExhaustedMessageHistory(true)
							.logHandled(true)
							.logNewException(true)
							.logStackTrace(true)
			);
		} else
			errorHandler(loggingErrorHandler(logger, LoggingLevel.ERROR));
	}

	private enum ROUTE_TYPE {
		FROM, TO, PROCESSOR
	}
}
