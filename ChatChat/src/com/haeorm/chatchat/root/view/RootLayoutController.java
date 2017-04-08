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

/**
 * 채팅 프로그램의 가장 기본이 되는 Scene
 * @author Jeongsam
 * @since 2017-04-04
 * @version 0.1
 *
 */
public class RootLayoutController implements Initializable{

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
	
	//대화 리스트를 반환한다.
	public ObservableList<BorderPane> getChatList(){
		return chatList;
	}
	
	public void showMenuPane(){
		client.getManager().sendRequestUserListFromServer();
		client.getRootStage().showMenuBar = true;
		
		
		double originalSize = client.getRootStage().getWidth();
		double maxSize = client.getRootStage().getWidth() + 400.0;
		
		//double maxPaneSize = client.getRootStage().loadMenuPane().getWidth();
		
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
                i++;
                pane++;
            }

        }, 0, 2);
		
        rootPane.setRight(client.getRootStage().loadMenuPane());
		
		
	}
	public void closeMenuPane(){
		client.getRootStage().showMenuBar = false;

		double originalSize = client.getRootStage().getWidth();
		double minSize = client.getRootStage().getWidth() - 420.0;
		
		Timer animTimer = new Timer();
        animTimer.scheduleAtFixedRate(new TimerTask() {
            double i = originalSize;
            double pane = 0.0;

            @Override
            public void run() {
            	if (i >= minSize) {
                    client.getRootStage().setWidth(i);
                    client.getRootStage().loadMenuPane().setPrefWidth(pane);
                } else {
                	Platform.runLater(() -> {
                		rootPane.setRight(menuButton);
                	});
                	
                    this.cancel();
                }
                i--;
                pane--;
            }

        }, 0, 1);
		
        
		
	}
	
	
	
	
}
