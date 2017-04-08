package com.haeorm.chatchat;

import com.haeorm.chatchat.model.ServerData;
import com.haeorm.util.LogView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Server {

	public static double version = 1.0;
	
	
	
	ObservableList<ServerData> serverDatas = FXCollections.observableArrayList();
	
	
	public Server() {
		
		LogView.append("### 채팅 서버 동작 시작 ###");
		
		serverDatas.addAll(new ServerData("메인 채널", "10.160.1.49", 10430 ,"test"),
				new ServerData("테스트 채널", "10.160.1.49", 10440, "test"));
		
		for(ServerData server:serverDatas)
			server.run();

	}
	
	
	
	public static void main(String[] args) {
		
		new Server();
		
	}
	
	
	
}
