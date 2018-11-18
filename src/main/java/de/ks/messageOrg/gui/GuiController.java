package de.ks.messageOrg.gui;

import de.ks.messageOrg.app.MainApp;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

public class GuiController {
	
	public static void doStuff(Scene scene) {
		
		if (!scene.getUserData().equals("gui.fxml")) return;

		ScrollPane scrollTeil = (ScrollPane) scene.lookup("#scrollTeil");
		if(scrollTeil == null) System.out.println("scrollTeil ist null");
		

		Label anzeige = (Label) scene.lookup("#tf");
		if(anzeige == null) System.out.println("anzeige ist null");
		
		Button read = (Button) scene.lookup("#read");
		if(read == null) System.out.println("read button ist null");
		read.setOnAction(e -> {
			
			VBox vBox = (VBox) scene.lookup("#wtf");
			if(vBox == null) System.out.println("vBox ist null");
			MainApp.buildUp(vBox, anzeige);
		});
		
		Button unwritten = (Button) scene.lookup("#unwritten");
		unwritten.setOnAction(e -> MainApp.showUnwritten());
		
		Button written = (Button) scene.lookup("#written");
		written.setOnAction(e -> MainApp.showWritten());
		
		Button group = (Button) scene.lookup("#group");
		group.setOnAction(e -> MainApp.showGroup());
		
		Button video = (Button) scene.lookup("#video");
		video.setOnAction(e -> MainApp.showVideo());
		
		
//		Button reset = (Button) scene.lookup("#reset");
//		reset.setOnAction(e -> reset());
//		TextField tf = (TextField) scene.lookup("#textfeld");
//		c = new Counter();
//		c.getPropertyChangeSupport().addPropertyChangeListener(c.PROPERTY_NUMBER, (a) -> {
//
//			tf.setText("" + c.getNumber());
//		});
	}
}
