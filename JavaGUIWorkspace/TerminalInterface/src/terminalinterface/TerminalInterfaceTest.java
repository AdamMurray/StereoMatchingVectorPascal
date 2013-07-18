package terminalinterface;

import java.io.*;

public class TerminalInterfaceTest
{
	public static void main(String args[]) throws IOException
    {
    	String command = "gnome-terminal"; 
    	Runtime runTime = Runtime.getRuntime(); 	
//    	Process process = runTime.exec(command);
    	runTime.exec("/bin/bash -c \"echo 'Hello world!'\"");
    }
	
	/*
	public static void main(String[] args)
	{
		Process process = null;  
		try
		{  
		    process = new ProcessBuilder("Terminal").start();  
		}
		catch (IOException ex)
		{  
		    System.err.println(ex);  
		}  
		
		try
		{
			Runtime.getRuntime().exec("/bin/bash -c \"echo 'Hello world!'\"");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}*/
}
