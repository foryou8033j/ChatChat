package com.haeorm.chatchat.login;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.login.view.LoginLayoutController;
import com.haeorm.chatchat.model.RegistyNameData;
import com.haeorm.chatchat.util.Regedit;
import com.haeorm.chatchat.util.logview.LogView;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * 로그인 Stage를 관리한다.
 * @author Jeongsam
 * @since 2017-04-04
 * @version 1.0
 *
 */
public class LoginStage extends Stage{

	private LoginLayoutController controller;
	
	public LoginStage(Client client) {
		
		super();
		
		setTitle(client.getTitle());
		setResizable(false);
		getIcons().add(client.icon);
		
		setOnCloseRequest(event -> {
			doShutdownCall();
		});
		
		try{
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("./view/LoginLayout.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();
			
			controller = loader.getController();
			controller.setClient(client);
			
			Scene scene = new Scene(pane);
			setScene(scene);
			
		}catch (Exception e){
			LogView.append("[오류] LoginStage 초기화 도중 오류가 발생하였습니다.", e);
			System.exit(0);
		}
		
		loadDefaultPos();
		show();
	}
	
	/**
	 * 레지스트리 상에 등록된 기본 위치에 프레임을 위치시킨다.
	 */
	private void loadDefaultPos(){
		
		try{
			double posX = Regedit.getDoubleValue(RegistyNameData.LOGIN_VIEW_X);
			double posY = Regedit.getDoubleValue(RegistyNameData.LOGIN_VIEW_Y);
			
			if(posX==0 && posY==0)
				throw new Exception("해당 레지스트리 값이 존재하지 않음," + RegistyNameData.LOGIN_VIEW_X + ", " + RegistyNameData.LOGIN_VIEW_Y);
			
			LogView.append("[알림] 레지스트리의 값에 따라 LoginStage의 기본 위치를 " + posX + ", " + posY + " 로 조정");
			
			setX(posY);
			setY(posY);
			
		}catch (Exception e){
			try{
				Regedit.setRegistry(RegistyNameData.LOGIN_VIEW_X, getX());
				Regedit.setRegistry(RegistyNameData.LOGIN_VIEW_Y, getY());
				
				LogView.append("[알림] 설정 된 LoginStage의 기본 위치 값이 없어 등록 완료");
				
			}catch (Exception ie){
				
				LogView.append("[알림] LoginStage 의 기본 위치 등록 실패", ie);
			}
			
		}finally {
			addPosListener();
		}
		
		
	}
	
	/**
	 * 프레임의 위치 변화에 따라 레지스트리 상 값을 수정하도록 리스너를 등록시킨다.
	 */
	private void addPosListener(){
		
		xProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Regedit.setRegistry(RegistyNameData.LOGIN_VIEW_X, newValue.doubleValue());
			}
		});
		
		yProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Regedit.setRegistry(RegistyNameData.LOGIN_VIEW_Y, newValue.doubleValue());
			}
		});
		
	}
	
	/**
	 * LoginStage 가 종료 될 때 실행되는 Call Method 이다.
	 */
	private void doShutdownCall(){
		LogView.append("[알림] LoginStage Shutdown Call 이 실행되었습니다. (" + getX() + ", " + getY() + ")");
		Regedit.setRegistry(RegistyNameData.LOGIN_VIEW_X, getX());
		Regedit.setRegistry(RegistyNameData.LOGIN_VIEW_Y, getY());
	}
	
	public LoginLayoutController getController(){
		return controller;
	}
	
}
