package com.haeorm.chatchat.util;

import java.util.prefs.Preferences;

import com.haeorm.chatchat.Client;

public class Regedit 
{

	private static Preferences userRootPrefs = Preferences.userNodeForPackage(Client.class);
	
	
	public static boolean isContains(String key)
	{
		return userRootPrefs.get(key, null) != null;
	}
	
	public static void setRegistry(String key, String value)
	{
		try
		{
			if(isContains(key))
				userRootPrefs.put(key, value);
			else
				userRootPrefs.put(key, value);
		}catch (Exception e)
		{
			
		}
	}
	
	public static void setRegistry(String key, double value)
	{
		try
		{
			userRootPrefs.putDouble(key, value);
			
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void setRegistry(String key, int value)
	{
		try
		{
			userRootPrefs.putInt(key, value);
		}catch (Exception e)
		{
			
		}
	}
	
	public static void setRegistry(String key, Boolean value)
	{
		try
		{
			userRootPrefs.putBoolean(key, value);
		}catch (Exception e)
		{
			
		}
	}
	
	public static String getStringValue(String key)
	{
		try
		{
			return userRootPrefs.get(key, "");
		}catch (Exception e)
		{
			return null;
		}
		
	}
	
	public static double getDoubleValue(String key)
	{
		try
		{
			return userRootPrefs.getDouble(key, 0);
		}catch (Exception e)
		{
			return (Double) null;
		}
	}
	
	public static int getIntegerValue(String Key)
	{
		try
		{
			return userRootPrefs.getInt(Key, 0);
		}catch (Exception e)
		{
			return (Integer) null;
		}
	}
	
	public static boolean getBooleanValue(String Key)
	{
		try
		{
			return userRootPrefs.getBoolean(Key, false);
		}catch (Exception e)
		{
			return (Boolean) null;
		}
	}
	
	
}
