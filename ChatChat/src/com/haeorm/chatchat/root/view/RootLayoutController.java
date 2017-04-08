package com.haeorm.chatchat.root.view;

import java.awt.AWTException;
import java.awt.Robot;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.root.chatnode.ChatNode;
import com.haeorm.chatchat.root.chatnode.ChatNode.NODE_STYLE;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * 채팅 프로그램의 가장 기본이 되는 Scene
 * @author Jeongsam
 * @since 2017-04-04
 * @version 0.1
 *
 */
public class RootLayoutController implements Initializable{

	public enum NOTICE_STYLE{ERROR, INFORMATION};
	
	@FXML BorderPane rootPane;
	
	ObservableList<BorderPane> chatList = FXCollections.observableArrayList();
	
	@FXML ListView<BorderPane> chatListView;
	@FXML TextField chatInputBox;
	@FXML Button sendMessageButton;
	@FXML Button clearChatListButton;
	
	private @FXML Button menuButton;

	private Client client;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	public RootLayoutController() {
		
	}
	
	@FXML
	private void handleSendMessage(){
		client.getManager().sendNomalText(chatInputBox.getText());
		chatInputBox.setText("");
	}
	
	@FXML
	public void handleClearChatList(){
		chatList.clear();
		client.getRootStage().recentlySender = "";
	}
	
	@FXML
	private void handleMenuButton(){
		showMenuPane();
	}
	
	/**
	 * 클라이언트와 연동한다.
	 */
	public void setClient(Client client){
		this.client = client;
		
		chatInputBox.setOnKeyPressed(event -> {
			if(event.getCode().equals(KeyCode.ENTER))
				handleSendMessage();
			else if(event.getCode().equals(KeyCode.ESCAPE))
				chatInputBox.setText("");
		});
		chatListView.setItems(chatList);
		
		chatListView.getItems().addListener(new ListChangeListener<BorderPane>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends BorderPane> c) {

				chatListView.scrollTo(c.getList().size() - 1);
				
			}
		});
		
	}
	
	public void appendMessage(String name, String message){
		Platform.runLater(() -> {
		
			chatList.add(new ChatNode(client, NODE_STYLE.NOMAL, name, message).getChatNode());
			
		});
		
		
	}
	
	
	/**
	 * 메뉴 버튼을 반환한다.
	 * @return
	 */
	public Button getMenuButton(){
		return menuButton;
	}
	
	/**
	 * 대화 리스트를 반환한다.
	 * @return
	 */
	public ObservableList<BorderPane> getChatList(){
		return chatList;
	}
	
	/**
	 * 메뉴화면을 보여준다.
	 * TODO 화면 확장 딜레이시 프레임 안맞는 현상 있음.
	 */
	public void showMenuPane(){
		client.getManager().sendRequestUserListFromServer();
		client.getRootStage().showMenuBar = true;
		
		
		double originalSize = client.getRootStage().getWidth();
		double maxSize = client.getRootStage().getWidth() + client.getRootStage().loadMenuPane().getPrefWidth();
		
		//double maxPaneSize = client.getRootStage().loadMenuPane().getWidth();
		
		//메뉴창을 여는 애니메이션
		Timer animTimer = new Timer();
        animTimer.scheduleAtFixedRate(new TimerTask() {
            double i = originalSize;
            double pane = 0.0;

            @Override
            public void run() {
            	if (i <= maxSize) {
                    client.getRootStage().setWidth(i);
                    client.getRootStage().loadMenuPane().setPrefWidth(pane);
                } else {
                    this.cancel();
                }
                i += 0.7;
                pane += 0.7;
            }

        }, 0, 1);
		
        rootPane.setRight(client.getRootStage().loadMenuPane());
		
		
	}
	/**
	 * 메뉴 화면을 닫는다.
	 * TODO 화면 닫을 때 클라이언트 메모리가 누수되는 문제 있음.
	 * @deprecated 클라이언트 복제 문제 있음
	 * 
	 */
	public void closeMenuPane(){
		client.getRootStage().showMenuBar = false;

		double originalSize = client.getRootStage().getWidth();
		double minSize = client.getRootStage().getWidth() - client.getRootStage().loadMenuPane().getPrefWidth();
		
		//메뉴창을 여는 애니메이션
		Timer animTimer = new Timer();
        animTimer.scheduleAtFixedRate(new TimerTask() {
            double i = originalSize;
            double pane = client.getRootStage().loadMenuPane().getWidth();

            @Override
            public void run() {
            	if (i >= minSize) {
                    client.getRootStage().setWidth(i);
                    client.getRootStage().loadMenuPane().setPrefWidth(pane);
                } else {
                	Platform.runLater(() -> {
                		rootPane.setRight(menuButton);
                		client.getRootStage().loadMenuPane().setPrefWidth(400);
                	});
                    this.cancel();
                }
                i -= 0.7;
                pane -= 0.7;
            }

        }, 0, 1);
	}
	
	public void showNoticePopup(NOTICE_STYLE style, String message, int time){
		
		Platform.runLater(() -> {
			rootPane.setTop(null);
		});
		
		BorderPane pane = new BorderPane();
		Text text = new Text(message);
		text.setFont(Font.font("Malgun Gothic", FontWeight.EXTRA_BOLD, 12));
		
		pane.setCenter(text);
		
		
		if(NOTICE_STYLE.ERROR.equals(style)){
			
			pane.setStyle(""
					+ "-fx-background-color : rgb(190, 37, 37);"
			);
			text.setFill(Color.WHITE);
			
		}else if(NOTICE_STYLE.INFORMATION.equals(style)){
			
			pane.setStyle(""
				+ "-fx-background-color : rgb(78, 156, 181);"
			);
			text.setFill(Color.BLACK);
			
		}else{
			
		}
		
		Platform.runLater(() -> {
			
			rootPane.setTop(pane);
			
			new Thread(() -> {
			
				try {
					new Robot().delay(time);
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Platform.runLater(() -> {
					rootPane.setTop(null);
				});
				
				
			}).start();
			
			
			
		});
		
	}
	
	
	
}
