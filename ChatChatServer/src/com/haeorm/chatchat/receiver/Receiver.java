package com.haeorm.chatchat.receiver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.StringTokenizer;

import com.haeorm.chatchat.Server;
import com.haeorm.chatchat.model.UserList;
import com.haeorm.control.Runner;

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
			
			try {
				in.close();
				client.close();
			} catch (IOException e1) {
				
				//e1.printStackTrace();
			}
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
			for(UserList user:runner.userList){
				
				String userSet = user.getName() + "$$" + user.getStatus();
				nameList = nameList.concat(userSet + "##");
			}
			
			runner.sendMessage("800" + Receiver.Key + hashKey + Receiver.Key + nameList);
			
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
			if(!runner.checkOverlabName(name))
				runner.sendMessage("121" + Receiver.Key + hashKey + Receiver.Key + name + " Name Added User List");
			else
				runner.sendMessage("122" + Receiver.Key + hashKey + Receiver.Key + name + " Name is Overlab in Server user List");
			break;
		
		case 2:	//접속자 닉네임 저장
			String tmpName = token.nextToken();
			runner.userList.add(new UserList(tmpName, "Online"));
			runner.log("[알림] 사용자( " + tmpName +" )채팅방 입장 승인, " + hashKey);
			runner.sendMessage("1000" + Receiver.Key + hashKey + Receiver.Key + tmpName + "님이 입장하였습니다.");
			runner.sendMessage("0" + Receiver.Key + hashKey);
			break;
		
		case 500:	//사용자 닉네임 변경 신청
			String originalName = token.nextToken();
			String changedName = token.nextToken();
			runner.log("[알림] 사용자 닉네임 변경 신청, " + originalName + " -> " + changedName);
			if(!runner.checkOverlabName(changedName)){ //0603
				
				runner.changeName(originalName, changedName);
				runner.sendMessage("1000" + Receiver.Key + hashKey + Receiver.Key + "닉네임 변경 [ " + originalName + " ] → [ " + changedName + " ]" );
				runner.sendMessage("501" + Receiver.Key + hashKey + Receiver.Key + changedName + Receiver.Key + " User Name Change Request Accepted");
				runner.sendMessage("0" + Receiver.Key + hashKey);
			}
			else
				runner.sendMessage("502" + Receiver.Key + hashKey + Receiver.Key + " User Name Change Request Repused");
			
			break;
			
		case 520: //사용자 상태 변경 신청
			runner.changeStatus(token.nextToken(), token.nextToken());
			runner.sendMessage("0" + Receiver.Key + hashKey);
			break;
			
		///관리자 기능 부분///
		case 60:	//관리자 권한 획득 시도
			String adminPassword = token.nextToken();
			if(adminPassword.equals(runner.getServerData().getAdminPassword()))
				runner.sendMessage("61" + Receiver.Key + hashKey + Receiver.Key + "Get admin authority request Accepped");
			else
				runner.sendMessage("62" + Receiver.Key + hashKey + Receiver.Key + "Get admin authority request Repused");
			break;
			
		case 63:	//특정 유저 추방
			runner.sendMessage("63" + Receiver.Key + hashKey + Receiver.Key + token.nextToken() + Receiver.Key + token.nextToken());
			break;
		case 64:
			runner.sendMessage("64" + Receiver.Key + hashKey + Receiver.Key + token.nextToken());
			break;	//전체 유저 추방
		case 65:	//특정 유저 채팅 삭제
			runner.sendMessage("65" + Receiver.Key + hashKey + Receiver.Key + token.nextToken());
			break;
		case 66:	//전체 유저 채팅 삭제
			runner.sendMessage("66" + Receiver.Key + hashKey);
			break;
		case 67:	//공지사항 전파
			runner.sendMessage("67" + Receiver.Key + hashKey + Receiver.Key + token.nextToken());
			break;
			
		
		case 700: //귓속말
			runner.sendMessage(message);
			break;
			
		case 444:	//사용자 채팅방 종료 명령
			String quitName = token.nextToken();
			runner.removeUser(hashKey);
			
			//여기서 스레드로 관리하면 동시 관리 익센셥이 발생한다.
			for(UserList user:runner.userList){
				if(user.getName().equals(quitName)){
					runner.userList.remove(user);
					break;
				}
			}
			runner.sendMessage("1000" + Receiver.Key + hashKey + Receiver.Key + quitName + "님이 퇴장하였습니다.");
			runner.sendMessage("0" + Receiver.Key + hashKey);
			runner.log("[알림] 사용자( " + quitName +" )채팅방 퇴장, " + hashKey);
			break;
			
		case 999:
			runner.sendMessage(message);
			break;
		
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	

}
