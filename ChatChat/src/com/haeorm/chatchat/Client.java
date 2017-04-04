package com.haeorm.chatchat;
	
import java.net.Socket;

import com.haeorm.chatchat.loading.LoadLayout;
import com.haeorm.chatchat.login.LoginStage;
import com.haeorm.chatchat.login.NameInputDialog;
import com.haeorm.chatchat.login.view.LoginLayoutController;
import com.haeorm.chatchat.model.Data;
import com.haeorm.chatchat.model.ServerData;
import com.haeorm.chatchat.root.RootStage;
import com.haeorm.chatchat.transmit.receiver.Receiver;
import com.haeorm.chatchat.transmit.sender.MessageManager;
import com.haeorm.chatchat.transmit.sender.Sender;

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
	
	private Client client = null;
	
	private String title = "ChatChat";
	private double version = 0.1;
	
	private Data data = null;
	private ObservableList<ServerData> serverDatas = FXCollections.observableArrayList();
	
	public Image icon = null;
	
	private LoginStage loginStage = null;
	private RootStage rootStage = null;
	
	private boolean acceptShowRootLayout = true;
	
	private Receiver receiver = null;
	private Sender sender = null;
	
	@Override
	public void start(Stage primaryStage) {
		this.client = this;
		
		data = new Data();
		serverDatas.addAll(new ServerData("메인 채널", "10.160.1.49", 10430),
				new ServerData("테스트 채널", "10.160.1.49", 10440));
		
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
				

				try{
					
					int index = loginStage.getController().getSelectedIndex();
					
					updateProgress(3, 10);
					updateMessage(serverDatas.get(index).getName() + " 에 접속 중...");
					Socket connection = new Socket(serverDatas.get(index).getIP(), serverDatas.get(index).getPort());
					
					updateProgress(6, 10);
					updateMessage("수신 서버 연결 중...");
					receiver = new Receiver(client, connection);
					receiver.start();
					
					
					updateProgress(8, 10);
					updateMessage("전송 서버 연결 중...");
					sender = new Sender(client, connection);
					
					
					manager().sendPasswordCheckPlag();
					updateProgress(8, 10);
					updateMessage("패스워드 일치 확인 중");
					
					
					/*new NameInputDialog(client).showAndWait();
					manager().sendNameCheckPlag();
					updateProgress(8, 10);
					updateMessage("닉네임 중복 확인 중");*/
					
					
					
					updateMessage("마무리 하는중...");
				}catch (Exception e){
					cancel();
					acceptShowRootLayout = false;
					e.printStackTrace();
				}
				
				return null;
			}
		};
		
		loadLayout.initOwner(loginStage);
		loadLayout.activateProgress(task);
		loadLayout.activateText(task);
		
		task.setOnSucceeded(event -> {
			loadLayout.close();
			acceptShowRootLayout = true;
			initRootStage();
		});
		
		task.setOnCancelled(event -> {
			loadLayout.close();
			acceptShowRootLayout = false;
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(loginStage);
			alert.setTitle("접속 불가");
			alert.setHeaderText("서버 비활성화");
			alert.setContentText("서버가 동작중이지 않거나, 문제가 발생하였습니다.");
			alert.showAndWait();
		});
		
		
		
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}
	
	/**
	 * 클라이언트 정보 데이터를 반환한다.
	 * @return
	 */
	public Data getData(){
		return data;
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
	
	/**
	 * 발신 소켓을 반환한다.
	 * @return
	 */
	public Sender getSender(){
		return sender;
	}
	
	/**
	 * 수신 소켓을 반환한다.
	 */
	public Receiver getReceiver(){
		return receiver;
	}
	
	public LoginStage getLoginStage(){
		return loginStage;
	}
	
	public MessageManager manager(){
		return sender.getManager();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
