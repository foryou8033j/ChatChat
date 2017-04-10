package com.haeorm.chatchat.transmit.receiver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

import com.haeorm.chatchat.Client;
import com.haeorm.chatchat.model.Data;
import com.haeorm.chatchat.model.UserList;
import com.haeorm.chatchat.root.chatnode.ChatNode;
import com.haeorm.chatchat.root.chatnode.ChatNode.NODE_STYLE;
import com.haeorm.chatchat.root.view.RootLayoutController;
import com.haeorm.chatchat.root.view.RootLayoutController.NOTICE_STYLE;
import com.haeorm.chatchat.util.logview.LogView;
import com.haeorm.chatchat.util.notification.DesktopNotify;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * 서버와의 통신에서 수신 역활을 수행한다.
 * @author Jeognsam
 * @since 2017-04-04
 * @version 1.0
 *
 */
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
		
		String message = "";
		
		try
		{
			//메세지를 지속적으로 수신한다.
			while(true)
			{
				while((message = in.readLine() ) != null)
				{
					LogView.append("[수신] " + message);
					manager(message);
				}
			}
			
		}catch (Exception e)
		{
			e.printStackTrace();
		}finally
		{
			//removeUser(name);
		}
		
	}
	
	/**
	 * 메세지를 수신했을때 plag 에 맞는 클라이언트의 동작을 수행한다.
	 * @param message
	 */
	public void manager(String message){
		StringTokenizer token = new StringTokenizer(message, Data.Key);
		
		int plag = Integer.valueOf(token.nextToken());
		String hashKey = "";
		if(token.hasMoreTokens())
			 hashKey = token.nextToken();
		
		//현재 클라이언트만 해당되어야 하는 명령
		if(client.getData().getHashKey() == Double.valueOf(hashKey)){
			
			switch (plag) {
			case 0:
				client.getManager().sendRequestUserListFromServer();
				break;
			
			case 800:	//사용자 확인 명령 수행
				refreshUserList(token.nextToken());
				break;
			
			case 101: //패스워드 승인
				client.getData().serverPasswordPass = true;
				client.getSender().getManager().sendVersionCheckPlag();
				break;
			case 102: //패스워드 승인 실패
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(client.getLoginStage());
				alert.setTitle("접근 불가");
				alert.setHeaderText("패스워드 불일치");
				alert.setContentText("서버에 접속하는데 필요한 충분한 권한이 획득되지 않았습니다.");
				alert.showAndWait();
				client.getData().serverPasswordPass = false;
				break;
			
			case 111: //버전 승인
				client.getData().versionCheckPass = true;
				break;
			case 112: //버전 승인이나 클라이언트 버전이 더 높음
				client.getData().versionCheckPass = true;
				break;
			case 113: //버전 승인 실패
				client.getData().versionCheckPass = false;
				break;
			
			case 61:	//관리자 권한 획득
				client.getRootStage().setAdmin(true);
				client.getManager().sendChangedStatus("Admin");
				client.getRootStage().showNoticePopup(NOTICE_STYLE.INFORMATION, "관리자 입니다.", 4000);
				client.getRootStage().getMenuLayoutController().getStatusList().add("Admin");
				Platform.runLater(() -> {
					client.getRootStage().getMenuLayoutController().getStatusComboBox().getSelectionModel().select("Admin");
				});
				DesktopNotify.showDesktopMessage("관리자 입니다.", "서버가 관리자 권한을 승인하였습니다.", DesktopNotify.SUCCESS, 4000);
				break;
			case 62:	//관리자 권한 획득 실패
				client.getRootStage().setAdmin(false);
				client.getRootStage().showNoticePopup(NOTICE_STYLE.ERROR, "관리자 권한 획득에 실패하였습니다.", 4000);
				DesktopNotify.showDesktopMessage("권한 획득 실패", "서버가 관리자 권한 획득을 거부하였습니다.", DesktopNotify.ERROR, 4000);
				break;
				
			case 67:	//공지사항 전파
				String noticeMessage = token.nextToken();
				receiveNotice(noticeMessage);
				receiveNoticePopup(noticeMessage);
				DesktopNotify.showDesktopMessage("공지사항", noticeMessage, DesktopNotify.INFORMATION, 4000);
				break;
			
			case 121: //닉네임 승인
				client.getData().nameOverLabPass = true;
				break;
			case 122: //닉네임 승인 실패
				client.getData().nameOverLabPass = false;
				break;
			
			case 501:	//닉네임 변경 승인
				client.getData().setName(token.nextToken());
				client.getRootStage().showNoticePopup(NOTICE_STYLE.INFORMATION, "닉네임 변경에 성공하였습니다.", 4000);
				break;
			case 502:	//닉네임 변경 실패
				client.getRootStage().showNoticePopup(NOTICE_STYLE.ERROR, "닉네임 변경에 실패하였습니다.", 4000);
				break;
				
			case 700:	//귓속말 수신
				receiveWisper(token.nextToken(),token.nextToken(), token.nextToken());
				break;
			
			case 999:	//일반 메세지
				receivedNomalText(token.nextToken(), token.nextToken());
				break;
				
			case 1000:	//일반 공지
				receiveNotice(token.nextToken());
				break;
			case 1001:	//팝업 공지
				receiveNoticePopup(token.nextToken());
				break;
			}
			
		}else{	//전체 클라이언트에 해당되는 명령
			switch(plag){
			case 0:
				client.getManager().sendRequestUserListFromServer();
				break;
				
			case 63:	//특정 사용자 추방
				if(client.getData().getName().equals(token.nextToken())){
					Platform.runLater(() -> {

						Alert alert = new Alert(AlertType.ERROR);
						alert.initOwner(client.getRootStage());
						alert.setTitle("관리자에 의해 추방됨");
						alert.setHeaderText("관리자에 의해 추방되었습니다.");
						alert.setContentText("사유 : " + token.nextToken());
						alert.showAndWait();
						
						client.getRootStage().doShutdownCall();
					});
				}
				break;
			case 64:	//전체 사용자 추방
				Platform.runLater(() -> {

					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(client.getRootStage());
					alert.setTitle("관리자에 의해 추방됨");
					alert.setHeaderText("관리자에 의해 추방되었습니다.");
					alert.setContentText("사유 : " + token.nextToken());
					alert.showAndWait();
					
					client.getRootStage().doShutdownCall();
				});
				break;
			case 65:	//특정 사용자 채팅 삭제
				if(client.getData().getName().equals(token.nextToken())){
					Platform.runLater(() -> {
						client.getRootStage().showNoticePopup(NOTICE_STYLE.INFORMATION, "관리자 명령으로 채팅내용을 지웠습니다.", 5000);
						client.getRootStage().getRootLayoutController().handleClearChatList();
					});
				}
					
				break;
			case 66:	//전체 사용자 채팅 삭제
				Platform.runLater(() -> {
					client.getRootStage().showNoticePopup(NOTICE_STYLE.INFORMATION, "관리자 명령으로 채팅내용을 지웠습니다.", 5000);
					client.getRootStage().getRootLayoutController().handleClearChatList();	
				});
				break;
				
			case 67:	//공지사항 전파
				String noticeMessage = token.nextToken();
				receiveNotice(noticeMessage);
				receiveNoticePopup(noticeMessage);
				DesktopNotify.showDesktopMessage("공지사항", noticeMessage, DesktopNotify.INFORMATION, 4000);
				break;
				
			case 800:	//사용자 확인 명령 수행
				refreshUserList(token.nextToken());
				break;
				
			case 700:	//귓속말 수신
				receiveWisper(token.nextToken(),token.nextToken(), token.nextToken());
				break;
			
			case 999:	//일반 메세지
				receivedNomalText(token.nextToken(), token.nextToken());
				break;
				
			case 1000:	//일반 공지
				receiveNotice(token.nextToken());
				break;
			case 1001:	//팝업 공지
				receiveNoticePopup(token.nextToken());
				break;
			}
		}
	}
	
	private void refreshUserList(String listCode){
		client.getRootStage().getMenuLayoutController().getUserList().clear();
		
		StringTokenizer token = new StringTokenizer(listCode, "##");
		
		while(token.hasMoreTokens()){
			StringTokenizer userSet = new StringTokenizer(token.nextToken(), "$$");
			String name = userSet.nextToken();
			String status = userSet.nextToken();
			client.getRootStage().getMenuLayoutController().getUserList().add(new UserList(name, status));
		}
	}

	//일반메세지 수신
	private void receivedNomalText(String name, String message){
		Platform.runLater(() -> {
			client.getRootStage().appendMessage(name, message);
		});
		
		if(message.equals("@"+client.getData().getName()) && !isMe(name))
			DesktopNotify.showDesktopMessage("툭!", name + "님이 당신을 찾습니다!", DesktopNotify.TIP, 6000);
		else{
			if(client.getRootStage().isAlarm() && !isMe(name))
				DesktopNotify.showDesktopMessage(name, message, DesktopNotify.INFORMATION, 2000);
		}
	}
	
	//귓속말 수신
	private void receiveWisper(String name, String receiver, String message){
		if(name.equals(client.getData().getName()) || receiver.equals(client.getData().getName())){
			Platform.runLater(() -> {
				client.getRootStage().getRootLayoutController().appendWisperMessage(name, message);
			});
		}
		
		if(client.getRootStage().isAlarm() && !isMe(name))
			DesktopNotify.showDesktopMessage(name + "의 귓속말", message, DesktopNotify.INFORMATION, 2500);
		
	}
	
	//대화 공지사항 수신
	private void receiveNotice(String message){
		Platform.runLater(() -> {
			client.getRootStage().getRootLayoutController().getChatList().add(new ChatNode(client, NODE_STYLE.NOTICE, "", message).getChatNode());
			client.getRootStage().recentlySender = "";
		});
		
	}
	
	//알림 공지사항 수신
	private void receiveNoticePopup(String message){
		Platform.runLater(() -> {
			client.getRootStage().showNoticePopup(NOTICE_STYLE.INFORMATION, message, 4000);
			client.getRootStage().recentlySender = "";
		});
	}
	
	private boolean isMe(String name){
		if(name.equals(client.getData().getName()))
			return true;
		else
			return false;
	}
	
	
}
