package com.haeorm.chatchat.node;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

public class ChatNodeController implements Initializable {

	enum NODE_SENDER_TYPE {CLIENT, OTHER, NOTICE};
	enum NODE_MESSAGE_TYPE {MESSAGE, NOTICE, PRIVACY, FILE, IMAGE};
	
	private boolean chatRelated = false;
	
	
	@FXML Text Sender;
	@FXML Text Message;
	@FXML Text Time;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
