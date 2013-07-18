package terminalinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GoodLinuxExec
{
	// Get OS name
	private static String OS = System.getProperty("os.name").toLowerCase();

	public static void main(String args[])
	{
		// Prompt user for an input string
		System.out.print("Enter a string: ");
		
		// Open up standard input
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		// Declare input string
		String inputString = null;
		
		// Read string from the command-line
		try
		{
			inputString = bufferedReader.readLine();
		}
		catch (IOException iox)
		{
			System.out.println("IO Error!");
			System.exit(1);
		}
		
		// If no arguments are supplied to the program
//		if (args.length < 1)
//		{
//			System.out.println("USAGE: java GoodLinuxExec <cmd>");
//			System.exit(1);
//		}

		try
		{
			// String array to hold command to be executed
			String[] cmd = new String[3];
			
			if (isWindows())
			{
				System.out.println("This is Windows");
				cmd[0] = "cmd.exe" ;
				cmd[1] = "/C" ;
				cmd[2] = args[0];
			}
			else if (isMac())
			{
				System.out.println("This is Mac");
			}
			else if (isUnix())
			{
				System.out.println("This is Unix or Linux");
				cmd[0] = "/bin/bash" ;
				cmd[1] = "-c" ;
//				cmd[2] = args[0];
				cmd[2] = inputString;
			}
			else if (isSolaris())
			{
				System.out.println("This is Solaris");
			}
			else
			{
				System.out.println("Your OS is not supported.");
			}

			Runtime runtime = Runtime.getRuntime();
			System.out.println("Executing " + cmd[0] + " " + cmd[1] 
					+ " " + cmd[2]);
			Process process = runtime.exec(cmd);
			
			// any error message?
			StreamGobbler errorGobbler = new 
					StreamGobbler(process.getErrorStream(), "ERROR");            

			// any output?
			StreamGobbler outputGobbler = new 
					StreamGobbler(process.getInputStream(), "OUTPUT");

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			// any error???
			int exitVal = process.waitFor();
			System.out.println("ExitValue: " + exitVal);        
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}

	/*
	 * Tests for which OS is being run
	 */
	public static boolean isWindows()
	{
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac()
	{
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isUnix()
	{ 
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
	}

	public static boolean isSolaris()
	{
		return (OS.indexOf("sunos") >= 0);
	}
}
