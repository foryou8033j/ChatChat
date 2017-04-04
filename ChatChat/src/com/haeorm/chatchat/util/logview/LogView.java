package com.haeorm.chatchat.util.logview;

public class LogView {

	public static void append(String message){
		System.out.println(message);
	}
	
	public static void append(String message, Exception e){
		System.out.println(message);
	}
	
	public static void append(String message, String exceptionMessage){
		System.out.println(message);
	}
	
}
