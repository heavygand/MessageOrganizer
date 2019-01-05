package de.ks.messageOrg.app;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import de.ks.messageOrg.gui.GuiController;
import de.ks.messageOrg.model.Message;
import de.ks.messageOrg.model.Person;
import de.uniks.networkparser.converter.GUIConverter;
import helpers.H;

public class App {

	private static String				generatedPathDB		= "C:\\Users\\erikd\\Dropbox\\Apps\\MessageOrganizer\\writeToList.txt";
	private static StartApp 			appInstance;

	/*
	 * 
	 * LADEN DER LISTEN
	 * 
	 */
	public static List<String> getAll() {

		return DBController.getAll();
	}

	public static List<String> getUnwritten() {

		return DBController.getUnwritten();
	}

	public static List<String> getWritten() {

		return DBController.getWritten();
	}

	public static List<String> getInGroup() {

		return DBController.getInGroup();
	}

	public static List<String> getNachzufassen() {

		return DBController.getNachzufassen();
	}

	public static List<String> getGroupSend() {

		return DBController.getGroupSend();
	}

	public static List<String> getVideo() {

		return DBController.getVideo();
	}

	public static List<String> getCustomers() {

		return DBController.getCustomers();
	}

	public static boolean search(String name) {

		Person person = getPerson(name);
		
		if (person == null) return false;
		
		appInstance.showPerson(person);
		
		return true;
	}

	/**
	 * GETS AND CREATES A PERSON FROM THE DATABASE
	 * @param name
	 * @return 
	 */
	@SuppressWarnings("rawtypes")
	private static Person getPerson(String name) {

		List rawPerson = DBController.getPerson(name);
		
		if(rawPerson == null) return null;
		
		Object[] objectArray = (Object[]) rawPerson.get(0);
		
		// Direct Person data
		Person person = new Person();
		person.setTitle((String) objectArray[0]);
		person.setThread_path((String) objectArray[1]);
		person.setLastContact((long) objectArray[2]);
		person.setFriendsSince((long) objectArray[3]);
		person.setFirstContact((long) objectArray[4]);
		
		// Messages
		List rawMessages = DBController.getMessages(person.getThread_path());
		if(rawMessages == null) System.out.println(person.getTitle() + " hat keine Messages");
		Object[] objectArray2 = (Object[]) rawMessages.get(0);
		
		ArrayList<Message> messages = new ArrayList<>();
		for(int i = 0 ; i < objectArray2.length ; i++) {
			
			Message message = new Message();
			message.setContent((String) objectArray2[1]);
			message.setSender_name((String) objectArray2[2]);
			message.setTimestamp_ms((long) objectArray2[3]);
		}
		
		person.setMessages(messages);
		
		return person;
	}

	public static void generatePersonList(String text) {

		int amount = Integer.parseInt(text);
		List<String> subList = GuiController.getCurrentPersonListFromGUI().subList(0, amount);
		ArrayList<String> localPersons = new ArrayList<String>(subList);
		
		if (GuiController.getCurrentTab().equals("Unwritten")) {
			
			GuiController.moveToVBox(localPersons, "Angeschriebene");
		}
		
		H.appendToFile(localPersons, generatedPathDB);
	}

	/*
	private static boolean hasMessageFromMe(Person person) {

		for (Message message : person.getMessages()) {
			if (message.getSender_name().equals("Christian Wiegand"))
				return true;
		}
		return false;
	}

	private static Person getPersonInPersonList(String name) {

		for (Person person : persons) {
			if (person.getTitle().equals(name))
				return person;
		}
		return null;
	}

	private static boolean checkContain(ArrayList<String> list, Person person) {

		boolean contains = false;
		for (String toSearch : list) {
			for (Message message : person.getMessages()) {
				contains = contains || StringUtils.containsIgnoreCase(message.getContent(), toSearch);
			}
		}
		return contains;
	}

	private static Message getLastMessageFromPerson(Person person) {

		ArrayList<Message> messages = person.getMessages();
		for (Message message : messages) {
			if (message.getSender_name().equals(person.getTitle())) {
				return message;
			}
		}
		return null;
	}

	public static void showNoReaction(VBox vBox) {

		currentUserList = vBox.getChildren();
		persons.forEach(person -> {
			if (hasMessageFromMe(person)) {
				addToCurrentGUIList(null, person);
			}
		});
	}

	public static void setAppInstance(StartApp startApp) {

		appInstance = startApp;
	}
	*/
}
