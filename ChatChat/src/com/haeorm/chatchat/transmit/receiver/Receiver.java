package com.haeorm.chatchat.transmit.receiver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.model.Data;
import com.haeorm.chatchat.util.logview.LogView;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Receiver extends Thread {
	
	
	BufferedReader in = null;
	
	Client client;
	
	public Receiver(Client client, Socket socket) {
		
		this.client = client;
		
		try{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		}catch (Exception e){
			
		}
		
	}
	
	@Override
	public void run() {
		super.run();
		
		
		String name = "";
		String message = "";
		
		try
		{


			
			//수신된 메세지를 전체 클라이언트에 전송한다.
			while(true)
			{
				while((message = in.readLine() ) != null)
				{
					//server.sendMessage(name + " : " + message);
					LogView.append("[수신] " + message);
				}
			}
			
		}catch (Exception e)
		{
			//ignore
		}finally
		{
			//removeUser(name);
		}
		
	}
	
	public void manager(String message){
		StringTokenizer token = new StringTokenizer(message, Data.Key);
		
		int plag = Integer.valueOf(token.nextToken());
		
		switch (plag) {
		case 101:
			client.getData().serverPasswordPass = true;
			client.getSender().getManager().sendVersionCheckPlag();
			break;
		case 102:
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(client.getLoginStage());
			alert.setTitle("접근 불가");
			alert.setHeaderText("패스워드 불일치");
			alert.setContentText("서버에 접속하는데 필요한 충분한 권한이 획득되지 않았습니다.");
			alert.showAndWait();
			client.getData().serverPasswordPass = false;
			break;
		case 111:
			client.getData().versionCheckPass = true;
			break;
		case 112:
			client.getData().versionCheckPass = false;
			break;
			
		default:
			break;
		}
	}

	
	
	
}
