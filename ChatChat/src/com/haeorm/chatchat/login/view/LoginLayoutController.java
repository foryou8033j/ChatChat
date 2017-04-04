package com.haeorm.chatchat.login.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.model.ServerData;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Callback;

/**
 * 접속 전 설정 화면 레이아웃을 조정한다.
 * @author Jeongsam
 * @since 2017-04-04
 * @version 0.1
 *
 */
public class LoginLayoutController implements Initializable{	
	@FXML ComboBox<ServerData> serverComboBox;
	@FXML TextField nameInputBox;
	@FXML PasswordField passwordInputBox;
	@FXML Button connectButton;
	
	private Client client;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Callback<ListView<ServerData>, ListCell<ServerData>> factory = lv -> new ListCell<ServerData>() {

		    @Override
		    protected void updateItem(ServerData item, boolean empty) {
		        super.updateItem(item, empty);
		        setText(empty ? "" : item.getName());
		        setAlignment(Pos.CENTER);
		    }

		};

		serverComboBox.setCellFactory(factory);
		
		serverComboBox.setButtonCell(
			    new ListCell<ServerData>() {
			        @Override
			        protected void updateItem(ServerData t, boolean bln) {
			            super.updateItem(t, bln); 
			            if (bln) {
			                setText("");
			            } else {
			                setText(t.getName());
			            }
			        }
			    });
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
		serverComboBox.setItems(client.getServerDatas());
		serverComboBox.getSelectionModel().select(0);
		
	}
	
}
