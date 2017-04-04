package com.haeorm.chatchat;
	
import com.haeorm.chatchat.login.LoginStage;
import com.haeorm.chatchat.model.Data;
import com.haeorm.chatchat.model.RegistyNameData;
import com.haeorm.chatchat.model.ServerData;
import com.haeorm.chatchat.root.RootStage;
import com.haeorm.chatchat.util.Regedit;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * 클라이언트 동작을 위한 중심 클래스
 * @author Jeongsam
 * @version 1.0
 * @since 2017-04-04
 *
 */
public class Client extends Application {
	
	private Data data = null;
	private ObservableList<ServerData> serverDatas = FXCollections.observableArrayList();
	
	public Image icon = null;
	
	private LoginStage loginStage = null;
	private RootStage rootStage = null;
	
	private boolean acceptShowRootLayout = false;
	
	@Override
	public void start(Stage primaryStage) {
		
		data = new Data();
		serverDatas.add(new ServerData("테스트 서버", "10.160.1.90", 8888));
		
		loadImages();
		
		initLoginStage();
		
	}
	
	private void loadImages(){
		icon = new Image("chat.png");
	}
	
	/**
	 * LoginStage 를 초기화한다.
	 */
	private void initLoginStage(){
		loginStage = new LoginStage(this);
		//loginStage.show();
	}
	
	/**
	 * RootStage 를 초기화 한다.
	 */
	private void initRootStage(){
		
		if(acceptShowRootLayout){
			if(loginStage != null){
				loginStage.close();
				loginStage = null;
			}
			rootStage = new RootStage(this);
			rootStage.show();
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(loginStage);
			alert.setTitle("접근 불가");
			alert.setHeaderText("서버 접속 권한 부족");
			alert.setContentText("서버에 접속하는데 필요한 충분한 권한이 획득되지 않았습니다.");
			alert.showAndWait();
		}
		
	}
	
	/**
	 * 서버로 접속을 시도한다.
	 */
	public void tryConnectToServer(){
		
		
		
		initRootStage();
	}
	
	/**
	 * ServerDatas 를 반환한다.
	 * @return
	 */
	public ObservableList<ServerData> getServerDatas(){
		return serverDatas;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
