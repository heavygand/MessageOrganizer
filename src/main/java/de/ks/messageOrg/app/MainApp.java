package de.ks.messageOrg.app;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

public class MainApp extends Application {

	private static String				inboxPath		= "C:\\Users\\erikd\\Downloads\\messages\\inbox";
	private static String				friendsPath		= "C:\\Users\\erikd\\Downloads\\friends\\friends.json";
	private static String				ausnahmePath	= "./docs/ausgenommen";
	private static ArrayList<Person>	persons			= new ArrayList<Person>();
	private static ArrayList<String>	friends			= new ArrayList<String>();
	private static ArrayList<Handler>	handlers		= new ArrayList<>();
	private static Person				currentPerson;
	private static ObservableList<Node> userList;
	private static Label anzeige;

	public static void main(String[] args) {

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

	public static void buildUp(VBox vBox, Label anzeige) {
		
		if(persons.isEmpty()) init(vBox, anzeige);
		
		showAll();
	}

	public static void showUnwritten() {

		userList.removeAll(userList);
		
		persons.forEach(person -> {
			
			if(person.getMessages().size() == 1) {
				
				addToList(person);
			}
		});
		
		anzeige.setText(userList.size() + " Personen");
	}

	public static void showWritten() {

		userList.removeAll(userList);
		
		ArrayList<String> notOk = new ArrayList<>();
		
		notOk.add("https://www.facebook.com/groups/197111937786655/");
		notOk.add("https://www.youtube.com/watch?v=A2_xGg_O2lE");
		notOk.add("https://youtu.be/opBGuztAvtw");
		notOk.add("https://youtu.be/gJxp4loJyyA");
		
		persons.forEach(person -> {
			
			if(person.getMessages().size() > 1 && !checkContain(notOk, person)) {
				
				addToList(person);
			}
		});
		
		anzeige.setText(userList.size() + " Personen");
	}

	public static void showGroup() {
		
		ArrayList<String> ok = new ArrayList<>();
		ArrayList<String> notOk = new ArrayList<>();
		
		ok.add("https://www.facebook.com/groups/197111937786655/");
		notOk.add("https://www.youtube.com/watch?v=A2_xGg_O2lE");
		notOk.add("https://youtu.be/opBGuztAvtw");
		notOk.add("https://youtu.be/gJxp4loJyyA");

		addPeopleWithRightMessages(ok, notOk);
	}

	/**
	 * @param notOkList 
	 * @param okList 
	 * 
	 */
	private static void addPeopleWithRightMessages(ArrayList<String> okList, ArrayList<String> notOkList) {

		userList.removeAll(userList);
		
		persons.forEach(person -> {
			
			if(checkContain(okList, person) && !checkContain(notOkList, person)) addToList(person);
		});
		
		anzeige.setText(userList.size() + " Personen");
	}

	/**
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

	public static void showVideo() {

		ArrayList<String> ok = new ArrayList<>();
		ArrayList<String> notOk = new ArrayList<>();
		
		ok.add("https://www.youtube.com/watch?v=A2_xGg_O2lE");
		ok.add("https://youtu.be/opBGuztAvtw");
		ok.add("https://youtu.be/gJxp4loJyyA");

		addPeopleWithRightMessages(ok, notOk);
	}

	private static void showAll() {

		userList.removeAll(userList);
		
		persons.forEach(person -> {
			
			addToList(person);
		});
		
		anzeige.setText(userList.size() + " Personen");
	}

	private static void init(VBox vBox, Label anzeige2) {

		anzeige = anzeige2;
		userList = vBox.getChildren();
		
		getFriends();

		getMessages();
	}

	/**
	 * 
	 */
	private static void getFriends() {

		File friendsFile = new File(friendsPath);
		String friendsText = H.readFile(friendsFile);
		
		JSONObject jsonArrObj = new JSONObject(friendsText);
		JSONArray jsonArr = jsonArrObj.getJSONArray("friends");
		
		jsonArr.forEach(friendObj -> {
			
			JSONObject fjo = (JSONObject) friendObj;
			String friendString = fjo.get("name").toString();
			friends.add(friendString);
		});
	}

	/**
	 * 
	 */
	private static void getMessages() {

		File folder = new File(inboxPath);
		File[] listOfFiles = folder.listFiles();
		for (File node : listOfFiles) {
			
			if (node.isFile()) continue;
			
			JSONObject jsonObj = new JSONObject(H.readFile(node.getPath() + "\\message.json"));
			
			if (	!jsonObj.has("title") ||
					isAusnahme(jsonObj.get("title").toString()) ||
					!friends.contains(jsonObj.get("title").toString())
					) continue;
			
			Person newPerson = new Person();
			setCurrentPerson(newPerson);
			readAttributes(jsonObj);
			persons.add(newPerson);
			
			addToList(newPerson);
		}
	}

	private static boolean isAusnahme(String name) {

		File ausnahmeFile = new File(ausnahmePath);
		
		for (String line : H.getLines(ausnahmeFile)) {

			if(line.equals(name)) return true;
		}
		
		return false;
	}

	private static void addToList(Person person) {
		
		HBox personBox = new HBox();
		personBox.setPrefHeight(32);
		personBox.setPrefWidth(600);
		userList.add(personBox);
		
		String name = person.getTitle();
		Label label = new Label(name);
		label.setPrefWidth(200);
		personBox.getChildren().add(label);

		long timeStampLong = ((Message)person.getMessages().toArray()[0]).getTimestamp_ms();
        Timestamp timeStamp = new Timestamp(timeStampLong);  
        LocalDateTime triggerTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp.getTime()), TimeZone.getDefault().toZoneId());
        String formattedDate = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm", Locale.GERMAN).format(triggerTime);
        
        Label dateLabel = new Label(formattedDate);
        dateLabel.setPrefWidth(300);
        personBox.getChildren().add(dateLabel);        
		
		label.setOnMouseClicked(e -> {
			
			ObservableList<Node> children = personBox.getChildren();
			int indexOf = children.indexOf(label);
			Node node = children.get(indexOf);
			TextField tf = new TextField();
			tf.setText(name);
			tf.setPrefWidth(300);
			children.set(indexOf, tf);
		});
	}

	/**
	 * @param jsonObj
	 * @param newPerson
	 */
	private static void readAttributes(JSONObject jsonObj) {

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

		startWindow(primaryStage, getParameters().getUnnamed().get(0));
		
	}

	private void startWindow(Stage stage, String fxmlName) {

		Pane pane = null;
		try {
			pane = FXMLLoader.load(getClass().getResource(fxmlName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(pane);
		scene.setUserData(fxmlName);
		GuiController.doStuff(scene);
		stage.setScene(scene);
		stage.show();
		
	}
}
