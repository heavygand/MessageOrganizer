package de.ks.messageOrg.gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import de.ks.messageOrg.app.MainApp;
import de.ks.messageOrg.model.Message;
import de.ks.messageOrg.model.Person;
import helpers.H;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

@SuppressWarnings("restriction")
public class GuiController {
	
	private static VBox vBoxGruppe_verschickt;
	private static VBox vBoxIn_Gruppe;
	private static Scene mainScene;
	private static VBox vBoxKunden;

	public static void initMainWindow(Scene scene) {
		
		mainScene = scene;
		if (!scene.getUserData().equals("gui.fxml")) return;

		ToolBar toolbar = (ToolBar) scene.lookup("#toolbar");
		Label anzeige = (Label)toolbar.getItems().get(0);
		MainApp.setPersonenAnzahlAnzeige(anzeige);

		TabPane tabPane = (TabPane) scene.lookup("#tabPane");
		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				MainApp.refreshAnzeige((VBox) newValue.getContent());
			}
		});
		
		VBox vBoxUnangeschriebene = (VBox) scene.lookup("#Unangeschriebene");
		MainApp.showUnwritten(vBoxUnangeschriebene);

		VBox vBoxAngeschriebene = (VBox) scene.lookup("#Angeschriebene");
		MainApp.showWritten(vBoxAngeschriebene);
		
		vBoxGruppe_verschickt = (VBox) scene.lookup("#Gruppe_verschickt");
		MainApp.showGroup(vBoxGruppe_verschickt);
		
		vBoxIn_Gruppe = (VBox) scene.lookup("#In_Gruppe");
		MainApp.showInGroup(vBoxIn_Gruppe, vBoxGruppe_verschickt);
		
		VBox vBoxNachfassen = (VBox) scene.lookup("#Nachfassen");
		MainApp.showNachzufassen(vBoxNachfassen);

		VBox vBoxVideo = (VBox) scene.lookup("#Video");
		MainApp.showVideo(vBoxVideo);
		
		VBox vBoxAlle = (VBox) scene.lookup("#Alle");
		MainApp.showAll(vBoxAlle);
		
		vBoxKunden = (VBox) scene.lookup("#Kunden");
		MainApp.showCustomers(vBoxKunden);
		
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
		person.setStatusLabel(currentStatus);
		
		TextArea notesArea = (TextArea) scene.lookup("#notesArea");
		notesArea.setText(person.getNotes());
		
		Button saveButton = (Button) scene.lookup("#saveButton");
		saveButton.setUserData(person);
		saveButton.setOnAction(e -> MainApp.savePersonNotes((Person)saveButton.getUserData(), notesArea, nachfassenAm));
		notesArea.setText(person.getNotes());
		
		Button istInGruppeButton = (Button) scene.lookup("#istInGruppeButton");
		istInGruppeButton.setUserData(person);
		istInGruppeButton.setOnAction(e -> MainApp.makeGroupMember((Person)istInGruppeButton.getUserData(), vBoxGruppe_verschickt, vBoxIn_Gruppe ) );
		
		Button istKundeButton = (Button) scene.lookup("#istKundeButton");
		istKundeButton.setUserData(person);
		istKundeButton.setOnAction(e -> MainApp.makeCustomer((Person)istKundeButton.getUserData(), vBoxKunden));
	}

	public static VBox getVBox(String state) {

//		System.out.println("Suche #" + state + " in der GUI");
		return (VBox) mainScene.lookup("#"+state);
	}
}
