package in.viveksrivastava.simplerouter.processors;

import org.apache.camel.Processor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by viveksrivastava on 26/07/15.
 */
public abstract class AProcessor implements Processor {
	private Map<String, String> params = new HashMap<String, String>();

	public void addParam(String key, String value) {
		params.put(key, value);
	}

	public String getParam(String key){
		return params.get(key);
	}
}
