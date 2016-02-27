package server.model.players;

import java.util.HashMap;

public class Attributes
{
	private HashMap<String, Object> attributeMap = new HashMap<String, Object>();
	
	public void setAttribute(String key, Object value) {
		attributeMap.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String key) {
		return (T) attributeMap.get(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String key, T fail) {
		T t = (T) attributeMap.get(key);
		if (t != null) {
			return t;
		}
		return fail;
	}

	public void removeAttribute(String key) {
		attributeMap.remove(key);
	}
}