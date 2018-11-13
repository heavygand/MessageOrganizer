package de.ks.messageOrg.app;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import de.uniks.networkparser.IdMap;
import de.uniks.networkparser.json.JsonObject;
import helpers.Helpers;

public class MainApp {

	private static String				path	= "C:\\Users\\erikd\\Downloads\\messages\\inbox";
	private static ArrayList<Person>	persons	= new ArrayList<Person>();

	public static void main(String[] args) {
		
		Person newPerson = new Person()
				.withTitle("Christian Wiegand")
				.withIs_still_participant(true);

		IdMap map = new IdMap();

		JsonObject json = map.toJsonObject(newPerson);
		String data = json.toString();
		
		System.out.println(data);
//		buildUp();
	}

	private static void buildUp() {

		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		for (File node : listOfFiles) {
			if (node.isDirectory()) {
				
				JSONObject jsonObj = new JSONObject(Helpers.readFile(node.getPath() + "\\message.json"));
				
				if (!jsonObj.has("title")) continue;
				
				Person newPerson = new Person();
				persons.add(newPerson);
				
				IdMap map = new IdMap();

			}
			break;
		}
	}
}
