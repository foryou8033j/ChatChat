package com.haeorm.chatchat.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserList {

	private StringProperty name = new SimpleStringProperty();
	private StringProperty status = new SimpleStringProperty();
	
	public UserList() {
		this(null, null);
	}
	
	public UserList(String name, String status){
		this.name.set(name);
		this.status.set(status);
	}
	
	
	public void setName(String name){
		this.name.set(name);
	}
	
	public String getName(){
		return name.get();
	}
	
	public StringProperty getNameProperty(){
		return name;
	}
	
	
	public void setStatus(String status){
		this.status.set(status);
	}
	
	public String getStatus(){
		return status.get();
	}
	
	public StringProperty getStatusProperty(){
		return status;
	}
	
	
}
