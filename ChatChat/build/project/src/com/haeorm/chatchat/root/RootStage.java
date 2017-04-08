package com.haeorm.chatchat.root;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.model.RegistyNameData;
import com.haeorm.chatchat.model.UserList;
import com.haeorm.chatchat.root.chatnode.ChatNode;
import com.haeorm.chatchat.root.chatnode.ChatNode.NODE_STYLE;
import com.haeorm.chatchat.root.menu.MenuLayoutController;
import com.haeorm.chatchat.root.view.RootLayoutController;
import com.haeorm.chatchat.root.view.RootLayoutController.NOTICE_STYLE;
import com.haeorm.chatchat.util.Regedit;
import com.haeorm.chatchat.util.logview.LogView;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RootStage extends Stage{
	
	
	
	public String recentlySender = ""; 
	
	private RootLayoutController controller = null;
	private Client client;
	
	private BorderPane menuPane = null;
	private MenuLayoutController menuController = null;
	
	
	public boolean showMenuBar = false;
	
	public RootStage(Client client) {
		super();
		this.client = client;
		
		
		
		setTitle(client.getTitle());
		getIcons().add(client.icon);
		
		setMinWidth(345);
		setMinHeight(280);
		
		setAlwaysOnTop(true);
		
		setOnCloseRequest(event -> {
			doShutdownCall();
		});
		
		try{
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("./view/RootLayout.fxml"));
			BorderPane pane = (BorderPane) loader.load();
			
			controller = loader.getController();
			controller.setClient(client);
			
			Scene scene = new Scene(pane);
			setScene(scene);
			
		}catch (Exception e){
			e.printStackTrace();
			LogView.append("[오류] RootStage 초기화 도중 오류가 발생하였습니다.", e);
			System.exit(0);
		}
		
		loadDefaultPos();
		loadDefaultSize();
		
		initMenuPane();
		
		show();
		
		controller.showNoticePopup(NOTICE_STYLE.ERROR, "testMessage", 2000);
		
		/*try{
			recentlySender = "";
			getRootLayoutController().getChatList().add(new ChatNode(client, NODE_STYLE.NOTICE, "", client.getData().getName() + "님 환영합니다.").getChatNode());
		}catch (Exception e){
			//ignore
		}*/
		
		
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
	private void doShutdownCall(){
		
		LogView.append("[알림] RootStage Shutdown Call 이 실행되었습니다. (" + getX() + ", " + getY() + ")");
		
		try{
			//채팅방 종료 플래그를 전송한다.
			client.getSender().getManager().sendQuitPlag();
		}catch (Exception e){
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
		
		client.clearTransmit();
		client.initLoginStage();
		
	}
	
	/**
	 * 메뉴 화면을 초기화 한다.
	 */
	private void initMenuPane(){
		
		try {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("./menu/MenuLayout.fxml"));
			menuPane = (BorderPane) loader.load();
			
			menuController = loader.getController();
			menuController.setClient(client);
			
			
			
		}catch (Exception e){
			
		}
		
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
	 * 채팅창에 메세지를 출력한다.
	 * @param message
	 */
	public void appendMessage(String name, String message){
		controller.appendMessage(name, message);
	}
	

}
