package de.ks.messageOrg.model.util;

import de.ks.messageOrg.model.Message;
import de.uniks.networkparser.IdMap;
import de.uniks.networkparser.interfaces.SendableEntityCreator;
import helpers.H;

public class MessageCreator implements SendableEntityCreator {

	private final String[] properties = new String[] { Message.PROPERTY_SENDER_NAME, Message.PROPERTY_TIMESTAMP_MS,
			Message.PROPERTY_CONTENT, Message.PROPERTY_TYPE, };

	@Override
	public String[] getProperties() {

		return properties;
	}

	@Override
	public Object getSendableInstance(boolean prototyp) {

		return new Message();
	}

	@Override
	public Object getValue(Object entity, String attribute) {

		if (attribute == null || entity instanceof Message == false) {
			return null;
		}
		Message element = (Message) entity;
		int pos = attribute.indexOf('.');
		String attrName = attribute;
		if (pos > 0) {
			attrName = attribute.substring(0, pos);
		}
		if (attrName.length() < 1) {
			return null;
		}
		if (Message.PROPERTY_SENDER_NAME.equalsIgnoreCase(attrName)) {
			return element.getSender_name();
		}
		if (Message.PROPERTY_TIMESTAMP_MS.equalsIgnoreCase(attrName)) {
			return element.getTimestamp_ms();
		}
		if (Message.PROPERTY_CONTENT.equalsIgnoreCase(attrName)) {
			return element.getContent();
		}
		if (Message.PROPERTY_TYPE.equalsIgnoreCase(attrName)) {
			return element.getType();
		}
		return null;
	}

	@Override
	public boolean setValue(Object entity, String attribute, Object value, String type) {

		if (attribute == null || entity instanceof Message == false) {
			return false;
		}
		Message element = (Message) entity;
		if (SendableEntityCreator.REMOVE.equals(type) && value != null) {
			attribute = attribute + type;
		}
		if (Message.PROPERTY_SENDER_NAME.equalsIgnoreCase(attribute)) {
			String name = H.cleanUp((String) value);
			element.setSender_name(name);
			return true;
		}
		if (Message.PROPERTY_TIMESTAMP_MS.equalsIgnoreCase(attribute)) {
			element.setTimestamp_ms((long) value);
			return true;
		}
		if (Message.PROPERTY_CONTENT.equalsIgnoreCase(attribute)) {
			String content = H.cleanUp((String) value);
			element.setContent(content);
			return true;
		}
		if (Message.PROPERTY_TYPE.equalsIgnoreCase(attribute)) {
			element.setType((String) value);
			return true;
		}
		return false;
	}

	public IdMap createMap(String session) {

		return CreatorCreator.createIdMap(session);
	}
}
