package com.haeorm.chatchat.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ServerData {

	
	private StringProperty name = new SimpleStringProperty();
	private StringProperty ip = new SimpleStringProperty();
	private IntegerProperty port = new SimpleIntegerProperty();
	
	public ServerData() {
		this(null, null, 0);
	}
	
	public ServerData(String name, String ip, int port){
		this.name.set(name);
		this.ip.set(ip);
		this.port.set(port);
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
	
}
