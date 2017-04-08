package com.haeorm.chatchat;
	
import java.awt.AWTException;
import java.awt.Robot;
import java.net.Socket;

import com.haeorm.chatchat.loading.LoadLayout;
import com.haeorm.chatchat.login.LoginStage;
import com.haeorm.chatchat.model.Data;
import com.haeorm.chatchat.model.ServerData;
import com.haeorm.chatchat.root.RootStage;
import com.haeorm.chatchat.transmit.receiver.Receiver;
import com.haeorm.chatchat.transmit.sender.MessageManager;
import com.haeorm.chatchat.transmit.sender.Sender;
import com.haeorm.chatchat.util.ExceptionDialog;
import com.haeorm.chatchat.util.logview.LogView;

import javafx.application.Application;
import javafx.application.Platform;
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
	
	private boolean acceptShowRootLayout = false;
	
	private Receiver receiver = null;
	private Sender sender = null;
	
	@Override
	public void start(Stage primaryStage) {
		this.client = this;
		
		data = new Data();
		
		serverDatas.addAll(new ServerData("메인 채널", data.getLocalIP(), 8080),
				new ServerData("테스트 채널", data.getLocalIP(), 8888));
		
		loadImages();
		
		initLoginStage();
		
	}
	
	private void loadImages(){
		icon = new Image("chat.png");
	}
	
	/**
	 * LoginStage 를 초기화한다.
	 */
	public void initLoginStage(){
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
		
		Task<Void> task = new Task<Void>() {
			
			@Override
			protected Void call() throws Exception {
				
				Socket connection = null;
				
				try{
					int index = loginStage.getController().getSelectedIndex();
					
					updateMessage(serverDatas.get(index).getName() + " 에 접속 중...");
					connection = new Socket(serverDatas.get(index).getIP(), serverDatas.get(index).getPort());
					showprogressIndicate(1, 10, 5);
					
				}catch (Exception e){
					connection = null;
					e.printStackTrace();
					Platform.runLater(() -> {
						ExceptionDialog ex = new ExceptionDialog(AlertType.ERROR, "접속 실패", "서버 연결 불가", "서버가 동작중이지 않거나 알 수 없는 오류가 발생하였습니다.", e);
						ex.initOwner(loginStage);
						ex.show();
					});
					this.cancel();
				}
					
					updateMessage("수신 서버 연결 중...");
					showprogressIndicate(10, 15, 5);
					receiver = new Receiver(client, connection);
					receiver.start();
					
					
					updateMessage("전송 서버 연결 중...");
					showprogressIndicate(15, 20, 5);
					sender = new Sender(client, connection);
					
				
					
					//서버 접속과 성공과 동시에 최초 티켓 발송
					updateMessage("최초 인증 토큰 발송...");
					getManager().sendFirstTicket();
					
					
					updateMessage("패스 워드 일치 확인...");
					getManager().sendPasswordCheckPlag();
					showprogressIndicate(20, 40, 20);
					
					if(getData().serverPasswordPass){
						LogView.append("패스워드 일치 확인");
						
						updateMessage("서버 버전 일치 확인...");
						getManager().sendPasswordCheckPlag();
						showprogressIndicate(40, 60, 20);
						
						if(getData().versionCheckPass){
							LogView.append("서버 버전 일치 확인");
							
							updateMessage("이름 중복 확인 ...");
							getManager().sendNameCheckPlag();
							showprogressIndicate(60, 80, 20);
							
							if(getData().nameOverLabPass){
								LogView.append("이름 중복 없음 확인");
								getData().setName(loginStage.getController().getName());
								
								LogView.append("서버 접속 최종 승인");
								updateMessage("서버 최종 접속 중 ...");
								showprogressIndicate(80, 101, 10);
								acceptShowRootLayout = true;
								this.succeeded();
								
							}else{
								LogView.append("이름 중복");
								Platform.runLater(() -> {
									ExceptionDialog ex = new ExceptionDialog(AlertType.ERROR, "접속 실패", "서버에 중복 된 이름이 존재합니다.");
									ex.initOwner(loginStage);
									ex.show();
								});
								this.cancel();
							}
							
						}else{
							LogView.append("서버 버전 불일치");
							Platform.runLater(() -> {
								ExceptionDialog ex = new ExceptionDialog(AlertType.ERROR, "접속 실패", "클라이언트의 버전이 올바르지 않습니다.");
								ex.initOwner(loginStage);
								ex.show();
							});
							this.cancel();
						}

					}else{
						LogView.append("패스워드 불일치");
						Platform.runLater(() -> {
							ExceptionDialog ex = new ExceptionDialog(AlertType.ERROR, "접속 실패", "서버 패스워드가 일치하지 않습니다.");
							ex.initOwner(loginStage);
							ex.show();
						});
						this.cancel();
					}
					

				
				return null;
			}
			
			private void showprogressIndicate(int startValue, int endValue, int time) throws AWTException{
				for(int i=startValue; i<endValue; i++){
					new Robot().delay(time);
					updateProgress(i, 100);
				}
			}
			
		};
		
		loadLayout.initOwner(loginStage);
		loadLayout.activateProgress(task);
		loadLayout.activateText(task);
		
		task.setOnSucceeded(event -> {
			
			loadLayout.close();
			initRootStage();
			
			getManager().sendInitChatRoomPlag();
		});
		
		task.setOnCancelled(event -> {
			loadLayout.close();
			receiver = null;
			sender = null;
			acceptShowRootLayout = false;
		});
		

		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}
	
	/**
	 * Task 를 중지한다.
	 * @param task
	 */
	private void taskCancle(Task<Void> task){
		if(task != null)
			task.cancel();
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
	
	/**
	 * 수신/발신 소켓을 소멸한다.
	 */
	public void clearTransmit(){
		receiver = null;
		sender = null;
	}
	
	/**
	 * LoginStage를 반환한다.
	 * @return
	 */
	public LoginStage getLoginStage(){
		return loginStage;
	}
	
	/**
	 * Root Stage를 반환한다.
	 * @return
	 */
	public RootStage getRootStage(){
		return rootStage;
	}
	
	/**
	 * MessageManager를 반환한다.
	 * @return
	 */
	public MessageManager getManager(){
		return sender.getManager();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
