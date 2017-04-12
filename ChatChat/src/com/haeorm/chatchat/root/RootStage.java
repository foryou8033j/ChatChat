package com.haeorm.chatchat.root;

import java.util.Random;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.model.RegistyNameData;
import com.haeorm.chatchat.root.menu.MenuLayoutController;
import com.haeorm.chatchat.root.view.RootLayoutController;
import com.haeorm.chatchat.root.view.RootLayoutController.NOTICE_STYLE;
import com.haeorm.chatchat.transmit.ConnectionKeeper;
import com.haeorm.chatchat.util.ExceptionDialog;
import com.haeorm.chatchat.util.Regedit;
import com.haeorm.chatchat.util.logview.LogView;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RootStage extends Stage{



	public String recentlySender = "";

	private RootLayoutController controller = null;
	private Client client;

	private BorderPane menuPane = null;
	private MenuLayoutController menuController = null;

	private boolean alarm = false;
	private boolean chatAlwaysDown = true;

	public boolean showMenuBar = false;

	private boolean admin = false;

	private ConnectionKeeper connectionKeeper = null;

	public RootStage(Client client) {
		super();
		this.client = client;



		setTitle(client.getTitle());
		getIcons().add(client.icon);

		setMinWidth(480);
		setMinHeight(280);

		setOnCloseRequest(event -> {
			doShutdownCall();
		});

		try{
			FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("com/haeorm/chatchat/root/view/RootLayout.fxml"));
			BorderPane pane = (BorderPane) loader.load();

			controller = loader.getController();
			controller.setClient(client);

			Scene scene = new Scene(pane);
			setScene(scene);

		}catch (Exception e){
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "RootStage 초기화 도중 오류가 발생하였습니다.", "", e).showAndWait();
			LogView.append("[오류] RootStage 초기화 도중 오류가 발생하였습니다.", e);
			doShutdownCall();
		}

		loadDefaultPos();
		loadDefaultSize();

		initMenuPane();

		show();

		showRandomMessage();	//랜덤 공지사항 출력


		//RootStage 초기화가 완료되면 서버 지속 연결 스레드를 시작한다.
		//connectionKeeper = new ConnectionKeeper(client);
		//connectionKeeper.start();


	}

	private void showRandomMessage(){

		String[] notice = {
			"이름 앞에 '@'를 붙여 누군가를 호출 해 보세요",
			"'##'을 입력하고 이름을 입력하면 귓속말을 할 수 있어요",
			"오른쪽 '▶' 버튼을 누르면 메뉴를 볼 수 있습니다",
			"'##'만 입력 후 전송하면 전체대화모드가 됩니다.",
			"다른 사용자를 우측클릭하면 다른 옵션이 있어요"
		};

		int rand = new Random().nextInt(notice.length);

		showNoticePopup(NOTICE_STYLE.INFORMATION, notice[rand], 6000);
	}

	/**
	 * 레지스트리 상에 등록된 기본 위치에 프레임을 위치시킨다.
	 */
	private void loadDefaultPos(){

		try{
			double posX = Regedit.getDoubleValue(RegistyNameData.ROOT_VIEW_X);
			double posY = Regedit.getDoubleValue(RegistyNameData.ROOT_VIEW_Y);

			if(posX==0 && posY==0)
				throw new Exception("해당 레지스트리 값이 존재하지 않음," + RegistyNameData.ROOT_VIEW_X + ", " + RegistyNameData.ROOT_VIEW_Y);

			LogView.append("[알림] 레지스트리의 값에 따라 RootStage의 기본 위치를 " + posX + ", " + posY + " 로 조정");

			setX(posY);
			setY(posY);

		}catch (Exception e){
			try{
				Regedit.setRegistry(RegistyNameData.ROOT_VIEW_X, getX());
				Regedit.setRegistry(RegistyNameData.ROOT_VIEW_Y, getY());

				LogView.append("[알림] 설정 된 RootStage의 기본 위치 값이 없어 등록 완료");

			}catch (Exception ie){

				LogView.append("[알림] RootStage 의 기본 위치 등록 실패", ie);
			}

		}finally {
			addPosListener();
		}
	}

	/**
	 * 프레임의 위치 변화에 따라 레지스트리 상 값을 수정하도록 리스너를 등록시킨다.
	 */
	private void addPosListener(){

		xProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Regedit.setRegistry(RegistyNameData.ROOT_VIEW_X, newValue.doubleValue());
			}
		});

		yProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Regedit.setRegistry(RegistyNameData.ROOT_VIEW_Y, newValue.doubleValue());
			}
		});

	}


	/**
	 * 레지스트리 상에 등록된 기본 크기를 반영 시킨다.
	 */
	private void loadDefaultSize(){

		try{
			double width = Regedit.getDoubleValue(RegistyNameData.ROOT_VIEW_WIDTH);
			double height = Regedit.getDoubleValue(RegistyNameData.ROOT_VIEW_HEIGHT);

			if(width==0 && height==0)
				throw new Exception("해당 레지스트리 값이 존재하지 않음," + RegistyNameData.ROOT_VIEW_WIDTH + ", " + RegistyNameData.ROOT_VIEW_HEIGHT);

			LogView.append("[알림] 레지스트리의 값에 따라 RootStage의 기본 크기를 " + getWidth() + ", " + getHeight() + " 로 조정");

			setWidth(width);
			setHeight(height);

		}catch (Exception e){
			try{
				Regedit.setRegistry(RegistyNameData.ROOT_VIEW_WIDTH, getWidth());
				Regedit.setRegistry(RegistyNameData.ROOT_VIEW_HEIGHT, getHeight());

				LogView.append("[알림] 설정 된 RootStage의 기본 크기 값이 없어 등록 완료");

			}catch (Exception ie){

				LogView.append("[알림] RootStage 의 기본 크기 등록 실패", ie);
			}

		}finally {
			addSizeListener();
		}
	}

	/**
	 * 프레임의 위치 변화에 따라 레지스트리 상 값을 수정하도록 리스너를 등록시킨다.
	 */
	private void addSizeListener(){

		widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(showMenuBar)
					Regedit.setRegistry(RegistyNameData.ROOT_VIEW_WIDTH, newValue.doubleValue()-400);
				else
					Regedit.setRegistry(RegistyNameData.ROOT_VIEW_WIDTH, newValue.doubleValue());
			}
		});

		heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Regedit.setRegistry(RegistyNameData.ROOT_VIEW_HEIGHT, newValue.doubleValue());
			}
		});

	}



	/**
	 * LoginStage 가 종료 될 때 실행되는 Call Method 이다.
	 */
	public void doShutdownCall(){

		LogView.append("[알림] RootStage Shutdown Call 이 실행되었습니다. (" + getX() + ", " + getY() + ")");

		try{
			//채팅방 종료 플래그를 전송한다.
			client.getSender().getManager().sendQuitPlag();
		}catch (Exception e){
			LogView.append("[오류] 채팅방 종료 플래그 전송 도중 오류가 발생하였습니다.", e);
			e.printStackTrace();
		}


		//채팅 리스트
		controller.handleClearChatList();

		Regedit.setRegistry(RegistyNameData.ROOT_VIEW_X, getX());
		Regedit.setRegistry(RegistyNameData.ROOT_VIEW_Y, getY());
		Regedit.setRegistry(RegistyNameData.ROOT_VIEW_WIDTH, getWidth());
		Regedit.setRegistry(RegistyNameData.ROOT_VIEW_HEIGHT, getHeight());

		if(showMenuBar)
			Regedit.setRegistry(RegistyNameData.ROOT_VIEW_WIDTH, getWidth()-400);

		close();

		client.clearTransmit();
		client.initLoginStage();

	}

	/**
	 * 메뉴 화면을 초기화 한다.
	 */
	private void initMenuPane(){

		try {

			FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("com/haeorm/chatchat/root/menu/MenuLayout.fxml"));
			menuPane = (BorderPane) loader.load();

			menuController = loader.getController();
			menuController.setClient(client, this);

		}catch (Exception e){
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "메뉴 화면 초기화 도중 오류가 발생하였습니다.", "", e).showAndWait();
			LogView.append("[오류] 메뉴 화면 초기화 도중 오류가 발생하였습니다.", e);
			doShutdownCall();
		}

	}

	/**
	 * 어드민 여부를 설정한다.
	 * @param set
	 */
	public void setAdmin(boolean set){
		admin = set;
	}

	/**
	 * 어드민 여부를 반환한다.
	 * @return
	 */
	public boolean isAdmin(){
		return admin;
	}

	/**
	 * 알람 옵션을 설정한다.
	 * @param set
	 */
	public void setAlarm(boolean set){
		alarm = set;
	}

	/**
	 * 알람 설정을 반환한다.
	 * @return
	 */
	public boolean isAlarm(){
		return alarm;
	}

	/**
	 * 채팅 항상 내림 기능을 설정한다
	 * @param set
	 */
	public void setAlwaysChatDown(boolean set){
		chatAlwaysDown = set;
	}

	/**
	 * 채팅 항상 내림 기능 설정을 반환한다.
	 * @return
	 */
	public boolean isAlwaysChatDown(){
		return chatAlwaysDown;
	}

	/**
	 * 메뉴 레이아웃을 반환한다.
	 * @return
	 */
	public BorderPane loadMenuPane(){
		return menuPane;
	}

	/**
	 * 메뉴 레이아웃 컨트롤러를 반환한다.
	 * @return
	 */
	public MenuLayoutController getMenuLayoutController(){
		return menuController;
	}

	/**
	 * RootLayoutController 를 반환한다.
	 * @return Controller
	 */
	public RootLayoutController getRootLayoutController(){
		return controller;
	}

	/**
	 * 연결 유지 스레드를 반환한다.
	 * @return
	 */
	public ConnectionKeeper getConnectionKeeper(){
		return connectionKeeper;
	}


	/**
	 * 채팅창에 메세지를 출력한다.
	 * @param message
	 */
	public void appendMessage(String name, String message){
		controller.appendMessage(name, message);
	}

	/**
	 * 상단 메세지를 띄운다.
	 * @param style
	 * @param message
	 * @param time
	 */
	public void showNoticePopup(NOTICE_STYLE style, String message, int time){
		controller.showNoticePopup(style, message, time);
	}


}
