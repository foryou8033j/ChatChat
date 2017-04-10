package com.haeorm.chatchat.model;

import com.haeorm.control.Runner;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 서버 고유 데이터를 저장 해 둔다.
 * @author Jeongsam
 * @since 2017-04-04
 * @version 1.0
 *
 */
public class ServerData {

	
	private StringProperty name = new SimpleStringProperty();
	private StringProperty ip = new SimpleStringProperty();
	private IntegerProperty port = new SimpleIntegerProperty();
	private StringProperty password = new SimpleStringProperty();
	private StringProperty adminPaassword = new SimpleStringProperty();
	
	public ServerData() {
		this(null, null, 0, null, null);
	}
	
	public ServerData(String name, String ip, int port, String password, String adminPassword){
		this.name.set(name);
		this.ip.set(ip);
		this.port.set(port);
		this.password.set(password);
		this.adminPaassword.set(adminPassword);
	}
	
	public void setName(String name){
		this.name.set(name);
	}
	
	public String getName(){
		return name.get();
	}
	
	public void setIP(String ip){
		this.ip.set(ip);
	}
	
	public String getIP(){
		return ip.get();
	}
	
	public void setPort(int port){
		this.port.set(port);
	}
	
	public int getPort(){
		return port.get();
	}
	
	public void setPassword(String password){
		this.password.set(password);
	}
	
	public String getPassword(){
		return password.get();
	}
	
	public void setAdminPassword(String adminPassword){
		this.adminPaassword.set(adminPassword);
	}
	
	public String getAdminPassword(){
		return adminPaassword.get();
	}
	
	/**
	 * 해당 서버를 구동 시킨다.
	 */
	public void run(){
		new Runner(this).start();
	}
	
}
