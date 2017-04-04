package com.haeorm.chatchat.loading;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;

public class LoadLayoutController {
	
	@FXML
	Text text;
	
	@FXML
	ProgressIndicator progress;
	
	
	public LoadLayoutController() {
		// TODO Auto-generated constructor stub
	}
	
	public void setTitle(String title)
	{
		text.setText(title);
	}
	
	public Text getText(){
		return text;
	}
	
	public ProgressIndicator getProgress()
	{
		return progress;
	}


}
