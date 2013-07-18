package terminalinterface;

import java.io.*;

/*
 * Class to handle the standard error and standard input streams.
 * StreamGobbler empties any stream passes into it in a separate
 * thread.
 */
class StreamGobbler extends Thread
{
	InputStream inputStream;
	String type;

	StreamGobbler(InputStream inputStream, String type)
	{
		this.inputStream = inputStream;
		this.type = type;
	}

	public void run()
	{
		try
		{
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line = null;
			while ((line = bufferedReader.readLine()) != null)
				System.out.println(type + "> " + line);    
		}
		catch (IOException iox)
		{
			iox.printStackTrace();  
		}
	}
}