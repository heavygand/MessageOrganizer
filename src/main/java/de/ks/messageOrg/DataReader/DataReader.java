package de.ks.messageOrg.DataReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.json.JSONArray;
import org.json.JSONObject;

import de.ks.messageOrg.model.Message;
import de.ks.messageOrg.model.Person;
import de.ks.messageOrg.model.util.MessageCreator;
import de.ks.messageOrg.model.util.PersonCreator;
import helpers.H;

public class DataReader {
	
	private static String				inboxPath			= "C:\\Users\\erikd\\Downloads\\messages\\inbox";
	private static String				friendsPath			= "C:\\Users\\erikd\\Downloads\\friends\\friends.json";
	private static String				generatedPathDB		= "C:\\Users\\erikd\\Dropbox\\Apps\\MessageOrganizer\\writeToList.txt";
	private static String				blackListPath		= "./docs/blacklist";
	private static String				createPersonsFile	= "./docs/createPersons.txt";
	private static String				createMessagesFile	= "./docs/createMessages.txt";
	private static String				createPropertiesFile= "./docs/createProperties.txt";
	private static ArrayList<Person>	persons				= new ArrayList<Person>();
	private static Person currentPerson;
	private static List<String> ausnahmenList;

	public static void main(String[] args) {
		
		readJsonData();
	}
	
	public static void readJsonData() {
		
		EntityManager em = Person.getEm();
		
		em.getTransaction().begin();
		em.createNativeQuery("SET NAMES utf8mb4").executeUpdate();
		
		// Ausnahmen abrufen
		readAusnahmen(em);
		
		readTextFilesForInitialization();
		
		System.out.println("Persistiere alles...");
		
		em.createNativeQuery("drop table persons").executeUpdate();
		em.createNativeQuery(H.readFile(createPersonsFile)).executeUpdate();
		
		em.createNativeQuery("drop table messages").executeUpdate();
		em.createNativeQuery(H.readFile(createMessagesFile)).executeUpdate();
		
		// FREUNDE
		for (Person person : persons) {
			
			Query query = em.createNativeQuery("INSERT IGNORE INTO persons "
					+ "(title, id, lastReaction, friendsSince, firstContact, threadType, isStillParticipant) "
					+ "values (?, ?, ?, ?, ?, ?, ?) ");
			
			// Values
			query.setParameter(1, person.getTitle());					// title
			query.setParameter(2, person.getThread_path());				// id
			query.setParameter(3, person.getLastContact());				// lastReaction
			query.setParameter(4, person.getFriendsSince());			// friendsSince
			query.setParameter(5, person.getFirstContact());			// firstContact
			query.setParameter(6, person.getThread_type());				// threadType
			query.setParameter(7, person.getIs_still_participant());	// isStillParticipant
			
			query.executeUpdate();
			
			// NACHRICHTEN
			for (Message message : person.getMessages()) {
				
				Query query2 = em.createNativeQuery("INSERT IGNORE INTO messages "
						+ "(id, CONTENT, SENDER_NAME, TIMESTAMP_MS, TYPE) "
						+ "values (?, ?, ?, ?, ?)");
				query2.setParameter(1, person.getThread_path());	// id
				query2.setParameter(2, message.getContent());		// CONTENT
				query2.setParameter(3, message.getSender_name());	// SENDER_NAME
				query2.setParameter(4, message.getTimestamp_ms());	// TIMESTAMP_MS
				query2.setParameter(5, message.getType());			// TYPE
				
				query2.executeUpdate();
			}
		}
		
		em.getTransaction().commit();
		em.close();
		
		System.out.println("Fertig.");
	}


	/**
	 * @param em
	 */
	private static void readAusnahmen(EntityManager em) {

		ausnahmenList = em.createNativeQuery("select * from ausnahmen").getResultList();
	}


	private static void readTextFilesForInitialization() {

		System.out.println("Lese Freunde ein...");
		readFriendsFromFile();

		System.out.println("Lese Messages ein...");
		readMessagesFromFile();
	}

	/**
	 * 
	 */
	private static void readFriendsFromFile() {

		String friendsText = H.readFile(friendsPath);
		
		JSONObject jsonArrObj = new JSONObject(friendsText);
		JSONArray jsonArr = jsonArrObj.getJSONArray("friends");
		
		jsonArr.forEach(friendObj -> {
			
			JSONObject fjo = (JSONObject) friendObj;
			String friendString = H.cleanUp(fjo.get("name").toString());
			
			if (!isAusnahme(friendString)) {
				
				long friendsSince = (long) (int)fjo.get("timestamp");
				Person newPerson = new Person().withTitle(friendString);
				newPerson.setFriendsSince(friendsSince*1000);
				
				persons.add(newPerson);
				
//				if(newPerson.getTitle().startsWith("Andre R"))System.out.println(newPerson.getTitle() + " angelegt");
			}
		});
	}

	private static Message getLastMessageFromPerson(Person person) {

		ArrayList<Message> messages = person.getMessages();
		
		for(Message message : messages) {
			
			if(message.getSender_name().equals(person.getTitle())) {
				
				return message;
			}
		}
		
		return null;
	}

	private static Message getLastMessage(Person person) {
		
		return person.getMessages().get(0);
	}

	private static Message getFirstMessage(Person person) {

		ArrayList<Message> messages = person.getMessages();
		return messages.get(messages.size()-1);
	}

	public static boolean isAusnahme(String name2Check, EntityManager em) {
		
		if(ausnahmenList == null) readAusnahmen(em);
		
		if(ausnahmenList.contains(name2Check)) return true;
		
		return false;
	}

	private static boolean isAusnahme(String name2Check) {
		
		if(ausnahmenList.contains(name2Check)) return true;
		
		return false;
	}

	/**
	 * 
	 */
	private static void readMessagesFromFile() {

		File folder = new File(inboxPath);
		File[] listOfFiles = folder.listFiles();
		for (File node : listOfFiles) {
			
			if (node.isFile()) continue;
			
			JSONObject jsonObj = new JSONObject(H.readFile(node.getPath() + "\\message.json"));
			
			if (!jsonObj.has("title")) continue;
			String title = H.cleanUp(jsonObj.get("title").toString());
			if (isAusnahme(title)) continue;
			
			Person thisPerson = getPersonInPersonList(title);
			
			if(thisPerson == null) {
				
//				System.out.println(title + " war null in getMessages()");
				continue;
			}
			
			currentPerson = thisPerson;
			readMessageFileAttributes(jsonObj);
			
			long timeStampLongFirst = thisPerson.getMessages().isEmpty() ? 0 : getFirstMessage(thisPerson).getTimestamp_ms();        
			thisPerson.setFirstContact(timeStampLongFirst);

			long timeStampLongLast = getLastMessageFromPerson(thisPerson)==null ? 0 : getLastMessageFromPerson(thisPerson).getTimestamp_ms();        
			thisPerson.setLastContact(timeStampLongLast);
		}
	}

	private static Person getPersonInPersonList(String name) {

		for (Person person : persons) {
			
			if(person.getTitle().equals(name)) return person;
		}
		
		return null;
	}

	/**
	 * @param jsonObj
	 * @param newPerson
	 */
	private static void readMessageFileAttributes(JSONObject jsonObj) {

		jsonObj.keys().forEachRemaining(key -> {
			
			if (H.isJsonArray(jsonObj, key)) {
				
				handle(key, jsonObj.getJSONArray(key));
			}
			else {
				
				PersonCreator ps = new PersonCreator();
				ps.setValue(currentPerson, key, jsonObj.get(key), null);
			}
		});
	}

	/**
	 * @param key
	 * @param value
	 * 
	 */
	private static void handle(String key, JSONArray jSONArray) {

    	if(		key.equals("is_still_participant") ||
    			key.equals("thread_type") ||
    			key.equals("title") ||
    			key.equals("thread_path")) {
    		
    		PersonCreator personCreator = new PersonCreator();
    		personCreator.setValue(currentPerson, key, jSONArray, null);
    		
    	}
    	if(key.equals("messages")) {
    	
	    	ArrayList<Message> messages = new ArrayList<>();
	    	
	    	jSONArray.forEach(str -> {
	    		
	    		Message newMessage = new Message();
	        	
	    		JSONObject messageData = (JSONObject)str;
	    		
	    		if(setAttributes(newMessage, messageData)){
	    		
	    			messages.add(newMessage);
	    		}
	    	});
	    	
	    	currentPerson.setMessages(messages);
    	}
	}
	
	
	/**
	 * @param message
	 * @param messageObject
	 * @return If setting was successfull
	 */
	private static boolean setAttributes(Message message, JSONObject messageObject) {
		
		if(((String)messageObject.get("content")).contains("Sag deinem/r neuen Facebook-Freund/in")) return false;
		
		MessageCreator mc = new MessageCreator();

		messageObject.keys().forEachRemaining(messObjKey -> {
			
			mc.setValue(message, messObjKey, messageObject.get(messObjKey), null);
		});
		
		return true;
	}
}
