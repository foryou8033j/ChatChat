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
	
	public void send(String plag, String message){
		sender.sendMessage(plag + Data.Key + client.getData().getHashKey() + Data.Key + message);
	}
	
	public void send(String plag){
		sender.sendMessage(plag + Data.Key + client.getData().getHashKey());
	}
	
	//플래그 / 해쉬값 
	public void sendFirstTicket(){
		send("1");
	}
	
	//플래그 / 이름 / 해쉬값 / 메세지
	public void sendNomalText(String message){
		send("999", client.getData().getName() + Data.Key + message);
	}
	
	//패스워드 일치 확인 플래스
	public void sendPasswordCheckPlag(){
		send("100", client.getLoginStage().getController().getPassword());
	}
	
	//버전 일치 검사 플래그
	public void sendVersionCheckPlag(){
		send("110", String.valueOf(client.getVersion()));
	}
	
	//이름 중복 검사 확인 플래그
	public void sendNameCheckPlag(){
		send("120", client.getLoginStage().getController().getName());
	}
	
	//채팅방 접속 승인에 성공하고 정보를 보내는 플래그
	public void sendInitChatRoomPlag(){
		send("2", client.getData().getName());
	}
	
	//상태 변경 메시지 플래그
	public void sendChangedStatus(String status){
		send("520", client.getData().getName() + Data.Key + status);
	}
	
	//채팅방 접속 종료 플래그
	public void sendQuitPlag(){
		send("4444", client.getData().getName());
	}
	
	//서버에 사용자 리스트 목록을 요청하는 플래그
	public void sendRequestUserListFromServer(){
		send("0");
	}
	
	//사용자 이름 변경 신청 플래그
	public void sendRequestChangeName(String name){
		send("500", client.getData().getName() + Data.Key + name);
	}
	
	
	
	
}
