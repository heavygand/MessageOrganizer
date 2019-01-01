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
	private static String				ausnahmePath		= "./docs/ausgenommen";
	private static String				blackListPath		= "./docs/blacklist";
	private static String				properiesPath		= "./docs/properties";
	private static String				cyPath				= "./docs/CY.txt";
	private static String				customersPath		= "./docs/Kunden.txt";
	private static ArrayList<Person>	persons				= new ArrayList<Person>();
	private static long starttime;
	private static Person currentPerson;
	private static List<String> ausnahmenList;

	public static void main(String[] args) {
		
		EntityManager em = Person.getEm();
		
		em.getTransaction().begin();
		em.createNativeQuery("SET NAMES utf8mb4").executeUpdate();
		
		// Ausnahmen abrufen
		ausnahmenList = em.createNativeQuery("select * from ausnahmen").getResultList();
		
		readTextFilesForInitialization();
		
		System.out.println("Persistiere alles...");
		
		em.createNativeQuery("drop table persons").executeUpdate();
		em.createNativeQuery("CREATE TABLE `persons` (`title` VARCHAR(100) NOT NULL,`state` VARCHAR(100) NULL DEFAULT NULL,`id` VARCHAR(100) NULL DEFAULT NULL,`notes` VARCHAR(3000) NULL DEFAULT NULL,`nachfassen` BIGINT(20) NULL DEFAULT NULL,`lastReaction` BIGINT(20) NULL DEFAULT NULL,`friendsSince` BIGINT(20) NOT NULL,`firstContact` BIGINT(20) NULL DEFAULT NULL,`threadType` VARCHAR(50) NULL DEFAULT NULL,`isStillParticipant` BIT(1) NULL DEFAULT NULL,PRIMARY KEY (`title`, `friendsSince`))COLLATE='utf8_general_ci'ENGINE=InnoDB;").executeUpdate();
		
		// FREUNDE
		for (Person person : persons) {
			
			Query query = em.createNativeQuery("INSERT INTO persons "
					+ "(title, state, id, notes, nachfassen, lastReaction, friendsSince, firstContact, threadType, isStillParticipant) "
					+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) "
					+ "ON DUPLICATE KEY UPDATE "
					+ "state = ?, notes = ?, nachfassen = ?, lastReaction = ?, isStillParticipant = ?");
			
			// Values
			query.setParameter(1, person.getTitle());					// title
			query.setParameter(2, person.getState());					// state
			query.setParameter(3, person.getThread_path());				// id
			query.setParameter(4, person.getNotes());					// notes
			query.setParameter(5, person.getNachfassen());				// nachfassen
			query.setParameter(6, person.getLastContact());				// lastReaction
			query.setParameter(7, person.getFriendsSince());			// friendsSince
			query.setParameter(8, person.getFirstContact());			// firstContact
			query.setParameter(9, person.getThread_type());				// threadType
			query.setParameter(10, person.getIs_still_participant());	// isStillParticipant
			
			// ON DUPLICATE KEY UPDATE
			query.setParameter(11, person.getState());					// state
			query.setParameter(12, person.getNotes());					// notes
			query.setParameter(13, person.getNachfassen());				// nachfassen
			query.setParameter(14, person.getLastContact());			// lastReaction
			query.setParameter(15, person.getIs_still_participant());	// isStillParticipant
			
			query.executeUpdate();
			
			for (Message message : person.getMessages()) {
				
				Query query2 = em.createNativeQuery("INSERT IGNORE INTO messages "
						+ "(id, CONTENT, SENDER_NAME, TIMESTAMP_MS, TYPE) "
						+ "values (?, ?, ?, ?, ?)");
				query2.setParameter(1, person.getThread_path());	// id
				query2.setParameter(2, message.getContent());		// CONTENT
				query2.setParameter(3, message.getSender_name());	// SENDER_NAME
				query2.setParameter(4, message.getTimestamp_ms());	// TIMESTAMP_MS
				query2.setParameter(5, message.getType());			// TYPE
				try {
					
					query2.executeUpdate();
				}
				catch (PersistenceException e) {
					
					System.out.println("Es geht um folgendes: ");
					System.out.println(person.getThread_path());
					System.out.println(message.getContent());
					System.out.println(message.getSender_name());
					System.out.println(message.getTimestamp_ms());
					System.out.println(message.getType());
					e.printStackTrace();
				}
			}
		}
		// Ausnahmen
		File ausnahmeFile = new File(ausnahmePath);
		
		for (String line : H.getLines(ausnahmeFile)) {

			Query query3 = em.createNativeQuery("INSERT IGNORE INTO ausnahmen (title) values (?)");
			query3.setParameter(1, line);
			query3.executeUpdate();
		}
		
		em.getTransaction().commit();
		em.close();
		
		System.out.println("Fertig.");
	}


	public static void readTextFilesForInitialization() {

		starttime = System.currentTimeMillis();
		System.out.println("Lese Freunde ein...");
		readFriendsFromFile();
		System.out.println("Freunde hat " + H.getSeconds(System.currentTimeMillis() - starttime) + " sekunden gedauert");

		starttime = System.currentTimeMillis();
		System.out.println("Lese Messages ein...");
		readMessagesFromFile();
		System.out.println("Messages hat " + H.getSeconds(System.currentTimeMillis() - starttime) + " sekunden gedauert");

		starttime = System.currentTimeMillis();
		System.out.println("Lese Properties ein...");
		readPropertiesFromFile();
		System.out.println("Properties hat " + H.getSeconds(System.currentTimeMillis() - starttime) + " sekunden gedauert");
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
			
			setCurrentPerson(thisPerson);
			readMessageFileAttributes(jsonObj);
		}
		
//		Collections.sort(persons);
	}

	private static Person getPersonInPersonList(String name) {

		for (Person person : persons) {
			
			if(person.getTitle().equals(name)) return person;
		}
		
		return null;
	}

	private static void readPropertiesFromFile() {

		String properties = H.readFile(properiesPath);
		
		JSONObject jsonObj = new JSONObject(properties);
		JSONArray jsonArr = jsonObj.getJSONArray("properties");
		
		for(Object propertyObj : jsonArr){
			
			JSONObject propertyJsonObj = (JSONObject) propertyObj;
			String name = propertyJsonObj.get("name").toString(); // Achtung, hier wird kein H.cleanUp gemacht, weil ich davon ausgehe, dass es dort immer sauber reingeschrieben wird
			
			Person person = getPersonInPersonList(name);
			
			if(person == null) {
				
				System.out.println(name + " war nicht bei den Personen, aber in der property Liste");
				
				continue;
			}
			
			propertyJsonObj.keys().forEachRemaining(key -> {
				
				if(key.equals("nachfassen")) {
					
					person.setNachfassen(propertyJsonObj.getLong(key));
				}
				
				if(key.equals("notes")) {
					
					person.setNotes(propertyJsonObj.getString(key));
				}
			});
		}
	}

	public static Person getCurrentPerson() {

		return currentPerson;
	}

	public static void setCurrentPerson(Person currentPerson) {

		DataReader.currentPerson = currentPerson;
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
	    		
	    		setAttributes(newMessage, messageData);
	    		
	    		messages.add(newMessage);
	    	});
	    	
	    	currentPerson.setMessages(messages);
    	}
	}
	
	private static void setAttributes(Message message, JSONObject messageObject) {
		
		MessageCreator mc = new MessageCreator();

		messageObject.keys().forEachRemaining(messObjKey -> {
			
			mc.setValue(message, messObjKey, messageObject.get(messObjKey), null);
		});
	}
}
