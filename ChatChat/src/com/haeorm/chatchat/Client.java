package com.haeorm.chatchat;
	
import com.haeorm.chatchat.login.LoginStage;
import com.haeorm.chatchat.root.RootStage;

import javafx.application.Application;
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
	
	public Image icon = null;
	
	@Override
	public void start(Stage primaryStage) {
		
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
		new LoginStage(this).show();
	}
	
	/**
	 * RootStage 를 초기화 한다.
	 */
	private void initRootStage(){
		new RootStage(this).show();
	}
	
	/**
	 * 서버로 접속을 시도한다.
	 */
	public void tryConnectToServer(){
		
		
		
		initRootStage();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
