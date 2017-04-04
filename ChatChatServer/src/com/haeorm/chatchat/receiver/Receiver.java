package com.haeorm.chatchat.receiver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.StringTokenizer;

import com.haeorm.chatchat.Server;
import com.haeorm.control.Runner;

public class Receiver extends Thread{
	
	public static String Key = "%%";
	
	Runner runner;
	Socket client;
	
	BufferedReader in;
	
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
			//e.printStackTrace();
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
		runner.clients.remove(bf);
	}
	
	
	public void manager(String message){
		
		StringTokenizer token = new StringTokenizer(message, Receiver.Key);
		
		int plag = Integer.valueOf(token.nextToken());
		int hashKey = Integer.valueOf(token.nextToken());
		
		switch (plag){
		
		case 999:
			break;
			
		case 100:	//패스워드 일치 확인
			if(token.nextToken().equals(runner.getServerData().getPassword()))
				runner.sendMessage("101" + Receiver.Key + "OK");
			else
				runner.sendMessage("102" + Receiver.Key + "NO");
			break;
			
		case 110:	//서버 버전 일치 확인
			if(token.nextToken().equals(Server.version))
				runner.sendMessage("111" + Receiver.Key + "OK");
			else
				runner.sendMessage("112" + Receiver.Key + "NO");
			break;
			
		case 120:	//닉네임 중복 검사
			runner.sendMessage("3^%OK");
			break;
		
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	

}
