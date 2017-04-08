package com.haeorm.chatchat.receiver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.StringTokenizer;

import com.haeorm.chatchat.Server;
import com.haeorm.control.Runner;
import com.haeorm.util.LogView;

public class Receiver extends Thread{
	
	public static String Key = "//";
	
	Runner runner;
	Socket client;
	
	BufferedReader in;
	String hashKey = null;
	
	public Receiver(Runner runner, Socket client) {

		this.runner = runner;
		this.client = client;
		
		try
		{
			//클라이언트 소켓에서 데이터를 수신 받기 위한 InputStream 생성
			in = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
			
		}catch (Exception e)
		{
			runner.log("in/out 스트림 생성 중 오류 발생");
		}
		
	}
	
	@Override
	public void run() {
		super.run();
		
		String message = "";
		
		try
		{
			
			//수신된 메세지를 전체 클라이언트에 전송한다.
			while(true)
			{
				while((message = in.readLine() ) != null)
				{
					//server.sendMessage(name + " : " + message);
					runner.log("수신 : " + message);
					manager(message);
				}
			}
			
		}catch (Exception e)
		{
			removeUser(hashKey);
		}finally
		{
			
		}
		
	}
	
	/**
	 * 유저를 제거한다
	 * @param name
	 */
	private void removeUser(BufferedWriter bf)
	{
		runner.clients.remove(hashKey, bf);
	}
	
	private void removeUser(String hashKey){
		runner.clients.remove(hashKey);
	}
	
	
	public void manager(String message){
		
		StringTokenizer token = new StringTokenizer(message, Receiver.Key);
		
		int plag = Integer.valueOf(token.nextToken());
		String hashKey = token.nextToken();
		
		switch (plag){
		
		//현재 사용자 리스트 반환
		case 0:
			String nameList = "";
			for(String name:runner.userList){
				nameList = nameList.concat(name + Receiver.Key);
			}
			
			runner.sendMessage("0" + Receiver.Key + hashKey + Receiver.Key + nameList);
			
			break;
		
		//사용자의 최초입장을 확인하고 해당 BufferedWriter를 Cliens Maps 에 입력한다.
		case 1:
			this.hashKey = hashKey;
			try{
				BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
				if(runner.clients.containsKey(hashKey)){
					runner.clients.replace(hashKey, bout);
					runner.log("[알림] 이미 등록된 HashKey, BufferdWriter 교체, " + hashKey);
				}
				else{
					runner.clients.put(hashKey, bout);
					runner.log("[알림] 사용자 추가, " + hashKey);
				}
			}catch (Exception e){
				runner.log("[오류] 사용자 추가 중 오류 발생, " + hashKey); 
			}
			
			break;
			
		case 100:	//패스워드 일치 확인
			String password = token.nextToken();
			if(password.equals(runner.getServerData().getPassword()))
				runner.sendMessage("101" + Receiver.Key + hashKey + Receiver.Key + "Server Password Check OK");
			else
				runner.sendMessage("102" + Receiver.Key + hashKey + Receiver.Key + "Server Password Check Fail");
			break;
			
		case 110:	//서버 버전 일치 확인
			double version = Double.valueOf(token.nextToken());
			if(version == Server.version)
				runner.sendMessage("111" + Receiver.Key + hashKey + Receiver.Key + "Server version Check OK");
			else if(version > Server.version)
				runner.sendMessage("112" + Receiver.Key + hashKey + Receiver.Key + "Client Version is Higher then Server version");
			else
				runner.sendMessage("113" + Receiver.Key + hashKey + Receiver.Key + "Server version Check Fail");
			break;
			
		case 120:	//닉네임 중복 검사
			String name = token.nextToken();
			if(!runner.userList.contains(name))
				runner.sendMessage("121" + Receiver.Key + hashKey + Receiver.Key + name + " Name Added User List");
			else
				runner.sendMessage("122" + Receiver.Key + hashKey + Receiver.Key + name + " Name is Overlab in Server user List");
			break;
		
		case 2:	//접속자 닉네임 저장
			String tmpName = token.nextToken();
			runner.userList.add(tmpName);
			runner.log("[알림] 사용자( " + tmpName +" )채팅방 입장 승인, " + hashKey);
			runner.sendMessage("1000" + Receiver.Key + hashKey + Receiver.Key + tmpName + "님이 입장하였습니다.");
			break;
		
		case 4444:	//사용자 채팅방 종료 명령
			String quitName = token.nextToken();
			runner.removeUser(hashKey);
			runner.userList.remove(quitName);
			runner.log("[알림] 사용자( " + quitName +" )채팅방 퇴장, " + hashKey);
			runner.sendMessage("1000" + Receiver.Key + hashKey + Receiver.Key + quitName + "님이 퇴장하였습니다.");
			break;
			
		case 999:
			runner.sendMessage(message);
			break;
		
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	

}
