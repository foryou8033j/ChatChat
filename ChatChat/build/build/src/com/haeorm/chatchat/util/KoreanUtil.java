package com.haeorm.chatchat.util;

/**
 * @author http://gun0912.tistory.com/65
 */
public class KoreanUtil {

	
	public static final String getCompleteWordByJongSung(String name, String firstValue, String secondValue)
	{
		
		char lastName = name.charAt(name.length() - 1);
		
		if(lastName < 0xAC00 || lastName > 0xD7A3)
		{
			return "[" + name + "] 가 ";
		}
		
		String selectedValue = (lastName - 0xAC00) % 28 > 0 ? firstValue : secondValue;
		
		return "[" +name +"] " +  selectedValue;
		
	}
	
}
