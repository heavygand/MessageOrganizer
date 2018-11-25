import java.util.ArrayList;

import org.apache.poi.hpsf.Array;
import org.json.JSONArray;
import org.json.JSONObject;

import de.ks.messageOrg.model.Person;
import helpers.H;

public class SachenProbieren {
	private static String				properiesPath	= "./docs/properties";

	public static void main(String[] args) {
		
		readPropertiesFromFile();
	}
	
	private static void readPropertiesFromFile() {

		String properties = H.readFile(properiesPath);
		
		JSONObject jsonObj = new JSONObject(properties);
		JSONArray jsonArr = jsonObj.getJSONArray("properties");
		
		for(Object propertyObj : jsonArr){
			
			JSONObject propertyJsonObj = (JSONObject) propertyObj;
			String name = H.cleanUp(propertyJsonObj.get("name").toString());
			
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

	private static Person getPersonInPersonList(String name) {

		ArrayList<Person> persons = new ArrayList();
		
		persons.add(new Person().withTitle("Alexander Hötzl"));
		persons.add(new Person().withTitle("Frank Walter"));
		persons.add(new Person().withTitle("Julius Freund"));
		persons.add(new Person().withTitle("Nadine Kaiser"));
		
		for (Person person : persons ) {
			
			if(person.getTitle().equals(name)) return person;
		}
		
		return null;
	}
}
