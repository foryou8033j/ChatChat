package com.haeorm.chatchat.root.chatnode;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ChatNodeLayoutController implements Initializable{

	enum SENDER{CLIENT, OTHER, NOTICE};
	
	String name = null;
	String message = null;
	
	@FXML BorderPane borderPane;
	@FXML FlowPane top;
	@FXML FlowPane center;
	@FXML FlowPane bottom;
	
	@FXML Text sender;
	@FXML Text contents;
	@FXML Text time;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	public ChatNodeLayoutController() {
		// TODO Auto-generated constructor stub
	}
	
	public void setDefaultData(String name, String message){
		
		this.name = name;
		this.message = message;
		
		//대화 생성 시간
		SimpleDateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss] ");
		time.setText(dateFormat.format(System.currentTimeMillis()));
		
		sender.setText(name);
		contents.setText(message);
	}
	
	public void setSenderType(SENDER senderType){
		if(SENDER.CLIENT.equals(senderType)){
			top.setAlignment(Pos.CENTER_RIGHT);
			center.setAlignment(Pos.CENTER_RIGHT);
			bottom.setAlignment(Pos.CENTER_RIGHT);
			contents.setTextAlignment(TextAlignment.RIGHT);
		}
		else if (SENDER.OTHER.equals(senderType)){
			top.setAlignment(Pos.CENTER_LEFT);
			center.setAlignment(Pos.CENTER_LEFT);
			bottom.setAlignment(Pos.CENTER_LEFT);
			contents.setTextAlignment(TextAlignment.LEFT);
			
			center.getChildren().clear();
			center.getChildren().addAll(contents, time);
			
		}else{
			borderPane.setCenter(null);
			borderPane.setBottom(null);
			
			top.setAlignment(Pos.CENTER);
			sender.setFill(Color.BLUE);
			sender.setTextAlignment(TextAlignment.CENTER);
			
			sender.setFont(Font.font("Malgun Gothic", FontWeight.EXTRA_BOLD, 13));
			sender.setText(message);
			
		}
	}
	
	public void isSameSender(boolean senderSame){
		if(senderSame){
			borderPane.setTop(null);
		}
	}
	
	/**
	 * 귓속말 메세지로 지정한다.
	 */
	public void setWisper(){
		contents.setFill(Color.GREEN);
		contents.setFont(Font.font("Malgun Gothic", FontWeight.BOLD, 12));
	}
	
	
	
	
}
