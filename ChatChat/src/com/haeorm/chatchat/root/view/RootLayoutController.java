package com.haeorm.chatchat.root.view;

import java.net.URL;
import java.util.ResourceBundle;

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
	
	@FXML Button menuButton;

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
	
	//대화 리스트를 반환한다.
	public ObservableList<BorderPane> getChatList(){
		return chatList;
	}
	
	
	
	
}
