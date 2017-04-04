package com.haeorm.chatchat.model;

import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import com.haeorm.chatchat.util.logview.LogView;

/**
 * 클라이언트의 기본 데이터를 구성하는 클래스
 * @author Jeongsam
 * @version 1.0
 * @since 2017-04-04
 *
 */
public class Data {

	private String serverIP = "";
	private int serverPort = 8888;
	private String localIP = "";
	
	private int hashKey;
	private String name = "";
	
	public static String Key = "^%";
	
	public boolean nameOverLabPass = false;
	public boolean versionCheckPass = false;
	public boolean serverPasswordPass = false;
	
	public Data() {
		try {
			localIP = Inet4Address.getLocalHost().getHostAddress();	//클라이언트의 로컬 IP를 가져온다.
			hashKey = new Random().nextInt(1000000);	//클라이언트 고유 HashKey를 생성한다.
			
		} catch (UnknownHostException e) {
			LogView.append("로컬 IP를 가져오는 도중 문제가 발생하였습니다.", e);
		}
	}
	
	/**
	 * Server IP 값을 설정한다.
	 * @param ip
	 */
	public void setServerIP(String ip){
		this.serverIP = ip;
	}
	
	/**
	 * 서버 IP 값을 반환받는다.
	 * @return
	 */
	public String getServerIP(){
		return serverIP;
	}
	
	/**
	 * 서버 Port 값을 설정한다.
	 * @param port
	 */
	public void setServerPort(int port){
		this.serverPort = port;
	}
	
	/**
	 * 서버 Port 값을 반환받는다.
	 * @return
	 */
	public int getServerPort(){
		return serverPort;
	}
	
	/**
	 * 현재 클라이언트의 IP를 반환받는다.
	 * @return 클라이언트 IP
	 */
	public String getLocalIP(){
		return localIP;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public int getHashKey(){
		return hashKey;
	}
}
