package de.ks.messageOrg.handlers;

import org.json.JSONArray;

public interface Handler {

	public boolean handle(String key, JSONArray value);
}
