package com.haeorm.chatchat.root.chatnode;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.root.chatnode.ChatNodeLayoutController.SENDER;
import com.haeorm.chatchat.util.ExceptionDialog;
import com.haeorm.chatchat.util.logview.LogView;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.ContextMenuEvent;
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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatNodeLayout.fxml"));
			borderPane = (BorderPane) loader.load();

			//대화 노드의 기본 스타일을 지정한다.
			controller = loader.getController();
			controller.setDefaultData(name, message);

			//대화 노드의 발신자를 지정한다.
			if(name.equals(client.getData().getName())){
				controller.setSenderType(SENDER.CLIENT);
				if(NODE_STYLE.WISPPER.equals(nodeStyle))
					controller.setWisper();
			}

			else if(NODE_STYLE.NOTICE.equals(nodeStyle))
				controller.setSenderType(SENDER.NOTICE);
			else{
				controller.setSenderType(SENDER.OTHER);
				if(NODE_STYLE.WISPPER.equals(nodeStyle))
					controller.setWisper();
			}

			//동일한 발신자 일때 발신자 이름을 숨기기 위한 옵션
			if(!client.getRootStage().recentlySender.equals("") && client.getRootStage().recentlySender.equals(name))
				controller.isSameSender(true);
			else
				controller.isSameSender(false);

			client.getRootStage().recentlySender = name;

		}catch (Exception e){
			e.printStackTrace();
			new ExceptionDialog(AlertType.ERROR, "오류", "대화 노드 입력 도중 오류가 발생하였습니다.", "", e).showAndWait();
			LogView.append("[오류] 대화 노드 입력 도중 오류가 발생하였습니다.", e);
			client.getRootStage().doShutdownCall();
		}
	}



	public BorderPane getChatNode(){

		borderPane.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			public void handle(ContextMenuEvent event) {
				initContextMenu().show(borderPane, event.getScreenX(), event.getScreenY());
			};
		});

		return borderPane;
	}

	private ContextMenu initContextMenu(){

		ContextMenu contextMenu = new ContextMenu();

		MenuItem copy = new MenuItem("복사");

		copy.setOnAction(event -> {
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Clipboard clipboard = toolkit.getSystemClipboard();
			StringSelection strSel = new StringSelection("["+name+"] " + message);
			clipboard.setContents(strSel, null);
		});

		contextMenu.getItems().add(copy);

		return contextMenu;
	}



}
