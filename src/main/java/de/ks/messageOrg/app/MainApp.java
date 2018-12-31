package de.ks.messageOrg.app;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import de.ks.messageOrg.DataReader.DataReader;
import de.ks.messageOrg.gui.GuiController;
import de.ks.messageOrg.model.Message;
import de.ks.messageOrg.model.Person;
import de.ks.messageOrg.model.util.MessageCreator;
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
	private static String				inboxPath			= "C:\\Users\\erikd\\Downloads\\messages\\inbox";
	private static String				friendsPath			= "C:\\Users\\erikd\\Downloads\\friends\\friends.json";
	private static String				generatedPathDB		= "C:\\Users\\erikd\\Dropbox\\Apps\\MessageOrganizer\\writeToList.txt";
	private static String				ausnahmePath		= "./docs/ausgenommen";
	private static String				blackListPath		= "./docs/blacklist";
	private static String				properiesPath		= "./docs/properties";
	private static String				cyPath				= "./docs/CY.txt";
	private static String				customersPath		= "./docs/Kunden.txt";

	private static ArrayList<Person>	persons				= new ArrayList<Person>();
	private static ArrayList<Person> 	personListInGroup	= new ArrayList<Person>();
	private static ArrayList<Person>	personListUnwritten	= new ArrayList<Person>();
	private static Person				currentPerson;
	private static ObservableList<Node> currentUserList;
	private static Label 				anzahlPersonenAnzeige;
	private final int 					daysBack			= 3;
	private Stage stage;
	private static MainApp appInstance;
	private static long starttime;
	private static VBox currentVBox;
	
	/*
	 * 
	 * START UP
	 * 
	 */

	public static void main(String[] args) {
		
		starttime = System.currentTimeMillis();

		launch("gui.fxml");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		DataReader.readTextFilesForInitialization();

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
	
	/*
	 * 
	 * LADEN DER LISTEN
	 * 
	 */

	public static void showAll(VBox vBox) {

		currentUserList = vBox.getChildren();

		persons.forEach(person -> {
			
			addToCurrentGUIList(person.getState(), person);
		});
	}

	public static void showUnwritten(VBox vBox) {

		currentUserList = vBox.getChildren();
		
		List<String> cyPathLines = H.getLines(cyPath);
		
		persons.forEach(person -> {
			
			if(!hasMessageFromMe(person) && !cyPathLines.contains(person.getTitle())) {
				
				addToCurrentGUIList(vBox.getId(), person);
				personListUnwritten.add(person);
			}
		});
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

	public static void showInGroup(VBox vBoxInGroup) {
		
		long newStarttime = System.currentTimeMillis();
		System.out.println("Lese CY Gruppenmitglieder ein...");

		currentUserList = vBoxInGroup.getChildren();
		ArrayList<String> notOk = new ArrayList<>();
		
		notOk.add("https://www.youtube.com/watch?v=A2_xGg_O2lE");
		notOk.add("https://youtu.be/opBGuztAvtw");
		notOk.add("https://youtu.be/gJxp4loJyyA");
		
		H.getLines(cyPath).forEach(line -> {
			
			Person person = getPersonInPersonList(line);
			
			if(person == null && isAusnahme(line)) {} //System.out.println(line + " ist ausnahme");
			else if(person == null) System.out.println(line + " ist nicht bei den Personen, aber in " + vBoxInGroup.getId());
			
			if(person != null && !checkContain(notOk, person)) {
				
				personListInGroup.add(person);
				moveToVBox(person, vBoxInGroup);
			}
		});
		
		System.out.println("CY Gruppenmitglieder hat " + H.getSeconds(System.currentTimeMillis() - newStarttime) + " sekunden gedauert");
	}

	private static boolean isAusnahme(String name) {

		File ausnahmeFile = new File(ausnahmePath);
		
		for (String kackLine : H.getLines(ausnahmeFile)) {

			String line = H.cleanUp(kackLine);
			
			if(line.equals(name)) return true;
		}
		
		return false;
	}

	public static void showNachzufassen(VBox vBox) {

		currentUserList = vBox.getChildren();
		
		persons.forEach(person -> {
			
			if(person.getNachfassen() != 0 && H.isEarlierOrToday(person.getNachfassen())) {
				
				addToCurrentGUIList(null, person);
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
		
		vBoxKunden.getChildren().clear();
		
		H.getLines(customersPath).forEach(personString -> {
			
			Person person = getPersonInPersonList(personString);
				
			moveToVBox(person, vBoxKunden);
		});
	}
	
	/*
	 * 
	 * HELPERS
	 * 
	 */

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
	
	private static void addPeopleWithRightMessagesToCurrentGUIList(String status, ArrayList<String> okList, ArrayList<String> notOkList) {

		currentUserList.removeAll(currentUserList);
		
		persons.forEach(person -> {
			
			if(checkContain(okList, person) && !checkContain(notOkList, person)) {
				
				addToCurrentGUIList(status, person);
			}
		});
	}

	/**
	 * @param person
	 * @param vBoxTo
	 */
	private static void moveToVBox(Person person, VBox vBoxTo) {
		
		removeFromGUIList(person);
		addToGUIList(vBoxTo, person);
		
		person.setState(vBoxTo.getId());
		
		if (person.getStatusLabel() != null) {
			
			person.getStatusLabel().setText(vBoxTo.getId());
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void moveToVBox(ArrayList<Person> persons, VBox vBoxTo) {

//		Collections.sort(persons);
		
		vBoxTo.getChildren().clear();

		persons.forEach(person -> moveToVBox(person, vBoxTo));
	}

	/**
	 * @param person
	 * @return
	 */
	private static VBox getCurrentVBox4Person(Person person) {

		String state = person.getState();
		VBox vBoxOld = GuiController.getVBox(state);
		return vBoxOld;
	}

	private static Person getPersonInPersonList(String name) {

		for (Person person : persons) {
			
			if(person.getTitle().equals(name)) return person;
		}
		
		return null;
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

		long timeStampLongLast = getLastMessageFromPerson(person)==null ? 0 : getLastMessageFromPerson(person).getTimestamp_ms();        
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

	private static void removeFromGUIList(ArrayList<Person> personList) {
		
		personList.forEach(person -> removeFromGUIList(person));
	}

	private static void removeFromGUIList(Person person) {
		
		ArrayList<Object> toRemove = new ArrayList<>();

		VBox vBox = getCurrentVBox4Person(person);
		
		if(vBox==null) return;
		
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

	public static Person getCurrentPerson() {

		return currentPerson;
	}

	public static void setCurrentPerson(Person currentPerson) {

		MainApp.currentPerson = currentPerson;
	}


	/**
	 * @return the anzeige
	 */
	public static Label getAnzeige() {
	
		return anzahlPersonenAnzeige;
	}

	
	/**
	 * @param anzeige the anzeige to set
	 */
	public static void setPersonenAnzahlAnzeige(Label anzeige) {
	
		MainApp.anzahlPersonenAnzeige = anzeige;
	}
	
	private static void refreshAnzeige() {
		
		anzahlPersonenAnzeige.setText(currentUserList.size() + " Personen");
	}

	public static void refreshAnzeige(VBox vBox) {

		currentUserList = vBox.getChildren();
		anzahlPersonenAnzeige.setText(currentUserList.size() + " Personen");
	}

	public static void savePersonNotes(Person person, TextArea notesArea, DatePicker nachfassenAm) {

		String notes = notesArea.getText();
		LocalDate dpValue = nachfassenAm.getValue();

		if (notes != null) {
			
			person.setNotes(notes);
		}
		
		if (dpValue != null) {
			
			person.setNachfassen(H.getTimeStampAsLong(dpValue));
		}
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
		
		H.writeJSONObjectToFile(rootObj, properiesPath);
	}

	public static void makeGroupMember(Person person, VBox vBoxGruppe_verschickt, VBox vBoxIn_Gruppe) {

		personListInGroup.add(person);
		moveToVBox(personListInGroup, vBoxIn_Gruppe);
		
		refreshAnzeige();
		person.setState(vBoxIn_Gruppe.getId());
		person.getStatusLabel().setText(vBoxIn_Gruppe.getId());
		
		H.appendToFile(person.getTitle(), cyPath);
	}

	public static void makeCustomer(Person person, VBox vBoxKunden) {

		moveToVBox(person, vBoxKunden);
		
		H.appendToFile(person.getTitle(), customersPath);
	}
	
	private static List<Person> getCurrentPersonListFromGUI() {

		ArrayList<Person> localPersons = new ArrayList<>();
		
		for(Node userNode : currentUserList) {
			
			HBox userHBox = (HBox) userNode;
			
			Label nameLabel = (Label) userHBox.getChildren().get(0);
			
			Person person = getPersonInPersonList(nameLabel.getText());
			
			localPersons.add(person);
		}
		
		return localPersons;
	}

	public static void generatePersonList(String text) {

		int amount = Integer.parseInt(text);
		
		ArrayList<Person> localPersons  = new ArrayList<Person>(getCurrentPersonListFromGUI().subList(0, amount));
		
		if (currentVBox.getId().equals("Unangeschriebene")) {
			
			moveToVBox(localPersons, GuiController.getVBox("Angeschriebene"));
			personListUnwritten.removeAll(localPersons);
		}
		refreshAnzeige();
		
		H.appendToFile(getNameListFromPersons(localPersons), generatedPathDB);
	}

	private static List<String> getNameListFromPersons(ArrayList<Person> localPersons) {
		
		ArrayList<String> nameList = new ArrayList<String>();

		for(Person person : localPersons) {
			
			nameList.add(person.getTitle());
		}
		
		return nameList;
	}

	public static void search(String name) {

		Person person = getPersonInPersonList(name);
		
		if(person == null) {
			
			Alert errorAlert = new Alert(Alert.AlertType.ERROR);
			errorAlert.setHeaderText("Fehler");
			errorAlert.setContentText("Diese Person konnte nicht gefunden werden");
			errorAlert.showAndWait();
		}
		else {
			
			appInstance.showPerson(person);
		}
	}

	public static void setCurrentVBox(VBox newVBox) {

		currentVBox = newVBox;
	}

	public static void showNoReaction(VBox vBox) {

		currentUserList = vBox.getChildren();
		
		persons.forEach(person -> {
			
			if(hasMessageFromMe(person)) {
				
				addToCurrentGUIList(null, person);
			}
		});
	}

	public static void showOld(VBox vBoxAlt) {

		// TODO Auto-generated method stub
		
	}
}
