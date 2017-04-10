package com.haeorm.chatchat;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import com.haeorm.chatchat.model.ServerData;
import com.haeorm.util.LogView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * 서버 동작을 위한 중심 클래스
 * @author Jeongsam
 * @version 0.1
 * @since 2017-04-04
 *
 */
public class Server {

	public static double version = 0.1;
	
	
	
	ObservableList<ServerData> serverDatas = FXCollections.observableArrayList();
	
	
	public Server() throws UnknownHostException {
		
		LogView.append("### 채팅 서버 동작 시작 ###");
		
		serverDatas.addAll(new ServerData("메인 채널", Inet4Address.getLocalHost().getHostAddress(), 8080 ,"test", "admin"),
				new ServerData("테스트 채널", Inet4Address.getLocalHost().getHostAddress(), 8888, "test", "admin"));
		
		for(ServerData server:serverDatas)
			server.run();

	}
	
	
	
	public static void main(String[] args) throws UnknownHostException {
		
		new Server();
		
	}
	
	
	
}
