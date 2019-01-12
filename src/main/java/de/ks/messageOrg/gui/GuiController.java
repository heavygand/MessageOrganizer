package de.ks.messageOrg.gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import de.ks.messageOrg.app.App;
import de.ks.messageOrg.app.StartApp;
import de.ks.messageOrg.model.Message;
import de.ks.messageOrg.model.Person;
import helpers.H;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

@SuppressWarnings("restriction")
public class GuiController {
	
	private static Scene mainScene;
	private static VBox vBoxKunden;
	private static VBox vBoxAlle;
	private static VBox currentVBox = (VBox) vBoxAlle;
	private static ObservableList<Node> currentUserList;
	private static Label anzahlPersonenAnzeige;

	public static void initMainWindow(Scene scene) {
		
		mainScene = scene;
		if (!scene.getUserData().equals("gui.fxml")) return;
		
		// Anzahl Anzeige
		ToolBar toolbar = (ToolBar) scene.lookup("#toolbar");
		anzahlPersonenAnzeige = (Label)toolbar.getItems().get(0);

		// Listener für das Wechseln eines Tabs
		TabPane tabPane = (TabPane) scene.lookup("#tabPane");
		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				
				currentVBox = (VBox) newValue.getContent();
				currentUserList = currentVBox.getChildren();
				
				boolean initialized = currentUserList.size() > 0;
				if (!initialized ) {
					
					loadCurrentList();
				}
			}
		});
		
		/*
		 * 
		 * DIE VERSCHIEDENEN TABS
		 * 
		 */
//		String name;
//		
//		name = "unwritten";
//		VBox vBoxUnangeschriebene = (VBox) scene.lookup("#" + name);
//
//		VBox vBoxAngeschriebene = (VBox) scene.lookup("#written");
//		
//		VBox vBoxGruppe_verschickt = (VBox) scene.lookup("#group_send");
//		
//		VBox vBoxIn_Gruppe = (VBox) scene.lookup("#in_group");
//		
//		VBox vBoxNachfassen = (VBox) scene.lookup("#Nachfassen");
//
//		VBox vBoxVideo = (VBox) scene.lookup("#Video");
		
		vBoxKunden = (VBox) scene.lookup("#Kunden");
		
//		vBoxKeineReaktion = (VBox) scene.lookup("#keineReaktion");
//		MainApp.showNoReaction(vBoxKeineReaktion);
//		
//		vBoxAlt = (VBox) scene.lookup("#alt");
//		MainApp.showOld(vBoxAlt);
		
		vBoxAlle = (VBox) scene.lookup("#Persons");
		currentVBox = vBoxAlle;
		currentUserList = currentVBox.getChildren();
		
		loadCurrentList();
		
		/*
		 * 
		 * WERKZEUGLEISTE
		 * 
		 */
		TextField searchField = (TextField) toolbar.getItems().get(2);
		if(!searchField.getId().equals("searchField")) System.err.println("Das war nicht searchField");
		
		Button search = (Button) toolbar.getItems().get(3);
		search.setOnAction(e -> {
			
			String name1 = searchField.getText();
			if(!App.search(name1)) {
				
				Alert errorAlert = new Alert(Alert.AlertType.ERROR);
				errorAlert.setHeaderText("Fehler");
				errorAlert.setContentText(name1 + " konnte nicht gefunden werden");
				errorAlert.showAndWait();
			}
		});
		
		TextField generateAmount = (TextField) toolbar.getItems().get(5);
		
		Button generateButton = (Button) toolbar.getItems().get(6);
		generateButton.setOnAction(e -> App.generatePersonList(generateAmount.getText(), currentVBox.getId()));
		
//		c = new Counter();
//		c.getPropertyChangeSupport().addPropertyChangeListener(c.PROPERTY_NUMBER, (a) -> {
//
//			tf.setText("" + c.getNumber());
//		});
	}

	private static void loadCurrentList() {
		
		ArrayList<Person> persons = App.getPersons4Tab(currentVBox.getId());
		addToVBox(currentUserList, persons);
		refreshAnzeige();
	}
	
	public static void initPersonWindow(Scene scene, Person person) {
		
		if(scene == null) System.out.println("scene war null");
		
		VBox messageList = (VBox) scene.lookup("#messageList");
		for(Message message : person.getMessages()) {
			
			Label headerLabel = new Label();
			Label messageLabel = new Label();
			String sender = message.getSender_name();
			
			String date = H.getGermanDateString(message.getTimestamp_ms());
			String hour = H.getGermanHourString(message.getTimestamp_ms());
			
			headerLabel.setText(sender + " sendete am " + date + " um " + hour + ":\n");
			headerLabel.setStyle("-fx-font-weight: bold");
			messageLabel.setText(message.getContent() +	"\n\n");
			
			headerLabel.setPrefWidth(600);
			headerLabel.setWrapText(true);
			
			messageLabel.setPrefWidth(600);
			messageLabel.setWrapText(true);
			
			messageList.getChildren().add(headerLabel);
			messageList.getChildren().add(messageLabel);
		}
		
		TextField nameTextField = (TextField) scene.lookup("#nameTextField");
		nameTextField.setText(person.getTitle());
		
		Label friendsSince = (Label) scene.lookup("#friendsSince");
		if(person.getFriendsSince()==0) System.out.println("ACHTUNG: " + person.getTitle() + " hat kein friendsSince");
		friendsSince.setText(H.getGermanDateTimeString(person.getFriendsSince()));
		
		Label firstContact = (Label) scene.lookup("#firstContact");
		firstContact.setText(person.getFirstContact()==0?"":H.getGermanDateTimeString(person.getFirstContact()));
		
		Label lastContact = (Label) scene.lookup("#lastContact");
		lastContact.setText(person.getLastContact()==0?"":H.getGermanDateTimeString(person.getLastContact()));
		
		DatePicker nachfassenAm = (DatePicker) scene.lookup("#nachfassenAm");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		if(person.getNachfassen()!=0) nachfassenAm.setValue(LocalDate.parse(H.getGermanDateString(person.getNachfassen()), formatter));
		
		Label currentStatus = (Label) scene.lookup("#currentStatus");
		currentStatus.setText(person.getState());
		
		TextArea notesArea = (TextArea) scene.lookup("#notesArea");
		notesArea.setText(person.getNotes());
		
		Button saveButton = (Button) scene.lookup("#saveButton");
		saveButton.setUserData(person);
		saveButton.setOnAction(e -> App.savePersonNotes((Person)saveButton.getUserData(), notesArea.getText(), nachfassenAm.getValue()));
		notesArea.setText(person.getNotes());
		
//		Button istInGruppeButton = (Button) scene.lookup("#istInGruppeButton");
//		istInGruppeButton.setUserData(person);
//		istInGruppeButton.setOnAction(e -> {
//			
//			currentStatus.setText(vBoxIn_Gruppe.getId());
//			makeGroupMember((Person)istInGruppeButton.getUserData(), vBoxGruppe_verschickt, vBoxIn_Gruppe );
//		});
		
		Button istKundeButton = (Button) scene.lookup("#istKundeButton");
		istKundeButton.setUserData(person);
		istKundeButton.setOnAction(e -> {
			
			currentStatus.setText(vBoxKunden.getId());
			makeCustomer((Person)istKundeButton.getUserData(), vBoxKunden);
		});
	}

	private static VBox getVBox(String state) {

		VBox vBox = null;
		try {
			
			vBox = (VBox) mainScene.lookup("#" + state);
		}
		catch (ClassCastException e) {
			
			System.out.println("Suche #" + state + " in der GUI");
		}
		
		return vBox;
	}

//	private static void addToCurrentVBox(String status, Person person) {
//
//		addToVBox(currentUserList, person, status);
//	}
	
	private static void addToVBox(ObservableList<Node> currentUserList, List<Person> persons) {
		
		for (Person person : persons) {
			
			addToVBox(currentUserList, person);
		}
	}

	private static void addToVBox(VBox vBox, Person person) {

		ObservableList<Node> children = vBox.getChildren();
		addToVBox(children, person);
	}

	private static void addToVBox(ObservableList<Node> currentUserList, Person person) {

		// Build Personbox
		HBox personBox = new HBox();
		personBox.setPrefHeight(32);
		personBox.setPrefWidth(600);
		
		String name = person.getTitle();
		Label label = new Label(name);
		label.setPrefWidth(200);
		personBox.getChildren().add(label);
		
		// And add to the GUI userlist
		currentUserList.add(personBox);
		
		label.setOnMouseClicked(e -> {
			
			StartApp.showPerson(person);
		});
	}

//	private static void removeFromVBox(ArrayList<Person> personList) {
//
//		personList.forEach(person -> removeFromVBox(person));
//	}

	private static void removeFromVBox(Person person) {

		ArrayList<Object> toRemove = new ArrayList<>();
		VBox vBox = getCurrentVBox4Person(person);
		if (vBox == null)
			return;
		ObservableList<Node> children = vBox.getChildren();
		for (Object personBoxObj : children) {
			HBox personBox = (HBox) personBoxObj;
			for (Object obj : personBox.getChildren()) {
				if (obj instanceof Label && ((Label) obj).getText().equals(person.getTitle())) {
					toRemove.add(personBoxObj);
				}
			}
		}
		for (Object obj : toRemove) {
			children.remove(obj);
		}
	}

	private static void moveToVBox(Person person, VBox vBoxTo) {

		removeFromVBox(person);
		addToVBox(vBoxTo, person);

		refreshAnzeige();
	}

	public static void moveToVBox(ArrayList<Person> persons, String vBoxToName) {

		VBox vBoxTo = getVBox(vBoxToName);
		// Collections.sort(persons);
		vBoxTo.getChildren().clear();
		persons.forEach(person -> moveToVBox(person, vBoxTo));
	}

	private static VBox getCurrentVBox4Person(Person person) {

		String state = person.getState();
		VBox vBoxOld = GuiController.getVBox(state);
		return vBoxOld;
	}

	public static void refreshAnzeige(VBox vBox) {

		currentUserList = vBox.getChildren();
		anzahlPersonenAnzeige.setText(currentUserList.size() + " Personen");
	}

	public static void refreshAnzeige() {

		anzahlPersonenAnzeige.setText(currentUserList.size() + " Personen");
	}

	public static ArrayList<String> getCurrentPersonListFromGUI() {

		ArrayList<String> localPersons = new ArrayList<>();
		for (Node userNode : currentVBox.getChildren()) {
			
			HBox userHBox = (HBox) userNode;
			Label nameLabel = (Label) userHBox.getChildren().get(0);
			String name = nameLabel.getText();
			localPersons.add(name);
		}
		return localPersons;
	}

	public static String getCurrentTab() {

		return currentVBox.getId();
	}

//	public static void makeGroupMember(Person person, VBox vBoxGruppe_verschickt, VBox vBoxIn_Gruppe) {
//
////		personListInGroup.add(person);
//		moveToVBox(person, vBoxIn_Gruppe);
////		refreshAnzeige();
//		
//		App.savePersonState(person, vBoxIn_Gruppe.getId());
//	}

	public static void makeCustomer(Person person, VBox vBoxKunden) {

		moveToVBox(person, vBoxKunden);
		App.savePersonState(person, vBoxKunden.getId());
	}
}
