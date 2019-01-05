package de.ks.messageOrg.gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import de.ks.messageOrg.app.App;
import de.ks.messageOrg.model.Message;
import de.ks.messageOrg.model.Person;
import helpers.H;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

@SuppressWarnings("restriction")
public class GuiController {
	
	private static Scene mainScene;
	private static GUIList vBoxGruppe_verschickt;
	private static GUIList vBoxIn_Gruppe;
	private static GUIList vBoxKunden;
	private static GUIList vBoxKeineReaktion;
	private static GUIList vBoxAlt;
	private static GUIList vBoxAlle;
	private static GUIList currentVBox = (GUIList) vBoxAlle;

	class GUIList extends VBox{
		
		public String name;
		public boolean loaded;
		
		void loadData() {
			
			H.callMethod("App", "get"+name);
		}
	}

	public static void initMainWindow(Scene scene) {
		
		mainScene = scene;
		if (!scene.getUserData().equals("gui.fxml")) return;
		
		// Anzahl Anzeige
		ToolBar toolbar = (ToolBar) scene.lookup("#toolbar");
		Label anzahlAnzeige = (Label)toolbar.getItems().get(0);

		// Listener für das Wechseln eines Tabs
		TabPane tabPane = (TabPane) scene.lookup("#tabPane");
		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				
				currentVBox = (GUIList) newValue.getContent();
				currentVBox.loadData();
			}
		});
		
		/*
		 * 
		 * DIE VERSCHIEDENEN TABS
		 * 
		 */
		String name;
		
		name = "Unwritten";
		GUIList vBoxUnangeschriebene = (GUIList) scene.lookup("#" + name);
		vBoxUnangeschriebene.name = name;

		GUIList vBoxAngeschriebene = (GUIList) scene.lookup("#Written");
		vBoxAngeschriebene.name = "Written";
		
		vBoxGruppe_verschickt = (GUIList) scene.lookup("#GroupSend");
		vBoxGruppe_verschickt.name = "GroupSend";
		
		vBoxIn_Gruppe = (GUIList) scene.lookup("#In_Gruppe");
		vBoxIn_Gruppe.name = "InGroup";
		
		GUIList vBoxNachfassen = (GUIList) scene.lookup("#Nachfassen");
		vBoxNachfassen.name = "Nachzufassen";

		GUIList vBoxVideo = (GUIList) scene.lookup("#Video");
		vBoxVideo.name = "Video";
		
		vBoxKunden = (GUIList) scene.lookup("#Kunden");
		vBoxKunden.name = "Customers";
		
//		vBoxKeineReaktion = (VBox) scene.lookup("#keineReaktion");
//		MainApp.showNoReaction(vBoxKeineReaktion);
//		
//		vBoxAlt = (VBox) scene.lookup("#alt");
//		MainApp.showOld(vBoxAlt);
		
		vBoxAlle = (GUIList) scene.lookup("#Alle");
		vBoxAlle.name = "Customers";
		
		/*
		 * 
		 * WERKZEUGLEISTE
		 * 
		 */
		TextField searchField = (TextField) toolbar.getItems().get(2);
		
		Button search = (Button) toolbar.getItems().get(3);
		String name1 = searchField.getText();
		search.setOnAction(e -> {
			
			if(!App.search(name1)) {
				
				Alert errorAlert = new Alert(Alert.AlertType.ERROR);
				errorAlert.setHeaderText("Fehler");
				errorAlert.setContentText(name1 + " konnte nicht gefunden werden");
				errorAlert.showAndWait();
			}
		});
		
		TextField generateAmount = (TextField) toolbar.getItems().get(5);
		
		Button generateButton = (Button) toolbar.getItems().get(6);
		generateButton.setOnAction(e -> App.generatePersonList(generateAmount.getText()));
		
//		c = new Counter();
//		c.getPropertyChangeSupport().addPropertyChangeListener(c.PROPERTY_NUMBER, (a) -> {
//
//			tf.setText("" + c.getNumber());
//		});
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
		saveButton.setOnAction(e -> App.savePersonNotes((Person)saveButton.getUserData(), notesArea, nachfassenAm));
		notesArea.setText(person.getNotes());
		
		Button istInGruppeButton = (Button) scene.lookup("#istInGruppeButton");
		istInGruppeButton.setUserData(person);
		istInGruppeButton.setOnAction(e -> App.makeGroupMember((Person)istInGruppeButton.getUserData(), vBoxGruppe_verschickt, vBoxIn_Gruppe ) );
		
		Button istKundeButton = (Button) scene.lookup("#istKundeButton");
		istKundeButton.setUserData(person);
		istKundeButton.setOnAction(e -> App.makeCustomer((Person)istKundeButton.getUserData(), vBoxKunden));
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
		long timeStampLongLast = getLastMessageFromPerson(person) == null ? 0
				: getLastMessageFromPerson(person).getTimestamp_ms();
		person.setLastContact(timeStampLongLast);
		String formattedDate = H.getGermanDateTimeString(timeStampLongFirst);
		if (timeStampLongFirst == 0)
			formattedDate = "";
		Label dateLabel = new Label(formattedDate);
		dateLabel.setPrefWidth(300);
		personBox.getChildren().add(dateLabel);
		if (status != null)
			person.setState(status);
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

	private static void addPeopleWithRightMessagesToCurrentGUIList(String status, ArrayList<String> okList,
			ArrayList<String> notOkList) {

		currentUserList.removeAll(currentUserList);
		persons.forEach(person -> {
			if (checkContain(okList, person) && !checkContain(notOkList, person)) {
				addToCurrentGUIList(status, person);
			}
		});
	}

	private static void moveToVBox(Person person, VBox vBoxTo) {

		removeFromGUIList(person);
		addToGUIList(vBoxTo, person);
		person.setState(vBoxTo.getId());
		if (person.getStatusLabel() != null) {
			person.getStatusLabel().setText(vBoxTo.getId());
		}
		refreshAnzeige();
	}

	@SuppressWarnings("unchecked")
	public static void moveToVBox(ArrayList<String> persons, String vBoxToName) {

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

		return currentVBox.name;
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
}
