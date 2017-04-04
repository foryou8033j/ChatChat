package com.haeorm.chatchat.receiver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.haeorm.control.Runner;

public class Receiver extends Thread{
	
	
	Runner runner;
	Socket client;
	
	BufferedReader in;
	BufferedWriter out;
	
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
					runner.log(name + " : "+ message);
					runner.sendMessage(message);
				}
			}
			
		}catch (Exception e)
		{
			e.printStackTrace();
		}finally
		{
			removeUser(name);
		}
		
	}
	
	/**
	 * 유저를 제거한다
	 * @param name
	 */
	private void removeUser(String name)
	{
		runner.clients.remove(name);
		
		//finally 절 실행은 클라이언트가 quit한 것을 의미
		runner.log(name + " 퇴장");
		
		//updateUserDataToClient();
		
		//사용자 퇴장 메세지를 출력한다.
		//if(isNotice) server.sendMessage(MsgManager.make("101", server.getServerApp().getData().getHashKey(), name, name));
	}
	

}
