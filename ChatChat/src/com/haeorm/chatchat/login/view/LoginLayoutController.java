package com.haeorm.chatchat.login.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.haeorm.chatchat.Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * 접속 전 설정 화면 레이아웃을 조정한다.
 * @author Jeongsam
 * @since 2017-04-04
 * @version 0.1
 *
 */
public class LoginLayoutController implements Initializable{

	@FXML ChoiceBox serverChoiceBox;
	@FXML TextField nameInputBox;
	@FXML PasswordField passwordInputBox;
	@FXML Button connectButton;
	
	private Client client;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
	
	public LoginLayoutController() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * 서버 접속을 시도한다.
	 */
	@FXML 
	private void handleConnectButton(){
		client.tryConnectToServer();
	}
	

	/**
	 * Client 와 연동한다.
	 * @param client
	 */
	public void setClient(Client client){
		this.client = client;
	}
	
}
