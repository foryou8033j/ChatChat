package com.haeorm.chatchat.transmit.receiver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.util.logview.LogView;

public class Receiver extends Thread {
	
	
	BufferedReader in = null;
	
	Client client;
	
	public Receiver(Client client, Socket socket) {
		
		this.client = client;
		
		try{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		}catch (Exception e){
			
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
					LogView.append("[수신] " + message);
				}
			}
			
		}catch (Exception e)
		{
			//ignore
		}finally
		{
			//removeUser(name);
		}
		
	}
	
	public void manager(String message){
		StringTokenizer token = new StringTokenizer(message, "^%");
		
		String plag = token.nextToken();
		
		switch (plag) {
		case "1":
			if(token.nextToken().equals("OK"))
				client.getData().serverPasswordPass = true;
			else 
				client.getData().serverPasswordPass = true;
			break;
		case "2":
			if(token.nextToken().equals("OK"))
				client.getData().versionCheckPass = true;
			else 
				client.getData().versionCheckPass = false;
			
		default:
			break;
		}
	}

	
	
	
}
