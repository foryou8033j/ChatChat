package com.haeorm.chatchat.root.menu;

import java.net.URL;
import java.util.ResourceBundle;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.model.RegistyNameData;
import com.haeorm.chatchat.model.UserList;
import com.haeorm.chatchat.root.RootLayoutController;
import com.haeorm.chatchat.root.RootStage;
import com.haeorm.chatchat.root.menu.namechange.NameChangeDialog;
import com.haeorm.chatchat.root.menu.namechange.PasswordInputDialog;
import com.haeorm.chatchat.util.Regedit;
import com.haeorm.chatchat.util.notification.DesktopNotify;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;

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
	
	private Client client = null;
	private RootStage rootStage = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		statusColumn.setCellValueFactory(cellData -> cellData.getValue().getStatusProperty());
		
		initNameCellFactory(nameColumn);
		initStatusCellFactory(statusColumn);
		
		alwaysTop.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue.booleanValue())
					alwaysTop.setTextFill(Color.RED);
				else
					alwaysTop.setTextFill(Color.BLACK);
			}
		});
		
		alarm.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue.booleanValue())
					alarm.setTextFill(Color.RED);
				else
					alarm.setTextFill(Color.BLACK);
			}
		});
		
		showAlwaysBotton.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue.booleanValue())
					showAlwaysBotton.setTextFill(Color.RED);
				else
					showAlwaysBotton.setTextFill(Color.BLACK);
			}
		});
	}
	
	public MenuLayoutController() {
		// TODO Auto-generated constructor stub
	}
	
	@FXML
	private void handleMenubar(){
		rootStage.getRootLayoutController().closeMenuPane();
	}
	
	@FXML
	private void handleSendFile(){
		
	}
	
	@FXML
	private void handleCheckFileList(){
		
	}
	
	@FXML
	private void handleAlarm(){
		if(alarm.isSelected()){
			rootStage.setAlarm(true);
			Regedit.setRegistry(RegistyNameData.NOITIFY_ALARM, true);
		}else{
			rootStage.setAlarm(false);
			Regedit.setRegistry(RegistyNameData.NOITIFY_ALARM, false);
		}
	}
	
	@FXML
	private void handleAlwayShowBottom(){
		if(showAlwaysBotton.isSelected()){
			rootStage.setAlwaysChatDown(true);
			Regedit.setRegistry(RegistyNameData.ROOT_VIEW_CHAT_ALWAYS_DOWN, true);
		}else{
			rootStage.setAlwaysChatDown(false);
			Regedit.setRegistry(RegistyNameData.ROOT_VIEW_CHAT_ALWAYS_DOWN, false);
		}
	}
	
	@FXML
	private void handleToogleAlwaysTop(){
		if(alwaysTop.isSelected()){
			rootStage.setAlwaysOnTop(true);
			Regedit.setRegistry(RegistyNameData.ROOT_VIEW_ALWAY_ON_TOP, true);
		}
		else{
			rootStage.setAlwaysOnTop(false);
			Regedit.setRegistry(RegistyNameData.ROOT_VIEW_ALWAY_ON_TOP, false);
		}
			
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
	public void setClient(Client client, RootStage rootStage){
		this.client = client;
		this.rootStage = rootStage;
		
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
		
		setDefaultAlwaysOnTopOption();
		setDefaultAlarmOption();
		setDefaultAlwaysChatDown();
		
		sendFile.setDisable(true);
		checkFileList.setDisable(true);
		
		
	}
	
	private ContextMenu initContextMenu(String name){
		
		ContextMenu contextMenu = new ContextMenu();
		
		MenuItem getAdmin = new MenuItem("관리자 권한 얻기");
		MenuItem removeAdmin = new MenuItem("관리자 권한 버리기");
		
		MenuItem setWisper = new MenuItem("귓속말 대상 선택");
		MenuItem resetWisper = new MenuItem("전체대화로 전환");
		
		Menu admin = new Menu("관리자");
		MenuItem kick = new MenuItem("이 유저 추방");
		MenuItem kickAll = new MenuItem("전체 유저 추방");
		MenuItem clear = new MenuItem("이 유저 채팅 삭제");
		MenuItem clearAll = new MenuItem("전체 유저 채팅 삭제");
		
		MenuItem notice = new MenuItem("공지 사항 전파");
		
		//관리자 권한 얻기
		getAdmin.setOnAction(event -> {
			new PasswordInputDialog(client).show();
		});
		
		//관리자 권한 버리기
		removeAdmin.setOnAction(event -> {
			rootStage.setAdmin(false);
			client.getRootStage().getMenuLayoutController().getStatusList().remove("Admin");
			client.getRootStage().getMenuLayoutController().getStatusComboBox().getSelectionModel().select(statusList.get(0));
			client.getManager().sendChangedStatus(statusList.get(0));
			initNameCellFactory(nameColumn);
		});
		
		//귓속말 대상 선택
		setWisper.setOnAction(event -> {
			rootStage.getRootLayoutController().setWisperReceiver(name);
			initNameCellFactory(nameColumn);
		});
		
		//전체대화로 전환
		resetWisper.setOnAction(event -> {
			rootStage.getRootLayoutController().resetReceiver();
			initNameCellFactory(nameColumn);
		});
		
		//해당 유저 추방
		kick.setOnAction(event -> {
			client.getManager().kickUser(name, "MessageTest");
			initNameCellFactory(nameColumn);
		});
		
		//전체 유저 추방
		kickAll.setOnAction(event -> {
			client.getManager().kickAllUser("TestMessage");
			initNameCellFactory(nameColumn);
		});
		
		//해당 유저 채팅창 지우기
		clear.setOnAction(event -> {
			client.getManager().clearChatUser(name);
			initNameCellFactory(nameColumn);
		});
		
		//전체 유저 채팅창 지우기
		clearAll.setOnAction(event -> {
			client.getManager().clearChatAllUser();
			initNameCellFactory(nameColumn);
		});
		
		//공지사항 전파
		notice.setOnAction(event -> {

			rootStage.getRootLayoutController().setWisperReceiver(RootLayoutController.NOTICE_RECEIVER);
			
			initNameCellFactory(nameColumn);
		});
		
		//나의 이름일 경우
		if(name.equals(client.getData().getName()))
		{
			if(rootStage.isAdmin()){
				admin.getItems().addAll(removeAdmin, kickAll, clearAll, notice);
				contextMenu.getItems().add(admin);
			}
			else
				contextMenu.getItems().add(getAdmin);
			
			if(!rootStage.getRootLayoutController().getWisperReceiver().equals(RootLayoutController.DEFAULT_RECEIVER))
				contextMenu.getItems().add(resetWisper);
			
		}else{

			if(rootStage.isAdmin()){
				admin.getItems().addAll(removeAdmin, kick, kickAll, clear, clearAll, notice);
				contextMenu.getItems().add(admin);
			}
			
			if(rootStage.getRootLayoutController().getWisperReceiver().equals(name))
				contextMenu.getItems().add(resetWisper);
			else if(!rootStage.getRootLayoutController().getWisperReceiver().equals(RootLayoutController.DEFAULT_RECEIVER))
				contextMenu.getItems().addAll(setWisper, resetWisper);
			else
				contextMenu.getItems().add(setWisper);
		}
		
		return contextMenu;
	}
	
	/**
	 * 유저리스트 네임뷰를 새로고침한다.
	 */
	private void initNameCellFactory(TableColumn<UserList, String> column){
		
		column.setCellFactory(new Callback<TableColumn<UserList,String>, TableCell<UserList,String>>() {
			
			@Override
			public TableCell<UserList, String> call(TableColumn<UserList, String> param) {
				
				
				final TableCell<UserList, String> cell = new TableCell<UserList, String>() {
					
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
						} else {
							setText(item);
							
							if(item.equals(client.getData().getName())){
								if(rootStage.isAdmin())
									setTextFill(Color.RED);
								else
									setTextFill(Color.GREEN);
							}
							
							setContextMenu(initContextMenu(item));
							
						}
						setContentDisplay(ContentDisplay.TEXT_ONLY);
					}
				};
				
				return cell;
			}
		});
	}
	
	/**
	 * 이름 셀을 새로고친다.
	 */
	public void refreshNameCell(){
			initNameCellFactory(nameColumn);
	}
	
	private void initStatusCellFactory(TableColumn<UserList, String> column){
		
		column.setCellFactory(new Callback<TableColumn<UserList,String>, TableCell<UserList,String>>() {
			
			@Override
			public TableCell<UserList, String> call(TableColumn<UserList, String> param) {
				
				
				final TableCell<UserList, String> cell = new TableCell<UserList, String>() {
					
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
						} else {
							setText(item);
							
							//FXCollections.observableArrayList("Online", "Busy", "Away", "Offline");
							
							if(item.equals(status.getItems().get(0)))
								setTextFill(Color.BLUEVIOLET);
							if(item.equals(status.getItems().get(1)))
								setTextFill(Color.BLUE);
							if(item.equals(status.getItems().get(2)))
								setTextFill(Color.BROWN);
							if(item.equals(status.getItems().get(3)))
								setTextFill(Color.CRIMSON);
							if(item.equals("Admin"))
								setTextFill(Color.RED);

							
						}
						setContentDisplay(ContentDisplay.TEXT_ONLY);
					}
				};
				
				return cell;
			}
		});
		
	}
	
	/**
	 * 항상위에 기능 기본 데이터를 불러온다.
	 */
	private void setDefaultAlwaysOnTopOption(){
		boolean alwaysOnTop = Regedit.getBooleanValue(RegistyNameData.ROOT_VIEW_ALWAY_ON_TOP);
		alwaysTop.setSelected(alwaysOnTop);
		rootStage.setAlwaysOnTop(alwaysOnTop);
		
		if(alwaysTop.isSelected())
			alwaysTop.setTextFill(Color.RED);
	}
	
	/**
	 * 알람 기능 기본 데이터를 불러온다.
	 */
	private void setDefaultAlarmOption(){
		boolean alarmBoolean = Regedit.getBooleanValue(RegistyNameData.NOITIFY_ALARM);
		alarm.setSelected(alarmBoolean);
		rootStage.setAlarm(alarmBoolean);
		
		if(alarm.isSelected())
			alarm.setTextFill(Color.RED);
	}
	
	/**
	 * 채팅 항상 내리기 기능 기본 데이터를 불러온다.
	 */
	private void setDefaultAlwaysChatDown(){
		boolean chatDown = Regedit.getBooleanValue(RegistyNameData.ROOT_VIEW_CHAT_ALWAYS_DOWN);
		showAlwaysBotton.setSelected(chatDown);
		rootStage.setAlwaysChatDown(chatDown);
		
		if(showAlwaysBotton.isSelected())
			showAlwaysBotton.setTextFill(Color.RED);
		
	}
	
	/**
	 * 상태 리스트를 반환한다.
	 * @return
	 */
	public ObservableList<String> getStatusList(){
		return statusList;
	}
	
	/**
	 * 상태 콤보박스를 반환한다.
	 * @return
	 */
	public ComboBox<String> getStatusComboBox(){
		return status;
	}
	
	
	/**
	 * 현재 접속자 목록 리스트를 반환한다.
	 * @return
	 */
	public ObservableList<UserList> getUserList(){
		return userList;
	}
	
}
