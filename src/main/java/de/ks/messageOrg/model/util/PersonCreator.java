package de.ks.messageOrg.model.util;

import de.ks.messageOrg.model.Person;
import de.uniks.networkparser.IdMap;
import de.uniks.networkparser.interfaces.SendableEntityCreator;
import helpers.H;

public class PersonCreator implements SendableEntityCreator {

	private final String[] properties = new String[] { Person.PROPERTY_TITLE, Person.PROPERTY_IS_STILL_PARTICIPANT,
			Person.PROPERTY_THREAD_TYPE, Person.PROPERTY_THREAD_PATH, };

	@Override
	public String[] getProperties() {

		return properties;
	}

	@Override
	public Object getSendableInstance(boolean prototyp) {

		return new Person();
	}

	@Override
	public Object getValue(Object entity, String attribute) {

		if (attribute == null || entity instanceof Person == false) {
			return null;
		}
		Person element = (Person) entity;
		int pos = attribute.indexOf('.');
		String attrName = attribute;
		if (pos > 0) {
			attrName = attribute.substring(0, pos);
		}
		if (attrName.length() < 1) {
			return null;
		}
		if (Person.PROPERTY_TITLE.equalsIgnoreCase(attrName)) {
			return element.getTitle();
		}
		if (Person.PROPERTY_IS_STILL_PARTICIPANT.equalsIgnoreCase(attrName)) {
			return element.getIs_still_participant();
		}
		if (Person.PROPERTY_THREAD_TYPE.equalsIgnoreCase(attrName)) {
			return element.getThread_type();
		}
		if (Person.PROPERTY_THREAD_PATH.equalsIgnoreCase(attrName)) {
			return element.getThread_path();
		}
		return null;
	}

	@Override
	public boolean setValue(Object entity, String attribute, Object value, String type) {

		if (attribute == null || entity instanceof Person == false) {
			return false;
		}
		Person element = (Person) entity;
		if (SendableEntityCreator.REMOVE.equals(type) && value != null) {
			attribute = attribute + type;
		}
		if (Person.PROPERTY_TITLE.equalsIgnoreCase(attribute)) {
			
			String title = H.cleanUp((String) value);
			
			element.setTitle(title);
			return true;
		}
		if (Person.PROPERTY_IS_STILL_PARTICIPANT.equalsIgnoreCase(attribute)) {
			element.setIs_still_participant((boolean) value);
			return true;
		}
		if (Person.PROPERTY_THREAD_TYPE.equalsIgnoreCase(attribute)) {
			element.setThread_type((String) value);
			return true;
		}
		if (Person.PROPERTY_THREAD_PATH.equalsIgnoreCase(attribute)) {
			element.setThread_path((String) value);
			return true;
		}
		return false;
	}

	public static IdMap createMap(String session) {

		return CreatorCreator.createIdMap(session);
	}
}
