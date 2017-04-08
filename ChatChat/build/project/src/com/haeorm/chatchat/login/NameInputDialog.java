package com.haeorm.chatchat.login;

import com.haeorm.chatchat.Client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class NameInputDialog extends Stage{

	VBox vb = new VBox(10);
	
	
	PasswordField password = new PasswordField();
	Text title = new Text("패스워드를 입력하세요.");
	Text text = new Text("");
	
	private int maxLength = 14;
	private boolean pass = false;
	
	public NameInputDialog(Client client) {
		
		getIcons().add(client.icon);
		
		vb.getChildren().add(title);
		vb.getChildren().add(password);
		vb.getChildren().add(text);
		
		vb.setAlignment(Pos.CENTER);
		
		title.setFont(Font.font(14));
		title.setTextAlignment(TextAlignment.CENTER);
		
		text.setTextAlignment(TextAlignment.CENTER);
		
		setTitle("닉네임 입력");
		initOwner(client.getLoginStage());
		
		setWidth(240);
		setHeight(120);
		setResizable(false);
		
		Scene scene = new Scene(vb);
		setScene(scene);
		
		setOnCloseRequest(Event -> {
			pass = false;
		});
		
		
		
		password.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> obersvable, String oldValue, String newValue) {
				
				if(newValue.length() > maxLength){
					String s = newValue.substring(0, maxLength);
					password.setText(s);
					return;
				}
				
			}
		});
		
		password.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {
				
				if(arg0.getCode() == KeyCode.ENTER){
					
					client.getData().setName(password.getText());
					close();
					
				}
				
			};
		});
		
	}
	
	public boolean isPass(){
		return pass;
	}
	
}
