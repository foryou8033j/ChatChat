package com.haeorm.chatchat.login;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.login.view.LoginLayoutController;
import com.haeorm.chatchat.util.logview.LogView;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginStage extends Stage{

	public LoginStage(Client client) {
		
		setTitle("ChatChat");
		setResizable(false);
		getIcons().add(client.icon);
		
		try{
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("./view/LoginLayoutController.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();
			
			LoginLayoutController controller = loader.getController();
			controller.setClient(client);
			
			Scene scene = new Scene(pane);
			setScene(scene);
			
		}catch (Exception e){
			LogView.append("[오류] LoginStage 초기화 도중 오류가 발생하였습니다.");
			System.exit(0);
		}
		
	}
	
}
