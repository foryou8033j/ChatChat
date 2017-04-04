package com.haeorm.chatchat.loading;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoadLayout extends Stage {

	private Stage stage;

	private Stage primaryStage;
	
	private LoadLayoutController controller;

	public LoadLayout(String title, Stage stage) {
		super();

		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LoadLayout.fxml"));
			BorderPane pane = loader.load();

			controller = loader.getController();
			controller.setTitle(title);

			Scene scene = new Scene(pane);

			setScene(scene);

			initStyle(StageStyle.UNDECORATED);
			initOwner(stage);
			initModality(Modality.APPLICATION_MODAL);

		} catch (Exception e) {

		}

	}
	

	
	public void setContents(String title)
	{
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				controller.setTitle(title);
			}
		});
	}
	

	public void activateProgress(final Task<?> task){
		
		show();
		controller.getProgress().progressProperty().bind(task.progressProperty());
		
		
	}
	
	public void activateText(final Task<?> task){
		controller.getText().textProperty().bind(task.messageProperty());
	}
	
}
