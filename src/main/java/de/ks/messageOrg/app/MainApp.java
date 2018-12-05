package de.ks.messageOrg.app;

import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.DateFormatter;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import de.ks.messageOrg.gui.GuiController;
import de.ks.messageOrg.handlers.*;
import de.ks.messageOrg.model.Message;
import de.ks.messageOrg.model.Person;
import de.ks.messageOrg.model.util.PersonCreator;
import helpers.H;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class MainApp extends Application {

	private static String				inboxPath		= "C:\\Users\\erikd\\Downloads\\messages\\inbox";
	private static String				friendsPath		= "C:\\Users\\erikd\\Downloads\\friends\\friends.json";
	private static String				ausnahmePath	= "./docs/ausgenommen";
	private static String				blackListPath	= "./docs/blacklist";
	private static String				properiesPath	= "./docs/properties";
	private static String				cyPath			= "./docs/CY.txt";
	private static String				customersPath	= "./docs/Kunden.txt";
	private static ArrayList<Person>	persons			= new ArrayList<Person>();
	private static ArrayList<Handler>	handlers		= new ArrayList<>();
	private static Person				currentPerson;
	private static ObservableList<Node> currentUserList;
	private static Label 				anzeige;
	private final int 					daysBack		= 3;
	private Stage stage;
	private static MainApp appInstance;
	private static long starttime;
	private static ArrayList<Person> personListInGroup;

	public static void main(String[] args) {
		
		starttime = System.currentTimeMillis();

		initHandlers();
		launch("gui.fxml");
	}

	/**
	 * 
	 */
	private static void initHandlers() {

		handlers.add(new MessageHandler());
		handlers.add(new ParticipantHandler());
		handlers.add(new PersonHandler());
	}

	public static void showAll(VBox vBox) {

		currentUserList = vBox.getChildren();

		persons.forEach(person -> {
			
			addToCurrentGUIList(person.getState(), person);
		});
	}

	public static void showUnwritten(VBox vBox) {

		currentUserList = vBox.getChildren();
		
		persons.forEach(person -> {
			
			if(!hasMessageFromMe(person)) {
				
				addToCurrentGUIList(vBox.getId(), person);
			}
		});
	}

	/**
	 * @param person
	 * @return
	 */
	private static boolean hasMessageFromMe(Person person) {
		
		for (Message message : person.getMessages()) {
			
			if(message.getSender_name().equals("Christian Wiegand")) return true;
		}
		
		return false;
	}

	public static void showWritten(VBox vBox) {

		currentUserList = vBox.getChildren();
		
		ArrayList<String> notOk = new ArrayList<>();
		
		notOk.add("https://www.facebook.com/groups/197111937786655/");
		notOk.add("https://www.youtube.com/watch?v=A2_xGg_O2lE");
		notOk.add("https://youtu.be/opBGuztAvtw");
		notOk.add("https://youtu.be/gJxp4loJyyA");
		
		persons.forEach(person -> {
			
			if(hasMessageFromMe(person) && !checkContain(notOk, person)) {
				
				addToCurrentGUIList(vBox.getId(), person);
			}
		});
	}

	public static void showGroup(VBox vBox) {

		currentUserList = vBox.getChildren();
		ArrayList<String> ok = new ArrayList<>();
		ArrayList<String> notOk = new ArrayList<>();
		
		ok.add("https://www.facebook.com/groups/197111937786655/");
		notOk.add("https://www.youtube.com/watch?v=A2_xGg_O2lE");
		notOk.add("https://youtu.be/opBGuztAvtw");
		notOk.add("https://youtu.be/gJxp4loJyyA");

		addPeopleWithRightMessagesToCurrentGUIList(vBox.getId(), ok, notOk);
	}
	
	public static void showVideo(VBox vBox) {
		
		currentUserList = vBox.getChildren();
		ArrayList<String> ok = new ArrayList<>();
		ArrayList<String> notOk = new ArrayList<>();
		
		ok.add("https://www.youtube.com/watch?v=A2_xGg_O2lE");
		ok.add("https://youtu.be/opBGuztAvtw");
		ok.add("https://youtu.be/gJxp4loJyyA");
		
		addPeopleWithRightMessagesToCurrentGUIList(vBox.getId(), ok, notOk);
	}

	public static void showCustomers(VBox vBoxKunden) {

		currentUserList = vBoxKunden.getChildren();
		ArrayList<String> ok = new ArrayList<>();
		ArrayList<String> notOk = new ArrayList<>();
		
		ArrayList<Person> personListCustomers = new ArrayList<>();
		
		vBoxKunden.getChildren().clear();
		H.getLines(customersPath).forEach(personString -> {
			
			Person person = getPersonInPersonList(personString);
				
			moveToVBox(person, vBoxKunden);
		});
	}

	/**
	 * @param person
	 * @param vBoxKunden
	 */
	private static void moveToVBox(Person person, VBox vBoxKunden) {

		String state = person.getState();
		VBox vBoxOld = GuiController.getVBox(state);	
		removeFromGUIList(vBoxOld, person);
		addToGUIList(vBoxKunden, person);
	}

	public static void showInGroup(VBox vBoxInnGroup, VBox vBoxGruppe_verschickt) {
		
		long newStarttime = System.currentTimeMillis();
		System.out.println("Lese CY Gruppenmitglieder ein...");

		currentUserList = vBoxInnGroup.getChildren();
		ArrayList<String> ok = new ArrayList<>();
		ArrayList<String> notOk = new ArrayList<>();
		
		ok.add("https://www.facebook.com/groups/197111937786655/");
		notOk.add("https://www.youtube.com/watch?v=A2_xGg_O2lE");
		notOk.add("https://youtu.be/opBGuztAvtw");
		notOk.add("https://youtu.be/gJxp4loJyyA");
		
		personListInGroup = new ArrayList<>();
		
		addPeopleWithRightMessages2ArrayList(personListInGroup, cyPath, vBoxInnGroup, ok, notOk);
		
		buildGUIList4PersonListFromTo(personListInGroup, vBoxGruppe_verschickt, vBoxInnGroup);
		
		System.out.println("CY Gruppenmitglieder hat " + H.getSeconds(System.currentTimeMillis() - newStarttime) + " sekunden gedauert");
	}

	private static void addPeopleWithRightMessages2ArrayList(ArrayList<Person> personList, String path, VBox vBox, ArrayList<String> ok, ArrayList<String> notOk) {

		H.getLines(path).forEach(line -> {
			
			Person person = getPersonInPersonList(line);
			
			if(person == null && isAusnahme(line)) {} //System.out.println(line + " ist ausnahme");
			else if(person == null) System.out.println(line + " ist nicht bei den Personen, aber in " + vBox.getId());
			
			if(person != null && checkContain(ok, person) && !checkContain(notOk, person)) {
				
				personList.add(person);
			}
		});
	}

	/**
	 * @param vBoxTo
	 * @param vBoxFrom
	 */
	@SuppressWarnings("unchecked")
	private static void buildGUIList4PersonListFromTo(ArrayList<Person> personList, VBox vBoxFrom, VBox vBoxTo) {

		Collections.sort(personList);
		
		vBoxTo.getChildren().clear();
		
		for(Person person : personList) {			
			
			removeFromGUIList(vBoxFrom, person);
			addToGUIList(vBoxTo, person);
		}
	}

	public static void showNachzufassen(VBox vBox) {

		currentUserList = vBox.getChildren();
		
		persons.forEach(person -> {
			
			if(person.getNachfassen() != 0 && H.isEarlierOrToday(person.getNachfassen())) {
				
				addToCurrentGUIList(null, person);
			}
		});
	}

	private static Person getPersonInPersonList(String name) {

		for (Person person : persons) {
			
			if(person.getTitle().equals(name)) return person;
		}
		
		return null;
	}

	private static void addPeopleWithRightMessagesToCurrentGUIList(String status, ArrayList<String> okList, ArrayList<String> notOkList) {

		currentUserList.removeAll(currentUserList);
		
		persons.forEach(person -> {
			
			if(checkContain(okList, person) && !checkContain(notOkList, person)) {
				
				addToCurrentGUIList(status, person);
			}
		});
	}

	/**
	 * Checks if the messages of this person contain one of the strings
	 * 
	 * @param list
	 * @param person
	 * @return
	 */
	private static boolean checkContain(ArrayList<String> list, Person person) {

		boolean contains = false;
		for (String toSearch : list) {

			for (Message message : person.getMessages()) {
				
				contains = contains || StringUtils.containsIgnoreCase(message.getContent(), toSearch);
			}
		}
		return contains;
	}

	private static void readTextFilesForInitialization() {

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
				
//				System.out.println(newPerson.getTitle() + " angelegt");
			}
		});
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
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
		
		Collections.sort(persons);
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

	private static boolean isAusnahme(String name) {

		File ausnahmeFile = new File(ausnahmePath);
		
		for (String kackLine : H.getLines(ausnahmeFile)) {

			String line = H.cleanUp(kackLine);
			
			if(line.equals(name)) return true;
		}
		
		return false;
	}
	
	private static void addToCurrentGUIList(String status, Person person) {
		
		addToGUIList(currentUserList, person, status);
	}

	private static void addToGUIList(VBox vBox, Person person) {

		ObservableList<Node> children = vBox.getChildren();
		addToGUIList(children, person, vBox.getId());
	}

	private static void addToGUIList(ObservableList<Node> currentUserList, Person person, String status) {
		
		HBox personBox = new HBox();
		personBox.setPrefHeight(32);
		personBox.setPrefWidth(600);
		currentUserList.add(personBox);
		
		String name = person.getTitle();
		Label label = new Label(name);
		label.setPrefWidth(200);
		personBox.getChildren().add(label);

		long timeStampLongFirst = person.getMessages().isEmpty() ? 0 : getFirstMessage(person).getTimestamp_ms();        
        person.setFirstContact(timeStampLongFirst);

		long timeStampLongLast = person.getMessages().isEmpty() ? 0 : getLastMessage(person).getTimestamp_ms();        
        person.setLastContact(timeStampLongLast);
        
        String formattedDate = H.getGermanDateTimeString(timeStampLongFirst);
        
        if(timeStampLongFirst == 0) formattedDate = "";
        
        Label dateLabel = new Label(formattedDate);
        dateLabel.setPrefWidth(300);
        personBox.getChildren().add(dateLabel);
        
        if (status != null) person.setState(status);
        
		label.setOnMouseClicked(e -> {
			
			appInstance.showPerson(person);
		});
	}

	private static void removeFromGUIList(VBox vBox, Person person) {
		
		ArrayList<Object> toRemove = new ArrayList<>();

		ObservableList<Node> children = vBox.getChildren();
		for(Object personBoxObj : children) {
			
			HBox personBox = (HBox) personBoxObj;
			
			for(Object obj : personBox.getChildren()) {
				
				if(obj instanceof Label && ((Label)obj).getText().equals(person.getTitle())) {
					
					toRemove.add(personBoxObj);
				}
			}
		}
		
		for(Object obj : toRemove) {
			
			children.remove(obj);
		}
	}

	private void showPerson(Person person) {
		
		String name = person.getTitle();

		Pane pane = null;
		try {
			pane = FXMLLoader.load(getClass().getResource("personWindow.fxml"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        Stage stage = new Stage();
        stage.setTitle("Eigenschaften von " + name);
        Scene scene = new Scene(pane, 1200, 900);
		stage.setScene(scene);
		GuiController.initPersonWindow(scene, person);
        stage.show();
	}

	private static Message getLastMessage(Person person) {

		ArrayList<Message> messages = person.getMessages();
		return messages.get(0);
	}

	private static Message getFirstMessage(Person person) {

		ArrayList<Message> messages = person.getMessages();
		return messages.get(messages.size()-1);
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
	private static void handle(String key, JSONArray value) {

		boolean success = false;
		for (Handler h : handlers) {
			success = h.handle(key, value) || success;
		}
		if (!success) {
			System.out.println("Not handled: key " + key + " value " + value);
		}
	}

	public static Person getCurrentPerson() {

		return currentPerson;
	}

	public static void setCurrentPerson(Person currentPerson) {

		MainApp.currentPerson = currentPerson;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		readTextFilesForInitialization();

		starttime = System.currentTimeMillis();
		System.out.println("Baue GUI auf...");
		
		stage = primaryStage;
		
		startWindow(getParameters().getUnnamed().get(0));

		System.out.println("GUI hat " + H.getSeconds(System.currentTimeMillis() - starttime) + " sekunden gedauert (incl CY)");
	}

	private void startWindow(String fxmlName) {
		
		appInstance = this;

		Pane pane = null;
		try {
			pane = FXMLLoader.load(getClass().getResource(fxmlName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(pane);
		stage.setTitle("Message Organizer");
		scene.setUserData(fxmlName);
		GuiController.initMainWindow(scene);
		stage.setScene(scene);
		stage.show();
		
		refreshAnzeige();
	}


	/**
	 * @return the anzeige
	 */
	public static Label getAnzeige() {
	
		return anzeige;
	}

	
	/**
	 * @param anzeige the anzeige to set
	 */
	public static void setPersonenAnzahlAnzeige(Label anzeige) {
	
		MainApp.anzeige = anzeige;
	}
	
	private void refreshAnzeige() {
		
		anzeige.setText(currentUserList.size() + " Personen");
	}

	public static void refreshAnzeige(VBox vBox) {

		currentUserList = vBox.getChildren();
		anzeige.setText(currentUserList.size() + " Personen");
	}

	public static void savePersonNotes(Person person, TextArea notesArea, DatePicker nachfassenAm) {

		String notes = notesArea.getText();
		LocalDate dpValue = nachfassenAm.getValue();
		
		person.setNotes(notes);
		person.setNachfassen(H.getTimeStampAsLong(dpValue));
		
		writeBackProperties();
	}

	private static void writeBackProperties() {
		
		JSONObject rootObj = new JSONObject();
		JSONArray propertiesArray = new JSONArray();
		rootObj.put("properties", propertiesArray);

		persons.forEach(person -> {
			
			if(person.hasProperties()) {

				JSONObject jsonObj = new JSONObject();
				jsonObj.put("name", H.cleanUp(person.getTitle()));
				jsonObj.put("nachfassen", person.getNachfassen());
				jsonObj.put("notes", person.getNotes());
				
				propertiesArray.put(jsonObj);
			}
		});
		
		H.writeToFile(rootObj, properiesPath);
	}

	public static void makeGroupMember(Person person, VBox vBoxGruppe_verschickt, VBox vBoxIn_Gruppe) {

		personListInGroup.add(person);
		buildGUIList4PersonListFromTo(personListInGroup, vBoxGruppe_verschickt, vBoxIn_Gruppe);
		
		H.appendLineToFile(person.getTitle(), cyPath);
	}

	public static void makeCustomer(Person person, VBox vBoxKunden) {

		moveToVBox(person, vBoxKunden);
		
		person.setState(vBoxKunden.getId());
		person.getStatusLabel().setText(vBoxKunden.getId());
		
		H.appendLineToFile(person.getTitle(), customersPath);
	}
}
