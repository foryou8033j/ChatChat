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
import com.haeorm.chatchat.receiver.Receiver;
import com.haeorm.util.LogView;

public class Runner extends Thread{

	ServerData serverData;
	
	ServerSocket server;
	
	public Map<String, BufferedWriter> clients;
	
	public Runner(ServerData serverData) {
		this.serverData = serverData;
		
		clients = Collections.synchronizedMap(new HashMap<String, BufferedWriter>());
		
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
		//접속한 유저의 대화명 리스트 추출
		Iterator<String> iterator = clients.keySet().iterator();
		
		//TODO 메세지 전송 안되는 문제 있음.
		while(iterator.hasNext())
		{
			String name = iterator.next();
			try
			{
				
				BufferedWriter out = clients.get(name);
				out.write(message + "\n");
				out.flush();
			}
			catch (Exception e)
			{
				//메세지를 송신하였으나 소켓이 닫혀있다 -> 클라이언트 연결 끊어짐으로 간주
				clients.remove(name);
				
				//finally 절 실행은 클라이언트가 quit한 것을 의미
				log(name + " 퇴장");
				
				String sendMessage = "000";
				
				//사용자 이름 iterator 를 분리한다.
				Iterator<String> iterators = clients.keySet().iterator();
				
				while(iterators.hasNext())
				{
					sendMessage = sendMessage.concat("|");
					sendMessage = sendMessage.concat(iterators.next());
				}
				
				//개정된 사용자 정보를 서버에서 송신한다.
				sendMessage(sendMessage);
				
				//사용자 퇴장 메세지를 출력한다.
				//sendMessage(MsgManager.make("101", getServerApp().getData().getHashKey(), name, name));
				log("메세지 전송 중 오류 발생 : " + e.getMessage());
			}
		}
		
	}
	
	
	
}
