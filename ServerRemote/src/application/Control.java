package application;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Control
{
	
	public static void CommandControl(String cmm) throws AWTException, IOException
	{
		System.out.println(cmm);
		String[] cmmarr = cmm.split("_");
		
		Runtime runtime = Runtime.getRuntime();
		switch (cmmarr[0])
		{
		////Disconnect
		case "Over":
			break;
		////Power Control
	    case "pwcontrol":
	    	switch (cmmarr[1])
	    	{
	    	//ShutDown
			case "sd":
		        try
		        {
		        	String s = "shutdown -s -t "+cmmarr[2];
					runtime.exec(s);
				}
		        catch (IOException e)
		        {
					e.printStackTrace();
				}
		        break;
			//Restart
			case "rs":
		        try
		        {
		        	String s = "shutdown -r -t "+cmmarr[2];
					runtime.exec(s);
				}
		        catch (IOException e)
		        {
					e.printStackTrace();
				}
		        break;
		    //LogOut
			case "lg":
		        try
		        {
		        	String s = "shutdown /l";
					runtime.exec(s);
				}
		        catch (IOException e)
		        {
					e.printStackTrace();
				}
		        break;		        
		    //Sleep
			case "sl":
		        try
		        {
		        	String s = "Rundll32.exe powrprof.dll,SetSuspendState Sleep";
					runtime.exec(s);
				}
		        catch (IOException e)
		        {
					e.printStackTrace();
				}
		        break;		    
			default:
				throw new IllegalArgumentException("Unexpected value: " + cmmarr[1]);
			}
	        break;
	        
	    ////Screen_Shot
	    case "scrshot":
	        File oldfile = new File("C:\\Users\\Public\\Pictures\\scr.jpg"); 
	        oldfile.delete();
	        
	    	Rectangle screenRectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
	        BufferedImage image = new Robot().createScreenCapture(screenRectangle);
	        ImageIO.write(image, "jpg", new FileOutputStream("C:\\Users\\Public\\Pictures\\scr.jpg"));
            System.out.println("Screenshot saved");
	    	break;
	    
	    ////KeyLogger
	    case "keylog":
	    	break;
	    ////Manager:process
	    case "process-get":
	    	break;
	    case "processc":
	    	switch (cmmarr[1]) {
			case "stop":
				String pc0_temp = "taskkill /F /PID " + cmmarr[2];
				System.out.println(pc0_temp);
				runtime.exec(pc0_temp);
				break;
			case "start":
				String pc1_temp = cmmarr[2] + ".exe";
				runtime.exec(pc1_temp);
				break;
			default:
				break;
			}
	    	break;
	    ////Manager: Application
	    case "Application-get":
	    	break;
		}

	}
	
}