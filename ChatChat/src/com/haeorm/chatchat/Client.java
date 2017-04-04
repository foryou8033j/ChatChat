package com.haeorm.chatchat;
	
import java.awt.Robot;

import com.haeorm.chatchat.loading.LoadLayout;
import com.haeorm.chatchat.login.LoginStage;
import com.haeorm.chatchat.model.Data;
import com.haeorm.chatchat.model.ServerData;
import com.haeorm.chatchat.root.RootStage;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
	
	private String title = "ChatChat";
	private double version = 0.1;
	
	private Data data = null;
	private ObservableList<ServerData> serverDatas = FXCollections.observableArrayList();
	
	public Image icon = null;
	
	private LoginStage loginStage = null;
	private RootStage rootStage = null;
	
	private boolean acceptShowRootLayout = true;
	
	@Override
	public void start(Stage primaryStage) {
		
		data = new Data();
		serverDatas.addAll(new ServerData("메인 채널", "10.160.1.90", 8880),
				new ServerData("테스트 채널", "10.160.1.90", 8890));
		
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
		
		LoadLayout loadLayout = new LoadLayout("서버에 접속 중 입니다.", loginStage);
		
		Task task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {


				
				
				
				
				
				return null;
			}
		};
		
		loadLayout.activateProgress(task);
		
		task.setOnSucceeded(event -> {
		
			loadLayout.close();
			initRootStage();
		});
		
		
		
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}
	
	/**
	 * ServerDatas 를 반환한다.
	 * @return
	 */
	public ObservableList<ServerData> getServerDatas(){
		return serverDatas;
	}
	
	/**
	 * 버전 값을 반환한다.
	 * @return Version
	 */
	public double getVersion(){
		return version;
	}
	
	/**
	 * 타이틀 문자열을 반환한다.
	 * @return
	 */
	public String getTitle(){
		return title;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
