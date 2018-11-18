package de.ks.messageOrg.gui;

import de.ks.messageOrg.app.MainApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

@SuppressWarnings("restriction")
public class GuiController {
	
	public static void doStuff(Scene scene) {
		
		if (!scene.getUserData().equals("gui.fxml")) return;

		ToolBar toolbar = (ToolBar) scene.lookup("#toolbar");
		Label anzeige = (Label)toolbar.getItems().get(0);
		MainApp.setAnzeige(anzeige);

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
		
		VBox vBoxGruppe_verschickt = (VBox) scene.lookup("#Gruppe_verschickt");
		MainApp.showGroup(vBoxGruppe_verschickt);
		
		VBox vBoxIn_Gruppe = (VBox) scene.lookup("#In_Gruppe");
		MainApp.showInGroup(vBoxIn_Gruppe);

		VBox vBoxVideo = (VBox) scene.lookup("#Video");
		MainApp.showVideo(vBoxVideo);
		
		VBox vBoxAlle = (VBox) scene.lookup("#Alle");
		MainApp.showAll(vBoxAlle);
		
//		c = new Counter();
//		c.getPropertyChangeSupport().addPropertyChangeListener(c.PROPERTY_NUMBER, (a) -> {
//
//			tf.setText("" + c.getNumber());
//		});
	}
}
