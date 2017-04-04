package com.haeorm.chatchat.transmit.sender;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Sender {

	BufferedWriter bout = null;
	
	public Sender(Socket socket) {
		
		try{
			bout = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
		}catch (Exception e){
			
		}
		
	}
	
	
	public void sendMessage(String message){
		try {
			System.out.println(message);
			bout.write(message);
			bout.newLine();
			bout.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
