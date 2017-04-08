package com.haeorm.chatchat.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;

public class ExceptionDialog extends Alert{
	
	String exceptionText;

	public ExceptionDialog(AlertType arg0, String title, String headerText, String contentsText, Exception exception) {
		super(arg0);
		setTitle(title);
		setHeaderText(headerText);
		setContentText(contentsText);
		
		drawPane(exception.toString());
	}
	
	public ExceptionDialog(AlertType arg0, String title, String headerText, String contentsText, String exception) {
		super(arg0);
		setTitle(title);
		setHeaderText(headerText);
		setContentText(contentsText);
		
		drawPane(exception);
	}
	
	public ExceptionDialog(AlertType arg0, String title, String headerText, String contentsText) {
		super(arg0);
		setTitle(title);
		setHeaderText(headerText);
		setContentText(contentsText);
	}
	
	public ExceptionDialog(AlertType arg0, String title, String headerText) {
		super(arg0);
		setTitle(title);
		setHeaderText(headerText);
		setContentText(null);
	}
	
	
	private void drawPane(String exceptionText)
	{
		
		Label label = new Label("The Exception stacktrace was : ");
		
		TextArea textArea = new TextArea(exceptionText);
		//textArea.getEngine().loadContent(exceptionText);
		
		textArea.setEditable(false);
		textArea.setWrapText(true);
		
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		
		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
		
		getDialogPane().setExpandableContent(expContent);
	}

	

}
