package com.haeorm.chatchat.root.menu.namechange;

import java.awt.AWTException;
import java.awt.Robot;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.util.logview.LogView;
import com.haeorm.chatchat.util.notification.DesktopNotify;

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

public class PasswordInputDialog extends Stage{

	VBox vb = new VBox(10);
	
	
	PasswordField password = new PasswordField();
	Text title = new Text("패스워드를 입력하세요.");
	Text text = new Text("");
	
	private int maxLength = 13;

	public PasswordInputDialog(Client client) {
		
		getIcons().add(client.icon);
		
		vb.getChildren().add(title);
		vb.getChildren().add(password);
		vb.getChildren().add(text);
		
		vb.setAlignment(Pos.CENTER);
		
		title.setFont(Font.font(14));
		title.setTextAlignment(TextAlignment.CENTER);
		
		text.setTextAlignment(TextAlignment.CENTER);
		
		setTitle("관리자 권한 획득");
		initOwner(client.getRootStage());
		
		setWidth(240);
		setHeight(120);
		setResizable(false);
		
		double centerXPosition = client.getRootStage().getX() + client.getRootStage().getWidth()/2d;
        double centerYPosition = client.getRootStage().getY() + client.getRootStage().getHeight()/2d;
        
        setX(centerXPosition - getWidth()/2d);
        setY(centerYPosition - getHeight()/2d);
		
		
		Scene scene = new Scene(vb);
		setScene(scene);
			
		password.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> obersvable, String oldValue, String newValue) {
				
				if(newValue.length() > maxLength){
					String s = newValue.substring(0, maxLength);
					password.setText(s);
					return;
				}
				
				if(newValue.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")){
					text.setFill(Color.RED);
					text.setText("한글이 입력되고 있습니다.");
				}else{
					text.setText("");
				}
				
			}
		});
		
		password.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0) {
				
				if(arg0.getCode() == KeyCode.ENTER){
					
					if(password.getText().equals("")){
						close();
						return;
					}else{
						LogView.append("서버에 관리자 권한 획득 시도");
						new Thread(() -> {
							DesktopNotify.showDesktopMessage("서버에 요청 전송 완료", "서버에 관리자 권한 요청을 시도하였습니다.", DesktopNotify.TIP, 4000);
							try {
								new Robot().delay(2500);
							} catch (AWTException e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
							}
							client.getManager().sendRequestAdmin(password.getText());
						}).start();
						close();
					}
				}
				
			};
		});
		
	}
	
}
