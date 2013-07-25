package stereomatching.gui;

import java.io.*;
import javax.swing.*;

/*
 * Class to handle the standard error and standard input streams.
 * StreamGobbler empties any stream passed into it in a separate
 * thread.
 */
class StreamGobbler extends Thread
{
	private JTextArea output;
	
	private InputStream inputStream;
	private String type;
	private String line;

	public StreamGobbler(
			InputStream inputStream,
			String type,
			JTextArea output)
	{
		this.inputStream = inputStream;
		this.type = type;
		this.output = output;
	}

	public void run()
	{
		try
		{
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			line = null;
			while ((line = bufferedReader.readLine()) != null)
			{
				output.append(type + " " + line + "\n");
			}
		}
		catch (IOException iox)
		{
			iox.printStackTrace();
		}
	}
}