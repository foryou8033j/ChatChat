package com.haeorm.chatchat.login.view;

import java.net.URL;
import java.util.ResourceBundle;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.model.ServerData;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * 접속 전 설정 화면 레이아웃을 조정한다.
 * @author Jeongsam
 * @since 2017-04-04
 * @version 1.0
 *
 */
public class LoginLayoutController implements Initializable{
	
	ObservableList<String> blockNameList = FXCollections.observableArrayList(
			"root", "admin");
	
	@FXML Text version;
	
	@FXML ImageView logo;
	@FXML ComboBox<ServerData> serverComboBox;
	@FXML TextField nameInputBox;
	@FXML PasswordField passwordInputBox;
	@FXML Button connectButton;
	
	@FXML Text nameNoticeText;
	@FXML Text passwordNoticeText;
	
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
		
		
		nameInputBox.setOnKeyPressed(event -> {
			if(event.getCode().equals(KeyCode.ENTER))
				handleConnectButton();
			else if (event.getCode().equals(KeyCode.ESCAPE))
				nameInputBox.setText("");
		});
		
		passwordInputBox.setOnKeyPressed(event -> {
			if(event.getCode().equals(KeyCode.ENTER))
				handleConnectButton();
			else if (event.getCode().equals(KeyCode.ESCAPE))
				passwordInputBox.setText("");
		});
		
		connectButton.setOnKeyPressed(event -> {
			if(event.getCode().equals(KeyCode.ENTER))
				handleConnectButton();
		});
		
		nameInputBox.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				if(blockNameList.contains(newValue.toLowerCase())){
					nameNoticeText.setText("사용 할 수 없는 이름입니다.");
					connectButton.setDisable(true);
				}
				else if (newValue.equals("") || passwordInputBox.getText().equals("")){
					connectButton.setDisable(true);
				}
				else{
					nameNoticeText.setText("");
					connectButton.setDisable(false);
				}
					
			}
		});
		
		
		passwordInputBox.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				if(newValue.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")){
					passwordNoticeText.setFill(Color.RED);
					passwordNoticeText.setText("한글이 입력되고 있습니다.");
					connectButton.setDisable(true);
				}
				else if (newValue.equals("") || nameInputBox.getText().equals("")){
					connectButton.setDisable(true);
				}
				else{
					passwordNoticeText.setText("");
					connectButton.setDisable(false);
				}
				
			}
		});
		
		
	}
	
	/**
	 * 서버 접속을 시도한다, 입력된 텍스트 필드를 우선적으로 검증한다.
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
		version.setText("v" + String.valueOf(client.getVersion()));
		
		logo.setImage(client.icon);
		
		serverComboBox.setItems(client.getServerDatas());
		serverComboBox.getSelectionModel().select(0);
		
		ImageView image = new ImageView("join.png");
		image.setFitWidth(30);
		image.setFitHeight(30);
		
		connectButton.setGraphic(image);
		
	}
	
}
