package de.ks.messageOrg.app;

import java.io.IOException;

import de.ks.messageOrg.DataReader.DataReader;
import de.ks.messageOrg.gui.GuiController;
import de.ks.messageOrg.model.Person;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class StartApp extends Application {
	
	private Stage stage;
	
	/*
	 * 
	 * START UP
	 * 
	 */

	public static void main(String[] args) {

		launch("gui.fxml");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		DataReader.readJsonData();

		System.out.println("Baue GUI auf...");
		
		stage = primaryStage;
		
		startWindow(getParameters().getUnnamed().get(0));
	}

	private void startWindow(String fxmlName) {
		
//		App.setAppInstance(this);

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
	}

	public void showPerson(Person person) {

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
}