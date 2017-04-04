package com.haeorm.chatchat.root.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.haeorm.chatchat.Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * 채팅 프로그램의 가장 기본이 되는 Scene
 * @author Jeongsam
 * @since 2017-04-04
 * @version 0.1
 *
 */
public class RootLayoutController implements Initializable{

	@FXML BorderPane rootPane;
	
	@FXML ListView chatListView;
	@FXML TextField chatInputBox;
	@FXML Button sendMessageButton;
	@FXML Button clearChatListButton;
	
	@FXML Button menuButton;

	private Client client;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	public RootLayoutController() {
		
	}
	
	/**
	 * 클라이언트와 연동한다.
	 */
	public void setClient(Client client){
		this.client = client;
		
		
		
	}
	
	
	
	
}
