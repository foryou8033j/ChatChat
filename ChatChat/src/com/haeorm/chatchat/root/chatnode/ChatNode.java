package com.haeorm.chatchat.root.chatnode;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.root.chatnode.ChatNodeLayoutController.SENDER;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class ChatNode{
	
	private BorderPane borderPane = null;
	public enum NODE_STYLE{NOMAL, NOTICE, FILE, IMAGE, WISPPER};
	
	NODE_STYLE nodeStyle = null;
	String name = null;
	String message = null;
	
	ChatNodeLayoutController controller = null;
	
	public ChatNode(Client client, NODE_STYLE nodeStyle, String name, String message) {
		
		this.nodeStyle = nodeStyle;
		this.name = name;
		this.message = message;
		
		try{
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("./ChatNodeLayout.fxml"));
			borderPane = (BorderPane) loader.load();
			
			//대화 노드의 기본 스타일을 지정한다.
			controller = loader.getController();
			controller.setDefaultData(name, message);
			
			//대화 노드의 발신자를 지정한다.
			if(name.equals(client.getData().getName()))
				controller.setSenderType(SENDER.CLIENT);
			else if(NODE_STYLE.NOTICE.equals(nodeStyle))
				controller.setSenderType(SENDER.NOTICE);
			else
				controller.setSenderType(SENDER.OTHER);
			
			if(client.getRootStage().recentlySender.equals(name))
				controller.isSameSender(true);
			else
				controller.isSameSender(false);
				
			client.getRootStage().recentlySender = name;
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	public BorderPane getChatNode(){
		return borderPane;
	}
	
	

}
