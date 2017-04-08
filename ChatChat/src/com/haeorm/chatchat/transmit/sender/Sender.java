package com.haeorm.chatchat.transmit.sender;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.util.logview.LogView;

public class Sender {

	BufferedWriter bout = null;
	
	MessageManager manager = null;
	
	public Sender(Client client, Socket socket) {
		
		try{
			
			manager = new MessageManager(client, this);
			
			bout = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
		}catch (Exception e){
			
		}
		
	}
	
	
	/**
	 * 메세지를 서버로 전송한다.
	 * @param message
	 */
	public void sendMessage(String message){
		try {
			LogView.append("[전송] " + message);
			bout.write(message);
			bout.newLine();
			bout.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 메시지 메니저를 반환한다.
	 * @return
	 */
	public MessageManager getManager(){
		return manager;
	}
	
	
}
