package com.haeorm.chatchat.root.menu;

import java.net.URL;
import java.util.ResourceBundle;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.model.UserList;
import com.haeorm.chatchat.root.menu.namechange.NameChangeDialog;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;

public class MenuLayoutController implements Initializable{


	private ObservableList<String> statusList = FXCollections.observableArrayList("Online", "Busy", "Away", "Offline");
	private ObservableList<UserList> userList = FXCollections.observableArrayList();
	
	@FXML Button menuButton;
	
	@FXML TableView<UserList> userListView;
	@FXML TableColumn<UserList, String> nameColumn;
	@FXML TableColumn<UserList, String> statusColumn;
	
	@FXML Button sendFile;
	@FXML Button checkFileList;
	
	@FXML ToggleButton alarm;
	@FXML ToggleButton showAlwaysBotton;
	
	@FXML Text name;
	@FXML Button changeName;
	
	@FXML ComboBox<String> status;
	@FXML ToggleButton alwaysTop;
	
	private Client client;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		statusColumn.setCellValueFactory(cellData -> cellData.getValue().getStatusProperty());
		
	}
	
	public MenuLayoutController() {
		// TODO Auto-generated constructor stub
	}
	
	@FXML
	private void handleMenubar(){
		client.getRootStage().getRootLayoutController().closeMenuPane();
	}
	
	@FXML
	private void handleSendFile(){
		
	}
	
	@FXML
	private void handleCheckFileList(){
		
	}
	
	@FXML
	private void handleAlarm(){
		
	}
	
	@FXML
	private void handleAlwayShowBottom(){
		
	}
	
	@FXML
	private void handleToogleAlwaysTop(){
		
	}
	
	@FXML
	private void handleChangeName(){
		new NameChangeDialog(client).show();
	}
	
	@FXML
	private void handleChangeStatus(){
		
	}
	
	
	/**
	 * 클라이언트와 연동한다.
	 * @param client
	 */
	public void setClient(Client client){
		this.client = client;
		
		name.setText(client.getData().getName());
		
		userListView.setItems(userList);
		status.setItems(statusList);
		
		client.getData().getNameProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				name.setText(newValue);
			}
		});
		
		client.getData().getStatusProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				status.getSelectionModel().select(newValue);
			}
		});
		
		status.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				client.getData().setStatus(newValue);
				client.getSender().getManager().sendChangedStatus(newValue);
			}
		});
		
	}
	
	/**
	 * 상태 리스트를 반환한다.
	 * @return
	 */
	public ObservableList<String> getStatusList(){
		return statusList;
	}
	
	
	/**
	 * 현재 접속자 목록 리스트를 반환한다.
	 * @return
	 */
	public ObservableList<UserList> getUserList(){
		return userList;
	}
	
}
