package de.ks.messageOrg.app;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import de.ks.messageOrg.handlers.*;
import de.ks.messageOrg.model.Person;
import helpers.Helpers;

public class MainApp {

	private static String				path	= "C:\\Users\\erikd\\Downloads\\messages\\inbox";
	private static ArrayList<Person>	persons		= new ArrayList<Person>();
	private static ArrayList<Handler>	handlers	= new ArrayList<>();
	private static Person currentPerson;
	
	public static void main(String[] args) {
		
		handlers.add(new MessageHandler());
		handlers.add(new ParticipantHandler());
		handlers.add(new PersonHandler());
		
		MainApp mainApp = new MainApp();
		mainApp.main();
	}

	public void main() {
		
//		Person newPerson = (Person)new Person()
//				.withTitle("Christian Wiegand")
//				.withIs_still_participant(true)
//				.withThread_type("Regular")
//				.withThread_path("inbox/AaronArtest_0303dc3fa9")
//				.withKid(
//						new Participant().withName("Aaron Artest"),
//						new Participant().withName("Christian Wiegand")
//						)
//				.withKid(
//						new Message()
//						.withSender_name("Aaron Artest")
//						.withTimestamp_ms(1536523770355l)
//						.withContent("Verschwinde")
//						.withType("Generic"),
//						new Message()
//						.withSender_name("Christian Wiegand")
//						.withTimestamp_ms(1536163232452l)
//						.withContent("Hey Aaron, wir kennen uns zwar nicht, aber...")
//						.withType("Generic")
//						);
//
//		IdMap map = CreatorCreator.createIdMap("newPersonEcoded");
//		
//		JsonObject json = map.toJsonObject(newPerson);
//		
//		String jsonString = json.toString(2);
//		System.out.println(jsonString);
		
		buildUp();
	}

	private static void buildUp() {

		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		for (File node : listOfFiles) {
			
			if (node.isFile()) return;
			
			JSONObject jsonObj = new JSONObject(Helpers.readFile(node.getPath() + "\\message.json"));
			
			if (!jsonObj.has("title")) continue;
			
			Person newPerson = new Person();
			setCurrentPerson(newPerson);
			
			System.out.println(jsonObj.toString(2));
			
			jsonObj.toMap().forEach((key, value) -> {

				System.out.println("Bearbeite key " + key + " mit value " + value);
				handle(key, value);
			});
			
			persons.add(newPerson);
			
			break;
		}
	}

	/**
	 * @param key 
	 * @param value 
	 * 
	 */
	private static boolean handle(String key, Object value) {

		boolean success = false;
		for(Handler h : handlers) {
			
			success = h.handle(key, value);
			
			if(success) {
				
				return true;
			}
		}
		return false;
	}

	public static Person getCurrentPerson() {

		return currentPerson;
	}

	public static void setCurrentPerson(Person currentPerson) {

		MainApp.currentPerson = currentPerson;
	}
}
