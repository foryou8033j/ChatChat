package com.haeorm.chatchat.transmit.sender;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.model.Data;

public class MessageManager {

	private Sender sender = null;
	private  Client client = null;
	
	public MessageManager(Client client, Sender sender) {
		this.sender = sender;
		this.client = client;
	}
	
	private void send(String message){
		sender.sendMessage(message);
	}
	
	//플래그 / 이름 / 해쉬값 / 메세지
	public void sendNomalText(String message){
		send("999" + Data.Key + client.getData().getName() + Data.Key + client.getData().getHashKey() + Data.Key + message);
	}
	
	//이름 중복 검사 확인 플래그
	public void sendNameCheckPlag(){
		send("3" + Data.Key + client.getData().getName());
	}
	
	//버전 일치 검사 플래그
	public void sendVersionCheckPlag(){
		send("2" + Data.Key + client.getVersion());
	}
	
	//패스워드 일치 확인 플래스
	public void sendPasswordCheckPlag(){
		send("1" + Data.Key + client.getLoginStage().getController().getPassword());
	}
	
	
}
