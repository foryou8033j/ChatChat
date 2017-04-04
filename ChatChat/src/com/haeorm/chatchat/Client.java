package com.haeorm.chatchat;
	
import com.haeorm.chatchat.login.LoginStage;
import com.haeorm.chatchat.model.Data;
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
	
	private Data data = null;
	
	public Image icon = null;
	
	private LoginStage loginStage = null;
	private RootStage rootStage = null;
	
	@Override
	public void start(Stage primaryStage) {
		
		data = new Data();
		
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
		loginStage.show();
	}
	
	/**
	 * RootStage 를 초기화 한다.
	 */
	private void initRootStage(){
		if(loginStage != null){
			loginStage.close();
			loginStage = null;
		}
		rootStage = new RootStage(this);
		rootStage.show();
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
