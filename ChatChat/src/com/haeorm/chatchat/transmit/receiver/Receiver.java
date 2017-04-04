package com.haeorm.chatchat.transmit.receiver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.util.logview.LogView;

public class Receiver extends Thread {
	
	
	BufferedReader in = null;
	
	public Receiver(Client client, Socket socket) {
		
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
					LogView.append(message);
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

	
	
	
}
