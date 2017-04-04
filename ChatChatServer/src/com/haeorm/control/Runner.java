package com.haeorm.control;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.haeorm.chatchat.model.ServerData;
import com.haeorm.chatchat.receiver.Receiver;
import com.haeorm.util.LogView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Runner extends Thread{

	ServerData serverData;
	
	ServerSocket server;
	
	public ObservableList<BufferedWriter> clients;
	public ObservableList<String> names;
	
	public Runner(ServerData serverData) {
		this.serverData = serverData;
		
		clients = FXCollections.observableArrayList();
		names = FXCollections.observableArrayList();
		
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
				
				BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
				clients.add(bout);
				
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
		for(BufferedWriter sender:clients){
			try {
				log(message);
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
