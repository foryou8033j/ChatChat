package com.haeorm.control;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.haeorm.chatchat.model.ServerData;
import com.haeorm.chatchat.model.UserList;
import com.haeorm.chatchat.receiver.Receiver;
import com.haeorm.util.LogView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Runner extends Thread{

	ServerData serverData;
	
	ServerSocket server;
	
	public Map<String, BufferedWriter> clients;	//hashKey와 Client BufferWriter 를 저장한다.
	public ObservableList<UserList> userList = FXCollections.observableArrayList();
	
	
	public Runner(ServerData serverData) {
		this.serverData = serverData;
		
		clients = Collections.synchronizedMap(new HashMap<String, BufferedWriter>());
		
		//Map에서 String을 떼어낼때 사용
		//Iterator<String> iterator = clients.keySet().iterator();
		
		try {
			server = new ServerSocket(serverData.getPort());
			
		} catch (IOException e) 
		{
			log(e.getMessage());
		}
	}
	
	public void log(String message){
		LogView.append("["+serverData.getName()+"] \t" + message);
	}
	
	/**
	 * 사용자 삭제
	 * @param hashKey
	 */
	public void removeUser(String hashKey){
		try{
			clients.get(hashKey).close();
			clients.remove(hashKey);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 닉네임 중복 검사
	 * @param name
	 * @return
	 */
	public boolean checkOverlabName(String name){
		for(UserList user:userList){
			if(user.getName().equals(name))
				return true;
		}
		
		return false;
	}
	
	public void changeName(String name, String changedName){
		for(UserList user:userList){
			if(user.getName().equals(name))
				user.setName(changedName);
		}
	}
	
	public void changeStatus(String name, String status){
		for(UserList user:userList){
			if(user.getName().equals(name))
				user.setStatus(status);
		}
	}
	
	@Override
	public void run() {
		super.run();
		log("동작 시작");
		
		
		while(!currentThread().isInterrupted())
		{
			
			try {
				log("클라이언트 접속 대기중");
				//클라이언트 접속 대기
				Socket client = server.accept();
				
				log(client.getInetAddress() + ":" + client.getPort() + " 접속");
				
				//서버에서 클라이언트로 메세지를 전송할 Thread 생성
				new Receiver(this, client).start();
				
			} catch (IOException e) {
				log("서버 구동 중 오류");
				e.printStackTrace();
				if(e.getMessage().equals("Socket is closed"))
				{
					try {
						log("소켓 미 개방으로 인해 재 연결");
						server = new ServerSocket(serverData.getPort());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			finally{
				
			}
		}
		
	}
	
	public void sendMessage(String message)
	{
		Iterator<String> iterator = clients.keySet().iterator();
		while(iterator.hasNext()){
			try {
				String hashKey = iterator.next();
				BufferedWriter sender = clients.get(hashKey);
				
				log("송신 : " + message);
				sender.write(message);
				sender.newLine();
				sender.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public ServerData getServerData(){
		return serverData;
	}
	
	
	
}
