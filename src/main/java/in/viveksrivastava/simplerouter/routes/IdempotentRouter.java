package in.viveksrivastava.simplerouter.routes;

import in.viveksrivastava.simplerouter.processors.AProcessor;
import org.apache.camel.builder.SimpleBuilder;
import org.apache.camel.model.language.SimpleExpression;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by viveksrivastava on 25/07/15.
 * TO=
 * FROM=
 * PROCESSOR=
 * check
 * https://camel.apache.org/dead-letter-channel.html
 * https://camel.apache.org/error-handler.html
 */
public class IdempotentRouter extends GenericRouter {
	private Logger logger = LoggerFactory.getLogger(IdempotentRouter.class);

	@Override
	public void configure() throws Exception {

		addErrorChannel(logger);

		String to = getConfig().getString("TO");
		String from = getConfig().getString("FROM");
		String processor = getConfig().getString("PROCESSOR");
		String pattern = getConfig().getString("PATTERN");
		AProcessor aProcessor = null;
		if (processor != null) {
			String[] processParams = processor.split("\\?");
			Class<AProcessor> processorClass = (Class<AProcessor>) Class.forName(processParams[0]);
			aProcessor = processorClass.newInstance();

			for (int i = 1; i < processParams.length; i++) {
				int equalIndex = processParams[i].indexOf("=");
				String key = processParams[i].substring(0, equalIndex);
				String value = processParams[i].substring(equalIndex + 1);
				aProcessor.addParam(key, value);
			}
		}

		if (aProcessor != null)
			from(from).filter(new SimpleBuilder("${body} regex '" + pattern + "'")).idempotentConsumer(body(), MemoryIdempotentRepository.memoryIdempotentRepository(1000)).process(aProcessor).to(to);
		else
			from(from).filter(new SimpleBuilder("${body} regex '" + pattern + "'")).idempotentConsumer(body(), MemoryIdempotentRepository.memoryIdempotentRepository(1000)).to(to);


		logger.info("LOADED IdempotentRouter");
	}
}