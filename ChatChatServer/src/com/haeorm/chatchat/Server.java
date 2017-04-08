package com.haeorm.chatchat;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import com.haeorm.chatchat.model.ServerData;
import com.haeorm.util.LogView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Server {

	public static double version = 1.0;
	
	
	
	ObservableList<ServerData> serverDatas = FXCollections.observableArrayList();
	
	
	public Server() throws UnknownHostException {
		
		LogView.append("### 채팅 서버 동작 시작 ###");
		
		serverDatas.addAll(new ServerData("메인 채널", Inet4Address.getLocalHost().getHostAddress(), 8080 ,"test"),
				new ServerData("테스트 채널", Inet4Address.getLocalHost().getHostAddress(), 8888, "test"));
		
		for(ServerData server:serverDatas)
			server.run();

	}
	
	
	
	public static void main(String[] args) throws UnknownHostException {
		
		new Server();
		
	}
	
	
	
}
